package cn.itedus.lottery.domain.activity.service.partake.impl;

import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import cn.itedus.lottery.common.Constants;
import cn.itedus.lottery.common.Result;
import cn.itedus.lottery.domain.activity.model.req.PartakeReq;
import cn.itedus.lottery.domain.activity.model.vo.ActivityBillVO;
import cn.itedus.lottery.domain.activity.model.vo.DrawOrderVO;
import cn.itedus.lottery.domain.activity.model.vo.UserTakeActivityVO;
import cn.itedus.lottery.domain.activity.repository.IUserTakeActivityRepository;
import cn.itedus.lottery.domain.activity.service.partake.BaseActivityPartake;
import cn.itedus.lottery.domain.support.ids.IIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 活动参与功能实现类 继承自BaseActivityPartake，实现了活动参与的具体逻辑
 */
@Service
public class ActivityPartakeImpl extends BaseActivityPartake {

    private final Logger logger = LoggerFactory.getLogger(ActivityPartakeImpl.class);

    @Resource
    private IUserTakeActivityRepository userTakeActivityRepository;

    @Resource
    private Map<Constants.Ids, IIdGenerator> idGeneratorMap;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private IDBRouterStrategy dbRouter;

    /**
     * 校验活动账单
     *
     * @param partake 参与活动请求
     * @param bill    活动账单
     * @return 校验结果
     */
    @Override
    protected Result checkActivityBill(PartakeReq partake, ActivityBillVO bill) {
        // 1. 校验活动状态是否可用
        if (!Constants.ActivityState.DOING.getCode().equals(bill.getState())) {
            logger.warn("活动当前状态非可用 state：{}", bill.getState());
            return Result.buildResult(Constants.ResponseCode.UN_ERROR, "活动当前状态非可用");
        }

        // 2. 校验活动日期是否在有效范围内
        if (bill.getBeginDateTime().after(partake.getPartakeDate()) || bill.getEndDateTime().before(partake.getPartakeDate())) {
            logger.warn("活动时间范围非可用 beginDateTime：{} endDateTime：{}", bill.getBeginDateTime(), bill.getEndDateTime());
            return Result.buildResult(Constants.ResponseCode.UN_ERROR, "活动时间范围非可用");
        }

        // 3. 校验活动库存
        if (bill.getStockSurplusCount() <= 0) {
            logger.warn("活动剩余库存不足 stockSurplusCount: {}", bill.getStockSurplusCount());
            return Result.buildResult(Constants.ResponseCode.UN_ERROR, "活动剩余库存不足");
        }

        // 4. 校验个人活动可领取次数
        if (bill.getUserTakeLeftCount() <= 0) {
            logger.warn("个人领取次数不足 userTakeLeftCount: {}", bill.getUserTakeLeftCount());
            return Result.buildResult(Constants.ResponseCode.UN_ERROR, "个人领取次数不足");
        }
        return Result.buildSuccessResult();
    }

    /**
     * 扣减活动库存
     *
     * @param req 参与活动请求
     * @return 扣减结果
     */
    @Override
    protected Result subtractionActivityStock(PartakeReq req) {
        int count = activityRepository.subtractionActivityStock(req.getActivityId());
        if (0 == count) {
            logger.error("扣减活动库存失败 activityId：{}", req.getActivityId());
            return Result.buildResult(Constants.ResponseCode.NO_UPDATE);
        }
        return Result.buildSuccessResult();
    }


    /**
     * 查询是否存在未执行抽奖领取活动单
     *
     * @param activityId 活动ID
     * @param uId        用户ID
     * @return 领取单
     */
    @Override
    protected UserTakeActivityVO queryNoConsumedTakeActivityOrder(Long activityId, String uId) {
        return userTakeActivityRepository.queryNoConsumedTakeActivityOrder(activityId, uId);
    }

    /**
     * 领取活动：编程式事务
     * <p>
     * 扣减用户参与次数 写入领取活动记录
     *
     * @param partake 参与活动请求
     * @param bill    活动账单
     * @return 领取结果
     */
    @Override
    protected Result grabActivity(PartakeReq partake, ActivityBillVO bill, Long takeId) {
        try {
            dbRouter.doRouter(partake.getUId());
            return transactionTemplate.execute(status -> {
                try {
                    // 扣减个人已参与次数
                    int updateCount = userTakeActivityRepository.subtractionLeftCount(bill.getActivityId(),
                            bill.getActivityName(), bill.getTakeCount(), bill.getUserTakeLeftCount(),
                            partake.getUId(), partake.getPartakeDate());
                    if (0 == updateCount) {
                        status.setRollbackOnly();
                        logger.error("领取活动，扣减个人已参与次数失败 activityId：{} uId：{}", partake.getActivityId(),
                                partake.getUId());
                        return Result.buildResult(Constants.ResponseCode.NO_UPDATE);
                    }

                    // 写入领取活动记录
                    userTakeActivityRepository.takeActivity(bill.getActivityId(), bill.getActivityName(),
                            bill.getStrategyId(), bill.getTakeCount(), bill.getUserTakeLeftCount(), partake.getUId(),
                            partake.getPartakeDate(), takeId);
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    logger.error("领取活动，唯一索引冲突 activityId：{} uId：{}", partake.getActivityId(), partake.getUId(), e);
                    return Result.buildResult(Constants.ResponseCode.INDEX_DUP);
                }
                return Result.buildSuccessResult();
            });
        } finally {
            dbRouter.clear();
        }
    }

    /**
     * 保存奖品单
     * <p>
     * 锁定已领取的活动记录（标记为已消费状态） 保存用户实际的中奖信息
     *
     * @param drawOrder 奖品单
     * @return 保存结果
     */
    @Override
    public Result recordDrawOrder(DrawOrderVO drawOrder) {
        try {
            dbRouter.doRouter(drawOrder.getUId());
            return transactionTemplate.execute(status -> {
                try {
                    //锁定活动领取记录
                   int lockCount = userTakeActivityRepository.lockTakeActivity(drawOrder.getUId(), drawOrder.getActivityId(), drawOrder.getTakeId());
                   if (0 == lockCount){
                       status.setRollbackOnly();
                       return Result.buildResult(Constants.ResponseCode.NO_UPDATE);
                   }
                   //保存抽奖信息
                    userTakeActivityRepository.saveUserStrategyExport(drawOrder);
                } catch (DuplicateKeyException e) {
                    logger.error("记录中奖单，唯一索引冲突 activityId：{} uId：{}", drawOrder.getActivityId(), drawOrder.getUId(), e);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return Result.buildSuccessResult();
            });
        } finally {
            dbRouter.clear();
        }

    }

    /**
     * 更新发货单MQ状态
     *
     * @param uId     用户ID
     * @param orderId 订单ID
     * @param mqState MQ 发送状态
     */
    @Override
    public void updateInvoiceMqState(String uId, Long orderId, Integer mqState) {
        userTakeActivityRepository.updateInvoiceMqState(uId, orderId, mqState);

    }
}

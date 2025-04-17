package cn.itedus.lottery.domain.activity.service.partake;

import cn.itedus.lottery.common.Constants;
import cn.itedus.lottery.common.Result;
import cn.itedus.lottery.domain.activity.model.req.PartakeReq;
import cn.itedus.lottery.domain.activity.model.res.PartakeResult;
import cn.itedus.lottery.domain.activity.model.vo.ActivityBillVO;
import cn.itedus.lottery.domain.activity.model.vo.UserTakeActivityVO;
import cn.itedus.lottery.domain.support.ids.IIdGenerator;

import javax.annotation.Resource;
import java.util.Map;

/** 活动领取抽象类：模版模式 */
public abstract class BaseActivityPartake extends ActivityPartakeSupport implements IActivityPartake {

    @Resource
    private Map<Constants.Ids, IIdGenerator> idGeneratorMap;

    /**
     * 参与活动
     *
     * @param req 参与活动请求
     * @return PartakeResult 参与结果
     */
    @Override
    public PartakeResult doPartake(PartakeReq req) {

        // 1. 查询是否存在未执行抽奖领取活动单【 state = 0，可以直接返回领取结果继续抽奖】
        UserTakeActivityVO userTakeActivityVO = this.queryNoConsumedTakeActivityOrder(req.getActivityId(),
                req.getUId());
        if (null != userTakeActivityVO) {
            return buildPartakeResult(userTakeActivityVO.getStrategyId(), userTakeActivityVO.getTakeId());
        }

        // 查询活动账单
        ActivityBillVO activityBillVO = super.queryActivityBill(req);

        Result result;
        // 2. 活动信息校验
        result = this.checkActivityBill(req, activityBillVO);
        if (!isSuccess(result)) return new PartakeResult(result.getCode(), result.getInfo());

        // 3. 扣减活动库存
        result = this.subtractionActivityStock(req);
        if (!isSuccess(result)) return new PartakeResult(result.getCode(), result.getInfo());
        // 4. 插入领取活动信息【个人用户把活动信息写入到用户表】
        Long takeId = idGeneratorMap.get(Constants.Ids.SnowFlake).nextId();
        result = this.grabActivity(req, activityBillVO, takeId);
        if (!isSuccess(result)) return new PartakeResult(result.getCode(), result.getInfo());

        return buildPartakeResult(activityBillVO.getStrategyId(), takeId);
    }

    /**
     * 判断结果是否成功
     */
    private boolean isSuccess(Result result) {
        return Constants.ResponseCode.SUCCESS.getCode().equals(result.getCode());
    }

    /**
     * 封装结果【返回的策略ID，用于继续完成抽奖步骤】
     *
     * @param strategyId 策略ID
     * @param takeId     领取ID
     * @return 封装结果
     */
    private PartakeResult buildPartakeResult(Long strategyId, Long takeId) {
        PartakeResult partakeResult = new PartakeResult(Constants.ResponseCode.SUCCESS.getCode(),
                Constants.ResponseCode.SUCCESS.getInfo());
        partakeResult.setStrategyId(strategyId);
        partakeResult.setTakeId(takeId);
        return partakeResult;
    }


    /**
     * 活动信息校验处理
     *
     * @param partake 参与活动请求
     * @param bill    活动账单
     * @return 校验结果
     */
    protected abstract Result checkActivityBill(PartakeReq partake, ActivityBillVO bill);

    /**
     * 扣减活动库存
     *
     * @param req 参与活动请求
     * @return 扣减结果
     */
    protected abstract Result subtractionActivityStock(PartakeReq req);

    /**
     * 领取活动
     *
     * @param partake 参与活动请求
     * @param bill    活动账单
     * @param takeId  领取活动ID
     * @return 领取结果
     */
    protected abstract Result grabActivity(PartakeReq partake, ActivityBillVO bill, Long takeId);

    /**
     * 查询是否存在未执行抽奖领取活动单【user_take_activity 存在 state = 0，领取了但抽奖过程失败的，可以直接返回领取结果继续抽奖】
     *
     * @param activityId 活动ID
     * @param uId        用户ID
     * @return 领取单
     */
    protected abstract UserTakeActivityVO queryNoConsumedTakeActivityOrder(Long activityId, String uId);

}

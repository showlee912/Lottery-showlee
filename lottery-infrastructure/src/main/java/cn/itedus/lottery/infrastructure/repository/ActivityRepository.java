package cn.itedus.lottery.infrastructure.repository;

import cn.itedus.lottery.common.Constants;
import cn.itedus.lottery.domain.activity.model.req.PartakeReq;
import cn.itedus.lottery.domain.activity.model.vo.*;
import cn.itedus.lottery.domain.activity.repository.IActivityRepository;
import cn.itedus.lottery.infrastructure.dao.*;
import cn.itedus.lottery.infrastructure.po.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 活动仓储实现类
 */
@Component
public class ActivityRepository implements IActivityRepository {

    @Resource
    private IActivityDao activityDao;
    @Resource
    private IAwardDao awardDao;
    @Resource
    private IStrategyDao strategyDao;
    @Resource
    private IStrategyDetailDao strategyDetailDao;
    @Resource
    private IUserTakeActivityCountDao userTakeActivityCountDao;

    /**
     * 添加活动
     *
     * @param activity 活动信息
     */
    @Override
    public void addActivity(ActivityVO activity) {
        Activity req = new Activity();
        BeanUtils.copyProperties(activity, req);
        activityDao.insert(req);
    }

    /**
     * 添加奖品
     *
     * @param awardList 奖品列表
     */
    @Override
    public void addAward(List<AwardVO> awardList) {
        List<Award> req = new ArrayList<>();
        for (AwardVO awardVO : awardList) {
            Award award = new Award();
            BeanUtils.copyProperties(awardVO, award);
            req.add(award);
        }
        awardDao.insertList(req);
    }

    /**
     * 添加策略
     *
     * @param strategy 策略信息
     */
    @Override
    public void addStrategy(StrategyVO strategy) {
        Strategy req = new Strategy();
        BeanUtils.copyProperties(strategy, req);
        strategyDao.insert(req);
    }

    /**
     * 添加策略明细列表
     *
     * @param strategyDetailList 策略明细列表
     */
    @Override
    public void addStrategyDetailList(List<StrategyDetailVO> strategyDetailList) {
        List<StrategyDetail> req = new ArrayList<>();
        for (StrategyDetailVO strategyDetailVO : strategyDetailList) {
            StrategyDetail strategyDetail = new StrategyDetail();
            BeanUtils.copyProperties(strategyDetailVO, strategyDetail);
            req.add(strategyDetail);
        }
        strategyDetailDao.insertList(req);
    }

    /**
     * 变更活动状态
     *
     * @param activityId  活动ID
     * @param beforeState 变更前状态
     * @param afterState  变更后状态
     * @return 是否变更成功
     */
    @Override
    public boolean alterStatus(Long activityId, Enum<Constants.ActivityState> beforeState, Enum<Constants.ActivityState> afterState) {
        AlterStateVO alterStateVO = new AlterStateVO(activityId,((Constants.ActivityState) beforeState).getCode(),((Constants.ActivityState) afterState).getCode());
        int count = activityDao.alterState(alterStateVO);
        return 1 == count;
    }

    /**
     * 查询活动账单
     *
     * @param req 参与活动请求
     * @return 活动账单
     */
    @Override
    public ActivityBillVO queryActivityBill(PartakeReq req) {
        // 查询活动信息
        Activity activity = activityDao.queryActivityById(req.getActivityId());

        // 查询领取次数
        UserTakeActivityCount userTakeActivityCountReq = new UserTakeActivityCount();
        userTakeActivityCountReq.setUId(req.getUId());
        userTakeActivityCountReq.setActivityId(req.getActivityId());
        UserTakeActivityCount userTakeActivityCount = userTakeActivityCountDao.queryUserTakeActivityCount(userTakeActivityCountReq);


        // 用户首次参与活动，初始化参与记录
        if (null == userTakeActivityCount) {
            userTakeActivityCount = new UserTakeActivityCount();
            userTakeActivityCount.setUId(req.getUId());
            userTakeActivityCount.setActivityId(req.getActivityId());
            userTakeActivityCount.setTotalCount(activity.getTakeCount());
            userTakeActivityCount.setLeftCount(activity.getTakeCount());

            // 插入初始记录
            userTakeActivityCountDao.insert(userTakeActivityCount);
        }

        // 封装结果信息
        ActivityBillVO activityBillVO = new ActivityBillVO();
        activityBillVO.setUId(req.getUId());
        activityBillVO.setActivityId(req.getActivityId());
        activityBillVO.setActivityName(activity.getActivityName());
        activityBillVO.setBeginDateTime(activity.getBeginDateTime());
        activityBillVO.setEndDateTime(activity.getEndDateTime());
        activityBillVO.setTakeCount(activity.getTakeCount());
        activityBillVO.setStockSurplusCount(activity.getStockSurplusCount());
        activityBillVO.setStrategyId(activity.getStrategyId());
        activityBillVO.setState(activity.getState());
        activityBillVO.setUserTakeLeftCount(null == userTakeActivityCount ? userTakeActivityCountReq.getTotalCount() : userTakeActivityCount.getLeftCount());

        return activityBillVO;
    }

    /**
     * 扣减活动库存
     *
     * @param activityId 活动ID
     * @return 扣减结果
     */
    @Override
    public int subtractionActivityStock(Long activityId) {
        return activityDao.subtractionActivityStock(activityId);
    }

}

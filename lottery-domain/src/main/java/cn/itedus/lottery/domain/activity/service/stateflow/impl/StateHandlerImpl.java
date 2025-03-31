package cn.itedus.lottery.domain.activity.service.stateflow.impl;

import cn.itedus.lottery.common.Constants;
import cn.itedus.lottery.common.Result;
import cn.itedus.lottery.domain.activity.service.stateflow.IStateHandler;
import cn.itedus.lottery.domain.activity.service.stateflow.StateConfig;

/**状态处理服务*/
public class StateHandlerImpl extends StateConfig implements IStateHandler {
    /**
     * 提审
     *
     * @param activityId    活动ID
     * @param currentStatus 当前状态
     * @return 审核结果
     */
    @Override
    public Result arraignment(Long activityId, Enum<Constants.ActivityState> currentStatus) {
        return null;
    }

    /**
     * 审核通过
     *
     * @param activityId    活动ID
     * @param currentStatus 当前状态
     * @return 审核结果
     */
    @Override
    public Result checkPass(Long activityId, Enum<Constants.ActivityState> currentStatus) {
        return null;
    }

    /**
     * 审核拒绝
     *
     * @param activityId    活动ID
     * @param currentStatus 当前状态
     * @return 审核结果
     */
    @Override
    public Result checkRefuse(Long activityId, Enum<Constants.ActivityState> currentStatus) {
        return null;
    }

    /**
     * 撤销审核
     *
     * @param activityId    活动ID
     * @param currentStatus 当前状态
     * @return 审核结果
     */
    @Override
    public Result checkRevoke(Long activityId, Enum<Constants.ActivityState> currentStatus) {
        return null;
    }

    /**
     * 关闭
     *
     * @param activityId    活动ID
     * @param currentStatus 当前状态
     * @return 审核结果
     */
    @Override
    public Result close(Long activityId, Enum<Constants.ActivityState> currentStatus) {
        return null;
    }

    /**
     * 开启
     *
     * @param activityId    活动ID
     * @param currentStatus 当前状态
     * @return 审核结果
     */
    @Override
    public Result open(Long activityId, Enum<Constants.ActivityState> currentStatus) {
        return null;
    }

    /**
     * 运行活动中
     *
     * @param activityId    活动ID
     * @param currentStatus 当前状态
     * @return 审核结果
     */
    @Override
    public Result doing(Long activityId, Enum<Constants.ActivityState> currentStatus) {
        return null;
    }
}

package cn.itedus.lottery.domain.activity.service.partake;

import cn.itedus.lottery.common.Constants;
import cn.itedus.lottery.common.Result;
import cn.itedus.lottery.domain.activity.model.req.PartakeReq;
import cn.itedus.lottery.domain.activity.model.res.PartakeResult;
import cn.itedus.lottery.domain.activity.model.vo.ActivityBillVO;

/**活动领取抽象类：模版模式*/
public abstract class BaseActivityPartake extends ActivityPartakeSupport implements IActivityPartake {
    @Override
    public PartakeResult doPartake(PartakeReq req) {
        // 查询活动账单
        ActivityBillVO activityBillVO = super.queryActivityBill(req);

        // 依次执行活动参与的三个核心步骤，任一步骤失败则直接返回
        Result result;

        // 1. 活动信息校验
        result = this.checkActivityBill(req, activityBillVO);
        if (!isSuccess(result)) return new PartakeResult(result.getCode(), result.getInfo());

        // 2. 扣减活动库存
        result = this.subtractionActivityStock(req);
        if (!isSuccess(result)) return new PartakeResult(result.getCode(), result.getInfo());

        // 3. 领取活动信息
        result = this.grabActivity(req, activityBillVO);
        if (!isSuccess(result)) return new PartakeResult(result.getCode(), result.getInfo());

        // 4. 封装成功结果
        PartakeResult partakeResult = new PartakeResult(Constants.ResponseCode.SUCCESS.getCode(),
                Constants.ResponseCode.SUCCESS.getInfo());
        partakeResult.setStrategyId(activityBillVO.getStrategyId());
        return partakeResult;
    }

    /**
     * 判断结果是否成功
     */
    private boolean isSuccess(Result result) {
        return Constants.ResponseCode.SUCCESS.getCode().equals(result.getCode());
    }


    /**
     * 活动信息校验处理，把活动库存、状态、日期、个人参与次数
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
     * @return 领取结果
     */
    protected abstract Result grabActivity(PartakeReq partake, ActivityBillVO bill);

}

package cn.itedus.lottery.application.process.impl;

import cn.itedus.lottery.application.process.IActivityProcess;
import cn.itedus.lottery.application.process.req.DrawProcessReq;
import cn.itedus.lottery.application.process.res.DrawProcessResult;
import cn.itedus.lottery.common.Constants;
import cn.itedus.lottery.domain.activity.model.req.PartakeReq;
import cn.itedus.lottery.domain.activity.model.res.PartakeResult;
import cn.itedus.lottery.domain.activity.model.vo.DrawOrderVO;
import cn.itedus.lottery.domain.activity.service.partake.IActivityPartake;
import cn.itedus.lottery.domain.strategy.model.req.DrawReq;
import cn.itedus.lottery.domain.strategy.model.res.DrawResult;
import cn.itedus.lottery.domain.strategy.model.vo.DrawAwardInfo;
import cn.itedus.lottery.domain.strategy.service.draw.IDrawExec;
import cn.itedus.lottery.domain.support.ids.IIdGenerator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 活动抽奖流程
 */
@Service
public class ActivityProcessImpl implements IActivityProcess {

    @Resource
    private IActivityPartake activityPartake;

    @Resource
    private IDrawExec drawExec;

    @Resource
    private Map<Constants.Ids, IIdGenerator> idGeneratorMap;

    @Override
    public DrawProcessResult doDrawProcess(DrawProcessReq req) {
        try {
            // 1. 领取活动
            PartakeResult partakeResult = activityPartake.doPartake(new PartakeReq(req.getUId(), req.getActivityId()));
            if (!Constants.ResponseCode.SUCCESS.getCode().equals(partakeResult.getCode())) {
                return new DrawProcessResult(partakeResult.getCode(), partakeResult.getInfo());
            }

            // 2. 执行抽奖
            Long strategyId = partakeResult.getStrategyId();
            Long takeId = partakeResult.getTakeId();
            DrawResult drawResult;
            try {
                drawResult = drawExec.doDrawExec(new DrawReq(req.getUId(), strategyId, String.valueOf(takeId)));
                if (Constants.DrawState.FAIL.getCode().equals(drawResult.getDrawState())) {
                    return new DrawProcessResult(Constants.ResponseCode.LOSING_DRAW.getCode(), Constants.ResponseCode.ILLEGAL_PARAMETER.getInfo());
                }
            } catch (Exception e) {
                return handleException("执行抽奖", e);
            }

            // 3. 结果落库
            try {
                DrawAwardInfo drawAwardInfo = drawResult.getDrawAwardInfo();
                activityPartake.recordDrawOrder(buildDrawOrderVO(req, strategyId, takeId, drawAwardInfo));
            } catch (Exception e) {
                return handleException("结果落库", e);
            }

            // 4. 发送MQ，触发发奖流程


            // 5. 返回结果
            return new DrawProcessResult(Constants.ResponseCode.SUCCESS.getCode(), Constants.ResponseCode.SUCCESS.getInfo(), drawResult.getDrawAwardInfo());

        } catch (Exception e) {
            // 捕获整个流程的异常
            return handleException("整体流程", e);
        }
    }

    // 异常处理方法
    private DrawProcessResult handleException(String step, Exception e) {
        // 可以在这里记录日志，比如使用 log.error()
        System.err.println("在步骤 [" + step + "] 中发生异常: " + e.getMessage());
        e.printStackTrace();
        return new DrawProcessResult(Constants.ResponseCode.UN_ERROR.getCode(), "在步骤 [" + step + "] 中发生异常：" + e.getMessage());
    }

    private DrawOrderVO buildDrawOrderVO(DrawProcessReq req, Long strategyId, Long takeId, DrawAwardInfo drawAwardInfo) {
        long orderId = idGeneratorMap.get(Constants.Ids.SnowFlake).nextId();
        DrawOrderVO drawOrderVO = new DrawOrderVO();
        drawOrderVO.setUId(req.getUId());
        drawOrderVO.setTakeId(takeId);
        drawOrderVO.setActivityId(req.getActivityId());
        drawOrderVO.setOrderId(orderId);
        drawOrderVO.setStrategyId(strategyId);
        drawOrderVO.setStrategyMode(drawAwardInfo.getStrategyMode());
        drawOrderVO.setGrantType(drawAwardInfo.getGrantType());
        drawOrderVO.setGrantDate(drawAwardInfo.getGrantDate());
        drawOrderVO.setGrantState(Constants.GrantState.INIT.getCode());
        drawOrderVO.setAwardId(drawAwardInfo.getAwardId());
        drawOrderVO.setAwardType(drawAwardInfo.getAwardType());
        drawOrderVO.setAwardName(drawAwardInfo.getAwardName());
        drawOrderVO.setAwardContent(drawAwardInfo.getAwardContent());
        return drawOrderVO;
    }

}

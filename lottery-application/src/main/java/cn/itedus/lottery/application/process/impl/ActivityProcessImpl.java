package cn.itedus.lottery.application.process.impl;

import cn.itedus.lottery.application.process.IActivityProcess;
import cn.itedus.lottery.application.process.req.DrawProcessReq;
import cn.itedus.lottery.application.process.res.DrawProcessResult;
import cn.itedus.lottery.application.process.res.RuleQuantificationCrowdResult;
import cn.itedus.lottery.common.Constants;
import cn.itedus.lottery.domain.activity.model.req.PartakeReq;
import cn.itedus.lottery.domain.activity.model.res.PartakeResult;
import cn.itedus.lottery.domain.activity.model.vo.DrawOrderVO;
import cn.itedus.lottery.domain.activity.service.partake.IActivityPartake;
import cn.itedus.lottery.domain.rule.model.req.DecisionMatterReq;
import cn.itedus.lottery.domain.strategy.model.req.DrawReq;
import cn.itedus.lottery.domain.strategy.model.res.DrawResult;
import cn.itedus.lottery.domain.strategy.model.vo.DrawAwardVO;
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

    /**
     * 执行抽奖流程
     *
     * <p>该方法实现了完整的抽奖流程，包括以下几个步骤：</p>
     * <ol>
     *     <li>用户参与活动：通过 {@link IActivityPartake#doPartake(PartakeReq)} 方法判断用户是否可以参与指定的活动。</li>
     *     <li>执行抽奖：如果用户可以参与活动，则调用 {@link IDrawExec#doDrawExec(DrawReq)} 方法执行抽奖。</li>
     *     <li>结果落库：将抽奖结果保存到数据库中，通过 {@link IActivityPartake#recordDrawOrder(DrawOrderVO)} 方法记录抽奖订单。</li>
     *     <li>发送MQ：触发发奖流程（该步骤目前未实现）。</li>
     *     <li>返回结果：返回抽奖结果，包括是否成功以及具体的奖品信息。</li>
     * </ol>
     *
     * @param req 抽奖请求对象，包含用户ID和活动ID
     * @return 抽奖结果对象，包含状态码、状态信息和奖品信息
     */
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
                DrawAwardVO drawAwardVO = drawResult.getDrawAwardVO();
                activityPartake.recordDrawOrder(buildDrawOrderVO(req, strategyId, takeId, drawAwardVO));
            } catch (Exception e) {
                return handleException("结果落库", e);
            }

            // 4. 发送MQ，触发发奖流程


            // 5. 返回结果
            return new DrawProcessResult(Constants.ResponseCode.SUCCESS.getCode(), Constants.ResponseCode.SUCCESS.getInfo(), drawResult.getDrawAwardVO());

        } catch (Exception e) {
            // 捕获整个流程的异常
            return handleException("整体流程", e);
        }
    }

    /**
     * 规则量化人群，返回可参与的活动ID
     *
     * @param req 规则请求
     * @return 量化结果，用户可以参与的活动ID
     */
    @Override
    public RuleQuantificationCrowdResult doRuleQuantificationCrowd(DecisionMatterReq req) {
        return null;
    }

    // 异常处理方法
    private DrawProcessResult handleException(String step, Exception e) {
        // 可以在这里记录日志，比如使用 log.error()
        System.err.println("在步骤 [" + step + "] 中发生异常: " + e.getMessage());
        e.printStackTrace();
        return new DrawProcessResult(Constants.ResponseCode.UN_ERROR.getCode(), "在步骤 [" + step + "] 中发生异常：" + e.getMessage());
    }

    private DrawOrderVO buildDrawOrderVO(DrawProcessReq req, Long strategyId, Long takeId, DrawAwardVO drawAwardVO) {
        long orderId = idGeneratorMap.get(Constants.Ids.SnowFlake).nextId();
        DrawOrderVO drawOrderVO = new DrawOrderVO();
        drawOrderVO.setUId(req.getUId());
        drawOrderVO.setTakeId(takeId);
        drawOrderVO.setActivityId(req.getActivityId());
        drawOrderVO.setOrderId(orderId);
        drawOrderVO.setStrategyId(strategyId);
        drawOrderVO.setStrategyMode(drawAwardVO.getStrategyMode());
        drawOrderVO.setGrantType(drawAwardVO.getGrantType());
        drawOrderVO.setGrantDate(drawAwardVO.getGrantDate());
        drawOrderVO.setGrantState(Constants.GrantState.INIT.getCode());
        drawOrderVO.setAwardId(drawAwardVO.getAwardId());
        drawOrderVO.setAwardType(drawAwardVO.getAwardType());
        drawOrderVO.setAwardName(drawAwardVO.getAwardName());
        drawOrderVO.setAwardContent(drawAwardVO.getAwardContent());
        return drawOrderVO;
    }

}

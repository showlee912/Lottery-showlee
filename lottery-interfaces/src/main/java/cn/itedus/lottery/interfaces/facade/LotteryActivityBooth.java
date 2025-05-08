package cn.itedus.lottery.interfaces.facade;

import cn.itedus.lottery.application.process.IActivityProcess;
import cn.itedus.lottery.application.process.req.DrawProcessReq;
import cn.itedus.lottery.application.process.res.DrawProcessResult;
import cn.itedus.lottery.application.process.res.RuleQuantificationCrowdResult;
import cn.itedus.lottery.common.Constants;
import cn.itedus.lottery.domain.rule.model.req.DecisionMatterReq;
import cn.itedus.lottery.rpc.ILotteryActivityBooth;
import cn.itedus.lottery.rpc.dto.AwardDTO;
import cn.itedus.lottery.rpc.req.DrawReq;
import cn.itedus.lottery.rpc.req.QuantificationDrawReq;
import cn.itedus.lottery.rpc.res.DrawRes;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
public class LotteryActivityBooth implements ILotteryActivityBooth {

    private final Logger logger = LoggerFactory.getLogger(LotteryActivityBooth.class);

    @Resource
    private IActivityProcess activityProcess;

//    @Resource
//    private IMapping<DrawAwardVO, AwardDTO> awardMapping;

    @Override
    public DrawRes doDraw(DrawReq drawReq) {
        logger.info("抽奖，开始 uId：{} activityId：{}", drawReq.getUId(), drawReq.getActivityId());
        return processDraw(drawReq.getUId(), drawReq.getActivityId(), null);
    }

    @Override
    public DrawRes doQuantificationDraw(QuantificationDrawReq quantificationDrawReq) {
        logger.info("量化人群抽奖，开始 uId：{} treeId：{}", quantificationDrawReq.getUId(), quantificationDrawReq.getTreeId());
        // 1. 执行规则引擎，获取用户可以参与的活动号
        RuleQuantificationCrowdResult ruleQuantificationCrowdResult =
                activityProcess.doRuleQuantificationCrowd(new DecisionMatterReq(quantificationDrawReq.getUId(),
                        quantificationDrawReq.getTreeId(), quantificationDrawReq.getValMap()));
        //未成功
        if (!Constants.ResponseCode.SUCCESS.getCode().equals(ruleQuantificationCrowdResult.getCode())) {
            logger.error("量化人群抽奖，失败(规则引擎执行异常) uId：{} treeId：{}", quantificationDrawReq.getUId(),
                    quantificationDrawReq.getTreeId());
            return new DrawRes(ruleQuantificationCrowdResult.getCode(), ruleQuantificationCrowdResult.getInfo());
        }

        Long activityId = ruleQuantificationCrowdResult.getActivityId();//获取活动号
        return processDraw(quantificationDrawReq.getUId(), activityId, quantificationDrawReq.getTreeId());

    }

    /**
     * 统一处理抽奖逻辑
     */
    private DrawRes processDraw(String uId, Long activityId, Long treeId) {
        try {
            // 1. 执行抽奖
            DrawProcessResult drawProcessResult = activityProcess.doDrawProcess(new DrawProcessReq(uId, activityId));

            // 如果抽奖失败，则记录错误日志并返回失败结果
            if (!Constants.ResponseCode.SUCCESS.getCode().equals(drawProcessResult.getCode())) {
                logError(treeId, uId, activityId, "抽奖过程异常");
                return new DrawRes(drawProcessResult.getCode(), drawProcessResult.getInfo());
            }

            // 2. 数据转换 DrawAwardVO -> AwardDTO
//            AwardDTO awardDTO = awardMapping.sourceToTarget(drawProcessResult.getDrawAwardVO());
//            awardDTO.setActivityId(activityId);
            AwardDTO awardDTO = org.springframework.beans.BeanUtils.instantiateClass(AwardDTO.class);
            org.springframework.beans.BeanUtils.copyProperties(awardDTO, drawProcessResult.getDrawAwardVO());
            awardDTO.setUserId(drawProcessResult.getDrawAwardVO().getuId());
            awardDTO.setActivityId(activityId);


            // 3. 封装数据并返回
            DrawRes drawRes = new DrawRes(Constants.ResponseCode.SUCCESS.getCode(),
                    Constants.ResponseCode.SUCCESS.getInfo());
            drawRes.setAwardDTO(awardDTO);
            logSuccess(treeId, uId, activityId, drawRes);
            return drawRes;

        } catch (Exception e) {
            // 捕获异常并记录详细的错误日志
            logError(treeId, uId, activityId, "抽奖失败", e);
            return new DrawRes(Constants.ResponseCode.UN_ERROR.getCode(), Constants.ResponseCode.UN_ERROR.getInfo());
        }
    }


    /**
     * 记录成功日志
     */
    private void logSuccess(Long treeId, String uId, Long activityId, DrawRes drawRes) {
        if (treeId != null) {
            logger.info("量化人群抽奖，完成 uId：{} treeId：{} drawRes：{}", uId, treeId, JSON.toJSONString(drawRes));
        } else {
            logger.info("抽奖，完成 uId：{} activityId：{} drawRes：{}", uId, activityId, JSON.toJSONString(drawRes));
        }
    }


    /**
     * 记录错误日志
     */
    private void logError(Long treeId, String uId, Long activityId, String message, Exception... e) {
        if (treeId != null) {
            logger.error("量化人群抽奖，失败 uId：{} treeId：{} 消息：{}", uId, treeId, message, e.length > 0 ? e[0] : null);
        } else {
            logger.error("抽奖，失败 uId：{} activityId：{} 消息：{}", uId, activityId, message, e.length > 0 ? e[0] : null);
        }
    }

}

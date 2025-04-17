package cn.itedus.lottery.domain.strategy.service.draw;

import cn.itedus.lottery.common.Constants;
import cn.itedus.lottery.domain.strategy.model.aggregates.StrategyRich;
import cn.itedus.lottery.domain.strategy.model.req.DrawReq;
import cn.itedus.lottery.domain.strategy.model.res.DrawResult;
import cn.itedus.lottery.domain.strategy.model.vo.*;
import cn.itedus.lottery.domain.strategy.service.algorithm.IDrawAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽奖过程抽象类（模板模式）
 */
public abstract class AbstractDrawBase extends DrawStrategySupport implements IDrawExec {

    private final Logger logger = LoggerFactory.getLogger(AbstractDrawBase.class);


    /**
     * 模版入口
     */
    @Override
    public DrawResult doDrawExec(DrawReq req) {
        // 1. 获取抽奖策略
        StrategyRich strategyRich = super.queryStrategyRich(req.getStrategyId());
        StrategyBriefVO strategy = strategyRich.getStrategy();

        // 2. 校验抽奖策略是否已经初始化到内存
        this.checkAndInitRateData(req.getStrategyId(), strategy.getStrategyMode(),
                strategyRich.getStrategyDetailList());

        // 3. 获取不在抽奖范围内的列表，包括：奖品库存为空、风控策略、临时调整等
        List<String> excludeAwardIds = this.queryExcludeAwardIds(req.getStrategyId());

        // 4. 执行抽奖算法
        String awardId = this.drawAlgorithm(req.getStrategyId(), drawAlgorithmMap.get(strategy.getStrategyMode()),
                excludeAwardIds);

        // 5. 包装中奖结果
        return buildDrawResult(req.getUid(), req.getStrategyId(), awardId);
    }


    /**
     * 2. 校验抽奖策略是否已经初始化到内存，只有单项概率策略模式才需要初始化
     *
     * @param strategyId         抽奖策略ID
     * @param strategyMode       抽奖策略模式
     * @param strategyDetailList 抽奖策略详情
     */
    private void checkAndInitRateData(Long strategyId, Integer strategyMode, List<StrategyDetailBriefVO> strategyDetailList) {
        //不是单项概率策略模式
        if (!Constants.StrategyMode.SINGLE.getCode().equals(strategyMode)) return;

        IDrawAlgorithm DrawAlgorithm = drawAlgorithmMap.get(strategyMode);

        //数据已初始化
        if (DrawAlgorithm.isExistRateTuple(strategyId)) return;

        //初始化概率元组
        ArrayList<AwardRateVO> awardRateVOS = new ArrayList<>(strategyDetailList.size());

        for (StrategyDetailBriefVO strategyDetail : strategyDetailList) {
            awardRateVOS.add(new AwardRateVO(strategyDetail.getAwardId(),strategyDetail.getAwardRate()));
        }

        DrawAlgorithm.initRateTuple(strategyId, awardRateVOS);
    }

    /**
     * 3. 获取不在抽奖范围内的列表，包括：奖品库存为空、风控策略、临时调整等
     *
     * @param strategyId 策略ID
     * @return 排除的奖品ID集合
     */
    protected abstract List<String> queryExcludeAwardIds(Long strategyId);


    /**
     * 4. 执行抽奖算法
     *
     * @param strategyId      策略ID
     * @param drawAlgorithm   抽奖算法模型
     * @param excludeAwardIds 排除的抽奖ID集合
     * @return 中奖奖品ID
     */
    protected abstract String drawAlgorithm(Long strategyId, IDrawAlgorithm drawAlgorithm,
                                            List<String> excludeAwardIds);


    /**
     * 5. 包装抽奖结果
     *
     * @param uid        用户ID
     * @param strategyId 策略ID
     * @param awardId    奖品ID，null 情况：并发抽奖情况下，库存临界值1 -> 0，会有用户中奖结果为 null
     * @return 中奖结果
     */
    private DrawResult buildDrawResult(String uid, Long strategyId, String awardId) {
        if (null == awardId) {
            logger.info("执行策略抽奖完成【未中奖】，用户：{} 策略ID：{}", uid, strategyId);
            return new DrawResult(uid, strategyId, Constants.DrawState.FAIL.getCode());
        }

        AwardBriefVO award = super.queryAwardInfoByAwardId(awardId);
        DrawAwardVO drawAwardVO = new DrawAwardVO(award.getAwardId(), award.getAwardName(),award.getAwardType(),award.getAwardContent());
        logger.info("执行策略抽奖完成【已中奖】，用户：{} 策略ID：{} 奖品ID：{} 奖品名称：{}", uid, strategyId, awardId, award.getAwardName());

        return new DrawResult(uid, strategyId, Constants.DrawState.SUCCESS.getCode(), drawAwardVO);
    }
}


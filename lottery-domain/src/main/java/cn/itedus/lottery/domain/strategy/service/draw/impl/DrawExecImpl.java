package cn.itedus.lottery.domain.strategy.service.draw.impl;

import cn.itedus.lottery.domain.strategy.model.aggregates.StrategyRich;
import cn.itedus.lottery.domain.strategy.model.req.DrawReq;
import cn.itedus.lottery.domain.strategy.model.res.DrawResult;
import cn.itedus.lottery.domain.strategy.repository.IStrategyRepository;
import cn.itedus.lottery.domain.strategy.service.algorithm.IDrawAlgorithm;
import cn.itedus.lottery.domain.strategy.service.draw.AbstractDrawBase;
import cn.itedus.lottery.domain.strategy.service.draw.DrawBase;
import cn.itedus.lottery.domain.strategy.service.draw.IDrawExec;
import cn.itedus.lottery.infrastructure.po.Award;
import cn.itedus.lottery.infrastructure.po.Strategy;
import cn.itedus.lottery.infrastructure.po.StrategyDetail;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("drawExec")
public class DrawExecImpl extends AbstractDrawBase {

    private final Logger logger = LoggerFactory.getLogger(DrawExecImpl.class);

    /**
     * 3. 获取不在抽奖范围内的列表，包括：奖品库存为空、风控策略、临时调整等
     *
     * @param strategyId 策略ID
     * @return 排除的奖品ID集合
     */
    @Override
    protected List<String> queryExcludeAwardIds(Long strategyId) {
        List<String> NoStockAwardList = strategyRepository.queryNoStockStrategyAwardList(strategyId);
        logger.info("执行抽奖策略 strategyId：{}，无库存排除奖品列表ID集合 awardList：{}", strategyId, JSON.toJSONString(NoStockAwardList));
        return NoStockAwardList;
    }

    /**
     * 4. 执行抽奖算法
     *
     * @param strategyId      策略ID
     * @param drawAlgorithm   抽奖算法模型
     * @param excludeAwardIds 排除的抽奖ID集合
     * @return 中奖奖品ID
     */
    @Override
    protected String drawAlgorithm(Long strategyId, IDrawAlgorithm drawAlgorithm, List<String> excludeAwardIds) {
        //1. 执行抽奖
        String awardId = drawAlgorithm.randomDraw(strategyId, excludeAwardIds);

        if (null == awardId) {
            return null;
        }

        //2. 扣减库存
        boolean isSuccess = strategyRepository.deductStock(strategyId, awardId);

        return isSuccess ? awardId : null;
    }
}

package cn.itedus.lottery.domain.strategy.repository;

import cn.itedus.lottery.domain.strategy.model.aggregates.StrategyRich;
import cn.itedus.lottery.domain.strategy.model.vo.AwardBriefVO;

import java.util.List;

public interface IStrategyRepository {

    /**
     * 查询策略聚合信息
     * @param strategyId 策略ID
     * @return 包含策略和策略详情的聚合对象
     */
    StrategyRich queryStrategyRich(Long strategyId);

    /**
     * 查询奖品详细信息
     * @param awardId 奖品ID
     * @return 奖品数据对象
     */
    AwardBriefVO queryAwardInfo(String awardId);

    /**
     * 查询无库存的奖品ID集合
     * @param strategyId 策略ID
     * @return 无库存的奖品ID列表（不可参与抽奖）
     */
    List<String> queryNoStockStrategyAwardList(Long strategyId);

    /**
     * 扣减库存
     * @param strategyId 策略ID
     * @param awardId    奖品ID
     * @return           扣减结果
     */
    boolean deductStock(Long strategyId, String awardId);

}

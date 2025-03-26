package cn.itedus.lottery.domain.strategy.model.aggregates;

import cn.itedus.lottery.infrastructure.po.Strategy;
import cn.itedus.lottery.infrastructure.po.StrategyDetail;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


/**
 * 策略信息聚合根对象
 */

@Data
@AllArgsConstructor

public class StrategyRich {

    // 策略ID
    private Long strategyId;
    // 策略配置
    private Strategy strategy;
    // 策略明细
    private List<StrategyDetail> strategyDetailList;

}

package cn.itedus.lottery.domain.strategy.model.aggregates;

import cn.itedus.lottery.domain.strategy.model.vo.StrategyBriefVO;
import cn.itedus.lottery.domain.strategy.model.vo.StrategyDetailBriefVO;
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
    private StrategyBriefVO strategy;
    // 策略明细
    private List<StrategyDetailBriefVO> strategyDetailList;

}

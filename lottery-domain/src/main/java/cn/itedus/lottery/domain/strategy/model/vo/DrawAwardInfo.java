package cn.itedus.lottery.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 中奖奖品信息
 */
@Data
@AllArgsConstructor
public class DrawAwardInfo {

    /**
     * 奖品ID
     */
    private String rewardId;

    /**
     * 奖品名称
     */
    private String awardName;
}


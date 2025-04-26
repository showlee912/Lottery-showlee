package cn.itedus.lottery.domain.strategy.model.res;

import cn.itedus.lottery.common.Constants;
import cn.itedus.lottery.domain.strategy.model.vo.DrawAwardVO;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 抽奖结果
 */
@Data
@AllArgsConstructor
public class DrawResult {

    // 用户ID
    private String uId;

    // 策略ID
    private Long strategyId;

    /**
     * 中奖状态：0未中奖、1已中奖、2兜底奖
     */
    private Integer drawState = Constants.DrawState.FAIL.getCode();

    /**
     * 中奖奖品信息
     */
    private DrawAwardVO drawAwardVO;

    public DrawResult(String uId, Long strategyId, Integer drawState) {
        this.uId = uId;
        this.strategyId = strategyId;
        this.drawState = drawState;
    }



}

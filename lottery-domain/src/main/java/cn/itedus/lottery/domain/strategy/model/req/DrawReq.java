package cn.itedus.lottery.domain.strategy.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrawReq {

    // 用户ID
    private String Uid;

    // 策略ID
    private Long strategyId;

    /**
     * 防重ID
     */
    private String uuid;

    public DrawReq(String uId, Long strategyId) {
        this.Uid = uId;
        this.strategyId = strategyId;
    }
}

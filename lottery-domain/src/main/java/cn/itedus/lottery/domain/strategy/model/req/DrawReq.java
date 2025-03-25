package cn.itedus.lottery.domain.strategy.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DrawReq {

    // 用户ID
    private String Uid;

    // 策略ID
    private Long strategyId;

}

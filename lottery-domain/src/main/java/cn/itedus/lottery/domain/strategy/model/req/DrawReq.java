package cn.itedus.lottery.domain.strategy.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DrawReq {

    // 用户ID
    private String uId;

    // 策略ID
    private Long strategyId;

    // public DrawReq() {
    // }

    // public String getuId() {
    // return uId;
    // }

    // public void setuId(String uId) {
    // this.uId = uId;
    // }

    // public Long getStrategyId() {
    // return strategyId;
    // }

    // public void setStrategyId(Long strategyId) {
    // this.strategyId = strategyId;
    // }

}

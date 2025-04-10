package cn.itedus.lottery.domain.activity.model.res;

import cn.itedus.lottery.common.Result;
import lombok.Getter;
import lombok.Setter;

/**
 * 活动参与结果
 */
@Getter
@Setter
public class PartakeResult extends Result {

    /**
     * 策略ID
     */
    private Long strategyId;

    /**
     * 活动领取ID
     */
    private Long takeId;

    public PartakeResult(String code, String info) {
        super(code, info);
    }
}

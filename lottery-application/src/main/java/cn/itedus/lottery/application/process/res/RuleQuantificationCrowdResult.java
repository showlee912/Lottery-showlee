package cn.itedus.lottery.application.process.res;

import cn.itedus.lottery.common.Result;
import lombok.Getter;
import lombok.Setter;

/**规则量化结果，得到可参与的活动ID
 */
@Getter
@Setter
public class RuleQuantificationCrowdResult extends Result {

    /** 活动ID */
    private Long activityId;

    public RuleQuantificationCrowdResult(String code, String info) {
        super(code, info);
    }

}

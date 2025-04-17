package cn.itedus.lottery.domain.rule.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 决策请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DecisionMatterReq {

    /** 用户ID */
    private String userId;
    /** 规则树ID */
    private Long treeId;
    /** 决策值 */
    private Map<String, Object> valMap;

}

package cn.itedus.lottery.domain.rule.model.req;

import lombok.Data;

import java.util.Map;

/**
 * 决策请求
 */
@Data
public class DecisionMatterReq {

    /** 规则树ID */
    private Long treeId;
    /** 用户ID */
    private String userId;
    /** 决策值 */
    private Map<String, Object> valMap;
}

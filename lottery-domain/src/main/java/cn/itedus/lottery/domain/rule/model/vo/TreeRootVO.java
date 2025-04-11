package cn.itedus.lottery.domain.rule.model.vo;

import lombok.Data;

/**
 * 规则树根VO
 */
@Data
public class TreeRootVO {

    /** 规则树ID */
    private Long treeId;
    /** 规则树根ID */
    private Long treeRootNodeId;
    /** 规则树名称 */
    private String treeName;

}

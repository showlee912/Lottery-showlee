package cn.itedus.lottery.domain.rule.model.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 决策结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EngineResult {

    /** 执行结果 */
    private boolean isSuccess;
    /** 用户ID */
    private String userId;
    /** 规则树ID */
    private Long treeId;
    /** 果实节点ID */
    private Long nodeId;
    /** 果实节点值 */
    private String nodeValue;

    public EngineResult(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }


}

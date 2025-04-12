package cn.itedus.lottery.domain.rule.model.aggregates;

import cn.itedus.lottery.domain.rule.model.vo.TreeNodeVO;
import cn.itedus.lottery.domain.rule.model.vo.TreeRootVO;
import lombok.Data;

import java.util.Map;

/**
 *  规则树聚合
 */
@Data
public class TreeRuleRich {

    /**根节点VO*/
    private TreeRootVO treeRoot;

    /**节点VO映射*/
    private Map<Long, TreeNodeVO> treeNodeMap;

}


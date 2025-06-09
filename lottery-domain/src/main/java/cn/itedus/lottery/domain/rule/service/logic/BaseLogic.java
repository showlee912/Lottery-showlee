package cn.itedus.lottery.domain.rule.service.logic;

import cn.itedus.lottery.common.Constants;
import cn.itedus.lottery.domain.rule.model.req.DecisionMatterReq;
import cn.itedus.lottery.domain.rule.model.vo.TreeNodeLineVO;

import java.util.List;

/**
 * 规则基础抽象类
 */
public abstract class BaseLogic implements LogicFilter {

    /**
     * 过滤规则
     * 
     * @param matterValue          决策的值
     * @param treeNodeLineInfoList 规则树节点连线信息列表
     * @return 匹配的节点ID，如果没有匹配则返回 TREE_NULL_NODE
     */
    @Override
    public Long filter(String matterValue, List<TreeNodeLineVO> treeNodeLineInfoList) {
        // 遍历连线List
        for (TreeNodeLineVO nodeLine : treeNodeLineInfoList) {
            // 判断决策值是否符合当前节点连线的规则
            if (decisionLogic(matterValue, nodeLine)) {
                // 如果符合规则，返回当前节点连线的目标节点ID
                return nodeLine.getNodeIdTo();
            }
        }
        // 未匹配任何规则，返回空节点
        return Constants.Global.TREE_NULL_NODE;
    }

    /**
     * 子类实现：获取规则比对值
     * 
     * @param decisionMatter 决策请求
     * @return 比对值
     */
    @Override
    public abstract String matterValue(DecisionMatterReq decisionMatter);

    /**
     * 判断决策值是否符合规则连线的限制条件
     * 
     * @param matterValue 决策的值
     * @param nodeLine    规则树节点连线信息
     * @return 是否匹配
     */
    private boolean decisionLogic(String matterValue, TreeNodeLineVO nodeLine) {
        switch (nodeLine.getRuleLimitType()) {
            case Constants.RuleLimitType.EQUAL:
                return matterValue.equals(nodeLine.getRuleLimitValue());
            case Constants.RuleLimitType.GT:
                return Double.parseDouble(matterValue) > Double.parseDouble(nodeLine.getRuleLimitValue());
            case Constants.RuleLimitType.LT:
                return Double.parseDouble(matterValue) < Double.parseDouble(nodeLine.getRuleLimitValue());
            case Constants.RuleLimitType.GE:
                return Double.parseDouble(matterValue) >= Double.parseDouble(nodeLine.getRuleLimitValue());
            case Constants.RuleLimitType.LE:
                return Double.parseDouble(matterValue) <= Double.parseDouble(nodeLine.getRuleLimitValue());
            default:
                return false;
        }
    }

}

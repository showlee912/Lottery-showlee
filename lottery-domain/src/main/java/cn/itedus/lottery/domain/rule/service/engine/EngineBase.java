package cn.itedus.lottery.domain.rule.service.engine;

import cn.itedus.lottery.common.Constants;
import cn.itedus.lottery.domain.rule.model.aggregates.TreeRuleRich;
import cn.itedus.lottery.domain.rule.model.res.EngineResult;
import cn.itedus.lottery.domain.rule.model.req.DecisionMatterReq;
import cn.itedus.lottery.domain.rule.model.vo.TreeNodeVO;
import cn.itedus.lottery.domain.rule.model.vo.TreeRootVO;
import cn.itedus.lottery.domain.rule.service.logic.LogicFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 规则引擎基础类
 */
public abstract class EngineBase extends EngineConfig implements EngineFilter {

    private Logger logger = LoggerFactory.getLogger(EngineBase.class);

    /**
     * 处理决策请求
     * @param matter 决策请求参数
     * @return 决策结果
     */
    public abstract EngineResult process(DecisionMatterReq matter);

    /**
     * 决策树引擎决策方法
     * @param treeRuleRich 规则树
     * @param matter 决策请求参数
     * @return 决策树节点信息
     */
    protected TreeNodeVO engineDecisionMaker(TreeRuleRich treeRuleRich, DecisionMatterReq matter) {
        // 获取规则树根节点和节点映射
        TreeRootVO treeRoot = treeRuleRich.getTreeRoot();
        Map<Long, TreeNodeVO> treeNodeMap = treeRuleRich.getTreeNodeMap();

        // 规则树根ID
        Long rootNodeId = treeRoot.getTreeRootNodeId();
        TreeNodeVO treeNodeInfo = treeNodeMap.get(rootNodeId);

        logger.info("决策树引擎=>{} userId：{} treeId：{}", treeRoot.getTreeName(), matter.getUserId(), matter.getTreeId());

        // 向下遍历子叶节点，直到找到最终的决策结果
        while (Constants.NodeType.STEM.equals(treeNodeInfo.getNodeType())) {
            // 获取规则键
            String ruleKey = treeNodeInfo.getRuleKey();
            // 根据规则键获取逻辑过滤器
            LogicFilter logicFilter = logicFilterMap.get(ruleKey);
            // 获取决策请求参数的值
            String matterValue = logicFilter.matterValue(matter);
            // 根据决策请求参数的值过滤并获取下一个节点
            Long nextNode = logicFilter.filter(matterValue, treeNodeInfo.getTreeNodeLineInfoList());
            // 更新当前节点信息
            treeNodeInfo = treeNodeMap.get(nextNode);

            logger.info("treeNode：{} ruleKey：{} matterValue：{}",  treeNodeInfo.getTreeNodeId(), ruleKey, matterValue);
        }

        // 返回最终决策节点信息
        return treeNodeInfo;
    }

}

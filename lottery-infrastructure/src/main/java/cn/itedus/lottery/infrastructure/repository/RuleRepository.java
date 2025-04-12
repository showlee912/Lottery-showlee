package cn.itedus.lottery.infrastructure.repository;

import cn.itedus.lottery.common.Constants;
import cn.itedus.lottery.domain.rule.model.aggregates.TreeRuleRich;
import cn.itedus.lottery.domain.rule.model.vo.TreeNodeVO;
import cn.itedus.lottery.domain.rule.model.vo.TreeNodeLineVO;
import cn.itedus.lottery.domain.rule.model.vo.TreeRootVO;
import cn.itedus.lottery.domain.rule.repository.IRuleRepository;
import cn.itedus.lottery.infrastructure.dao.RuleTreeDao;
import cn.itedus.lottery.infrastructure.dao.RuleTreeNodeDao;
import cn.itedus.lottery.infrastructure.dao.RuleTreeNodeLineDao;
import cn.itedus.lottery.infrastructure.po.RuleTree;
import cn.itedus.lottery.infrastructure.po.RuleTreeNode;
import cn.itedus.lottery.infrastructure.po.RuleTreeNodeLine;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 规则信息仓储服务
 */
@Repository
public class RuleRepository implements IRuleRepository {

    @Resource
    private RuleTreeDao ruleTreeDao;
    @Resource
    private RuleTreeNodeDao ruleTreeNodeDao;
    @Resource
    private RuleTreeNodeLineDao ruleTreeNodeLineDao;

    @Override
    public TreeRuleRich queryTreeRuleRich(Long treeId) {
        // 1. 构建树根
        RuleTree ruleTree = ruleTreeDao.queryRuleTreeByTreeId(treeId);
        TreeRootVO treeRoot = buildTreeRoot(ruleTree);

        // 2. 构建节点映射
        Map<Long, TreeNodeVO> treeNodeMap = buildTreeNodeMap(treeId);

        // 3. 组装并返回结果
        TreeRuleRich treeRuleRich = new TreeRuleRich();
        treeRuleRich.setTreeRoot(treeRoot);
        treeRuleRich.setTreeNodeMap(treeNodeMap);

        return treeRuleRich;
    }

    /**
     * 构建树根信息
     */
    private TreeRootVO buildTreeRoot(RuleTree ruleTree) {
        TreeRootVO treeRoot = new TreeRootVO();
        treeRoot.setTreeId(ruleTree.getId());
        treeRoot.setTreeRootNodeId(ruleTree.getTreeRootNodeId());
        treeRoot.setTreeName(ruleTree.getTreeName());
        return treeRoot;
    }

    /**
     * 构建树节点映射
     */
    private Map<Long, TreeNodeVO> buildTreeNodeMap(Long treeId) {
        Map<Long, TreeNodeVO> treeNodeMap = new HashMap<>();
        List<RuleTreeNode> treeNodeList = ruleTreeNodeDao.queryRuleTreeNodeList(treeId);

        for (RuleTreeNode node : treeNodeList) {
            TreeNodeVO treeNodeInfo = new TreeNodeVO();
            treeNodeInfo.setTreeId(node.getTreeId());
            treeNodeInfo.setTreeNodeId(node.getId());
            treeNodeInfo.setNodeType(node.getNodeType());
            treeNodeInfo.setNodeValue(node.getNodeValue());
            treeNodeInfo.setRuleKey(node.getRuleKey());
            treeNodeInfo.setRuleDesc(node.getRuleDesc());

            // 只有STEM类型节点才需要获取连接线信息
            if (Constants.NodeType.STEM.equals(node.getNodeType())) {
                treeNodeInfo.setTreeNodeLineInfoList(getTreeNodeLineList(treeId, node.getId()));
            } else {
                treeNodeInfo.setTreeNodeLineInfoList(new ArrayList<>());
            }

            treeNodeMap.put(node.getId(), treeNodeInfo);
        }

        return treeNodeMap;
    }

    /**
     * 获取节点连接线列表
     */
    private List<TreeNodeLineVO> getTreeNodeLineList(Long treeId, Long nodeIdFrom) {
        RuleTreeNodeLine queryParam = new RuleTreeNodeLine();
        queryParam.setTreeId(treeId);
        queryParam.setNodeIdFrom(nodeIdFrom);

        List<TreeNodeLineVO> lineList = new ArrayList<>();
        List<RuleTreeNodeLine> ruleTreeNodeLineList = ruleTreeNodeLineDao.queryRuleTreeNodeLineList(queryParam);

        for (RuleTreeNodeLine nodeLine : ruleTreeNodeLineList) {
            TreeNodeLineVO lineInfo = new TreeNodeLineVO();
            lineInfo.setNodeIdFrom(nodeLine.getNodeIdFrom());
            lineInfo.setNodeIdTo(nodeLine.getNodeIdTo());
            lineInfo.setRuleLimitType(nodeLine.getRuleLimitType());
            lineInfo.setRuleLimitValue(nodeLine.getRuleLimitValue());
            lineList.add(lineInfo);
        }

        return lineList;
    }

}

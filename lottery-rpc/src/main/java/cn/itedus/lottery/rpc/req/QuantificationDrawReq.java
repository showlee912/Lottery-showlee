package cn.itedus.lottery.rpc.req;

import lombok.Data;

import java.util.Map;

/**
 * 量化人群抽奖请求参数
 */
@Data
public class QuantificationDrawReq {

    /** 用户ID */
    private String uId;
    /** 规则树ID */
    private Long treeId;
    /** 决策值 */
    private Map<String, Object> valMap;

}

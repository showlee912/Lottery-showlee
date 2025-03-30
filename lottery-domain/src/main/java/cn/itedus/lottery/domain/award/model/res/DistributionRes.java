package cn.itedus.lottery.domain.award.model.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品配送结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistributionRes {

    /** 用户ID */
    private String uid;

    /** 编码 */
    private Integer code;
    /** 描述 */
    private String info;

    /** 结算单ID，如：发券后有券码、发货后有单号等，用于存根查询 */
    private String statementId;

    /**
     * 构造函数
     *
     * @param uid   用户ID
     * @param code  编码
     * @param info  描述
     */
    public DistributionRes(String uid, Integer code, String info) {
        this.uid = uid;
        this.code = code;
        this.info = info;
    }

}

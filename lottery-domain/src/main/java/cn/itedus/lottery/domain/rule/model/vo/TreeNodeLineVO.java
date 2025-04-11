package cn.itedus.lottery.domain.rule.model.vo;

import lombok.Data;

/**
 * 节点连线
 */
@Data
public class TreeNodeLineVO {

    /** 节点From */
    private Long nodeIdFrom;
    /** 节点To */
    private Long nodeIdTo;
    /** 限定类型；1:=;2:>;3:<;4:>=;5<=;6:enum[枚举范围] */
    private Integer ruleLimitType;
    /** 限定值 */
    private String ruleLimitValue;

}

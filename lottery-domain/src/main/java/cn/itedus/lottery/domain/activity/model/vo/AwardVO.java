package cn.itedus.lottery.domain.activity.model.vo;


import lombok.Data;

/**
 * 奖品信息配置
 */
@Data
public class AwardVO {

    /** 奖品ID */
    private String awardId;

    /** 奖品类型（1:文字描述、2:兑换码、3:优惠券、4:实物奖品） */
    private Integer awardType;

    /** 奖品名称 */
    private String awardName;

    /** 奖品内容「描述、奖品码、sku」 */
    private String awardContent;

    @Override
    public String toString() {
        return "AwardVO{" + "awardId='" + awardId + '\'' + ", awardType=" + awardType + ", awardName='" + awardName + '\'' + ", awardContent='" + awardContent + '\'' + '}';
    }

}


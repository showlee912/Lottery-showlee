package cn.itedus.lottery.domain.activity.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * 活动信息配置
 */
@Data
public class ActivityVO {


    private Long id;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动描述
     */
    private String activityDesc;

    /**
     * 开始时间
     */
    private Date beginDateTime;

    /**
     * 结束时间
     */
    private Date endDateTime;

    /**
     * 库存
     */
    private Integer stockCount;

    /**
     * 每人可参与次数
     */
    private Integer takeCount;

    /**
     * 活动状态：编辑、提审、撤审、通过、运行、拒绝、关闭、开启
     */
    private Integer state;

    /**
     * 创建人
     */
    private String creator;

    @Override
    public String toString() {
        return "ActivityVO{" + "activityId=" + activityId + ", activityName='" + activityName + '\'' + ", " +
                "activityDesc='" + activityDesc + '\'' + ", beginDateTime=" + beginDateTime + ", endDateTime=" + endDateTime + ", stockCount=" + stockCount + ", takeCount=" + takeCount + ", state=" + state + ", creator='" + creator + '\'' + '}';
    }

}

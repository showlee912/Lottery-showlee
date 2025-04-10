package cn.itedus.lottery.application.process.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 抽奖请求
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrawProcessReq {

    /** 用户ID */
    private String uId;
    /** 活动ID */
    private Long activityId;

}

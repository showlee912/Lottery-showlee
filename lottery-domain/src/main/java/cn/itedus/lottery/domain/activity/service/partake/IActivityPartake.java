package cn.itedus.lottery.domain.activity.service.partake;

import cn.itedus.lottery.domain.activity.model.req.PartakeReq;
import cn.itedus.lottery.domain.activity.model.res.PartakeResult;

/**
 *  抽奖活动参与接口
 */
public interface IActivityPartake {

    PartakeResult doPartake(PartakeReq req);
}

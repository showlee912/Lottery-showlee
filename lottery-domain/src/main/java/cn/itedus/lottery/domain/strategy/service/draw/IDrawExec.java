package cn.itedus.lottery.domain.strategy.service.draw;

import cn.itedus.lottery.domain.strategy.model.req.DrawReq;
import cn.itedus.lottery.domain.strategy.model.res.DrawResult;

/**
抽奖执行接口
 */
public interface IDrawExec {

    DrawResult doDrawExec(DrawReq req);

}

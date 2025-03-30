package cn.itedus.lottery.domain.award.service.goods;

import cn.itedus.lottery.domain.award.repository.IAwardRepository;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

@Slf4j
public class DistributionBase {

    @Resource
    private IAwardRepository awardRepository;

    protected void updateUserAwardState(String uId, String orderId, String awardId, Integer awardState, String awardStateInfo) {
        // TODO 后期添加更新分库分表中，用户个人的抽奖记录表中奖品发奖状态
        log.info("TODO 后期添加更新分库分表中，用户个人的抽奖记录表中奖品发奖状态 uId：{}", uId);
    }
}

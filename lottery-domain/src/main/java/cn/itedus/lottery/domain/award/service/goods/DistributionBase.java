package cn.itedus.lottery.domain.award.service.goods;

import cn.itedus.lottery.domain.award.repository.IOrderRepository;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * 奖品发放的基本类，定义了通用的方法
 */
@Slf4j
public class DistributionBase {

    @Resource
    private IOrderRepository awardRepository;

    /**
     * 更新用户奖品状态
     *
     * @param uId        用户ID
     * @param orderId    订单ID
     * @param awardId    奖品ID
     * @param grantState 发放状态
     */
    protected void updateUserAwardState(String uId, Long orderId, String awardId, Integer grantState) {
        awardRepository.updateUserAwardState(uId, orderId, awardId, grantState);
    }
}

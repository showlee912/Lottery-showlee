package cn.itedus.lottery.domain.award.service.goods.impl;

import cn.itedus.lottery.common.Constants;
import cn.itedus.lottery.domain.award.model.req.GoodsReq;
import cn.itedus.lottery.domain.award.model.res.DistributionRes;
import cn.itedus.lottery.domain.award.service.goods.DistributionBase;
import cn.itedus.lottery.domain.award.service.goods.IDistributionGoods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 兑换码类商品
 */
@Component
@Slf4j
public class RedeemCodeGoods extends DistributionBase implements IDistributionGoods {

    @Override
    public DistributionRes doDistribution(GoodsReq req) {

        // 模拟调用兑换码
        log.info("模拟调用兑换码 uId：{} awardContent：{}", req.getUid(), req.getAwardContent());

        // 更新用户领奖结果
        super.updateUserAwardState(req.getUid(), req.getOrderId(), req.getAwardId(), Constants.AwardState.SUCCESS.getCode());

        return new DistributionRes(req.getUid(), Constants.AwardState.SUCCESS.getCode(), Constants.AwardState.SUCCESS.getInfo());
    }

    @Override
    public Integer getDistributionGoodsName() {
        return Constants.AwardType.RedeemCodeGoods.getCode();
    }

}

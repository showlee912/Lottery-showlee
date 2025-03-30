package cn.itedus.lottery.domain.award.service.goods.impl;

import cn.itedus.lottery.common.Constants;
import cn.itedus.lottery.domain.award.model.req.GoodsReq;
import cn.itedus.lottery.domain.award.model.res.DistributionRes;
import cn.itedus.lottery.domain.award.service.goods.DistributionBase;
import cn.itedus.lottery.domain.award.service.goods.IDistributionGoods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * 优惠券发放
 */
@Slf4j
@Component
public class CouponGoods extends DistributionBase implements IDistributionGoods {


    @Override
    public DistributionRes doDistribution(GoodsReq req) {

        // 模拟调用优惠券发放接口
        log.info("模拟调用优惠券发放接口 uId：{} awardContent：{}", req.getUid(), req.getAwardContent());

        // 更新用户领奖结果
        super.updateUserAwardState(req.getUid(), req.getOrderId(), req.getAwardId(), Constants.AwardState.SUCCESS.getCode(), Constants.AwardState.SUCCESS.getInfo());

        return new DistributionRes(req.getUid(), Constants.AwardState.SUCCESS.getCode(), Constants.AwardState.SUCCESS.getInfo());
    }

    /**
     * 查询奖品类型
     *
     * @return 奖品类型
     */
    @Override
    public Integer getDistributionGoodsName() {
        return Constants.AwardType.CouponGoods.getCode();
    }

}

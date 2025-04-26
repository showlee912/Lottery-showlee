package cn.itedus.lottery.application.mq.consumer;

import cn.hutool.core.lang.Assert;
import cn.itedus.lottery.common.Constants;
import cn.itedus.lottery.domain.activity.model.vo.InvoiceVO;
import cn.itedus.lottery.domain.award.model.req.GoodsReq;
import cn.itedus.lottery.domain.award.model.res.DistributionRes;
import cn.itedus.lottery.domain.award.service.factory.DistributionGoodsFactory;
import cn.itedus.lottery.domain.award.service.goods.IDistributionGoods;
import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

@Component
public class LotteryInvoiceListener {

    private Logger logger = LoggerFactory.getLogger(LotteryInvoiceListener.class);

    @Resource
    private DistributionGoodsFactory distributionGoodsFactory;

    // 修改KafkaListener配置，添加更多参数
    @KafkaListener(topics = "lottery_invoice",
            groupId = "lottery",
            containerFactory = "kafkaListenerContainerFactory")
    public void onMessage(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        logger.info("接收到消息开始处理: topic={}, partition={}, offset={}, value={}",
                topic, record.partition(), record.offset(), record.value());

        Optional<?> message = Optional.ofNullable(record.value());

        // 1. 判断消息是否存在
        if (!message.isPresent()) {
            logger.warn("接收到空消息，跳过处理");
            ack.acknowledge();
            return;
        }

        // 2. 处理 MQ 消息
        try {
            // 1. 转化对象
            InvoiceVO invoiceVO = JSON.parseObject((String) message.get(), InvoiceVO.class);
            logger.info("消息解析成功: uId={}, orderId={}, awardId={}",
                    invoiceVO.getUId(), invoiceVO.getOrderId(), invoiceVO.getAwardId());

            // 2. 获取发送奖品工厂，执行发奖
            IDistributionGoods distributionGoodsService = distributionGoodsFactory.getDistributionGoodsService(invoiceVO.getAwardType());
            DistributionRes distributionRes = distributionGoodsService.doDistribution(new GoodsReq(invoiceVO.getUId(),
                    invoiceVO.getOrderId(),
                    invoiceVO.getAwardId(),
                    invoiceVO.getAwardName(),
                    invoiceVO.getAwardContent()));

            Assert.isTrue(Constants.AwardState.SUCCESS.getCode().equals(distributionRes.getCode()), distributionRes.getInfo());

            // 3. 打印日志
            logger.info("消费MQ消息成功，完成 topic：{} bizId：{} 发奖结果：{}", topic, invoiceVO.getUId(), JSON.toJSONString(distributionRes));

            // 4. 消息消费完成
            ack.acknowledge();
        } catch (Exception e) {
            // 发奖环节失败，记录详细错误信息
            logger.error("消费MQ消息失败 topic：{} message：{}, 错误信息: {}", topic, message.get(), e.getMessage(), e);
            throw e;
        }
    }
}
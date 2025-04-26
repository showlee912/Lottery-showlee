package cn.itedus.lottery.application.mq.producer;

import cn.itedus.lottery.domain.activity.model.vo.InvoiceVO;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;

/**
 * Kafka消息发送者，负责将中奖发货单消息发送到指定的Kafka主题。
 */
@Component
public class KafkaProducer {

    private Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Kafka主题：中奖发货单
     */
    public static final String TOPIC_INVOICE = "lottery_invoice";

    /**
     * 发送中奖物品发货单消息到Kafka主题。
     *
     * @param invoice 发货单信息，包含中奖用户的ID、订单ID、奖品ID、奖品名称和奖品内容等。
     * @return 发送结果的ListenableFuture对象，可以用于异步处理发送结果。
     */
    public ListenableFuture<SendResult<String, Object>> sendLotteryInvoice(InvoiceVO invoice) {
        String objJson = JSON.toJSONString(invoice);
        logger.info("发送MQ消息 topic：{} bizId：{} message：{}", TOPIC_INVOICE, invoice.getUId(), objJson);
        // 向主题发送中奖单信息
        return kafkaTemplate.send(TOPIC_INVOICE, objJson);
    }

    

}

package cn.itedus.lottery.application.mq;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 消息消费者
 */
@Component
public class KafkaConsumer {

    private Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    /**
     * 监听并处理 Kafka 主题中的消息。
     * 监听TOPIC_TEST主题，指定为TOPIC_GROUP消费者组
     *
     * @param record 消费者记录，包含从 Kafka 主题中接收到的消息。
     * @param ack    手动确认机制，用于告知 Kafka 消息已被成功处理。
     * @param topic  消息所属的主题名称。
     */
    @KafkaListener(topics = KafkaProducer.TOPIC_TEST, groupId = KafkaProducer.TOPIC_GROUP)
    public void topicTest(ConsumerRecord<?, ?> record, Acknowledgment ack, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional<?> message = Optional.ofNullable(record.value());
        if (message.isPresent()) {//如果消息存在
            Object msg = message.get();
            logger.info("topic_test 消费了： Topic:" + topic + ",Message:" + msg);
            ack.acknowledge();
        }
    }

}

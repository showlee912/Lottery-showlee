package cn.itedus.lottery.test.application;

import cn.itedus.lottery.application.process.IActivityProcess;
import cn.itedus.lottery.application.process.req.DrawProcessReq;
import cn.itedus.lottery.application.process.res.DrawProcessResult;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityProcessTest {

    private Logger logger = LoggerFactory.getLogger(ActivityProcessTest.class);

    @Resource
    private IActivityProcess activityProcess;

    // 在测试类中添加等待逻辑
    @Test
    public void test_doDrawProcess() throws InterruptedException {
        DrawProcessReq req = new DrawProcessReq();
        req.setUId("fustack");
        req.setActivityId(100001L);

        DrawProcessResult drawProcessResult = activityProcess.doDrawProcess(req);

        logger.info("请求入参：{}", JSON.toJSONString(req));
        logger.info("测试结果：{}", JSON.toJSONString(drawProcessResult));

        // 添加等待时间，确保消费者有足够时间处理消息
        logger.info("等待Kafka消费者处理消息...");
        Thread.sleep(5000);
    }
}

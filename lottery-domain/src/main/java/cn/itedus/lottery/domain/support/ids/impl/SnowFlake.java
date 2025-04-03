package cn.itedus.lottery.domain.support.ids.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import cn.itedus.lottery.domain.support.ids.IIdGenerator;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SnowFlake implements IIdGenerator {

    private Snowflake snowflake;

    @PostConstruct
    public void init() { // 0 ~ 31 位，可以采用配置的方式使用
        long workerId;
        try {
            // 尝试通过本机IP生成workerId
            workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
        } catch (Exception e) {
            // 异常时使用主机名哈希值
            workerId = NetUtil.getLocalhostStr().hashCode();
        }

        // 位运算确保workerId在0-31范围内（5位最大值）
        workerId = workerId >> 16 & 31;

        long dataCenterId = 1L; // 固定数据中心ID
        // 创建hutool的雪花算法实例
        snowflake = IdUtil.getSnowflake(workerId, dataCenterId);

    }


    /**
     * 获取ID，目前有两种实现方式
     * 1. 雪花算法，用于生成单号
     * 2. 日期算法，用于生成活动编号类，特性是生成数字串较短，但指定时间内不能生成太多
     * 3. 随机算法，用于生成策略ID
     *
     * @return ID
     */
    @Override
    public long nextId() {
        return snowflake.nextId();
    }
}

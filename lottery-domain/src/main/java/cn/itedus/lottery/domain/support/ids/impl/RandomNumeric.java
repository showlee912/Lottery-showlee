package cn.itedus.lottery.domain.support.ids.impl;

import cn.itedus.lottery.domain.support.ids.IIdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

/**工具类生成 org.apache.commons.lang3.RandomStringUtils*/
@Component
public class RandomNumeric implements IIdGenerator {
    /**
     * 获取ID，目前有两种实现方式 1. 雪花算法，用于生成单号 2. 日期算法，用于生成活动编号类，特性是生成数字串较短，但指定时间内不能生成太多 3. 随机算法，用于生成策略ID
     *
     * @return ID
     */
    @Override
    public long nextId() {
        return Long.parseLong(RandomStringUtils.randomNumeric(11));
    }
}

package cn.itedus.lottery.domain.support.ids;

import cn.itedus.lottery.common.Constants;
import cn.itedus.lottery.domain.support.ids.impl.RandomNumeric;
import cn.itedus.lottery.domain.support.ids.impl.ShortCode;
import cn.itedus.lottery.domain.support.ids.impl.SnowFlake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * ID生成策略上下文配置类
 * <p>
 * 核心功能：统一管理所有ID生成策略，通过Spring容器提供ID生成器的按需获取 设计模式：采用策略模式，支持多种ID生成算法灵活切换
 */
@Configuration  // 标识为Spring配置类
public class IdContext {

    /**
     * 创建ID生成器策略集合
     *
     * @param snowFlake     雪花算法ID生成器（适用于分布式场景）
     * @param shortCode     短码生成器（生成短链接接ID）
     * @param randomNumeric 随机数生成器（生成高随机性数字ID）
     * @return ID生成器映射表，Key为策略枚举，Value为具体实现
     * <p>
     * 实现逻辑： 1. 通过Spring自动注入所有实现了IIdGenerator接口的Bean 2. 使用策略模式将不同生成器注册到统一容器 3. 对外提供通过枚举值获取对应生成器的方式
     */
    @Bean
    public Map<Constants.Ids, IIdGenerator> idGenerator(SnowFlake snowFlake, ShortCode shortCode,
                                                        RandomNumeric randomNumeric) {
        // 初始化策略容器（初始容量8考虑到后续可能的扩展）
        Map<Constants.Ids, IIdGenerator> idGeneratorMap = new HashMap<>(8);

        // 注册三种ID生成器实现
        idGeneratorMap.put(Constants.Ids.SnowFlake, snowFlake);       // 分布式ID（默认使用）
        idGeneratorMap.put(Constants.Ids.ShortCode, shortCode);       // 活动ID（短链优化）
        idGeneratorMap.put(Constants.Ids.RandomNumeric, randomNumeric);// 奖品ID（随机性要求高）

        return idGeneratorMap;
    }

}

package cn.itedus.lottery.domain.strategy.service.draw;

import cn.itedus.lottery.domain.strategy.service.algorithm.IDrawAlgorithm;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//抽奖算法配置类（核心工厂配置）
public class DrawConfig {

    // 总体概率抽奖算法（奖品总概率等于1）
    @Resource
    private IDrawAlgorithm defaultRateRandomDrawAlgorithm;

    // 单项概率抽奖算法（奖品固定概率且每个奖品概率独立）
    @Resource
    private IDrawAlgorithm singleRateRandomDrawAlgorithm;

    /**
     * 算法映射容器（静态缓存）
     * <p>
     * key: 策略模式代码（1-总体概率 2-单项概率）
     * <p>
     * value: 对应的抽奖算法实现
     */
    protected static Map<Integer, IDrawAlgorithm> drawAlgorithmMap = new ConcurrentHashMap<>();

    /**
     * 将不同策略模式对应的算法实现注册到容器中
     */
    @PostConstruct //Bean初始化后执行注册（Spring生命周期回调）
    public void init() {
        // 注册总体概率算法（策略模式1）
        drawAlgorithmMap.put(1, defaultRateRandomDrawAlgorithm);
        // 注册单项概率算法（策略模式2）
        drawAlgorithmMap.put(2, singleRateRandomDrawAlgorithm);
    }
}


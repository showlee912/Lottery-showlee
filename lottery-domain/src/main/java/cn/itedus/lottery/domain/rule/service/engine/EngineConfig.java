package cn.itedus.lottery.domain.rule.service.engine;

import cn.itedus.lottery.domain.rule.service.logic.LogicFilter;
import cn.itedus.lottery.domain.rule.service.logic.impl.UserAgeFilter;
import cn.itedus.lottery.domain.rule.service.logic.impl.UserGenderFilter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 规则配置类，存储所有的逻辑过滤器映射
 */
public class EngineConfig {

    /** 存储全部的过滤器映射 */
    protected static Map<String, LogicFilter> logicFilterMap = new ConcurrentHashMap<>();

    @Resource
    private UserAgeFilter userAgeFilter;
    @Resource
    private UserGenderFilter userGenderFilter;

    @PostConstruct
    public void init() {
        logicFilterMap.put("userAge", userAgeFilter);
        logicFilterMap.put("userGender", userGenderFilter);
    }

}

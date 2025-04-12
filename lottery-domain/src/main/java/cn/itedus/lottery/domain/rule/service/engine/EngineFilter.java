package cn.itedus.lottery.domain.rule.service.engine;

import cn.itedus.lottery.domain.rule.model.res.EngineResult;
import cn.itedus.lottery.domain.rule.model.req.DecisionMatterReq;

/**
 * 规则过滤器引擎接口
 */
public interface EngineFilter {

    /**
     * 处理规则引擎
     *
     * @param matter 决策请求数据，包含进行规则判断所需的各项参数
     * @return 决策结果
     */
    EngineResult process(final DecisionMatterReq matter);

}

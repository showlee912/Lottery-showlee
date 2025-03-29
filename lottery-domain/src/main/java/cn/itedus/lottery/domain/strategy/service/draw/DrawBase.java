package cn.itedus.lottery.domain.strategy.service.draw;

import cn.itedus.lottery.domain.strategy.model.vo.AwardRateInfo;
import cn.itedus.lottery.domain.strategy.service.algorithm.IDrawAlgorithm;
import cn.itedus.lottery.infrastructure.po.StrategyDetail;

import java.util.ArrayList;
import java.util.List;

public class DrawBase extends DrawConfig {

    /**
     * 校验并初始化概率元组数据，核心逻辑：
     * <p>
     * 1. 仅对策略模式为1（权重策略）的配置进行初始化
     * <p>
     * 2. 避免重复初始化已有策略的概率元组
     * <p>
     * 3. 将数据库配置的奖品概率数据转换为算法需要的格式
     *
     * @param strategyId 策略ID
     * @param strategyMode 策略模式（1: 单项概率 | 2: 总体概率）
     * @param strategyDetailList 策略详细配置列表（包含奖品概率数据）
     */
    public void checkAndInitRateData(Long strategyId, Integer strategyMode, List<StrategyDetail> strategyDetailList) {
        // 仅处理单项概率策略（策略模式=1）
        if (1 != strategyMode) return;

        // 根据策略模式获取对应的抽奖算法（从父类继承的算法集合）
        IDrawAlgorithm drawAlgorithm = drawAlgorithmMap.get(strategyMode);

        // 检查是否已经初始化过该策略的概率元组
        boolean existRateTuple = drawAlgorithm.isExistRateTuple(strategyId);
        if (existRateTuple) return;

        // 转换数据结构：将策略详情列表转换为算法所需的概率元组格式
        List<AwardRateInfo> awardRateInfoList = new ArrayList<>(strategyDetailList.size());
        for (StrategyDetail strategyDetail : strategyDetailList) {
            // 构建包含奖品ID和概率值的元组对象
            awardRateInfoList.add(new AwardRateInfo(
                    strategyDetail.getAwardId(),
                    strategyDetail.getAwardRate()
            ));
        }

        // 调用算法进行概率元组初始化（后续抽奖计算的基础数据）
        drawAlgorithm.initRateTuple(strategyId, awardRateInfoList);
    }
}


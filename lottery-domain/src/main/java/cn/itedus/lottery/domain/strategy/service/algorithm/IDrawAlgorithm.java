package cn.itedus.lottery.domain.strategy.service.algorithm;

import cn.itedus.lottery.domain.strategy.model.vo.AwardRateInfo;

import java.util.List;

/**
 * 抽奖算法接口
 */
public interface IDrawAlgorithm {

    /**
     * 程序启动时初始化概率元祖，在初始化完成后使用过程中不允许修改元祖数据
     * @param strategyId        策略ID
     * @param awardRateInfoList 奖品概率配置集合，值示例：AwardRateInfo.awardRate = 0.04
     */
    void initRateTuple(Long strategyId, List<AwardRateInfo> awardRateInfoList);

    /**
     * 判断是否已经，做了数据初始化
     * @param strategyId 策略Id
     * @return true：已初始化，false：未初始化
     */
    boolean isExistRateTuple(Long strategyId);

    /**
     * 执行必中奖的随机抽奖算法
     *
     * @param strategyId        策略ID
     * @param excludeAwardIds   排除的奖品ID列表（已中奖/不可参与抽奖的奖品）
     * @return                  中奖的奖品ID，返回空字符串则无奖品
     *
     */
    String randomDraw(Long strategyId, List<String> excludeAwardIds);

}

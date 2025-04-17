package cn.itedus.lottery.domain.strategy.service.algorithm;

import cn.itedus.lottery.domain.strategy.model.vo.AwardRateVO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 共用的算法逻辑
 */
public abstract class BaseAlgorithm implements IDrawAlgorithm {

    // 数组初始化长度
    private final int RATE_TUPLE_LENGTH = 128;

    /**
     * 存放概率与奖品对应的散列结果，策略ID -> 概率元组
     * rateTupleMap内部结构示例：
     * {
     * 1001L: [ // 策略ID为1001的概率元组数组
     * 0: "A001", // 索引0处存储的奖品ID
     * 1: "C003", // 索引1处存储的奖品ID
     * 2: "B002", // 索引2处存储的奖品ID
     * ... //
     * 127: "A001" // 索引127处存储的奖品ID
     * ],
     *
     * 1002L: [ // 策略ID为1002的概率元组数组
     * 0: "D004",
     * 1: "E005",
     * ...
     * 127: "D004"
     * ]
     * }
     */
    protected Map<Long, String[]> rateTupleMap = new ConcurrentHashMap<>();

    /**
     * 奖品区间概率值，策略ID -> [awardId->begin、awardId->end]
     * awardRateInfoMap内部结构示例：
     * {
     * 1001L: [ // 策略ID为1001的奖品概率列表
     * AwardRateInfo{awardId="A001", awardRate=0.3}, // 奖品A占比30%
     * AwardRateInfo{awardId="B002", awardRate=0.2}, // 奖品B占比20%
     * AwardRateInfo{awardId="C003", awardRate=0.5} // 奖品C占比50%
     * ],
     * 
     * 1002L: [ // 策略ID为1002的奖品概率列表
     * AwardRateInfo{awardId="D004", awardRate=0.4}, // 奖品D占比40%
     * AwardRateInfo{awardId="E005", awardRate=0.6} // 奖品E占比60%
     * ]
     * }
     */
    protected Map<Long, List<AwardRateVO>> awardRateInfoMap = new ConcurrentHashMap<>();

    /**
     * 初始化概率元组
     * 1. 将奖项概率按百分比转换为整数值范围
     * 2. 根据概率分布填充散列数组，每个奖项对应的区间段
     * 3. 如果不存在当前策略ID
     * 3. 算法复杂度为 O(n) ，n为奖项数量
     *
     * @param strategyId        策略ID
     * @param awardRateVOList 奖品概率配置列表（包含奖品ID和概率）
     */
    @Override
    public void initRateTuple(Long strategyId, List<AwardRateVO> awardRateVOList) {
        // 保存奖品概率信息到 awardRateInfoMap 中，键为策略ID，值为奖品概率配置列表
        awardRateInfoMap.put(strategyId, awardRateVOList);

        // 如果 rateTupleMap 中不存在当前策略ID，则新增一个长度为 RATE_TUPLE_LENGTH 的数组并添加进 rateTupleMap
        String[] rateTuple = rateTupleMap.computeIfAbsent(strategyId, k -> new String[RATE_TUPLE_LENGTH]);

        // 初始化游标值
        int cursor = 0;

        // 遍历奖品概率配置列表
        for (AwardRateVO awardRateVO : awardRateVOList) {
            // 将奖品概率（小数）乘以100，转换为整数值范围
            int rateVal = awardRateVO.getAwardRate().multiply(new BigDecimal(100)).intValue();

            // 循环填充概率范围值，将奖品ID填充到 rateTuple 数组的特定索引位置
            for (int i = cursor + 1; i <= (rateVal + cursor); i++) {
                // 计算哈希索引，并将奖品ID填充到对应位置
                rateTuple[hashIdx(i)] = awardRateVO.getAwardId();
            }

            // 更新游标值，以便下一个奖品的概率范围能够正确填充
            cursor += rateVal;
        }
    }

    /**
     * 检查指定策略是否已经初始化概率元组
     *
     * @param strategyId 策略ID
     * @return true 表示已初始化，false 表示未初始化
     */
    @Override
    public boolean isExistRateTuple(Long strategyId) {
        return rateTupleMap.containsKey(strategyId);
    }

    /**
     * 斐波那契（Fibonacci）散列法，计算哈希索引下标值
     *
     * @param val 值
     * @return 索引
     */
    protected int hashIdx(int val) {
        // 斐波那契散列增量，逻辑：黄金分割点：(√5 - 1) / 2 = 0.6180339887，Math.pow(2, 32) * 0.6180339887
        // = 0x61c88647
        int HASH_INCREMENT = 0x61c88647;
        int hashCode = val * HASH_INCREMENT + HASH_INCREMENT;
        return hashCode & (RATE_TUPLE_LENGTH - 1);
    }

}

package cn.itedus.lottery.domain.strategy.service.algorithm.impl;

import cn.itedus.lottery.domain.strategy.model.vo.AwardRateInfo;
import cn.itedus.lottery.domain.strategy.service.algorithm.BaseAlgorithm;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**

 * 必中奖策略，排掉已经中奖的概率后重新计算中奖范围
 */
@Component("defaultRateRandomDrawAlgorithm")
public class DefaultRateRandomDrawAlgorithm extends BaseAlgorithm {

    /**
     * 执行必中奖的随机抽奖算法
     *
     * @param strategyId        策略ID
     * @param excludeAwardIds   排除的奖品ID列表（已中奖/不可参与抽奖的奖品）
     * @return                  中奖的奖品ID，返回空字符串则无奖品
     *
     */
    @Override
    public String randomDraw(Long strategyId, List<String> excludeAwardIds) {
        // 阶段1：构建有效奖品集合
        // -----------------------------------------------------------
        BigDecimal differenceDenominator = BigDecimal.ZERO;
        List<AwardRateInfo> differenceAwardRateList = new ArrayList<>();

        // 遍历策略下所有奖品配置
        List<AwardRateInfo> awardRateIntervalValList = awardRateInfoMap.get(strategyId);
        for (AwardRateInfo awardRateInfo : awardRateIntervalValList) {
            String awardId = awardRateInfo.getAwardId();
            // 过滤黑名单中的奖品（已中奖/不可参与抽奖的奖品）
            if (excludeAwardIds.contains(awardId)) continue;

            // 累计有效奖品总概率（用于后续概率重新分配）
            differenceAwardRateList.add(awardRateInfo);
            differenceDenominator = differenceDenominator.add(awardRateInfo.getAwardRate());
        }

        // 阶段2：边界情况处理
        // -----------------------------------------------------------
        if (differenceAwardRateList.isEmpty()) return "";  // 无有效奖品时返回空
        if (differenceAwardRateList.size() == 1) {         // 单个有效奖品直接返回
            return differenceAwardRateList.get(0).getAwardId();
        }

        // 阶段3：重新计算概率区间
        // -----------------------------------------------------------
        SecureRandom secureRandom = new SecureRandom();
        int randomVal = secureRandom.nextInt(100) + 1;  // 生成1-100的随机数（包含边界）

        // 阶段4：概率区间匹配
        String awardId = "";
        int cursorVal = 0;  // 概率区间游标
        for (AwardRateInfo awardRateInfo : differenceAwardRateList) {
            // 计算当前奖品在剩余概率中的实际占比（百分比取整）
            int rateVal = awardRateInfo.getAwardRate()
                    .divide(differenceDenominator, 2, BigDecimal.ROUND_UP)  // 保留2位小数（向上取整）
                    .multiply(new BigDecimal(100))
                    .intValue();

            // 判断随机数是否落在当前区间
            if (randomVal <= (cursorVal + rateVal)) {
                awardId = awardRateInfo.getAwardId();
                break;
            }
            cursorVal += rateVal;  // 移动概率游标
        }

        return awardId;
    }

}

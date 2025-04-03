package cn.itedus.lottery.test;

import cn.itedus.lottery.infrastructure.dao.IActivityDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.security.SecureRandom;
import java.util.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    private Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Resource
    IActivityDao activityDao;

    /**
     * 测试奖项策略配置的随机概率分布
     */
    @Test
    public void test_strategy() {
        SecureRandom random = new SecureRandom();
        int rate = random.nextInt(100);

        System.out.println("概率：" + rate);

        List<Map<String, String>> strategyList = new ArrayList<>();

        strategyList.add(new HashMap<String, String>() {{
            put("awardDesc", "一等奖：彩电");
            put("awardId", "10001");
            put("awardCount", "3");
            put("awardRate", "0.2");
        }});

        strategyList.add(new HashMap<String, String>() {{
            put("awardDesc", "二等奖：冰箱");
            put("awardId", "10002");
            put("awardCount", "5");
            put("awardRate", "0.3");
        }});

        strategyList.add(new HashMap<String, String>() {{
            put("awardDesc", "三等奖：洗衣机");
            put("awardId", "10003");
            put("awardCount", "10");
            put("awardRate", "0.5");
        }});


    }


    /**
     * 测试斐波那契散列算法在128槽位的分布情况
     */
    @Test
    public void test_idx() {
        // 创建用于统计槽位分布的哈希表（key: 槽位索引，value: 出现次数）
        Map<Integer, Integer> map = new HashMap<>();

        // 使用斐波那契散列魔数（0x61c88647）
        int HASH_INCREMENT = 0x61c88647;
        int hashCode = 0;

        // 对1-100的数字进行散列测试
        for (int i = 1; i <= 100; i++) {
            // 斐波那契散列计算公式
            hashCode = i * HASH_INCREMENT + HASH_INCREMENT;
            // 计算128槽位索引（等价于 hashCode % 128）
            int idx = hashCode & (128 - 1);

            // 统计各槽位的命中次数
            map.merge(idx, 1, Integer::sum);

            // 对比普通哈希算法在128槽位的分布
            System.out.println("斐波那契散列：" + idx + " 普通散列：" + (String.valueOf(i).hashCode() & (128 - 1)));
        }

        // 输出最终分布统计结果
        System.out.println(map);
    }


    /**
     * 测试完整抽奖流程，验证抽奖策略的初始化与随机抽奖结果
     */
    @Test
    public void test_DrawStrategy() {

        List<Map<String, String>> strategyList = new ArrayList<>();

        strategyList.add(new HashMap<String, String>() {{
            put("awardDesc", "一等奖：彩电");
            put("awardId", "10001");
            put("awardCount", "3");
            put("awardRate", "20");
        }});

        strategyList.add(new HashMap<String, String>() {{
            put("awardDesc", "二等奖：冰箱");
            put("awardId", "10002");
            put("awardCount", "5");
            put("awardRate", "30");
        }});

        strategyList.add(new HashMap<String, String>() {{
            put("awardDesc", "三等奖：洗衣机");
            put("awardId", "10003");
            put("awardCount", "10");
            put("awardRate", "50");
        }});

        DrawStrategy drawStrategy = new DrawStrategy();
        drawStrategy.initRateTuple(strategyList);

        for (int i = 0; i < 20; i++) {
            System.out.println("中奖结果：" + drawStrategy.randomDraw());
        }

    }

    /**
     * 验证安全随机数生成器在边界条件下的行为
     */
    @Test
    public void test_random() {
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < 20; i++) {
            System.out.println(random.nextInt(1));
        }
    }
}

 class DrawStrategy {
    // 黄金分割点散列因子
    private final int HASH_INCREMENT = 0x61c88647;
    // 概率分布数组
    private String[] rateTuple = new String[128];
    // 共享的随机数生成器
    private final SecureRandom random = new SecureRandom();
    // 记录已填充的概率总和
    private int probabilitySum = 0;

    /**
     * 初始化概率分布映射表
     * <p>
     * 实现逻辑：
     * 1. 清空现有概率分布数组
     * 2. 校验配置数据合法性（总概率≤100%）
     * 3. 使用斐波那契散列算法将奖项分布到128槽位
     *
     * @param drawConfig 抽奖配置列表，每个配置项应包含：
     *                   awardDesc - 奖项描述（String）
     *                   awardRate - 奖项概率（0-100的整数，代表百分比）
     * @throws IllegalArgumentException 当总概率超过100%时抛出
     */
    public void initRateTuple(List<Map<String, String>> drawConfig) {
        // 1. 清空数组
        Arrays.fill(rateTuple, null);

        // 2. 预解析配置并计算总概率
        int totalProbability = 0;
        List<Award> awards = new ArrayList<>();
        for (Map<String, String> config : drawConfig) {
            try {
                String desc = config.get("awardDesc");
                int rate = Integer.parseInt(config.get("awardRate"));
                awards.add(new Award(desc, rate));
                totalProbability += rate;
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("无效的概率值: " + config.get("awardRate"));
            }
        }

        if (totalProbability > 100) {
            throw new IllegalArgumentException("概率总和不能超过100%");
        }
        probabilitySum = totalProbability;

        // 3. 分配槽位
        int cumulativeProbability = 0;
        for (Award award : awards) {
            int rateVal = award.getRate();
            for (int i = cumulativeProbability + 1; i <= cumulativeProbability + rateVal; i++) {
                int hashCode = (i + 1) * HASH_INCREMENT;
                int idx = hashCode & (rateTuple.length - 1);

                if (rateTuple[idx] != null) {
                    System.out.println("警告：散列冲突 index=" + idx
                            + ", 当前奖项=" + award.getDesc()
                            + ", 已存在奖项=" + rateTuple[idx]);
                }
                rateTuple[idx] = award.getDesc();
            }
            cumulativeProbability += rateVal;
        }
    }


    private static class Award {
        private final String desc;
        private final int rate;

        public Award(String desc, int rate) {
            this.desc = desc;
            this.rate = rate;
        }

        public String getDesc() { return desc; }
        public int getRate() { return rate; }
    }

    /**
     * 随机抽奖
     *
     * @return 中奖结果，如果概率未覆盖到100%，可能返回null表示未中奖
     */
    public String randomDraw() {
        // 生成1-100的随机数
        int rate = random.nextInt(100) + 1;

        // 如果随机数超出了概率范围，则视为未中奖
        if (rate > probabilitySum) {
            return "未中奖";
        }

        // 计算索引位置
        int hashCode = rate * HASH_INCREMENT + HASH_INCREMENT;
        int idx = hashCode & (rateTuple.length - 1);

        return rateTuple[idx] != null ? rateTuple[idx] : "未中奖";
    }

    /**
     * 验证概率分布
     *
     * @param testTimes 测试次数
     * @return 各奖项的实际概率分布
     */
    public Map<String, Double> verifyProbability(int testTimes) {
        Map<String, Integer> countMap = new HashMap<>();

        for (int i = 0; i < testTimes; i++) {
            String result = randomDraw();
            countMap.merge(result, 1, Integer::sum);
        }

        // 计算实际概率
        Map<String, Double> probabilityMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            double probability = (double) entry.getValue() / testTimes * 100;
            probabilityMap.put(entry.getKey(), probability);
        }
        return probabilityMap;
    }
}



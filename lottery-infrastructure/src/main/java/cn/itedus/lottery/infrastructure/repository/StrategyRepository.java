package cn.itedus.lottery.infrastructure.repository;

import cn.itedus.lottery.domain.strategy.model.aggregates.StrategyRich;
import cn.itedus.lottery.domain.strategy.model.vo.AwardBriefVO;
import cn.itedus.lottery.domain.strategy.model.vo.StrategyBriefVO;
import cn.itedus.lottery.domain.strategy.model.vo.StrategyDetailBriefVO;
import cn.itedus.lottery.domain.strategy.repository.IStrategyRepository;
import cn.itedus.lottery.infrastructure.dao.IAwardDao;
import cn.itedus.lottery.infrastructure.dao.IStrategyDao;
import cn.itedus.lottery.infrastructure.dao.IStrategyDetailDao;
import cn.itedus.lottery.infrastructure.po.Award;
import cn.itedus.lottery.infrastructure.po.Strategy;
import cn.itedus.lottery.infrastructure.po.StrategyDetail;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
核心数据访问实现类，从DAO层将所需的多种数据聚合到聚合对象中
 */
@Component
public class StrategyRepository implements IStrategyRepository {

    @Resource
    private IStrategyDao strategyDao;

    @Resource
    private IStrategyDetailDao strategyDetailDao;

    @Resource
    private IAwardDao awardDao;

    /**
     * 查询策略聚合信息
     * @param strategyId 策略ID
     * @return 包含策略和策略详情的聚合对象
     */
    @Override
    public StrategyRich queryStrategyRich(Long strategyId) {

        //得到数据库中策略详细配置
        Strategy strategy = strategyDao.queryStrategy(strategyId);
        List<StrategyDetail> strategyDetailList = strategyDetailDao.queryStrategyDetailList(strategyId);

        // 转换为需要的VO对象
        StrategyBriefVO strategyBriefVO = new StrategyBriefVO();
        BeanUtils.copyProperties(strategy, strategyBriefVO);

        // 使用Stream API处理strategyDetail的VO集合转化
        List<StrategyDetailBriefVO> strategyDetailBriefVOList = strategyDetailList.stream()
                .map(detail -> {
                    StrategyDetailBriefVO vo = new StrategyDetailBriefVO();
                    BeanUtils.copyProperties(detail, vo);
                    return vo;
                })
                .collect(Collectors.toList());

        return new StrategyRich(strategyId, strategyBriefVO, strategyDetailBriefVOList);
    }

    /**
     * 查询奖品详细信息
     * @param awardId 奖品ID
     * @return 奖品数据对象
     */
    @Override
    public AwardBriefVO queryAwardInfo(String awardId) {
        Award award = awardDao.queryAwardInfo(awardId);

        // 如果性能不是关键考量，可以简化为：
        AwardBriefVO awardBriefVO = new AwardBriefVO();
        BeanUtils.copyProperties(award, awardBriefVO);
        return awardBriefVO;
    }

    /**
     * 查询无库存的奖品ID集合
     *
     * @param strategyId 策略ID
     * @return 无库存的奖品ID列表（不可参与抽奖）
     */
    @Override
    public List<String> queryNoStockStrategyAwardList(Long strategyId) {
        return strategyDetailDao.queryNoStockStrategyAwardList(strategyId);
    }

    /**
     * 扣减库存
     *
     * @param strategyId 策略ID
     * @param awardId    奖品ID
     * @return 扣减结果
     */
    @Override
    public boolean deductStock(Long strategyId, String awardId) {
        StrategyDetail strategyDetail = new StrategyDetail();
        strategyDetail.setStrategyId(strategyId);
        strategyDetail.setAwardId(awardId);
        int count = strategyDetailDao.deductStock(strategyDetail);
        return count == 1;
    }

}

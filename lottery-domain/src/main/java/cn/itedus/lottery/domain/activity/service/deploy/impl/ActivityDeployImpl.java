package cn.itedus.lottery.domain.activity.service.deploy.impl;

import cn.itedus.lottery.domain.activity.model.aggregates.ActivityConfigRich;
import cn.itedus.lottery.domain.activity.model.req.ActivityConfigReq;
import cn.itedus.lottery.domain.activity.model.vo.ActivityVO;
import cn.itedus.lottery.domain.activity.model.vo.AwardVO;
import cn.itedus.lottery.domain.activity.model.vo.StrategyDetailVO;
import cn.itedus.lottery.domain.activity.model.vo.StrategyVO;
import cn.itedus.lottery.domain.activity.repository.IActivityRepository;
import cn.itedus.lottery.domain.activity.service.deploy.IActivityDeploy;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class ActivityDeployImpl implements IActivityDeploy {

    @Resource
    private IActivityRepository activityRepository;

    /**
     * 创建活动信息
     *
     * @param req 活动配置信息
     */
    @Override
    public void createActivity(ActivityConfigReq req) {
        ActivityConfigRich activityConfigRich = req.getActivityConfigRich();
        try {
            // 添加活动配置
            ActivityVO activity = activityConfigRich.getActivity();
            activityRepository.addActivity(activity);

            // 添加奖品配置
            List<AwardVO> awardList = activityConfigRich.getAwardList();
            activityRepository.addAward(awardList);

            // 添加策略配置
            StrategyVO strategy = activityConfigRich.getStrategy();
            activityRepository.addStrategy(strategy);

            // 添加策略明细配置
            List<StrategyDetailVO> strategyDetailList = activityConfigRich.getStrategy().getStrategyDetailList();
            activityRepository.addStrategyDetailList(strategyDetailList);

            log.info("创建活动配置完成，activityId：{}", req.getActivityId());
        } catch (DuplicateKeyException e) {
            log.error("创建活动配置失败，唯一索引冲突 activityId：{} reqJson：{}", req.getActivityId(), JSON.toJSONString(req), e);
            throw e;
        }
    }

    /**
     * 修改活动信息
     *
     * @param req 活动配置信息
     */
    @Override
    public void updateActivity(ActivityConfigReq req) {
        // TODO: 非核心功能后续补充
    }

    /**
     * 扫描待处理的活动列表，状态为：通过、活动中
     * <p>
     * 通过 -> 时间符合时 -> 活动中 活动中 -> 时间到期时 -> 关闭
     *
     * @param id ID
     * @return 待处理的活动集合
     */
    @Override
    public List<ActivityVO> scanToDoActivityList(Long id) {
        return activityRepository.scanToDoActivityList(id);
    }
}

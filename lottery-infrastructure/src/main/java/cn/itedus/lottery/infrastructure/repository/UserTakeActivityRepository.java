package cn.itedus.lottery.infrastructure.repository;

import cn.itedus.lottery.common.Constants;
import cn.itedus.lottery.domain.activity.model.vo.DrawOrderVO;
import cn.itedus.lottery.domain.activity.model.vo.UserTakeActivityVO;
import cn.itedus.lottery.domain.activity.repository.IUserTakeActivityRepository;
import cn.itedus.lottery.infrastructure.dao.IUserStrategyExportDao;
import cn.itedus.lottery.infrastructure.dao.IUserTakeActivityCountDao;
import cn.itedus.lottery.infrastructure.dao.IUserTakeActivityDao;
import cn.itedus.lottery.infrastructure.po.UserStrategyExport;
import cn.itedus.lottery.infrastructure.po.UserTakeActivity;
import cn.itedus.lottery.infrastructure.po.UserTakeActivityCount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 用户参与活动仓储
 */
@Component
public class UserTakeActivityRepository implements IUserTakeActivityRepository {

    @Resource
    private IUserTakeActivityCountDao userTakeActivityCountDao;

    @Resource
    private IUserTakeActivityDao userTakeActivityDao;

    @Resource
    private IUserStrategyExportDao userStrategyExportDao;


    /**
     * 扣减活动库存
     *
     * @param activityId        活动ID
     * @param activityName      活动名称
     * @param takeCount         活动个人可领取次数
     * @param userTakeLeftCount 用户当前剩余的参与次数。（如果为 null，说明用户尚未参与该活动。）
     * @param uId               用户ID
     * @param partakeDate       参与时间
     * @return 更新的记录数
     */
    @Override
    public int subtractionLeftCount(Long activityId, String activityName, Integer takeCount,
                                    Integer userTakeLeftCount, String uId, Date partakeDate) {
        //之前未参加过活动
        if (null == userTakeLeftCount) {
            UserTakeActivityCount userTakeActivityCount = new UserTakeActivityCount();
            userTakeActivityCount.setUId(uId);
            userTakeActivityCount.setActivityId(activityId);
            userTakeActivityCount.setTotalCount(takeCount);
            userTakeActivityCount.setLeftCount(takeCount - 1);
            userTakeActivityCountDao.insert(userTakeActivityCount);
            return 1;
        } else {
            //之前参加过活动
            UserTakeActivityCount userTakeActivityCount = new UserTakeActivityCount();
            userTakeActivityCount.setUId(uId);
            userTakeActivityCount.setActivityId(activityId);
            return userTakeActivityCountDao.updateLeftCount(userTakeActivityCount);
        }
    }

    /**
     * 领取活动
     *
     * @param activityId        活动ID
     * @param activityName      活动名称
     * @param takeCount         活动个人可领取次数
     * @param userTakeLeftCount 用户已领取次数
     * @param uId               用户ID
     * @param takeDate          领取时间
     * @param takeId            领取ID
     */
    @Override
    public void takeActivity(Long activityId, String activityName, Long strategyId, Integer takeCount,
                             Integer userTakeLeftCount, String uId, Date takeDate, Long takeId) {
        UserTakeActivity userTakeActivity = new UserTakeActivity();
        userTakeActivity.setUId(uId);
        userTakeActivity.setTakeId(takeId);
        userTakeActivity.setActivityId(activityId);
        userTakeActivity.setActivityName(activityName);
        userTakeActivity.setTakeDate(takeDate);
        if (null == userTakeLeftCount) {
            userTakeActivity.setTakeCount(1);
        } else {
            userTakeActivity.setTakeCount(takeCount - userTakeLeftCount + 1);
        }
        userTakeActivity.setStrategyId(strategyId);
        userTakeActivity.setState(Constants.TaskState.NO_USED.getCode());
        String uuid = uId + "_" + activityId + "_" + userTakeActivity.getTakeCount();
        userTakeActivity.setUuid(uuid);

        userTakeActivityDao.insert(userTakeActivity);
    }

    /**
     * 查询是否存在未执行抽奖领取活动单【state = 0】
     *
     * @param activityId 活动ID
     * @param uId        用户ID
     * @return 领取单
     */
    @Override
    public UserTakeActivityVO queryNoConsumedTakeActivityOrder(Long activityId, String uId) {

        UserTakeActivity queryCondition = new UserTakeActivity();
        queryCondition.setUId(uId);
        queryCondition.setActivityId(activityId);

        // 查询未消费的领取单
        UserTakeActivity noConsumedTakeActivityOrder = userTakeActivityDao.queryNoConsumedTakeActivityOrder(queryCondition);

        // 如果未查询到符合条件的记录，直接返回 null
        if (noConsumedTakeActivityOrder == null) {
            return null;
        }

        UserTakeActivityVO userTakeActivityVO = new UserTakeActivityVO();
        BeanUtils.copyProperties(noConsumedTakeActivityOrder, userTakeActivityVO);
        return userTakeActivityVO;
    }

    /**
     * 锁定活动领取记录
     *
     * @param uId        用户ID
     * @param activityId 活动ID
     * @param takeId     领取ID
     * @return 更新结果
     */
    @Override
    public int lockTakeActivity(String uId, Long activityId, Long takeId) {
        UserTakeActivity userTakeActivity = new UserTakeActivity();
        userTakeActivity.setUId(uId);
        userTakeActivity.setActivityId(activityId);
        userTakeActivity.setTakeId(takeId);
        return userTakeActivityDao.lockTakeActivity(userTakeActivity);
    }

    /**
     * 保存抽奖信息
     *
     * @param drawOrder 中奖单
     */
    @Override
    public void saveUserStrategyExport(DrawOrderVO drawOrder) {
        UserStrategyExport userStrategyExport = new UserStrategyExport();
        BeanUtils.copyProperties(drawOrder,userStrategyExport);
        userStrategyExport.setUuid(String.valueOf(drawOrder.getOrderId()));

        userStrategyExportDao.insert(userStrategyExport);
    }
}

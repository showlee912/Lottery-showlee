package cn.itedus.lottery.infrastructure.dao;

import cn.itedus.lottery.infrastructure.po.UserTakeActivity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户领取活动表DAO
 */
@Mapper
public interface IUserTakeActivityDao {

    /**
     * 插入用户领取活动信息
     *
     * @param userTakeActivity 入参
     */
    void insert(UserTakeActivity userTakeActivity);

}

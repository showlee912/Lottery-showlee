package cn.itedus.lottery.rpc.res;

import cn.itedus.lottery.common.Result;
import cn.itedus.lottery.rpc.dto.AwardDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 抽奖结果
 */
@Getter
@Setter
public class DrawRes extends Result implements Serializable {

    private AwardDTO awardDTO;

    public DrawRes(String code, String info) {
        super(code, info);
    }

}

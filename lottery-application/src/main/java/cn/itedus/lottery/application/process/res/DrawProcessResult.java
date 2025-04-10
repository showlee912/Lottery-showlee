package cn.itedus.lottery.application.process.res;

import cn.itedus.lottery.common.Result;
import cn.itedus.lottery.domain.strategy.model.vo.DrawAwardInfo;
import lombok.Getter;
import lombok.Setter;

/**
 *  活动抽奖结果
 */
@Getter
@Setter
public class DrawProcessResult extends Result {

    private DrawAwardInfo drawAwardInfo;

    public DrawProcessResult(String code, String info) {
        super(code, info);
    }

    public DrawProcessResult(String code, String info, DrawAwardInfo drawAwardInfo) {
        super(code, info);
        this.drawAwardInfo = drawAwardInfo;
    }


}

package cn.itedus.lottery.application.process.res;

import cn.itedus.lottery.common.Result;
import cn.itedus.lottery.domain.strategy.model.vo.DrawAwardVO;
import lombok.Getter;
import lombok.Setter;

/**
 *  活动抽奖结果
 */
@Getter
@Setter
public class DrawProcessResult extends Result {

    /**奖品信息*/
    private DrawAwardVO drawAwardVO;

    public DrawProcessResult(String code, String info) {
        super(code, info);
    }

    public DrawProcessResult(String code, String info, DrawAwardVO drawAwardVO) {
        super(code, info);
        this.drawAwardVO = drawAwardVO;
    }


}

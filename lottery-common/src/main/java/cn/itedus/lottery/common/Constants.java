package cn.itedus.lottery.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 枚举信息定义
 */
public class Constants {

    @Getter
    @AllArgsConstructor
    public enum ResponseCode {
        SUCCESS("0000", "成功"),

        UN_ERROR("0001", "未知失败"),

        ILLEGAL_PARAMETER("0002", "非法参数"),

        INDEX_DUP("0003", "主键冲突"),

        NO_UPDATE("0004","SQL操作无更新");

        private final String code;
        private final String info;

    }


    /**
     * 抽奖策略模式：总体概率、单项概率
     */
    @Getter
    @AllArgsConstructor
    public enum StrategyMode {

        SINGLE(1, "单项概率"),

        ENTIRETY(2, "总体概率");

        private Integer code;
        private String info;

        public void setCode(Integer code) {
            this.code = code;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

    /**
     * 中奖状态：0未中奖、1已中奖、2兜底奖
     */
    @Getter
    @AllArgsConstructor
    public enum DrawState {

        FAIL(0, "未中奖"),

        SUCCESS(1, "已中奖"),

        Cover(2, "兜底奖");

        private Integer code;
        private String info;

        public void setCode(Integer code) {
            this.code = code;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }


    /**
     * 发奖状态：0等待发奖、1发奖成功、2发奖失败
     */
    @Getter
    @AllArgsConstructor
    public enum AwardState {

        /**
         * 等待发奖
         */
        WAIT(0, "等待发奖"),

        /**
         * 发奖成功
         */
        SUCCESS(1, "发奖成功"),

        /**
         * 发奖失败
         */
        FAILURE(2, "发奖失败");

        private Integer code;
        private String info;

        public void setCode(Integer code) {
            this.code = code;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

    /**
     * 奖品类型（1:文字描述、2:兑换码、3:优惠券、4:实物奖品）
     */
    @Getter
    @AllArgsConstructor
    public enum AwardType {
        /**
         * 文字描述
         */
        DESC(1, "文字描述"),
        /**
         * 兑换码
         */
        RedeemCodeGoods(2, "兑换码"),
        /**
         * 优惠券
         */
        CouponGoods(3, "优惠券"),
        /**
         * 实物奖品
         */
        PhysicalGoods(4, "实物奖品");

        private Integer code;
        private String info;


        public void setCode(Integer code) {
            this.code = code;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

    /**
     * 活动状态
     */
    @Getter
    @AllArgsConstructor
    public enum ActivityState {

        /** 1：编辑 */
        EDIT(1, "编辑"),
        /** 2：提审 */
        ARRAIGNMENT(2, "提审"),
        /** 3：撤审 */
        REVOKE(3, "撤审"),
        /** 4：通过 */
        PASS(4, "通过"),
        /** 5：运行(活动中) */
        DOING(5, "运行(活动中)"),
        /** 6：拒绝 */
        REFUSE(6, "拒绝"),
        /** 7：关闭 */
        CLOSE(7, "关闭"),
        /** 8：开启 */
        OPEN(8, "开启");

        private Integer code;
        private String info;

        public void setCode(Integer code) {
            this.code = code;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

    /**
     * Ids 生成策略
     */
    public enum Ids {
        /** 雪花算法 */
        SnowFlake,
        /** 日期算法 */
        ShortCode,
        /** 随机算法 */
        RandomNumeric;
    }

}

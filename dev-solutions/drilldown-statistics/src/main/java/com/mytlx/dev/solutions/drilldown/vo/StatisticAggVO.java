package com.mytlx.dev.solutions.drilldown.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * 平台管理页面聚合信息
 */
@Slf4j
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StatisticAggVO {
    // 当前数据限制列表
    private List<Object> rangeIds;
    // 当前聚合维度
    private Integer groupBy;
    // 聚合数据列表项
    private List<AggItem> aggItems;

    /**
     * 平台管理页面聚合页-单行信息
     */
    @Slf4j
    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AggItem {

        // 前端类型，0 普通数据、1 汇总、2 公海掉落、3 未分配
        private Integer showType;
        // 状态，不同维度下含义不同，Agent 维度时为 t_jtfw_user 表的 status
        private Integer status;

        // 当前数据主键
        private String rangeId;
        // 当前数据门店主键
        private String shopId;
        // 当前数据公司主键
        private String companyId;

        // 当前数据类型，StatisticRangeByEnum
        private Integer rangeBy;
        // 名字，可能为城市、公司、经纪人等的名字
        private String rangeName;
        // 头像
        private String rangePic;

        // 待支付单数量
        private long todoPay;
        // 客户待签
        private long todoCustomerToSign;
        // 阿姨待签
        private long todoAuntToSign;
        // 待续约
        private long todoRenew;

        // 签单量
        private long achieveOrderSign;
        // 退单量
        private long achieveOrderRefund;
        // 成单金额，单位分
        private long achieveOrderAmountRaw;
        // 成单金额，美化
        private String achieveOrderAmount;
        // 营收（财务），单位分
        private long achieveRevenueFinanceRaw;
        // 营收（财务），美化
        private String achieveRevenueFinance;
        // 成单转化率
        private String achieveOrderRate;

        // 客户跟进-今日分配量
        private long customerTodayAssign;
        // 客户跟进-今日A级线索
        private long customerTodayA;
        // 客户跟进-今日B级线索
        private long customerTodayB;
        // 客户跟进-今日约面
        private long customerTodayInterview;
        // 客户跟进-今日签单且支付
        private long customerTodaySign;
        // 客户跟进-进入公海
        private long customerTodayPublicSea;
        // 客户跟进-今日AB率
        private String customerTodayABRate;

        // 客户跟进-当月分配量
        private long customerMonthAssign;

        // 存量客户-累计
        private long customerAll;
        // 存量客户-AB级线索
        private long customerAllAB;
        // 存量客户-AB级线索占比
        private String customerAllABRate;
        // 存量客户-A级线索
        private long customerAllA;
        // 存量客户-B级线索
        private long customerAllB;
        // 存量客户-今日约面
        private long customerAllInterview;
        // 存量客户-明日约面
        private long customerAllTomorrowInterview;
        // 存量客户-今日视频面试
        private long customerAllTodayVideoInterviewed;
        // 存量客户-待支付
        private long customerAllToPay;
        // 存量客户-进入公海
        private long customerAllPublicSea;

        // 公海跟进量
        private long publicSeaFollow;
        // 公海跟进-A级线索
        private long publicSeaFollowA;
        // 公海跟进-B级线索
        private long publicSeaFollowB;
        // 公海跟进-当日约面
        private long publicSeaFollowInterview;
        // 公海跟进-当日签单
        private long publicSeaFollowSign;
        // 公海跟进-AB率
        private String publicSeaFollowABRate;

        // 阿姨约面-视频面试 待视频
        private long interviewTodayToVideoInterview;
        // 阿姨约面-视频面试 待反馈面试结果
        private long interviewTodayToFeedback;

        // n3员工待办-今日未覆盖
        private long n3TaskTodayNotCover;
        // n3员工待办-未接通拨打<3
        private long n3TaskConnectFailLt3;
        // n3员工待办-新3今日未跟进
        private long n3TaskTodayNotFollow;


        public boolean hasAnyData() {

            // for(Field field : FieldUtils.getAllFields(AggItem.class)) {
            //     try {
            //         String name = field.getName();
            //         if (field.getType().equals(long.class) && (name.startsWith("publicSea") || name.startsWith("customerAll")
            //                 || name.startsWith("customerToday") || name.startsWith("achieve") || name.startsWith("todo")
            //                 || name.startsWith("customerMonth") || name.startsWith("interview") || name.startsWith("n3Task"))) {
            //             if (field.getLong(this) != 0) {
            //                 return true;
            //             }
            //         }
            //     } catch (Exception e) {
            //         log.error("parse fail " + field, e);
            //     }
            // }
            return false;
        }

        public void setAchieveOrderAmountRaw(long raw) {
            this.achieveOrderAmountRaw = raw;
            this.achieveOrderAmount = raw >= 10000 ? (raw / 10000) / 100.0 + "W" : raw / 100.0 + "";
        }

        public void setAchieveRevenueFinanceRaw(long raw) {
            this.achieveRevenueFinanceRaw = raw;
            this.achieveRevenueFinance = raw >= 10000 ? (raw / 10000) / 100.0 + "W" : raw / 100.0 + "";
        }

        public void setPublicSeaFollow(long publicSeaFollow) {
            this.publicSeaFollow = publicSeaFollow;
            this.publicSeaFollowABRate = (Arrays.stream(new long[]{publicSeaFollowA, publicSeaFollowB})
                    .sum() * 100 * 100 /
                    (publicSeaFollow > 0 ? publicSeaFollow : 1) / 100.0) + "%";
        }

        public void setPublicSeaFollowA(long publicSeaFollowA) {
            this.publicSeaFollowA = publicSeaFollowA;
            this.publicSeaFollowABRate = (Arrays.stream(new long[]{publicSeaFollowA, publicSeaFollowB})
                    .sum() * 100 * 100 /
                    (publicSeaFollow > 0 ? publicSeaFollow : 1) / 100.0) + "%";
        }

        public void setPublicSeaFollowB(long publicSeaFollowB) {
            this.publicSeaFollowB = publicSeaFollowB;
            this.publicSeaFollowABRate = (Arrays.stream(new long[]{publicSeaFollowA, publicSeaFollowB})
                    .sum() * 100 * 100 /
                    (publicSeaFollow > 0 ? publicSeaFollow : 1) / 100.0) + "%";
        }

        public void setCustomerTodayAssign(long customerTodayAssign) {
            this.customerTodayAssign = customerTodayAssign;
            this.customerTodayABRate = (Arrays.stream(new long[]{customerTodayA, customerTodayB})
                    .sum() * 100 * 100 /
                    (customerTodayAssign > 0 ? customerTodayAssign : 1) / 100.0) + "%";
        }

        public void setCustomerAllA(long customerAllA) {
            this.customerAllA = customerAllA;
            setCustomerAllAB(customerAllA + customerAllB);

        }

        public void setCustomerAllB(long customerAllB) {
            this.customerAllB = customerAllB;
            setCustomerAllAB(customerAllA + customerAllB);
        }

        public void setCustomerAll(long customerAll) {
            this.customerAll = customerAll;
            if (this.customerAll > 0) {
                this.customerAllABRate = (100 * 100 * customerAllAB / customerAll / 100.0) + "%";
            } else {
                this.customerAllABRate = "0.0%";
            }
        }

        public void setCustomerAllAB(long customerAllAB) {
            this.customerAllAB = customerAllAB;
            if (this.customerAll > 0) {
                this.customerAllABRate = (100 * 100 * customerAllAB / customerAll / 100.0) + "%";
            } else {
                this.customerAllABRate = "0.0%";
            }
        }

        public void setCustomerTodayA(long customerTodayA) {
            this.customerTodayA = customerTodayA;
            this.customerTodayABRate = (Arrays.stream(new long[]{customerTodayA, customerTodayB}).sum() * 100 * 100 /
                    (customerTodayAssign > 0 ? customerTodayAssign : 1) / 100.0) + "%";
        }

        public void setCustomerTodayB(long customerTodayB) {
            this.customerTodayB = customerTodayB;
            this.customerTodayABRate = (Arrays.stream(new long[]{customerTodayA, customerTodayB}).sum() * 100 * 100 /
                    (customerTodayAssign > 0 ? customerTodayAssign : 1) / 100.0) + "%";
        }

        public void setAchieveOrderSign(long achieveOrderSign) {
            this.achieveOrderSign = achieveOrderSign;
            if (this.customerMonthAssign > 0) {
                this.achieveOrderRate = (100 * 100 * achieveOrderSign / customerMonthAssign / 100.0) + "%";
            } else {
                this.achieveOrderRate = "0.0%";
            }
        }

        public void setCustomerMonthAssign(long customerMonthAssign) {
            this.customerMonthAssign = customerMonthAssign;
            if (this.customerMonthAssign > 0) {
                this.achieveOrderRate = (100 * 100 * achieveOrderSign / customerMonthAssign / 100.0) + "%";
            } else {
                this.achieveOrderRate = "0.0%";
            }
        }
    }

}

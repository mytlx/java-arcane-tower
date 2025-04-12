package com.mytlx.dev.solutions.drilldown.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 首页信息
 */
@Slf4j
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StatisticDashboardVO {

    /**
     * 列表项
     */
    private List<ManagementGroup> managementGroups;

    /**
     * 首页信息-分组信息
     */
    @Slf4j
    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ManagementGroup {

        /**
         * 枚举名字
         */
        private String managementCodeName;

        /**
         * 标题
         */
        private String managementTitle;

        /**
         * 浮窗提示信息
         */
        private String popupText;

        /**
         * 列表
         */
        private List<ManagementItem> managementItems;
    }

    /**
     * 首页信息-单项信息
     */
    @Slf4j
    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ManagementItem {

        /**
         * 枚举名字
         */
        private String managementCodeName;

        /**
         * 枚举值
         */
        private int managementCode;

        /**
         * 键
         */
        private String managementKey;

        /**
         * 值
         */
        private String managementValue;
    }
}


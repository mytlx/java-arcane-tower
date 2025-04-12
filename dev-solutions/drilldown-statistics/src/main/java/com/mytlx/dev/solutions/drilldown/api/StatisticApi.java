package com.mytlx.dev.solutions.drilldown.api;

import com.mytlx.dev.solutions.drilldown.service.statistic.StatisticAggListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-04-12 21:37
 */
@Component
@Slf4j
public class StatisticApi {


    @Autowired
    private StatisticAggListService aggListService;


    // @Api(value = "首页数据看板", result = MeiShiStatisticDashboardVO.class)
    // public Out dashboard(In in) throws Exception {
    //     return dashboardService.dashboard(in);
    // }

    // @Api(value = "聚合统计列表页", result = MeiShiStatisticAggVO.class)
    // @ApiParams({
    //         @ApiParam(value = "rangeBy", description = "数据限制类型，大区、城市等，StatisticRangeByEnum，不传时系统自动判断"),
    //         @ApiParam(value = "rangeId", description = "数据限制主键，可能是大区、城市、公司等 id"),
    //         @ApiParam(value = "queryBy", description = "查询数据范围，1 订单、2 今日客户、3 公海跟进、4 存量客户、5 今日投入公海、6 存量投入公海、9 新三客户"),
    //         @ApiParam(value = "sortBy", description = "按哪个字段排序"),
    //         @ApiParam(value = "order", description = "排序方向， asc 或者 desc")
    // })
    // public Out aggList(In in) throws Exception {
    //     return aggListService.meiShiAggList(in);
    // }

    // @Api(value = "聚合统计详情页", result = StatisticAggDetailVO.class)
    // @ApiParams({
    //         @ApiParam(value = "agentId", description = "经纪人 id"),
    //         @ApiParam(value = "companyId", description = "公司 id"),
    //         @ApiParam(value = "clickItem", description = "点击查询项"),
    //         @ApiParam(value = "showType", description = "点击数据行showType，MeiShiStatisticAggVO.AggItem#showType"),
    // })
    // public Out aggDetail(In in) throws Exception {
    //     return aggDetailService.meiShiAggDetail(in);
    // }

    // @Api(value = "聚合统计列表页", result = MeiShiStatisticAggVO.class)
    // @ApiParams({
    //         @ApiParam(value = "queryBy", description = "查询数据范围，1 订单、2 今日客户、3 公海跟进、" +
    //                 "4 存量客户、5 今日投入公海、6 存量投入公海、7 今日待办， 8 阿姨约面"),
    //         @ApiParam(value = "sortBy", description = "按哪个字段排序"),
    //         @ApiParam(value = "order", description = "排序方向， asc 或者 desc")
    // })
    // public Out appAggList(In in) throws Exception {
    //     return aggListService.appAggList(in);
    // }
    //
    // @Api(value = "聚合统计详情页", result = StatisticAggDetailVO.class)
    // @ApiParams({
    //         @ApiParam(value = "agentId", description = "经纪人 id"),
    //         @ApiParam(value = "clickItem", description = "点击查询项")
    // })
    // public Out appAggDetail(In in) throws Exception {
    //     return aggDetailService.appAggDetail(in);
    // }

}

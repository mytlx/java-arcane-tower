package com.mytlx.dev.solutions.drilldown.router;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 公共页面实体
 *
 * @author wuhaichao
 * @version 1.0.0
 * @since 2020/11/27 16:38
 */
@Data
@Accessors(chain = true)
public class CrmPage<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -6438023673298175073L;

    @SerializedName("list")
    private List<T> data;

    /**
     * 当前页码
     */
    private int pageNum;

    /**
     * 一页多少条数据
     */
    private int pageSize;

    /**
     * 数据总条数
     */
    private long totalCount;

    /**
     * 数据总页数
     */
    private int totalPage;

    public <R> CrmPage<R> convert(Function<? super T, ? extends R> mapper) {
        return new CrmPage<R>()
                .setData(Optional.ofNullable(data).orElse(new ArrayList<>()).parallelStream().map(mapper).collect(Collectors.toList()))
                .setPageNum(pageNum)
                .setPageSize(pageSize)
                .setTotalCount(totalCount)
                .setTotalPage(totalPage);
    }

    public static <T> CrmPage<T> build(List<T> data, int pageNum, int pageSize, long totalCount) {
        CrmPage<T> tCrmPage = new CrmPage<T>().setData(data)
                .setPageNum(pageNum)
                .setPageSize(pageSize)
                .setTotalCount(totalCount);
        if (pageSize > 0) {
            tCrmPage.setTotalPage((int) (totalCount / pageSize + (totalCount % pageSize == 0 ? 0 : 1)));
        }
        return tCrmPage;
    }

    // public static <T> CrmPage<T> build(List<T> data, JtPageParamRequest pageParamRequest, long totalCount) {
    //     return build(data, pageParamRequest.getPageNum(), pageParamRequest.getPageSize(), totalCount);
    // }
    //
    // public static <T> CrmPage<T> empty(int pageNum, int pageSize) {
    //     return build(Collections.emptyList(), pageNum, pageSize, 0);
    // }
    //
    // public static <T> CrmPage<T> empty(JtPageParamRequest pageParamRequest) {
    //     return empty(pageParamRequest.getPageNum(), pageParamRequest.getPageSize());
    // }
}

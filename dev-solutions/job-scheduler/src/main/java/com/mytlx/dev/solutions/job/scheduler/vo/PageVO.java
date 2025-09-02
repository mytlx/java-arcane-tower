package com.mytlx.dev.solutions.job.scheduler.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-07-02 18:53:26
 */
@Data
@Accessors(chain = true)
public class PageVO<T> {

    private int pageNum;

    private int pageSize;

    private long total;

    private List<T> list;

    public static <T> PageVO<T> of(int pageNum, int pageSize, long total, List<T> data) {
        return new PageVO<T>()
                .setPageNum(pageNum)
                .setPageSize(pageSize)
                .setTotal(total)
                .setList(data);
    }

    public <R> PageVO<R> convert(Function<? super T, ? extends R> mapper) {
        return new PageVO<R>()
                .setPageNum(pageNum)
                .setPageSize(pageSize)
                .setTotal(total)
                .setList(Optional.ofNullable(list).orElse(new ArrayList<>()).parallelStream().map(mapper).collect(Collectors.toList()));
    }

}

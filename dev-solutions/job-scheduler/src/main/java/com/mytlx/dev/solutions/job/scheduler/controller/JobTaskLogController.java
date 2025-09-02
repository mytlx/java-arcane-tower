package com.mytlx.dev.solutions.job.scheduler.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mytlx.dev.solutions.job.scheduler.dto.JobTaskLogQueryDTO;
import com.mytlx.dev.solutions.job.scheduler.service.JobTaskLogService;
import com.mytlx.dev.solutions.job.scheduler.vo.JobTaskLogVO;
import com.mytlx.dev.solutions.job.scheduler.vo.PageVO;
import com.mytlx.dev.solutions.job.scheduler.vo.ResponseVO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-07-02 18:35:47
 */
@RestController
@RequestMapping("/api/job/task/log")
public class JobTaskLogController {

    @Autowired
    private JobTaskLogService logService;

    @Operation(summary = "分页查询任务执行日志")
    @PostMapping("/page")
    public ResponseVO<PageVO<JobTaskLogVO>> pageLogs(@RequestBody JobTaskLogQueryDTO dto) {
        return ResponseVO.success(logService.pageQuery(dto).convert(JobTaskLogVO::fromEntity));
    }

}

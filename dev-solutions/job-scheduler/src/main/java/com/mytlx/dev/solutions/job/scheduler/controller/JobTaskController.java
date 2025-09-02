package com.mytlx.dev.solutions.job.scheduler.controller;

import com.mytlx.dev.solutions.job.scheduler.entity.JobTask;
import com.mytlx.dev.solutions.job.scheduler.service.JobTaskService;
import com.mytlx.dev.solutions.job.scheduler.vo.JobTaskVO;
import com.mytlx.dev.solutions.job.scheduler.vo.ResponseVO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-07-02 16:47:44
 */
@RestController
@RequestMapping("/api/job/task")
public class JobTaskController {

    @Autowired
    private JobTaskService jobTaskService;

    @Operation(summary = "新增任务")
    @PostMapping
    public ResponseVO<String> addTask(@RequestBody JobTask jobTask) throws Exception {
        Long id = jobTaskService.addJobTask(jobTask);
        return ResponseVO.success(String.valueOf(id), "新增成功");
    }

    @Operation(summary = "更新任务")
    @PutMapping
    public ResponseVO<String> updateTask(@RequestBody JobTask jobTask) throws Exception {
        jobTaskService.updateJobTask(jobTask);
        return ResponseVO.success("更新成功");
    }

    @Operation(summary = "启用任务")
    @PutMapping("/{id}/enable")
    public ResponseVO<String> enableTask(@PathVariable Long id) throws Exception {
        jobTaskService.enableJob(id);
        return ResponseVO.success("已启用");
    }

    @Operation(summary = "禁用任务")
    @PutMapping("/{id}/disable")
    public ResponseVO<String> disableTask(@PathVariable Long id) throws Exception {
        jobTaskService.disableJob(id);
        return ResponseVO.success("已禁用");
    }

    @Operation(summary = "删除任务")
    @DeleteMapping("/{id}")
    public ResponseVO<String> deleteTask(@PathVariable Long id) throws Exception {
        jobTaskService.deleteJob(id);
        return ResponseVO.success("删除成功");
    }

    @Operation(summary = "查询任务列表")
    @GetMapping("/list")
    public ResponseVO<List<JobTaskVO>> listTasks(@RequestParam(required = false) String keyword) {
        List<JobTask> list = jobTaskService.listByKeyword(keyword);
        return ResponseVO.success(JobTaskVO.fromEntityList(list));
    }

}

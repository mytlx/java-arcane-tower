package com.mytlx.dev.solutions.job.scheduler.job;

import com.mytlx.dev.solutions.job.scheduler.entity.JobTaskLog;
import com.mytlx.dev.solutions.job.scheduler.service.JobTaskLogService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-07-02 16:02:07
 */
@Slf4j
@Component
public class CommonJob implements Job {

    @Autowired
    private JobTaskLogService logService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Long taskId = context.getJobDetail().getJobDataMap().getLong("taskId");
        String taskName = context.getJobDetail().getKey().getName();

        log.info("[任务执行] 开始执行任务：{} (ID: {})", taskName, taskId);
        LocalDateTime start = LocalDateTime.now();

        JobTaskLog logEntry = new JobTaskLog();
        logEntry.setTaskId(taskId);
        logEntry.setStartTime(start);

        try {
            // ======= 执行具体业务逻辑 ↓ =======
            log.info("[模拟逻辑] Hello from task {}", taskName);
            Thread.sleep(1000); // 模拟耗时任务
            // ======= 执行具体业务逻辑 ↑ =======
            logEntry.setStatus(1);
            logEntry.setMessage("执行成功");
        } catch (Exception e) {
            logEntry.setStatus(0);
            logEntry.setMessage("异常：" + e.getMessage());
        }

        logEntry.setEndTime(LocalDateTime.now());
        logService.save(logEntry);
    }
}

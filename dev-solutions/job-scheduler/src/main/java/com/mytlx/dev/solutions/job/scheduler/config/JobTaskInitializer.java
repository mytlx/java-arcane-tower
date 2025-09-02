package com.mytlx.dev.solutions.job.scheduler.config;

import com.mytlx.dev.solutions.job.scheduler.entity.JobTask;
import com.mytlx.dev.solutions.job.scheduler.job.JobSchedulerHelper;
import com.mytlx.dev.solutions.job.scheduler.service.JobTaskService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-07-02 16:04:28
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JobTaskInitializer {

    private final JobTaskService jobTaskService;
    private final JobSchedulerHelper schedulerHelper;

    @PostConstruct
    public void init() {
        List<JobTask> taskList = jobTaskService.getEnabledTasks();
        for (JobTask task : taskList) {
            try {
                schedulerHelper.addJob(task);
            } catch (Exception e) {
                log.error("任务加载失败：{}", task.getId(), e);
            }
        }
    }

}

package com.mytlx.dev.solutions.job.scheduler.job;

import com.mytlx.dev.solutions.job.scheduler.entity.JobTask;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-07-02 16:03:59
 */
@Slf4j
@Component
public class JobSchedulerHelper {
    private final Scheduler scheduler;

    public JobSchedulerHelper(@Qualifier("scheduler") Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void addJob(JobTask task) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(CommonJob.class)
                .withIdentity("job-" + task.getId(), "default")
                .usingJobData("taskId", task.getId())
                .build();

        CronScheduleBuilder cronSchedule = CronScheduleBuilder.cronSchedule(task.getCronExpression());
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger-" + task.getId(), "default")
                .withSchedule(cronSchedule)
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
        log.info("已注册任务：{}（cron: {}）", task.getName(), task.getCronExpression());
    }

    public void deleteJob(Long taskId) throws SchedulerException {
        JobKey key = new JobKey("job-" + taskId, "default");
        scheduler.deleteJob(key);
        log.info("已移除任务：{}", taskId);
    }
}

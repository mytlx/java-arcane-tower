package com.mytlx.dev.solutions.job.scheduler.service;

import com.mytlx.dev.solutions.job.scheduler.entity.JobTask;

import java.util.List;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-07-02 16:05:04
 */
public interface JobTaskService {
    Long addJobTask(JobTask task) throws Exception;

    void updateJobTask(JobTask task) throws Exception;

    void enableJob(Long id) throws Exception;

    void disableJob(Long id) throws Exception;

    void deleteJob(Long id) throws Exception;

    List<JobTask> listByKeyword(String keyword);

    List<JobTask> getEnabledTasks();
}

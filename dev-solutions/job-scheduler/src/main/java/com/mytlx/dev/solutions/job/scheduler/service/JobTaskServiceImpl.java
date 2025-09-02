package com.mytlx.dev.solutions.job.scheduler.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mytlx.dev.solutions.job.scheduler.entity.JobTask;
import com.mytlx.dev.solutions.job.scheduler.job.JobSchedulerHelper;
import com.mytlx.dev.solutions.job.scheduler.mapper.JobTaskMapper;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-07-02 17:13:01
 */
@Slf4j
@Service
public class JobTaskServiceImpl implements JobTaskService {

    @Autowired
    private JobTaskMapper jobTaskMapper;
    @Autowired
    private JobSchedulerHelper jobSchedulerHelper;

    @Override
    public Long addJobTask(JobTask task) throws Exception {
        jobTaskMapper.insert(task);
        if (task.getStatus() == 1) {
            jobSchedulerHelper.addJob(task);
        }
        return task.getId();
    }

    @Override
    public void updateJobTask(JobTask task) throws Exception {
        jobTaskMapper.updateById(task);
        jobSchedulerHelper.deleteJob(task.getId());
        if (task.getStatus() == 1) {
            jobSchedulerHelper.addJob(task);
        }
    }

    @Override
    public void enableJob(Long id) throws Exception {
        JobTask updateTask = new JobTask()
                .setId(id)
                .setStatus(1);
        jobTaskMapper.updateById(updateTask);
        JobTask task = jobTaskMapper.selectById(id);
        jobSchedulerHelper.addJob(task);
    }

    @Override
    public void disableJob(Long id) throws Exception {
        JobTask updateTask = new JobTask()
                .setId(id)
                .setStatus(0);
        jobTaskMapper.updateById(updateTask);
        jobSchedulerHelper.deleteJob(id);
    }

    @Override
    public void deleteJob(Long id) throws Exception {
        jobTaskMapper.deleteById(id);
        jobSchedulerHelper.deleteJob(id);
    }

    @Override
    public List<JobTask> listByKeyword(String keyword) {
        LambdaQueryWrapper<JobTask> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(JobTask::getName, keyword);
        }
        return jobTaskMapper.selectList(wrapper);
    }

    @Override
    public List<JobTask> getEnabledTasks() {
        return jobTaskMapper.selectList(
                new LambdaQueryWrapper<JobTask>().eq(JobTask::getStatus, 1)
        );
    }

}

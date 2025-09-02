package com.mytlx.dev.solutions.job.scheduler.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mytlx.dev.solutions.job.scheduler.dto.JobTaskLogQueryDTO;
import com.mytlx.dev.solutions.job.scheduler.entity.JobTaskLog;
import com.mytlx.dev.solutions.job.scheduler.mapper.JobTaskLogMapper;
import com.mytlx.dev.solutions.job.scheduler.vo.JobTaskLogVO;
import com.mytlx.dev.solutions.job.scheduler.vo.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-07-02 17:12:50
 */
@Service
@Slf4j
public class JobTaskLogServiceImpl implements JobTaskLogService {
    @Autowired
    private JobTaskLogMapper logMapper;

    @Override
    public void save(JobTaskLog logEntry) {
        logMapper.insert(logEntry);
    }

    @Override
    public PageVO<JobTaskLog> pageQuery(JobTaskLogQueryDTO dto) {
        LambdaQueryWrapper<JobTaskLog> wrapper = Wrappers.lambdaQuery(JobTaskLog.class)
                .eq(dto.getTaskId() != null && dto.getTaskId() > 0, JobTaskLog::getTaskId, dto.getTaskId())
                .eq(dto.getStatus() != null, JobTaskLog::getStatus, dto.getStatus())
                .ge(dto.getStartTime() != null, JobTaskLog::getStartTime, dto.getStartTime())
                .le(dto.getEndTime() != null, JobTaskLog::getEndTime, dto.getEndTime())
                .orderByDesc(JobTaskLog::getStartTime);

        Page<JobTaskLog> result = logMapper.selectPage(new Page<>(dto.getPageNum(), dto.getPageSize()), wrapper);

        return PageVO.of(dto.getPageNum(), dto.getPageSize(), result.getTotal(), result.getRecords());
    }
}

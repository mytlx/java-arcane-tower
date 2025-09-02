package com.mytlx.dev.solutions.job.scheduler.service;

import com.mytlx.dev.solutions.job.scheduler.dto.JobTaskLogQueryDTO;
import com.mytlx.dev.solutions.job.scheduler.entity.JobTaskLog;
import com.mytlx.dev.solutions.job.scheduler.vo.PageVO;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-07-02 16:02:45
 */
public interface JobTaskLogService {

    void save(JobTaskLog logEntry);

    PageVO<JobTaskLog> pageQuery(JobTaskLogQueryDTO dto);

}

package com.mytlx.dev.solutions.job.scheduler.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mytlx.dev.solutions.job.scheduler.entity.JobTask;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-07-02 15:45:28
 */
@Mapper
public interface JobTaskMapper extends BaseMapper<JobTask> {

}

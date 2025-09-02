package com.mytlx.dev.solutions.job.scheduler.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-07-02 15:25:15
 */
@Data
@TableName("t_job_task_log")
public class JobTaskLog {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long taskId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer status; // 1成功 0失败

    private String message;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

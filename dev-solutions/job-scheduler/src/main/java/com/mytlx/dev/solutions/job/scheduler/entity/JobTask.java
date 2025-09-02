package com.mytlx.dev.solutions.job.scheduler.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-07-02 15:25:04
 */
@Data
@TableName("t_job_task")
@Accessors(chain = true)
public class JobTask {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String name;

    private String cronExpression;

    private Integer status; // 1启用 0禁用

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}

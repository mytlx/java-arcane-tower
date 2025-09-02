package com.mytlx.dev.solutions.job.scheduler.vo;

import com.mytlx.dev.solutions.job.scheduler.entity.JobTaskLog;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-07-02 18:32:32
 */
@Data
@Accessors(chain = true)
@Schema(description = "任务执行日志VO")
public class JobTaskLogVO {

    @Schema(description = "任务日志ID")
    private String id;

    @Schema(description = "任务ID")
    private String taskId;

    @Schema(description = "任务开始时间")
    private LocalDateTime startTime;

    @Schema(description = "任务结束时间")
    private LocalDateTime endTime;

    @Schema(description = "执行状态 1成功 0失败")
    private Integer status; // 1成功 0失败

    @Schema(description = "执行信息")
    private String message;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public static JobTaskLogVO fromEntity(JobTaskLog logEntry) {
        return new JobTaskLogVO()
                .setId(String.valueOf(logEntry.getId()))
                .setTaskId(String.valueOf(logEntry.getTaskId()))
                .setStartTime(logEntry.getStartTime())
                .setEndTime(logEntry.getEndTime())
                .setStatus(logEntry.getStatus())
                .setMessage(logEntry.getMessage())
                .setCreateTime(logEntry.getCreateTime())
                .setUpdateTime(logEntry.getUpdateTime());
    }

    public static List<JobTaskLogVO> fromEntityList(List<JobTaskLog> logEntries) {
        return logEntries.stream()
                .map(JobTaskLogVO::fromEntity)
                .collect(Collectors.toList());
    }
}

package com.mytlx.dev.solutions.job.scheduler.vo;

import com.mytlx.dev.solutions.job.scheduler.entity.JobTask;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-07-02 17:01:26
 */
@Schema(description = "任务定义VO")
@Data
@Accessors(chain = true)
public class JobTaskVO {
    @Schema(description = "任务ID")
    private String id;

    @Schema(description = "任务名称")
    private String name;

    @Schema(description = "Cron 表达式")
    private String cronExpression;

    @Schema(description = "状态：1启用，0禁用")
    private Integer status; // 1启用 0禁用

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public static JobTaskVO fromEntity(JobTask task) {
        return new JobTaskVO()
                .setId(String.valueOf(task.getId()))
                .setName(task.getName())
                .setCronExpression(task.getCronExpression())
                .setStatus(task.getStatus())
                .setRemark(task.getRemark())
                .setCreateTime(task.getCreateTime())
                .setUpdateTime(task.getUpdateTime());
    }

    public static List<JobTaskVO> fromEntityList(List<JobTask> taskList) {
        return taskList.stream()
                .map(JobTaskVO::fromEntity)
                .toList();
    }
}

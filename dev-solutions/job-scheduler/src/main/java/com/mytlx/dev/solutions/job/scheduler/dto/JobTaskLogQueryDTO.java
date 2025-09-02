package com.mytlx.dev.solutions.job.scheduler.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author TLX
 * @version 1.0.0
 * @since 2025-07-02 18:40:44
 */
@Data
@Schema(description = "任务日志分页查询参数")
public class JobTaskLogQueryDTO {

    @Schema(description = "任务ID")
    private Long taskId;

    @Schema(description = "执行状态：1成功 0失败")
    private Integer status;

    @Schema(description = "开始时间（yyyy-MM-dd HH:mm:ss）")
    private LocalDateTime startTime;

    @Schema(description = "结束时间（yyyy-MM-dd HH:mm:ss）")
    private LocalDateTime endTime;

    @Schema(description = "页码，从1开始")
    private Integer pageNum = 1;

    @Schema(description = "每页条数")
    private Integer pageSize = 10;

}

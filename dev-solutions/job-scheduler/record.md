
## 建表语句

```sql
-- 任务定义表
CREATE TABLE t_job_task (
  id BIGINT PRIMARY KEY,
  name VARCHAR(100) NOT NULL COMMENT '任务名称',
  cron_expression VARCHAR(100) NOT NULL COMMENT 'Cron 表达式',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '任务状态 1启用 0禁用',
  remark VARCHAR(255) DEFAULT NULL COMMENT '任务描述',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_time datetime DEFAULT NULL COMMENT '修改时间'
);

-- 任务执行日志表
CREATE TABLE t_job_task_log (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  task_id BIGINT NOT NULL COMMENT '关联任务ID',
  start_time datetime DEFAULT NULL COMMENT '开始时间',
  end_time datetime DEFAULT NULL COMMENT '结束时间',
  status TINYINT COMMENT '执行状态 1成功 0失败',
  message TEXT COMMENT '执行信息或异常日志',
  create_time datetime DEFAULT NULL COMMENT '创建时间',
  update_time datetime DEFAULT NULL COMMENT '修改时间'
);
```
package com.yzr.dida.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 更新任务请求 DTO
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskUpdateRequest {

    /**
     * 任务ID（如果未提供，将默认使用路径参数中的 taskId）
     */
    private String id;

    /**
     * 所属项目ID（必填）
     */
    @NotBlank(message = "项目ID不能为空")
    private String projectId;

    /**
     * 任务标题（可选）
     */
    private String title;

    /**
     * 任务内容
     */
    private String content;

    /**
     * 任务描述
     */
    private String desc;

    /**
     * 是否全天任务
     */
    @JsonProperty("isAllDay")
    private Boolean allDay;

    /**
     * 开始时间，格式：yyyy-MM-dd'T'HH:mm:ssZ
     */
    private String startDate;

    /**
     * 截止时间，格式：yyyy-MM-dd'T'HH:mm:ssZ
     */
    private String dueDate;

    /**
     * 时区
     */
    private String timeZone;

    /**
     * 提醒列表
     */
    private List<String> reminders;

    /**
     * 重复规则
     */
    private String repeatFlag;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 排序值
     */
    private Long sortOrder;

    /**
     * 子任务列表（更新通常需携带完整列表）
     */
    @Valid
    private List<TaskItemRequest> items;
}


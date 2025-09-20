package com.yzr.dida.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 子任务 (ChecklistItem) 请求模型
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskItemRequest {

    /**
     * 子任务ID
     */
    private String id;

    /**
     * 子任务标题
     */
    @NotBlank(message = "子任务标题不能为空")
    private String title;

    /**
     * 子任务完成状态：0-未完成，1-已完成
     */
    private Integer status;

    /**
     * 子任务完成时间，格式：yyyy-MM-dd'T'HH:mm:ssZ
     */
    private String completedTime;

    /**
     * 是否全天
     */
    @JsonProperty("isAllDay")
    private Boolean allDay;

    /**
     * 子任务排序值
     */
    private Long sortOrder;

    /**
     * 子任务开始日期时间，格式：yyyy-MM-dd'T'HH:mm:ssZ
     */
    private String startDate;

    /**
     * 子任务时区
     */
    private String timeZone;
}


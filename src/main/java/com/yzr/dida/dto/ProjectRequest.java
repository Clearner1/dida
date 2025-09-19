package com.yzr.dida.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;

/**
 * 项目请求DTO（创建和更新通用）
 */
@Data
public class ProjectRequest {

    /**
     * 项目名称（创建时必需，更新时可选）
     */
    @NotNull(message = "项目名称不能为空")
    private String name;

    /**
     * 项目颜色，例如："#F18181"（可选）
     */
    private String color;

    /**
     * 项目的排序值（可选）
     */
    private Long sortOrder;

    /**
     * 视图模式，可选值："list", "kanban", "timeline"（可选）
     */
    private String viewMode;

    /**
     * 项目类型，可选值："TASK", "NOTE"（可选）
     */
    private String kind;
}
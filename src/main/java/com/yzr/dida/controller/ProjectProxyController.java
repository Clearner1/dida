package com.yzr.dida.controller;

import com.yzr.dida.services.IProjectProxyService;
import com.yzr.dida.services.servicesImplement.CurrentUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/open/v1")
public class ProjectProxyController {

    private final IProjectProxyService projectProxyService;
    private final CurrentUserService currentUserService;

    public ProjectProxyController(IProjectProxyService projectProxyService, CurrentUserService currentUserService) {
        this.projectProxyService = projectProxyService;
        this.currentUserService = currentUserService;
    }

    /**
     * 获取用户项目列表
     * GET /open/v1/project
     */
    @GetMapping("/project")
    public ResponseEntity<String> getProjects() {
        String userId = currentUserService.currentUserId();
        return projectProxyService.getProjects(userId);
    }

    /**
     * 根据ID获取项目
     * GET /open/v1/project/{projectId}
     */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<String> getProject(@PathVariable String projectId) {
        String userId = currentUserService.currentUserId();
        return projectProxyService.getProject(userId, projectId);
    }

    /**
     * 获取项目及其数据(任务和栏目)
     * GET /open/v1/project/{projectId}/data
     */
    @GetMapping("/project/{projectId}/data")
    public ResponseEntity<String> getProjectData(@PathVariable String projectId) {
        String userId = currentUserService.currentUserId();
        return projectProxyService.getProjectData(userId, projectId);
    }
}
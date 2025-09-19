package com.yzr.dida.controller;

import com.yzr.dida.dto.ProjectRequest;
import com.yzr.dida.services.IProjectProxyService;
import com.yzr.dida.services.servicesImplement.CurrentUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    /**
     * 创建项目
     * @param request 项目创建请求
     * @return 返回创建后的项目信息
     */
    @PostMapping("/project")
    public ResponseEntity<String> createProject(@Valid @RequestBody ProjectRequest request) {
        String userId = currentUserService.currentUserId();
        return projectProxyService.createProject(userId, request);
    }

    /**
     * 更新项目
     * @param projectId 项目ID
     * @param request 项目更新请求
     * @return 更新后的项目信息
     */
    @PostMapping("/project/{projectId}")
    public ResponseEntity<String> updateProject(@PathVariable String projectId, @RequestBody ProjectRequest request) {
        String userId = currentUserService.currentUserId();
        return projectProxyService.updateProject(userId, projectId, request);
    }

    /**
     * 删除项目
     * @param projectId 项目ID
     * @return 删除是否成功
     */
    @DeleteMapping("/project/{projectId}")
    public ResponseEntity<Boolean> deleteProject(@PathVariable String projectId) {
        String userId = currentUserService.currentUserId();
        return projectProxyService.deleteProject(userId, projectId);
    }
}
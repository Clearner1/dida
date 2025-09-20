package com.yzr.dida.controller;

import com.yzr.dida.dto.TaskCreateRequest;
import com.yzr.dida.dto.TaskUpdateRequest;
import com.yzr.dida.services.ITaskProxyService;
import com.yzr.dida.services.servicesImplement.CurrentUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/open/v1")
public class TaskProxyController {

    private final ITaskProxyService taskProxyService;
    private final CurrentUserService currentUserService;

    public TaskProxyController(ITaskProxyService taskProxyService, CurrentUserService currentUserService) {
        this.taskProxyService = taskProxyService;
        this.currentUserService = currentUserService;
    }

    /**
     * 查看单个任务
     * GET /open/v1/project/{projectId}/task/{taskId}
     */
    @GetMapping("/project/{projectId}/task/{taskId}")
    public ResponseEntity<String> getTask(@PathVariable String projectId, @PathVariable String taskId) {
        String userId = currentUserService.currentUserId();
        return taskProxyService.getTask(userId, projectId, taskId);
    }

    /**
     * 创建任务
     * POST /open/v1/task
     */
    @PostMapping("/task")
    public ResponseEntity<String> createTask(@Valid @RequestBody TaskCreateRequest request) {
        String userId = currentUserService.currentUserId();
        return taskProxyService.createTask(userId, request);
    }

    /**
     * 更新任务
     * POST /open/v1/task/{taskId}
     */
    @PostMapping("/task/{taskId}")
    public ResponseEntity<String> updateTask(@PathVariable String taskId, @Valid @RequestBody TaskUpdateRequest request) {
        String userId = currentUserService.currentUserId();
        if (request.getId() == null || request.getId().isBlank()) {
            request.setId(taskId);
        }
        return taskProxyService.updateTask(userId, taskId, request);
    }

    /**
     * 完成任务（将任务标记为已完成）
     * POST /open/v1/project/{projectId}/task/{taskId}/complete
     */
    @PostMapping("/project/{projectId}/task/{taskId}/complete")
    public ResponseEntity<String> completeTask(@PathVariable String projectId, @PathVariable String taskId) {
        String userId = currentUserService.currentUserId();
        return taskProxyService.completeTask(userId, projectId, taskId);
    }

    /**
     * 删除任务
     * DELETE /open/v1/project/{projectId}/task/{taskId}
     */
    @DeleteMapping("/project/{projectId}/task/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable String projectId, @PathVariable String taskId) {
        String userId = currentUserService.currentUserId();
        return taskProxyService.deleteTask(userId, projectId, taskId);
    }
}

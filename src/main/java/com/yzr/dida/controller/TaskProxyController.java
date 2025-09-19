package com.yzr.dida.controller;

import com.yzr.dida.services.ITaskProxyService;
import com.yzr.dida.services.servicesImplement.CurrentUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * 先知道 userId 是谁，然后通过 userId 获取 token，
     * 转发到 http，get 方法，带上 Bearer token 和下面的 API 信息，获取任务详情
     * GET /open/v1/project/{projectId}/task/{taskId}
     * 需要先获取 projectId 和 taskId 才可以知道任务详情
     */
    @GetMapping("/project/{projectId}/task/{taskId}")
    public ResponseEntity<String> getTask(@PathVariable String projectId, @PathVariable String taskId) {
        String userId = currentUserService.currentUserId();
        return taskProxyService.getTask(userId, projectId, taskId);
    }
}

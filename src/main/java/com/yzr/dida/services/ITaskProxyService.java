package com.yzr.dida.services;

import org.springframework.http.ResponseEntity;

public interface ITaskProxyService {
    
    /**
     * 获取单个任务
     * @param userId 用户ID
     * @param projectId 项目ID
     * @param taskId 任务ID
     * @return 任务详情
     */
    ResponseEntity<String> getTask(String userId, String projectId, String taskId);
}
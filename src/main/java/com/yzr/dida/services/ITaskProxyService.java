package com.yzr.dida.services;

import com.yzr.dida.dto.TaskCreateRequest;
import com.yzr.dida.dto.TaskUpdateRequest;
import org.springframework.http.ResponseEntity;

/**
 * 滴答清单任务代理服务
 */
public interface ITaskProxyService {

    /**
     * 获取单个任务
     * @param userId 用户ID
     * @param projectId 项目ID
     * @param taskId 任务ID
     * @return 任务详情
     */
    ResponseEntity<String> getTask(String userId, String projectId, String taskId);

    /**
     * 创建任务
     * @param userId 用户ID
     * @param request 创建任务请求体
     * @return 创建后的任务信息
     */
    ResponseEntity<String> createTask(String userId, TaskCreateRequest request);

    /**
     * 更新任务
     * @param userId 用户ID
     * @param taskId 任务ID
     * @param request 更新任务请求体
     * @return 更新后的任务信息
     */
    ResponseEntity<String> updateTask(String userId, String taskId, TaskUpdateRequest request);

    /**
     * 完成任务
     * @param userId 用户ID
     * @param projectId 项目ID
     * @param taskId 任务ID
     * @return 操作结果
     */
    ResponseEntity<String> completeTask(String userId, String projectId, String taskId);

    /**
     * 删除任务
     * @param userId 用户ID
     * @param projectId 项目ID
     * @param taskId 任务ID
     * @return 操作结果
     */
    ResponseEntity<String> deleteTask(String userId, String projectId, String taskId);
}

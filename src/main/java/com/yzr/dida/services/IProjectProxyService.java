package com.yzr.dida.services;

import com.yzr.dida.dto.ProjectRequest;
import org.springframework.http.ResponseEntity;

public interface IProjectProxyService {
    
    /**
     * 获取用户项目列表
     * @param userId 用户ID
     * @return 项目列表
     */
    ResponseEntity<String> getProjects(String userId);
    
    /**
     * 获取单个项目
     * @param userId 用户ID
     * @param projectId 项目ID
     * @return 项目详情
     */
    ResponseEntity<String> getProject(String userId, String projectId);
    
    /**
     * 获取项目及其数据(任务和栏目)
     * @param userId 用户ID
     * @param projectId 项目ID
     * @return 项目数据
     */
    ResponseEntity<String> getProjectData(String userId, String projectId);

    /**
     * 创建项目
     * @param userId 用户ID
     * @param request 项目创建请求
     * @return 创建后的项目信息
     */
    ResponseEntity<String> createProject(String userId, ProjectRequest request);

    /**
     * 更新项目
     * @param userId 用户ID
     * @param projectId 项目ID
     * @param request 项目更新请求
     * @return 更新后的项目信息
     */
    ResponseEntity<String> updateProject(String userId, String projectId, ProjectRequest request);

    /**
     * 删除项目
     * @param userId 用户ID
     * @param projectId 项目ID
     * @return 删除是否成功
     */
    ResponseEntity<Boolean> deleteProject(String userId, String projectId);
}
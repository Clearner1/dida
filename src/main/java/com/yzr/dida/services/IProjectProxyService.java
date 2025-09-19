package com.yzr.dida.services;

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
}
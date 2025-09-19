package com.yzr.dida.services.servicesImplement;

import com.yzr.dida.services.ITaskProxyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TaskProxyService implements ITaskProxyService {

    private final AuthorizationConnectionService authConnectionService;
    private final RestTemplate http;

    @Value("${dida.api-base:https://api.dida365.com}")
    private String apiBase;

    public TaskProxyService(AuthorizationConnectionService authConnectionService) {
        this.authConnectionService = authConnectionService;
        this.http = new RestTemplate();
    }

    @Override
    public ResponseEntity<String> getTask(String userId, String projectId, String taskId) {
        // 获取用户的access token
        String accessToken = authConnectionService.getValidAccessToken(userId, "dida");
        if (accessToken == null) {
            return ResponseEntity.status(401).body("{\"error\":\"No valid access token found\"}");
        }

        // 构建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 调用滴答清单API
        String url = apiBase + "/open/v1/project/" + projectId + "/task/" + taskId;
        
        try {
            return http.exchange(url, HttpMethod.GET, entity, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"error\":\"Failed to fetch task: " + e.getMessage() + "\"}");
        }
    }
}
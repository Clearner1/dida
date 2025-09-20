package com.yzr.dida.services.servicesImplement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzr.dida.dto.TaskCreateRequest;
import com.yzr.dida.dto.TaskUpdateRequest;
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
    private final ObjectMapper objectMapper;

    @Value("${dida.api-base:https://api.dida365.com}")
    private String apiBase;

    public TaskProxyService(AuthorizationConnectionService authConnectionService) {
        this.authConnectionService = authConnectionService;
        this.http = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public ResponseEntity<String> getTask(String userId, String projectId, String taskId) {
        String accessToken = resolveAccessToken(userId);
        if (accessToken == null) {
            return unauthorizedResponse();
        }

        HttpHeaders headers = buildHeaders(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = apiBase + "/open/v1/project/" + projectId + "/task/" + taskId;

        try {
            return http.exchange(url, HttpMethod.GET, entity, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Failed to fetch task: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<String> createTask(String userId, TaskCreateRequest request) {
        String accessToken = resolveAccessToken(userId);
        if (accessToken == null) {
            return unauthorizedResponse();
        }

        HttpHeaders headers = buildHeaders(accessToken);
        String url = apiBase + "/open/v1/task";

        try {
            String jsonBody = objectMapper.writeValueAsString(request);
            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
            return http.exchange(url, HttpMethod.POST, entity, String.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(500).body(error("Failed to serialize task payload: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Failed to create task: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<String> updateTask(String userId, String taskId, TaskUpdateRequest request) {
        String accessToken = resolveAccessToken(userId);
        if (accessToken == null) {
            return unauthorizedResponse();
        }

        // Ensure the payload carries the task id; default to the path value when missing
        if (request.getId() == null || request.getId().isBlank()) {
            request.setId(taskId);
        }

        HttpHeaders headers = buildHeaders(accessToken);
        String url = apiBase + "/open/v1/task/" + taskId;

        try {
            String jsonBody = objectMapper.writeValueAsString(request);
            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
            return http.exchange(url, HttpMethod.POST, entity, String.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(500).body(error("Failed to serialize task payload: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Failed to update task: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<String> completeTask(String userId, String projectId, String taskId) {
        String accessToken = resolveAccessToken(userId);
        if (accessToken == null) {
            return unauthorizedResponse();
        }

        HttpHeaders headers = buildHeaders(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = apiBase + "/open/v1/project/" + projectId + "/task/" + taskId + "/complete";

        try {
            return http.exchange(url, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Failed to complete task: " + e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<String> deleteTask(String userId, String projectId, String taskId) {
        String accessToken = resolveAccessToken(userId);
        if (accessToken == null) {
            return unauthorizedResponse();
        }

        HttpHeaders headers = buildHeaders(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = apiBase + "/open/v1/project/" + projectId + "/task/" + taskId;

        try {
            return http.exchange(url, HttpMethod.DELETE, entity, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(error("Failed to delete task: " + e.getMessage()));
        }
    }

    private String resolveAccessToken(String userId) {
        return authConnectionService.getValidAccessToken(userId, "dida");
    }

    private HttpHeaders buildHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/json");
        return headers;
    }

    private ResponseEntity<String> unauthorizedResponse() {
        return ResponseEntity.status(401).body(error("No valid access token found"));
    }

    private String error(String message) {
        String sanitized = message == null ? "" : message.replace("\"", "\\\"");
        return "{\"error\":\"" + sanitized + "\"}";
    }
}

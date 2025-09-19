package com.yzr.dida.services.servicesImplement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzr.dida.dto.ProjectRequest;
import com.yzr.dida.services.IProjectProxyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProjectProxyService implements IProjectProxyService {

    private final AuthorizationConnectionService authConnectionService;
    private final RestTemplate http;
    private final ObjectMapper objectMapper;

    @Value("${dida.api-base:https://api.dida365.com}")
    private String apiBase;

    public ProjectProxyService(AuthorizationConnectionService authConnectionService) {
        this.authConnectionService = authConnectionService;
        this.http = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public ResponseEntity<String> getProjects(String userId) {
        String accessToken = authConnectionService.getValidAccessToken(userId, "dida");
        if (accessToken == null) {
            return ResponseEntity.status(401).body("{\"error\":\"No valid access token found\"}");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = apiBase + "/open/v1/project";
        
        try {
            return http.exchange(url, HttpMethod.GET, entity, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"error\":\"Failed to fetch projects: " + e.getMessage() + "\"}");
        }
    }

    @Override
    public ResponseEntity<String> getProject(String userId, String projectId) {
        String accessToken = authConnectionService.getValidAccessToken(userId, "dida");
        if (accessToken == null) {
            return ResponseEntity.status(401).body("{\"error\":\"No valid access token found\"}");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = apiBase + "/open/v1/project/" + projectId;
        
        try {
            return http.exchange(url, HttpMethod.GET, entity, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"error\":\"Failed to fetch project: " + e.getMessage() + "\"}");
        }
    }

    @Override
    public ResponseEntity<String> getProjectData(String userId, String projectId) {
        String accessToken = authConnectionService.getValidAccessToken(userId, "dida");
        if (accessToken == null) {
            return ResponseEntity.status(401).body("{\"error\":\"No valid access token found\"}");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = apiBase + "/open/v1/project/" + projectId + "/data";
        
        try {
            return http.exchange(url, HttpMethod.GET, entity, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"error\":\"Failed to fetch project data: " + e.getMessage() + "\"}");
        }
    }

    @Override
    public ResponseEntity<String> createProject(String userId, ProjectRequest request) {
        String accessToken = authConnectionService.getValidAccessToken(userId, "dida");
        if (accessToken == null) {
            return ResponseEntity.status(401).body("{\"error\":\"No valid access token found\"}");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/json");

        try {
            // 构建请求体，只包含非空字段
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("name", request.getName());
            if (request.getColor() != null) {
                requestBody.put("color", request.getColor());
            }
            if (request.getSortOrder() != null) {
                requestBody.put("sortOrder", request.getSortOrder());
            }
            if (request.getViewMode() != null) {
                requestBody.put("viewMode", request.getViewMode());
            }
            if (request.getKind() != null) {
                requestBody.put("kind", request.getKind());
            }

            String jsonBody = objectMapper.writeValueAsString(requestBody);
            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
            String url = apiBase + "/open/v1/project";
            
            return http.exchange(url, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"error\":\"Failed to create project: " + e.getMessage() + "\"}");
        }
    }

    @Override
    public ResponseEntity<String> updateProject(String userId, String projectId, ProjectRequest request) {
        String accessToken = authConnectionService.getValidAccessToken(userId, "dida");
        if (accessToken == null) {
            return ResponseEntity.status(401).body("{\"error\":\"No valid access token found\"}");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/json");

        try {
            // 构建请求体，只包含非空字段
            Map<String, Object> requestBody = new HashMap<>();
            if (request.getName() != null) {
                requestBody.put("name", request.getName());
            }
            if (request.getColor() != null) {
                requestBody.put("color", request.getColor());
            }
            if (request.getSortOrder() != null) {
                requestBody.put("sortOrder", request.getSortOrder());
            }
            if (request.getViewMode() != null) {
                requestBody.put("viewMode", request.getViewMode());
            }
            if (request.getKind() != null) {
                requestBody.put("kind", request.getKind());
            }

            String jsonBody = objectMapper.writeValueAsString(requestBody);
            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
            String url = apiBase + "/open/v1/project/" + projectId;
            
            return http.exchange(url, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"error\":\"Failed to update project: " + e.getMessage() + "\"}");
        }
    }

    @Override
    public ResponseEntity<Boolean> deleteProject(String userId, String projectId) {
        String accessToken = authConnectionService.getValidAccessToken(userId, "dida");
        if (accessToken == null) {
            return ResponseEntity.status(401).body(false);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = apiBase + "/open/v1/project/" + projectId;
        
        try {
            ResponseEntity<String> response = http.exchange(url, HttpMethod.DELETE, entity, String.class);
            return ResponseEntity.ok(response.getStatusCode().is2xxSuccessful());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(false);
        }
    }
}
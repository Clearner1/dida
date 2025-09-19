package com.yzr.dida.services.servicesImplement;

import com.yzr.dida.services.IProjectProxyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProjectProxyService implements IProjectProxyService {

    private final AuthorizationConnectionService authConnectionService;
    private final RestTemplate http;

    @Value("${dida.api-base:https://api.dida365.com}")
    private String apiBase;

    public ProjectProxyService(AuthorizationConnectionService authConnectionService) {
        this.authConnectionService = authConnectionService;
        this.http = new RestTemplate();
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
}
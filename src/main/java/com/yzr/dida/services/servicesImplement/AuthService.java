package com.yzr.dida.services.servicesImplement;

import com.yzr.dida.lib.EncryptionService;
import com.yzr.dida.models.AuthorizationConnection;
import com.yzr.dida.repositories.AuthorizationConnectionRepository;
import com.yzr.dida.services.IAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

import static com.yzr.dida.utils.AuthUtils.url;

@Service
public class AuthService implements IAuthService {

    private final AuthorizationConnectionRepository repo;
    private final EncryptionService encryptionService;
    private final RestTemplate http;

    public AuthService(AuthorizationConnectionRepository repo,
                       EncryptionService encryptionService) {
        this.repo = repo;
        this.encryptionService = encryptionService;
        this.http = new RestTemplate();
    }

    @Value("${dida.oauth.client-id:}")
    private String clientId;

    @Value("${dida.oauth.client-secret:}")
    private String clientSecret;

    @Value("${dida.oauth.redirect-uri:}")
    private String redirectUri;

    @Value("${dida.oauth.authorize-endpoint:https://dida365.com/oauth/authorize}")
    private String authorizeEndpoint;

    @Value("${dida.oauth.token-endpoint:https://dida365.com/oauth/token}")
    private String tokenEndpoint;

    public String buildAuthorizeUrl(String userId, String scopeValue) {
        String state = UUID.randomUUID().toString();
        String didaScope = scopeValue;
        // Persist pending state
        repo.upsertState(userId, "dida", didaScope, state);

        String scope = url(didaScope);
        String cid = url(clientId);
        String ru = url(redirectUri);
        return authorizeEndpoint + "?scope=" + scope +
                "&client_id=" + cid +
                "&state=" + state +
                "&redirect_uri=" + ru +
                "&response_type=code";
    }

    public void handleCallback(String userId, String code, String state) {
        var pending = repo.findByUserAndState(userId, "dida", state)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired state"));

        // Exchange code for token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String basic = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));
        headers.add(HttpHeaders.AUTHORIZATION, "Basic " + basic);
        String body = "code=" + url(code)
                + "&grant_type=authorization_code"
                + "&scope=" + url(scopeKey(pending.getScope()))
                + "&redirect_uri=" + url(redirectUri);
        HttpEntity<String> req = new HttpEntity<>(body, headers);

        @SuppressWarnings("unchecked")
        Map<String, Object> resp = http.postForObject(tokenEndpoint, req, Map.class);
        if (resp == null || !resp.containsKey("access_token")) {
            throw new IllegalStateException("Token exchange failed");
        }
        String accessToken = String.valueOf(resp.get("access_token"));
        String enc = encryptionService.encrypt(accessToken);

        Instant expiresAt = null;
        if (resp.containsKey("expires_in")) {
            try {
                long seconds = Long.parseLong(String.valueOf(resp.get("expires_in")));
                expiresAt = Instant.now().plusSeconds(seconds);
            } catch (NumberFormatException ignore) { }
        }

        repo.saveToken(userId, "dida", scopeKey(pending.getScope()), enc, expiresAt);
    }

    public Map<String, Object> status(String userId) {
        return repo.status(userId, "dida");
    }

    public void disconnect(String userId) {
        repo.disconnect(userId, "dida");
    }

//    private static String url(String s) {
//        return URLEncoder.encode(s == null ? "" : s, StandardCharsets.UTF_8);
//    }

    private String scopeKey(AuthorizationConnection.Scope scope) {
        return scope == AuthorizationConnection.Scope.READ_WRITE ? "tasks:read tasks:write" : "tasks:read";
    }
}

/**
 * static, 创建类就可以直接使用
 * 在内存中只有一份
 */

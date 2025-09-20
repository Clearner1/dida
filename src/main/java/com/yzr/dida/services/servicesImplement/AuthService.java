package com.yzr.dida.services.servicesImplement;

//import com.yzr.dida.utils.EncryptionService;
import com.yzr.dida.entity.AuthorizationConnectionDO;
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

    private final AuthorizationConnectionService authConnectionService;
//    private final EncryptionService encryptionService;
    private final RestTemplate http;

    public AuthService(AuthorizationConnectionService authConnectionService
                       /*EncryptionService encryptionService*/) {
        this.authConnectionService = authConnectionService;
//        this.encryptionService = encryptionService;
        this.http = new RestTemplate();
    }

    //在 yml 文件中配置，使用@Value 来指代是哪一个配置文件中的属性
    @Value("${dida.client-id:}")
    private String clientId;

    @Value("${dida.client-secret:}")
    private String clientSecret;

    @Value("${dida.redirect-uri:}")
    private String redirectUri;

    @Value("${dida.oauth.authorize-endpoint:https://dida365.com/oauth/authorize}")
    private String authorizeEndpoint;

    @Value("${dida.oauth.token-endpoint:https://dida365.com/oauth/token}")
    private String tokenEndpoint;

    public String buildAuthorizeUrl(String userId, String scopeValue) {
//        滴答 API 需要生成一个恒定的 state
        final String state = "STATE";
        String didaScope = scopeValue;
        // 向数据库插入一条关于谁请求了读写权限的一条记录
        authConnectionService.upsertState(userId, "dida", didaScope, state);

        String scope = url(didaScope);
        String cid = url(clientId);
        String ru = url(redirectUri);
//      https://dida365.com/oauth/authorize?scope=read&client_id=client_id&state=state&redirect_uri=redirect_uri&response_type=code
        return authorizeEndpoint + "?scope=" + scope +
                "&client_id=" + cid +
                "&state=" + state +
                "&redirect_uri=" + ru +
                "&response_type=code";
    }

    public void handleCallback(String userId, String code, String state) {
        var pending = authConnectionService.findByUserAndState(userId, "dida", state)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired state"));

        // Exchange code for token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String basic = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));
        headers.add(HttpHeaders.AUTHORIZATION, "Basic " + basic);
        AuthorizationConnectionDO.Scope pendingScopeEnum = authConnectionService.getScopeEnum(pending.getScope());
        String body = "code=" + url(code)
                + "&grant_type=authorization_code"
                + "&scope=" + url(scopeKey(pendingScopeEnum))
                + "&redirect_uri=" + url(redirectUri);
        HttpEntity<String> req = new HttpEntity<>(body, headers);

        @SuppressWarnings("unchecked")
        Map<String, Object> resp = http.postForObject(tokenEndpoint, req, Map.class);
        if (resp == null || !resp.containsKey("access_token")) {
            throw new IllegalStateException("Token exchange failed");
        }
        String accessToken = String.valueOf(resp.get("access_token"));
//        String enc = encryptionService.encrypt(accessToken);

        Instant expiresAt = null;
        if (resp.containsKey("expires_in")) {
            try {
                long seconds = Long.parseLong(String.valueOf(resp.get("expires_in")));
                expiresAt = Instant.now().plusSeconds(seconds);
            } catch (NumberFormatException ignore) { }
        }

        authConnectionService.saveToken(userId, "dida", scopeKey(pendingScopeEnum), accessToken, expiresAt);
    }

    public Map<String, Object> status(String userId) {
        return authConnectionService.status(userId, "dida");
    }

    public void disconnect(String userId) {
        authConnectionService.disconnect(userId, "dida");
    }

//    private static String url(String s) {
//        return URLEncoder.encode(s == null ? "" : s, StandardCharsets.UTF_8);
//    }

    private String scopeKey(AuthorizationConnectionDO.Scope scope) {
        return scope == AuthorizationConnectionDO.Scope.READ_WRITE ? "tasks:read tasks:write" : "tasks:read";
    }
}

/**
 * static, 创建类就可以直接使用
 * 在内存中只有一份
 */

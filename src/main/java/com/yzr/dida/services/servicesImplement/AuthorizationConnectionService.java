package com.yzr.dida.services.servicesImplement;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yzr.dida.mappers.AuthorizationConnectionMapper;
import com.yzr.dida.entity.AuthorizationConnectionDO;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorizationConnectionService {
    private final AuthorizationConnectionMapper mapper;

    public AuthorizationConnectionService(AuthorizationConnectionMapper mapper) {
        this.mapper = mapper;
    }

    public String upsertState(String userId, String provider, String scope, String stateNonce) {
        QueryWrapper<AuthorizationConnectionDO> qw = new QueryWrapper<>();
        qw.eq("user_id", userId).eq("provider", provider);
        AuthorizationConnectionDO existing = mapper.selectOne(qw);
        if (existing == null) {
            AuthorizationConnectionDO rec = new AuthorizationConnectionDO();
            String id = UUID.randomUUID().toString();
            rec.setId(id);
            rec.setUserId(userId);
            rec.setProvider(provider);
            rec.setScope(scopeKey(scope));
            rec.setAccessTokenEnc(null);
            rec.setExpiresAt(null);
            rec.setStateNonce(stateNonce);
            mapper.insert(rec);
            return id;
        } else {
            UpdateWrapper<AuthorizationConnectionDO> uw = new UpdateWrapper<>();
            uw.eq("user_id", userId).eq("provider", provider)
              .set("scope", scopeKey(scope))
              .set("access_token_enc", null)
              .set("expires_at", null)
              .set("revoked_at", null)
              .set("state_nonce", stateNonce);
            mapper.update(null, uw);
            return existing.getId();
        }
    }

    public Optional<AuthorizationConnectionDO> findByUserAndState(String userId, String provider, String stateNonce) {
        QueryWrapper<AuthorizationConnectionDO> qw = new QueryWrapper<>();
        qw.eq("user_id", userId).eq("provider", provider).eq("state_nonce", stateNonce).last("LIMIT 1");
        AuthorizationConnectionDO rec = mapper.selectOne(qw);
        return Optional.ofNullable(rec);
    }

    public void saveToken(String userId, String provider, String scope, String encToken, Instant expiresAt) {
        UpdateWrapper<AuthorizationConnectionDO> uw = new UpdateWrapper<>();
        uw.eq("user_id", userId).eq("provider", provider)
          .set("access_token_enc", encToken)
          .set("expires_at", expiresAt == null ? null : LocalDateTime.ofInstant(expiresAt, ZoneOffset.UTC))
          .set("scope", scopeKey(scope))
          .set("state_nonce", null);
        mapper.update(null, uw);
    }

    public void disconnect(String userId, String provider) {
        UpdateWrapper<AuthorizationConnectionDO> uw = new UpdateWrapper<>();
        uw.eq("user_id", userId).eq("provider", provider)
          .set("revoked_at", LocalDateTime.now(ZoneOffset.UTC))
          .set("access_token_enc", null)
          .set("expires_at", null)
          .set("state_nonce", null);
        mapper.update(null, uw);
    }

    public Map<String, Object> status(String userId, String provider) {
        QueryWrapper<AuthorizationConnectionDO> qw = new QueryWrapper<>();
        qw.select("access_token_enc", "scope").eq("user_id", userId).eq("provider", provider).last("LIMIT 1");
        AuthorizationConnectionDO rec = mapper.selectOne(qw);
        if (rec == null) return Map.of("connected", false, "scope", null);
        return Map.of(
                "connected", rec.getAccessTokenEnc() != null,
                "scope", rec.getScope()
        );
    }

    private String scopeKey(String scope) {
        return ("tasks:read tasks:write".equalsIgnoreCase(scope) || "read_write".equalsIgnoreCase(scope)) ? "read_write" : "read";
    }

    public AuthorizationConnectionDO.Scope getScopeEnum(String scopeString) {
        return "read_write".equalsIgnoreCase(scopeString) ? AuthorizationConnectionDO.Scope.READ_WRITE : AuthorizationConnectionDO.Scope.READ;
    }
}
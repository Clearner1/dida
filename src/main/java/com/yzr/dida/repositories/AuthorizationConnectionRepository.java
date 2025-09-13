package com.yzr.dida.repositories;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yzr.dida.mappers.AuthorizationConnectionMapper;
import com.yzr.dida.domain.dataobject.AuthorizationConnectionDO;
import com.yzr.dida.models.AuthorizationConnection;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AuthorizationConnectionRepository {
    private final AuthorizationConnectionMapper mapper;

    public AuthorizationConnectionRepository(AuthorizationConnectionMapper mapper) {
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

    public Optional<AuthorizationConnection> findByUserAndState(String userId, String provider, String stateNonce) {
        QueryWrapper<AuthorizationConnectionDO> qw = new QueryWrapper<>();
        qw.eq("user_id", userId).eq("provider", provider).eq("state_nonce", stateNonce).last("LIMIT 1");
        AuthorizationConnectionDO rec = mapper.selectOne(qw);
        if (rec == null) return Optional.empty();
        AuthorizationConnection ac = new AuthorizationConnection();
        ac.setId(rec.getId());
        ac.setUserId(rec.getUserId());
        ac.setProvider(rec.getProvider());
        ac.setScope("read_write".equalsIgnoreCase(rec.getScope()) ? AuthorizationConnection.Scope.READ_WRITE : AuthorizationConnection.Scope.READ);
        ac.setAccessTokenEnc(rec.getAccessTokenEnc());
        ac.setExpiresAt(rec.getExpiresAt() == null ? null : rec.getExpiresAt().toInstant(ZoneOffset.UTC));
        ac.setCreatedAt(rec.getCreatedAt() == null ? null : rec.getCreatedAt().toInstant(ZoneOffset.UTC));
        ac.setRevokedAt(rec.getRevokedAt() == null ? null : rec.getRevokedAt().toInstant(ZoneOffset.UTC));
        ac.setStateNonce(rec.getStateNonce());
        return Optional.of(ac);
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
}

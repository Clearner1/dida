package com.yzr.dida.models;

import java.time.Instant;

public class AuthorizationConnection {
    public enum Scope { READ, READ_WRITE }

    private String id;
    private String userId;
    private String provider; // dida
    private Scope scope;
    private String accessTokenEnc;
    private Instant expiresAt;
    private Instant createdAt;
    private Instant revokedAt;
    private String stateNonce;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public Scope getScope() { return scope; }
    public void setScope(Scope scope) { this.scope = scope; }
    public String getAccessTokenEnc() { return accessTokenEnc; }
    public void setAccessTokenEnc(String accessTokenEnc) { this.accessTokenEnc = accessTokenEnc; }
    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getRevokedAt() { return revokedAt; }
    public void setRevokedAt(Instant revokedAt) { this.revokedAt = revokedAt; }
    public String getStateNonce() { return stateNonce; }
    public void setStateNonce(String stateNonce) { this.stateNonce = stateNonce; }
}


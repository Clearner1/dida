CREATE TABLE IF NOT EXISTS authorization_connection (
  id VARCHAR(64) PRIMARY KEY,
  user_id VARCHAR(128) NOT NULL,
  provider VARCHAR(32) NOT NULL,
  scope VARCHAR(16) NOT NULL,
  access_token_enc TEXT NOT NULL,
  expires_at TIMESTAMP NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  revoked_at TIMESTAMP NULL,
  state_nonce VARCHAR(128) NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_user_provider ON authorization_connection(user_id, provider);


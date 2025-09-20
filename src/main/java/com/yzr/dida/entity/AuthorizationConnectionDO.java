package com.yzr.dida.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("authorization_connection")
public class AuthorizationConnectionDO{
    public enum Scope { READ, READ_WRITE }
    @TableId("id")
    private String id;

    @TableField("user_id")
    private String userId;

    @TableField("provider")
    private String provider;

    // scope是 mysql 保留字，所以需要加引号
    @TableField(value = "`scope`")
    private String scope; // read | read_write

    @TableField("access_token_enc")
    private String accessTokenEnc;

    @TableField("expires_at")
    private LocalDateTime expiresAt;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("revoked_at")
    private LocalDateTime revokedAt;

    @TableField("state_nonce")
    private String stateNonce;
}

/**
 * | 概念                             | 作用      | 和数据库关系       | 用途                                   |
 * | ------------------------------ | ------- | ------------ | ------------------------------------ |
 * | **DO** (Data Object)           | 数据库实体对象 | **直接对应数据库表** | DAO 层使用，CRUD                         |
 * | **DTO** (Data Transfer Object) | 数据传输对象  | 字段来自 DO，但更灵活 | 系统内部数据传输（Controller ↔ Service ↔ DAO） |
 * | **VO** (View Object)           | 视图对象    | 与数据库关系不大     | Controller 返回前端的数据模型                 |
 */
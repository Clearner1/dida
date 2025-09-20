package com.yzr.dida.services;

import java.util.Map;

public interface IAuthService {
    String buildAuthorizeUrl(String userId, String scopeValue);
    void handleCallback(String userId, String code, String state);
    Map<String, Object> status(String userId);
    void disconnect(String userId);

}

/**
 *   - 生产环境：返回真实OAuth URL
 *   - 测试环境：返回Mock URL，测试更快
 *   - 开发环境：返回简化URL，便于调试
 *   在不同的环境使用同一个接口的不同实现类
 */
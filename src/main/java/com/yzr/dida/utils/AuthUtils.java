package com.yzr.dida.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 处理以下问题
 *   - 空格 → 会被截断
 *   - 中文 → 乱码
 *   - 冒号(:) → URL协议分隔符
 *   - 问号(?) → 查询参数开始
 *   - &符号 → 参数分隔符
 */
public class AuthUtils {
    public static String url(String s) {
        return URLEncoder.encode(s == null ? "" : s, StandardCharsets.UTF_8);
    }
}

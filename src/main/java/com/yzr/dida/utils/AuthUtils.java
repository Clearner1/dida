package com.yzr.dida.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class AuthUtils {
    public static String url(String s) {
        return URLEncoder.encode(s == null ? "" : s, StandardCharsets.UTF_8);
    }
}

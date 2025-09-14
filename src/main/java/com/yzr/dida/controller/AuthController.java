package com.yzr.dida.controller;

import com.yzr.dida.services.servicesImplement.AuthService;
import com.yzr.dida.services.servicesImplement.CurrentUserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth/dida")
public class AuthController {

////    认证相关业务逻辑
//    private final AuthService authService;
////    管理当前登录用户的信息和状态
//    private final CurrentUserService currentUserService;

//    //    认证相关业务逻辑
//    @Autowired
//    private AuthService authService;
//    //    管理当前登录用户的信息和状态
//    @Autowired
//    private CurrentUserService currentUserService;
//    构造函数注入
//    public AuthController(AuthService authService, CurrentUserService currentUserService) {
//        this.authService = authService;
//        this.currentUserService = currentUserService;
//    }
//        --------------------------------------------------------------------------------------
//        构造器注入
    private  final AuthService authService;
    private  final CurrentUserService currentUserService;

    public AuthController(AuthService authService, CurrentUserService currentUserService) {
        this.authService = authService;
        this.currentUserService = currentUserService;
    }

    /**
     * 其他方法进行依赖注入：1，字段注入 2. 构造器注入 3. setter方法注入
     * 1. Setter方法注入
     * 需要@Autowired
     * 需要将对象设置为非final
     * @return
     */
//    @Autowired
//    public void setAuthController(AuthService authService, CurrentUserService currentUserService) {
//        this.authService = authService;
//        this.currentUserService = currentUserService;
//    }


    @GetMapping("/status")
    public Map<String, Object> status() {
        String userId = currentUserService.currentUserId();
        return authService.status(userId);
    }

    @GetMapping("/connect")
    public ResponseEntity<Void> connect(@RequestParam(name = "scope", defaultValue = "read") String scope) {
        String userId = currentUserService.currentUserId();
        String didaScope = "read".equalsIgnoreCase(scope) ? "tasks:read" : "tasks:read tasks:write";
        String authorizeUrl = authService.buildAuthorizeUrl(userId, didaScope);
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, authorizeUrl);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/callback")
    public ResponseEntity<Void> callback(@RequestParam("code") String code,
                                         @RequestParam("state") String state) {
        String userId = currentUserService.currentUserId();
        authService.handleCallback(userId, code, state);
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/settings/integrations?dida=connected");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PostMapping("/disconnect")
    public ResponseEntity<Void> disconnect() {
        String userId = currentUserService.currentUserId();
        authService.disconnect(userId);
        return ResponseEntity.noContent().build();
    }
}


/**
 * Spring容器 → 注入依赖 → AuthController
 * 字段注入的问题：
 * 什么是字段注入？
 *     @Autowired
 *     private CurrentUserService currentUserService;
 *  SpringBoot不建议字段注入的原因：
 *  1. 在运行时才会出现问题
 *  构造函数注入在对象创建的时候就会报问题，如果没有这个对象直接报错
 *  2. 构造器注入，可以对`对象`使用final，保证线程运行的安全
 */
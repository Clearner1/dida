package com.yzr.dida;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import static com.yzr.dida.utils.AuthUtils.url;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
class DidaApplicationTests {

//    @Autowired
//    private IAuthService authService;

//    构造器注入
//    public DidaApplicationTests(IAuthService authService) {
//        this.authService = authService;
//    }
    @Test
    void contextLoads() {
    }

    @Test
    void hello() {
        System.out.println("hello");
    }

}

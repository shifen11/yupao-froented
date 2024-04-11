package com.shifen.usercenter.service;

import java.util.Date;
import java.util.List;

import com.shifen.usercenter.controller.UserController;
import com.shifen.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 用户服务测试
 *
 * @author shifen
 */

@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;
    @Resource
    private UserController userController;
    @Test
    void testAddUser() {
        User user = new User();

        user.setUsername("shifen");
        user.setUserAccount("213");
        user.setAvatarUrl("https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png");

        user.setUserPassword("123456");
        user.setPhone("213");
        user.setEmail("123");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);

    }

    @Test
    void userRegister() {
    }
}
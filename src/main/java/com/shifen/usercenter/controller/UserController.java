package com.shifen.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shifen.usercenter.constant.UserConstant;
import com.shifen.usercenter.model.domain.User;
import com.shifen.usercenter.model.domain.request.UserLoginRequest;
import com.shifen.usercenter.model.domain.request.UserRegisterRequest;
import com.shifen.usercenter.service.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户接口
 *
 * @author shifen
 * @version 1.0.1
 * @date 2023/12/31 1:22
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")

    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        //@RequestBody作用，把前端传来的JSON转为这个对象
        if (userRegisterRequest == null) {
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        userService.userRegister(userAccount, userPassword, checkPassword);
        return 1L;
    }

    @PostMapping("/login")

    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        return userService.userLogin(userAccount, userPassword, request);
    }

    @GetMapping("/current")
    public User getCurrent(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            return null;
        }
        // todo 校验用户是否合法
        Long userId = currentUser.getId();
        User user = userService.getById(userId);

        return userService.getSafetyUser(user);
    }

    @PostMapping("/logout")
    public Integer userLogout(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        // 退出登录
        return userService.userLogout(request);
    }

    @GetMapping("/search")
    public List<User> searchUsers(String userName, HttpServletRequest request) {
        // 仅管理员可以查看所有用户
        if (!isAdmin(request)) {
            return new ArrayList<>();
        }

        // 模糊查询
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(userName)) {
            queryWrapper.like("username", userName);

        }
        List<User> users = userService.list(queryWrapper);

        if (ObjectUtils.isNotEmpty(users)) {
            return new ArrayList<>();
        }
        users.stream().map(user -> {
            //用户信息脱敏
            return userService.getSafetyUser(user);
        }).collect(Collectors.toList());
        return users;


    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return false;
        }

        if (id <= 0) {
            return false;
        }
        return userService.removeById(id);

    }

    /**
     * 判断是否是管理员
     *
     * @param request
     * @return true:是管理员，false：不是管理员
     */
    private boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可以删除用户
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == UserConstant.ADMIN_ROLE;
    }


}

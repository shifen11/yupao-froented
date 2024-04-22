package com.shifen.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shifen.usercenter.common.BaseResponse;
import com.shifen.usercenter.common.ErrorCode;
import com.shifen.usercenter.common.ResultUtils;
import com.shifen.usercenter.constant.UserConstant;
import com.shifen.usercenter.exception.BusinessException;
import com.shifen.usercenter.model.domain.User;
import com.shifen.usercenter.model.domain.request.UserLoginRequest;
import com.shifen.usercenter.model.domain.request.UserRegisterRequest;
import com.shifen.usercenter.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        //@RequestBody作用，把前端传来的JSON转为这个对象
        if (userRegisterRequest == null) {
//            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            return null;
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(result);

    }

    @PostMapping("/login")

    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        User result = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(result);
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
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        // 退出登录
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String userName, HttpServletRequest request) {
        // 仅管理员可以查看所有用户
        if (!isAdmin(request)) {
            ResultUtils.success(new ArrayList<>());
        }

        // 模糊查询
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(userName)) {
            queryWrapper.like("userName", userName);

        }
        List<User> users = userService.list(queryWrapper);

//        QueryWrapper<User> queryWrapper = QueryWrappers.lambdaQuery()
//                .likeRight(User::getUsername, userName);
//        List<User> users = userService.list(queryWrapper);
//        List<User> users;
//        if (StringUtils.isNotBlank(userName)) {
//            users = userService.lambdaQuery()
//                    .like(User::getUsername, userName).list();
//        } else {
//            users = userService.lambdaQuery().list();
//        }
        if (!ObjectUtils.isNotEmpty(users)) {
            ResultUtils.success(new ArrayList<>());
        }
        users.stream().map(user -> {
            //用户信息脱敏
            return userService.getSafetyUser(user);
        }).collect(Collectors.toList());
        return ResultUtils.success(users);


    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return null;
        }

        if (id <= 0) {
            return null;
        }
        boolean result = userService.removeById(id);
        return ResultUtils.success(result);
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

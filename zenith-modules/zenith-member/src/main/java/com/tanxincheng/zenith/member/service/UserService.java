package com.tanxincheng.zenith.member.service;

import com.tanxincheng.zenith.member.dto.UserLoginRequest;
import com.tanxincheng.zenith.member.dto.UserLoginResponse;
import com.tanxincheng.zenith.member.dto.UserRegisterRequest;
import com.tanxincheng.zenith.member.dto.UserInfoResponse;
import com.tanxincheng.zenith.member.entity.User;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 用户注册
     *
     * @param request 注册请求
     * @return 用户ID
     */
    Long register(UserRegisterRequest request);

    /**
     * 用户登录（支持用户名或手机号登录）
     *
     * @param request 登录请求
     * @return 登录响应（包含Token和用户信息）
     */
    UserLoginResponse login(UserLoginRequest request);

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserInfoResponse getUserInfo(Long userId);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户
     */
    User getUserByUsername(String username);

    /**
     * 根据手机号查询用户
     *
     * @param phone 手机号
     * @return 用户
     */
    User getUserByPhone(String phone);
}

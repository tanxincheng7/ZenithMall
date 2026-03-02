package com.tanxincheng.zenith.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tanxincheng.zenith.common.enums.ResultCode;
import com.tanxincheng.zenith.common.exception.BusinessException;
import com.tanxincheng.zenith.common.util.JwtUtil;
import com.tanxincheng.zenith.common.util.PasswordUtil;
import com.tanxincheng.zenith.member.dto.UserLoginRequest;
import com.tanxincheng.zenith.member.dto.UserLoginResponse;
import com.tanxincheng.zenith.member.dto.UserRegisterRequest;
import com.tanxincheng.zenith.member.dto.UserInfoResponse;
import com.tanxincheng.zenith.member.entity.User;
import com.tanxincheng.zenith.member.mapper.UserMapper;
import com.tanxincheng.zenith.member.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public Long register(UserRegisterRequest request) {
        // 检查用户名是否已存在
        User existUser = getUserByUsername(request.getUsername());
        if (existUser != null) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXISTS);
        }

        // 检查手机号是否已存在
        existUser = getUserByPhone(request.getPhone());
        if (existUser != null) {
            throw new BusinessException(ResultCode.PHONE_ALREADY_EXISTS);
        }

        // 检查邮箱是否已存在（可选）
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, request.getEmail());
        existUser = userMapper.selectOne(queryWrapper);
        if (existUser != null) {
            throw new BusinessException(ResultCode.EMAIL_ALREADY_EXISTS);
        }

        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(PasswordUtil.encrypt(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setStatus(1); // 默认正常状态

        userMapper.insert(user);

        return user.getId();
    }

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        // 支持用户名或手机号登录
        User user = getUserByUsername(request.getUsername());
        if (user == null) {
            // 尝试使用手机号登录
            user = getUserByPhone(request.getUsername());
        }

        if (user == null) {
            throw new BusinessException(ResultCode.USERNAME_OR_PASSWORD_ERROR);
        }

        // 检查用户状态（在密码验证之前检查，提高安全性）
        if (user.getStatus() == 0) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND, "用户已被禁用");
        }

        // 验证密码
        if (!PasswordUtil.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.USERNAME_OR_PASSWORD_ERROR);
        }

        // 生成 Token
        String token = JwtUtil.generateToken(user.getId(), user.getUsername());

        // 构建响应
        UserLoginResponse.UserInfo userInfo = new UserLoginResponse.UserInfo(
                user.getId(),
                user.getUsername(),
                user.getPhone(),
                user.getEmail()
        );

        return new UserLoginResponse(token, userInfo);
    }

    @Override
    public UserInfoResponse getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        return new UserInfoResponse(
                user.getId(),
                user.getUsername(),
                user.getPhone(),
                user.getEmail()
        );
    }

    @Override
    public User getUserByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User getUserByPhone(String phone) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);
        return userMapper.selectOne(queryWrapper);
    }
}

package com.tanxincheng.zenith.member.controller;

import com.tanxincheng.zenith.common.constant.JwtConstants;
import com.tanxincheng.zenith.common.enums.ResultCode;
import com.tanxincheng.zenith.common.exception.BusinessException;
import com.tanxincheng.zenith.common.result.Result;
import com.tanxincheng.zenith.common.util.JwtUtil;
import com.tanxincheng.zenith.member.dto.UserLoginRequest;
import com.tanxincheng.zenith.member.dto.UserLoginResponse;
import com.tanxincheng.zenith.member.dto.UserRegisterRequest;
import com.tanxincheng.zenith.member.dto.UserInfoResponse;
import com.tanxincheng.zenith.member.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/api/member/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 用户注册
     *
     * POST /api/member/v1/users/register
     */
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result<Map<String, Long>> register(@Valid @RequestBody @ApiParam("注册信息") UserRegisterRequest request) {
        Long userId = userService.register(request);
        Map<String, Long> data = new HashMap<>();
        data.put("userId", userId);
        return Result.success(data);
    }

    /**
     * 用户登录（支持用户名或手机号登录）
     *
     * POST /api/member/v1/users/login
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<UserLoginResponse> login(@Valid @RequestBody @ApiParam("登录信息") UserLoginRequest request) {
        UserLoginResponse response = userService.login(request);
        return Result.success(response);
    }

    /**
     * 获取用户信息（需要 JWT Token 认证）
     *
     * GET /api/member/v1/users/info
     */
    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public Result<UserInfoResponse> getUserInfo(@RequestHeader(value = JwtConstants.TOKEN_HEADER, required = false) @ApiParam("认证Token") String authHeader) {
        // 提取并验证 Token
        if (authHeader == null || !authHeader.startsWith(JwtConstants.TOKEN_PREFIX)) {
            throw new BusinessException(ResultCode.INVALID_TOKEN, "请提供有效的 Token");
        }

        String token = JwtUtil.extractToken(authHeader);
        if (!JwtUtil.validateToken(token)) {
            throw new BusinessException(ResultCode.INVALID_TOKEN, "Token 无效或已过期");
        }

        // 从 Token 中获取用户ID
        Long userId = JwtUtil.getUserIdFromToken(token);
        if (userId == null) {
            throw new BusinessException(ResultCode.INVALID_TOKEN, "Token 解析失败");
        }

        // 获取用户信息
        UserInfoResponse userInfo = userService.getUserInfo(userId);
        return Result.success(userInfo);
    }
}

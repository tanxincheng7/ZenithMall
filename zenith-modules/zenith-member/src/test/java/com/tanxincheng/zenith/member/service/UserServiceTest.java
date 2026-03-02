package com.tanxincheng.zenith.member.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tanxincheng.zenith.common.enums.ResultCode;
import com.tanxincheng.zenith.common.exception.BusinessException;
import com.tanxincheng.zenith.member.dto.UserLoginRequest;
import com.tanxincheng.zenith.member.dto.UserLoginResponse;
import com.tanxincheng.zenith.member.dto.UserRegisterRequest;
import com.tanxincheng.zenith.member.dto.UserInfoResponse;
import com.tanxincheng.zenith.member.entity.User;
import com.tanxincheng.zenith.member.mapper.UserMapper;
import com.tanxincheng.zenith.member.service.impl.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * UserService 单元测试
 * <p>
 * 使用 Mockito 隔离外部依赖（数据库），只测试业务逻辑
 * <p>
 * 特点：
 * - 不需要真实的数据库连接
 * - 使用 Mock 对象模拟依赖
 * - 快速执行，适合 TDD
 * - 易于定位问题
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService 单元测试")
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegisterRequest registerRequest;
    private UserLoginRequest loginRequest;
    private User testUser;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        registerRequest = new UserRegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setPassword("password123");
        registerRequest.setPhone("13800138000");
        registerRequest.setEmail("test@example.com");

        loginRequest = new UserLoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        // 使用真实的 BCrypt 加密密码
        testUser.setPassword(cn.hutool.crypto.digest.BCrypt.hashpw("password123", cn.hutool.crypto.digest.BCrypt.gensalt()));
        testUser.setPhone("13800138000");
        testUser.setEmail("test@example.com");
        testUser.setStatus(1);
    }

    @Test
    @DisplayName("测试用户注册 - 成功注册新用户")
    void testRegister_Success() {
        // Mock: 用户名不存在
        when(userMapper.selectOne(any(LambdaQueryWrapper.class)))
                .thenReturn(null)
                .thenReturn(null)
                .thenReturn(null);

        // Mock: 插入成功
        when(userMapper.insert(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return 1;
        });

        // 执行测试
        Long userId = userService.register(registerRequest);

        // 验证结果
        assertNotNull(userId);
        assertEquals(1L, userId);

        // 验证调用次数
        verify(userMapper, times(3)).selectOne(any(LambdaQueryWrapper.class)); // 检查用户名、手机号、邮箱
        verify(userMapper, times(1)).insert(any(User.class));

        System.out.println("✅ 用户注册测试通过");
    }

    @Test
    @DisplayName("测试用户注册 - 用户名已存在")
    void testRegister_UsernameExists() {
        // Mock: 用户名已存在
        when(userMapper.selectOne(any(LambdaQueryWrapper.class)))
                .thenReturn(testUser);

        // 执行测试并验证异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.register(registerRequest);
        });

        assertEquals(ResultCode.USER_ALREADY_EXISTS.getCode(), exception.getCode());

        // 验证不会执行插入操作
        verify(userMapper, never()).insert(any(User.class));

        System.out.println("✅ 用户名已存在测试通过");
    }

    @Test
    @DisplayName("测试用户注册 - 手机号已存在")
    void testRegister_PhoneExists() {
        // Mock: 用户名不存在，但手机号已存在
        when(userMapper.selectOne(any(LambdaQueryWrapper.class)))
                .thenReturn(null) // 用户名不存在
                .thenReturn(testUser); // 手机号已存在

        // 执行测试并验证异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.register(registerRequest);
        });

        assertEquals(ResultCode.PHONE_ALREADY_EXISTS.getCode(), exception.getCode());

        verify(userMapper, never()).insert(any(User.class));

        System.out.println("✅ 手机号已存在测试通过");
    }

    @Test
    @DisplayName("测试用户登录 - 使用用户名登录成功")
    void testLogin_WithUsername_Success() {
        // Mock: 找到用户
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testUser);

        // 执行测试
        UserLoginResponse response = userService.login(loginRequest);

        // 验证结果
        assertNotNull(response);
        assertNotNull(response.getToken());
        assertNotNull(response.getUserInfo());
        assertEquals("testuser", response.getUserInfo().getUsername());
        assertEquals("13800138000", response.getUserInfo().getPhone());

        System.out.println("✅ 用户名登录测试通过");
    }

    @Test
    @DisplayName("测试用户登录 - 用户不存在")
    void testLogin_UserNotFound() {
        // Mock: 用户不存在
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);

        // 执行测试并验证异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.login(loginRequest);
        });

        assertEquals(ResultCode.USERNAME_OR_PASSWORD_ERROR.getCode(), exception.getCode());

        System.out.println("✅ 用户不存在测试通过");
    }

    @Test
    @DisplayName("测试用户登录 - 密码错误")
    void testLogin_WrongPassword() {
        // Mock: 找到用户
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testUser);

        // 设置错误的密码
        loginRequest.setPassword("wrongpassword");

        // 执行测试并验证异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.login(loginRequest);
        });

        assertEquals(ResultCode.USERNAME_OR_PASSWORD_ERROR.getCode(), exception.getCode());

        System.out.println("✅ 密码错误测试通过");
    }

    @Test
    @DisplayName("测试用户登录 - 用户被禁用")
    void testLogin_UserDisabled() {
        // Mock: 找到用户但状态为禁用
        testUser.setStatus(0);
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testUser);

        // 执行测试并验证异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.login(loginRequest);
        });

        assertEquals(ResultCode.USER_NOT_FOUND.getCode(), exception.getCode());

        System.out.println("✅ 用户被禁用测试通过");
    }

    @Test
    @DisplayName("测试获取用户信息 - 成功获取")
    void testGetUserInfo_Success() {
        Long userId = 1L;

        // Mock: 找到用户
        when(userMapper.selectById(userId)).thenReturn(testUser);

        // 执行测试
        UserInfoResponse userInfo = userService.getUserInfo(userId);

        // 验证结果
        assertNotNull(userInfo);
        assertEquals(userId, userInfo.getId());
        assertEquals("testuser", userInfo.getUsername());
        assertEquals("13800138000", userInfo.getPhone());
        assertEquals("test@example.com", userInfo.getEmail());

        verify(userMapper, times(1)).selectById(userId);

        System.out.println("✅ 获取用户信息测试通过");
    }

    @Test
    @DisplayName("测试获取用户信息 - 用户不存在")
    void testGetUserInfo_UserNotFound() {
        Long userId = 999L;

        // Mock: 用户不存在
        when(userMapper.selectById(userId)).thenReturn(null);

        // 执行测试并验证异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.getUserInfo(userId);
        });

        assertEquals(ResultCode.USER_NOT_FOUND.getCode(), exception.getCode());

        System.out.println("✅ 用户不存在测试通过");
    }

    @Test
    @DisplayName("测试根据用户名查询用户")
    void testGetUserByUsername_Success() {
        String username = "testuser";

        // Mock: 找到用户
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testUser);

        // 执行测试
        User user = userService.getUserByUsername(username);

        // 验证结果
        assertNotNull(user);
        assertEquals(username, user.getUsername());

        System.out.println("✅ 根据用户名查询测试通过");
    }

    @Test
    @DisplayName("测试根据手机号查询用户")
    void testGetUserByPhone_Success() {
        String phone = "13800138000";

        // Mock: 找到用户
        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(testUser);

        // 执行测试
        User user = userService.getUserByPhone(phone);

        // 验证结果
        assertNotNull(user);
        assertEquals(phone, user.getPhone());

        System.out.println("✅ 根据手机号查询测试通过");
    }
}

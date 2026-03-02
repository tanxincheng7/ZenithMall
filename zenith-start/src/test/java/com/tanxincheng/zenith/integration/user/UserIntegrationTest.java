package com.tanxincheng.zenith.integration.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.tanxincheng.zenith.common.constant.JwtConstants;
import com.tanxincheng.zenith.integration.BaseIntegrationTest;
import com.tanxincheng.zenith.member.dto.UserLoginRequest;
import com.tanxincheng.zenith.member.dto.UserRegisterRequest;
import com.tanxincheng.zenith.member.entity.User;
import com.tanxincheng.zenith.member.mapper.UserMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.StringUtils;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 用户模块集成测试
 * <p>
 * 测试用户注册、登录、查询用户信息的完整流程
 * 验证与数据库的联通性
 * <p>
 * 继承自 BaseIntegrationTest，具有以下特点：
 * - 启动完整的 Spring Boot 应用
 * - 连接真实测试数据库
 * - 测试后自动回滚事务
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("用户模块集成测试")
public class UserIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserMapper userMapper;

    private static String testUsername;
    private static String testPhone;
    private static String testEmail;
    private static String testPassword;
    private static String jwtToken;

    @BeforeAll
    static void initTestData() {
        // 生成唯一的测试数据，避免重复
        String timestamp = String.valueOf(System.currentTimeMillis());
        testUsername = "user" + timestamp.substring(timestamp.length() - 8); // 12个字符
        testPhone = "138" + timestamp.substring(timestamp.length() - 8);
        testEmail = "test" + timestamp.substring(timestamp.length() - 8) + "@example.com";
        testPassword = "test123456";

        System.out.println("========================================");
        System.out.println("用户模块集成测试");
        System.out.println("测试用户名: " + testUsername);
        System.out.println("测试手机号: " + testPhone);
        System.out.println("测试邮箱: " + testEmail);
        System.out.println("========================================");
    }

    @Test
    @Order(1)
    @DisplayName("集成测试：用户注册功能 - 正常注册并验证数据库")
    public void testRegister_Success() throws Exception {
        // 准备注册数据
        UserRegisterRequest request = new UserRegisterRequest();
        request.setUsername(testUsername);
        request.setPassword(testPassword);
        request.setPhone(testPhone);
        request.setEmail(testEmail);

        String jsonRequest = objectMapper.writeValueAsString(request);

        // 执行注册请求
        MvcResult result = mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.userId").exists())
                .andExpect(jsonPath("$.data.userId", greaterThan(0)))
                .andReturn();

        // 验证数据库中是否真的插入了数据（关键：验证数据库联通性）
        User user = userMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                .eq(User::getUsername, testUsername));

        assertNotNull(user, "用户应该存在于数据库中");
        assertEquals(testUsername, user.getUsername(), "用户名应该匹配");
        assertEquals(testPhone, user.getPhone(), "手机号应该匹配");
        assertEquals(testEmail, user.getEmail(), "邮箱应该匹配");
        assertEquals(1, user.getStatus(), "用户状态应该是正常");
        assertNotNull(user.getCreateTime(), "创建时间不应该为空");
        assertNotNull(user.getUpdateTime(), "更新时间不应该为空");

        System.out.println("✅ 用户注册测试通过 - 数据库联通验证成功");
        System.out.println("注册用户ID: " + user.getId());
    }

    @Test
    @Order(2)
    @DisplayName("集成测试：用户注册功能 - 用户名重复验证")
    public void testRegister_DuplicateUsername() throws Exception {
        // 先注册一个用户
        UserRegisterRequest firstRequest = new UserRegisterRequest();
        firstRequest.setUsername("duplicate_test");
        firstRequest.setPassword("test123456");
        firstRequest.setPhone("13900000001");
        firstRequest.setEmail("duplicate1@test.com");

        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        // 验证数据库中有该用户
        User firstUser = userMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                .eq(User::getUsername, "duplicate_test"));
        assertNotNull(firstUser, "第一个用户应该存在于数据库中");

        // 尝试用相同的用户名再次注册
        UserRegisterRequest duplicateRequest = new UserRegisterRequest();
        duplicateRequest.setUsername("duplicate_test");
        duplicateRequest.setPassword("test123456");
        duplicateRequest.setPhone("13900000002");
        duplicateRequest.setEmail("duplicate2@test.com");

        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(not(200)))
                .andExpect(jsonPath("$.message").value(containsString("用户已存在")));

        System.out.println("✅ 用户名重复测试通过 - 数据库唯一约束验证成功");
    }

    @Test
    @Order(3)
    @DisplayName("集成测试：用户登录功能 - 使用用户名登录并获取Token")
    public void testLogin_WithUsername() throws Exception {
        // 先注册一个用户
        UserRegisterRequest registerRequest = new UserRegisterRequest();
        registerRequest.setUsername("login_test_user");
        registerRequest.setPassword("login123456");
        registerRequest.setPhone("13911111111");
        registerRequest.setEmail("loginuser@test.com");

        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        // 验证数据库中有该用户
        User registeredUser = userMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                .eq(User::getUsername, "login_test_user"));
        assertNotNull(registeredUser, "注册用户应该存在于数据库中");

        // 使用用户名登录
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUsername("login_test_user");
        loginRequest.setPassword("login123456");

        MvcResult result = mockMvc.perform(post("/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.token").exists())
                .andExpect(jsonPath("$.data.token", not(emptyString())))
                .andExpect(jsonPath("$.data.userInfo").exists())
                .andExpect(jsonPath("$.data.userInfo.id").exists())
                .andExpect(jsonPath("$.data.userInfo.id", is(registeredUser.getId().intValue())))
                .andExpect(jsonPath("$.data.userInfo.username").value("login_test_user"))
                .andExpect(jsonPath("$.data.userInfo.phone").value("13911111111"))
                .andExpect(jsonPath("$.data.userInfo.email").value("loginuser@test.com"))
                .andReturn();

        // 保存Token用于后续测试
        String response = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(response);
        jwtToken = jsonNode.path("data").path("token").asText();

        assertNotNull(jwtToken, "Token不应该为空");
        assertTrue(jwtToken.length() > 20, "Token长度应该大于20");

        System.out.println("✅ 用户名登录测试通过 - Token获取成功");
        System.out.println("Token: " + jwtToken.substring(0, Math.min(20, jwtToken.length())) + "...");
    }

    @Test
    @Order(4)
    @DisplayName("集成测试：用户登录功能 - 使用手机号登录")
    public void testLogin_WithPhone() throws Exception {
        // 先注册一个用户
        UserRegisterRequest registerRequest = new UserRegisterRequest();
        registerRequest.setUsername("phone_login_test");
        registerRequest.setPassword("phone123456");
        registerRequest.setPhone("13922222222");
        registerRequest.setEmail("phonelogin@test.com");

        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        // 使用手机号登录
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUsername("13922222222"); // 使用手机号
        loginRequest.setPassword("phone123456");

        mockMvc.perform(post("/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token").exists())
                .andExpect(jsonPath("$.data.userInfo.username").value("phone_login_test"));

        System.out.println("✅ 手机号登录测试通过");
    }

    @Test
    @Order(5)
    @DisplayName("集成测试：用户登录功能 - 密码错误验证")
    public void testLogin_WrongPassword() throws Exception {
        // 先注册一个用户
        UserRegisterRequest registerRequest = new UserRegisterRequest();
        registerRequest.setUsername("wrongpwd_test");
        registerRequest.setPassword("correct123");
        registerRequest.setPhone("13933333333");
        registerRequest.setEmail("wrongpwd@test.com");

        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        // 使用错误密码登录
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUsername("wrongpwd_test");
        loginRequest.setPassword("wrong123");

        mockMvc.perform(post("/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(not(200)))
                .andExpect(jsonPath("$.message").value(containsString("用户名或密码")));

        System.out.println("✅ 密码错误测试通过");
    }

    @Test
    @Order(6)
    @DisplayName("集成测试：获取用户信息功能 - 使用有效Token")
    public void testGetUserInfo_ValidToken() throws Exception {
        // 先注册并登录获取Token
        UserRegisterRequest registerRequest = new UserRegisterRequest();
        registerRequest.setUsername("info_test_user");
        registerRequest.setPassword("info123456");
        registerRequest.setPhone("13944444444");
        registerRequest.setEmail("infouser@test.com");

        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        // 从数据库中获取用户信息
        User registeredUser = userMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                .eq(User::getUsername, "info_test_user"));
        assertNotNull(registeredUser, "注册用户应该存在于数据库中");

        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUsername("info_test_user");
        loginRequest.setPassword("info123456");

        MvcResult loginResult = mockMvc.perform(post("/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String loginResponse = loginResult.getResponse().getContentAsString();
        String token = objectMapper.readTree(loginResponse).path("data").path("token").asText();

        // 使用Token获取用户信息
        mockMvc.perform(get("/v1/users/info")
                        .header(JwtConstants.TOKEN_HEADER, JwtConstants.TOKEN_PREFIX + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.id", is(registeredUser.getId().intValue())))
                .andExpect(jsonPath("$.data.username").value("info_test_user"))
                .andExpect(jsonPath("$.data.phone").value("13944444444"))
                .andExpect(jsonPath("$.data.email").value("infouser@test.com"));

        System.out.println("✅ 获取用户信息测试通过 - 数据库查询验证成功");
    }

    @Test
    @Order(7)
    @DisplayName("集成测试：获取用户信息功能 - 无Token")
    public void testGetUserInfo_NoToken() throws Exception {
        mockMvc.perform(get("/v1/users/info"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(not(200)))
                .andExpect(jsonPath("$.message").value(containsString("Token")));

        System.out.println("✅ 无Token测试通过");
    }

    @Test
    @Order(8)
    @DisplayName("集成测试：获取用户信息功能 - 无效Token")
    public void testGetUserInfo_InvalidToken() throws Exception {
        String invalidToken = "invalid.token.string";

        mockMvc.perform(get("/v1/users/info")
                        .header(JwtConstants.TOKEN_HEADER, JwtConstants.TOKEN_PREFIX + invalidToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(not(200)))
                .andExpect(jsonPath("$.message").value(containsString("Token")));

        System.out.println("✅ 无效Token测试通过");
    }

    @Test
    @Order(9)
    @DisplayName("集成测试：完整的用户流程 - 注册→登录→查询信息")
    public void testCompleteUserFlow() throws Exception {
        String flowUsername = generateUniqueValue("flow_test");
        String flowPhone = generateUniquePhone();
        String flowEmail = generateUniqueEmail("flow");
        String flowPassword = "flow123456";

        System.out.println("开始完整流程测试...");
        System.out.println("用户名: " + flowUsername);
        System.out.println("手机号: " + flowPhone);

        // 1. 注册
        UserRegisterRequest registerRequest = new UserRegisterRequest();
        registerRequest.setUsername(flowUsername);
        registerRequest.setPassword(flowPassword);
        registerRequest.setPhone(flowPhone);
        registerRequest.setEmail(flowEmail);

        MvcResult registerResult = mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.userId").exists())
                .andReturn();

        // 从响应中获取用户ID
        String registerResponse = registerResult.getResponse().getContentAsString();
        Long userId = objectMapper.readTree(registerResponse).path("data").path("userId").asLong();

        // 验证数据库
        User dbUser = userMapper.selectById(userId);
        assertNotNull(dbUser, "用户应该存在于数据库中");
        assertEquals(flowUsername, dbUser.getUsername());
        assertEquals(flowPhone, dbUser.getPhone());
        assertEquals(flowEmail, dbUser.getEmail());
        System.out.println("✅ 步骤1: 注册成功，用户ID: " + dbUser.getId());

        // 2. 登录
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUsername(flowUsername);
        loginRequest.setPassword(flowPassword);

        MvcResult loginResult = mockMvc.perform(post("/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token").exists())
                .andExpect(jsonPath("$.data.userInfo.username").value(flowUsername))
                .andReturn();

        String loginResponse = loginResult.getResponse().getContentAsString();
        String token = objectMapper.readTree(loginResponse).path("data").path("token").asText();
        assertTrue(StringUtils.hasText(token), "Token应该存在");
        System.out.println("✅ 步骤2: 登录成功，获取Token");

        // 3. 获取用户信息
        mockMvc.perform(get("/v1/users/info")
                        .header(JwtConstants.TOKEN_HEADER, JwtConstants.TOKEN_PREFIX + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value(flowUsername))
                .andExpect(jsonPath("$.data.phone").value(flowPhone))
                .andExpect(jsonPath("$.data.email").value(flowEmail));

        System.out.println("✅ 步骤3: 获取用户信息成功");
        System.out.println("========================================");
        System.out.println("完整流程测试全部通过！");
        System.out.println("✅ 数据库联通性验证成功");
        System.out.println("✅ JWT Token 认证验证成功");
        System.out.println("✅ API 接口协作验证成功");
        System.out.println("========================================");
    }
}

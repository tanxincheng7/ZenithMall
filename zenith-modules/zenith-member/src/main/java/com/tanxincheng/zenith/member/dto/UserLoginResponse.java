package com.tanxincheng.zenith.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录响应
 */
@ApiModel("用户登录响应")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponse {

    /**
     * JWT Token
     */
    @ApiModelProperty(value = "JWT Token", example = "eyJhbGciOiJIUzUxMiJ9...")
    private String token;

    /**
     * 用户信息
     */
    @ApiModelProperty("用户信息")
    private UserInfo userInfo;

    @ApiModel("用户信息")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        @ApiModelProperty("用户ID")
        private Long id;

        @ApiModelProperty("用户名")
        private String username;

        @ApiModelProperty("手机号")
        private String phone;

        @ApiModelProperty("邮箱")
        private String email;
    }
}

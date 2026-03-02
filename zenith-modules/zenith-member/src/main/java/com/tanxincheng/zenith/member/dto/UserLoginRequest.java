package com.tanxincheng.zenith.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录请求
 */
@ApiModel("用户登录请求")
@Data
public class UserLoginRequest {

    /**
     * 用户名或手机号
     */
    @ApiModelProperty(value = "用户名或手机号", required = true, example = "test")
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", required = true, example = "123456")
    @NotBlank(message = "密码不能为空")
    private String password;
}

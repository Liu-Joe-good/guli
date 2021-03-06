package com.atguigu.ucenter.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class RegisterVo implements Serializable {
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty(value = "昵称")
    private String nickname;
    @ApiModelProperty(value = "手机验证码")
    private String code;
}

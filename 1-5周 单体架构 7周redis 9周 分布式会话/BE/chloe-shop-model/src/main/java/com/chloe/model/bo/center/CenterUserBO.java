package com.chloe.model.bo.center;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * 用户信息BO
 */
public class CenterUserBO {
    private String username;

    private String password;

    private String confirmPassword;

    @NotBlank(message = "用户昵称不能为空")
    @Length(max = 12, message = "昵称长度不能超过12位")
    private String nickname;

    @NotBlank(message = "用户姓名不能为空")
    @Length(max = 12, message = "姓名长度不能超过12位")
    private String realname;

    @Pattern(regexp = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$",
            message = "手机号格式不正确")
    private String mobile;

    @Email(message = "邮箱格式不正确")
    private String email;

    @Max(value = 2, message = "性别选择不正确")
    private Integer sex;

    private Date birthday;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}

package cn.mrcode.foodiedev.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * BO：前端传递给后端的业务对象
 *
 * @author mrcode
 * @date 2021/2/13 11:53
 */
@ApiModel(value = "用户对象 BO", description = "从客户端，由用户传入的数据封装在此")
public class UserBO {
    @ApiModelProperty(value = "用户名", name = "username", example = "例如  admin", required = true)
    private String username;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("确认密码")
    private String confirmPassword;

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
}

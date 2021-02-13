package cn.mrcode.foodiedev.pojo.bo;

/**
 * BO：前端传递给后端的业务对象
 *
 * @author mrcode
 * @date 2021/2/13 11:53
 */
public class UserBO {
    private String username;
    private String password;
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

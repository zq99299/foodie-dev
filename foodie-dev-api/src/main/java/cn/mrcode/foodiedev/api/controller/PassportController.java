package cn.mrcode.foodiedev.api.controller;

import cn.mrcode.foodiedev.common.util.CookieUtils;
import cn.mrcode.foodiedev.common.util.JSONResult;
import cn.mrcode.foodiedev.common.util.JsonUtils;
import cn.mrcode.foodiedev.common.util.MD5Utils;
import cn.mrcode.foodiedev.pojo.Users;
import cn.mrcode.foodiedev.pojo.bo.UserBO;
import cn.mrcode.foodiedev.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "注册登录", tags = {"用户注册登录的相关接口"}) // API 分组
@RestController
@RequestMapping("/passport")
public class PassportController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户名是否存在", notes = "判断用户名是否存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名")
    })
    public JSONResult usernameIsExist(@RequestParam String username) {
        // 1. 判断用户名不能为空
        if (StringUtils.isBlank(username)) {
            return JSONResult.errorMsg("用户名不能为空");
        }

        // 2. 查找注册的用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return JSONResult.errorMsg("用户名已存在");
        }
        // 3. 用户名没有重复
        return JSONResult.ok();
    }

    @ApiOperation(value = "用户注册", notes = "用户用户注册", httpMethod = "POST")
    @PostMapping("/regist")
    public JSONResult regist(@RequestBody UserBO userBO,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPassword = userBO.getConfirmPassword();
        // 0. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password) ||
                StringUtils.isBlank(confirmPassword)) {
            return JSONResult.errorMsg("用户名或密码不能为空");
        }
        // 1. 查询用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return JSONResult.errorMsg("用户名已经存在");
        }
        // 2. 密码长度不能少于 6 位
        if (password.length() < 6) {
            return JSONResult.errorMsg("密码长度不能少于 6");
        }
        // 3. 判断两次密码是否一致
        if (!password.equals(confirmPassword)) {
            return JSONResult.errorMsg("两次密码输入不一致");
        }
        // 4. 实现注册
        Users user = userService.createUser(userBO);

        // 脱敏信息
        setNullProperty(user);

        // 设置 cookie
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(user), true);

        return JSONResult.ok(user);
    }

    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public JSONResult login(@RequestBody UserBO userBO,
                            HttpServletRequest request,
                            HttpServletResponse response) throws Exception {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        // 0. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password)) {
            return JSONResult.errorMsg("用户名或密码不能为空");
        }
        password = MD5Utils.getMD5Str(password);
        Users user = userService.queryUserForLogin(username, password);
        if (user == null) {
            return JSONResult.errorMsg("用户名或密码不正确");
        }

        // 脱敏信息
        setNullProperty(user);

        // 设置 cookie
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(user), true);

        return JSONResult.ok(user);
    }

    /**
     * <pre>
     * 由于目前响应对象是 数据库实体对象，不适用适用 @JsonIgone 直接抹去不显示该字段信息
     * 直接重置为空
     * </pre>
     *
     * @param user
     */
    private void setNullProperty(Users user) {
        user.setPassword(null);
        user.setMobile(null);
        user.setEmail(null);
        user.setCreatedTime(null);
        user.setUpdatedTime(null);
        user.setBirthday(null);
    }

    @ApiOperation(value = "用户退出登录")
    @PostMapping("/logout")
    public JSONResult logout(@RequestParam String userId,
                             HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        // 这里暂时没有使用到 session 相关信息，不用清理，同样后续还会清空购物车
        // 但是需要清空 cookie 里面的信息
        CookieUtils.deleteCookie(request, response, "user");
        return JSONResult.ok();
    }
}

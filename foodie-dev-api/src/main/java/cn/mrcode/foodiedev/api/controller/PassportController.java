package cn.mrcode.foodiedev.api.controller;

import cn.mrcode.foodiedev.common.util.*;
import cn.mrcode.foodiedev.pojo.Users;
import cn.mrcode.foodiedev.pojo.bo.ShopcartBO;
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
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Api(value = "注册登录", tags = {"用户注册登录的相关接口"}) // API 分组
@RestController
@RequestMapping("/passport")
public class PassportController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisOperator redisOperator;

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

        // 同步购物车数据
        synchShopcartData(user.getId(), request, response);
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

        // 生成用户 token 存入 redis 会话
        // 同步购物车数据
        synchShopcartData(user.getId(), request, response);
        return JSONResult.ok(user);
    }

    private void synchShopcartData(String userId,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {

        // 获取 redis 中的购物车数据
        String key = BaseController.FOODIE_SHOPCART + ":" + userId;
        String redisShopcartJson = redisOperator.get(key);
        // 获取 cookie 中的数据
        String cookieShopcartJson = CookieUtils.getCookieValue(request, BaseController.FOODIE_SHOPCART, true);

        if (StringUtils.isBlank(redisShopcartJson)) {
            // redis 为空，cookie 不为空，直接吧 cookie 放入 redis 中
            if (StringUtils.isNotBlank(cookieShopcartJson)) {
                redisOperator.set(key, cookieShopcartJson);
            }
        } else {
            // redis 不为空，cookie 不为空，合并 cookie 和 redis 中购物商品
            if (StringUtils.isNotBlank(cookieShopcartJson)) {
                /*
                   1. 已经存在的，用 cookie 中对应的数量，覆盖 redis（参考京东）
                   2. 该商品标记诶待删除，统一放入一个待删除的 list
                   3. 从 cookie 中清理所有的待删除 list
                   4. 合并 redis 和 cookie 中的数据
                   5. 更新到 redis 和 cookie 中
                 */
                List<ShopcartBO> redisShopcarts = JsonUtils.jsonToList(redisShopcartJson, ShopcartBO.class);
                List<ShopcartBO> cookieShopcarts = JsonUtils.jsonToList(cookieShopcartJson, ShopcartBO.class);

                List<ShopcartBO> pendingDeleteList = new ArrayList<>();
                for (ShopcartBO redisShopcart : redisShopcarts) {
                    String redisSpecId = redisShopcart.getSpecId();
                    for (ShopcartBO cookieShopcart : cookieShopcarts) {
                        String cookieSpecId = cookieShopcart.getSpecId();

                        // 如果都存在相同的商品：以 cookie 中的数量为准
                        if (redisSpecId.equals(cookieSpecId)) {
                            redisShopcart.setBuyCounts(cookieShopcart.getBuyCounts());
                            pendingDeleteList.add(cookieShopcart);
                        }
                    }
                }
                // 将 cookie 中删除对应的覆盖过的商品数据
                cookieShopcarts.removeAll(pendingDeleteList);
                // 合并两个 list
                redisShopcarts.addAll(cookieShopcarts);
                // 更新 redis 和 cookie 中的数据
                String meragedShopcartJson = JsonUtils.objectToJson(redisShopcarts);
                redisOperator.set(key, meragedShopcartJson);
                CookieUtils.setCookie(request, response,
                        BaseController.FOODIE_SHOPCART,
                        meragedShopcartJson, true);
            } else {
                //  redis 不为空，cookie 为空，直接把 redis 覆盖
                CookieUtils.setCookie(request, response,
                        BaseController.FOODIE_SHOPCART,
                        redisShopcartJson, true);
            }
        }
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

        // 清理 cookie 中的购物车，但是 redis 中不要清理，相当于购物车数据已经保存在服务端了
        CookieUtils.deleteCookie(request, response, BaseController.FOODIE_SHOPCART);
        return JSONResult.ok();
    }
}

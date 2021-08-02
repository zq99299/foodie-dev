package cn.mrcode.foodiedev.sso;

import cn.mrcode.foodiedev.common.util.*;
import cn.mrcode.foodiedev.pojo.Users;
import cn.mrcode.foodiedev.pojo.vo.UsersVO;
import cn.mrcode.foodiedev.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class SSOController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisOperator redisOperator;
    public static final String REDIS_USER_TOKEN = "redis_user_token";
    public static final String REDIS_USER_TICKET = "redis_user_ticket";
    public static final String REDIS_TMP_TICKET = "redis_tmp_ticket";
    public static final String COOKIE_USER_TICKET = "cookie_user_ticket";

    @GetMapping("/login")
    public String login(String retrunUrl,
                        Model model,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        // 将传递来的 url 放进 model 中，在 thymeleaf 页面中保存下来
        model.addAttribute("retrunUrl", retrunUrl);
        // todo: 后续再来实现具体的逻辑
        // 跳转到 login 页面
        return "login";
    }

    /**
     * 用户登录：CAS 的统一登录接口
     * <pre>
     *  目的：
     *      1. 登录后创建用户的全局会话   --> uniqueToken
     *      2. 创建用户全局门票，用于标识在 CAS 端是否登录  --> userTicket
     *      3. 创建用户的临时票据，用于回传会跳 --> userTicket
     * </pre>
     *
     * @param username
     * @param password
     * @param retrunUrl
     * @param model
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @PostMapping("doLogin")
    public String doLogin(String username,
                          String password,
                          String retrunUrl,
                          Model model,
                          HttpServletRequest request,
                          HttpServletResponse response) throws Exception {

        // 0. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password)) {
            model.addAttribute("errmsg", "用户名或密码不能为空");
            return "login";
        }

        // 1. 实现登录
        password = MD5Utils.getMD5Str(password);
        Users user = userService.queryUserForLogin(username, password);
        if (user == null) {
            model.addAttribute("errmsg", "用户名或密码不正确");
            return "login";
        }

        // 2. 实现用户的 redis 会话
        UsersVO usersVO = convertVo(user);

        // 3. 生成 ticket 门票，全局门票，代表用户在 CAS 端登录过
        String userTicket = UUID.randomUUID().toString().trim();

        // 3.1 全局门票需要放入到 CAS 端的 cookie 中，下次在访问 CAS 系统，则会通过该全局门票判定是否已经在 CAS 端登录过
        setCookie(COOKIE_USER_TICKET, userTicket, response);

        // 4. userTicket 关联用户 ID，并且放入到 redis 中
        redisOperator.set(REDIS_USER_TICKET + ":" + userTicket, usersVO.getId());

        // 5. 生成临时票据：用于回跳到调用端网站，是由 CAS 端所签发的一个一次性的临时 ticket
        String tmpTicket = createTmpTicket();

        return "login";
        // return "redirect:" + retrunUrl + "?tmpTicket=" + tmpTicket;
    }

    private UsersVO convertVo(Users user) {
        // 实现用户的 redis 会话
        String uniqueToken = UUID.randomUUID().toString();
        // 永远不过期，除非自动退出
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(user, usersVO);
        usersVO.setUniqueToken(uniqueToken);
        // redis_user_token
        // 将用户信息放到 redis 中
        redisOperator.set(REDIS_USER_TOKEN + ":" + user.getId(), JsonUtils.objectToJson(usersVO));
        return usersVO;
    }

    /**
     * 创建临时票据
     *
     * @return
     */
    private String createTmpTicket() {
        String tmpTicket = UUID.randomUUID().toString().trim();
        try {
            // 放入 redis 中，value 使用 md5 加密，并设置过期时间为 10 分钟
            redisOperator.set(REDIS_TMP_TICKET + ":" + tmpTicket, MD5Utils.getMD5Str(tmpTicket), 600);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpTicket;
    }

    private void setCookie(String key, String val, HttpServletResponse response) {
        Cookie cookie = new Cookie(key, val);
        cookie.setDomain("sso.com");
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
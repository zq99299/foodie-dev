package cn.mrcode.tcc.controller;

import cn.mrcode.tcc.dao.db01.bean.User;
import cn.mrcode.tcc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mrcode
 * @date 2022/2/10 21:34
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户列表，跳转到列表页面
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("/userList")
    public String userList(ModelMap modelMap) {
        List<User> allUsers = userService.getAllUsers();
        modelMap.put("users", allUsers);
        return "user/user-list";
    }

    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    @RequestMapping("/delUser")
    @ResponseBody
    public Map<String, Object> delUser(@RequestParam Integer userId) {
        int result = userService.delUser(userId);
        Map<String, Object> map = new HashMap<>();
        map.put("status", result);
        return map;
    }
}

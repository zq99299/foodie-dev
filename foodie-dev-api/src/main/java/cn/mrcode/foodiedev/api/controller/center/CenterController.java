package cn.mrcode.foodiedev.api.controller.center;

import cn.mrcode.foodiedev.common.util.JSONResult;
import cn.mrcode.foodiedev.pojo.Users;
import cn.mrcode.foodiedev.service.center.CenterUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mrcode
 * @date 2021/2/18 20:55
 */
@Api(value = "用户中心", tags = {"用户中心相关接口"})
@RequestMapping("center")
@RestController
public class CenterController {
    @Autowired
    private CenterUserService centerUserService;

    @ApiOperation(value = "获取用户信息", notes = "获取用户信息", httpMethod = "GET")
    @GetMapping("userInfo")
    public JSONResult userInfo(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId) {

        Users user = centerUserService.queryUserInfo(userId);
        return JSONResult.ok(user);
    }
}

package cn.mrcode.foodiedev.fs.controller;

import cn.mrcode.foodiedev.common.util.CookieUtils;
import cn.mrcode.foodiedev.common.util.DateUtil;
import cn.mrcode.foodiedev.common.util.JSONResult;
import cn.mrcode.foodiedev.common.util.JsonUtils;
import cn.mrcode.foodiedev.fs.service.FdfsService;
import cn.mrcode.foodiedev.pojo.Users;
import cn.mrcode.foodiedev.pojo.vo.UsersVO;
import cn.mrcode.foodiedev.service.center.CenterUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author mrcode
 * @date 2021/2/18 21:07
 */
@Api(value = "用户信息接口", tags = {"用户信息相关接口"})
@RestController
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {
    @Autowired
    private CenterUserService centerUserService;
    @Autowired
    private FdfsService fdfsService;

    /**
     * 替代的原来接口：cn.mrcode.foodiedev.api.controller.center.CenterUserController#uploadFace(java.lang.String, org.springframework.web.multipart.MultipartFile, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     *
     * @param userId
     * @param file
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "用户头像修改", notes = "用户头像修改", httpMethod = "POST")
    @PostMapping("uploadFace")
    public JSONResult uploadFace(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "file", value = "用户头像", required = true) MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        String userFaceUrl = null;
        // 开始文件上传
        if (file != null) {
            // 获得文件上传的文件名称
            String fileName = file.getOriginalFilename();

            if (StringUtils.isNotBlank(fileName)) {

                // 文件重命名  imooc-face.png -> ["imooc-face", "png"]
                String fileNameArr[] = fileName.split("\\.");

                // 获取文件的后缀名
                String suffix = fileNameArr[fileNameArr.length - 1];

                if (!suffix.equalsIgnoreCase("png") &&
                        !suffix.equalsIgnoreCase("jpg") &&
                        !suffix.equalsIgnoreCase("jpeg")) {
                    return JSONResult.errorMsg("图片格式不正确！");
                }
                userFaceUrl = fdfsService.upload(file, suffix);
                System.out.println(userFaceUrl);
            }
        } else {
            return JSONResult.errorMsg("文件不能为空！");
        }

        // 由于浏览器可能存在缓存的情况，所以在这里，我们需要加上时间戳来保证更新后的图片可以及时刷新
        // 拼接上 fastfs 的访问地址
        String finalUserFaceUrl = "http://192.168.56.106:8888/" + userFaceUrl + "?t=" + DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);

        // 更新用户头像到数据库
        Users userResult = centerUserService.updateUserFace(userId, finalUserFaceUrl);

        // 转换，并重新生成 token 放入 redis 中
        UsersVO usersVO = convertVo(userResult);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(usersVO), true);

        return JSONResult.ok();
    }
}

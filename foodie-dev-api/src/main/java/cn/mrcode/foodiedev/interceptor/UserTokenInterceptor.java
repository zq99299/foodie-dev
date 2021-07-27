package cn.mrcode.foodiedev.interceptor;

import cn.mrcode.foodiedev.api.controller.BaseController;
import cn.mrcode.foodiedev.common.util.JSONResult;
import cn.mrcode.foodiedev.common.util.JsonUtils;
import cn.mrcode.foodiedev.common.util.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author mrcode
 * @date 2021/7/27 21:57
 */
public class UserTokenInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisOperator redisOperator;

    /**
     * 以 json 形式响应错误信息
     *
     * @param response
     * @param result
     */
    public void returnErrorResponse(HttpServletResponse response, JSONResult result) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/json");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(JsonUtils.objectToJson(result));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用 controller 方法之前
     * <pre>
     *     拦截处理程序的执行。 在 HandlerMapping 确定合适的处理程序对象之后，但在 HandlerAdapter 调用处理程序之前调用
     * </pre>
     *
     * @param request
     * @param response
     * @param handler
     * @return false : 请求被拦截，验证出现问题； true：验证通过，可以放行
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String headerUserId = request.getHeader("headerUserId");
        String headerUserToken = request.getHeader("headerUserToken");

        if (StringUtils.isBlank(headerUserId) || StringUtils.isBlank(headerUserToken)) {
            returnErrorResponse(response, JSONResult.errorMsg("请登录..."));
            return false;
        }

        String tokenKey = BaseController.REDIS_USER_TOKEN + ":" + headerUserId;
        String token = redisOperator.get(tokenKey);
        if (StringUtils.isBlank(token)) {
            returnErrorResponse(response, JSONResult.errorMsg("请登录..."));
            return false;
        }

        // token 不匹配
        if (!headerUserToken.equals(token)) {
            returnErrorResponse(response, JSONResult.errorMsg("账户在异地登录..."));
            return false;
        }

        // 校验通过，直接放行
        return true;
    }

    /**
     * 调用 controller 方法之后，渲染视图之前
     * <pre>
     *     拦截截处理程序的执行。 在 HandlerAdapter 实际调用处理程序之后调用，
     *     但在 DispatcherServlet 呈现视图之前调用。
     *     可以通过给定的 ModelAndView 向视图公开其他模型对象。
     * </pre>
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }


    /**
     * 调用 controller 方法之后，渲染视图之后
     * <pre>
     *     请求处理完成后的回调，即渲染视图后。
     *     将在处理程序执行的任何结果上调用，从而允许适当的资源清理。
     *     注意：只有在此拦截器的 preHandle 方法成功完成并返回 true 才会调用！
     * </pre>
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}

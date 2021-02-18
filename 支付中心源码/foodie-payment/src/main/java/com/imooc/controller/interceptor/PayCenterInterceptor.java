package com.imooc.controller.interceptor;

import com.imooc.pojo.Users;
import com.imooc.service.UserService;
import com.imooc.utils.DateUtil;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

public class PayCenterInterceptor implements HandlerInterceptor {

	@Autowired
	private UserService userService;

	/**
	 * 拦截请求，在controller调用之前
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
			Object arg2) throws Exception {
		String imoocUserId = request.getHeader("imoocUserId");
		String password = request.getHeader("password");

		if (StringUtils.isNotBlank(imoocUserId) && StringUtils.isNotBlank(password)) {

			// 请求数据库查询用户是否存在
			Users  user = userService.queryUserInfo(imoocUserId, password);
			if (user == null) {
				returnErrorResponse(response, new IMOOCJSONResult().errorTokenMsg("用户id或密码不正确！"));
				return false;
			}

			Date endDate = user.getEndDate();
			Date nowDate = new Date();

			int days = DateUtil.daysBetween(nowDate, endDate);
			if (days < 0) {
				returnErrorResponse(response, new IMOOCJSONResult().errorTokenMsg("该账户授权访问日期已失效！"));
				return false;
			}

			// 判断限制访问次数
			/*Integer limit = user.getLimit();
			if (limit != -1) {
				// -1 代表访问无限次
			}*/

		} else {
			returnErrorResponse(response, new IMOOCJSONResult().errorTokenMsg("请在header中携带支付中心所需的用户id以及密码！"));
			return false;
		}
		
		
		/**
		 * 返回 false：请求被拦截，返回
		 * 返回 true ：请求OK，可以继续执行，放行
		 */
		return true;
	}
	
	public void returnErrorResponse(HttpServletResponse response, IMOOCJSONResult result)
			throws IOException, UnsupportedEncodingException {
		OutputStream out=null;
		try{
		    response.setCharacterEncoding("utf-8");
		    response.setContentType("application/json");
		    out = response.getOutputStream();
		    out.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
		    out.flush();
		} finally{
		    if(out!=null){
		        out.close();
		    }
		}
	}
	
	/**
	 * 请求controller之后，渲染视图之前
	 */
	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
	}
	
	/**
	 * 请求controller之后，视图渲染之后
	 */
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}

}

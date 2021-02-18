package com.imooc.service;

import com.imooc.pojo.Orders;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.MerchantOrdersBO;

public interface UserService {

	/**
	 * @Description: 查询用户信息
	 */
	public Users queryUserInfo(String userId, String pwd);

}


package com.imooc.wx.service;

import com.imooc.wx.entity.PayResult;
import com.imooc.wx.entity.PreOrderResult;

import java.io.InputStream;

/**
 * 
 * @Title: OrderService.java
 * @Package com.itzixi.wx.service
 * @Description: 处理微信支付的相关订单业务
 * Copyright: Copyright (c) 2016
 * Company:FURUIBOKE.SCIENCE.AND.TECHNOLOGY
 * 
 * @author leechenxiang
 * @date 2017年8月31日 上午10:41:21
 * @version V1.0
 */
public interface WxOrderService {
	
	/**
	 * 
	 * @Description: 调用微信接口进行统一下单
	 * @param body
	 * @param out_trade_no
	 * @param total_fee
	 * @return
	 * @throws Exception
	 * 
	 * @author leechenxiang
	 * @date 2017年8月31日 下午2:56:56
	 */
	public PreOrderResult placeOrder(String body, String out_trade_no, String total_fee) throws Exception;
	
	/**
	 * 
	 * @Description: 获取支付结果
	 * @return
	 * @throws Exception
	 * 
	 * @author leechenxiang
	 * @date 2017年9月1日 上午8:57:53
	 */
	public PayResult getWxPayResult(InputStream inStream) throws Exception;
	
}

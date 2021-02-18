package com.imooc.wx.service.impl;

import com.imooc.resource.WXPayResource;
import com.imooc.wx.entity.PayResult;
import com.imooc.wx.entity.PreOrder;
import com.imooc.wx.entity.PreOrderResult;
import com.imooc.wx.service.WxOrderService;
import com.imooc.wx.util.HttpUtil;
import com.imooc.wx.util.Sign;
import com.imooc.wx.util.XmlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

@Service
public class WxOrderServiceImpl implements WxOrderService {

	@Autowired
	private WXPayResource wxPayResource;
	
	/**
	 * ==========================================
	 * 微信预付单：指的是在自己的平台需要和微信进行支付交易生成的一个微信订单，称之为“预付单”
	 * 订单：指的是自己的网站平台与用户之间交易生成的订单
	 * 
	 * 1. 用户购买产品 --> 生成网站订单
	 * 2. 用户支付 --> 网站在微信平台生成预付单
	 * 3. 最终实际根据预付单的信息进行支付
	 * ==========================================
	 */
	
	@Override
	public PreOrderResult placeOrder(String body, String out_trade_no, String total_fee) throws Exception {
		// 生成预付单对象
		PreOrder o = new PreOrder();
		// 生成随机字符串
		String nonce_str = UUID.randomUUID().toString().trim().replaceAll("-", "");
		o.setAppid(wxPayResource.getAppId());
		o.setBody(body);
		o.setMch_id(wxPayResource.getMerchantId());
		o.setNotify_url(wxPayResource.getNotifyUrl());
		o.setOut_trade_no(out_trade_no);
		// 判断有没有输入订单总金额，没有输入默认1分钱
		if (total_fee != null && !total_fee.equals("")) {
			o.setTotal_fee(Integer.parseInt(total_fee));
		} else {
			o.setTotal_fee(1);
		}
		o.setNonce_str(nonce_str);
		o.setTrade_type(wxPayResource.getTradeType());
		o.setSpbill_create_ip(wxPayResource.getSpbillCreateIp());
		SortedMap<Object, Object> p = new TreeMap<Object, Object>();
		p.put("appid", wxPayResource.getAppId());
		p.put("mch_id", wxPayResource.getMerchantId());
		p.put("body", body);
		p.put("nonce_str", nonce_str);
		p.put("out_trade_no", out_trade_no);
		p.put("total_fee", total_fee);
		p.put("spbill_create_ip", wxPayResource.getSpbillCreateIp());
		p.put("notify_url", wxPayResource.getNotifyUrl());
		p.put("trade_type", wxPayResource.getTradeType());
		// 获得签名
		String sign = Sign.createSign("utf-8", p, wxPayResource.getSecrectKey());
		o.setSign(sign);
		// Object转换为XML
		String xml = XmlUtil.object2Xml(o, PreOrder.class);
		// 统一下单地址
		String url = wxPayResource.getPlaceOrderUrl();
		// 调用微信统一下单地址
		String returnXml = HttpUtil.sendPost(url, xml);
		
		// XML转换为Object
		PreOrderResult preOrderResult = (PreOrderResult) XmlUtil.xml2Object(returnXml, PreOrderResult.class);
		
		return preOrderResult;
	}

	@Override
	public PayResult getWxPayResult(InputStream inStream) throws Exception {
		BufferedReader in = null;
		String result = "";
		in = new BufferedReader(
				new InputStreamReader(inStream));
		String line;
		while ((line = in.readLine()) != null) {
			result += line;
		}
		PayResult pr = (PayResult)XmlUtil.xml2Object(result, PayResult.class);
//		System.out.println(pr.toString());
		return pr;
	}

}

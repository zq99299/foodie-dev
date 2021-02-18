package com.imooc.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.imooc.enums.PayMethod;
import com.imooc.enums.PaymentStatus;
import com.imooc.pojo.Orders;
import com.imooc.pojo.bo.MerchantOrdersBO;
import com.imooc.pojo.vo.PaymentInfoVO;
import com.imooc.resource.AliPayResource;
import com.imooc.resource.WXPayResource;
import com.imooc.service.PaymentOrderService;
import com.imooc.utils.CurrencyUtils;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.RedisOperator;
import com.imooc.wx.entity.PreOrderResult;
import com.imooc.wx.service.WxOrderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "payment")
public class PaymentController {

	final static Logger log = LoggerFactory.getLogger(PaymentController.class);

	@Autowired
	public RedisOperator redis;

	@Autowired
	private WXPayResource wxPayResource;
	@Autowired
	private AliPayResource aliPayResource;
	
	@Autowired
	private PaymentOrderService paymentOrderService;

	@Autowired
	private WxOrderService wxOrderService;

	/**
	 * 接受商户订单信息，保存到自己的数据库
	 */
	@PostMapping("/createMerchantOrder")
	public IMOOCJSONResult createMerchantOrder(@RequestBody MerchantOrdersBO merchantOrdersBO, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String merchantOrderId = merchantOrdersBO.getMerchantOrderId();             	// 订单id
		String merchantUserId = merchantOrdersBO.getMerchantUserId();     		// 用户id
		Integer amount = merchantOrdersBO.getAmount();    // 实际支付订单金额
		Integer payMethod = merchantOrdersBO.getPayMethod();          	// 支付方式
		String returnUrl = merchantOrdersBO.getReturnUrl();           	// 支付成功后的回调地址（学生自定义）

		if (StringUtils.isBlank(merchantOrderId)) {
			return IMOOCJSONResult.errorMsg("参数[orderId]不能为空");
		}
		if (StringUtils.isBlank(merchantUserId)) {
			return IMOOCJSONResult.errorMsg("参数[userId]不能为空");
		}
		if (amount == null || amount < 1) {
			return IMOOCJSONResult.errorMsg("参数[realPayAmount]不能为空并且不能小于1");
		}
		if (payMethod == null) {
			return IMOOCJSONResult.errorMsg("参数[payMethod]不能为空并且不能小于1");
		}
		if (payMethod != PayMethod.WEIXIN.type && payMethod != PayMethod.ALIPAY.type) {
			return IMOOCJSONResult.errorMsg("参数[payMethod]目前只支持微信支付或支付宝支付");
		}
		if (StringUtils.isBlank(returnUrl)) {
			return IMOOCJSONResult.errorMsg("参数[returnUrl]不能为空");
		}

		// 保存传来的商户订单信息
		boolean isSuccess = false;
		try {
			isSuccess = paymentOrderService.createPaymentOrder(merchantOrdersBO);
		} catch (Exception e) {
			e.printStackTrace();
			IMOOCJSONResult.errorException(e.getMessage());
		}

		if (isSuccess) {
			return IMOOCJSONResult.ok("商户订单创建成功！");
		} else {
			return IMOOCJSONResult.errorMsg("商户订单创建失败，请重试...");
		}
	}

	/**
	 * 提供给大家查询的方法，用于查询订单信息
	 * @param merchantOrderId
	 * @param merchantUserId
	 * @return
	 */
	@PostMapping("getPaymentCenterOrderInfo")
	public IMOOCJSONResult getPaymentCenterOrderInfo(String merchantOrderId, String merchantUserId) {

		if (StringUtils.isBlank(merchantOrderId) || StringUtils.isBlank(merchantUserId)) {
			return IMOOCJSONResult.errorMsg("查询参数不能为空！");
		}

		Orders orderInfo = paymentOrderService.queryOrderInfo(merchantUserId, merchantOrderId);

		return IMOOCJSONResult.ok(orderInfo);
	}


	/******************************************  以下所有方法开始支付流程   ******************************************/
	
	/**
	 * @Description: 微信扫码支付页面
	 */
//	@GetMapping(value="/getWXPayQRCode")
	@PostMapping(value="/getWXPayQRCode")
	public IMOOCJSONResult getWXPayQRCode(String merchantOrderId, String merchantUserId) throws Exception{

//		System.out.println(wxPayResource.toString());

		// 根据订单ID和用户ID查询订单详情
    	Orders waitPayOrder = paymentOrderService.queryOrderByStatus(merchantUserId, merchantOrderId, PaymentStatus.WAIT_PAY.type);

    	// 商品描述
		String body = "天天吃货-付款用户[" + merchantUserId + "]";
		// 商户订单号
		String out_trade_no = merchantOrderId;
		// 从redis中去获得这笔订单的微信支付二维码，如果订单状态没有支付没有就放入，这样的做法防止用户频繁刷新而调用微信接口
		if (waitPayOrder != null) {
			String qrCodeUrl = redis.get(wxPayResource.getQrcodeKey() + ":" + merchantOrderId);

			if (StringUtils.isEmpty(qrCodeUrl)) {
				// 订单总金额，单位为分
				String total_fee = String.valueOf(waitPayOrder.getAmount());
//				String total_fee = "1";	// 测试用 1分钱

				// 统一下单
				PreOrderResult preOrderResult = wxOrderService.placeOrder(body, out_trade_no, total_fee);
				qrCodeUrl = preOrderResult.getCode_url();
			}

			PaymentInfoVO paymentInfoVO = new PaymentInfoVO();
			paymentInfoVO.setAmount(waitPayOrder.getAmount());
			paymentInfoVO.setMerchantOrderId(merchantOrderId);
			paymentInfoVO.setMerchantUserId(merchantUserId);
			paymentInfoVO.setQrCodeUrl(qrCodeUrl);

			redis.set(wxPayResource.getQrcodeKey() + ":" + merchantOrderId, qrCodeUrl, wxPayResource.getQrcodeExpire());

			return IMOOCJSONResult.ok(paymentInfoVO);
		} else {
			return IMOOCJSONResult.errorMsg("该订单不存在，或已经支付");
		}
	}


	/**
	 *
	 * @Description: 前往支付宝进行支付
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/goAlipay")
	public IMOOCJSONResult goAlipay(String merchantOrderId, String merchantUserId) throws Exception{

		// 查询订单详情
		Orders waitPayOrder = paymentOrderService.queryOrderByStatus(merchantUserId, merchantOrderId, PaymentStatus.WAIT_PAY.type);

		//获得初始化的AlipayClient
		AlipayClient alipayClient = new DefaultAlipayClient(aliPayResource.getGatewayUrl(),
															aliPayResource.getAppId(),
															aliPayResource.getMerchantPrivateKey(),
															"json",
															aliPayResource.getCharset(),
															aliPayResource.getAlipayPublicKey(),
															aliPayResource.getSignType());

		//设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(aliPayResource.getReturnUrl());
		alipayRequest.setNotifyUrl(aliPayResource.getNotifyUrl());

		// 商户订单号, 商户网站订单系统中唯一订单号, 必填
		String out_trade_no = merchantOrderId;
		// 付款金额, 必填 单位元
		String total_amount = CurrencyUtils.getFen2YuanWithPoint(waitPayOrder.getAmount());
//    	String total_amount = "0.01";	// 测试用 1分钱
		// 订单名称, 必填
		String subject = "天天吃货-付款用户[" + merchantUserId + "]";
		// 商品描述, 可空, 目前先用订单名称
		String body = subject;

		// 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
		String timeout_express = "1d";

		alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
				+ "\"total_amount\":\""+ total_amount +"\","
				+ "\"subject\":\""+ subject +"\","
				+ "\"body\":\""+ body +"\","
				+ "\"timeout_express\":\""+ timeout_express +"\","
				+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

		//若想给BizContent增加其他可选请求参数, 以增加自定义超时时间参数timeout_express来举例说明
		//alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
		//		+ "\"total_amount\":\""+ total_amount +"\","
		//		+ "\"subject\":\""+ subject +"\","
		//		+ "\"body\":\""+ body +"\","
		//		+ "\"timeout_express\":\"10m\","
		//		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
		//请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节

		//请求
		String alipayForm = "";
		try {
			alipayForm = alipayClient.pageExecute(alipayRequest).getBody();
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}

		log.info("支付宝支付 - 前往支付页面, alipayForm: \n{}", alipayForm);

		return IMOOCJSONResult.ok(alipayForm);
	}

}

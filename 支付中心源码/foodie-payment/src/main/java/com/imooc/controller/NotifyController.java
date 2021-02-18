package com.imooc.controller;

import com.alipay.api.domain.PreOrderResult;
import com.alipay.api.internal.util.AlipaySignature;
import com.imooc.enums.PayMethod;
import com.imooc.enums.PaymentStatus;
import com.imooc.pojo.Orders;
import com.imooc.pojo.bo.MerchantOrdersBO;
import com.imooc.resource.AliPayResource;
import com.imooc.resource.WXPayResource;
import com.imooc.service.PaymentOrderService;
import com.imooc.utils.CurrencyUtils;
import com.imooc.utils.DateUtil;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.RedisOperator;
import com.imooc.wx.entity.PayResult;
import com.imooc.wx.service.WxOrderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping(value = "/payment/notice")
public class NotifyController {

	final static Logger log = LoggerFactory.getLogger(NotifyController.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private AliPayResource aliPayResource;

	@Autowired
	private WxOrderService wxOrderService;

	@Autowired
	private PaymentOrderService paymentOrderService;

	/**
	 * 支付成功后的微信支付异步通知
	 */
	@RequestMapping(value="/wxpay")
	public void wxpay(HttpServletRequest request, HttpServletResponse response) throws Exception {

		log.info("支付成功后的微信支付异步通知");

		// 获取微信支付结果
		PayResult payResult = wxOrderService.getWxPayResult(request.getInputStream());

		boolean isPaid = payResult.getReturn_code().equals("SUCCESS") ? true : false;
		// 查询该笔订单在微信那边是否成功支付
		// 支付成功，商户处理后同步返回给微信参数
		PrintWriter writer = response.getWriter();
		if (isPaid) {

			String merchantOrderId = payResult.getOut_trade_no();			// 商户订单号
			String wxFlowId = payResult.getTransaction_id();
			Integer paidAmount = payResult.getTotal_fee();

//			System.out.println("================================= 支付成功 =================================");

			// ====================== 操作商户自己的业务，比如修改订单状态等 start ==========================
			String merchantReturnUrl = paymentOrderService.updateOrderPaid(merchantOrderId, paidAmount);
			// ============================================ 业务结束， end ==================================

			log.info("************* 支付成功(微信支付异步通知) - 时间: {} *************", DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));
			log.info("* 商户订单号: {}", merchantOrderId);
			log.info("* 微信订单号: {}", wxFlowId);
			log.info("* 实际支付金额: {}", paidAmount);
			log.info("*****************************************************************************");


			// 通知天天吃货服务端订单已支付
//			String url = "http://192.168.1.2:8088/orders/notifyMerchantOrderPaid";

			MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
			requestEntity.add("merchantOrderId", merchantOrderId);
			String httpStatus = restTemplate.postForObject(merchantReturnUrl, requestEntity, String.class);
			log.info("*** 通知天天吃货后返回的状态码 httpStatus: {} ***", httpStatus);

			// 通知微信已经收到消息，不要再给我发消息了，否则微信会10连击调用本接口
			String noticeStr = setXML("SUCCESS", "");
			writer.write(noticeStr);
			writer.flush();

		} else {
			System.out.println("================================= 支付失败 =================================");

			// 支付失败
			String noticeStr = setXML("FAIL", "");
			writer.write(noticeStr);
			writer.flush();
		}

	}

	public static String setXML(String return_code, String return_msg) {
		return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
	}

	/**
	 * @Description: 支付成功后的支付宝异步通知
	 */
	@RequestMapping(value="/alipay")
	public String alipay(HttpServletRequest request, HttpServletResponse response) throws Exception {

		log.info("支付成功后的支付宝异步通知");

		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map<String,String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用
//			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}

		boolean signVerified = AlipaySignature.rsaCheckV1(params,
														aliPayResource.getAlipayPublicKey(),
														aliPayResource.getCharset(),
														aliPayResource.getSignType()); //调用SDK验证签名

		if(signVerified) {//验证成功
			// 商户订单号
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
			// 支付宝交易号
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
			// 交易状态
			String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
			// 付款金额
			String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");

			if (trade_status.equals("TRADE_SUCCESS")){
				String merchantReturnUrl = paymentOrderService.updateOrderPaid(out_trade_no, CurrencyUtils.getYuan2Fen(total_amount));
				notifyFoodieShop(out_trade_no, merchantReturnUrl);
			}

			log.info("************* 支付成功(支付宝异步通知) - 时间: {} *************", DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));
			log.info("* 订单号: {}", out_trade_no);
			log.info("* 支付宝交易号: {}", trade_no);
			log.info("* 实付金额: {}", total_amount);
			log.info("* 交易状态: {}", trade_status);
			log.info("*****************************************************************************");

			return "success";
		}else {
			//验证失败
			log.info("验签失败, 时间: {}", DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));
			return "fail";
		}
	}

	/**
	 * 通知天天吃货商户平台
	 */
	private void notifyFoodieShop(String merchantOrderId, String merchantReturnUrl) {
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("merchantOrderId", merchantOrderId);
		String httpStatus = restTemplate.postForObject(merchantReturnUrl, requestEntity, String.class);
		System.out.println("*** 通知天天吃货后返回的状态码 httpStatus: " + httpStatus + " ***");
		log.info("*** 通知天天吃货后返回的状态码 httpStatus: {} ***", httpStatus);
	}


	@GetMapping("ttt")
	public IMOOCJSONResult test() {
		paymentOrderService.updateOrderPaid("190718FW17BFP8SW", 1001);
		return IMOOCJSONResult.ok();
	}

}

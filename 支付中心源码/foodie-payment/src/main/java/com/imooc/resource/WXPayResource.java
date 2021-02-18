package com.imooc.resource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

//@Configuration
@Component
@ConfigurationProperties(prefix="wxpay")
@PropertySource("classpath:wxpay.properties")
public class WXPayResource {

	private String qrcodeKey;
	private long qrcodeExpire;

	private String appId;
	private String merchantId;
	private String secrectKey;

	private String spbillCreateIp;
	private String notifyUrl;

	private String tradeType;
	private String placeOrderUrl;

	public String getQrcodeKey() {
		return qrcodeKey;
	}

	public void setQrcodeKey(String qrcodeKey) {
		this.qrcodeKey = qrcodeKey;
	}

	public long getQrcodeExpire() {
		return qrcodeExpire;
	}

	public void setQrcodeExpire(long qrcodeExpire) {
		this.qrcodeExpire = qrcodeExpire;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getSecrectKey() {
		return secrectKey;
	}

	public void setSecrectKey(String secrectKey) {
		this.secrectKey = secrectKey;
	}

	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}

	public void setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getPlaceOrderUrl() {
		return placeOrderUrl;
	}

	public void setPlaceOrderUrl(String placeOrderUrl) {
		this.placeOrderUrl = placeOrderUrl;
	}
}

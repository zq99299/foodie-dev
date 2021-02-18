package com.imooc.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

public class Orders {
    /**
     * 订单主键
     */
    @Id
    private String id;

    /**
     * 商户订单号
     */
    @Column(name = "merchant_order_id")
    private String merchantOrderId;

    /**
     * 商户方的发起用户的用户主键id
     */
    @Column(name = "merchant_user_id")
    private String merchantUserId;

    /**
     * 实际支付总金额（包含商户所支付的订单费邮费总额）
     */
    private Integer amount;

    /**
     * 支付方式
     */
    @Column(name = "pay_method")
    private Integer payMethod;

    /**
     * 支付状态 10：未支付 20：已支付 30：支付失败 40：已退款
     */
    @Column(name = "pay_status")
    private Integer payStatus;

    /**
     * 从哪一端来的，比如从天天吃货这门实战过来的
     */
    @Column(name = "come_from")
    private String comeFrom;

    /**
     * 支付成功后的通知地址，这个是开发者那一段的，不是第三方支付通知的地址
     */
    @Column(name = "return_url")
    private String returnUrl;

    /**
     * 逻辑删除状态;1: 删除 0:未删除
     */
    @Column(name = "is_delete")
    private Integer isDelete;

    /**
     * 创建时间（成交时间）
     */
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 获取订单主键
     *
     * @return id - 订单主键
     */
    public String getId() {
        return id;
    }

    /**
     * 设置订单主键
     *
     * @param id 订单主键
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取商户订单号
     *
     * @return merchant_order_id - 商户订单号
     */
    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    /**
     * 设置商户订单号
     *
     * @param merchantOrderId 商户订单号
     */
    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    /**
     * 获取商户方的发起用户的用户主键id
     *
     * @return merchant_user_id - 商户方的发起用户的用户主键id
     */
    public String getMerchantUserId() {
        return merchantUserId;
    }

    /**
     * 设置商户方的发起用户的用户主键id
     *
     * @param merchantUserId 商户方的发起用户的用户主键id
     */
    public void setMerchantUserId(String merchantUserId) {
        this.merchantUserId = merchantUserId;
    }

    /**
     * 获取实际支付总金额（包含商户所支付的订单费邮费总额）
     *
     * @return amount - 实际支付总金额（包含商户所支付的订单费邮费总额）
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * 设置实际支付总金额（包含商户所支付的订单费邮费总额）
     *
     * @param amount 实际支付总金额（包含商户所支付的订单费邮费总额）
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * 获取支付方式
     *
     * @return pay_method - 支付方式
     */
    public Integer getPayMethod() {
        return payMethod;
    }

    /**
     * 设置支付方式
     *
     * @param payMethod 支付方式
     */
    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }

    /**
     * 获取支付状态 10：未支付 20：已支付 30：支付失败 40：已退款
     *
     * @return pay_status - 支付状态 10：未支付 20：已支付 30：支付失败 40：已退款
     */
    public Integer getPayStatus() {
        return payStatus;
    }

    /**
     * 设置支付状态 10：未支付 20：已支付 30：支付失败 40：已退款
     *
     * @param payStatus 支付状态 10：未支付 20：已支付 30：支付失败 40：已退款
     */
    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    /**
     * 获取从哪一端来的，比如从天天吃货这门实战过来的
     *
     * @return come_from - 从哪一端来的，比如从天天吃货这门实战过来的
     */
    public String getComeFrom() {
        return comeFrom;
    }

    /**
     * 设置从哪一端来的，比如从天天吃货这门实战过来的
     *
     * @param comeFrom 从哪一端来的，比如从天天吃货这门实战过来的
     */
    public void setComeFrom(String comeFrom) {
        this.comeFrom = comeFrom;
    }

    /**
     * 获取支付成功后的通知地址，这个是开发者那一段的，不是第三方支付通知的地址
     *
     * @return return_url - 支付成功后的通知地址，这个是开发者那一段的，不是第三方支付通知的地址
     */
    public String getReturnUrl() {
        return returnUrl;
    }

    /**
     * 设置支付成功后的通知地址，这个是开发者那一段的，不是第三方支付通知的地址
     *
     * @param returnUrl 支付成功后的通知地址，这个是开发者那一段的，不是第三方支付通知的地址
     */
    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    /**
     * 获取逻辑删除状态;1: 删除 0:未删除
     *
     * @return is_delete - 逻辑删除状态;1: 删除 0:未删除
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * 设置逻辑删除状态;1: 删除 0:未删除
     *
     * @param isDelete 逻辑删除状态;1: 删除 0:未删除
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * 获取创建时间（成交时间）
     *
     * @return created_time - 创建时间（成交时间）
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * 设置创建时间（成交时间）
     *
     * @param createdTime 创建时间（成交时间）
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
package cn.mrcode.foodiedev.pojo.vo;

import java.util.Date;
import java.util.List;

/**
 * 用户中心，我的订单列表VO
 */
public class MyOrdersVO {

    private String orderId;
    private Date createdTime;
    private Integer payMethod;
    private Integer realPayAmount;
    private Integer postAmount;
    private Integer isComment;
    private Integer orderStatus;

    private List<MySubOrderItemVO> subOrderItemList;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }

    public Integer getRealPayAmount() {
        return realPayAmount;
    }

    public void setRealPayAmount(Integer realPayAmount) {
        this.realPayAmount = realPayAmount;
    }

    public Integer getPostAmount() {
        return postAmount;
    }

    public void setPostAmount(Integer postAmount) {
        this.postAmount = postAmount;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<MySubOrderItemVO> getSubOrderItemList() {
        return subOrderItemList;
    }

    public void setSubOrderItemList(List<MySubOrderItemVO> subOrderItemList) {
        this.subOrderItemList = subOrderItemList;
    }

    public Integer getIsComment() {
        return isComment;
    }

    public void setIsComment(Integer isComment) {
        this.isComment = isComment;
    }
}
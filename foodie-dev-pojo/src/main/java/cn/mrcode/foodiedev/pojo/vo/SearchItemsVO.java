package cn.mrcode.foodiedev.pojo.vo;

import java.util.Date;

/**
 * 用于展示商品搜索列表结果的 VO
 */
public class SearchItemsVO {

    private String itemId;
    private String itemName;
    private int sellCounts;
    private String imgUrl;
    /**
     * 价格是以分为单位，原因是因为：计算时有四舍五入，以分为单位的话，差别就不会特别大
     */
    private int price;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getSellCounts() {
        return sellCounts;
    }

    public void setSellCounts(int sellCounts) {
        this.sellCounts = sellCounts;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}

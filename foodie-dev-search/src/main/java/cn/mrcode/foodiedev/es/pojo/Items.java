package cn.mrcode.foodiedev.es.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


/**
 * <pre>
 *     对应的 vo 类是：cn.mrcode.foodiedev.pojo.vo.SearchItemsVO
 * </pre>
 */
@Document(indexName = "foodie-items-ik",
        type = "_doc",  // 一定要写默认的，否则这个 data es 会使用类名作为 type，导致搜索不到数据
        createIndex = false // 不创建 index，我们的 index 在数据同步的时候已经创建好了
)
public class Items {
    /**
     * 商品主键id, 不需要倒排索引
     */
    @Id
    @Field(store = true, type = FieldType.Text, index = false)
    private String itemId;

    /**
     * 商品名称 商品名称
     */
    @Field(store = true, type = FieldType.Text, index = true)
    private String itemName;

    @Field(store = true, type = FieldType.Text, index = false)
    private String imgUrl;

    @Field(store = true, type = FieldType.Integer, index = true)
    private Integer price;

    @Field(store = true, type = FieldType.Integer, index = true)
    private int sellCounts;

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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public int getSellCounts() {
        return sellCounts;
    }

    public void setSellCounts(int sellCounts) {
        this.sellCounts = sellCounts;
    }

    @Override
    public String toString() {
        return "Items{" +
                "itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", price=" + price +
                ", sellCounts=" + sellCounts +
                '}';
    }
}
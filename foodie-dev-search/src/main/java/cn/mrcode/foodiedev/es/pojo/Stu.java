package cn.mrcode.foodiedev.es.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author mrcode
 * @date 2021/8/26 21:24
 */
@Document(indexName = "stu")
public class Stu {
    // 如果不写 @ID 的话，会自动生成 _id 数据
    @Id
    private Long stuId;

    @Field(store = true, index = true)
    private String name;

    @Field(store = true)
    private Integer age;

    // 设置字段类型
    @Field(store = true, type = FieldType.Keyword)
    private String sing;

    @Field(store = true)
    private String description;

    @Field(store = true)
    private Float money;

    public Long getStuId() {
        return stuId;
    }

    public void setStuId(Long stuId) {
        this.stuId = stuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSing() {
        return sing;
    }

    public void setSing(String sing) {
        this.sing = sing;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "Stu{" +
                "stuId=" + stuId +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sing='" + sing + '\'' +
                ", description='" + description + '\'' +
                ", money=" + money +
                '}';
    }
}

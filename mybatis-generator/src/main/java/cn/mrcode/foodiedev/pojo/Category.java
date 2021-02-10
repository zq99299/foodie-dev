package cn.mrcode.foodiedev.pojo;

import javax.persistence.*;

public class Category {
    /**
     * 主键 分类id主键
     */
    @Id
    private Integer id;

    /**
     * 分类名称 分类名称
     */
    private String name;

    /**
     * 分类类型 分类得类型，
1:一级大分类
2:二级分类
3:三级小分类
     */
    private Integer type;

    /**
     * 父id 父id 上一级依赖的id，1级分类则为0，二级三级分别依赖上一级
     */
    @Column(name = "father_id")
    private Integer fatherId;

    /**
     * 图标 logo
     */
    private String logo;

    /**
     * 口号
     */
    private String slogan;

    /**
     * 分类图
     */
    @Column(name = "cat_image")
    private String catImage;

    /**
     * 背景颜色
     */
    @Column(name = "bg_color")
    private String bgColor;

    /**
     * 获取主键 分类id主键
     *
     * @return id - 主键 分类id主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键 分类id主键
     *
     * @param id 主键 分类id主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取分类名称 分类名称
     *
     * @return name - 分类名称 分类名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置分类名称 分类名称
     *
     * @param name 分类名称 分类名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取分类类型 分类得类型，
1:一级大分类
2:二级分类
3:三级小分类
     *
     * @return type - 分类类型 分类得类型，
1:一级大分类
2:二级分类
3:三级小分类
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置分类类型 分类得类型，
1:一级大分类
2:二级分类
3:三级小分类
     *
     * @param type 分类类型 分类得类型，
1:一级大分类
2:二级分类
3:三级小分类
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取父id 父id 上一级依赖的id，1级分类则为0，二级三级分别依赖上一级
     *
     * @return father_id - 父id 父id 上一级依赖的id，1级分类则为0，二级三级分别依赖上一级
     */
    public Integer getFatherId() {
        return fatherId;
    }

    /**
     * 设置父id 父id 上一级依赖的id，1级分类则为0，二级三级分别依赖上一级
     *
     * @param fatherId 父id 父id 上一级依赖的id，1级分类则为0，二级三级分别依赖上一级
     */
    public void setFatherId(Integer fatherId) {
        this.fatherId = fatherId;
    }

    /**
     * 获取图标 logo
     *
     * @return logo - 图标 logo
     */
    public String getLogo() {
        return logo;
    }

    /**
     * 设置图标 logo
     *
     * @param logo 图标 logo
     */
    public void setLogo(String logo) {
        this.logo = logo;
    }

    /**
     * 获取口号
     *
     * @return slogan - 口号
     */
    public String getSlogan() {
        return slogan;
    }

    /**
     * 设置口号
     *
     * @param slogan 口号
     */
    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    /**
     * 获取分类图
     *
     * @return cat_image - 分类图
     */
    public String getCatImage() {
        return catImage;
    }

    /**
     * 设置分类图
     *
     * @param catImage 分类图
     */
    public void setCatImage(String catImage) {
        this.catImage = catImage;
    }

    /**
     * 获取背景颜色
     *
     * @return bg_color - 背景颜色
     */
    public String getBgColor() {
        return bgColor;
    }

    /**
     * 设置背景颜色
     *
     * @param bgColor 背景颜色
     */
    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }
}
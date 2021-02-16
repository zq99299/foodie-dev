package cn.mrcode.foodiedev.mapper;

/**
 * @author mrcode
 * @date 2021/2/16 10:12
 */
public class SearchItemsParams {
    private String keywords;
    private String sort;

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}

package cn.mrcode.foodiedev.es.service;

import cn.mrcode.foodiedev.common.util.PagedGridResult;
import cn.mrcode.foodiedev.es.pojo.Items;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author mrcode
 * @date 2021/8/30 22:24
 */
@Service
public class ItemEsServiceImpl implements ItemEsService {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize) {
        // k: 默认，根据 name ，这里不指定，按 es 默认返回顺序" +
        // c: 根据销售数量 降序
        // p: 根据价格排序 升序"
        FieldSortBuilder sortBuilder = null;
        if ("c".equals(sort)) {
            sortBuilder = new FieldSortBuilder("sellCounts").order(SortOrder.DESC);
        } else if ("p".equals(sort)) {
            sortBuilder = new FieldSortBuilder("price").order(SortOrder.ASC);
        } else {
            sortBuilder = new FieldSortBuilder("itemName.keyword").order(SortOrder.ASC);
        }
        String itemNameFiled = "itemName";
        SearchQuery query = new NativeSearchQueryBuilder()
                // 基于商品名称搜索
                .withQuery(QueryBuilders.matchQuery(itemNameFiled, keywords))
                // 分页数据，从 0 开始，每页多少条
                .withPageable(PageRequest.of(page, pageSize))
                // 高亮字段设置
                .withHighlightFields(
                        new HighlightBuilder.Field(itemNameFiled)
                                .preTags("<font color='red'>")
                                .postTags("</font>")
                )
                // 排序
                .withSort(sortBuilder)
                .build();

        AggregatedPage<Items> pageItem = elasticsearchTemplate.queryForPage(query, Items.class,
                // 自定义结果集
                new SearchResultMapper() {
                    @Override
                    public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                        SearchHits hits = searchResponse.getHits();
                        List<Items> items = new ArrayList<>();
                        for (SearchHit hit : hits) {
                            // 获取高亮字段
                            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                            HighlightField highlightField = highlightFields.get(itemNameFiled);
                            String itemHighlight = highlightField.getFragments()[0].toString();

                            Items item = new Items();
                            item.setItemName(itemHighlight);
                            // 获取文档数据，然后复制到类中其他属性
                            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                            item.setItemId(sourceAsMap.get("itemId").toString());
                            item.setImgUrl((String) sourceAsMap.get("imgUrl"));
                            item.setPrice(Integer.valueOf(sourceAsMap.get("price").toString()));
                            item.setSellCounts(Integer.valueOf(sourceAsMap.get("sellCounts").toString()));

                            items.add(item);
                        }
                        if (items.size() > 0) {
                            // List<T> content, Pageable pageable, long total, float maxScore
                            AggregatedPageImpl<T> ap = new AggregatedPageImpl<>((List<T>) items,
                                    pageable,
                                    hits.getTotalHits(),
                                    hits.getMaxScore());
                            return ap;
                        }
                        return null;
                    }

                    @Override
                    public <T> T mapSearchHit(SearchHit searchHit, Class<T> aClass) {
                        return null;
                    }
                });

        PagedGridResult pagedGridResult = new PagedGridResult();
        if (pageItem == null) {
            return pagedGridResult;
        }

        pagedGridResult.setTotal(pageItem.getTotalPages());  // 总页数
        pagedGridResult.setPage(page + 1);  // 当前页,前端比 es 多 1 页
        pagedGridResult.setRecords((int) pageItem.getTotalElements());  // 总记录数
        pagedGridResult.setRows(pageItem.getContent());
        return pagedGridResult;
    }
}

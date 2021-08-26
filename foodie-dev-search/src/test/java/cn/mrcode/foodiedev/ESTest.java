package cn.mrcode.foodiedev;

import cn.mrcode.foodiedev.es.pojo.Stu;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.test.context.junit4.SpringRunner;

import javax.naming.directory.SearchResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author mrcode
 * @date 2021/8/26 21:22
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ESTest {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    /**
     * 创建 index
     */
    @Test
    public void createIndexStu() {
        Stu stu = new Stu();
        stu.setStuId(1001L);
        stu.setAge(18);
        stu.setName("wo");
        IndexQuery indexQuery = new IndexQueryBuilder().withObject(stu).build();
        elasticsearchTemplate.index(indexQuery);
    }

    /**
     * 创建 doc
     */
    @Test
    public void createIndexStu2() {
        Stu stu = new Stu();
        stu.setStuId(1002L);
        stu.setAge(22);
        stu.setName("spider man");
        stu.setSing("i am spider man");
        stu.setDescription("i am spider man de");
        IndexQuery indexQuery = new IndexQueryBuilder().withObject(stu).build();
        elasticsearchTemplate.index(indexQuery);
    }

    /**
     * 删除 index
     */
    @Test
    public void deleteIndexStu() {
        elasticsearchTemplate.deleteIndex(Stu.class);
    }

    /**
     * 更新 doc 部分字段
     */
    @Test
    public void updateIndexStu() {
        Map<String, Object> sourceMap = new HashMap<>();
        sourceMap.put("sing", "i am spider man update");
        sourceMap.put("age", 33);

        IndexRequest indexRequest = new IndexRequest();
        indexRequest.source(sourceMap);

        // org.springframework.data.elasticsearch.core.query.UpdateQueryBuilder;
        UpdateQuery updateQuery = new UpdateQueryBuilder()
                // 指定文档索引信息，从类上的注解中获取
                .withClass(Stu.class)
                // 指定 ID
                .withId("1002")
                // 要更新的数据
                .withIndexRequest(indexRequest)
                .build();
        elasticsearchTemplate.update(updateQuery);
    }

    /**
     * 通过文档 ID 获取文档数据
     */
    @Test
    public void queryDocStu() {
        // org.springframework.data.elasticsearch.core.query.*
        GetQuery getQuery = new GetQuery();
        getQuery.setId("1002");
        Stu stu = elasticsearchTemplate.queryForObject(getQuery, Stu.class);
        System.out.println(stu);
    }

    /**
     * 通过  文档 ID 删除文档
     */
    @Test
    public void deleteDocStu() {
        elasticsearchTemplate.delete(Stu.class, "1002");
    }


    /**
     * 创建测试数据
     */
    @Test
    public void createTestData() {
        Stu stu = new Stu();
        stu.setStuId(1101L);
        stu.setAge(22);
        stu.setName("spider man");
        stu.setSing("i am spider man");
        stu.setDescription("i am spider man de");
        IndexQuery indexQuery = new IndexQueryBuilder().withObject(stu).build();
        elasticsearchTemplate.index(indexQuery);

        stu = new Stu();
        stu.setStuId(1102L);
        stu.setAge(18);
        stu.setName("spider xx ds");
        stu.setSing("i am spider man 1102");
        stu.setDescription("i am spider man de 1102");
        indexQuery = new IndexQueryBuilder().withObject(stu).build();
        elasticsearchTemplate.index(indexQuery);

        stu = new Stu();
        stu.setStuId(1103L);
        stu.setAge(40);
        stu.setName("spider xx ds");
        stu.setSing("i am spider man 1103");
        stu.setDescription("i am spider man de 1103");
        indexQuery = new IndexQueryBuilder().withObject(stu).build();
        elasticsearchTemplate.index(indexQuery);
    }

    /**
     * 分页获取数据 form\size
     */
    @Test
    public void queryForPage() {
        SearchQuery query = new NativeSearchQueryBuilder()
                //
                .withQuery(QueryBuilders.matchQuery("description", "man"))
                // 分页数据，从 0 开始，每页多少条
                .withPageable(PageRequest.of(0, 2))
                .build();
        AggregatedPage<Stu> pageStud = elasticsearchTemplate.queryForPage(query, Stu.class);

        System.out.println("总分页数：" + pageStud.getTotalPages());
        System.out.println("总数：" + pageStud.getTotalElements());

        List<Stu> content = pageStud.getContent();
        for (Stu stu : content) {
            System.out.println(stu);
        }

    }

    /**
     * 高亮搜索
     */
    @Test
    public void withHighlightFields() {
        SearchQuery query = new NativeSearchQueryBuilder()
                //
                .withQuery(QueryBuilders.matchQuery("description", "man"))
                // 分页数据，从 0 开始，每页多少条
                .withPageable(PageRequest.of(0, 2))
                // 高亮字段设置
                .withHighlightFields(
                        new HighlightBuilder.Field("description")
                                .preTags("<font color='red'>")
                                .postTags("</font>")
                )
                .build();


        AggregatedPage<Stu> pageStud = elasticsearchTemplate.queryForPage(query, Stu.class,
                // 自定义结果集
                new SearchResultMapper() {
                    @Override
                    public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                        SearchHits hits = searchResponse.getHits();
                        List<Stu> stuList = new ArrayList<>();
                        for (SearchHit hit : hits) {
                            // 获取高亮字段
                            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                            HighlightField highlightField = highlightFields.get("description");
                            String descriptionHighlight = highlightField.getFragments()[0].toString();

                            Stu e = new Stu();
                            e.setDescription(descriptionHighlight);
                            // 获取文档数据，然后复制到类中其他属性
                            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                            e.setStuId(Long.valueOf(sourceAsMap.get("stuId").toString()));
                            e.setName((String) sourceAsMap.get("name"));
                            e.setAge(Integer.valueOf(sourceAsMap.get("age").toString()));
                            stuList.add(e);
                        }
                        if (stuList.size() > 0) {
                            // List<T> content, Pageable pageable, long total, float maxScore
                            AggregatedPageImpl<T> ap = new AggregatedPageImpl<>((List<T>) stuList,
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

        System.out.println("总分页数：" + pageStud.getTotalPages());
        System.out.println("总数：" + pageStud.getTotalElements());

        List<Stu> content = pageStud.getContent();
        for (Stu stu : content) {
            System.out.println(stu);
        }
    }

    /**
     * 实现排序
     */
    @Test
    public void sort() {
        // 构建一个原生的查询
        SearchQuery query = new NativeSearchQueryBuilder()
                // query 可以里面有很多类型可以使用
                .withQuery(QueryBuilders.matchQuery("description", "man"))
                // 按年龄升序排列，多个排序，则在使用一个 withSort
                .withSort(new FieldSortBuilder("age").order(SortOrder.ASC))
                // 分页数据，从 0 开始，每页多少条
                .withPageable(PageRequest.of(0, 10))
                .build();
        AggregatedPage<Stu> pageStud = elasticsearchTemplate.queryForPage(query, Stu.class);

        System.out.println("总分页数：" + pageStud.getTotalPages());
        System.out.println("总数：" + pageStud.getTotalElements());

        List<Stu> content = pageStud.getContent();
        for (Stu stu : content) {
            System.out.println(stu);
        }

    }
}
package cn.mrcode.foodiedev;

import cn.mrcode.foodiedev.es.pojo.Stu;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.GetQuery;

/**
 * @author mrcode
 * @date 2021/8/26 23:06
 */
public class EsHighTest {
    @Test
    public void fun1() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("192.168.56.105:9200", "192.168.56.106:9200", "192.168.56.107:9200")
                .build();

        RestHighLevelClient rest = RestClients.create(clientConfiguration).rest();

        ElasticsearchRestTemplate elasticsearchRestTemplate = new ElasticsearchRestTemplate(rest);
        GetQuery getQuery = new GetQuery();
        getQuery.setId("1103");
        Stu stu = elasticsearchRestTemplate.queryForObject(getQuery, Stu.class);
        System.out.println(stu);
    }
}

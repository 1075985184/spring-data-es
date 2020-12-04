package com.example.demo;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
class DemoApplicationTests {

    @Qualifier("restHighLevelClient")
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    void testCreateIndex()throws Exception {

        CreateIndexRequest indexRequest = new CreateIndexRequest("test-spring-boot2");
        restHighLevelClient.indices().create(indexRequest, RequestOptions.DEFAULT);
    }

    @Test
    void testDeleteIndex()throws Exception {

        DeleteIndexRequest indexRequest = new DeleteIndexRequest("test-spring-boot2");
        restHighLevelClient.indices().delete(indexRequest, RequestOptions.DEFAULT);
    }


    @Test
    void testgetIndex()throws Exception {

        DeleteIndexRequest indexRequest = new DeleteIndexRequest("test-spring-boot2");
        restHighLevelClient.indices().delete(indexRequest, RequestOptions.DEFAULT);

    }

    @Test
    void test11()throws Exception {

        String status= "审核通过"  ;
        String substring = status.substring(2);
        System.out.println(substring);

    }

}

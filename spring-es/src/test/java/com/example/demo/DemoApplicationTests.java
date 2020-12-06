package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pojo.TestEs;
import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestOperations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


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

    @Test
    void testAddOneDocument()throws Exception {
        IndexRequest indexRequest=new IndexRequest("20201205");
            TestEs testEs = new TestEs();
            testEs.setAge(1);
            testEs.setCreate_time(LocalDateTime.now());
            testEs.setName("test 1");
            indexRequest.id("1");
            indexRequest.source(JSON.toJSONString(testEs), XContentType.JSON);
            IndexResponse index = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            System.out.println(index.toString());
            System.out.println(index.status());

    }


    @Test
    void testIsExistsDocument()throws Exception {
        GetIndexRequest getRequest = new GetIndexRequest("20201205");
        boolean exists = restHighLevelClient.indices().exists(getRequest, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    @Test
    void testisEDocument()throws Exception {
        GetRequest getRequest = new GetRequest("20201205","1");
        //不获取_sourced的上下文
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        boolean exists = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
        System.out.println(exists);
    }
    @Test
    //没测
    void testUpdateDocument()throws Exception {
       UpdateRequest updateRequest=new UpdateRequest("20201205","1");
        TestEs testEs = new TestEs();
        updateRequest.doc(JSON.toJSONString(testEs), XContentType.JSON);
        restHighLevelClient.update(updateRequest,RequestOptions.DEFAULT);
    }
    @Test
        //没测
    void testdeleteDocument()throws Exception {
        DeleteRequest deleteRequest=new DeleteRequest("20201205","1");
        restHighLevelClient.delete(deleteRequest,RequestOptions.DEFAULT);
    }

    @Test
    void test111()throws Exception {
        GetRequest getRequest = new GetRequest("20201205","1");
        //不获取_sourced的上下文
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        boolean exists = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
        System.out.println(exists);
    }


    @Test
    void testAddListDocument()throws Exception {
        GetIndexRequest getRequest = new GetIndexRequest("20201205");
        boolean exists = restHighLevelClient.indices().exists(getRequest, RequestOptions.DEFAULT);
        if(exists){
            BulkRequest bulkRequest = new BulkRequest();
            bulkRequest.timeout("10000m");
            for (int i = 20; i <30000 ; i++) {
                TestEs testEs = new TestEs();
                testEs.setAge(i);
                testEs.setCreate_time(LocalDateTime.now());
                testEs.setName("test "+i);
                bulkRequest.add(new IndexRequest("20201205").source(JSON.toJSONString(testEs),XContentType.JSON));
            }
            BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            System.out.println(bulk.toString());
            System.out.println(bulk.status());
        }


    }

    @Test
    void testselect()throws Exception {
        SearchRequest searchRequest = new SearchRequest("20201205");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "test");
//        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        searchSourceBuilder.query(termQueryBuilder);
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchSourceBuilder.from(1);
        searchSourceBuilder.size(5000);
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        SearchHit[] hits1 = hits.getHits();
        for (SearchHit documentFields : hits1) {
            System.out.println(documentFields.toString());
        }
        System.out.println(search.toString());
    }

    @Test
    void testScrollselect()throws Exception {
        SearchRequest searchRequest = new SearchRequest("20201205");
        final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
        searchRequest.scroll(scroll);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.sort("age", SortOrder.ASC);
//        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "test");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.termQuery("name","test"));


        searchSourceBuilder.size(5000);
//        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        SearchHit[] hits1 = hits.getHits();
        for (SearchHit documentFields : hits1) {
            System.out.println(JSON.toJSONString(documentFields));
        }
        System.out.println(search.getScrollId());
    }


    @Test
    void testScrollselectId()throws Exception {
        String scrollId="DnF1ZXJ5VGhlbkZldGNoBQAAAAAAAAmQFnVwdV9ndEJOUjd5dE5ZcGtRZzlxYncAAAAAAAAJjxZ1cHVfZ3RCTlI3eXROWXBrUWc5cWJ3AAAAAAAACZIWdXB1X2d0Qk5SN3l0Tllwa1FnOXFidwAAAAAAAAmRFnVwdV9ndEJOUjd5dE5ZcGtRZzlxYncAAAAAAAAJkxZ1cHVfZ3RCTlI3eXROWXBrUWc5cWJ3";
        SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
        scrollRequest.scroll(new Scroll(TimeValue.timeValueMinutes(1L)));
        SearchResponse search = restHighLevelClient.scroll(scrollRequest, RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        SearchHit[] hits1 = hits.getHits();
        for (SearchHit documentFields : hits1) {
            System.out.println(JSON.toJSONString(documentFields));
        }
        System.out.println(search.getScrollId());
    }



}

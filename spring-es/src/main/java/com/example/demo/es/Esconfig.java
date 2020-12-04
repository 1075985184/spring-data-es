/**
 * Copyright (C), 2015‐2020,
 * Author:  lihui
 * Date:  2020/12/1 10:42
 * History:
 * <author> <time> <version> <desc>
 */
package com.example.demo.es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Esconfig {

  @Qualifier("restHighLevelClient")
  @Bean
  public RestHighLevelClient getclient(){
    return new RestHighLevelClient(
            RestClient.builder(new HttpHost("127.0.0.1", 9200, "http")));
  }

}

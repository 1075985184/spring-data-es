/**
 * Copyright (C), 2015‐2020,
 * Author:  lihui
 * Date:  2020/12/2 10:26
 * History:
 * <author> <time> <version> <desc>
 */
package com.example.demo.cache;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class LocalCache {
    private Map<String, String> maProcessRuleMap = new HashMap<>();


    @PostConstruct
    private void init() {
       this.maProcessRuleMap.put("test","test1");
    }

    public Map<String, String> loadMaProcessRule() {
        return this.maProcessRuleMap;
    }
    public void set() {
        this.maProcessRuleMap.put("test",this.maProcessRuleMap.get("test")+"t");
    }

}

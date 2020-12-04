/**
 * Copyright (C), 2015‐2020,
 * Author:  lihui
 * Date:  2020/12/2 10:34
 * History:
 * <author> <time> <version> <desc>
 */
package com.example.demo.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CacheImpl implements CacheService{

    @Autowired
    LocalCache localCache;

    @Override
    public Map getLocalCache() {
        localCache.set();
        return localCache.loadMaProcessRule();
    }
}

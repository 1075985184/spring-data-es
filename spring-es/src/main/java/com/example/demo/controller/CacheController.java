/**
 * Copyright (C), 2015‐2020,
 * Author:  lihui
 * Date:  2020/12/2 10:32
 * History:
 * <author> <time> <version> <desc>
 */
package com.example.demo.controller;

import com.example.demo.cache.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/cahce")
public class CacheController {

@Autowired
private CacheService cacheService;

    @GetMapping("/get")
    public Map selectCache(){

       return cacheService.getLocalCache();
    }
}

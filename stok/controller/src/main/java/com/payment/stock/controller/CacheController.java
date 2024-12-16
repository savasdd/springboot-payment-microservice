package com.payment.stock.controller;

import com.payment.stock.common.utils.CacheUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api/payment/stocks/cache")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CacheController {
    private static final List<String> CACHE_LIST = List.of(CacheUtil.CACHE_NAME, CacheUtil.GET_IMAGE);
    private final CacheManager cacheManager;

    @GetMapping(value = "/get-cache")
    public ResponseEntity<List<String>> index() {
        return ResponseEntity.ok(CACHE_LIST);
    }

    @GetMapping(value = "/clear-cache")
    public ResponseEntity<String> clearCache(@RequestParam(value = "name") String name) {
        Objects.requireNonNull(cacheManager.getCache(name)).clear();
        return ResponseEntity.ok("Cache cleared");
    }

    @GetMapping(value = "/clear-all-cache")
    public ResponseEntity<String> clearAllCache() {
        cacheManager.getCacheNames().stream().filter(CACHE_LIST::contains).map(cacheManager::getCache).filter(Objects::nonNull).forEach(Cache::clear);
        return ResponseEntity.ok("Successfully cleared cache");
    }
}

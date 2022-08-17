package com.zzangmin.gesipan.practice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED) // 실제 DB 사용하고 싶을때 NONE 사용
public class practice1 {


    @Autowired
    RedisTemplate redisTemplate;



    @Test
    void asdf() {
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        connection.flushAll();
        HashOperations<String, Long, Long> hashOperations = redisTemplate.opsForHash();
        RedisSerializer hashKeySerializer = redisTemplate.getHashKeySerializer();
        System.out.println("hashKeySerializer = " + hashKeySerializer);
        //hashOperations.increment("bbb",345L,1);
        //hashOperations.increment("bbb",567L,1);

        redisTemplate.opsForHash().increment("bbb",123L,1);
        Set<Long> bbb = hashOperations.keys("bbb");
        System.out.println("1351rwadf = " + bbb);
        ScanOptions build = ScanOptions.scanOptions()
                .count(100)
                .build();

        //Cursor<Map.Entry<Long, Long>> bbb1 = hashOperations.scan("bbb", build);
        Cursor<Map.Entry<Long, Long>> scan = hashOperations.scan("bbb", build);

        while (scan.hasNext()) {
            Map.Entry<Long, Long> next = scan.next();
            String s = next.getKey().toString();
            System.out.println("s = " + s);


        }


    }


}
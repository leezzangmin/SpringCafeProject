package com.zzangmin.gesipan.practice;

import com.zzangmin.gesipan.component.login.service.JwtProvider;
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

import java.util.Map;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED) // 실제 DB 사용하고 싶을때 NONE 사용
public class practice1 {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    JwtProvider jwtProvider;

    @Test
    void asdf() {

        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        connection.flushAll();
        String a="key1";
        HashOperations<String, Long, Long> hashOperations = redisTemplate.opsForHash();
        RedisSerializer hashKeySerializer = redisTemplate.getHashKeySerializer();

        System.out.println("hashKeySerializer = " + hashKeySerializer);


        redisTemplate.opsForHash().increment(a,123L,1);


        ScanOptions build = ScanOptions.scanOptions()
                .count(100)
                .build();

        //Cursor<Map.Entry<Long, Long>> bbb1 = hashOperations.scan("bbb", build);
        Cursor<Map.Entry<Long, Long>> scan = hashOperations.scan(a, build);

        while (scan.hasNext()) {
            Map.Entry<Long, Long> next = scan.next();
            String s = next.getKey().toString();
            System.out.println("s = " + s);


        }


    }

    @Test
    void token() {
        String token = jwtProvider.createToken(33L);
        System.out.println("token = " + token);
    }


}

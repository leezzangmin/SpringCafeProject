//package com.zzangmin.gesipan.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//import org.springframework.util.StringUtils;
//import redis.embedded.RedisServer;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//
//@Slf4j
//@Profile("test")
//@Configuration
//public class EmbeddedTestRedisConfig {
//
//    //@Value("${spring.redis.port}")
//    // 윈도우 마이그레이션 하면서 임시로 랜덤 포트 할당받게 함
//    private int port = (int) (Math.random() * (65535 - 10000 + 1)) + 10000;
//
//    @Value("${spring.redis.host}")
//    private String host;
//
//    private RedisServer redisServer;
//
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        return new LettuceConnectionFactory(host, port);
//    }
//
//    @Bean
//    public RedisTemplate<?, ?> redisTemplate() {
//        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory());
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        redisTemplate.setHashKeySerializer(new Jackson2JsonRedisSerializer<>(Long.class));
//        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Long.class));
//        return redisTemplate;
//    }
//
//    @PostConstruct
//    public void redisServer() throws IOException {
////        int port = isRedisRunning() ? findAvailablePort() : this.port;
//        redisServer = new RedisServer(port);
//        System.out.println("port = " + port);
//        redisServer.start();
//    }
//
//    @PreDestroy
//    public void stopRedis() {
//        if (redisServer != null) {
//            redisServer.stop();
//        }
//    }
//
//    /**
//     * Embedded Redis가 현재 실행중인지 확인
//     */
//    private boolean isRedisRunning() throws IOException {
//        return isRunning(executeGrepProcessCommandWindows(port));
//    }
//
//    /**
//     * 현재 PC/서버에서 사용가능한 포트 조회
//     * 윈도우에서 사용가능한 시스템콜 함수 찾아서 메서드 수정 필요
//     */
//
//    public int findAvailablePort() throws IOException {
//
//        for (int port = 10000; port <= 65535; port++) {
//            Process process = executeGrepProcessCommandWindows(port);
//            if (!isRunning(process)) {
//                return port;
//            }
//        }
//
//        throw new IllegalArgumentException("Not Found Available port: 10000 ~ 65535");
//    }
//
//    /**
//     * 해당 port를 사용중인 프로세스 확인하는 sh 실행
//     */
//    private Process executeGrepProcessCommandMac(int port) throws IOException {
//        String command = String.format("netstat -nat | grep LISTEN|grep %d", port);
//        String[] shell = {"/bin/sh", "-c", command};
//        return Runtime.getRuntime().exec(shell);
//    }
//        private Process executeGrepProcessCommandWindows(int port) throws IOException {
//        String command = String.format("netstat -nao | find \"LISTEN\" | find \"%d\"", port);
//        String[] shell = {"cmd.exe", "/y", "/c", command};
//        return Runtime.getRuntime().exec(shell);
//    }
//
//    /**
//     * 해당 Process가 현재 실행중인지 확인
//     */
//    private boolean isRunning(Process process) {
//        String line;
//        StringBuilder pidInfo = new StringBuilder();
//
//        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//
//            while ((line = input.readLine()) != null) {
//                pidInfo.append(line);
//            }
//
//        } catch (Exception e) {}
//        return !StringUtils.isEmpty(pidInfo.toString());
//    }
//}

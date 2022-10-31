package com.zzangmin.gesipan.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zzangmin.gesipan.layer.basiccrud.repository.custom.CustomPostRepository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuerydslConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public CustomPostRepository customPostRepository() {
        return new CustomPostRepository(jpaQueryFactory());
    }
}

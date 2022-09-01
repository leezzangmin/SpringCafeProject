package com.zzangmin.gesipan.web.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTemporaryPost is a Querydsl query type for TemporaryPost
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTemporaryPost extends EntityPathBase<TemporaryPost> {

    private static final long serialVersionUID = 1934885230L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTemporaryPost temporaryPost = new QTemporaryPost("temporaryPost");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath postContent = createString("postContent");

    public final StringPath postSubject = createString("postSubject");

    public final NumberPath<Long> tempPostId = createNumber("tempPostId", Long.class);

    public final QUsers user;

    public QTemporaryPost(String variable) {
        this(TemporaryPost.class, forVariable(variable), INITS);
    }

    public QTemporaryPost(Path<? extends TemporaryPost> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTemporaryPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTemporaryPost(PathMetadata metadata, PathInits inits) {
        this(TemporaryPost.class, metadata, inits);
    }

    public QTemporaryPost(Class<? extends TemporaryPost> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUsers(forProperty("user")) : null;
    }

}


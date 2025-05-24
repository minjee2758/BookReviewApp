package com.example.bookreviewapp.domain.review.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = -37718504L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReview review = new QReview("review");

    public final com.example.bookreviewapp.common.entity.QBaseEntity _super = new com.example.bookreviewapp.common.entity.QBaseEntity(this);

    public final com.example.bookreviewapp.domain.book.entity.QBook book;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<PinStatus> isPinned = createEnum("isPinned", PinStatus.class);

    public final NumberPath<Integer> score = createNumber("score", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.example.bookreviewapp.domain.user.entity.QUser user;

    public final NumberPath<Long> viewer = createNumber("viewer", Long.class);

    public QReview(String variable) {
        this(Review.class, forVariable(variable), INITS);
    }

    public QReview(Path<? extends Review> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReview(PathMetadata metadata, PathInits inits) {
        this(Review.class, metadata, inits);
    }

    public QReview(Class<? extends Review> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.book = inits.isInitialized("book") ? new com.example.bookreviewapp.domain.book.entity.QBook(forProperty("book"), inits.get("book")) : null;
        this.user = inits.isInitialized("user") ? new com.example.bookreviewapp.domain.user.entity.QUser(forProperty("user")) : null;
    }

}


package com.capgemini.store.persistence.repo.impl;

import com.capgemini.store.persistence.TransactionSearchCriteria;
import com.capgemini.store.persistence.entity.QProductEntity;
import com.capgemini.store.persistence.entity.QTransactionEntity;
import com.capgemini.store.persistence.entity.TransactionEntity;
import com.capgemini.store.persistence.repo.TransactionRepoCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionRepoImpl implements TransactionRepoCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private QTransactionEntity transaction;

    private QProductEntity product;

    private JPAQueryFactory queryFactory;

    @PostConstruct
    private void initQueryDSL(){
        transaction = QTransactionEntity.transactionEntity;
        product = QProductEntity.productEntity;
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<TransactionEntity> findTransactionsByCriteria(TransactionSearchCriteria tsc){
        BooleanBuilder builder = new BooleanBuilder();
        if (tsc.getStatus() != null){
            builder.and(transaction.status.eq(tsc.getStatus()));
        }
        if (tsc.getStartDate() != null && tsc.getEndDate() != null){
            builder.and(transaction.date.between(tsc.getStartDate(), tsc.getEndDate()));
        }
        if (tsc.getIdProduct() != null){
            builder.and(transaction.product.id.eq(tsc.getIdProduct()));
        }
        if (tsc.getTotalPrice() != null){
            builder.and(transaction.product.price.multiply(transaction.numberProducts).eq(tsc.getTotalPrice()));
        }
        return queryFactory.selectFrom(transaction).where(builder).fetch();
    }

    @Override
    public Double calculateProfitInPeriod(LocalDateTime startDate, LocalDateTime endDate){
        return queryFactory.selectFrom(transaction)
                .select(product.price.multiply(transaction.numberProducts).multiply(product.margin).sum())
                .innerJoin(transaction.product, product)
                .where(transaction.date.between(startDate, endDate))
                .fetchOne();
    }
}

package com.capgemini.store.persistence.repo.impl;

import com.capgemini.store.persistence.entity.*;
import com.capgemini.store.persistence.repo.ClientRepoCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

public class ClientRepoImpl implements ClientRepoCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private QClientEntity client;

    private QTransactionEntity transaction;

    private QProductEntity product;

    private JPAQueryFactory queryFactory;

    @PostConstruct
    private void initQueryDSL(){
        transaction = QTransactionEntity.transactionEntity;
        client = QClientEntity.clientEntity;
        product = QProductEntity.productEntity;
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Double calculateTotalCostOfTransactionsForSpecifiedClient(Long idClient){
        return queryFactory.selectFrom(client)
                .select((transaction.product.price.multiply(transaction.numberProducts)).sum())
                .innerJoin(client.transactions, transaction)
                .where(client.id.eq(idClient))
                .fetchOne();
    }

    @Override
    public Double calculateTotalCostOfTransactionsWithSpecifiedStatusForSpecifiedClient(Long idClient, Status status){
        return queryFactory.selectFrom(client)
                .select((transaction.product.price.multiply(transaction.numberProducts)).sum())
                .innerJoin(client.transactions, transaction)
                .where(client.id.eq(idClient).and(transaction.status.eq(status)))
                .fetchOne();
    }

    @Override
    public List<ClientEntity> findClientsWhoSpendTheMostMoneyInSpecifiedPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return queryFactory.selectFrom(client)
                .select(client)
                .innerJoin(client.transactions, transaction)
                .innerJoin(transaction.product, product)
                .groupBy(client.id)
                .where(transaction.date.between(startDate, endDate))
                .orderBy(product.price.multiply(transaction.numberProducts).sum().desc())
                .limit(2)
                .fetch();
    }
}

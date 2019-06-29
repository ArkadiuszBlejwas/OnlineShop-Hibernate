package com.capgemini.store.persistence.repo.impl;

import com.capgemini.store.persistence.dto.ProductInRealizaton;
import com.capgemini.store.persistence.entity.ProductEntity;
import com.capgemini.store.persistence.entity.QProductEntity;
import com.capgemini.store.persistence.entity.QTransactionEntity;
import com.capgemini.store.persistence.entity.TransactionEntity;
import com.capgemini.store.persistence.repo.ProductRepoCustom;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.capgemini.store.persistence.entity.Status.in_realization;

public class ProductRepoImpl implements ProductRepoCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private CriteriaBuilder criteriaBuilder;

    private CriteriaQuery<ProductEntity> query;

    private Root<ProductEntity> root;

    private QTransactionEntity transaction;

    private QProductEntity product;

    private JPAQueryFactory queryFactory;

    @PostConstruct
    public void initCriteriaAPI(){
        criteriaBuilder = entityManager.getCriteriaBuilder();
        query = criteriaBuilder.createQuery(ProductEntity.class);
        root = query.from(ProductEntity.class);
    }

    @PostConstruct
    private void initQueryDSL(){
        transaction = QTransactionEntity.transactionEntity;
        product = QProductEntity.productEntity;
        queryFactory = new JPAQueryFactory(entityManager);
    }


    @Override
    public List<ProductEntity> find10BestSellingProducts(){
        Join<ProductEntity, TransactionEntity> joinProductTransaction = root.join("transactions", JoinType.LEFT);
        Predicate predicate = criteriaBuilder.equal(joinProductTransaction.get("id"), root.get("id"));
        TypedQuery<ProductEntity> typedQuery = entityManager.createQuery(query.select(root)
                        .where(predicate)
                        .groupBy(joinProductTransaction.get("id"))
                        .orderBy(criteriaBuilder.desc(joinProductTransaction.get("id"))))
                .setMaxResults(10);
        return typedQuery.getResultList();
    }

    @Override
    public List<ProductEntity> findProductsWhichAreInRealization(){
        Join<ProductEntity, TransactionEntity> joinProductTransaction = root.join("transactions", JoinType.LEFT);
        Predicate predicate = criteriaBuilder.equal(joinProductTransaction.get("status"), in_realization);
        TypedQuery<ProductEntity> typedQuery = entityManager.createQuery(query.select(root)
                .where(predicate)
                .groupBy(root.get("id")));
        return typedQuery.getResultList();
    }

    @Override
    public List<ProductInRealizaton> findProductsWhichAreInRealizationInObject() {
        List<Tuple> listTuple = queryFactory
                .select(product.name, transaction.numberProducts)
                .from(transaction)
                .join(transaction.product, product)
                .where(transaction.status.eq(in_realization))
                .groupBy(product.id)
                .fetch();

        return listTuple.stream()
                .map(tuple -> new ProductInRealizaton(tuple.get(0, String.class), tuple.get(1, Integer.class)))
                .collect(Collectors.toList());
    }
}

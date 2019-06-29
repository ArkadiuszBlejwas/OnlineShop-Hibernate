package com.capgemini.store.persistence.repo;

import com.capgemini.store.persistence.TransactionSearchCriteria;
import com.capgemini.store.persistence.entity.TransactionEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepoCustom {

    List<TransactionEntity> findTransactionsByCriteria(TransactionSearchCriteria tsc);

    Double calculateProfitInPeriod(LocalDateTime startDate, LocalDateTime endDate);
}

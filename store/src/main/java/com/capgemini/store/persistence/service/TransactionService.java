package com.capgemini.store.persistence.service;

import com.capgemini.store.persistence.TransactionSearchCriteria;
import com.capgemini.store.persistence.dto.TransactionDTO;
import com.capgemini.store.persistence.repo.TransactionRepo;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService extends Service {

    List<TransactionDTO> findAllTransactions();

    TransactionDTO findTransactionById(Long id);

    TransactionDTO addTransaction(TransactionDTO transactionDTO);

    TransactionDTO updateTransaction(TransactionDTO transactionDTO);

    void deleteTransaction(Long id);

    TransactionRepo getTransactionRepo();

    List<TransactionDTO> findTransactionsByCriteria(TransactionSearchCriteria tsc);

    Double calculateProfitInPeriod(LocalDateTime startDate, LocalDateTime endDate);
}

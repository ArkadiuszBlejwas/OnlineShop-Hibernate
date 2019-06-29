package com.capgemini.store.persistence.repo;

import com.capgemini.store.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo extends JpaRepository<TransactionEntity, Long>, TransactionRepoCustom{
}

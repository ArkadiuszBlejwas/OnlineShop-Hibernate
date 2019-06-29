package com.capgemini.store.persistence.repo;

import com.capgemini.store.persistence.entity.ClientEntity;
import com.capgemini.store.persistence.entity.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface ClientRepoCustom {

    Double calculateTotalCostOfTransactionsForSpecifiedClient(Long idClient);

    Double calculateTotalCostOfTransactionsWithSpecifiedStatusForSpecifiedClient(Long idClient, Status status);

    List<ClientEntity> findClientsWhoSpendTheMostMoneyInSpecifiedPeriod(LocalDateTime startDate, LocalDateTime endDate);
}

package com.capgemini.store.persistence.service;

import com.capgemini.store.persistence.dto.ClientDTO;
import com.capgemini.store.persistence.dto.TransactionDTO;
import com.capgemini.store.persistence.entity.Status;
import com.capgemini.store.persistence.repo.ClientRepo;

import java.time.LocalDateTime;
import java.util.List;

public interface ClientService extends Service {

    List<ClientDTO> findAllClients();

    ClientDTO findClientById(Long id);

    ClientDTO addClient(ClientDTO clientDTO);

    ClientDTO updateClient(ClientDTO clientDTO);

    void deleteClient(Long id);

    ClientRepo getClientRepo();

    ClientDTO performTransaction(Long idClient, TransactionDTO transactionDTO, Long idProduct);

    Double calculateTotalCostOfTransactionsForSpecifiedClient(Long idClient);

    Double calculateTotalCostOfTransactionsWithSpecifiedStatusForSpecifiedClient(Long idClient, Status status);

    List<ClientDTO> findClientsWhoSpendTheMostMoneyInSpecifiedPeriod(LocalDateTime startDate, LocalDateTime endDate);

}

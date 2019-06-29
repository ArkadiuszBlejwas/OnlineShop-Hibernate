package com.capgemini.store.persistence.service.impl;

import com.capgemini.store.persistence.dto.ClientDTO;
import com.capgemini.store.persistence.dto.ProductDTO;
import com.capgemini.store.persistence.dto.TransactionDTO;
import com.capgemini.store.persistence.entity.ClientEntity;
import com.capgemini.store.persistence.entity.Status;
import com.capgemini.store.persistence.entity.TransactionEntity;
import com.capgemini.store.persistence.mapper.ClientMapper;
import com.capgemini.store.persistence.repo.ClientRepo;
import com.capgemini.store.persistence.repo.TransactionRepo;
import com.capgemini.store.persistence.service.ClientService;
import com.capgemini.store.persistence.service.ProductService;
import com.capgemini.store.persistence.service.exception.TransactionCantBeExecutedException;
import com.capgemini.store.persistence.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ClientServiceImpl implements ClientService {

    private ClientMapper clientMapper;

    private ClientRepo clientRepo;

    private TransactionService transactionService;

    private ProductService productService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ClientServiceImpl(ClientMapper clientMapper, ClientRepo clientRepo, TransactionService transactionService, ProductService productService){
        this.clientMapper = clientMapper;
        this.clientRepo = clientRepo;
        this.transactionService = transactionService;
        this.productService = productService;
    }

    @Override
    public List<ClientDTO> findAllClients(){
        return clientMapper.mapToDTOList(clientRepo.findAll());
    }

    @Override
    public ClientDTO findClientById(Long id){
        checkWhetherArgumentIsNull(id);
        ClientEntity clientEntity = clientRepo.getOne(id);
        return clientMapper.mapToDTO(clientEntity);
    }


    @Override
    @Transactional(readOnly = false)
    public ClientDTO addClient(ClientDTO clientDTO){
        checkWhetherArgumentIsNull(clientDTO);
        ClientEntity clientEntity = addTransactionsToClientEntity(clientDTO);
        clientEntity = clientRepo.save(clientEntity);
        return clientMapper.mapToDTO(clientEntity);
    }

    private ClientEntity addTransactionsToClientEntity(ClientDTO client){
        ClientEntity clientEntity = clientMapper.mapToEntity(client);
        TransactionRepo transactionRepo = transactionService.getTransactionRepo();

        Set<TransactionEntity> transactions = client.getTransactions().stream()
                .map(transactionRepo::getOne)
                .collect(Collectors.toSet());

        clientEntity.setTransactions(transactions);
        return clientEntity;
    }

    @Override
    @Transactional(readOnly = false)
    public ClientDTO updateClient(ClientDTO clientDTO){
        checkWhetherArgumentIsNull(clientDTO);
        ClientDTO client = findClientById(clientDTO.getId());
        client.setFirstName(clientDTO.getFirstName());
        client.setSurName(clientDTO.getSurName());
        client.setAddress(clientDTO.getAddress());
        client.setBirthDate(clientDTO.getBirthDate());
        client.setTransactions(clientDTO.getTransactions());
        ClientEntity clientEntity = addTransactionsToClientEntity(client);

        clientRepo.save(clientEntity);
        return clientMapper.mapToDTO(clientEntity);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteClient(Long id){
        checkWhetherArgumentIsNull(id);
        clientRepo.deleteById(id);
    }

    public ClientRepo getClientRepo(){
        return clientRepo;
    }

    private boolean hasClientLessThan3ExecutedTransactions(Long idClient){
        ClientEntity clientEntity = clientRepo.getOne(idClient);
        Set<TransactionEntity> transactions = clientEntity.getTransactions();
        int executedTransactionCounter = 0;
        for (TransactionEntity t : transactions){
            if (t.getStatus() != null && t.getStatus().equals(Status.executed)){
                ++executedTransactionCounter;
                if (executedTransactionCounter == 3){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    @Transactional(readOnly = false)
    public ClientDTO performTransaction(Long idClient, TransactionDTO transactionDTO, Long idProduct){
        transactionDTO.setStatus(Status.in_realization);
        ProductDTO productDTO = productService.findProductById(idProduct);
        ClientDTO clientDTO = findClientById(idClient);

        if (hasClientLessThan3ExecutedTransactions(idClient)){
            Double unitPrice = productDTO.getPrice();
            Double totalPrice = transactionDTO.getNumberProducts() * unitPrice;

            if (totalPrice > 5000.0){
                throw new TransactionCantBeExecutedException("You can't perform transaction with payment greater than 5000.0" +
                        "when has less than 3 executed transactions");
            }
        }
        transactionDTO.setClient(idClient);
        transactionDTO.setProduct(idProduct);
        TransactionDTO savedTransaction = transactionService.addTransaction(transactionDTO);

        Set<Long> transactionsOfProduct = productDTO.getTransactions();
        transactionsOfProduct.add(savedTransaction.getId());
        productDTO.setTransactions(transactionsOfProduct);
        productService.updateProduct(productDTO);

        Set<Long> transactionsOfClient = clientDTO.getTransactions();
        transactionsOfClient.add(savedTransaction.getId());
        clientDTO.setTransactions(transactionsOfClient);
        return updateClient(clientDTO);
    }

    @Override
    public Double calculateTotalCostOfTransactionsForSpecifiedClient(Long idClient){
        checkWhetherArgumentIsNull(idClient);
        return clientRepo.calculateTotalCostOfTransactionsForSpecifiedClient(idClient);
    }

    @Override
    public Double calculateTotalCostOfTransactionsWithSpecifiedStatusForSpecifiedClient(Long idClient, Status status){
        checkWhetherArgumentIsNull(idClient, status);
        return clientRepo.calculateTotalCostOfTransactionsWithSpecifiedStatusForSpecifiedClient(idClient, status);
    }

    @Override
    public List<ClientDTO> findClientsWhoSpendTheMostMoneyInSpecifiedPeriod(LocalDateTime startDate, LocalDateTime endDate){
        checkWhetherArgumentIsNull(startDate, endDate);
        return clientMapper.mapToDTOList(clientRepo.findClientsWhoSpendTheMostMoneyInSpecifiedPeriod(startDate, endDate));
    }
}

package com.capgemini.store.persistence.service.impl;

import com.capgemini.store.persistence.TransactionSearchCriteria;
import com.capgemini.store.persistence.dto.TransactionDTO;
import com.capgemini.store.persistence.entity.ClientEntity;
import com.capgemini.store.persistence.entity.ProductEntity;
import com.capgemini.store.persistence.entity.TransactionEntity;
import com.capgemini.store.persistence.mapper.TransactionMapper;
import com.capgemini.store.persistence.repo.ClientRepo;
import com.capgemini.store.persistence.repo.ProductRepo;
import com.capgemini.store.persistence.repo.TransactionRepo;
import com.capgemini.store.persistence.service.exception.TransactionCantBeExecutedException;
import com.capgemini.store.persistence.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepo transactionRepo;

    private TransactionMapper transactionMapper;

    private ProductRepo productRepo;

    private ClientRepo clientRepo;

    @Autowired
    public TransactionServiceImpl(TransactionRepo transactionRepo, TransactionMapper transactionMapper, ProductRepo productRepo, ClientRepo clientRepo){
        this.transactionRepo = transactionRepo;
        this.transactionMapper = transactionMapper;
        this.productRepo = productRepo;
        this.clientRepo = clientRepo;
    }

    @Override
    public List<TransactionDTO> findAllTransactions(){
        return transactionMapper.mapToDTOList(transactionRepo.findAll());
    }

    @Override
    public TransactionDTO findTransactionById(Long id){
        checkWhetherArgumentIsNull(id);
        return transactionMapper.mapToDTO(transactionRepo.getOne(id));
    }

    private TransactionEntity addProductAndClientToTransactionEntity(TransactionDTO transaction){
        TransactionEntity transactionEntity = transactionMapper.mapToEntity(transaction);
        if (transaction.getProduct() != null){
            ProductEntity product = productRepo.getOne(transaction.getProduct());
            transactionEntity.setProduct(product);
        }
        if (transaction.getClient() != null){
            ClientEntity client = clientRepo.getOne(transaction.getClient());
            transactionEntity.setClient(client);
        }
        return transactionEntity;
    }

    @Override
    @Transactional(readOnly = false)
    public TransactionDTO addTransaction(TransactionDTO transactionDTO){
        checkWhetherArgumentIsNull(transactionDTO);
        TransactionEntity transactionEntity = addProductAndClientToTransactionEntity(transactionDTO);
        checkWhetherTransactionCanBeExecuted(transactionEntity);

        transactionEntity = transactionRepo.save(transactionEntity);
        return transactionMapper.mapToDTO(transactionEntity);
    }

    @Override
    @Transactional(readOnly = false)
    public TransactionDTO updateTransaction(TransactionDTO transactionDTO){
        checkWhetherArgumentIsNull(transactionDTO);
        TransactionDTO transaction = findTransactionById(transactionDTO.getId());
        transaction.setDate(transactionDTO.getDate());
        transaction.setNumberProducts(transactionDTO.getNumberProducts());
        transaction.setStatus(transactionDTO.getStatus());
        transaction.setClient(transactionDTO.getClient());
        transaction.setProduct(transactionDTO.getProduct());
        TransactionEntity transactionEntity = addProductAndClientToTransactionEntity(transaction);

        checkWhetherTransactionCanBeExecuted(transactionEntity);
        transactionRepo.save(transactionEntity);
        return transactionMapper.mapToDTO(transactionEntity);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteTransaction(Long id){
        checkWhetherArgumentIsNull(id);
        transactionRepo.deleteById(id);
    }

    public TransactionRepo getTransactionRepo(){
        return transactionRepo;
    }

    private boolean hasMoreThan5TheSameProductsWhoseUnitPriceIsOver7000(TransactionEntity transactionEntity){
        if (transactionEntity.getProduct() == null
                || transactionEntity.getProduct().getPrice() == null) {
            return false;
        }

        Double unitPrice = transactionEntity.getProduct().getPrice();

        if (unitPrice > 7000.0){
            if (transactionEntity.getNumberProducts() > 5){
                return true;
            }
        }
        return false;
    }

    private boolean hasMoreThan25kg(TransactionEntity transactionEntity){
        if (transactionEntity.getProduct() == null
                || transactionEntity.getProduct().getWeight() == null
                || transactionEntity.getNumberProducts() == null){
            return false;
        }

        Double totalWeight = transactionEntity.getProduct().getWeight() * transactionEntity.getNumberProducts();

        if (totalWeight > 25.0){
            return true;
        }
        return false;
    }

    private void checkWhetherTransactionCanBeExecuted(TransactionEntity transactionEntity){
        if (hasMoreThan5TheSameProductsWhoseUnitPriceIsOver7000(transactionEntity)){
            throw new TransactionCantBeExecutedException("You can't perform transaction" +
                    " which has more than 5 the same products whose unit price is over 7000");
        }

        if (hasMoreThan25kg(transactionEntity)){
            throw  new TransactionCantBeExecutedException("You can't perform transaction" +
                    " which has products which has more than 25 kg. Deacrese quantity of products" +
                    " and add rest of products to new transaction");
        }
    }

    @Override
    public List<TransactionDTO> findTransactionsByCriteria(TransactionSearchCriteria tsc){
        checkWhetherArgumentIsNull(tsc);
        return transactionMapper.mapToDTOList(transactionRepo.findTransactionsByCriteria(tsc));
    }

    @Override
    public Double calculateProfitInPeriod(LocalDateTime startDate, LocalDateTime endDate){
        return transactionRepo.calculateProfitInPeriod(startDate, endDate);
    }
}

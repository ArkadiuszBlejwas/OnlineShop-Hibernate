package com.capgemini.store.persistence.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import com.capgemini.store.StarterkitApplication;
import com.capgemini.store.persistence.TransactionSearchCriteria;
import com.capgemini.store.persistence.dto.ClientDTO;
import com.capgemini.store.persistence.dto.ProductDTO;
import com.capgemini.store.persistence.dto.TransactionDTO;
import com.capgemini.store.persistence.entity.Address;
import com.capgemini.store.persistence.entity.Status;
import com.capgemini.store.persistence.mapper.TransactionMapper;
import com.capgemini.store.persistence.repo.TransactionRepo;
import com.capgemini.store.persistence.service.ClientService;
import com.capgemini.store.persistence.service.ProductService;
import com.capgemini.store.persistence.service.TransactionService;
import com.capgemini.store.persistence.service.exception.ArgumentIsNullException;
import com.capgemini.store.persistence.service.exception.TransactionCantBeExecutedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Transactional
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(classes = {StarterkitApplication.class}, properties = "spring.profiles.active=hsql")

public class TransactionServiceImplTest {

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ProductService productService;

    @Autowired
    private TransactionSearchCriteria criteria;

    @PersistenceContext
    private EntityManager entityManager;

    private TransactionDTO generateFirstTransaction(){
        ClientDTO client1 = generateFirstClient();
        ClientDTO savedClient1 = clientService.addClient(client1);
        ProductDTO product1 = generateFirstProduct();
        ProductDTO savedProduct1 = productService.addProduct(product1);
        return TransactionDTO.builder()
                .client(savedClient1.getId())
                .product(savedProduct1.getId())
                .numberProducts(5)
                .status(Status.canceled)
                .date(LocalDateTime.of(2019, Month.FEBRUARY, 2, 18, 20))
                .build();
    }

    private TransactionDTO generateSecondTransaction(){
        ClientDTO client2 = generateSecondClient();
        ClientDTO savedClient2 = clientService.addClient(client2);
        ProductDTO product2 = generateSecondProduct();
        ProductDTO savedProduct2 = productService.addProduct(product2);
        return TransactionDTO.builder()
                .client(savedClient2.getId())
                .product(savedProduct2.getId())
                .numberProducts(2)
                .status(Status.in_realization)
                .date(LocalDateTime.of(2019, Month.MARCH, 3, 15, 30))
                .build();
    }

    private TransactionDTO generateThirdTransaction(){
        ClientDTO client2 = generateSecondClient();
        ClientDTO savedClient2 = clientService.addClient(client2);
        ProductDTO product3 = generateThirdProduct();
        ProductDTO savedProduct3 = productService.addProduct(product3);
        return TransactionDTO.builder()
                .client(savedClient2.getId())
                .product(savedProduct3.getId())
                .numberProducts(2)
                .status(Status.in_realization)
                .date(LocalDateTime.of(2019, Month.MARCH, 3, 15, 30))
                .build();
    }

    private TransactionDTO generateFourthTransaction(){
        ClientDTO client2 = generateSecondClient();
        ClientDTO savedClient2 = clientService.addClient(client2);
        ProductDTO product4 = generateFourthProduct();
        ProductDTO savedProduct4 = productService.addProduct(product4);
        return TransactionDTO.builder()
                .client(savedClient2.getId())
                .product(savedProduct4.getId())
                .numberProducts(6)
                .status(Status.in_realization)
                .date(LocalDateTime.of(2019, Month.MARCH, 3, 15, 30))
                .build();
    }

    private Address generateFirstAddress(){
        return Address.builder()
                .country("Polska")
                .city("Poznań")
                .street("ul. Kolorowa 1")
                .email("mojmail@gmail.com")
                .phoneNumber("777888999")
                .zipCode("679-67")
                .build();
    }

    private ClientDTO generateFirstClient(){
        return ClientDTO.builder()
                .firstName("Arek")
                .surName("Blejwas")
                .address(generateFirstAddress())
                .birthDate(LocalDate.of(1996, 1, 1))
                .transactions(new HashSet<>())
                .build();
    }

    private ClientDTO generateSecondClient(){
        return ClientDTO.builder()
                .firstName("Adam")
                .surName("Nowak")
                .address(generateFirstAddress())
                .birthDate(LocalDate.of(1995, 2, 2))
                .transactions(new HashSet<>())
                .build();
    }

    private ProductDTO generateFirstProduct(){
        return ProductDTO.builder()
                .name("Laptop Thinkpad T570")
                .price(3000.0)
                .margin(0.1)
                .weight(2.5)
                .transactions(new HashSet<>())
                .build();
    }

    private ProductDTO generateSecondProduct(){
        return ProductDTO.builder()
                .name("Książka 'Java dla zielonych' autor: Arkadiusz Blejwas")
                .price(50.0)
                .margin(0.1)
                .weight(0.5)
                .transactions(new HashSet<>())
                .build();
    }

    private ProductDTO generateThirdProduct(){
        return ProductDTO.builder()
                .name("Ziemniaki")
                .price(50.0)
                .margin(0.1)
                .weight(20.0)
                .transactions(new HashSet<>())
                .build();
    }

    private ProductDTO generateFourthProduct(){
        return ProductDTO.builder()
                .name("MacBook Pro TB Sur Mesure 15")
                .price(7100.0)
                .margin(0.1)
                .weight(2.0)
                .transactions(new HashSet<>())
                .build();
    }

    @Test
    public void shouldFindAllTransactions() {
        //given
        TransactionDTO transaction1 = generateFirstTransaction();
        TransactionDTO transaction2 = generateSecondTransaction();
        TransactionDTO savedTransaction1 = transactionService.addTransaction(transaction1);
        TransactionDTO savedTransaction2 = transactionService.addTransaction(transaction2);
        List<TransactionDTO> transactionList = new ArrayList<>();
        transactionList.add(savedTransaction1);
        transactionList.add(savedTransaction2);

        //when
        List<TransactionDTO> list = transactionService.findAllTransactions();

        //then
        assertThat(transactionList).isEqualTo(list);
    }

    @Test
    public void shouldFindTransactionById() {
        //given
        TransactionDTO transaction1 = generateFirstTransaction();
        TransactionDTO savedTransaction1 = transactionService.addTransaction(transaction1);

        //when
        TransactionDTO transactionDTO = transactionService.findTransactionById(savedTransaction1.getId());

        //then
        assertThat(transactionDTO).isEqualTo(savedTransaction1);
    }

    @Test
    public void shouldAddTransaction() {
        //given
        TransactionDTO transaction1 = generateFirstTransaction();

        //when
        TransactionDTO savedTransaction = transactionService.addTransaction(transaction1);
        transaction1.setId(savedTransaction.getId());
        transaction1.setCreateDate(savedTransaction.getCreateDate());
        transaction1.setUpdateDate(savedTransaction.getUpdateDate());
        transaction1.setVersion(savedTransaction.getVersion());

        //then
        assertThat(savedTransaction).isEqualTo(transaction1);
    }

    @Test
    public void shouldUpdateTransaction() {
        //given
        TransactionDTO transaction1 = generateFirstTransaction();
        TransactionDTO savedTransaction = transactionService.addTransaction(transaction1);
        TransactionDTO transaction2 = generateSecondTransaction();
        transaction2.setId(savedTransaction.getId());

        //when
        TransactionDTO updatedTransaction = transactionService.updateTransaction(transaction2);
        TransactionDTO transactionDTO = transactionService.findTransactionById(updatedTransaction.getId());

        //then
        assertThat(updatedTransaction).isEqualTo(transactionDTO);
    }

    @Test
    public void shouldDeleteTransaction() {
        //given
        TransactionDTO transaction1 = generateFirstTransaction();
        TransactionDTO savedTransaction = transactionService.addTransaction(transaction1);

        //when
        transactionService.deleteTransaction(savedTransaction.getId());

        //then
        assertThat(transactionService.findAllTransactions()).isEmpty();
    }

    @Test
    public void shouldIsEmptyWhenCallFindAllFromEmptyDatabase() {
        //when
        List<TransactionDTO> transactionList = transactionService.findAllTransactions();

        //then
        assertThat(transactionList).isEmpty();
    }

    @Test
    public void shouldFindTransactionsByCriteria() {
        //given
        TransactionDTO transaction1 = generateFirstTransaction();
        TransactionDTO transaction2 = generateSecondTransaction();
        TransactionDTO savedTransaction1 = transactionService.addTransaction(transaction1);
        TransactionDTO savedTransaction2 = transactionService.addTransaction(transaction2);

        List<TransactionDTO> transactionList = new ArrayList<>();
        transactionList.add(savedTransaction1);
        Double totalPrice = savedTransaction1.getNumberProducts()
                * productService.findProductById(savedTransaction1.getProduct()).getPrice();

        criteria.setIdProduct(savedTransaction1.getProduct());
        criteria.setStartDate(savedTransaction1.getDate());
        criteria.setEndDate(savedTransaction1.getDate());
        criteria.setStatus(savedTransaction1.getStatus());
        criteria.setTotalPrice(totalPrice);

        //when
        List<TransactionDTO> transactionDTOList = transactionService.findTransactionsByCriteria(criteria);

        //then
        assertThat(transactionDTOList).isEqualTo(transactionList);
    }

    @Test
    public void shouldOnlyUpdateTransactionAndNotAddToDatabase(){
        //given
        TransactionDTO transaction1 = generateFirstTransaction();
        TransactionDTO savedTransaction1 = transactionService.addTransaction(transaction1);
        TransactionDTO transaction2 = generateSecondTransaction();
        transaction2.setId(savedTransaction1.getId());

        //when
        transactionService.updateTransaction(transaction2);

        //then
        assertThat(transactionService.findAllTransactions().size()).isEqualTo(1);
    }

    @Test(expected = ArgumentIsNullException.class)
    public void shouldThrowArgumentIsNullExceptionWhenCallAdd(){
        //when
        transactionService.addTransaction(null);
    }

    @Test(expected = ArgumentIsNullException.class)
    public void shouldThrowArgumentIsNullExceptionWhenCallUpdate(){
        //when
        transactionService.updateTransaction(null);
    }

    @Test(expected = ArgumentIsNullException.class)
    public void shouldThrowArgumentIsNullExceptionWhenCallDelete(){
        //when
        transactionService.deleteTransaction(null);
    }

    @Test(expected = ArgumentIsNullException.class)
    public void shouldThrowArgumentIsNullExceptionWhenCallFindById(){
        //when
        transactionService.findTransactionById(null);
    }

    @Test(expected = TransactionCantBeExecutedException.class)
    public void shouldThrowExceptionWhenTransactionHasMoreThan25kg(){
        //given
        TransactionDTO transaction1 = generateThirdTransaction();

        //when
        TransactionDTO savedTransaction1 = transactionService.addTransaction(transaction1);
    }

    @Test(expected = TransactionCantBeExecutedException.class)
    public void shouldThrowExceptionWhenHasMoreThan5TheSameProductsWhoseUnitPriceIsOver7000(){
        //given
        TransactionDTO transaction1 = generateFourthTransaction();

        //when
        TransactionDTO savedTransaction1 =transactionService.addTransaction(transaction1);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void shouldThrowExceptionWhenTryDeleteFromEmptyDatabase(){
        //when
        transactionService.deleteTransaction(1L);
    }

    @Test(expected = ArgumentIsNullException.class)
    public void shouldThrowExceptionWhenCallFindTransactionsByCriteria(){
        //when
        transactionService.findTransactionsByCriteria(null);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void shouldntAddTransactionWithoutProductAndWithoutClient(){
        //given
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .status(Status.canceled)
                .build();

        //when
        TransactionDTO savedTransaction = transactionService.addTransaction(transactionDTO);
    }

    @Test(expected = OptimisticLockingFailureException.class)
    public void shouldThrowOptimisticLockException(){
        //given
        TransactionDTO transaction1 = generateFirstTransaction();
        TransactionDTO savedTransaction = transactionService.addTransaction(transaction1);

        Long transactionId = savedTransaction.getId();
        TransactionDTO transactionDTO1 = transactionService.findTransactionById(transactionId);
        transactionDTO1.setNumberProducts(5);
        TransactionDTO transactionDTO2 = transactionService.findTransactionById(transactionId);

        //when
        transactionRepo.save(transactionMapper.mapToEntity(transactionDTO1));
        entityManager.flush();
        transactionRepo.save(transactionMapper.mapToEntity(transactionDTO2));
    }

    @Test
    public void shouldUpdateDateIsNotEqualCreateDate() throws InterruptedException {
        //given
        TransactionDTO transaction1 = generateFirstTransaction();
        TransactionDTO savedTransaction1 = transactionService.addTransaction(transaction1);

        savedTransaction1.setStatus(Status.in_delivery);
        LocalDateTime createDate = savedTransaction1.getCreateDate();
        TimeUnit.SECONDS.sleep(1);

        //when
        transactionService.updateTransaction(savedTransaction1);
        entityManager.flush();
        TransactionDTO transactionDTO = transactionService.findTransactionById(savedTransaction1.getId());
        LocalDateTime updateDate = transactionDTO.getUpdateDate();

        //then
        assertThat(updateDate).isNotEqualTo(createDate);
    }

    @Test
    public void shouldCalculateProfitInPeriod(){
        //given
        TransactionDTO transaction1 = generateFirstTransaction();
        TransactionDTO savedTransaction = transactionService.addTransaction(transaction1);
        ProductDTO productDTO = productService.findProductById(savedTransaction.getProduct());

        Double totalProfit = savedTransaction.getNumberProducts() * productDTO.getPrice() * productDTO.getMargin();

        //when
        Double profit = transactionService.calculateProfitInPeriod(savedTransaction.getDate(), savedTransaction.getDate());

        //then
        assertThat(profit).isEqualTo(totalProfit);
    }
}
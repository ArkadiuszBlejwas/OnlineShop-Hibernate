package com.capgemini.store.persistence.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import com.capgemini.store.StarterkitApplication;
import com.capgemini.store.persistence.dto.ClientDTO;
import com.capgemini.store.persistence.dto.ProductDTO;
import com.capgemini.store.persistence.dto.TransactionDTO;
import com.capgemini.store.persistence.entity.Address;
import com.capgemini.store.persistence.entity.Status;
import com.capgemini.store.persistence.mapper.ClientMapper;
import com.capgemini.store.persistence.repo.ClientRepo;
import com.capgemini.store.persistence.service.ClientService;
import com.capgemini.store.persistence.service.ProductService;
import com.capgemini.store.persistence.service.TransactionService;
import com.capgemini.store.persistence.service.exception.ArgumentIsNullException;
import com.capgemini.store.persistence.service.exception.TransactionCantBeExecutedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Transactional
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(classes = {StarterkitApplication.class}, properties = "spring.profiles.active=hsql")

public class ClientServiceImplTest {

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ProductService productService;

    @Autowired
    ClientMapper clientMapper;

    @PersistenceContext
    private EntityManager entityManager;

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

    private TransactionDTO generateFirstTransaction(){
        ClientDTO client1 = generateFirstClient();
        ClientDTO savedClient1 = clientService.addClient(client1);
        ProductDTO product1 = generateFistProduct();
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

    private TransactionDTO generateThirdTransaction(){
        ClientDTO client2 = generateSecondClient();
        ClientDTO savedClient2 = clientService.addClient(client2);
        ProductDTO product3 = generateThirdProduct();
        ProductDTO savedProduct3 = productService.addProduct(product3);
        return TransactionDTO.builder()
                .client(savedClient2.getId())
                .product(savedProduct3.getId())
                .numberProducts(2)
                .status(Status.executed)
                .date(LocalDateTime.of(2019, Month.MARCH, 3, 15, 30))
                .build();
    }

    private TransactionDTO generateFourthTransaction(){
        ClientDTO client2 = generateSecondClient();
        ClientDTO savedClient2 = clientService.addClient(client2);
        ProductDTO product3 = generateThirdProduct();
        ProductDTO savedProduct3 = productService.addProduct(product3);
        return TransactionDTO.builder()
                .client(savedClient2.getId())
                .product(savedProduct3.getId())
                .numberProducts(2)
                .status(Status.executed)
                .date(LocalDateTime.of(2019, Month.MARCH, 3, 15, 30))
                .build();
    }

    private TransactionDTO generateFifthTransaction(){
        ClientDTO client2 = generateSecondClient();
        ClientDTO savedClient2 = clientService.addClient(client2);
        ProductDTO product3 = generateThirdProduct();
        ProductDTO savedProduct3 = productService.addProduct(product3);
        return TransactionDTO.builder()
                .client(savedClient2.getId())
                .product(savedProduct3.getId())
                .numberProducts(2)
                .status(Status.executed)
                .date(LocalDateTime.of(2019, Month.MARCH, 3, 15, 30))
                .build();
    }

    private ProductDTO generateFistProduct(){
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
                .name("MacBook Air 18")
                .price(5100.0)
                .margin(0.1)
                .weight(0.5)
                .transactions(new HashSet<>())
                .build();
    }

    @Test
    public void shouldFindAllClients() {
        //given
        ClientDTO client1 = generateFirstClient();
        ClientDTO client2 = generateSecondClient();
        ClientDTO savedClient1 = clientService.addClient(client1);
        ClientDTO savedClient2 = clientService.addClient(client2);
        List<ClientDTO> clientList = new ArrayList<>();
        clientList.add(savedClient1);
        clientList.add(savedClient2);

        //when
        List<ClientDTO> list = clientService.findAllClients();

        //then
        assertThat(clientList).isEqualTo(list);
    }

    @Test
    public void shouldFindClientById() {
        //given
        ClientDTO client1 = generateFirstClient();
        ClientDTO savedClient1 = clientService.addClient(client1);

        //when
        ClientDTO clientDTO = clientService.findClientById(savedClient1.getId());

        //then
        assertThat(clientDTO).isEqualTo(savedClient1);
    }

    @Test
    public void shouldAddClient() {
        //given
        ClientDTO client1 = generateFirstClient();

        //when
        ClientDTO savedClient1 = clientService.addClient(client1);
        client1.setId(savedClient1.getId());
        client1.setVersion(savedClient1.getVersion());
        client1.setCreateDate(savedClient1.getCreateDate());
        client1.setUpdateDate(savedClient1.getUpdateDate());

        //then
        assertThat(savedClient1).isEqualTo(client1);
    }

    @Test
    public void shouldUpdateClient() {
        //given
        ClientDTO client1 = generateFirstClient();
        ClientDTO savedClient1 = clientService.addClient(client1);
        ClientDTO client2 = generateSecondClient();
        client2.setId(savedClient1.getId());

        //when
        ClientDTO updatedClient = clientService.updateClient(client2);
        ClientDTO clientDTO = clientService.findClientById(updatedClient.getId());

        //then
        assertThat(updatedClient).isEqualTo(clientDTO);
    }

    @Test
    public void shouldDeleteClient() {
        //given
        ClientDTO client1 = generateFirstClient();
        ClientDTO savedClient1 = clientService.addClient(client1);

        //when
        clientService.deleteClient(savedClient1.getId());

        //then
        assertThat(clientService.findAllClients()).isEmpty();
    }

    @Test
    public void shouldIsEmptyWhenCallFindAllFromEmptyDatabase() {
        //when
        List<ClientDTO> clientList = clientService.findAllClients();

        //then
        assertThat(clientList).isEmpty();
    }

    @Test
    public void shouldUpdateDateisNotNull() throws InterruptedException {
        //given
        ClientDTO client1 = generateFirstClient();
        ClientDTO savedClient1 = clientService.addClient(client1);
        ClientDTO client2 = generateSecondClient();
        client2.setId(savedClient1.getId());
        LocalDateTime createDate = savedClient1.getCreateDate();
        TimeUnit.SECONDS.sleep(1);

        //when
        ClientDTO updatedClient = clientService.updateClient(client2);
        entityManager.flush();
        LocalDateTime updateDate = updatedClient.getUpdateDate();

        //then
        assertThat(updateDate).isNotNull();
    }

    @Test
    public void shouldUpdateDateIsNotEqualCreateDate() throws InterruptedException {
        //given
        ClientDTO client1 = generateFirstClient();
        ClientDTO savedClient1 = clientService.addClient(client1);
        savedClient1.setFirstName("Natalia");
        LocalDateTime createDate = savedClient1.getCreateDate();
        TimeUnit.SECONDS.sleep(1);

        //when
        clientRepo.save(clientMapper.mapToEntity(savedClient1));
        entityManager.flush();
        ClientDTO clientDTO = clientService.findClientById(savedClient1.getId());
        LocalDateTime updateDate = clientDTO.getUpdateDate();

        //then
        assertThat(updateDate).isNotEqualTo(createDate);
    }

    @Test
    public void shouldCreateDateIsNotNull(){
        //given
        ClientDTO client1 = generateFirstClient();
        ClientDTO savedClient1 = clientService.addClient(client1);

        //when
        LocalDateTime createDate = savedClient1.getCreateDate();

        //then
        assertThat(createDate).isNotNull();
    }

    @Test(expected = OptimisticLockingFailureException.class)
    public void shouldThrowOptimisticLockException(){
        //given
        ClientDTO client1 = generateFirstClient();
        ClientDTO savedClient = clientService.addClient(client1);
        Long clientID1 = savedClient.getId();
        ClientDTO clientDTO1 = clientService.findClientById(clientID1);
        clientDTO1.setFirstName("XDXD");
        ClientDTO clientDTO2 = clientService.findClientById(clientID1);

        //when
        clientRepo.save(clientMapper.mapToEntity(clientDTO1));
        entityManager.flush();
        clientRepo.save(clientMapper.mapToEntity(clientDTO2));
    }

    @Test
    public void shouldCalculateTotalCostOfTransactionsForSpecifiedClient() {
        //given
        TransactionDTO transaction1 = generateFirstTransaction();
        TransactionDTO savedTransaction = transactionService.addTransaction(transaction1);

        ClientDTO client1 = clientService.findClientById(savedTransaction.getClient());
        Set<Long> trans = new HashSet<>();
        trans.add(savedTransaction.getId());
        client1.setTransactions(trans);
        ClientDTO updatedClient = clientService.updateClient(client1);

        ProductDTO product1 = productService.findProductById(savedTransaction.getProduct());
        product1.setTransactions(trans);
        ProductDTO updatedProduct = productService.updateProduct(product1);
        Double totalPrice = updatedProduct.getPrice() * savedTransaction.getNumberProducts();

        //when
        Double total = clientService.calculateTotalCostOfTransactionsForSpecifiedClient(updatedClient.getId());

        //then
        assertThat(total).isEqualTo(totalPrice);
    }

    @Test
    public void shouldCalculateTotalCostOfTransactionsWithSpecifiedStatusForSpecifiedClient() {
        //given
        TransactionDTO transaction1 = generateFirstTransaction();
        TransactionDTO savedTransaction = transactionService.addTransaction(transaction1);

        ClientDTO client1 = clientService.findClientById(savedTransaction.getClient());
        Set<Long> trans = new HashSet<>();
        trans.add(savedTransaction.getId());
        client1.setTransactions(trans);
        ClientDTO updatedClient = clientService.updateClient(client1);

        ProductDTO product1 = productService.findProductById(savedTransaction.getProduct());
        product1.setTransactions(trans);
        ProductDTO updatedProduct = productService.updateProduct(product1);
        Double totalPrice = updatedProduct.getPrice() * savedTransaction.getNumberProducts();

        //when
        Double total = clientService.calculateTotalCostOfTransactionsWithSpecifiedStatusForSpecifiedClient(updatedClient.getId(), Status.canceled);

        //then
        assertThat(total).isEqualTo(totalPrice);
    }

    @Test
    public void shouldFindClientsWhoSpendTheMostMoneyInSpecifiedPeriod() {
        //given
        TransactionDTO transaction1 = generateFirstTransaction();
        TransactionDTO savedTransaction = transactionService.addTransaction(transaction1);

        ClientDTO client1 = clientService.findClientById(savedTransaction.getClient());
        Set<Long> trans = new HashSet<>();
        trans.add(savedTransaction.getId());
        client1.setTransactions(trans);
        ClientDTO updatedClient = clientService.updateClient(client1);

        ProductDTO product1 = productService.findProductById(savedTransaction.getProduct());
        product1.setTransactions(trans);
        ProductDTO updatedProduct = productService.updateProduct(product1);
        LocalDateTime date = savedTransaction.getDate();

        List<ClientDTO> list = new ArrayList<>();
        list.add(clientService.findClientById(updatedClient.getId()));

        //when
        List<ClientDTO> clientList = clientService.findClientsWhoSpendTheMostMoneyInSpecifiedPeriod(date, date);

        //then
        assertThat(clientList).isEqualTo(list);
    }

    @Test
    public void shouldOnlyUpdateClientAndNotAddToDatabase(){
        //given
        ClientDTO client1 = generateFirstClient();
        ClientDTO savedClient1 = clientService.addClient(client1);
        ClientDTO client2 = generateSecondClient();
        client2.setId(savedClient1.getId());

        //when
        clientService.updateClient(client2);

        //then
        assertThat(clientService.findAllClients().size()).isEqualTo(1);
    }

    @Test(expected = ArgumentIsNullException.class)
    public void shouldThrowArgumentIsNullExceptionWhenCallAdd(){
        //when
        clientService.addClient(null);
    }

    @Test(expected = ArgumentIsNullException.class)
    public void shouldThrowArgumentIsNullExceptionWhenCallUpdate(){
        //when
        clientService.updateClient(null);
    }

    @Test(expected = ArgumentIsNullException.class)
    public void shouldThrowArgumentIsNullExceptionWhenCallDelete(){
        //when
        clientService.deleteClient(null);
    }

    @Test(expected = ArgumentIsNullException.class)
    public void shouldThrowArgumentIsNullExceptionWhenCallFindById(){
        //when
        clientService.findClientById(null);
    }

    @Test(expected = TransactionCantBeExecutedException.class)
    public void shouldThrowExceptionWhenClientHasLessThan3TransactionAndTryDoTransactionWithTotalPriceMoreThan5000(){
        //given
        TransactionDTO transaction1 = generateSecondTransaction();
        TransactionDTO savedTransaction1 = transactionService.addTransaction(transaction1);

        ClientDTO clientDTO = clientService.findClientById(savedTransaction1.getClient());
        ProductDTO productDTO = productService.findProductById(savedTransaction1.getProduct());

        //when
        clientService.performTransaction(clientDTO.getId(), savedTransaction1, productDTO.getId());
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void shouldThrowExceptionWhenTryDeleteFromEmptyDatabase(){
        //when
        clientService.deleteClient(1L);
    }

    @Test
    public void shouldDoCascadeRemove(){
        //given
        TransactionDTO transaction1 = generateFirstTransaction();
        TransactionDTO savedTransaction1 = transactionService.addTransaction(transaction1);

        ClientDTO client1 = clientService.findClientById(savedTransaction1.getClient());
        Set<Long> transactions = new HashSet<>();
        transactions.add(savedTransaction1.getId());
        client1.setTransactions(transactions);
        ClientDTO updatedClient1 = clientService.updateClient(client1);

        //when
        clientService.deleteClient(client1.getId());

        //then
        assertThat(transactionService.findAllTransactions()).isEmpty();
    }

    @Test
    public void shouldGetRepo(){
        //when
        ClientRepo clientRepo = clientService.getClientRepo();

        //then
        assertThat(clientRepo).isNotNull();
    }

    @Test(expected = ArgumentIsNullException.class)
    public void shouldThrowExceptionWhenCallCalculateTotalCostForClient(){
        //when
        clientService.calculateTotalCostOfTransactionsForSpecifiedClient(null);
    }

    @Test(expected = ArgumentIsNullException.class)
    public void shouldThrowExceptionWhenCallCalculateTotalCostForClientWithStatus(){
        //when
        clientService.calculateTotalCostOfTransactionsWithSpecifiedStatusForSpecifiedClient(null, null);
    }

    @Test(expected = ArgumentIsNullException.class)
    public void shouldThrowExceptionWhenCallFindClientsWhoSpendTheMostMoney(){
        //when
        clientService.findClientsWhoSpendTheMostMoneyInSpecifiedPeriod(null, null);
    }

    @Test
    public void shouldPerformTransaction(){
        //given
        TransactionDTO transaction1 = generateThirdTransaction();
        TransactionDTO transaction2 = generateFourthTransaction();
        TransactionDTO transaction3 = generateFifthTransaction();

        TransactionDTO savedTransaction1 = transactionService.addTransaction(transaction1);
        TransactionDTO savedTransaction2 = transactionService.addTransaction(transaction2);
        TransactionDTO savedTransaction3 = transactionService.addTransaction(transaction3);

        ClientDTO client1 = clientService.findClientById(savedTransaction1.getClient());
        Set<Long> transactions = new HashSet<>();
        transactions.add(savedTransaction1.getId());
        transactions.add(savedTransaction2.getId());
        transactions.add(savedTransaction3.getId());
        client1.setTransactions(transactions);
        ClientDTO updatedClient1 = clientService.updateClient(client1);

        ProductDTO productDTO = generateThirdProduct();
        ProductDTO savedProduct1 = productService.addProduct(productDTO);

        TransactionDTO newTransaction = generateFirstTransaction();

        //when
        ClientDTO clientWithNewTransaction = clientService.performTransaction(client1.getId(), newTransaction, savedProduct1.getId());

        //then
        assertThat(clientWithNewTransaction.getTransactions().size()).isEqualTo(4);
    }
}
package com.capgemini.store.persistence.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import com.capgemini.store.StarterkitApplication;
import com.capgemini.store.persistence.dto.ClientDTO;
import com.capgemini.store.persistence.dto.ProductDTO;
import com.capgemini.store.persistence.dto.ProductInRealizaton;
import com.capgemini.store.persistence.dto.TransactionDTO;
import com.capgemini.store.persistence.entity.Address;
import com.capgemini.store.persistence.entity.Status;
import com.capgemini.store.persistence.mapper.ProductMapper;
import com.capgemini.store.persistence.repo.ProductRepo;
import com.capgemini.store.persistence.service.ClientService;
import com.capgemini.store.persistence.service.ProductService;
import com.capgemini.store.persistence.service.TransactionService;
import com.capgemini.store.persistence.service.exception.ArgumentIsNullException;
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
import java.util.*;
import java.util.concurrent.TimeUnit;


@Transactional
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(classes = {StarterkitApplication.class}, properties = "spring.profiles.active=hsql")

public class ProductServiceImplTest {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductService productService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;

    @PersistenceContext
    private EntityManager entityManager;

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
                .name("Byłka")
                .price(50.0)
                .margin(0.1)
                .weight(0.5)
                .transactions(new HashSet<>())
                .build();
    }

    private ProductDTO generateFourthProduct(){
        return ProductDTO.builder()
                .name("Masło")
                .price(50.0)
                .margin(0.1)
                .weight(0.5)
                .transactions(new HashSet<>())
                .build();
    }

    private ProductDTO generateFifthProduct(){
        return ProductDTO.builder()
                .name("Monitor")
                .price(50.0)
                .margin(0.1)
                .weight(0.5)
                .transactions(new HashSet<>())
                .build();
    }

    private ProductDTO generateSixthProduct(){
        return ProductDTO.builder()
                .name("lizak")
                .price(50.0)
                .margin(0.1)
                .weight(0.5)
                .transactions(new HashSet<>())
                .build();
    }

    private ProductDTO generateSeventhProduct(){
        return ProductDTO.builder()
                .name("Biórko")
                .price(50.0)
                .margin(0.1)
                .weight(0.5)
                .transactions(new HashSet<>())
                .build();
    }

    private ProductDTO generateEighthProduct(){
        return ProductDTO.builder()
                .name("Tablica")
                .price(50.0)
                .margin(0.1)
                .weight(0.5)
                .transactions(new HashSet<>())
                .build();
    }

    private ProductDTO generateNinthProduct(){
        return ProductDTO.builder()
                .name("Długopis")
                .price(50.0)
                .margin(0.1)
                .weight(0.5)
                .transactions(new HashSet<>())
                .build();
    }

    private ProductDTO generateTenthProduct(){
        return ProductDTO.builder()
                .name("Ziemniak")
                .price(50.0)
                .margin(0.1)
                .weight(0.5)
                .transactions(new HashSet<>())
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
        ClientDTO client1 = generateFirstClient();
        ClientDTO savedClient1 = clientService.addClient(client1);
        ProductDTO product1 = generateSecondProduct();
        ProductDTO savedProduct1 = productService.addProduct(product1);
        return TransactionDTO.builder()
                .client(savedClient1.getId())
                .product(savedProduct1.getId())
                .numberProducts(5)
                .status(Status.canceled)
                .date(LocalDateTime.of(2019, Month.FEBRUARY, 2, 18, 20))
                .build();
    }

    private TransactionDTO generateThirdTransaction(){
        ClientDTO client1 = generateFirstClient();
        ClientDTO savedClient1 = clientService.addClient(client1);
        ProductDTO product1 = generateThirdProduct();
        ProductDTO savedProduct1 = productService.addProduct(product1);
        return TransactionDTO.builder()
                .client(savedClient1.getId())
                .product(savedProduct1.getId())
                .numberProducts(5)
                .status(Status.in_realization)
                .date(LocalDateTime.of(2019, Month.FEBRUARY, 2, 18, 20))
                .build();
    }

    private TransactionDTO generateFourthTransaction(){
        ClientDTO client1 = generateFirstClient();
        ClientDTO savedClient1 = clientService.addClient(client1);
        ProductDTO product1 = generateFourthProduct();
        ProductDTO savedProduct1 = productService.addProduct(product1);
        return TransactionDTO.builder()
                .client(savedClient1.getId())
                .product(savedProduct1.getId())
                .numberProducts(5)
                .status(Status.in_realization)
                .date(LocalDateTime.of(2019, Month.FEBRUARY, 2, 18, 20))
                .build();
    }

    private TransactionDTO generateFifthTransaction(){
        ClientDTO client1 = generateFirstClient();
        ClientDTO savedClient1 = clientService.addClient(client1);
        ProductDTO product1 = generateFifthProduct();
        ProductDTO savedProduct1 = productService.addProduct(product1);
        return TransactionDTO.builder()
                .client(savedClient1.getId())
                .product(savedProduct1.getId())
                .numberProducts(5)
                .status(Status.in_realization)
                .date(LocalDateTime.of(2019, Month.FEBRUARY, 2, 18, 20))
                .build();
    }

    private TransactionDTO generateSixthTransaction(){
        ClientDTO client1 = generateFirstClient();
        ClientDTO savedClient1 = clientService.addClient(client1);
        ProductDTO product1 = generateSixthProduct();
        ProductDTO savedProduct1 = productService.addProduct(product1);
        return TransactionDTO.builder()
                .client(savedClient1.getId())
                .product(savedProduct1.getId())
                .numberProducts(5)
                .status(Status.in_realization)
                .date(LocalDateTime.of(2019, Month.FEBRUARY, 2, 18, 20))
                .build();
    }

    private TransactionDTO generateSeventhTransaction(){
        ClientDTO client1 = generateFirstClient();
        ClientDTO savedClient1 = clientService.addClient(client1);
        ProductDTO product1 = generateSeventhProduct();
        ProductDTO savedProduct1 = productService.addProduct(product1);
        return TransactionDTO.builder()
                .client(savedClient1.getId())
                .product(savedProduct1.getId())
                .numberProducts(5)
                .status(Status.in_realization)
                .date(LocalDateTime.of(2019, Month.FEBRUARY, 2, 18, 20))
                .build();
    }

    private TransactionDTO generateEighthTransaction(){
        ClientDTO client1 = generateFirstClient();
        ClientDTO savedClient1 = clientService.addClient(client1);
        ProductDTO product1 = generateEighthProduct();
        ProductDTO savedProduct1 = productService.addProduct(product1);
        return TransactionDTO.builder()
                .client(savedClient1.getId())
                .product(savedProduct1.getId())
                .numberProducts(5)
                .status(Status.canceled)
                .date(LocalDateTime.of(2019, Month.FEBRUARY, 2, 18, 20))
                .build();
    }

    private TransactionDTO generateNinthTransaction(){
        ClientDTO client1 = generateFirstClient();
        ClientDTO savedClient1 = clientService.addClient(client1);
        ProductDTO product1 = generateNinthProduct();
        ProductDTO savedProduct1 = productService.addProduct(product1);
        return TransactionDTO.builder()
                .client(savedClient1.getId())
                .product(savedProduct1.getId())
                .numberProducts(5)
                .status(Status.canceled)
                .date(LocalDateTime.of(2019, Month.FEBRUARY, 2, 18, 20))
                .build();
    }

    private TransactionDTO generateTenthTransaction(){
        ClientDTO client1 = generateFirstClient();
        ClientDTO savedClient1 = clientService.addClient(client1);
        ProductDTO product1 = generateTenthProduct();
        ProductDTO savedProduct1 = productService.addProduct(product1);
        return TransactionDTO.builder()
                .client(savedClient1.getId())
                .product(savedProduct1.getId())
                .numberProducts(5)
                .status(Status.canceled)
                .date(LocalDateTime.of(2019, Month.FEBRUARY, 2, 18, 20))
                .build();
    }

    @Test
    public void shouldFindAllProducts() {
        //given
        ProductDTO product1 = generateFirstProduct();
        ProductDTO product2 = generateFirstProduct();
        ProductDTO savedProduct1 = productService.addProduct(product1);
        ProductDTO savedProduct2 = productService.addProduct(product2);
        List<ProductDTO> productList = new ArrayList<>();
        productList.add(savedProduct1);
        productList.add(savedProduct2);

        //when
        List<ProductDTO> list = productService.findAllProducts();

        //then
        assertThat(list).isEqualTo(productList);
    }

    @Test
    public void shouldFindProductById() {
        //given
        ProductDTO product1 = generateFirstProduct();
        ProductDTO savedProduct1 = productService.addProduct(product1);

        //when
        ProductDTO product = productService.findProductById(savedProduct1.getId());

        //then
        assertThat(product).isEqualTo(savedProduct1);
    }

    @Test
    public void shouldAddProduct() {
        //given
        ProductDTO product1 = generateFirstProduct();

        //when
        ProductDTO savedProduct1 = productService.addProduct(product1);
        product1.setId(savedProduct1.getId());
        product1.setCreateDate(savedProduct1.getCreateDate());
        product1.setUpdateDate(savedProduct1.getUpdateDate());
        product1.setVersion(savedProduct1.getVersion());

        //then
        assertThat(savedProduct1).isEqualTo(product1);
    }

    @Test
    public void shouldUpdateProduct() {
        //given
        ProductDTO product1 = generateFirstProduct();
        ProductDTO savedProduct1 = productService.addProduct(product1);
        ProductDTO product2 = generateSecondProduct();
        product2.setId(savedProduct1.getId());

        //when
        ProductDTO updatedProduct1 = productService.updateProduct(product2);
        ProductDTO productDTO = productService.findProductById(updatedProduct1.getId());

        //then
        assertThat(updatedProduct1).isEqualTo(productDTO);
    }

    @Test
    public void shouldDeleteProduct() {
        //given
        ProductDTO product1 = generateFirstProduct();
        ProductDTO savedProduct1 = productService.addProduct(product1);

        //when
        productService.deleteProduct(savedProduct1.getId());

        //then
        assertThat(productService.findAllProducts()).isEmpty();
    }

    @Test
    public void shouldIsEmptyWhenCallFindAllFromEmptyDatabase() {
        //when
        List<ProductDTO> list = productService.findAllProducts();

        //then
        assertThat(list).isEmpty();
    }

    @Test
    public void shouldUpdateDate(){
        //given
        ProductDTO product1 = generateFirstProduct();
        ProductDTO savedProduct1 = productService.addProduct(product1);

        //when
        LocalDateTime updateDate = savedProduct1.getUpdateDate();

        //then
        assertThat(updateDate).isNotNull();
    }

    @Test
    public void shouldCreateDate(){
        //given
        ProductDTO product1 = generateFirstProduct();
        ProductDTO savedProduct1 = productService.addProduct(product1);

        //when
        LocalDateTime createDate = savedProduct1.getCreateDate();

        //then
        assertThat(createDate).isNotNull();

    }

    @Test
    public void shouldFind10BestSellingProducts() {
        //given
        TransactionDTO t1 = generateFirstTransaction();
        TransactionDTO t2 = generateSecondTransaction();
        TransactionDTO t3 = generateThirdTransaction();
        TransactionDTO t4 = generateFourthTransaction();
        TransactionDTO t5 = generateFifthTransaction();
        TransactionDTO t6 = generateSixthTransaction();
        TransactionDTO t7 = generateSeventhTransaction();
        TransactionDTO t8 = generateEighthTransaction();
        TransactionDTO t9 = generateNinthTransaction();
        TransactionDTO t10 = generateTenthTransaction();

        TransactionDTO st1 = transactionService.addTransaction(t1);
        TransactionDTO st2 = transactionService.addTransaction(t2);
        TransactionDTO st3 = transactionService.addTransaction(t3);
        TransactionDTO st4 = transactionService.addTransaction(t4);
        TransactionDTO st5 = transactionService.addTransaction(t5);
        TransactionDTO st6 = transactionService.addTransaction(t6);
        TransactionDTO st7 = transactionService.addTransaction(t7);
        TransactionDTO st8 = transactionService.addTransaction(t8);
        TransactionDTO st9 = transactionService.addTransaction(t9);
        TransactionDTO st10 = transactionService.addTransaction(t10);

        ProductDTO p1 = productService.findProductById(st1.getProduct());
        ProductDTO p2 = productService.findProductById(st2.getProduct());
        ProductDTO p3 = productService.findProductById(st3.getProduct());
        ProductDTO p4 = productService.findProductById(st4.getProduct());
        ProductDTO p5 = productService.findProductById(st5.getProduct());
        ProductDTO p6 = productService.findProductById(st6.getProduct());
        ProductDTO p7 = productService.findProductById(st7.getProduct());
        ProductDTO p8 = productService.findProductById(st8.getProduct());
        ProductDTO p9 = productService.findProductById(st9.getProduct());
        ProductDTO p10 = productService.findProductById(st10.getProduct());

        List<ProductDTO> pList = new ArrayList<>();
        pList.add(p10);
        pList.add(p9);
        pList.add(p8);
        pList.add(p7);
        pList.add(p6);
        pList.add(p5);
        pList.add(p4);
        pList.add(p3);
        pList.add(p2);
        pList.add(p1);

        //when
        List<ProductDTO> list = productService.find10BestSellingProducts();

        //then
        assertThat(list).isEqualTo(pList);
    }

    @Test
    public void shouldFindProductsWhichAreInRealization() {
        //given
        TransactionDTO t1 = generateFirstTransaction();
        TransactionDTO t2 = generateSecondTransaction();
        TransactionDTO t3 = generateThirdTransaction();
        TransactionDTO t4 = generateFourthTransaction();
        TransactionDTO t5 = generateFifthTransaction();
        TransactionDTO t6 = generateSixthTransaction();
        TransactionDTO t7 = generateSeventhTransaction();
        TransactionDTO t8 = generateEighthTransaction();
        TransactionDTO t9 = generateNinthTransaction();
        TransactionDTO t10 = generateTenthTransaction();

        TransactionDTO st1 = transactionService.addTransaction(t1);
        TransactionDTO st2 = transactionService.addTransaction(t2);
        TransactionDTO st3 = transactionService.addTransaction(t3);
        TransactionDTO st4 = transactionService.addTransaction(t4);
        TransactionDTO st5 = transactionService.addTransaction(t5);
        TransactionDTO st6 = transactionService.addTransaction(t6);
        TransactionDTO st7 = transactionService.addTransaction(t7);
        TransactionDTO st8 = transactionService.addTransaction(t8);
        TransactionDTO st9 = transactionService.addTransaction(t9);
        TransactionDTO st10 = transactionService.addTransaction(t10);

        ProductDTO p1 = productService.findProductById(st1.getProduct());
        ProductDTO p2 = productService.findProductById(st2.getProduct());
        ProductDTO p3 = productService.findProductById(st3.getProduct());
        ProductDTO p4 = productService.findProductById(st4.getProduct());
        ProductDTO p5 = productService.findProductById(st5.getProduct());
        ProductDTO p6 = productService.findProductById(st6.getProduct());
        ProductDTO p7 = productService.findProductById(st7.getProduct());
        ProductDTO p8 = productService.findProductById(st8.getProduct());
        ProductDTO p9 = productService.findProductById(st9.getProduct());
        ProductDTO p10 = productService.findProductById(st10.getProduct());

        List<ProductDTO> pList = new ArrayList<>();
        pList.add(p3);
        pList.add(p4);
        pList.add(p5);
        pList.add(p6);
        pList.add(p7);

        //when
        List<ProductDTO> productList = productService.findProductsWhichAreInRealization();

        assertThat(productList).isEqualTo(pList);
    }

    @Test
    public void shouldOnlyUpdateProductAndNotAddToDatabase(){
        //given
        ProductDTO product1 = generateFirstProduct();
        ProductDTO savedProduct1 = productService.addProduct(product1);
        savedProduct1.setName("widły");
        productService.updateProduct(savedProduct1);

        //when
        productService.updateProduct(savedProduct1);

        //then
        assertThat(productService.findAllProducts().size()).isEqualTo(1);
    }

    @Test
    public void shouldGetRepo(){
        //when
        ProductRepo productRepo = productService.getProductRepo();

        //then
        assertThat(productRepo).isNotNull();
    }

    @Test(expected = ArgumentIsNullException.class)
    public void shouldThrowArgumentIsNullExceptionWhenCallAdd(){
        //when
        productService.addProduct(null);
    }

    @Test(expected = ArgumentIsNullException.class)
    public void shouldThrowArgumentIsNullExceptionWhenCallUpdate(){
        //when
        productService.updateProduct(null);
    }

    @Test(expected = ArgumentIsNullException.class)
    public void shouldThrowArgumentIsNullExceptionWhenCallDelete(){
        //when
        productService.deleteProduct(null);
    }

    @Test(expected = ArgumentIsNullException.class)
    public void shouldThrowArgumentIsNullExceptionWhenCallFindById(){
        //when
        productService.findProductById(null);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void shouldThrowExceptionWhenTryDeleteFromEmptyDatabase(){
        //when
        productService.deleteProduct(1L);
    }

    @Test
    public void shouldFindProductsWhichAreInRealizationInObject() {
        //given
        TransactionDTO t1 = generateFirstTransaction();
        TransactionDTO t2 = generateSecondTransaction();
        TransactionDTO t3 = generateThirdTransaction();
        TransactionDTO t4 = generateFourthTransaction();
        TransactionDTO t5 = generateFifthTransaction();
        TransactionDTO t6 = generateSixthTransaction();
        TransactionDTO t7 = generateSeventhTransaction();
        TransactionDTO t8 = generateEighthTransaction();
        TransactionDTO t9 = generateNinthTransaction();
        TransactionDTO t10 = generateTenthTransaction();

        TransactionDTO st1 = transactionService.addTransaction(t1);
        TransactionDTO st2 = transactionService.addTransaction(t2);
        TransactionDTO st3 = transactionService.addTransaction(t3);
        TransactionDTO st4 = transactionService.addTransaction(t4);
        TransactionDTO st5 = transactionService.addTransaction(t5);
        TransactionDTO st6 = transactionService.addTransaction(t6);
        TransactionDTO st7 = transactionService.addTransaction(t7);
        TransactionDTO st8 = transactionService.addTransaction(t8);
        TransactionDTO st9 = transactionService.addTransaction(t9);
        TransactionDTO st10 = transactionService.addTransaction(t10);

        ProductDTO p1 = productService.findProductById(st1.getProduct());
        ProductDTO p2 = productService.findProductById(st2.getProduct());
        ProductDTO p3 = productService.findProductById(st3.getProduct());
        ProductDTO p4 = productService.findProductById(st4.getProduct());
        ProductDTO p5 = productService.findProductById(st5.getProduct());
        ProductDTO p6 = productService.findProductById(st6.getProduct());
        ProductDTO p7 = productService.findProductById(st7.getProduct());
        ProductDTO p8 = productService.findProductById(st8.getProduct());
        ProductDTO p9 = productService.findProductById(st9.getProduct());
        ProductDTO p10 = productService.findProductById(st10.getProduct());

        List<ProductInRealizaton> pList = new ArrayList<>();
        pList.add(new ProductInRealizaton(p3.getName(), t3.getNumberProducts()));
        pList.add(new ProductInRealizaton(p4.getName(), t4.getNumberProducts()));
        pList.add(new ProductInRealizaton(p5.getName(), t5.getNumberProducts()));
        pList.add(new ProductInRealizaton(p6.getName(), t6.getNumberProducts()));
        pList.add(new ProductInRealizaton(p7.getName(), t7.getNumberProducts()));

        //when
        List<ProductInRealizaton> productList = productService.findProductsWhichAreInRealizationInObject();

        assertThat(productList).isEqualTo(pList);
    }

    @Test
    public void shouldDoCascadeRemove(){
        //given
        TransactionDTO transaction1 = generateFirstTransaction();
        TransactionDTO savedTransaction1 = transactionService.addTransaction(transaction1);

        ProductDTO product1 = productService.findProductById(savedTransaction1.getProduct());
        Set<Long> transactions = new HashSet<>();
        transactions.add(savedTransaction1.getId());
        product1.setTransactions(transactions);
        ProductDTO updatedProduct1 = productService.updateProduct(product1);

        //when
        productService.deleteProduct(product1.getId());

        //then
        assertThat(transactionService.findAllTransactions()).isEmpty();
    }

    @Test(expected = OptimisticLockingFailureException.class)
    public void shouldThrowOptimisticLockException(){
        //given
        ProductDTO product1 = generateFirstProduct();
        ProductDTO savedProduct = productService.addProduct(product1);

        Long productId = savedProduct.getId();
        ProductDTO productDTO1 = productService.findProductById(productId);
        productDTO1.setName("Tapety");
        ProductDTO productDTO2 = productService.findProductById(productId);

        //when
        productRepo.save(productMapper.mapToEntity(productDTO1));
        entityManager.flush();
        productRepo.save(productMapper.mapToEntity(productDTO2));
    }

    @Test
    public void shouldUpdateDateIsNotEqualCreateDate() throws InterruptedException {
        //given
        ProductDTO product1 = generateFirstProduct();
        ProductDTO savedProduct1 = productService.addProduct(product1);

        savedProduct1.setName("Ziemia do doniczek");
        LocalDateTime createDate = savedProduct1.getCreateDate();
        TimeUnit.SECONDS.sleep(1);

        //when
        productRepo.save(productMapper.mapToEntity(savedProduct1));
        entityManager.flush();
        ProductDTO productDTO = productService.findProductById(savedProduct1.getId());
        LocalDateTime updateDate = productDTO.getUpdateDate();

        //then
        assertThat(updateDate).isNotEqualTo(createDate);
    }
}
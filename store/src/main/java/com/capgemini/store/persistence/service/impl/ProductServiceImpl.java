package com.capgemini.store.persistence.service.impl;

import com.capgemini.store.persistence.dto.ProductDTO;
import com.capgemini.store.persistence.dto.ProductInRealizaton;
import com.capgemini.store.persistence.entity.ProductEntity;
import com.capgemini.store.persistence.entity.TransactionEntity;
import com.capgemini.store.persistence.mapper.ProductMapper;
import com.capgemini.store.persistence.repo.ProductRepo;
import com.capgemini.store.persistence.repo.TransactionRepo;
import com.capgemini.store.persistence.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private ProductMapper productMapper;

    private ProductRepo productRepo;

    private TransactionRepo transactionRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ProductServiceImpl(ProductMapper productMapper, ProductRepo productRepo, TransactionRepo transactionRepo){
        this.productMapper = productMapper;
        this.productRepo = productRepo;
        this.transactionRepo = transactionRepo;
    }

    @Override
    public List<ProductDTO> findAllProducts(){
        return productMapper.mapToDTOList(productRepo.findAll());
    }

    @Override
    public ProductDTO findProductById(Long id){
        checkWhetherArgumentIsNull(id);
        return productMapper.mapToDTO(productRepo.getOne(id));
    }

    private ProductEntity addTransactionsToProductEntity(ProductDTO product){
        ProductEntity productEntity = productMapper.mapToEntity(product);

        Set<TransactionEntity> transactions = product.getTransactions().stream()
                .map(transactionRepo::getOne)
                .collect(Collectors.toSet());

        productEntity.setTransactions(transactions);
        return productEntity;
    }

    @Override
    @Transactional(readOnly = false)
    public ProductDTO addProduct(ProductDTO productDTO){
        checkWhetherArgumentIsNull(productDTO);
        ProductEntity productEntity = addTransactionsToProductEntity(productDTO);
        productEntity = productRepo.save(productEntity);
        return productMapper.mapToDTO(productEntity);
    }

    @Override
    @Transactional(readOnly = false)
    public ProductDTO updateProduct(ProductDTO productDTO){
        checkWhetherArgumentIsNull(productDTO);
        ProductDTO product = findProductById(productDTO.getId());
        product.setName(productDTO.getName());
        product.setMargin(productDTO.getMargin());
        product.setPrice(productDTO.getPrice());
        product.setWeight(productDTO.getWeight());
        product.setTransactions(productDTO.getTransactions());
        ProductEntity productEntity = addTransactionsToProductEntity(product);

        productRepo.save(productEntity);
        return productMapper.mapToDTO(productEntity);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteProduct(Long id){
        checkWhetherArgumentIsNull(id);
        productRepo.deleteById(id);
    }

    public ProductRepo getProductRepo(){
        return productRepo;
    }

    @Override
    public List<ProductDTO> find10BestSellingProducts(){
        return productMapper.mapToDTOList(productRepo.find10BestSellingProducts());
    }

    @Override
    public List<ProductDTO> findProductsWhichAreInRealization(){
        return productMapper.mapToDTOList(productRepo.findProductsWhichAreInRealization());
    }

    @Override
    public List<ProductInRealizaton> findProductsWhichAreInRealizationInObject(){
        return productRepo.findProductsWhichAreInRealizationInObject();
    }

}

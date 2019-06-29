package com.capgemini.store.persistence.repo;

import com.capgemini.store.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<ProductEntity, Long>, ProductRepoCustom{
}

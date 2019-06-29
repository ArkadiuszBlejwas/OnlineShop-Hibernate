package com.capgemini.store.persistence.repo;

import com.capgemini.store.persistence.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepo extends JpaRepository<ClientEntity, Long>, ClientRepoCustom {
}

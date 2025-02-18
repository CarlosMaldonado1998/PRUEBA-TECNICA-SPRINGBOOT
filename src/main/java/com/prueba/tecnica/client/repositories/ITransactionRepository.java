package com.prueba.tecnica.client.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.tecnica.client.entities.AccountEntity;
import com.prueba.tecnica.client.entities.TransactionEntity;
import java.util.List;

public interface ITransactionRepository extends JpaRepository<TransactionEntity, Integer> {
    List<TransactionEntity> findByAccount(AccountEntity account);
}

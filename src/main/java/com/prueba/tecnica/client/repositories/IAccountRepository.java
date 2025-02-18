package com.prueba.tecnica.client.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.tecnica.client.entities.AccountEntity;
import java.util.List;
import java.util.Optional;

public interface IAccountRepository extends JpaRepository<AccountEntity, Integer> {

        Optional<AccountEntity> findByAccountNumber(String accountNumber);

        List<AccountEntity> findByAccountId(Integer accountId);
}

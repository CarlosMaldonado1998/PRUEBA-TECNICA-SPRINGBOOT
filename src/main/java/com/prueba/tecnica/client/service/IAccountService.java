package com.prueba.tecnica.client.service;

import java.util.Optional;

import com.prueba.tecnica.client.entities.AccountEntity;
import com.prueba.tecnica.vo.dto.AccountDto;

public interface IAccountService {

    public AccountEntity createAccount(AccountDto accountDto);

    public Optional<AccountEntity> getAccountById(Integer id);

    public AccountEntity updateAccount(Integer id, AccountDto accountDto);

    public void deleteAccount(Integer id);

}

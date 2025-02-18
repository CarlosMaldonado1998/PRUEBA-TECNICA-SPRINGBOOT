package com.prueba.tecnica.core.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.prueba.tecnica.client.entities.AccountEntity;
import com.prueba.tecnica.client.entities.CustomerEntity;
import com.prueba.tecnica.client.entities.TransactionEntity;
import com.prueba.tecnica.client.repositories.IAccountRepository;
import com.prueba.tecnica.client.repositories.ICustomerRepository;
import com.prueba.tecnica.client.repositories.ITransactionRepository;
import com.prueba.tecnica.client.service.IAccountService;
import com.prueba.tecnica.service.common.CustomBadRequestException;
import com.prueba.tecnica.vo.dto.AccountDto;
import com.prueba.tecnica.vo.dto.CreateTransactionDto;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class AccountService implements IAccountService {

    private final IAccountRepository accountRepository;
    private final ICustomerRepository customerRepository;
    private final ITransactionRepository transactionRepository;

    @Autowired
    public AccountService(IAccountRepository accountRepository, ICustomerRepository customerRepository,
            ITransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public AccountEntity createAccount(AccountDto accountDTO) {

        Optional<AccountEntity> existingAccountNumber = accountRepository
                .findByAccountNumber(accountDTO.getAccountNumber());
        if (!existingAccountNumber.isEmpty()) {
            throw new CustomBadRequestException("Ya existe ese número de cuenta", HttpStatus.CONFLICT);
        }

        Optional<CustomerEntity> existingCustomer = customerRepository.findById(accountDTO.getCustomerId());
        if (!existingCustomer.isPresent()) {
            throw new CustomBadRequestException("Cliente no encontrado", HttpStatus.NOT_FOUND);
        }

        if (!isValidAccountType(accountDTO.getAccountType())) {
            throw new CustomBadRequestException("Tipo de cuenta inválido. Debe ser 'Ahorros' o 'Corriente'.",
                    HttpStatus.BAD_REQUEST);
        }

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountNumber(accountDTO.getAccountNumber());
        accountEntity.setAccountType(accountDTO.getAccountType());
        accountEntity.setInitialBalance(accountDTO.getInitialBalance());
        accountEntity.setStatus(accountDTO.getStatus());
        accountEntity.setCustomer(existingCustomer.get());
        return accountRepository.save(accountEntity);
    }

    public Optional<AccountEntity> getAccountById(Integer id) {
        return accountRepository.findById(id);
    }

    public AccountEntity updateAccount(Integer id, AccountDto accountDTO) {
        Optional<AccountEntity> existingAccountOpt = accountRepository
                .findByAccountNumber(accountDTO.getAccountNumber());
        Optional<CustomerEntity> existingCustomer = customerRepository.findById(accountDTO.getCustomerId());
        if (!existingCustomer.isPresent()) {
            throw new CustomBadRequestException("Cliente no encontrado", HttpStatus.NOT_FOUND);
        }

        if (!isValidAccountType(accountDTO.getAccountType())) {
            throw new CustomBadRequestException("Tipo de cuenta inválido. Debe ser 'Ahorros' o 'Corriente'.",
                    HttpStatus.BAD_REQUEST);
        }

        if (existingAccountOpt.isPresent()) {
            AccountEntity existingAccount = existingAccountOpt.get();
            existingAccount.setAccountNumber(accountDTO.getAccountNumber());
            existingAccount.setAccountType(accountDTO.getAccountType());
            existingAccount.setInitialBalance(accountDTO.getInitialBalance());
            existingAccount.setStatus(accountDTO.getStatus());
            return accountRepository.save(existingAccount);
        } else {
            throw new EntityNotFoundException("La cuenta con ID " + id + " no se ha encontrado");
        }
    }

    public void deleteAccount(Integer id) {
        AccountEntity account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("La cuenta con ID " + id + " no se ha encontrado"));
        account.setStatus("I");
        accountRepository.save(account);
    }

    private boolean isValidAccountType(String type) {
        return type != null && (type.equals("Ahorros") || type.equals("Corriente"));
    }

    @Transactional
    public TransactionEntity createTransaction(Integer accountId, CreateTransactionDto createTransactionDto) {
        // Buscar la cuenta usando el accountId
        List<AccountEntity> accounts = accountRepository.findByAccountId(accountId);
        if (accounts.isEmpty()) {
            throw new CustomBadRequestException("No se ha encontrado ninguna cuenta con el ID " + accountId,
                    HttpStatus.NOT_FOUND);
        }

        AccountEntity account = accounts.get(0);

        // Verificar si el saldo es suficiente para un retiro (si el tipo de transacción
        // es "Retiro")
        if ("Retiro".equals(createTransactionDto.getTransactionType())
                && account.getBalance() < createTransactionDto.getAmount()) {
            throw new CustomBadRequestException("Saldo no disponible", HttpStatus.CONFLICT);
        }

        Double amount = Math.abs(createTransactionDto.getAmount());

        TransactionEntity transaction = new TransactionEntity();
        transaction.setAccount(account);
        transaction.setTransactionDate(new java.sql.Date(System.currentTimeMillis()));
        transaction.setTransactionType(createTransactionDto.getTransactionType());
        transaction.setAmount(amount);
        transaction.setStatus("A");

        // Actualizar el saldo de la cuenta dependiendo del tipo de transacción
        Double newBalance;
        if ("Depósito".equals(createTransactionDto.getTransactionType())) {
            newBalance = account.getBalance() + amount;
        } else if ("Retiro".equals(createTransactionDto.getTransactionType())) {
            newBalance = account.getBalance() - amount;
        } else {
            throw new CustomBadRequestException("Tipo de transacción no válido", HttpStatus.BAD_REQUEST);
        }

        transaction.setBalance(newBalance);
        transactionRepository.save(transaction);
        account.setBalance(newBalance);
        accountRepository.save(account);

        return transaction;
    }

}
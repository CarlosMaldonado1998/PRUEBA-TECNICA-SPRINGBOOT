package com.prueba.tecnica.core.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.prueba.tecnica.client.entities.AccountEntity;
import com.prueba.tecnica.client.entities.TransactionEntity;
import com.prueba.tecnica.client.repositories.IAccountRepository;
import com.prueba.tecnica.client.repositories.ITransactionRepository;
import com.prueba.tecnica.client.service.ITransactionService;
import com.prueba.tecnica.service.common.CustomBadRequestException;
import com.prueba.tecnica.vo.dto.TransactionDto;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class TransactionService implements ITransactionService {

    private final ITransactionRepository transactionRepository;
    private final IAccountRepository accountRepository;

    @Autowired
    public TransactionService(ITransactionRepository transactionRepository, IAccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public TransactionEntity createTransaction(TransactionDto transactionDTO) {

        Optional<AccountEntity> existingAccount = accountRepository.findById(transactionDTO.getAccountId());
        if (!existingAccount.isPresent()) {
            throw new CustomBadRequestException("Cuenta no encontrada", HttpStatus.NOT_FOUND);
        }

        if (!isValidTransaction(transactionDTO.getTransactionType())) {
            throw new CustomBadRequestException("Transacción inválida. Debe ser 'Depósito' o 'Retiro'.", HttpStatus.BAD_REQUEST);
        }

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setTransactionDate(transactionDTO.getTransactionDate());
        transactionEntity.setTransactionType(transactionDTO.getTransactionType());
        transactionEntity.setAmount(transactionDTO.getAmount());
        transactionEntity.setBalance(transactionDTO.getBalance());
        transactionEntity.setStatus(transactionDTO.getStatus());
        transactionEntity.setAccount(existingAccount.get()); 

        return transactionRepository.save(transactionEntity);
    }

    public Optional<TransactionEntity> getTransactionById(Integer id) {
        return transactionRepository.findById(id);
    }

    public TransactionEntity updateTransaction(Integer id, TransactionDto transactionDTO) {
        Optional<TransactionEntity> existingTransactionOpt = transactionRepository.findById(id);
        
        if (!isValidTransaction(transactionDTO.getTransactionType())) {
            throw new CustomBadRequestException("Transacción inválida. Debe ser 'Depósito' o 'Retiro'.", HttpStatus.BAD_REQUEST);
        }
        
        if (existingTransactionOpt.isPresent()) {
            TransactionEntity existingTransaction = existingTransactionOpt.get();
            existingTransaction.setTransactionDate(transactionDTO.getTransactionDate());
            existingTransaction.setTransactionType(transactionDTO.getTransactionType());
            existingTransaction.setAmount(transactionDTO.getAmount());
            existingTransaction.setBalance(transactionDTO.getBalance());
            existingTransaction.setStatus(transactionDTO.getStatus());
            return transactionRepository.save(existingTransaction);
        } else {
            throw new EntityNotFoundException("El movimiento con ID " + id + " no se ha encontrado");
        }
    }

    public void deleteTransaction(Integer id) {
        TransactionEntity transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El movimiento con ID " + id + " no se ha encontrado"));
        transaction.setStatus("I");
        transactionRepository.save(transaction);
    }

    private boolean isValidTransaction(String transaction) {
        return transaction != null && (transaction.equals("Depósito") || transaction.equals("Retiro") );
    }
}

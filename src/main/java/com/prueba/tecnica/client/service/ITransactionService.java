package com.prueba.tecnica.client.service;

import java.util.Optional;

import com.prueba.tecnica.client.entities.TransactionEntity;
import com.prueba.tecnica.vo.dto.TransactionDto;

public interface ITransactionService {

    public TransactionEntity createTransaction(TransactionDto transactionDto);

    public Optional<TransactionEntity> getTransactionById(Integer id);

    public TransactionEntity updateTransaction(Integer id, TransactionDto transactionDto);

    public void deleteTransaction(Integer id);

}

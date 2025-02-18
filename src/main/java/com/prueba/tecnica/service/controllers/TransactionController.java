package com.prueba.tecnica.service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.prueba.tecnica.client.entities.TransactionEntity;
import com.prueba.tecnica.core.services.AccountService;
import com.prueba.tecnica.core.services.TransactionService;
import com.prueba.tecnica.vo.dto.CreateTransactionDto;
import com.prueba.tecnica.vo.dto.TransactionDto;

import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;

@RestController
@RequestMapping("/movimientos")
public class TransactionController {

    private final TransactionService transactionService;
    private final AccountService accountService;

    @Autowired
    public TransactionController(TransactionService transactionService, AccountService accountService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<TransactionEntity> createTransaction(@Validated @RequestBody TransactionDto transaction) {
        return ResponseEntity.ok(transactionService.createTransaction(transaction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionEntity> getTransactionById(@PathVariable Integer id) {
        Optional<TransactionEntity> transaction = transactionService.getTransactionById(id);
        return transaction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionEntity> updateTransaction(@PathVariable Integer id,
            @RequestBody @Validated TransactionDto transactionDTO) {
        try {
            TransactionEntity updatedTransaction = transactionService.updateTransaction(id, transactionDTO);
            return ResponseEntity.ok(updatedTransaction);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteTransaction(@PathVariable Integer id) {
        try {
            transactionService.deleteTransaction(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/crear/{accountId}")
    public ResponseEntity<TransactionEntity> createTransaction(
            @PathVariable Integer accountId,
            @RequestBody @Validated CreateTransactionDto createTransactionDto) {

        TransactionEntity transaction = accountService.createTransaction(accountId, createTransactionDto);
        return ResponseEntity.ok(transaction);

    }
}
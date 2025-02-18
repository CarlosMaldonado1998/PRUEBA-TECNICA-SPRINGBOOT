package com.prueba.tecnica.client.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "account")
@Data
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Integer accountId;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id", nullable = false)
    private CustomerEntity customer;

    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;

    @Column(name = "account_type")
    private String accountType;

    @Column(name = "initial_balance")
    private Double initialBalance;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "status")
    private String status;
}

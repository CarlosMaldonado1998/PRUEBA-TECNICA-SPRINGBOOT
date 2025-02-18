package com.prueba.tecnica.client.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "customer")
@Data
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer customerId;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id", nullable = false)
    private PersonEntity person;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private String status;
}

package com.prueba.tecnica.client.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.tecnica.client.entities.CustomerEntity;

public interface ICustomerRepository extends JpaRepository<CustomerEntity, Integer> {

}

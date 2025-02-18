package com.prueba.tecnica.client.service;

import java.util.Optional;

import com.prueba.tecnica.client.entities.CustomerEntity;
import com.prueba.tecnica.vo.dto.CustomerDto;

public interface ICustomerService {

    public CustomerEntity createCustomer(CustomerDto customerDto);

    public Optional<CustomerEntity> getCustomerById(Integer id);

    public CustomerEntity updateCustomer(Integer id, CustomerDto customerDto);

    public void deleteCustomer(Integer id);

}

package com.prueba.tecnica.core.services;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.prueba.tecnica.client.entities.CustomerEntity;
import com.prueba.tecnica.client.entities.PersonEntity;
import com.prueba.tecnica.client.repositories.ICustomerRepository;
import com.prueba.tecnica.client.repositories.IPersonRepository;
import com.prueba.tecnica.client.service.ICustomerService;
import com.prueba.tecnica.service.common.CustomBadRequestException;
import com.prueba.tecnica.vo.dto.CustomerDto;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CustomerService implements ICustomerService {

    private final ICustomerRepository customerRepository;
    private final IPersonRepository personRepository;

    @Autowired
    public CustomerService(ICustomerRepository customerRepository, IPersonRepository personRepository) {
        this.customerRepository = customerRepository;
        this.personRepository = personRepository;
    }

    @Transactional
    public CustomerEntity createCustomer(CustomerDto customerDTO) {
        Optional<PersonEntity> existingPerson = personRepository.findById(customerDTO.getPersonId());
        if (!existingPerson.isPresent()) {
            throw new CustomBadRequestException("Persona no encontrada", HttpStatus.NOT_FOUND);
        }

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setPassword(customerDTO.getPassword());
        customerEntity.setStatus(customerDTO.getStatus());
        customerEntity.setPerson(existingPerson.get()); 

        return customerRepository.save(customerEntity);
    }

    public Optional<CustomerEntity> getCustomerById(Integer id) {
        return customerRepository.findById(id);
    }

    public CustomerEntity updateCustomer(Integer id, CustomerDto customerDTO) {
        Optional<CustomerEntity> existingCustomerOpt = customerRepository.findById(id);
        Optional<PersonEntity> existingPerson = personRepository.findById(customerDTO.getPersonId());
        if (!existingPerson.isPresent()) {
            throw new CustomBadRequestException("Persona no encontrada", HttpStatus.NOT_FOUND);
        }
        
        if (existingCustomerOpt.isPresent()) {
            CustomerEntity existingCustomer = existingCustomerOpt.get();
            existingCustomer.setPassword(customerDTO.getPassword());
            existingCustomer.setStatus(customerDTO.getStatus());
            return customerRepository.save(existingCustomer);
        } else {
            throw new EntityNotFoundException("El cliente con ID " + id + " no se ha encontrado");
        }
    }

    public void deleteCustomer(Integer id) {
        CustomerEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El cliente con ID " + id + " no se ha encontrado"));
        customer.setStatus("I");
        customerRepository.save(customer);
    }
}
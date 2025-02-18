package com.prueba.tecnica.client.service;

import java.util.Optional;

import com.prueba.tecnica.client.entities.PersonEntity;
import com.prueba.tecnica.vo.dto.PersonDto;

public interface IPersonService {

    public PersonEntity createPerson(PersonDto personDTO);

    public Optional<PersonEntity> getPersonById(Integer id);

    public PersonEntity updatePerson(Integer id, PersonDto personDTO);

    public void deletePerson(Integer id);

}
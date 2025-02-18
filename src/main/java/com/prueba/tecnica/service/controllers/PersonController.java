package com.prueba.tecnica.service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import com.prueba.tecnica.client.entities.PersonEntity;
import com.prueba.tecnica.core.services.PersonService;
import com.prueba.tecnica.vo.dto.PersonDto;

import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;


@RestController
@RequestMapping("/persona")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<PersonEntity> createPerson(@Validated @RequestBody PersonDto person) {
        return ResponseEntity.ok(personService.createPerson(person));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonEntity> getPersonById(@PathVariable Integer id) {
        Optional<PersonEntity> person = personService.getPersonById(id);
        return person.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

   @PutMapping("/{id}")
    public ResponseEntity<PersonEntity> updatePerson(@PathVariable Integer id, @RequestBody @Validated PersonDto personDTO) {
        try {
            PersonEntity updatedPerson = personService.updatePerson(id, personDTO);
            return ResponseEntity.ok(updatedPerson); 
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeletePerson(@PathVariable Integer id) {
        try {
            personService.deletePerson(id); 
            return ResponseEntity.noContent().build(); 
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); 
        }
    }
}
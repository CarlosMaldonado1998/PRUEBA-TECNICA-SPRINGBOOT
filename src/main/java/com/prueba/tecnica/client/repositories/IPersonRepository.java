package com.prueba.tecnica.client.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.tecnica.client.entities.PersonEntity;
import java.util.List;

public interface IPersonRepository extends JpaRepository<PersonEntity, Integer> {

    List<PersonEntity> findByIdentification(String identification);

}

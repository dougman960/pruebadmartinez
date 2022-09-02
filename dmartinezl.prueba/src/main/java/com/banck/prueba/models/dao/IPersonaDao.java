package com.banck.prueba.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banck.prueba.models.entity.Persona;

public interface IPersonaDao extends JpaRepository<Persona, Long>{

	 boolean existsByIdentificacion(String identificacion);
	
	 Persona findByIdentificacion(String identificacion);
}

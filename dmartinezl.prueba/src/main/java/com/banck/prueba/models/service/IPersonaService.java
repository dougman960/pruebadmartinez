package com.banck.prueba.models.service;

import com.banck.prueba.models.entity.Persona;

public interface IPersonaService {

	
	public  boolean existsByIdentificacion(String identificacion);
	
	public Persona findByIdentificacion(String identificacion);
}

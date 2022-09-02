package com.banck.prueba.models.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banck.prueba.models.dao.IPersonaDao;
import com.banck.prueba.models.entity.Persona;
import com.banck.prueba.models.service.IPersonaService;

@Service
public class PersonaServiceImpl implements IPersonaService{

	@Autowired
	IPersonaDao iPersonaDao;
	
	
	
	@Override
	@Transactional(readOnly = true)
	public  boolean existsByIdentificacion(String identificacion) {
		return iPersonaDao.existsByIdentificacion(identificacion);
	}



	@Override
	@Transactional(readOnly = true)
	public Persona findByIdentificacion(String identificacion) {
		return iPersonaDao.findByIdentificacion(identificacion);
	}

}

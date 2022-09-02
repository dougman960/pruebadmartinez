package com.banck.prueba.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banck.prueba.models.entity.Cliente;

public interface IClienteDao extends JpaRepository<Cliente, Long>{
	
	Cliente findByClienteId(Long id);
	
	boolean existsByClienteId(Long id);

}

package com.banck.prueba.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banck.prueba.models.entity.Cuenta;

public interface ICuentaDao extends JpaRepository<Cuenta, Long>{
	
	Cuenta findByNumeroCuenta(Long numeroCuenta);
	
	boolean existsByNumeroCuenta(Long numeroCuenta);
	

}

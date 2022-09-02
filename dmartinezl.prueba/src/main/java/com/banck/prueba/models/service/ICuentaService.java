package com.banck.prueba.models.service;

import com.banck.prueba.models.entity.Cuenta;

public interface ICuentaService {
	
	public Cuenta create (Cuenta cuenta,Long clienteId);
	
	public boolean existsByNumeroCuenta(Long numeroCuenta);
	
	public void delete (Long numerCuenta);
	
	public Cuenta findByNumeroCuenta(Long numeroCuenta);
}

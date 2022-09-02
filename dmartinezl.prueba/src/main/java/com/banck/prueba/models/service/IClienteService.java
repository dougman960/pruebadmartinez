package com.banck.prueba.models.service;

import com.banck.prueba.models.entity.Cliente;

public interface IClienteService {

	
	public Cliente save (Cliente cliente);
	
	public Cliente findByClienteId(Long clienteId);
	
	public void update(Cliente cliente);
	
	public void delete (Long clienteId);
	
	public boolean existsByClienteId(Long clienteId);
	
}

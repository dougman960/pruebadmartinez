package com.banck.prueba.models.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banck.prueba.models.dao.IClienteDao;
import com.banck.prueba.models.entity.Cliente;
import com.banck.prueba.models.entity.Cuenta;
import com.banck.prueba.models.entity.Persona;
import com.banck.prueba.models.service.IClienteService;


@Service
public class ClienteServiceImpl implements IClienteService {

	@Autowired
	IClienteDao iClienteDao;
	
	@Autowired
	PersonaServiceImpl personaServiceImpl;
	
	
	@Override
	@Transactional
	public Cliente save(Cliente cliente) {
		return iClienteDao.save(cliente);
	}


	@Override
	@Transactional(readOnly = true)
	public Cliente findByClienteId(Long clienteId) {
		return iClienteDao.findByClienteId(clienteId);
	}


	@Override
	@Transactional
	public void update(Cliente cliente) {
		
		System.out.println("contraseña" + cliente.getContrasena());
		Cliente clienteActualizado = new Cliente();
		Persona personaActualizada = new Persona();

		Cliente clienteAnterior =  iClienteDao.findByClienteId(cliente.getClienteId());
		Persona personaAnterior = personaServiceImpl.findByIdentificacion(cliente.getPersona().getIdentificacion());
		

		
		clienteAnterior.setContrasena(cliente.getContrasena());
		
		personaAnterior.setGenero(cliente.getPersona().getGenero());
		personaAnterior.setEdad(cliente.getPersona().getEdad());
		personaAnterior.setIdentificacion(cliente.getPersona().getIdentificacion());
		personaAnterior.setDireccion(cliente.getPersona().getDireccion());
		personaAnterior.setTelefono(cliente.getPersona().getTelefono());
		
		personaActualizada = personaAnterior;
		
		clienteAnterior.setPersona(personaActualizada);
		
		clienteActualizado = clienteAnterior;
		
		iClienteDao.save(clienteActualizado);
		
	}


	@Override
	@Transactional
	public void delete(Long clienteId) {
		Cliente clienteEleminado = new Cliente();
		Cliente clienteAnterior = iClienteDao.findByClienteId(clienteId);
		List<Cuenta> cuenta = new ArrayList<>();
		
		clienteAnterior.setEstado("X");
		
		for(Cuenta cuentas : clienteAnterior.getCuentas()) {
			cuentas.setEstado("X");
			cuenta.add(cuentas);
		}
		
		clienteAnterior.setCuentas(cuenta);
		
		clienteEleminado = clienteAnterior;
	
		iClienteDao.save(clienteEleminado);
	}


	@Override
	@Transactional(readOnly = true)
	public boolean existsByClienteId(Long id) {
		return iClienteDao.existsByClienteId(id);
	}

}

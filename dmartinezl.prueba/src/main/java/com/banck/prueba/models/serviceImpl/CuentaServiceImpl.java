package com.banck.prueba.models.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banck.prueba.models.dao.ICuentaDao;
import com.banck.prueba.models.entity.Cliente;
import com.banck.prueba.models.entity.Cuenta;
import com.banck.prueba.models.service.ICuentaService;

@Service
public class CuentaServiceImpl implements ICuentaService{

	@Autowired
	ClienteServiceImpl clienteServiceImpl;
	
	@Autowired
	ICuentaDao iCuentaDao;
	
	
	@Override
	@Transactional
	public Cuenta create(Cuenta cuenta,Long clienteId) {
		Cliente cliente = clienteServiceImpl.findByClienteId(clienteId);
		List<Cuenta> cuentas = cliente.getCuentas();
		
		cuentas.add(cuenta);			
		cliente.setCuentas(cuentas);
		
		clienteServiceImpl.save(cliente);
		
		return iCuentaDao.findByNumeroCuenta(cuenta.getNumeroCuenta());
	}


	@Override
	@Transactional(readOnly = true)
	public boolean existsByNumeroCuenta(Long numeroCuenta) {
		return iCuentaDao.existsByNumeroCuenta(numeroCuenta);
	}


	@Override
	@Transactional
	public void delete(Long numerCuenta) {
		Cuenta cuenta = iCuentaDao.findByNumeroCuenta(numerCuenta);
		
		cuenta.setEstado("X");
		
		iCuentaDao.save(cuenta);
		
	}


	@Override
	@Transactional(readOnly = true)
	public Cuenta findByNumeroCuenta(Long numeroCuenta) {
		return iCuentaDao.findByNumeroCuenta(numeroCuenta);
	}

	
	
	
	
}

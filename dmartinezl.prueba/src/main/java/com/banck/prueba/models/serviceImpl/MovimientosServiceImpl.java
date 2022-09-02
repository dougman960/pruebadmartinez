package com.banck.prueba.models.serviceImpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banck.prueba.models.dao.IMovimientoDao;
import com.banck.prueba.models.entity.Cuenta;
import com.banck.prueba.models.entity.Movimientos;
import com.banck.prueba.models.service.IMovimientoService;


@Service
public class MovimientosServiceImpl implements IMovimientoService{
	
	@Autowired
	IMovimientoDao iMovimientoDao;
	
	@Autowired 
	CuentaServiceImpl cuentaServiceImpl;

	@Override
	@Transactional(readOnly = true)
	public Movimientos findByNumeroCuentaAndEstado(Long numeroCuenta) {
		return iMovimientoDao.findByNumeroCuentaAndEstado(numeroCuenta,"A");
	}

	@Override
	@Transactional(readOnly = true)
	public List<Movimientos> findByNumeroCuentaAndFechaBetween(Long numeroCuenta,Date fechaDesde,Date  fechaHasta) {
		return iMovimientoDao.findByNumeroCuentaAndFechaBetween(numeroCuenta,fechaDesde,fechaHasta);
	}

	@Override
	@Transactional
	public void saveAcreditacion(Long numeroCuenta , Double valor) {
		
		Cuenta cuenta = new Cuenta();
		Double valorInicial= 0.0;
		Double suma = 0.0;
		Movimientos movimientoNuevo = new Movimientos();
		
		if (iMovimientoDao.existsByNumeroCuentaAndEstado(numeroCuenta,"A") == false) {
			cuenta = cuentaServiceImpl.findByNumeroCuenta(numeroCuenta);
			valorInicial = cuenta.getSaldoInicial();
			suma = valorInicial + valor;
			
	
		}else {
			Movimientos movimientoAnterior = iMovimientoDao.findByNumeroCuentaAndEstado(numeroCuenta, "A");				
			suma = movimientoAnterior.getSaldo() + valor;
			
			AnulaRegistro(movimientoAnterior.getIdMovimientos());

		}
	
		movimientoNuevo.setNumeroCuenta(numeroCuenta);
		movimientoNuevo.setTipoMovimiento("Acreditacion de ".concat(String.valueOf(valor)));
		movimientoNuevo.prePersist();
		movimientoNuevo.setValor(String.valueOf(valor));
		movimientoNuevo.setSaldo(suma);
		movimientoNuevo.setEstado("A");
		
		
		iMovimientoDao.save(movimientoNuevo);
		
		
	}
	
	
	@Override
	@Transactional
	public void saveDebito(Long numeroCuenta , Double valor) {
		
		Cuenta cuenta = new Cuenta();
		Double valorInicial= 0.0;
		Double suma = 0.0;
		Movimientos movimientoNuevo = new Movimientos();
		
		if (iMovimientoDao.existsByNumeroCuentaAndEstado(numeroCuenta,"A") == false) {
			cuenta = cuentaServiceImpl.findByNumeroCuenta(numeroCuenta);
			valorInicial = cuenta.getSaldoInicial();
			suma = valorInicial - valor;
			
	
		}else {
			Movimientos movimientoAnterior = iMovimientoDao.findByNumeroCuentaAndEstado(numeroCuenta, "A");				
			suma = movimientoAnterior.getSaldo() - valor;
			
			AnulaRegistro(movimientoAnterior.getIdMovimientos());

		}
	
		movimientoNuevo.setNumeroCuenta(numeroCuenta);
		movimientoNuevo.setTipoMovimiento("Debito de ".concat(String.valueOf(valor)));
		movimientoNuevo.prePersist();
		movimientoNuevo.setValor("-".concat(String.valueOf(valor)));
		movimientoNuevo.setSaldo(suma);
		movimientoNuevo.setEstado("A");
		
		
		iMovimientoDao.save(movimientoNuevo);
		
		
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existsByNumeroCuentaAndEstado(Long numeroCuenta) {
		return iMovimientoDao.existsByNumeroCuentaAndEstado(numeroCuenta,"A");
	}
	
	
	@Override
	@Transactional
	public void AnulaRegistro (Long idMovimiento) {
		Movimientos movimientoAnulado = new Movimientos();
		Movimientos movimiento = iMovimientoDao.findByIdMovimientos(idMovimiento);
		
		movimiento.setEstado("X");
		
		movimientoAnulado = movimiento;
		
		iMovimientoDao.save(movimientoAnulado);
	}

}

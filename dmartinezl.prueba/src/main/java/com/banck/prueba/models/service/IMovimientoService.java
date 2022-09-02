package com.banck.prueba.models.service;

import java.util.Date;
import java.util.List;

import com.banck.prueba.models.entity.Movimientos;

public interface IMovimientoService {
	
	public Movimientos findByNumeroCuentaAndEstado(Long numeroCuenta);
	
	public List<Movimientos> findByNumeroCuentaAndFechaBetween(Long numeroCuenta,Date fechaDesde,Date  fechaHasta) ;
	
	public void saveAcreditacion (Long numeroCuenta , Double valor);
	
	public boolean existsByNumeroCuentaAndEstado(Long numeroCuenta);
	
	public void AnulaRegistro (Long idMovimiento);
	
	public void saveDebito(Long numeroCuenta , Double valor) ;


}

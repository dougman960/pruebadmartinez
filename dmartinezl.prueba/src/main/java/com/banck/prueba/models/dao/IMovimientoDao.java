package com.banck.prueba.models.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banck.prueba.models.entity.Movimientos;

public interface IMovimientoDao extends JpaRepository<Movimientos, Long>{
	
	Movimientos findByNumeroCuentaAndEstado(Long numeroCuenta,String estado);
	
	List<Movimientos> findByNumeroCuentaAndFechaBetween(Long numeroCuenta,Date fechaDesde,Date fechaHasta);
	
	boolean existsByNumeroCuentaAndEstado(Long numeroCuenta,String estado);
	
	Movimientos findByIdMovimientos(Long id);

}

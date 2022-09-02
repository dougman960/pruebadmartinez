package com.banck.prueba.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banck.prueba.models.dto.MovimientosDto;
import com.banck.prueba.models.dto.ResponseMovimientosDto;
import com.banck.prueba.models.dto.TransformDto;
import com.banck.prueba.models.entity.Cuenta;
import com.banck.prueba.models.entity.Movimientos;
import com.banck.prueba.models.serviceImpl.CuentaServiceImpl;
import com.banck.prueba.models.serviceImpl.MovimientosServiceImpl;

@RestController
@RequestMapping("/api/movimientos/")
public class MovimientosController {
	
	private static final Logger logger = LogManager.getLogger(MovimientosController.class);

	@Autowired
	MovimientosServiceImpl movimientosServiceImpl;
	
	@Autowired 
	CuentaServiceImpl cuentaServiceImpl;
	
	@Autowired
	TransformDto transformDto;
	

	
	@GetMapping("/find")
	public ResponseEntity<?> findMovimientos (@RequestParam Long numeroCuenta,@RequestParam Date fechaDesde,@RequestParam Date fechaHasta){
		
		Map<String, Object> response = new HashMap<>();
		List<Movimientos> movimientos = new ArrayList<>();
		Cuenta cuenta = new Cuenta();
	
		try {
			
			if(cuentaServiceImpl.existsByNumeroCuenta(numeroCuenta) == false) {
				response.put("mensaje", "La cuenta No. "
						.concat(String.valueOf(numeroCuenta) ).concat(" no existe"));
				logger.info("mensaje", "La cuenta No "
						.concat(String.valueOf(numeroCuenta)).concat(" no existe"));

				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			
			movimientos= movimientosServiceImpl.findByNumeroCuentaAndFechaBetween(numeroCuenta, fechaDesde, fechaHasta);
			cuenta = cuentaServiceImpl.findByNumeroCuenta(numeroCuenta);
			
		} catch (DataAccessException e) {
			response.put("mensaje", "Error en la consulta ");
			logger.info("Error en la consulta  "
					+ e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		List<ResponseMovimientosDto> responseMovimientosDtos = transformDto.transforma(cuenta, movimientos);
		
		response.put("movimientos", responseMovimientosDtos);
		System.out.println(response);
		
		return 	 new ResponseEntity<>(responseMovimientosDtos, HttpStatus.OK);
		
	}
	
	
	@PostMapping(value="/acreditar" , consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
				MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> acreditacion (@RequestBody MovimientosDto movimientosDto,@RequestParam Long numeroCuenta){
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			Double valor = movimientosDto.getValor();
			movimientosServiceImpl.saveAcreditacion(numeroCuenta, valor);
			
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al registrar acreditacion ");
			logger.info("Error al registrar acreditacion  "
					+ e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);		}
		
		
		response.put("mensaje", "Acreditacion exitosa a la cuenta ".concat(String.valueOf(numeroCuenta)));
		return new ResponseEntity<>(response, HttpStatus.OK);	
	}
	
	
	@PostMapping(value = "/debitar", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> debito (@RequestBody MovimientosDto movimientosDto,@RequestParam Long numeroCuenta){
		
		Map<String, Object> response = new HashMap<>();
		
		try {
			
			if (movimientosServiceImpl.existsByNumeroCuentaAndEstado(numeroCuenta) == false) {
				Cuenta cuenta = cuentaServiceImpl.findByNumeroCuenta(numeroCuenta);
				if (cuenta.getSaldoInicial() < 0) {
					response.put("mensaje", "Fondos Insuficientes");
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				}
			}else {
				Movimientos movimientos = movimientosServiceImpl.findByNumeroCuentaAndEstado(numeroCuenta);
				if (movimientos.getSaldo() < 0) {
					response.put("mensaje", "Fondos Insuficientes");
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				}
			}
			
			Double valor = movimientosDto.getValor();
			movimientosServiceImpl.saveDebito(numeroCuenta, valor);
			
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al registrar acreditacion ");
			logger.info("Error al registrar acreditacion  "
					+ e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);		}
		
		
		response.put("mensaje", "Debito exitoso de la cuenta ".concat(String.valueOf(numeroCuenta)));
		return new ResponseEntity<>(response, HttpStatus.OK);	
	}
	
	
}

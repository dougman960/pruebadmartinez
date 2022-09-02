package com.banck.prueba.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banck.prueba.models.entity.Cuenta;
import com.banck.prueba.models.serviceImpl.ClienteServiceImpl;
import com.banck.prueba.models.serviceImpl.CuentaServiceImpl;

@RestController
@RequestMapping("/api/cuentas/")
public class CuentaController {
	
	private static final Logger logger = LogManager.getLogger(ClienteController.class);


	@Autowired
	ClienteServiceImpl clienteServiceImpl;
	
	@Autowired
	CuentaServiceImpl cuentaServiceImpl;
	
	@PostMapping("create")
	public ResponseEntity<?> createCuenta(@RequestBody Cuenta cuenta, @RequestParam Long clienteId){
		Map<String, Object> response = new HashMap<>();
		
		
		try {
			
			if(clienteServiceImpl.existsByClienteId(clienteId) == false) {
				response.put("mensaje", "El cliente con id "
						.concat(String.valueOf(clienteId) ).concat(" no existe"));
				logger.info("mensaje", "El cliente con identifiacion "
						.concat(String.valueOf(clienteId)).concat(" no existe"));

				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			if (cuentaServiceImpl.existsByNumeroCuenta(cuenta.getNumeroCuenta()) == true) {
				response.put("mensaje", "La cuenta No. "
						.concat(String.valueOf(cuenta.getNumeroCuenta()) ).concat(" ya existe"));
				logger.info("mensaje", "La cuenta No. "
						.concat(String.valueOf(cuenta.getNumeroCuenta())).concat(" ya existe"));
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

			}
			
			cuentaServiceImpl.create(cuenta, clienteId);
			
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al registrar cuenta ");
			logger.info("Error al registrar cuenta  "
					+ e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "Cuenta No. ".concat(String.valueOf(cuenta.getNumeroCuenta()).concat(" registrada exitosamente")));
		
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	
	@DeleteMapping("delete")
	public ResponseEntity<?> deleteCuenta(@RequestParam Long numeroCuenta){	
		Map<String, Object> response = new HashMap<>();

		try {
			Cuenta cuenta = cuentaServiceImpl.findByNumeroCuenta(numeroCuenta);
			
			if(cuentaServiceImpl.existsByNumeroCuenta(numeroCuenta) == false) {
				response.put("mensaje", "La cuenta No. "
						.concat(String.valueOf(numeroCuenta) ).concat(" no existe"));
				logger.info("mensaje", "La cuenta No "
						.concat(String.valueOf(numeroCuenta)).concat(" no existe"));

				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}else if (cuenta.getEstado().equals("X")) {
				response.put("mensaje", "La cuenta No. "
						.concat(String.valueOf(numeroCuenta) ).concat(" se encuentra inactiva"));
				logger.info("mensaje", "La cuenta No "
						.concat(String.valueOf(numeroCuenta)).concat(" se encuentra inactiva"));
			}
			
			cuentaServiceImpl.delete(numeroCuenta);
			
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar la cuenta");
			logger.info("Error al eliminar la cuenta  "
					+ e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "Cuenta No. ".concat(String.valueOf(numeroCuenta).concat(" eliminada exitosamente")));

		
		return  new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@GetMapping("find")
	public ResponseEntity<?> findCuenta (@RequestParam Long numeroCuenta){
		
		Map<String, Object> response = new HashMap<>();
		Cuenta cuenta = new Cuenta();
		
		try {
			
			if (cuentaServiceImpl.existsByNumeroCuenta(numeroCuenta)) {
				response.put("mensaje", "La cuenta No. "
						.concat(String.valueOf(numeroCuenta) ).concat(" no existe"));
				logger.info("mensaje", "La cuenta No "
						.concat(String.valueOf(numeroCuenta)).concat(" no existe"));				
			}
			
			cuenta = cuentaServiceImpl.findByNumeroCuenta(numeroCuenta);
			
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al consulta cuenta");
			logger.info("Error al consultar cuenta  "
					+ e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		return 	new ResponseEntity<>(cuenta, HttpStatus.OK);		
	}

	
}

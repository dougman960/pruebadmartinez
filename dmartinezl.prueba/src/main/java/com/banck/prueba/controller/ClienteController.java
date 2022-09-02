package com.banck.prueba.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banck.prueba.models.entity.Cliente;
import com.banck.prueba.models.entity.Persona;
import com.banck.prueba.models.serviceImpl.ClienteServiceImpl;
import com.banck.prueba.models.serviceImpl.PersonaServiceImpl;

@RestController
@RequestMapping("/api/clientes/")
public class ClienteController {

	private static final Logger logger = LogManager.getLogger(ClienteController.class);

	@Autowired
	private PersonaServiceImpl personaServiceImpl;

	@Autowired
	private ClienteServiceImpl clienteServiceImpl;

	@PostMapping(value = "/create", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> save(@Valid @RequestBody Cliente cliente, BindingResult result) {

		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(err -> {
				return "El campo '" + err.getField() + " ' " + err.getDefaultMessage();
			}).collect(Collectors.toList());
			response.put("errors", errors);

			logger.info("error bindig result" + errors);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {

			if (personaServiceImpl.existsByIdentificacion(cliente.getPersona().getIdentificacion()) == true) {
				response.put("mensaje", "El cliente con identifiacion "
						.concat(cliente.getPersona().getIdentificacion().trim()).concat(" ya existe"));
				logger.info("mensaje", "El cliente con identifiacion "
						.concat(cliente.getPersona().getIdentificacion().trim()).concat(" ya existe"));

				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			clienteServiceImpl.save(cliente);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al registrar datos ");
			logger.info("Error al registrar datos  "
					+ e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El cliente con identifiacion ".concat(cliente.getPersona().getIdentificacion().trim())
				.concat(" fue registrado exitosamente"));
		return new ResponseEntity<>(response, HttpStatus.CREATED);

	}

	@GetMapping("find")
	public ResponseEntity<?> findCliente(@RequestParam String identificacion) {

		Map<String, Object> response = new HashMap<>();
		Persona persona = new Persona();
		Cliente cliente = new Cliente();

		try {

			if (personaServiceImpl.existsByIdentificacion(identificacion) == false) {
				response.put("mensaje", "El cliente con identifiacion ".concat(identificacion).concat(" no existe"));
				logger.info("mensaje", "El cliente con identifiacion ".concat(identificacion).concat(" no existe"));

				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			persona = personaServiceImpl.findByIdentificacion(identificacion);
			cliente = clienteServiceImpl.findByClienteId(persona.getClienteId());

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta ");
			logger.info("Error al realizar la consulta "
					+ e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(cliente, HttpStatus.OK);

	}

	@PutMapping("update")
	public ResponseEntity<?> updateCliente(@Valid @RequestBody Cliente cliente, BindingResult result) {

		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(err -> {
				return "El campo '" + err.getField() + " ' " + err.getDefaultMessage();
			}).collect(Collectors.toList());
			response.put("errors", errors);

			logger.info("error bindig result" + errors);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {

			if (personaServiceImpl.existsByIdentificacion(cliente.getPersona().getIdentificacion()) == false) {
				response.put("mensaje", "El cliente con identifiacion ".concat(cliente.getPersona().getIdentificacion())
						.concat(" no existe"));
				logger.info("mensaje", "El cliente con identifiacion ".concat(cliente.getPersona().getIdentificacion())
						.concat(" no existe"));
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			clienteServiceImpl.update(cliente);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar datos ");
			logger.info("Error al actualizar datos  "
					+ e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje",
				"Cliente con id ".concat(cliente.getClienteId().toString().concat(" actualizado exitosamente")));

		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@DeleteMapping("delete")
	public ResponseEntity<?> deleteCliente(@RequestParam Long clienteId) {
		Map<String, Object> response = new HashMap<>();

		try {

			if (clienteServiceImpl.existsByClienteId(clienteId) == false) {
				response.put("mensaje", "El cliente con id ".concat(String.valueOf(clienteId) )
						.concat(" no existe"));
				logger.info("mensaje", "El cliente con id ".concat(String.valueOf(clienteId))
						.concat(" no existe"));
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			clienteServiceImpl.delete(clienteId);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar los datos ");
			logger.info("Error al eliminar los datos  "
					+ e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El cliente con id ".concat(String.valueOf(clienteId)).concat(" fue eliminado exitosamente"));

		return 	new ResponseEntity<>(response, HttpStatus.OK);

	}

}

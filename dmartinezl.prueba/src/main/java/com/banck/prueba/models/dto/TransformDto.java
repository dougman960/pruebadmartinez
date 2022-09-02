package com.banck.prueba.models.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;

import com.banck.prueba.models.entity.Cuenta;
import com.banck.prueba.models.entity.Movimientos;

@Configuration
public class TransformDto {

	
	public List<ResponseMovimientosDto> transforma (Cuenta cuenta,List<Movimientos> movimientos) {
		
		List<ResponseMovimientosDto> ListresponseMovimientosDto = new ArrayList<>();
		
		for(Movimientos movimiento : movimientos) {
			ResponseMovimientosDto responseMovimientosDto = new ResponseMovimientosDto();
			
			responseMovimientosDto.setFecha(movimiento.getFecha());
			responseMovimientosDto.setCliente(cuenta.getCliente().getPersona().getNombre());
			responseMovimientosDto.setNumeroCuenta(cuenta.getNumeroCuenta());
			responseMovimientosDto.setTipo(cuenta.getTipoCuenta());
			responseMovimientosDto.setSaldoInicial(cuenta.getSaldoInicial());
			responseMovimientosDto.setEstado(cuenta.getEstado());
			responseMovimientosDto.setMovimieto(movimiento.getTipoMovimiento());
			responseMovimientosDto.setSaldoDisponible(movimiento.getSaldo());
			
			ListresponseMovimientosDto.add(responseMovimientosDto);
		}
		
		
		return ListresponseMovimientosDto;
		
	}
}

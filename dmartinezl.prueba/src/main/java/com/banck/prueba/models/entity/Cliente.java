package com.banck.prueba.models.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "cliente")
public class Cliente implements Serializable {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long clienteId;
	
	@NotNull
	@NotEmpty(message = "no puede estar vacio")
	private String contrasena;
	
	@NotNull
	@NotEmpty(message = "no puede estar vacio")
	private String estado;
	
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "clienteId")
	@JsonIgnoreProperties({ "hibernateLazyInitialzer" , "handler" })
	@Valid
	private Persona persona;
	
	@OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinTable(name = "clientes_cuentas", joinColumns = @JoinColumn(name = "clienteId"), 
			   inverseJoinColumns = @JoinColumn(name = "numeroCuenta"), uniqueConstraints = {
			   @UniqueConstraint(columnNames = { "clienteId", "numeroCuenta" }) })
	@JsonIgnoreProperties({ "hibernateLazyInitialzer" , "handler" })
	private List<Cuenta> cuentas;
	
	
	
	public Long getClienteId() {
		return clienteId;
	}



	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}

	public String getContrasena() {
		return contrasena;
	}



	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}



	public String getEstado() {
		return estado;
	}



	public void setEstado(String estado) {
		this.estado = estado;
	}



	public Persona getPersona() {
		return persona;
	}



	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	


	public List<Cuenta> getCuentas() {
		return cuentas;
	}



	public void setCuentas(List<Cuenta> cuentas) {
		this.cuentas = cuentas;
	}




	private static final long serialVersionUID = 1L;

}

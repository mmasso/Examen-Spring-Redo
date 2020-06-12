package org.elsmancs.practica.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_items")
public class Torneo {

	@Id
	@Column(name = "item_nom")
	private String nombre;

	@Column(name = "item_prop")
	private int profesionalidad;

	@Column(name = "item_tipo")
	private String tipo;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getProfesionalidad() {
		return profesionalidad;
	}

	public void setProfesionalidad(int profesionalidad) {
		this.profesionalidad = profesionalidad;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}

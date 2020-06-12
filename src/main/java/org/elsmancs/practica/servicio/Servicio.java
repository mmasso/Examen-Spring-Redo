package org.elsmancs.practica.servicio;

import java.util.List;

import org.elsmancs.practica.domain.Orden;
import org.elsmancs.practica.repositorio.Repositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class Servicio {

	@Autowired
	Repositorio repositori;

	public List<Orden> listarOrdenesUser(String nombre) {
		return repositori.listarOrdenesUser(nombre);
	}
}
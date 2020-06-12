package org.elsmancs.practica.controlador;


import org.elsmancs.practica.domain.Usuaria;
import org.elsmancs.practica.repositorio.Repositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Controlador {
	
	@Autowired
	Repositorio repositori;
    
	@RequestMapping(path="/usuaria/{nombre}")
	@ResponseBody
	public Usuaria getUsuariaDestreza(@PathVariable String nombre){
		return repositori.cargaUser(nombre);
	}	
	
	
}
package org.elsmancs.practica.controlador;


import org.elsmancs.practica.domain.Orden;
import org.elsmancs.practica.domain.Usuaria;
import org.elsmancs.practica.repositorio.Repositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@RequestMapping(path="/ordena", method=RequestMethod.POST)
	@ResponseBody
	public String afegirPersona(@RequestParam String usuaria, String item) throws Exception {
		try {
			Orden orden = repositori.ordenar(usuaria, item);
			if(orden == null) {
				throw new Exception();
			}
			return "OK";
		} catch (Exception Ko) {
			return "KO";
		}
	}

}
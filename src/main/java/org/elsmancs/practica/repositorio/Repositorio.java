package org.elsmancs.practica.repositorio;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.elsmancs.practica.domain.Orden;
import org.elsmancs.practica.domain.Torneo;
import org.elsmancs.practica.domain.Usuaria;
import org.springframework.stereotype.Repository;

@Repository
public class Repositorio {
    
	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public Usuaria cargaUser(String nom) {
		Usuaria user = em.find(Usuaria.class, nom);
		return user;
	}
	
	@Transactional
	public Torneo cargaItem(String nom) {
		Torneo torneig = em.find(Torneo.class, nom);
		return torneig;
	}
	
	@Transactional
	public Orden ordenar (String nomUser, String nomItem) throws NotEnoughProException{
		Usuaria user = em.find(Usuaria.class, nomUser);
		Torneo torneig = em.find(Torneo.class, nomItem);
		if(user!=null&torneig!=null) {
			if(user.getDestreza()<torneig.getProfesionalidad()) {
				throw new NotEnoughProException();
			}
			if(em.contains(user)&em.contains(torneig)) {
					Orden subscripcion = new Orden();
					subscripcion.setUser(user);
					subscripcion.setItem(torneig);
					em.persist(subscripcion);
					return subscripcion;
					}
		}
		return null;
		}

	public List<Orden> ordenarMultiple(String nom, List<String> torneigs) {
		Usuaria user = em.find(Usuaria.class, nom);
		List<Orden> ordenes = new ArrayList<Orden>();
		if(em.contains(user)){
			for(String nomTorneig : torneigs) {
				Torneo torneig = em.find(Torneo.class, nomTorneig);
				if(em.contains(torneig)) {
					Orden subscripcion = new Orden();
					subscripcion.setUser(user);
					subscripcion.setItem(torneig);
					em.persist(subscripcion);
					ordenes.add(subscripcion);
				}
			}
		}

		return ordenes;
	}
	
	public List<Orden> listarOrdenesUser(String nombre) {
		TypedQuery<Orden> query = em.createQuery( "select order from Orden order where order.user.nombre = :nom", Orden.class);
		query.setParameter("nom", nombre);
		List<Orden> ordersList = query.getResultList();
		return ordersList;
	}
}


	
	



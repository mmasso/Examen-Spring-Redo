
/**
 * Instrucciones:
 * 
 *  - Crea un repo privado compartido sólo con dfleta en GitHub.
 *  - Realiza un commit al pasar cada caso test.
 *  - Sin este commit tras cada caso, no corrijo el examen.
 */


package org.elsmancs.practica;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.elsmancs.practica.repositorio.NotEnoughProException;
import org.elsmancs.practica.repositorio.Repositorio;
import org.elsmancs.practica.servicio.Servicio;
import org.elsmancs.practica.controlador.Controlador;
import org.elsmancs.practica.domain.Orden;
import org.elsmancs.practica.domain.Torneo;
import org.elsmancs.practica.domain.Usuaria;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Construye una aplicacion que maneja una base de datos
 * de una liga o temporada de bolos,
 * con las personas usuarias (users) del servicio
 * y los torneos disponibles (items).
 * Las usuarias realizan subscripciones (ordenes) al servicio
 * para inscribirse en los campeonatos. 
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(statements = {
		"delete from t_ordenes",
		"delete from t_items",
		"delete from t_users",
		"insert into t_users (user_nom, user_prop) values ('Munson', 15)",
		"insert into t_users (user_nom, user_prop) values ('McCracken', 100)",
		"insert into t_items (item_nom, item_prop, item_tipo) values ('Murfreesboro Strike and Spare', 20, 'Torneo')",
		"insert into t_items (item_nom, item_prop, item_tipo) values ('Bowlerama Lanes Iowa', 7, 'Torneo')",
		"insert into t_ordenes (ord_id, ord_user, ord_item) values (1,'Munson','Bowlerama Lanes Iowa')",
})
@AutoConfigureMockMvc
public class KingPinTest {

    @PersistenceContext
	private EntityManager em;

	@Autowired(required = false)
	Repositorio repo;
	
	@Autowired(required = false)
	Servicio servicio;

	@Autowired(required = false)
	private MockMvc mockMvc;

	@Autowired(required = false)
	Controlador controlador;


	/**
	 * Tests sobre los mappings
	 * 
	 * Observa el esquema de la base de datos que espera 
	 * la aplicacion en el fichero:
	 * src/main/resources/schema.sql
	 */
	
	// Completa la definicion y el mapping
	// de la clase Torneo a la tabla t_items
	@Test
	public void test_mapping_torneo() {
		Torneo game = em.find(Torneo.class, "Bowlerama Lanes Iowa");
		assertNotNull(game);
		assertEquals("Bowlerama Lanes Iowa", game.getNombre()); //item_nom
		assertEquals(7, game.getProfesionalidad(), 0); //item_prop
		assertEquals("Torneo", game.getTipo()); //item_tipo
	}
	
	// Completa la definicion y el mapping
	// de la clase Usuaria a la tabla t_users
	@Test
	public void test_mapping_user() {
		Usuaria roy = em.find(Usuaria.class, "Munson");
		assertNotNull(roy);
		assertEquals("Munson", roy.getNombre()); //user_nom
		assertEquals(15, roy.getDestreza(), 0);  //user_prop
	}
	

    // Completa la definicion y el mapping
	// de la clase Orden (una subscripcion) a la tabla t_ordenes.
	// El id de esta clase ha de seguir una estrategia
	// Identity
	@Test 
	public void test_mapping_orden() {
		Orden inscripcion = em.find(Orden.class, 1L);
		assertNotNull(inscripcion);
		assertEquals("Munson", inscripcion.getUser().getNombre()); //ord_user
		assertEquals("Bowlerama Lanes Iowa", inscripcion.getItem().getNombre()); //ord_item
		}
	
	/**
	 * Crea una clase llamada Repositorio e indica
	 * que es un repositorio o componente de Spring 
	 */
	@Test
	public void test_repositorio_es_componente() {
		assertNotNull(repo);
	}

	/**
	 * Implementa el metodo cargaUser del repositorio
	 * que devuelve el usuario/a con el nombre indicado
	 */
	@Test
	public void test_carga_user() {
		assertNotNull(repo);
		Usuaria roy = repo.cargaUser("Munson");
		assertNotNull(roy);
		assertEquals("Munson", roy.getNombre());
		assertEquals(15, roy.getDestreza());
	}
	
	/**
	 * Implementa el metodo cargaItem del repositorio
	 * que devuelve el item (torneo) con el nombre indicado
	 */
	@Test
	public void test_carga_item() {
		assertNotNull(repo);
		Torneo item = (Torneo) repo.cargaItem("Bowlerama Lanes Iowa");
		assertNotNull(item);
		assertEquals("Bowlerama Lanes Iowa", item.getNombre());
		assertEquals(7, item.getProfesionalidad(), 0);
	}
	
	/**
     * Implementa el metodo ordenar del repositorio
	 * que permite a un usuario/a dar la orden de 
	 * subscribirse a un torneo (item).
     * El usuario/a y el item/torneo ya existen en la bbdd (NO has de crearlos).
	 * 
     * El metodo devuelve la orden de subscripcion de tipo Orden creada.
     * 
     * Guarda la orden en la base de datos.
	 * @throws NotEnoughProException 
	 */
	@Test
	@Transactional
	public void test_ordenar_ok() throws NotEnoughProException {
		assertNotNull(repo);
		Orden orden = repo.ordenar("McCracken", "Murfreesboro Strike and Spare");
		assertNotNull(orden);
		assertNotNull(orden.getId());
		assertEquals("McCracken", orden.getUser().getNombre());
		assertEquals("Murfreesboro Strike and Spare", orden.getItem().getNombre());
	}
	
	/**
     * Implementa el metodo ordenar del repositorio
	 * para que no permita generar ordenes de subscripcion a Torneos
	 * si no existe el usuario/a en la base de datos.
	 * @throws NotEnoughProException 
	 */
	@Test
	@Transactional
	public void test_ordenar_no_user() throws NotEnoughProException {
		assertNotNull(repo);
		Orden orden = repo.ordenar("Ishmael", "Murfreesboro Strike and Spare");
		assertNull(orden);
	}
	
	/**
     * Implementa el metodo ordenar del repositorio
	 * para que no permita generar ordenes de subscripcion a Torneos
	 * si no existe el Torneo en la base de datos.
	 * @throws NotEnoughProException 
	 */
	@Test
	@Transactional
	public void test_ordenar_no_torneo() throws NotEnoughProException {
		assertNotNull(repo);
		Orden orden = repo.ordenar("McCracken", "Dia de bolos con Amish");
		assertNull(orden);
	}
	
	/**
	 * Modifica el metodo ordenar para que lance una excepcion
	 * del tipo NotEnoughProException
	 * cuando la destreza del usuario/a sea menor
	 * a la profesionalidad del Torneo.
	 */
	@org.junit.Test(expected = NotEnoughProException.class)
	@Transactional
	public void test_ordenar_torneo_sin_pro() throws NotEnoughProException {
		assertNotNull(repo);
		repo.ordenar("Munson", "Murfreesboro Strike and Spare");
		Assert.fail();
	}
	
	/**
	 * Implementa el metodo ordenarMultiple para que un usuario/a
	 * pueda ordenar más de una subscripcion a un Torneo a la vez.
	 * Guarda las ordenes en la base de datos.
     * 
     * No se permiten ordenes si el usuario no existe en la base de datos.
	 * 
	 * No se ordenan items que no existen en la base de datos.
	 */
	@Test
	@Transactional
	public void test_ordenar_multiples_items() {
		assertNotNull(repo);
		List<Orden> ordenes = repo.ordenarMultiple("McCracken", Arrays.asList("Murfreesboro Strike and Spare", "Bowlerama Lanes Iowa"));
		assertNotNull(ordenes);

		assertEquals(2, ordenes.size());
		assertFalse(ordenes.contains(null));

		// no se permiten ordenes si el usuario no existe en la base de datos
		ordenes = repo.ordenarMultiple("Ishmael", Arrays.asList("Murfreesboro Strike and Spare", "Bowlerama Lanes Iowa"));
		assertTrue(ordenes.isEmpty());
		assertEquals(0, ordenes.size());

		// no se ordenan items que no existen en la base de datos
		ordenes = repo.ordenarMultiple("McCracken", Arrays.asList("Murfreesboro Strike and Spare", "Dia de bolos con Amish"));
		assertEquals(1, ordenes.size());
	}
	
	/**
	 * Implementa un Servicio con el metodo listarOrdenesUser.
	 * Lista las ordenes que ha generado una determinada Usuaria.
	 * 
	 * Has de implementar el servicio e indicar
	 * que es un componente Spring.
	 */
    @Test
    @Transactional
	public void test_listar_ordenes_user() {

        assertNotNull(repo);
        
        // Has de crear el servicio e indicar que es un componente Spring.
        assertNotNull(servicio);
        
		List<Orden> ordenes = repo.ordenarMultiple("McCracken", Arrays.asList("Murfreesboro Strike and Spare", "Bowlerama Lanes Iowa"));
		assertNotNull(ordenes);

		assertEquals(2, ordenes.size());
		assertFalse(ordenes.contains(null));

		ordenes = servicio.listarOrdenesUser("McCracken");
		assertEquals(2, ordenes.size());
		assertFalse(ordenes.contains(null));
	}
    
	/**
	 * Añade una clase controlador para hacer peticiones web
	 * a nuestra app. 
	 * Anotala para que sea un controlador de Spring.
     */
    @Test
    public void test_controlador() {
    	assertNotNull(controlador);
	}
    
	/**
     * La peticion /usuaria/<nombre>
     * ha de retornar el nombre y la destreza de la persona 
	 * indicada de la base de datos.
     */
    @Test
    public void test_get_persona() throws Exception {

		mockMvc.perform(get("/usuaria/McCracken").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(content().json("{nombre : 'McCracken', destreza: 100}"));
	}


	
} 
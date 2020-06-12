package org.elsmancs.practica.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_ordenes")
public class Orden {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ord_id")
	private Long id;

	@OneToOne
	@JoinColumn(name = "ord_user")
	private Usuaria user;

	@OneToOne
	@JoinColumn(name = "ord_item")
	private Torneo item;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuaria getUser() {
		return user;
	}

	public Torneo getItem() {
		return item;
	}

	public void setUser(Usuaria user) {
		this.user = user;
	}

	public void setItem(Torneo item) {
		this.item = item;
	}
}
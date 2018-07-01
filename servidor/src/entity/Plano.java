package entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the planos database table.
 * 
 */
@Entity
@Table(name="planos")
@NamedQuery(name="Plano.findAll", query="SELECT p FROM Plano p")
public class Plano implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PlanoPK id;

	private String enlace;

	private Integer nivel;

	//bi-directional many-to-one association to Edificio
	@ManyToOne
	@JoinColumn(name="idedificio",updatable=false, insertable=false)
	private Edificio edificio;

	public Plano() {
	}

	public PlanoPK getId() {
		return this.id;
	}

	public void setId(PlanoPK id) {
		this.id = id;
	}

	public String getEnlace() {
		return this.enlace;
	}

	public void setEnlace(String enlace) {
		this.enlace = enlace;
	}


	public Integer getNivel() {
		return this.nivel;
	}

	public void setNivel(Integer nivel) {
		this.nivel = nivel;
	}

	public Edificio getEdificio() {
		return this.edificio;
	}

	public void setEdificio(Edificio edificio) {
		this.edificio = edificio;
	}

}
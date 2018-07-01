package entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the planos database table.
 * 
 */

public class PlanoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	
	private String idedificio;

	private String idplano;

	public PlanoPK() {
	}
	public String getIdedificio() {
		return this.idedificio;
	}
	public void setIdedificio(String idedificio) {
		this.idedificio = idedificio;
	}
	public String getIdplano() {
		return this.idplano;
	}
	public void setIdplano(String idplano) {
		this.idplano = idplano;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof PlanoPK)) {
			return false;
		}
		PlanoPK castOther = (PlanoPK)other;
		return 
			this.idedificio.equals(castOther.idedificio)
			&& this.idplano.equals(castOther.idplano);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idedificio.hashCode();
		hash = hash * prime + this.idplano.hashCode();
		
		return hash;
	}
}
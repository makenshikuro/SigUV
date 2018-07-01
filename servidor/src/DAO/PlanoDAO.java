package DAO;

import java.util.List;

import entity.Plano;

public interface PlanoDAO {
		public Plano getPlanoById(int id);
	    public List<Plano> getPlano();
	    public void createPlano(Plano p);
	    public void deletePlano(Plano p);
}

package DAO;

import java.util.List;

import entity.Coordenada;

public interface CoordenadaDAO {
	   public Coordenada getCoordenadaById(String id);
	   public List<Coordenada> getCoordenadas();
	   public void createCoordenada(Coordenada c);
	   public void deleteCoordenada(Coordenada c);
}

package DAO;

import java.util.List;

import entity.Panorama;

public interface PanoramaDAO {
	public Panorama getPanoramaById(int id);
    public List<Panorama> getPanorama();
    public void createPanorama(Panorama p);
    public void deletePanorama(Panorama p);
}

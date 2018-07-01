package DAO;

public abstract class DAOFactory {
	
	public enum TYPE {JPA,XML}
	
	//Definimos un getDAO para cada entidad
	public abstract CoordenadaDAO getCoordenadaDAO();
	public abstract EdificioDAO getEdificioDAO();
	public abstract EspacioDAO getEspacioDAO();
	public abstract PlanoDAO getPlanoDAO();
	public abstract PanoramaDAO getPanoramaDAO();
	public abstract ProfesorDAO getProfesorDAO();
	public abstract UsuarioDAO getUsuarioDAO();

	public static DAOFactory getDAOFactory(TYPE t){
		switch(t){
			case JPA:
				return new JPADAOFactory();
			
		}
		return null;
	}

}

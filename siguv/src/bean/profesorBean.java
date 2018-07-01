package bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.DualListModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import entity.Espacio;
import entity.Profesor;
import serializer.ProfesorSerializer;

@ManagedBean
@SessionScoped
public class profesorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB(lookup = "java:global/siguv/EJBean!bean.EJBean")
	private EJBean ejb;

	@Inject
	private UserMB userMB;

//	private String idprofesor;
//	private String nombre;
//	private boolean visibilidad = true;

	private Profesor profesor;
	private Profesor selectedProfesor;

	protected List<Espacio> espacios = new ArrayList<Espacio>();
	protected List<Profesor> profesores = new ArrayList<Profesor>();
	protected List<Profesor> filtroProfesores = new ArrayList<Profesor>();

	List<String> espaciosSource = new ArrayList<String>();
	List<String> espaciosTarget = new ArrayList<String>();
	private DualListModel<String> pickListEspacios = new DualListModel<String>(espaciosSource, espaciosTarget);;
	
	public String consultaProfesores() {
		String nav;
		profesores = ejb.getListaProfesores();
		nav = "profesores/profesores.xhtml?faces-redirect=true";
		return nav;
	}
	
	public String crearProfesor() {
		String nav = "nuevoProfesor.xhtml?faces-redirect=true";

		espaciosSource = ejb.getNombresEspacios();
		espaciosTarget = new ArrayList<String>();

		pickListEspacios = new DualListModel<String>(espaciosSource, espaciosTarget);

		profesor = new Profesor();
		return nav;
		
	}
	
	
	
	public String nuevoProfesor() {
		
		String nav;
		
		List<Espacio> le = new ArrayList<Espacio>();
		for (String id : pickListEspacios.getTarget()) {
			Espacio espacio = new Espacio();
			espacio.setIdespacio(id);
			le.add(espacio);		
		}
		profesor.setEspacios(le);
		ejb.createProfesor(profesor);
		
		profesores = ejb.getListaProfesores();
		
		cleanProfesor();

		nav = "/admin/profesores/profesores.xhtml?faces-redirect=true";

		return nav;
		
//		String nav;
//		generarProfesor();
//
//		Gson gson = new GsonBuilder().registerTypeAdapter(Profesor.class, new ProfesorSerializer()).setPrettyPrinting()
//				.create();
//		String arrayToJson = gson.toJson(profesor);
//
//		ejb.createProfesor(profesor);
//
//		profesores = ejb.getListaProfesores();
//
//		cleanProfesor();
//
//		nav = "/admin/profesores/profesores.xhtml?faces-redirect=true";
//
//		return nav;
	}

	

	public String actualizaProfesor() {
		String nav;
		List<Espacio> le = new ArrayList<Espacio>();
		for (String id : pickListEspacios.getTarget()) {
			Espacio espacio = new Espacio();
			espacio.setIdespacio(id);
			le.add(espacio);		
		}
		profesor.setEspacios(le);
		
		ejb.updateProfesor(getProfesor());
		
		profesores = ejb.getListaProfesores();

		cleanProfesor();

		nav = "/admin/profesores/profesores.xhtml?faces-redirect=true";

		return nav;
//		String nav;
//		generarProfesor();
//
//		Gson gson = new GsonBuilder().registerTypeAdapter(Profesor.class, new ProfesorSerializer()).setPrettyPrinting()
//				.create();
//		String arrayToJson = gson.toJson(profesor);
//
//		ejb.updateProfesor(profesor);
//
//		profesores = ejb.getListaProfesores();
//
//		cleanProfesor();
//
//		nav = "/admin/profesores/profesores.xhtml?faces-redirect=true";
//
//		return nav;
	}

	public String editProfesor(String id) {
		
		String nav;
		nav = "/admin/profesores/editarProfesor.xhtml?faces-redirect=true";
		setProfesor(ejb.getProfesor(id));
		
		

		espaciosSource = ejb.getNombresEspacios();
		espaciosTarget = new ArrayList<String>();
		
		for (Espacio e : getProfesor().getEspacios()) {

			espaciosSource.remove(e.getIdespacio());
			espaciosTarget.add(e.getIdespacio());

		}
		pickListEspacios = new DualListModel<String>(espaciosSource, espaciosTarget);

		return nav;
		
		
//		String nav;
//
//		
//		nav = "/admin/profesores/editarProfesor.xhtml?faces-redirect=true";
//		profesor = ejb.getProfesor(id);
//		idprofesor = profesor.getIdprofesor();
//		nombre = profesor.getUsuario();
//		visibilidad = profesor.getVisibilidad();
//
//		espaciosSource = ejb.getNombresEspacios();
//		for (Espacio e : profesor.getEspacios()) {
//
//			espaciosSource.remove(e.getIdespacio());
//			espaciosTarget.add(e.getIdespacio());
//
//		}
//		pickListEspacios = new DualListModel<String>(espaciosSource, espaciosTarget);
//
//		return nav;
	}

	public void eliminaProfesor(String id) {
		
		ejb.removeProfesor(id);

		profesores = ejb.getListaProfesores();
		
		try {reload();
		 } catch (IOException e) {e.printStackTrace();
		 }


	}
	public void reload() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

	private void cleanProfesor() {
		profesor = null;
	}

//	private void generarProfesor() {
//		profesor = new Profesor();
//
//		profesor.setIdprofesor(idprofesor);
//		profesor.setUsuario(nombre);
//		profesor.setVisibilidad(visibilidad);
//
//		List<Espacio> le = new ArrayList<Espacio>();
//		// System.out.println("Espacio ID: "+ pickListEspacios.getTarget().size());
//		for (String id : pickListEspacios.getTarget()) {
//
//			// System.out.println("ID: "+ id);
//			Espacio espacio = new Espacio();
//			espacio.setIdespacio(id);
//			le.add(espacio);
//			// System.out.println("Success EEspacio: "+ esp.getIdespacio());
//
//		}
//		profesor.setEspacios(le);
//
//	}

	public String volver() {
		cleanProfesor();
		return "/admin/profesores/profesores.xhtml?faces-redirect=true";
	}

	public String volverIndex() {
		cleanProfesor();
		return "/admin/index.xhtml?faces-redirect=true";
	}

	public String consultaEspacios() {

		// this.usuarios = ejb.getListaUsuarios();
		// System.out.println("usu"+ejb.getListaUsuarios());
		String ok;

		espacios = ejb.getListaEspacios();
		// if (edificios.isEmpty()||edificios==null) {
		// ok = "index";
		// }else {
		ok = "profesores/profesores.xhtml?faces-redirect=true";
		// }

		return ok;
	}

	

	public List<Espacio> getEspacios() {
		return espacios;
	}

	public void setEspacios(List<Espacio> espacios) {
		this.espacios = espacios;
	}

//	public String getIdprofesor() {
//		return idprofesor;
//	}
//
//	public void setIdprofesor(String idprofesor) {
//		this.idprofesor = idprofesor;
//	}
//
//	public String getNombre() {
//		return nombre;
//	}
//
//	public void setNombre(String nombre) {
//		this.nombre = nombre;
//	}
//
//	public boolean isVisibilidad() {
//		return visibilidad;
//	}
//
//	public void setVisibilidad(boolean visibilidad) {
//		this.visibilidad = visibilidad;
//	}

	public List<Profesor> getProfesores() {
		return profesores;
	}

	public void setProfesores(List<Profesor> profesores) {
		this.profesores = profesores;
	}

	public List<Profesor> getFiltroProfesores() {
		return filtroProfesores;
	}

	public void setFiltroProfesores(List<Profesor> filtroProfesores) {
		this.filtroProfesores = filtroProfesores;
	}

	public DualListModel<String> getPickListEspacios() {
		return pickListEspacios;
	}

	public void setPickListEspacios(DualListModel<String> pickListEspacios) {
		this.pickListEspacios = pickListEspacios;
	}

	public List<String> getEspaciosSource() {
		return espaciosSource;
	}

	public void setEspaciosSource(List<String> espaciosSource) {
		this.espaciosSource = espaciosSource;
	}

	public List<String> getEspaciosTarget() {
		return espaciosTarget;
	}

	public void setEspaciosTarget(List<String> espaciosTarget) {
		this.espaciosTarget = espaciosTarget;
	}

	public Profesor getProfesor() {
		return profesor;
	}

	public void setProfesor(Profesor profesor) {
		this.profesor = profesor;
	}

	public Profesor getSelectedProfesor() {
		return selectedProfesor;
	}

	public void setSelectedProfesor(Profesor selectedProfesor) {
		this.selectedProfesor = selectedProfesor;
	}

//	public void addMessage() {
//		String summary = visibilidad ? "Checked" : "Unchecked";
//		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
//	}

//	public List<String> completeText(String query) {
//		List<String> results = new ArrayList<String>();
//		for (int i = 0; i < 10; i++) {
//			results.add(query + i);
//		}
//
//		return results;
//	}
}

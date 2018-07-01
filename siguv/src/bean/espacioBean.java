package bean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DualListModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import entity.Coordenada;
import entity.Edificio;
import entity.Espacio;
import entity.Imagen;
import entity.Panorama;
import entity.Profesor;
import serializer.EspacioSerializer;

//@ManagedBean
//@SessionScoped
@Named
@SessionScoped
public class espacioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@javax.ejb.EJB(lookup = "java:global/siguv/EJBean!bean.EJBean")
	private EJBean ejb;


	protected Espacio espacio;
	protected Edificio edificio;

	protected List<Espacio> espacios = new ArrayList<Espacio>();

	protected String idespacio;
	protected String codEspacio;
	protected String boundingbox;


	protected String localizacion;
	protected String polygon;
	protected Coordenada coordenada;
	protected List<Imagen> panos = new ArrayList<Imagen>();
	int numPano;

	String path = "D:\\wamp64\\www\\panoramas\\";
	// String path = "\\var\\www\\html\\panoramas\\";
	String nav = "basic";

	protected Espacio selectedEspacio;
	protected List<Espacio> filtroEspacio = new ArrayList<Espacio>();


	List<Profesor> profesores = new ArrayList<Profesor>();
	List<String> profesorSource = new ArrayList<String>();
	List<String> profesorTarget = new ArrayList<String>();
	private DualListModel<String> pickListProfesor = new DualListModel<String>(profesorSource, profesorTarget);

	protected String testMap;


	public String consultaEspacios(String idedificio) throws IOException {
		String nav;
		edificio = ejb.getEdificio(idedificio);
		espacios = ejb.getEspaciosFromEdificio(edificio.getIdedificio());
		nav = "/admin/espacios/espacios.xhtml?faces-redirect=true";
		return nav;
	}
	public String crearEspacio() {
		String navegacion;
		numPano = 1;
		navegacion = "nuevoEspacio.xhtml?faces-redirect=true";
		nav = "basic";

		profesorSource = ejb.getNombresProfesores();
		profesorTarget = new ArrayList<String>();
		pickListProfesor = new DualListModel<String>(profesorSource, profesorTarget);
		
		espacio = new Espacio();
		panos = new ArrayList<Imagen>();
		
		return navegacion;
	}

	public String nuevoEspacio() {
		String navegacion;
		generarEspacio(true);

		Gson gson = new GsonBuilder().registerTypeAdapter(Espacio.class, new EspacioSerializer()).setPrettyPrinting()
				.create();
		String arrayToJson = gson.toJson(getEspacio());
		try {
			ejb.createEspacio(panos, path, arrayToJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
		cleanEspacio();
		espacios = ejb.getEspaciosFromEdificio(edificio.getIdedificio());

		navegacion = "/admin/espacios/espacios.xhtml?faces-redirect=true";
		return navegacion;
	}
	
	public String actualizaEspacio() {
		String navegacion;
		generarEspacio(false);

		Gson gson = new GsonBuilder().registerTypeAdapter(Espacio.class, new EspacioSerializer()).setPrettyPrinting()
				.create();
		String arrayToJson = gson.toJson(getEspacio());
		try {
			ejb.updateEspacio(panos, path, arrayToJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
		cleanEspacio();
		espacios = ejb.getEspaciosFromEdificio(edificio.getIdedificio());

		navegacion = "/admin/espacios/espacios.xhtml?faces-redirect=true";
		return navegacion;
	}

	public String editarEspacio(String id) {
		String navegacion;
		nav = "basic";
		setEspacio(ejb.getEspacio(id));
		profesorSource = ejb.getNombresProfesores();
		profesorTarget = new ArrayList<String>();
		
		for (Profesor p : espacio.getProfesores()) {
			profesorSource.remove(p.getIdprofesor());
			profesorTarget.add(p.getIdprofesor());
		}

		pickListProfesor = new DualListModel<String>(profesorSource, profesorTarget);
		
		navegacion = "/admin/espacios/editarEspacio.xhtml?faces-redirect=true";
		return navegacion;
	}

	public void saveFile(FileUploadEvent event) {

		System.out.println("save photo");

		try (InputStream input = event.getFile().getInputstream()) {

			Imagen imagen = new Imagen();
			imagen.setInputStream(input);
			int dot = event.getFile().getFileName().lastIndexOf(".");

			String extension = event.getFile().getFileName().substring(dot + 1);
			// System.out.println("save photo1" + edificio.getIdedificio());
			System.out.println("save photo2" + idespacio);
			System.out.println("save phot3" + numPano);
			// nombre = edificio.getIdedificio() + "-" + idespacio;

			// String nombre = edificio.getIdedificio() + "-" + getIdespacio() + "_" +
			// numPano;

			imagen.setNombre(edificio.getIdedificio() + "-" + getIdespacio() + "_" + numPano);
			imagen.setNivel(numPano);
			imagen.setExtension(extension);

			int size = (int) (event.getFile().getSize() / 1000);
			imagen.setSize(size);

			imagen.setTipo(event.getFile().getContentType());

			panos.add(imagen);

			numPano++;


			String ruta = path; // +"\\" sumar el nombre + ext

			File file = new File(ruta);

			if (!file.exists()) {
				if (file.mkdir()) {
					System.out.println("Directory is created!");
					Files.copy(input, new File(ruta, imagen.getNombre() + "." + extension).toPath(),
							StandardCopyOption.REPLACE_EXISTING);
				} else {
					System.out.println("Failed to create directory!");
				}
			} else {

				Files.copy(input, new File(ruta, imagen.getNombre() + "." + extension).toPath(),
						StandardCopyOption.REPLACE_EXISTING);
			}

			// setTestMap("temp\\" + userMB.getUser().getUsuario()+"\\"+nombre+ "." +
			// extension);

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("save Pano " + numPano);
		nav = "pano";

		try {
			reload();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void handleKeyEvent() {

		idespacio = "B" + espacio.getBloque() + "P" + espacio.getPiso() + "E" + codEspacio;
		espacio.setIdespacio(edificio.getIdedificio() + "-" + idespacio);
	}


	private void cleanEspacio() {
		espacio = null;
		setCodEspacio("");
		setLocalizacion("");
		setBoundingbox("");
		setCoordenada(null);
		setIdespacio("");
	}

	public void removeFile(int pos) {

		panos.remove(pos);
	}

	public void next(int pos) {
		if (pos == 0) {
			nav = "basic";
		}
		if (pos == 1) {
			nav = "loc";
		}
		if (pos == 2) {
			nav = "pano";
		}
		if (pos == 3) {
			nav = "profesor";

		}
	}

	private void generarEspacio(boolean create) {

		espacio.setEdificio(edificio);
		if (create) {
			espacio.setIdespacio(edificio.getIdedificio() + "-" + getIdespacio());
		}
		coordenada = new Coordenada();
		coordenada.setIdcoordenada(espacio.getIdespacio());
		coordenada.setDescripcion("Coordenadas de " + espacio.getNombrees());
		coordenada.setLatitud(Float.valueOf(localizacion.split(",")[0]));
		coordenada.setLongitud(Float.valueOf(localizacion.split(",")[1]));

		espacio.setCoordenada(coordenada);

		List<Panorama> imagenes = new ArrayList<Panorama>();
		for (Imagen p : panos) {
			Panorama pano = new Panorama();
			pano.setEnlace(p.getNombre() + p.getExtension());
			pano.setEspacio(espacio);
			pano.setIdpanorama(espacio.getIdespacio() + "_" + p.getNivel());
			
			Espacio aux = new Espacio();
			aux.setIdespacio(getIdespacio());
			pano.setEspacio(aux);
			pano.setPanorama(p.getNivel());
			imagenes.add(pano);
		}

		espacio.setPanoramas(imagenes);

		List<Profesor> le = new ArrayList<Profesor>();
		for (String id : pickListProfesor.getTarget()) {
			Profesor p = new Profesor();
			p.setIdprofesor(id);
			le.add(p);
		}
		espacio.setProfesores(le);
	}


	public void eliminaEspacio(String id) {
		ejb.removeEspacio(id);
		espacios = ejb.getEspaciosFromEdificio(edificio.getIdedificio());
		try {
			reload();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Espacio getEspacio() {
		return espacio;
	}

	public void setEspacio(Espacio e) {

		espacio = e;
		setCoordenada(e.getCoordenada());
		setLocalizacion(e.getCoordenada().getLatitud() + "," + e.getCoordenada().getLongitud());
		setCodEspacio(getEspacio().getIdespacio().substring(getEspacio().getIdespacio().lastIndexOf("E") + 1));
		setTestMap("planos\\" + e.getEdificio().getIdedificio() + e.getPiso() + "es");

		List<Imagen> imagenes = new ArrayList<Imagen>();
		for (Panorama pano : espacio.getPanoramas()) {
			Imagen img = new Imagen();
			img.setNombre(pano.getIdpanorama());
			img.setExtension(pano.getEnlace().substring(pano.getEnlace().lastIndexOf(".") + 1));
			imagenes.add(img);
		}

		setPanos(imagenes);
		setProfesores(e.getProfesores());
	}

	// public String onRowSelect(SelectEvent event) {
	//
	// setSelectedEspacio((Espacio) event.getObject());
	// return editarEspacio(selectedEspacio.getIdespacio());
	// }


	public List<String> getProfesorSource() {
		return profesorSource;
	}

	public void setProfesorSource(List<String> profesorSource) {
		this.profesorSource = profesorSource;
	}

	public List<String> getProfesorTarget() {
		return profesorTarget;
	}

	public void setProfesorTarget(List<String> profesorTarget) {
		this.profesorTarget = profesorTarget;
	}

	public DualListModel<String> getPickListProfesor() {
		return pickListProfesor;
	}

	public void setPickListProfesor(DualListModel<String> pickListProfesor) {
		this.pickListProfesor = pickListProfesor;
	}

	public Espacio getSelectedEspacio() {
		return selectedEspacio;
	}

	public void setSelectedEspacio(Espacio selectedEspacio) {
		this.selectedEspacio = selectedEspacio;
	}

	public List<Espacio> getFiltroEspacio() {
		return filtroEspacio;
	}

	public void setFiltroEspacio(List<Espacio> filtroEspacio) {
		this.filtroEspacio = filtroEspacio;
	}

	public String volverEdificios() throws IOException {
		cleanEspacio();
		return "/admin/edificios/edificios.xhtml?faces-redirect=true";
	}

	public String volverIndex() throws IOException {
		cleanEspacio();
		return "/admin/index.xhtml?faces-redirect=true";
	}

	public String volver() throws IOException {
		cleanEspacio();
		return "/admin/espacios/espacios.xhtml?faces-redirect=true";
	}

	public void reload() throws IOException {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}

	public List<Espacio> getEspacios() {
		return espacios;
	}

	public void setEspacios(List<Espacio> espacios) {
		this.espacios = espacios;
	}

	public Edificio getEdificio() {
		return edificio;
	}

	public void setEdificio(Edificio edificio) {
		this.edificio = edificio;
	}

	public String getIdespacio() {
		return idespacio;
	}

	public void setIdespacio(String idespacio) {
		this.idespacio = idespacio;
	}

	public String getBoundingbox() {
		return boundingbox;
	}

	public void setBoundingbox(String boundingbox) {
		this.boundingbox = boundingbox;
	}

	public String getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(String localizacion) {
		this.localizacion = localizacion;
	}

	public Coordenada getCoordenada() {
		return coordenada;
	}

	public void setCoordenada(Coordenada coordenada) {
		this.coordenada = coordenada;
	}

	public String getTestMap() {
		return testMap;
	}

	public void setTestMap(String testMap) {
		this.testMap = testMap;
	}

	public String getPolygon() {
		return polygon;
	}

	public void setPolygon(String polygon) {
		this.polygon = polygon;
	}

	public List<Imagen> getPanos() {
		return panos;
	}

	public void setPanos(List<Imagen> panos) {
		this.panos = panos;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getNav() {
		return nav;
	}

	public void setNav(String nav) {
		this.nav = nav;
	}

	public int getNumPano() {
		return numPano;
	}

	public void setNumPano(int numPano) {
		this.numPano = numPano;
	}

	public String getCodEspacio() {
		return codEspacio;
	}

	public void setCodEspacio(String codEspacio) {
		this.codEspacio = codEspacio;
	}

	public List<Profesor> getProfesores() {
		return profesores;
	}

	public void setProfesores(List<Profesor> profesores) {
		this.profesores = profesores;
	}
	

}

package bean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.UploadedFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import entity.Coordenada;
import entity.Edificio;
import entity.Imagen;
import entity.Plano;
import entity.PlanoPK;
import entity.Usuario;
import serializer.EdificioSerializer;

/**
 * Session Bean implementation class DateFlowBean
 */
// @ManagedBean
// @SessionScoped
@Named
@SessionScoped
public class edificioBean implements Serializable {

	/**
	
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// @EJB
	@EJB(lookup = "java:global/siguv/EJBean!bean.EJBean")
	private EJBean ejb;

	@Inject
	private UserMB userMB;
	protected Coordenada coordenada;
	protected String denominacion = "co";
	protected String nivel;
	protected String localizacion;

	protected Edificio edificio;
	String path = "D:\\wamp64\\www\\planos\\";

	private static final String EDIFICIOS_NAV = "edificios/edificios.xhtml?faces-redirect=true";
	private static final String BASIC = "basic";

	protected Edificio selectedEdificio;
	private List<Edificio> filtroEdificio = new ArrayList<>();

	private List<Edificio> edificios = new ArrayList<>();
	private List<Imagen> planos = new ArrayList<>();


	protected String testMap;
	protected String nav = "basic";

	private List<String> usuariosSource = new ArrayList<>();
	private List<String> usuariosTarget = new ArrayList<>();
	private DualListModel<String> pickListUsuario = new DualListModel<>(usuariosSource, usuariosTarget);




	public String consultaEdificios() {
		String navegacion;
		if (userMB.isUserAdmin()) {
			edificios = ejb.getListaEdificios();
		} else {
			edificios = ejb.getListaEdificiosEditor(userMB.getUser().getUsuario());
		}
		navegacion = EDIFICIOS_NAV;
		return navegacion;
	}

	public String crearEdificio() {
		String navegacion;
		navegacion = "nuevoEdificio.xhtml?faces-redirect=true";
		nav = BASIC;

		usuariosSource = ejb.getNombresUsuarios();
		usuariosTarget = new ArrayList<>();
		pickListUsuario = new DualListModel<>(usuariosSource, usuariosTarget);

		edificio = new Edificio();
		planos = new ArrayList<>();
		System.out.println("Crear!");

		return navegacion;
	}

	public void eliminaEdificio(String id) {
		ejb.removeEdificio(id);
		edificios = ejb.getListaEdificios();
		System.out.println("Deleted!");
		try {
			reload();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String nuevoEdificio() {
		String nav;

		generarEdificio();

		Gson gson = new GsonBuilder().registerTypeAdapter(Edificio.class, new EdificioSerializer()).setPrettyPrinting()
				.create();
		String arrayToJson = gson.toJson(edificio);

		try {
			ejb.createEdificio(planos, path, arrayToJson);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Created!");


		nav = EDIFICIOS_NAV;

		edificios = ejb.getListaEdificios();
		return nav;
	}


	public String editarEdificio(String id) {
		String navegacion;

		nav = BASIC;
		navegacion = "/admin/edificios/editarEdificio.xhtml?faces-redirect=true";

		setEdificio(ejb.getEdificio(id));
		
		usuariosSource = ejb.getNombresEdificios();
		usuariosTarget = new ArrayList<>();

		for (Usuario u : getEdificio().getUsuarios()) {

			usuariosSource.remove(u.getUsuario());
			usuariosTarget.add(u.getUsuario());
		}

		System.out.println("Editar!");

		pickListUsuario = new DualListModel<>(usuariosSource, usuariosTarget);

		return navegacion;
	}

	public String actualizaEdificio() {

		String nav;

		generarEdificio();

		Gson gson = new GsonBuilder().registerTypeAdapter(Edificio.class, new EdificioSerializer()).setPrettyPrinting()
				.create();
		String arrayToJson = gson.toJson(edificio);

		try {
			ejb.updateEdificio(planos, path, arrayToJson);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("updated!");

		nav = EDIFICIOS_NAV;

		edificios = ejb.getListaEdificios();
		return nav;
	}

	private void cleanEdificio()
	{
		edificio = null;
		setLocalizacion("");
	}

	public String volverIndex() {

		cleanEdificio();
		return "/admin/index.xhtml?faces-redirect=true";
	}

	public String volver() {
		cleanEdificio();
		return EDIFICIOS_NAV;
	}

	public void reload() throws IOException {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}


	public void next(int pos) {
		if (pos == 0) {
			nav = BASIC;
		}
		if (pos == 1) {
			nav = "plano";

		}
		if (pos == 2) {
			nav = "loc";
		}
		if (pos == 3) {
			nav = "usuario";
			muestraUsuarios();
		}
	}

	public void muestraUsuarios() {

		usuariosSource = ejb.getNombresUsuarios();

		pickListUsuario.setSource(usuariosSource);
		pickListUsuario.setTarget(usuariosTarget);

	}



	public void setEdificio(Edificio e) {
		edificio = e;
		setCoordenada(e.getCoordenada());
		setLocalizacion(e.getCoordenada().getLatitud() + "," + e.getCoordenada().getLongitud());
		List<Imagen> imagenes = new ArrayList<>();
		for (Plano plano : e.getPlanos()) {
			Imagen img = new Imagen();

			setTestMap("planos\\" + plano.getEnlace());

			img.setNivel(plano.getNivel());
			img.setNombre(plano.getId().getIdplano());
			img.setExtension(plano.getEnlace().substring(plano.getEnlace().lastIndexOf('.') + 1));
			imagenes.add(img);
		}
		setPlanos(imagenes);
	}

	private void generarEdificio() {

		coordenada = new Coordenada();
		coordenada.setIdcoordenada(getEdificio().getIdedificio());
		coordenada.setDescripcion("Coordenadas de " + getEdificio().getNombrees());
		coordenada.setLatitud(Float.valueOf(localizacion.split(",")[0]));
		coordenada.setLongitud(Float.valueOf(localizacion.split(",")[1]));

		edificio.setCoordenada(coordenada);

		List<Plano> imagenes = new ArrayList<>();
		for (Imagen p : planos) {

			Plano plano = new Plano();

			plano.setEnlace(p.getNombre() + p.getExtension());
			plano.setEdificio(edificio);
			plano.setNivel(p.getNivel());

			PlanoPK planoPK = new PlanoPK();
			planoPK.setIdedificio(edificio.getIdedificio());
			planoPK.setIdplano(p.getNombre());
			plano.setId(planoPK);

			imagenes.add(plano);

		}
		edificio.setPlanos(imagenes);

		List<Usuario> le = new ArrayList<>();
		for (String id : pickListUsuario.getTarget()) {

			Usuario user = new Usuario();
			user.setUsuario(id);

			le.add(user);

		}

		edificio.setUsuarios(le);

	}

	public void handleKeyEvent() {
		System.out.println("handel: " + nivel);
	}

	public void handleDenChange() {
		System.out.println("handel: " + denominacion);
	}

	public void saveFile(FileUploadEvent event) {

		try (InputStream input = event.getFile().getInputstream()) {

			Imagen imagen = new Imagen();
			imagen.setInputStream(input);

			int dot = event.getFile().getFileName().lastIndexOf('.');

			String extension = event.getFile().getFileName().substring(dot + 1);

			String nombre = edificio.getIdedificio() + nivel + denominacion;
			imagen.setNombre(nombre);
			imagen.setExtension(extension);

			int size = (int) (event.getFile().getSize() / 1000);
			imagen.setSize(size);
			imagen.setDenominacion(denominacion);
			imagen.setNivel(Integer.valueOf(getNivel()));

			imagen.setTipo(event.getFile().getContentType());

			planos.add(imagen);

			String ruta = path;

			File file = new File(ruta);

			if (!file.exists()) {
				if (file.mkdir()) {
					
					Files.copy(input, new File(ruta, nombre + "." + extension).toPath(),
							StandardCopyOption.REPLACE_EXISTING);
				} else {
					System.out.println("Failed to create directory!");
				}
			} else {
				Files.copy(input, new File(ruta, nombre + "." + extension).toPath(),
						StandardCopyOption.REPLACE_EXISTING);
			}

			setTestMap("planos\\" + nombre + "." + extension);


		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("saveFile EFlow Fin");
		try {
			reload();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeFile(int pos) {

		planos.remove(pos);
	}

	public Edificio getSelectedEdificio() {
		return selectedEdificio;
	}

	public void setSelectedEdificio(Edificio selectedEdificio) {
		this.selectedEdificio = selectedEdificio;
	}

	public List<Edificio> getFiltroEdificio() {
		return filtroEdificio;
	}

	public void setFiltroEdificio(List<Edificio> filtroEdificio) {
		this.filtroEdificio = filtroEdificio;
	}

	public String getNav() {
		return nav;
	}

	public void setNav(String nav) {
		this.nav = nav;
	}

	public String getTestMap() {
		return testMap;
	}

	public void setTestMap(String testMap) {
		this.testMap = testMap;
	}

	public Coordenada getCoordenada() {
		return coordenada;
	}

	public void setCoordenada(Coordenada coordenada) {
		this.coordenada = coordenada;
	}

	public List<Edificio> getEdificios() {
		return edificios;
	}

	public void setEdificios(List<Edificio> edificios) {
		this.edificios = edificios;
	}

	public String getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(String localizacion) {
		this.localizacion = localizacion;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<Imagen> getPlanos() {
		return planos;
	}

	public void setPlanos(List<Imagen> planos) {
		this.planos = planos;
	}

	public String getDenominacion() {
		return denominacion;
	}

	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	public String getNivel() {
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	public Edificio getEdificio() {
		return edificio;
	}

	public DualListModel<String> getPickListUsuario() {
		return pickListUsuario;
	}

	public void setPickListUsuario(DualListModel<String> pickListUsuario) {
		this.pickListUsuario = pickListUsuario;
	}

	public List<String> getUsuariosSource() {
		return usuariosSource;
	}

	public void setUsuariosSource(List<String> usuariosSource) {
		this.usuariosSource = usuariosSource;
	}

	public List<String> getUsuariosTarget() {
		return usuariosTarget;
	}

	public void setUsuariosTarget(List<String> usuariosTarget) {
		this.usuariosTarget = usuariosTarget;
	}



}

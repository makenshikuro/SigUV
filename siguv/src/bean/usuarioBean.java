package bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.DualListModel;

import entity.Edificio;
import entity.Usuario;

/**
 * Session Bean implementation class DateFlowBean
 */
@ManagedBean
@SessionScoped
public class usuarioBean implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB(lookup="java:global/siguv/EJBean!bean.EJBean")
	private EJBean ejb;

//    protected String usuario;
//    protected String nombre;
//    protected String rol;
//    protected String pass;
//    protected String uuid;
//    protected String email;
	protected Usuario user;
    protected Usuario selectedUsuario;
    
    protected List<Edificio> edificios = new ArrayList<Edificio>();
    protected List<Usuario> usuarios = new ArrayList<Usuario>();
	protected List<Usuario> filtroUsuarios = new ArrayList<Usuario>();
    
    List<String> edificiosSource = new ArrayList<String>();
    List<String> edificiosTarget = new ArrayList<String>();
	private DualListModel<String> pickListEdificios = new DualListModel<String>(edificiosSource, edificiosTarget);
	
	public String consultaUsuarios() {
		String nav;

		usuarios = ejb.getListaUsuarios();
		nav = "usuarios/usuarios.xhtml?faces-redirect=true";

		edificios = ejb.getListaEdificios();

		return nav;
	}

	public String crearUsuario() {
		String nav;
		nav = "/admin/usuarios/nuevoUsuario.xhtml?faces-redirect=true";

		edificiosSource = ejb.getNombresEdificios();
		edificiosTarget = new ArrayList<String>();

		pickListEdificios = new DualListModel<String>(edificiosSource, edificiosTarget);

		user = new Usuario();
		return nav;
	}

	public String nuevoUsuario() {

		List<Edificio> le = new ArrayList<Edificio>();

		for (String id : pickListEdificios.getTarget()) {
			Edificio edificio = new Edificio();
			edificio.setIdedificio(id);
			le.add(edificio);

		}
		user.setEdificios(le);

		ejb.createUsuario(user);

		usuarios = ejb.getListaUsuarios();

		clearUsuario();

		return "/admin/usuarios/usuarios.xhtml?faces-redirect=true";
	}

	public String editarUsuario(String id) {
		String nav;

		nav = "/admin/usuarios/editarUsuario.xhtml?faces-redirect=true";

		setUser(ejb.getUsuario(id));

		edificiosSource = ejb.getNombresEdificios();
		edificiosTarget = new ArrayList<>();
		for (Edificio e : getUser().getEdificios()) {

			edificiosSource.remove(e.getIdedificio());
			edificiosTarget.add(e.getIdedificio());

		}

		pickListEdificios = new DualListModel<>(edificiosSource, edificiosTarget);
		return nav;
	}

	public String actualizaUsuario() {

		List<Edificio> le = new ArrayList<Edificio>();

		for (String id : pickListEdificios.getTarget()) {

			Edificio edificio = new Edificio();
			edificio.setIdedificio(id);

			le.add(edificio);

		}
		user.setEdificios(le);

		ejb.updateUsuario(getUser());

		usuarios = ejb.getListaUsuarios();

		clearUsuario();
		return "/admin/usuarios/usuarios.xhtml?faces-redirect=true";
	}


	public void eliminaUsuario(String id) {
		 ejb.removeUsuario(id);
		 usuarios = ejb.getListaUsuarios();
		
		 try {reload();
		 } catch (IOException e) {e.printStackTrace();
		 }
	}

	private void clearUsuario() {
		user = null;
	}

	public String volverIndex() {

		return "/admin/index.xhtml?faces-redirect=true";
	}

	public String volver() {

		return "/admin/usuarios/usuarios.xhtml?faces-redirect=true";
	}
    

    public void reload() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

	public List<Usuario> getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public List<Edificio> getEdificios() {
		return edificios;
	}
	public void setEdificios(List<Edificio> edificios) {
		this.edificios = edificios;
	}
	public DualListModel<String> getPickListEdificios() {
		return pickListEdificios;
	}
	public void setPickListEdificios(DualListModel<String> pickListEdificios) {
		this.pickListEdificios = pickListEdificios;
	}
	public List<String> getEdificiosSource() {
		return edificiosSource;
	}
	public void setEdificiosSource(List<String> edificiosSource) {
		this.edificiosSource = edificiosSource;
	}
	public List<String> getEdificiosTarget() {
		return edificiosTarget;
	}
	public void setEdificiosTarget(List<String> edificiosTarget) {
		this.edificiosTarget = edificiosTarget;
	}
	public Usuario getSelectedUsuario() {

		return selectedUsuario;
	}
	public void setSelectedUsuario(Usuario selectedUsuario) {
		this.selectedUsuario = selectedUsuario;
	}

	public Usuario getUser() {
		return user;
	}

	public void setUser(Usuario user) {
		this.user = user;
	}

	public List<Usuario> getFiltroUsuarios() {
		return filtroUsuarios;
	}

	public void setFiltroUsuarios(List<Usuario> filtroUsuarios) {
		this.filtroUsuarios = filtroUsuarios;
	}
	
	



    
    
    
   

	
	

	
	
	


}

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
public class infoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// @EJB
	@EJB(lookup = "java:global/siguv/EJBean!bean.EJBean")
	private EJBean ejb;

	private String edificioCount = "0";
	private String profesorCount = "0";
	private String usuariosCount = "0";
	private String uuid = "";
	private boolean valido = false;
	private boolean stop = false;

	public void edificioSize() {

		ejb.getUsuariosSize();
	}

	public void usuariosSize() {
		setUsuariosCount(ejb.getUsuariosSize());
	}

	public void profesoresSize() {

	}

	public void updateSize() {
		usuariosCount = ejb.getUsuariosSize();
		profesorCount = ejb.getProfesorSize();
		edificioCount = ejb.getEdificioSize();
		setStop(true);
	}

	public void onload() {
		if (uuid != null && uuid.length() > 0) {
			// Usuario u = ejb.checkCuenta(uuid);
			// System.out.println("OKOL" + u.getNombre());
			System.out.println("Uuid: " + uuid);
		}
		
	}

	public boolean isValido() {
		return valido;
	}

	public void setValido(boolean valido) {
		this.valido = valido;
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public String getEdificioCount() {
		return edificioCount;
	}

	public void setEdificioCount(String edificioCount) {
		this.edificioCount = edificioCount;
	}

	public String getProfesorCount() {
		return profesorCount;
	}

	public void setProfesorCount(String profesorCount) {
		this.profesorCount = profesorCount;
	}

	public String getUsuariosCount() {
		return usuariosCount;
	}

	public void setUsuariosCount(String usuariosCount) {
		this.usuariosCount = usuariosCount;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}

	
	
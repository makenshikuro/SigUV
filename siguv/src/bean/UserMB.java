
package bean;
import java.io.IOException;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import entity.Usuario;

@SessionScoped
@ManagedBean
public class UserMB implements Serializable{
 
	private static final long serialVersionUID = -5266742634352625360L;

private Usuario user;

	@EJB(lookup = "java:global/siguv/EJBean!bean.EJBean")
 private EJBean userFacade;

 public Usuario getUser(){
  if(user == null){
   ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
   String userId = context.getUserPrincipal().getName();
   
   user = userFacade.getUsuario(userId);

  }

  return user;
 }

 public boolean isUserAdmin(){
  return getRequest().isUserInRole("ADMIN");
 }

 public String logOut(){
  getRequest().getSession().invalidate();
  return "logout";
 }
 

 public HttpServletRequest getRequest() {
  return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
 }
 public String getRootContext() throws IOException {
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();

	    return ec.getRequestContextPath();
	    
	}
 
 
 
}
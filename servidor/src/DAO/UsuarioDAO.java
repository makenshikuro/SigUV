package DAO;

import java.util.List;

import entity.Usuario;

public interface UsuarioDAO {
	public Usuario getUsuarioById(String id);

	public Usuario getUsuarioByEmail(String email);
    public List<Usuario> getUsuario();
    public void createUsuario(Usuario p);
    public void deleteUsuario (Usuario p);

	public String getCount();

	public Usuario getByUUID(String uuid);
    public List<Usuario> getUsuarios();
	List<String> getAllUsuariosNames();
}

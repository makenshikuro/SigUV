package services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.text.WordUtils;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.postgis.Polygon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import DAO.CoordenadaDAOImpl;
import DAO.EdificioDAOImpl;
import DAO.EspacioDAOImpl;
import DAO.PanoramaDAOImpl;
import DAO.PlanoDAOImpl;
import DAO.ProfesorDAOImpl;
import DAO.UsuarioDAOImpl;
import entity.Coordenada;
import entity.Edificio;
import entity.Espacio;
import entity.Panorama;
import entity.Plano;
import entity.PlanoPK;
import entity.Profesor;
import entity.Usuario;
import utils.EdificioSerializer;
import utils.EspacioSerializer;
import utils.ProfesorSerializer;
import utils.UsuarioSerializer;

@Stateless
@Path("/v0")
public class ProjectsService {
	
	@PersistenceContext(name = "bduv")
	private EntityManager em;

	String pathPanorama = "D:\\wamp64\\www\\panoramas\\";
	String pathPlano = "D:\\wamp64\\www\\planos\\";
	String pathTmp = "D:\\wamp64\\www\\tmp\\";
	// String pathPanorama = "\\var\\www\\html\\panorama\\";
	// String pathPlano = "\\var\\www\\html\\plano\\";

	@POST
	@Path("/recovery")

	public String getCuenta(@FormParam("correo") String correo) {

		String jsonInString = "";
		String email = "";
		String pass = "";
		
		System.out.println("llego1");

		System.out.println("correo: " + correo);

		if (correo != null) {

			UsuarioDAOImpl uDAO = new UsuarioDAOImpl(em);
			Usuario u = new Usuario();

				u = uDAO.getUsuarioByEmail(correo);
				email = u.getEmail();
				pass = u.getPassword();

			UUID uuid = UUID.randomUUID();

			u.setUuid(uuid.toString());
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			System.out.println(dateFormat.format(date)); // 2016/11/16 12:08:43

			u.setRecovery(date);

			uDAO.update(u);


			System.out.println("Email: " + email);
			System.out.println("Pass: " + pass);

		final Properties properties = new Properties();
		Session session;

		// Nombre del host de correo, es smtp.gmail.com
		properties.setProperty("mail.smtp.host", "smtp.gmail.com");

		// TLS si está disponible
		properties.setProperty("mail.smtp.starttls.enable", "true");

		// Puerto de gmail para envio de correos
		properties.setProperty("mail.smtp.port", "587");

		// Nombre del usuario
		properties.setProperty("mail.smtp.user", "ejemplo@gmail.com");

		// Si requiere o no usuario y password para conectarse.
		properties.setProperty("mail.smtp.auth", "true");

		session = Session.getDefaultInstance(properties);
		session.setDebug(true);
		try {
			MimeMessage message = new MimeMessage(session);

			// Quien envia el correo
			message.setFrom(new InternetAddress("ejemplo@gmail.com"));

			// A quien va dirigido
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

			message.setSubject("Recuperación de Cuenta SigUV");
				
				message.setText("Mensajito con Java Mail<br>" + "su contraseña es: <b>"+pass+"</b>" , "UTF-8", "html");

			Transport t = session.getTransport("smtp");
			t.connect("adbuspaz@gmail.com", "h4s3lbl4d");

			t.sendMessage(message, message.getAllRecipients());
			t.close();
				jsonInString = "OK";
		} catch (MessagingException me) {
			// Aqui se deberia o mostrar un mensaje de error o en lugar
			// de no hacer nada con la excepcion, lanzarla para que el modulo
			// superior la capture y avise al usuario con un popup, por ejemplo.

		}
		} else {
			jsonInString = "ERROR";

		}

		return jsonInString;
	}

	@GET
	@Path("/usuarios/count")
	@Produces("application/json")
	public String getUsuariosCount() {
		UsuarioDAOImpl eDAO = new UsuarioDAOImpl(em);
		return eDAO.getCount();

	}

	@GET
	@Path("/usuarios/uuid/{uuid}")
	@Produces("application/json")
	public String getUsuariosUuid(@FormParam("uuid") String uuid) {

		UsuarioDAOImpl uDAO = new UsuarioDAOImpl(em);
		Usuario u = uDAO.getByUUID(uuid);

		Gson gson = new GsonBuilder().registerTypeAdapter(Usuario.class, new UsuarioSerializer()).setPrettyPrinting()
				.create();
		String arrayToJson = gson.toJson(u);

		return arrayToJson;
	}
	/**
	 * Devuelve todos los usuarios
	 * 
	 * @return String(JSON)
	 */
	@GET
	@Path("/usuarios")
	@Produces("application/json")
	public String getUsuarios() {

		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		UsuarioDAOImpl hDAO = new UsuarioDAOImpl(em);

		List<Usuario> lu = hDAO.getUsuarios();

		// Gson gson = new GsonBuilder().registerTypeAdapter(Usuario.class, new
		// UsuarioSerializer()).setPrettyPrinting().create();

		String arrayToJson = null;
		try {
			arrayToJson = objectMapper.writeValueAsString(lu);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		// System.out.println("Gson on"+lu.size());
		// System.out.println("Gson on "+gson.toJson(lu));
		// System.out.println("Gson"+arrayToJson);

		/*
		 * Gson gson = new Gson(); arrayToJson = gson.toJson(lu);
		 * 
		 * System.out.println("Gson off");
		 */
		return arrayToJson;
	}

	@GET
	@Path("/usuarios/{id}")
	@Produces("application/json")
	public String getUsuario(@PathParam("id") String usuario) {
		// System.out.println("Entro");
		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		UsuarioDAOImpl hDAO = new UsuarioDAOImpl(em);

		Usuario u = hDAO.getById(usuario);

		String jsonInString = null;
		try {
			jsonInString = objectMapper.writeValueAsString(u);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return jsonInString;
	}

	@GET
	@Path("/usuarios/{id}/edificios")
	@Produces("application/json")
	public String getEdificiosEditor(@PathParam("id") String usuario) {
		System.out.println("Entro Edi Editor");

		EdificioDAOImpl eDAO = new EdificioDAOImpl(em);
		List<Edificio> le = eDAO.getEdificiosEditor(usuario);

		Gson gson = new GsonBuilder().registerTypeAdapter(Edificio.class, new EdificioSerializer()).setPrettyPrinting()
				.create();
		String arrayToJson = gson.toJson(le);

		return arrayToJson;

	}

	@GET
	@Path("/usuarios/nombres")
	@Produces("application/json")
	public String getUsuariosNombres() {

		UsuarioDAOImpl esDAO = new UsuarioDAOImpl(em);
		List<String> le = esDAO.getAllUsuariosNames();
		Gson gson = new Gson();
		// Gson gson = new GsonBuilder().registerTypeAdapter(Espacio.class, new
		// EspacioSerializer()).setPrettyPrinting().create();
		String jsonInString = gson.toJson(le);

		// System.out.println("REST ESPACIOS:" +jsonInString);

		return jsonInString;
	}

	/**
	 * Crea Usuario
	 * 
	 * @param usuario
	 * @param password
	 * @param role
	 * @param nombre
	 * @return
	 */
	@POST
	@Path("/usuarios")
	@Produces("application/json")
	public String createUsuario(String jsonObj) {
		String response = "";
		UsuarioDAOImpl uDAO = new UsuarioDAOImpl(em);
		EdificioDAOImpl edDAO = new EdificioDAOImpl(em);

		JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(jsonObj).getAsJsonObject();

		Gson gsonaux = new GsonBuilder().setPrettyPrinting().create();
		String prettyJson = gsonaux.toJson(json);

		System.out.println("Server: " + prettyJson);
		Gson gson = new Gson();
		Usuario u = gson.fromJson(jsonObj, Usuario.class);

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date)); // 2016/11/16 12:08:43

		u.setTime(date);

		uDAO.create(u);

		return response;
	}

	/**
	 * Actualiza usuario
	 * 
	 * @param usuario
	 * @param password
	 * @param role
	 * @param nombre
	 * @return
	 */
	@PUT
	@Path("/usuarios/{usuario}")
	@Produces("application/json")
	// public String updateUsuario (@PathParam("usuario") String usuario,
	// @FormParam("password") String password,
	// @FormParam("role") String role, @FormParam("nombre") String
	// nombre,@FormParam("edificios") String edificios )
	public String updateUsuario(String jsonObj) {
		String response = "";
		UsuarioDAOImpl ui = new UsuarioDAOImpl(em);
		// EdificioDAOImpl edDAO = new EdificioDAOImpl(em);

		JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(jsonObj).getAsJsonObject();

		Gson gsonaux = new GsonBuilder().setPrettyPrinting().create();
		String prettyJson = gsonaux.toJson(json);

		// System.out.println("ServerUPD: " + prettyJson);
		Gson gson = new Gson();
		Usuario u = gson.fromJson(jsonObj, Usuario.class);

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date)); // 2016/11/16 12:08:43

		u.setTime(date);

		ui.update(u);

		return response;
	}

	/**
	 * Elimina usuario
	 * 
	 * @param usuario
	 * @return
	 */
	@DELETE
	@Path("/usuarios/{usuario}")
	@Produces("application/json")
	public Response removeUsuario(@PathParam("usuario") String usuario) {

		UsuarioDAOImpl ui = new UsuarioDAOImpl(em);
		// ui.deleteById(usuario);
		String result = "";
		try {
			ui.deleteById(usuario);
			result = "ok";

		} catch (Exception e) {
			result = "fail";
			e.printStackTrace();
		}

		return Response.status(200).entity(result).build();

	}

	/**
	 * Devuelve todas las coordenadas
	 * 
	 * @return Response
	 */
	@GET
	@Path("/coordenadas")
	@Produces("application/json")
	public String getCoordenadas() {

		CoordenadaDAOImpl cDAO = new CoordenadaDAOImpl(em);

		List<Coordenada> lc = cDAO.getCoordenadas();

		ObjectMapper JSONMapper = new ObjectMapper();
		String jsonInString = "";

		Map<String, List<Map<String, Object>>> map = new HashMap<>();
		List<Map<String, Object>> jsonResponse = new ArrayList<Map<String, Object>>();

		for (Coordenada c : lc) {

			Map<String, Object> jsonResponseaux = new HashMap<>();
			jsonResponseaux.put("Coordenada", c.getIdcoordenada());
			jsonResponseaux.put("Latitud", c.getLatitud());
			jsonResponseaux.put("Longitud", c.getLongitud());
			jsonResponseaux.put("Descripcion", c.getDescripcion());
			jsonResponseaux.put("Espacios", c.getEspacios());
			jsonResponseaux.put("Edificios", c.getEdificios());
			jsonResponse.add(jsonResponseaux);
		}

		try {
			map.put("usuarios", jsonResponse);
			jsonInString = JSONMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return jsonInString;
	}

	/**
	 * Devuelve una coordenada
	 * 
	 * @return Response
	 */
	@GET
	@Path("/coordenadas/{id}")
	@Produces("application/json")
	public String getCoordenada(@PathParam("id") String coordenada) {

		CoordenadaDAOImpl cDAO = new CoordenadaDAOImpl(em);
		Coordenada c = cDAO.getById(coordenada);

		ObjectMapper JSONMapper = new ObjectMapper();
		String jsonInString = "";

		Map<String, List<Map<String, Object>>> map = new HashMap<>();
		List<Map<String, Object>> jsonResponse = new ArrayList<Map<String, Object>>();

		Map<String, Object> jsonResponseaux = new HashMap<>();
		jsonResponseaux.put("Coordenada", c.getIdcoordenada());
		jsonResponseaux.put("Latitud", c.getLatitud());
		jsonResponseaux.put("Longitud", c.getLongitud());
		jsonResponseaux.put("Descripcion", c.getDescripcion());
		jsonResponseaux.put("Espacios", c.getEspacios());
		jsonResponseaux.put("Edificios", c.getEdificios());

		jsonResponse.add(jsonResponseaux);

		try {
			map.put("coordenada", jsonResponse);
			jsonInString = JSONMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return jsonInString;
	}

	/**
	 * Método POST que genera una coordenada los parámetros van en el cuerpo del
	 * mensaje y codificados en x-urlencoder-www
	 * 
	 * @param edificio
	 *            String
	 * @param latitud
	 *            double
	 * @param longitud
	 *            double
	 * @return response
	 */
	@POST
	@Path("/coordenadas")
	@Produces("application/json")
	public String createCoordenada(@FormParam("coordenada") String coordenada,
			@FormParam("descripcion") String descripcion, @FormParam("latitud") double latitud,
			@FormParam("longitud") double longitud, @FormParam("edificios") String edificios,
			@FormParam("espacios") String espacios) {

		CoordenadaDAOImpl cDAO = new CoordenadaDAOImpl(em);
		EspacioDAOImpl esDAO = new EspacioDAOImpl(em);
		EdificioDAOImpl edDAO = new EdificioDAOImpl(em);

		Coordenada c = new Coordenada();
		c.setIdcoordenada(coordenada);
		c.setDescripcion(descripcion);
		c.setLatitud(latitud);
		c.setLongitud(longitud);

		List<Espacio> les = new ArrayList<Espacio>();
		String[] arrayEs = espacios.split(",");
		for (String es : arrayEs) {
			Espacio e = esDAO.getById(es);
			les.add(e);
		}

		c.setEspacios(les);

		List<Edificio> led = new ArrayList<Edificio>();
		String[] arrayEd = edificios.split(",");
		for (String ed : arrayEd) {
			Edificio e = edDAO.getById(ed);
			led.add(e);
		}
		c.setEdificios(led);

		cDAO.create(c);

		String response = "";
		response = "La coordenada " + coordenada + " es " + c.getLatitud() + " " + c.getLongitud();
		return response;

	}

	/**
	 * Método POST que genera una coordenada los parámetros van en el cuerpo del
	 * mensaje y codificados en x-urlencoder-www
	 * 
	 * @param edificio
	 *            String
	 * @param latitud
	 *            double
	 * @param longitud
	 *            double
	 * @return response
	 */
	@PUT
	@Path("/coordenadas/{id}")
	@Produces("application/json")
	public String updateCoordenada(@PathParam("id") String id, @FormParam("coordenada") String coordenada,
			@FormParam("descripcion") String descripcion, @FormParam("latitud") double latitud,
			@FormParam("longitud") double longitud, @FormParam("edificios") String edificios,
			@FormParam("espacios") String espacios) {

		CoordenadaDAOImpl cDAO = new CoordenadaDAOImpl(em);
		EspacioDAOImpl esDAO = new EspacioDAOImpl(em);
		EdificioDAOImpl edDAO = new EdificioDAOImpl(em);
		Coordenada c = cDAO.getById(id);

		c.setIdcoordenada(coordenada);
		c.setDescripcion(descripcion);
		c.setLatitud(latitud);
		c.setLongitud(longitud);

		List<Espacio> les = new ArrayList<Espacio>();
		String[] arrayEs = espacios.split(",");
		for (String es : arrayEs) {
			Espacio e = esDAO.getById(es);
			les.add(e);
		}

		c.setEspacios(les);

		List<Edificio> led = new ArrayList<Edificio>();
		String[] arrayEd = edificios.split(",");
		for (String ed : arrayEd) {
			Edificio e = edDAO.getById(ed);
			led.add(e);
		}
		c.setEdificios(led);

		cDAO.update(c);

		String response = "";
		response = "La coordenada " + coordenada + " es " + c.getLatitud() + " " + c.getLongitud();
		return response;

	}

	/**
	 * Método POST que genera una coordenada los parámetros van en el cuerpo del
	 * mensaje y codificados en x-urlencoder-www
	 * 
	 * @param edificio
	 *            String
	 * @param latitud
	 *            double
	 * @param longitud
	 *            double
	 * @return response
	 */
	@DELETE
	@Path("/coordenadas/{id}")
	@Produces("application/json")
	public String setCoordenada(@PathParam("id") String id) {

		CoordenadaDAOImpl cDAO = new CoordenadaDAOImpl(em);
		cDAO.deleteById(id);

		String response = "";

		return response;

	}

	/**
	 * @param idespacio
	 * @param boundingbox
	 * @param piso
	 * @return
	 */
	@GET
	@Path("/edificios/{id}/espacios")
	@Produces("application/json")
	public String getEspaciosFromEdificio(@PathParam("id") String id) {

		EspacioDAOImpl esDAO = new EspacioDAOImpl(em);
		List<Espacio> le = esDAO.getEspaciosByEdificio(id);
		Gson gson = new GsonBuilder().registerTypeAdapter(Espacio.class, new EspacioSerializer()).setPrettyPrinting()
				.create();
		String jsonInString = gson.toJson(le);

		// System.out.println("REST ESPACIOSBYEDIFICIO:" +jsonInString);

		return jsonInString;
	}

	/**
	 * @param idespacio
	 * @param boundingbox
	 * @param piso
	 * @return
	 */
	@GET
	@Path("/espacios")
	@Produces("application/json")
	public String getEspacios() {

		EspacioDAOImpl esDAO = new EspacioDAOImpl(em);
		List<Espacio> le = esDAO.getEspacios();
		Gson gson = new GsonBuilder().registerTypeAdapter(Espacio.class, new EspacioSerializer()).setPrettyPrinting()
				.create();
		String jsonInString = gson.toJson(le);

		// System.out.println("REST ESPACIOS:" +jsonInString);

		return jsonInString;
	}

	@GET
	@Path("/espacios/{id}")
	@Produces("application/json")
	public String getEspacio(@PathParam("id") String espacio) {

		EspacioDAOImpl eDAO = new EspacioDAOImpl(em);
		Espacio e = eDAO.getEspacioById(espacio);
		Gson gson = new GsonBuilder().registerTypeAdapter(Espacio.class, new EspacioSerializer()).setPrettyPrinting()
				.create();
		String jsonInString = gson.toJson(e);

		// System.out.println("REST ESPACIOS:" + jsonInString);

		return jsonInString;
	}

	@GET
	@Path("/espacios/buscar")
	@Produces("application/json")
	public String getEspacioWithLatLong(@QueryParam("lat") float lat, @QueryParam("long") float lng,
			@QueryParam("piso") int piso) {

		EspacioDAOImpl eDAO = new EspacioDAOImpl(em);
		Espacio e =new Espacio();
		try {
			e = eDAO.getEspacioByLatLng(lat, lng, piso);
		} catch (IndexOutOfBoundsException ex) {
			e = null;
		}
		
		String jsonInString;
		if (e != null) {
			Gson gson = new GsonBuilder().registerTypeAdapter(Espacio.class, new EspacioSerializer())
					.setPrettyPrinting().create();
			jsonInString = gson.toJson(e);
		} else {
			jsonInString = "error";

		}
		System.out.println("REST ESPACIOS with :" + jsonInString);

		return jsonInString;
	}

	@GET
	@Path("/espacios/nombres")
	@Produces("application/json")
	public String getEspaciosNombres() {

		EspacioDAOImpl esDAO = new EspacioDAOImpl(em);
		List<String> le = esDAO.getAllEspaciosNames();
		String jsonInString;
		if (!le.isEmpty()) {
			Gson gson = new Gson();
			jsonInString = gson.toJson(le);
		}
		else {
			jsonInString = "empty";
		}
		// System.out.println("REST ESPACIOS:" +jsonInString);

		return jsonInString;
	}

	/**
	 * @param idespacio
	 * @param boundingbox
	 * @param piso
	 * @return
	 * @throws IOException 
	 */
	@POST
	@Path("/espacios")
	@Consumes("multipart/form-data")
	public Response crearEspacio(@MultipartForm MultipartFormDataInput form) throws IOException {
		// System.out.println("Entidad POST Crear Espacio");

		// System.out.println("Entidad size; " + form.getParts().size());
		EspacioDAOImpl eDAO = new EspacioDAOImpl(em);
		PanoramaDAOImpl pDAO = new PanoramaDAOImpl(em);

		String fileName = "";

		Map<String, List<InputPart>> formParts = form.getFormDataMap();

		// List<InputPart> inPart = formParts.get("file"); // "file" should match the
		// name attribute of your HTML file input
		List<InputPart> inPart = form.getParts(); // "file" should match the name attribute of your HTML file input
		// List<InputPart> inParte = formParts.get;
		InputStream is = null;

		for (InputPart inputPart : inPart) {
			// Retrieve headers, read the Content-Disposition header to obtain the original
			// name of the file
			MultivaluedMap<String, String> headers = inputPart.getHeaders();
			try {
				is = inputPart.getBody(InputStream.class, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println("Project Service: headers: " + headers.toString());
			String[] contentDispositionHeader = headers.getFirst("Content-Disposition").split(";|,");
			// System.out.println("type: " + inputPart.getMediaType());
			for (String name : contentDispositionHeader) {

				if ((name.trim().startsWith("filename"))) {

					if (!inputPart.getMediaType().toString().contains("png")) {
						// System.out.println("Project Service: json object");

						String json = null;
						try {
							json = inputPart.getBodyAsString();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// System.out.println("Project Service: json: " + json);

						/// Deserializar///

						GsonBuilder gsonBuilder = new GsonBuilder();

						JsonDeserializer<Espacio> deserializer = new JsonDeserializer<Espacio>() {

							@Override
							public Espacio deserialize(JsonElement json, Type typeOfT,
									JsonDeserializationContext context) throws JsonParseException {
								JsonObject jsonObject = json.getAsJsonObject();

								Espacio e = new Espacio();

								e.setIdespacio(jsonObject.get("idespacio").getAsString());
								e.setBloque(jsonObject.get("bloque").getAsString());

								e.setPiso(jsonObject.get("piso").getAsInt());
								e.setTipo(jsonObject.get("tipo").getAsString());
								e.setBoundingbox(jsonObject.get("boundingbox").getAsString());
								e.setVisibilidad(jsonObject.get("visibilidad").getAsBoolean());
								if (jsonObject.has("nombrees")) {
									e.setNombrees(jsonObject.get("nombrees").getAsString());
								}
								if (jsonObject.has("nombreen")) {
									e.setNombreen(jsonObject.get("nombreen").getAsString());
								}
								if (jsonObject.has("nombrevl")) {
									e.setNombrevl(jsonObject.get("nombrevl").getAsString());
								}

								// System.out.println("Deserializer: Edi Obj: "+e.toString());

								List<Panorama> lp = new ArrayList<Panorama>();
								JsonArray panoramasArray = jsonObject.getAsJsonArray("panoramas");
								for (JsonElement pano : panoramasArray) {
									Panorama p = new Panorama();

									JsonObject panoObj = pano.getAsJsonObject();

									p.setIdpanorama(panoObj.get("idpanorama").getAsString());
									Espacio aux = new Espacio();
									aux.setIdespacio(panoObj.get("idespacio").getAsString());
									p.setEspacio(aux);
									// p.setPanorama(panoObj.get("usuario").getAsString());

									p.setEspacio(e);
									p.setEnlace(panoObj.get("enlace").getAsString());

									lp.add(p);
								}
								// System.out.println("Deserializer: usu Obj: "+e.toString());

								e.setPanoramas(lp);

								List<Profesor> lprof = new ArrayList<Profesor>();
								JsonArray profesoresArray = jsonObject.getAsJsonArray("profesores");
								for (JsonElement profesor : profesoresArray) {
									Profesor p = new Profesor();

									JsonObject profeObj = profesor.getAsJsonObject();

									p.setIdprofesor(profeObj.get("idprofesor").getAsString());

									lprof.add(p);
								}
								// System.out.println("Deserializer: usu Obj: "+e.toString());

								e.setProfesores(lprof);

								JsonObject coordenadaObj = jsonObject.getAsJsonObject("coordenada");
								Coordenada coordenada = new Coordenada();
								coordenada.setIdcoordenada(coordenadaObj.get("idcoordenada").getAsString());
								coordenada.setDescripcion(coordenadaObj.get("idcoordenada").getAsString());
								coordenada.setLatitud(coordenadaObj.get("latitud").getAsDouble());
								coordenada.setLongitud(coordenadaObj.get("longitud").getAsDouble());

								e.setCoordenada(coordenada);

								JsonObject edificioObj = jsonObject.getAsJsonObject("edificio");
								Edificio edificio = new Edificio();
								edificio.setIdedificio(edificioObj.get("idedificio").getAsString());
								if (edificioObj.has("nombrees")) {
									edificio.setNombrees(edificioObj.get("nombrees").getAsString());
								}
								if (edificioObj.has("nombreen")) {
									edificio.setNombreen(edificioObj.get("nombreen").getAsString());
								}
								if (edificioObj.has("nombrevl")) {
									edificio.setNombrevl(edificioObj.get("nombrevl").getAsString());
								}
								if (edificioObj.has("enlace")) {
									edificio.setEnlace(edificioObj.get("enlace").getAsString());
								}
								edificio.setXano(edificioObj.get("xano").getAsString());
								edificio.setTopleft(edificioObj.get("topleft").getAsString());
								edificio.setTopright(edificioObj.get("topright").getAsString());
								edificio.setBottomleft(edificioObj.get("bottomleft").getAsString());
								Coordenada auxCoord = new Coordenada();
								auxCoord.setIdcoordenada(edificioObj.get("idedificio").getAsString());
								edificio.setCoordenada(auxCoord);

								e.setEdificio(edificio);

								// System.out.println("Deserializer: Obj: "+e.toString());

								return e;
							}

						};
						gsonBuilder.registerTypeAdapter(Espacio.class, deserializer);

						Gson customGson = gsonBuilder.create();
						Espacio espacio = customGson.fromJson(json, Espacio.class);

						// System.out.println("Project Service: Obj: " + espacio.toString());

						List<Panorama> panos = espacio.getPanoramas();

						espacio.setPanoramas(null);
						eDAO.update(espacio);

						for (Panorama p : panos) {

							p.setEspacio(espacio);
							pDAO.update(p);
						}

						System.out.println("Success!");

					} else {
						String[] tmp = name.split("=");
						System.out.println("Project Service: filename: " + tmp[1].trim().replaceAll("\"", ""));

						fileName = tmp[1].trim().replaceAll("\"", "");
						
						
						int read = 0;
						byte[] bytes = new byte[1024];
						
//						try {
//							outpuStream = new FileOutputStream(new File(pathPanorama + fileName));
//							while ((read = is.read(bytes)) != -1) {
//								outpuStream.write(bytes, 0, read);
//							}
//							outpuStream.flush();
//							outpuStream.close();
//						}catch (Exception e) {
//							System.out.println(e);
//						}
						
						try (OutputStream outpuStream = new FileOutputStream(new File(pathPanorama + fileName))){
							
							while ((read = is.read(bytes)) != -1) {
								outpuStream.write(bytes, 0, read);
							}
							outpuStream.flush();

						}catch (Exception e) {
							System.out.println(e);
						}
						
						
						
						
						
						
					}
				}
			}
		}

		return null;
	}

	/**
	 * @param espacio
	 * @param piso
	 * @param bloque
	 * @param nombreVL
	 * @param nombreES
	 * @param tipo
	 * @param nombreEN
	 * @param boundingbox
	 * @param visibilidad
	 * @param edificio
	 * @param coordenada
	 * @return
	 */
	@PUT
	@Path("/espacios/{id}")
	@Produces("application/json")
	public String updateEspacio(@PathParam("id") String espacio, @FormParam("piso") Integer piso,
			@FormParam("bloque") String bloque, @FormParam("nombrevl") String nombreVL,
			@FormParam("nombrees") String nombreES, @FormParam("tipo") String tipo,
			@FormParam("nombreen") String nombreEN, @FormParam("boundingbox") String boundingbox,
			@FormParam("visibilidad") boolean visibilidad, @FormParam("edificios") String edificio,
			@FormParam("coordenada") String coordenada) {
		String response = "";

		EspacioDAOImpl esDAO = new EspacioDAOImpl(em);
		Espacio e = esDAO.getById(espacio);

		Polygon poly = null;
		try {
			poly = new Polygon(boundingbox);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		e.setIdespacio(espacio);
		e.setNombreen(nombreEN);
		e.setNombrees(nombreES);
		e.setNombrevl(nombreVL);
		e.setPolygonBoundingbox(poly);
		e.setBloque(bloque);
		e.setPiso(piso);
		e.setTipo(tipo);
		e.setVisibilidad(visibilidad);

		esDAO.update(e);

		response = "nombre espacio: " + espacio;

		return response;
	}

	/**
	 * @param espacio
	 * @return
	 */
	@DELETE
	@Path("/espacios/{id}")
	@Produces("application/json")
	public String removeEspacio(@PathParam("id") String espacio) {
		String response = "";
		EspacioDAOImpl esDAO = new EspacioDAOImpl(em);
		esDAO.deleteById(espacio);
		return response;
	}

	@GET
	@Path("/edificios")
	@Produces("application/json")
	public String getEdificios() {

		EdificioDAOImpl eDAO = new EdificioDAOImpl(em);
		List<Edificio> le = eDAO.getEdificios();

		Gson gson = new GsonBuilder().registerTypeAdapter(Edificio.class, new EdificioSerializer()).setPrettyPrinting()
				.create();
		String arrayToJson = gson.toJson(le);

		return arrayToJson;
	}

	@GET
	@Path("/edificios/count")
	@Produces("application/json")
	public String getEdificioCount() {
		EdificioDAOImpl eDAO = new EdificioDAOImpl(em);
		return eDAO.getCount();

	}

	@GET
	@Path("/edificios/{id}")
	@Produces("application/json")
	public String getEdificio(@PathParam("id") String edificio) {

		EdificioDAOImpl edDAO = new EdificioDAOImpl(em);
		Edificio e = edDAO.getEdificioById(edificio);
		Gson gson = new GsonBuilder().registerTypeAdapter(Edificio.class, new EdificioSerializer()).setPrettyPrinting()
				.create();
		String arrayToJson = gson.toJson(e);

		return arrayToJson;
	}

	@GET
	@Path("/edificios/nombres")
	@Produces("application/json")
	public String getEdificiosNombres() {

		EdificioDAOImpl esDAO = new EdificioDAOImpl(em);
		List<String> le = esDAO.getAllEdificiosNames();
		Gson gson = new Gson();
		String jsonInString = gson.toJson(le);

		return jsonInString;
	}

	/**
	 * 
	 * @return
	 */
	@POST
	@Path("/edificios")
	@Consumes("multipart/form-data")
	public Response crearEdificio(@MultipartForm MultipartFormDataInput form) {
		System.out.println("Entidad POST Crear Edificio");

		System.out.println("Entidad size; " + form.getParts().size());
		EdificioDAOImpl eDAO = new EdificioDAOImpl(em);
		PlanoDAOImpl pDAO = new PlanoDAOImpl(em);

		String fileName = "";
		// String path ="D:\\test\\plano\\";
		Map<String, List<InputPart>> formParts = form.getFormDataMap();

		// List<InputPart> inPart = formParts.get("file"); // "file" should match the
		// name attribute of your HTML file input
		List<InputPart> inPart = form.getParts(); // "file" should match the name attribute of your HTML file input
		// List<InputPart> inParte = formParts.get;
		InputStream is = null;

		for (InputPart inputPart : inPart) {
			// Retrieve headers, read the Content-Disposition header to obtain the original
			// name of the file
			MultivaluedMap<String, String> headers = inputPart.getHeaders();
			try {
				is = inputPart.getBody(InputStream.class, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Project Service: headers: " + headers.toString());
			String[] contentDispositionHeader = headers.getFirst("Content-Disposition").split(";|,");
			System.out.println("type: " + inputPart.getMediaType());
			for (String name : contentDispositionHeader) {

				if ((name.trim().startsWith("filename"))) {

					if (!inputPart.getMediaType().toString().contains("svg")) {
						System.out.println("Project Service: json object");

						String json = null;
						try {
							json = inputPart.getBodyAsString();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("Project Service: json: " + json);

						/// Deserializar///

						GsonBuilder gsonBuilder = new GsonBuilder();

						JsonDeserializer<Edificio> deserializer = new JsonDeserializer<Edificio>() {

							@Override
							public Edificio deserialize(JsonElement json, Type typeOfT,
									JsonDeserializationContext context) throws JsonParseException {
								JsonObject jsonObject = json.getAsJsonObject();

								Edificio e = new Edificio();

								e.setIdedificio(jsonObject.get("idedificio").getAsString());

								if (jsonObject.has("nombreen")) {
									e.setNombreen(jsonObject.get("nombreen").getAsString());
								}
								if (jsonObject.has("nombrevl")) {
									e.setNombrevl(jsonObject.get("nombrevl").getAsString());
								}
								if (jsonObject.has("nombrees")) {
									e.setNombrees(jsonObject.get("nombrees").getAsString());
								}

								e.setDireccion(jsonObject.get("direccion").getAsString());
								e.setTelefono(jsonObject.get("telefono").getAsString());
								e.setEnlace(jsonObject.get("enlace").getAsString());
								e.setXano(jsonObject.get("xano").getAsString());
								e.setTopleft(jsonObject.get("topleft").getAsString());
								e.setTopright(jsonObject.get("topright").getAsString());
								e.setBottomleft(jsonObject.get("bottomleft").getAsString());

								// System.out.println("Deserializer: Edi Obj: "+e.toString());

								List<Usuario> lu = new ArrayList<Usuario>();
								JsonArray usuariosArray = jsonObject.getAsJsonArray("usuarios");
								for (JsonElement usuario : usuariosArray) {
									Usuario u = new Usuario();

									JsonObject usuarioObj = usuario.getAsJsonObject();
									u.setUsuario(usuarioObj.get("usuario").getAsString());
									// u.setNombre(usuarioObj.get("nombre").getAsString());
									// u.setPassword(usuarioObj.get("password").getAsString());
									// u.setUuid(usuarioObj.get("uuid").getAsString());
									// u.setRole(usuarioObj.get("role").getAsString());
									lu.add(u);
								}
								// System.out.println("Deserializer: usu Obj: "+e.toString());

								e.setUsuarios(lu);

								List<Plano> lp = new ArrayList<Plano>();
								JsonArray planoArray = jsonObject.getAsJsonArray("planos");
								for (JsonElement plano : planoArray) {
									Plano p = new Plano();
									PlanoPK pk = new PlanoPK();
									JsonObject planoObj = plano.getAsJsonObject();
									pk.setIdedificio(e.getIdedificio());
									pk.setIdplano(planoObj.get("idplano").getAsString());
									p.setId(pk);
									p.setEnlace(planoObj.get("enlace").getAsString());
									p.setNivel(planoObj.get("nivel").getAsInt());
									Edificio edAux = new Edificio();
									edAux.setIdedificio(edAux.getIdedificio());
									p.setEdificio(edAux);

									lp.add(p);
								}

								e.setPlanos(lp);

								JsonObject coordenadaObj = jsonObject.getAsJsonObject("coordenada");
								Coordenada coordenada = new Coordenada();
								coordenada.setIdcoordenada(coordenadaObj.get("idcoordenada").getAsString());
								coordenada.setDescripcion(coordenadaObj.get("idcoordenada").getAsString());
								coordenada.setLatitud(coordenadaObj.get("latitud").getAsDouble());
								coordenada.setLongitud(coordenadaObj.get("longitud").getAsDouble());

								e.setCoordenada(coordenada);

								// System.out.println("Deserializer: Obj: "+e.toString());

								return e;
							}

						};

						gsonBuilder.registerTypeAdapter(Edificio.class, deserializer);

						Gson customGson = gsonBuilder.create();
						Edificio edificio = customGson.fromJson(json, Edificio.class);

						System.out.println("Project Service: Obj: " + edificio.toString());

						List<Plano> planos = edificio.getPlanos();

						edificio.setPlanos(null);
						eDAO.update(edificio);

						for (Plano p : planos) {

							p.setEdificio(edificio);
							pDAO.update(p);
						}
						System.out.println("Success!");

					} else {
						String[] tmp = name.split("=");
						System.out.println("Project Service: filename: " + tmp[1].trim().replaceAll("\"", ""));

						fileName = tmp[1].trim().replaceAll("\"", "");

						// try {
						//
						// Files.copy(new File(pathTmp + fileName).toPath(), new File(pathPlano +
						// fileName).toPath());
						//
						// } catch (IOException e) {
						//
						// e.printStackTrace();
						// }
					}

				}
			}

		}

		return null;
	}

	/**
	 * @param edificio
	 * @param enlace
	 * @param coordenada
	 * @param nombreVL
	 * @param nombreES
	 * @param nombreEN
	 * @param direccion
	 * @param telefono
	 * @param xano
	 * @return
	 */
	@PUT
	@Path("/edificios/{id}")
	@Produces("application/json")
	public String updateEdificio(@PathParam("edificio") String edificio, @FormParam("enlace") String enlace,
			@FormParam("coordenada") String coordenada, @FormParam("nombrevl") String nombreVL,
			@FormParam("nombrees") String nombreES, @FormParam("nombreen") String nombreEN,
			@FormParam("direccion") String direccion, @FormParam("telefono") String telefono,
			@FormParam("xano") String xano) {
		String response = "";
		EdificioDAOImpl edDAO = new EdificioDAOImpl(em);
		Edificio e = edDAO.getById(edificio);
		e.setIdedificio(edificio);
		e.setDireccion(direccion);
		e.setNombreen(nombreEN);
		e.setNombrees(nombreES);
		e.setNombrevl(nombreVL);
		e.setTelefono(telefono);
		e.setXano(xano);
		e.setEnlace(enlace);

		edDAO.update(e);

		return response;
	}

	/**
	 * @param edificio
	 * @return
	 */
	@DELETE
	@Path("/edificios/{edificio}")
	@Produces("application/json")
	public String removeEdificio(@PathParam("edificio") String edificio) {
		String response = "";

		EdificioDAOImpl edDAO = new EdificioDAOImpl(em);
		edDAO.deleteById(edificio);
		return response;
	}

	/**
	 * @return
	 * 
	 */
	@GET
	@Path("/profesores/count")
	@Produces("application/json")
	public String getProfesoresCount() {
		ProfesorDAOImpl eDAO = new ProfesorDAOImpl(em);

		return eDAO.getCount();
	}

	@GET
	@Path("/profesores")
	@Produces("application/json")
	public String getProfesores() {


		ProfesorDAOImpl eDAO = new ProfesorDAOImpl(em);
		List<Profesor> le = eDAO.getProfesores();
		Gson gson = new GsonBuilder().registerTypeAdapter(Profesor.class, new ProfesorSerializer()).setPrettyPrinting()
				.create();


		return gson.toJson(le);
	}

	/**
	 * @param profesor
	 * @return
	 */
	@GET
	@Path("/profesores/{id}")
	@Produces("application/json")
	public String getProfesor(@PathParam("id") String profesor) {

		ProfesorDAOImpl pDAO = new ProfesorDAOImpl(em);
		Profesor e = pDAO.getProfesorById(profesor);
		Gson gson = new GsonBuilder().registerTypeAdapter(Profesor.class, new ProfesorSerializer()).setPrettyPrinting()
				.create();
		String arrayToJson = gson.toJson(e);

		return arrayToJson;
	}
	@GET
	@Path("/profesores/buscar")
	@Produces("application/json")
	public String getProfesorDatos(@QueryParam("user") String profesor) {

		ProfesorDAOImpl pDAO = new ProfesorDAOImpl(em);
		Profesor e = pDAO.getProfesorById(profesor);

		Document doc = null;
		try {
			doc = Jsoup.connect("https://www.uv.es/uvweb/universidad/es/ficha-persona-1285950309813.html?p2="+profesor).get();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		Element nombre = doc.select("#persona-nom").first();
		String nombreLabel;
		try {
			nombreLabel = WordUtils.capitalizeFully(nombre.text());
		}catch(NullPointerException ex){
			nombreLabel = e.getUsuario();
		}
		
		Element afiliacion = doc.select(".afiliacio").first();
		String afiliacionLabel;
		try {
			afiliacionLabel = afiliacion.text();
		}catch(NullPointerException ex){
			afiliacionLabel = "";
		}
		
		Element departamento = doc.select("#persona-dept").first();
		String departamentoLabel;
		try {
			departamentoLabel = departamento.text();
		}catch(NullPointerException ex){
			departamentoLabel = "";
		}
		
		Element foto = doc.select("#persona-foto").first();
		String fotoSrc;
		try {
			
			fotoSrc = foto.attr("src");
		}catch(NullPointerException ex){
			fotoSrc = "";
		}
	
		JsonObject object = new JsonObject();

		object.addProperty("idprofesor", e.getIdprofesor());
		object.addProperty("usuario", e.getUsuario());
		object.addProperty("visibilidad", e.getVisibilidad());
		object.addProperty("nombre", nombreLabel);
		object.addProperty("afiliacion", afiliacionLabel);
		object.addProperty("departamento", departamentoLabel);
		object.addProperty("foto", fotoSrc);

		JsonArray objectArray = new JsonArray();
		for (Espacio esp : e.getEspacios()) {
			JsonObject objectEspacio = new JsonObject();
			
			if (Boolean.TRUE.equals(esp.getVisibilidad())) {

				objectEspacio.addProperty("idespacio", esp.getIdespacio());
			 objectEspacio.addProperty("bloque", esp.getBloque());
			 objectEspacio.addProperty("piso", esp.getPiso() );
			 objectEspacio.addProperty("nombrevl", esp.getNombrevl() );
			 objectEspacio.addProperty("nombrees", esp.getNombrees() );
			 objectEspacio.addProperty("nombreen", esp.getNombreen() );
			 objectEspacio.addProperty("tipo", esp.getTipo() );
			 objectEspacio.addProperty("visibilidad", esp.getVisibilidad() );
			 objectEspacio.addProperty("latitud", esp.getCoordenada().getLatitud() );
			 objectEspacio.addProperty("longitud", esp.getCoordenada().getLongitud());
			 objectEspacio.addProperty("boundingbox", esp.getBoundingbox() );
			 objectEspacio.addProperty("xano", esp.getEdificio().getXano() );
			 objectEspacio.addProperty("edificioNombreES", esp.getEdificio().getNombrees() );
			 
				JsonArray objectPanoArray = new JsonArray();
				for (Panorama pano : esp.getPanoramas()) {
					JsonObject objectPano = new JsonObject();
					objectPano.addProperty("enlace", pano.getEnlace());
					objectPanoArray.add(objectPano);
				}
				objectEspacio.add("panoramas", objectPanoArray);

				objectArray.add(objectEspacio);
			}	
		}
		object.add("espacios", objectArray);
		return object.toString();

	}

	@GET
	@Path("/profesores/nombres")
	@Produces("application/json")
	public String getProfesoresNombres() {

		ProfesorDAOImpl pDAO = new ProfesorDAOImpl(em);
		List<String> lp = pDAO.getAllProfesorNames();
		Gson gson = new Gson();
		String jsonInString = gson.toJson(lp);

		return jsonInString;
	}

	@POST
	@Path("/profesores")
	@Produces("application/json")
	public String createProfesor(String jsonObj) {
		String response = "";
		ProfesorDAOImpl pDAO = new ProfesorDAOImpl(em);

		JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(jsonObj).getAsJsonObject();

		Gson gsonaux = new GsonBuilder().setPrettyPrinting().create();
		String prettyJson = gsonaux.toJson(json);

		System.out.println("Server: " + prettyJson);
		Gson gson = new Gson();
		Profesor p = gson.fromJson(jsonObj, Profesor.class);

		pDAO.update(p);

		return response;
	}

	@PUT
	@Path("/profesores/{id}")
	@Produces("application/json")
	public String updateProfesor(String jsonObj) {
		String response = "";
		ProfesorDAOImpl pDAO = new ProfesorDAOImpl(em);

		JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(jsonObj).getAsJsonObject();

		Gson gsonaux = new GsonBuilder().setPrettyPrinting().create();
		String prettyJson = gsonaux.toJson(json);

		System.out.println("Server: " + prettyJson);
		Gson gson = new Gson();
		Profesor p = gson.fromJson(jsonObj, Profesor.class);

		pDAO.update(p);

		return response;
	}

	/**
	 * @param profesor
	 * @return
	 */
	@DELETE
	@Path("/profesores/{id}")
	@Produces("application/json")
	public String removeProfesor(@PathParam("id") String profesor) {
		String response = "";
		ProfesorDAOImpl pDAO = new ProfesorDAOImpl(em);
		pDAO.deleteById(profesor);

		return response;
	}

}

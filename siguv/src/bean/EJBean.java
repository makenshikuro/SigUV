/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import entity.Coordenada;
import entity.Edificio;
import entity.Espacio;
import entity.Imagen;
import entity.Panorama;
import entity.Plano;
import entity.PlanoPK;
import entity.Profesor;
import entity.Usuario;

@Stateless
public class EJBean {
	protected Client client;
	protected String baseURI = "http://localhost:8080/servidor/rest/v0";

	// String pathPano = "D:\\test\\panoramas\\";
	// String pathPlano = "D:\\test\\planos\\";
	String pathPano = "D:\\wamp64\\www\\panoramas\\";
	String pathPlano = "D:\\wamp64\\www\\planos\\";

	@Inject
	private UserMB userMB;

	public List<Usuario> getListaUsuarios() {

		client = ClientBuilder.newClient();

		String usuarios = client.target(baseURI).path("usuarios").request(MediaType.APPLICATION_JSON).get(String.class);

		client.close();

		// System.out.println("METHOD2 "+usuarios);

		Type listType = new TypeToken<List<Usuario>>() {
		}.getType();
		List<Usuario> jsonToUsuarioList = new Gson().fromJson(usuarios, listType);

		return jsonToUsuarioList;

	}

	public List<String> getNombresUsuarios() {

		client = ClientBuilder.newClient();

		String usuarios = client.target(baseURI).path("usuarios").path("nombres").request(MediaType.APPLICATION_JSON)
				.get(String.class);

		client.close();

		// System.out.println("METHOD2 "+usuarios);

		Type listType = new TypeToken<List<String>>() {
		}.getType();
		List<String> jsonToUsuarioList = new Gson().fromJson(usuarios, listType);

		return jsonToUsuarioList;

	}

	public Usuario checkCuenta(String uuid) {
		client = ClientBuilder.newClient();

		String usuarios = client.target(baseURI).path("usuarios").path("uuid").path(uuid)
				.request(MediaType.APPLICATION_JSON)

				.get(String.class);
		client.close();

		// System.out.println("get Usuario: "+usuarios);
		Usuario jsonToUsuario = new Gson().fromJson(usuarios, Usuario.class);

		return jsonToUsuario;

	}

	public Usuario getUsuario(String id) {

		client = ClientBuilder.newClient();

		String usuarios = client.target(baseURI).path("usuarios").path(id).request(MediaType.APPLICATION_JSON)

				.get(String.class);
		client.close();

		// System.out.println("get Usuario: "+usuarios);
		Usuario jsonToUsuario = new Gson().fromJson(usuarios, Usuario.class);

		return jsonToUsuario;

	}

	public String getUsuariosSize() {
		client = ClientBuilder.newClient();

		String usuarios = client.target(baseURI).path("usuarios").path("count").request(MediaType.APPLICATION_JSON)

				.get(String.class);
		client.close();


		return usuarios;
	}

	public void removeUsuario(String id) {

		client = ClientBuilder.newClient();
		Response response = client.target(baseURI).path("usuarios").path(id).request(MediaType.APPLICATION_JSON)
				.delete();
		client.close();

	}

	public void createUsuario(Usuario u) {
		final Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
		final String gson = prettyGson.toJson(u);

		// System.out.println(gson);

		client = ClientBuilder.newClient();
		Response response = client.target(baseURI).path("usuarios").request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(gson, MediaType.APPLICATION_JSON), Response.class);
		client.close();
	}

	public void updateUsuario(Usuario u) {

		String gson = new Gson().toJson(u);

		client = ClientBuilder.newClient();
		Response response = client.target(baseURI).path("usuarios").path(u.getUsuario())
				.request(MediaType.APPLICATION_JSON)
				.put(Entity.entity(gson, MediaType.APPLICATION_JSON), Response.class);
		client.close();

	}

	public List<String> getNombresEdificios() {
		client = ClientBuilder.newClient();
		String edificios = client.target(baseURI).path("edificios").path("nombres").request(MediaType.APPLICATION_JSON)
				.get(String.class);
		client.close();

		Type listType = new TypeToken<List<String>>() {
		}.getType();
		List<String> jsonToEspacioList = new Gson().fromJson(edificios, listType);

		return jsonToEspacioList;
	}

	public List<Edificio> getListaEdificios() {

		client = ClientBuilder.newClient();
		String edificios = client.target(baseURI).path("edificios").request(MediaType.APPLICATION_JSON)
				.get(String.class);
		client.close();

		Type listType = new TypeToken<List<Edificio>>() {
		}.getType();
		List<Edificio> jsonToEdificioList = new Gson().fromJson(edificios, listType);

		return jsonToEdificioList;
	}

	public List<Edificio> getListaEdificiosEditor(String id) {
		System.out.println("EJB Bean: " + userMB.isUserAdmin());
		client = ClientBuilder.newClient();
		String edificios = client.target(baseURI).path("usuarios").path(id).path("edificios")
				.request(MediaType.APPLICATION_JSON).get(String.class);
		client.close();

		Type listType = new TypeToken<List<Edificio>>() {
		}.getType();
		List<Edificio> jsonToEdificioList = new Gson().fromJson(edificios, listType);

		return jsonToEdificioList;
	}

	public String getEdificioSize() {
		client = ClientBuilder.newClient();

		String edificios = client.target(baseURI).path("edificios").path("count").request(MediaType.APPLICATION_JSON)

				.get(String.class);
		client.close();

		return edificios;
	}

	public Edificio getEdificio(String id) {

		client = ClientBuilder.newClient();
		String edificio = client.target(baseURI).path("edificios").path(id).request(MediaType.APPLICATION_JSON)
				.get(String.class);
		client.close();

		/// Deserializar///

		GsonBuilder gsonBuilder = new GsonBuilder();

		JsonDeserializer<Edificio> deserializer = new JsonDeserializer<Edificio>() {

			@Override
			public Edificio deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
					throws JsonParseException {
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
				if (jsonObject.has("direccion")) {
					e.setDireccion(jsonObject.get("direccion").getAsString());
				}
				if (jsonObject.has("telefono")) {
					e.setTelefono(jsonObject.get("telefono").getAsString());
				}
				if (jsonObject.has("enlace")) {
					e.setEnlace(jsonObject.get("enlace").getAsString());
				}
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
					lu.add(u);
				}
				// System.out.println("Deserializer: usu Obj: "+e.toString());

				e.setUsuarios(lu);
				// System.out.println("Deserializer: usu Obj: " + e.getUsuarios().size());
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

				return e;
			}

		};
		gsonBuilder.registerTypeAdapter(Edificio.class, deserializer);

		System.out.println("GSON" + edificio);

		Gson customGson = gsonBuilder.create();
		Edificio jsonToEdificio = customGson.fromJson(edificio, Edificio.class);
		System.out.print("Size users EJB: " + jsonToEdificio.getUsuarios().size());

		return jsonToEdificio;
	}

	public void removeEdificio(String id) {
		System.out.println("REMOVE Ed");
		client = ClientBuilder.newClient();

		Response response = client.target(baseURI).path("edificios").path(id).request(MediaType.APPLICATION_JSON)
				.delete();

		client.close();
	}

	public void removeEspacio(String id) {
		System.out.println("REMOVE Espacio");
		client = ClientBuilder.newClient();

		Response response = client.target(baseURI).path("espacios").path(id).request(MediaType.APPLICATION_JSON)
				.delete();

		client.close();
	}

	public Response createEdificio(List<Imagen> planos, String path, String gson) throws IOException {
		MultipartFormDataOutput formData = new MultipartFormDataOutput();
		String ruta = path;
		int index = 0;
		for (Imagen plano : planos) {
			FileInputStream file = new FileInputStream(new File(ruta + plano.getNombre() + plano.getExtension()));
			System.out.println("rutaArchivo: " + ruta + plano.getNombre() + plano.getExtension());
			formData.addFormData("file" + (index++), file, MediaType.APPLICATION_SVG_XML_TYPE,
					plano.getNombre() + plano.getExtension());
		}
		formData.addFormData("json", gson, MediaType.APPLICATION_JSON_TYPE, "edificio.json");

		client = ClientBuilder.newClient();
		Response response = client.target(baseURI).path("edificios")
				.request(MediaType.MULTIPART_FORM_DATA)
				.post(Entity.entity(formData, MediaType.MULTIPART_FORM_DATA_TYPE), Response.class);
		client.close();

		System.out.println("UploadFile EJBEAN Fin");

		return response;

	}

	public Response createEspacio(List<Imagen> panos, String path, String gson) throws IOException {
		MultipartFormDataOutput formData = new MultipartFormDataOutput();

		String ruta = path;
		int index = 0;
		for (Imagen pano : panos) {
			FileInputStream file = new FileInputStream(new File(ruta + pano.getNombre() + pano.getExtension()));
			System.out.println("rutaArchivo: " + ruta + pano.getNombre() + pano.getExtension());	
			formData.addFormData("file" + (index++), file, new MediaType("image", "png"),
					pano.getNombre() + pano.getExtension());
		}

		formData.addFormData("json", gson, MediaType.APPLICATION_JSON_TYPE, "espacios.json");

		client = ClientBuilder.newClient();

		Response response = client.target(baseURI).path("espacios")

				.request(MediaType.MULTIPART_FORM_DATA)
				.post(Entity.entity(formData, MediaType.MULTIPART_FORM_DATA_TYPE), Response.class);
		client.close();

		System.out.println("UploadFile EJBEAN Fin");

		return response;
	}

	public Response createPlano(MultipartFormDataInput multipartFormDataInput) {
		MultivaluedMap<String, String> multivaluedMap = null;
		String fileName = null;
		InputStream inputStream = null;
		String uploadFilePath = null;

		try {
			Map<String, List<InputPart>> map = multipartFormDataInput.getFormDataMap();
			List<InputPart> lstInputPart = map.get("uploadedFile");

			for (InputPart inputPart : lstInputPart) {

				// get filename to be uploaded
				multivaluedMap = inputPart.getHeaders();
				fileName = getFileName(multivaluedMap);

				if (null != fileName && !"".equalsIgnoreCase(fileName)) {

					// write & upload file to UPLOAD_FILE_SERVER
					inputStream = inputPart.getBody(InputStream.class, null);
					// uploadFilePath = writeToFileServer(inputStream, fileName);

					// close the stream
					inputStream.close();
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			// release resources, if any
		}
		return Response.ok("File uploaded successfully at " + uploadFilePath).build();
	}

	private String getFileName(MultivaluedMap<String, String> multivaluedMap) {

		String[] contentDisposition = multivaluedMap.getFirst("Content-Disposition").split(";");

		for (String filename : contentDisposition) {

			if ((filename.trim().startsWith("filename"))) {
				String[] name = filename.split("=");
				String exactFileName = name[1].trim().replaceAll("\"", "");
				return exactFileName;
			}
		}
		return "UnknownFile";
	}

	public Response updateEdificio(List<Imagen> planos, String path, String gson) throws IOException {
		System.out.println("updateEdificio EJBean");

		MultipartFormDataOutput formData = new MultipartFormDataOutput();

		String ruta = path;

		int index = 0;
		for (Imagen plano : planos) {
			FileInputStream file = new FileInputStream(new File(ruta + plano.getNombre() + plano.getExtension()));
			System.out.println("rutaArchivo: " + ruta + plano.getNombre() + plano.getExtension());
			formData.addFormData("file" + (index++), file, MediaType.APPLICATION_SVG_XML_TYPE,
					plano.getNombre() + plano.getExtension());
		}

		formData.addFormData("json", gson, MediaType.APPLICATION_JSON_TYPE, "edificio.json");

		client = ClientBuilder.newClient();

		Response response = client.target(baseURI).path("edificios")

				.request(MediaType.MULTIPART_FORM_DATA)
				.post(Entity.entity(formData, MediaType.MULTIPART_FORM_DATA_TYPE), Response.class);
		client.close();

		System.out.println("UploadFile EJBEAN Fin");

		return response;

	}

	public Response updateEspacio(List<Imagen> panos, String path, String gson) throws IOException {
		System.out.println("updateEspacio EJBean");
		MultipartFormDataOutput formData = new MultipartFormDataOutput();

		String ruta = path;
		int index = 0;
		for (Imagen pano : panos) {
			FileInputStream file = new FileInputStream(new File(ruta + pano.getNombre() + pano.getExtension()));
			System.out.println("rutaArchivo: " + ruta + pano.getNombre() + pano.getExtension());
			formData.addFormData("file" + (index++), file, new MediaType("image", "png"),
					pano.getNombre() + pano.getExtension());
		}

		formData.addFormData("json", gson, MediaType.APPLICATION_JSON_TYPE, "espacios.json");

		client = ClientBuilder.newClient();

		Response response = client.target(baseURI).path("espacios")
				.request(MediaType.MULTIPART_FORM_DATA)
				.post(Entity.entity(formData, MediaType.MULTIPART_FORM_DATA_TYPE), Response.class);
		client.close();

		System.out.println("UPdateEspacio EJBEAN Fin");

		return response;
	}


	public List<Espacio> getListaEspacios() {

		client = ClientBuilder.newClient();
		String espacios = client.target(baseURI).path("espacios").request(MediaType.APPLICATION_JSON).get(String.class);
		client.close();

		Type listType = new TypeToken<List<Espacio>>() {
		}.getType();
		List<Espacio> jsonToEspacioList = new Gson().fromJson(espacios, listType);

		return jsonToEspacioList;
	}

	public Espacio getEspacio(String id) {
		client = ClientBuilder.newClient();
		String espacio = client.target(baseURI).path("espacios").path(id).request(MediaType.APPLICATION_JSON)
				.get(String.class);
		client.close();

		/// Deserializar///
		
//		System.out.println(espacio);

		GsonBuilder gsonBuilder = new GsonBuilder();

		JsonDeserializer<Espacio> deserializer = new JsonDeserializer<Espacio>() {

			@Override
			public Espacio deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
					throws JsonParseException {
				JsonObject jsonObject = json.getAsJsonObject();

				Espacio e = new Espacio();

				e.setIdespacio(jsonObject.get("idespacio").getAsString());

				if (jsonObject.has("nombreen")) {
					e.setNombreen(jsonObject.get("nombreen").getAsString());
				}
				if (jsonObject.has("nombrevl")) {
					e.setNombrevl(jsonObject.get("nombrevl").getAsString());
				}
				if (jsonObject.has("nombrees")) {
					e.setNombrees(jsonObject.get("nombrees").getAsString());
				}
				if (jsonObject.has("bloque")) {
					e.setBloque(jsonObject.get("bloque").getAsString());
				}
				if (jsonObject.has("piso")) {
					e.setPiso(jsonObject.get("piso").getAsInt());
				}
				if (jsonObject.has("tipo")) {
					e.setTipo(jsonObject.get("tipo").getAsString());
				}
				e.setVisibilidad(jsonObject.get("visibilidad").getAsBoolean());
				if (jsonObject.has("boundingbox")) {
					e.setBoundingbox(jsonObject.get("boundingbox").getAsString());
				}

				// System.out.println("Deserializer: Edi Obj: "+e.toString());
				JsonObject edificioObj = jsonObject.getAsJsonObject("edificio");
				Edificio edificio = new Edificio();
				edificio.setIdedificio(edificioObj.get("idedificio").getAsString());

				e.setEdificio(edificio);

				List<Panorama> lp = new ArrayList<Panorama>();
				JsonArray panoArray = jsonObject.getAsJsonArray("panoramas");
				for (JsonElement pano : panoArray) {
					Panorama p = new Panorama();
					JsonObject panoObj = pano.getAsJsonObject();
					p.setIdpanorama(panoObj.get("idpanorama").getAsString());
					p.setEnlace(panoObj.get("enlace").getAsString());
					lp.add(p);
				}

				e.setPanoramas(lp);

				JsonObject coordenadaObj = jsonObject.getAsJsonObject("coordenada");
				Coordenada coordenada = new Coordenada();
				coordenada.setIdcoordenada(coordenadaObj.get("idcoordenada").getAsString());
				coordenada.setDescripcion(coordenadaObj.get("idcoordenada").getAsString());
				coordenada.setLatitud(coordenadaObj.get("latitud").getAsDouble());
				coordenada.setLongitud(coordenadaObj.get("longitud").getAsDouble());

				e.setCoordenada(coordenada);
				
				if (jsonObject.has("profesores")) {
					List<Profesor> lprof = new ArrayList<Profesor>();
					JsonArray profArray = jsonObject.getAsJsonArray("profesores");
					for (JsonElement prof : profArray) {
						Profesor p = new Profesor();
						JsonObject profObj = prof.getAsJsonObject();
						p.setIdprofesor(profObj.get("idprofesor").getAsString());
						p.setUsuario(profObj.get("usuario").getAsString());
						p.setVisibilidad(profObj.get("visibilidad").getAsBoolean());
						
						lprof.add(p);
					}

					e.setProfesores(lprof);
				}

				return e;
			}

		};
		gsonBuilder.registerTypeAdapter(Espacio.class, deserializer);

		// System.out.println("GSON"+espacio);

		Gson customGson = gsonBuilder.create();
		Espacio jsonToEspacio = customGson.fromJson(espacio, Espacio.class);

		return jsonToEspacio;
	}

	public List<String> getNombresEspacios() {
		client = ClientBuilder.newClient();
		String espacios = client.target(baseURI).path("espacios").path("nombres").request(MediaType.APPLICATION_JSON)
				.get(String.class);
		client.close();

		System.out.println("GSON " + espacios);

		Type listType = new TypeToken<List<String>>() {
		}.getType();
		List<String> jsonToEspacioList = new Gson().fromJson(espacios, listType);

		return jsonToEspacioList;
	}

	public List<Espacio> getEspaciosFromEdificio(String id) {

		client = ClientBuilder.newClient();
		String espaciosJSON = client.target(baseURI).path("edificios").path(id).path("espacios")
				.request(MediaType.APPLICATION_JSON).get(String.class);
		client.close();

		GsonBuilder gsonBuilder = new GsonBuilder();

		JsonDeserializer<Espacio> deserializer = new JsonDeserializer<Espacio>() {

			@Override
			public Espacio deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
				JsonObject jsonObject = json.getAsJsonObject();

				Espacio e = new Espacio();

				// System.out.println("HOLA "+jsonObject.get("bloque").getAsString());

				e.setIdespacio(jsonObject.get("idespacio").getAsString());
				e.setNombrees(jsonObject.get("nombrees").getAsString());
				// e.setNombreen(jsonObject.get("nombreen").getAsString());
				// e.setNombrevl(jsonObject.get("nombrevl").getAsString());
				e.setBloque(jsonObject.get("bloque").getAsString());
				e.setBoundingbox(jsonObject.get("boundingbox").getAsString());
				e.setPiso(jsonObject.get("piso").getAsInt());
				e.setTipo(jsonObject.get("tipo").getAsString());
				e.setVisibilidad(jsonObject.get("visibilidad").getAsBoolean());

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
				/*
				 * edificio.setNombrees(edificioObj.get("nombrees").getAsString());
				 * edificio.setNombreen(edificioObj.get("nombreen").getAsString());
				 * edificio.setNombrevl(edificioObj.get("nombrevl").getAsString());
				 * edificio.setEnlace(edificioObj.get("enlace").getAsString());
				 * edificio.setXano(edificioObj.get("xano").getAsString());
				 * edificio.setTopleft(edificioObj.get("topleft").getAsString());
				 * edificio.setTopright(edificioObj.get("topright").getAsString());
				 * edificio.setBottomleft(edificioObj.get("bottomleft").getAsString());
				 */
				e.setEdificio(edificio);

				List<Panorama> lu = new ArrayList<Panorama>();
				JsonArray panoramaArray = jsonObject.getAsJsonArray("panoramas");
				for (JsonElement panorama : panoramaArray) {
					Panorama p = new Panorama();

					JsonObject panoramaObj = panorama.getAsJsonObject();

					p.setIdpanorama(panoramaObj.get("idpanorama").getAsString());
					p.setEnlace(panoramaObj.get("enlace").getAsString());
					if (panoramaObj.has("panorama")) {
						p.setPanorama(panoramaObj.get("panorama").getAsInt());
					}

					lu.add(p);
				}

				e.setPanoramas(lu);

				List<Profesor> lp = new ArrayList<Profesor>();
				if (jsonObject.has("profesores")) {
					JsonArray profesorArray = jsonObject.getAsJsonArray("profesores");
					for (JsonElement profesor : profesorArray) {
						Profesor p = new Profesor();

						JsonObject profesorObj = profesor.getAsJsonObject();
						p.setIdprofesor(profesorObj.get("idprofesor").getAsString());
						p.setUsuario(profesorObj.get("usuario").getAsString());
						p.setVisibilidad(profesorObj.get("visibilidad").getAsBoolean());
						lp.add(p);
					}
				}
				e.setProfesores(lp);

				return e;
			}

		};
		gsonBuilder.registerTypeAdapter(Espacio.class, deserializer);

		Type listType = new TypeToken<List<Espacio>>() {
		}.getType();

		Gson customGson = gsonBuilder.create();
		List<Espacio> jsonToEspacios = customGson.fromJson(espaciosJSON, listType);

		// System.out.println("Success Espacio: "+ jsonToEspacios.size());

		return jsonToEspacios;

	}



	public List<Profesor> getListaProfesores() {

		// System.out.println("EJB prof: ");
		client = ClientBuilder.newClient();
		String profesores = client.target(baseURI).path("profesores").request(MediaType.APPLICATION_JSON)
				.get(String.class);
		client.close();

		Type listType = new TypeToken<List<Profesor>>() {
		}.getType();
		List<Profesor> jsonToProfesorList = new Gson().fromJson(profesores, listType);

		return jsonToProfesorList;
	}

	public List<String> getNombresProfesores() {

		client = ClientBuilder.newClient();

		String profesores = client.target(baseURI).path("profesores").path("nombres")
				.request(MediaType.APPLICATION_JSON).get(String.class);

		client.close();

		// System.out.println("METHOD2 "+usuarios);

		Type listType = new TypeToken<List<String>>() {
		}.getType();
		List<String> jsonToUsuarioList = new Gson().fromJson(profesores, listType);

		return jsonToUsuarioList;

	}

	public String getProfesorSize() {
		client = ClientBuilder.newClient();

		String prof = client.target(baseURI).path("profesores").path("count").request(MediaType.APPLICATION_JSON)

				.get(String.class);
		client.close();

		return prof;
	}
	public Profesor getProfesor(String id) {

		client = ClientBuilder.newClient();
		String profesor = client.target(baseURI).path("profesores").path(id).request(MediaType.APPLICATION_JSON)
				.get(String.class);
		client.close();

		Profesor jsonToProfesor = new Gson().fromJson(profesor, Profesor.class);

		return jsonToProfesor;
	}

	public void createProfesor(Profesor u) {

		final Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
		final String gson = prettyGson.toJson(u);

		System.out.println("create profesor: " + gson);

		client = ClientBuilder.newClient();
		Response response = client.target(baseURI).path("profesores").request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(gson, MediaType.APPLICATION_JSON), Response.class);
		client.close();

	}

	public void updateProfesor(Profesor u) {

		final Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
		final String gson = prettyGson.toJson(u);

		client = ClientBuilder.newClient();
		Response response = client.target(baseURI).path("profesores").path(u.getIdprofesor())
				.request(MediaType.APPLICATION_JSON)
				.put(Entity.entity(gson, MediaType.APPLICATION_JSON), Response.class);
		client.close();

	}

	public void removeProfesor(String id) {
		client = ClientBuilder.newClient();

		Response response = client.target(baseURI).path("profesores").path(id).request(MediaType.APPLICATION_JSON)
				.delete();

		client.close();
	}

}

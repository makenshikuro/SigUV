package utils;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import entity.Espacio;
import entity.Profesor;

public class ProfesorSerializer implements JsonSerializer<Profesor> {

	@Override
	public JsonElement serialize(Profesor e, Type type, JsonSerializationContext context) {

		JsonObject object = new JsonObject();

		// System.out.println(e.getIdedificio());
		object.addProperty("idprofesor", e.getIdprofesor());
		object.addProperty("usuario", e.getUsuario());
		object.addProperty("visibilidad", e.getVisibilidad());

		JsonArray objectArray = new JsonArray();
		// System.out.println("Serializer: " + e.getEspacios().size());
		for (Espacio esp : e.getEspacios()) {
			JsonObject objectEspacio = new JsonObject();
			objectEspacio.addProperty("idespacio", esp.getIdespacio());
			/*
			 * objectEspacio.addProperty("bloque", esp.getBloque());
			 * objectEspacio.addProperty("piso", esp.getPiso() );
			 * objectEspacio.addProperty("nombrevl", esp.getNombrevl() );
			 * objectEspacio.addProperty("nombrees", esp.getNombrees() );
			 * objectEspacio.addProperty("nombreen", esp.getNombreen() );
			 * objectEspacio.addProperty("tipo", esp.getTipo() );
			 * objectEspacio.addProperty("visibilidad", esp.getVisibilidad() );
			 * objectEspacio.addProperty("boundingbox", esp.getBoundingbox() );
			 */

			objectArray.add(objectEspacio);
		}

		object.add("espacios", objectArray);

		return object;

	}

}

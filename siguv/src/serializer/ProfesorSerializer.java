package serializer;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import entity.Espacio;
import entity.Profesor;

public class ProfesorSerializer implements JsonSerializer<Profesor>{

	@Override
	public JsonElement serialize(Profesor e , Type type, JsonSerializationContext context) {


		JsonObject object = new JsonObject();

		//System.out.println(e.getIdedificio());
		object.addProperty("idprofesor", e.getIdprofesor());
		object.addProperty("usuario", e.getUsuario());
		object.addProperty("visibilidad", e.getVisibilidad());
		
		
		JsonArray objectArray = new JsonArray();
		
		//System.out.println("json Size: "+e.getEspacios().size());
		List<Espacio> espacios = e.getEspacios();
//		
		for(Espacio esp : espacios) {
			JsonObject objectEspacio = new JsonObject();
//			System.out.println("json ID: "+esp.getIdespacio());
			objectEspacio.addProperty("idespacio", esp.getIdespacio());
		
			objectArray.add(objectEspacio);
		}
		
		object.add("espacios", objectArray);

        return object;
		
	}

	

}

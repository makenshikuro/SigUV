package serializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import entity.Edificio;
import entity.Plano;
import entity.Usuario;

public class EdificioSerializer implements JsonSerializer<Edificio>{

	@Override
	public JsonElement serialize(Edificio e , Type type, JsonSerializationContext context) {


		JsonObject object = new JsonObject();
		
		
		
		//System.out.println(e.getIdedificio());
		object.addProperty("idedificio", e.getIdedificio() );
		object.addProperty("nombrevl", e.getNombrevl() );
		object.addProperty("nombrees", e.getNombrees() );
		object.addProperty("nombreen", e.getNombreen() );
		object.addProperty("direccion", e.getDireccion());
		object.addProperty("enlace", e.getEnlace());
		object.addProperty("telefono", e.getTelefono());
		object.addProperty("xano", e.getXano());
		object.addProperty("topleft", e.getTopleft());
		object.addProperty("topright", e.getTopright());
		object.addProperty("bottomleft", e.getBottomleft());
	

			JsonArray objectArray = new JsonArray();
			
			for(Usuario u : e.getUsuarios()) {
				JsonObject objectUser = new JsonObject();
				objectUser.addProperty("usuario", u.getUsuario() );
				/*objectUser.addProperty("nombre", u.getNombre() );
				objectUser.addProperty("uuid", u.getUuid() );
				objectUser.addProperty("role", u.getRole());
				objectUser.addProperty("password", u.getPassword());*/
				
				objectArray.add(objectUser);
			}
			
			object.add("usuarios", objectArray);
			
			JsonArray objectArrayPlano = new JsonArray();
			
			for(Plano u : e.getPlanos()) {
				JsonObject objectPlano = new JsonObject();
				
				objectPlano.addProperty("idplano", u.getId().getIdplano() );
				objectPlano.addProperty("nivel", u.getNivel() );
				objectPlano.addProperty("enlace", u.getEnlace() );
				/*objectUser.addProperty("nombre", u.getNombre() );
				objectUser.addProperty("uuid", u.getUuid() );
				objectUser.addProperty("role", u.getRole());
				objectUser.addProperty("password", u.getPassword());*/
				
				objectArrayPlano.add(objectPlano);
			}
			
			object.add("planos", objectArrayPlano);

				JsonObject objectCoordenada = new JsonObject();
				objectCoordenada.addProperty("idcoordenada", e.getCoordenada().getIdcoordenada() );
				objectCoordenada.addProperty("descripcion", e.getCoordenada().getDescripcion() );
				objectCoordenada.addProperty("latitud", e.getCoordenada().getLatitud() );
				objectCoordenada.addProperty("longitud", e.getCoordenada().getLongitud());

			object.add("coordenada", objectCoordenada);

        return object;
		
	}

}

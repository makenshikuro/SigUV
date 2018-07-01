package utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import entity.Edificio;
import entity.Usuario;

public class UsuarioSerializer implements JsonSerializer<Usuario>{

	@Override
	public JsonElement serialize(Usuario u, Type type, JsonSerializationContext context) {


		JsonObject object = new JsonObject();
		
		
		
		System.out.println(u.getUsuario());
		object.addProperty("usuario", u.getUsuario() );
		object.addProperty("nombre", u.getNombre() );
		object.addProperty("uuid", u.getUuid() );
		object.addProperty("Role", u.getRole());
		object.addProperty("password", u.getPassword());
		object.addProperty("email", u.getEmail());
		
		

        return object;
		
	}

}

package serializer;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import entity.Espacio;
import entity.Panorama;
import entity.Profesor;

public class EspacioSerializer implements JsonSerializer<Espacio>{

	@Override
	public JsonElement serialize(Espacio e , Type type, JsonSerializationContext context) {


		JsonObject object = new JsonObject();

		//System.out.println(e.getIdedificio());
		object.addProperty("idespacio", e.getIdespacio());
		object.addProperty("bloque", e.getBloque());
		object.addProperty("piso", e.getPiso() );
		object.addProperty("nombrevl", e.getNombrevl() );
		object.addProperty("nombrees", e.getNombrees() );
		object.addProperty("nombreen", e.getNombreen() );
		object.addProperty("tipo", e.getTipo() );
		object.addProperty("visibilidad", e.getVisibilidad() );
		object.addProperty("boundingbox", e.getBoundingbox() );
		

		JsonArray objectArray = new JsonArray();
			
			for(Panorama p : e.getPanoramas()) {
				JsonObject objectPano = new JsonObject();
				
				objectPano.addProperty("idpanorama", p.getIdpanorama());
				objectPano.addProperty("idespacio", e.getEdificio().getIdedificio());
				objectPano.addProperty("enlace", p.getEnlace() );
				objectPano.addProperty("panorama", p.getPanorama() );
				
				
				objectArray.add(objectPano);
			}
			
		object.add("panoramas", objectArray);

				JsonObject objectCoordenada = new JsonObject();
				objectCoordenada.addProperty("idcoordenada", e.getCoordenada().getIdcoordenada() );
				objectCoordenada.addProperty("descripcion", e.getCoordenada().getDescripcion() );
				objectCoordenada.addProperty("latitud", e.getCoordenada().getLatitud() );
				objectCoordenada.addProperty("longitud", e.getCoordenada().getLongitud());

		object.add("coordenada", objectCoordenada);
			

			JsonObject objectEdificio = new JsonObject();
			objectEdificio.addProperty("idedificio", e.getEdificio().getIdedificio() );
			objectEdificio.addProperty("nombrevl", e.getEdificio().getNombrevl() );
			objectEdificio.addProperty("nombrees", e.getEdificio().getNombrees() );
			objectEdificio.addProperty("nombreen", e.getEdificio().getNombreen() );
			objectEdificio.addProperty("direccion", e.getEdificio().getDireccion());
			objectEdificio.addProperty("enlace", e.getEdificio().getEnlace());
			objectEdificio.addProperty("telefono", e.getEdificio().getTelefono());
			objectEdificio.addProperty("xano", e.getEdificio().getXano());
			objectEdificio.addProperty("topleft", e.getEdificio().getTopleft());
			objectEdificio.addProperty("topright", e.getEdificio().getTopright());
			objectEdificio.addProperty("bottomleft", e.getEdificio().getBottomleft());

		object.add("edificio", objectEdificio);
		
		JsonArray profesorArray = new JsonArray();
		
		//System.out.println("json Size: "+e.getEspacios().size());
		List<Profesor> profesores = e.getProfesores();
//		
		for(Profesor p : profesores) {
			JsonObject objectProfesor = new JsonObject();
//			System.out.println("json ID: "+esp.getIdespacio());
			objectProfesor.addProperty("idprofesor", p.getIdprofesor());
			objectProfesor.addProperty("usuario", p.getUsuario());
			objectProfesor.addProperty("visibilidad", p.getVisibilidad());
		
			profesorArray.add(objectProfesor);
		}
		
		object.add("profesores", profesorArray);

        return object;
		
	}

}

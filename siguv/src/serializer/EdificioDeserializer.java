package serializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import entity.Coordenada;
import entity.Edificio;
import entity.Plano;
import entity.PlanoPK;
import entity.Usuario;

public class EdificioDeserializer implements JsonDeserializer<Edificio>{

	@Override
	public Edificio deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {


		JsonObject jsonObject = json.getAsJsonObject();
        
        Edificio e = new Edificio();
        
        e.setIdedificio(jsonObject.get("idedificio").getAsString());
        
        if(jsonObject.has("nombreen")) {
        	e.setNombreen(jsonObject.get("nombreen").getAsString());
        }
        if(jsonObject.has("nombrevl")) {
        	e.setNombrevl(jsonObject.get("nombrevl").getAsString());
        }
        if(jsonObject.has("nombrees")) {
        	e.setNombrevl(jsonObject.get("nombrees").getAsString());
        }       
        e.setDireccion(jsonObject.get("direccion").getAsString());
        e.setTelefono(jsonObject.get("telefono").getAsString());
        e.setEnlace(jsonObject.get("enlace").getAsString());
        e.setXano(jsonObject.get("xano").getAsString());
        e.setTopleft(jsonObject.get("topleft").getAsString());
        e.setTopright(jsonObject.get("topright").getAsString());
        e.setBottomleft(jsonObject.get("bottomleft").getAsString());
        
        //System.out.println("Deserializer: Edi Obj: "+e.toString());
        
        List<Usuario> lu = new ArrayList<Usuario>();
        JsonArray usuariosArray = jsonObject.getAsJsonArray("usuarios");
        for (JsonElement usuario : usuariosArray) {
        	Usuario u = new Usuario();
        	
            JsonObject usuarioObj = usuario.getAsJsonObject();
            u.setUsuario(usuarioObj.get("usuario").getAsString());
        	lu.add(u);
        }
        //System.out.println("Deserializer: usu Obj: "+e.toString());

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
        
        
        return e;
		
	}

	

}

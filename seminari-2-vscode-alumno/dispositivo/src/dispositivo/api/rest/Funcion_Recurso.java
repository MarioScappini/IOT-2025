package dispositivo.api.rest;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Options;
import org.restlet.resource.Put;

import dispositivo.interfaces.IDispositivo;
import dispositivo.interfaces.IFuncion;
import dispositivo.utils.MySimpleLogger;

public class Funcion_Recurso extends Recurso {
	
	
	public static final String ID = "FUNCION-ID";
	public static final String RUTA = Dispositivo_Recurso.RUTA + "/funcion/{" + Funcion_Recurso.ID + "}";

	public static JSONObject serialize(IFuncion f) {
		JSONObject jsonResult = new JSONObject();
		try {
			jsonResult.put("id", f.getId());
			jsonResult.put("estado", f.getEstado().name());
		} catch (JSONException e) {
		}
		return jsonResult;
	}
	
	
	protected IFuncion getFuncion() {
		IDispositivo dispositivo = this.getDispositivo_RESTApplication().getDispositivo();
		String funcionId = getAttribute(Funcion_Recurso.ID);
		return dispositivo.getFuncion(funcionId);
	}

	
    @Get
    public Representation get() {
    	
    	// Obtenemos la función indicada como parámetro en la Ruta
		IFuncion f = this.getFuncion();
		if ( f == null ) {
			return this.generateResponseWithErrorCode(Status.CLIENT_ERROR_NOT_FOUND);
		}
		
		// Construimos el mensaje de respuesta

    	JSONObject resultJSON = Funcion_Recurso.serialize(f);
		
		// Si todo va bien, devolvemos el resultado calculado
    	this.setStatus(Status.SUCCESS_OK);
        return new StringRepresentation(resultJSON.toString(), MediaType.APPLICATION_JSON);
    }
    
    
    
	@Put
	public Representation put(Representation entity) {
	
		IDispositivo dispositivo = this.getDispositivo_RESTApplication().getDispositivo();
		IFuncion f = this.getFuncion();
	
		if (f == null || dispositivo == null) {
			return this.generateResponseWithErrorCode(Status.CLIENT_ERROR_NOT_FOUND);
		}
	
		JSONObject payload = null;
		try {
			payload = new JSONObject(entity.getText());
			String action = payload.getString("accion");
	
			// Si el dispositivo está deshabilitado, solo se permite la acción "habilitar"
			if (!dispositivo.isHabilitado()) {
				if (action.equalsIgnoreCase("habilitar")) {
					dispositivo.habilitar();
				} else {
					MySimpleLogger.warn("Funcion-Recurso", "Dispositivo deshabilitado. Acción no permitida.");
					return this.generateResponseWithErrorCode(Status.CLIENT_ERROR_FORBIDDEN);
				}
			} else {
				// Dispositivo habilitado: ejecutar acción normalmente
				if (action.equalsIgnoreCase("encender"))
					f.encender();
				else if (action.equalsIgnoreCase("apagar"))
					f.apagar();
				else if (action.equalsIgnoreCase("parpadear"))
					f.parpadear();
				else if (action.equalsIgnoreCase("deshabilitar"))
					dispositivo.deshabilitar();
				else {
					MySimpleLogger.warn("Funcion-Recurso", "Acción '" + action + "' no reconocida.");
					return this.generateResponseWithErrorCode(Status.CLIENT_ERROR_BAD_REQUEST);
				}
			}
	
		} catch (JSONException | IOException e) {
			MySimpleLogger.warn("Funcion-Recurso", "Error procesando la petición.");
			return this.generateResponseWithErrorCode(Status.CLIENT_ERROR_BAD_REQUEST);
		}
	
		// Respuesta JSON con el estado actual de la función
		JSONObject resultJSON = Funcion_Recurso.serialize(f);
	
		this.setStatus(Status.SUCCESS_OK);
		return new StringRepresentation(resultJSON.toString(), MediaType.APPLICATION_JSON);
	}
    
    
    
	@Options
	public void describe() {
		Set<Method> meths = new HashSet<Method>();
		meths.add(Method.GET);
		meths.add(Method.PUT);
		meths.add(Method.OPTIONS);
		this.getResponse().setAllowedMethods(meths);
	}	
	
	
	

    
}

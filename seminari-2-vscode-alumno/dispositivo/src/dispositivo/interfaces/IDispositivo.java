package dispositivo.interfaces;

import java.util.Collection;

public interface IDispositivo {

	public String getId();
	
	public IDispositivo iniciar();
	public IDispositivo detener();
		
	public IDispositivo addFuncion(IFuncion f);
	public IFuncion getFuncion(String funcionId);
	public Collection<IFuncion> getFunciones();

	// Métodos añadidos para el ejercicio 5.4
	public void habilitar();
	public void deshabilitar();
	public boolean isHabilitado();
}

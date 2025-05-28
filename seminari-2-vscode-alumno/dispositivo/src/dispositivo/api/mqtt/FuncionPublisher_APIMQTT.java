package dispositivo.api.mqtt;

import dispositivo.interfaces.IFuncion;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import dispositivo.interfaces.Configuracion;

public class FuncionPublisher_APIMQTT {
    private final MqttClient mqttClient;
    private final IFuncion funcion;
    private final String dispositivoId;

    public FuncionPublisher_APIMQTT(MqttClient mqttClient, IFuncion funcion, String dispositivoId) {
        this.mqttClient = mqttClient;
        this.funcion = funcion;
        this.dispositivoId = dispositivoId;
    }

    public void publish_status() {
        String topic = Configuracion.TOPIC_BASE + "dispositivo/" + dispositivoId + "/funcion/" + funcion.getId() + "/info";
        String mensaje = "{\"id\":\"" + funcion.getId() + "\", \"estado\":\"" + funcion.getEstado().name() + "\"}";

        try {
            MqttMessage mqttMessage = new MqttMessage(mensaje.getBytes());
            mqttClient.publish(topic, mqttMessage);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}



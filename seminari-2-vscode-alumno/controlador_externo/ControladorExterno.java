import org.eclipse.paho.client.mqttv3.*;

public class ControladorExterno implements MqttCallback {

    private static final String BROKER = "tcp://localhost:1883";
    private static final String ID_FUNCION = "f1";
    private static final String DISPOSITIVO_MAESTRO = "ttmi050";
    private static final String[] DISPOSITIVOS_CONTROLADOS = {"ttmi051", "ttmi052"};

    private MqttClient cliente;

    public static void main(String[] args) throws Exception {
        new ControladorExterno().start();
    }

    public void start() throws Exception {
        cliente = new MqttClient(BROKER, MqttClient.generateClientId());
        cliente.setCallback(this);
        cliente.connect();
        String topic = "dispositivo/" + DISPOSITIVO_MAESTRO + "/funcion/" + ID_FUNCION + "/info";
        cliente.subscribe(topic);
        System.out.println("Escuchando en: " + topic);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String payload = new String(message.getPayload());
        org.json.JSONObject json = new org.json.JSONObject(payload);
        String estado = json.getString("estado");

        String accion = switch (estado) {
            case "ON" -> "encender";
            case "OFF" -> "apagar";
            case "BLINK" -> "parpadear";
            default -> null;
        };

        if (accion != null) {
            for (String dispositivo : DISPOSITIVOS_CONTROLADOS) {
                String topicControl = "dispositivo/" + dispositivo + "/funcion/" + ID_FUNCION + "/comandos";
                String comando = "{\"accion\":\"" + accion + "\"}";
                cliente.publish(topicControl, new MqttMessage(comando.getBytes()));
                System.out.println("Publicado en " + topicControl + ": " + comando);
            }
        }
    }

    @Override public void connectionLost(Throwable cause) { }
    @Override public void deliveryComplete(IMqttDeliveryToken token) { }

}


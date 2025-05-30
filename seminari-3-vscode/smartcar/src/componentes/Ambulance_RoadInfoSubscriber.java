package componentes;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;
import utils.MySimpleLogger;

public class Ambulance_RoadInfoSubscriber extends AmbulanceMqttClient {

    protected Ambulance ambulance;

    public Ambulance_RoadInfoSubscriber(String clientId, Ambulance ambulance, String MQTTBrokerURL) {
        super(clientId, ambulance, MQTTBrokerURL);
        this.ambulance = ambulance;
    }

    @Override
    public void connect() {
        super.connect();

        String segment = this.ambulance.getCurrentPlace().getRoad();

        // M4: Trafico
        String trafficTopic = "es/upv/pros/tatami/smartcities/traffic/PTPaterna/road/" + segment + "/traffic";

        // M1: estado de carretera M2: accidente
        String infoTopic = "es/upv/pros/tatami/smartcities/traffic/PTPaterna/road/" + segment + "/info";

        this.subscribe(trafficTopic);
        this.subscribe(infoTopic);
    }

    

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

        super.messageArrived(topic, message);

        String payload = new String(message.getPayload());

        try {
            JSONObject json = new JSONObject(payload);

            // M2, accidente
            if (json.has("action") && json.getString("action").equals("ACCIDENT")) {
                System.out.println("Accidente detectado en el tramo " + ambulance.getCurrentPlace().getRoad() + "");
                
                
                String roadSegment = json.getString("road-segment");
                int position = json.getInt("position");

                System.out.println("Ambulancia recibe alerta. Dirigiéndose a " + roadSegment + ", km " + position);

                RoadPlace destino = new RoadPlace(roadSegment, position);
                ambulance.setCurrentRoadPlace(destino);
                ambulance.setEmergencyStatus(true);
                ambulance.publishVehicleIn(); 
            
            
            // M1, estado de carretera
            }else if (json.has("type") && json.getString("type").equals("ROAD_STATUS")) {
                JSONObject msgObject = json.getJSONObject("msg");
        
                // Check and retrieve the status field
                if (msgObject.has("status") && msgObject.getString("status").equals("Congested") && ambulance.isOnEmergencyMode) {
                    String status = msgObject.getString("status");
                    System.out.println("Status: " + status);
                    ambulance.changeTrafficLightEnter();
                    
                }
            } 
           

        } catch (Exception e) {
            System.err.println("Error procesando mensaje recibido:");
            e.printStackTrace();
        }
    }
}


package componentes;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONException;
import org.json.JSONObject;
import utils.MySimpleLogger;

public class Ambulance_LocationNotifier extends MyMqttClient {
	
	public Ambulance_LocationNotifier(String clientId, Ambulance ambulance, String brokerURL) {
		super(clientId, ambulance, brokerURL);
	}
	
	
	public void alert(String id, String event, RoadPlace location) {

		String myTopic =  "es/upv/pros/tatami/smartcities/traffic/iot-2025-1/road/" + location.getRoad() + "/alerts";

		MqttTopic topic = myClient.getTopic(myTopic);


		JSONObject pubMsg = new JSONObject();
		try {
			pubMsg.put("vehicle", id);
			pubMsg.put("event", event);
			pubMsg.put("road", location.getRoad());
			pubMsg.put("kp", location.getKm());
	   		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
   		int pubQoS = 0;
		MqttMessage message = new MqttMessage(pubMsg.toString().getBytes());
    	message.setQos(pubQoS);
    	message.setRetained(false);

    	// Publish the message
    	MySimpleLogger.trace(this.clientId, "Publishing to topic " + topic + " qos " + pubQoS);
    	MqttDeliveryToken token = null;
    	try {
    		// publish message to broker
			token = topic.publish(message);
			MySimpleLogger.trace(this.clientId, pubMsg.toString());
	    	// Wait until the message has been delivered to the broker
			token.waitForCompletion();
			Thread.sleep(100);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    		    	

	}
	
}
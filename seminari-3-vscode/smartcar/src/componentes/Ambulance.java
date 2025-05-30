package componentes;

import utils.MySimpleLogger;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;


public class Ambulance extends SmartCar {
    
    protected Ambulance_LocationNotifier notifier = null;    
    protected Ambulance_RoadInfoSubscriber subscriber = null; 
    protected boolean isOnEmergencyMode = false;
    private int previousRoadSpeed =0;

    public Ambulance(String id, String brokerURL) {
        super(id, brokerURL);

        this.notifier = new Ambulance_LocationNotifier(id + ".location-notifier", this, this.brokerURL);
        this.notifier.connect();
    }

    @Override
    public void setCurrentRoadPlace(RoadPlace rp) {
        this.rp = rp;

        // Si ya hay un suscriptor, lo desconectamos
        if (this.subscriber != null) {
            this.subscriber.disconnect();
        }

        // Crear nuevo suscriptor para este tramo
        this.subscriber = new Ambulance_RoadInfoSubscriber(this.smartCarID + ".road-subscriber", this, this.brokerURL);
        this.subscriber.connect();
    }


    public void changeTrafficLightEnter() {
        
        String topic = "es/upv/pros/tatami/smartcities/traffic/PTPaterna/road/"+this.rp.getRoad()+"/signals";
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);

        String payload = "{\n" +
            "  \"id\":\""+this.smartCarID+"\",\n" +
            "  \"type\":\"TRAFFIC\",\n" +
            "  \"timestamp\":\""+formattedNow+"\",\n" +
            "  \"msg\":\"{\n" +
                "  \"action\":\"VEHICLE_IN\",\n" +
                "  \"road\":\"" + this.rp.getRoad().substring(0, 2) + "\",\n" +
                "  \"road-segment\":\"" + this.rp.getRoad() + "\",\n" +
                "  \"position\":" + this.rp.getKm() + ",\n" +
                "  \"role\":\"MedicalAssitance\",\n" +
                "}"+
            "}";

        this.notifier.publish(topic, payload);
    }

    public void setEmergencyStatus(boolean status){
        this.isOnEmergencyMode = status;
        String topic = "es/upv/pros/tatami/smartcities/traffic/PTPaterna/road/" + this.rp.getRoad() + "/traffic";
        if (this.isOnEmergencyMode){
            this.previousRoadSpeed = getRoadCurrentSpeed();
            System.out.println(this.previousRoadSpeed);
    
            String payload = "{\n" +
            "  \"action\":\"SPEED_LIMIT\",\n" +
            "  \"road-segment\":\"" + this.rp.getRoad() + "\",\n" +
            "  \"speed-limit\": \"0\"\n" +
            "}";
    
    
            this.notifier.publish(topic, payload);
        }else{
            String payload = "{\n" +
            "  \"action\":\"SPEED_LIMIT\",\n" +
            "  \"road-segment\":\"" + this.rp.getRoad() + "\",\n" +
            "  \"speed-limit\": "+this.previousRoadSpeed+"\n" +
            "}";
    
    
            this.notifier.publish(topic, payload);
        }
    }

    @Override
    public void publishVehicleIn() {
        
        String topic = "es/upv/pros/tatami/smartcities/traffic/PTPaterna/road/" + this.rp.getRoad() + "/traffic";

        String payload = "{\n" +
            "  \"action\":\"VEHICLE_IN\",\n" +
            "  \"road\":\"" + this.rp.getRoad().substring(0, 2) + "\",\n" +
            "  \"road-segment\":\"" + this.rp.getRoad() + "\",\n" +
            "  \"vehicle-id\":\"" + this.smartCarID + "\",\n" +
            "  \"position\":" + this.rp.getKm() + ",\n" +
            "  \"role\":\"MedicalAssitance\"\n" +
            "}";

        this.notifier.publish(topic, payload);

        if (this.isOnEmergencyMode){
            this.previousRoadSpeed = getRoadCurrentSpeed();
            System.out.println(this.previousRoadSpeed);
    
             payload = "{\n" +
            "  \"action\":\"SPEED_LIMIT\",\n" +
            "  \"road-segment\":\"" + this.rp.getRoad() + "\",\n" +
            "  \"speed-limit\": \"0\"\n" +
            "}";
    
    
            this.notifier.publish(topic, payload);
        }
        
    }


    @Override
    public void publishVehicleOut() {
        String topic = "es/upv/pros/tatami/smartcities/traffic/PTPaterna/road/"+this.rp.getRoad()+"/signals";
        
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);

        
        String payload = "{\n" +
            "  \"id\":\""+this.smartCarID+"\",\n" +
            "  \"type\":\"MedicalVehicle\",\n" +
            "  \"timestamp\":\""+formattedNow+"\",\n" +
            "  \"action\":\"VEHICLE_OUT\",\n" +
            "  \"road\":\"" + this.rp.getRoad().substring(0, 2) + "\",\n" +
            "  \"road-segment\":\"" + this.rp.getRoad() + "\",\n" +
            "  \"position\":" + this.rp.getKm() + ",\n" +
            "}";

        this.notifier.publish(topic, payload);

        topic = "es/upv/pros/tatami/smartcities/traffic/PTPaterna/road/"+this.rp.getRoad()+"/traffic";
        payload = "{\n" +
        "  \"action\":\"SPEED_LIMIT\",\n" +
        "  \"road-segment\":\"" + this.rp.getRoad() + "\",\n" +
        "  \"speed-limit\": "+this.previousRoadSpeed+"\n" +
        "}";


        this.notifier.publish(topic, payload);

    }


}

package componentes;

import utils.MySimpleLogger;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;

enum AmbulanceStatus{
    NONE,
    EMERGENCY,
}

public class Ambulance extends SmartCar {
     
    protected AmbulanceStatus status = null;    
    protected Ambulance_LocationNotifier notifier = null;    

    public Ambulance(String id, String brokerURL) {
        super(id, brokerURL);

        this.notifier = new Ambulance_LocationNotifier(id + ".location-notifier", this, this.brokerURL);
        this.notifier.connect();
    }

    public void setAmbulanceStatus(AmbulanceStatus status){
        this.status = status;
        String topic = "iot/2025/road/"+this.rp.getRoad()+"/alerts";

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);

        String payload = "{\n" +
            "  \"id\":\""+this.smartCarID+"\",\n" +
            "  \"type\":\"MedicalVehicle\",\n" +
            "  \"timestamp\":\""+formattedNow+"\",\n" +
            "  \"action\":\"""\",\n" +
            "  \"road\":\"" + this.rp.getRoad().substring(0, 2) + "\",\n" +
            "  \"road-segment\":\"" + this.rp.getRoad() + "\",\n" +
            "  \"vehicle-id\":\"" + this.smartCarID + "\",\n" +
            "  \"position\":" + this.rp.getKm() + ",\n" +
            "}";

        this.notifier.publish(topic, payload);
    }

    public AmbulanceStatus getAmbulanceStatus(){
        return this.status;
    }

    @Override
    public void setCurrentRoadPlace(RoadPlace rp) {
        this.rp = rp;
    }

    @Override
    public void changeKm(int km) {
        this.getCurrentPlace().setKm(km);
    }

    // Publicar que entra en un segmento
    @Override
    public void publishVehicleIn() {
        
        String topic = "iot/2025/road/"+this.rp.getRoad()+"/traffic";

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);

        String payload = "{\n" +
            "  \"id\":\""+this.smartCarID+"\",\n" +
            "  \"type\":\"MedicalVehicle\",\n" +
            "  \"timestamp\":\""+formattedNow+"\",\n" +
            "  \"action\":\"VEHICLE_IN\",\n" +
            "  \"road\":\"" + this.rp.getRoad().substring(0, 2) + "\",\n" +
            "  \"road-segment\":\"" + this.rp.getRoad() + "\",\n" +
            "  \"vehicle-id\":\"" + this.smartCarID + "\",\n" +
            "  \"position\":" + this.rp.getKm() + ",\n" +
            "}";

        this.notifier.publish(topic, payload);
    }

    // Publicar que sale del segmento
    @Override
    public void publishVehicleOut() {
        String topic = "iot/2025/road/"+this.rp.getRoad()+"/traffic";
        
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);

        
        String payload = "{\n" +
            "  \"id\":\""+this.smartCarID+"\",\n" +
            "  \"type\":\"EMERGENCY\",\n" +
            "  \"timestamp\":\""+formattedNow+"\",\n" +
            "  \"action\":\"VEHICLE_OUT\",\n" +
            "  \"road\":\"" + this.rp.getRoad().substring(0, 2) + "\",\n" +
            "  \"road-segment\":\"" + this.rp.getRoad() + "\",\n" +
            "  \"vehicle-id\":\"" + this.smartCarID + "\",\n" +
            "  \"position\":" + this.rp.getKm() + ",\n" +
            "  \"role\":\""+this.status+"\"\n" +
            "}";

        this.notifier.publish(topic, payload);
    }


}

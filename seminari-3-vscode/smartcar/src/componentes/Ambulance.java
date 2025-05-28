package componentes;

import utils.MySimpleLogger;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;

enum AmbulanceStatus{
    NONE,
    ACTIVE,
}

public class Ambulance {

    protected String brokerURL = null;

    protected String ambulanceId = null;
    protected AmbulanceStatus status = null;
    protected RoadPlace rp = null; // simula la ubicación actual del vehículo
    protected Ambulance_LocationNotifier notifier = null;    

    public Ambulance(String id, String brokerURL) {
        this.setambulanceId(id);
        this.brokerURL = brokerURL;

        this.setCurrentRoadPlace(new RoadPlace("R5s1", 0));

        this.notifier = new Ambulance_LocationNotifier(id + ".location-notifier", this, this.brokerURL);
        this.notifier.connect();
    }

    public void setambulanceId(String ambulanceId) {
        this.ambulanceId = ambulanceId;
    }

    public void setAmbulanceStatus(AmbulanceStatus status){
        this.status = status;
    }

    public AmbulanceStatus getAmbulanceStatus(){
        return this.status;
    }

    public String getambulanceId() {
        return ambulanceId;
    }

    public void setCurrentRoadPlace(RoadPlace rp) {
        this.rp = rp;

        // Si ya hay un suscriptor, lo desconectamos
        // if (this.subscriber != null) {
        //     this.subscriber.disconnect();
        // }

        // Crear nuevo suscriptor para este tramo
        // this.subscriber = new SmartCar_RoadInfoSubscriber(this.ambulanceId + ".road-subscriber", this, this.brokerURL);
        // this.subscriber.connect();
    }

    public RoadPlace getCurrentPlace() {
        return rp;
    }

    public void changeKm(int km) {
        if (km > this.rp.getKm() + 5){
            publishVehicleIn();
        }
        this.getCurrentPlace().setKm(km);
    }

    public void getIntoRoad(String road, int km) {
        this.getCurrentPlace().setRoad(road);
        this.getCurrentPlace().setKm(km);
    }

    public void publishChangeLocation() {
        
        String topic = "iot/2025/road/"+this.rp.getRoad()+"/traffic";

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);

        String payload = "{\n" +
            "  \"id\":\""+this.ambulanceId+"\",\n" +
            "  \"type\":\"EMERGENCY\",\n" +
            "  \"timestamp\":\""+formattedNow+"\",\n" +
            "  \"action\":\"LOCATION_CHANGE\",\n" +
            "  \"road\":\"" + this.rp.getRoad().substring(0, 2) + "\",\n" +
            "  \"road-segment\":\"" + this.rp.getRoad() + "\",\n" +
            "  \"vehicle-id\":\"" + this.ambulanceId + "\",\n" +
            "  \"position\":" + this.rp.getKm() + ",\n" +
            "  \"role\":\""+this.status+"\"\n" +
            "}";

        this.notifier.publish(topic, payload);
    }

    // Publicar que entra en un segmento
    public void publishVehicleIn() {
        
        String topic = "iot/2025/road/"+this.rp.getRoad()+"/traffic";

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);

        String payload = "{\n" +
            "  \"id\":\""+this.ambulanceId+"\",\n" +
            "  \"type\":\"EMERGENCY\",\n" +
            "  \"timestamp\":\""+formattedNow+"\",\n" +
            "  \"action\":\"VEHICLE_IN\",\n" +
            "  \"road\":\"" + this.rp.getRoad().substring(0, 2) + "\",\n" +
            "  \"road-segment\":\"" + this.rp.getRoad() + "\",\n" +
            "  \"vehicle-id\":\"" + this.ambulanceId + "\",\n" +
            "  \"position\":" + this.rp.getKm() + ",\n" +
            "  \"role\":\""+this.status+"\"\n" +
            "}";

        this.notifier.publish(topic, payload);
    }

    // Publicar que sale del segmento
    public void publishVehicleOut() {
        String topic = "iot/2025/road/"+this.rp.getRoad()+"/traffic";
        
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);

        
        String payload = "{\n" +
            "  \"id\":\""+this.ambulanceId+"\",\n" +
            "  \"type\":\"EMERGENCY\",\n" +
            "  \"timestamp\":\""+formattedNow+"\",\n" +
            "  \"action\":\"VEHICLE_OUT\",\n" +
            "  \"road\":\"" + this.rp.getRoad().substring(0, 2) + "\",\n" +
            "  \"road-segment\":\"" + this.rp.getRoad() + "\",\n" +
            "  \"vehicle-id\":\"" + this.ambulanceId + "\",\n" +
            "  \"position\":" + this.rp.getKm() + ",\n" +
            "  \"role\":\""+this.status+"\"\n" +
            "}";

        this.notifier.publish(topic, payload);
    }


}

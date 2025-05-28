package componentes;

public class TrafficSignSpeedLimit {

    private String id;
    private String brokerURL;
    private String roadSegment;
    private int newSpeedLimit;

    private SmartCar_InicidentNotifier notifier;

    public TrafficSignSpeedLimit(String id, String brokerURL, String roadSegment, int newSpeedLimit) {
        this.id = id;
        this.brokerURL = brokerURL;
        this.roadSegment = roadSegment;
        this.newSpeedLimit = newSpeedLimit;

        this.notifier = new SmartCar_InicidentNotifier(id + ".speed-limiter", null, brokerURL);
        this.notifier.connect();
    }

    public void publishSpeedLimit() {
        String topic = "es/upv/pros/tatami/smartcities/traffic/PTPaterna/road/" + roadSegment + "/traffic";

        String payload = "{\n" +
            "  \"action\":\"SPEED_LIMIT\",\n" +
            "  \"road-segment\":\"" + roadSegment + "\",\n" +
            "  \"speed-limit\":" + newSpeedLimit + "\n" +
            "}";

        this.notifier.publish(topic, payload);
        System.out.println("Límite de velocidad actualizado a " + newSpeedLimit + " km/h en " + roadSegment);
    }
}

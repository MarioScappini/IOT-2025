import componentes.SmartCar;
import componentes.TrafficSignSpeedLimit;
import java.util.ArrayList;

public class SmartCarStarterApp {
    public static void main(String[] args) throws Exception {

        if (args.length < 3) {
            System.out.println("Usage: SmartCarStarterApp <numCars> <brokerURL> <newSpeedLimit>");
            System.exit(1);
        }

        int numCars = Integer.parseInt(args[0]);       // Número de vehículos
        String brokerURL = args[1];                    // Broker MQTT
        int newSpeedLimit = Integer.parseInt(args[2]); // Nuevo límite de velocidad

        ArrayList<SmartCar> vehicles = new ArrayList<>();

        // Crear e iniciar varios vehículos
        for (int i = 1; i <= numCars; i++) {
            String smartCarID = "vehiculo" + i;
            SmartCar car = new SmartCar(smartCarID, brokerURL);
            car.getIntoRoad("R5s1", 100 + i);  // Cada uno en un km distinto
            car.publishVehicleIn();
            vehicles.add(car);
            System.out.println(smartCarID + " ha entrado en el tramo.");
        }

        // Añadir la señal de tráfico con nuevo límite
        TrafficSignSpeedLimit sign = new TrafficSignSpeedLimit("senalVelocidad", brokerURL, "R5s1", newSpeedLimit);
        sign.publishSpeedLimit();

        // Esperar unos segundos
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Uno de los vehículos reporta un accidente
        SmartCar reporter = vehicles.get(0);
        reporter.notifyIncident("ACCIDENT");
        System.out.println(reporter.getSmartCarID() + " ha reportado un accidente en el tramo.");

        // Esperar unos segundos más para que los demás reciban el aviso
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Todos los vehículos salen
        for (SmartCar car : vehicles) {
            car.publishVehicleOut();
            System.out.println(car.getSmartCarID() + " ha salido del tramo.");
        }

        System.out.println("Todos los vehículos han completado su paso por el tramo R5s1.");
    }
}

import componentes.Ambulance;
import componentes.SmartCar;
import componentes.TrafficSignSpeedLimit;
import java.util.ArrayList;

public class SmartCarStarterApp {
    public static void main(String[] args) throws Exception {

        // if (args.length < 3) {
        //     System.out.println("Usage: SmartCarStarterApp <numCars> <brokerURL> <newSpeedLimit>");
        //     System.exit(1);
        // }

        int numCars = 1;       // Número de vehículos
        String brokerURL = "tcp://tambori.dsic.upv.es:1883";                    // Broker MQTT
        int newSpeedLimit = 10; // Nuevo límite de velocidad

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

        // // Añadir la señal de tráfico con nuevo límite
        // TrafficSignSpeedLimit sign = new TrafficSignSpeedLimit("senalVelocidad", brokerURL, "R5s1", newSpeedLimit);
        // sign.publishSpeedLimit();

        
        Ambulance ambulance = new Ambulance("ambulance", brokerURL);
        ambulance.getIntoRoad("R5s1", 99);
        ambulance.publishVehicleIn();
        System.out.println("Ambulancia ha entrado en el tramo.");

        // // Esperar unos segundos
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ambulance.publishVehicleOut();


        // // Uno de los vehículos reporta un accidente
        // SmartCar reporter = vehicles.get(0);
        // reporter.notifyIncident("ACCIDENT");
        // System.out.println(reporter.getSmartCarID() + " ha reportado un accidente en el tramo.");

        // // Esperar unos segundos más para que los demás reciban el aviso
        // try {
        //     Thread.sleep(8000);
        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }

        // // Todos los vehículos salen
        // for (SmartCar car : vehicles) {
        //     car.publishVehicleOut();
        //     System.out.println(car.getSmartCarID() + " ha salido del tramo.");
        // }

        // System.out.println("Todos los vehículos han completado su paso por el tramo R5s1.");
    }
}
import componentes.Ambulance;
import componentes.SmartCar;
import componentes.TrafficSignSpeedLimit;
import java.util.ArrayList;

public class SmartCarStarterApp {
    public static void main(String[] args) throws Exception {

        if (args.length < 2) {
            System.out.println("Usage: SmartCarStarterApp <numCars> <brokerURL>");
            System.exit(1);
        }

        int numCars = Integer.parseInt(args[0]);       // Número de vehículos
        String brokerURL = args[1];                    // Broker MQTT 
        
        Ambulance ambulance = new Ambulance("ambulancia1", brokerURL);
        ambulance.getIntoRoad("R5s1", 90); 
        ambulance.publishVehicleIn();
        System.out.println("ambulancia1 ha entrado en el tramo.");

        // Esperar unos segundos
        try {
            for(int i =0;i<10;i++){
                Thread.sleep(1000);
                ambulance.changeKm(i*2);
            }
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        
        ambulance.publishVehicleOut();
        System.out.println(ambulance.getambulanceId() + " ha salido del tramo.");
        

        System.out.println("Todos los vehículos han completado su paso por el tramo R5s1.");
    }
}

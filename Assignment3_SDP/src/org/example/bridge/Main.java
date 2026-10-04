package org.example.bridge;

public class Main {

    public static void main(String[] args) {
        Powertrain electricPowertrain = new ElectricPowertrain();
        Powertrain gasolinePowertrain = new GasolinePowertrain();

        System.out.println("=== Eco mode with electric powertrain ===");

        DriveMode ecoMode = new EcoMode(electricPowertrain);
        ecoMode.drive();

        System.out.println("\n=== Sport mode with gasoline powertrain ===");

        DriveMode sportMode = new SportMode(gasolinePowertrain);
        sportMode.drive();

        System.out.println(
                "\n=== Switching Sport mode to electric powertrain ==="
        );

        sportMode.setPowertrain(electricPowertrain);
        sportMode.drive();
    }
}
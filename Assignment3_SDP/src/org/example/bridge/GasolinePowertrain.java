package org.example.bridge;

public class GasolinePowertrain implements Powertrain {

    @Override
    public void start() {
        System.out.println("Gasoline engine started.");
    }

    @Override
    public void setPowerLevel(int powerPercentage) {
        System.out.println(
                "Gasoline engine power set to " + powerPercentage + "%."
        );
    }

    @Override
    public void stop() {
        System.out.println("Gasoline engine stopped.");
    }
}
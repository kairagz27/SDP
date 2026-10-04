package org.example.bridge;

public class ElectricPowertrain implements Powertrain {

    @Override
    public void start() {
        System.out.println("Electric powertrain started silently.");
    }

    @Override
    public void setPowerLevel(int powerPercentage) {
        System.out.println(
                "Electric motor power set to " + powerPercentage + "%."
        );
    }

    @Override
    public void stop() {
        System.out.println("Electric powertrain stopped.");
    }
}
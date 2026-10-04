package org.example.bridge;

public class EcoMode extends DriveMode {

    private static final int ECO_POWER_PERCENTAGE = 40;

    public EcoMode(Powertrain powertrain) {
        super(powertrain);
    }

    @Override
    public void drive() {
        System.out.println("\nEco mode activated.");

        getPowertrain().start();
        getPowertrain().setPowerLevel(ECO_POWER_PERCENTAGE);

        System.out.println("The car is driving with reduced energy consumption.");

        getPowertrain().stop();
    }
}
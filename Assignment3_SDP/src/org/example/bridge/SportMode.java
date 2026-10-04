package org.example.bridge;

public class SportMode extends DriveMode {

    private static final int SPORT_POWER_PERCENTAGE = 100;

    public SportMode(Powertrain powertrain) {
        super(powertrain);
    }

    @Override
    public void drive() {
        System.out.println("\nSport mode activated.");

        getPowertrain().start();
        getPowertrain().setPowerLevel(SPORT_POWER_PERCENTAGE);

        System.out.println("The car is driving with maximum performance.");

        getPowertrain().stop();
    }
}
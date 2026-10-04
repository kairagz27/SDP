package org.example.bridge;

public abstract class DriveMode {

    private Powertrain powertrain;

    public DriveMode(Powertrain powertrain) {
        this.powertrain = powertrain;
    }

    protected Powertrain getPowertrain() {
        return powertrain;
    }

    public void setPowertrain(Powertrain powertrain) {
        this.powertrain = powertrain;
    }

    public abstract void drive();
}
package org.example.bridge;

public interface Powertrain {

    void start();

    void setPowerLevel(int powerPercentage);

    void stop();
}
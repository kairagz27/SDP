import factories.*;
import client.ComputerSetupClient;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- Part B: Abstract Factory ---");

        PeripheralFactory gamingFactory = new GamingPeripheralFactory();
        ComputerSetupClient gamingSetup = new ComputerSetupClient(gamingFactory);
        gamingSetup.testSetup();

        System.out.println("--------------------------------");

        PeripheralFactory officeFactory = new OfficePeripheralFactory();
        ComputerSetupClient officeSetup = new ComputerSetupClient(officeFactory);
        officeSetup.testSetup();
    }
}
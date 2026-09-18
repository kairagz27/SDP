package client;
import factories.PeripheralFactory;
import products.Keyboard;
import products.Mouse;

public class ComputerSetupClient {
    private Keyboard keyboard;
    private Mouse mouse;

    public ComputerSetupClient(PeripheralFactory factory) {
        if (factory == null) {
            throw new IllegalArgumentException("Factory cannot be null");
        }
        this.keyboard = factory.createKeyboard();
        this.mouse = factory.createMouse();
    }

    public void testSetup() {
        keyboard.type();
        mouse.click();
    }
}
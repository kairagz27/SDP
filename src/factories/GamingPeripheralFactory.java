package factories;
import products.*;

public class GamingPeripheralFactory implements PeripheralFactory {
    @Override
    public Keyboard createKeyboard() { return new GamingKeyboard(); }
    @Override
    public Mouse createMouse() { return new GamingMouse(); }
}
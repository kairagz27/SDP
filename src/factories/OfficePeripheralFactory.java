package factories;
import products.*;

public class OfficePeripheralFactory implements PeripheralFactory {
    @Override
    public Keyboard createKeyboard() { return new OfficeKeyboard(); }
    @Override
    public Mouse createMouse() { return new OfficeMouse(); }
}
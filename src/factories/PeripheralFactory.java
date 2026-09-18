package factories;
import products.Keyboard;
import products.Mouse;

public interface PeripheralFactory {
    Keyboard createKeyboard();
    Mouse createMouse();
}
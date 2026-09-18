package factories;
import products.Keyboard;
import products.OfficeKeyboard;

public class OfficeKeyboardFactory extends KeyboardFactory {
    @Override
    public Keyboard createKeyboard() {
        return new OfficeKeyboard();
    }
}
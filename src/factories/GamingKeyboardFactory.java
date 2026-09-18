package factories;
import products.Keyboard;
import products.GamingKeyboard;

public class GamingKeyboardFactory extends KeyboardFactory {
    @Override
    public Keyboard createKeyboard() {
        return new GamingKeyboard();
    }
}
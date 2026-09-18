import factories.KeyboardFactory;
import factories.GamingKeyboardFactory;
import products.Keyboard;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- Part A: Factory Method ---");
        KeyboardFactory factory = new GamingKeyboardFactory();
        Keyboard keyboard = factory.createKeyboard();
        keyboard.type();
    }
}
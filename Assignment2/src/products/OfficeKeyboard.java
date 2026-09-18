package products;

public class OfficeKeyboard implements Keyboard {
    @Override
    public void type() {
        System.out.println("Typing silently with membrane switches.");
    }
}
public class Bicycle {
    private String frame;
    private String groupset;
    private String brakes;
    private String chainring;

    // Конструктор доступен только внутри пакета (для Builder)
    Bicycle(String frame, String groupset, String brakes, String chainring) {
        this.frame = frame;
        this.groupset = groupset;
        this.brakes = brakes;
        this.chainring = chainring;
    }

    @Override
    public String toString() {
        return "Bicycle Configuration: " +
                "Frame = '" + frame + '\'' +
                ", Groupset = '" + groupset + '\'' +
                ", Brakes = '" + brakes + '\'' +
                ", Chainring = '" + chainring + '\'';
    }
}
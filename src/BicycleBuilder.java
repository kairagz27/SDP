public class BicycleBuilder {
    private String frame;
    private String groupset;
    private String brakes;
    private String chainring;

    private static final String ERROR_MISSING_FRAME = "Bicycle frame is mandatory for assembly.";

    // Fluent API: каждый setter возвращает текущий экземпляр Builder
    public BicycleBuilder setFrame(String frame) {
        this.frame = frame;
        return this;
    }

    public BicycleBuilder setGroupset(String groupset) {
        this.groupset = groupset;
        return this;
    }

    public BicycleBuilder setBrakes(String brakes) {
        this.brakes = brakes;
        return this;
    }

    public BicycleBuilder setChainring(String chainring) {
        this.chainring = chainring;
        return this;
    }

    // Валидация перед созданием объекта
    public Bicycle build() {
        if (this.frame == null || this.frame.isEmpty()) {
            throw new IllegalStateException(ERROR_MISSING_FRAME);
        }
        return new Bicycle(frame, groupset, brakes, chainring);
    }
}
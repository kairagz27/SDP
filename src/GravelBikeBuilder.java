public class GravelBikeBuilder implements BicycleBuilder {

    private Bicycle bicycle;

    private static final String ERROR_MISSING_FRAME = "Frame is strictly required for assembly.";

    public GravelBikeBuilder() {
        this.bicycle = new Bicycle();
    }

    @Override
    public BicycleBuilder buildFrame() {
        bicycle.setFrame("52cm Gravel Geometry");
        return this;
    }

    @Override
    public BicycleBuilder buildGroupset() {
        bicycle.setGroupset("L-Twoo A7");
        return this;
    }

    @Override
    public BicycleBuilder buildBrakes() {
        bicycle.setBrakes("Shimano MT200");
        return this;
    }

    @Override
    public BicycleBuilder buildChainring() {
        bicycle.setChainring("40T Narrow Wide");
        return this;
    }

    @Override
    public Bicycle build() {
        validateBicycle();
        return this.bicycle;
    }

    private void validateBicycle() {
        if (this.bicycle == null) {
            throw new IllegalStateException(ERROR_MISSING_FRAME);
        }
    }
}
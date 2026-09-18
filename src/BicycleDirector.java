public class BicycleDirector {

    public Bicycle constructBike(BicycleBuilder builder) {
        return builder.buildFrame()
                .buildGroupset()
                .buildBrakes()
                .buildChainring()
                .build();
    }
}
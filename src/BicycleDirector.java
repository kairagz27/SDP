public class BicycleDirector {

    public void constructGravelBike(BicycleBuilder builder) {
        builder.setFrame("52cm Gravel Geometry")
                .setGroupset("L-Twoo A7")
                .setBrakes("Shimano MT200")
                .setChainring("40T Narrow Wide");
    }

    public void constructUrbanTouringBike(BicycleBuilder builder) {
        builder.setFrame("52cm Urban Commuter")
                .setGroupset("Shimano Nexus")
                .setBrakes("Shimano MT200")
                .setChainring("42T Standard");
    }
}
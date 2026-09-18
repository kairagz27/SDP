public interface BicycleBuilder {
    BicycleBuilder buildFrame();
    BicycleBuilder buildGroupset();
    BicycleBuilder buildBrakes();
    BicycleBuilder buildChainring();
    Bicycle build();
}
public class Main {
    public static void main(String[] args) {
        BicycleDirector director = new BicycleDirector();

        BicycleBuilder gravelBuilder = new GravelBikeBuilder();
        Bicycle gravelBike = director.constructBike(gravelBuilder);
        System.out.println(gravelBike);

        BicycleBuilder urbanBuilder = new UrbanBikeBuilder();
        Bicycle urbanBike = director.constructBike(urbanBuilder);
        System.out.println(urbanBike);
    }
}
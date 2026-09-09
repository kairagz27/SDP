public class Main {
    public static void main(String[] args) {
        BicycleDirector director = new BicycleDirector();
        BicycleBuilder builder = new BicycleBuilder();

        // Сборка гравийного велосипеда
        director.constructGravelBike(builder);
        Bicycle gravelBike = builder.build();
        System.out.println(gravelBike);

        // Кастомная ручная сборка (без директора)
        Bicycle customBike = new BicycleBuilder()
                .setFrame("54cm Track Frame")
                .setChainring("48T")
                .build();
        System.out.println(customBike);
    }
}
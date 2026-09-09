# Custom Bicycle Builder - Assignment 1

**Product Description:**
This project implements the Builder creational design pattern for assembling custom bicycles. It allows step-by-step construction of complex bicycle configurations (e.g., Gravel, Urban Touring) varying by frame size, groupset, brakes, and chainring.

## Clean Code Principles Applied

1. **Meaningful, intention-revealing names:** Classes like `BicycleDirector` and methods like `constructGravelBike()` clearly describe their exact role.
2. **Small methods, each doing one thing:** In `BicycleDirector`, each construction method is responsible for setting up only one specific type of bike.
3. **Validated construction:** The `build()` method in `BicycleBuilder` checks if the mandatory `frame` field is set before instantiating the object, throwing an `IllegalStateException` if invalid.
   *Excerpt:* `if (this.frame == null) { throw new IllegalStateException(...); }`
4. **No magic strings:** The error message for missing frames is stored as a constant `ERROR_MISSING_FRAME` rather than being hardcoded inside the exception.
5. **Method Chaining (Fluent API):** Every setter in `BicycleBuilder` returns `this`, allowing concise and readable configuration blocks.
   *Excerpt:* `builder.setFrame("52cm").setGroupset("L-Twoo A7");`
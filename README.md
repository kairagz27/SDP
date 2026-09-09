# Assignment #1 — Builder Pattern

**Student:** Agzam Kairbek, Group SE25-13

## 1. Product Description
This project implements the Builder creational design pattern in Java to construct custom bicycles. The domain genuinely benefits from step-by-step construction because bicycles have multiple configurations (e.g., Gravel, Urban Touring) and components (frames, groupsets, brakes). For example, a user might want a 52cm frame with an L-Twoo A7 groupset and Shimano MT200 brakes, while another might need a custom track setup.

## 2. Clean Code Principles Applied

Here are five Clean Code principles explicitly applied in this implementation, complete with annotated excerpts:

### 1. Meaningful, intention-revealing names for classes/methods/variables
Names clearly indicate their purpose and intent, making the code self-documenting.
*Excerpt:*

    // Bad:
    public void buildBike(BicycleBuilder b) { ... }

    // Good (Applied):
    public void constructGravelBike(BicycleBuilder builder) { ... }

### 2. Method chaining (fluent API)
Each setter in the builder returns the builder itself (return this;), allowing for readable, chainable configuration blocks.
*Excerpt:*

    // Applied Fluent API:
    Bicycle customBike = new BicycleBuilder()
            .setFrame("52cm Gravel Geometry")
            .setGroupset("L-Twoo A7")
            .setBrakes("Shimano MT200")
            .build();

### 3. Validated construction
The build() method ensures the object is not created in an invalid state by throwing a clear exception if mandatory fields (like the frame) are missing.
*Excerpt:*

    public Bicycle build() {
        if (this.frame == null || this.frame.isEmpty()) {
            throw new IllegalStateException(ERROR_MISSING_FRAME);
        }
        return new Bicycle(frame, groupset, brakes, chainring);
    }

### 4. No magic numbers/strings
Error messages and important literals are extracted into named static final variables rather than being hardcoded deep within the logic.
*Excerpt:*

    // Bad:
    throw new IllegalStateException("Bicycle frame is mandatory for assembly.");

    // Good (Applied):
    private static final String ERROR_MISSING_FRAME = "Bicycle frame is mandatory for assembly.";
    // ...
    throw new IllegalStateException(ERROR_MISSING_FRAME);

### 5. Small methods, each doing one thing
In the BicycleDirector class, each method is strictly responsible for orchestrating one specific, known configuration without complex conditional logic or passing boolean flags.
*Excerpt:*

    public void constructUrbanTouringBike(BicycleBuilder builder) {
        builder.setFrame("52cm Urban Commuter")
               .setGroupset("Shimano Nexus");
        // Strictly configures only the urban variant
    }
# Assignment #1 — Builder Pattern

**Student:** Agzam Kairbek, Group SE25-13

## 1. Product Description
This project implements the Builder creational design pattern to construct custom bicycles (e.g., Gravel, Urban Touring). It genuinely benefits from the pattern as bikes require a specific step-by-step assembly of various components (frame, groupset, brakes, chainring).

## 2. Clean Code Principles Applied

**1. Meaningful, intention-revealing names**
Names clearly indicate their purpose.
*Excerpt:*

    // Bad: public Bicycle make() { ... }
    // Good: public Bicycle constructBike(BicycleBuilder builder) { ... }

**2. Method chaining (Fluent API)**
Setters return `this` for readable, sequential configurations.
*Excerpt:*

    return builder.buildFrame()
                  .buildGroupset()
                  .build();

**3. Validated construction**
The `build()` method ensures the object isn't created in an invalid state.
*Excerpt:*

    public Bicycle build() {
        if (this.bicycle == null) throw new IllegalStateException(ERROR);
        return this.bicycle;
    }

**4. No magic strings**
Error messages are stored as constants, not hardcoded strings.
*Excerpt:*

    private static final String ERROR_MISSING_FRAME = "Frame is required.";
    // ...
    throw new IllegalStateException(ERROR_MISSING_FRAME);

**5. Small methods, each doing one thing**
Validation logic is extracted from `build()` into its own tiny method to separate concerns.
*Excerpt:*

    private void validateBicycle() {
        if (this.bicycle == null) throw new IllegalStateException(...);
    }
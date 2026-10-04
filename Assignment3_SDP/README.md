# Car Drive Mode Bridge Pattern

## Project Description

This Java project demonstrates the Bridge structural design pattern.

The project separates car drive modes from powertrain implementations.
Drive modes and powertrains can be extended independently without changing
each other's class hierarchies.

## Bridge Pattern Structure

### Abstraction

`DriveMode` is an abstract class that stores a reference to the
`Powertrain` interface.

### Refined Abstractions

- `EcoMode` uses reduced power to improve energy efficiency.
- `SportMode` uses maximum power to improve performance.

### Implementor

`Powertrain` defines the low-level operations required by every powertrain:

- `start()`
- `setPowerLevel()`
- `stop()`

### Concrete Implementors

- `ElectricPowertrain`
- `GasolinePowertrain`

### Client

`Main` creates drive modes with different powertrains and demonstrates
changing the powertrain at runtime without changing the drive mode object.

## Clean Code Principles

### 1. Meaningful Names

Class and method names clearly describe their responsibilities.
For example, `EcoMode`, `SportMode`, `ElectricPowertrain`, and
`setPowerLevel()` explain their purposes without additional comments.

### 2. Single Responsibility Principle

Each class has one responsibility. Drive mode classes control driving
behavior, while powertrain classes control engine-specific operations.

### 3. Small and Focused Classes

Every class contains only the methods required for its role in the Bridge
pattern. This makes the code easier to read, test, and maintain.

### 4. Programming to an Interface

`DriveMode` depends on the `Powertrain` interface instead of depending
directly on `ElectricPowertrain` or `GasolinePowertrain`.

### 5. Open Closed Principle

New powertrains, such as `HybridPowertrain`, can be added without modifying
the existing drive mode classes.

### 6. No Duplicated Business Logic

Drive mode behavior is located in `EcoMode` and `SportMode`. Concrete
powertrain classes contain only implementation-specific operations.

### 7. Constants Instead of Magic Numbers

Power percentages are stored in named constants such as
`ECO_POWER_PERCENTAGE` and `SPORT_POWER_PERCENTAGE`.

## How to Run

1. Open the project in IntelliJ IDEA.
2. Make sure that JDK 17 or newer is configured.
3. Open `Main.java`.
4. Run the `main()` method.
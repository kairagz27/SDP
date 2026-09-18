# Assignment 2 - Factory Method & Abstract Factory

## Clean Code Principles Applied

**1. Small, focused classes & Consistent formatting**
*Justification:* Each class is placed in its own file and package (`products`, `factories`, `client`). We avoid "God classes".
*Annotated Excerpt:*
Instead of putting everything in `Main.java`,

we separated the client:
`public class ComputerSetupClient { ... }` lives in the `client` package, focusing solely on using the factory.

**2. Validated construction**
*Justification:* An object should not be created in an invalid state.
*Before:* `public ComputerSetupClient(PeripheralFactory factory) { this.keyboard = factory.createKeyboard(); }` (Could throw NullPointerException later).
*After:*
```java
public ComputerSetupClient(PeripheralFactory factory) {
    if (factory == null) {
        throw new IllegalArgumentException("Factory cannot be null");
    }
    this.keyboard = factory.createKeyboard();
}
```
**3. Meaningful, intention-revealing names**

*Justification:* Names answer why it exists and what it does.

*Excerpt:* Instead of generic names like FactoryA or Product1, we use GamingPeripheralFactory and OfficeKeyboard.

This clearly states the domain and product family.

**4. Small methods, each doing one thing**

*Justification:* Methods should have one level of abstraction.

*Excerpt:*
```java
@Override
public Keyboard createKeyboard() {
    return new GamingKeyboard();
}
```

This method does exactly one thing: instantiates a specific product. There are no complex if/else statements inside it.

**5. Programming to Interfaces (Client Isolation)**
*Justification:* The client code should not depend on concrete classes.
*Excerpt:*
```java
private Keyboard keyboard;
private Mouse mouse;
```
The client never uses new GamingKeyboard(), it relies entirely on the PeripheralFactory interface.
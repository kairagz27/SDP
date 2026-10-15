# Assignment 4 Defense Guide

Read the code while using these notes. The written defense is worth **50%** of the assignment, so a working project alone cannot guarantee the full grade.

## Short explanation in simple English

My project is a car wash booking system. I use the Facade structural design pattern. The client calls `bookWash` with a customer, a service and a start time. It does not manage the five subsystem services.

The Facade gets a service quote, reserves a wash bay, reserves chemicals, takes payment and saves the booking. The services contain the business rules. The Facade only controls their order. If a step fails, it returns the money and releases the resources.

The normal client knows only the Facade and simple data records. The subsystem services do not know the Facade. An advanced program can still use them directly. This makes the common task easier without removing the original subsystem API.

## What to draw on paper

Draw `Main` with an arrow to `CarWashFacade`. Draw five arrows from the Facade to `ServiceCatalog`, `WashBayScheduler`, `ChemicalInventory`, `PaymentGateway` and `BookingRepository`. Do not draw an arrow from a subsystem back to the Facade.

Below the diagram, write the successful sequence:

1. `catalog.quote`
2. `scheduler.reserve`
3. `inventory.reserve`
4. `payments.charge`
5. `bookings.save`

For a failed booking, write `refund`, `inventory.release`, `scheduler.release`, then `bookings.delete`.

## Likely questions and simple answers

**1. What is Facade?**  
It is a structural design pattern. It gives a simple, high-level interface to a more complex subsystem.

**2. Why do you use it here?**  
A wash booking needs several services in a specific order. Without the Facade, each normal client must know that order and write recovery code.

**3. Which class is your Facade?**  
`CarWashFacade`. It has two business operations: `bookWash` and `cancelBooking`.

**4. Which class is the client?**  
`client.Main`. It creates the Facade through `createDemo()` and calls its business methods. It does not create subsystem objects.

**5. What does createDemo do?**  
It creates and connects the five services with sample bays, stock and customer balances. It is a creation helper, not another business operation.

**6. Why is the constructor package-private?**  
The normal public API does not need subsystem types. Tests in the same package can still supply services with chosen state and a failing repository.

**7. What does the Facade simplify?**  
One booking call replaces quote selection, bay reservation, chemical reservation, payment, storage and failure recovery.

**8. Why are the subsystem classes not just print methods?**  
They validate service names, detect time conflicts, reserve stock, change balances and store receipts. The tests check these state changes.

**9. Do subsystem classes know the Facade?**  
No. They contain no Facade fields, imports or method calls. The dependency direction goes from the Facade to the subsystem.

**10. Can an advanced client bypass the Facade?**  
Yes. Facade simplifies access; it does not forbid direct access. `DirectSubsystemExample` proves this using the public services without creating a Facade.

**11. Why does that advanced example not break the client requirement?**  
The normal application client is `Main` and uses only the Facade. The separate example demonstrates the assignment's explicit requirement that advanced clients may bypass it.

**12. What if payment fails?**  
The Facade catches the failure. It releases chemicals and the bay. No receipt remains and the customer's balance is unchanged.

**13. What if saving fails after payment?**  
The Facade refunds the payment and releases both resources. Test 9 checks the final balance, stock, schedule and repository.

**14. Why can recovery release something that was never reserved?**  
Release and refund are idempotent for a booking ID. If there is no matching reservation or charge, they do nothing.

**15. What does idempotent mean here?**  
Calling a release or refund again has no extra effect. It cannot add stock or money twice.

**16. Is cancelBooking itself idempotent?**  
It does not change state twice, but the second call reports that the booking is missing. The lower-level release and refund methods quietly do nothing when repeated.

**17. Why do you use BigDecimal?**  
Money needs exact decimal values. Binary floating-point numbers can produce rounding errors. Prices use string-based BigDecimal construction.

**18. How do you detect an overlap?**  
Two intervals overlap when `newStart < oldEnd` and `oldStart < newEnd`. The checks use `isBefore`, so a wash may start exactly when another wash ends.

**19. How do the services collaborate without calling each other?**  
The Facade passes the quote's duration, stock units and price to the correct services. The same booking ID links their state.

**20. What is Single Responsibility in this code?**  
The scheduler manages time and bays. Inventory manages chemicals. Payments manages balances. The repository manages saved receipts. The Facade manages the booking workflow.

**21. Give five Clean Code principles with code evidence.**  
Meaningful names: `bookWash` and `refund`. Single Responsibility: separate schedule, stock, payment and storage classes. Small functions: `isFree` and `find`. Encapsulation: private maps and no subsystem getters on the Facade. Error handling: contextual `BookingException` with its original cause. Also, `releaseResources` avoids repeated recovery logic.

**22. Why are records useful?**  
A record is a compact Java type for data. Its fields cannot be reassigned. `WashRequest`, `ServiceQuote` and `BookingReceipt` hold values rather than services. Java creates their accessors, constructor, equals, hashCode and toString methods.

**23. Does Facade require an interface or inheritance?**  
No. The pattern is about a simpler interface to a subsystem. One concrete coordinating class is enough here.

**24. Is Facade the same as Adapter?**  
No. Adapter changes an interface so a client can use an incompatible object. Facade makes a subsystem easier to use through high-level operations.

**25. Is Facade the same as Mediator?**  
No. Mediator manages communication between participating objects, which often know the mediator. Here the services do not know the Facade and remain directly usable.

**26. Is the Facade a Singleton?**  
No. The project may create several independent Facade instances. Singleton is not required by Facade.

**27. Why is there no second Facade?**  
It is optional. Booking and cancellation are related operations. There is no unrelated feature set that needs a separate Facade.

**28. Does this system use a real bank or database?**  
No. It is an in-memory model for the assignment. A real external system needs persistent state, concurrency control and reliable recovery.

**29. Is the project thread-safe or crash-safe?**  
No. It is a single-threaded console project. In-memory maps and compensation are enough for this model, but do not provide distributed transactions or durability.

**30. What if refund itself fails in a real system?**  
The current in-memory refund has no network operation and does not fail for a missing ID. A real system must record failed recovery, retry safely and preserve both the original and recovery errors.

**31. Why are all service classes not interfaces?**  
The assignment needs Facade, not a large dependency framework. Concrete services keep the code small. The repository is extensible for a test that injects a storage failure.

**32. What does the Facade not contain?**  
It does not calculate prices, check overlap formulas, manage stock counters or perform balance arithmetic. Those rules belong to the services.

## Paper trace exercises

### Exercise 1 Successful PREMIUM booking

Initial state: one bay, ten stock units, balance 20000 KZT, zero bookings. A PREMIUM wash needs sixty minutes, three units and 6000 KZT.

**Answer:** One bay reservation, seven available stock units, balance 14000 KZT, one saved booking.

### Exercise 2 Payment failure

Initial state: one bay, ten stock units, balance 1000 KZT, zero bookings. The customer requests PREMIUM.

**Answer:** Quote succeeds, bay and stock are reserved, then payment fails. Recovery leaves zero reservations, ten stock units, balance 1000 KZT and zero bookings.

### Exercise 3 Time intervals

A BASIC booking uses bay 1 from 10:00 to 10:30. There is only one bay. Can another BASIC booking start at 10:15? At 10:30?

**Answer:** 10:15 is rejected because the intervals overlap. 10:30 is accepted because intervals are half-open.

### Exercise 4 Storage failure

Payment has succeeded, but `BookingRepository.save` throws an exception. Which actions must happen?

**Answer:** Refund the charge, restore chemicals, release the bay and remove an incomplete record. The client gets a `BookingException` that keeps the original cause.

### Exercise 5 Add a new service

Where would you add a 90-minute service with five chemical units and a price of 9000 KZT?

**Answer:** Add a named entry in `ServiceCatalog`. The existing Facade workflow uses its duration, stock units and price without a new booking method.

## Before the defense

- Be able to write the overlap formula and explain each comparison.
- Be able to trace every line of `bookWash` and `releaseResources`.
- Explain why every one of the five services has its own responsibility.
- Explain the difference between a service, a record and the Facade.
- Run Main and the tests yourself in IntelliJ before submission.
- Check that the topic is unique in your group and differs from Lecture 6.
- Submit the GitHub repository URL, not only the ZIP file.

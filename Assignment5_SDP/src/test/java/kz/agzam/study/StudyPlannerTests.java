package kz.agzam.study;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import kz.agzam.study.context.StudyPlanner;
import kz.agzam.study.model.StudyTask;
import kz.agzam.study.strategy.DeadlineFirstStrategy;
import kz.agzam.study.strategy.HardestFirstStrategy;
import kz.agzam.study.strategy.ShortestFirstStrategy;
import kz.agzam.study.strategy.StudyStrategy;

public final class StudyPlannerTests {
    private static final LocalDate DAY = LocalDate.of(2026, 10, 22);
    private static final StudyTask ESSAY = new StudyTask("Essay", DAY.plusDays(3), 3, 90);
    private static final StudyTask QUIZ = new StudyTask("Quiz", DAY.plusDays(1), 1, 15);
    private static final StudyTask PROJECT = new StudyTask("Project", DAY.plusDays(5), 5, 60);
    private static final List<StudyTask> TASKS = List.of(ESSAY, QUIZ, PROJECT);
    private static final List<StudyStrategy> STRATEGIES = List.of(
            new DeadlineFirstStrategy(), new HardestFirstStrategy(), new ShortestFirstStrategy());
    private static int passed;

    public static void main(String[] args) {
        run("Nearest deadline is first", () -> checkOrder(new DeadlineFirstStrategy(),
                List.of(QUIZ, ESSAY, PROJECT)));
        run("Highest difficulty is first", () -> checkOrder(new HardestFirstStrategy(),
                List.of(PROJECT, ESSAY, QUIZ)));
        run("Shortest duration is first", () -> checkOrder(new ShortestFirstStrategy(),
                List.of(QUIZ, PROJECT, ESSAY)));
        run("One planner changes strategy at runtime", StudyPlannerTests::runtimeSwitching);
        run("Equal priorities use documented tie breakers", StudyPlannerTests::tieBreakers);
        run("Planning preserves input and duplicate tasks", StudyPlannerTests::preserveInput);
        run("All strategies accept an empty list", StudyPlannerTests::emptyInput);
        run("All strategies accept a single task", StudyPlannerTests::singleTask);
        run("Task construction rejects invalid data", StudyPlannerTests::invalidTasks);
        run("Null strategy is rejected without losing the current one",
                StudyPlannerTests::nullStrategies);
        run("Null list and null elements are rejected", StudyPlannerTests::nullTasks);
        run("A fourth algorithm works without changing the Context",
                StudyPlannerTests::extensionStrategy);
        System.out.println("ALL " + passed + " TESTS PASSED");
    }

    private static void checkOrder(StudyStrategy strategy, List<StudyTask> expected) {
        check(new StudyPlanner(strategy).plan(TASKS).equals(expected), "Unexpected task order");
    }

    private static void runtimeSwitching() {
        StudyPlanner planner = new StudyPlanner(new DeadlineFirstStrategy());
        check(planner.plan(TASKS).equals(List.of(QUIZ, ESSAY, PROJECT)), "Wrong initial strategy");
        planner.setStrategy(new HardestFirstStrategy());
        check(planner.plan(TASKS).equals(List.of(PROJECT, ESSAY, QUIZ)), "First switch failed");
        planner.setStrategy(new ShortestFirstStrategy());
        check(planner.plan(TASKS).equals(List.of(QUIZ, PROJECT, ESSAY)), "Second switch failed");
        planner.setStrategy(new DeadlineFirstStrategy());
        check(planner.plan(TASKS).equals(List.of(QUIZ, ESSAY, PROJECT)), "Switch back failed");
    }

    private static void tieBreakers() {
        StudyTask later = new StudyTask("A later task", DAY.plusDays(1), 3, 30);
        StudyTask beta = new StudyTask("Beta", DAY, 3, 30);
        StudyTask alpha = new StudyTask("Alpha", DAY, 3, 30);
        List<StudyTask> tiedTasks = List.of(later, beta, alpha);
        for (StudyStrategy strategy : STRATEGIES) {
            check(new StudyPlanner(strategy).plan(tiedTasks).equals(List.of(alpha, beta, later)),
                    "Ties must use deadline, then title");
        }
    }

    private static void preserveInput() {
        List<StudyTask> original = List.of(ESSAY, QUIZ, PROJECT, QUIZ);
        for (StudyStrategy strategy : STRATEGIES) {
            List<StudyTask> input = new ArrayList<>(original);
            List<StudyTask> result = new StudyPlanner(strategy).plan(input);
            check(input.equals(original), "Input was modified");
            check(result.size() == original.size(), "A task was dropped or added");
            check(result.stream().filter(QUIZ::equals).count() == 2, "Duplicate task was lost");
            input.clear();
            check(result.size() == original.size(), "Result depends on the input list");
        }
    }

    private static void emptyInput() {
        for (StudyStrategy strategy : STRATEGIES) {
            check(new StudyPlanner(strategy).plan(List.of()).isEmpty(), "Expected an empty plan");
        }
    }

    private static void singleTask() {
        for (StudyStrategy strategy : STRATEGIES) {
            check(new StudyPlanner(strategy).plan(List.of(ESSAY)).equals(List.of(ESSAY)),
                    "Single task was changed");
        }
    }

    private static void invalidTasks() {
        expect(NullPointerException.class, () -> new StudyTask(null, DAY, 1, 10));
        expect(NullPointerException.class, () -> new StudyTask("Task", null, 1, 10));
        expect(IllegalArgumentException.class, () -> new StudyTask(" \t", DAY, 1, 10));
        expect(IllegalArgumentException.class, () -> new StudyTask("Task", DAY, 0, 10));
        expect(IllegalArgumentException.class, () -> new StudyTask("Task", DAY, 6, 10));
        expect(IllegalArgumentException.class, () -> new StudyTask("Task", DAY, 1, 0));
        expect(IllegalArgumentException.class, () -> new StudyTask("Task", DAY, 1, -1));
        check(new StudyTask(" Task ", DAY, 1, 1).title().equals("Task"), "Title was not trimmed");
        check(new StudyTask("Task", DAY, 5, 1).difficulty() == 5, "Maximum difficulty rejected");
    }

    private static void nullStrategies() {
        expect(NullPointerException.class, () -> new StudyPlanner(null));
        StudyPlanner planner = new StudyPlanner(new DeadlineFirstStrategy());
        expect(NullPointerException.class, () -> planner.setStrategy(null));
        check(planner.plan(TASKS).equals(List.of(QUIZ, ESSAY, PROJECT)),
                "Rejected strategy replaced the current one");
    }

    private static void nullTasks() {
        StudyPlanner planner = new StudyPlanner(new DeadlineFirstStrategy());
        expect(NullPointerException.class, () -> planner.plan(null));
        List<StudyTask> tasksWithNull = new ArrayList<>(TASKS);
        tasksWithNull.add(null);
        expect(NullPointerException.class, () -> planner.plan(tasksWithNull));
    }

    private static void extensionStrategy() {
        StudyStrategy longestFirst = tasks -> tasks.stream()
                .sorted(Comparator.comparingInt(StudyTask::estimatedMinutes).reversed()
                        .thenComparing(StudyTask::deadline)
                        .thenComparing(StudyTask::title))
                .toList();
        checkOrder(longestFirst, List.of(ESSAY, PROJECT, QUIZ));
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void expect(Class<? extends RuntimeException> type, Runnable action) {
        try {
            action.run();
        } catch (RuntimeException failure) {
            if (type.isInstance(failure)) {
                return;
            }
            throw new AssertionError("Expected " + type.getSimpleName(), failure);
        }
        throw new AssertionError("Expected " + type.getSimpleName() + " but nothing was thrown");
    }

    private static void run(String name, Runnable test) {
        test.run();
        passed++;
        System.out.println("PASS " + passed + ": " + name);
    }
}

package kz.agzam.study.client;

import java.time.LocalDate;
import java.util.List;
import kz.agzam.study.context.StudyPlanner;
import kz.agzam.study.model.StudyTask;
import kz.agzam.study.strategy.DeadlineFirstStrategy;
import kz.agzam.study.strategy.HardestFirstStrategy;
import kz.agzam.study.strategy.ShortestFirstStrategy;

public final class Main {
    public static void main(String[] args) {
        List<StudyTask> tasks = List.of(
                new StudyTask("Strategy implementation", LocalDate.of(2026, 10, 25), 4, 90),
                new StudyTask("English vocabulary", LocalDate.of(2026, 10, 23), 1, 20),
                new StudyTask("Algorithms problem set", LocalDate.of(2026, 10, 27), 5, 60),
                new StudyTask("Database report", LocalDate.of(2026, 10, 24), 3, 120));

        StudyPlanner planner = new StudyPlanner(new DeadlineFirstStrategy());
        printPlan("Nearest deadline first", planner.plan(tasks));

        planner.setStrategy(new HardestFirstStrategy());
        printPlan("Hardest task first", planner.plan(tasks));

        planner.setStrategy(new ShortestFirstStrategy());
        printPlan("Shortest task first", planner.plan(tasks));
    }

    private static void printPlan(String heading, List<StudyTask> tasks) {
        System.out.println("\n" + heading);
        for (StudyTask task : tasks) {
            System.out.printf("  %-24s deadline=%s difficulty=%d duration=%d min%n",
                    task.title(), task.deadline(), task.difficulty(), task.estimatedMinutes());
        }
    }
}

package kz.agzam.study.model;

import java.time.LocalDate;
import java.util.Objects;

public record StudyTask(String title, LocalDate deadline, int difficulty, int estimatedMinutes) {
    public static final int MIN_DIFFICULTY = 1;
    public static final int MAX_DIFFICULTY = 5;

    public StudyTask {
        Objects.requireNonNull(title, "Task title is required");
        Objects.requireNonNull(deadline, "Task deadline is required");
        title = title.strip();
        if (title.isEmpty()) {
            throw new IllegalArgumentException("Task title must not be blank");
        }
        if (difficulty < MIN_DIFFICULTY || difficulty > MAX_DIFFICULTY) {
            throw new IllegalArgumentException("Difficulty must be between 1 and 5");
        }
        if (estimatedMinutes <= 0) {
            throw new IllegalArgumentException("Estimated duration must be positive");
        }
    }
}

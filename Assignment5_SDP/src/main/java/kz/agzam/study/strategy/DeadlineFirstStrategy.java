package kz.agzam.study.strategy;

import java.util.Comparator;
import java.util.List;
import kz.agzam.study.model.StudyTask;

public final class DeadlineFirstStrategy implements StudyStrategy {
    @Override
    public List<StudyTask> prioritize(List<StudyTask> tasks) {
        return tasks.stream()
                .sorted(Comparator.comparing(StudyTask::deadline)
                        .thenComparing(StudyTask::title))
                .toList();
    }
}

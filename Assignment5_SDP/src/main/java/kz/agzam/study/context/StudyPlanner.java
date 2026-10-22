package kz.agzam.study.context;

import java.util.List;
import java.util.Objects;
import kz.agzam.study.model.StudyTask;
import kz.agzam.study.strategy.StudyStrategy;

public final class StudyPlanner {
    private StudyStrategy strategy;

    public StudyPlanner(StudyStrategy strategy) {
        setStrategy(strategy);
    }

    public void setStrategy(StudyStrategy strategy) {
        this.strategy = Objects.requireNonNull(strategy, "Study strategy is required");
    }

    public List<StudyTask> plan(List<StudyTask> tasks) {
        return strategy.prioritize(List.copyOf(tasks));
    }
}

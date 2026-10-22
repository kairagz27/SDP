package kz.agzam.study.strategy;

import java.util.List;
import kz.agzam.study.model.StudyTask;

@FunctionalInterface
public interface StudyStrategy {
    /**
     * Returns every input task in priority order without modifying the input.
     * The input must be a non-null list containing non-null tasks.
     */
    List<StudyTask> prioritize(List<StudyTask> tasks);
}

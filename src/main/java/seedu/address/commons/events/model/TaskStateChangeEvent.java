package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.task.ReadOnlyTask;

/** Indicates that a task's completeness has been changed */
public class TaskStateChangeEvent extends BaseEvent {

    public final ReadOnlyTask targetToReplace;
    public final ReadOnlyTask newTask;

    public TaskStateChangeEvent(ReadOnlyTask targetToReplace, ReadOnlyTask newTask) {
        this.targetToReplace = targetToReplace;
        this.newTask = newTask;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}

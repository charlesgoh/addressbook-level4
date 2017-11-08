package seedu.address.model.task;

import java.util.ArrayList;

import seedu.address.commons.core.index.Index;
import seedu.address.model.person.ReadOnlyPerson;
/**
 * Represents the list of {@code ReadOnlyPerson} assigned to a task.
 * Contains support for some limited modification operations
 */
public class Assignees {
    private ArrayList<Index> assignedList;

    public Assignees(ArrayList<Index> assignees) {
        this.assignedList = assignees;
    }

    public Assignees() {
        this.assignedList = new ArrayList<>();
    }

    public Assignees(Assignees toCopy) {
        this.assignedList = new ArrayList<>();
        assignedList.addAll(toCopy.getList());
    }

    /** Assigns all {@code ReadOnlyPerson} in the specified list */
    public void assign(ArrayList<Index> personsToAssign) {
        for (Index i : personsToAssign) {
            if (!assignedList.contains(i)) {
                assignedList.add(i);
            }
        }
    }

    /** Updates the internal assignedList with the correct Index values after a sort operation */
    public void updateList(Index[] mappings) {
        ArrayList<Index> updatedList = new ArrayList<>();
        for (Index i : assignedList) {
            Index updatedPosition = mappings[i.getZeroBased()];
            updatedList.add(updatedPosition);
        }
        assignedList.clear();
        assignedList.addAll(updatedList);
    }

    /** Removes all {@code ReadOnlyPerson} from the specified list */
    public void dismiss(ArrayList<Index> personsToDismiss) {
        assignedList.removeAll(personsToDismiss);
    }

    public boolean contains(ReadOnlyPerson toFind) {
        return assignedList.contains(toFind);
    }

    public ArrayList<Index> getList() {
        return this.assignedList;
    }

    /**
     * Deletes the specified index from the internal list, and decrements all other indexes in the assigned list
     * that have a value lower than the deleted index by 1.
     */
    public void decrementIndex(Index deletedIndex) {
        assignedList.remove(deletedIndex);
        for (int i = 0; i < assignedList.size(); i++) {
            Index current = assignedList.get(i);
            if (current.getZeroBased() > deletedIndex.getZeroBased()) {
                int indexValue = current.getZeroBased() - 1;
                Index decrementedIndex = Index.fromZeroBased(indexValue);
                assignedList.set(i, decrementedIndex);
                continue;
            }
        }
    }

    @Override
    public String toString() {
        return assignedList.size() + " persons assigned";
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Assignees // instanceof handles nulls
                && this.assignedList.equals(((Assignees) other).assignedList)); // state check
    }

    @Override
    public int hashCode() {
        return assignedList.hashCode();
    }
}

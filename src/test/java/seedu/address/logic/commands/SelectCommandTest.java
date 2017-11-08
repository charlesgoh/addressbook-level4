package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.address.logic.commands.CommandTestUtil.showFirstPersonOnly;
import static seedu.address.testutil.TypicalAddressBook.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_TASK;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_TASK;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.commons.events.ui.JumpToListRequestTaskEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.ui.testutil.EventsCollectorRule;

/**
 * Contains integration tests (interaction with the Model) for {@code SelectCommand}.
 */
public class SelectCommandTest {
    private static final boolean IS_TYPE_PERSON = false;
    private static final boolean IS_TYPE_TASK = true;
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();
    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Index lastPersonIndex = Index.fromOneBased(model.getFilteredPersonList().size());
        Index lastTaskIndex = Index.fromOneBased(model.getFilteredTaskList().size());

        assertExecutionSuccess(INDEX_FIRST_PERSON, IS_TYPE_PERSON);
        assertExecutionSuccess(INDEX_THIRD_PERSON, IS_TYPE_PERSON);
        assertExecutionSuccess(lastPersonIndex, IS_TYPE_PERSON);
        assertExecutionSuccess(INDEX_FIRST_TASK, IS_TYPE_TASK);
        assertExecutionSuccess(INDEX_THIRD_TASK, IS_TYPE_TASK);
        assertExecutionSuccess(lastTaskIndex, IS_TYPE_TASK);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Index taskOutOfBoundsIndex = Index.fromOneBased(model.getFilteredTaskList().size() + 1);

        assertExecutionFailure(outOfBoundsIndex, IS_TYPE_PERSON, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        assertExecutionFailure(taskOutOfBoundsIndex, IS_TYPE_TASK, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showFirstPersonOnly(model);

        assertExecutionSuccess(INDEX_FIRST_PERSON, IS_TYPE_PERSON);
    }

    @Test
    public void execute_invalidIndexFilteredList_failure() {
        showFirstPersonOnly(model);

        Index outOfBoundsIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundsIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        assertExecutionFailure(outOfBoundsIndex, IS_TYPE_PERSON, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        SelectCommand selectFirstCommand = new SelectCommand(INDEX_FIRST_PERSON, false);
        SelectCommand selectSecondCommand = new SelectCommand(INDEX_SECOND_PERSON, false);
        SelectCommand selectFirstTaskCommand = new SelectCommand(INDEX_FIRST_TASK, true);
        SelectCommand selectSecondTaskCommand = new SelectCommand(INDEX_SECOND_TASK, true);

        // same object -> returns true
        assertTrue(selectFirstCommand.equals(selectFirstCommand));
        assertTrue(selectFirstTaskCommand.equals(selectFirstTaskCommand));

        // same values -> returns true
        SelectCommand selectFirstCommandCopy = new SelectCommand(INDEX_FIRST_PERSON, false);
        assertTrue(selectFirstCommand.equals(selectFirstCommandCopy));

        // different types -> returns false
        assertFalse(selectFirstCommand.equals(1));

        // null -> returns false
        assertFalse(selectFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(selectFirstCommand.equals(selectSecondCommand));

        // different task -> returns false
        assertFalse(selectFirstTaskCommand.equals(selectSecondTaskCommand));

        // different types -> returns false
        assertFalse(selectFirstCommand.equals(selectFirstTaskCommand));
    }

    /**
     * Executes a {@code SelectCommand} with the given {@code index}, and checks that {@code JumpToListRequestEvent}
     * is raised with the correct index.
     */
    private void assertExecutionSuccess(Index index, boolean isTask) {
        SelectCommand selectCommand = prepareCommand(index, isTask);
        String expectedMessage = String.format(SelectCommand.MESSAGE_SELECT_PERSON_SUCCESS, index.getOneBased());
        if (isTask) {
            expectedMessage = String.format(SelectCommand.MESSAGE_SELECT_TASK_SUCCESS, index.getOneBased());
        }
        try {
            CommandResult commandResult = selectCommand.execute();
            assertEquals(expectedMessage, commandResult.feedbackToUser);
        } catch (CommandException ce) {
            throw new IllegalArgumentException("Execution of command should not fail.", ce);
        }
        if (isTask) {
            JumpToListRequestTaskEvent lastTaskEvent =
                    (JumpToListRequestTaskEvent) eventsCollectorRule.eventsCollector.getMostRecent();
            assertEquals(index, Index.fromZeroBased(lastTaskEvent.targetIndex));
        } else {
            JumpToListRequestEvent lastEvent =
                    (JumpToListRequestEvent) eventsCollectorRule.eventsCollector.getMostRecent();
            assertEquals(index, Index.fromZeroBased(lastEvent.targetIndex));
        }

    }

    /**
     * Executes a {@code SelectCommand} with the given {@code index}, and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     */
    private void assertExecutionFailure(Index index, boolean isTask, String expectedMessage) {
        SelectCommand selectCommand = prepareCommand(index, isTask);

        try {
            selectCommand.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException ce) {
            assertEquals(expectedMessage, ce.getMessage());
            assertTrue(eventsCollectorRule.eventsCollector.isEmpty());
        }
    }

    /**
     * Returns a {@code SelectCommand} with parameters {@code index}.
     */
    private SelectCommand prepareCommand(Index index, boolean isTask) {
        SelectCommand selectCommand = new SelectCommand(index, isTask);
        selectCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return selectCommand;
    }
}

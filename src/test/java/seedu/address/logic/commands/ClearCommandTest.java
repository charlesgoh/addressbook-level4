package seedu.address.logic.commands;

import static seedu.address.logic.commands.ClearCommand.TYPE_ALL;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalPersonsAddressBook;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ClearCommandTest {

    @Test
    public void execute_emptyAddressBook_success() {
        Model model = new ModelManager();
        assertCommandSuccess(prepareCommand(model), model,
                String.format(ClearCommand.MESSAGE_SUCCESS, TYPE_ALL), model);
    }

    @Test
    public void execute_nonEmptyAddressBook_success() {
        Model model = new ModelManager(getTypicalPersonsAddressBook(), new UserPrefs());
        assertCommandSuccess(prepareCommand(model), model,
                String.format(ClearCommand.MESSAGE_SUCCESS, TYPE_ALL), model);
    }

    /**
     * Generates a new {@code ClearCommand} which upon execution, clears the contents in {@code model}.
     */
    private ClearCommand prepareCommand(Model model) {
        ClearCommand command = new ClearCommand();
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}

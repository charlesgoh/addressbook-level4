package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AVATAR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.exceptions.DuplicateTaskException;

/**
 * Adds a person or task to the address book.
 */
public class AddCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "add";
    public static final String COMMAND_ALIAS = "a";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to the address book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + PREFIX_REMARK + "REMARK "
            + PREFIX_AVATAR + "AVATAR "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_REMARK + "Sells sea shells on the sea shore "
            + PREFIX_AVATAR + "avatar-filepath.png"
            + PREFIX_TAG + "friends "
            + PREFIX_TAG + "owesMoney";

    public static final String MESSAGE_TASK_USAGE = COMMAND_WORD + ": Adds a task to the address book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_DESCRIPTION + "DESCRIPTION "
            + PREFIX_DEADLINE + "DEADLINE "
            + PREFIX_PRIORITY + "PRIORITY "
            + PREFIX_ADDRESS + "ADDRESS"
            + "Example: " + COMMAND_WORD + " " + PREFIX_TASK + " "
            + PREFIX_NAME + "Buy pencil "
            + PREFIX_DESCRIPTION + "Buy a new pencil from ABS "
            + PREFIX_DEADLINE + "10-10-2017 "
            + PREFIX_PRIORITY + "4 "
            + PREFIX_ADDRESS + "12 Kent Ridge Crescent, 119275";

    public static final String MESSAGE_TASK_SUCCESS = "New task added: \n%1$s";
    public static final String MESSAGE_SUCCESS = "New person added: \n%1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the address book";

    private final Person personToAdd;
    private final Task taskToAdd;
    private boolean isTask = false;

    /**
     * Creates an AddCommand to add the specified {@code ReadOnlyPerson}
     */
    public AddCommand(ReadOnlyPerson person) {
        personToAdd = new Person(person);
        taskToAdd = null;
    }

    /**
     * Creates an AddCommand to add the specified {@Code ReadOnlyTask}
     */
    public AddCommand(ReadOnlyTask task) {
        taskToAdd = new Task(task);
        personToAdd = null;
        isTask = true;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            if (isTask) {
                model.addTask(taskToAdd);
                return new CommandResult(String.format(MESSAGE_TASK_SUCCESS, taskToAdd));
            } else {
                model.addPerson(personToAdd);
                return new CommandResult(String.format(MESSAGE_SUCCESS, personToAdd));
            }
        } catch (DuplicatePersonException e) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        } catch (DuplicateTaskException e) {
            throw new CommandException(MESSAGE_DUPLICATE_TASK);
        }

    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (!(other instanceof AddCommand)) {
            return false;
        } else if (taskToAdd == null && ((AddCommand) other).taskToAdd == null) {
            assert(personToAdd != null);
            assert(((AddCommand) other).personToAdd != null); // The personToAdd cannot be null
            return personToAdd.equals(((AddCommand) other).personToAdd);
        } else if (personToAdd == null && ((AddCommand) other).personToAdd == null) {
            assert(taskToAdd != null);
            assert(((AddCommand) other).taskToAdd != null); // The taskToAdd cannot be null
            return taskToAdd.equals(((AddCommand) other).taskToAdd);
        } else {
            return false;
        }
    }
}

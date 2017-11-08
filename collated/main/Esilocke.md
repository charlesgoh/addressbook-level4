# Esilocke
###### /java/seedu/address/ui/TaskCard.java
``` java
/**
 * A UI component that displays the information of a {@code Task}
 * */
public class TaskCard  extends UiPart<Region> {

    public static final int DEFAULT_NAME_SIZE = 15;
    public static final int DEFAULT_ATTRIBUTE_SIZE = 10;
    public static final int FONT_SIZE_EXTENDER = 5;
    public static final int DEFAULT_FONT_SIZE_MULTIPLIER = 0;

    private static final String FXML = "TaskListCard.fxml";
    private static int nameSize = DEFAULT_NAME_SIZE;
    private static int attributeSize = DEFAULT_ATTRIBUTE_SIZE;

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final ReadOnlyTask task;

    @FXML
    private HBox taskCardPane;
    @FXML
    private Label taskName;
    @FXML
    private Label id;
    @FXML
    private Label state;
    @FXML
    private Label description;
    @FXML
    private Label deadline;
    @FXML
    private Label priority;
    @FXML
    private Label assignCount;
    @FXML
    private Label taskAddress;

    private int fontSizeMultipler;

    public TaskCard(ReadOnlyTask task, int displayedIndex, int fontSizeMultiplier) {
        super(FXML);
        this.task = task;
        this.fontSizeMultipler = fontSizeMultiplier;
        id.setText(displayedIndex + ". ");
        bindListeners(task);
        updateAttributeSizes();
    }

    /**
     * Binds the individual UI elements to observe their respective {@code Task} properties
     * so that they will be notified of any changes.
     */
    private void bindListeners(ReadOnlyTask task) {
        taskName.textProperty().bind(Bindings.convert(task.taskNameProperty()));
        description.textProperty().bind(Bindings.convert(task.descriptionProperty()));
        deadline.textProperty().bind(Bindings.convert(task.deadlineProperty()));
        priority.textProperty().bind(Bindings.convert(task.priorityProperty()));
        assignCount.textProperty().bind(Bindings.convert(task.assigneeProperty()));
        taskAddress.textProperty().bind(Bindings.convert(task.taskAddressProperty()));
        state.textProperty().bind(Bindings.convert(task.stateProperty()));
    }

```
###### /java/seedu/address/ui/TaskListPanel.java
``` java
/**
 * Panel containing the list of tasks.
 */
public class TaskListPanel extends UiPart<Region> {
    private static final String FXML = "TaskListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(TaskListPanel.class);

    @FXML
    private ListView<TaskCard> taskListView;

    private int fontSizeMultiplier;
    private ObservableList<ReadOnlyTask> taskList;

    public TaskListPanel(ObservableList<ReadOnlyTask> taskList) {
        super(FXML);
        this.taskList = taskList;
        fontSizeMultiplier = MINIMUM_FONT_SIZE_MULTIPLIER;
        setConnections(taskList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<ReadOnlyTask> taskList) {
        ObservableList<TaskCard> mappedList = EasyBind.map(
                taskList, (task) -> new TaskCard(task, taskList.indexOf(task) + 1, fontSizeMultiplier));
        taskListView.setItems(mappedList);
        taskListView.setCellFactory(listView -> new TaskListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        taskListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in task list panel changed to : '" + newValue + "'");
                        raise(new TaskPanelSelectionChangedEvent(newValue));
                    }
                });
    }

```
###### /java/seedu/address/logic/parser/DismissTaskCommandParser.java
``` java

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FROM;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;

import java.util.ArrayList;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DismissCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses input arguments, and creates a new {@code DismissCommand} object**/
public class DismissTaskCommandParser implements Parser<DismissCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the DismissCommand
     * and returns an DismissCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DismissCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_FROM);
        if (!argMultimap.getValue(PREFIX_FROM).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DismissCommand.MESSAGE_USAGE));
        }
        String target = argMultimap.getValue(PREFIX_FROM).get();
        String persons = argMultimap.getPreamble();
        ArrayList<Index> targetIndexes = parseIndexes(target);
        ArrayList<Index> personIndexes = parseIndexes(persons);
        if (targetIndexes.size() != 1) {
            throw new ParseException(DismissCommand.MESSAGE_INVALID_TARGET_ARGS);
        } else if (personIndexes.size() < 1) {
            throw new ParseException(DismissCommand.MESSAGE_INVALID_PERSONS_ARGS);
        }
        Index taskIndex = targetIndexes.get(0);
        return new DismissCommand(personIndexes, taskIndex);
    }

    /**
     *   Parses the given {@code String} and returns an ArrayList of Indexes that correspond to
     *   the value in the String.
     *   @throws ParseException if any of the values in the String cannot be converted into an {@code Index}
     */
    private ArrayList<Index> parseIndexes(String args) throws ParseException {
        String[] splitted = args.split(" ");
        ArrayList<Index> targetsToAdd = new ArrayList<>();
        int parsedInt;
        try {
            for (String s : splitted) {
                parsedInt = Integer.parseInt(s);
                targetsToAdd.add(Index.fromOneBased(parsedInt));
            }
        } catch (NumberFormatException nfe) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return targetsToAdd;
    }
}
```
###### /java/seedu/address/logic/parser/AddCommandParser.java
``` java
    /**
     * Constructs a ReadOnlyTask from the arguments provided.
     */
    private static ReadOnlyTask constructTask(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_DESCRIPTION, PREFIX_DEADLINE, PREFIX_PRIORITY,
                        PREFIX_ADDRESS);

        if (!(arePrefixesPresent(argMultimap, PREFIX_NAME))) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_TASK_USAGE));
        }

        try {
            TaskName name;
            Description description;
            Deadline deadline;
            Priority priority;
            TaskAddress address;

            name = ParserUtil.parseTaskName(argMultimap.getValue(PREFIX_NAME)).get();

            description = arePrefixesPresent(argMultimap, PREFIX_DESCRIPTION)
                    ? ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESCRIPTION)).get()
                    : new Description(null);

            deadline = arePrefixesPresent(argMultimap, PREFIX_DEADLINE)
                    ? ParserUtil.parseDeadline(argMultimap.getValue(PREFIX_DEADLINE)).get()
                    : new Deadline(null);
            priority = arePrefixesPresent(argMultimap, PREFIX_PRIORITY)
                    ? ParserUtil.parsePriority(argMultimap.getValue(PREFIX_PRIORITY)).get()
                    : new Priority(null);

            address = arePrefixesPresent(argMultimap, PREFIX_ADDRESS)
                    ? ParserUtil.parseTaskAddress(argMultimap.getValue(PREFIX_ADDRESS)).get()
                    : new TaskAddress(null);

            ReadOnlyTask task = new Task(name, description, deadline, priority, address);
            return task;
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }
}
```
###### /java/seedu/address/logic/parser/SetTaskIncompleteCommandParser.java
``` java

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.SetIncompleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses input arguments and returns a {@code SetIncompleteCommand} that changes the state of the given command */
public class SetTaskIncompleteCommandParser implements Parser<SetIncompleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SetIncompleteCommand
     * and returns an SetIncompleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SetIncompleteCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new SetIncompleteCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetIncompleteCommand.MESSAGE_USAGE));
        }
    }
}
```
###### /java/seedu/address/logic/parser/SetTaskCompleteCommandParser.java
``` java

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.SetCompleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses input arguments and returns a {@code SetCompleteCommand} that changes the state of the given command */
public class SetTaskCompleteCommandParser implements Parser<SetCompleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SetCompleteCommand
     * and returns an SetCompleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SetCompleteCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new SetCompleteCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetCompleteCommand.MESSAGE_USAGE));
        }
    }
}
```
###### /java/seedu/address/logic/parser/AssignTaskCommandParser.java
``` java

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TARGET;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;

import java.util.ArrayList;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AssignCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses input arguments, and creates a new {@code AssignCommand} object**/
public class AssignTaskCommandParser implements Parser<AssignCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the AssignCommand
     * and returns an AssignCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AssignCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TARGET);
        if (!argMultimap.getValue(PREFIX_TARGET).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignCommand.MESSAGE_USAGE));
        }
        String target = argMultimap.getValue(PREFIX_TARGET).get();
        String persons = argMultimap.getPreamble();
        ArrayList<Index> targetIndexes = parseIndexes(target);
        ArrayList<Index> personIndexes = parseIndexes(persons);
        if (targetIndexes.size() != 1) {
            throw new ParseException(AssignCommand.MESSAGE_INVALID_TARGET_ARGS);
        } else if (personIndexes.size() < 1) {
            throw new ParseException(AssignCommand.MESSAGE_INVALID_PERSONS_ARGS);
        }
        Index taskIndex = targetIndexes.get(0);
        return new AssignCommand(personIndexes, taskIndex);
    }

    /**
     *   Parses the given {@code String} and returns an ArrayList of Indexes that correspond to
     *   the value in the String.
     *   @throws ParseException if any of the values in the String cannot be converted into an {@code Index}
     */
    private ArrayList<Index> parseIndexes(String args) throws ParseException {
        String[] splitted = args.split(" ");
        ArrayList<Index> targetsToAdd = new ArrayList<>();
        int parsedInt;
        try {
            for (String s : splitted) {
                parsedInt = Integer.parseInt(s);
                targetsToAdd.add(Index.fromOneBased(parsedInt));
            }
        } catch (NumberFormatException nfe) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return targetsToAdd;
    }
}
```
###### /java/seedu/address/logic/parser/EditTagCommandParser.java
``` java
/** Parses input arguments and creates a new EditTagCommand object */
public class EditTagCommandParser implements Parser<EditTagCommand> {
    public static final String EDITTAG_VALIDATION_REGEX = "[\\p{Alnum}\\s]+[\\p{Alnum}]+";
    public static final int EXPECTED_NUMBER_OF_ARGS = 2;

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditTagCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArrayList<Tag> tags;
        String trimmed = args.trim();
        if (!args.matches(EDITTAG_VALIDATION_REGEX)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditTagCommand.MESSAGE_USAGE));
        }

        try {
            tags = readTags(trimmed);
        } catch (IllegalValueException ive) {
            throw new ParseException(MESSAGE_INVALID_TAG_NAME);
        }

        if (tags.size() != EXPECTED_NUMBER_OF_ARGS) {
            throw new ParseException(MESSAGE_INSUFFICIENT_ARGS);
        }

        Tag toChange = tags.get(0);
        Tag newTag = tags.get(1);
        if (toChange.equals(newTag)) {
            throw new ParseException(MESSAGE_DUPLICATE_TAGS);
        }

        return new EditTagCommand(toChange, newTag);
    }
    /** Atempts to read the string and parse it into a Tag set*/
    private ArrayList<Tag> readTags(String args) throws IllegalValueException {
        String[] splittedArgs = args.split("\\s+");
        ArrayList<Tag> tagList = new ArrayList<>();
        for (String s : splittedArgs) {
            Tag newTag = new Tag(s);
            tagList.add(newTag);
        }
        return tagList;
    }
}
```
###### /java/seedu/address/logic/parser/ParserUtil.java
``` java
    /**
     * Parses a string into a {@code TaskName} if it is present.
     */
    public static Optional<TaskName> parseTaskName(Optional<String> name) throws IllegalValueException {
        requireNonNull(name);
        return name.isPresent() ? Optional.of(new TaskName(name.get())) : Optional.empty();
    }

    /**
     * Parses a string into a {@code Description} if it is present.
     */
    public static Optional<Description> parseDescription(Optional<String> description) throws IllegalValueException {
        requireNonNull(description);
        return description.isPresent() ? Optional.of(new Description(description.get())) : Optional.empty();
    }

    /**
     * Parses a string into a {@code Deadline} if it is present.
     */
    public static Optional<Deadline> parseDeadline(Optional<String> deadline) throws IllegalValueException {
        requireNonNull(deadline);
        return deadline.isPresent() ? Optional.of(new Deadline(deadline.get())) : Optional.empty();
    }

    /**
     * Parses a string into a {@code Priority} if it is present.
     */
    public static Optional<Priority> parsePriority(Optional<String> priority) throws IllegalValueException {
        requireNonNull(priority);
        return priority.isPresent() ? Optional.of(new Priority(priority.get())) : Optional.empty();
    }

```
###### /java/seedu/address/logic/commands/SetIncompleteCommand.java
``` java

/** Marks the specified {@Code task} as incomplete */
public class SetIncompleteCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "setincomplete";
    public static final String COMMAND_ALIAS = "sti";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the task at the specified index as <Incomplete>\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Marked Task as incomplete: %1$s";
    public static final String MESSAGE_TASK_ALREADY_COMPLETE = "The specified task is already incomplete";

    private final Index targetIndex;

    public SetIncompleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {

        List<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
        ReadOnlyTask taskToComplete = lastShownList.get(targetIndex.getZeroBased());
        try {

            model.setAsComplete(taskToComplete, false);
        } catch (DuplicateTaskException dte) {
            throw new CommandException(MESSAGE_TASK_ALREADY_COMPLETE);
        } catch (TaskNotFoundException tnfe) {
            throw new AssertionError("This task cannot be missing");
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, taskToComplete));

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SetIncompleteCommand // instanceof handles nulls
                && this.targetIndex.equals(((SetIncompleteCommand) other).targetIndex)); // state check
    }
}
```
###### /java/seedu/address/logic/commands/AssignCommand.java
``` java

import static seedu.address.logic.parser.CliSyntax.PREFIX_TARGET;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.exceptions.DuplicateTaskException;
import seedu.address.model.task.exceptions.TaskNotFoundException;

/** Assigns at least 1 person to a specified task in the Address Book**/
public class AssignCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "assign";
    public static final String COMMAND_ALIAS = "as";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Assigns people to a task in the Address Book. "
            + "Parameters: "
            + "PERSON INDEXES... "
            + PREFIX_TARGET + "TASK ";

    public static final String MESSAGE_SUCCESS = "Assigned %1$s people to \n%2$s";
    public static final String MESSAGE_INVALID_TARGET_ARGS = "Only 1 task index should be specified";
    public static final String MESSAGE_INVALID_PERSONS_ARGS = "At least 1 person index must be specified";
    public static final String MESSAGE_NONE_ASSIGNED = "All the specified persons are already assigned to this task";

    private ArrayList<Index> personIndexes;
    private Index taskIndex;

    public AssignCommand(ArrayList<Index> personIndexes, Index taskIndex) {
        assert(personIndexes.size() > 0);
        this.personIndexes = personIndexes;
        this.taskIndex = taskIndex;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        List<ReadOnlyTask> tasksList = model.getFilteredTaskList();
        ArrayList<ReadOnlyPerson> personIndexes = createPersonsToAssign(this.personIndexes);

        if (taskIndex.getZeroBased() >= tasksList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
        ReadOnlyTask assignedTask = tasksList.get(taskIndex.getZeroBased());
        try {
            model.assignToTask(personIndexes, assignedTask);
        } catch (TaskNotFoundException tnfe) {
            throw new AssertionError("The specified task cannot be missing");
        } catch (DuplicateTaskException dte) {
            throw new CommandException(MESSAGE_NONE_ASSIGNED);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, personIndexes.size(), assignedTask));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AssignCommand // instanceof handles nulls
                && this.personIndexes.equals(((AssignCommand) other).personIndexes)
                && this.taskIndex.equals(((AssignCommand) other).taskIndex)); // state check
    }

    /**
     * Creates a {@code ArrayList} that contains all the {@code ReadOnlyPerson} converted from the {@Code Index}
     * @throws CommandException if the specified Index is out of range
     */
    public ArrayList<ReadOnlyPerson> createPersonsToAssign (ArrayList<Index> indexes)  throws CommandException {
        HashSet<ReadOnlyPerson> addedPersons = new HashSet<>();
        ArrayList<ReadOnlyPerson> personsToAssign = new ArrayList<>();
        List<ReadOnlyPerson> personsList = model.getFilteredPersonList();
        try {
            for (Index i : personIndexes) {
                ReadOnlyPerson toAssign = personsList.get(i.getZeroBased());
                if (!addedPersons.contains(toAssign)) {
                    addedPersons.add(toAssign);
                    personsToAssign.add(toAssign);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        return personsToAssign;
    }
}
```
###### /java/seedu/address/logic/commands/ClearCommand.java
``` java
    public ClearCommand() {
        isClearAll = true;
        isClearPerson = false;
        isClearTask = false;
        type = null;
        cleared = TYPE_ALL;
    }
```
###### /java/seedu/address/logic/commands/ClearCommand.java
``` java
    public ClearCommand(Prefix type) {
        if (type.equals(PREFIX_TASK)) {
            isClearTask = true;
            isClearPerson = false;
            isClearAll = false;
            this.type = PREFIX_TASK;
            cleared = TYPE_TASKS;
        } else if (type.equals(PREFIX_PERSON)) {
            isClearPerson = true;
            isClearTask = false;
            isClearAll = false;
            this.type = PREFIX_PERSON;
            cleared = TYPE_PERSONS;
        } else {
            throw new AssertionError("An invalid type was provided!");
        }
    }

    @Override
```
###### /java/seedu/address/logic/commands/ClearCommand.java
``` java
    public CommandResult executeUndoableCommand() {
        requireNonNull(model);
        if (isClearAll) {
            model.resetData(new AddressBook());
        } else if (isClearTask) {
            model.resetPartialData(new AddressBook(), type);
        } else if (isClearPerson) {
            model.resetPartialData(new AddressBook(), type);
        } else {
            assert false : "At least one boolean must be true.";
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, cleared));
    }
}
```
###### /java/seedu/address/logic/commands/EditTagCommand.java
``` java
public class EditTagCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "edittag";
    public static final String COMMAND_ALIAS = "etag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the specified tag "
            + "and updates all existing contacts that shares this tag with the new value.\n"
            + "Parameters: TAGTOCHANGE (must be alphanumerical) "
            + "TAGNEWNAME (must be alphanumerical)\n"
            + "Example: " + COMMAND_WORD + " friends enemies";

    public static final String MESSAGE_EDIT_TAG_SUCCESS = "Replaced tag %1$s with %2$s";
    public static final String MESSAGE_TAG_NOT_FOUND = "No such tag was found in the address book.";
    public static final String MESSAGE_INSUFFICIENT_ARGS = "Only 2 arguments should be provided!";
    public static final String MESSAGE_INVALID_TAG_NAME = "Tag names must be alphanumerical.";
    public static final String MESSAGE_DUPLICATE_TAGS = "The new name of the tag cannot be the same as the old name.";
    private final ArrayList<Index> affectedIndexes;
    private final Tag toEdit;
    private final Tag newTag;

    /**
     * @param toEdit The value of the tag to be changed
     * @param newTag The new value for the tag
     */
    public EditTagCommand(Tag toEdit, Tag newTag) {
        requireNonNull(toEdit);
        requireNonNull(newTag);
        this.toEdit = toEdit;
        this.newTag = newTag;
        this.affectedIndexes = new ArrayList<>();
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();
        ReadOnlyPerson currentlyViewed;
        Set<Tag> tagSet;
        boolean tagNotPresent = true;
        for (int i = 0; i < lastShownList.size(); i++) {
            currentlyViewed = lastShownList.get(i);
            tagSet = currentlyViewed.getTags();
            if (tagSet.contains(toEdit)) {
                tagNotPresent = false;
                affectedIndexes.add(Index.fromZeroBased(i));
            }
        }

        if (tagNotPresent) {
            throw new CommandException(MESSAGE_TAG_NOT_FOUND);
        }
        try {
            model.editTag(toEdit, newTag, affectedIndexes);
        } catch (DuplicatePersonException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_TAGS);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target person cannot be missing");
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_TAG_SUCCESS, toEdit.tagName, newTag.tagName));
    }


    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditTagCommand)) {
            return false;
        }

        // state check
        EditTagCommand e = (EditTagCommand) other;
        return toEdit.equals(e.toEdit)
                && newTag.equals(e.newTag);
    }
}
```
###### /java/seedu/address/logic/commands/DismissCommand.java
``` java

import static seedu.address.logic.parser.CliSyntax.PREFIX_FROM;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.exceptions.DuplicateTaskException;
import seedu.address.model.task.exceptions.TaskNotFoundException;

/** Dismisses at least 1 person from a specified task in the Address Book**/
public class DismissCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "dismiss";
    public static final String COMMAND_ALIAS = "ds";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Dismisses people from a task in the Address Book. "
            + "Parameters: "
            + "PERSON INDEXES... "
            + PREFIX_FROM + "TASK ";

    public static final String MESSAGE_SUCCESS = "Dismissed %1$s people from task \n%2$s";
    public static final String MESSAGE_INVALID_TARGET_ARGS = "Only 1 task index should be specified";
    public static final String MESSAGE_INVALID_PERSONS_ARGS = "At least 1 person index must be specified";
    public static final String MESSAGE_NONE_ASSIGNED = "None of the specified persons are assigned to this task";

    private ArrayList<Index> personIndexes;
    private Index taskIndex;

    public DismissCommand(ArrayList<Index> personIndexes, Index taskIndex) {
        assert(personIndexes.size() > 0);
        this.personIndexes = personIndexes;
        this.taskIndex = taskIndex;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        List<ReadOnlyTask> tasksList = model.getFilteredTaskList();
        ArrayList<ReadOnlyPerson> personIndexes = createPersonsToDismiss(this.personIndexes);

        if (taskIndex.getZeroBased() >= tasksList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
        ReadOnlyTask dismissedTask = tasksList.get(taskIndex.getZeroBased());
        try {
            model.dismissFromTask(personIndexes, dismissedTask);
        } catch (TaskNotFoundException tnfe) {
            throw new AssertionError("The specified task cannot be missing");
        } catch (DuplicateTaskException dte) {
            throw new CommandException(MESSAGE_NONE_ASSIGNED);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, personIndexes.size(), dismissedTask));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DismissCommand // instanceof handles nulls
                && this.personIndexes.equals(((DismissCommand) other).personIndexes)
                && this.taskIndex.equals(((DismissCommand) other).taskIndex)); // state check
    }

    /**
     * Creates a {@code ArrayList} that contains all the {@code ReadOnlyPerson} converted from the {@Code Index}
     * @throws CommandException if the specified Index is out of range
     */
    public ArrayList<ReadOnlyPerson> createPersonsToDismiss (ArrayList<Index> indexes)  throws CommandException {
        ArrayList<ReadOnlyPerson> personsToDismiss = new ArrayList<>();
        List<ReadOnlyPerson> personsList = model.getFilteredPersonList();
        try {
            for (Index i : indexes) {
                ReadOnlyPerson toDismiss = personsList.get(i.getZeroBased());
                if (!personsToDismiss.contains(toDismiss)) {
                    personsToDismiss.add(toDismiss);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        return personsToDismiss;
    }
}
```
###### /java/seedu/address/logic/commands/EditCommand.java
``` java
    /**
     * Stores the details to edit the task with. Each non-empty field value will replace the
     * corresponding field value of the task.
     */
    public static class EditTaskDescriptor {
        private TaskName taskName;
        private Description description;
        private Deadline deadline;
        private Priority priority;
        private Assignees assignees;
        private TaskAddress taskAddress;

        public EditTaskDescriptor() {}

        public EditTaskDescriptor(EditTaskDescriptor toCopy) {
            this.taskName = toCopy.taskName;
            this.description = toCopy.description;
            this.deadline = toCopy.deadline;
            this.priority = toCopy.priority;
            this.assignees = toCopy.assignees;
            this.taskAddress = toCopy.taskAddress;
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(this.taskName, this.description, this.deadline, this.priority,
                    this.taskAddress);
        }

        public void setTaskName(TaskName taskName) {
            this.taskName = taskName;
        }

        public Optional<TaskName> getTaskName() {
            return Optional.ofNullable(taskName);
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public Optional<Description> getDescription() {
            return Optional.ofNullable(description);
        }

        public void setDeadline(Deadline deadline) {
            this.deadline = deadline;
        }

        public Optional<Deadline> getDeadline()  {
            return Optional.ofNullable(deadline);
        }

        public void setPriority(Priority priority) {
            this.priority = priority;
        }

        public Optional<Priority> getPriority() {
            return Optional.ofNullable(priority);
        }

        public void setTaskAddress(TaskAddress taskAddress) {
            this.taskAddress = taskAddress;
        }

        public Optional<TaskAddress> getTaskAddress() {
            return Optional.ofNullable(taskAddress);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditTaskDescriptor)) {
                return false;
            }

            // state check
            EditTaskDescriptor e = (EditTaskDescriptor) other;

            return getTaskName().equals(e.getTaskName())
                    && getDescription().equals(e.getDescription())
                    && getDeadline().equals(e.getDeadline())
                    && getPriority().equals(e.getPriority())
                    && getTaskAddress().equals(e.getTaskAddress());
        }
    }
}
```
###### /java/seedu/address/logic/commands/SetCompleteCommand.java
``` java

/** Marks the specified {@Code task} as complete */
public class SetCompleteCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "setcomplete";
    public static final String COMMAND_ALIAS = "stc";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the task at the specified index as <Completed>\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Marked Task as completed: %1$s";
    public static final String MESSAGE_TASK_ALREADY_COMPLETE = "The specified task is already completed";

    private final Index targetIndex;

    public SetCompleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {

        List<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
        ReadOnlyTask taskToComplete = lastShownList.get(targetIndex.getZeroBased());
        try {
            model.setAsComplete(taskToComplete, true);
        } catch (DuplicateTaskException dte) {
            throw new CommandException(MESSAGE_TASK_ALREADY_COMPLETE);
        } catch (TaskNotFoundException tnfe) {
            throw new AssertionError("This task cannot be missing");
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, taskToComplete));

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SetCompleteCommand // instanceof handles nulls
                && this.targetIndex.equals(((SetCompleteCommand) other).targetIndex)); // state check
    }
}
```
###### /java/seedu/address/logic/LogicManager.java
``` java
    @Override
    public ObservableList<ReadOnlyTask> getFilteredTaskList() {
        return model.getFilteredTaskList();
    }
```
###### /java/seedu/address/storage/XmlAdaptedTask.java
``` java
/** JAXB-friendly version of a Task */
public class XmlAdaptedTask {
    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String description;
    @XmlElement(required = true)
    private String deadline;
    @XmlElement(required = true)
    private String priority;
    @XmlElement(required = true)
    private String state;
    @XmlElement
    private List<XmlAdaptedIndex> assignees = new ArrayList<>();
    @XmlElement(required = true)
    private String address;

    /**
     * Constructs an XmlAdaptedTask.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedTask() {}


    /**
     * Converts a given Task into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedTask
     */
    public XmlAdaptedTask(ReadOnlyTask source) {
        name = source.getTaskName().taskName;
        description = source.getDescription().value;
        deadline = source.getDeadline().value;
        priority = Integer.toString(source.getPriority().value);
        state = String.valueOf(source.getCompleteState());
        address = source.getTaskAddress().taskAddress;
        assignees = new ArrayList<>();
        for (Index i : source.getAssignees().getList()) {
            assignees.add(new XmlAdaptedIndex(i));
        }
    }

    /**
     * Converts this jaxb-friendly adapted person object into the model's Task object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Task toModelType() throws IllegalValueException {
        final TaskName name = new TaskName(this.name);
        final Description description = new Description(this.description);
        final Deadline deadline = new Deadline(this.deadline);
        final Priority priority = new Priority(this.priority);
        final Boolean state = Boolean.valueOf(this.state);
        final TaskAddress address = new TaskAddress(this.address);
        final ArrayList<Index> assigneeIndexes = new ArrayList<>();
        for (XmlAdaptedIndex index : assignees) {
            assigneeIndexes.add(index.toModelType());
        }
        final Assignees assignees = new Assignees(assigneeIndexes);
        return new Task(name, description, deadline, priority, assignees, state, address);
    }
}
```
###### /java/seedu/address/model/person/UniquePersonList.java
``` java
    /**
     * Returns an array list of {@code Index} corresponding to the {@code ReadOnlyPerson} specified
     */
    public ArrayList<Index> extractIndexes(ArrayList<ReadOnlyPerson> persons) {
        ArrayList<Index> indexes = new ArrayList<>();
        for (ReadOnlyPerson p : persons) {
            assert(internalList.contains(p));
            int position = internalList.indexOf(p);
            indexes.add(Index.fromZeroBased(position));
        }
        return indexes;
    }

    /**
     * Returns an array containing:
     * Index - The old index of each person in the internalList before sorting
     * Value - The new index of each person after a sort operation
     */
    public Index[] getMappings() {
        Index[] mappings = new Index[internalCopy.size()];
        int count = 0;
        for (Person p : internalCopy) {
            assert(internalList.contains(p));
            int index =  internalList.indexOf(p);
            mappings[count] = Index.fromZeroBased(index);
            count++;
        }
        return mappings;
    }
```
###### /java/seedu/address/model/AddressBook.java
``` java
    /**
     * Resets only the existing contact or task data of this {@code AddressBook}.
     */
    public void resetPartialData(ReadOnlyAddressBook newData, Prefix type) {
        requireNonNull(newData);
        requireNonNull(type);
        try {
            if (type.equals(PREFIX_TASK)) {
                setTasks(newData.getTasksList());
            } else if (type.equals(PREFIX_PERSON)) {
                tasks.clearAssignees();
                setPersons(newData.getPersonList());
                setTags(new HashSet<>(newData.getTagList()));
                syncMasterTagListWith(persons);
            } else {
                throw new AssertionError("Type must either be persons or tasks");
            }
        } catch (DuplicatePersonException e) {
            assert false : "Address books should not have duplicate persons";
        } catch (DuplicateTaskException e) {
            assert false : "Address books should not have duplicate tasks";
        }
    }
```
###### /java/seedu/address/model/AddressBook.java
``` java
    /**
     * Adds a task to the address book.
     *
     * @throws DuplicateTaskException if an equivalent task already exists.
     */
    public void addTask(ReadOnlyTask t) throws DuplicateTaskException {
        Task newTask = new Task(t);
        tasks.add(newTask);
    }

```
###### /java/seedu/address/model/AddressBook.java
``` java
    /**
     * Removes {@code key} from this {@code AddressBook}.
     * @throws TaskNotFoundException if the {@code key} is not in this {@code AddressBook}.
     */
    public boolean removeTask(ReadOnlyTask key) throws TaskNotFoundException {
        if (tasks.remove(key)) {
            return true;
        } else {
            throw new TaskNotFoundException();
        }
    }

    /** Removes the specified person from all assignment lists for every task **/
    public void removePersonFromAssignees(Index target) {
        tasks.removeAssignee(target);
    }

    /**
     * Updates the Assignees for all tasks in the internal tasks list with their new mappings
     */
    public void updateTaskAssigneeMappings(Index[] mappings) {
        tasks.updateAssignees(mappings);
    }

```
###### /java/seedu/address/model/AddressBook.java
``` java
    /**
     * Replaces the given task {@code target} in the list with {@code editedReadOnlyTask}.
     *
     * @throws DuplicateTaskException if updating the task's details causes the task to be equivalent to
     *      another existing task in the list.
     * @throws TaskNotFoundException if {@code target} could not be found in the list.
     */
    public void updateTask(ReadOnlyTask target, ReadOnlyTask editedReadOnlyTask)
            throws DuplicateTaskException, TaskNotFoundException {
        requireNonNull(editedReadOnlyTask);

        Task editedTask = new Task(editedReadOnlyTask);
        tasks.setTask(target, editedTask);
    }
```
###### /java/seedu/address/model/AddressBook.java
``` java
    @Override
    public ObservableList<ReadOnlyTask> getTasksList() {
        return tasks.asObservableList();
    }
```
###### /java/seedu/address/model/ModelManager.java
``` java
    @Override
    public void resetPartialData(ReadOnlyAddressBook newData, Prefix type) {
        assert(type.equals(PREFIX_TASK) || type.equals(PREFIX_PERSON));
        if (type.equals(PREFIX_TASK)) {
            addressBook.resetPartialData(newData, PREFIX_TASK);
            indicateAddressBookChanged();
        } else {
            addressBook.resetPartialData(newData, PREFIX_PERSON);
            indicateAddressBookChanged();
        }
    }
```
###### /java/seedu/address/model/ModelManager.java
``` java
    /**
     * Replaces the toChange Tag with the newTag Tag, for all Person objects denoted by the indexes.
     * Guarantees: indexes contains at least 1 person that has the toChange Tag.
     */
    public synchronized void editTag(Tag toChange, Tag newTag, ArrayList<Index> indexes)
            throws PersonNotFoundException, DuplicatePersonException {
        List<ReadOnlyPerson> allPersons = this.getFilteredPersonList();
        Set<Tag> personTags;
        Person toUpdate;
        ReadOnlyPerson toRead;
        int index;
        for (Index i : indexes) {
            index = i.getZeroBased();
            toRead = allPersons.get(index);
            toUpdate = new Person(toRead);
            personTags = new HashSet<>(toRead.getTags());
            personTags.remove(toChange);
            personTags.add(newTag);
            toUpdate.setTags(personTags);
            addressBook.updatePerson(toRead, toUpdate);
        }
        indicateAddressBookChanged();
    }
```
###### /java/seedu/address/model/ModelManager.java
``` java
    @Override
    public synchronized void addTask(ReadOnlyTask toAdd) throws DuplicateTaskException {
        addressBook.addTask(toAdd);
        updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        indicateAddressBookChanged();
    }

```
###### /java/seedu/address/model/ModelManager.java
``` java
    @Override
    public synchronized void deleteTask(ReadOnlyTask toDelete) throws TaskNotFoundException {
        addressBook.removeTask(toDelete);
        indicateAddressBookChanged();
    }

```
###### /java/seedu/address/model/ModelManager.java
``` java
    @Override
    public void updateTask(ReadOnlyTask target, ReadOnlyTask editedTask)
            throws DuplicateTaskException, TaskNotFoundException {
        requireAllNonNull(target, editedTask);

        addressBook.updateTask(target, editedTask);
        indicateAddressBookChanged();
    }

```
###### /java/seedu/address/model/ModelManager.java
``` java
    @Override
    public void assignToTask(ArrayList<ReadOnlyPerson> personsToAssign, ReadOnlyTask taskToAssignTo)
            throws TaskNotFoundException, DuplicateTaskException {
        Assignees assignees = taskToAssignTo.getAssignees();
        Assignees newAssignees = new Assignees(assignees);
        ArrayList<Index> positions = addressBook.extractPersonIndexes(personsToAssign);

        newAssignees.assign(positions);
        ReadOnlyTask updatedTask = constructTaskWithNewAssignee(taskToAssignTo, newAssignees);
        addressBook.updateTask(taskToAssignTo, updatedTask);
        indicateAddressBookChanged();
    }

```
###### /java/seedu/address/model/ModelManager.java
``` java
    @Override
    public void dismissFromTask(ArrayList<ReadOnlyPerson> personsToDismiss, ReadOnlyTask taskToDismissFrom)
            throws TaskNotFoundException, DuplicateTaskException {
        Assignees assignees = taskToDismissFrom.getAssignees();
        Assignees newAssignees = new Assignees(assignees);
        ArrayList<Index> positions = addressBook.extractPersonIndexes(personsToDismiss);

        newAssignees.dismiss(positions);
        ReadOnlyTask updatedTask = constructTaskWithNewAssignee(taskToDismissFrom, newAssignees);
        addressBook.updateTask(taskToDismissFrom, updatedTask);
        indicateAddressBookChanged();
    }

```
###### /java/seedu/address/model/ModelManager.java
``` java
    public void setAsComplete(ReadOnlyTask toSet, boolean isComplete)
            throws TaskNotFoundException, DuplicateTaskException {
        TaskName taskName = toSet.getTaskName();
        Description description = toSet.getDescription();
        Deadline deadline = toSet.getDeadline();
        Priority priority = toSet.getPriority();
        Assignees assignees = toSet.getAssignees();
        TaskAddress taskAddress = toSet.getTaskAddress();
        Boolean state = isComplete;
        if (state == toSet.getCompleteState()) {
            throw new DuplicateTaskException();
        }
        ReadOnlyTask updatedTask = new Task(taskName, description, deadline, priority, assignees, state, taskAddress);
        updateTask(toSet, updatedTask);
    }
```
###### /java/seedu/address/model/ModelManager.java
``` java
    @Override
    public ObservableList<ReadOnlyTask> getFilteredTaskList() {
        return FXCollections.unmodifiableObservableList(filteredTasks);
    }
```
###### /java/seedu/address/model/ModelManager.java
``` java
    @Override
    public void updateFilteredTaskList(Predicate<ReadOnlyTask> predicate) {
        requireNonNull(predicate);
        filteredTasks.setPredicate(predicate);
    }
```
###### /java/seedu/address/model/task/ReadOnlyTask.java
``` java
/**
 * Provides an immutable interface for a Task in the address book.
 */
public interface ReadOnlyTask {

    TaskName getTaskName();
    Description getDescription();
    Deadline getDeadline();
    Priority getPriority();
    Assignees getAssignees();
    boolean getCompleteState();
    String getPrintableState();
    TaskAddress getTaskAddress();
    ObjectProperty<TaskName> taskNameProperty();
    ObjectProperty<Description> descriptionProperty();
    ObjectProperty<Deadline> deadlineProperty();
    ObjectProperty<Priority> priorityProperty();
    ObjectProperty<Assignees> assigneeProperty();
    ObjectProperty<TaskAddress> taskAddressProperty();
    ObjectProperty<String> stateProperty();
    ObjectProperty<String> changeStateProperty();
    void changeState();

    default String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getTaskName())
                .append(" Description: ")
                .append(getDescription())
                .append(" Deadline: ")
                .append(getDeadline())
                .append(" Priority: ")
                .append(getPriority())
                .append(" Address: ")
                .append(getTaskAddress())
                .append(" ")
                .append(getPrintableState());
        return builder.toString();
    }

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyTask other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getTaskName().equals(this.getTaskName()) // state checks here onwards
                && other.getDescription().equals(this.getDescription())
                && other.getDeadline().equals(this.getDeadline())
                && other.getPriority().equals(this.getPriority()))
                && other.getAssignees().equals(this.getAssignees())
                && other.getCompleteState() == this.getCompleteState()
                && other.getTaskAddress().equals(this.getTaskAddress());
    }
}
```
###### /java/seedu/address/model/task/Description.java
``` java
/**
 * Represents a task description in the address book.
 */
public class Description {

    public static final String MESSAGE_DESCRIPTION_CONSTRAINTS =
            "Task descriptions can be in any format";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String DESCRIPTION_VALIDATION_REGEX = "[^\\s].*";
    public static final String DESCRIPTION_PLACEHOLDER_VALUE = "";
    public final String value;

    /**
     * Validates given description.
     *
     * @throws IllegalValueException if given description string is invalid.
     */
    public Description(String description) throws IllegalValueException {
        if (description == null) {
            this.value = DESCRIPTION_PLACEHOLDER_VALUE;
            return;
        }
        String trimmedDescription = description.trim();
        if (!isValidDescription(trimmedDescription)) {
            throw new IllegalValueException(MESSAGE_DESCRIPTION_CONSTRAINTS);
        }
        this.value = trimmedDescription;
    }

    /**
     * Returns true if a given string is a valid description.
     */
    public static boolean isValidDescription(String test) {
        return test.equals(DESCRIPTION_PLACEHOLDER_VALUE) || test.matches(DESCRIPTION_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Description // instanceof handles nulls
                && this.value.equals(((Description) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
```
###### /java/seedu/address/model/task/exceptions/TaskNotFoundException.java
``` java
/**
 * Signals that the operation could not find the specified task.
 */
public class TaskNotFoundException extends Exception {
}
```
###### /java/seedu/address/model/task/exceptions/DuplicateTaskException.java
``` java
/**
 * Signals that the operation will result in duplicate Person objects.
 */
public class DuplicateTaskException extends DuplicateDataException {
    public DuplicateTaskException() {
        super("Operation would result in duplicate tasks");
    }
}
```
###### /java/seedu/address/model/task/TaskContainsKeywordPredicate.java
``` java
/**
 * Tests that a {@code ReadOnlyTask}'s {@code TaskName} or {@code Description} matches any of the keywords given.
 */
public class TaskContainsKeywordPredicate  implements Predicate<ReadOnlyTask> {
    private final List<String> keywords;
    private boolean needFilterByState;
    private boolean needFilterByPriority;
    private boolean isComplete;
    private int basePriority;

    public TaskContainsKeywordPredicate(List<String> keywords, boolean isStateCheckRequired,
                                        boolean isPriorityCheckRequired, boolean isComplete, int basePriority) {
        this.keywords = keywords;
        this.needFilterByPriority = isPriorityCheckRequired;
        this.needFilterByState = isStateCheckRequired;
        this.isComplete = isComplete;
        this.basePriority = basePriority;
    }

    public TaskContainsKeywordPredicate(List<String> keywords) {
        this.keywords = keywords;
        this.needFilterByPriority = false;
        this.needFilterByState = false;
        this.isComplete = false;
        this.basePriority = 0;
    }

    @Override
    public boolean test(ReadOnlyTask task) {
        for (int i = 0; i < keywords.size(); i++) {
            String keyword = keywords.get(i);
            if (needFilterByState && task.getCompleteState() != isComplete) {
                return false;
            } else if (needFilterByPriority && task.getPriority().value < basePriority) {
                return false;
            } else {
                return (StringUtil.containsWordIgnoreCase(task.getTaskName().taskName, keyword)
                        || StringUtil.containsWordIgnoreCase(task.getDescription().value, keyword));
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskContainsKeywordPredicate // instanceof handles nulls
                && this.keywords.equals(((TaskContainsKeywordPredicate) other).keywords)
                && this.needFilterByPriority == ((TaskContainsKeywordPredicate) other).needFilterByPriority
                && this.needFilterByState == ((TaskContainsKeywordPredicate) other).needFilterByState
                && this.isComplete == ((TaskContainsKeywordPredicate) other).isComplete
                && this.basePriority == ((TaskContainsKeywordPredicate) other).basePriority); // state check
    }
}
```
###### /java/seedu/address/model/task/Task.java
``` java
/**
 * Represents a task object in the address book.
 */
public class Task implements ReadOnlyTask {

    public static final String TASK_INCOMPLETE = "<Incomplete>";
    public static final String TASK_COMPLETE = "<Complete>";

    private ObjectProperty<TaskName> taskName;
    private ObjectProperty<Description> description;
    private ObjectProperty<Deadline> deadline;
    private ObjectProperty<Priority> priority;
    private ObjectProperty<Assignees> assignees;
    private ObjectProperty<Boolean> state;
    private ObjectProperty<TaskAddress> taskAddress;

    public Task(TaskName taskName, Description description, Deadline deadline, Priority priority,
                Assignees assignees, boolean isComplete, TaskAddress taskAddress) {
        this.taskName = new SimpleObjectProperty<>(taskName);
        this.description = new SimpleObjectProperty<>(description);
        this.deadline = new SimpleObjectProperty<>(deadline);
        this.priority = new SimpleObjectProperty<>(priority);
        this.assignees = new SimpleObjectProperty<>(assignees);
        this.state = new SimpleObjectProperty<>(isComplete);
        this.taskAddress = new SimpleObjectProperty<>(taskAddress);
    }

    /**
     * Creates a new Task object from the given arguments
     * New tasks will not have anyone assigned to them by default, and will be marked as incomplete
     * by default.
     */
    public Task(TaskName taskName, Description description, Deadline deadline, Priority priority,
                TaskAddress taskAddress) {
        this.taskName = new SimpleObjectProperty<>(taskName);
        this.description = new SimpleObjectProperty<>(description);
        this.deadline = new SimpleObjectProperty<>(deadline);
        this.priority = new SimpleObjectProperty<>(priority);
        this.assignees = new SimpleObjectProperty<>(new Assignees());
        this.state = new SimpleObjectProperty<>(false);
        this.taskAddress = new SimpleObjectProperty<>(taskAddress);
    }

    public Task(ReadOnlyTask task) {
        this(task.getTaskName(), task.getDescription(), task.getDeadline(), task.getPriority(),
                task.getAssignees(), task.getCompleteState(), task.getTaskAddress());
    }

    public TaskName getTaskName() {
        return taskName.get();
    }

    @Override
    public Description getDescription() {
        return description.get();
    }

    @Override
    public Deadline getDeadline() {
        return deadline.get();
    }

    @Override
    public Priority getPriority() {
        return priority.get();
    }

    @Override
    public Assignees getAssignees() {
        return assignees.get();
    }

    @Override
    public boolean getCompleteState() {
        return state.get();
    }

    @Override
    public TaskAddress getTaskAddress() {
        return taskAddress.get();
    }

    @Override
    public String toString() {
        return getAsText();
    }

    // JavaFX property functions
    @Override
    public ObjectProperty<TaskName> taskNameProperty() {
        return taskName;
    }

    @Override
    public ObjectProperty<Description> descriptionProperty() {
        return description;
    }

    @Override
    public ObjectProperty<Deadline> deadlineProperty() {
        return deadline;
    }

    @Override
    public ObjectProperty<Priority> priorityProperty() {
        return priority;
    }

    @Override
    public ObjectProperty<Assignees> assigneeProperty() {
        return assignees;
    }

    public void clearAssignees() {
        this.assignees = new SimpleObjectProperty<>(new Assignees());
    }

    @Override
    public ObjectProperty<TaskAddress> taskAddressProperty() {
        return taskAddress;
    }

    @Override
    public ObjectProperty<String> stateProperty() {
        String printableState = getPrintableState();
        return new SimpleObjectProperty<>(printableState);
    }

    @Override
    public ObjectProperty<String> changeStateProperty() {
        boolean state = getCompleteState();
        if (state) {
            return new SimpleObjectProperty<>("Set as incomplete");
        } else {
            return new SimpleObjectProperty<>("Set as complete");
        }
    }
    // Setters for TaskBuilder testing

    public void setTaskName(TaskName taskName) {
        this.taskName.set(taskName);
    }

    public void setDeadline(Deadline deadline) {
        this.deadline.set(deadline);
    }

    public void setDescription(Description description) {
        this.description.set(description);
    }

    public void setPriority(Priority priority) {
        this.priority.set(priority);
    }

    public void setAssignees(Assignees assignees) {
        this.assignees.set(assignees);
    }

    public void setState(boolean state) {
        this.state.set(state);
    }

    public void changeState() {
        this.setState(!this.getCompleteState());
    }

    public void setTaskAddress(TaskAddress taskAddress) {
        this.taskAddress.set(taskAddress);
    }

    public String getPrintableState() {
        String printableState;
        if (state.get()) {
            printableState = TASK_COMPLETE;
        } else {
            printableState = TASK_INCOMPLETE;
        }
        return printableState;
    }
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof ReadOnlyTask // instanceof handles nulls
            && this.isSameStateAs((ReadOnlyTask) other));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(taskName, description, deadline, priority, assignees, state);
    }
}
```
###### /java/seedu/address/model/task/UniqueTaskList.java
``` java
/**
 * A list of tasks that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Task#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueTaskList implements Iterable<Task> {

    private final ObservableList<Task> internalList = FXCollections.observableArrayList();
    // used by asObservableList()
    private final ObservableList<ReadOnlyTask> mappedList = EasyBind.map(internalList, (task) -> task);

    /**
     * Returns true if the list contains an equivalent task as the given argument.
     */
    public boolean contains(ReadOnlyTask toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a task to the list.
     *
     * @throws DuplicateTaskException if the task to add is a duplicate of an existing task in the list.
     */
    public void add(ReadOnlyTask toAdd) throws DuplicateTaskException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateTaskException();
        }
        internalList.add(new Task(toAdd));
    }

    /**
     * Replaces the task {@code target} in the list with {@code editedTask}.
     *
     * @throws DuplicateTaskException if the replacement is equivalent to another existing task in the list.
     * @throws TaskNotFoundException if {@code target} could not be found in the list.
     */
    public void setTask(ReadOnlyTask target, ReadOnlyTask editedTask)
            throws DuplicateTaskException, TaskNotFoundException {
        requireNonNull(editedTask);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new TaskNotFoundException();
        }

        if (!target.equals(editedTask) && internalList.contains(editedTask)) {
            throw new DuplicateTaskException();
        }

        internalList.set(index, new Task(editedTask));
    }

    /**
     * Removes the equivalent task from the list.
     *
     * @throws TaskNotFoundException if no such task could be found in the list.
     */
    public boolean remove(ReadOnlyTask toRemove) throws TaskNotFoundException {
        requireNonNull(toRemove);
        final boolean taskFoundAndDeleted = internalList.remove(toRemove);
        if (!taskFoundAndDeleted) {
            throw new TaskNotFoundException();
        }
        return taskFoundAndDeleted;
    }

    public void setTasks(UniqueTaskList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setTasks(List<? extends ReadOnlyTask> tasks) throws DuplicateTaskException {
        final UniqueTaskList replacement = new UniqueTaskList();
        for (final ReadOnlyTask task : tasks) {
            replacement.add(new Task(task));
        }
        setTasks(replacement);
    }

    /**
     * Removes all assignees from all tasks.
     */
    public void clearAssignees() {
        for (Task t : internalList) {
            t.clearAssignees();
        }
    }

    /** Removes the specified assignee from all tasks **/
    public void removeAssignee(Index personIndex) {
        ObservableList<Task> internalListCopy = FXCollections.observableArrayList();
        for (Task t : internalList) {
            TaskName name = t.getTaskName();
            Description description = t.getDescription();
            Deadline deadline = t.getDeadline();
            Priority priority = t.getPriority();
            TaskAddress taskAddress = t.getTaskAddress();
            boolean state = t.getCompleteState();
            Assignees assignees = t.getAssignees();

            Assignees updated = new Assignees(assignees);
            updated.decrementIndex(personIndex);
            internalListCopy.add(new Task(name, description, deadline, priority, updated, state, taskAddress));
        }
        internalList.clear();
        internalList.addAll(internalListCopy);
    }

    /**
     * Updates the assignee list within each task to match that of the newPersonIndexes.
     * This method is called after a sort persons operation due to the order change
     */
    public void updateAssignees(Index[] newPersonIndexes) {
        ObservableList<Task> internalListCopy = FXCollections.observableArrayList();
        for (Task t : internalList) {
            TaskName name = t.getTaskName();
            Description description = t.getDescription();
            Deadline deadline = t.getDeadline();
            Priority priority = t.getPriority();
            TaskAddress taskAddress = t.getTaskAddress();
            boolean state = t.getCompleteState();
            Assignees assignees = t.getAssignees();

            Assignees updated = new Assignees(assignees);
            updated.updateList(newPersonIndexes);
            internalListCopy.add(new Task(name, description, deadline, priority, updated, state, taskAddress));
        }
        internalList.clear();
        internalList.addAll(internalListCopy);
    }
    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<ReadOnlyTask> asObservableList() {
        return FXCollections.unmodifiableObservableList(mappedList);
    }

    /**
     * Sorts person list by all persons by any field in ascending or descending order
     * @param field
     * @param order
     */
```
###### /java/seedu/address/model/task/Deadline.java
``` java
/**
 * Represents the deadline of a task in the address book.
 */
public class Deadline {
    public static final String MESSAGE_INVALID_DATE =
            "The specified date is invalid.";
    public static final String DEADLINE_PLACEHOLDER_VALUE = "";

    public final Date date;
    public final String value;

    /**
     * Validates given deadline.
     *
     * @throws IllegalValueException if given deadline string is invalid.
     */
    public Deadline(String deadline) throws IllegalValueException {
        if (deadline == null || deadline.equals(DEADLINE_PLACEHOLDER_VALUE)) {
            this.value = DEADLINE_PLACEHOLDER_VALUE;
            this.date = null;
            return;
        }
        this.date = setDateFromArgs(deadline);
        this.value = date.toString();
    }

    /**
     * Returns true if the given string is a valid date.
     * Guarantees: given string format is valid
     */
    public static boolean isValidDeadline(String test) {
        if (test.equals(DEADLINE_PLACEHOLDER_VALUE)) {
            return true;
        }
        try {
            setDateFromArgs(test);
            return true;
        } catch (IllegalValueException e) {
            return false;
        }
    }

    /**
     * Returns a Date object that represents the given date string.
     */
    private static Date setDateFromArgs(String date) throws IllegalValueException {
        Parser deadlineParser = new Parser();
        List<DateGroup> groups = deadlineParser.parse(date);
        List<Date> dates = null;
        for (DateGroup group : groups) {
            dates = group.getDates();
        }
        if (dates == null) {
            throw new IllegalValueException(MESSAGE_INVALID_DATE);
        } else {
            return dates.get(0);
        }
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Deadline // instanceof handles nulls
                && this.value.equals(((Deadline) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
```
###### /java/seedu/address/model/task/TaskName.java
``` java
/**
 * Represents a Task name in the address book.
 */
public class TaskName {

    public static final String MESSAGE_NAME_CONSTRAINTS =
            "Task names can be in any format, and should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String NAME_VALIDATION_REGEX = "[^\\s].*";

    public final String taskName;

    /**
     * Validates given name.
     *
     * @throws IllegalValueException if given name string is invalid.
     */
    public TaskName(String name) throws IllegalValueException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!isValidName(trimmedName)) {
            throw new IllegalValueException(MESSAGE_NAME_CONSTRAINTS);
        }
        this.taskName = trimmedName;
    }

    /**
     * Returns true if a given string is a valid task name.
     */
    public static boolean isValidName(String test) {
        return test.matches(NAME_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return taskName;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskName // instanceof handles nulls
                && this.taskName.equals(((TaskName) other).taskName)); // state check
    }

    @Override
    public int hashCode() {
        return taskName.hashCode();
    }

}
```
###### /java/seedu/address/model/task/Priority.java
``` java
/**
 * Represents a task priority in the address book.
 */
public class Priority {

    public static final String MESSAGE_PRIORITY_CONSTRAINTS =
            "Task priorities must be an integer from 1 to 5, inclusive, where 1 represents the lowest priority";
    public static final String[] PRIORITY_TEXT_STRINGS = {"", "Lowest", "Low", "Medium", "High", "Highest"};

    public static final int PRIORITY_LOWER_BOUND = 0;
    public static final int PRIORITY_UPPER_BOUND = 5;
    public static final String PRIORITY_VALIDATION_REGEX = "[\\d].*";
    public static final String PRIORITY_PLACEHOLDER_VALUE = "";
    public final int value;

    /**
     * Validates given priority.
     *
     * @throws IllegalValueException if given priority string is invalid.
     */
    public Priority(String priority) throws IllegalValueException {
        if (priority == null || priority.equals(PRIORITY_PLACEHOLDER_VALUE)) {
            this.value = 0;
            return;
        }
        String trimmedPriority = priority.trim();
        try {
            this.value = Integer.parseInt(trimmedPriority);
        } catch (NumberFormatException e) {
            throw new IllegalValueException(MESSAGE_PRIORITY_CONSTRAINTS);
        }
        if (!isValidPriority(trimmedPriority)) {
            throw new IllegalValueException(MESSAGE_PRIORITY_CONSTRAINTS);
        }
    }

    /**
     * Returns true if a given string is a valid task priority.
     */
    public static boolean isValidPriority(String test) {
        if (test.equals(PRIORITY_PLACEHOLDER_VALUE)) {
            return true;
        } else if (!test.matches(PRIORITY_VALIDATION_REGEX)) {
            return false;
        } else {
            int intTest = Integer.parseInt(test);
            return isWithinBounds(intTest);
        }
    }

    /**
     * Returns true if the value is within the upper and lower bounds of priority
     */
    public static boolean isWithinBounds(int test) {
        return test <= PRIORITY_UPPER_BOUND && test >= PRIORITY_LOWER_BOUND;
    }

    @Override
    public String toString() {
        return PRIORITY_TEXT_STRINGS[value];
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Priority // instanceof handles nulls
                && this.value == ((Priority) other).value); // state check
    }
}
```

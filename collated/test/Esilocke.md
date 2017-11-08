# Esilocke
###### /java/seedu/address/logic/parser/EditTagCommandParserTest.java
``` java
public class EditTagCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditTagCommand.MESSAGE_USAGE);


    private EditTagCommandParser parser = new EditTagCommandParser();
    @Test
    public void invalidInputTest() {
        // empty argument
        assertParseFailure(parser, " ", MESSAGE_INVALID_FORMAT);
        // too little args
        assertParseFailure(parser, VALID_TAG_FRIEND, MESSAGE_INSUFFICIENT_ARGS);
        // too many args
        assertParseFailure(parser, VALID_TAG_FRIEND + " " + VALID_TAG_FRIEND
                + " " + VALID_TAG_FRIEND, MESSAGE_INSUFFICIENT_ARGS);
        // args are the same
        assertParseFailure(parser, VALID_TAG_FRIEND + " " + VALID_TAG_FRIEND, MESSAGE_DUPLICATE_TAGS);
        // args are invalid
        assertParseFailure(parser, INVALID_TAG_DESC + " " + INVALID_TAG_DESC, MESSAGE_INVALID_FORMAT);
    }
    @Test
    public void validInputTest() throws IllegalValueException {
        Tag friendTag = new Tag(VALID_TAG_FRIEND);
        Tag husbandTag = new Tag(VALID_TAG_HUSBAND);
        Tag friendTagUpper = new Tag (VALID_TAG_FRIEND.toUpperCase());
        // case changes
        assertParseSuccess(parser, VALID_TAG_FRIEND + " "
                + VALID_TAG_FRIEND.toUpperCase(), new EditTagCommand(friendTag, friendTagUpper));
        // two distinct words
        assertParseSuccess(parser, VALID_TAG_FRIEND + " "
                + VALID_TAG_HUSBAND, new EditTagCommand(friendTag, husbandTag));

    }
}
```
###### /java/seedu/address/logic/parser/AddressBookParserTest.java
``` java
    @Test
    public void parseCommandAssign() throws Exception {
        ArrayList<Index> personIndexes = new ArrayList<>(Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));
        AssignCommand command = (AssignCommand) parser.parseCommand(AssignCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + INDEX_SECOND_PERSON.getOneBased() + " "
                + PREFIX_TARGET + INDEX_FIRST_TASK.getOneBased());
        assertEquals(new AssignCommand(personIndexes, INDEX_FIRST_TASK), command);
    }

    @Test
    public void parseCommandAliasAssign() throws Exception {
        ArrayList<Index> personIndexes = new ArrayList<>(Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));
        AssignCommand command = (AssignCommand) parser.parseCommand(AssignCommand.COMMAND_ALIAS + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + INDEX_SECOND_PERSON.getOneBased() + " "
                + PREFIX_TARGET + INDEX_FIRST_TASK.getOneBased());
        assertEquals(new AssignCommand(personIndexes, INDEX_FIRST_TASK), command);
    }

```
###### /java/seedu/address/logic/parser/AddressBookParserTest.java
``` java
    @Test
    public void parseCommandDismiss() throws Exception {
        ArrayList<Index> personIndexes = new ArrayList<>(Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));
        DismissCommand command = (DismissCommand) parser.parseCommand(DismissCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + INDEX_SECOND_PERSON.getOneBased() + " "
                + PREFIX_FROM + INDEX_FIRST_TASK.getOneBased());
        assertEquals(new DismissCommand(personIndexes, INDEX_FIRST_TASK), command);
    }

    @Test
    public void parseCommandAliasDismiss() throws Exception {
        ArrayList<Index> personIndexes = new ArrayList<>(Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));
        DismissCommand command = (DismissCommand) parser.parseCommand(DismissCommand.COMMAND_ALIAS + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + INDEX_SECOND_PERSON.getOneBased() + " "
                + PREFIX_FROM + INDEX_FIRST_TASK.getOneBased());
        assertEquals(new DismissCommand(personIndexes, INDEX_FIRST_TASK), command);
    }
```
###### /java/seedu/address/logic/parser/AddressBookParserTest.java
``` java
    @Test
    public void parseCommandEditTag() throws Exception {
        EditTagCommand command = (EditTagCommand) parser.parseCommand(EditTagCommand.COMMAND_WORD + " "
                + " friends enemies");
        Tag friends = new Tag("friends");
        Tag enemies = new Tag("enemies");
        assertEquals(new EditTagCommand(friends, enemies), command);
    }

    @Test
    public void parseCommandAliasEditTag() throws Exception {
        EditTagCommand command = (EditTagCommand) parser.parseCommand(EditTagCommand.COMMAND_ALIAS + " "
                + " friends enemies");
        Tag friends = new Tag("friends");
        Tag enemies = new Tag("enemies");
        assertEquals(new EditTagCommand(friends, enemies), command);
    }
```
###### /java/seedu/address/logic/parser/AddressBookParserTest.java
``` java
    @Test
    public void  parseCommandSetComplete() throws Exception {
        SetCompleteCommand command = (SetCompleteCommand) parser.parseCommand(SetCompleteCommand.COMMAND_WORD
                + " " + INDEX_FIRST_TASK.getOneBased());
        assertEquals(new SetCompleteCommand(INDEX_FIRST_TASK), command);
    }

    @Test
    public void  parseCommandAliasSetComplete() throws Exception {
        SetCompleteCommand command = (SetCompleteCommand) parser.parseCommand(SetCompleteCommand.COMMAND_ALIAS
                + " " + INDEX_FIRST_TASK.getOneBased());
        assertEquals(new SetCompleteCommand(INDEX_FIRST_TASK), command);
    }

    @Test
    public void  parseCommandSetIncomplete() throws Exception {
        SetIncompleteCommand command = (SetIncompleteCommand) parser.parseCommand(SetIncompleteCommand.COMMAND_ALIAS
                + " " + INDEX_FIRST_TASK.getOneBased());
        assertEquals(new SetIncompleteCommand(INDEX_FIRST_TASK), command);
    }

    @Test
    public void  parseCommandAliasSetIncomplete() throws Exception {
        SetIncompleteCommand command = (SetIncompleteCommand) parser.parseCommand(SetIncompleteCommand.COMMAND_ALIAS
                + " " + INDEX_FIRST_TASK.getOneBased());
        assertEquals(new SetIncompleteCommand(INDEX_FIRST_TASK), command);
    }
```
###### /java/seedu/address/logic/parser/DeleteCommandParserTest.java
``` java
    @Test
    public void parseTaskValidArgs_returnsDeleteCommand() {
        assertParseSuccess(parser, TASK_SEPARATOR + "1",
                new DeleteCommand(INDEX_FIRST_PERSON, DELETE_TYPE_TASK));
    }
```
###### /java/seedu/address/logic/parser/DeleteCommandParserTest.java
``` java
    @Test
    public void parseTaskInvalidArgs_throwsParseException() {
        assertParseFailure(parser, TASK_SEPARATOR + "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }
```
###### /java/seedu/address/logic/parser/AddCommandParserTest.java
``` java
    @Test
    public void parseTasksAllFieldsPresent_success() {
        Task expectedTask = new TaskBuilder().withTaskName(VALID_TASK_NAME_PENCIL)
                .withDescription(VALID_DESCRIPTION_PENCIL).withDeadline(VALID_DEADLINE_PENCIL)
                .withPriority(VALID_PRIORITY_PENCIL).withTaskAddress(VALID_TASK_ADDRESS_PENCIL).build();

        // multiple names - last name accepted
        assertParseSuccess(parser, AddCommand.COMMAND_WORD + TASK_SEPARATOR + TASK_NAME_DESC_PAPER
                + TASK_NAME_DESC_PENCIL + DESCRIPTION_DESC_PENCIL + DEADLINE_DESC_PENCIL
                + PRIORITY_DESC_PENCIL + TASK_ADDRESS_DESC_PENCIL, new AddCommand(expectedTask));

        // multiple descriptions - last description accepted
        assertParseSuccess(parser, AddCommand.COMMAND_WORD + TASK_SEPARATOR + TASK_NAME_DESC_PENCIL
                + DESCRIPTION_DESC_PAPER + DESCRIPTION_DESC_PENCIL + DEADLINE_DESC_PENCIL
                + PRIORITY_DESC_PENCIL + TASK_ADDRESS_DESC_PENCIL, new AddCommand(expectedTask));

        // multiple deadlines - last deadline accepted
        assertParseSuccess(parser, AddCommand.COMMAND_WORD + TASK_SEPARATOR + TASK_NAME_DESC_PENCIL
                + DESCRIPTION_DESC_PENCIL + DEADLINE_DESC_PAPER + DEADLINE_DESC_PENCIL
                + PRIORITY_DESC_PENCIL + TASK_ADDRESS_DESC_PENCIL, new AddCommand(expectedTask));

        // multiple priorities - last priority accepted
        assertParseSuccess(parser, AddCommand.COMMAND_WORD + TASK_SEPARATOR + TASK_NAME_DESC_PENCIL
                + DESCRIPTION_DESC_PENCIL + DEADLINE_DESC_PENCIL + PRIORITY_DESC_PAPER
                + PRIORITY_DESC_PENCIL + TASK_ADDRESS_DESC_PENCIL, new AddCommand(expectedTask));

        // multiple addresses - last address accepted
        assertParseSuccess(parser, AddCommand.COMMAND_WORD + TASK_SEPARATOR + TASK_NAME_DESC_PENCIL
                + DESCRIPTION_DESC_PENCIL + DEADLINE_DESC_PENCIL + PRIORITY_DESC_PENCIL
                + TASK_ADDRESS_DESC_PAPER + TASK_ADDRESS_DESC_PENCIL, new AddCommand(expectedTask));
    }
```
###### /java/seedu/address/logic/commands/EditTaskDescriptorTest.java
``` java
public class EditTaskDescriptorTest {
    @Test
    public void equals() {
        // same values -> returns true
        EditTaskDescriptor descriptorWithSameValues = new EditTaskDescriptor(DESC_PENCIL);
        assertTrue(DESC_PENCIL.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_PENCIL.equals(DESC_PENCIL));

        // null -> returns false
        assertFalse(DESC_PENCIL.equals(null));

        // different types -> returns false
        assertFalse(DESC_PENCIL.equals(5));

        // different values -> returns false
        assertFalse(DESC_PENCIL.equals(DESC_PAPER));

        // different name -> returns false
        EditTaskDescriptor editedPencil = new EditTaskDescriptorBuilder(DESC_PENCIL)
                .withTaskName(VALID_TASK_NAME_PAPER).build();
        assertFalse(DESC_PENCIL.equals(editedPencil));

        // different description -> returns false
        editedPencil = new EditTaskDescriptorBuilder(DESC_PENCIL).withDescription(VALID_DESCRIPTION_PAPER).build();
        assertFalse(DESC_PENCIL.equals(editedPencil));

        // different deadline -> returns false
        editedPencil = new EditTaskDescriptorBuilder(DESC_PENCIL).withDeadline(VALID_DEADLINE_PAPER).build();
        assertFalse(DESC_PENCIL.equals(editedPencil));

        // different priority -> returns false
        editedPencil = new EditTaskDescriptorBuilder(DESC_PENCIL).withPriority(VALID_PRIORITY_PAPER).build();
        assertFalse(DESC_PENCIL.equals(editedPencil));
    }
}
```
###### /java/seedu/address/logic/commands/EditTagCommandTest.java
``` java
public class EditTagCommandTest {
    private Model model = new ModelManager(getTypicalPersonsAddressBook(), new UserPrefs());
    @Test
    public void noTagsPresent() throws IllegalValueException {
        Model taglessModel = new ModelManager(getTaglessAddressBook(), new UserPrefs());
        Model blankModel = new ModelManager(new AddressBook(), new UserPrefs());
        String absentTag = "notInAddressBook";
        EditTagCommand noPersonCommand = prepareCommand(VALID_TAG_FRIEND, absentTag, blankModel);
        EditTagCommand noTagsCommand = prepareCommand(VALID_TAG_FRIEND, absentTag, taglessModel);
        EditTagCommand absentTagCommand = prepareCommand(VALID_TAG_FRIEND, absentTag, model);

        // No people are in this address book
        assertCommandFailure(noPersonCommand, taglessModel, MESSAGE_TAG_NOT_FOUND);
        // All persons do not have tags
        assertCommandFailure(noTagsCommand, taglessModel, MESSAGE_TAG_NOT_FOUND);
        // No persons in address book has the required tag
        assertCommandFailure(absentTagCommand, model, MESSAGE_TAG_NOT_FOUND);
    }

    @Test
    public void editTagSubset() throws IllegalValueException, PersonNotFoundException {
        AddressBook testBook = prepareAddressBook();
        Model testModel = new ModelManager(testBook, new UserPrefs());
        EditTagCommand tagChangeColleagueToHusband = prepareCommand(VALID_TAG_COLLEAGUE, VALID_TAG_HUSBAND, testModel);
        String expectedMessage = String.format(MESSAGE_EDIT_TAG_SUCCESS, VALID_TAG_COLLEAGUE, VALID_TAG_HUSBAND);
        Tag husbandTag = new Tag(VALID_TAG_HUSBAND);
        Tag colleagueTag = new Tag(VALID_TAG_COLLEAGUE);

        // Attempt to change some Person objects
        Model expectedModel = new ModelManager(testModel.getAddressBook(), new UserPrefs());
        ArrayList<Index> indices = new ArrayList<>();
        indices.add(Index.fromZeroBased(0));
        indices.add(Index.fromZeroBased(2));
        expectedModel.editTag(colleagueTag, husbandTag, indices);
        assertCommandSuccess(tagChangeColleagueToHusband, testModel, expectedMessage, expectedModel);
    }
    @Test
    public void editTagAll() throws IllegalValueException, PersonNotFoundException {
        AddressBook testBook = prepareAddressBook();
        Model testModel = new ModelManager(testBook, new UserPrefs());
        EditTagCommand tagChangeFriendToHusband = prepareCommand(VALID_TAG_FRIEND, VALID_TAG_HUSBAND, testModel);
        String expectedMessage = String.format(MESSAGE_EDIT_TAG_SUCCESS, VALID_TAG_FRIEND, VALID_TAG_HUSBAND);
        Tag friendTag = new Tag(VALID_TAG_FRIEND);
        Tag husbandTag = new Tag(VALID_TAG_HUSBAND);

        // Attempt to change all Person objects
        Model expectedModel = new ModelManager(testModel.getAddressBook(), new UserPrefs());
        ArrayList<Index> indices = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            indices.add(Index.fromZeroBased(i));
        }
        expectedModel.editTag(friendTag, husbandTag, indices);
        assertCommandSuccess(tagChangeFriendToHusband, testModel, expectedMessage, expectedModel);
    }
    /** Returns a new EditTagCommand with the parameters */
    public EditTagCommand prepareCommand(String toChange, String newValue, Model model) throws IllegalValueException {
        Tag changedTag = new Tag(toChange);
        Tag newTag = new Tag(newValue);
        EditTagCommand editTagCommand = new EditTagCommand(changedTag, newTag);
        editTagCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return editTagCommand;
    }
    /** Returns a pre-made Address Book for testing purposes */
    public AddressBook prepareAddressBook() throws DuplicatePersonException {
        ReadOnlyPerson alice = new PersonBuilder().withName("Alice Pauline")
                .withTags(VALID_TAG_FRIEND, VALID_TAG_COLLEAGUE).build();
        ReadOnlyPerson bernice = new PersonBuilder().withName("Bernice Applecut")
                .withTags(VALID_TAG_FRIEND).build();
        ReadOnlyPerson clarice = new PersonBuilder().withName("Clarice Fenderbunt")
                .withTags(VALID_TAG_FRIEND, VALID_TAG_COLLEAGUE).build();
        ReadOnlyPerson denise = new PersonBuilder().withName("Denise Lieselocke")
                .withTags(VALID_TAG_FRIEND).build();
        ArrayList<ReadOnlyPerson> toAdd = new ArrayList<>(Arrays.asList(alice, bernice, clarice, denise));
        AddressBook preparedBook = new AddressBook();
        for (ReadOnlyPerson r : toAdd) {
            preparedBook.addPerson(r);
        }
        return preparedBook;
    }
}
```
###### /java/seedu/address/logic/commands/SetIncompleteCommandTest.java
``` java
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_TASK;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_TASK;
import static seedu.address.testutil.TypicalTasks.getTypicalTasksOnlyAddressBook;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.task.ReadOnlyTask;

public class SetIncompleteCommandTest {
    private Model model = new ModelManager(getTypicalTasksOnlyAddressBook(), new UserPrefs());

    @Test
    public void execute_validTaskIndex_success() throws Exception {
        ReadOnlyTask taskToChange = model.getFilteredTaskList().get(INDEX_THIRD_TASK.getZeroBased());
        SetIncompleteCommand setIncompleteCommand = prepareCommand(INDEX_THIRD_TASK);

        String expectedMessage = String.format(setIncompleteCommand.MESSAGE_SUCCESS, taskToChange);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setAsComplete(taskToChange, false);

        assertCommandSuccess(setIncompleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidTaskIndex_failure() throws Exception {
        Index outOfRangeIndex = Index.fromZeroBased(model.getFilteredTaskList().size());
        SetIncompleteCommand setIncompleteCommand = prepareCommand(outOfRangeIndex);

        assertCommandFailure(setIncompleteCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_changeCompletedTask_failure() throws Exception {
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        SetIncompleteCommand setIncompleteCommand = new SetIncompleteCommand(INDEX_FIRST_TASK);
        setIncompleteCommand.setData(expectedModel, new CommandHistory(), new UndoRedoStack());

        assertCommandFailure(setIncompleteCommand, expectedModel, setIncompleteCommand.MESSAGE_TASK_ALREADY_COMPLETE);
    }

    @Test
    public void equals() {
        SetIncompleteCommand setFirstCommand = new SetIncompleteCommand(INDEX_FIRST_TASK);
        SetIncompleteCommand setSecondCommand = new SetIncompleteCommand(INDEX_SECOND_TASK);

        // same object -> returns true
        assertTrue(setFirstCommand.equals(setFirstCommand));

        // same values -> returns true
        SetIncompleteCommand setFirstCommandCopy = new SetIncompleteCommand(INDEX_FIRST_TASK);
        assertTrue(setFirstCommand.equals(setFirstCommandCopy));

        // different types -> returns false
        assertFalse(setFirstCommand.equals(1));

        // null -> returns false
        assertFalse(setFirstCommand.equals(null));

        // different task -> returns false
        assertFalse(setFirstCommand.equals(setSecondCommand));
    }

    /**
     * Returns a {@code SetIncompleteCommand} with the parameter {@code index}.
     */
    public SetIncompleteCommand prepareCommand(Index taskIndex) {
        SetIncompleteCommand command = new SetIncompleteCommand(taskIndex);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### /java/seedu/address/logic/commands/AssignCommandTest.java
``` java
public class AssignCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_assignOnePerson_addSuccessful() throws Exception {
        List<Index> toAssign = Arrays.asList(INDEX_FIRST_PERSON);
        ReadOnlyTask assignedTask = model.getFilteredTaskList().get(0);
        ReadOnlyPerson assignedPerson = model.getFilteredPersonList().get(0);
        ArrayList<ReadOnlyPerson> persons = new ArrayList<>();
        persons.add(assignedPerson);

        AssignCommand assignCommand = prepareCommand(toAssign, INDEX_FIRST_TASK);
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.assignToTask(persons, assignedTask);
        String expectedMessage = String.format(AssignCommand.MESSAGE_SUCCESS, toAssign.size(),
                assignedTask);
        assertCommandSuccess(assignCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_assignManyPersons_addSuccessful() throws Exception {
        List<Index> toAssign = Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON, INDEX_THIRD_PERSON);
        ReadOnlyTask assignedTask = model.getFilteredTaskList().get(0);
        ReadOnlyPerson firstPerson = model.getFilteredPersonList().get(0);
        ReadOnlyPerson secondPerson = model.getFilteredPersonList().get(1);
        ReadOnlyPerson thirdPerson = model.getFilteredPersonList().get(2);
        ArrayList<ReadOnlyPerson> persons = new ArrayList<>();
        persons.add(firstPerson);
        persons.add(secondPerson);
        persons.add(thirdPerson);

        AssignCommand assignCommand = prepareCommand(toAssign, INDEX_FIRST_TASK);
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.assignToTask(persons, assignedTask);
        String expectedMessage = String.format(AssignCommand.MESSAGE_SUCCESS, toAssign.size(),
                assignedTask);
        assertCommandSuccess(assignCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_assignDuplicates_addSuccessful() throws Exception {
        List<Index> toAssign = Arrays.asList(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, INDEX_FIRST_PERSON);
        ReadOnlyTask assignedTask = model.getFilteredTaskList().get(0);
        ReadOnlyPerson assignedPerson = model.getFilteredPersonList().get(0);
        ArrayList<ReadOnlyPerson> persons = new ArrayList<>();
        persons.add(assignedPerson);

        AssignCommand assignCommand = prepareCommand(toAssign, INDEX_FIRST_TASK);
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        String expectedMessage = String.format(AssignCommand.MESSAGE_SUCCESS, 1, assignedTask);
        expectedModel.assignToTask(persons, assignedTask);
        assertCommandSuccess(assignCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() throws Exception {
        Index outOfRangeIndex = Index.fromZeroBased(model.getFilteredPersonList().size());
        List<Index> toAssign = Arrays.asList(INDEX_FIRST_PERSON, outOfRangeIndex, INDEX_SECOND_PERSON);

        AssignCommand assignCommand = prepareCommand(toAssign, INDEX_FIRST_TASK);
        assertCommandFailure(assignCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidTaskIndex_throwsCommandException() throws Exception {
        Index outOfRangeIndex = Index.fromZeroBased(model.getFilteredTaskList().size());
        List<Index> toAssign = Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);

        AssignCommand assignCommand = prepareCommand(toAssign, outOfRangeIndex);
        assertCommandFailure(assignCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        ArrayList<Index> assignFirstThree = new ArrayList<>(Arrays.asList(INDEX_FIRST_PERSON,
                INDEX_SECOND_PERSON, INDEX_THIRD_PERSON));
        ArrayList<Index> assignFirstTwo = new ArrayList<>(Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));
        AssignCommand assignTwoToFirst = new AssignCommand(assignFirstTwo, INDEX_FIRST_TASK);
        AssignCommand assignThreeToFirst = new AssignCommand(assignFirstThree, INDEX_FIRST_TASK);
        AssignCommand assignTwoToSecond = new AssignCommand(assignFirstTwo, INDEX_SECOND_TASK);

        // same object -> returns true
        assertTrue(assignTwoToFirst.equals(assignTwoToFirst));

        // same values -> returns true
        AssignCommand assignTwoToFirstCopy = new AssignCommand(assignFirstTwo, INDEX_FIRST_TASK);
        assertTrue(assignTwoToFirst.equals(assignTwoToFirstCopy));

        // different types -> returns false
        assertFalse(assignTwoToFirst.equals(1));

        // null -> returns false
        assertFalse(assignTwoToFirst.equals(null));

        // different person/task indexes -> returns false
        assertFalse(assignTwoToFirst.equals(assignThreeToFirst));
        assertFalse(assignTwoToFirst.equals(assignTwoToSecond));
    }
    /**
     * Generates a new AssignCommand with the specified targets.
     */
    private AssignCommand prepareCommand(List<Index> personsToAssign, Index task) {
        ArrayList<Index> listIndexes = new ArrayList<>(personsToAssign);
        AssignCommand command = new AssignCommand(listIndexes, task);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### /java/seedu/address/logic/commands/DismissCommandTest.java
``` java
public class DismissCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());


    @Test
    public void execute_assignOnePerson_addSuccessful() throws Exception {
        List<Index> toAssign = Arrays.asList(INDEX_FIRST_PERSON);
        ReadOnlyTask dismissedTask = model.getFilteredTaskList().get(0);
        ReadOnlyPerson dismissedPerson = model.getFilteredPersonList().get(0);
        ArrayList<ReadOnlyPerson> persons = new ArrayList<>();
        persons.add(dismissedPerson);

        DismissCommand assignCommand = prepareCommand(toAssign, INDEX_FIRST_TASK);
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.dismissFromTask(persons, dismissedTask);
        String expectedMessage = String.format(DismissCommand.MESSAGE_SUCCESS, toAssign.size(),
                dismissedTask);
        assertCommandSuccess(assignCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_assignManyPersons_addSuccessful() throws Exception {
        List<Index> toAssign = Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON, INDEX_THIRD_PERSON);
        ReadOnlyTask dismissedTask = model.getFilteredTaskList().get(0);
        ReadOnlyPerson firstPerson = model.getFilteredPersonList().get(0);
        ReadOnlyPerson secondPerson = model.getFilteredPersonList().get(1);
        ReadOnlyPerson thirdPerson = model.getFilteredPersonList().get(2);
        ArrayList<ReadOnlyPerson> persons = new ArrayList<>();
        persons.add(firstPerson);
        persons.add(secondPerson);
        persons.add(thirdPerson);

        DismissCommand assignCommand = prepareCommand(toAssign, INDEX_FIRST_TASK);
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.dismissFromTask(persons, dismissedTask);
        String expectedMessage = String.format(DismissCommand.MESSAGE_SUCCESS, toAssign.size(),
                dismissedTask);
        assertCommandSuccess(assignCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_assignDuplicates_addSuccessful() throws Exception {
        List<Index> toAssign = Arrays.asList(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, INDEX_FIRST_PERSON);
        ReadOnlyTask dismissedTask = model.getFilteredTaskList().get(0);
        ReadOnlyPerson dismissedPerson = model.getFilteredPersonList().get(0);
        ArrayList<ReadOnlyPerson> persons = new ArrayList<>();
        persons.add(dismissedPerson);

        DismissCommand assignCommand = prepareCommand(toAssign, INDEX_FIRST_TASK);
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        String expectedMessage = String.format(DismissCommand.MESSAGE_SUCCESS, 1, dismissedTask);
        expectedModel.dismissFromTask(persons, dismissedTask);
        assertCommandSuccess(assignCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() throws Exception {
        Index outOfRangeIndex = Index.fromZeroBased(model.getFilteredPersonList().size());
        List<Index> toDismiss = Arrays.asList(INDEX_FIRST_PERSON, outOfRangeIndex, INDEX_SECOND_PERSON);

        DismissCommand dismissCommand = prepareCommand(toDismiss, INDEX_FIRST_TASK);
        assertCommandFailure(dismissCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidTaskIndex_throwsCommandException() throws Exception {
        Index outOfRangeIndex = Index.fromZeroBased(model.getFilteredTaskList().size());
        List<Index> toDismiss = Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);

        DismissCommand dismissCommand = prepareCommand(toDismiss, outOfRangeIndex);
        assertCommandFailure(dismissCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        ArrayList<Index> dismissFirstThree = new ArrayList<>(Arrays.asList(INDEX_FIRST_PERSON,
                INDEX_SECOND_PERSON, INDEX_THIRD_PERSON));
        ArrayList<Index> dismissFirstTwo = new ArrayList<>(Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));
        DismissCommand dismissTwoToFirst = new DismissCommand(dismissFirstTwo, INDEX_FIRST_TASK);
        DismissCommand dismissThreeToFirst = new DismissCommand(dismissFirstThree, INDEX_FIRST_TASK);
        DismissCommand dismissTwoToSecond = new DismissCommand(dismissFirstTwo, INDEX_SECOND_TASK);

        // same object -> returns true
        assertTrue(dismissTwoToFirst.equals(dismissTwoToFirst));

        // same values -> returns true
        DismissCommand dismissTwoToFirstCopy = new DismissCommand(dismissFirstTwo, INDEX_FIRST_TASK);
        assertTrue(dismissTwoToFirst.equals(dismissTwoToFirstCopy));

        // different types -> returns false
        assertFalse(dismissTwoToFirst.equals(1));

        // null -> returns false
        assertFalse(dismissTwoToFirst.equals(null));

        // different person/task indexes -> returns false
        assertFalse(dismissTwoToFirst.equals(dismissThreeToFirst));
        assertFalse(dismissTwoToFirst.equals(dismissTwoToSecond));
    }
    /**
     * Generates a new DismissCommand with the specified targets.
     */
    private DismissCommand prepareCommand(List<Index> personsToDismiss, Index task) {
        ArrayList<Index> listIndexes = new ArrayList<>(personsToDismiss);
        DismissCommand command = new DismissCommand(listIndexes, task);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### /java/seedu/address/logic/commands/SetCompleteCommandTest.java
``` java
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_TASK;
import static seedu.address.testutil.TypicalTasks.getTypicalTasksOnlyAddressBook;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.task.ReadOnlyTask;

public class SetCompleteCommandTest {
    private Model model = new ModelManager(getTypicalTasksOnlyAddressBook(), new UserPrefs());

    @Test
    public void execute_validTaskIndex_success() throws Exception {
        ReadOnlyTask taskToChange = model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased());
        SetCompleteCommand setCompleteCommand = prepareCommand(INDEX_FIRST_TASK);

        String expectedMessage = String.format(setCompleteCommand.MESSAGE_SUCCESS, taskToChange);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setAsComplete(taskToChange, true);

        assertCommandSuccess(setCompleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidTaskIndex_failure() throws Exception {
        Index outOfRangeIndex = Index.fromZeroBased(model.getFilteredTaskList().size());
        SetCompleteCommand setCompleteCommand = prepareCommand(outOfRangeIndex);

        assertCommandFailure(setCompleteCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void execute_changeCompletedTask_failure() throws Exception {
        ReadOnlyTask taskToChange = model.getFilteredTaskList().get(INDEX_FIRST_TASK.getZeroBased());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setAsComplete(taskToChange, true);
        SetCompleteCommand setCompleteCommand = new SetCompleteCommand(INDEX_FIRST_TASK);
        setCompleteCommand.setData(expectedModel, new CommandHistory(), new UndoRedoStack());

        assertCommandFailure(setCompleteCommand, expectedModel, setCompleteCommand.MESSAGE_TASK_ALREADY_COMPLETE);
    }

    @Test
    public void equals() {
        SetCompleteCommand setFirstCommand = new SetCompleteCommand(INDEX_FIRST_TASK);
        SetCompleteCommand setSecondCommand = new SetCompleteCommand(INDEX_SECOND_TASK);

        // same object -> returns true
        assertTrue(setFirstCommand.equals(setFirstCommand));

        // same values -> returns true
        SetCompleteCommand setFirstCommandCopy = new SetCompleteCommand(INDEX_FIRST_TASK);
        assertTrue(setFirstCommand.equals(setFirstCommandCopy));

        // different types -> returns false
        assertFalse(setFirstCommand.equals(1));

        // null -> returns false
        assertFalse(setFirstCommand.equals(null));

        // different task -> returns false
        assertFalse(setFirstCommand.equals(setSecondCommand));
    }

    /**
     * Returns a {@code SetCompleteCommand} with the parameter {@code index}.
     */
    public SetCompleteCommand prepareCommand(Index taskIndex) {
        SetCompleteCommand command = new SetCompleteCommand(taskIndex);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### /java/seedu/address/model/task/TaskContainsKeywordPredicateTest.java
``` java

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.address.testutil.TaskBuilder;

public class TaskContainsKeywordPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        TaskContainsKeywordPredicate firstPredicate = new TaskContainsKeywordPredicate(firstPredicateKeywordList);
        TaskContainsKeywordPredicate secondPredicate = new TaskContainsKeywordPredicate(secondPredicateKeywordList);
        TaskContainsKeywordPredicate thirdPredicate = new TaskContainsKeywordPredicate(firstPredicateKeywordList,
                true, false, false, 0);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        TaskContainsKeywordPredicate firstPredicateCopy = new TaskContainsKeywordPredicate(firstPredicateKeywordList,
                false, false, false, 0);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different keywords -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
        assertFalse(firstPredicate.equals(thirdPredicate));
    }

    @Test
    public void test_nameContainsKeywords_returnsTrue() {
        // One keyword
        TaskContainsKeywordPredicate predicate = new TaskContainsKeywordPredicate(Collections.singletonList("Pencil"));
        assertTrue(predicate.test(new TaskBuilder().withTaskName("Buy Pencil").build()));

        // Multiple keywords
        predicate = new TaskContainsKeywordPredicate(Arrays.asList("Buy", "Pencil"));
        assertTrue(predicate.test(new TaskBuilder().withTaskName("Buy Pencil").build()));

        // Only one matching keyword
        predicate = new TaskContainsKeywordPredicate(Arrays.asList("Pencil", "Pen"));
        assertTrue(predicate.test(new TaskBuilder().withTaskName("Buy Pen").build()));

        // Mixed-case keywords
        predicate = new TaskContainsKeywordPredicate(Arrays.asList("buY", "pEnciL"));
        assertTrue(predicate.test(new TaskBuilder().withTaskName("Buy Pencil").build()));
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        TaskContainsKeywordPredicate predicate = new TaskContainsKeywordPredicate(Collections.emptyList());
        assertFalse(predicate.test(new TaskBuilder().withTaskName("Buy").build()));

        // Non-matching keyword
        predicate = new TaskContainsKeywordPredicate(Arrays.asList("Pen"));
        assertFalse(predicate.test(new TaskBuilder().withTaskName("Buy Pencil").build()));
    }

    @Test
    public void test_descriptionContainsKeywords_returnsTrue() {
        // One keyword
        TaskContainsKeywordPredicate predicate = new TaskContainsKeywordPredicate(Collections.singletonList("Pencil"));
        assertTrue(predicate.test(new TaskBuilder().withDescription("Buy 3 Pencil").build()));

        // Multiple keywords
        predicate = new TaskContainsKeywordPredicate(Arrays.asList("Buy", "Pencil"));
        assertTrue(predicate.test(new TaskBuilder().withDescription("Buy Pencil").build()));

        // Only one matching keyword
        predicate = new TaskContainsKeywordPredicate(Arrays.asList("Pencil", "Pen"));
        assertTrue(predicate.test(new TaskBuilder().withDescription("Buy Pen").build()));

        // Mixed-case keywords
        predicate = new TaskContainsKeywordPredicate(Arrays.asList("buY", "pEnciL"));
        assertTrue(predicate.test(new TaskBuilder().withDescription("Buy Pencil").build()));
    }

    @Test
    public void priorityMatches() {
        // Priority level equal to required level
        TaskContainsKeywordPredicate predicate = new TaskContainsKeywordPredicate(Collections.singletonList("Pencil"),
                false, true, false, 3);
        assertTrue(predicate.test(new TaskBuilder().withTaskName("Buy Pencil").withPriority("3").build()));

        // Priority level greater than required level
        predicate = new TaskContainsKeywordPredicate(Collections.singletonList("Pencil"),
                false, true, false, 3);
        assertTrue(predicate.test(new TaskBuilder().withTaskName("Buy Pencil").withPriority("4").build()));

        // Priority level less than required level
        predicate = new TaskContainsKeywordPredicate(Collections.singletonList("Pencil"),
                false, true, false, 3);
        assertFalse(predicate.test(new TaskBuilder().withTaskName("Buy Pencil").withPriority("1").build()));

        // Name matches, priority check not required, even though priority level does not match
        predicate = new TaskContainsKeywordPredicate(Collections.singletonList("Pencil"),
                false, false, false, 3);
        assertTrue(predicate.test(new TaskBuilder().withTaskName("Buy Pencil").withPriority("1").build()));

        // Priority matches, but name does not match
        predicate = new TaskContainsKeywordPredicate(Collections.singletonList("Something"),
                false, true, false, 3);
        assertFalse(predicate.test(new TaskBuilder().withTaskName("Buy Pencil").withPriority("3").build()));
    }

    @Test
    public void stateMatches() {
        // States are equivalent
        TaskContainsKeywordPredicate predicate = new TaskContainsKeywordPredicate(Collections.singletonList("Pencil"),
                true, false, true, 0);
        assertTrue(predicate.test(new TaskBuilder().withTaskName("Buy Pencil").withState(true).build()));

        // States are different
        predicate = new TaskContainsKeywordPredicate(Collections.singletonList("Pencil"),
                true, false, false, 0);
        assertFalse(predicate.test(new TaskBuilder().withTaskName("Buy Pencil").withState(true).build()));

        // Name matches, state check not required, even though state does not match
        predicate = new TaskContainsKeywordPredicate(Collections.singletonList("Pencil"),
                false, false, false, 0);
        assertTrue(predicate.test(new TaskBuilder().withTaskName("Buy Pencil").withState(true).build()));

        // State matches, but name does not match
        predicate = new TaskContainsKeywordPredicate(Collections.singletonList("Something"),
                true, false, true, 0);
        assertFalse(predicate.test(new TaskBuilder().withTaskName("Buy Pencil").withState(true).build()));
    }


    @Test
    public void combinationTests() {
        // At most 1 invalid input per test case

        // Matches all
        TaskContainsKeywordPredicate predicate = new TaskContainsKeywordPredicate(Collections.singletonList("Pencil"),
                true, true, true, 3);
        assertTrue(predicate.test(new TaskBuilder().withTaskName("Buy Pencil").withDescription("Get 3 Pencil now")
                .withPriority("4").withState(true).build()));

        // Name does not match, but description does
        predicate = new TaskContainsKeywordPredicate(Collections.singletonList("Pencil"),
                true, true, true, 3);
        assertTrue(predicate.test(new TaskBuilder().withTaskName("Get something").withDescription("Get 3 Pencil now")
                .withPriority("4").withState(true).build()));

        // Description does not match, but name does
        predicate = new TaskContainsKeywordPredicate(Collections.singletonList("Pencil"),
                true, true, true, 3);
        assertTrue(predicate.test(new TaskBuilder().withTaskName("Buy 3 Pencil").withDescription("Get something")
                .withPriority("4").withState(true).build()));

        // Name or description matches, but priority does not
        predicate = new TaskContainsKeywordPredicate(Collections.singletonList("Pencil"),
                true, true, true, 3);
        assertFalse(predicate.test(new TaskBuilder().withTaskName("Buy Pencil").withDescription("Get 3 Pencils now")
                .withPriority("2").withState(true).build()));

        // Name or description matches, but state does not
        predicate = new TaskContainsKeywordPredicate(Collections.singletonList("Pencil"),
                true, true, true, 3);
        assertFalse(predicate.test(new TaskBuilder().withTaskName("Buy Pencil").withDescription("Get 3 Pencils now")
                .withPriority("4").withState(false).build()));
    }
}
```
###### /java/seedu/address/testutil/EditTaskDescriptorBuilder.java
``` java
/**
 * A utility class to help with building EditTaskDescriptor objects.
 */
public class EditTaskDescriptorBuilder {

    private EditTaskDescriptor descriptor;

    public EditTaskDescriptorBuilder() {
        descriptor = new EditTaskDescriptor();
    }

    public EditTaskDescriptorBuilder(EditTaskDescriptor descriptor) {
        this.descriptor = new EditTaskDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditTaskDescriptor} with fields containing {@code task}'s details
     */
    public EditTaskDescriptorBuilder(ReadOnlyTask task) {
        descriptor = new EditTaskDescriptor();
        descriptor.setTaskName(task.getTaskName());
        descriptor.setDescription(task.getDescription());
        descriptor.setDeadline(task.getDeadline());
        descriptor.setPriority(task.getPriority());
        descriptor.setTaskAddress(task.getTaskAddress());
    }

    /**
     * Sets the {@code TaskName} of the {@code EditTaskDescriptor} that we are building.
     */
    public EditTaskDescriptorBuilder withTaskName(String name) {
        try {
            ParserUtil.parseTaskName(Optional.of(name)).ifPresent(descriptor::setTaskName);
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("name is expected to be unique.");
        }
        return this;
    }

    /**
     * Sets the {@code Description} of the {@code EditTaskDescriptor} that we are building.
     */
    public EditTaskDescriptorBuilder withDescription(String description) {
        try {
            ParserUtil.parseDescription(Optional.of(description)).ifPresent(descriptor::setDescription);
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("description is expected to be unique.");
        }
        return this;
    }

    /**
     * Sets the {@code Deadline} of the {@code EditTaskDescriptor} that we are building.
     */
    public EditTaskDescriptorBuilder withDeadline(String deadline) {
        try {
            ParserUtil.parseDeadline(Optional.of(deadline)).ifPresent(descriptor::setDeadline);
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("deadline is expected to be unique.");
        }
        return this;
    }

    /**
     * Sets the {@code Priority} of the {@code EditTaskDescriptor} that we are building.
     */
    public EditTaskDescriptorBuilder withPriority(String priority) {
        try {
            ParserUtil.parsePriority(Optional.of(priority)).ifPresent(descriptor::setPriority);
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("priority is expected to be unique.");
        }
        return this;
    }

    /**
     * Sets the {@code TaskAddress} of the {@code EditTaskDescriptor} that we are building.
     */
    public EditTaskDescriptorBuilder withTaskAddress(String address) {
        try {
            ParserUtil.parseTaskAddress(Optional.of(address)).ifPresent(descriptor::setTaskAddress);
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("address is expected to be unique.");
        }
        return this;
    }

    public EditTaskDescriptor build() {
        return descriptor;
    }
}
```
###### /java/seedu/address/testutil/TypicalTasks.java
``` java
/**
 * A utility class containing a list of {@code Task} objects to be used in tests.
 */
public class TypicalTasks {
    public static final ReadOnlyTask ACCEPT = new TaskBuilder().withTaskName("Acceptance Testing")
            .withDescription("Perform acceptance testing on application")
            .withDeadline("04-04-2017").withPriority("3")
            .withTaskAddress("21 Heng Mui Keng Terrace, #02-01-01 I-Cube Building").build();
    public static final ReadOnlyTask BUY = new TaskBuilder().withTaskName("Buy pencil")
            .withDescription("Buy pencils for tomorrow's test")
            .withDeadline("04-04-2017").withPriority("5").withTaskAddress("Tampines Mall").build();
    public static final ReadOnlyTask COOK = new TaskBuilder().withTaskName("Cook Paella")
            .withDescription("Cook Paella for 4 people tonight")
            .withDeadline("11-04-2016").withPriority("5").withState(true).withTaskAddress("27 Prince George's Park")
            .build();
    public static final ReadOnlyTask DATE = new TaskBuilder().withTaskName("Date with Lucy")
            .withDescription("Sunday, 10am at Central Park")
            .withDeadline("21-05-2015").withPriority("5").withTaskAddress("Central Park").build();
    public static final ReadOnlyTask ESCAPE = new TaskBuilder().withTaskName("Escape dungeon")
            .withDescription("Escape dungeon group formation")
            .withDeadline("04-04-2017").withPriority("1").withTaskAddress("16 Gemmill Ln").build();
    public static final ReadOnlyTask FREE = new TaskBuilder().withTaskName("Free memory space")
            .withDescription("Implement new version of free()")
            .withDeadline("21-08-2019").withPriority("2").withState(true)
            .withTaskAddress("NUS School of Computing, COM1, 13 Computing Drive, 117417").build();
    public static final ReadOnlyTask GRADLE = new TaskBuilder().withTaskName("Resolve gradle")
            .withDescription("Resolve gradle problems when building project")
            .withDeadline("06-06-2016").withPriority("5")
            .withTaskAddress("Changi Airport").build();

    // Manually added - Task's details found in {@code CommandTestUtil}
    public static final ReadOnlyTask PENCIL = new TaskBuilder().withTaskName(VALID_TASK_NAME_PENCIL)
            .withDescription(VALID_DESCRIPTION_PENCIL)
            .withDeadline(VALID_DEADLINE_PENCIL).withPriority(VALID_PRIORITY_PENCIL)
            .withTaskAddress(VALID_TASK_ADDRESS_PENCIL).build();
    public static final ReadOnlyTask PAPER = new TaskBuilder().withTaskName(VALID_TASK_NAME_PAPER)
            .withDescription(VALID_DESCRIPTION_PAPER)
            .withDeadline(VALID_DEADLINE_PAPER).withPriority(VALID_PRIORITY_PAPER)
            .withTaskAddress(VALID_TASK_ADDRESS_PAPER).build();

    private TypicalTasks() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalTasksOnlyAddressBook() {
        AddressBook ab = new AddressBook();
        for (ReadOnlyTask task : getTypicalTasks()) {
            try {
                ab.addTask(task);
            } catch (DuplicateTaskException e) {
                assert false : "not possible";
            }
        }
        return ab;
    }


    public static List<ReadOnlyTask> getTypicalTasks() {
        return new ArrayList<>(Arrays.asList(ACCEPT, BUY, COOK, DATE, ESCAPE, FREE, GRADLE));
    }
}
```

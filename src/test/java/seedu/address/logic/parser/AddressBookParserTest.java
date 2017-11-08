package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.DeleteCommand.DELETE_TYPE_PERSON;
import static seedu.address.logic.commands.DeleteCommand.DELETE_TYPE_TASK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FROM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAVIGATE_FROM_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAVIGATE_TO_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TARGET;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AssignCommand;
import seedu.address.logic.commands.BackupCommand;
import seedu.address.logic.commands.ChangePrivacyCommand;
import seedu.address.logic.commands.ChangePrivacyCommand.PersonPrivacySettings;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DismissCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.EditCommand.EditTaskDescriptor;
import seedu.address.logic.commands.EditTagCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindTagCommand;
import seedu.address.logic.commands.FontSizeCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.LocateCommand;
import seedu.address.logic.commands.NavigateCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.SetCompleteCommand;
import seedu.address.logic.commands.SetIncompleteCommand;
import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Location;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.NameContainsTagsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskContainsKeywordPredicate;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.EditTaskDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonPrivacySettingsBuilder;
import seedu.address.testutil.PersonUtil;
import seedu.address.testutil.TaskBuilder;
import seedu.address.testutil.TaskUtil;

public class AddressBookParserTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommandAdd() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);

        Task task = new TaskBuilder().build();
        command = (AddCommand) parser.parseCommand(TaskUtil.getAddCommand(task));
        assertEquals(new AddCommand(task), command);
    }

    //@@author
    @Test
    public void parseCommandAliasAdd() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(AddCommand.COMMAND_ALIAS + " "
                + PersonUtil.getPersonDetails(person));
        assertEquals(new AddCommand(person), command);

        Task task = new TaskBuilder().build();
        command = (AddCommand) parser.parseCommand(AddCommand.COMMAND_ALIAS + " "
                + PREFIX_TASK + " " + TaskUtil.getTaskDetails(task));
        assertEquals(new AddCommand(task), command);
    }

    //@@author Esilocke
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

    //@@author jeffreygohkw
    @Test
    public void parseCommandChangePrivacy() throws Exception {
        Person person = new PersonBuilder().build();
        PersonPrivacySettings pps = new PersonPrivacySettingsBuilder(person).build();

        ChangePrivacyCommand command = (ChangePrivacyCommand) parser.parseCommand(
                ChangePrivacyCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased()
                        + " " + PREFIX_NAME + String.valueOf(person.getName().isPrivate())
                        + " " + PREFIX_PHONE + String.valueOf(person.getPhone().isPrivate())
                        + " " + PREFIX_EMAIL + String.valueOf(person.getEmail().isPrivate())
                        + " " + PREFIX_ADDRESS + String.valueOf(person.getAddress().isPrivate()));
        ChangePrivacyCommand actualCommand = new ChangePrivacyCommand(INDEX_FIRST_PERSON, pps);

        assertTrue(changePrivacyCommandsEqual(command, actualCommand));
    }

    @Test
    public void parseCommandAliasChangePrivacy() throws Exception {
        Person person = new PersonBuilder().build();
        PersonPrivacySettings pps = new PersonPrivacySettingsBuilder(person).build();

        ChangePrivacyCommand command = (ChangePrivacyCommand) parser.parseCommand(
                ChangePrivacyCommand.COMMAND_ALIAS + " " + INDEX_FIRST_PERSON.getOneBased()
                        + " " + PREFIX_NAME + String.valueOf(person.getName().isPrivate())
                        + " " + PREFIX_PHONE + String.valueOf(person.getPhone().isPrivate())
                        + " " + PREFIX_EMAIL + String.valueOf(person.getEmail().isPrivate())
                        + " " + PREFIX_ADDRESS + String.valueOf(person.getAddress().isPrivate()));
        ChangePrivacyCommand actualCommand = new ChangePrivacyCommand(INDEX_FIRST_PERSON, pps);

        assertTrue(changePrivacyCommandsEqual(command, actualCommand));
    }

    //@@author
    @Test
    public void parseCommandClear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
    }

    @Test
    public void parseCommandAliasClear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_ALIAS) instanceof ClearCommand);
    }

    @Test
    public void parseCommandDelete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON, DELETE_TYPE_PERSON), command);

        command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + PREFIX_TASK + " " + INDEX_FIRST_TASK.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_TASK, DELETE_TYPE_TASK), command);
    }

    @Test
    public void parseCommandAliasDelete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_ALIAS + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON, DELETE_TYPE_PERSON), command);

        command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_ALIAS + " " + PREFIX_TASK + " "  + INDEX_FIRST_TASK.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_TASK, DELETE_TYPE_TASK), command);
    }

    //@@author Esilocke
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
    //@@author

    @Test
    public void parseCommandEdit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getPersonDetails(person));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);

        Task task = new TaskBuilder().build();
        EditTaskDescriptor taskDescriptor = new EditTaskDescriptorBuilder(task).build();
        command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_TASK.getOneBased() + " " + PREFIX_TASK + " " + TaskUtil.getTaskDetails(task));
        assertEquals(new EditCommand(INDEX_FIRST_TASK, taskDescriptor), command);
    }

    @Test
    public void parseCommandAliasEdit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_ALIAS + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getPersonDetails(person));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);

        Task task = new TaskBuilder().build();
        EditTaskDescriptor taskDescriptor = new EditTaskDescriptorBuilder(task).build();
        command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_ALIAS + " "
                + INDEX_FIRST_TASK.getOneBased() + " " + PREFIX_TASK + " " + TaskUtil.getTaskDetails(task));
        assertEquals(new EditCommand(INDEX_FIRST_TASK, taskDescriptor), command);
    }

    //@@author Esilocke
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
    //@@author

    @Test
    public void parseCommandExit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommandFind() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);

        command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + PREFIX_TASK +  " "
                        + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new TaskContainsKeywordPredicate(keywords, false, false, false, 0)), command);
    }

    @Test
    public void parseCommandAliasFind() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_ALIAS + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);

        command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_ALIAS + " " + PREFIX_TASK +  " "
                        + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new TaskContainsKeywordPredicate(keywords, false, false, false, 0)), command);
    }
    //@@author wangyiming1019
    @Test
    public void parseCommandFindTag() throws Exception {
        List<String> keywords = Arrays.asList("friend", "colleague");
        FindTagCommand command = (FindTagCommand) parser.parseCommand(
                FindTagCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindTagCommand(
                new NameContainsTagsPredicate(keywords)), command);
    }

    @Test
    public void parseCommandAliasFindTag() throws Exception {
        List<String> keywords = Arrays.asList("friend", "colleague");
        FindTagCommand command = (FindTagCommand) parser.parseCommand(
                FindTagCommand.COMMAND_ALIAS + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindTagCommand(
                new NameContainsTagsPredicate(keywords)), command);
    }
    //@@author
    @Test
    public void parseCommandHelp() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommandHistory() throws Exception {
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD) instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD + " 3") instanceof HistoryCommand);

        try {
            parser.parseCommand("histories");
            fail("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(MESSAGE_UNKNOWN_COMMAND, pe.getMessage());
        }
    }

    @Test
    public void parseCommandAliasHistory() throws Exception {
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_ALIAS) instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_ALIAS + " 3") instanceof HistoryCommand);

        try {
            parser.parseCommand("histories");
            fail("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(MESSAGE_UNKNOWN_COMMAND, pe.getMessage());
        }
    }

    @Test
    public void parseCommandList() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommandAliasList() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_ALIAS) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_ALIAS + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommandSelect() throws Exception {
        SelectCommand command = (SelectCommand) parser.parseCommand(
                SelectCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new SelectCommand(INDEX_FIRST_PERSON, false), command);
    }

    @Test
    public void parseCommandAliasSelect() throws Exception {
        SelectCommand command = (SelectCommand) parser.parseCommand(
                SelectCommand.COMMAND_ALIAS + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new SelectCommand(INDEX_FIRST_PERSON, false), command);
    }

    //@@author jeffreygohkw
    @Test
    public void parseCommandLocate() throws Exception {
        LocateCommand command = (LocateCommand) parser.parseCommand(
                LocateCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new LocateCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommandAliasLocate() throws Exception {
        LocateCommand command = (LocateCommand) parser.parseCommand(
                LocateCommand.COMMAND_ALIAS + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new LocateCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommandNavigate() throws Exception {
        NavigateCommand command = (NavigateCommand) parser.parseCommand(
                NavigateCommand.COMMAND_WORD + " " + PREFIX_NAVIGATE_FROM_ADDRESS + "NUS"
                        + " " + PREFIX_NAVIGATE_TO_ADDRESS + "Sentosa");
        Location from = new Location("NUS");
        Location to = new Location("Sentosa");
        assertEquals(new NavigateCommand(from, to, null, null, false, false),
                command);
    }

    @Test
    public void parseCommandAliasNavigate() throws Exception {
        NavigateCommand command = (NavigateCommand) parser.parseCommand(
                NavigateCommand.COMMAND_ALIAS + " " + PREFIX_NAVIGATE_FROM_ADDRESS + "NUS"
                        + " " + PREFIX_NAVIGATE_TO_ADDRESS + "Sentosa");
        Location from = new Location("NUS");
        Location to = new Location("Sentosa");
        assertEquals(new NavigateCommand(from, to, null, null, false, false),
                command);
    }

    //@@author charlesgoh
    @Test
    public void parseSortCommandWord() throws Exception {
        assertTrue(parser.parseCommand(SortCommand.COMMAND_WORD + " "
                + SortCommand.ACCEPTED_LIST_PARAMETERS.get(0) + " " + SortCommand.ACCEPTED_FIELD_PARAMETERS.get(0)
                + " " + SortCommand.ACCEPTED_ORDER_PARAMETERS.get(0)) instanceof SortCommand);
    }

    @Test
    public void parseSortCommandAlias() throws Exception {
        assertTrue(parser.parseCommand(SortCommand.COMMAND_ALIAS + " "
                + SortCommand.ACCEPTED_LIST_PARAMETERS.get(0) + " " + SortCommand.ACCEPTED_FIELD_PARAMETERS.get(0)
                + " " + SortCommand.ACCEPTED_ORDER_PARAMETERS.get(0)) instanceof SortCommand);
    }

    @Test
    public void parseCommandBackup() throws Exception {
        assertTrue(parser.parseCommand(BackupCommand.COMMAND_WORD + " testbackupfilename") instanceof BackupCommand);
        assertTrue(parser.parseCommand(BackupCommand.COMMAND_ALIAS + " testbackupfilename") instanceof BackupCommand);
    }

    @Test
    public void parseCommandFontSizeWord() throws Exception {
        for (String arg: FontSizeCommand.ACCEPTED_PARAMETERS) {
            assertTrue(parser
                    .parseCommand(FontSizeCommand.COMMAND_WORD + " " + arg) instanceof FontSizeCommand);
        }

    }

    @Test
    public void parseCommandFontSizeAlias() throws Exception {
        for (String arg: FontSizeCommand.ACCEPTED_PARAMETERS) {
            assertTrue(parser
                    .parseCommand(FontSizeCommand.COMMAND_ALIAS + " " + arg) instanceof FontSizeCommand);
        }

    }

    //@@author
    @Test
    public void parseCommandRedoCommandWordReturnsRedoCommand() throws Exception {
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_WORD) instanceof RedoCommand);
        assertTrue(parser.parseCommand("redo 1") instanceof RedoCommand);
    }

    //@@author Esilocke
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
    //@@author

    @Test
    public void parseCommandUndoCommandWordReturnsUndoCommand() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD) instanceof UndoCommand);
        assertTrue(parser.parseCommand("undo 3") instanceof UndoCommand);
    }

    @Test
    public void parseCommandUnrecognisedInputThrowsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        parser.parseCommand("");
    }

    @Test
    public void parseCommandUnknownCommandThrowsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_UNKNOWN_COMMAND);
        parser.parseCommand("unknownCommand");
    }

    //@@author jeffreygohkw
    /**
     * Checks if 2 ChangePrivacyCommands are equal
     * @param command the expected command
     * @param actualCommand the actual command
     * @return true if all the data are equal
     */
    private boolean changePrivacyCommandsEqual(ChangePrivacyCommand command, ChangePrivacyCommand actualCommand) {
        assertEquals(command.getIndex(), actualCommand.getIndex());
        assertEquals(command.getPps().getAddressIsPrivate(), actualCommand.getPps().getAddressIsPrivate());
        assertEquals(command.getPps().getNameIsPrivate(), actualCommand.getPps().getNameIsPrivate());
        assertEquals(command.getPps().getEmailIsPrivate(), actualCommand.getPps().getEmailIsPrivate());
        assertEquals(command.getPps().getPhoneIsPrivate(), actualCommand.getPps().getPhoneIsPrivate());
        return true;
    }
}

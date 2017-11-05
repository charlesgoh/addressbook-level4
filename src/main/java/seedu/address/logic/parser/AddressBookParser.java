package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddTagCommand;
import seedu.address.logic.commands.AssignCommand;
import seedu.address.logic.commands.BackupCommand;
import seedu.address.logic.commands.ChangePrivacyCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteTagCommand;
import seedu.address.logic.commands.DismissCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditTagCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FavouriteCommand;
import seedu.address.logic.commands.FavouriteListCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindTagCommand;
import seedu.address.logic.commands.FontSizeCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.LocateCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.SetCompleteCommand;
import seedu.address.logic.commands.SetIncompleteCommand;
import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.commands.TagListCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.UnfavouriteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
        case AddCommand.COMMAND_ALIAS:
            return new AddCommandParser().parse(arguments);

        case AddTagCommand.COMMAND_WORD:
        case AddTagCommand.COMMAND_ALIAS:
            return new AddTagCommandParser().parse(arguments);

        case AssignCommand.COMMAND_WORD:
        case AssignCommand.COMMAND_ALIAS:
            return new AssignTaskCommandParser().parse(arguments);

        case DismissCommand.COMMAND_WORD:
        case DismissCommand.COMMAND_ALIAS:
            return new DismissTaskCommandParser().parse(arguments);

        case ChangePrivacyCommand.COMMAND_WORD:
        case ChangePrivacyCommand.COMMAND_ALIAS:
            return new ChangePrivacyCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
        case EditCommand.COMMAND_ALIAS:
            return new EditCommandParser().parse(arguments);

        case EditTagCommand.COMMAND_WORD:
        case EditTagCommand.COMMAND_ALIAS:
            return new EditTagCommandParser().parse(arguments);

        case SetCompleteCommand.COMMAND_WORD:
        case SetCompleteCommand.COMMAND_ALIAS:
            return new SetTaskCompleteCommandParser().parse(arguments);

        case SetIncompleteCommand.COMMAND_WORD:
        case SetIncompleteCommand.COMMAND_ALIAS:
            return new SetTaskIncompleteCommandParser().parse(arguments);
        case SelectCommand.COMMAND_WORD:
        case SelectCommand.COMMAND_ALIAS:
            return new SelectCommandParser().parse(arguments);

        case LocateCommand.COMMAND_WORD:
        case LocateCommand.COMMAND_ALIAS:
            return new LocateCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
        case DeleteCommand.COMMAND_ALIAS:
            return new DeleteCommandParser().parse(arguments);

        case DeleteTagCommand.COMMAND_WORD:
        case DeleteTagCommand.COMMAND_ALIAS:
            return new DeleteTagCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
        case ClearCommand.COMMAND_ALIAS:
            return new ClearCommandParser().parse(arguments);

        case FindCommand.COMMAND_WORD:
        case FindCommand.COMMAND_ALIAS:
            return new FindCommandParser().parse(arguments);

        case FindTagCommand.COMMAND_WORD:
        case FindTagCommand.COMMAND_ALIAS:
            return new FindTagCommandParser().parse(arguments);

        case FavouriteCommand.COMMAND_WORD:
        case FavouriteCommand.COMMAND_ALIAS:
            return new FavouriteCommandParser().parse(arguments);

        case FavouriteListCommand.COMMAND_WORD:
        case FavouriteListCommand.COMMAND_ALIAS:
            return new FavouriteListCommand();

        case UnfavouriteCommand.COMMAND_WORD:
        case UnfavouriteCommand.COMMAND_ALIAS:
            return new UnfavouriteCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
        case ListCommand.COMMAND_ALIAS:
            return new ListCommand();

        case TagListCommand.COMMAND_WORD:
        case TagListCommand.COMMAND_ALIAS:
            return new TagListCommand();

        case HistoryCommand.COMMAND_WORD:
        case HistoryCommand.COMMAND_ALIAS:
            return new HistoryCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case UndoCommand.COMMAND_WORD:
        case UndoCommand.COMMAND_ALIAS:
            return new UndoCommand();

        case RedoCommand.COMMAND_WORD:
        case RedoCommand.COMMAND_ALIAS:
            return new RedoCommand();
        //@@author charlesgoh
        case SortCommand.COMMAND_WORD:
        case SortCommand.COMMAND_ALIAS:
            return new SortCommandParser().parse(arguments);

        case BackupCommand.COMMAND_WORD:
        case BackupCommand.COMMAND_ALIAS:
            return new BackupCommandParser().parse(arguments);

        case FontSizeCommand.COMMAND_WORD:
        case FontSizeCommand.COMMAND_ALIAS:
            return new FontSizeCommandParser().parse(arguments);
        //author charlesgoh
        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}

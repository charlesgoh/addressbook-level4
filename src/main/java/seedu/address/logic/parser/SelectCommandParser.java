package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SelectCommand object
 */
public class SelectCommandParser implements Parser<SelectCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SelectCommand
     * and returns an SelectCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SelectCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TASK);
        if (argMultimap.getValue(PREFIX_TASK).isPresent()) {
            String trimmed = args.replaceFirst(PREFIX_TASK.getPrefix(), "").trim();
            Index index = extractIndex(trimmed);
            return new SelectCommand(index, true);
        } else {
            Index index = extractIndex(args);
            return new SelectCommand(index, false);
        }
    }

    /**
     * Extracts one index from the provided string and returns it
     * @throws ParseException if the string does not contain a valid index
     */
    public Index extractIndex(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return index;
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));
        }
    }
}

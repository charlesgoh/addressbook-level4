package seedu.address.logic.parser;

import seedu.address.logic.commands.BackupCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses arguments for backup command and returns a BackupCommand instance. Arguments should consist of only one
 * filepath.
 */
public class BackupCommandParser implements Parser<BackupCommand> {

    @Override
    public BackupCommand parse(String userInput) throws ParseException {
        String trimmedArgs = userInput.trim();
        return new BackupCommand(trimmedArgs);
    }
}

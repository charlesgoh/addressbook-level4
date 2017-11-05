package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Arrays;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ChangeFontSizeEvent;

//@@author charlesgoh
/**
 * Sorts all persons in address book by any field. Sorting can be done in ascending or descending order
 */
public class FontSizeCommand extends Command {
    public static final int MINIMUM_FONT_SIZE_MULTIPLIER = 0;
    public static final int MAXIMUM_FONT_SIZE_MULTIPLIER = 5;
    public static final String COMMAND_WORD = "fontsize";
    public static final String COMMAND_ALIAS = "fs";
    public static final String INCREASE_SIZE_PARAMETER = "increase";
    public static final String DECREASE_SIZE_PARAMETER = "decrease";
    public static final String RESET_SIZE_PARAMETER = "reset";

    public static final ArrayList<String> ACCEPTED_PARAMETERS = new ArrayList<>(Arrays.asList(
            INCREASE_SIZE_PARAMETER, DECREASE_SIZE_PARAMETER, RESET_SIZE_PARAMETER));

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Increases, decreases or resets font sizes \n"
                + "Parameters: KEYWORD [OPTION]\n"
                + "Example: " + COMMAND_WORD + " increase\n"
                + "Example 2: " + COMMAND_ALIAS + " reset\n";

    public static final String MESSAGE_INVALID_INPUT = "Invalid Input.\n" + MESSAGE_USAGE;

    public static final String MESSAGE_SUCCESS_FONT = "Font size increased successfully";

    private final String option;

    public FontSizeCommand(String option) {
        this.option = option;
    }

    public String getOption() {
        return this.option;
    }

    @Override
    public CommandResult execute() {
        requireNonNull(model);

        switch (option) {
        case INCREASE_SIZE_PARAMETER:
            EventsCenter.getInstance().post(new ChangeFontSizeEvent(ChangeFontSizeEvent.getIncreaseSizeEventIndex()));
            break;
        case DECREASE_SIZE_PARAMETER:
            EventsCenter.getInstance().post(new ChangeFontSizeEvent(ChangeFontSizeEvent.getDecreaseSizeEventIndex()));
            break;
        case RESET_SIZE_PARAMETER:
            EventsCenter.getInstance().post(new ChangeFontSizeEvent(ChangeFontSizeEvent.getResetSizeEventIndex()));
            break;
        default:
            System.err.println("Parameter is invalid");
            break;
        }

        return new CommandResult(MESSAGE_SUCCESS_FONT);
    }

}

package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS_PRIVATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AVATAR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AVATAR_PRIVATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL_PRIVATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME_PRIVATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE_PRIVATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK_PRIVATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG_PRIVATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK;

import java.util.Set;
import java.util.stream.Stream;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Avatar;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.Remark;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Description;
import seedu.address.model.task.Priority;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskAddress;
import seedu.address.model.task.TaskName;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_TASK);
        if (arePrefixesPresent(argMultimap, PREFIX_TASK)) {
            ReadOnlyTask taskToAdd = constructTask(args);
            return new AddCommand(taskToAdd);
        }
        ReadOnlyPerson personToAdd = constructPerson(args);
        return new AddCommand(personToAdd);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    //@@author jeffreygohkw
    /**
     * Constructs a ReadOnlyPerson from the arguments provided.
     */
    private static ReadOnlyPerson constructPerson(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_REMARK,
                        PREFIX_AVATAR, PREFIX_TAG, PREFIX_NAME_PRIVATE, PREFIX_PHONE_PRIVATE, PREFIX_EMAIL_PRIVATE,
                        PREFIX_ADDRESS_PRIVATE, PREFIX_REMARK_PRIVATE, PREFIX_TAG_PRIVATE, PREFIX_AVATAR_PRIVATE);

        if (!(arePrefixesPresent(argMultimap, PREFIX_NAME)
                || (arePrefixesPresent(argMultimap, PREFIX_NAME_PRIVATE)))) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        try {
            Name name;
            Phone phone;
            Email email;
            Address address;
            Remark remark;
            Avatar avatar;

            if ((arePrefixesPresent(argMultimap, PREFIX_NAME))) {
                name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME)).get();
            } else {
                name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME_PRIVATE), true).get();
            }

            if ((arePrefixesPresent(argMultimap, PREFIX_PHONE))) {
                phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE)).get();
            } else if (arePrefixesPresent(argMultimap, PREFIX_PHONE_PRIVATE)) {
                phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE_PRIVATE), true).get();
            } else {
                phone = new Phone(null);
            }

            if ((arePrefixesPresent(argMultimap, PREFIX_EMAIL))) {
                email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL)).get();
            } else if (arePrefixesPresent(argMultimap, PREFIX_EMAIL_PRIVATE)) {
                email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL_PRIVATE), true).get();
            } else {
                email = new Email(null);
            }

            if ((arePrefixesPresent(argMultimap, PREFIX_ADDRESS))) {
                address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS)).get();
            } else if (arePrefixesPresent(argMultimap, PREFIX_ADDRESS_PRIVATE)) {
                address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS_PRIVATE), true).get();
            } else {
                address = new Address(null);
            }

            if ((arePrefixesPresent(argMultimap, PREFIX_AVATAR))) {
                avatar = ParserUtil.parseAvatar(argMultimap.getValue(PREFIX_AVATAR)).get();
            } else if (arePrefixesPresent(argMultimap, PREFIX_AVATAR_PRIVATE)) {
                avatar = ParserUtil.parseAvatar(argMultimap.getValue(PREFIX_AVATAR_PRIVATE), true).get();
            } else {
                avatar = new Avatar(null);
            }

            if ((arePrefixesPresent(argMultimap, PREFIX_REMARK))) {
                remark = ParserUtil.parseRemark(argMultimap.getValue(PREFIX_REMARK)).get();
            } else if (arePrefixesPresent(argMultimap, PREFIX_REMARK_PRIVATE)) {
                remark = ParserUtil.parseRemark(argMultimap.getValue(PREFIX_REMARK_PRIVATE), true).get();
            } else {
                remark = new Remark(null);
            }

            Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
            ReadOnlyPerson person = new Person(name, phone, email, address, false, remark, avatar, tagList);
            return person;
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }
    //@@author Esilocke
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

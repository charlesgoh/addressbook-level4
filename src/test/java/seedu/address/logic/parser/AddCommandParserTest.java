package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.DEADLINE_DESC_PAPER;
import static seedu.address.logic.commands.CommandTestUtil.DEADLINE_DESC_PENCIL;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_PAPER;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_PENCIL;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DEADLINE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PRIORITY_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PRIORITY_DESC_PAPER;
import static seedu.address.logic.commands.CommandTestUtil.PRIORITY_DESC_PENCIL;
import static seedu.address.logic.commands.CommandTestUtil.REMARK_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.REMARK_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.TASK_NAME_DESC_PAPER;
import static seedu.address.logic.commands.CommandTestUtil.TASK_NAME_DESC_PENCIL;
import static seedu.address.logic.commands.CommandTestUtil.TASK_SEPARATOR;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DEADLINE_PENCIL;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_PENCIL;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRIORITY_PENCIL;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMARK_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMARK_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TASK_NAME_PAPER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TASK_NAME_PENCIL;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Priority;
import seedu.address.model.task.Task;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TaskBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withRemark(VALID_REMARK_AMY)
                .withTags(VALID_TAG_FRIEND).build();

        // multiple names - last name accepted
        assertParseSuccess(parser, AddCommand.COMMAND_WORD + NAME_DESC_AMY + NAME_DESC_BOB + PHONE_DESC_BOB
                + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + REMARK_DESC_AMY
                + TAG_DESC_FRIEND, new AddCommand(expectedPerson));

        // multiple phones - last phone accepted
        assertParseSuccess(parser, AddCommand.COMMAND_WORD + NAME_DESC_BOB + PHONE_DESC_AMY + PHONE_DESC_BOB
                + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + REMARK_DESC_AMY
                + TAG_DESC_FRIEND, new AddCommand(expectedPerson));

        // multiple emails - last email accepted
        assertParseSuccess(parser, AddCommand.COMMAND_WORD + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_AMY
                + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + REMARK_DESC_AMY
                + TAG_DESC_FRIEND, new AddCommand(expectedPerson));

        // multiple addresses - last address accepted
        assertParseSuccess(parser, AddCommand.COMMAND_WORD + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_AMY + ADDRESS_DESC_BOB + REMARK_DESC_AMY
                + TAG_DESC_FRIEND, new AddCommand(expectedPerson));

        // multiple tags - all accepted
        Person expectedPersonMultipleTags = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withRemark(VALID_REMARK_BOB)
                .withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND).build();
        assertParseSuccess(parser, AddCommand.COMMAND_WORD + NAME_DESC_BOB + PHONE_DESC_BOB
                        + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + REMARK_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                new AddCommand(expectedPersonMultipleTags));
    }

    //@@author Esilocke
    @Test
    public void parseTasksAllFieldsPresent_success() {
        Task expectedTask = new TaskBuilder().withTaskName(VALID_TASK_NAME_PENCIL)
                .withDescription(VALID_DESCRIPTION_PENCIL).withDeadline(VALID_DEADLINE_PENCIL)
                .withPriority(VALID_PRIORITY_PENCIL).build();

        // multiple names - last name accepted
        assertParseSuccess(parser, AddCommand.COMMAND_WORD + TASK_SEPARATOR + TASK_NAME_DESC_PAPER
                + TASK_NAME_DESC_PENCIL + DESCRIPTION_DESC_PENCIL + DEADLINE_DESC_PENCIL
                + PRIORITY_DESC_PENCIL, new AddCommand(expectedTask));

        // multiple descriptions - last description accepted
        assertParseSuccess(parser, AddCommand.COMMAND_WORD + TASK_SEPARATOR + TASK_NAME_DESC_PENCIL
                + DESCRIPTION_DESC_PAPER + DESCRIPTION_DESC_PENCIL + DEADLINE_DESC_PENCIL
                + PRIORITY_DESC_PENCIL, new AddCommand(expectedTask));

        // multiple deadlines - last deadline accepted
        assertParseSuccess(parser, AddCommand.COMMAND_WORD + TASK_SEPARATOR + TASK_NAME_DESC_PENCIL
                + DESCRIPTION_DESC_PENCIL + DEADLINE_DESC_PAPER + DEADLINE_DESC_PENCIL
                + PRIORITY_DESC_PENCIL, new AddCommand(expectedTask));

        // multiple priorities - last priority accepted
        assertParseSuccess(parser, AddCommand.COMMAND_WORD + TASK_SEPARATOR + TASK_NAME_DESC_PENCIL
                + DESCRIPTION_DESC_PENCIL + DEADLINE_DESC_PENCIL + PRIORITY_DESC_PAPER
                + PRIORITY_DESC_PENCIL, new AddCommand(expectedTask));
    }
    //@@author

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Person expectedPerson = new PersonBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
                .withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY).withRemark(VALID_REMARK_AMY)
                .withTags().build();
        assertParseSuccess(parser, AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + REMARK_DESC_AMY, new AddCommand(expectedPerson));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, AddCommand.COMMAND_WORD + VALID_NAME_BOB + PHONE_DESC_BOB
                + EMAIL_DESC_BOB + ADDRESS_DESC_BOB, expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, AddCommand.COMMAND_WORD + VALID_NAME_BOB + VALID_PHONE_BOB
                + VALID_EMAIL_BOB + VALID_ADDRESS_BOB, expectedMessage);
    }

    @Test
    public void parseTasksCompulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_TASK_USAGE);

        // missing task name prefix
        assertParseFailure(parser, AddCommand.COMMAND_WORD + TASK_SEPARATOR + VALID_TASK_NAME_PAPER
                + DESCRIPTION_DESC_PENCIL + DEADLINE_DESC_PENCIL + PRIORITY_DESC_PENCIL, expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, AddCommand.COMMAND_WORD + TASK_SEPARATOR + VALID_TASK_NAME_PENCIL
                + VALID_DESCRIPTION_PENCIL + VALID_DEADLINE_PENCIL + VALID_PRIORITY_PENCIL, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, AddCommand.COMMAND_WORD + INVALID_NAME_DESC + PHONE_DESC_BOB
                + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + REMARK_DESC_BOB + TAG_DESC_HUSBAND
                + TAG_DESC_FRIEND, Name.MESSAGE_NAME_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, AddCommand.COMMAND_WORD + NAME_DESC_BOB + INVALID_PHONE_DESC
                + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + REMARK_DESC_BOB + TAG_DESC_HUSBAND
                + TAG_DESC_FRIEND, Phone.MESSAGE_PHONE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, AddCommand.COMMAND_WORD + NAME_DESC_BOB + PHONE_DESC_BOB
                + INVALID_EMAIL_DESC + ADDRESS_DESC_BOB + REMARK_DESC_AMY
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Email.MESSAGE_EMAIL_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, AddCommand.COMMAND_WORD + NAME_DESC_BOB + PHONE_DESC_BOB
                + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + REMARK_DESC_BOB
                + INVALID_TAG_DESC + VALID_TAG_FRIEND, Tag.MESSAGE_TAG_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, AddCommand.COMMAND_WORD + INVALID_NAME_DESC + INVALID_PHONE_DESC
                + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + REMARK_DESC_BOB, Name.MESSAGE_NAME_CONSTRAINTS);
    }

    @Test
    public void parseTaskInvalidValue_failure() {
        // invalid deadline
        assertParseFailure(parser, AddCommand.COMMAND_WORD + TASK_SEPARATOR + TASK_NAME_DESC_PENCIL
                + DESCRIPTION_DESC_PENCIL + INVALID_DEADLINE_DESC
                + PRIORITY_DESC_PENCIL, Deadline.MESSAGE_INVALID_DATE);

        // invalid priority
        assertParseFailure(parser, AddCommand.COMMAND_WORD + TASK_SEPARATOR + TASK_NAME_DESC_PENCIL
                + DESCRIPTION_DESC_PENCIL + DEADLINE_DESC_PENCIL
                + INVALID_PRIORITY_DESC, Priority.MESSAGE_PRIORITY_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, AddCommand.COMMAND_WORD + TASK_SEPARATOR + TASK_NAME_DESC_PENCIL
                + DESCRIPTION_DESC_PENCIL + INVALID_DEADLINE_DESC
                + INVALID_PRIORITY_DESC, Deadline.MESSAGE_INVALID_DATE);
    }
}

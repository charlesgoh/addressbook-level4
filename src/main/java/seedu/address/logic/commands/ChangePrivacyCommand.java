package seedu.address.logic.commands;

//@@author jeffreygohkw
import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AVATAR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Avatar;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.Remark;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;

/**
 * Changes the privacy setting of a person's details in the address book
 */
public class ChangePrivacyCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "changeprivacy";
    public static final String COMMAND_ALIAS = "cp";

    public static final String TRUE_WORD = "true";
    public static final String FALSE_WORD = "false";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Changes the privacy of the details of the person"
            + " identified by the index number used in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + TRUE_WORD + " OR " + FALSE_WORD + "]"
            + "[" + PREFIX_PHONE + TRUE_WORD + " OR " + FALSE_WORD + "]"
            + "[" + PREFIX_EMAIL + TRUE_WORD + " OR " + FALSE_WORD + "]"
            + "[" + PREFIX_ADDRESS + TRUE_WORD + " OR " + FALSE_WORD + "]\n"
            + "[" + PREFIX_AVATAR + TRUE_WORD + " OR " + FALSE_WORD + "]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_NAME + TRUE_WORD + " "
            + PREFIX_PHONE + FALSE_WORD + " "
            + PREFIX_EMAIL + TRUE_WORD + " "
            + PREFIX_ADDRESS + FALSE_WORD + " "
            + PREFIX_AVATAR + FALSE_WORD;

    public static final String MESSAGE_CHANGE_PRIVACY_SUCCESS = "Changed the Privacy of the Person: %1$s";
    public static final String MESSAGE_NO_FIELDS = "At least one field to change must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final Index index;
    private final PersonPrivacySettings pps;

    /**
     * @param index of the person in the filtered person list to change the privacy of
     */
    public ChangePrivacyCommand(Index index, PersonPrivacySettings pps) {
        requireNonNull(index);
        requireNonNull(pps);

        this.index = index;
        this.pps = pps;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        ReadOnlyPerson personToChange = lastShownList.get(index.getZeroBased());

        Person newPerson = null;
        try {
            newPerson = createPersonWithChangedPrivacy(personToChange, pps);
        } catch (IllegalValueException e) {
            throw new AssertionError("Person must have all fields initialised.");
        }

        try {
            model.updatePerson(personToChange, newPerson);
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        } catch (DuplicatePersonException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target person cannot be missing");
        }

        return new CommandResult(String.format(MESSAGE_CHANGE_PRIVACY_SUCCESS, newPerson));
    }

    /**
     * Changes a person's fields' privacy
     * @param person the person whose privacy we would like to change
     * @param pps the settings of privacy for each field
     */
    private static Person createPersonWithChangedPrivacy(ReadOnlyPerson person, PersonPrivacySettings pps)
            throws IllegalValueException {
        assert person != null;

        Name name = createNameWithPrivacy(person, pps);
        Phone phone = createPhoneWithPrivacy(person, pps);
        Email email = createEmailWithPrivacy(person, pps);
        Address address = createAddressWithPrivacy(person, pps);
        Remark remark = createRemarkWithPrivacy(person, pps);
        Avatar avatar = createAvatarWithPrivacy(person, pps);
        Boolean favourite = person.getFavourite();
        Set<Tag> tag = person.getTags();

        return new Person(name, phone, email, address, favourite, remark, avatar, tag);
    }

    /**
     * Creates a new (@code Name) based on the input (@code Person) and (@code PersonPrivacySettings)
     * @return A (@code Name) with the same value as that of the (@code Person)'s but with the privacy set to that
     * of the (@code PersonPrivacySettings)
     */
    private static Name createNameWithPrivacy(ReadOnlyPerson person, PersonPrivacySettings pps) {
        Name n;
        try {
            if (person.getName().isPrivate()) {
                person.getName().setPrivate(false);
                n = new Name(person.getName().toString());
                person.getName().setPrivate(true);
            } else {
                n = new Name(person.getName().toString());
            }
        } catch (IllegalValueException e) {
            throw new AssertionError("Invalid Name");
        }
        if (pps.getNameIsPrivate() != null) {
            n.setPrivate(pps.getNameIsPrivate());
        }
        return n;
    }


    /**
     * Creates a new (@code Phone) based on the input (@code Person) and (@code PersonPrivacySettings)
     * @return A (@code Phone) with the same value as that of the (@code Person)'s but with the privacy set to that
     * of the (@code PersonPrivacySettings)
     */
    private static Phone createPhoneWithPrivacy(ReadOnlyPerson person, PersonPrivacySettings pps) {
        Phone p;
        try {
            if (person.getPhone().isPrivate()) {
                person.getPhone().setPrivate(false);
                p = new Phone(person.getPhone().toString());
                person.getPhone().setPrivate(true);
            } else {
                p = new Phone(person.getPhone().toString());
            }
        } catch (IllegalValueException e) {
            throw new AssertionError("Invalid Phone");
        }
        if (pps.getPhoneIsPrivate() != null) {
            p.setPrivate(pps.getPhoneIsPrivate());
        }
        return p;
    }


    /**
     * Creates a new (@code Email) based on the input (@code Person) and (@code PersonPrivacySettings)
     * @return A (@code Email) with the same value as that of the (@code Person)'s but with the privacy set to that
     * of the (@code PersonPrivacySettings)
     */
    private static Email createEmailWithPrivacy(ReadOnlyPerson person, PersonPrivacySettings pps) {
        Email em;
        try {
            if (person.getEmail().isPrivate()) {
                person.getEmail().setPrivate(false);
                em = new Email(person.getEmail().toString());
                person.getEmail().setPrivate(true);
            } else {
                em = new Email(person.getEmail().toString());
            }
        } catch (IllegalValueException e) {
            throw new AssertionError("Invalid Email");
        }
        if (pps.getEmailIsPrivate() != null) {
            em.setPrivate(pps.getEmailIsPrivate());
        }
        return em;
    }

    /**
     * Creates a new (@code Address) based on the input (@code Person) and (@code PersonPrivacySettings)
     * @return A (@code Address) with the same value as that of the (@code Person)'s but with the privacy set to that
     * of the (@code PersonPrivacySettings)
     */
    private static Address createAddressWithPrivacy(ReadOnlyPerson person, PersonPrivacySettings pps) {
        Address a;
        try {
            if (person.getAddress().isPrivate()) {
                person.getAddress().setPrivate(false);
                a = new Address(person.getAddress().toString());
                person.getAddress().setPrivate(true);
            } else {
                a = new Address(person.getAddress().toString());
            }
        } catch (IllegalValueException e) {
            throw new AssertionError("Invalid Address");
        }
        if (pps.getAddressIsPrivate() != null) {
            a.setPrivate(pps.getAddressIsPrivate());
        }
        return a;
    }

    /**
     * Creates a new (@code Remark) based on the input (@code Person) and (@code PersonPrivacySettings)
     * @return A (@code Remark) with the same value as that of the (@code Person)'s but with the privacy set to that
     * of the (@code PersonPrivacySettings)
     */
    private static Remark createRemarkWithPrivacy(ReadOnlyPerson person, PersonPrivacySettings pps) {
        Remark r;
        try {
            if (person.getRemark().isPrivate()) {
                person.getRemark().setPrivate(false);
                r = new Remark(person.getRemark().toString());
                person.getRemark().setPrivate(true);
            } else {
                r = new Remark(person.getRemark().toString());
            }
        } catch (IllegalValueException e) {
            throw new AssertionError("Invalid Remark");
        }
        if (pps.getRemarkIsPrivate() != null) {
            r.setPrivate(pps.getRemarkIsPrivate());
        }
        return r;
    }

    //@@author charlesgoh
    /**
     * Creates a new (@code Avatar) based on the input (@code Person) and (@code PersonPrivacySettings)
     * @return A (@code Avatar) with the same value as that of the (@code Person)'s but with the privacy set to that
     * of the (@code PersonPrivacySettings)
     */
    private static Avatar createAvatarWithPrivacy(ReadOnlyPerson person, PersonPrivacySettings pps) {
        Avatar v;
        try {
            if (person.getAvatar().isPrivate()) {
                person.getAvatar().setPrivate(false);
                v = new Avatar(person.getAvatar().toString());
                person.getAvatar().setPrivate(true);
            } else {
                v = new Avatar(person.getAvatar().toString());
            }
        } catch (IllegalValueException e) {
            throw new AssertionError("Invalid Avatar");
        }
        if (pps.getAvatarIsPrivate() != null) {
            v.setPrivate(pps.getAvatarIsPrivate());
        }
        return v;
    }
    //@@author

    public Index getIndex() {
        return index;
    }

    public PersonPrivacySettings getPps() {
        return pps;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ChangePrivacyCommand)) {
            return false;
        }

        // state check
        ChangePrivacyCommand c = (ChangePrivacyCommand) other;
        return index.equals(c.index)
                && pps.equals(c.pps);
    }

    /**
     * Stores the privacy settings for each field of a person.
     */
    public static class PersonPrivacySettings {
        private Boolean nameIsPrivate;
        private Boolean phoneIsPrivate;
        private Boolean emailIsPrivate;
        private Boolean addressIsPrivate;
        private Boolean remarkIsPrivate;
        private Boolean avatarIsPrivate;

        public PersonPrivacySettings() {}

        public PersonPrivacySettings(PersonPrivacySettings toCopy) {
            this.nameIsPrivate = toCopy.nameIsPrivate;
            this.phoneIsPrivate = toCopy.phoneIsPrivate;
            this.emailIsPrivate = toCopy.emailIsPrivate;
            this.addressIsPrivate = toCopy.addressIsPrivate;
            this.remarkIsPrivate = toCopy.remarkIsPrivate;
            this.avatarIsPrivate = toCopy.avatarIsPrivate;
        }

        /**
         * Returns true if at least one field is not null.
         */
        public boolean isAnyFieldNonNull() {
            return CollectionUtil.isAnyNonNull(this.nameIsPrivate, this.phoneIsPrivate,
                    this.emailIsPrivate, this.addressIsPrivate, this.remarkIsPrivate, this.avatarIsPrivate);
        }

        /**
         * Returns the value of nameIsPrivate
         * @return the value of nameIsPrivate
         */
        public Boolean getNameIsPrivate() {
            return nameIsPrivate;
        }

        public void setNameIsPrivate(boolean nameIsPrivate) {
            requireNonNull(nameIsPrivate);
            this.nameIsPrivate = nameIsPrivate;
        }

        /**
         * Returns the value of phoneIsPrivate
         * @return the value of phoneIsPrivate
         */
        public Boolean getPhoneIsPrivate() {
            return phoneIsPrivate;
        }

        public void setPhoneIsPrivate(boolean phoneIsPrivate) {
            requireNonNull(phoneIsPrivate);
            this.phoneIsPrivate = phoneIsPrivate;
        }

        /**
         * Returns the value of emailIsPrivate
         * @return the value of emailIsPrivate
         */
        public Boolean getEmailIsPrivate() {
            return emailIsPrivate;
        }

        public void setEmailIsPrivate(boolean emailIsPrivate) {
            requireNonNull(emailIsPrivate);
            this.emailIsPrivate = emailIsPrivate;
        }

        /**
         * Returns the value of addressIsPrivate
         * @return the value of addressIsPrivate
         */
        public Boolean getAddressIsPrivate() {
            return addressIsPrivate;
        }

        public void setAddressIsPrivate(boolean addressIsPrivate) {
            requireNonNull(addressIsPrivate);
            this.addressIsPrivate = addressIsPrivate;
        }
        //@@author charlesgoh
        /**
         * Returns the value of remarkIsPrivate
         * @return the value of remarkIsPrivate
         */
        public Boolean getRemarkIsPrivate() {
            return remarkIsPrivate;
        }

        public void setRemarkIsPrivate(boolean remarkIsPrivate) {
            requireNonNull(remarkIsPrivate);
            this.remarkIsPrivate = remarkIsPrivate;
        }

        /**
         * Returns the value of avatarIsPrivate
         * @return the value of avatarIsPrivate
         */
        public Boolean getAvatarIsPrivate() {
            return avatarIsPrivate;
        }

        public void setAvatarIsPrivate(boolean avatarIsPrivate) {
            requireNonNull(avatarIsPrivate);
            this.avatarIsPrivate = avatarIsPrivate;
        }
        //@@author

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof PersonPrivacySettings)) {
                return false;
            }

            // state check
            PersonPrivacySettings c = (PersonPrivacySettings) other;

            return getNameIsPrivate().equals(c.getNameIsPrivate())
                    && getPhoneIsPrivate().equals(c.getPhoneIsPrivate())
                    && getEmailIsPrivate().equals(c.getEmailIsPrivate())
                    && getAddressIsPrivate().equals(c.getAddressIsPrivate())
                    && getRemarkIsPrivate().equals(c.getRemarkIsPrivate())
                    && getAvatarIsPrivate().equals(c.getAvatarIsPrivate());
        }
    }
}

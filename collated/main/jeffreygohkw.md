# jeffreygohkw
###### /java/seedu/address/ui/BrowserPanel.java
``` java
    /**
     * Loads a google search for a person's address if their address is not private
     * Prints out a message on the result display otherwise
     * @param person The person's address we want to search for
     */
    private void loadMapsPage(ReadOnlyPerson person) {
        if (person.getAddress().isPrivate()) {
            raise(new NewResultAvailableEvent(PRIVATE_ADDRESS_CANNOT_SEARCH));
        } else {
            loadPage(GOOGLE_MAPS_URL_PREFIX + person.getAddress().toString().replaceAll(" ", "+")
                + GOOGLE_MAPS_URL_SUFFIX);
        }
    }

    /**
     * Loads Google Maps with directions on how to go from one location to another
     * @param fromLocation The location we want to start from
     * @param toLocation The location we want to reach
     */
    private void loadDirectionsPage(String fromLocation, String toLocation) {
        loadPage(GOOGLE_MAPS_DIRECTIONS_PREFIX + "&origin="
                + fromLocation.replaceAll("#(\\w+)\\s*", "").replaceAll(" ", "+")
                .replaceAll("-(\\w+)\\s*", "")
                + "&destination="
                + toLocation.replaceAll("#(\\w+)\\s*", "").replaceAll(" ", "+")
                .replaceAll("-(\\w+)\\s*", "")
                + GOOGLE_MAPS_DIRECTIONS_SUFFIX);
    }

```
###### /java/seedu/address/ui/BrowserPanel.java
``` java
    @Subscribe
    private void handleBrowserPanelLocateEvent(BrowserPanelLocateEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadMapsPage(event.getNewSelection());
    }

    @Subscribe
    private void handleBrowserPanelNavigateEvent(BrowserPanelNavigateEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadDirectionsPage(event.getFromLocation().toString(), event.getToLocation().toString());
    }
}
```
###### /java/seedu/address/ui/MainWindow.java
``` java
    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
        setAccelerator(openMenuItem, KeyCombination.valueOf("SHORTCUT+O"));
        setAccelerator(saveMenuItem, KeyCombination.valueOf("SHORTCUT+S"));
        setAccelerator(exitMenuItem, KeyCombination.valueOf("ALT+F4"));
        setAccelerator(increaseSizeMenuItem, KeyCombination.valueOf("SHORTCUT+W"));
        setAccelerator(decreaseSizeMenuItem, KeyCombination.valueOf("SHORTCUT+E"));
        setAccelerator(resetSizeMenuItem, KeyCombination.valueOf("SHORTCUT+R"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp the MainApp itself
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Is called by the main application to provide MainWindow with Storage
     *
     * @param s the Storage used by MainApp
     */
    public void setStorage(Storage s) {
        this.storage = s;
    }

    /**
     * Is called by the main application to  provide MainWindow with Model
     *
     * @param m the Model used by MainApp
     */
    public void setModel(Model m) {
        this.model = m;
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        browserPanel = new BrowserPanel();
        viewTaskPanel = new ViewTaskPanel();
        viewPersonPanel = new ViewPersonPanel();
        browserPlaceholder.getChildren().add(browserPanel.getRoot());

        personListPanel = new PersonListPanel(logic.getFilteredPersonList());
        personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());

        taskListPanel = new TaskListPanel(logic.getFilteredTaskList());
        taskListPanelPlaceholder.getChildren().add(taskListPanel.getRoot());

        ResultDisplay resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        String preferredFilePath = this.prefs.getAddressBookFilePath();
        StatusBarFooter statusBarFooter = new StatusBarFooter(preferredFilePath, logic.getFilteredPersonList().size());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(logic);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    void hide() {
        primaryStage.hide();
    }

    private void setTitle(String appTitle) {
        primaryStage.setTitle(appTitle);
    }

    /**
     * Sets the given image as the icon of the main window.
     * @param iconSource e.g. {@code "/images/help_icon.png"}
     */
    private void setIcon(String iconSource) {
        FxViewUtil.setStageIcon(primaryStage, iconSource);
    }

    /**
     * Sets the default size based on user preferences.
     */
    private void setWindowDefaultSize(UserPrefs prefs) {
        primaryStage.setHeight(prefs.getGuiSettings().getWindowHeight());
        primaryStage.setWidth(prefs.getGuiSettings().getWindowWidth());
        if (prefs.getGuiSettings().getWindowCoordinates() != null) {
            primaryStage.setX(prefs.getGuiSettings().getWindowCoordinates().getX());
            primaryStage.setY(prefs.getGuiSettings().getWindowCoordinates().getY());
        }
    }

    private void setWindowMinSize() {
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
    }

    /**
     * Returns the current size and the position of the main Window.
     */
    GuiSettings getCurrentGuiSetting() {
        return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
    }

```
###### /java/seedu/address/ui/MainWindow.java
``` java
    /**
     * Opens the data from a desired location
     */
    @FXML
    private void handleOpen() throws IOException, DataConversionException {
        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            // Change file path to the opened file
            storage.changeFilePath(file.getPath(), prefs);
            // Reset data in the model to the data from the opened file
            model.resetData(XmlFileStorage.loadDataFromSaveFile(file));
            // Update the UI
            fillInnerParts();
        }
        raise(new OpenRequestEvent());
    }

    /**
     * Saves the data at a desired location
     */
    @FXML
    private void handleSaveAs() throws IOException {
        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            // Change file path to the new save file
            storage.changeFilePath(file.getPath(), prefs);
            // Save the address book data and the user preferences
            storage.saveAddressBook(model.getAddressBook());
            storage.saveUserPrefs(prefs);
            // Update the UI
            fillInnerParts();
        }
        raise(new SaveAsRequestEvent());
    }

```
###### /java/seedu/address/commons/events/ui/BrowserPanelNavigateEvent.java
``` java

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.Location;

/**
 * Represents a selection change in the Person List Panel
 */
public class BrowserPanelNavigateEvent extends BaseEvent {

    private final Location fromLocation;
    private final Location toLocation;

    public BrowserPanelNavigateEvent(Location fromLocation, Location toLocation) {
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public Location getFromLocation() {
        return fromLocation;
    }

    public Location getToLocation() {
        return toLocation;
    }
}
```
###### /java/seedu/address/commons/events/ui/SaveAsRequestEvent.java
``` java
import seedu.address.commons.events.BaseEvent;

/**
 * An Event for the saving of data to a selected location.
 */
public class SaveAsRequestEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### /java/seedu/address/commons/events/ui/OpenRequestEvent.java
``` java
import seedu.address.commons.events.BaseEvent;

/**
 * An Event for the opening of a save file from a selected location.
 */
public class OpenRequestEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### /java/seedu/address/commons/events/ui/BrowserPanelLocateEvent.java
``` java
import seedu.address.commons.events.BaseEvent;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * Represents a selection change in the Person List Panel
 */
public class BrowserPanelLocateEvent extends BaseEvent {

    private final ReadOnlyPerson person;

    public BrowserPanelLocateEvent(ReadOnlyPerson person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public ReadOnlyPerson getNewSelection() {
        return person;
    }
}
```
###### /java/seedu/address/logic/parser/AddCommandParser.java
``` java
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
```
###### /java/seedu/address/logic/parser/ChangePrivacyCommandParser.java
``` java
import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.ChangePrivacyCommand;
import seedu.address.logic.commands.ChangePrivacyCommand.PersonPrivacySettings;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ChangePrivacyCommand object
 */
public class ChangePrivacyCommandParser implements Parser<ChangePrivacyCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ChangePrivacyCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                        PREFIX_REMARK);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());

        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ChangePrivacyCommand.MESSAGE_USAGE));
        }

        PersonPrivacySettings pps = new PersonPrivacySettings();

        checkName(argMultimap, pps);
        checkPhone(argMultimap, pps);
        checkEmail(argMultimap, pps);
        checkAddress(argMultimap, pps);
        checkRemark(argMultimap, pps);

        if (!pps.isAnyFieldNonNull()) {
            throw new ParseException(ChangePrivacyCommand.MESSAGE_NO_FIELDS);
        }

        return new ChangePrivacyCommand(index, pps);
    }

    /**
     * Checks the input under the name prefix and sets the PersonPrivacySettings depending on the input
     * @param argMultimap The input arguments of the Command
     * @param pps The PersonPrivacySettings to modify
     * @throws ParseException if the input is neither true nor false
     */
    private void checkName(ArgumentMultimap argMultimap, PersonPrivacySettings pps) throws ParseException {
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            if (argMultimap.getValue(PREFIX_NAME).toString().equals("Optional[true]")) {
                pps.setNameIsPrivate(true);

            } else if (argMultimap.getValue(PREFIX_NAME).toString().equals("Optional[false]")) {
                pps.setNameIsPrivate(false);
            } else {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        ChangePrivacyCommand.MESSAGE_USAGE));
            }
        }
    }

    /**
     * Checks the input under the phone prefix and sets the PersonPrivacySettings depending on the input
     * @param argMultimap The input arguments of the Command
     * @param pps The PersonPrivacySettings to modify
     * @throws ParseException if the input is neither true nor false
     */
    private void checkPhone(ArgumentMultimap argMultimap, PersonPrivacySettings pps) throws ParseException {
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            if (argMultimap.getValue(PREFIX_PHONE).toString().equals("Optional[true]")) {
                pps.setPhoneIsPrivate(true);
            } else if (argMultimap.getValue(PREFIX_PHONE).toString().equals("Optional[false]")) {
                pps.setPhoneIsPrivate(false);
            } else {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        ChangePrivacyCommand.MESSAGE_USAGE));
            }
        }
    }

    /**
     * Checks the input under the email prefix and sets the PersonPrivacySettings depending on the input
     * @param argMultimap The input arguments of the Command
     * @param pps The PersonPrivacySettings to modify
     * @throws ParseException if the input is neither true nor false
     */
    private void checkEmail(ArgumentMultimap argMultimap, PersonPrivacySettings pps) throws ParseException {
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            if (argMultimap.getValue(PREFIX_EMAIL).toString().equals("Optional[true]")) {
                pps.setEmailIsPrivate(true);
            } else if (argMultimap.getValue(PREFIX_EMAIL).toString().equals("Optional[false]")) {
                pps.setEmailIsPrivate(false);
            } else {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        ChangePrivacyCommand.MESSAGE_USAGE));
            }
        }
    }

    /**
     * Checks the input under the address prefix and sets the PersonPrivacySettings depending on the input
     * @param argMultimap The input arguments of the Command
     * @param pps The PersonPrivacySettings to modify
     * @throws ParseException if the input is neither true nor false
     */
    private void checkAddress(ArgumentMultimap argMultimap, PersonPrivacySettings pps) throws ParseException {
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            if (argMultimap.getValue(PREFIX_ADDRESS).toString().equals("Optional[true]")) {
                pps.setAddressIsPrivate(true);
            } else if (argMultimap.getValue(PREFIX_ADDRESS).toString().equals("Optional[false]")) {
                pps.setAddressIsPrivate(false);
            } else {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        ChangePrivacyCommand.MESSAGE_USAGE));
            }
        }
    }

    /**
     * Checks the input under the address prefix and sets the PersonPrivacySettings depending on the input
     * @param argMultimap The input arguments of the Command
     * @param pps The PersonPrivacySettings to modify
     * @throws ParseException if the input is neither true nor false
     */
    private void checkRemark(ArgumentMultimap argMultimap, PersonPrivacySettings pps) throws ParseException {
        if (argMultimap.getValue(PREFIX_REMARK).isPresent()) {
            if (argMultimap.getValue(PREFIX_REMARK).toString().equals("Optional[true]")) {
                pps.setRemarkIsPrivate(true);
            } else if (argMultimap.getValue(PREFIX_REMARK).toString().equals("Optional[false]")) {
                pps.setRemarkIsPrivate(false);
            } else {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        ChangePrivacyCommand.MESSAGE_USAGE));
            }
        }
    }
}
```
###### /java/seedu/address/logic/parser/LocateCommandParser.java
``` java
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.LocateCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SelectCommand object
 */
public class LocateCommandParser implements Parser<LocateCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SelectCommand
     * and returns an SelectCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public LocateCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new LocateCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, LocateCommand.MESSAGE_USAGE));
        }
    }
}
```
###### /java/seedu/address/logic/parser/ParserUtil.java
``` java
    /**
     * Parses a {@code Optional<String> name} into an {@code Optional<Name>} if {@code name} is present.
     * Takes in a (@code boolean isPrivate) which will set the Name to be private if true.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Name> parseName(Optional<String> name, boolean isPrivate) throws IllegalValueException {
        requireNonNull(name);
        return name.isPresent() ? Optional.of(new Name(name.get(), isPrivate)) : Optional.empty();
    }

```
###### /java/seedu/address/logic/parser/ParserUtil.java
``` java
    /**
     * Parses a {@code Optional<String> phone} into an {@code Optional<Phone>} if {@code phone} is present.
     * Takes in a (@code boolean isPrivate) which will set the Phone to be private if true.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Phone> parsePhone(Optional<String> phone, boolean isPrivate) throws IllegalValueException {
        requireNonNull(phone);
        return phone.isPresent() ? Optional.of(new Phone(phone.get(), isPrivate)) : Optional.empty();
    }

```
###### /java/seedu/address/logic/parser/ParserUtil.java
``` java
    /**
     * Parses a {@code Optional<String> address} into an {@code Optional<Address>} if {@code address} is present.
     * Takes in a (@code boolean isPrivate) which will set the Address to be private if true.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Address> parseAddress(Optional<String> address, boolean isPrivate)
            throws IllegalValueException {
        requireNonNull(address);
        return address.isPresent() ? Optional.of(new Address(address.get(), isPrivate)) : Optional.empty();
    }
    //author charlesgoh
    /**
     * Parses a {@code Optional<String> avatar} into an {@code Optional<Address>} if {@code avatar} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Avatar> parseAvatar(Optional<String> avatar) throws IllegalValueException {
        requireNonNull(avatar);
        return avatar.isPresent() ? Optional.of(new Avatar(avatar.get())) : Optional.empty();
    }

    /**
     * Parses a {@code Optional<String> address} into an {@code Optional<Address>} if {@code address} is present.
     * Takes in a (@code boolean isPrivate) which will set the Address to be private if true.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Avatar> parseAvatar(Optional<String> avatar, boolean isPrivate)
            throws IllegalValueException {
        requireNonNull(avatar);
        return avatar.isPresent() ? Optional.of(new Avatar(avatar.get(), isPrivate)) : Optional.empty();
    }

    /**
     * Parses a {@code Optional<String> remark} into an {@code Optional<Remark>} if {@code remark} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Remark> parseRemark(Optional<String> remark) throws IllegalValueException {
        requireNonNull(remark);
        return remark.isPresent() ? Optional.of(new Remark(remark.get())) : Optional.empty();
    }

```
###### /java/seedu/address/logic/parser/ParserUtil.java
``` java
    /**
     * Parses a {@code Optional<String> remark} into an {@code Optional<Remark>} if {@code remark} is present.
     * Takes in a (@code boolean isPrivate) which will set the Remark to be private if true.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Remark> parseRemark(Optional<String> remark, boolean isPrivate)
            throws IllegalValueException {
        requireNonNull(remark);
        return remark.isPresent() ? Optional.of(new Remark(remark.get(), isPrivate)) : Optional.empty();
    }
```
###### /java/seedu/address/logic/parser/ParserUtil.java
``` java
    /**
     * Parses a {@code Optional<String> email} into an {@code Optional<Email>} if {@code email} is present.
     * Takes in a (@code boolean isPrivate) which will set the Email to be private if true.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Email> parseEmail(Optional<String> email, boolean isPrivate) throws IllegalValueException {
        requireNonNull(email);
        return email.isPresent() ? Optional.of(new Email(email.get(), isPrivate)) : Optional.empty();
    }

```
###### /java/seedu/address/logic/parser/ParserUtil.java
``` java
    /**
     * Parses a string into a {@code TaskAddress} if it is present.
     */
    public static Optional<TaskAddress> parseTaskAddress(Optional<String> address) throws IllegalValueException {
        requireNonNull(address);
        return address.isPresent() ? Optional.of(new TaskAddress(address.get())) : Optional.empty();
    }

    /**
     * Parses a string into a (@code Location) if it is present.
     */
    public static Optional<Location> parseLocationFromAddress(Optional<String> location) throws IllegalValueException {
        requireNonNull(location);
        return location.isPresent() ? Optional.of(new Location(location.get())) : Optional.empty();
    }
}
```
###### /java/seedu/address/logic/parser/NavigateCommandParser.java
``` java
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAVIGATE_FROM_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAVIGATE_FROM_PERSON;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAVIGATE_FROM_TASK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAVIGATE_TO_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAVIGATE_TO_PERSON;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAVIGATE_TO_TASK;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.NavigateCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Location;

/**
 * Parses input arguments and creates a new SelectCommand object
 */
public class NavigateCommandParser implements Parser<NavigateCommand> {

    private Location from = null;
    private Location to = null;
    private Index fromIndex = null;
    private Index toIndex = null;
    /**
     * Parses the given {@code String} of arguments in the context of the SelectCommand
     * and returns an SelectCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public NavigateCommand parse(String args) throws ParseException {
        resetValues();
        ArgumentMultimap argumentMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAVIGATE_FROM_PERSON,
                PREFIX_NAVIGATE_FROM_TASK, PREFIX_NAVIGATE_FROM_ADDRESS, PREFIX_NAVIGATE_TO_PERSON,
                PREFIX_NAVIGATE_TO_TASK, PREFIX_NAVIGATE_TO_ADDRESS);

        boolean fromAddress = arePrefixesPresent(argumentMultimap, PREFIX_NAVIGATE_FROM_ADDRESS);
        boolean fromPerson = arePrefixesPresent(argumentMultimap, PREFIX_NAVIGATE_FROM_PERSON);
        boolean fromTask = arePrefixesPresent(argumentMultimap, PREFIX_NAVIGATE_FROM_TASK);

        boolean toAddress = arePrefixesPresent(argumentMultimap, PREFIX_NAVIGATE_TO_ADDRESS);
        boolean toPerson = arePrefixesPresent(argumentMultimap, PREFIX_NAVIGATE_TO_PERSON);
        boolean toTask = arePrefixesPresent(argumentMultimap, PREFIX_NAVIGATE_TO_TASK);

        checkFrom(argumentMultimap, fromAddress, fromPerson, fromTask);

        checkTo(argumentMultimap, toAddress, toPerson, toTask);

        try {
            return new NavigateCommand(from, to, fromIndex, toIndex, fromTask, toTask);
        } catch (IllegalValueException e) {
            throw new ParseException(e.getMessage(), e);
        }
    }

    /**
     * Resets the values stored in NavigateCommandParser object to null
     */
    private void resetValues() {
        from = null;
        to = null;
        fromIndex = null;
        toIndex = null;
    }

    /**
     * Checksif only 1 To argument is provided
     * @throws ParseException if there are no To arguments or there are more than 1 To arguements
     */
    private void checkTo(ArgumentMultimap argumentMultimap, boolean toAddress, boolean toPerson, boolean toTask)
            throws ParseException {
        if (!(toAddress || toPerson || toTask)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, NavigateCommand.MESSAGE_USAGE));
        } else if ((toAddress && (toPerson || toTask)) || (toPerson && toTask)) {
            // If 2 or more to prefixes are present
            throw new ParseException(NavigateCommand.MESSAGE_MULTIPLE_TO_ERROR);
        } else {
            try {
                setArgsForNavigateCommand(argumentMultimap, toAddress, toPerson, false);
            } catch (IllegalValueException e) {
                throw new ParseException(e.getMessage(), e);
            }
        }
    }
    /**
     * Checks if only 1 From argument is provided
     * @throws ParseException if there are no From arguments or there are more than 1 From arguments
     */
    private void checkFrom(ArgumentMultimap argumentMultimap, boolean fromAddress, boolean fromPerson, boolean fromTask)
            throws ParseException {
        if (!(fromAddress || fromPerson || fromTask)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, NavigateCommand.MESSAGE_USAGE));
        } else if ((fromAddress && (fromPerson || fromTask)) || (fromPerson && fromTask)) {
            // If 2 or more from prefixes are present
            throw new ParseException(NavigateCommand.MESSAGE_MULTIPLE_FROM_ERROR);
        } else {
            try {
                setArgsForNavigateCommand(argumentMultimap, fromAddress, fromPerson, true);
            } catch (IllegalValueException e) {
                throw new ParseException(e.getMessage(), e);
            }
        }
    }

    private void setArgsForNavigateCommand(ArgumentMultimap argumentMultimap, boolean address, boolean person,
                                           boolean forFrom) throws IllegalValueException {
        if (address) {
            if (forFrom) {
                from = new Location(ParserUtil.parseLocationFromAddress(
                        argumentMultimap.getValue(PREFIX_NAVIGATE_FROM_ADDRESS)).get().toString());
            } else {
                to = new Location(ParserUtil.parseLocationFromAddress(
                        argumentMultimap.getValue(PREFIX_NAVIGATE_TO_ADDRESS)).get().toString());
            }
        } else if (person) {
            if (forFrom) {
                fromIndex = ParserUtil.parseIndex(argumentMultimap
                        .getValue(PREFIX_NAVIGATE_FROM_PERSON).get());
            } else {
                toIndex = ParserUtil.parseIndex(argumentMultimap
                        .getValue(PREFIX_NAVIGATE_TO_PERSON).get());
            }
        } else {
            if (forFrom) {
                fromIndex = ParserUtil.parseIndex(argumentMultimap
                        .getValue(PREFIX_NAVIGATE_FROM_TASK).get());
            } else {
                toIndex = ParserUtil.parseIndex(argumentMultimap
                        .getValue(PREFIX_NAVIGATE_TO_TASK).get());
            }
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
```
###### /java/seedu/address/logic/commands/NavigateCommand.java
``` java
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.BrowserPanelNavigateEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Location;


/**
 * Navigates from one address to another with the help of Google Maps
 */
public class NavigateCommand extends Command {

    public static final String COMMAND_WORD = "navigate";
    public static final String COMMAND_ALIAS = "nav";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Get directions from one address to another.\n"
            + "Parameters: [fp/INDEX] OR [ft/INDEX] (must be a positive integer) OR [fa/ADDRESS] (Only one of three)"
            + " AND [tp/INDEX] OR [tt/INDEX] (must be a positive integer) OR [ta/ADDRESS] (Only one of three)\n"
            + "Example: " + COMMAND_WORD + " fp/2 ta/University Town";

    public static final String MESSAGE_NAVIGATE_SUCCESS = "Navigating from %1$s to %2$s";
    public static final String MESSAGE_MULTIPLE_FROM_ERROR = "Only one type of From prefix allowed.";
    public static final String MESSAGE_MULTIPLE_TO_ERROR = "Only one type of To prefix allowed.";
    public static final String MESSAGE_PRIVATE_PERSON_ADDRESS_ERROR = "Address of the Person at index %1$s is private.";
    public static final String MESSAGE_PERSON_HAS_NO_ADDRESS_ERROR = "Person at index %1$s does not have an address.";
    public static final String MESSAGE_TASK_HAS_NO_ADDRESS_ERROR = "Task at index %1$s does not have an address.";

    private final Location locationFrom;
    private final Location locationTo;
    private final Index fromIndex;
    private final Index toIndex;
    private final boolean fromIsTask;
    private final boolean toIsTask;
    public NavigateCommand(Location locationFrom, Location locationTo, Index fromIndex, Index toIndex,
                           boolean fromIsTask, boolean toIsTask) throws IllegalValueException {
        Location from = null;
        Location to = null;
        checkDuplicateFromAndToLocation(locationFrom, locationTo, fromIndex, toIndex);

        if (locationFrom != null) {
            from = locationFrom;
        }
        if (locationTo != null) {
            to = locationTo;
        }

        this.locationFrom = from;
        this.locationTo = to;
        this.toIndex = toIndex;
        this.fromIndex = fromIndex;
        this.toIsTask = toIsTask;
        this.fromIsTask = fromIsTask;
    }

    /**
     * Throws an IllegalArgumentException if there is both locationFrom and fromIndex are not null,
     * or if both locationTo and toIndex are not null.
     */
    private void checkDuplicateFromAndToLocation(Location locationFrom, Location locationTo,
                                                 Index fromIndex, Index toIndex) throws IllegalArgumentException {
        if (locationFrom != null && fromIndex != null) {
            throw new IllegalArgumentException(MESSAGE_MULTIPLE_FROM_ERROR);
        }
        if (locationTo != null && toIndex != null) {
            throw new IllegalArgumentException(MESSAGE_MULTIPLE_TO_ERROR);
        }
    }

    private Location setLocationByIndex(Index index, boolean isTask) throws IllegalValueException, CommandException {
        if (isTask) {
            if (model.getFilteredTaskList().get(index.getZeroBased()).getTaskAddress().toString().equals("")) {
                throw new CommandException(String.format(MESSAGE_TASK_HAS_NO_ADDRESS_ERROR, index.getOneBased()));
            } else {
                return new Location(model.getFilteredTaskList().get(index.getZeroBased()).getTaskAddress().toString());
            }
        } else {
            if (model.getFilteredPersonList().get(index.getZeroBased()).getAddress().toString().equals("")) {
                throw new CommandException(String.format(MESSAGE_PERSON_HAS_NO_ADDRESS_ERROR, index.getOneBased()));
            } else if (model.getFilteredPersonList().get(index.getZeroBased()).getAddress().isPrivate()) {
                throw new IllegalArgumentException(MESSAGE_PRIVATE_PERSON_ADDRESS_ERROR);
            } else {
                return new Location(model.getFilteredPersonList().get(index.getZeroBased())
                        .getAddress().toString());
            }
        }
    }
    @Override
    public CommandResult execute() throws CommandException {
        Location from;
        Location to;
        if (fromIndex != null) {
            try {
                from = setLocationByIndex(fromIndex, fromIsTask);
            } catch (IllegalValueException e) {
                throw new IllegalArgumentException(MESSAGE_PRIVATE_PERSON_ADDRESS_ERROR);
            }
        } else {
            from = locationFrom;
        }
        if (toIndex != null) {
            try {
                to = setLocationByIndex(toIndex, toIsTask);
            } catch (IllegalValueException e) {
                throw new IllegalArgumentException(MESSAGE_PRIVATE_PERSON_ADDRESS_ERROR);
            }
        } else {
            to = locationTo;
        }
        EventsCenter.getInstance().post(new BrowserPanelNavigateEvent(from, to));
        return new CommandResult(String.format(MESSAGE_NAVIGATE_SUCCESS, from, to));
    }

    public Location getLocationFrom() {
        return locationFrom;
    }

    public Location getLocationTo() {
        return locationTo;
    }

    public Index getFromIndex() {
        return fromIndex;
    }

    public Index getToIndex() {
        return toIndex;
    }

    public boolean isFromIsTask() {
        return fromIsTask;
    }

    public boolean isToIsTask() {
        return toIsTask;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NavigateCommand // instanceof handles nulls
                && equalsLocationFrom(other)
                && equalsLocationTo(other)
                && equalsFromIndex(other)
                && equalsToIndex(other)
                && equalsFromIsTask(other)
                && equalsToIsTask(other)); // state check
    }

    /**
     * Checks if the (@codde locationFrom) of this object is equal to that of the other Object
     * @param other The other Object we are comparing against
     * @return True if both are null or both have the same value
     */
    private boolean equalsLocationFrom(Object other) {
        if (this.locationFrom == null) {
            return ((NavigateCommand) other).locationFrom == null;
        } else {
            return this.locationFrom.equals(((NavigateCommand) other).locationFrom);
        }
    }

    /**
     * Checks if the (@codde locationTo) of this object is equal to that of the other Object
     * @param other The other Object we are comparing against
     * @return True if both are null or both have the same value
     */
    private boolean equalsLocationTo(Object other) {
        if (this.locationTo == null) {
            return ((NavigateCommand) other).locationTo == null;
        } else {
            return this.locationTo.equals(((NavigateCommand) other).locationTo);
        }
    }

    /**
     * Checks if the (@codde fromIndex) of this object is equal to that of the other Object
     * @param other The other Object we are comparing against
     * @return True if both are null or both have the same value
     */
    private boolean equalsFromIndex(Object other) {
        if (this.fromIndex == null) {
            return ((NavigateCommand) other).fromIndex == null;
        } else {
            return this.fromIndex.equals(((NavigateCommand) other).fromIndex);
        }
    }

    /**
     * Checks if the (@codde toIndex) of this object is equal to that of the other Object
     * @param other The other Object we are comparing against
     * @return True if both are null or both have the same value
     */
    private boolean equalsToIndex(Object other) {
        if (this.toIndex == null) {
            return ((NavigateCommand) other).toIndex == null;
        } else {
            return this.toIndex.equals(((NavigateCommand) other).toIndex);
        }
    }

    /**
     * Checks if the (@codde fromIsTask) of this object is equal to that of the other Object
     * @param other The other Object we are comparing against
     * @return True if both are null or both have the same value
     */
    private boolean equalsFromIsTask(Object other) {
        return this.fromIsTask == (((NavigateCommand) other).fromIsTask);
    }

    /**
     * Checks if the (@codde toIsTask) of this object is equal to that of the other Object
     * @param other The other Object we are comparing against
     * @return True if both are null or both have the same value
     */
    private boolean equalsToIsTask(Object other) {
        return this.toIsTask == (((NavigateCommand) other).toIsTask);
    }
}
```
###### /java/seedu/address/logic/commands/LocateCommand.java
``` java
import java.util.List;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.BrowserPanelLocateEvent;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * Locates a person's address on Google Maps identified using it's last displayed index from the address book.
 */
public class LocateCommand extends Command {

    public static final String COMMAND_WORD = "locate";
    public static final String COMMAND_ALIAS = "loc";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Locates the address of the person identified by the index number used in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_LOCATE_PERSON_SUCCESS = "Searching for Person: %1$s";
    public static final String MESSAGE_PRIVATE_ADDRESS_FAILURE = "Person %1$s has a Private Address";

    private final Index targetIndex;

    public LocateCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {

        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        if (model.getFilteredPersonList().get(targetIndex.getZeroBased()).getAddress().isPrivate()) {
            throw new CommandException(String.format(MESSAGE_PRIVATE_ADDRESS_FAILURE, targetIndex.getOneBased()));
        }
        EventsCenter.getInstance().post(new BrowserPanelLocateEvent(
                model.getFilteredPersonList().get(targetIndex.getZeroBased())));
        return new CommandResult(String.format(MESSAGE_LOCATE_PERSON_SUCCESS, targetIndex.getOneBased()));

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof LocateCommand // instanceof handles nulls
                && this.targetIndex.equals(((LocateCommand) other).targetIndex)); // state check
    }
}
```
###### /java/seedu/address/logic/commands/EditCommand.java
``` java
    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     * A person with private fields cannot be edited
     */
    private static Person createEditedPerson(ReadOnlyPerson personToEdit,
                                             EditPersonDescriptor editPersonDescriptor)
            throws IllegalArgumentException {
        assert personToEdit != null;

        Name updatedName;
        Phone updatedPhone;
        Email updatedEmail;
        Address updatedAddress;
        Remark updatedRemark;
        Set<Tag> updatedTags;
        Boolean updateFavourite;
        Avatar updatedAvatar;

        areFieldsAllPrivate = true;
        updatedName = createUpdatedName(personToEdit, editPersonDescriptor);

        updatedPhone = createUpdatedPhone(personToEdit, editPersonDescriptor);

        updatedEmail = createUpdatedEmail(personToEdit, editPersonDescriptor);

        updatedAddress = createUpdatedAddress(personToEdit, editPersonDescriptor);

        updatedRemark = createUpdatedRemark(personToEdit, editPersonDescriptor);

        updatedTags = createUpdatedTags(personToEdit, editPersonDescriptor);

        updateFavourite = createUpdatedFavourite(personToEdit, editPersonDescriptor);

        updatedAvatar = createUpdatedAvatar(personToEdit, editPersonDescriptor);

        if (areFieldsAllPrivate) {
            throw new IllegalArgumentException();
        }
        return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress,
                          updateFavourite, updatedRemark, updatedAvatar, updatedTags);
    }

    /**
     * Creates an updated (@code Name) for use in createEditedPerson
     * @param personToEdit The person to edit
     * @param editPersonDescriptor Edited with this editPersonDescriptor
     * @return A new (@code Name) from either the personToEdit or the editPersonDescriptor depending on privacy
     */
    private static Name createUpdatedName(ReadOnlyPerson personToEdit, EditPersonDescriptor editPersonDescriptor) {
        Name updatedName;
        if (!personToEdit.getName().isPrivate()) {
            updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
            if (editPersonDescriptor.getName().isPresent()) {
                areFieldsAllPrivate = false;
            }
        } else {
            updatedName = personToEdit.getName();
        }
        return updatedName;
    }

    /**
     * Creates an updated (@code Phone) for use in createEditedPerson
     * @param personToEdit The person to edit
     * @param editPersonDescriptor Edited with this editPersonDescriptor
     * @return A new (@code Phone) from either the personToEdit or the editPersonDescriptor
     * depending on privacy and the input
     */
    private static Phone createUpdatedPhone(ReadOnlyPerson personToEdit, EditPersonDescriptor editPersonDescriptor) {
        Phone updatedPhone;
        if (!personToEdit.getPhone().isPrivate()) {
            updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
            if (editPersonDescriptor.getPhone().isPresent()) {
                areFieldsAllPrivate = false;
            }
        } else {
            updatedPhone = personToEdit.getPhone();
        }
        return updatedPhone;
    }

    /**
     * Creates an updated (@code Email) for use in createEditedPerson
     * @param personToEdit The person to edit
     * @param editPersonDescriptor Edited with this editPersonDescriptor
     * @return A new (@code Email) from either the personToEdit or the editPersonDescriptor
     * depending on privacy and the input
     */
    private static Email createUpdatedEmail(ReadOnlyPerson personToEdit, EditPersonDescriptor editPersonDescriptor) {
        Email updatedEmail;
        if (!personToEdit.getEmail().isPrivate()) {
            updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
            if (editPersonDescriptor.getEmail().isPresent()) {
                areFieldsAllPrivate = false;
            }
        } else {
            updatedEmail = personToEdit.getEmail();
        }
        return updatedEmail;
    }

    /**
     * Creates an updated (@code Address) for use in createEditedPerson
     * @param personToEdit The person to edit
     * @param editPersonDescriptor Edited with this editPersonDescriptor
     * @return A new (@code Address) from either the personToEdit or the editPersonDescriptor
     * depending on privacy and the input
     */
    private static Address createUpdatedAddress(ReadOnlyPerson personToEdit,
                                                EditPersonDescriptor editPersonDescriptor) {
        Address updatedAddress;
        if (!personToEdit.getAddress().isPrivate()) {
            updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());
            if (editPersonDescriptor.getAddress().isPresent()) {
                areFieldsAllPrivate = false;
            }
        } else {
            updatedAddress = personToEdit.getAddress();
        }
        return updatedAddress;
    }
    //**author charlesgoh
    /**
     * Creates an updated (@code Remark) for use in createEditedPerson
     * @param personToEdit The person to edit
     * @param editPersonDescriptor Edited with this editPersonDescriptor
     * @return A new (@code Remark) from either the personToEdit or the editPersonDescriptor
     * depending on privacy and the input
     */
    private static Remark createUpdatedRemark(ReadOnlyPerson personToEdit, EditPersonDescriptor editPersonDescriptor) {
        Remark updatedRemark;
        if (!personToEdit.getRemark().isPrivate()) {
            updatedRemark = editPersonDescriptor.getRemark().orElse(personToEdit.getRemark());
            if (editPersonDescriptor.getRemark().isPresent()) {
                areFieldsAllPrivate = false;
            }
        } else {
            updatedRemark = personToEdit.getRemark();
        }
        return updatedRemark;
    }

    /**
     * Creates an updated (@code Avatar) for use in createEditedPerson
     * @param personToEdit The person to edit
     * @param editPersonDescriptor Edited with this editPersonDescriptor
     * @return A new (@code Avatar) from either the personToEdit or the editPersonDescriptor
     * depending on privacy and the input
     */
    private static Avatar createUpdatedAvatar(ReadOnlyPerson personToEdit, EditPersonDescriptor editPersonDescriptor) {
        Avatar updatedAvatar;
        if (!personToEdit.getAvatar().isPrivate()) {
            updatedAvatar = editPersonDescriptor.getAvatar().orElse(personToEdit.getAvatar());
            if (editPersonDescriptor.getAvatar().isPresent()) {
                areFieldsAllPrivate = false;
            }
        } else {
            updatedAvatar = personToEdit.getAvatar();
        }
        return updatedAvatar;
    }
    //author
    /**
     * Creates an updated (@code Tag) for use in createEditedPerson
     * @param personToEdit The person to edit
     * @param editPersonDescriptor Edited with this editPersonDescriptor
     * @return A new (@code Tag) from either the personToEdit or the editPersonDescriptor depending on the input
     */
    private static Set<Tag> createUpdatedTags(ReadOnlyPerson personToEdit, EditPersonDescriptor editPersonDescriptor) {
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());
        if (editPersonDescriptor.getTags().isPresent()) {
            areFieldsAllPrivate = false;
        }
        return updatedTags;
    }

    /**
     * Creates an updated (@code Favourite) for use in createEditedPerson
     * @param personToEdit The person to edit
     * @param editPersonDescriptor Edited with this editPersonDescriptor
     * @return A new (@code Favourite) from either the personToEdit or the editPersonDescriptor depending on the input
     */
    private static Boolean createUpdatedFavourite(ReadOnlyPerson personToEdit,
                                                  EditPersonDescriptor editPersonDescriptor) {
        Boolean updateFavourite = editPersonDescriptor.getFavourite().orElse(personToEdit.getFavourite());
        if (editPersonDescriptor.getFavourite().isPresent()) {
            areFieldsAllPrivate = false;
        }
        return updateFavourite;
    }

    /**
     * Creates and returns a {@code Task} with the details of {@code taskToEdit}
     * edited with {@code editTaskDescriptor}.
     */
    private static Task createEditedTask(ReadOnlyTask taskToEdit,
                                             EditTaskDescriptor editTaskDescriptor) {
        assert taskToEdit != null;

        TaskName updatedTaskName;
        Description updatedDescription;
        Deadline updatedDeadline;
        Priority updatedPriority;
        Assignees assignees;
        Boolean updatedState;
        TaskAddress updatedTaskAddress;

        updatedTaskName = editTaskDescriptor.getTaskName().orElse(taskToEdit.getTaskName());
        updatedDescription = editTaskDescriptor.getDescription().orElse(taskToEdit.getDescription());
        updatedDeadline = editTaskDescriptor.getDeadline().orElse(taskToEdit.getDeadline());
        updatedPriority = editTaskDescriptor.getPriority().orElse(taskToEdit.getPriority());
        // You cannot edit assignees or state using edit command
        assignees = taskToEdit.getAssignees();
        updatedState = taskToEdit.getCompleteState();
        updatedTaskAddress = editTaskDescriptor.getTaskAddress().orElse(taskToEdit.getTaskAddress());

        return new Task(updatedTaskName, updatedDescription, updatedDeadline, updatedPriority, assignees, updatedState,
                updatedTaskAddress);
    }

```
###### /java/seedu/address/logic/commands/ChangePrivacyCommand.java
``` java
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

```
###### /java/seedu/address/storage/XmlAdaptedPerson.java
``` java
    /**
     * Converts a given Person into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedPerson
     */
    public XmlAdaptedPerson(ReadOnlyPerson source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        favourite = source.getFavourite().toString();
        remark = source.getRemark().value;
        avatar = source.getAvatar().value;

        nameIsPrivate = source.getName().isPrivate();
        phoneIsPrivate = source.getPhone().isPrivate();
        emailIsPrivate = source.getEmail().isPrivate();
        addressIsPrivate = source.getAddress().isPrivate();
        remarkIsPrivate = source.getRemark().isPrivate();
        avatarIsPrivate = source.getAvatar().isPrivate();

        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }
    }

    /**
     * Converts this jaxb-friendly adapted person object into the model's Person object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            personTags.add(tag.toModelType());
        }
        if (nameIsPrivate == null) {
            nameIsPrivate = false;
        }
        if (phoneIsPrivate == null) {
            phoneIsPrivate = false;
        }
        if (emailIsPrivate == null) {
            emailIsPrivate = false;
        }
        if (addressIsPrivate == null) {
            addressIsPrivate = false;
        }
        if (remarkIsPrivate == null) {
            remarkIsPrivate = false;
        }
        if (avatarIsPrivate == null) {
            avatarIsPrivate = false;
        }
        final Name name = new Name(this.name, this.nameIsPrivate);
        final Phone phone = new Phone(this.phone, this.phoneIsPrivate);
        final Email email = new Email(this.email, this.emailIsPrivate);
        final Address address = new Address(this.address, this.addressIsPrivate);
        final Boolean favourite = new Boolean(this.favourite);
        final Remark remark = new Remark(this.remark, this.remarkIsPrivate);
        final Avatar avatar = new Avatar(this.avatar, this.avatarIsPrivate);
        final Set<Tag> tags = new HashSet<>(personTags);
        return new Person(name, phone, email, address, favourite, remark, avatar, tags);
    }
}
```
###### /java/seedu/address/MainApp.java
``` java
    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting AddressBook " + MainApp.VERSION);
        ui.start(primaryStage);
        MainWindow mw = ui.getMainWindow();
        mw.setMainApp(this);
        mw.setStorage(storage);
        mw.setModel(model);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping Address Book ] =============================");
        ui.stop();
        try {
            storage.saveUserPrefs(userPrefs);
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
        Platform.exit();
        System.exit(0);
    }

    @Subscribe
    public void handleExitAppRequestEvent(ExitAppRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        this.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```
###### /java/seedu/address/model/person/Phone.java
``` java
    public Phone(String phone, boolean isPrivate) throws IllegalValueException {
        this(phone);
        this.setPrivate(isPrivate);
    }

```
###### /java/seedu/address/model/person/Phone.java
``` java
    @Override
    public String toString() {
        if (isPrivate) {
            return "<Private Phone>";
        }
        return value;
    }

```
###### /java/seedu/address/model/person/Phone.java
``` java
    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
}
```
###### /java/seedu/address/model/person/Email.java
``` java
    public Email(String email, boolean isPrivate) throws IllegalValueException {
        this(email);
        this.setPrivate(isPrivate);
    }

```
###### /java/seedu/address/model/person/Email.java
``` java
    @Override
    public String toString() {
        if (isPrivate) {
            return "<Private Email>";
        }
        return value;
    }

```
###### /java/seedu/address/model/person/Email.java
``` java
    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
}
```
###### /java/seedu/address/model/person/Address.java
``` java
    public Address(String address, boolean isPrivate) throws IllegalValueException {
        this(address);
        this.setPrivate(isPrivate);
    }

```
###### /java/seedu/address/model/person/Address.java
``` java
    @Override
    public String toString() {
        if (isPrivate) {
            return "<Private Address>";
        }
        return value;
    }

```
###### /java/seedu/address/model/person/Address.java
``` java
    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
}
```
###### /java/seedu/address/model/person/Name.java
``` java
    public Name(String name, boolean isPrivate) throws IllegalValueException {
        this(name);
        this.setPrivate(isPrivate);
    }

```
###### /java/seedu/address/model/person/Name.java
``` java
    @Override
    public String toString() {
        if (isPrivate) {
            return "<Private Name>";
        }
        return fullName;
    }

```
###### /java/seedu/address/model/person/Name.java
``` java
    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
}
```
###### /java/seedu/address/model/task/TaskAddress.java
``` java
import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Task's address in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidTaskAddress(String)}
 */
public class TaskAddress {
    public static final String MESSAGE_TASK_ADDRESS_CONSTRAINTS =
            "Task addresses can take any values, and it should not be blank";
    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String ADDRESS_VALIDATION_REGEX = "[^\\s].*";
    public static final String ADDRESS_PLACEHOLDER_VALUE = "";

    public final String taskAddress;
    /**
     * Validates given address.
     *
     * @throws IllegalValueException if given address string is invalid.
     */
    public TaskAddress(String address) throws IllegalValueException {
        if (address == null) {
            this.taskAddress = ADDRESS_PLACEHOLDER_VALUE;
            return;
        }
        if (!isValidTaskAddress(address)) {
            throw new IllegalValueException(MESSAGE_TASK_ADDRESS_CONSTRAINTS);
        }
        this.taskAddress = address;
    }

    /**
     * Returns true if a given string is a valid task address.
     */
    public static boolean isValidTaskAddress(String test) {
        return test.matches(ADDRESS_VALIDATION_REGEX) || test.equals(ADDRESS_PLACEHOLDER_VALUE);
    }

    @Override
    public String toString() {
        return taskAddress;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskAddress // instanceof handles nulls
                && this.taskAddress.equals(((TaskAddress) other).taskAddress)); // state check
    }

    @Override
    public int hashCode() {
        return taskAddress.hashCode();
    }
}

```
###### /java/seedu/address/model/Location.java
``` java
import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Location in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidLocation(String)}
 */
public class Location {

    public static final String MESSAGE_ADDRESS_CONSTRAINTS =
            "Location can take any value, and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String LOCATION_VALIDATION_REGEX = "[^\\s].*";

    public final String value;

    /**
     * Validates given location.
     *
     * @throws IllegalValueException if given location string is invalid.
     */
    public Location(String location) throws IllegalValueException {
        if (location == null) {
            throw new IllegalValueException(MESSAGE_ADDRESS_CONSTRAINTS);
        }
        if (!isValidLocation(location)) {
            throw new IllegalValueException(MESSAGE_ADDRESS_CONSTRAINTS);
        }
        this.value = location;
    }

    /**
     * Returns true if a given string is a valid location.
     */
    public static boolean isValidLocation(String test) {
        return test.matches(LOCATION_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Location // instanceof handles nulls
                && this.value.equals(((Location) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
```

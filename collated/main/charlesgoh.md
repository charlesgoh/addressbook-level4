# charlesgoh
###### /resources/view/MainWindow.fxml
``` fxml
      <Menu mnemonicParsing="false" text="Font Size">
        <items>
          <MenuItem fx:id="increaseSizeMenuItem" mnemonicParsing="false" onAction="#handleIncreaseFontSize" text="Increase +" />
            <MenuItem fx:id="decreaseSizeMenuItem" mnemonicParsing="false" onAction="#handleDecreaseFontSize" text="Decrease -" />
            <MenuItem fx:id="resetSizeMenuItem" mnemonicParsing="false" onAction="#handleResetFontSize" text="Reset" />
        </items>
      </Menu>
  </MenuBar>

  <StackPane fx:id="commandBoxPlaceholder" styleClass="pane-with-border" VBox.vgrow="NEVER">
    <padding>
      <Insets bottom="5" left="10" right="10" top="5" />
    </padding>
  </StackPane>

  <StackPane fx:id="resultDisplayPlaceholder" maxHeight="100" minHeight="100" prefHeight="100" styleClass="pane-with-border" VBox.vgrow="NEVER">
    <padding>
      <Insets bottom="5" left="10" right="10" top="5" />
    </padding>
  </StackPane>

  <SplitPane id="splitPane" fx:id="splitPane" dividerPositions="0.4, 0.5" VBox.vgrow="ALWAYS">
    <VBox fx:id="personList" SplitPane.resizableWithParent="false">
      <padding>
        <Insets bottom="10" left="10" right="10" top="10" />
      </padding>
      <StackPane fx:id="personListPanelPlaceholder" VBox.vgrow="ALWAYS" />
    </VBox>
      <VBox fx:id="taskList" prefHeight="262.0" prefWidth="121.0">
         <children>
            <StackPane fx:id="taskListPanelPlaceholder" alignment="TOP_CENTER" VBox.vgrow="ALWAYS">
               <opaqueInsets>
                  <Insets />
               </opaqueInsets></StackPane>
         </children>
         <padding>
            <Insets bottom="10.0" left="10.0" right="10.0" top="10.0" />
         </padding>
      </VBox>

    <StackPane fx:id="browserPlaceholder">
      <padding>
        <Insets bottom="10" left="10" right="10" top="10" />
      </padding>
    </StackPane>
  </SplitPane>

  <StackPane fx:id="statusbarPlaceholder" VBox.vgrow="NEVER" />
</VBox>
```
###### /java/seedu/address/ui/TaskCard.java
``` java
    /**
     * Set default size for all attributes
     */
    public void updateAttributeSizes() {
        nameSize = DEFAULT_NAME_SIZE + (fontSizeMultipler * FONT_SIZE_EXTENDER);
        attributeSize = DEFAULT_ATTRIBUTE_SIZE + (fontSizeMultipler * FONT_SIZE_EXTENDER);

        // Set styles using set name and attribute sizes
        taskName.setStyle("-fx-font-size: " + Integer.toString(nameSize));
        id.setStyle("-fx-font-size: " + Integer.toString(nameSize));
        description.setStyle("-fx-font-size: " + Integer.toString(attributeSize));
        deadline.setStyle("-fx-font-size: " + Integer.toString(attributeSize));
        priority.setStyle("-fx-font-size: " + Integer.toString(attributeSize));
        taskAddress.setStyle("-fx-font-size: " + Integer.toString(attributeSize));
    }
```
###### /java/seedu/address/ui/TaskCard.java
``` java

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TaskCard)) {
            return false;
        }

        // state check
        TaskCard card = (TaskCard) other;
        return id.getText().equals(card.id.getText())
                && task.equals(card.task);
    }

    public Label getTaskName() {
        return taskName;
    }

    public Label getId() {
        return id;
    }

    public Label getDescription() {
        return description;
    }

    public Label getDeadline() {
        return deadline;
    }

    public Label getPriority() {
        return priority;
    }

    public Label getTaskAddress() {
        return taskAddress;
    }

    public void setFontSizeMultiplier(int fontSizeMultipler) {
        this.fontSizeMultipler = fontSizeMultipler;
    }

    public int getFontSizeMultiplier() {
        return this.fontSizeMultipler;
    }
}
```
###### /java/seedu/address/ui/TaskListPanel.java
``` java
    /**
     * Increases all task cards' font sizes in person list
     */
    public void increaseFontSize() {
        logger.info("TaskListPanel: Increasing font sizes");
        setFontSizeMultiplier(fontSizeMultiplier + 1);
        setConnections(taskList);
    }

    /**
     * Decreases all task cards' font sizes in person list
     */
    public void decreaseFontSize() {
        logger.info("TaskListPanel: Decreasing font sizes");
        setFontSizeMultiplier(fontSizeMultiplier - 1);
        setConnections(taskList);
    }

    /**
     * Resets all task cards' font sizes in person list
     */
    public void resetFontSize() {
        logger.info("TaskListPanel: Resetting font sizes");
        fontSizeMultiplier = MINIMUM_FONT_SIZE_MULTIPLIER;
        setConnections(taskList);
    }

    /**
     * Gets integer value of font size multiplier
     */
    public int getFontSizeMultiplier() {
        return fontSizeMultiplier;
    }

    /**
     * Set integer value of font size multiplier
     */
    public void setFontSizeMultiplier(int fontSizeMultiplier) {
        // Set new font size
        this.fontSizeMultiplier = fontSizeMultiplier;

        // Restrict from minimum
        this.fontSizeMultiplier = Math.max(MINIMUM_FONT_SIZE_MULTIPLIER, this.fontSizeMultiplier);

        // Restrict from maximum
        this.fontSizeMultiplier = Math.min(MAXIMUM_FONT_SIZE_MULTIPLIER, this.fontSizeMultiplier);

        logger.info("New task font size multiplier: " + Integer.toString(this.fontSizeMultiplier));
    }

    /**
     * Handles command induced change font size event for task cards
     * @param event
     */
    @Subscribe
    private void handleTaskCardChangeFontSizeEvent (ChangeFontSizeEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        switch (event.getTriggerOption()) {
        case 0:
            logger.info("Attempting to increase font size");
            increaseFontSize();
            break;
        case 1:
            decreaseFontSize();
            logger.info("Attempting to decrease font size");
            break;
        case 2:
            resetFontSize();
            logger.info("Attempting to reset font size");
            break;
        default:
            logger.info("Unable to handle change font size event. Stopping execution now");
        }
    }
```
###### /java/seedu/address/ui/ViewPersonPanel.java
``` java
    public static final int DEFAULT_NAME_SIZE = 15;
    public static final int DEFAULT_ATTRIBUTE_SIZE = 10;
    public static final int FONT_SIZE_EXTENDER = 5;
    public static final int DEFAULT_FONT_SIZE_MULTIPLIER = 0;
```
###### /java/seedu/address/ui/ViewPersonPanel.java
``` java
    /**
     * Set default size for all attributes
     */
    public void updateAttributeSizes() {
        nameSize = DEFAULT_NAME_SIZE + (fontSizeMultipler * FONT_SIZE_EXTENDER);
        attributeSize = DEFAULT_ATTRIBUTE_SIZE + (fontSizeMultipler * FONT_SIZE_EXTENDER);

        // Set styles using set name and attribute sizes
        name.setStyle("-fx-font-size: " + Integer.toString(nameSize));
        phone.setStyle("-fx-font-size: " + Integer.toString(attributeSize));
        address.setStyle("-fx-font-size: " + Integer.toString(attributeSize));
        remark.setStyle("-fx-font-size: " + Integer.toString(attributeSize));
        email.setStyle("-fx-font-size: " + Integer.toString(attributeSize));
    }
```
###### /java/seedu/address/ui/MainWindow.java
``` java
    /**
     * Handle increase font size command for menu item
     */
    @FXML
    public void handleIncreaseFontSize() {
        logger.info("Handling increase in font size");
        raise(new ChangeFontSizeEvent(ChangeFontSizeEvent.getIncreaseSizeEventIndex()));
    }

    /**
     * Handle decrease font size command for menu item
     */
    @FXML
    public void handleDecreaseFontSize() {
        logger.info("Handling decrease in font size");
        raise(new ChangeFontSizeEvent(ChangeFontSizeEvent.getDecreaseSizeEventIndex()));
    }

    /**
     * Handle reset font size command
     */
    @FXML
    public void handleResetFontSize() {
        logger.info("Handling reset in font size");
        raise(new ChangeFontSizeEvent(ChangeFontSizeEvent.getResetSizeEventIndex()));
    }
```
###### /java/seedu/address/ui/PersonListPanel.java
``` java
    /**
     * Increases all person cards' font sizes in person list
     */
    public void increaseFontSize() {
        logger.info("PersonListPanel: Increasing font sizes");
        setFontSizeMultiplier(this.fontSizeMultiplier + 1);
        setConnections(personList);
    }

    /**
     * Decreases all person cards' font sizes in person list
     */
    public void decreaseFontSize() {
        logger.info("PersonListPanel: Decreasing font sizes");
        setFontSizeMultiplier(this.fontSizeMultiplier - 1);
        setConnections(personList);
    }

    /**
     * Resets all person cards' font sizes in person list
     */
    public void resetFontSize() {
        logger.info("PersonListPanel: Resetting font sizes");
        fontSizeMultiplier = MINIMUM_FONT_SIZE_MULTIPLIER;
        setConnections(personList);
    }

    /**
     * Gets integer value of font size multiplier
     */
    public int getFontSizeMultiplier() {
        return fontSizeMultiplier;
    }

    /**
     * Set integer value of font size multiplier
     */
    public void setFontSizeMultiplier(int fontSizeMultiplier) {
        // Set new font size
        this.fontSizeMultiplier = fontSizeMultiplier;

        // Restrict from minimum
        this.fontSizeMultiplier = Math.max(MINIMUM_FONT_SIZE_MULTIPLIER, this.fontSizeMultiplier);

        // Restrict from maximum
        this.fontSizeMultiplier = Math.min(MAXIMUM_FONT_SIZE_MULTIPLIER, this.fontSizeMultiplier);

        logger.info("New person font size multiplier: " + Integer.toString(this.fontSizeMultiplier));
    }

    /**
     * Handles command induced change font size event for person cards
     * @param event
     */
    @Subscribe
    private void handlePersonCardChangeFontSizeEvent (ChangeFontSizeEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        switch (event.getTriggerOption()) {
        case 0:
            logger.info("Attempting to increase font size");
            increaseFontSize();
            break;
        case 1:
            decreaseFontSize();
            logger.info("Attempting to decrease font size");
            break;
        case 2:
            resetFontSize();
            logger.info("Attempting to reset font size");
            break;
        default:
            logger.info("Unable to handle change font size event. Stopping execution now");
        }
    }
```
###### /java/seedu/address/ui/PersonCard.java
``` java
    /**
     * Set default size for all attributes
     */
    public void updateAttributeSizes() {
        nameSize = DEFAULT_NAME_SIZE + (fontSizeMultipler * FONT_SIZE_EXTENDER);
        attributeSize = DEFAULT_ATTRIBUTE_SIZE + (fontSizeMultipler * FONT_SIZE_EXTENDER);

        // Set styles using set name and attribute sizes
        name.setStyle("-fx-font-size: " + Integer.toString(nameSize));
        id.setStyle("-fx-font-size: " + Integer.toString(nameSize));
        phone.setStyle("-fx-font-size: " + Integer.toString(attributeSize));
        address.setStyle("-fx-font-size: " + Integer.toString(attributeSize));
        remark.setStyle("-fx-font-size: " + Integer.toString(attributeSize));
        email.setStyle("-fx-font-size: " + Integer.toString(attributeSize));
    }
```
###### /java/seedu/address/ui/PersonCard.java
``` java
    public Label getName() {
        return name;
    }

    public Label getId() {
        return id;
    }

    public Label getPhone() {
        return phone;
    }

    public Label getAddress() {
        return address;
    }

    public Label getRemark() {
        return remark;
    }

    public Label getEmail() {
        return email;
    }

    public int getFontSizeMultipler() {
        return fontSizeMultipler;
    }

    public void setFontSizeMultipler(int fontSizeMultipler) {
        this.fontSizeMultipler = fontSizeMultipler;
    }
    //author
}
```
###### /java/seedu/address/logic/parser/BackupCommandParser.java
``` java
public class BackupCommandParser implements Parser<BackupCommand> {

    public static final int SIZE_OF_ARG_ARRAY = 1;

    /**
     * Parses the given user input and backs up data into a separate file name.
     * @param userInput
     * @throws ParseException
     */
    public BackupCommand parse(String userInput) throws ParseException {
        String trimmedArgs = userInput.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, BackupCommand.MESSAGE_USAGE));
        }

        // Converts arg arrays to lower case to account for caps entries
        String[] argKeywords = trimmedArgs.split("\\s");
        // System.out.println(Integer.toString(argKeywords.length));

        if (argKeywords.length != SIZE_OF_ARG_ARRAY) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, BackupCommand.MESSAGE_USAGE));
        }

        return new BackupCommand(trimmedArgs);
    }
}
```
###### /java/seedu/address/logic/parser/AddressBookParser.java
``` java
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
```
###### /java/seedu/address/logic/parser/SortCommandParser.java
``` java
/**
 * Parses input arguments and creates a new SortCommand object based on the field and order parameters provided
 */
public class SortCommandParser implements Parser<SortCommand> {

    public static final int LIST_ARG_POSITION = 0;
    public static final int FIELD_ARG_POSITION = 1;
    public static final int ORDER_ARG_POSITION = 2;
    public static final int SIZE_OF_ARG_ARRAY = 3;

    public static final List<String> PERSON_FIELD_ARGS = ACCEPTED_FIELD_PARAMETERS.subList(0, 4);
    public static final List<String> TASK_FIELD_ARGS = ACCEPTED_FIELD_PARAMETERS.subList(4, 6);

    /**
     * Parses the given {@code String} of arguments in the context of the SortCommand
     * and returns a SortCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SortCommand parse(String args) throws ParseException {
        //System.out.println(args);
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        // Converts arg arrays to lower case to account for caps entries
        String[] argKeywords = trimmedArgs.split("\\s");
        // System.out.println(Integer.toString(argKeywords.length));

        // Eliminate the sort keyword
        for (int i = 0; i < argKeywords.length; i++) {
            argKeywords[i] = argKeywords[i].toLowerCase();
            // System.out.println(argKeywords[i] + " " + Integer.toString(i));
        }

        if (argKeywords.length != SIZE_OF_ARG_ARRAY
                || (argKeywords[0].equals(ACCEPTED_LIST_PARAMETERS.get(0))
                && TASK_FIELD_ARGS.contains(argKeywords[1]))
                || (argKeywords[0].equals(ACCEPTED_LIST_PARAMETERS.get(1))
                && PERSON_FIELD_ARGS.contains(argKeywords[1]))) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        if (!SortCommand.ACCEPTED_FIELD_PARAMETERS.contains(argKeywords[FIELD_ARG_POSITION])
                    || !SortCommand.ACCEPTED_ORDER_PARAMETERS.contains(argKeywords[ORDER_ARG_POSITION])
                    || !SortCommand.ACCEPTED_LIST_PARAMETERS.contains(argKeywords[LIST_ARG_POSITION])) {
            throw new ParseException(String.format(MESSAGE_INVALID_INPUT, SortCommand.MESSAGE_USAGE));
        }

        // If there are no problems with the input, return a new instance of SortCommand
        return new SortCommand(argKeywords[LIST_ARG_POSITION],
                argKeywords[FIELD_ARG_POSITION], argKeywords[ORDER_ARG_POSITION]);

    }

}
```
###### /java/seedu/address/logic/parser/FontSizeCommandParser.java
``` java
/**
 * Parses input arguments and creates a new SortCommand object based on the field and order parameters provided
 */
public class FontSizeCommandParser implements Parser<FontSizeCommand> {
    public static final int PARAMETER_POSITION = 0;

    /**
     * Parses the given {@code String} of arguments in the context of the FontSizeCommand
     * and returns a FontSizeCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FontSizeCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FontSizeCommand.MESSAGE_USAGE));
        }

        // Converts arg arrays to lower case to account for caps entries
        String[] argKeywords = trimmedArgs.split("\\s");
        for (int i = 0; i < argKeywords.length; i++) {
            argKeywords[i] = argKeywords[i].toLowerCase();
        }

        if (argKeywords.length != 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FontSizeCommand.MESSAGE_USAGE));
        }

        if (!FontSizeCommand.ACCEPTED_PARAMETERS.contains(argKeywords[PARAMETER_POSITION])
                || !FontSizeCommand.ACCEPTED_PARAMETERS.contains(argKeywords[PARAMETER_POSITION])
                || !FontSizeCommand.ACCEPTED_PARAMETERS.contains(argKeywords[PARAMETER_POSITION])) {
            throw new ParseException(String.format(MESSAGE_INVALID_INPUT, FontSizeCommand.MESSAGE_USAGE));
        }

        // If there are no problems with the input, return a new instance of SortCommand
        return new FontSizeCommand(argKeywords[PARAMETER_POSITION]);

    }

}
```
###### /java/seedu/address/logic/commands/BackupCommand.java
``` java
public class BackupCommand extends Command {
    public static final String COMMAND_WORD = "backup";
    public static final String COMMAND_ALIAS = "bk";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Backs up data to a user input "
            + "location field [FILEPATH]\n"
            + "Parameter: KEYWORD [FILEPATH]\n"
            + "Example: " + COMMAND_WORD + "MyBackUpFile";

    public static final String MESSAGE_INVALID_INPUT = "Invalid Input.\n";

    public static final String MESSAGE_SUCCESS = "AddressBook++ data backed up successfully.";

    private String args;

    public BackupCommand(String trimmedArgs) {
        super();
        this.args = trimmedArgs;
    }

    @Override
    public CommandResult execute() throws CommandException {
        requireNonNull(model);
        requireNonNull(model.getAddressBook());
        requireNonNull(storage);
        try {
            storage.backupAddressBook(model.getAddressBook(), args);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
```
###### /java/seedu/address/logic/commands/SortCommand.java
``` java
public class SortCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "sort";
    public static final String COMMAND_ALIAS = "so";

    public static final ArrayList<String> ACCEPTED_LIST_PARAMETERS = new ArrayList<>(Arrays.asList(
            "person", "task"));

    public static final ArrayList<String> ACCEPTED_FIELD_PARAMETERS = new ArrayList<>(Arrays.asList(
            "name", "phone", "email", "address", "priority", "deadline"));

    public static final ArrayList<String> ACCEPTED_ORDER_PARAMETERS = new ArrayList<>(Arrays.asList(
            "asc", "desc"));

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sorts all persons or all tasks by chosen "
                + "field [NAME/PHONE/EMAIL/ADDRESS -- PRIORITY/DEADLINE] by [ASC/DESC] order. Case insensitive\n"
                + "Parameters: KEYWORD [LIST] [FIELD] [ORDER]\n"
                + "Example: " + COMMAND_WORD + " person email desc\n"
                + "Example 2: " + COMMAND_WORD + " task deadline desc\n";

    public static final String MESSAGE_INVALID_INPUT = "Invalid Input.\n"
            + "Accepted Person Field Values: NAME, PHONE, EMAIL, ADDRESS, TASK \n"
            + "Accepted Task Field Values: DEADLINE, PRIORITY\n"
            + "Accepted Order Values: ASC, DESC";

    public static final String MESSAGE_SUCCESS_PERSONS = "All persons in address book successfully sorted";
    public static final String MESSAGE_SUCCESS_TASKS = "All tasks in address book successfully sorted";

    private final String list;
    private final String field;
    private final String order;

    public SortCommand(String list, String field, String order) {
        this.field = field;
        this.order = order;
        this.list = list;
    }

    /**
     * Checks if argument command has the same parameter properties
     * @param commandB
     */
    public boolean sameCommandAs(SortCommand commandB) {
        return commandB.getList().equals(this.list) && commandB.getField()
                .equals(this.field) && commandB.getOrder().equals(this.order);
    }

    public String getField() {
        return this.field;
    }

    public String getOrder() {
        return this.order;
    }

    public String getList() {
        return this.list;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(model);
        if (this.list.equals(ACCEPTED_LIST_PARAMETERS.get(0))) {
            model.sortPersons(getField(), getOrder());
            return new CommandResult(MESSAGE_SUCCESS_PERSONS);
        } else {
            model.sortTasks(getField(), getOrder());
            return new CommandResult(MESSAGE_SUCCESS_TASKS);
        }
    }

}
```
###### /java/seedu/address/logic/commands/FontSizeCommand.java
``` java
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
```
###### /java/seedu/address/logic/commands/ChangePrivacyCommand.java
``` java
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
```
###### /java/seedu/address/logic/commands/ChangePrivacyCommand.java
``` java
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
```
###### /java/seedu/address/storage/AddressBookStorage.java
``` java
    void backupAddressBook(ReadOnlyAddressBook addressBook, String args) throws IOException;
    //author
    void changeFilePath(String fp, UserPrefs u);
}
```
###### /java/seedu/address/storage/StorageManager.java
``` java
    /**
     * Backs up data to a remote location.
     * @param addressBook
     * @param filePath
     * @throws IOException
     */
    public void backupAddressBook(ReadOnlyAddressBook addressBook, String filePath) throws IOException {
        logger.fine("Attempting to write to backup data file in custom location");
        this.saveAddressBook(addressBook, filePath);
    }
```
###### /java/seedu/address/model/person/UniquePersonList.java
``` java
public class UniquePersonList implements Iterable<Person> {

    private final ObservableList<Person> internalList = FXCollections.observableArrayList();
    private final ObservableList<Person> internalCopy = FXCollections.observableArrayList();
    // used by asObservableList()
    private final ObservableList<ReadOnlyPerson> mappedList = EasyBind.map(internalList, (person) -> person);

    /**
     * Returns true if the list contains an equivalent person as the given argument.
     */
    public boolean contains(ReadOnlyPerson toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a person to the list.
     *
     * @throws DuplicatePersonException if the person to add is a duplicate of an existing person in the list.
     */
    public void add(ReadOnlyPerson toAdd) throws DuplicatePersonException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicatePersonException();
        }
        internalList.add(new Person(toAdd));
    }

    /**
     * Replaces the person {@code target} in the list with {@code editedPerson}.
     *
     * @throws DuplicatePersonException if the replacement is equivalent to another existing person in the list.
     * @throws PersonNotFoundException if {@code target} could not be found in the list.
     */
    public void setPerson(ReadOnlyPerson target, ReadOnlyPerson editedPerson)
            throws DuplicatePersonException, PersonNotFoundException {
        requireNonNull(editedPerson);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new PersonNotFoundException();
        }

        if (!target.equals(editedPerson) && internalList.contains(editedPerson)) {
            throw new DuplicatePersonException();
        }

        internalList.set(index, new Person(editedPerson));
    }

    /**
     * Removes the equivalent person from the list.
     *
     * @throws PersonNotFoundException if no such person could be found in the list.
     */
    public boolean remove(ReadOnlyPerson toRemove) throws PersonNotFoundException {
        requireNonNull(toRemove);
        final boolean personFoundAndDeleted = internalList.remove(toRemove);
        if (!personFoundAndDeleted) {
            throw new PersonNotFoundException();
        }
        return personFoundAndDeleted;
    }
```
###### /java/seedu/address/model/person/UniquePersonList.java
``` java
    public void sortBy(String field, String order) {
        //sortyBy first chooses the right comparator
        System.out.println(internalList.size());
        internalCopy.clear();
        for (Person p : internalList) {
            internalCopy.add(p);
        }
        System.out.println(internalCopy.size());
        Comparator<Person> comparator = null;

        /**
         * Comparators for the various fields available for sorting
         */
        Comparator<Person> personNameComparator = new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getName().fullName.compareTo(o2.getName().fullName);
            }
        };

        Comparator<Person> personPhoneComparator = new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getPhone().value.compareTo(o2.getPhone().value);
            }
        };

        Comparator<Person> personEmailComparator = new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getEmail().value.compareTo(o2.getEmail().value);
            }
        };

        Comparator<Person> personAddressComparator = new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getAddress().value.compareTo(o2.getAddress().value);
            }
        };

        switch (field) {
        case "name":
            comparator = personNameComparator;
            break;

        case "phone":
            comparator = personPhoneComparator;
            break;

        case "email":
            comparator = personEmailComparator;
            break;

        case "address":
            comparator = personAddressComparator;
            break;

        default:
            try {
                System.out.println("An error occured");
                throw new Exception("Invalid field parameter entered...\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //sortBy then chooses the right ordering
        switch (order) {
        case "asc":
            Collections.sort(internalList, comparator);
            break;

        case "desc":
            Collections.sort(internalList, Collections.reverseOrder(comparator));
            break;

        default:
            try {
                System.out.println("An error occured");
                throw new Exception("Invalid field parameter entered...\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public Iterator<Person> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniquePersonList // instanceof handles nulls
                        && this.internalList.equals(((UniquePersonList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
```
###### /java/seedu/address/model/person/Remark.java
``` java
public class Remark {
    public static final String MESSAGE_ADDRESS_CONSTRAINTS =
            "Person remarks can take any values, and it should not be blank";

    /*
     * The first character of the remark must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String REMARK_VALIDATION_REGEX = "[^\\s].*";
    public static final String REMARK_PLACEHOLDER_VALUE = "";

    public final String value;
    private boolean isPrivate = false;

    /**
     * Validates given remark.
     *
     * @throws IllegalValueException if given remark string is invalid.
     */
    public Remark(String remark) throws IllegalValueException {
        if (remark == null) {
            this.value = REMARK_PLACEHOLDER_VALUE;
            return;
        }
        if (!isValidRemark(remark)) {
            throw new IllegalValueException(MESSAGE_ADDRESS_CONSTRAINTS);
        }
        this.value = remark;
    }

    public Remark(String remark, boolean isPrivate) throws IllegalValueException {
        this(remark);
        this.setPrivate(isPrivate);
    }

    /**
     * Returns true if a given string is a valid person email.
     */
    public static boolean isValidRemark(String test) {
        return test.matches(REMARK_VALIDATION_REGEX) || test.equals(REMARK_PLACEHOLDER_VALUE);
    }

    @Override
    public String toString() {
        if (isPrivate) {
            return "<Private Remark>";
        }
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Remark // instanceof handles nulls
                && this.value.equals(((Remark) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
}
```
###### /java/seedu/address/model/person/Person.java
``` java
    public void setRemark(Remark remark) {
        this.remark.set(requireNonNull(remark));
    }

    @Override
    public ObjectProperty<Remark> remarkProperty() {
        return remark;
    }

    @Override
    public Remark getRemark() {
        return remark.get();
    }

    public void setAvatar(Avatar avatar) {
        this.avatar.set(requireNonNull(avatar));
    }

    @Override
    public ObjectProperty<Avatar> avatarProperty() {
        return avatar;
    }

    @Override
    public Avatar getAvatar() {
        return avatar.get();
    }
```
###### /java/seedu/address/model/AddressBook.java
``` java
    /**
     * Sorts persons in person list by any field, in either ascending or descending order
     *
     * @param field
     * @param order
     */
    public void sortPersonsBy(String field, String order) {
        persons.sortBy(field, order);
    }

    /**
     * Sorts persons in person list by any field, in either ascending or descending order
     *
     * @param field
     * @param order
     */
    public void sortTasksBy(String field, String order) {
        tasks.sortBy(field, order);
    }

    /**
     * Returns an array list of {@code Index} corresponding to the index of {@code ReadOnlyPerson} specified
     */
    public ArrayList<Index> extractPersonIndexes(ArrayList<ReadOnlyPerson> personsToExtract) {
        return persons.extractIndexes(personsToExtract);
    }

```
###### /java/seedu/address/model/ModelManager.java
``` java
    @Override
    public void sortPersons(String field, String order) {
        addressBook.sortPersonsBy(field, order);
        Index[] updatedIndexes = addressBook.getMappings();
        System.out.println(updatedIndexes.length);
        addressBook.updateTaskAssigneeMappings(updatedIndexes);
        indicateAddressBookChanged();
    }

    @Override
    public void sortTasks(String field, String order) {
        addressBook.sortTasksBy(field, order);
        indicateAddressBookChanged();
    }
```
###### /java/seedu/address/model/task/UniqueTaskList.java
``` java
    public void sortBy(String field, String order) {
        //sortyBy first chooses the right comparator
        Comparator<Task> comparator = null;

        /**
         * Comparators for the various fields available for sorting
         */
        Comparator<Task> priorityComparator = new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return Integer.compare(o1.getPriority().value, o2.getPriority().value);
            }
        };

        Comparator<Task> deadlineComparator = new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return o1.getDeadline().date.compareTo(o2.getDeadline().date);
            }
        };

        switch (field) {
        case "priority":
            comparator = priorityComparator;
            break;

        case "deadline":
            comparator = deadlineComparator;
            break;

        default:
            try {
                System.out.println("An error occured");
                throw new Exception("Invalid field parameter entered...\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //sortBy then chooses the right ordering
        switch (order) {
        case "asc":
            Collections.sort(internalList, comparator);
            break;

        case "desc":
            Collections.sort(internalList, Collections.reverseOrder(comparator));
            break;

        default:
            try {
                System.out.println("An error occured");
                throw new Exception("Invalid field parameter entered...\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
```
###### /java/seedu/address/model/Model.java
``` java
    /**
     * Sorts all persons in person list by chosen field in ascending (asc) or descending (desc) order
     * @param field
     * @param order
     */
    void sortPersons(String field, String order);

    /**
     * Sorts all tasks in task list by chosen field in ascending (asc) or descending (desc) order
     * @param field
     * @param order
     */
    void sortTasks(String field, String order);
```

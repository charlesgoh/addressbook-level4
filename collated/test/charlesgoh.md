# charlesgoh
###### /java/seedu/address/ui/PersonCardTest.java
``` java
    @Test
    public void editFontSizeTests() {
        int fontSizeMultiplier = PersonCard.DEFAULT_FONT_SIZE_MULTIPLIER;
        Person testPerson = new PersonBuilder().build();
        PersonCard personCard = new PersonCard(testPerson, 1, fontSizeMultiplier);
        assertEquals(PersonCard.DEFAULT_FONT_SIZE_MULTIPLIER, personCard.getFontSizeMultipler());
        assertNotEquals(personCard.getFontSizeMultipler(), fontSizeMultiplier + 1);

        // Verify font size increase
        fontSizeMultiplier = PersonCard.DEFAULT_FONT_SIZE_MULTIPLIER + 1;
        personCard.setFontSizeMultipler(fontSizeMultiplier);
        assertEquals(personCard.getFontSizeMultipler(), fontSizeMultiplier);
        assertNotEquals(personCard.getFontSizeMultipler(), PersonCard.DEFAULT_FONT_SIZE_MULTIPLIER);

        // Verify font size decrease
        fontSizeMultiplier = PersonCard.DEFAULT_FONT_SIZE_MULTIPLIER - 1;
        personCard.setFontSizeMultipler(fontSizeMultiplier);
        assertEquals(personCard.getFontSizeMultipler(), fontSizeMultiplier);
        assertNotEquals(personCard.getFontSizeMultipler(), PersonCard.DEFAULT_FONT_SIZE_MULTIPLIER);
    }
```
###### /java/seedu/address/logic/parser/SortCommandParserTest.java
``` java
    @Test
    public void parse_validArguments_success() throws ParseException {
        SortCommand expectedCommand;
        SortCommand actualCommand;

        // For person sorts
        String list = ACCEPTED_LIST_PARAMETERS.get(0);
        String field = ACCEPTED_FIELD_PARAMETERS.get(0);
        String order = ACCEPTED_ORDER_PARAMETERS.get(0);

        expectedCommand = new SortCommand(list, field, order);
        actualCommand = parser.parse(list + " " + field + " " + order);
        assertEquals(true, expectedCommand.sameCommandAs(actualCommand));

        // For task sorts
        list = ACCEPTED_LIST_PARAMETERS.get(1);
        field = ACCEPTED_FIELD_PARAMETERS.get(5);
        order = ACCEPTED_ORDER_PARAMETERS.get(1);

        expectedCommand = new SortCommand(list, field, order);
        actualCommand = parser.parse(list + " " + field + " " + order);
        assertEquals(true, expectedCommand.sameCommandAs(actualCommand));
    }
```
###### /java/seedu/address/logic/parser/AddressBookParserTest.java
``` java
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

```
###### /java/seedu/address/logic/commands/AddCommandTest.java
``` java
        public void increaseFontSize() {
            fail("This method should not be called.");
        }

        public void decreaseFontSize() {
            fail("This method should not be called.");
        }

        public void resetFontSize() {
            fail("This method should not be called.");
        }

        public void setMainWindow(MainWindow mainWindow) {
            fail("This method should not be called.");
        }
```
###### /java/seedu/address/model/UniqueTaskListTest.java
``` java
public class UniqueTaskListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        UniqueTaskList uniquePersonList = new UniqueTaskList();
        thrown.expect(UnsupportedOperationException.class);
        uniquePersonList.asObservableList().remove(0);
    }

    @Test
    public void sortTasks_byDeadline_bothOrders() {
        // Set up expected result
        List<ReadOnlyTask> taskList = TypicalTasks.getTypicalTasks();
        Comparator<ReadOnlyTask> deadlineComparator = new Comparator<ReadOnlyTask>() {
            @Override
            public int compare(ReadOnlyTask o1, ReadOnlyTask o2) {
                return o1.getDeadline().date.compareTo(o2.getDeadline().date);
            }
        };
        Collections.sort(taskList, deadlineComparator);
        ObservableList<ReadOnlyTask> expectedTaskList = FXCollections.observableList(taskList);

        // Set up actual result
        AddressBook addressBook = TypicalTasks.getTypicalTasksOnlyAddressBook();
        addressBook.sortTasksBy("deadline", "asc");
        ObservableList<ReadOnlyTask> actualTaskList = addressBook.getTasksList();

        // Check ascending order sort
        assertEquals(expectedTaskList, actualTaskList);

        // Set up descending order sort
        Collections.sort(taskList, Collections.reverseOrder(deadlineComparator));
        expectedTaskList = FXCollections.observableList(taskList);
        addressBook.sortTasksBy("deadline", "desc");
        actualTaskList = addressBook.getTasksList();

        // Check descending order sort
        assertEquals(expectedTaskList, actualTaskList);
    }

    @Test
    public void sortTasks_byPriority_bothOrders() {
        // Set up expected result
        List<ReadOnlyTask> taskList = TypicalTasks.getTypicalTasks();
        Comparator<ReadOnlyTask> deadlineComparator = new Comparator<ReadOnlyTask>() {
            @Override
            public int compare(ReadOnlyTask o1, ReadOnlyTask o2) {
                return Integer.compare(o1.getPriority().value, o2.getPriority().value);
            }
        };
        Collections.sort(taskList, deadlineComparator);
        ObservableList<ReadOnlyTask> expectedTaskList = FXCollections.observableList(taskList);

        // Set up actual result
        AddressBook addressBook = TypicalTasks.getTypicalTasksOnlyAddressBook();
        addressBook.sortTasksBy("priority", "asc");
        ObservableList<ReadOnlyTask> actualTaskList = addressBook.getTasksList();

        // Check ascending order sort
        assertEquals(expectedTaskList, actualTaskList);

        // Set up descending order sort
        Collections.sort(taskList, Collections.reverseOrder(deadlineComparator));
        expectedTaskList = FXCollections.observableList(taskList);
        addressBook.sortTasksBy("priority", "desc");
        actualTaskList = addressBook.getTasksList();

        // Check descending order sort
        assertEquals(expectedTaskList, actualTaskList);
    }
}
```
###### /java/seedu/address/model/UniquePersonListTest.java
``` java
    @Test
    public void sortPerson_byName_bothOrders() {
        // Set up expected result
        List<ReadOnlyPerson> personList = TypicalPersons.getTypicalPersons();
        Comparator<ReadOnlyPerson> nameComparator = new Comparator<ReadOnlyPerson>() {
            @Override
            public int compare(ReadOnlyPerson o1, ReadOnlyPerson o2) {
                return o1.getName().fullName.compareTo(o2.getName().fullName);
            }
        };
        Collections.sort(personList, nameComparator);
        ObservableList<ReadOnlyPerson> expectedTaskList = FXCollections.observableList(personList);

        // Set up actual result
        AddressBook addressBook = TypicalPersons.getTypicalPersonsAddressBook();
        addressBook.sortPersonsBy("name", "asc");
        ObservableList<ReadOnlyPerson> actualTaskList = addressBook.getPersonList();

        // Check ascending order sort
        assertEquals(expectedTaskList, actualTaskList);

        // Set up descending order sort
        Collections.sort(personList, Collections.reverseOrder(nameComparator));
        expectedTaskList = FXCollections.observableList(personList);
        addressBook.sortPersonsBy("name", "desc");
        actualTaskList = addressBook.getPersonList();

        // Check descending order sort
        assertEquals(expectedTaskList, actualTaskList);
    }

    @Test
    public void sortPerson_byPhone_bothOrders() {
        // Set up expected result
        List<ReadOnlyPerson> personList = TypicalPersons.getTypicalPersons();
        Comparator<ReadOnlyPerson> phoneComparator = new Comparator<ReadOnlyPerson>() {
            @Override
            public int compare(ReadOnlyPerson o1, ReadOnlyPerson o2) {
                return o1.getPhone().value.compareTo(o2.getPhone().value);
            }
        };
        Collections.sort(personList, phoneComparator);
        ObservableList<ReadOnlyPerson> expectedTaskList = FXCollections.observableList(personList);

        // Set up actual result
        AddressBook addressBook = TypicalPersons.getTypicalPersonsAddressBook();
        addressBook.sortPersonsBy("phone", "asc");
        ObservableList<ReadOnlyPerson> actualTaskList = addressBook.getPersonList();

        // Check ascending order sort
        assertEquals(expectedTaskList, actualTaskList);

        // Set up descending order sort
        Collections.sort(personList, Collections.reverseOrder(phoneComparator));
        expectedTaskList = FXCollections.observableList(personList);
        addressBook.sortPersonsBy("phone", "desc");
        actualTaskList = addressBook.getPersonList();

        // Check descending order sort
        assertEquals(expectedTaskList, actualTaskList);
    }

    @Test
    public void sortPerson_byEmail_bothOrders() {
        // Set up expected result
        List<ReadOnlyPerson> personList = TypicalPersons.getTypicalPersons();
        Comparator<ReadOnlyPerson> emailComparator = new Comparator<ReadOnlyPerson>() {
            @Override
            public int compare(ReadOnlyPerson o1, ReadOnlyPerson o2) {
                return o1.getEmail().value.compareTo(o2.getEmail().value);
            }
        };
        Collections.sort(personList, emailComparator);
        ObservableList<ReadOnlyPerson> expectedTaskList = FXCollections.observableList(personList);

        // Set up actual result
        AddressBook addressBook = TypicalPersons.getTypicalPersonsAddressBook();
        addressBook.sortPersonsBy("email", "asc");
        ObservableList<ReadOnlyPerson> actualTaskList = addressBook.getPersonList();

        // Check ascending order sort
        assertEquals(expectedTaskList, actualTaskList);

        // Set up descending order sort
        Collections.sort(personList, Collections.reverseOrder(emailComparator));
        expectedTaskList = FXCollections.observableList(personList);
        addressBook.sortPersonsBy("email", "desc");
        actualTaskList = addressBook.getPersonList();

        // Check descending order sort
        assertEquals(expectedTaskList, actualTaskList);
    }

    @Test
    public void sortPerson_byAddress_bothOrders() {
        // Set up expected result
        List<ReadOnlyPerson> personList = TypicalPersons.getTypicalPersons();
        Comparator<ReadOnlyPerson> addressComparator = new Comparator<ReadOnlyPerson>() {
            @Override
            public int compare(ReadOnlyPerson o1, ReadOnlyPerson o2) {
                return o1.getAddress().value.compareTo(o2.getAddress().value);
            }
        };
        Collections.sort(personList, addressComparator);
        ObservableList<ReadOnlyPerson> expectedTaskList = FXCollections.observableList(personList);

        // Set up actual result
        AddressBook addressBook = TypicalPersons.getTypicalPersonsAddressBook();
        addressBook.sortPersonsBy("address", "asc");
        ObservableList<ReadOnlyPerson> actualTaskList = addressBook.getPersonList();

        // Check ascending order sort
        assertEquals(expectedTaskList, actualTaskList);

        // Set up descending order sort
        Collections.sort(personList, Collections.reverseOrder(addressComparator));
        expectedTaskList = FXCollections.observableList(personList);
        addressBook.sortPersonsBy("address", "desc");
        actualTaskList = addressBook.getPersonList();

        // Check descending order sort
        assertEquals(expectedTaskList, actualTaskList);
    }
```
###### /java/seedu/address/testutil/EditPersonDescriptorBuilder.java
``` java
    /**
     * Sets the {@code Remark} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withRemark(String remark) {
        try {
            ParserUtil.parseRemark(Optional.of(remark)).ifPresent(descriptor::setRemark);
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("remark is expected to be unique.");
        }
        return this;
    }

    /**
     * Sets the {@code Avatar} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withAvatar(String avatar) {
        try {
            ParserUtil.parseAvatar(Optional.of(avatar)).ifPresent(descriptor::setAvatar);
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("avatar is expected to be unique.");
        }
        return this;
    }
    //@author

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditPersonDescriptor}
     * that we are building.
     */
    public EditPersonDescriptorBuilder withTags(String... tags) {
        try {
            descriptor.setTags(ParserUtil.parseTags(Arrays.asList(tags)));
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("tags are expected to be unique.");
        }
        return this;
    }
```
###### /java/seedu/address/testutil/PersonBuilder.java
``` java
    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withRemark(String remark) {
        try {
            this.person.setRemark(new Remark(remark));
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("Remark is invalid");
        }
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withAvatar(String filepath) {
        try {
            this.person.setAvatar(new Avatar(filepath));
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("Avatar is invalid");
        }
        return this;
    }
```
###### /java/guitests/guihandles/TaskCardHandle.java
``` java
public class TaskCardHandle extends NodeHandle<Node> {
    private static final String ID_FIELD_ID = "#id";
    private static final String TASK_NAME_ID = "#taskName";
    private static final String DESCRIPTION_ID = "#description";
    private static final String DEADLINE_ID = "#deadline";
    private static final String PRIORITY_ID = "#priority";

    private final Label idLabel;
    private final Label taskNameLabel;
    private final Label descriptionLabel;
    private final Label deadlineLabel;
    private final Label priorityLabel;

    public TaskCardHandle(Node cardNode) {
        super(cardNode);

        this.idLabel = getChildNode(ID_FIELD_ID);
        this.taskNameLabel = getChildNode(TASK_NAME_ID);
        this.descriptionLabel = getChildNode(DESCRIPTION_ID);
        this.priorityLabel = getChildNode(PRIORITY_ID);
        this.deadlineLabel = getChildNode(DEADLINE_ID);
    }

    public String getId() {
        return idLabel.getText();
    }

    public String getTaskName() {
        return taskNameLabel.getText();
    }

    public String getDescription() {
        return descriptionLabel.getText();
    }

    public String getDeadline() {
        return deadlineLabel.getText();
    }

    public String getPriority() {
        return priorityLabel.getText();
    }
}
```
###### /java/guitests/guihandles/TaskListPanelHandle.java
``` java
public class TaskListPanelHandle extends NodeHandle<ListView<TaskCard>> {
    public static final String TASK_LIST_VIEW_ID = "#taskListView";

    private Optional<TaskCard> lastRememberedSelectedTaskCard;

    public TaskListPanelHandle(ListView<TaskCard> taskListPanelNode) {
        super(taskListPanelNode);
    }

    /**
     * Returns a handle to the selected {@code TaskCardHandle}.
     * A maximum of 1 item can be selected at any time.
     * @throws AssertionError if no card is selected, or more than 1 card is selected.
     */
    public TaskCardHandle getHandleToSelectedCard() {
        List<TaskCard> taskList = getRootNode().getSelectionModel().getSelectedItems();

        if (taskList.size() != 1) {
            throw new AssertionError("Task list size expected 1.");
        }

        return new TaskCardHandle(taskList.get(0).getRoot());
    }

    /**
     * Returns the index of the selected card.
     */
    public int getSelectedCardIndex() {
        return getRootNode().getSelectionModel().getSelectedIndex();
    }

    /**
     * Returns true if a card is currently selected.
     */
    public boolean isAnyCardSelected() {
        List<TaskCard> selectedCardsList = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedCardsList.size() > 1) {
            throw new AssertionError("Card list size expected 0 or 1.");
        }

        return !selectedCardsList.isEmpty();
    }

    /**
     * Navigates the listview to display and select the task.
     */
    public void navigateToCard(ReadOnlyTask task) {
        List<TaskCard> cards = getRootNode().getItems();
        Optional<TaskCard> matchingCard = cards.stream().filter(card -> card.task.equals(task)).findFirst();

        if (!matchingCard.isPresent()) {
            throw new IllegalArgumentException("Task does not exist.");
        }

        guiRobot.interact(() -> {
            getRootNode().scrollTo(matchingCard.get());
            getRootNode().getSelectionModel().select(matchingCard.get());
        });
        guiRobot.pauseForHuman();
    }

    /**
     * Returns the task card handle of a task associated with the {@code index} in the list.
     */
    public TaskCardHandle getTaskCardHandle(int index) {
        return getTaskCardHandle(getRootNode().getItems().get(index).task);
    }

    /**
     * Returns the {@code TaskCardHandle} of the specified {@code task} in the list.
     */
    public TaskCardHandle getTaskCardHandle(ReadOnlyTask task) {
        Optional<TaskCardHandle> handle = getRootNode().getItems().stream()
                .filter(card -> card.task.equals(task))
                .map(card -> new TaskCardHandle(card.getRoot()))
                .findFirst();
        return handle.orElseThrow(() -> new IllegalArgumentException("Task does not exist."));
    }

    /**
     * Selects the {@code TaskCard} at {@code index} in the list.
     */
    public void select(int index) {
        getRootNode().getSelectionModel().select(index);
    }

    /**
     * Remembers the selected {@code TaskCard} in the list.
     */
    public void rememberSelectedTaskCard() {
        List<TaskCard> selectedItems = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            lastRememberedSelectedTaskCard = Optional.empty();
        } else {
            lastRememberedSelectedTaskCard = Optional.of(selectedItems.get(0));
        }
    }

    /**
     * Returns true if the selected {@code TaskCard} is different from the value remembered by the most recent
     * {@code rememberSelectedTaskCard()} call.
     */
    public boolean isSelectedTaskCardChanged() {
        List<TaskCard> selectedItems = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            return lastRememberedSelectedTaskCard.isPresent();
        } else {
            return !lastRememberedSelectedTaskCard.isPresent()
                    || !lastRememberedSelectedTaskCard.get().equals(selectedItems.get(0));
        }
    }

    /**
     * Returns the size of the list.
     */
    public int getListSize() {
        return getRootNode().getItems().size();
    }
}
```

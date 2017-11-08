package seedu.address.ui;

import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * Contains the details of a person.
 */
public class ViewPersonPanel extends UiPart<Region> {
    //@@author charlesgoh
    public static final int DEFAULT_NAME_SIZE = 15;
    public static final int DEFAULT_ATTRIBUTE_SIZE = 10;
    public static final int FONT_SIZE_EXTENDER = 5;
    public static final int DEFAULT_FONT_SIZE_MULTIPLIER = 0;
    //@@author
    private static final String FXML = "ViewPersonPanel.fxml";
    /**
     * Preset values for random selection later.
     */
    private enum Colours {
        blue, green, brown, purple, navy, crimson, firebrick, maroon, red, black
    }
    private static HashMap<String, String> colourHash = new HashMap<String, String>();
    private static Random randomNumber = new Random();
    private static int nameSize = DEFAULT_NAME_SIZE;
    private static int attributeSize = DEFAULT_ATTRIBUTE_SIZE;

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    private ReadOnlyPerson person;
    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private VBox personPanel;
    @FXML
    private Label name;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label remark;
    @FXML
    private ImageView avatarImage;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;

    private int fontSizeMultipler;
    public ViewPersonPanel(ReadOnlyPerson person, int fontSizeMultiplier) {
        super(FXML);
        this.person = person;
        this.fontSizeMultipler = fontSizeMultiplier;
        initTags(person);
        initializeWithPerson(person);
        initializeAvatar();
        updateAttributeSizes();
        registerAsAnEventHandler(this);
    }

    public ViewPersonPanel() {
        super(FXML);
        registerAsAnEventHandler(this);
    }

    /**
     * Binds the individual UI elements to observe their respective {@code Person} properties
     * so that they will be notified of any changes.
     */
    private void initializeWithPerson(ReadOnlyPerson person) {
        name.textProperty().bind(Bindings.convert(person.nameProperty()));
        phone.textProperty().bind(Bindings.convert(person.phoneProperty()));
        address.textProperty().bind(Bindings.convert(person.addressProperty()));
        remark.textProperty().bind(Bindings.convert(person.remarkProperty()));
        email.textProperty().bind(Bindings.convert(person.emailProperty()));
        person.tagProperty().addListener((observable, oldValue, newValue) -> {
            tags.getChildren().clear();
            initTags(person);
        });
    }
    //author charlesgoh
    /**
     * Sets avatar to a filepath or the avatar placeholder by default
     */
    private void initializeAvatar() {
        try {
            String avatarPath = person.getAvatar().value;
            if (!avatarPath.equals("")) {
                logger.info("Initializing avatar to image at saved URL");
                Image newImage = new Image(avatarPath);
                avatarImage.setImage(newImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //author
    //@@author wangyiming1019
    /**
     * Locate hashed colour for tag. If not found, new colour is assigned to tag
     * @param tag
     * @return
     */
    private String getTagColour(String tag) {
        if (!colourHash.containsKey(tag)) {
            int randomiser = randomNumber.nextInt(ViewPersonPanel.Colours.values().length);
            String colour = ViewPersonPanel.Colours.values()[randomiser].toString();
            colourHash.put(tag, colour);
        }
        return colourHash.get(tag);
    }

    /**
     * Assigns each tag a colour
     * @param person
     */
    private void initTags(ReadOnlyPerson person) {
        person.getTags().forEach(tag -> {
            Label newTagLabel = new Label(tag.getTagName());

            newTagLabel.setStyle("-fx-background-color: " + this.getTagColour(tag.getTagName()));

            tags.getChildren().add(newTagLabel);
        });
    }
    //@@author
    //@@author charlesgoh
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
    //@@author


    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ViewPersonPanel)) {
            return false;
        }

        // state check
        ViewPersonPanel card = (ViewPersonPanel) other;
        return person.equals(card.person);
    }

    public Label getName() {
        return name;
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

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        this.person = event.getNewSelection().person;
        initializeWithPerson(person);
        initializeAvatar();
    }
}

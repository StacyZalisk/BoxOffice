/**
 * -----BoxOffice Media Rentals: Kiosk Software
 * -----BoxOffice Media Description Page
 * @author      Stacy Zalisk
 * @date        4/30/2017
 * @version     1.9.5
 * Purpose:     Shows the description for whatever media item is selected from
 *              the browse screen
 * Revisions:   Removed rent window variable.  User must be logged in to add to the cart
 */
package BoxOffice;

//Imports
//-----Graphic Imports
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

public class BoxOffice_Media_Description {
    
    /**
     * Method getScene returns the page showing all descriptive information for the BoxOffice_Media_Item item
     * @param item the media item that the page should be loaded from
     * @return Scene the scene displaying the description of item
     */
    public Scene getScene(BoxOffice_Media_Item item) {
        //If the scen hasn't been created, create it
        if(scene == null)
            createScene();
        
        //Update the menu
        menu.updateMenu();
        
        //Store item information for navigation purposes
        this.item = item;
        
        //Load new graphics for new media item
        loadMediaItem();
        
        return scene;
    }
    
    /**
     * Method createScene generates the Media Description page from scratch
     * @return none
     */
    private void createScene() {       
        //Initialize graphic items
        //-----Poster for the media item (mediaImage)
        mediaImage = new ImageView();
        mediaImage.setFitWidth(337);
        mediaImage.setPreserveRatio(true);
        //-----Parental rating image (generalRating)
        generalRating = new ImageView();
        generalRating.setPreserveRatio(true);
        //-----User rating stars (star1 - star5)
        star1 = new ImageView();
        star2 = new ImageView();
        star3 = new ImageView();
        star4 = new ImageView();
        star5 = new ImageView();
        
        //Title of media item (title)
        title = new Label();
        title.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 65));
        title.setTextFill(Color.web("#6CA510"));
        title.setWrapText(true);
        
        //Artist of media item (artist)
        artist = new Label();
        artist.setFont(Font.font("Trebuchet MS", FontPosture.ITALIC, 25));
        artist.setPadding(new Insets(0, 0, 20, 0));
        artist.setWrapText(true);
        
        //Description of media item (description)
        description = new Label();
        description.setFont(Font.font("Trebuchet MS", 20));
        description.setPadding(new Insets(20, 0, 0, 0));
        description.setWrapText(true);
        description.setMinWidth(500);
        
        //Rental Buttons
        //-----Add to cart button (cartButton
        cartButton = new ImageButton("images/cartBtnReg.png", "images/cartBtnHigh.png");
        cartButton.buttonImage.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            if(BoxOffice_Account.isUserLoggedIn()) {
                if(item.inCart) {
                    cartConfirmation.setTextFill(Color.RED);
                    cartConfirmation.setText(item.title + " is already in the cart");
                    cartConfirmation.setVisible(true);
                }
                else if(BoxOffice_Account.checkIfRented(item.mediaID)) {
                    cartConfirmation.setTextFill(Color.RED);
                    cartConfirmation.setText("You have already rented " + item.title);
                    cartConfirmation.setVisible(true);
                }
                else {
                    BoxOffice_Cart.addToCart(item);
                    cartConfirmation.setTextFill(Color.web("#6CA510"));
                    cartConfirmation.setText(item.title + " added to the cart");
                    cartConfirmation.setVisible(true);
                    menu.updateCartCount();
                }
            }
            else
                menu.loginWindow.showLoginWindow(menu, "You Must Be Logged in to Add to Your Cart");
        });

        //Box office menu
        menu = new BoxOffice_Menu();
        //-----Menu Button
        menu.homeBtn.buttonImage.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            BoxOffice_Main.switchScene("browse", "description", item);
        });
        //-----Account Button
        menu.account.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            if(BoxOffice_Account.isUserLoggedIn())
                BoxOffice_Main.switchScene("account", "browse");
            else
                menu.loginWindow.showLoginWindow(menu, "You Must Be Logged in to Access Your Account");            
        });
        //-----Cart Button
        menu.cartBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            BoxOffice_Main.switchScene("cart", "description", item);
        });
        
        //Pane to hold the user star rating (starRating)
        starRating = new HBox();
        starRating.getChildren().add(star1);
        starRating.getChildren().add(star2);
        starRating.getChildren().add(star3);
        starRating.getChildren().add(star4);
        starRating.getChildren().add(star5);
        
        //Pane to hold all the rating information (ratings)
        ratings = new HBox(10);
        ratings.setAlignment(Pos.CENTER_LEFT);
        ratings.getChildren().add(starRating);
        ratings.getChildren().add(generalRating);
        
        //Label to hold the genre of the meida
        genre = new Label();
        genre.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 30));
        genre.setPadding(new Insets(0, 0, 10, 0));
        
        //Label to confirm item was added to cart
        cartConfirmation = new Label();
        cartConfirmation.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 30));
        cartConfirmation.setTextFill(Color.web("#6CA510"));
        cartConfirmation.setPadding(new Insets(20, 0, 0, 0));
        cartConfirmation.setWrapText(true);
        
        //Pane to hold the rental buttons (rentButtons)
        rentButtons = new HBox(5);
        rentButtons.setPadding(new Insets(20, 0, 0, 0));
        rentButtons.getChildren().add(cartButton);
        
        //Pane to hold all descriptive items (itemInfo)
        itemInfo = new VBox();
        itemInfo.setPadding(new Insets(10, 10, 10, 20));
        itemInfo.getChildren().add(title);
        itemInfo.getChildren().add(genre);
        itemInfo.getChildren().add(artist);
        itemInfo.getChildren().add(ratings);
        itemInfo.getChildren().add(description);
        itemInfo.getChildren().add(rentButtons);
        itemInfo.getChildren().add(cartConfirmation);
        
        //Pane to hold the poster of the media (mediaImagePane)
        mediaImagePane = new StackPane();
        mediaImagePane.setPadding(new Insets(10, 10, 10, 10));
        mediaImagePane.setStyle("-fx-background-color: BLACK;");
        mediaImagePane.getChildren().add(mediaImage);
        
        //Pane to hold image and description (mediaContent)
        mediaContent = new HBox();
        mediaContent.setPadding(new Insets(10, 10, 10, 30));
        mediaContent.getChildren().add(mediaImagePane);
        mediaContent.getChildren().add(itemInfo);        

        //Pane to hold all items (mainPane)
        mainPane = new VBox();
        mainPane.getChildren().add(menu.getMenu());
        mainPane.getChildren().add(mediaContent);
        
        //Scene to be displayed (scene)
        scene = new Scene(mainPane, BoxOffice_Main.getScreenWidth(), BoxOffice_Main.getScreenHeight());
    }
    
    /**
     * Method loadMediaItem() updates the contents of the pane
     * @return none
     */
    private void loadMediaItem() {
        Image image = new Image(item.imageURL);
        //Load main image
        //If the media is not a CD
        if(!(item.mediaType.equals("music"))) {
            mediaImage.setImage(image);
            mediaImagePane.setMaxSize(mediaImage.getFitHeight(), mediaImage.getFitWidth());
        }
        //If the media is a CD
        else {
            mediaImage.setImage(new Image(item.imageURL));
            mediaImagePane.setMaxSize(mediaImage.getFitHeight(), mediaImage.getFitWidth());
        }

        //Set up 5 star rating
        double star = item.starRating;
        initializeStar(star1, star);
        star -= 1.0;
        initializeStar(star2, star);
        star -= 1.0;
        initializeStar(star3, star);
        star -= 1.0;
        initializeStar(star4, star);
        star -= 1.0;
        initializeStar(star5, star);
        star -= 1.0;
        
        //Set parental rating
        switch (item.mediaType) {
            //-----Music rating
            case "Music":
                if(!(item.rating == null)) {
                    image = new Image("images/ratingPA.jpg");
                    generalRating.setFitHeight(75);
                    generalRating.setImage(image);
                    generalRating.setVisible(true);
                }
                else
                    generalRating.setVisible(false);
                break;
            //-----Movie rating
            case "Video":
                switch (item.rating) {
                    case "G":
                        image = new Image("images/ratingG.png");
                        generalRating.setImage(image);
                        generalRating.setVisible(true);
                        break;
                    case "PG":
                        image = new Image("images/ratingPG.png");
                        generalRating.setImage(image);
                        generalRating.setVisible(true);
                        break;
                    case "PG-13":
                        image = new Image("images/ratingPG13.png");
                        generalRating.setImage(image);
                        generalRating.setVisible(true);
                        break;
                    case "R":
                        image = new Image("images/ratingR.png");
                        generalRating.setImage(image);
                        generalRating.setVisible(true);
                        break;
                    default:
                        break;
                }
                generalRating.setFitHeight(50);
                break;
            //-----Game rating
            case "Game":
                switch (item.rating) {
                    case "C":
                        image = new Image("images/ratingC.png");
                        generalRating.setImage(image);
                        generalRating.setVisible(true);
                        break;
                    case "E":
                        image = new Image("images/ratingE.png");
                        generalRating.setImage(image);
                        generalRating.setVisible(true);
                        break;
                    case "E 10+":
                        image = new Image("images/ratingE10.png");
                        generalRating.setImage(image);
                        generalRating.setVisible(true);
                        break;
                    case "T":
                        image = new Image("images/ratingT.png");
                        generalRating.setImage(image);
                        generalRating.setVisible(true);
                        break;
                    case "M":
                        image = new Image("images/ratingM.png");
                        generalRating.setImage(image);
                        generalRating.setVisible(true);
                        break;
                    default:
                        break;
                }
                generalRating.setFitHeight(75);
                break;
            default:
                break;
        }
        
        //Text labels
        //-----Title of media
        title.setText(item.title);
        //-----Artist of media
        artist.setText(item.artist);
        //-----Description of media
        description.setText(item.description);
        //-----Cart confirmation
        cartConfirmation.setText(item.title + " added to the cart");
        cartConfirmation.setVisible(false);
        //-----Genre
        if(!(item.genre3 == null)) {
            genre.setText(item.genre1 + " - " + item.genre2 + " - " + item.genre3);
        }
        else if(!(item.genre2 == null)) {
            genre.setText(item.genre1 + " - " + item.genre2);
        }
        else {
            genre.setText(item.genre1);
        }
        
        //-----Check if the item is in stock
        if(BoxOffice_Kiosk_Data.checkStock(item.mediaID)) {
            cartButton.setVisible(true);
        }
        else {
            cartButton.setVisible(false);
            cartConfirmation.setTextFill(Color.RED);
            cartConfirmation.setText(item.title + " is out of stock!");
            cartConfirmation.setVisible(true);
        }
    }
    
    /**
     * Method initializeStar() initializes the five star rating of the media item
     * @param currentStar the star image to be updated
     * @param starVal the floating point value of the star to be loaded
     * @return none
     */
    private void initializeStar(ImageView currentStar, double starVal) {
        Image image;
        if(starVal >=  0.875)       //full star
            image = new Image("images/100Star.png");
        else if(starVal >= 0.625)   //75% star
            image = new Image("images/75Star.png");
        else if(starVal >= 0.375)   //50% star
            image = new Image("images/50Star.png");
        else if(starVal > 0)        //25% star
            image = new Image("images/25Star.png");
        else                        //Empty star
            image = new Image("images/0Star.png");
        
       currentStar.setImage(image);
    }
    
    //Variables
    //-----VBoxes
    private VBox mainPane; //Pane that holds everything
    private VBox itemInfo; //Description of media item
    //-----Menu
    private BoxOffice_Menu menu;
    //-----HBoxes
    private HBox mediaContent; //Main area below header
    private HBox rentButtons; //Holds the two rental buttons and confirmation message
    private HBox ratings; //Star and MPAA rating
    private HBox starRating; //Displays the rating system out of five star
    //-----Stack panes
    private StackPane mediaImagePane; //Special pane to hold the media's image
    //-----Scenes
    private Scene scene;
    //-----Image views
    private ImageView mediaImage; //Media image
    private ImageView star1, star2, star3, star4, star5; // Stars for the five star rating
    private ImageView generalRating;
    //-----Labels
    private Label title; //Title of media
    private Label artist; //Artist of media
    private Label description; //Description of media item
    private Label genre; //Genre labels
    private Label cartConfirmation; //Label to confirm item is added to the cart
    //-----Image Buttons
    private ImageButton cartButton; //Cart button
    //-----ID elements
    private String pageID = "description";
    private BoxOffice_Media_Item item;
}

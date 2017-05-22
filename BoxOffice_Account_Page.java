/**
 * -----BoxOffice Media Rentals: Kiosk Software
 * -----BoxOffice Account Page
 * @author      Stacy Zalisk
 * @date        4/25/2017
 * @version     1.3.3
 * Purpose:     Shows account information for the user that is signed into the
 *              kiosk
 * Revisions:   Overdue items are highlighted in red
 */

package BoxOffice;

//Imports
//-----Graphic Imports
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class BoxOffice_Account_Page {
    
    /**
     * Method getScene generates and returns the BoxOffice_Account_Page scene
     * @return scene The scene that contains the BoxOffice_Account_Page
     */
    public Scene getScene() {
        //If the scene doesn't exist, create it
        if(scene == null)
            createScene();
        
        if(!lastUser.equals(BoxOffice_Account.getUserName()))
            newUser();
        
        //Update the menu
        menu.updateMenu();
        
        //Update page contents
        loadContent();
        
        //Return the scene
        return scene;
    }
    
    /**
     * Method createScene creates the BoxOffice_Account_Page scene from scratch
     */
    private void createScene() {
        //Menu
        menu = new BoxOffice_Menu();
        menu.homeBtn.buttonImage.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            BoxOffice_Main.switchScene("browse", "account");
        });
        //-----Cart Button
        menu.cartBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            BoxOffice_Main.switchScene("cart", "account");
        });
        
        //Main Content
        //-----Page title label
        title = new Label();
        title.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 60));
        title.setTextFill(Color.web("#6CA510"));
        title.setPadding(new Insets(0, 0, 0, 50));
        //-----No rental label
        emptyLabel = new Label("You Have No Rented Media");
        emptyLabel.setFont(Font.font("Trebuchet MS", 50));
        emptyLabel.setTextFill(Color.web("#6CA510"));
        emptyLabel.setPadding(new Insets(30, 0, 0, 50));
        
        //Kiosk Inventory
        //-----Initial Pane Generation
        //----------Pane to hold overdue items
        overduePane = new FlowPane(Orientation.HORIZONTAL, 20, 50);
        overduePane.setMinWidth(BoxOffice_Main.getScreenWidth() - 50);
        overduePane.setMaxWidth(BoxOffice_Main.getScreenWidth() - 50);
        overduePane.setPadding(new Insets(0, 100, 0, 100));
        overduePane.setRowValignment(VPos.TOP);
        //----------Pane to hold returnable items
        returnablePane = new FlowPane(Orientation.HORIZONTAL, 20, 50);
        returnablePane.setMinWidth(BoxOffice_Main.getScreenWidth() - 50);
        returnablePane.setMaxWidth(BoxOffice_Main.getScreenWidth() - 50);
        returnablePane.setPadding(new Insets(0, 100, 0, 100));
        returnablePane.setRowValignment(VPos.TOP);
        //----------Pane to hold game items (gamePane)
        otherPane = new FlowPane(Orientation.HORIZONTAL, 20, 50);
        otherPane.setMinWidth(BoxOffice_Main.getScreenWidth() - 50);
        otherPane.setMaxWidth(BoxOffice_Main.getScreenWidth() - 50);
        otherPane.setPadding(new Insets(0, 100, 0, 100));
        otherPane.setRowValignment(VPos.TOP);
        
        //Heading Labels
        //-----Label for overdue media
        overdueLabel = new Label("Overdue Media");
        overdueLabel.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 40));
        overdueLabel.setTextFill(Color.RED);
        //-----Label for returnable media
        returnableLabel = new Label("Returnable Media");
        returnableLabel.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 40));
        returnableLabel.setTextFill(Color.web("#6CA510"));
        //-----Lable for other rented media
        otherLabel = new Label("Other Rentals");
        otherLabel.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 40));
        otherLabel.setTextFill(Color.web("#6CA510"));     
        
        //Set up section headers
        //-----Overdue header
        //----------Horizontal rule to act as a barrier between sections
        overdueBreak = new Pane();
        overdueBreak.setMinHeight(2);
        overdueBreak.setMaxWidth(BoxOffice_Main.getScreenWidth() - 200);
        overdueBreak.setStyle("-fx-background-color: #FF0000");
        //----------Pane to contain entire movie header (movieHead)
        overdueHead = new BorderPane();
        overdueHead.setPadding(new Insets(0, 100, 0, 100));
        overdueHead.setLeft(overdueLabel);
        overdueHead.setBottom(overdueBreak);
        
        //-----Returnable header
        //----------Horizontal rule to act as a barrier between sections
        returnableBreak = new Pane();
        returnableBreak.setMinHeight(2);
        returnableBreak.setMaxWidth(BoxOffice_Main.getScreenWidth() - 200);
        returnableBreak.setStyle("-fx-background-color: #6CA510");
        //----------Pane to contain entire music header (musicHead)
        returnableHead = new BorderPane();
        returnableHead.setPadding(new Insets(0, 100, 0, 100));
        returnableHead.setLeft(returnableLabel);
        returnableHead.setBottom(returnableBreak);
        
        //-----Other media header
        //----------Horizontal rule to act as a barrier between sections
        otherBreak = new Pane();
        otherBreak.setMinHeight(2);
        otherBreak.setMaxWidth(BoxOffice_Main.getScreenWidth() - 200);
        otherBreak.setStyle("-fx-background-color: #6CA510");
        //----------Pane to contain entire other media header
        otherHead = new BorderPane();
        otherHead.setPadding(new Insets(0, 100, 0, 100));
        otherHead.setLeft(otherLabel);
        otherHead.setBottom(otherBreak);
        
        //-----User pane to hold all information
        userPane = new VBox(5);
        
        //Content Pane to hold averything except menu
        contentPane = new ScrollPane(userPane);
        
        //Main Pane to hold everything
        mainPane = new VBox(5);
        mainPane.getChildren().add(menu.getMenu());
        mainPane.getChildren().add(contentPane);
        
        //Create the Scene
        scene = new Scene(mainPane, BoxOffice_Main.getScreenWidth(), BoxOffice_Main.getScreenHeight());
    }
    
    /**
     * Method loadContent loads the user account information into the page
     */
    private void loadContent() {             
        //Load user information
        title.setText("Welcome " + BoxOffice_Account.getUserName());
        
        userPane.getChildren().clear();
        userPane.getChildren().add(title);
        
        updateRentedItems();
        
        if(overduePane.getChildren().isEmpty() && returnablePane.getChildren().isEmpty() && otherPane.getChildren().isEmpty()) {
            userPane.getChildren().add(emptyLabel);
        }
        else {
            //Show overdue items
            if(!overduePane.getChildren().isEmpty()) {
                userPane.getChildren().add(overdueHead);
                userPane.getChildren().add(overduePane);
            }

            //Show returnable items
            if(!returnablePane.getChildren().isEmpty()) {
                userPane.getChildren().add(returnableHead);
                userPane.getChildren().add(returnablePane);
            }

            //Show other rented items
            if(!otherPane.getChildren().isEmpty()) {
                userPane.getChildren().add(otherHead);
                userPane.getChildren().add(otherPane);
            }
        }
    }
    
    /**
     * Method updateRentedItems updates what rented items are displayed on the account page.
     */
    private void updateRentedItems() {
        //Temporary variables
        Image image;
        ImageView mediaIcon;
        StackPane imagePane;
        ImageButton imageButton;
        VBox holdingPane;
        
        //Clear old data
        overduePane.getChildren().clear();
        returnablePane.getChildren().clear();
        otherPane.getChildren().clear();
        
        //Get rented items from account
        mediaItems = BoxOffice_Account.getRentedItems();
        
        //Go through list and store the items in their corresponding categories
        for(BoxOffice_Rental_Item item: mediaItems) {
            //Panes
            holdingPane = new VBox(5);
            holdingPane.setAlignment(Pos.CENTER);
            imagePane = new StackPane();

            //Image
            image = new Image(item.imageURL);
            mediaIcon = new ImageView(image);
            if(item.mediaType.equals("Music")) {
                mediaIcon.setFitWidth(150);
                mediaIcon.setFitHeight(150);
            }
            else {
                mediaIcon.setFitWidth(150);
                mediaIcon.setFitHeight(225);
            }
            imagePane.getChildren().add(mediaIcon);
            imagePane.setPadding(new Insets(3, 3, 3, 3));
            
            //Item is overdue and returnable
            if(item.overdue && item.returnable) {
                //Return button
                imageButton = new ImageButton("images/returnOverdueBtnReg.png", "images/returnOverdueBtnHigh.png");
                imageButton.buttonImage.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                    BoxOffice_Kiosk_Data.returnItem(item.barcode);
                    loadContent();
                });

                //Pane to hold the image and button
                holdingPane.getChildren().add(imagePane);
                holdingPane.getChildren().add(imageButton);
                
                //Mark item as overdue
                imagePane.setStyle("-fx-background-color: #FF0000");
                overduePane.getChildren().add(holdingPane);
            }
            //Item is not overdue but is returnable
            else if(!item.overdue && item.returnable) {
                //Return button
                imageButton = new ImageButton("images/returnBtnReg.png", "images/returnBtnHigh.png");
                imageButton.buttonImage.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                    BoxOffice_Kiosk_Data.returnItem(item.barcode);
                    loadContent();
                });

                //Pane to hold the image and button
                holdingPane.getChildren().add(imagePane);
                holdingPane.getChildren().add(imageButton);
                
                //Mark item as returnable
                imagePane.setStyle("-fx-background-color: #6CA510");
                returnablePane.getChildren().add(holdingPane);
            }
            //Item is not returnable
            else {
                holdingPane.getChildren().add(imagePane);
                otherPane.getChildren().add(holdingPane);
            }
        }
    }
    
    /**
     * Method newUser loads relevant information if a new user logs in
     */
    private void newUser() {        
        //Record the current user
        lastUser = BoxOffice_Account.getUserName();
    }
    
    //Variables
    //-----Scenes
    private Scene scene; //The main scene
    //-----Menu
    private BoxOffice_Menu menu; //Top menu
    //-----VBoxes
    private VBox mainPane; //Pane that holds everything;
    private VBox userPane; //Pane to hold the user content of the page
    //-----Scroll Panes
    private ScrollPane contentPane; //Pane that hold the main content of the page
    //-----Labels
    private Label title; //Label to hold the title of the page
    private Label emptyLabel; //Label to display if the user has no item rented
    private Label overdueLabel, returnableLabel, otherLabel; //Labels for the media sections
    //-----Flow panes
    private FlowPane overduePane, returnablePane, otherPane; //Panes to hold what the user has rented
    //-----Panes
    private Pane overdueBreak, returnableBreak, otherBreak; //Horizontal rules for the media sections
    //-----Border Panes
    private BorderPane overdueHead, returnableHead, otherHead; //Panes for the header for the media sections
    //-----Array Lists
    private ArrayList<BoxOffice_Rental_Item> mediaItems = new ArrayList(); //All the rented items
    //-----String
    private String lastUser = "";
}

/**
 * -----BoxOffice Media Rentals: Kiosk Software
 * -----BoxOffice Cart Page
 * @author      Stacy Zalisk
 * @date        5/07/2017
 * @version     1.3.6
 * Purpose:     A page to show users what is stored in their cart, manipulate their cart,
 *              and rent all items in their cart
 * Revisions:   Added call to update the menu upon a successful rental
 */
package BoxOffice;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class BoxOffice_Cart_Page {
    
    /**
     * Method getScene() creates and returns the BoxOffice_Cart_Page
     * @return the Cart_page scene
     */
    public Scene getScene() {
        //Create the scene if not already existing
        if (scene == null)
            createScene();
        
        //Load the contents of the cart
        loadContent();
        menu.updateMenu();
        
        //Return the completed scene
        return scene;
    }
    
    /**
     * Method createScene() creates the BoxOffice_Cart_Page from scratch
     */
    private void createScene() {
        //Menu
        menu = new BoxOffice_Menu();
        //-----Home Button
        menu.homeBtn.buttonImage.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
          BoxOffice_Main.switchScene("browse", "cart");
        });
        //-----Account Button
        menu.account.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            if(BoxOffice_Account.isUserLoggedIn())
                BoxOffice_Main.switchScene("account", "cart");
            else
                menu.loginWindow.showLoginWindow(menu, "You Must Be Logged in to Access Your Account");            
        });
        
        //-----Rental window
        rentalWindow = new BoxOffice_Rent_PopUp();
        rentalWindow.createRentalPopUp();
        
        //Page Header
        header = new Label("Your Cart");
        header.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 40));
        header.setTextFill(Color.web("#6CA510"));
        
        //Empty cart label
        nothing = new Label("Your Cart is Empty");
        nothing.setFont(Font.font("Trebuchet MS", FontPosture.ITALIC, 35));
        
        //Price Disclosure statement
        overdueDisclosure = new Label("*There is a $1.00 charge per week for each overdue item");
        overdueDisclosure.setFont(Font.font("Trebuchet MS", FontPosture.ITALIC, 20));
        overdueDisclosure.setTextFill(Color.RED);
        
        //"Table"
        table = new VBox(0);
        
        //Table Columns
        movieColumns = new HBox(10);
        musicColumns = new HBox(10);
        gameColumns = new HBox(10);
        bookColumns = new HBox(10);
        createColumnHead(movieColumns);
        createColumnHead(gameColumns);
        createColumnHead(musicColumns);
        createColumnHead(bookColumns);
        
        //Table Headings
        //-----Movies
        Label tempLabel = new Label("Movies");
        tempLabel.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 25));
        tempLabel.setStyle("-fx-background-color: #6ca510");
        tempLabel.setPadding(new Insets(10, 0, 10, 0));
        tempLabel.setMinWidth(BoxOffice_Main.getScreenWidth() - 200);
        tempLabel.setAlignment(Pos.CENTER);
        movieHeader = new VBox();
        movieHeader.getChildren().add(tempLabel);
        movieHeader.getChildren().add(movieColumns);
        //-----Games
        tempLabel = new Label("Video Games");
        tempLabel.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 25));
        tempLabel.setStyle("-fx-background-color: #6ca510");
        tempLabel.setPadding(new Insets(10, 0, 10, 0));
        tempLabel.setMinWidth(BoxOffice_Main.getScreenWidth() - 200);
        tempLabel.setAlignment(Pos.CENTER);
        gameHeader = new VBox();
        gameHeader.getChildren().add(tempLabel);
        gameHeader.getChildren().add(gameColumns);
        //-----Music
        tempLabel = new Label("Music");
        tempLabel.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 25));
        tempLabel.setStyle("-fx-background-color: #6ca510");
        tempLabel.setPadding(new Insets(10, 0, 10, 0));
        tempLabel.setMinWidth(BoxOffice_Main.getScreenWidth() - 200);
        tempLabel.setAlignment(Pos.CENTER);
        musicHeader = new VBox();
        musicHeader.getChildren().add(tempLabel);
        musicHeader.getChildren().add(musicColumns);
        //-----Books
        tempLabel = new Label("Audiobooks");
        tempLabel.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 25));
        tempLabel.setStyle("-fx-background-color: #6ca510");
        tempLabel.setPadding(new Insets(10, 0, 10, 0));
        tempLabel.setMinWidth(BoxOffice_Main.getScreenWidth() - 200);
        tempLabel.setAlignment(Pos.CENTER);
        bookHeader = new VBox();
        bookHeader.getChildren().add(tempLabel);
        bookHeader.getChildren().add(bookColumns);
        
        //Total Column
        totalTitle = new Label("Total:");
        totalTitle.setFont(Font.font("Trebuchet MS", 25));
        totalLabel = new Label();
        totalLabel.setFont(Font.font("Trebuchet MS", 25));
        totalLabel.setMaxWidth(200);
        totalRow = new HBox(10);
        generateTotalRow(totalRow); //Generate the total row in the table
        
        //Notice pane
        noticePane = new StackPane();
        noticePane.setAlignment(Pos.CENTER_LEFT);
        noticePane.getChildren().add(overdueDisclosure);
        noticePane.getChildren().add(nothing);
        
        //Buttons
        //-----Rent button
        rentButton = new ImageButton("images/rentBtnReg.png", "images/rentBtnHigh.png");
        rentButton.buttonImage.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            if(BoxOffice_Account.isUserLoggedIn()) {
                rentalWindow.showRentalPopUp(this);
                menu.updateCartCount();
                loadContent();
            }
            else
                menu.loginWindow.showLoginWindow(menu, "You Must Be Logged in to Rent Media"); 
        });
        //-----Empty Cart Button
        emptyCartButton = new ImageButton("images/emptyBtnReg.png", "images/emptyBtnHigh.png");
        emptyCartButton.buttonImage.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            BoxOffice_Cart.emptyCart();
            menu.updateCartCount();
            loadContent();
        });
        
        //Button Pane
        buttonPane = new HBox(5);
        buttonPane.setMinWidth(BoxOffice_Main.getScreenWidth() - 200);
        buttonPane.setAlignment(Pos.CENTER_RIGHT);
        buttonPane.getChildren().add(emptyCartButton);
        buttonPane.getChildren().add(rentButton);
                
        //Page break
        pageBreak = new Pane();
        pageBreak.setPrefSize(BoxOffice_Main.getScreenWidth() - 200, 3);
        pageBreak.setStyle("-fx-background-color: #6CA510;");
        
        //Content Pane
        contentPane = new VBox(10);
        contentPane.setPadding(new Insets(10, 0, 10, 100));
        contentPane.getChildren().add(header);
        contentPane.getChildren().add(table);
        contentPane.getChildren().add(noticePane);
        contentPane.getChildren().add(buttonPane);
        
        //Scroll pane
        pane = new ScrollPane(contentPane);
        
        //Main pane
        mainPane = new VBox(5);
        mainPane.getChildren().add(menu.getMenu());
        mainPane.getChildren().add(pane);
        mainPane.setMinSize(BoxOffice_Main.getScreenWidth(), BoxOffice_Main.getScreenHeight());
        
        //Main scene
        scene = new Scene(mainPane, BoxOffice_Main.getScreenWidth(), BoxOffice_Main.getScreenHeight());
    }
    
    /**
     * Method loadContent() loads the content of the BoxOffice_Cart_Page based on the content of the cart
     */
    protected void loadContent() {
        
        menu.updateMenu();
        
        int empty = 0;
        double total = 0;
        boolean isEven = true;
        
        //Clear the table
        table.getChildren().clear();
        
        //Movies
        //-----If movies in the cart, display them
        if(!BoxOffice_Cart.getCartMovies().isEmpty()) {
            //-----Add movie Header
            table.getChildren().add(movieHeader);
            //Get the list
            tempList = BoxOffice_Cart.getCartMovies();
            
            //Add items from the list to the table
            for (BoxOffice_Media_Item item : tempList) {
                generateRow(item, isEven);
                isEven = !isEven;
                total += item.price;
            }
        }
        //If no movies, say it
        else
            empty++;
        
        //Games
        isEven = true;
        
        //-----If games in the cart, display them
        if(!BoxOffice_Cart.getCartGames().isEmpty()) {
            
            //-----Add game header
            table.getChildren().add(gameHeader);
            
            //Get the list
            tempList = BoxOffice_Cart.getCartGames();
            
            //Add items from the list to the table
            for (BoxOffice_Media_Item item : tempList) {
                generateRow(item, isEven);
                isEven = !isEven;
                total += item.price;
            }
        }
        //If no movies, say it
        else
            empty++;
        
        //Music
        isEven = true;
        //-----If music in the cart, display them
        if(!BoxOffice_Cart.getCartMusic().isEmpty()) {
            //Get the list
            tempList = BoxOffice_Cart.getCartMusic();
            
            //-----Add music header
            table.getChildren().add(musicHeader);
            
            //Add items from the list to the table
            for (BoxOffice_Media_Item item : tempList) {
                generateRow(item, isEven);
                isEven = !isEven;
                total += item.price;
            }
        }
        //If no movies, say it
        else
            empty++;
        
        //books
        isEven = true;
        
        //-----If movies in the cart, display them
        if(!BoxOffice_Cart.getCartBooks().isEmpty()) {
            //-----Add book header
            table.getChildren().add(bookHeader);            

            //Get the list
            tempList = BoxOffice_Cart.getCartBooks();
            
            //Add items from the list to the table
            for (BoxOffice_Media_Item item : tempList) {
                generateRow(item, isEven);
                isEven = !isEven;
                total += item.price;
            }
        }
        //If no movies, say it
        else
            empty++;
        
        //Nothing in cart, say it
        if (empty == 4) {
            nothing.setVisible(true);
            overdueDisclosure.setVisible(false);
            buttonPane.setVisible(false);
        }
        //Add the total column
        else {
            totalLabel.setText(String.format("$%.2f", total));
            table.getChildren().add(pageBreak);
            table.getChildren().add(totalRow);
            nothing.setVisible(false);
            overdueDisclosure.setVisible(true);
            buttonPane.setVisible(true);
        }
    }
    
    /**
     * Method GenerateRow() creates a row in the cart table for item
     * @param item the item to be showed in the row
     * @param isEven how to color the row
     */
    private void generateRow(BoxOffice_Media_Item item, boolean isEven) {
        //Variables
        HBox tempPane, titlePane;
        StackPane imagePane, datePane, removePane, pricePane;
        ImageView imageIcon;
        Label titleLabel, dueDate, priceLabel;
        ImageButton removeButton;
        
        //Pane to represent table row
        tempPane = new HBox(10);
        tempPane.setMinHeight(170);
        tempPane.setAlignment(Pos.CENTER);
        
        //Background coloring
        if(isEven) {
            tempPane.setStyle("-fx-background-color: #ffffff");
        }
        else {
            tempPane.setStyle("-fx-background-color: #eafad1");
        }
        
        //Media Icon
        imageIcon = new ImageView(new Image(item.imageURL));
        if(!item.mediaType.equals("Music")) {
            imageIcon.setFitWidth(100);
            imageIcon.setFitHeight(150);
        }
        else {
            imageIcon.setFitWidth(100);
            imageIcon.setFitHeight(100);
        }
        
        //Image pane
        imagePane = new StackPane();
        imagePane.getChildren().add(imageIcon);
        imagePane.setMinWidth(120);
        
        //Title Label
        titleLabel = new Label(item.title);
        titleLabel.setFont(Font.font("Trebuchet MS", 25));
        
        //Title Pane
        titlePane = new HBox();
        titlePane.getChildren().add(titleLabel);
        titlePane.setMinWidth(BoxOffice_Main.getScreenWidth() - 730);
        titlePane.setAlignment(Pos.CENTER_LEFT);
        
        //Due Date Label
        //Get the current date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate localDate = LocalDate.now();
        localDate = localDate.plusWeeks(2);
        String dateString = dtf.format(localDate);
        //Store the date
        dueDate = new Label(dateString);
        dueDate.setFont(Font.font("Trebuchet MS", 20));
        
        //Date pane
        datePane = new StackPane();
        datePane.getChildren().add(dueDate);
        datePane.setMinWidth(150);
        
        //Remove button
        removeButton = new ImageButton("images/removeBtnReg.png", "images/removeBtnHigh.png");
        removeButton.buttonImage.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            BoxOffice_Cart.removeFromCart(item);
            menu.updateCartCount();
            loadContent();
        });
        
        //Remove Pane
        removePane = new StackPane();
        removePane.getChildren().add(removeButton);
        removePane.setMinWidth(120);
        
        priceLabel = new Label();
        //Price label
        priceLabel.setText(String.format("$%.2f", item.price));
        priceLabel.setFont(Font.font("Trebuchet MS", 25));
        priceLabel.setMaxWidth(80);
        
        //Remove Pane
        pricePane = new StackPane();
        pricePane.getChildren().add(priceLabel);
        pricePane.setMinWidth(90);
        
        //Add everything to the "row"
        tempPane.getChildren().add(imagePane);
        tempPane.getChildren().add(titlePane);
        tempPane.getChildren().add(datePane);
        tempPane.getChildren().add(pricePane);
        tempPane.getChildren().add(removePane);
        
        //Add it to the table
        table.getChildren().add(tempPane);
    }
    
    /**
     * Method GenerateTotalRow() creates the total row in the cart table
     * @param tempPane the pane that is being turned into a row
     */
    private void generateTotalRow(HBox tempPane) {
        //Variables
        HBox titlePane;
        StackPane imagePane, datePane, removePane, pricePane;
        
        //Pane to represent table row
        tempPane.setAlignment(Pos.CENTER);
        
        //Background coloring
        tempPane.setStyle("-fx-background-color: #ffffff");
        
        //Image pane
        imagePane = new StackPane();
        imagePane.setMinWidth(120);
        
        //Title Pane
        titlePane = new HBox();
        titlePane.setMinWidth(BoxOffice_Main.getScreenWidth() - 730);
        
        datePane = new StackPane();
        datePane.getChildren().add(totalTitle);
        datePane.setMinWidth(150);
        datePane.setAlignment(Pos.CENTER_RIGHT);

        pricePane = new StackPane();
        pricePane.getChildren().add(totalLabel);
        pricePane.setMinWidth(210);
        
        //Add everything to the "row"
        tempPane.getChildren().add(imagePane);
        tempPane.getChildren().add(titlePane);
        tempPane.getChildren().add(datePane);
        tempPane.getChildren().add(pricePane);
    }
    
    /**
     * Method createColumnHead() creates a column header for the table
     * @param HBox header the header that will be created
     */
    private void createColumnHead(HBox header) {
        //Variables
        HBox titlePane;
        StackPane imagePane, datePane, removePane, pricePane;
        Label titleLabel, dueDate, imageLabel, removeLabel, priceLabel;
        
        header.setStyle("-fx-background-color: #b7ef5d");
        header.setAlignment(Pos.CENTER);
        
        //Image label
        imageLabel = new Label("");
        imageLabel.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 15));
        imageLabel.setMaxWidth(100);
        imageLabel.setWrapText(true);
        
        //Image pane
        imagePane = new StackPane();
        imagePane.getChildren().add(imageLabel);
        imagePane.setMinWidth(120);
        
        //Title Label
        titleLabel = new Label("Media Title");
        titleLabel.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 15));
        titleLabel.setMaxWidth(100);
        titleLabel.setWrapText(true);
        
        //Title Pane
        titlePane = new HBox();
        titlePane.getChildren().add(titleLabel);
        titlePane.setMinWidth(BoxOffice_Main.getScreenWidth() - 730);
        titlePane.setAlignment(Pos.CENTER_LEFT);
        
        //Due Date Label
        dueDate = new Label("Due Date");
        dueDate.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 15));
        dueDate.setMaxWidth(100);
        dueDate.setWrapText(true);
        
        //Date pane
        datePane = new StackPane();
        datePane.getChildren().add(dueDate);
        datePane.setMinWidth(150);
        
        //Remove button
        removeLabel = new Label("Remove from Cart");
        removeLabel.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 15));
        removeLabel.setMaxWidth(100);
        removeLabel.setWrapText(true);
        
        //Remove Pane
        removePane = new StackPane();
        removePane.getChildren().add(removeLabel);
        removePane.setMinWidth(120);
        
        //Price label
        priceLabel = new Label("Price");
        priceLabel.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 15));
        priceLabel.setMaxWidth(70);
        
        //Price Pane
        pricePane = new StackPane();
        pricePane.getChildren().add(priceLabel);
        pricePane.setMinWidth(90);
        
        //Add everything to the "row"
        header.getChildren().add(imagePane);
        header.getChildren().add(titlePane);
        header.getChildren().add(datePane);
        header.getChildren().add(pricePane);
        header.getChildren().add(removePane);
    }

    //Variables
    //-----Scenes
    private Scene scene; //The main scene
    //-----Scroll panes
    private ScrollPane pane; //Holds everything to allow it to scroll
    //-----Panes
    private Pane pageBreak; //Break between the table and the column
    //-----Stack panes
    private StackPane noticePane; //used to display any notices in the cart
    //-----VBoxes
    private VBox mainPane; //The main pane that displays all items
    private VBox contentPane; //Pane to hold the media table
    private VBox table; //"Table" to hold all the media items
    private VBox movieHeader, gameHeader, musicHeader, bookHeader; //Headers for the table sections
    //-----HBoxes
    private HBox movieColumns, gameColumns, musicColumns, bookColumns; //Column lists for the table sections
    private HBox totalRow; //Row that shows the total sum of all items in the cart.
    private HBox buttonPane; //Pane to hold all the buttons
    //-----Labels
    private Label nothing; //Label to show if nothing is in the cart
    private Label header; //Header for the whole page
    private Label overdueDisclosure; //Disclosure to state charges for overdue items
    private Label totalLabel, totalTitle; //Shows the total rental price of the cart
    //-----Image Buttons
    private ImageButton rentButton; //Button to rent the cart
    private ImageButton emptyCartButton; //Button to empty the cart
    //-----Menu
    private BoxOffice_Menu menu; //Menu
    //-----Rental popup
    private BoxOffice_Rent_PopUp rentalWindow; //Rental process
    //-----Array Lists
    private ArrayList<BoxOffice_Media_Item> tempList; //List to hold the media items in the cart
}

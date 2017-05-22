/**
 * -----BoxOffice Media Rentals: Kiosk Software
 * -----BoxOffice Menu
 * @author      Stacy Zalisk
 * @date        4/28/2017
 * @version     1.6.3
 * Purpose:     Menu to be displayed across all BoxOffice kiosk pages
 * Revisions:   Logging out now returns you to the browsing page.  Added ability
 *              to return to the account page using the back button
 */

package BoxOffice;

//Imports
//-----Graphic Imports
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
//-----Other Imports
import java.util.ArrayList;

public class BoxOffice_Menu {
    
    /**
     * Constructor BoxOffice_Menu creates the menu upon
     */
    public BoxOffice_Menu() {
        createMenu();
    }
    
    /**
     * Method getMenu generates and returns the BoxOffice_Menu
     * @return menu the BoxOffice Menu
     */
    public BorderPane getMenu() {
        //If the menu hasn't been created yet, create it
        if(menu == null)
            createMenu();
        
        //Update the menu upon loading
        updateMenu();
        
        //Return the menu
        return menu;
    }
    
    /**
     * Method createMenu creates the BoxOffice_Menu from scratch
     */
    private void createMenu() {
        //Pane to hold the created menu
        menu = new BorderPane();
        menu.setPadding(new Insets(5, 5, 5, 5));
        
        //Box Office Logo
        logo = new ImageView(new Image("images/BoxOfficeSmall.png"));
        
        //Home Button
        homeBtn = new ImageButton("images/homeBtnReg.png", "images/homeBtnHigh.png");
        
        //Back Button
        backBtn = new ImageButton("images/backBtnReg.png", "images/backBtnHigh.png");
        //-----Set up back functionality
        backBtn.buttonImage.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            //Make sure there are pages to go back to
            if(!PageList.isEmpty()) {
                //Browsing page
                switch (PageList.get(PageList.size() - 1)) {
                    case "browse":
                        BoxOffice_Main.switchScene("browse");
                        PageList.remove(PageList.size() - 1);
                        break;
                    case "description":
                        BoxOffice_Main.switchScene("description", ItemList.get(ItemList.size() - 1));
                        PageList.remove(PageList.size() - 1);
                        ItemList.remove(ItemList.size() - 1);
                        break;
                    case "search":
                        BoxOffice_Main.switchScene("search", SearchList.get(SearchList.size() - 1));
                        PageList.remove(PageList.size() - 1);
                        SearchList.remove(SearchList.size() - 1);
                        break;
                    case "cart":
                        BoxOffice_Main.switchScene("cart");
                        PageList.remove(PageList.size() - 1);
                        break;
                    case "account":
                        BoxOffice_Main.switchScene("account");
                        PageList.remove(PageList.size() - 1);
                        break;
                    default:
                        break;
                }
            }
        });
        
        //Exit Button--TEMPORARY FOR TESTING ONLY
        //exitButton = new Button("Exit");
        //exitButton.setOnAction((ActionEvent e) -> {
        //    System.exit(0);
        //});
        
        //Navigation buttons
        navBtns = new HBox(5);
        navBtns.getChildren().add(homeBtn);
        navBtns.getChildren().add(backBtn);
        //navBtns.getChildren().add(exitButton);
        
        //User account icon
        userIcon = new ImageView(new Image("images/userIcon.png"));
        
        //Welcome text
        welcome = new Label("Welcome");
        welcome.setFont(Font.font("Trebuchet MS", 12));
        //User Name
        userName = new Label(BoxOffice_Account.getUserName());
        userName.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 15));
        userName.setTextFill(Color.web("#6CA510"));
        //Pane to hold the user's information
        welcomeText = new VBox();
        welcomeText.setAlignment(Pos.CENTER);
        welcomeText.getChildren().add(welcome);
        welcomeText.getChildren().add(userName);
        
        //Spacers
        space = new Pane();
        space.setPrefSize(3, 30);
        space.setStyle("-fx-background-color: #6CA510;");
        space1 = new Pane();
        space1.setPrefSize(3, 30);
        space1.setStyle("-fx-background-color: #6CA510;");
        space2 = new Pane();
        space2.setPrefSize(3, 30);
        space2.setStyle("-fx-background-color: #6CA510;");
        
        //Cart button
        cartBtn = new ImageButton("images/cartIconReg.png", "images/cartIconHigh.png");
        
        //Cart label
        cartStock = new Label("0");
        cartStock.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 15));
        cartStock.setTextFill(Color.web("#6CA510"));
        cartStock.setPadding(new Insets(0, 0, 0, -5));
        
        //Account label
        account = new Label("Account");
        account.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 15));
        //-----Even Handlers
        //----------Mouse entered
        account.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            account.setTextFill(Color.web("#6CA510"));
        });
        //----------Mouse exited
        account.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            account.setTextFill(Color.web("#000000"));
        });
        
        //Logout label
        logout = new Label("Logout");
        logout.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 15));
        //-----Event Handlers
        //----------Mouse entered
        logout.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            logout.setTextFill(Color.web("#6CA510"));
        });
        //----------Mouse exited
        logout.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            logout.setTextFill(Color.web("#000000"));
        });
        //----------Mouse clicks
        logout.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            BoxOffice_Account.logOut();
            BoxOffice_Main.switchScene("browse");
            updateMenu();
        });
        
        //Login label
        login = new Label("Login");
        login.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 15));
        //-----Events
        //----------Mouse entered
        login.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            login.setTextFill(Color.web("#6CA510"));
        });
        //----------Mouse exited
        login.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            login.setTextFill(Color.web("#000000"));
        });
        //----------Mouse clicks
        login.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            loginWindow.showLoginWindow(this);
        });
        
        //Login and logout pane to hold login/logout button
        loginLogout = new HBox();
        loginLogout.setPrefWidth(50);
        loginLogout.setAlignment(Pos.CENTER);
        loginLogout.getChildren().add(login);
        
        //Add user GUI to userPane
        userPane = new HBox(5);
        userPane.setAlignment(Pos.CENTER_LEFT);
        userPane.getChildren().add(cartBtn);
        userPane.getChildren().add(cartStock);
        userPane.getChildren().add(space2);
        userPane.getChildren().add(userIcon);
        userPane.getChildren().add(welcomeText);
        userPane.getChildren().add(space);
        userPane.getChildren().add(account);
        userPane.getChildren().add(space1);
        userPane.getChildren().add(loginLogout);
        
        //Add sections to menu
        menu.setCenter(logo);
        menu.setRight(userPane);
        menu.setLeft(navBtns);
        
        //Login Window
        loginWindow = new BoxOffice_Login_Window();
        loginWindow.createLoginWindow();
    }
    
    /**
     * Method addToBackStack adds the provided page to the list of previous pages
     * @param page the page to be added to PageList
     */
    public static void addToBackStack(String page) {
        PageList.add(page);
    }
    
    /**
     * Method addToBackStack adds the provided page to the list of previous pages
     * @param page the page to be added to PageList
     * @param item the BoxOffice_Media_Item needed to load the provided page
     */
    public static void addToBackStack(String page, BoxOffice_Media_Item item) {
        PageList.add(page);
        ItemList.add(item);        
    }
    
    /**
     * Method addToBackStack adds the provided page to the list of previous pages
     * @param page the page to be added to PageList
     * @param search the search term needed to return to the search page
     */
    public static void addToBackStack(String page, String search) {
        PageList.add(page);
        SearchList.add(search);
    }
 
    /**
     * Method addToSearchStack adds the search term to the list of previous terms
     * @param term the term to be added to the stack
     */
    public static void addToSearchStack(String term) {
        SearchList.add(term);
    }
    
    /**
     * Method updateMenu updates the boxOffice menu to reflect what user is logged in
     */
    public void updateMenu() {
        if(BoxOffice_Account.isUserLoggedIn()) {
            userName.setText(BoxOffice_Account.getUserName());
            loginLogout.getChildren().clear();
            loginLogout.getChildren().add(logout);
        }
        else {
            userName.setText(BoxOffice_Account.getUserName());
            loginLogout.getChildren().clear();
            loginLogout.getChildren().add(login);
        }
        
        updateCartCount();
    }
    
    public void updateCartCount() {
        cartStock.setText(String.valueOf(BoxOffice_Cart.cartStock));
    }
    
    //Variables
    //-----Individualized navigation items
    public ImageButton homeBtn; //The home button
    public Label account; //The account label
    public ImageButton cartBtn; //The button that links to the cart page
    //----------Login Window
    public BoxOffice_Login_Window loginWindow;
    
    //-----Universal navigation items
    //----------HBoxes
    private HBox navBtns; //Pane to hold the navigation buttons
    private HBox userPane; //Pane to hold the user account information
    private HBox loginLogout; //Pane to hold the login and logout text
    //----------VBoxes
    private VBox welcomeText; //Pane to hold the "welcome user" text
    //----------Panes
    private Pane space, space1, space2; //Spacers in the menu
    //----------BorderPanes
    private BorderPane menu; //The menu itself
    //----------ImageViews
    private ImageView logo; //The box office logo
    private ImageView userIcon; //Icon for the user account
    //----------Image Buttons
    private ImageButton backBtn; //The back button
    //----------Buttons
    //private Button exitButton; //TEMPORARY EXIT BUTTON
    //----------Labels
    private Label welcome;  //Label that literally says "welcome"
    private Label userName; //Label to show the user name
    private Label login, logout; //Labels to display "login" or "logout"
    private Label cartStock; //Shows how many items are stored in the cart

    //-----Static items
    private static ArrayList<String> PageList = new ArrayList(); //Holds the list of pages for back functionality
    private static ArrayList<BoxOffice_Media_Item> ItemList = new ArrayList(); //Holds all media items needed for previous pages
    private static ArrayList<String> SearchList = new ArrayList(); ////Holds all search terms needed for previous pages
}

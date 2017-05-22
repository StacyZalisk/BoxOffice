/**
 * -----BoxOffice Media Rentals: Kiosk Software
 * -----BoxOffice Main
 * @author       Stacy Zalisk
 * @date         4/29/2017
 * @version      1.7.2
 * Purpose:      Allows the user to browse through the selection of media stored
 *               in the Kiosk
 * Revisions:    Fixed another navigation bug
 */
package BoxOffice;

//Imports
//-----Graphic Imports
import java.awt.Dimension;
import java.awt.Toolkit;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class BoxOffice_Main extends Application {  
    @Override
    public void start(Stage primaryStage) {
        //Calulate screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = screenSize.getWidth();
        screenHeight = screenSize.getHeight();
        
        //Set the stage
        mainStage = primaryStage;
        mainStage.setX(0);
        mainStage.setY(0);
        mainStage.setMinHeight(screenHeight);
        mainStage.setMinWidth(screenWidth);
        mainStage.initStyle(StageStyle.UNDECORATED);
        mainStage.setTitle("BoxOffice Media Rentals");
        mainStage.setScene(bootPage.getScene());
        mainStage.show();
    }
    
    /**
     * Overloaded method switchScene to navigate to other pages
     * --Navigates to the browsing page
     * @param key the page to be switched to
     */
    public static void switchScene(String key) {
        if(key.equals("browse")) {
            mainStage.setScene(browsePage.getScene());
        }
        else if(key.equals("cart")) {
            mainStage.setScene(cartPage.getScene());
        }
        else if(key.equals("account")) {
            mainStage.setScene(accountPage.getScene());
        }
    }
    
    /**
     * Overloaded method switchScene to navigate to other pages
     * --Navigates to the description page
     * @param key the page to be switched to
     * @param item the BoxOffice_Media_Item to be loaded
     */
    public static void switchScene(String key, BoxOffice_Media_Item item) {
        if(key.equals("description")) {
            mainStage.setScene(descriptionPage.getScene(item));
        }
    }
    
    /**
     * Overloaded method switchScene to navigate to other pages
     * --Navigates to the browsing or account page
     * --Stores the previous scene's key
     * @param key the page to be switched to
     * @param prevScene the page to be added to the back queue in the menu
     */
    public static void switchScene(String key, String prevScene) {
        if(key.equals("browse")) {
            mainStage.setScene(browsePage.getScene());
        }
        if(key.equals("account")) {
            mainStage.setScene(accountPage.getScene());
        }
        if(key.equals("cart")) {
            mainStage.setScene(cartPage.getScene());
        }
        if(key.equals("search")) {
            mainStage.setScene(searchPage.getScene(prevScene));
        }
        BoxOffice_Menu.addToBackStack(prevScene);
    }
    
    /**
     * Overloaded method switchScene to navigate to other pages
     * --Navigates to the description page
     * --Stores the previous scene's key (in this case, the search page)
     * @param key the page to be switched to
     * @param item the BoxOffice_Media_Item to be loaded by the description page
     * @param prevScene the page to be added to the back queue in the menu
     * @param search the search term needed to load prevScene
     */
    public static void switchScene(String key, BoxOffice_Media_Item item, String prevScene, String search) {
        if(key.equals("description")) {
            mainStage.setScene(descriptionPage.getScene(item));
        }
        BoxOffice_Menu.addToBackStack(prevScene, search);
    }
    
    /**
     * Overloaded method switchScene to navigate to other pages
     * --Navigates to the description page
     * --Stores the previous scene's key
     * @param key the page to be switched to
     * @param item the BoxOffice_Media_Item to be loaded by the description page
     * @param prevScene the page to be added to the back queue in the menu
     */
    public static void switchScene(String key, BoxOffice_Media_Item item, String prevScene) {
        if(key.equals("description")) {
            mainStage.setScene(descriptionPage.getScene(item));
        }
        BoxOffice_Menu.addToBackStack(prevScene);
    }
    
    /**
     * Overloaded method switchScene to navigate to other pages
     * --Navigates to the search or description page
     * --Stores the previous scene's key and any related information
     * @param key the page to be switched to
     * @param prevScene the page to be added to the back queue in the menu
     * @param search the search term needed to load the search page
     */
    public static void switchScene(String key, String search, String prevScene) {
        if(key.equals("search")) {
            mainStage.setScene(searchPage.getScene(search));
            BoxOffice_Menu.addToBackStack(prevScene, search);
        }
        else if(key.equals("browse")) {
            mainStage.setScene(browsePage.getScene());
            BoxOffice_Menu.addToBackStack(prevScene, search);
        }
        else if(key.equals("account")) {
            mainStage.setScene(accountPage.getScene());
            BoxOffice_Menu.addToBackStack(prevScene, search);
        }
    }
    
    /**
     * Overloaded method switchScene to navigate to other pages
     * --Navigates to the browsing page
     * --Stores the previous scene's key (in this case, the description page)
     * @param key the page to be switched to
     * @param prevScene the page to be added to the back queue in the menu
     * @param item the BoxOffice_Media_Item needed to load the description page
     */
    public static void switchScene(String key, String prevScene, BoxOffice_Media_Item item) {
        if(key.equals("browse")) {
           mainStage.setScene(browsePage.getScene());
        }
        else if (key.equals("cart")) {
            mainStage.setScene(cartPage.getScene());
        }
        else if (key.equals("account")) {
            mainStage.setScene(accountPage.getScene());
        }
        BoxOffice_Menu.addToBackStack(prevScene, item);
    }
    
    /**
     * Method getScreenWidth returns the width of the screen
     * @return the width of the screen
     */
    public static double getScreenWidth() {
        return screenWidth;
    }
    
    /**
     * Method getScreenHeight returns the width of the screen
     * @return the height of the screen
     */
    public static double getScreenHeight() {
        return screenHeight;
    }
    
    /**
     * Method getKioskID returns the kiosk's ID
     * @return the ID of the kiosk
     */
     public static String getKioskID() {
        return kioskID;
    }
     
    /**
     * Sets the ID of the kiosk
     * @param ID the ID of the kiosk
     */
    public static void setKioskID(String ID) {
         kioskID = ID;
    }
    
    /**
     * Method getKioskID returns the database URL
     * @return the ID of the kiosk
     */
     public static String getDatabaseURL() {
        return databaseURL;
    }
     
    /**
     * Sets the database URL for the kiosk
     * @param URL the URL of the kiosk's database
     */
    public static void setDatabaseURL(String URL) {
         databaseURL = URL;
    }
    
    /**
     * main function to launch the application
     * @param args any command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
    
    
    //Stage
    private static Stage mainStage;
    //Pages
    private static final BoxOffice_Browse browsePage = new BoxOffice_Browse();
    private static final BoxOffice_Media_Description descriptionPage = new BoxOffice_Media_Description();
    private static final BoxOffice_Search_Page searchPage = new BoxOffice_Search_Page();
    private static final BoxOffice_Account_Page accountPage = new BoxOffice_Account_Page();
    private static final BoxOffice_Cart_Page cartPage = new BoxOffice_Cart_Page();
    private static final BoxOffice_Boot_Page bootPage = new BoxOffice_Boot_Page();
    //Data
    private static double screenWidth;
    private static double screenHeight;
    private static String kioskID = "";
    private static String databaseURL = "";
}

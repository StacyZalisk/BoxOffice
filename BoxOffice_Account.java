/**
 * -----BoxOffice Media Rentals: Kiosk Software
 * -----BoxOffice Account
 * @author      Stacy Zalisk
 * @date        4/28/2017
 * @version     1.5.3
 * Purpose:     A class to represent the active user on the kiosk
 * Revisions:   Added method to check if the user has already rented the item
 */
package BoxOffice;

//Imports
//-----Data imports
import java.util.ArrayList;
//-----Database imports
import java.sql.*;

public class BoxOffice_Account {

    /**
     * Initialize the account so it can read from the database
     */
    public static void initializeAccount() {
        try {
            connection = DriverManager.getConnection(BoxOffice_Main.getDatabaseURL(), "", "");
            data = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            //Get the table with the kiosk data
            String selTable = "SELECT * FROM tblUserAccounts";
            data.execute(selTable);
            userTable = data.getResultSet();
            
            selTable = "SELECT * FROM tblMedia";
            data.execute(selTable);
            mediaTable = data.getResultSet();
            
            selTable = "SELECT * FROM tblContent";
            data.execute(selTable);
            contentTable = data.getResultSet();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Method to log in the user
     * @param username the username to be logged into
     * @param password the password to be logged into
     * @param window the window the user is logging in from
     */
    public static void logInUser(String username, String password, BoxOffice_Login_Window window) {
        try {
            //Get user table
            userTable.first();
            //Check for valid username
            while(!userTable.isAfterLast()) {
                //Username is in the table
                if(username.equals(userTable.getString("Username"))) {
                    //Successful login
                    if(password.equals(userTable.getString("Password"))) {
                        userName = userTable.getString("FirstName");    
                        active = true;
                        userID = userTable.getString("UserID");
                        loadRentalData();
                        window.updateLoginWindow(0);
                        return;
                    }
                    //Wrong password
                    else {
                        window.updateLoginWindow(2);
                        return;
                    }
                }
                userTable.next();
            }
            //All usernames exhausted, invalid username
            window.updateLoginWindow(1); //Invalid username
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Method to log out the current user
     */
    public static void logOut() {
        active = false;
        userName = "User";
        BoxOffice_Cart.emptyCart();
        rentedMedia.clear();
    }
    
    /**
     * Return whether or not a user is logged into the kiosk
     * @return true if a user is logged in, false if a user is not
     */
    public static boolean isUserLoggedIn() { return active; }
    
    /**
     * Loads the data of the user that just logged in
     */
    private static void loadRentalData() {
        int count;
        //Clear rented media
        rentedMedia.clear();
        try {
             //Make sure there are some results to find
             String selTable = "SELECT COUNT(*) AS total FROM tblRental WHERE UserID='" + userID + "' AND Active=true";
             data.execute(selTable);
             ResultSet tempset = data.getResultSet();
             tempset.first();
             count = tempset.getInt("total");
             
            //Get all rentals from the user
            if(count > 0) {
                selTable = "SELECT * FROM tblRental WHERE UserID='" + userID + "' AND Active=true";
                String tempString = "";
                boolean trigger = false;
                data.execute(selTable);
                rentalTable = data.getResultSet();

                rentalTable.first();
                mediaTable.first();
                contentTable.first();

                //Load information on the rentals
                while(!rentalTable.isAfterLast()) { //For every rental the user has checked out
                    //Reset variables
                    trigger = false;
                    tempString = BoxOffice_Kiosk_Data.getMediaId(rentalTable.getString("Barcode"));

                    if(tempString.equals("")) { //Item does not belong to this kiosk
                        while(!contentTable.isAfterLast()) { //For every item in media content
                            if(contentTable.getString("Barcode").equals(rentalTable.getString("Barcode"))) {
                                while(!mediaTable.isAfterLast()) { //For each media item in the table of rentals
                                    if(mediaTable.getString("MediaID").equals(contentTable.getString("MediaID"))) {
                                           rentedMedia.add(new BoxOffice_Rental_Item(mediaTable.getString("ImageURL"), rentalTable.getString("Barcode"), mediaTable.getString("Type"), mediaTable.getString("MediaID"), false, rentalTable.getBoolean("Overdue")));
                                           trigger = true;
                                           break;
                                    }
                                    mediaTable.next();
                                }
                            }
                            if(trigger)
                                break;
                            contentTable.next();
                            mediaTable.first();
                        } 
                    }
                    else {
                        while(!mediaTable.isAfterLast()) { //For each media item in the table of rentals
                            if(mediaTable.getString("MediaID").equals(tempString)) {
                                   rentedMedia.add(new BoxOffice_Rental_Item(mediaTable.getString("ImageURL"), rentalTable.getString("Barcode"), mediaTable.getString("Type"), mediaTable.getString("MediaID"), true, rentalTable.getBoolean("Overdue")));
                                   trigger = true;
                                   break;
                            }
                            mediaTable.next();
                        }
                    }
                    mediaTable.first();
                    contentTable.first();
                    rentalTable.next();
                }
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Returns the rented media item
     * @param code the barcode of the item being returned
     */
    public static void returnItem(String code) {
        BoxOffice_Rental_Item remove = new BoxOffice_Rental_Item("", "", "", "", false, false);
        for(BoxOffice_Rental_Item item: rentedMedia) {
            if(item.barcode.equals(code)) {
                remove = item;
                break;
            }
        }
        rentedMedia.remove(remove);
    }
    
    /**
     * Method addNewRental adds a new rental to the BoxOffice_Account
     */
    public static void updateRentals () {
        loadRentalData();
    }
    
    /**
     * Method getRentedBooks returns the list of all items currently rented to the user
     * @return the ArrayList containing the items the user has rented
     */
    public static ArrayList getRentedItems() { return rentedMedia; }
 
    /**
     * Checks if the user has rented the item prior to adding it to the cart
     * @param ID the ID of the item that is being added to the cart
     * @return true if the item is rented and false if it is not
     */
    public static boolean checkIfRented(String ID) {
        for(BoxOffice_Rental_Item item: rentedMedia) {
            if(item.mediaID.equals(ID))
                return true;
        }   
        return false;
    }
    
    /**
     * Return the username of the current user
     * @return the username of the current user
     */
    public static String getUserName() { return userName; }
    
    /**
     * Return the user ID of the current user
     * @return the user ID of the current user
     */
    public static String getUserID() { return userID; }
    
    
    //Variables
    //-----User information
    private static String userID; //ID of the current user
    private static String userName = "User"; //User name of the account
    private static boolean active; //Whether or not an active user is signed on
    private static final ArrayList<BoxOffice_Rental_Item> rentedMedia = new ArrayList(); //All items rented by the user
    //-----Database access
    private static Statement data;
    private static Connection connection;
    private static ResultSet userTable, rentalTable, mediaTable, contentTable;
}

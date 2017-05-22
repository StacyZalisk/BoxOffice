/**
 * -----BoxOffice Media Rentals: Kiosk Software
 * -----BoxOffice Kiosk Data
 * @author      Stacy Zalisk
 * @date        4/27/2017
 * @version     1.2.1
 * Purpose:     Handle all internal operations of the kiosk in terms of media stock
 * Revisions:   After data is loaded, navigates to the browsing page
 */
package BoxOffice;

//Imports
//-----Database Imports
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//-----Time and Date Imports
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
//-----Data Imports
import java.util.ArrayList;

public class BoxOffice_Kiosk_Data {
    
    /**
     * Loads the kiosk's stock information from the database
     * @param kioskID the ID of this kiosk
     */
    public static void loadData(String kioskID) {
        if(data == null) {
            try {
                connection = DriverManager.getConnection(BoxOffice_Main.getDatabaseURL(), "", "");
                data = connection.createStatement();
                
                //Get the mediaIDs of all items
                String selTable = "SELECT MediaID FROM tblMedia";
                data.execute(selTable);
                ResultSet rs = data.getResultSet();
                rs.next();
                while(!(rs.isAfterLast())) {
                    inventoryStock.add(new mediaStock(rs.getString(1)));
                    //Progress to next record
                    rs.next();
                }

                //Get the table with the kiosk data
                selTable = "SELECT Barcode, MediaID FROM tblContent WHERE KioskID='" + kioskID + "'";
                data.execute(selTable);
                rs = data.getResultSet();
                //Loop through results
                rs.next();
                //To begin, load everything into inStock
                while(!(rs.isAfterLast())) {
                    inStock.add(new mediaItem(rs.getString(2), rs.getString(1)));
                    //Update stock of that item
                    for (mediaStock item : inventoryStock) {
                        if(item.mediaID.equals(rs.getString(2))) {
                            //Move the item to out of stock
                            item.stock++;
                            break;
                        }
                    }
                    //Progress to next record
                    rs.next();
                }
                
                //Now, move all out of stock item into outOfStock
                selTable = "SELECT Barcode FROM tblRental WHERE Active=true";
                data.execute(selTable);
                rs = data.getResultSet();
                rs.next();
                //Check if the barcode matches anything in inStock
                while(!(rs.isAfterLast())) {
                    for (mediaItem item : inStock) {
                        if(rs.getString(1).equals(item.barcode)) {
                            //Move the item to out of stock
                            outOfStock.add(item);
                            inStock.remove(item);
                            //Update the stock of that item
                             for (mediaStock stock : inventoryStock) {
                                if(stock.mediaID.equals(item.mediaID)) {
                                    //Move the item to out of stock
                                    stock.stock--;
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    
                    rs.next();
                }
                
                BoxOffice_Main.switchScene("browse");
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Rents the current item from the kiosk
     * @param mediaID the ID of the media item being rented
     * @param type the type of media being rented
     * @param userID the ID of the user renting the item
     */
    public static void rentItem(String mediaID, String type, String userID) {
        mediaItem rentalItem = new mediaItem("", "");
        String selectStatement;
        
        //Get the current date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate localDate = LocalDate.now(); 
        String dateString = dtf.format(localDate);
        LocalDate tempDate;
        
        //Remove the item from inStock
        for(mediaItem item : inStock) {
            if(item.mediaID.equals(mediaID)) {
                outOfStock.add(item);
                inStock.remove(item);
                rentalItem = item;
                for(mediaStock stock : inventoryStock) {
                    if(stock.mediaID.equals(mediaID)) {
                        stock.stock--;
                        break;
                    }
                }
                break;
            }
        }
        
        //Write it to the database
        try {            
            //Set up date for database
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            Date date = new Date();
            try {
                date = df.parse(dateString);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }

            selectStatement = "INSERT INTO tblRental(Barcode, UserID, RentalDate) VALUES (?,?,?)";
            PreparedStatement values = connection.prepareStatement(selectStatement);
            values.setString(1, rentalItem.barcode);
            values.setString(2, userID);
            values.setTimestamp(3, new Timestamp(date.getTime()));
            values.executeUpdate();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Returns the provided item to the stock of the kiosk
     * @param barcode the barcode of the item to be returned
     */
    public static void returnItem(String barcode) {
        //Get the current date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate localDate = LocalDate.now(); 
        String dateString = dtf.format(localDate);
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        try {
            date = df.parse(dateString);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        
        //Mark the rental as returned in the database
        try {
            String selectStatement = "UPDATE tblRental SET Active=?, ActualReturnDate=? WHERE Barcode=?";
            PreparedStatement values;
            values = connection.prepareStatement(selectStatement);
            values.setBoolean(1, false);
            values.setTimestamp(2, new Timestamp(date.getTime()));
            values.setString(3, barcode);
            values.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        //Mark the item as returned to this kiosk
        //Add the item to inStock
        for(mediaItem item : outOfStock) {
            if(item.barcode.equals(barcode)) {
                inStock.add(item);
                outOfStock.remove(item);
                for(mediaStock stock : inventoryStock) {
                    if(stock.mediaID.equals(item.mediaID)) {
                        stock.stock++;
                        break;
                    }
                }
                break;
            }
        }
        
        //Update account information
        BoxOffice_Account.returnItem(barcode);
            
    }
    
    /**
     * Checks whether the item with the provided ID is in stock or not
     * @param mediaID the ID of the item to be checked
     * @return true if the item is in stock and false if it is not
     */
    public static boolean checkStock(String mediaID) {
        for(mediaStock item : inventoryStock) {
            if(item.mediaID.equals(mediaID)) {
                if(item.stock <= 0)
                    return false;
            }
        }
        return true;
    }
    
    /**
     * Returns the mediaID associated with the provided barcode
     * @param itemBarcode the barcode of the media item
     * @return the mediaID that corresponds to itemBarcode
     */
    public static String getMediaId(String itemBarcode) {
        String mediaID = "";
        
        for(mediaItem item: outOfStock) {
            if(item.barcode.equals(itemBarcode)) {
                mediaID = item.mediaID;
                break;
            }
        }
        
        //Item not found in out of stock, check in stock
        if(mediaID.equals("")) {
            for(mediaItem item: inStock) {
                if(item.barcode.equals(itemBarcode)) {
                    mediaID = item.mediaID;
                    break;
                }
            }
        }
        
        return mediaID;
    }
    
    //Variables
    //-----Arrays
    private static ArrayList<mediaItem> inStock = new ArrayList(); //All the media items currently available in the kiosk
    private static ArrayList<mediaItem> outOfStock = new ArrayList(); //All the media items that are rented out of the kiosk
    private static ArrayList<mediaStock> inventoryStock = new ArrayList();
    //-----Database access
    private static Statement data;
    private static Connection connection;
    
    //Class to represent any media item stored in the kiosk
    private static class mediaItem {
        protected mediaItem(String ID, String bar) {
            barcode = bar;
            mediaID = ID;
        }
        protected String barcode;
        protected String mediaID;
    }
    
    //Class to represent the stock of any media item in the kiosk
    private static class mediaStock {
        protected mediaStock(String ID) {
            mediaID = ID;
        }
        protected String mediaID;
        protected int stock = 0;
    }
}



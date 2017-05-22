/**
 * -----BoxOffice Media Rentals: Kiosk Software
 * -----BoxOffice_Rental_Item
 * @author      Stacy Zalisk
 * @date        4/28/2017
 * @version     1.0.1
 * Purpose:     A class to represent an item currently rented by a user
 * Revisions:   Added field mediaID to hold the ID of the media item.  Overdue status now recorded
 *              through constructor
 */
package BoxOffice;

public class BoxOffice_Rental_Item {
    
    /**
     * Constructor to create new BoxOffice_Rental_Item
     * @param URL the URL of the image representing the item
     * @param code the barcode of the rented item
     * @param ret boolean representing if the item can be returned at this kiosk or not
     * @param type the type of the media item
     * @param mediaID the ID of the media item
     * @param overdue if the item is overdue or not
     */
    public BoxOffice_Rental_Item(String URL, String code, String type, String mediaID, boolean ret, boolean overdue) {
        imageURL = URL;
        barcode = code;
        returnable = ret;
        mediaType = type;
        this.mediaID = mediaID;
        this.overdue = overdue;
    }

    protected String imageURL;
    protected String barcode;
    protected String mediaType;
    protected String mediaID;
    protected boolean returnable;
    protected boolean overdue;
}

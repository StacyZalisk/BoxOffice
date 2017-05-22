/**
 * -----BoxOffice Media Rentals: Kiosk Software
 * -----BoxOffice Media Item
 * @author      Stacy Zalisk
 * @date        4/17/2017
 * @version     1.5.2
 * Purpose:     Class to represent any type of media item stored in the kiosk
 * Revisions:   Added boolean to represent if the item is already in the cart.  Added double to hold the price of the media item
 */

package BoxOffice;

public class BoxOffice_Media_Item {
    
    /**
     * Constructor BoxOffice_Media_Item creates a media item for the kiosk with the provided information
     * @param ID the ID of the media item
     * @param type the media type of the item (CD, book, ect.)
     * @param title the title of the item
     * @param star the rating out of five for the media item
     * @param rating the parental rating of the item
     * @param image the URL of the image representing the media (poster for moves, album cover for music, ect)
     * @param description the text description of the media item
     * @param artist the artist of the media item (album = artist; movie = cast; ect.)
     * @param genre1 the genre of the media item
     * @param genre2 the genre of the media item
     * @param genre3 the genre of the media item
     * @param price the price of the media item
     */
    public BoxOffice_Media_Item(String ID, String type, String title, double star, String rating, String image, String description, String artist, String genre1, String genre2, String genre3, double price) {
        mediaID = ID;
        starRating = star;
        this.rating = rating;
        mediaType = type;
        imageURL = image;
        this.title = title;
        this.description = description;
        this.artist = artist;
        this.genre1 = genre1;
        this.genre2 = genre2;
        this.genre3 = genre3;
        this.price = price;
        
        createSearchStrings();
        
        button = new MediaButton(image, this.title, mediaType);
    }
    
    /**
     * Constructor BoxOffice_Media_Item creates a media item for the kiosk with the provided information
     */
    private void createSearchStrings() {
        //Set up search strings
        //Bring everything to lower case
        searchTitle = title.toLowerCase();
        searchArtist = artist.toLowerCase();
        
        //Remove leading "the" and "a" and "an" from title
        if(searchTitle.startsWith("the "))
            searchTitle = searchTitle.substring(4);
        else if(searchTitle.startsWith("a "))
            searchTitle = searchTitle.substring(2);
        else if(searchTitle.startsWith("an "))
            searchTitle = searchTitle.substring(3);
        
        //Remove leading "the" and "a" and "an" from artist
        if(searchArtist.startsWith("the "))
            searchArtist = searchArtist.substring(4);
        else if(searchArtist.startsWith("a "))
            searchArtist = searchArtist.substring(2);
        else if(searchArtist.startsWith("an "))
            searchArtist = searchArtist.substring(3);
    }
    
    //Variables
    protected String mediaID; //The id of the media in the kiosk
    protected double starRating; //The star rating of the item
    protected double price; //The price of the media item
    protected String rating; //Parental rating of the item
    protected String artist, searchArtist; //Artist of the media
    protected String mediaType; //Type of the media (movie, music, ect.)
    protected String imageURL; //The URL of the media's poster
    protected String title, searchTitle; //The title of the media item
    protected String description; //The description of the media item
    protected String genre1, genre2, genre3; //The genre of the media item
    protected MediaButton button; //Button for the browsing and searching pages
    protected Boolean inCart = false; //Represents if the item is in the cart or not
}

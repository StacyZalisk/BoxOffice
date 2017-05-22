/**
 * -----BoxOffice Media Rentals: Kiosk Software
 * -----BoxOffice Cart
 * @author       Stacy Zalisk
 * @date         4/25/2017
 * @version      1.3.2
 * Purpose:      Class to function as a shopping cart that holds all items the user wishes to rent
 * Revisions:    Fixed problems while recording the number of items in the cart.
 */
package BoxOffice;

//Imports
import java.util.ArrayList;

public class BoxOffice_Cart {
    
    /**
     * Method addToCart to add a BoxOffice_Media_Item to the cart
     * @param item the item to be added to the cart
     */
    public static void addToCart(BoxOffice_Media_Item item) {
        switch(item.mediaType) {
            case("Video"):
                if(!movies.contains(item)) {
                    movies.add(item);
                    item.inCart = true;
                    cartStock++;
                }
                break;
            case("Music"):
                if(!music.contains(item)) {
                    music.add(item);
                    item.inCart = true;
                    cartStock++;
                }
                break;
            case("Game"):
                if(!videoGames.contains(item)) {
                    videoGames.add(item);
                    item.inCart = true;
                    cartStock++;
                }
                break;
            case("Audiobook"):
                if(!books.contains(item)) {
                    books.add(item);
                    item.inCart = true;
                    cartStock++;
                }
                break;
        }
    }
    
    /**
     * Method removeFromCart removes the specified media item from the cart
     * @param item the media item to be removed from the cart
     */
    public static void removeFromCart(BoxOffice_Media_Item item) {
        switch(item.mediaType) {
            case("Video"):
                if(movies.remove(item)) {
                    item.inCart = false;
                    cartStock--;
                }
                break;
            case("Music"):
                if(music.remove(item)) {
                    item.inCart = false;
                    cartStock--;
                }
                break;
            case("Game"):
                if(videoGames.remove(item)){
                    item.inCart = false;
                    cartStock--;
                }
                break;
            case("Audiobook"):
                if(books.remove(item)){
                    item.inCart = false;
                    cartStock--;
                }
                break;
        }
    }
    
    /**
     * Method getCartMovies returns the movies in the cart
     * @return the list of movies in the cart
     */
    public static ArrayList getCartMovies() { return movies; }
    
    /**
     * Method getCartMusic returns the music in the cart
     * @return the list of music in the cart
     */
    public static ArrayList getCartMusic() { return music; }
    
    /**
     * Method getCartBooks returns the books in the cart
     * @return the list of books in the cart
     */
    public static ArrayList getCartBooks() { return books; }
    
    /**
     * Method getCartGames returns the movies in the cart
     * @return the list of games in the cart
     */
    public static ArrayList getCartGames() { return videoGames; }
    
    /**
     * Method rentAllItems() rents all media items stored in the cart
     */
    public static void rentAllItems() {
        movies.forEach((movie) -> { 
            BoxOffice_Kiosk_Data.rentItem(movie.mediaID, movie.mediaType, BoxOffice_Account.getUserID());
        });
        music.forEach((album) -> {
            BoxOffice_Kiosk_Data.rentItem(album.mediaID, album.mediaType, BoxOffice_Account.getUserID());
        });
        videoGames.forEach((game) -> {
            BoxOffice_Kiosk_Data.rentItem(game.mediaID, game.mediaType, BoxOffice_Account.getUserID());
        });
        books.forEach((book) -> {
            BoxOffice_Kiosk_Data.rentItem(book.mediaID, book.mediaType, BoxOffice_Account.getUserID());
        });
        cartStock = 0;
        //Update the rentals in the account
        BoxOffice_Account.updateRentals();
    }
    
    /**
     * Empties the content of the cart
     */
    public static void emptyCart() {
        //Reset cart status of all items
        movies.forEach((movie) -> { 
            movie.inCart = false;
        });
        music.forEach((album) -> {
            album.inCart = false;
        });
        videoGames.forEach((game) -> {
            game.inCart = false;
        });
        books.forEach((book) -> {
            book.inCart = false;
        });
        movies.clear();
        music.clear();
        videoGames.clear();
        books.clear();
        cartStock = 0;
    }
    
    //Variables
    //-----Array lists
    private static final ArrayList<BoxOffice_Media_Item> movies = new ArrayList(); //All the movies in the cart
    private static final ArrayList<BoxOffice_Media_Item> music = new ArrayList(); //All the music in the cart
    private static final ArrayList<BoxOffice_Media_Item> videoGames = new ArrayList(); //All the games in the cart
    private static final ArrayList<BoxOffice_Media_Item> books = new ArrayList(); //All the books in the cart
    
    public static int cartStock = 0;
}

/**
 * -----BoxOffice Media Rentals: Kiosk Software
 * -----BoxOffice Browse Page
 * @author       Stacy Zalisk
 * @date         5/05/2017
 * @version      1.8.6
 * Purpose:      Allows the user to browse through the selection of media stored
 *               in the Kiosk
 * Revisions:    Added check to prevent searching with blank strings
 */

package BoxOffice;

//Imports
//-----Graphic Imports
import javafx.scene.Scene;
import javafx.geometry.Orientation;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
//-----Data Imports
import java.util.ArrayList;
//-----Database Imports
import java.sql.*;
import org.apache.commons.lang.StringEscapeUtils;

public  class BoxOffice_Browse { 
    /**
     * Method getScene generates and returns the BoxOffice_Browse scene
     * @return Scene The scene that contains the BoxOffice_Browse page
     */
    public Scene getScene() {
        //If the scene hasn't been created, create it
        if(scene == null)
            createScene();
        
        //Update the menu
        menu.updateMenu();
        
        //Clear the search field
        searchField.setText("");
        
        //Check for out of stock items
        toggleStock();
        
        //Return the browsing scene
        return scene;
    }
    
    /**
     * Method createScene generates the entire Browse Page from scratch
     */
    private void createScene() {
        //Load media information from database
        loadMedia();
        
        //Create the menu
        menu = new BoxOffice_Menu();
        //-----Account Button
        menu.account.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            if(BoxOffice_Account.isUserLoggedIn())
                BoxOffice_Main.switchScene("account", "browse");
            else
                menu.loginWindow.showLoginWindow(menu, "You Must Be Logged in to Access Your Account");            
        });
        //-----Cart Button
        menu.cartBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            BoxOffice_Main.switchScene("cart", "browse");
        });
        
        //Universal Search Menu
        //-----Search Pane (searchPane)
        searchPane = new BorderPane();
        //-----Sort Pane (sortPane)
        sortPane = new HBox(5);
        sortPane.setAlignment(Pos.CENTER_LEFT);
        //-----Sorting label (searchSort)
        searchSort = new Label("Sort All:");
        searchSort.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 30));
        searchSort.setTextFill(Color.web("#6CA510"));
        //-----Sorting combo box (universalSort)
        universalSort = new ComboBox<>();
        universalSort.getItems().addAll("Alphabetic Ascending (A-Z)", "Alphabetic Descending (Z-A)", 
                "User Rating (Highest First)", "User Rating (Lowest First)");
        universalSort.setValue("Alphabetic Ascending (A-Z)");
        universalSort.setOnAction(e -> {
            switch (universalSort.getValue()) {
                case "Alphabetic Ascending (A-Z)":
                    sortAlphaAscending(movies, moviePane);
                    movieSort.setValue("Alphabetic Ascending (A-Z)");
                    sortAlphaAscending(music, musicPane);
                    musicSort.setValue("Alphabetic Ascending (A-Z)");
                    sortAlphaAscending(videoGames, gamePane);
                    gameSort.setValue("Alphabetic Ascending (A-Z)");
                    sortAlphaAscending(books, bookPane);
                    bookSort.setValue("Alphabetic Ascending (A-Z)");
                    break;
                case "Alphabetic Descending (Z-A)":
                    sortAlphaDescending(movies, moviePane);
                    movieSort.setValue("Alphabetic Descending (Z-A)");
                    sortAlphaDescending(music, musicPane);
                    musicSort.setValue("Alphabetic Descending (Z-A)");
                    sortAlphaDescending(videoGames, gamePane);
                    gameSort.setValue("Alphabetic Descending (Z-A)");
                    sortAlphaDescending(books, bookPane);
                    bookSort.setValue("Alphabetic Descending (Z-A)");
                    break;
                case "User Rating (Highest First)":
                    sortRatingDescending(movies, moviePane);
                    movieSort.setValue("User Rating (Highest First)");
                    sortRatingDescending(music, musicPane);
                    musicSort.setValue("User Rating (Highest First)");
                    sortRatingDescending(videoGames, gamePane);
                    gameSort.setValue("User Rating (Highest First)");
                    sortRatingDescending(books, bookPane);
                    bookSort.setValue("User Rating (Highest First)");
                    break;
                case "User Rating (Lowest First)":
                    sortRatingAscending(movies, moviePane);
                    movieSort.setValue("User Rating (Lowest First)");
                    sortRatingAscending(music, musicPane);
                    musicSort.setValue("User Rating (Lowest First)");
                    sortRatingAscending(videoGames, gamePane);
                    gameSort.setValue("User Rating (Lowest First)");
                    sortRatingAscending(books, bookPane);
                    bookSort.setValue("User Rating (Lowest First)");
                    break;
                default:
                    break;
            }
        });
        //-----Search label (searchText)
        searchText = new Label("Search:");
        searchText.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 30));
        searchText.setTextFill(Color.web("#6CA510"));
        searchText.setPadding(new Insets(0, 0, 0, 20));
        //-----Search Field (searchField)
        searchField = new TextField();
        searchField.setMinWidth(200); //TODO search on enter key
        //-----Search Button (searchButton)
        searchButton = new ImageButton("images/searchBtn.png", "images/searchBtnHigh.png");
        searchButton.buttonImage.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            if(!searchField.getText().isEmpty())
                searchMedia(searchField.getText());
        });
        //-----Add everything to sortPane
        sortPane.getChildren().add(searchSort);
        sortPane.getChildren().add(universalSort);
        sortPane.getChildren().add(searchText);
        sortPane.getChildren().add(searchField);
        sortPane.getChildren().add(searchButton);
        sortPane.setPadding(new Insets(0, 5, 0, 0));
        //-----Add searchPane to sortPane
        searchPane.setRight(sortPane);
        
        
        //Kiosk Inventory
        //-----Initial Pane Generation
        //----------Pane to hold all media items (mediaPane)
        mediaPane = new VBox(5);
        //----------Pane to hold movie items (moviePane)
        moviePane = new FlowPane(Orientation.HORIZONTAL, 20, 50);
        moviePane.setPadding(new Insets(0, 0, 0, 20));
        moviePane.setRowValignment(VPos.TOP);
        //----------Pane to hold music items (musicPane)
        musicPane = new FlowPane(Orientation.HORIZONTAL, 20, 50);
        musicPane.setPadding(new Insets(0, 0, 0, 20));
        musicPane.setRowValignment(VPos.TOP);
        //----------Pane to hold game items (gamePane)
        gamePane = new FlowPane(Orientation.HORIZONTAL, 20, 50);
        gamePane.setPadding(new Insets(0, 0, 0, 20));
        gamePane.setRowValignment(VPos.TOP);
        //----------Pane to hold book items (bookPane)
        bookPane = new FlowPane(Orientation.HORIZONTAL, 20, 50);
        bookPane.setPadding(new Insets(0, 0, 20, 20));
        bookPane.setRowValignment(VPos.TOP);
        
        //Heading Labels
        //-----Movies (movieLabel)
        movieLabel = new Label("Movies");
        movieLabel.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 40));
        movieLabel.setTextFill(Color.web("#6CA510"));
        //-----Music (musicLabel)
        musicLabel = new Label("Music");
        musicLabel.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 40));
        musicLabel.setTextFill(Color.web("#6CA510"));
        //-----Games (gameLabel)
        gameLabel = new Label("Video Games");
        gameLabel.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 40));
        gameLabel.setTextFill(Color.web("#6CA510"));
        //-----Books (bookLabel)
        bookLabel = new Label("Books");
        bookLabel.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 40));
        bookLabel.setTextFill(Color.web("#6CA510"));
        
        //Movie Icon Generation (MediaButton)
        //-----Movie Icons
        movies.forEach((movie) -> {
            movie.button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                BoxOffice_Main.switchScene("description", movie, "browse");
            });
            moviePane.getChildren().add(movie.button);
        });
        //-----Music Icons
        music.forEach((album) -> {
            album.button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                BoxOffice_Main.switchScene("description", album, "browse");
            });
            musicPane.getChildren().add(album.button);
        });
        //-----Game Icons
        videoGames.forEach((game) -> {
            game.button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                BoxOffice_Main.switchScene("description", game, "browse");
            });
            gamePane.getChildren().add(game.button);
        });
        //-----Book Icons
        books.forEach((book) -> {
            book.button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                BoxOffice_Main.switchScene("description", book, "browse");
            });
            bookPane.getChildren().add(book.button);
        });
        
        //Sort everything in alpha ascending order
        sortAlphaAscending(movies, moviePane);
        sortAlphaAscending(music, musicPane);
        sortAlphaAscending(books, bookPane);
        sortAlphaAscending(videoGames, gamePane);
        
        //Sorting Combo Boxes
        //-----Movie Sorting (movieSort)
        movieSort = new ComboBox<>();
        movieSort.getItems().addAll("Alphabetic Ascending (A-Z)", "Alphabetic Descending (Z-A)", 
                "User Rating (Highest First)", "User Rating (Lowest First)");
        movieSort.setValue("Alphabetic Ascending (A-Z)");
        movieSort.setOnAction(e -> {
            switch (movieSort.getValue()) {
                case "Alphabetic Ascending (A-Z)":
                    sortAlphaAscending(movies, moviePane);
                    break;
                case "Alphabetic Descending (Z-A)":
                    sortAlphaDescending(movies, moviePane);
                    break;
                case "User Rating (Highest First)":
                    sortRatingDescending(movies, moviePane);
                    break;
                case "User Rating (Lowest First)":
                    sortRatingAscending(movies, moviePane);
                    break;
                default:
                    break;
            }
        });
        //-----Music Sorting (musicSort)
        musicSort = new ComboBox<>();
        musicSort.getItems().addAll("Alphabetic Ascending (A-Z)", "Alphabetic Descending (Z-A)", 
                "User Rating (Highest First)", "User Rating (Lowest First)");
        musicSort.setValue("Alphabetic Ascending (A-Z)");
        musicSort.setOnAction(e -> {
            switch (musicSort.getValue()) {
                case "Alphabetic Ascending (A-Z)":
                    sortAlphaAscending(music, musicPane);
                    break;
                case "Alphabetic Descending (Z-A)":
                    sortAlphaDescending(music, musicPane);
                    break;
                case "User Rating (Highest First)":
                    sortRatingDescending(music, musicPane);
                    break;
                case "User Rating (Lowest First)":
                    sortRatingAscending(music, musicPane);
                    break;
                default:
                    break;
            }
        });
        //-----Book Sorting (bookSort)
        bookSort = new ComboBox<>();
        bookSort.getItems().addAll("Alphabetic Ascending (A-Z)", "Alphabetic Descending (Z-A)", 
                "User Rating (Highest First)", "User Rating (Lowest First)");
        bookSort.setValue("Alphabetic Ascending (A-Z)");
        bookSort.setOnAction(e -> {
            switch (bookSort.getValue()) {
                case "Alphabetic Ascending (A-Z)":
                    sortAlphaAscending(books, bookPane);
                    break;
                case "Alphabetic Descending (Z-A)":
                    sortAlphaDescending(books, bookPane);
                    break;
                case "User Rating (Highest First)":
                    sortRatingDescending(books, bookPane);
                    break;
                case "User Rating (Lowest First)":
                    sortRatingAscending(books, bookPane);
                    break;
                default:
                    break;
            }
        });
        //-----Game Sorting (gameSort)
        gameSort = new ComboBox<>();
        gameSort.getItems().addAll("Alphabetic Ascending (A-Z)", "Alphabetic Descending (Z-A)", 
                "User Rating (Highest First)", "User Rating (Lowest First)");
        gameSort.setValue("Alphabetic Ascending (A-Z)");
        gameSort.setOnAction(e -> {
            switch (gameSort.getValue()) {
                case "Alphabetic Ascending (A-Z)":
                    sortAlphaAscending(videoGames, gamePane);
                    break;
                case "Alphabetic Descending (Z-A)":
                    sortAlphaDescending(videoGames, gamePane);
                    break;
                case "User Rating (Highest First)":
                    sortRatingDescending(videoGames, gamePane);
                    break;
                case "User Rating (Lowest First)":
                    sortRatingAscending(videoGames, gamePane);
                    break;
                default:
                    break;
            }
        });
        
        //Set up section headers
        //-----Movie header
        //----------Sorting label (movieSortText)
        movieSortText = new Label("Sort:");
        movieSortText.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 30));
        movieSortText.setTextFill(Color.web("#6CA510"));
        //----------Pane to contain sorting controlls (movieFilter)
        movieFilter = new HBox(5);
        movieFilter.getChildren().add(movieSortText);
        movieFilter.getChildren().add(movieSort);
        movieFilter.setAlignment(Pos.CENTER_LEFT);
        //----------Horizontal rule to act as a barrier between the media icons and the header (movieBreak)
        movieBreak = new Pane();
        movieBreak.setMinHeight(2);
        movieBreak.setMinWidth(BoxOffice_Main.getScreenWidth() - 50);
        movieBreak.setStyle("-fx-background-color: #6CA510");
        //----------Pane to contain entire movie header (movieHead)
        movieHead = new BorderPane();
        movieHead.setPadding(new Insets(0, 0, 0, 20));
        movieHead.setLeft(movieLabel);
        movieHead.setRight(movieFilter);
        movieHead.setBottom(movieBreak);
        
        //-----Music header
        //----------Sorting label (musicSortText)
        musicSortText = new Label("Sort:");
        musicSortText.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 30));
        musicSortText.setTextFill(Color.web("#6CA510"));
        //----------Pane to contain sorting controlls (musicFilter)
        musicFilter = new HBox(5);
        musicFilter.getChildren().add(musicSortText);
        musicFilter.getChildren().add(musicSort);
        musicFilter.setAlignment(Pos.CENTER_LEFT);
        //----------Horizontal rule to act as a barrier between the media icons and the header (musicBreak)
        musicBreak = new Pane();
        musicBreak.setMinHeight(2);
        musicBreak.setMinWidth(BoxOffice_Main.getScreenWidth() - 50);
        musicBreak.setStyle("-fx-background-color: #6CA510");
        //----------Pane to contain entire music header (musicHead)
        musicHead = new BorderPane();
        musicHead.setPadding(new Insets(0, 0, 0, 20));
        musicHead.setLeft(musicLabel);
        musicHead.setRight(musicFilter);
        musicHead.setBottom(musicBreak);
        
        //-----Book header
        //----------Sorting label (bookSortText)
        bookSortText = new Label("Sort:");
        bookSortText.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 30));
        bookSortText.setTextFill(Color.web("#6CA510"));
        //----------Pane to contain sorting controlls (bookFilter)
        bookFilter = new HBox(5);
        bookFilter.getChildren().add(bookSortText);
        bookFilter.getChildren().add(bookSort);
        bookFilter.setAlignment(Pos.CENTER_LEFT);
        //----------Horizontal rule to act as a barrier between the media icons and the header (bookBreak)
        bookBreak = new Pane();
        bookBreak.setMinHeight(2);
        bookBreak.setMinWidth(BoxOffice_Main.getScreenWidth() - 50);
        bookBreak.setStyle("-fx-background-color: #6CA510");
        //----------Pane to contain entire book header (bookHead)
        bookHead = new BorderPane();
        bookHead.setPadding(new Insets(0, 0, 0, 20));
        bookHead.setLeft(bookLabel);
        bookHead.setRight(bookFilter);
        bookHead.setBottom(bookBreak);
        
        //-----Game header
        //----------Sorting label (gameSortText)
        gameSortText = new Label("Sort:");
        gameSortText.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 30));
        gameSortText.setTextFill(Color.web("#6CA510"));
        //----------Pane to contain sorting controlls (gameFilter)
        gameFilter = new HBox(5);
        gameFilter.getChildren().add(gameSortText);
        gameFilter.getChildren().add(gameSort);
        gameFilter.setAlignment(Pos.CENTER_LEFT);
        //----------Horizontal rule to act as a barrier between the media icons and the header (gameBreak)
        gameBreak = new Pane();
        gameBreak.setMinHeight(2);
        gameBreak.setMinWidth(BoxOffice_Main.getScreenWidth() - 50);
        gameBreak.setStyle("-fx-background-color: #6CA510");        
        //----------Pane to contain entire game header (gameHead)
        gameHead = new BorderPane();
        gameHead.setPadding(new Insets(0, 0, 0, 20));
        gameHead.setLeft(gameLabel);
        gameHead.setRight(gameFilter);
        gameHead.setBottom(gameBreak);
        
        //Add items to mediaPane
        mediaPane.getChildren().add(movieHead);
        mediaPane.getChildren().add(moviePane);
        mediaPane.getChildren().add(gameHead);
        mediaPane.getChildren().add(gamePane);
        mediaPane.getChildren().add(musicHead);
        mediaPane.getChildren().add(musicPane);
        mediaPane.getChildren().add(bookHead);
        mediaPane.getChildren().add(bookPane);
        mediaPane.setMinWidth(BoxOffice_Main.getScreenWidth() - 50);
        
        //Put the mediaPane in a scroll pane
        pane = new ScrollPane(mediaPane);
        
        //Add everything to mainPane
        mainPane = new VBox(5);
        mainPane.getChildren().add(menu.getMenu());
        mainPane.getChildren().add(searchPane);
        mainPane.getChildren().add(pane);
        
        //Set up the scene
        scene = new Scene(mainPane, BoxOffice_Main.getScreenWidth(), BoxOffice_Main.getScreenHeight());
    }
  
    /**
     * Method loadMeda loads the kiosk inventory into BoxOffice_Media_Item objects.
     * Note: will eventually be replaced with loading from the database
     */
    private void loadMedia() {
        //Link to the database if not already linked
        if(data == null)
        try {
            connection = DriverManager.getConnection(BoxOffice_Main.getDatabaseURL(), "", "");
            data = connection.createStatement();
            
            //Get the table with the kiosk data
            String selTable = "SELECT * FROM tblMedia";
            data.execute(selTable);
            ResultSet rs = data.getResultSet();
            
            //Read in the data
            rs.next();
            while(!rs.isAfterLast()) {
                //Check the type of media
                switch (rs.getString("Type")) {
                    case "Video":
                        movies.add(new BoxOffice_Media_Item(rs.getString("MediaID"), rs.getString("Type"),
                                rs.getString("MediaName"), rs.getDouble("Rating"), rs.getString("OfficialRating"), rs.getString("ImageURL"),
                                StringEscapeUtils.unescapeJava(rs.getString("Summary")), rs.getString("Artist"), 
                                rs.getString("Category1"), rs.getString("Category2"), rs.getString("Category3"), rs.getDouble("RentalPrice")));
                        break;
                    case "Game":
                        videoGames.add(new BoxOffice_Media_Item(rs.getString("MediaID"), rs.getString("Type"),
                                rs.getString("MediaName"), rs.getDouble("Rating"), rs.getString("OfficialRating"), rs.getString("ImageURL"),
                                StringEscapeUtils.unescapeJava(rs.getString("Summary")), rs.getString("Artist"), 
                                rs.getString("Category1"), rs.getString("Category2"), rs.getString("Category3"), rs.getDouble("RentalPrice")));
                        break;
                    case "Music":
                        music.add(new BoxOffice_Media_Item(rs.getString("MediaID"), rs.getString("Type"),
                                rs.getString("MediaName"), rs.getDouble("Rating"), rs.getString("OfficialRating"), rs.getString("ImageURL"),
                                StringEscapeUtils.unescapeJava(rs.getString("Summary")), rs.getString("Artist"), 
                                rs.getString("Category1"), rs.getString("Category2"), rs.getString("Category3"), rs.getDouble("RentalPrice")));
                        break;
                    case "Audiobook":
                        books.add(new BoxOffice_Media_Item(rs.getString("MediaID"), rs.getString("Type"),
                                rs.getString("MediaName"), rs.getDouble("Rating"), rs.getString("OfficialRating"), rs.getString("ImageURL"),
                                StringEscapeUtils.unescapeJava(rs.getString("Summary")), rs.getString("Artist"), 
                                rs.getString("Category1"), rs.getString("Category2"), rs.getString("Category3"), rs.getDouble("RentalPrice")));
                        break;
                    default:
                        break;
                }
                
                //Move to the next record
                rs.next();
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Method sortAlphaAscending sorts the contents of the selected pane into Alphabetic Ascending order by title
     * @param mediaList the list containing the BoxOffice_Media_Items to be sorted
     * @param mediaPane the pane containing the MediaButtons to be sorted
     */
    private void sortAlphaAscending(ArrayList<BoxOffice_Media_Item> mediaList, FlowPane mediaPane) {
        //Variables
        BoxOffice_Media_Item temp;
        boolean swap = true;
        
        //Loop through media array sorting in alpha ascending
        while(swap) {
            swap = false;
            for(int i = 0; i < mediaList.size() - 1; i++) {
                if(mediaList.get(i).searchTitle.compareTo(mediaList.get(i+1).searchTitle) > 0) {
                    temp = mediaList.get(i);
                    mediaList.set(i, mediaList.get(i+1));
                    mediaList.set(i+1, temp);
                    swap = true;
                }
            }
        }

        //Clear the buttons in the moviePane and replace them
        mediaPane.getChildren().clear();
        mediaList.forEach((item) -> {
            mediaPane.getChildren().add(item.button);
        });
    }
    
    /**
     * Method sortAlphaDescending sorts the contents of the selected pane into Alphabetic Descending order by title
     * @param mediaList the list containing the BoxOffice_Media_Items to be sorted
     * @param mediaPane the pane containing the MediaButtons to be sorted
     */
    private void sortAlphaDescending(ArrayList<BoxOffice_Media_Item> mediaList, FlowPane mediaPane) {
        //Variables
        BoxOffice_Media_Item temp;
        boolean swap = true;
        
        //Loop through array sorting in alpha descending
        while(swap) {
            swap = false;
            for(int i = 0; i < mediaList.size() - 1; i++) {
                if(mediaList.get(i).searchTitle.compareTo(mediaList.get(i+1).searchTitle) < 0) {
                    temp = mediaList.get(i);
                    mediaList.set(i, mediaList.get(i+1));
                    mediaList.set(i+1, temp);
                    swap = true;
                }
            }
        }

        //Clear the buttons in the pane and replace them
        mediaPane.getChildren().clear();
        mediaList.forEach((item) -> {
            mediaPane.getChildren().add(item.button);
        });
    }
    
    /**
     * Method sortRatingAscending() sorts the contents of the selected pane into ascending order by user rating
     * @param mediaList the list containing the BoxOffice_Media_Items to be sorted
     * @param mediaPane the pane containing the MediaButtons to be sorted
     */
    private void sortRatingAscending(ArrayList<BoxOffice_Media_Item> mediaList, FlowPane mediaPane) {
        //Variables
        BoxOffice_Media_Item temp;
        boolean swap = true;
        
        //Loop through media array sorting in rating ascending
        while(swap) {
            swap = false;
            for(int i = 0; i < mediaList.size() - 1; i++) {
                if(mediaList.get(i).starRating > mediaList.get(i+1).starRating) {
                    temp = mediaList.get(i);
                    mediaList.set(i, mediaList.get(i+1));
                    mediaList.set(i+1, temp);
                    swap = true;
                }
            }
        }

        //Clear the buttons in the pane and replace them
        mediaPane.getChildren().clear();
        mediaList.forEach((item) -> {
            mediaPane.getChildren().add(item.button);
        });
    }
    
    /**
     * Method sortRatingDescending() sorts the contents of the selected pane into descending order by user rating
     * @param mediaList the list containing the BoxOffice_Media_Items to be sorted
     * @param mediaPane the pane containing the MediaButtons to be sorted
     */
    private void sortRatingDescending(ArrayList<BoxOffice_Media_Item> mediaList, FlowPane mediaPane) {
        //Variables
        BoxOffice_Media_Item temp;
        boolean swap = true;
        
        //Loop through array sorting in rating descending
        while(swap) {
            swap = false;
            for(int i = 0; i < mediaList.size() - 1; i++) {
                if(mediaList.get(i).starRating < mediaList.get(i+1).starRating) {
                    temp = mediaList.get(i);
                    mediaList.set(i, mediaList.get(i+1));
                    mediaList.set(i+1, temp);
                    swap = true;
                }
            }
        }

        //Clear the buttons in the pane and replace them
        mediaPane.getChildren().clear();
        mediaList.forEach((item) -> {
            mediaPane.getChildren().add(item.button);
        });
    }
    
    /**
     * Method searchMedia navigates to BoxOffice_Search_Page and provides it with the keyword to search for
     * @param search the term the kiosk inventory is to be searched for
     */
    private void searchMedia(String search) {
        BoxOffice_Main.switchScene("search", search, "browse");
    }
    
    /**
     * Method toggleStock toggles the display of the image buttons if they are in stock
     */
    private void toggleStock() {
        movies.forEach((item) -> { 
            if(BoxOffice_Kiosk_Data.checkStock(item.mediaID))
                item.button.toggleStock(true);
            else
                item.button.toggleStock(false);
        });
        music.forEach((item) -> { 
            if(BoxOffice_Kiosk_Data.checkStock(item.mediaID))
                item.button.toggleStock(true);
            else
                item.button.toggleStock(false);
        });
        videoGames.forEach((item) -> { 
            if(BoxOffice_Kiosk_Data.checkStock(item.mediaID))
                item.button.toggleStock(true);
            else
                item.button.toggleStock(false);
        });
        books.forEach((item) -> { 
            if(BoxOffice_Kiosk_Data.checkStock(item.mediaID))
                item.button.toggleStock(true);
            else
                item.button.toggleStock(false);
        });
    }
    
    //Variables
    //-----Scroll panes
    private ScrollPane pane; //Holds everything to allow it to scroll
    //-----VBoxes
    private VBox mediaPane; //Holds the section headings and buttons
    private VBox mainPane; //The main pane that displays all items
    //-----HBoxes
    private HBox movieFilter, musicFilter, gameFilter, bookFilter; //Holds the sorting labels and combo boxes
    private HBox sortPane; //The pane that holds the search box and the universal sorting filter
    //-----FlowPanes
    private FlowPane moviePane, musicPane, gamePane, bookPane; //Area to hold inventory buttons
    //-----Border Panes
    private BorderPane movieHead, musicHead, bookHead, gameHead; //Section headings
    private BorderPane searchPane; //Pane to hold the search and universal sort functions
    //-----Scenes
    private Scene scene; //The main scene
    //-----Panes
    private Pane movieBreak, musicBreak, gameBreak, bookBreak; //Horizontal breaks for section headers
    //-----Labels
    private Label movieLabel, musicLabel, gameLabel, bookLabel; //Labels for the section headings
    private Label movieSortText, musicSortText, gameSortText, bookSortText, searchSort; //Sorting labels
    private Label searchText;
    //-----Combo boxes
    private ComboBox<String> movieSort, musicSort, bookSort, gameSort, universalSort; //Combo boxes to hold the sorting of each media section
    //-----Text fields
    private TextField searchField;
    //-----Buttons
    private ImageButton searchButton;
    //-----Lists
    private final ArrayList<BoxOffice_Media_Item> movies = new ArrayList(); //All the movies in the kiosk
    private final ArrayList<BoxOffice_Media_Item> music = new ArrayList(); //All the music in the kiosk
    private final ArrayList<BoxOffice_Media_Item> videoGames = new ArrayList(); //All the games in the kiosk
    private final ArrayList<BoxOffice_Media_Item> books = new ArrayList(); //All the books in the kiosk
    //-----Menu
    private BoxOffice_Menu menu;
    //-----Database access
    private Statement data;
    private Connection connection;
}

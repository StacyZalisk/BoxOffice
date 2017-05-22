/**
 * -----BoxOffice Media Rentals: Kiosk Software
 * -----BoxOffice Media Button
 * @author      Stacy Zalisk
 * @date        4/17/2017
 * @version     1.3
 * Purpose:     A custom button class for the media icons on the browsing
*               and search pages
 * Revisions:   Added overlay if the item is out of stock
 */

package BoxOffice;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MediaButton extends Parent {

    /**
     * Constructor MediaButton creates a media button with the provided information
     * @param url the URL of the image to be used for the button
     * @param mediaTitle the title of the media item
     * @param mediaType the type of the media item (movie, book, ect.)
     */
    public MediaButton(String url, String mediaTitle, String mediaType) {
        //Media image
        image = new Image(url);
        buttonImage = new ImageView(image);
        //Item is an album cover
        if(mediaType.equals("Music"))  {
            buttonImage.setFitHeight(150);
            buttonImage.setFitWidth(150);
            stockImage = new Image("images/outOfStockAlbumOverlay.png");
        }
        //Everything else
        else {
            buttonImage.setFitHeight(225);
            buttonImage.setFitWidth(150);
            stockImage = new Image("images/outOfStockOverlay.png");
        }
        
        stockOverlay = new ImageView(stockImage);
        stockOverlay.setVisible(false);
        
        //Image pane to enable highlighting
        imagePane = new StackPane();
        imagePane.getChildren().add(buttonImage);
        imagePane.getChildren().add(stockOverlay);

        imagePane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        imagePane.setPadding(new Insets(3, 3, 3, 3));
        
        //Media title
        title = new Label(mediaTitle);
        title.setPadding(new Insets(0, 0, 10, 0));
        title.setTextFill(Color.BLACK);
        title.setMaxWidth(156);
        title.setWrapText(true);
        title.setFont(Font.font("Trebuchet MS", 15));
        
        mediaPane = new VBox(5);
        mediaPane.setPadding(new Insets(-15, 0, 5, 0));
        mediaPane.getChildren().add(imagePane);
        mediaPane.getChildren().add(title);
        
        mediaPane.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            imagePane.setBackground(new Background(new BackgroundFill(Color.web("#6CA510"), CornerRadii.EMPTY, Insets.EMPTY)));
            title.setTextFill(Color.web("#6CA510"));
        });
        
        mediaPane.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            imagePane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
            title.setTextFill(Color.BLACK);
        });
        
        this.getChildren().add(mediaPane);
    }
    
    /**
     * Toggles the out of stock overlay
     * @param inStock whether or not the item is in stock
     */
    public void toggleStock(boolean inStock) {
        if(inStock)
            stockOverlay.setVisible(false);
        else
            stockOverlay.setVisible(true);
    }
    
    public StackPane imagePane; //Pane to hold the image
    private final Image image, stockImage; //The image
    public Label title; //The title of the media item
    public VBox mediaPane; //the pane that hold the button
    private ImageView buttonImage, stockOverlay; //The actual button
}

/*******************************************************************************
--------BoxOffice Media Rentals: Kiosk Software--------
* File:         ImageButton.java
* Author:       Stacy Zalisk
* Date:         3/4/2017
* Purpose:      A custom button class to show a button with images
* Version:      1.0
* Revisions:    Create custom image button
*******************************************************************************/
package BoxOffice;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ImageButton extends Parent {

    private Image regImage;
    private Image highImage;

    public ImageView buttonImage;

    public ImageButton(String regURL, String highURL) {
        regImage = new Image(regURL);
        highImage = new Image(highURL);
        buttonImage = new ImageView(regImage);
        
        this.getChildren().add(buttonImage);

        buttonImage.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            buttonImage.setImage(highImage);
        });
        
        buttonImage.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            buttonImage.setImage(regImage);
        }); 
    } 

}

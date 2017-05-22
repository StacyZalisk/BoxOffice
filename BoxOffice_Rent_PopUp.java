/**
 * -----BoxOffice Media Rentals: Kiosk Software
 * -----BoxOffice Rent PopUp
 * @author       Stacy Zalisk
 * @date         4/30/2017
 * @version      1.1
 * Purpose:      Window to guide user through the rental process
 * Revisions:    Actually implemented class
 */
package BoxOffice;

//Imports
//-----Graphic Imports
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class BoxOffice_Rent_PopUp {
    
    /**
     * Method createLoginWindow creates the login window from scratch
     */
    public void createRentalPopUp() {
        //Create the various pages of the rental process
        createCreditScreen();
        createSuccessScreen();
        
        //Username label
        username = new Label("Username:");
        username.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 15));
        username.setTextFill(Color.web("#6CA510"));
        
        //Password label
        password = new Label("Password:");
        password.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 15));
        password.setTextFill(Color.web("#6CA510"));
        
        //Error Message
        errorMsg = new Label("");
        errorMsg.setFont(Font.font("Trebuchet MS", FontPosture.ITALIC, 15));
        errorMsg.setTextFill(Color.RED);
        errorMsg.setWrapText(true);
        
        //Username field
        userField = new TextField();
        
        //Password field
        passField = new PasswordField();
        
        //Login pane to hold the login fields and labels
        loginInfo = new GridPane();
        loginInfo.setHgap(10);
        loginInfo.setVgap(10);
        loginInfo.setMinWidth(240);
        loginInfo.setMaxWidth(240);
        loginInfo.add(username, 0, 0);
        loginInfo.add(password, 0, 1);
        loginInfo.add(userField, 1, 0);
        loginInfo.add(passField, 1, 1);
        GridPane.setHalignment(username, HPos.RIGHT);
        GridPane.setHalignment(password, HPos.RIGHT);
        
        //Button pane to hold the buttons
        buttons = new BorderPane();
        buttons.setMaxWidth(200);
        buttons.setMinWidth(200);
        
        //Content Pane to hold everything
//        contentPane = new VBox(10);
//        contentPane.setAlignment(Pos.CENTER);
//        contentPane.setPadding(new Insets(10, 10, 10, 10));
//        contentPane.getChildren().add(header);
//        contentPane.getChildren().add(loginInfo);
//        contentPane.getChildren().add(errorMsg);
//        contentPane.getChildren().add(buttons);
//        contentPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
//        contentPane.setMaxSize(450, 275);
//        contentPane.setMinSize(450, 275);
//        contentPane.setStyle("-fx-effect: dropshadow(gaussian, #6CA510, 50, 0, 0, 0);");
        
        mainPane = new StackPane();
        mainPane.setAlignment(Pos.CENTER);
        mainPane.setMinSize(BoxOffice_Main.getScreenWidth(), BoxOffice_Main.getScreenHeight());
        mainPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);" );
        mainPane.getChildren().add(creditEnterPane);
        mainPane.getChildren().add(successPane);
        
        //Scene
        scene = new Scene(mainPane);
        scene.setFill(Color.TRANSPARENT);
        
        //Create main stage
        mainStage = new Stage();
        mainStage.setScene(scene);
        mainStage.initStyle(StageStyle.TRANSPARENT);
        mainStage.setX(0);
        mainStage.setY(0);
        mainStage.setAlwaysOnTop(true);
    }
    
    private void createCreditScreen() {
        //Credit card header
        Label creditHeader = new Label("Plese Insert Credit Card");
        creditHeader.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 30));
        creditHeader.setTextFill(Color.web("#6CA510"));
        creditHeader.setPadding(new Insets(0, 0, 10, 0));
        creditHeader.setWrapText(true);
        creditHeader.setTextAlignment(TextAlignment.CENTER);
        
        //Credit Card Image
        ImageView creditView = new ImageView(new Image ("images/creditReader.png"));
        
        //Cancel button
        ImageButton cancel = new ImageButton("images/cancelBtnReg.png", "images/cancelBtnHigh.png");
        cancel.buttonImage.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            mainStage.close();
            cancelFlag = true;
        });
        
        //Next button
        ImageButton next = new ImageButton("images/nextBtnReg.png", "images/nextBtnHigh.png");
        next.buttonImage.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            creditEnterPane.setVisible(false);
            successPane.setVisible(true);
            beginRentalProcess();
        });
        
        //Button pane
        HBox buttonPane = new HBox(25);
        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.getChildren().add(cancel);
        buttonPane.getChildren().add(next);
        
        //Create the pane
        creditEnterPane = new VBox(10);
        creditEnterPane.setAlignment(Pos.CENTER);
        creditEnterPane.setPadding(new Insets(10, 10, 10, 10));
        creditEnterPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        creditEnterPane.setMaxSize(500, 300);
        creditEnterPane.setMinSize(500, 300);
        creditEnterPane.setStyle("-fx-effect: dropshadow(gaussian, #6CA510, 50, 0, 0, 0);");
        
        //Add everything to the pane
        creditEnterPane.getChildren().add(creditHeader);
        creditEnterPane.getChildren().add(creditView);
        creditEnterPane.getChildren().add(buttonPane);
    }
    
    private void createSuccessScreen() {
        //Title
        Label title = new Label("Rental Successful!");
        title.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 50));
        title.setTextFill(Color.web("#6CA510"));
        title.setPadding(new Insets(0, 0, 10, 0));
        title.setWrapText(true);
        title.setTextAlignment(TextAlignment.CENTER);
        
        //Due date
        //Get the current date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate localDate = LocalDate.now();
        localDate = localDate.plusWeeks(2);
        String dateString = dtf.format(localDate);
        //Set the label
        Label dueDate = new Label("Your Items are Due On: " + dateString);
        dueDate.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 25));
        dueDate.setPadding(new Insets(0, 0, 10, 0));
        dueDate.setTextFill(Color.web("#6CA510"));
        
        //Button
        ImageButton exitButton = new ImageButton("images/exitBtnReg.png", "images/exitBtnHigh.png");
        exitButton.buttonImage.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            mainStage.close();
        });
        
        //Create the pane
        successPane = new VBox(10);
        successPane.setAlignment(Pos.CENTER);
        successPane.setPadding(new Insets(10, 10, 10, 10));
        successPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        successPane.setMaxSize(500, 300);
        successPane.setMinSize(500, 300);
        successPane.setStyle("-fx-effect: dropshadow(gaussian, #6CA510, 50, 0, 0, 0);");
        
        //Add everything to the pane
        successPane.getChildren().add(title);
        successPane.getChildren().add(dueDate);
        successPane.getChildren().add(exitButton);
    }
    
    /**
     * Method showRentalPopUp shows the rental popup and guides the user through the rental process
     * @param originPage the page that spawned the rental popup
     */
    public void showRentalPopUp(BoxOffice_Cart_Page originPage) {
        successPane.setVisible(false);
        creditEnterPane.setVisible(true);
        mainStage.show();
        cancelFlag = false;
        done = false;
        page = originPage;
    }
    
    /**
     * Method beginRentalProcess begins the rental process
     */
    public void beginRentalProcess() {
        BoxOffice_Cart.rentAllItems();
        BoxOffice_Cart.emptyCart();
        page.loadContent();
    }
    
    /**
     * Overloaded method showLoginWindow displays the login windows with "welcome" replaced with message
     * @param menuOfOrigin the menu that spawned the login window
     * @param message the message to be displayed in the window
     */
//    public void showLoginWindow(BoxOffice_Menu menuOfOrigin, String message) {
//        menu = menuOfOrigin;
//        userField.setText("");
//        passField.setText("");
//        header.setText(message);
//        errorMsg.setText("");
//        mainStage.show();
//    }
    
    /**
     * Method updateLoginWindows updates the login window when the login button is pressed
     * @param error the error code of the login process (0 is success {no error}, 1 is incorrect username, 2 is incorrect password)
     */
//    public void updateLoginWindow(int error) {
//        //What error happened
//        switch(error) {
//            case 0:     //Noting wrong
//                errorMsg.setText("");
//                mainStage.close();
//                menu.updateMenu();
//                break;
//            case 1:     //Wrong username
//                errorMsg.setText("Username \"" + userField.getText() + "\" does not exist");
//                break;
//            case 2:     //Wrong password
//                errorMsg.setText("Password is incorrect");
//                break;
//        }
//    }
    
    //Variables
    //-----Stages
    public Stage mainStage; //Stage to show login information
    //-----Labels
    private Label username, password; //Labels for the credit info page
    private Label errorMsg; //Error message to be displayed
    //-----Text fields
    private TextField userField; //Where the username is entered
    private PasswordField passField; //Where the password is entered
    //-----Image buttons
    //private ImageButton cancel; //The buttons to log in and out
    //-----Panes
    private GridPane loginInfo; //The pane that holds all the login fields and lables
    private VBox creditEnterPane, successPane; //The pane that hold everything
    private BorderPane buttons; //The pane to hold the login and cancel buttons
    private StackPane mainPane;  //Hold the login window
    //-----Scene
    private Scene scene; //The scene that displays everything
    //-----Booleans
    private Boolean cancelFlag, done;
    //-----Cart Pages
    private BoxOffice_Cart_Page page; //The menu that the window was spanned from
}

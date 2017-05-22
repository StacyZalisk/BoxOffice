/**
 * -----BoxOffice Media Rentals: Kiosk Software
 * -----BoxOffice Login Window
 * @author       Stacy Zalisk
 * @date         5/05/2017
 * @version      1.3
 * Purpose:      Pop Up window for when a user logins in
 * Revisions:    Added checks for blank password and username field
 */
package BoxOffice;

//Imports
//-----Graphic Imports
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class BoxOffice_Login_Window {
    
    /**
     * Method createLoginWindow creates the login window from scratch
     */
    public void createLoginWindow() {
        //Username label
        username = new Label("Username:");
        username.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 15));
        username.setTextFill(Color.web("#6CA510"));
        
        //Password label
        password = new Label("Password:");
        password.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 15));
        password.setTextFill(Color.web("#6CA510"));
        
        //Window header
        header = new Label("Enter Login Info");
        header.setFont(Font.font("Trebuchet MS", FontWeight.BOLD, 25));
        header.setTextFill(Color.web("#6CA510"));
        header.setPadding(new Insets(0, 0, 10, 0));
        header.setWrapText(true);
        header.setTextAlignment(TextAlignment.CENTER);
        
        //Error Message
        errorMsg = new Label("");
        errorMsg.setFont(Font.font("Trebuchet MS", FontPosture.ITALIC, 15));
        errorMsg.setTextFill(Color.RED);
        errorMsg.setWrapText(true);
        
        //Username field
        userField = new TextField();
        
        //Password field
        passField = new PasswordField();
        
         //Login button
        login = new ImageButton("images/loginBtnReg.png", "images/loginBtnHigh.png");
        login.buttonImage.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            if(userField.getText().isEmpty())
                updateLoginWindow(3);
            else if(passField.getText().isEmpty())
                updateLoginWindow(4);
            else         
                BoxOffice_Account.logInUser(userField.getText(), passField.getText(), this);
        });
        
        //Cancel button
        cancel = new ImageButton("images/cancelBtnReg.png", "images/cancelBtnHigh.png");
        cancel.buttonImage.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            mainStage.close();
        });
        
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
        buttons.setLeft(cancel);
        buttons.setRight(login);
        
        //Content Pane to hold everything
        contentPane = new VBox(10);
        contentPane.setAlignment(Pos.CENTER);
        contentPane.setPadding(new Insets(10, 10, 10, 10));
        contentPane.getChildren().add(header);
        contentPane.getChildren().add(loginInfo);
        contentPane.getChildren().add(errorMsg);
        contentPane.getChildren().add(buttons);
        contentPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        contentPane.setMaxSize(450, 275);
        contentPane.setMinSize(450, 275);
        contentPane.setStyle("-fx-effect: dropshadow(gaussian, #6CA510, 50, 0, 0, 0);");
        
        mainPane = new StackPane();
        mainPane.setAlignment(Pos.CENTER);
        mainPane.setMinSize(BoxOffice_Main.getScreenWidth(), BoxOffice_Main.getScreenHeight());
        mainPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);" );
        mainPane.getChildren().add(contentPane);
        
        //Scene
        scene = new Scene(mainPane);
        scene.setFill(Color.TRANSPARENT);
        
        //Create main stage
        mainStage = new Stage();
        mainStage.setScene(scene);
        mainStage.initStyle(StageStyle.TRANSPARENT);
        mainStage.setX(0);
        mainStage.setY(0);
    }
    
    /**
     * Overloaded method showLoginWindow displays the default login window
     * @param menuOfOrigin the menu that spawned the login window
     */
    public void showLoginWindow(BoxOffice_Menu menuOfOrigin) {
        menu = menuOfOrigin;
        userField.setText("");
        passField.setText("");
        header.setText("Enter Login Info");
        errorMsg.setText("");
        userField.home();
        mainStage.show();
    }
    
    /**
     * Overloaded method showLoginWindow displays the login windows with "welcome" replaced with message
     * @param menuOfOrigin the menu that spawned the login window
     * @param message the message to be displayed in the window
     */
    public void showLoginWindow(BoxOffice_Menu menuOfOrigin, String message) {
        menu = menuOfOrigin;
        userField.setText("");
        passField.setText("");
        header.setText(message);
        errorMsg.setText("");
        mainStage.show();
    }
    
    /**
     * Method updateLoginWindows updates the login window when the login button is pressed
     * @param error the error code of the login process (0 is success {no error}, 1 is incorrect username, 2 is incorrect password)
     */
    public void updateLoginWindow(int error) {
        //What error happened
        switch(error) {
            case 0:     //Noting wrong
                errorMsg.setText("");
                mainStage.close();
                menu.updateMenu();
                break;
            case 1:     //Wrong username
                errorMsg.setText("Username \"" + userField.getText() + "\" does not exist");
                break;
            case 2:     //Wrong password
                errorMsg.setText("Password is incorrect");
                break;
            case 3:     //Blank username
                errorMsg.setText("Username cannot be left blank");
                break;
            case 4:     //Blank password
                errorMsg.setText("Password cannot be left blank");
                break;
        }
    }
    
    //Variables
    //-----Stages
    public Stage mainStage; //Stage to show login information
    //-----Labels
    private Label username, password, header; //Labels for the login fields
    private Label errorMsg; //Error message to be displayed
    //-----Text fields
    private TextField userField; //Where the username is entered
    private PasswordField passField; //Where the password is entered
    //-----Image buttons
    private ImageButton login, cancel; //The buttons to log in and out
    //-----Panes
    private GridPane loginInfo; //The pane that holds all the login fields and lables
    private VBox contentPane; //The pane that hold everything
    private BorderPane buttons; //The pane to hold the login and cancel buttons
    private StackPane mainPane;  //Hold the login window
    //-----Scene
    private Scene scene; //The scene that displays everything
    //-----Menu
    private BoxOffice_Menu menu; //The menu that the window was spanned from
}

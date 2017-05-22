/**
 * -----BoxOffice Media Rentals: Kiosk Software
 * -----BoxOffice Boot Page
 * @author      Stacy Zalisk
 * @date        4/28/2017
 * @version     1.0.1
 * Purpose:     Initial boot page that links the Kiosk to it's Kiosk ID and database
 * Revisions:   Added call to initialize account.  Switched welcome message to logo.
 */
package BoxOffice;

//Imports
//-----Graphic Imports
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
//-----Database Access
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BoxOffice_Boot_Page {
    
    /**
     * Method getScene generates and returns the BoxOffice_Boot_Page scene
     * @return scene The scene that contains the BoxOffice_Boot_Page
     */
    public Scene getScene() {
        //If the scene doesn't exist, create it
        if(scene == null) {
            createScene();
        }
        
        //Return the scene
        return scene;
    }
    
    /**
     * Method createScene creates the BoxOffice_Account_Page scene from scratch
     */
    private void createScene() { 
        
        //Labels
        //-----ID field label
        Label idLabel = new Label("Kiosk ID:");
        idLabel.setFont(Font.font("Trebuchet MS", 30));
        idLabel.setTextFill(Color.web("#6CA510"));
        //-----Database field label
        Label databaseLabel = new Label("Database URL:");
        databaseLabel.setFont(Font.font("Trebuchet MS", 30));
        databaseLabel.setTextFill(Color.web("#6CA510"));
        //-----Database hint label
        Label databaseHint = new Label("Database URL should use forward slashes (/)");
        databaseHint.setFont(Font.font("Trebuchet MS", 25));
        //-----Error Label
        errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("Trebuchet MS", 30));
        
        //Images
        Image logo = new Image("images/BoxOfficeLogo.png");
        ImageView logoHolder = new ImageView(logo);
        
        //Fields
        //-----IDField
        IDField = new TextField();
        IDField.setFont(Font.font("Trebuchet MS", 30));
        IDField.setPrefWidth(750);
        //-----databaseField
        databaseField = new TextField();
        databaseField.setFont(Font.font("Trebuchet MS", 30));
        databaseField.setPrefWidth(750);
        
        //-----Buttons
        validate = new ImageButton("images/validateBtnReg.png", "images/validateBtnHigh.png");
        validate.buttonImage.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            validate();
        });
        
        //Data Pane
        dataPane = new GridPane();
        dataPane.setHgap(10);
        dataPane.setVgap(10);
        dataPane.setAlignment(Pos.CENTER);
        dataPane.add(idLabel, 0, 0);
        dataPane.add(databaseLabel, 0, 1);
        dataPane.add(IDField, 1, 0);
        dataPane.add(databaseField, 1, 1);
        GridPane.setHalignment(idLabel, HPos.RIGHT);
        GridPane.setHalignment(databaseLabel, HPos.RIGHT);
        
        //Main Pane to hold everything
        mainPane = new VBox(20);
        mainPane.setAlignment(Pos.CENTER);
        mainPane.getChildren().add(logoHolder);
        mainPane.getChildren().add(dataPane);
        mainPane.getChildren().add(databaseHint);
        mainPane.getChildren().add(validate);
        mainPane.getChildren().add(errorLabel);
        
        //Create the Scene
        scene = new Scene(mainPane, BoxOffice_Main.getScreenWidth(), BoxOffice_Main.getScreenHeight());
    }
    
    private void validate() {
        String temp;
        errorLabel.setText("Validating...");
        //Check the databaseURL if not already connected
        if(!database) {
            //Get the URL
            temp = "jdbc:ucanaccess://" + databaseField.getText();
            //Attempt to connect
            try {
                connection = DriverManager.getConnection(temp, "", "");
                data = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

                //Get the table with the kiosk ID
                String selTable = "SELECT * FROM tblKiosk";
                data.execute(selTable);
                kioskIDs = data.getResultSet();
                
                //Clear error label
                errorLabel.setText("");
                
                //Record database URL
                BoxOffice_Main.setDatabaseURL(temp);
                database = true;
            }
            catch(Exception ex) {
                errorLabel.setText("Invalid Database URL");
            }
        }
        
        //Check the KioskID
        if(database) {
            temp = IDField.getText();
            try {
                kioskIDs.first();
                //Scroll through all the kiosk IDS until you find a match
                while(true) {
                    if(kioskIDs.getString("KioskID").equals(temp)) {
                        errorLabel.setText("Loading...");
                        //Set the Kiosk ID
                        BoxOffice_Main.setKioskID(temp);
                        //Load the Kiosk's Data
                        BoxOffice_Kiosk_Data.loadData(temp);                     
                        //Initilize kiosk account
                        BoxOffice_Account.initializeAccount();
                        break;
                    }   
                    
                    kioskIDs.next();
                }
            }
            catch(Exception ex) {
                errorLabel.setText("Invalid Kiosk ID");
            }
            
            if(BoxOffice_Main.getKioskID().equals(""))
                errorLabel.setText("Invalid Kiosk ID");
        }
    }
    
    //Variables
    //-----Scenes
    private Scene scene; //The main scene
    //-----VBoxes
    private VBox mainPane; //Pane that holds everything;
    //-----GridPanes
    private GridPane dataPane; //Pane that hold the kiosk data fields
    //-----Fields
    private TextField IDField;
    private TextField databaseField;
    //-----Labels
    private Label errorLabel;
    //-----Image Buttons
    private ImageButton validate;
    //-----Booleans
    private boolean database = false;
    //-----Database access
    private Statement data;
    private Connection connection;
    private ResultSet kioskIDs;
}

package View;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class responsible for the properties window
 * @author Roee Sanker & Yossi Hekter
 */
public class Prop {

    //FXML
    //The text area of the text
    public TextArea textArea;

    /**
     * This present the properties window
     */
    public void showProperties(){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Properties");
        window.setWidth(260);
        window.setHeight(150);
        window.setResizable(false);
        Pane pane = new Pane();
        readProp();
        pane.getChildren().addAll(textArea);
        Scene scene = new Scene(pane, 250, 150);
        window.setScene(scene);
        window.showAndWait();
    }

    /**
     * This read the properties from the configuration file
     */
    public void readProp(){
        String ans ="The maze properties are\n\n";
        try {
            String filename = "config.properties";
            InputStream input2 = Prop.class.getClassLoader().getResourceAsStream(filename);

            if(input2 == null){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Error");
                alert.setHeaderText("There is no properties file");
            }
            else{
                Properties prop = new Properties();
                prop.load(input2);

                //init the maze generetor
                String tmpString = prop.getProperty("mazeGenerator");
                if (tmpString != null){
                    ans += "The maze generate by: "+ tmpString + "\n";
                }else
                    ans += "The maze generate by: MyMazeGenerator\n";

                //init the solving algorithm
                tmpString = prop.getProperty("solvingAlgorithm");
                if (tmpString != null){
                    ans += "The Solvig algorithm is: " + tmpString + "\n";
                }
                else
                    ans += "The Solvig algorithm is: BestFirstSearch\n";
                textArea = new TextArea(ans);
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}

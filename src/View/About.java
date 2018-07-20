package View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class responsible for the saving window
 * @author Yossi Hekter
 */
public class About {

    /**
     * This present the about window
     */
    public void display(){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("About");
        window.setMinWidth(350);
        window.setMinHeight(200);
        window.setResizable(false);

        //FXML load
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = null;
        try {
            root = fxmlLoader.load(getClass().getResource("AboutView.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root, 350, 200);
        window.setScene(scene);
        window.showAndWait();
    }
}

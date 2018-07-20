package View;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Optional;

/**
 * This class responsible for the generate box window
 * @author Yossi Hekter
 */
public class GenerateBox {

    /**
     * The size of the maze that needs to be generate
     */
    public static int [] ans = {10, 10};;
    /**
     * A variable that says whether a maze production request has been received
     */
    public static boolean generated = false;


    /**
     * The combo box to choose level
     */
    public javafx.scene.control.ComboBox comboBox_levels;
    public javafx.scene.control.CheckBox checkBox;

    public  Stage window;
    /**
     * This present the generate box window
     */
    public int[] display(){
        window = new Stage();
        window.setResizable(false);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Generate");
        window.setWidth(300);
        window.setHeight(250);

        //FXML load
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = null;
        try {
            root = fxmlLoader.load(getClass().getResource("GenerateView.fxml").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root, 400, 300);
        scene.getStylesheets().add(getClass().getResource("ViewStyle.css").toExternalForm());
        window.setScene(scene);
        SetStageCloseEvent(window);;
        window.showAndWait();
        return ans;
    }

    //set on action
    public void generateMaze(Event event){
        if(comboBox_levels.getSelectionModel().getSelectedItem()==null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Please choose a level");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
                alert.close();
        }
        else
        {
            setMazeSize();
            if(checkBox.isSelected()){

            }
            //window.close();
            final Node source = (Node) event.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        }
    }

    private void setMazeSize(){
        String level = comboBox_levels.getSelectionModel().getSelectedItem().toString();
        generated = true;
        switch (level) {
            case "Easy":
                ans[0] = 10;
                ans[1] = 10;
                break;
            case "Medium":
                ans[0] = 25;
                ans[1] = 25;
                break;
            case "Hard":
                ans[0] = 50;
                ans[1] = 50;
                break;
            case "Crazy":
                ans[0] = 150;
                ans[1] = 150;
                break;
        }
    }

    /**
     * This function handle press the "Exit" button
     * @param primaryStage The stage of the generate box
     */
    public static void SetStageCloseEvent(Stage primaryStage) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                primaryStage.close();
            }
        });
    }
}

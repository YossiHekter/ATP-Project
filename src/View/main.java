package View;

import Model.*;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;

/**
 * This class is the main call that start the app
 * @author Roee Sanker & Yossi Hekter
 */
public class main extends Application {

    /**
     * This function start the app and init all the classes that needed
     * @param primaryStage The stage of the ap
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        MyModel myModel = new MyModel();
        myModel.startServers();
        MyViewModel myViewModel = new MyViewModel(myModel);
        myModel.addObserver(myViewModel);
        //--------------
        primaryStage.setTitle("DEADPOOL MAZE");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("MyView.fxml").openStream());
        Scene scene = new Scene(root, 700, 500);
        scene.getStylesheets().add(getClass().getResource("ViewStyle.css").toExternalForm());
        primaryStage.setScene(scene);
        //--------------
        MyViewController view = fxmlLoader.getController();
        view.setResizeEvent(scene);
        view.setViewModel(myViewModel);
        view.playMusic("music.mp3", 60);
        myViewModel.addObserver(view);
        //--------------
        SetStageCloseEvent(primaryStage, myModel);
        primaryStage.show();
        view.Help();
    }

    /**
     * This function handle press the "Exit" button
     * @param primaryStage The stage of the app
     * @param model this is the modol of this app, the class where all the logic appen
     */
    public void SetStageCloseEvent(Stage primaryStage, MyModel model) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Exit");
                alert.setHeaderText("Are you sure you want to exit?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    model.stopServers();
                } else {
                    windowEvent.consume();
                }
            }
        });
    }

    /**
     * Main function
     * @param args arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}

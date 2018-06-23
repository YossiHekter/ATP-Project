package View;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * This class responsible for the generate box window
 * @author Roee Sanker & Yossi Hekter
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
     * This present the generate box window
     */
    public int[] display(){
        Stage window = new Stage();
        window.setResizable(false);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Generate");
        window.setWidth(270);
        window.setHeight(170);
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        //set row
        Label setRow = new Label("set row");
        TextField txtfid_row = new TextField("10");
        GridPane.setConstraints(setRow, 0, 0);
        GridPane.setConstraints(txtfid_row, 1, 0);

        //set column
        Label colmunRow = new Label("set column");
        TextField txtfid_column = new TextField("10");
        GridPane.setConstraints(colmunRow, 0, 1);
        GridPane.setConstraints(txtfid_column, 1, 1);

        //generate button
        Button btn_generate = new Button("Generate!");

        //init scene
        grid.getChildren().addAll(setRow, txtfid_row, colmunRow, txtfid_column);
        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(grid, btn_generate);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox, 250, 150);

        //setonaction
        btn_generate.setOnAction(e -> {
            generated = true;
            try{
                ans[0] = Integer.parseInt(txtfid_row.getText());
                ans[1] = Integer.parseInt(txtfid_column.getText());
            }catch (Exception ex)
            {
                ans[0]=10;
                ans[1]=10;
            }
            window.close();
        });
        scene.getStylesheets().add(getClass().getResource("ViewStyle.css").toExternalForm());
        window.setScene(scene);
        SetStageCloseEvent(window);
        window.showAndWait();

        return ans;
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

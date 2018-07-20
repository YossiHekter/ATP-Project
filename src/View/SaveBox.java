package View;


import javafx.scene.control.*;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.Optional;

/**
 * This class responsible for the saving window
 * @author Yossi Hekter
 */
public class SaveBox {

    //The name of the save
    public static String ans = "";

    //If the save succeeded
    public static boolean saved = false;

    /**
     * This present the save box window
     */
    public String display() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showSaveDialog(null);
        ans = "";
        if (selectedFile != null) {
            ans = selectedFile.getAbsolutePath();
            ans += ".maze";
            File file = new File(ans);
            if (file.exists()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("This name already exist, please try again with a different name");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK)
                    alert.close();
            }

            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Saved successfully");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK)
                    alert.close();
                saved = true;
            }
        }
        return ans;
    }
}

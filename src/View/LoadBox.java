package View;

import javafx.stage.FileChooser;
import java.io.File;

/**
 * This class responsible for the load window
 * @author Roee Sanker & Yossi Hekter
 */
public class LoadBox {
    /**
     * The location of the file to load
     */
    public static String ans = "";
    /**
     * A variable that says whether a maze load request has been received
     */
    public static boolean loaded = false;

    /**
     * This present the load window
     * @return - The location of the file
     */
    public String display() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null){
            ans = selectedFile.getAbsolutePath();
            loaded = true;
        }
        return ans;
    }
}

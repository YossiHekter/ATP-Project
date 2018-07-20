package View;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import ViewModel.MyViewModel;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

/**
 * This class is the class that responsible for all the main scene of the app
 * @author Yossi Hekter
 */
public class MyViewController implements IView, Observer {

    //The view model of the maze
    private MyViewModel myViewModel;

    //The class that draw
    public MazeDisplayer mazeDisplayer;

    //The class that present thr help
    private Help help;

    //The class that present thr about
    private About about;

    //The class that present thr properties
    private Prop properties;

    //The media player of the maze
    private MediaPlayer player;

    //The number of hint remain to the maze
    private byte numOfHint = 0;

    //If happen key event
    private boolean keyMoveEvent = false;

    //If the user won
    private boolean winEvent = false;

    //If user ask to present the solution
    private boolean solutionEvent = false;

    //If press on the mute button
    private boolean muteIsOn = false;

    private double clickedRow=-1;
    private double clickedcolumn  =-1;
    private boolean drag = false;

    //FXML
    public javafx.scene.control.Label fid_player_row;
    public javafx.scene.control.Label fid_player_column;
    public javafx.scene.control.Label fid_hints_left;
    public javafx.scene.control.Button btn_generate_maze;
    public javafx.scene.control.Button btn_solve_maze;
    public javafx.scene.control.Button btn_hint;

    /**
     * This function set the view model
     * @param myViewModel The view model of the maze
     */
    public void setViewModel(MyViewModel myViewModel) {
        this.myViewModel = myViewModel;
        bindProperties(myViewModel);
    }

    /**
     * This function bind the label that show the user info with the data of the player location
     * @param myViewModel The view model of the maze
     */
    private void bindProperties(MyViewModel myViewModel) {
        fid_player_row.textProperty().bind(myViewModel.playerPositionRow);
        fid_player_column.textProperty().bind(myViewModel.playerPositionColumn);
        fid_hints_left.setText(""+3);
    }

    /**
     * This function update this class according to his observer
     * @param observer The class that we observed
     * @param arg The argument we received from the class
     */
    @Override
    public void update(Observable observer, Object arg) {
        if (observer == myViewModel) {
            switch((String)arg){

                //If the user create new maze
                case "mazeCreate":
                    if(winEvent) {
                        stopMusic();
                        if(!muteIsOn)
                            playMusic("music.mp3", 60);
                        else {
                            playMusic("music.mp3", 60);
                            player.setMute(true);
                        }
                    }
                    numOfHint = 3;
                    fid_hints_left.setText(""+numOfHint);
                    winEvent = false;
                    solutionEvent = false;
                    mazeDisplayer.setFields(myViewModel.getMaze(), myViewModel.getPlayerPositionRow(), myViewModel.getPlayerPositionColumn());
                    mazeDisplayer.drawMaze();
                    btn_solve_maze.setDisable(false);
                    btn_hint.setDisable(false);
                    keyMoveEvent = true;
                    drag = false;
                    break;

                //If the user move his player
                case "playerMoved":
                    if(myViewModel.ShowHint()[0] != 0)
                        mazeDisplayer.deleteHint(myViewModel.ShowHint());
                    mazeDisplayer.movePlayer(myViewModel.getPlayerPositionRow(), myViewModel.getPlayerPositionColumn());
                    break;

                //If the user hit a wall while moved his player
                case "hitWall":
                    Media mama = new Media(MyViewController.class.getClassLoader().getResource("yo_mama.mp3").toExternalForm());
                    MediaPlayer hitWallPlayer = new MediaPlayer(mama);
                    if(!muteIsOn)
                        hitWallPlayer.play();
                    break;

                //If the user ask for the solution of the maze
                case "mazeSolve":
                    winEvent = false;
                    keyMoveEvent = false;
                    solutionEvent = true;
                    btn_hint.setDisable(true);
                    mazeDisplayer.persentSolution(myViewModel.getSolution());
                    break;

                //If the user solved the maze
                case "playerWin":
                    stopMusic();
                    if(!muteIsOn)
                        playMusic("winning_music.mp3", 26);
                    else {
                        playMusic("winning_music.mp3", 26);
                        player.setMute(true);
                    }
                    winEvent = true;
                    keyMoveEvent = false;
                    solutionEvent = false;
                    btn_solve_maze.setDisable(true);
                    btn_hint.setDisable(true);
                    mazeDisplayer.isWon(true);
                    mazeDisplayer.drawWin();
                    break;

                //If the user ask for a hint to the maze
                case "getHint":
                    if(numOfHint > 0) {
                        numOfHint--;
                        fid_hints_left.setText(""+numOfHint);
                        mazeDisplayer.showHint(myViewModel.ShowHint());
                        if(numOfHint <= 0)
                            btn_hint.setDisable(true);
                    }
                    break;

                //If the user load a old maze
                case "loadMaze":
                    stopMusic();
                    if(!muteIsOn)
                        playMusic("music.mp3", 60);
                    winEvent = false;
                    solutionEvent = false;
                    mazeDisplayer.setFields(myViewModel.getMaze(), myViewModel.getPlayerPositionRow(), myViewModel.getPlayerPositionColumn());
                    mazeDisplayer.drawMaze();
                    btn_solve_maze.setDisable(false);
                    btn_hint.setDisable(false);
                    numOfHint = myViewModel.getNumOfHints();
                    if (numOfHint <= 0)
                        btn_hint.setDisable(true);
                    else
                        btn_hint.setDisable(false);
                    fid_hints_left.setText(""+numOfHint);
                    keyMoveEvent = true;
                    drag = false;
                    break;
                case "dragSucceed":
                    mazeDisplayer.movePlayer(myViewModel.getPlayerPositionRow(), myViewModel.getPlayerPositionColumn());
                    drag = false;
                    break;
                case "dragFailed":
                    drag = false;
                    break;
            }
        }
    }

    /**
     * This function generate new maze
     */
    public void generateMaze() {
        GenerateBox generateBox = new GenerateBox();
        int[] mazeSize = generateBox.display();
        if(GenerateBox.generated) {
            GenerateBox.generated = false;
            btn_generate_maze.setDisable(false);
            myViewModel.generateMaze(mazeSize[0], mazeSize[1]);
        }
    }

    /**
     * This function solve the maze
     * @param actionEvent The user press on the solve button
     */
    public void solveMaze(ActionEvent actionEvent) {
        btn_solve_maze.setDisable(true);
        keyMoveEvent = false;
        myViewModel.solveMaze();

    }

    /**
     * This function save the maze on the disk
     */
    public void saveMaze() {
        SaveBox saveBox = new SaveBox();
        if(myViewModel.getMaze() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Must generate a maze before saving!");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                alert.close();
            }
        }
        else {
            String name = saveBox.display();
            if(SaveBox.saved){
                SaveBox.saved = false;
                myViewModel.saveMaze(name);
            }

        }
    }

    /**
     * This function load maze from the disk
     */
    public void loadMaze(){
        LoadBox loadBox = new LoadBox();
        String name = loadBox.display();
        if(loadBox.loaded) {
            loadBox.loaded = false;
            myViewModel.loadMaze(name);
        }
    }

    /**
     * This function handle key press
     * @param keyEvent The key that pressed
     */
    public void KeyPressed(KeyEvent keyEvent) {
        if(keyMoveEvent) {
            KeyCode keycode = keyEvent.getCode();
            myViewModel.moveCharacter(keycode);
            keyEvent.consume();
        }
    }

    //handle resize of the window
    /**
     * This handle with resize of the window
     * @param scene The scene of the app
     */
    public void setResizeEvent(Scene scene) {
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                mazeDisplayer.setWidth(mazeDisplayer.getWidth() + (newSceneWidth.doubleValue() - oldSceneWidth.doubleValue()));
                draw();
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                mazeDisplayer.setHeight(mazeDisplayer.getHeight() + (newSceneHeight.doubleValue() - oldSceneHeight.doubleValue()));
                draw();
            }
        });
    }

    /**
     * This function draw the maze after resize of the window
     */
    public void draw(){
        if(winEvent)
            mazeDisplayer.drawWin();
        else if(solutionEvent)
            mazeDisplayer.persentSolution(myViewModel.getSolution());
        else
            mazeDisplayer.drawMaze();
    }

    /**
     * This function present the 'about' info
     */
    public void About() {
        about = new About();
        about.display();
    }

    /**
     * This function present the 'help' info
     */
    public void Help() {
        help = new Help();
        help.display();
    }

    /**
     * This function present the 'properties' info
     */
    public void getProperties(){
        properties = new Prop();
        properties.showProperties();
    }

    /**
     * This function get a hint for the maze
     */
    public void getHint(){
        myViewModel.getHint();
    }

    /**
     * This function exit from the app
     */
    public void Exit(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Exit");
        alert.setHeaderText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            myViewModel.stopServe();
            Platform.exit();
        }
    }

    /**
     * This play the music
     * @param music The url of the music to play
     *  @param time  The length of the music
     */
    public void playMusic(String music, int time){
        Media m = new Media(MyViewController.class.getClassLoader().getResource(music).toExternalForm());
        player = new MediaPlayer(m);
        player.setAutoPlay(true);
        player.setStartTime(Duration.seconds(0));
        player.setStopTime(Duration.seconds(time));
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.play();
    }

    /**
     * This function stop the music
     */
    public void stopMusic(){
        player.stop();
    }

    /**
     * This function handle the mute button
     */
    public void MutePress(){
        if(!player.isMute() && !muteIsOn)
        {
            player.setMute(true);
            muteIsOn = true;
        }
        else
        {
            player.setMute(false);
            muteIsOn = false;
        }
    }

   public void mouseClick(MouseEvent event){
        double cellWidth = mazeDisplayer.getCellWidth();
        double cellHeight = mazeDisplayer.getCellHeight();
        double playerRow =  myViewModel.getPlayerPositionRow();
        double playerColumn = myViewModel.getPlayerPositionColumn();
       clickedRow = -1;
       clickedcolumn = -1;
        if(playerRow*cellWidth < event.getY() && (playerRow*cellWidth + cellWidth) > event.getY() &&
                playerColumn*cellHeight < event.getX() && (playerColumn*cellHeight+ cellHeight) > event.getX() ){
            clickedRow  = event.getY();
            clickedcolumn = event.getX();
        }
   }

   public void dragMouse(MouseEvent event){
        if(!drag && clickedRow>0 && clickedcolumn>0){
            double rowMove  = event.getY();
            double columnMove = event.getX();
            double cellWidth = mazeDisplayer.getCellWidth();
            double cellHeight = mazeDisplayer.getCellHeight();
            if(isLigalDrag(clickedRow, clickedcolumn, rowMove, columnMove, cellWidth, cellHeight)){
                drag = true;
                int row = (int)(rowMove/mazeDisplayer.getCellWidth());
                int column = (int)(columnMove/mazeDisplayer.getCellHeight());
                clickedRow = rowMove;
                clickedcolumn = columnMove;
                myViewModel.dragMove(row, column);
            }
        }
   }

   public boolean isLigalDrag(double clickedRow, double clickedcolumn, double rowMove, double columnMove, double cellWidth, double cellHeight){
        boolean ligalMove = false;
       if(rowMove > clickedRow + 0.7*cellWidth && rowMove < clickedRow + 1.1 * cellWidth)
           ligalMove = true;
       else if(columnMove > clickedcolumn + 0.7*cellHeight && columnMove < clickedcolumn + 1.1 * cellHeight)
           ligalMove = true;
       else if(rowMove < clickedRow - 0.7*cellWidth && rowMove > clickedRow - 1.1 * cellWidth)
           ligalMove = true;
       else if (columnMove < clickedcolumn - 0.7*cellHeight && columnMove > clickedcolumn - 1.1 * cellHeight)
           ligalMove = true;
       return ligalMove;
   }

   public void mouseRelase(MouseEvent event){
       clickedRow = -1;
       clickedcolumn = -1;
   }

}

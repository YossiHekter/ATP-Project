package View;


import ViewModel.MyViewModel;
import javafx.scene.Scene;

/**
 * The interface of the view layer
 * @author Yossi Hekter
 */
public interface IView  {
    void setViewModel(MyViewModel myViewModel);
    void setResizeEvent(Scene scene);
    void draw();
    void About();
    void Help();
    void Exit();
}

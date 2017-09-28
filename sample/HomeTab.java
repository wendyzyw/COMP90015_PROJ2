package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;

import static sample.Main.canvas_file;

public class HomeTab{
    private Tab home_tab;

    public HomeTab(){
        home_tab = new Tab("Canvas");
        buildTab();
    }

    public Tab getHomeTab(){
        return this.home_tab;
    }

    private void buildTab(){
        home_tab.setClosable(true);

        HBox toolbar = new HBox();
        toolbar.setId("toolbar");
        toolbar.setPrefHeight(100);
        toolbar.setSpacing(5);

        Actions actions = new Actions();
        toolbar.getChildren().add(actions.getActions());

        Draw draw = new Draw();
        toolbar.getChildren().add(draw.getDraw());

        ChooseColor cp = new ChooseColor();
        toolbar.getChildren().add(cp.getChooseColor());

        Shapes shape = new Shapes();
        toolbar.getChildren().add(shape.getShapes());

        Font font = new Font();
        toolbar.getChildren().add(font.get());

        home_tab.setContent(toolbar);
    }
}

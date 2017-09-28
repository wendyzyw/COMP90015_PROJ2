package sample;

import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;

public class HomeTab{
    private Tab home_tab;
    private String file_name;


    public HomeTab(){
        Label tab_label = new Label(file_name);
        home_tab = new Tab();
        home_tab.setGraphic(tab_label);
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

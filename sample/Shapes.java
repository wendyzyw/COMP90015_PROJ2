package sample;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import static sample.Main.*;

public class Shapes {
    private SplitMenuButton btnShape;
    private VBox root;
    public static int RECT = 5;
    public static int ROUND = 6;
    public static int CIRCLE = 7;
    public static int OVAL = 8;

    public Shapes(){
        this.root = new VBox();
        build();
    }

    public VBox getShapes() {
        return this.root;
    }

    private void build(){
        GridPane layout = new GridPane();
        layout.setGridLinesVisible(false);
        layout.setHgap(3);
//        layout.setStyle("-fx-padding:0 5 0 5");

        this.buildShapeButton();
        layout.add(this.btnShape,0,0);

        Label label = new Label("Shape");
        label.getStyleClass().add("ribbonLabel");
        label.setStyle("-fx-padding:5 0 0 0");
        label.setTooltip(new Tooltip("Shapes for this board"));

        VBox vbox = new VBox();
        vbox.getChildren().add(label);
        vbox.setVgrow(label, Priority.ALWAYS);
        vbox.setAlignment(Pos.BOTTOM_CENTER);
        vbox.setStyle("-fx-padding:5 0 0 0");
        layout.add(vbox,0,2,6,1);

        this.root.setAlignment(Pos.CENTER);
        this.root.getChildren().add(layout);
        this.root.getStyleClass().add("toolbarContainer");
    }

    private void buildShapeButton(){
        //create menu items
        String rec_path = "/sample/icons/rec.png";
        Image rec_icon = new Image(this.getClass().getResourceAsStream(rec_path),24.0,24.0,true,true);
        ImageView rec = new ImageView(rec_icon);
        MenuItem item_rec = new MenuItem("Rectangle",rec);
        item_rec.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.method = RECT;
                btnShape.setText("Rectangle");
                drawRect(canvas,graphicsContext);
            }
        });

        String roundrec_path = "/sample/icons/roundrec.png";
        Image roundrec_icon = new Image(this.getClass().getResourceAsStream(roundrec_path),24.0,24.0,true,true);
        ImageView roundrec = new ImageView(roundrec_icon);
        MenuItem item_roundrec = new MenuItem("Round Rectangle",roundrec);
        item_roundrec.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.method = ROUND;
                btnShape.setText("Round Rectangle");
                drawRoundrec(canvas,graphicsContext);
            }
        });

        String circle_path = "/sample/icons/circle.png";
        Image circle_icon = new Image(this.getClass().getResourceAsStream(circle_path),24.0,24.0,true,true);
        ImageView circle = new ImageView(circle_icon);
        MenuItem item_circle = new MenuItem("Circle",circle);
        item_circle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.method = CIRCLE;
                btnShape.setText("Circle");
                drawCircle(canvas,graphicsContext);
            }
        });

        String oval_path = "/sample/icons/oval.png";
        Image oval_icon = new Image(this.getClass().getResourceAsStream(oval_path),24.0,24.0,true,true);
        ImageView oval = new ImageView(oval_icon);
        MenuItem item_oval = new MenuItem("Oval",oval);
        item_oval.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.method = OVAL;
                btnShape.setText("Oval");
                drawOval(canvas,graphicsContext);
            }
        });

        this.btnShape = new SplitMenuButton(item_rec,item_roundrec,item_circle,item_oval);
        this.btnShape.setText("Shape");
        this.btnShape.setContentDisplay(ContentDisplay.TOP);

        String iconPath = "/sample/icons/shape.png";
        Image icon = new Image(this.getClass().getResourceAsStream(iconPath),24.0,24.0,true,true);
        ImageView imageView = new ImageView(icon);
        this.btnShape.setGraphic(imageView);

        this.btnShape.setTooltip(new Tooltip("choose shape"));
        this.btnShape.setAlignment(Pos.CENTER);

    }
}

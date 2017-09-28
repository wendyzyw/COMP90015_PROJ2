package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import static sample.Main.graphicsContext;

public class ChooseColor {
    private Button btnColor;
    private VBox root;
    public static Color current_color;

    public ChooseColor(){
        this.root = new VBox();
        build();
    }

    public VBox getChooseColor() {
        return this.root;
    }

    private void build(){
        GridPane layout = new GridPane();
        layout.setGridLinesVisible(false);
        layout.setHgap(3);
//        layout.setStyle("-fx-padding:0 5 0 5");

        final ColorPicker colorPicker = new ColorPicker();
        layout.add(colorPicker,1,0);
        colorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                graphicsContext.setStroke(colorPicker.getValue());
                current_color = colorPicker.getValue();
            }
        });

        Slider stroke_size = new Slider();
        stroke_size.setMin(1);
        stroke_size.setMax(50);
        stroke_size.setValue(1);
        stroke_size.setStyle("-fx-padding:8 0 0 0");
        stroke_size.valueProperty().addListener(new ChangeListener<Number>(){
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val){
                graphicsContext.setLineWidth(new_val.doubleValue());
            }
        });
        layout.add(stroke_size,1,1);

        Label color_label = new Label("Color:");
        Label size_label = new Label("Stroke size:");
        layout.add(color_label,0,0);
        layout.add(size_label,0,1);

        Label label = new Label("Style");
        label.getStyleClass().add("ribbonLabel");
        label.setStyle("-fx-padding:8 0 0 0");
        label.setTooltip(new Tooltip("ColorPicker for this board"));

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

    private void buildColorButton(){
        this.btnColor = new Button("Color");
        this.btnColor.setContentDisplay(ContentDisplay.TOP);

        String iconPath = "/sample/icons/colorwheel.png";
        Image icon = new Image(this.getClass().getResourceAsStream(iconPath),24.0,24.0,true,true);
        ImageView imageView = new ImageView(icon);
        this.btnColor.setGraphic(imageView);

        this.btnColor.setTooltip(new Tooltip("choose color"));
        this.btnColor.setAlignment(Pos.CENTER);
        this.btnColor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Color button clicked");
            }
        });
    }
}

package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import static sample.Main.*;

public class Draw {
    private Button btnPencil,btnRuler,btnErase,btnText;
    private VBox root;
    public static final int DRAW = 1;
    public static final int LINE = 2;
    public static final int ERASE = 3;
    public static final int TEXT = 4;

    public Draw(){
        this.root = new VBox();
        build();
    }

    public VBox getDraw() {
        return this.root;
    }

    private void build(){
        GridPane layout = new GridPane();
        layout.setGridLinesVisible(false);
        layout.setHgap(3);
//        layout.setStyle("-fx-padding:0 5 0 5");

        this.buildPencilButton();
        btnPencil.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.method = DRAW;
                canvasFreeDraw(canvas, graphicsContext);
                System.out.println("Pencil button! method is "+Main.method);
            }
        });
        layout.add(this.btnPencil,0,0);

        this.buildRulerButton();
        btnRuler.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.method = LINE;
                canvasLineDraw(canvas, graphicsContext);
                System.out.println("Line button! method is "+Main.method);
            }
        });
        layout.add(this.btnRuler,1,0);

        this.buildEraseButton();
        btnErase.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.method = ERASE;
                canvasErase(canvas,graphicsContext);
                System.out.println("Erase button! method is "+Main.method);
            }
        });
        layout.add(this.btnErase,2,0);

        this.buildTextButton();
        btnText.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.method = TEXT;
                canvasText(canvas,graphicsContext);
            }
        });
        layout.add(this.btnText,3,0);

        Label label = new Label("Draw");
        label.getStyleClass().add("ribbonLabel");
        label.setTooltip(new Tooltip("Actions for this board"));

        VBox vbox = new VBox();
        vbox.getChildren().add(label);
        vbox.setVgrow(label, Priority.ALWAYS);
        vbox.setAlignment(Pos.BOTTOM_CENTER);
        vbox.setStyle("-fx-padding:5 0 0 0");
        layout.add(vbox,0,2,6,1);
        label.setStyle("-fx-padding:5 0 0 0");

        this.root.setAlignment(Pos.CENTER);
        this.root.getChildren().add(layout);
        this.root.getStyleClass().add("toolbarContainer");
    }

    private void buildPencilButton(){
        this.btnPencil = new Button("Draw");
        this.btnPencil.setContentDisplay(ContentDisplay.TOP);

        String iconPath = "/sample/icons/pencil.png";
        Image icon = new Image(this.getClass().getResourceAsStream(iconPath),24.0,24.0,true,true);
        ImageView imageView = new ImageView(icon);
        this.btnPencil.setGraphic(imageView);

        this.btnPencil.setTooltip(new Tooltip("Free draw"));
        this.btnPencil.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Pencil button clicked");
            }
        });
    }

    private void buildRulerButton(){
        this.btnRuler = new Button("Ruler");
        this.btnRuler.setContentDisplay(ContentDisplay.TOP);

        String iconPath = "/sample/icons/ruler.png";
        Image icon = new Image(this.getClass().getResourceAsStream(iconPath),24.0,24.0,true,true);
        ImageView imageView = new ImageView(icon);
        this.btnRuler.setGraphic(imageView);

        this.btnRuler.setTooltip(new Tooltip("Draw line"));
        this.btnRuler.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Ruler button clicked");
            }
        });
    }

    private void buildEraseButton(){
        this.btnErase = new Button("Erase");
        this.btnErase.setContentDisplay(ContentDisplay.TOP);

        String iconPath = "/sample/icons/remove.png";
        Image icon = new Image(this.getClass().getResourceAsStream(iconPath),24.0,24.0,true,true);
        ImageView imageView = new ImageView(icon);
        this.btnErase.setGraphic(imageView);

        this.btnErase.setTooltip(new Tooltip("Erase"));
        this.btnErase.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Erase button clicked");
            }
        });
    }

    private void buildTextButton(){
        this.btnText = new Button("Text");
        this.btnText.setContentDisplay(ContentDisplay.TOP);

        String iconPath = "/sample/icons/text.png";
        Image icon = new Image(this.getClass().getResourceAsStream(iconPath),24.0,24.0,true,true);
        ImageView imageView = new ImageView(icon);
        this.btnText.setGraphic(imageView);

        this.btnText.setTooltip(new Tooltip("Text box"));
        this.btnText.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Text button clicked");
            }
        });
    }
}

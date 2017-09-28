package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import static sample.Main.*;

public class Actions {
    private Button btnNew, btnClose, btnOpen, btnSave, btnSaveas, btnDelete;
    private VBox root;

    public Actions(){
        this.root = new VBox();
        build();
    }

    public VBox getActions() {
        return this.root;
    }

    private void build(){
        GridPane layout = new GridPane();
        layout.setGridLinesVisible(false);
        layout.setHgap(3);
//        layout.setStyle("-fx-padding:0 5 0 5");

        this.buildNewButton();
        btnNew.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //pop up window asking if user wants to save current canvas
                ButtonType inside_btnNew = new ButtonType("Create new", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.WARNING,"", ButtonType.CANCEL);
                alert.getDialogPane().getButtonTypes().add(inside_btnNew);

                alert.setHeaderText("Create new canvas without saving this one?");
                alert.setResultConverter(new Callback<ButtonType, ButtonType>() {
                    @Override
                    public ButtonType call(ButtonType param) {
                        if (param == inside_btnNew){
                            if (canvas_file != null){
                                canvas_file.deleteOnExit();
                            }
                            graphicsContext.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
//                altCanvas.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
                            previousStroke = graphicsContext.getStroke();
                            previousWidth = graphicsContext.getLineWidth();
                            graphicsContext.setStroke(Color.BLACK);
                            graphicsContext.setLineWidth(1);
                            graphicsContext.strokeRect(
                                    0,              //x of the upper left corner
                                    0,              //y of the upper left corner
                                    canvas.getWidth(),    //width of the rectangle
                                    canvas.getHeight());  //height of the rectangle
                            graphicsContext.setStroke(previousStroke);
                            graphicsContext.setLineWidth(previousWidth);
                            Main.method = 0;
                        }
                        return null;
                    }
                });
                alert.showAndWait();
            }
        });
        layout.add(this.btnNew,0,0);

        this.buildCloseButton();
        btnClose.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //pop up window asking if user wants to save current canvas
                ButtonType inside_btnClose = new ButtonType("Close Anyway", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.WARNING,"", ButtonType.CANCEL);
                alert.getDialogPane().getButtonTypes().add(inside_btnClose);

                alert.setHeaderText("Close without saving this one?");
                alert.setResultConverter(new Callback<ButtonType, ButtonType>() {
                    @Override
                    public ButtonType call(ButtonType param) {
                        if (param == inside_btnClose) {
                            if (canvas_file != null){
                                canvas_file.deleteOnExit();
                            }
                            System.out.println("Close file!");
                                graphicsContext.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
//                altCanvas.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
                                previousStroke = graphicsContext.getStroke();
                                previousWidth = graphicsContext.getLineWidth();
                                graphicsContext.setStroke(Color.BLACK);
                                graphicsContext.setLineWidth(1);
                                graphicsContext.strokeRect(
                                        0,              //x of the upper left corner
                                        0,              //y of the upper left corner
                                        canvas.getWidth(),    //width of the rectangle
                                        canvas.getHeight());  //height of the rectangle
                                graphicsContext.setStroke(previousStroke);
                                graphicsContext.setLineWidth(previousWidth);
                                Main.method = 0;
                        }
                        return null;
                    }
                });
                alert.showAndWait();
            }
        });
        layout.add(this.btnClose,1,0);

        this.buildOpenButton();
        btnOpen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                //set extension filter
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("png files (*.png)","*.png");
                fileChooser.getExtensionFilters().add(extFilter);
                //show open file dialog
                canvas_file = fileChooser.showOpenDialog(primaryStage);
                if (canvas_file != null) {
                    try {
                        BufferedImage opened_canvas = ImageIO.read(canvas_file);
                        Image new_canvas = SwingFXUtils.toFXImage(opened_canvas,null);
                        graphicsContext.drawImage(new_canvas, 0, 0, canvas.getWidth(), canvas.getHeight());
                    } catch(IOException e){
                        e.printStackTrace();
                    }
                }

            }
        });
        layout.add(this.btnOpen,2,0);

        //if it's an opened image, save it to it's original path
        //if it's a new image, same function as save
        this.buildSaveButton();
        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (canvas_file != null){
                    try{
                        WritableImage writableImage = new WritableImage((int)canvas.getWidth(),(int)canvas.getHeight());
                        canvas.snapshot(null,writableImage);
                        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage,null);
                        ImageIO.write(renderedImage,"png",canvas_file);
                        Alert msg = new Alert(Alert.AlertType.INFORMATION,"",ButtonType.OK);
                        msg.setHeaderText("File "+canvas_file.getName()+" saved successfully!");
                        msg.showAndWait();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    FileChooser fileChooser = new FileChooser();
                    //set extension filter
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("png files (*.png)","*.png");
                    fileChooser.getExtensionFilters().add(extFilter);
                    //show save file dialog
                    File file = fileChooser.showSaveDialog(primaryStage);
                    if (file != null){
                        try{
                            WritableImage writableImage = new WritableImage((int)canvas.getWidth(),(int)canvas.getHeight());
                            canvas.snapshot(null,writableImage);
                            RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage,null);
                            ImageIO.write(renderedImage,"png",file);
                            Alert msg = new Alert(Alert.AlertType.INFORMATION,"",ButtonType.OK);
                            msg.setHeaderText("File "+file.getName()+" saved successfully!");
                            msg.showAndWait();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    canvas_file = file;
                }
            }
        });
        layout.add(this.btnSave,3,0);

        this.buildSaveasButton();
        btnSaveas.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                //set extension filter
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("png files (*.png)","*.png");
                fileChooser.getExtensionFilters().add(extFilter);
                //show save file dialog
                File file = fileChooser.showSaveDialog(primaryStage);
                if (file != null){
                    try{
                        WritableImage writableImage = new WritableImage((int)canvas.getWidth(),(int)canvas.getHeight());
                        canvas.snapshot(null,writableImage);
                        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage,null);
                        ImageIO.write(renderedImage,"png",file);
                        Alert msg = new Alert(Alert.AlertType.INFORMATION,"",ButtonType.OK);
                        msg.setHeaderText("File "+file.getName()+" saved successfully!");
                        msg.showAndWait();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                canvas_file = file;
            }
        });
        layout.add(this.btnSaveas,4,0);

        //this method clear the current canvas and do not involve any fileIO
        this.buildDeleteButton();
        btnDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                graphicsContext.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
//                altCanvas.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
                previousStroke = graphicsContext.getStroke();
                previousWidth = graphicsContext.getLineWidth();
                graphicsContext.setStroke(Color.BLACK);
                graphicsContext.setLineWidth(1);
                graphicsContext.strokeRect(
                        0,              //x of the upper left corner
                        0,              //y of the upper left corner
                        canvas.getWidth(),    //width of the rectangle
                        canvas.getHeight());  //height of the rectangle
                graphicsContext.setStroke(previousStroke);
                graphicsContext.setLineWidth(previousWidth);
                Main.method = 0;
            }
        });
        layout.add(this.btnDelete,5,0);

        Label label = new Label("File Menu");
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

    private void buildNewButton(){
        this.btnNew = new Button("New");
        this.btnNew.setContentDisplay(ContentDisplay.TOP);

        String iconPath = "/sample/icons/new.png";
        Image icon = new Image(this.getClass().getResourceAsStream(iconPath),24.0,24.0,true,true);
        ImageView imageView = new ImageView(icon);
        this.btnNew.setGraphic(imageView);

        this.btnNew.setTooltip(new Tooltip("New canvas"));
        this.btnNew.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("New file button clicked");
            }
        });
    }

    private void buildCloseButton(){
        this.btnClose = new Button("Close");
        this.btnClose.setContentDisplay(ContentDisplay.TOP);

        String iconPath = "/sample/icons/close.png";
        Image icon = new Image(this.getClass().getResourceAsStream(iconPath),24.0,24.0,true,true);
        ImageView imageView = new ImageView(icon);
        this.btnClose.setGraphic(imageView);

        this.btnClose.setTooltip(new Tooltip("Close this canvas"));
        this.btnClose.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Close file button clicked");
            }
        });
    }

    private void buildOpenButton(){
        this.btnOpen = new Button("Open");
        this.btnOpen.setContentDisplay(ContentDisplay.TOP);

        String iconPath = "/sample/icons/open.png";
        Image icon = new Image(this.getClass().getResourceAsStream(iconPath),24.0,24.0,true,true);
        ImageView imageView = new ImageView(icon);
        this.btnOpen.setGraphic(imageView);

        this.btnOpen.setTooltip(new Tooltip("Open stored canvas"));
        this.btnOpen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Open file button clicked");
            }
        });
    }

    private void buildSaveButton(){
        this.btnSave = new Button("Save");
        this.btnSave.setContentDisplay(ContentDisplay.TOP);

        String iconPath = "/sample/icons/Save.png";
        Image icon = new Image(this.getClass().getResourceAsStream(iconPath),24.0,24.0,true,true);
        ImageView imageView = new ImageView(icon);
        this.btnSave.setGraphic(imageView);

        this.btnSave.setTooltip(new Tooltip("Save current canvas"));
        this.btnSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Save button clicked");
            }
        });
    }

    private void buildSaveasButton(){
        this.btnSaveas = new Button("Save as");
        this.btnSaveas.setContentDisplay(ContentDisplay.TOP);

        String iconPath = "/sample/icons/Saveas.png";
        Image icon = new Image(this.getClass().getResourceAsStream(iconPath),24.0,24.0,true,true);
        ImageView imageView = new ImageView(icon);
        this.btnSaveas.setGraphic(imageView);

        this.btnSaveas.setTooltip(new Tooltip("Save as"));
        this.btnSaveas.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Save as button clicked");
            }
        });
    }

    private void buildDeleteButton(){
        this.btnDelete = new Button("Delete");
        this.btnDelete.setContentDisplay(ContentDisplay.TOP);

        String iconPath = "/sample/icons/delete.png";
        Image icon = new Image(this.getClass().getResourceAsStream(iconPath),24.0,24.0,true,true);
        ImageView imageView = new ImageView(icon);
        this.btnDelete.setGraphic(imageView);

        this.btnDelete.setTooltip(new Tooltip("Clear everything on current canvas"));
        this.btnDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Delete button clicked");
            }
        });
    }
}

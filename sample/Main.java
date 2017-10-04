package sample;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Side;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.Group;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.scene.Cursor;
import javafx.scene.text.Font;

import java.io.File;

import static sample.ChooseColor.current_color;
import static sample.Draw.*;
import static sample.Font.font_family;
import static sample.Font.font_size;
import static sample.Font.is_bold;
import static sample.Shapes.*;

public class Main extends Application {
    public static int method;
    public static Canvas canvas;
    public static File canvas_file = null;
    public static GraphicsContext graphicsContext;
    public static Scene scene;
    public static BorderPane layout;
    public static Paint previousStroke;
    public static double previousWidth;
    public static double startingX;
    public static double startingY;
    public static double x2, y2, x3, y3;
    public static double[] xpoints, ypoints;
    public static double width;
    public static double height;
    public static double topLeftX;
    public static double topLeftY;
    public static double text_x, text_y;
    public static TextArea ta;
    public static StackPane canvasStack;
    public static Canvas tempCanvas;
    public static Pane text_pane;
    public static GraphicsContext altCanvas;
    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        layout = new BorderPane();
        layout.setId("whiteboard");

        RibbonBar rbar = new RibbonBar();
        layout.setTop(rbar.getRibbonBar());
        layout.setStyle("-fx-background-color: white");
//        layout.setCenter(group_for_rectangles);

        Rectangle2D screenSize = Screen.getPrimary().getBounds();
        this.canvas = new Canvas(screenSize.getWidth(),screenSize.getHeight());
        this.graphicsContext = canvas.getGraphicsContext2D();
        tempCanvas = new Canvas(canvas.getWidth(),canvas.getHeight());

        initDraw(graphicsContext);

        canvasStack = new StackPane(tempCanvas,canvas);
        canvasStack.setStyle("-fx-background-color: white");
        layout.setCenter(canvasStack);

        TabPane chatRoom = new TabPane();
        chatRoom.setId("toolbar");
        TextArea ta = new TextArea();
        ta.setPrefRowCount(10);
        ta.setPrefColumnCount(20);
        Tab tab1 = new Tab("Chat");
        tab1.setContent(ta);
        Tab tab2 = new Tab("Peer List");
        chatRoom.setSide(Side.RIGHT);
        chatRoom.setTabMinWidth(80);
        chatRoom.getTabs().addAll(tab1,tab2);
        layout.setLeft(chatRoom);

        setSceneProperties();

        String css = this.getClass().getResource("/sample/toolbar.css").toExternalForm();
        scene.getStylesheets().addAll(css);
        primaryStage.setTitle("whiteboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static void canvasFreeDraw(Canvas canvas, GraphicsContext graphicsContext){
        canvas.setCursor(Cursor.DEFAULT);
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        if (method == DRAW) {
                            graphicsContext.beginPath();
                            graphicsContext.moveTo(event.getX(),event.getY());
                            graphicsContext.stroke();
                        }
                    }
                });
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        if (method == DRAW) {
                            graphicsContext.lineTo(event.getX(),event.getY());
                            graphicsContext.stroke();
                        }
                    }
                });
    }

    public static void canvasLineDraw(Canvas canvas, GraphicsContext graphicsContext){
        canvas.setCursor(Cursor.CROSSHAIR);
        altCanvas = tempCanvas.getGraphicsContext2D();
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (method == LINE) {
                            startingX = event.getX();
                            startingY = event.getY();
                            graphicsContext.beginPath();
                            graphicsContext.moveTo(startingX,startingY);
//                            graphicsContext.setStroke(Color.BLACK);
                        }
                    }
                });
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (method == LINE) {
                            altCanvas.clearRect(0,0,altCanvas.getCanvas().getWidth(),
                                    altCanvas.getCanvas().getHeight());
                            altCanvas.beginPath();
                            altCanvas.setStroke(current_color);
                            altCanvas.moveTo(startingX,startingY);
                            altCanvas.lineTo(event.getX(),event.getY());
//                            graphicsContext.setStroke(Color.BLACK);
                            altCanvas.stroke();
                            altCanvas.closePath();
                        }
                    }
                });
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (method == LINE){
                            altCanvas.clearRect(0,0,altCanvas.getCanvas().getWidth(),
                                    altCanvas.getCanvas().getWidth());
                            graphicsContext.lineTo(event.getX(),event.getY());
//                            graphicsContext.setStroke(Color.BLACK);
                            graphicsContext.stroke();
                            graphicsContext.closePath();
                        }
                    }
                });
    }

    public static void canvasErase(Canvas canvas, GraphicsContext graphicsContext){
        canvas.setCursor(Cursor.DEFAULT);
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (method == ERASE) {
                            previousStroke = graphicsContext.getStroke();
                            graphicsContext.setStroke(Color.WHITE);
                            graphicsContext.beginPath();
                        }
                    }
                });
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (method == ERASE) {
                            graphicsContext.lineTo(event.getX(), event.getY());
                            graphicsContext.stroke();
                        }
                    }
                });
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (method == ERASE) {
                            graphicsContext.closePath();
                            graphicsContext.setStroke(previousStroke);
//                            altCanvas.setStroke(previousStroke);
                        }
                        graphicsContext.setStroke(current_color);
//                        altCanvas.setStroke(current_color);
                    }
                });
    }

    public static void canvasText(Canvas canvas, GraphicsContext graphicsContext){
       canvas.setCursor(Cursor.TEXT);
       canvas.addEventHandler(MouseEvent.MOUSE_CLICKED,
               new EventHandler<MouseEvent>() {
                   @Override
                   public void handle(MouseEvent event) {
                       if (method == TEXT && ta == null) {
                           text_x = event.getX();
                           text_y = event.getY();
                           ta = new TextArea();
                           ta.setBorder(null);
                           ta.setPrefColumnCount(20);
                           ta.setPrefRowCount(10);
                           ta.setWrapText(true);
                           ta.setLayoutX(event.getX());
                           ta.setLayoutY(event.getY());
                           text_pane = new Pane();
                           text_pane.getChildren().add(ta);
                           text_pane.setPrefSize(canvas.getWidth(),canvas.getHeight());
                           if (canvasStack.getChildren().size() < 3) {
                               canvasStack.getChildren().add(text_pane);
                           }
                           ta.addEventHandler(KeyEvent.KEY_PRESSED,
                                   new EventHandler<KeyEvent>() {
                                       @Override
                                       public void handle(KeyEvent event) {
                                           if (event.getCode().equals(KeyCode.ENTER)){
                                               if (is_bold){
                                                   graphicsContext.setFont(Font.font(font_family, FontWeight.BOLD, Double.parseDouble(font_size)));
                                               } else {
                                                   graphicsContext.setFont(new Font(font_family, Double.parseDouble(font_size)));
                                               }
                                               graphicsContext.fillText(ta.getText(),text_x,text_y);
                                               canvasStack.getChildren().remove(text_pane);
                                               text_x = 0;
                                               text_y = 0;
                                               canvas.setCursor(Cursor.DEFAULT);
                                               ta = null;
                                           }
                                       }
                                   });
                       }
                   }
               });
    }

    public static void drawOval(Canvas canvas, GraphicsContext graphicsContext){
        canvas.setCursor(Cursor.CROSSHAIR);
        altCanvas = tempCanvas.getGraphicsContext2D();
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (method == OVAL) {
                            startingX = event.getX();
                            startingY = event.getY();
                        }
                    }
                });
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (method == OVAL) {
                            altCanvas.clearRect(0, 0, altCanvas.getCanvas().getWidth(),
                                    altCanvas.getCanvas().getHeight());
                            width = Math.abs(event.getX() - startingX);
                            height = Math.abs(event.getY() - startingY);
                            topLeftX = startingX < event.getX() ? startingX : event.getX();
                            topLeftY = startingY < event.getY() ? startingY
                                    : event.isShiftDown() ? startingY - width : event.getY();
                            altCanvas.strokeOval(topLeftX, topLeftY, width,
                                    event.isShiftDown() ? width : height);
                        }
                    }
                });
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (method == OVAL) {
                            altCanvas.clearRect(0, 0, altCanvas.getCanvas().getWidth(),
                                    altCanvas.getCanvas().getHeight());
//                            graphicsContext.fillOval(topLeftX, topLeftY, width,
//                                    event.isShiftDown() ? width : height);
                            graphicsContext.strokeOval(topLeftX, topLeftY, width,
                                    event.isShiftDown() ? width : height);
                        }
                    }
                });
    }

    public static void drawCircle(Canvas canvas, GraphicsContext graphicsContext){
        canvas.setCursor(Cursor.CROSSHAIR);
        altCanvas = tempCanvas.getGraphicsContext2D();
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (method == CIRCLE) {
                            startingX = event.getX();
                            startingY = event.getY();
                        }
                    }
                });
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (method == CIRCLE) {
                            altCanvas.clearRect(0, 0, altCanvas.getCanvas().getWidth(),
                                    altCanvas.getCanvas().getHeight());
                            width = Math.abs(event.getX() - startingX);
                            height = width;
                            topLeftX = startingX < event.getX() ? startingX : event.getX();
                            topLeftY = startingY < event.getY() ? startingY
                                    : event.isShiftDown() ? startingY - width : event.getY();
                            altCanvas.strokeOval(topLeftX, topLeftY, width,
                                    event.isShiftDown() ? width : height);
                        }
                    }
                });
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (method == CIRCLE) {
                            altCanvas.clearRect(0, 0, altCanvas.getCanvas().getWidth(),
                                    altCanvas.getCanvas().getHeight());
//                            graphicsContext.fillOval(topLeftX, topLeftY, width,
//                                    event.isShiftDown() ? width : height);
                            graphicsContext.strokeOval(topLeftX, topLeftY, width,
                                    event.isShiftDown() ? width : height);
                        }
                    }
                });
    }

    public static void drawRect(Canvas canvas, GraphicsContext graphicsContext){
        canvas.setCursor(Cursor.CROSSHAIR);
        altCanvas = tempCanvas.getGraphicsContext2D();
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (method == RECT) {
                            startingX = event.getX();
                            startingY = event.getY();
                        }
                    }
                });
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (method == RECT) {
                            altCanvas.clearRect(0, 0, altCanvas.getCanvas().getWidth(),
                                    altCanvas.getCanvas().getHeight());
                            width = Math.abs(event.getX() - startingX);
                            height = Math.abs(event.getY() - startingY);
                            topLeftX = startingX < event.getX() ? startingX : event.getX();
                            topLeftY = startingY < event.getY() ? startingY
                                    : event.isShiftDown() ? startingY - width : event.getY();
                            altCanvas.strokeRect(topLeftX, topLeftY, width,
                                    event.isShiftDown() ? width : height);
                        }
                    }
                });
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (method == RECT) {
                            altCanvas.clearRect(0, 0, altCanvas.getCanvas().getWidth(),
                                    altCanvas.getCanvas().getHeight());
                            graphicsContext.strokeRect(topLeftX, topLeftY, width,
                                    event.isShiftDown() ? width : height);
                            System.out.println("Rect: "+topLeftX+" "+topLeftY+" "+width+" "+height+"\n");
                        }
                    }
                });
    }

    public static void drawRoundrec(Canvas canvas, GraphicsContext graphicsContext){
        canvas.setCursor(Cursor.CROSSHAIR);
        altCanvas = tempCanvas.getGraphicsContext2D();
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (method == ROUND) {
                            startingX = event.getX();
                            startingY = event.getY();
                        }
                    }
                });
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (method == ROUND) {
                            altCanvas.clearRect(0, 0, altCanvas.getCanvas().getWidth(),
                                    altCanvas.getCanvas().getHeight());
                            width = Math.abs(event.getX() - startingX);
                            height = Math.abs(event.getY() - startingY);
                            topLeftX = startingX < event.getX() ? startingX : event.getX();
                            topLeftY = startingY < event.getY() ? startingY
                                    : event.isShiftDown() ? startingY - width : event.getY();
                            altCanvas.strokeRoundRect(topLeftX, topLeftY, width,
                                    event.isShiftDown() ? width : height,30,30);
                        }
                    }
                });
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (method == ROUND) {
                            altCanvas.clearRect(0, 0, altCanvas.getCanvas().getWidth(),
                                    altCanvas.getCanvas().getHeight());
                            graphicsContext.strokeRoundRect(topLeftX, topLeftY, width,
                                    event.isShiftDown() ? width : height,30,30);
                            System.out.println("Rect: "+topLeftX+" "+topLeftY+" "+width+" "+height+"\n");
                        }
                    }
                });
    }
    private void setSceneProperties(){
        //The percentage values are used as multipliers for screen width/height.
        double percentageWidth = 0.85;
        double percentageHeight = 0.80;

        //Calculate the width / height of screen.
        Rectangle2D screenSize = Screen.getPrimary().getBounds();
        percentageWidth *= screenSize.getWidth();
        percentageHeight *= screenSize.getHeight();

        //Create a scene object. Pass in the layout and set
        //the dimensions to 98% of screen width & 90% screen height.
        this.scene = new Scene(layout, percentageWidth, percentageHeight);

        String css = this.getClass().getResource("/sample/toolbar.css").toExternalForm();
        scene.getStylesheets().addAll(css);
    }

    private void initDraw(GraphicsContext gc){
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();

//        gc.setFill(Color.WHITE);
//        gc.setStroke(Color.BLACK);
//        gc.setLineWidth(5);

//        gc.fill();
        gc.strokeRect(
                0,              //x of the upper left corner
                0,              //y of the upper left corner
                canvasWidth,    //width of the rectangle
                canvasHeight);  //height of the rectangle
//        gc.setFill(Color.RED);
//        gc.setStroke(Color.BLUE);
//        gc.setLineWidth(1);

    }

}

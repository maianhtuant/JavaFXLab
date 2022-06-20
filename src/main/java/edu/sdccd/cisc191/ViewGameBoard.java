package edu.sdccd.cisc191;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Presents the user with the game graphical user interface
 */
public class ViewGameBoard extends Application
{
    private Canvas gameCanvas;
    private ControllerGameBoard controller;
    private GameBoardLabel fishRemaining;
    private GameBoardLabel guessesRemaining;
    private GameBoardLabel message;

    public static void main(String[] args)
    {
        // TODO: launch the app
        launch(args);
    }

    public void updateHeader() {
        //TODO update labels
        fishRemaining.setText("Fish: " + controller.modelGameBoard.getFishRemaining());
        guessesRemaining.setText("Bait: " + controller.modelGameBoard.getGuessesRemaining());
        if(controller.fishWin()) {
            //"Fishes win!"
            message.setText("Fishes Win!");
        } else if(controller.playerWins()) {
            //"You win!"
            message.setText("Player Win!");
        } else {
            //"Find the fish!"
            message.setText("Find the fish!");
        }
    }
    @Override
    public void start(Stage stage) throws Exception {
        controller = new ControllerGameBoard();
        // TODO initialize gameCanvas
        gameCanvas = new Canvas();
        //create Menu
        Menu menu = new Menu("File");
        MenuItem newItem = new MenuItem("new game");
        EventHandler<ActionEvent> eventNewGame = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ViewGameBoard game = new ViewGameBoard();
                try{
                    game.start(new Stage());
                    stage.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        newItem.setOnAction(eventNewGame);
        MenuItem exitItem = new MenuItem("exit game");
        EventHandler<ActionEvent> eventExitItem = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        };
        exitItem.setOnAction(eventExitItem);


        menu.getItems().add(newItem);
        menu.getItems().add(exitItem);
        //create menu bar
        MenuBar mBar = new MenuBar();
        mBar.getMenus().add(menu);

        fishRemaining = new GameBoardLabel();
        guessesRemaining = new GameBoardLabel();
        message = new GameBoardLabel();
        VBox menuBarBox = new VBox();
        menuBarBox.getChildren().add(mBar);

        // TODO display game there are infinite ways to do this, I used BorderPane with HBox and VBox.
        BorderPane bPane = new BorderPane();
        bPane.setTop(menuBarBox);

        VBox vBMain = new VBox();
        HBox hbTop = new HBox();

        hbTop.getChildren().add(fishRemaining);
        hbTop.getChildren().add(guessesRemaining);
        hbTop.getChildren().add(message);

        vBMain.getChildren().add(hbTop);
        VBox vBox = new VBox();

        updateHeader();

        for (int row=0; row < ModelGameBoard.DIMENSION; row++) {
            // TODO: create row container
            HBox hBox = new HBox();

            for (int col=0; col < ModelGameBoard.DIMENSION; col++) {
                GameBoardButton button = new GameBoardButton(row, col, controller.modelGameBoard.fishAt(row,col));
                int finalRow = row;
                int finalCol = col;
                button.setOnAction(e -> {
                    controller.makeGuess(finalRow, finalCol);
                    if(!controller.isGameOver()) {
                        button.handleClick();
                        updateHeader();
                    }
                });
                // TODO: add button to row
                hBox.getChildren().add(button);
                hBox.setSpacing(10);
            }
            // TODO: add row to column
            vBox.getChildren().add(hBox);
            vBox.setSpacing(5);
        }

        // TODO: create scene, stage, set title, and show
        vBMain.getChildren().add(vBox);
        bPane.setCenter(vBMain);
        Scene scene = new Scene(bPane);
        stage.setTitle("Gone Fishes");
        stage.setScene(scene);
        stage.setResizable(false);

        stage.show();

    }


}

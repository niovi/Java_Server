package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import sample.DataBases.DataBaseAgent;
import sample.MQTT.ConnectivityHandler;
import sample.SceneControl.SceneCreator;

public class Main extends Application  {

    public static void main(String[] args) {

        Application.launch(args);

    }

    public void start(Stage primaryStage) {

        new Thread(() -> new SceneCreator(primaryStage)).start();

        new Thread(DataBaseAgent::getInstance).start();

        new Thread(ConnectivityHandler::new).start();

        primaryStage.setOnCloseRequest(t -> {
            new ExitHandler();
            Platform.exit();
            System.exit(0);
        });


    }



}

package sample.SceneControl;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;


public class SceneCreator {

    public SceneCreator(Stage primaryStage){

        GeneralLayout window = new GeneralLayout();

        // Use tab pane with one tab for data UI and one tab for settings UI
        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab tabData= new Tab();

        tabData.setText("DataBase");
        tabData.setContent(window.DataTab());

        Tab tabSettings = new Tab();

        tabSettings.setText("Settings");
        tabSettings.setContent(window.SettingsTab());

        tabs.getTabs().addAll(tabData, tabSettings);

        Scene scene= new Scene(tabs, 300, 400);

        // Avoid throwing IllegalStateException by running from a non-JavaFX thread.
        Platform.runLater(
                () -> {
                    // Update UI here.
                    primaryStage.setTitle("Remote Collision Alert");
                    primaryStage.setScene(scene);
                    primaryStage.show();
                }
        );


    }

}

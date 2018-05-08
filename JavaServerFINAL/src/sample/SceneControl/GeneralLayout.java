package sample.SceneControl;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.TableView;
import sample.DataBases.DataBaseAgent;
import sample.StoredSettings;


class GeneralLayout {

    //TABLE VIEW AND DATA
    private ObservableList<ObservableList<String>> data1;
    private TableView tableview1;
    private TableView tableview2;
    //PAGINATION AND PREFERENCES
    private Pagination pagination;
    private final static int rowsPerPage = 23;
    private StoredSettings server_prefs;
    private DataBaseAgent agent;



    GeneralLayout(){

        // Initialize General Layout
        data1= FXCollections.observableArrayList();
        tableview1 = new TableView();
        tableview2 = new TableView();

        server_prefs = StoredSettings.getInstance();
        pagination = new Pagination(10);

        agent = DataBaseAgent.getInstance();
        agent.BuildTable(tableview1);
        agent.TerminalsSetColumns(tableview2);

    }


    private Node createPage(int pageIndex) {

        // Create specific page of pagination
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, data1.size());
        tableview1.setItems(FXCollections.observableArrayList(data1.subList(fromIndex, toIndex)));
        return new BorderPane(tableview1);

    }


    Pane DataTab() {

        // Get data from MySQL DataBase to populate the tableview
        data1=agent.BuildData();

        // Get current page
        pagination.setPageFactory(pageIndex -> {
            if (pageIndex > data1.size() / rowsPerPage + 1) {
                return null;
            } else {
                return createPage(pageIndex);
            }
        });

        // Set layout
        BorderPane border = new BorderPane(pagination);
        border.setPadding(new Insets(20, 20, 20, 20));
        border.setTop(SearchDataBase());

        return border;
    }


    private Pane SearchDataBase(){


        GridPane border = new GridPane();
        border.setPadding(new Insets(20, 20, 20, 20));
        border.setVgap(10);
        border.setHgap(70);


        //Field Labels for searching DataBase
        final Label field01 = new Label("Record ID:");
        final Label field02 = new Label("Terminal ID:");
        final Label field03 = new Label("Sensor ID:");
        final Label field04 = new Label("Sensor Value:");
        final Label field05 = new Label("Danger Status:");
        final Label field06 = new Label("Longitude:");
        final Label field07 = new Label("Latitude:");
        final Label field08 = new Label("TimeStamp:");

        // TextFields for searching DataBase
        TextField text01 = new TextField();
        TextField text02 = new TextField();
        TextField text03 = new TextField();
        TextField text04 = new TextField();
        TextField text05 = new TextField();
        TextField text06 = new TextField();
        TextField text07 = new TextField();
        TextField text08 = new TextField();


        // Set position in GridPane
        GridPane.setConstraints(field01, 0, 0);
        border.getChildren().add(field01);

        GridPane.setConstraints(text01, 1, 0);
        border.getChildren().add(text01);

        GridPane.setConstraints(field02, 0, 1);
        border.getChildren().add(field02);

        GridPane.setConstraints(text02, 1, 1);
        border.getChildren().add(text02);

        GridPane.setConstraints(field03, 0, 2);
        border.getChildren().add(field03);

        GridPane.setConstraints(text03, 1, 2);
        border.getChildren().add(text03);

        GridPane.setConstraints(field04, 0, 3);
        border.getChildren().add(field04);

        GridPane.setConstraints(text04, 1, 3);
        border.getChildren().add(text04);

        GridPane.setConstraints(field05, 2, 0);
        border.getChildren().add(field05);

        GridPane.setConstraints(text05, 3, 0);
        border.getChildren().add(text05);

        GridPane.setConstraints(field06, 2, 1);
        border.getChildren().add(field06);

        GridPane.setConstraints(text06, 3, 1);
        border.getChildren().add(text06);

        GridPane.setConstraints(field07, 2, 2);
        border.getChildren().add(field07);

        GridPane.setConstraints(text07, 3, 2);
        border.getChildren().add(text07);

        GridPane.setConstraints(field08, 2, 3);
        border.getChildren().add(field08);

        GridPane.setConstraints(text08, 3, 3);
        border.getChildren().add(text08);


        // Buttons for search and position in GridPane
        Button btnRefresh = new Button("Refresh");
        Button btnClear = new Button("Clear");
        Button btnShowAll = new Button("Show All");

        GridPane.setConstraints(btnClear, 4, 0);
        border.getChildren().add(btnClear);

        GridPane.setConstraints(btnRefresh, 4, 1);
        border.getChildren().add(btnRefresh);

        GridPane.setConstraints(btnShowAll, 4, 2);
        border.getChildren().add(btnShowAll);


        // Listener for Clear Button
        btnClear.setOnAction((ActionEvent e1) -> {

            // Clear all data in MySQL DataBase
            agent.ClearData();
            // Clear data
            data1=agent.BuildData();

            // Refresh pagination
            pagination.setPageFactory(pageIndex -> {
                if (pageIndex > data1.size() / rowsPerPage + 1) {
                    return null;
                } else {
                    return createPage(pageIndex);
                }
            });

        });

        // Listener for Refresh Button
        btnRefresh.setOnAction((ActionEvent e2) -> {
            String value = text01.getText()+","+text02.getText()+","
                    +text03.getText()+","+text04.getText()+","
                    +text05.getText()+","+text06.getText()+","
                    +text07.getText()+","+text08.getText()+",end";
            //System.out.println("search for: "+value);

            // Search for all the above substrings and refresh data
            data1 = agent.SearchData(value);
            //System.out.println(data1);
            //System.out.println("Refresh pressed");

            // Refresh pagination
            pagination.setPageFactory(pageIndex -> {
                if (pageIndex > data1.size() / rowsPerPage + 1) {
                    return null;
                } else {
                    return createPage(pageIndex);
                }
            });

        });

        //Listener for ShowAll Button
        btnShowAll.setOnAction((ActionEvent e2) -> {

            // Get all data from DataBase
            data1 = agent.BuildData();
            //System.out.println(data1);
            //System.out.println("Refresh pressed");

            // Refresh Pagination
            pagination.setPageFactory(pageIndex -> {
                if (pageIndex > data1.size() / rowsPerPage + 1) {
                    return null;
                } else {
                    return createPage(pageIndex);
                }
            });

        });

        return new BorderPane(border);
    }


    Pane SettingsTab() {
        GridPane bigborder = new GridPane();
        Button btnRefresh2 = new Button("Refresh");

        bigborder.setPadding(new Insets(20, 20, 20, 20));

        agent.TerminalsAvailable(tableview2);
        //buildTerminalData("TerminalDataBase","Terminal");

        BorderPane border1 = new BorderPane(bigborder);

        BorderPane border2 = new BorderPane(tableview2);




        //bigborder.add(btnClear2,0,0);
        bigborder.add(border2,0,1);
        bigborder.add(btnRefresh2,180,1);
        bigborder.add(Sliders_Thresholds(),0,100);
        bigborder.add(Sliders_Time_Intervals(),0,150);


        btnRefresh2.setOnAction((ActionEvent e2) -> {

            agent.TerminalsAvailable(tableview2);

            System.out.println("Refresh pressed");



        });


        return border1;
    }


    private Pane Sliders_Thresholds(){

        // Labels and Sliders for all Thresholds
        GridPane border = new GridPane();
        border.setPadding(new Insets(20, 20, 20, 20));
        border.setVgap(10);
        border.setHgap(70);

        //Light sensor
        Double progress = Double.parseDouble(server_prefs.getData(StoredSettings.NEW_THRESHOLD_01));
        final Slider threshold01 = new Slider(0, 40000, progress);

        //Proximity sensor
        progress = Double.parseDouble(server_prefs.getData(StoredSettings.NEW_THRESHOLD_02));
        final Slider threshold02 = new Slider(0, 10, progress);

        //Gyroscope sensor
        progress = Double.parseDouble(server_prefs.getData(StoredSettings.NEW_THRESHOLD_03_X));
        final Slider threshold03x = new Slider(0, 100, progress);
        progress = Double.parseDouble(server_prefs.getData(StoredSettings.NEW_THRESHOLD_03_Y));
        final Slider threshold03y = new Slider(0, 100, progress);
        progress = Double.parseDouble(server_prefs.getData(StoredSettings.NEW_THRESHOLD_03_Z));
        final Slider threshold03z = new Slider(0, 100, progress);

        //Linear Accelerator
        progress = Double.parseDouble(server_prefs.getData(StoredSettings.NEW_THRESHOLD_04_X));
        final Slider threshold04x = new Slider(0, 100, progress);
        progress = Double.parseDouble(server_prefs.getData(StoredSettings.NEW_THRESHOLD_04_Y));
        final Slider threshold04y = new Slider(0, 100, progress);
        progress = Double.parseDouble(server_prefs.getData(StoredSettings.NEW_THRESHOLD_04_Z));
        final Slider threshold04z = new Slider(0, 100, progress);

        //Accelerometer
        progress = Double.parseDouble(server_prefs.getData(StoredSettings.NEW_THRESHOLD_05_X));
        final Slider threshold05x = new Slider(0, 100, progress);
        progress = Double.parseDouble(server_prefs.getData(StoredSettings.NEW_THRESHOLD_05_Y));
        final Slider threshold05y = new Slider(0, 100, progress);
        progress = Double.parseDouble(server_prefs.getData(StoredSettings.NEW_THRESHOLD_05_Z));
        final Slider threshold05z = new Slider(0, 100, progress);

        //Light Sensor
        final Label thres01 = new Label("Light Sensor Threshold:");

        //Proximity Sensor
        final Label thres02 = new Label("Proximity Sensor Threshold:");

        //Gyroscope Sensor
        final Label thres03x = new Label("Gyroscope Sensor Threshold, X Axis:");
        final Label thres03y = new Label("Gyroscope Sensor Threshold, Y Axis:");
        final Label thres03z = new Label("Gyroscope Sensor Threshold, Z Axis:");

        //Linear Accelerator
        final Label thres04x = new Label("Linear Accelerator Threshold, X Axis:");
        final Label thres04y = new Label("Linear Accelerator Threshold, Y Axis:");
        final Label thres04z = new Label("Linear Accelerator Threshold, Z Axis:");

        //Accelerometer
        final Label thres05x = new Label("Accelerometer Threshold, X Axis:");
        final Label thres05y = new Label("Accelerometer Threshold, Y Axis:");
        final Label thres05z = new Label("Accelerometer Threshold, Z Axis:");

        //Light Sensor
        final Label thres01value = new Label(Double.toString(threshold01.getValue())+" lux");

        //Proximity Sensor
        final Label thres02value = new Label(Double.toString(threshold02.getValue())+" cm");

        //Gyroscope Sensor
        final Label thres03xvalue = new Label(Double.toString(threshold03x.getValue())+" rad/s");
        final Label thres03yvalue = new Label(Double.toString(threshold03y.getValue())+" rad/s");
        final Label thres03zvalue = new Label(Double.toString(threshold03z.getValue())+" rad/s");

        //Linear Accelerator
        final Label thres04xvalue = new Label(Double.toString(threshold04x.getValue())+" m/s^2");
        final Label thres04yvalue = new Label(Double.toString(threshold04y.getValue())+" m/s^2");
        final Label thres04zvalue = new Label(Double.toString(threshold04z.getValue())+" m/s^2");

        //Accelerometer
        final Label thres05xvalue = new Label(Double.toString(threshold05x.getValue())+" m/s^2");
        final Label thres05yvalue = new Label(Double.toString(threshold05y.getValue())+" m/s^2");
        final Label thres05zvalue = new Label(Double.toString(threshold05z.getValue())+" m/s^2");


        // Position in GridPane
        GridPane.setConstraints(thres01, 0, 0);
        border.getChildren().add(thres01);

        GridPane.setConstraints(threshold01, 1, 0);
        border.getChildren().add(threshold01);

        GridPane.setConstraints(thres01value, 2, 0);
        border.getChildren().add(thres01value);


        GridPane.setConstraints(thres02, 0, 1);
        border.getChildren().add(thres02);

        GridPane.setConstraints(threshold02, 1, 1);
        border.getChildren().add(threshold02);

        GridPane.setConstraints(thres02value, 2, 1);
        border.getChildren().add(thres02value);

        GridPane.setConstraints(thres03x, 0, 2);
        border.getChildren().add(thres03x);

        GridPane.setConstraints(threshold03x, 1, 2);
        border.getChildren().add(threshold03x);

        GridPane.setConstraints(thres03xvalue, 2, 2);
        border.getChildren().add(thres03xvalue);

        GridPane.setConstraints(thres03y, 0, 3);
        border.getChildren().add(thres03y);

        GridPane.setConstraints(threshold03y, 1, 3);
        border.getChildren().add(threshold03y);

        GridPane.setConstraints(thres03yvalue, 2, 3);
        border.getChildren().add(thres03yvalue);

        GridPane.setConstraints(thres03z, 0, 4);
        border.getChildren().add(thres03z);

        GridPane.setConstraints(threshold03z, 1, 4);
        border.getChildren().add(threshold03z);

        GridPane.setConstraints(thres03zvalue, 2, 4);
        border.getChildren().add(thres03zvalue);


        GridPane.setConstraints(thres04x, 0, 5);
        border.getChildren().add(thres04x);

        GridPane.setConstraints(threshold04x, 1, 5);
        border.getChildren().add(threshold04x);

        GridPane.setConstraints(thres04xvalue, 2, 5);
        border.getChildren().add(thres04xvalue);

        GridPane.setConstraints(thres04y, 0, 6);
        border.getChildren().add(thres04y);

        GridPane.setConstraints(threshold04y, 1, 6);
        border.getChildren().add(threshold04y);

        GridPane.setConstraints(thres04yvalue, 2, 6);
        border.getChildren().add(thres04yvalue);

        GridPane.setConstraints(thres04z, 0, 7);
        border.getChildren().add(thres04z);

        GridPane.setConstraints(threshold04z, 1, 7);
        border.getChildren().add(threshold04z);

        GridPane.setConstraints(thres04zvalue, 2, 7);
        border.getChildren().add(thres04zvalue);

        GridPane.setConstraints(thres05x, 0, 8);
        border.getChildren().add(thres05x);

        GridPane.setConstraints(threshold05x, 1, 8);
        border.getChildren().add(threshold05x);

        GridPane.setConstraints(thres05xvalue, 2, 8);
        border.getChildren().add(thres05xvalue);

        GridPane.setConstraints(thres05y, 0, 9);
        border.getChildren().add(thres05y);

        GridPane.setConstraints(threshold05y, 1, 9);
        border.getChildren().add(threshold05y);

        GridPane.setConstraints(thres05yvalue, 2, 9);
        border.getChildren().add(thres05yvalue);

        GridPane.setConstraints(thres05z, 0, 10);
        border.getChildren().add(thres05z);

        GridPane.setConstraints(threshold05z, 1, 10);
        border.getChildren().add(threshold05z);

        GridPane.setConstraints(thres05zvalue, 2, 10);
        border.getChildren().add(thres05zvalue);


        // Listeners for Sliders
        threshold01.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                thres01value.setText(String.format("%.2f lux", new_val));
                server_prefs.saveData(StoredSettings.NEW_THRESHOLD_01,new_val.toString());
            }
        });

        threshold02.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                thres02value.setText(String.format("%.2f cm", new_val));
                server_prefs.saveData(StoredSettings.NEW_THRESHOLD_02,new_val.toString());

            }
        });

        threshold03x.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                thres03xvalue.setText(String.format("%.2f rad/s", new_val));
                server_prefs.saveData(StoredSettings.NEW_THRESHOLD_03_X,new_val.toString());

            }
        });
        threshold03y.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                thres03yvalue.setText(String.format("%.2f rad/s", new_val));
                server_prefs.saveData(StoredSettings.NEW_THRESHOLD_03_Y,new_val.toString());

            }
        });
        threshold03z.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                thres03zvalue.setText(String.format("%.2f rad/s", new_val));
                server_prefs.saveData(StoredSettings.NEW_THRESHOLD_03_Z,new_val.toString());

            }
        });

        threshold04x.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                thres04xvalue.setText(String.format("%.2f m/s^2", new_val));
                server_prefs.saveData(StoredSettings.NEW_THRESHOLD_04_X,new_val.toString());

            }
        });
        threshold04y.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                thres04yvalue.setText(String.format("%.2f m/s^2", new_val));
                server_prefs.saveData(StoredSettings.NEW_THRESHOLD_04_Y,new_val.toString());

            }
        });
        threshold04z.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                thres04zvalue.setText(String.format("%.2f m/s^2", new_val));
                server_prefs.saveData(StoredSettings.NEW_THRESHOLD_04_Z,new_val.toString());

            }
        });

        threshold05x.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                thres05xvalue.setText(String.format("%.2f m/s^2", new_val));
                server_prefs.saveData(StoredSettings.NEW_THRESHOLD_05_X,new_val.toString());

            }
        });
        threshold05y.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                thres05yvalue.setText(String.format("%.2f m/s^2", new_val));
                server_prefs.saveData(StoredSettings.NEW_THRESHOLD_05_Y,new_val.toString());

            }
        });
        threshold05z.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                thres05zvalue.setText(String.format("%.2f m/s^2", new_val));
                server_prefs.saveData(StoredSettings.NEW_THRESHOLD_05_Z,new_val.toString());

            }
        });

        return new BorderPane(border);
    }

    private Pane Sliders_Time_Intervals(){

        // Labels and Sliders for Time Intervals
        GridPane border = new GridPane();
        border.setPadding(new Insets(20, 20, 20, 20));
        border.setVgap(10);
        border.setHgap(70);

        //Light sensor
        Double progress = Double.parseDouble(server_prefs.getData(StoredSettings.NEW_TIME_INTERVAL_01));
        final Slider threshold01 = new Slider(0, 5000, progress);

        //Proximity sensor
        progress = Double.parseDouble(server_prefs.getData(StoredSettings.NEW_TIME_INTERVAL_02));
        final Slider threshold02 = new Slider(0, 5000, progress);

        //Gyroscope sensor
        progress = Double.parseDouble(server_prefs.getData(StoredSettings.NEW_TIME_INTERVAL_03));
        final Slider threshold03x = new Slider(0, 5000, progress);

        //Linear Accelerator
        progress = Double.parseDouble(server_prefs.getData(StoredSettings.NEW_TIME_INTERVAL_04));
        final Slider threshold04x = new Slider(0, 5000, progress);

        //Accelerometer
        progress = Double.parseDouble(server_prefs.getData(StoredSettings.NEW_TIME_INTERVAL_05));
        final Slider threshold05x = new Slider(0, 5000, progress);

        //Light Sensor
        final Label thres01 = new Label("Light Sensor Time Interval:");

        //Proximity Sensor
        final Label thres02 = new Label("Proximity Sensor Time Interval:");

        //Gyroscope Sensor
        final Label thres03x = new Label("Gyroscope Sensor Time Interval:");

        //Linear Accelerator
        final Label thres04x = new Label("Linear Accelerator Time Interval:");

        //Accelerometer
        final Label thres05x = new Label("Accelerometer Time Interval:");

        //Light Sensor
        final Label thres01value = new Label(Double.toString(threshold01.getValue())+" ms");

        //Proximity Sensor
        final Label thres02value = new Label(Double.toString(threshold02.getValue())+" ms");

        //Gyroscope Sensor
        final Label thres03xvalue = new Label(Double.toString(threshold03x.getValue())+" ms");

        //Linear Accelerator
        final Label thres04xvalue = new Label(Double.toString(threshold04x.getValue())+" ms");

        //Accelerometer
        final Label thres05xvalue = new Label(Double.toString(threshold05x.getValue())+" ms");


        // Position in GridPane
        GridPane.setConstraints(thres01, 0, 0);
        border.getChildren().add(thres01);

        GridPane.setConstraints(threshold01, 1, 0);
        border.getChildren().add(threshold01);

        GridPane.setConstraints(thres01value, 2, 0);
        border.getChildren().add(thres01value);

        GridPane.setConstraints(thres02, 0, 1);
        border.getChildren().add(thres02);

        GridPane.setConstraints(threshold02, 1, 1);
        border.getChildren().add(threshold02);

        GridPane.setConstraints(thres02value, 2, 1);
        border.getChildren().add(thres02value);

        GridPane.setConstraints(thres03x, 0, 2);
        border.getChildren().add(thres03x);

        GridPane.setConstraints(threshold03x, 1, 2);
        border.getChildren().add(threshold03x);

        GridPane.setConstraints(thres03xvalue, 2, 2);
        border.getChildren().add(thres03xvalue);

        GridPane.setConstraints(thres04x, 0, 3);
        border.getChildren().add(thres04x);

        GridPane.setConstraints(threshold04x, 1, 3);
        border.getChildren().add(threshold04x);

        GridPane.setConstraints(thres04xvalue, 2, 3);
        border.getChildren().add(thres04xvalue);

        GridPane.setConstraints(thres05x, 0, 4);
        border.getChildren().add(thres05x);

        GridPane.setConstraints(threshold05x, 1, 4);
        border.getChildren().add(threshold05x);

        GridPane.setConstraints(thres05xvalue, 2, 4);
        border.getChildren().add(thres05xvalue);


        // Listeners for Sliders
        threshold01.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                thres01value.setText(String.format("%.2f ms", new_val));
                server_prefs.saveData(StoredSettings.NEW_TIME_INTERVAL_01,new_val.toString());
            }
        });

        threshold02.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                thres02value.setText(String.format("%.2f ms", new_val));
                server_prefs.saveData(StoredSettings.NEW_TIME_INTERVAL_02,new_val.toString());

            }
        });

        threshold03x.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                thres03xvalue.setText(String.format("%.2f ms", new_val));
                server_prefs.saveData(StoredSettings.NEW_TIME_INTERVAL_03,new_val.toString());

            }
        });


        threshold04x.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                thres04xvalue.setText(String.format("%.2f ms", new_val));
                server_prefs.saveData(StoredSettings.NEW_TIME_INTERVAL_04,new_val.toString());

            }
        });


        threshold05x.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                thres05xvalue.setText(String.format("%.2f ms", new_val));
                server_prefs.saveData(StoredSettings.NEW_TIME_INTERVAL_05,new_val.toString());

            }
        });

        return new BorderPane(border);
    }




}

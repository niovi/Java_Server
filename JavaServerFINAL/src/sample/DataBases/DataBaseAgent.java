package sample.DataBases;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import sample.DataBases.Terminals.TerminalTree;
import sample.StoredSettings;

import java.sql.*;

import static sample.StoredSettings.NUM_OF_RECORDS;


public class DataBaseAgent {


    private static DataBaseAgent mysql_agent;
    private StoredSettings server_prefs;


    private static Connection connection = null;
    private static Statement statement = null;

    private DataBaseAgent() {

        server_prefs = StoredSettings.getInstance();

        // Connecting to local MySQL DataBase
        String DRIVER = "com.mysql.jdbc.Driver";
        String DATABASE_URL = "jdbc:mysql://localhost:3306/CollisionDataBase";


        try {
            Class.forName(DRIVER).newInstance();
            try {
                connection = DriverManager.getConnection(DATABASE_URL, "root", "akka");
                statement = connection.createStatement();
                System.out.println("SQL connected");

            } catch (SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("VendorError: " + e.getErrorCode());
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }


    }


    public static DataBaseAgent getInstance() {

        if (mysql_agent == null) {
            mysql_agent = new DataBaseAgent();
        }
        return mysql_agent;

    }


    public ObservableList<ObservableList<String>> BuildData(){

        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

        try{

            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = "SELECT * FROM Record order by cast(recordID as unsigned) ASC";
            //ResultSet
            ResultSet rs = statement.executeQuery(SQL);

            /********************************
             * Data added to ObservableList *
             ********************************/
            while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                //System.out.println("Row [1] added "+row );
                data.add(row);

            }

            return data;
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
        // In case of error return null
        return null;
    }


    public ObservableList<ObservableList<String>> SearchData(String value){

        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

        String[] fieldsArray = value.split(",");

        int k=0;
        for(k=0;k<fieldsArray.length-1;k++){
            //System.out.print("before "+fieldsArray[k]);
            if (fieldsArray[k].equals(""))fieldsArray[k]="'%'";
            else fieldsArray[k]="'%"+fieldsArray[k]+"%'";
            //System.out.println(" after "+fieldsArray[k]);
        }

        // Search for all substrings given by user in the appropriate column
        String search = "WHERE recordID LIKE "+fieldsArray[0]+" AND terminalID LIKE " +fieldsArray[1]
                +" AND sensorID LIKE "+fieldsArray[2]+" AND SensorValue LIKE "+fieldsArray[3]
                +" AND dangerSTATUS LIKE "+fieldsArray[4]+" AND positionX LIKE "+fieldsArray[5]
                +" AND positionY LIKE "+fieldsArray[6]+" AND EventTimeStamp LIKE "+fieldsArray[7];

        String SQL;
        SQL = "SELECT * FROM Record ";
        String order = " order by cast(recordID as unsigned) ASC";


        try{

            //ResultSet
            ResultSet rs = statement.executeQuery(SQL+search+order);

            /********************************
             * Data added to ObservableList *
             ********************************/
            while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                //System.out.println("Row [1] added "+row );
                data.add(row);

            }
            return data;
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
        // in case of error return null
        return null;

    }

    public void BuildTable(TableView table){

        try{

            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = "SELECT * FROM Record";
            //ResultSet
            ResultSet rs = statement.executeQuery(SQL);

            for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){

                //We are using non property style for making dynamic table

                final int j = i;

                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));

                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){

                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList,String> param) {

                        return new SimpleStringProperty(param.getValue().get(j).toString());

                    }

                });

                table.getColumns().addAll(col);
                System.out.println("Column ["+i+"] ");

            }

            table.setRowFactory(tv -> new TableRow<ObservableList>() {
                @Override
                public void updateItem(ObservableList item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setStyle("");
                    } else if (item.get(3).equals("VERIFIED")) {
                        setStyle("-fx-background-color: #95baff;");
                    } else {
                        setStyle("");
                    }
                }
            });


        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on Building Columns");
        }

    }

    public void ClearData(){

        // Clear all data in the MySQL DataBase
        try{

            String SQL = "DELETE FROM Record";

            statement.executeUpdate(SQL);

            server_prefs.saveData(NUM_OF_RECORDS,"0");

        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on Clearing all Data");
        }

    }

    public void TerminalsAvailable(TableView table){

        //System.out.println("Searching for terminals available...");
        //Find terminals available
        table.getItems().removeAll();
        ObservableList<String> data= FXCollections.observableArrayList();
        data.clear();
        TerminalTree terminals = TerminalTree.getInstance();
        data = terminals.TerminalsAvailable();
        // Populate tableview with uuids
        table.setItems(data);
        table.refresh();
        System.out.println("Terminals available: "+data);
    }

    public void TerminalsSetColumns(TableView table){
        // Set Columns In Tableview of Record Data

        //System.out.println("Formating tableview for terminals available...");

        TableColumn col = new TableColumn("UUID");

        col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<String, String> p) {
                String x = p.getValue();

                    return new SimpleStringProperty(x);

            }
        });

        table.getColumns().addAll(col);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        System.out.println("Tableview ready");

    }


}

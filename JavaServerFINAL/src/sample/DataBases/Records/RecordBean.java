package sample.DataBases.Records;

import com.sun.rowset.JdbcRowSetImpl;

import java.sql.SQLException;
import javax.sql.rowset.JdbcRowSet;


class RecordBean {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/CollisionDataBase";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "akka";

    private JdbcRowSet rowSet = null;
    public RecordBean() {
        try {
            Class.forName(JDBC_DRIVER);
            rowSet = new JdbcRowSetImpl();
            rowSet.setUrl(DB_URL);
            rowSet.setUsername(DB_USER);
            rowSet.setPassword(DB_PASS);
            rowSet.setCommand("SELECT * FROM Record");
            rowSet.execute();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        catch (ClassNotFoundException ex){
            ex.printStackTrace();
        }
    }


    public Record create(Record p) {
        try {
            rowSet.moveToInsertRow();
            rowSet.updateString("recordID", p.get_record_id());
            rowSet.updateString("terminalID", p.get_terminal_id());
            rowSet.updateString("sensorID", p.get_sensor_id());
            rowSet.updateString("dangerSTATUS", p.get_danger_status());
            rowSet.updateString("SensorValue", p.get_sensor_value());
            rowSet.updateString("EventTimeStamp", p.get_timestamp());
            rowSet.updateString("positionX", p.get_position_x());
            rowSet.updateString("positionY", p.get_position_y());
            rowSet.insertRow();
            rowSet.moveToCurrentRow();
        } catch (SQLException ex) {
            try {
                rowSet.rollback();
                p = null;
            } catch (SQLException e) {

            }
            ex.printStackTrace();
        }
        return p;
    }


}




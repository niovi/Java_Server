package sample.DataBases.Records;


public class Record {

private String record_id;
private String terminal_id;
private String sensor_id;
private String status; // verified danger or not
private String sensor_value;
private String time_stamp;

private String position_x;
private String position_y;

 Record(String r_id,String t_id,String s_id,String s_value,String t_stamp, String p_x,String p_y){

    record_id = r_id;
    terminal_id = t_id;
    sensor_id = s_id;
    status="unknown";
    sensor_value = s_value;
    time_stamp = t_stamp ;

    position_x = p_x;
    position_y = p_y;

    }


    String get_record_id(){
        return record_id;
    }

    void set_record_id(String id){
        record_id = id;
    }

    String get_terminal_id(){
        return terminal_id;
    }

    String get_sensor_id(){
        return sensor_id;
    }

    void set_sensor_id(String id){
        sensor_id = id;
    }

    String get_sensor_value(){
        return sensor_value;
    }

    String get_danger_status(){
        return status;
    }

    void set_danger_status(String value){
        status = value;
    }

    String get_timestamp(){
        return time_stamp;
    }

    String get_position_x(){
        return position_x;
    }

    String get_position_y(){
        return position_y;
    }


}


package sample.DataBases.Records;

import sample.DataBases.Terminals.DangerDetector;
import sample.DataBases.Terminals.TerminalTree;
import sample.StoredSettings;

import java.util.Arrays;

import static sample.StoredSettings.NUM_OF_RECORDS;


public class SortRecords {

    private DangerDetector danger_detector = DangerDetector.getInstance();
    private StoredSettings server_prefs = StoredSettings.getInstance();
    private TerminalTree terminal_tree = TerminalTree.getInstance();
    private String sensor,term;

    public SortRecords(Record rec) {
    sensor = rec.get_sensor_id();
    Double thres;
    term =rec.get_terminal_id();



        switch (sensor) {
            case "value_01": {
                thres = Double.parseDouble(server_prefs.getData(StoredSettings.NEW_THRESHOLD_01));
                Double value = Double.parseDouble(rec.get_sensor_value());

                if (value < thres) {
                rec.set_sensor_id("light");
                set_record(rec);

                }else terminal_tree.SetDanger(term,sensor,"FALSE");

                break;
            }
            case "value_02": {
                thres = Double.parseDouble(server_prefs.getData(StoredSettings.NEW_THRESHOLD_02));
                Double value = Double.parseDouble(rec.get_sensor_value());

                if (value < thres) {
                    rec.set_sensor_id("proximity");

                    set_record(rec);

                }else terminal_tree.SetDanger(term,sensor,"FALSE");
                break;
            }
            case "value_03_X": {
                thres = Double.parseDouble(server_prefs.getData(StoredSettings.NEW_THRESHOLD_03_X));
                Double value = Double.parseDouble(rec.get_sensor_value());

                if (value > thres) {
                    rec.set_sensor_id("gyroscope X");

                    set_record(rec);

                }else terminal_tree.SetDanger(term,sensor,"FALSE");
                break;
            }
            case "value_03_Y": {
                thres = Double.parseDouble(server_prefs.getData(StoredSettings.NEW_THRESHOLD_03_Y));
                Double value = Double.parseDouble(rec.get_sensor_value());

                if (value > thres) {
                    rec.set_sensor_id("gyroscope Y");
                    set_record(rec);


                }else terminal_tree.SetDanger(term,sensor,"FALSE");
                break;
            }
            case "value_03_Z": {
                thres = Double.parseDouble(server_prefs.getData(StoredSettings.NEW_THRESHOLD_03_Z));
                Double value = Double.parseDouble(rec.get_sensor_value());

                if (value > thres) {
                    rec.set_sensor_id("gyroscope Z");
                    set_record(rec);

                }else terminal_tree.SetDanger(term,sensor,"FALSE");
                break;
            }
            case "value_04_X": {
                thres = Double.parseDouble(server_prefs.getData(StoredSettings.NEW_THRESHOLD_04_X));
                Double value = Double.parseDouble(rec.get_sensor_value());

                if (value > thres) {
                    rec.set_sensor_id("linear accelerator X");
                    set_record(rec);

                }else terminal_tree.SetDanger(term,sensor,"FALSE");
                break;
            }
            case "value_04_Y": {
                thres = Double.parseDouble(server_prefs.getData(StoredSettings.NEW_THRESHOLD_04_Y));
                Double value = Double.parseDouble(rec.get_sensor_value());

                if (value > thres) {rec.set_sensor_id("linear accelerator Y");
                    set_record(rec);


                }else terminal_tree.SetDanger(term,sensor,"FALSE");
                break;
            }
            case "value_04_Z": {
                thres = Double.parseDouble(server_prefs.getData(StoredSettings.NEW_THRESHOLD_04_Z));
                Double value = Double.parseDouble(rec.get_sensor_value());

                if (value > thres) {
                    rec.set_sensor_id("linear accelerator Z");
                    set_record(rec);

                }else terminal_tree.SetDanger(term,sensor,"FALSE");
                break;
            }
            case "value_05_X": {
                thres = Double.parseDouble(server_prefs.getData(StoredSettings.NEW_THRESHOLD_05_X));
                Double value = Double.parseDouble(rec.get_sensor_value());

                if (value > thres) {
                    rec.set_sensor_id("accelerometer X");
                    set_record(rec);

                }else terminal_tree.SetDanger(term,sensor,"FALSE");
                break;
            }
            case "value_05_Y": {
                thres = Double.parseDouble(server_prefs.getData(StoredSettings.NEW_THRESHOLD_05_Y));
                Double value = Double.parseDouble(rec.get_sensor_value());

                if (value > thres) {
                    rec.set_sensor_id("accelerometer Y");
                    set_record(rec);
                }else terminal_tree.SetDanger(term,sensor,"FALSE");
                break;
            }
            case "value_05_Z": {
                thres = Double.parseDouble(server_prefs.getData(StoredSettings.NEW_THRESHOLD_05_Z));
                Double value = Double.parseDouble(rec.get_sensor_value());

                if (value > thres) {
                    rec.set_sensor_id("accelerometer Z");
                    set_record(rec);

                }else terminal_tree.SetDanger(term,sensor,"FALSE");
                break;
            }
        }



    danger_detector.CheckTerminal(term);


}

private void set_record(Record rec){


    terminal_tree.SetDanger(term,sensor,"TRUE");

    int m = (int) Double.parseDouble(server_prefs.getData(NUM_OF_RECORDS));
    m++;
    rec.set_record_id(String.valueOf(m));
    server_prefs.saveData(NUM_OF_RECORDS, String.valueOf(m));
    rec.set_danger_status(danger_detector.CheckTerminal(term));
    RecordBean bean = new RecordBean();
    bean.create(rec);
    System.out.println(term + " in danger!");


}

public static Record extract_record(String in_mess){


    String[] fieldsArray = in_mess.split(",");
    if(fieldsArray.length>5) {
        System.out.println(Arrays.toString(fieldsArray));
        String time_stamp = fieldsArray[0];
        String term_id = fieldsArray[1];
        String sensor_id = fieldsArray[2];
        String value = fieldsArray[3];
        String gps_x = fieldsArray[4];
        String gps_y = fieldsArray[5];

        return new Record("0", term_id, sensor_id, value, time_stamp, gps_x, gps_y);
    }

    return null;


}

}

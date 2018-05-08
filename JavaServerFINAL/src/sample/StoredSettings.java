package sample;

import sample.MQTT.Mqtt_publisher;

import java.util.prefs.Preferences;

public class StoredSettings {

    // light sensor
    public static final String NEW_THRESHOLD_01 = "threshold_01";
    public static final String NEW_TIME_INTERVAL_01 = "time_interval_01";

    // proximity sensor
    public static final String NEW_THRESHOLD_02 = "threshold_02";
    public static final String NEW_TIME_INTERVAL_02 = "time_interval_02";

    // gyroscope sensor
    public static final String NEW_TIME_INTERVAL_03 = "time_interval_03";
    public static final String NEW_THRESHOLD_03_X = "threshold_03_X";
    public static final String NEW_THRESHOLD_03_Y = "threshold_03_Y";
    public static final String NEW_THRESHOLD_03_Z = "threshold_03_Z";

    // linear accelerator sensor
    public static final String NEW_TIME_INTERVAL_04= "time_interval_04";
    public static final String NEW_THRESHOLD_04_X = "threshold_04_X";
    public static final String NEW_THRESHOLD_04_Y = "threshold_04_Y";
    public static final String NEW_THRESHOLD_04_Z = "threshold_04_Z";

    // accelerometer sensor
    public static final String NEW_TIME_INTERVAL_05= "time_interval_05";
    public static final String NEW_THRESHOLD_05_X = "threshold_05_X";
    public static final String NEW_THRESHOLD_05_Y = "threshold_05_Y";
    public static final String NEW_THRESHOLD_05_Z = "threshold_05_Z";

    // TERMINALS TO SUBSCRIBE TO
    public static final String NUM_OF_TERMINALS = "number_of_terminals";
    public static final String NUM_OF_RECORDS = "number_of_records";

    // VARIABLES
    private static StoredSettings Set_preferences;
    private Preferences Stored_settings;


    private StoredSettings() {
        Stored_settings = Preferences.userRoot().node("java-server-storage");
    }

    public static StoredSettings getInstance() {
        if (Set_preferences == null) {
            Set_preferences = new StoredSettings();
        }
        return Set_preferences;
    }

    public void saveData(String key, String value) {
        Stored_settings.put(key, value);
        if(key.contains("interval")){
            Mqtt_publisher pub = Mqtt_publisher.getInstance();
            pub.publish("Time_Intervals",key+","+value);
        }
    }

    public String getData(String key) {
        return Stored_settings.get(key,"0.0");
    }











}

package sample.MQTT;

import sample.DataBases.Records.Record;
import sample.DataBases.Records.SortRecords;
import sample.DataBases.Terminals.TerminalTree;


class MessageSort {


    static void sorting(String in_mess) {


        String[] fieldsArray = in_mess.split(",");
        String topic = fieldsArray[1];
        TerminalTree terminal_tree = TerminalTree.getInstance();

        // Sort messages according to topic
        if(topic.equals("Availability"))
    {
        if(fieldsArray[3].equals("ON"))
        terminal_tree.AddTerminal(fieldsArray[2]);
        else if(terminal_tree.GetTerminal(fieldsArray[2])!=null)
                terminal_tree.RemoveTerminal(fieldsArray[2]);
    }
        else
    {
        Record rec = SortRecords.extract_record(in_mess);
        new SortRecords(rec);
    }

}


}

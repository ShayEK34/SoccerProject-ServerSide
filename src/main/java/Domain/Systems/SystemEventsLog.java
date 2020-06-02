package Domain.Systems;

import Domain.AssociationManagement.DateParser;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.Date;

/**
 * represent the Events log system.
 */
public class SystemEventsLog {

    /**
     * constructor.
     */
    public SystemEventsLog() {
    }

    /**
     * add new event log to the file.
     * @param content
     */
    public void addEventLog(String userName, String content) throws Exception {
        //Date today = Calendar.getInstance().getTime();
        String path="C:/Users/User/Desktop/Eventslog.txt";
        File file=new File(path);
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw=new BufferedWriter(fw);
        StringBuilder allStr=new StringBuilder();
        allStr.append(java.time.LocalDate.now()+", "+java.time.LocalTime.now()+"  |  "+userName+"  |  "+content+"\n");
        bw.append(allStr.toString());
        bw.close();
    }
}

package Domain.Systems;

import java.util.Calendar;
import java.util.Date;

public class Complain {
    String complain;
    String answer;
    String userName;
    Date date;

    public Complain(String context, String userName) {
        this.complain = context;
        this.userName = userName;
        this.date= Calendar.getInstance().getTime();
        this.answer="none";
    }

    public String getComplain() {
        return complain;
    }

    public String getUserName() {
        return userName;
    }

    public Date getDate() {
        return date;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        String comp="Username "+userName+": "+complain+". (At: "+date.toString()+")"+"\n"+"Answer: "+answer;
        return comp;
    }


}

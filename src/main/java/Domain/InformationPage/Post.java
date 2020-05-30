package Domain.InformationPage;

import Domain.AssociationManagement.DateParser;

import java.util.Calendar;
import java.util.Date;

public class Post {
    private String content;
    private String date;

    public Post(String content) {
        this.content = content;
        Date today = Calendar.getInstance().getTime(); ;
        this.date = DateParser.toString(today);
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

}


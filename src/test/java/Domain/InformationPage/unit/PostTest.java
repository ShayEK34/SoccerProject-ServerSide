package Domain.InformationPage.unit;

import Domain.AssociationManagement.DateParser;
import Domain.InformationPage.Post;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class PostTest {

    private Post post;
    private  String date;

    @Before
    public void init(){
        post=new Post("test");
        Date today = Calendar.getInstance().getTime(); ;
        date = DateParser.toString(today);
    }


    @Test
    public void getContentTest(){
        Assert.assertEquals(post.getContent(),"test");
    }

    @Test
    public void getDateTest(){
        Assert.assertNotNull(post.getDate());
    }

    @Test
    public void setDateTest(){
        post.setDate(date);
        Assert.assertEquals(post.getDate(),date);
    }

    @Test
    public void setContentTest(){
        post.setContent("check");
        Assert.assertEquals(post.getContent(),"check");
    }
}


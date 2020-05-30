package Domain.User.unit;
import Domain.User.Fan;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;


public class FanTest {

    Fan fan;


    @Before
    public void init() {
        fan=new Fan("Ran", "Cohen", "ran123", "ran1234","student","2/1/1996","ran@gmail.com");
    }


    @Test
    public void setStatusTest(){
        fan.setStatus(Fan.Status.offline);
        Assert.assertEquals(fan.getStatus(), Fan.Status.offline);
        fan.setStatus(Fan.Status.online);
        Assert.assertEquals(fan.getStatus(), Fan.Status.online);
    }

    @Test
    public void getFullNameTest(){
        Assert.assertEquals(fan.getFirstName(),"Ran");
        Assert.assertEquals(fan.getLastName(),"Cohen");
    }

    @Test
    public void getUserNameTest(){
        Assert.assertEquals(fan.getUserName(),"ran123");
    }

    @Test
    public void getPasswordTest(){
        Assert.assertEquals(fan.getPassword(),"ran1234");
    }

    @Test
    public void getOccupationTest(){
        Assert.assertEquals(fan.getOccupation(),"student");
    }

    @Test
    public void getBirthdayTest(){
        Assert.assertEquals(fan.getBirthday(),"2/1/1996");
    }

    @Test
    public void getEmailTest(){
        Assert.assertEquals(fan.getEmail(),"ran@gmail.com");
    }

    @Test
    public void getStatusTest(){
        Assert.assertEquals(fan.getStatus(), Fan.Status.offline);
        fan.setStatus(Fan.Status.online);
        Assert.assertEquals(fan.getStatus(), Fan.Status.online);

    }

    @Test
    public void setFirstNameTest(){
        fan.setFirstName("David");
        Assert.assertEquals(fan.getFirstName(),"David");
    }

    @Test
    public void setLastNameTest(){
        fan.setLastName("Levi");
        Assert.assertEquals(fan.getLastName(),"Levi");
    }

    @Test
    public void setPasswordTest(){
        fan.setPassword("123456");
        Assert.assertEquals(fan.getPassword(),"123456");
    }

    @Test
    public void setOccupationTest(){
        fan.setOccupation("No Occupation");
        Assert.assertEquals(fan.getOccupation(),"No Occupation");
    }

    @Test
    public void setBirthdayTest(){
        fan.setBirthday("1/2/1996");
        Assert.assertEquals(fan.getBirthday(),"1/2/1996");
    }

    @Test
    public void setEmailTest(){
        fan.setEmail("rancohen@gmail.com");
        Assert.assertEquals(fan.getEmail(),"rancohen@gmail.com");
    }

    @Test
    public void editInfoTest(){
        fan.editInfo("David","Levi","dav123","No Occupation","21/1/1997","david@gmail.com");
        Assert.assertEquals(fan.getFirstName(),"David");
        Assert.assertEquals(fan.getLastName(),"Levi");
        Assert.assertEquals(fan.getPassword(),"dav123");
        Assert.assertEquals(fan.getOccupation(),"No Occupation");
        Assert.assertEquals(fan.getBirthday(),"21/1/1997");
        Assert.assertEquals(fan.getEmail(),"david@gmail.com");
    }




}

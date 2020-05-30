package Domain.User.unit;
import Domain.User.Referee;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class RefereeTest {

    private Referee referee1;

    @Before
    public void init() {
        referee1=new Referee("raanan", "mesh", "raananm", "123","student","19/9/1965","raanan@gmail.com", "");
    }

//    @Test
//    public void getPreparationTest(){
//        Assert.assertEquals(referee1.getPreparation(), Referee.Role.main);
//    }

    @Test
    public void EditRefereeInfoTest(){
        referee1.EditRefereeInfo("rani", "meshu", "1234", "student","19/9/1965","raanan@gmail.com");
        Assert.assertEquals(referee1.getFirstName(),"rani");
        Assert.assertEquals(referee1.getLastName(),"meshu");
        Assert.assertEquals(referee1.getPassword(),"1234");
        Assert.assertEquals(referee1.getOccupation(),"student");
        Assert.assertEquals(referee1.getBirthday(),"19/9/1965");
        Assert.assertEquals(referee1.getEmail(),"raanan@gmail.com");
    }

    @Test
    public void setPreparationTest(){
        referee1.setRefereeRole("Main Referee");
        Assert.assertEquals(referee1.getRefereeRole(), "Main Referee");
    }

    @Test
    public void ViewAssignGamesTest(){
        Assert.assertEquals(referee1.ViewAssignGames(),referee1.getRefereeMatches());
    }

    @Test
    public void inviteToBeRefereeTest(){
        Assert.assertEquals(referee1.inviteToBeReferee(),true);
    }


}

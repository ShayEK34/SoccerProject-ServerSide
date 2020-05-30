package Domain.User.it;
//***
import Data.SystemDB.DataBaseInterface;
import Domain.InformationPage.InformationPage;
import Data.SystemDB.DB;
import Domain.ClubManagement.Court;
import Domain.ClubManagement.TeamInfo;
import Domain.InformationPage.TeamInformationPage;
import Domain.InformationPage.TeamMemberInformationPage;
import Domain.User.Fan;
import Domain.User.Guest;
import Domain.User.SystemManager;
import Domain.User.TeamMember;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class FanIT {

    private DataBaseInterface database = DB.getInstance();
    private SystemManager sysManager;
    private Guest guest;
    Fan fan1;
    TeamMember fan2;
    private TeamInfo team;
    private Court court;
    private TeamMember owner;
    TeamMemberInformationPage tmPage;
    TeamInformationPage tPage;


    @Before
    public void init(){
        database.resetDB();


        sysManager= new SystemManager("Rotem", "Mir", "rot123", "student","2/10/1996","rotm@gmail.com", "ya1234");
        guest=new Guest();
        guest.SignUp("Ran", "Cohen", "ran123", "student","2/1/1996","ran@gmail.com", "ran1234","","");
        fan1= database.getUser("ran123");
        guest.SignUp("Yotam", "Levi", "lev123", "student","2/10/1994","lev@gmail.com", "lev1234","Player","CF");
        fan2=(TeamMember)database.getUser("lev123");
    }

    @Test
    public void logoutTest() {
        fan1.setStatus(Fan.Status.online);
        fan1.Logout(fan1);
        Assert.assertEquals(fan1.getStatus(), Fan.Status.offline);
    }

    @Test
    public void followTest(){
        InformationPage info=fan2.getInfoPage();
        fan1.follow(info);
        Assert.assertTrue(fan1.getFollowPages().contains(info));
        Assert.assertTrue(fan1.getFollowPages().size()==1);


    }

    @Test
    public void reportIncorrectInfoTest(){
        DB.getInstance().resetDB();
        guest.SignUp("Ran", "Cohen", "ran123", "student","2/1/1996","ran@gmail.com", "ran1234","","");
        fan1= database.getUser("ran123");
        fan1.reportIncorrectInfo("complain");
        Assert.assertEquals(DB.getInstance().getComplaintSys().getComplain(0).getComplain(),"complain");
    }
}

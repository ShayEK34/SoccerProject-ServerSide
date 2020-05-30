package Domain.User.unit;
import Data.SystemDB.DataBaseInterface;
import Domain.ClubManagement.TeamInfo;
import Data.SystemDB.DB;
import Domain.User.Fan;
import Domain.User.Guest;
import Domain.User.SystemManager;
import Domain.User.TeamMember;
import org.junit.Before;
import org.junit.Test;

public class SystemManagerTest {
    private DataBaseInterface database = DB.getInstance();
    SystemManager systemManager;
    TeamInfo manUtd;
    Guest g;


    @Before
    public void init() {
        DB.getInstance().resetDB();
        systemManager=new SystemManager("David","Levi","dav123","dav1234","No Occupation","21/1/1997","david@gmail.com");
        g = new Guest();
        g.SignUp("gdfg","zbvcxbidan","glazer","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","glazer");
        Fan f =database.getUser("glazer");
        TeamMember ownerGlazer = (TeamMember) f;
        manUtd = new TeamInfo("ManUtd",1906,true,"Manchester",ownerGlazer);
        database.addTeam("ManUtd",1906,true,"Manchester",ownerGlazer.getUserName(),"");
    }

    @Test(expected = IllegalArgumentException.class)
    public void addExistUserTest() {
        g.SignUp("Michel", "Levi", "glazer", "student","2/1/1994","mic@gmail.com", "mic1234","","");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullCheckUserTest(){
        SystemManager.checkUser(null,"mic1234");
    }
    //
    @Test(expected = IllegalArgumentException.class)
    public void noUserCheckUserTest(){
        SystemManager.checkUser("1234","mich123");
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongPasswordCheckUserTest(){
        SystemManager.checkUser("125634","glazer");
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyUsernameaddUserTest(){
        SystemManager.addNewUser("gdfg","zbvcxbidan","","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyPasswordaddUserTest(){
        SystemManager.addNewUser("gdfg","zbvcxbidan","5gfj","","4/6/1954","fgfdssg","1234","Owner","");
    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void wrongPasscheckUserTest(){
//        checkUser("1234","mic123");
//    }
}


package Domain.User.it;

import Data.SystemDB.DB;
import Data.SystemDB.DataBaseInterface;
import Domain.User.Fan;
import Domain.User.Guest;
import Domain.User.SystemManager;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
//import sun.dc.pr.PRError;

import java.util.ArrayList;

public class GuestIT {

    private DataBaseInterface database = DB.getInstance();
    private SystemManager sysManager;
    private Guest guest1;
    private Guest guest2;


    @Before
    public void init() {
        database.resetDB();
        guest1=new Guest();
        guest2=new Guest();
        sysManager= new SystemManager("Rotem", "Mir", "rot123", "student","2/10/1996","rotm@gmail.com", "ya1234");
    }

    @Test
    public void signUpTest() {
        guest1.SignUp("Ran", "Cohen", "ran123", "student","2/1/1996","ran@gmail.com", "ran1234","","");

        Assert.assertTrue(DB.getInstance().getUsers().containsKey("ran123"));
        Assert.assertTrue(DB.getInstance().getUsers().get("ran123").getStatus().equals(Fan.Status.offline));
    }

    @Test
    public void LoginTest(){
        guest2.SignUp("Michel", "Levi", "mic123", "student","2/1/1994","mic@gmail.com", "mic1234","","");
        Fan fan=database.getUser("mic123");
        fan.Login("mic1234","mic123");
        Assert.assertTrue(fan.getStatus().equals(Fan.Status.online));
    }

    @Test
    public void searchTest(){
        guest1.SignUp("David", "Shalom", "dvi123", "student","12/3/1991","david@gmail.com", "div1234","","");
        guest1.SignUp("Yotam", "Levi", "lev123", "student","17/3/1994","lev@gmail.com", "lev1234","","");
        Fan fan1= database.getUser("lev123");
        Fan fan2= database.getUser("dvi123");
        ArrayList<Object> ans= guest1.SearchByName("lev123");
        Assert.assertFalse(ans.isEmpty());
    }


}

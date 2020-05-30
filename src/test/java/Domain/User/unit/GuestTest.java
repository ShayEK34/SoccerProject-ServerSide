package Domain.User.unit;
import Domain.User.Guest;
import Domain.User.SystemManager;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class GuestTest {

    private SystemManager sysManager;
    private Guest guest1;
    private Guest guest2;


    @Before
    public void init() {
        guest1=new Guest();
        guest2=new Guest();
        sysManager= new SystemManager("Rotem", "Mir", "rot123", "student","2/10/1996","rotm@gmail.com", "ya1234");
    }


    @Test
    public void checkReturnValueLoginTest(){
        guest2.SignUp("Michel", "Levi", "mic123", "student","2/1/1994","mic@gmail.com", "mic1234","","");
        Assert.assertNotNull(guest2.Login("mic1234","mic123"));
    }

    @Test(expected =IllegalArgumentException.class )
    public void nullLoginTest(){
        guest1.SignUp("David", "Shalom", "dvi123", "student","12/3/1991","david@gmail.com", "div1234","","");
        guest1.Login(null,"dvi123");
    }




//    @Test(expected = IllegalArgumentException.class  )
//    public void nullSearch(){
//        guest1.Search(null);
//    }

}


package Domain.User.unit;

import Data.SystemDB.DB;
import Domain.User.AssociationUser;
import org.junit.Before;
import org.junit.Test;
import junit.framework.Assert;

public class AssociationUserTest {


    private AssociationUser assUser;

    @Before
    public void init() {
        DB.getInstance().resetDB();
        assUser=new AssociationUser("meital", "raz", "mr123", "mr1234","student","1/2/1996","yasminav@gmail.com");
        assUser.setSalary(900);
    }

    @Test
    public void getSalaryTest(){
        Assert.assertEquals(assUser.getSalary(),900);
    }

    @Test
    public void setSalaryTest(){
        assUser.setSalary(10000);
        Assert.assertEquals(assUser.getSalary(),10000);
    }

}
package Domain.AssociationManagement.unit;

import Data.SystemDB.DataBaseInterface;
import Domain.AssociationManagement.League;
import Domain.AssociationManagement.Season;
import Data.SystemDB.DB;
import Domain.User.AssociationUser;
import Domain.User.Fan;
import Domain.User.Guest;
import Domain.User.Referee;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

public class SeasonTest {
    private DataBaseInterface database = DB.getInstance();

    @Before
    public void init() {
        DB.getInstance().resetDB();

        //association
        Guest g0 = new Guest();
        g0.SignUp("gdfg", "zbvcxbidan", "fa", "fdgsfd", "4/6/1954", "fgfdssg", "1234", "Association","");
        g0.Login("1234", "fa");
        Fan f0 = database.getUser("fa");
        AssociationUser associationUser = (AssociationUser) f0;
        database.addSeason(2021,1000000);
        database.addLeagueAndPolicyToSeason(2021,"PremierLeague",new Date().toString(),3,0,
                1,true,false,12,12);
        associationUser.createSeasonAndBudget(2021,1000000);

        //Refs
        Guest r1 = new Guest();
        r1.SignUp("gdfg", "zbvcxbidan", "ref1", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Referee","Main Referee");
        Fan re1 = database.getUser("ref1");
        Referee ref1 = (Referee) re1;

        Guest r2 = new Guest();
        r2.SignUp("gdfg", "zbvcxbidan", "ref2", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Referee","Side Referee");
        Fan re2 = database.getUser("ref2");
        Referee ref2 = (Referee) re2;

        Guest r3 = new Guest();
        r3.SignUp("gdfg", "zbvcxbidan", "ref3", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Referee","Side Referee");
        Fan re3 = database.getUser("ref3");
        Referee ref3 = (Referee) re3;

        Guest r4 = new Guest();
        r4.SignUp("gdfg", "zbvcxbidan", "ref4", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Referee","Side Referee");
        Fan re4 = database.getUser("ref4");
        Referee ref4 = (Referee) re3;
    }

    @Test
    public void testGettersSetters(){
        League pl = database.getLeagueByName("PremierLeague");
        Season season = database.getSeasonByYear(2021);

        Referee referee1 = (Referee)database.getUser("ref1");
        Referee referee2 = (Referee)database.getUser("ref2");
        Referee referee3 = (Referee)database.getUser("ref3");
        Referee referee4 = (Referee)database.getUser("ref4");

        ArrayList<League> leagues = new ArrayList<>();
        season.setAllLeagues(leagues);

        season.setYear(2021);

        season.getMainActiveReferees().add(referee1);

        Vector<Referee> main = season.getMainActiveReferees();
        Assert.assertEquals(main, season.getMainActiveReferees());
        season.setMainActiveReferees(new Vector<>());
        Assert.assertNotSame(main, season.getMainActiveReferees());

        season.getSideActiveReferees().add(referee2);

        Vector<Referee> side = season.getSideActiveReferees();
        Assert.assertEquals(side, season.getSideActiveReferees());
        season.setSideActiveReferees(new Vector<>());
        Assert.assertNotSame(side, season.getSideActiveReferees());




    }

    @After
    public void reset(){
        DB.getInstance().resetDB();
    }

}


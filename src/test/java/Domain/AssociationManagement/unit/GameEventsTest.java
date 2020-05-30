package Domain.AssociationManagement.unit;

import Data.SystemDB.DataBaseInterface;
import Domain.AssociationManagement.League;
import Domain.AssociationManagement.Match;
//import Domain.ClubManagement.AssociationManagement.*;
import Domain.ClubManagement.Court;
import Domain.ClubManagement.TeamInfo;
import Data.SystemDB.DB;
import Domain.User.AssociationUser;
import Domain.User.Fan;
import Domain.User.Guest;
import Domain.User.TeamMember;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GameEventsTest {
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

        //------------ManUtd----------------------

        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "fgfdssg", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember ownerGlazer = (TeamMember) f;
        TeamInfo manUtd = new TeamInfo("ManUtd", 1906, true, "Manchester", ownerGlazer);
        Court oldTraf = new Court("Old Trafford", "Manchester", 80000);
        ownerGlazer.setTeam(manUtd);
        ownerGlazer.AddCourt(oldTraf);
        try {
            ownerGlazer.AddIncoming(9000, "tickets");
        } catch (Exception e) {
            e.printStackTrace();
        }


        //coach
        Guest g1 = new Guest();
        g1.SignUp("gdfg", "zbvcxbidan", "oleGunar", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Coach","Head Coach");
        Fan f1 = database.getUser("oleGunar");
        TeamMember coach = (TeamMember) f1;
        ownerGlazer.AddCoach(coach, "", "head coach");

        //player
        Guest g_gk = new Guest();
        g_gk.SignUp("gdfg", "zbvcxbidan", "deGea", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_gk = database.getUser("deGea");
        TeamMember gk = (TeamMember) f_gk;
        ownerGlazer.AddPlayer(gk, "gk");

        //player
        Guest g_rb = new Guest();
        g_rb.SignUp("gdfg", "zbvcxbidan", "awb", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rb = database.getUser("awb");
        TeamMember rb = (TeamMember) f_rb;
        ownerGlazer.AddPlayer(rb, "rb");

        //player
        Guest g_rcb = new Guest();
        g_rcb.SignUp("gdfg", "zbvcxbidan", "meguire", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rcb = database.getUser("meguire");
        TeamMember rcb = (TeamMember) f_rcb;
        ownerGlazer.AddPlayer(rcb, "rcb");

        //player
        Guest g_lcb = new Guest();
        g_lcb.SignUp("gdfg", "zbvcxbidan", "baily", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lcb = database.getUser("baily");
        TeamMember lcb = (TeamMember) f_lcb;
        ownerGlazer.AddPlayer(lcb, "lcb");

        //player
        Guest g_lb = new Guest();
        g_lb.SignUp("gdfg", "zbvcxbidan", "shaw", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lb = database.getUser("shaw");
        TeamMember lb = (TeamMember) f_lb;
        ownerGlazer.AddPlayer(lb, "lb");

        //player
        Guest g_rm = new Guest();
        g_rm.SignUp("gdfg", "zbvcxbidan", "sancho", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rm = database.getUser("sancho");
        TeamMember rm = (TeamMember) f_rm;
        ownerGlazer.AddPlayer(rm, "rm");

        //player
        Guest g_rcm = new Guest();
        g_rcm.SignUp("gdfg", "zbvcxbidan", "pogba", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rcm = database.getUser("pogba");
        TeamMember rcm = (TeamMember) f_rcm;
        ownerGlazer.AddPlayer(rcm, "rcm");

        //player
        Guest g_lcm = new Guest();
        g_lcm.SignUp("gdfg", "zbvcxbidan", "bruno", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lcm = database.getUser("bruno");
        TeamMember lcm = (TeamMember) f_lcm;
        ownerGlazer.AddPlayer(lcm, "lcm");

        //player
        Guest g_lm = new Guest();
        g_lm.SignUp("gdfg", "zbvcxbidan", "martial", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lm = database.getUser("martial");
        TeamMember lm = (TeamMember) f_lm;
        ownerGlazer.AddPlayer(lm, "lm");

        //player
        Guest g_ls = new Guest();
        g_ls.SignUp("gdfg", "zbvcxbidan", "rashford", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_ls = database.getUser("rashford");
        TeamMember ls = (TeamMember) f_ls;
        ownerGlazer.AddPlayer(ls, "ls");

        //player
        Guest g_rs = new Guest();
        g_rs.SignUp("gdfg", "zbvcxbidan", "greenwood", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rs = database.getUser("greenwood");
        TeamMember rs = (TeamMember) f_rs;
        ownerGlazer.AddPlayer(rs, "rs");


        //------------Liverpool----------------------

        //owner
        Guest g111 = new Guest();
        g111.SignUp("gdfg", "zbvcxbidan", "fenway", "fdgsfd", "4/6/1954", "fgfdssg", "1234", "Owner","");
        g111.Login("1234", "fenway");
        Fan f111 = database.getUser("fenway");
        TeamMember fenway = (TeamMember) f111;
        TeamInfo liverpool = new TeamInfo("Liverpool", 1906, true, "Liverpool", fenway);
        Court anfield = new Court("Anfield", "Liverpool", 80000);
        fenway.setTeam(liverpool);
        fenway.AddCourt(anfield);

        //coach
        Guest g11 = new Guest();
        g11.SignUp("gdfg", "zbvcxbidan", "klop", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Coach","Head Coach");
        Fan f11 = database.getUser("klop");
        TeamMember coach1 = (TeamMember) f1;
        fenway.AddCoach(coach1, "", "head coach");

        //player
        Guest g_gk1 = new Guest();
        g_gk1.SignUp("gdfg", "zbvcxbidan", "allison", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_gk1 = database.getUser("allison");
        TeamMember gk1 = (TeamMember) f_gk1;
        fenway.AddPlayer(gk1, "gk");

        //player
        Guest g_rb1 = new Guest();
        g_rb1.SignUp("gdfg", "zbvcxbidan", "arnold", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rb1 = database.getUser("arnold");
        TeamMember rb1 = (TeamMember) f_rb1;
        fenway.AddPlayer(rb1, "rb");

        //player
        Guest g_rcb1 = new Guest();
        g_rcb1.SignUp("gdfg", "zbvcxbidan", "vandijk", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rcb1 = database.getUser("vandijk");
        TeamMember rcb1 = (TeamMember) f_rcb1;
        fenway.AddPlayer(rcb1, "rcb");

        //player
        Guest g_lcb1 = new Guest();
        g_lcb1.SignUp("gdfg", "zbvcxbidan", "gomez", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lcb1 = database.getUser("gomez");
        TeamMember lcb1 = (TeamMember) f_lcb1;
        fenway.AddPlayer(lcb1, "lcb");

        //player
        Guest g_lb1 = new Guest();
        g_lb1.SignUp("gdfg", "zbvcxbidan", "robertson", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lb1 = database.getUser("robertson");
        TeamMember lb1 = (TeamMember) f_lb1;
        fenway.AddPlayer(lb1, "lb");

        //player
        Guest g_rm1 = new Guest();
        g_rm1.SignUp("gdfg", "zbvcxbidan", "wijnaldum", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rm1 = database.getUser("wijnaldum");
        TeamMember rm1 = (TeamMember) f_rm1;
        fenway.AddPlayer(rm1, "rm");

        //player
        Guest g_rcm1 = new Guest();
        g_rcm1.SignUp("gdfg", "zbvcxbidan", "fabinho", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rcm1 = database.getUser("fabinho");
        TeamMember rcm1 = (TeamMember) f_rcm1;
        fenway.AddPlayer(rcm1, "rcm");

        //player
        Guest g_lcm1 = new Guest();
        g_lcm1.SignUp("gdfg", "zbvcxbidan", "henderson", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lcm1 = database.getUser("henderson");
        TeamMember lcm1 = (TeamMember) f_lcm1;
        fenway.AddPlayer(lcm1, "lcm");

        //player
        Guest g_lm1 = new Guest();
        g_lm1.SignUp("gdfg", "zbvcxbidan", "mane", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lm1 = database.getUser("mane");
        TeamMember lm1 = (TeamMember) f_lm1;
        fenway.AddPlayer(lm1, "lm");

        //player
        Guest g_ls1 = new Guest();
        g_ls1.SignUp("gdfg", "zbvcxbidan", "firmino", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_ls1 = database.getUser("firmino");
        TeamMember ls1 = (TeamMember) f_ls1;
        fenway.AddPlayer(ls1, "ls");

        //player
        Guest g_rs1 = new Guest();
        g_rs1.SignUp("gdfg", "zbvcxbidan", "salah", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rs1 = database.getUser("salah");
        TeamMember rs1 = (TeamMember) f_rs1;
        fenway.AddPlayer(rs1, "rs");

    }

    @Test
    public void testGetterSetters(){
        TeamMember glazerss = (TeamMember)database.getUser("glazer");
        TeamMember fenwayy = (TeamMember)database.getUser("fenway");
        League pl = database.getLeagueByName("PremierLeague");
        try{
            glazerss.AddIncoming(1000,"tickets");
            fenwayy.AddIncoming(10000,"tickets");
        }catch (Exception e){e.printStackTrace();}
        glazerss.registerTheTeamToLeague("PremierLeague");
        fenwayy.registerTheTeamToLeague("PremierLeague");

        Match game = new Match(glazerss.getTeam(),fenwayy.getTeam(),glazerss.getTeam().getTeamCourt(),new Date(),null);

        SimpleDateFormat sdf = game.getEvents().getFormat();
        Assert.assertEquals(sdf, game.getEvents().getFormat());
        SimpleDateFormat sdf1 = new SimpleDateFormat();
        game.getEvents().setFormat(sdf1);
        Assert.assertNotSame(sdf, game.getEvents().getFormat());

        game.getEvents().setHomeGoals(1);
        game.getEvents().setAwayGoals(1);

        ArrayList<String> e = game.getEvents().getAllGameEvents();
        Assert.assertEquals(e, game.getEvents().getAllGameEvents());
        ArrayList<String> yy = new ArrayList<>();
        game.getEvents().setAllGameEvents(yy);
        Assert.assertNotSame(e, game.getEvents().getAllGameEvents());

        Date t = game.getEvents().getBeginMatch();
        Assert.assertEquals(t, game.getEvents().getBeginMatch());
        Date tt = new Date();
        game.getEvents().setBeginMatch(tt);
        Assert.assertNotSame(t, game.getEvents().getBeginMatch());

    }

    @After
    public void reset(){
        DB.getInstance().resetDB();
    }



}


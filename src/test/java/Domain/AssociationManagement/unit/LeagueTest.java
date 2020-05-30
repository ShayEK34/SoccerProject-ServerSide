package Domain.AssociationManagement.unit;

import Data.SystemDB.DataBaseInterface;
import Domain.AssociationManagement.League;
import Domain.AssociationManagement.PointsPolicy;
import Domain.AssociationManagement.Season;
import Domain.ClubManagement.Court;
import Domain.ClubManagement.TeamInfo;
import Data.SystemDB.DB;
import Domain.User.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

public class LeagueTest {
    private DataBaseInterface database = DB.getInstance();

    @Before
    public void init(){
        database.resetDB();

        //association
        Guest g0 = new Guest();
        g0.SignUp("gdfg","zbvcxbidan","fa","fdgsfd","4/6/1954","fgfdssg","1234","Association","");
        g0.Login("1234","fa");
        Fan f0 = database.getUser("fa");
        AssociationUser associationUser = (AssociationUser) f0;
        database.addSeason(2021,1000000);
        database.addLeagueAndPolicyToSeason(2021,"PremierLeague",new Date().toString(),3,0,
                1,true,false,12,12);
        associationUser.createSeasonAndBudget(2021,1000000);
    }


    @Test
    public void testGettersSetters(){
        League pl = database.getLeagueByName("PremierLeague");

        pl.setLeagueName("Premier League");
        pl.setTeams(new Vector<>());
        pl.setNumOfTeams(1);

        Vector<Referee> main = pl.getMainReferees();
        Assert.assertEquals(main, pl.getMainReferees());
        pl.setMainReferees(new Vector<>());
        Assert.assertNotSame(main, pl.getMainReferees());

        Vector<Referee> side = pl.getSideReferees();
        Assert.assertEquals(side, pl.getSideReferees());
        pl.setSideReferees(new Vector<>());
        Assert.assertNotSame(side, pl.getSideReferees());

        PointsPolicy pp = pl.getPointsPolicy();
        Assert.assertEquals(pp, pl.getPointsPolicy());
        pl.setPointsPolicy(new PointsPolicy(3,2,0,true,false));
        Assert.assertNotSame(pp, pl.getPointsPolicy());

        int rounds = pl.getNumOfRounds();
        Assert.assertEquals(rounds, pl.getNumOfRounds());
        pl.setNumOfRounds(1);
        Assert.assertNotSame(rounds, pl.getNumOfRounds());

        //------------ManUtd---------------------------------------------------------------------------
        //==========================================================================================

        //owner
        Guest g = new Guest();
        g.SignUp("gdfg","zbvcxbidan","glazer","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","glazer");
        Fan f = database.getUser("glazer");
        TeamMember ownerGlazer = (TeamMember) f;
        TeamInfo manUtd = new TeamInfo("ManUtd",1906,true,"Manchester",ownerGlazer);
        Court oldTraf = new Court("Old Trafford","Manchester",80000);

        Assert.assertFalse(pl.addTeam(null));
        Assert.assertFalse(pl.addTeam(manUtd));

        ownerGlazer.setTeam(manUtd);
        ownerGlazer.AddCourt(oldTraf);
        try{
            ownerGlazer.AddIncoming(19000,"tickets");
        }catch (Exception e){e.printStackTrace();}

        Assert.assertFalse(pl.addTeam(manUtd));

        //coach
        Guest g1 = new Guest();
        g1.SignUp("gdfg","zbvcxbidan","oleGunar","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Coach","Head Coach");
        Fan f1 = database.getUser("oleGunar");
        TeamMember coach = (TeamMember) f1;
        ownerGlazer.AddCoach(coach,"","head coach");

        Assert.assertFalse(pl.addTeam(manUtd));

        //player
        Guest g_gk = new Guest();
        g_gk.SignUp("gdfg","zbvcxbidan","deGea","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_gk = database.getUser("deGea");
        TeamMember gk = (TeamMember) f_gk;
        ownerGlazer.AddPlayer(gk,"gk");

        Assert.assertFalse(pl.addTeam(manUtd));

        //player
        Guest g_rb = new Guest();
        g_rb.SignUp("gdfg","zbvcxbidan","awb","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_rb = database.getUser("awb");
        TeamMember rb = (TeamMember) f_rb;
        ownerGlazer.AddPlayer(rb,"rb");

        //player
        Guest g_rcb = new Guest();
        g_rcb.SignUp("gdfg","zbvcxbidan","meguire","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_rcb = database.getUser("meguire");
        TeamMember rcb = (TeamMember) f_rcb;
        ownerGlazer.AddPlayer(rcb,"rcb");

        //player
        Guest g_lcb = new Guest();
        g_lcb.SignUp("gdfg","zbvcxbidan","baily","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_lcb = database.getUser("baily");
        TeamMember lcb = (TeamMember) f_lcb;
        ownerGlazer.AddPlayer(lcb,"lcb");

        //player
        Guest g_lb = new Guest();
        g_lb.SignUp("gdfg","zbvcxbidan","shaw","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_lb = database.getUser("shaw");
        TeamMember lb = (TeamMember) f_lb;
        ownerGlazer.AddPlayer(lb,"lb");

        //player
        Guest g_rm = new Guest();
        g_rm.SignUp("gdfg","zbvcxbidan","sancho","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_rm = database.getUser("sancho");
        TeamMember rm = (TeamMember) f_rm;
        ownerGlazer.AddPlayer(rm,"rm");

        //player
        Guest g_rcm = new Guest();
        g_rcm.SignUp("gdfg","zbvcxbidan","pogba","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_rcm = database.getUser("pogba");
        TeamMember rcm = (TeamMember) f_rcm;
        ownerGlazer.AddPlayer(rcm,"rcm");

        //player
        Guest g_lcm = new Guest();
        g_lcm.SignUp("gdfg","zbvcxbidan","bruno","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_lcm = database.getUser("bruno");
        TeamMember lcm = (TeamMember) f_lcm;
        ownerGlazer.AddPlayer(lcm,"lcm");

        //player
        Guest g_lm = new Guest();
        g_lm.SignUp("gdfg","zbvcxbidan","martial","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_lm = database.getUser("martial");
        TeamMember lm = (TeamMember) f_lm;
        ownerGlazer.AddPlayer(lm,"lm");

        //player
        Guest g_ls = new Guest();
        g_ls.SignUp("gdfg","zbvcxbidan","rashford","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_ls = database.getUser("rashford");
        TeamMember ls = (TeamMember) f_ls;
        ownerGlazer.AddPlayer(ls,"ls");

        //player
        Guest g_rs = new Guest();
        g_rs.SignUp("gdfg","zbvcxbidan","greenwood","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_rs = database.getUser("greenwood");
        TeamMember rs = (TeamMember) f_rs;
        ownerGlazer.AddPlayer(rs,"rs");

        pl.addTeam(manUtd);

        TeamInfo t = pl.getWinner();
        Assert.assertEquals(t, pl.getWinner());
        pl.setWinner(pl.getTeams().get(0));
        Assert.assertNotSame(t, pl.getWinner());

        Date k = pl.getTournamentBeginDate();
        Assert.assertEquals(k, pl.getTournamentBeginDate());
        Date o = new Date();
        pl.setTournamentBeginDate(o);
        Assert.assertNotSame(k, pl.getTournamentBeginDate());

        Vector<Court> ccc = pl.getCourts();
        Assert.assertEquals(ccc, pl.getCourts());
        pl.setCourts(new Vector<>());
        Assert.assertNotSame(ccc, pl.getCourts());

        ArrayList<Season> ssss = pl.getSeasons();
        Assert.assertEquals(ssss, pl.getSeasons());
        pl.setSeasons(new ArrayList<>());
        Assert.assertNotSame(ssss, pl.getSeasons());





    }



    @After
    public void reset(){
        DB.getInstance().resetDB();
    }


}


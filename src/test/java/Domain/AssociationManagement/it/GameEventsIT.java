package Domain.AssociationManagement.it;

import Data.SystemDB.DataBaseInterface;
import Domain.AssociationManagement.League;
import Domain.AssociationManagement.Match;
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

import java.util.Date;

public class GameEventsIT {
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

        //------------ManUtd----------------------

        //owner
        Guest g = new Guest();
        g.SignUp("gdfg","zbvcxbidan","glazer","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","glazer");
        Fan f = database.getUser("glazer");
        TeamMember ownerGlazer = (TeamMember) f;
        TeamInfo manUtd = new TeamInfo("ManUtd",1906,true,"Manchester",ownerGlazer);
        Court oldTraf = new Court("Old Trafford","Manchester",80000);
        ownerGlazer.setTeam(manUtd);
        ownerGlazer.AddCourt(oldTraf);
        try{
            ownerGlazer.AddIncoming(9000,"tickets");
        }catch (Exception e){e.printStackTrace();}





        //coach
        Guest g1 = new Guest();
        g1.SignUp("gdfg","zbvcxbidan","oleGunar","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Coach","Head Coach");
        Fan f1 = database.getUser("oleGunar");
        TeamMember coach = (TeamMember) f1;
        ownerGlazer.AddCoach(coach,"","head coach");

        //player
        Guest g_gk = new Guest();
        g_gk.SignUp("gdfg","zbvcxbidan","deGea","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_gk = database.getUser("deGea");
        TeamMember gk = (TeamMember) f_gk;
        ownerGlazer.AddPlayer(gk,"gk");

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


        //------------Liverpool----------------------

        //owner
        Guest g111 = new Guest();
        g111.SignUp("gdfg","zbvcxbidan","fenway","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g111.Login("1234","fenway");
        Fan f111 = database.getUser("fenway");
        TeamMember fenway = (TeamMember) f111;
        TeamInfo liverpool = new TeamInfo("Liverpool",1906,true,"Liverpool",fenway);
        Court anfield = new Court("Anfield","Liverpool",80000);
        fenway.setTeam(liverpool);
        fenway.AddCourt(anfield);

        //coach
        Guest g11 = new Guest();
        g11.SignUp("gdfg","zbvcxbidan","klop","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Coach","Head Coach");
        Fan f11 = database.getUser("klop");
        TeamMember coach1 = (TeamMember) f1;
        fenway.AddCoach(coach1,"","head coach");

        //player
        Guest g_gk1 = new Guest();
        g_gk1.SignUp("gdfg","zbvcxbidan","allison","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_gk1 = database.getUser("allison");
        TeamMember gk1 = (TeamMember) f_gk1;
        fenway.AddPlayer(gk1,"gk");

        //player
        Guest g_rb1 = new Guest();
        g_rb1.SignUp("gdfg","zbvcxbidan","arnold","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_rb1 = database.getUser("arnold");
        TeamMember rb1 = (TeamMember) f_rb1;
        fenway.AddPlayer(rb1,"rb");

        //player
        Guest g_rcb1 = new Guest();
        g_rcb1.SignUp("gdfg","zbvcxbidan","vandijk","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_rcb1 = database.getUser("vandijk");
        TeamMember rcb1 = (TeamMember) f_rcb1;
        fenway.AddPlayer(rcb1,"rcb");

        //player
        Guest g_lcb1 = new Guest();
        g_lcb1.SignUp("gdfg","zbvcxbidan","gomez","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_lcb1 = database.getUser("gomez");
        TeamMember lcb1 = (TeamMember) f_lcb1;
        fenway.AddPlayer(lcb1,"lcb");

        //player
        Guest g_lb1 = new Guest();
        g_lb1.SignUp("gdfg","zbvcxbidan","robertson","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_lb1 = database.getUser("robertson");
        TeamMember lb1 = (TeamMember) f_lb1;
        fenway.AddPlayer(lb1,"lb");

        //player
        Guest g_rm1 = new Guest();
        g_rm1.SignUp("gdfg","zbvcxbidan","wijnaldum","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_rm1 = database.getUser("wijnaldum");
        TeamMember rm1 = (TeamMember) f_rm1;
        fenway.AddPlayer(rm1,"rm");

        //player
        Guest g_rcm1 = new Guest();
        g_rcm1.SignUp("gdfg","zbvcxbidan","fabinho","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_rcm1 = database.getUser("fabinho");
        TeamMember rcm1 = (TeamMember) f_rcm1;
        fenway.AddPlayer(rcm1,"rcm");

        //player
        Guest g_lcm1 = new Guest();
        g_lcm1.SignUp("gdfg","zbvcxbidan","henderson","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_lcm1 = database.getUser("henderson");
        TeamMember lcm1 = (TeamMember) f_lcm1;
        fenway.AddPlayer(lcm1,"lcm");

        //player
        Guest g_lm1 = new Guest();
        g_lm1.SignUp("gdfg","zbvcxbidan","mane","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_lm1 = database.getUser("mane");
        TeamMember lm1 = (TeamMember) f_lm1;
        fenway.AddPlayer(lm1,"lm");

        //player
        Guest g_ls1 = new Guest();
        g_ls1.SignUp("gdfg","zbvcxbidan","firmino","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_ls1 = database.getUser("firmino");
        TeamMember ls1 = (TeamMember) f_ls1;
        fenway.AddPlayer(ls1,"ls");

        //player
        Guest g_rs1 = new Guest();
        g_rs1.SignUp("gdfg","zbvcxbidan","salah","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_rs1 = database.getUser("salah");
        TeamMember rs1 = (TeamMember) f_rs1;
        fenway.AddPlayer(rs1,"rs");

        //------------ManCity----------------------

        //owner
        Guest g22 = new Guest();
        g22.SignUp("gdfg","zbvcxbidan","mansour","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g22.Login("1234","mansour");
        Fan f22 = database.getUser("mansour");
        TeamMember mansour = (TeamMember) f22;
        TeamInfo city = new TeamInfo("ManCity",1906,true,"Liverpool",mansour);
        Court etihad = new Court("Etihad","Manchester",80000);
        mansour.setTeam(city);
        mansour.AddCourt(etihad);
        try{
            mansour.AddIncoming(9000,"tickets");
        }catch (Exception e){e.printStackTrace();}

        //coach
        Guest g2 = new Guest();
        g2.SignUp("gdfg","zbvcxbidan","pep","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Coach","Head Coach");
        Fan f2 = database.getUser("pep");
        TeamMember coach2 = (TeamMember) f2;
        mansour.AddCoach(coach2,"","head coach");

        //player
        Guest g_gk2 = new Guest();
        g_gk2.SignUp("gdfg","zbvcxbidan","ederson","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_gk2 = database.getUser("ederson");
        TeamMember gk2 = (TeamMember) f_gk2;
        mansour.AddPlayer(gk2,"gk");

        //player
        Guest g_rb2 = new Guest();
        g_rb2.SignUp("gdfg","zbvcxbidan","walker","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_rb2 = database.getUser("walker");
        TeamMember rb2 = (TeamMember) f_rb2;
        mansour.AddPlayer(rb2,"rb");

        //player
        Guest g_rcb2 = new Guest();
        g_rcb2.SignUp("gdfg","zbvcxbidan","laporte","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_rcb2 = database.getUser("laporte");
        TeamMember rcb2 = (TeamMember) f_rcb2;
        mansour.AddPlayer(rcb2,"rcb");

        //player
        Guest g_lcb2 = new Guest();
        g_lcb2.SignUp("gdfg","zbvcxbidan","otamendi","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_lcb2 = database.getUser("otamendi");
        TeamMember lcb2 = (TeamMember) f_lcb2;
        mansour.AddPlayer(lcb2,"lcb");

        //player
        Guest g_lb2 = new Guest();
        g_lb1.SignUp("gdfg","zbvcxbidan","cancelo","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_lb2 = database.getUser("cancelo");
        TeamMember lb2 = (TeamMember) f_lb2;
        mansour.AddPlayer(lb2,"lb");

        //player
        Guest g_rm2 = new Guest();
        g_rm2.SignUp("gdfg","zbvcxbidan","sane","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_rm2 = database.getUser("sane");
        TeamMember rm2 = (TeamMember) f_rm2;
        mansour.AddPlayer(rm2,"rm");

        //player
        Guest g_rcm2 = new Guest();
        g_rcm2.SignUp("gdfg","zbvcxbidan","debruine","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_rcm2 = database.getUser("debruine");
        TeamMember rcm2 = (TeamMember) f_rcm2;
        mansour.AddPlayer(rcm2,"rcm");

        //player
        Guest g_lcm2 = new Guest();
        g_lcm2.SignUp("gdfg","zbvcxbidan","fernandinho","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_lcm2 = database.getUser("fernandinho");
        TeamMember lcm2 = (TeamMember) f_lcm2;
        mansour.AddPlayer(lcm2,"lcm");

        //player
        Guest g_lm2 = new Guest();
        g_lm2.SignUp("gdfg","zbvcxbidan","bernardo","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_lm2 = database.getUser("bernardo");
        TeamMember lm2 = (TeamMember) f_lm2;
        mansour.AddPlayer(lm2,"lm");

        //player
        Guest g_ls2 = new Guest();
        g_ls2.SignUp("gdfg","zbvcxbidan","aguero","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_ls2 = database.getUser("aguero");
        TeamMember ls2 = (TeamMember) f_ls2;
        mansour.AddPlayer(ls2,"ls");

        //player
        Guest g_rs2 = new Guest();
        g_rs2.SignUp("gdfg","zbvcxbidan","sterling","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_rs2 = database.getUser("sterling");
        TeamMember rs2 = (TeamMember) f_rs2;
        mansour.AddPlayer(rs2,"rs");

    }

    /**
     * test nulls
     * test if team can register to league with not enough money
     * test if team with enough money can register the league
     * test if team with enough money can register to full league
     * test AssoBudget after registration
     */
    @Test
    public void testEvents(){
        TeamMember glazerss = (TeamMember)database.getUser("glazer");
        TeamMember fenwayy = (TeamMember)database.getUser("fenway");
        TeamMember mansourrr = (TeamMember)database.getUser("mansour");

        League pl = database.getLeagueByName("PremierLeague");

        try{
            glazerss.AddIncoming(1000,"tickets");
            fenwayy.AddIncoming(10000,"tickets");
            mansourrr.AddIncoming(10000,"tickets");
        }catch (Exception e){e.printStackTrace();}
        glazerss.registerTheTeamToLeague("PremierLeague");
        fenwayy.registerTheTeamToLeague("PremierLeague");


        Match game = new Match(glazerss.getTeam(),fenwayy.getTeam(),glazerss.getTeam().getTeamCourt(),new Date(),null);

        //testAddGoal
        Assert.assertFalse(game.getEvents().addGoalEvent(null,"home"));
        Assert.assertFalse(game.getEvents().addGoalEvent("rashford",null));
        Assert.assertFalse(game.getEvents().addGoalEvent(null,null));
        Assert.assertFalse(game.getEvents().addGoalEvent("rashford","not suppose to"));
        Assert.assertTrue(game.getEvents().addGoalEvent("rashford","home"));
        Assert.assertTrue(game.getEvents().addGoalEvent("salah","away"));
        Assert.assertTrue(game.getEvents().getHomeGoals()==1);
        Assert.assertTrue(game.getEvents().getAwayGoals()==1);

        //testAddOffside
        Assert.assertFalse(game.getEvents().addOffsideEvent(null));
        Assert.assertTrue(game.getEvents().addOffsideEvent("salah"));

        //testAddFoul
        Assert.assertFalse(game.getEvents().addFoulEvent(null,"red"));
        Assert.assertFalse(game.getEvents().addFoulEvent("rashford",null));
        Assert.assertFalse(game.getEvents().addFoulEvent(null,null));
        Assert.assertFalse(game.getEvents().addFoulEvent("rashford","gdfg"));
        Assert.assertTrue(game.getEvents().addFoulEvent("rashford",""));
        Assert.assertTrue(game.getEvents().addFoulEvent("rashford","yellow"));
        Assert.assertTrue(game.getEvents().addFoulEvent("salah","red"));

        //testAddInjury
        Assert.assertFalse(game.getEvents().addInjuryEvent(null));
        Assert.assertTrue(game.getEvents().addInjuryEvent("mane"));

        //testAddSubstitute
        Assert.assertFalse(game.getEvents().addSubstituteEvent(null,"rashford"));
        Assert.assertFalse(game.getEvents().addSubstituteEvent("rashford",null));
        Assert.assertFalse(game.getEvents().addSubstituteEvent(null,null));
        Assert.assertTrue(game.getEvents().addSubstituteEvent("rashford","chong"));

    }

    @After
    public void reset(){
        DB.getInstance().resetDB();
    }


}


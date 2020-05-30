package Domain.User.it;

import Data.SystemDB.DB;
import Data.SystemDB.DataBaseInterface;
import Domain.AssociationManagement.BudgetManagement;
import Domain.AssociationManagement.League;
import Domain.AssociationManagement.PointsPolicy;
import Domain.AssociationManagement.Season;
import Domain.ClubManagement.TeamInfo;
import Domain.User.*;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class AssociationUserIT {

    private DataBaseInterface database = DB.getInstance();
    private Guest guest;
    private Referee referee1;
    private Referee referee2;
    private AssociationUser assUser;
    private Season season;
    private BudgetManagement bm;
    private TeamInfo team;
    private League league;
    private League league1;


    @Before
    public void init() {
        database.resetDB();

        referee2=new Referee("raanan", "mesh", "raananm", "123","student","19/9/1965","raanan@gmail.com", "Main Referee");

        guest=new Guest();
        Guest g1 = new Guest();
        g1.SignUp("Yasmin","Avram","yas123","fdgsfd","4/6/1954","fgfdssg","1234","Referee","Main Referee");
        g1.Login("1234","yas123");
        Fan f1 = database.getUser("yas123");
        referee1 = (Referee) f1;


        guest=new Guest();
        Guest g2 = new Guest();
        g1.SignUp("rotem","miara","rot","fdgsfd","4/6/1954","fgfdssg","1234","Referee","Side Referee");
        g1.Login("1234","rot");
        Fan f2 = database.getUser("rot");
        referee2 = (Referee) f2;



        assUser=new AssociationUser("meital", "raz", "mr123", "mr1234","student","1/2/1996","yasminav@gmail.com");
        assUser.setSalary(900);

        league1=new League("a");

        league=new League("ligaElit");
        season=new Season(2020);
        database.addSeason(2021,1000000);
        //database.addLeagueAndPolicyToSeason()

        guest=new Guest();
        Guest g = new Guest();
        g.SignUp("gdfg","zbvcxbidan","glazer","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","glazer");
        Fan f = database.getUser("glazer");
        TeamMember ownerGlazer = (TeamMember) f;
        team = new TeamInfo("ManUtd",1906,true,"Manchester",ownerGlazer);
        database.addTeam("ManUtd",1906,true,"Manchester",ownerGlazer.getUserName(),"");

        Date today= Calendar.getInstance().getTime();

        Guest g0 = new Guest();
        g0.SignUp("gdfg","zbvcxbidan","fa","fdgsfd","4/6/1954","fgfdssg","1234","Association","");
        g0.Login("1234","fa");
        Fan f0 = database.getUser("fa");
        AssociationUser associationUser = (AssociationUser) f0;
        database.addSeason(2021,1000000);
        database.addLeagueAndPolicyToSeason(2021,"ligaElit",new Date().toString(),3,0,
                1,true,false,12,12);
        associationUser.createSeasonAndBudget(2021,1000000);
        associationUser.addNewLeagueToSeason(2021,"a",new Date().toString(),3,0,
                1,true,false,12,12);
    }


    @Test
    public void addTeamToLeagueTest() {
        Assert.assertFalse(AssociationUser.addTeamToLeague(null,"ligaElit"));
    }

    @Test
    public void NominateRefereeTest() {

        guest.SignUp("Ran", "Cohen", "ran123", "student","2/1/1996","ran@gmail.com", "ran1234","Referee","Main Referee");
        Referee referee1 = (Referee)database.getUser("ran123");
        Assert.assertTrue(assUser.NominateReferee(referee1));

        guest.SignUp("Ran", "Cohen", "abc", "student","2/1/1996","ran@gmail.com", "ran1234","Referee","Side Referee");
        Referee referee2 = (Referee)database.getUser("abc");
        Assert.assertTrue(assUser.NominateReferee(referee2));

//        guest.SignUp("Ran", "Cohen", "meytal", "student","2/1/1996","ran@gmail.com", "ran1234","","");
//        Referee referee3 = (Referee)database.getUser("meytal");
//        assUser.NominateReferee(referee3);
//        Assert.assertFalse(assUser.NominateReferee(referee3));

    }

    @Test
    public void addRefereeToLeagueTest() {
        guest.SignUp("Ran", "Cohen", "ran123", "student","2/1/1996","ran@gmail.com", "ran1234","Referee","Main Referee");
        Referee referee2 = (Referee)database.getUser("ran123");
        Assert.assertFalse(assUser.addRefereeToLeague(null,null));
        assUser.NominateReferee(referee2);
        season.addMainRefereeToActiveReferees(referee1);
        season.addLeagueToSeasonAndSeasonToLeague(league);
        assUser.addRefereeToLeague(this.league, referee2);
        Assert.assertTrue(assUser.addRefereeToLeague(this.league, referee2));

        assUser.NominateReferee(referee2);
        season.addSideRefereeToActiveReferees(referee2);
        season.addLeagueToSeasonAndSeasonToLeague(league);
        Assert.assertTrue(assUser.addRefereeToLeague(league, referee2));
        Assert.assertFalse(assUser.addRefereeToLeague(league, null));

    }

    @Test
    public void DefineLeagueTest() {
        database.addSeason(2021,1000000);
        database.addLeagueAndPolicyToSeason(2021,"PremierLeague",new Date().toString(),3,0,
                1,true,false,12,12);

        Assert.assertTrue(assUser.addNewLeagueToSeason(2021,"PremierLeague",new Date().toString(),3,0,
                1,true,false,12,12));

        Assert.assertFalse(assUser.addNewLeagueToSeason(2021,"PremierLeague",new Date().toString(),3,0,
                1,true,false,12,12));
        database.resetDB();
        Assert.assertFalse(assUser.addNewLeagueToSeason(2021,"PremierLeague",new Date().toString(),3,0,
                1,true,false,12,12));

    }


    @Test
    public void createRankAndPointsPolicyTest() {
        assUser.addNewLeagueToSeason(2021,"ligaElit",new Date().toString(),3,0,
                1,true,false,12,12);
        League leg = database.getLeagueByName("ligaElit");
        Assert.assertTrue(assUser.createRankAndPointsPolicy(leg,4,3,3,true,false));
        Assert.assertFalse(assUser.createRankAndPointsPolicy(null,-4,-3,-3,true,false));
        Assert.assertFalse(assUser.createRankAndPointsPolicy(null,0,0,0,true,false));

        assUser.createRankAndPointsPolicy(leg,3,1,0,true,false);
        Assert.assertEquals(leg.getPointsPolicy().getPointsForWin(),3);


    }

    @Test
    public void updateRankAndPointsPolicyTest() {
        League pl = database.getLeagueByName("ligaElit");
        pl.setPointsPolicy(new PointsPolicy(3,2,0,true,false));

        Assert.assertTrue(assUser.updateRankAndPointsPolicy(pl,9,5,6,false,true));
        Assert.assertFalse(assUser.updateRankAndPointsPolicy(null,4,5,6,false,true));
        Assert.assertFalse(assUser.updateRankAndPointsPolicy(pl,-7,-5,-6,false,true));
        Assert.assertTrue(assUser.updateRankAndPointsPolicy(pl,0,0,0,false,true));

        assUser.updateRankAndPointsPolicy(pl,4,3,1,true,false);
        Assert.assertEquals(pl.getPointsPolicy().getPointsForWin(),4);
        Assert.assertEquals(pl.getPointsPolicy().getPointsForDraw(),3);
        Assert.assertEquals(pl.getPointsPolicy().getPointsForLoss(),1);
        Assert.assertEquals(pl.getPointsPolicy().isGoalDiffTieBreaker(),true);
        Assert.assertEquals(pl.getPointsPolicy().isDirectResultsTieBreaker(),false);



    }

    @Test
    public void updateGameSchedulingTest() {
        League pl = database.getLeagueByName("ligaElit");
        assUser.updateGameScheduling(pl,1);
        Assert.assertEquals(pl.getNumOfRounds(),1);
        Assert.assertFalse(assUser.updateGameScheduling(pl,3));
        Assert.assertTrue(assUser.updateGameScheduling(pl,1));
//
    }
}





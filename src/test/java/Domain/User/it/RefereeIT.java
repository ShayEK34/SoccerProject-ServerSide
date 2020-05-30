package Domain.User.it;
//***

import Data.SystemDB.DataBaseInterface;
import Domain.AssociationManagement.Match;
import Domain.ClubManagement.Court;
import Domain.ClubManagement.TeamInfo;
import Data.SystemDB.DB;
import Domain.User.Fan;
import Domain.User.Guest;
import Domain.User.Referee;
import Domain.User.TeamMember;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class RefereeIT {
    private DataBaseInterface database = DB.getInstance();

    private Referee referee1;
    private Referee referee2;

    private Match match;
    private Match match1;

    private TeamInfo team;
    private TeamInfo team1;
    private Court testCourt;
    Guest g;

    @Before
    public void init(){
        database.resetDB();

        referee1=new Referee("raanan", "mesh", "raananm", "123","student","19/9/1965","raanan@gmail.com","Main Referee");
        referee2=new Referee("raanan", "mesh", "raananm", "123","student","19/9/1965","raanan@gmail.com", "Side Referee");

        g = new Guest();
        g.SignUp("gdfg","zbvcxbidan","glazer","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","glazer");
        Fan f = database.getUser("glazer");
        TeamMember ownerGlazer = (TeamMember) f;
        team = new TeamInfo("ManUtd",1906,true,"Manchester",ownerGlazer);
        database.addTeam("ManUtd",1906,true,"Manchester",ownerGlazer.getUserName(),"");
        ownerGlazer.setTeam(team);
        team1 = new TeamInfo("westham",1906,true,"holon",ownerGlazer);
        testCourt= new Court("blumfild","yafo",17000);
        Date today= Calendar.getInstance().getTime();
        match= new Match(team,team1,testCourt,today,referee1);
        match.setMainRef(referee1);

        Date date=new Date(2323223232L);
        match1= new Match(team,team1,testCourt,date,referee1);

        //player
        Guest g_gk = new Guest();
        g_gk.SignUp("gdfg","zbvcxbidan","deGea","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_gk = database.getUser("deGea");
        TeamMember gk = (TeamMember) f_gk;
        ownerGlazer.AddPlayer(gk,"gk");

        //player
        Guest g_gk1 = new Guest();
        g_gk1.SignUp("gdfg","zbvcxbidan","hugo","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_gk1 = database.getUser("hugo");
        TeamMember gk1 = (TeamMember) f_gk1;
        ownerGlazer.AddPlayer(gk1,"gk1");
    }



    @Test
    public void addCommentToGameEventsTest() {
        Assert.assertFalse(referee1.addCommentToGameEvents(null,null));
        referee1.addCommentToGameEvents(match,"foul");
        Assert.assertTrue(referee1.addCommentToGameEvents(match,"goal"));
        Assert.assertTrue(match.getEvents().getAllGameEvents().contains("foul"));

    }

    @Test
    public void addMatchTest() {
        referee1.addMatch(match);
        Assert.assertTrue(referee1.getRefereeMatches().contains(match));
    }

    @Test
    public void getRefereeMatchesTest() {
        referee1.addMatch(match);
        Assert.assertTrue(referee1.getRefereeMatches().contains(match));
    }

    @Test
    public void EditGameEventsTest() {
        referee1.addMatch(match);
        referee1.addCommentToGameEvents(match,"foul");
        referee1.EditGameEvents(match,"goal");
        Assert.assertTrue(referee1.getRefereeMatches().get(0).getEvents().getAllGameEvents().contains("goal"));

    }

    @Test(expected = IllegalArgumentException.class)
    public void WrongEditGameEventsTest() {

        referee1.addMatch(match1);
        referee1.addCommentToGameEvents(match1,"foul");
        referee1.EditGameEvents(match1,"goal");

    }

    @Test(expected = IllegalArgumentException.class)
    public void sideEditGameEventsTest() {
        referee2.EditGameEvents(match1,"goal");

    }


    @Test
    public void addGoalEventTest() {
        Assert.assertTrue(referee1.addGoalEvent(match,"deGea","home"));
        Assert.assertFalse(referee1.addGoalEvent(null,"deGea","home"));
    }

    @Test
    public void addOffsideEventTest() {
        Assert.assertTrue(referee1.addOffsideEvent(match,"deGea"));
        Assert.assertFalse(referee1.addOffsideEvent(null,null));
    }

    @Test
    public void addFoulEventTest() {
        Assert.assertTrue(referee1.addFoulEvent(match,"deGea", "yellow"));
        Assert.assertFalse(referee1.addFoulEvent(null,null, ""));
    }

    @Test
    public void addInjuryEventTest() {
        Assert.assertTrue(referee1.addInjuryEvent(match,"deGea"));
        Assert.assertFalse(referee1.addInjuryEvent(null,null));
    }

    @Test
    public void addSubEventTest() {
        Assert.assertTrue(referee1.addSubEvent(match,"deGea", "hugo"));
        Assert.assertFalse(referee1.addSubEvent(null,"",""));
    }


}

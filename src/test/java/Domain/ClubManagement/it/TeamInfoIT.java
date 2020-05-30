package Domain.ClubManagement.it;
import Data.SystemDB.DataBaseInterface;
import Domain.ClubManagement.Court;
import Domain.ClubManagement.TeamInfo;
import Data.SystemDB.DB;
import Domain.User.Fan;
import Domain.User.Guest;
import Domain.User.TeamMember;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;


public class TeamInfoIT {
    private DataBaseInterface database = DB.getInstance();
    private TeamInfo manchesterUnited;

    @Before
    public void initTeam() {
        database.resetDB();

        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember ownerGlazer = (TeamMember) f;

        //coach
        Guest g1 = new Guest();
        g1.SignUp("gdfg", "zbvcxbidan", "oleGunar", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","");
        Fan f1 = database.getUser("oleGunar");
        TeamMember coach = (TeamMember) f1;

        //player
        Guest g_gk = new Guest();
        g_gk.SignUp("gdfg", "zbvcxbidan", "deGea", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan f_gk = database.getUser("deGea");
        TeamMember gk = (TeamMember) f_gk;

        //Team Manager
        Guest t_tm = new Guest();
        t_tm.SignUp("tmmu", "zbvcxbidan", "hjhass", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm = database.getUser("tmmu");
        TeamMember tm = (TeamMember) f_tm;

        this.manchesterUnited = new TeamInfo("Manchester United", 1878, true, "Manchester", ownerGlazer);
        ownerGlazer.setTeam(manchesterUnited);
        Court court = new Court("Old Trafford","Manchester",75643);
        database.addCourt("Old Trafford","Manchester",75643);
    }

    @Test
    public void testAddPlayer() {
        TeamMember userPlayer = (TeamMember) database.getUser("deGea");
        this.manchesterUnited.addPlayer(userPlayer);
        Assert.assertTrue(this.manchesterUnited.checkIfPlayerExistIn(userPlayer));
        Assert.assertEquals(this.manchesterUnited.getNumOfPlayersInTeam(),1);
        Assert.assertEquals(((TeamMember)this.manchesterUnited.getTeamPlayers().get(0)).getUserName(),"deGea");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullPlayer() {
        this.manchesterUnited.addPlayer(null);
    }

    @Test
    public void testAddCoach() {
        TeamMember userCoach = (TeamMember) database.getUser("oleGunar");
        this.manchesterUnited.addCoach(userCoach);
        Assert.assertTrue(this.manchesterUnited.checkIfCoachExistIn(userCoach));
        Assert.assertEquals(this.manchesterUnited.getNumOfCoachesInTeam(),1);
        Assert.assertEquals(((TeamMember)this.manchesterUnited.getTeamCoaches().get(0)).getUserName(),"oleGunar");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullCoach() {
        this.manchesterUnited.addCoach(null);
    }

    @Test
    public void testAddTeamManager() {
        TeamMember userTeamManager = (TeamMember) database.getUser("hjhass");
        this.manchesterUnited.addManager(userTeamManager);
        Assert.assertTrue(this.manchesterUnited.checkIfManagerExistIn(userTeamManager));
        Assert.assertEquals(this.manchesterUnited.getNumOfManagersInTeam(),1);
        Assert.assertEquals(((TeamMember)this.manchesterUnited.getTeamManagers().get(0)).getUserName(),"hjhass");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullTeamManager() {
        this.manchesterUnited.addManager(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullOwner() {
        this.manchesterUnited.addOwner(null);
    }

    @Test
    public void testAddSameOwner() {
        TeamMember userOwner = (TeamMember) database.getUser("glazer");
        Assert.assertFalse(this.manchesterUnited.addOwner(userOwner));
    }

    @Test
    public void testAddSamePlayer() {
        TeamMember userPlayer = (TeamMember) database.getUser("deGea");
        this.manchesterUnited.addPlayer(userPlayer);
        Assert.assertFalse(this.manchesterUnited.addPlayer(userPlayer));
    }

    @Test
    public void testAddSameCoach() {
        TeamMember userCoach = (TeamMember) database.getUser("oleGunar");
        this.manchesterUnited.addCoach(userCoach);
        Assert.assertFalse(this.manchesterUnited.addCoach(userCoach));
    }

    @Test
    public void testAddSameTeamManager() {
        TeamMember userTeamManager = (TeamMember) database.getUser("hjhass");
        this.manchesterUnited.addManager(userTeamManager);
        Assert.assertFalse(this.manchesterUnited.addManager(userTeamManager));
    }

    @Test
    public void testAddCourt() {
        Court court = database.getCourt("Old Trafford");
        this.manchesterUnited.getTeamOwner().AddCourt(court);
        Assert.assertTrue(this.manchesterUnited.checkIfCourtExistIn(court));
        Assert.assertEquals(this.manchesterUnited.getTeamCourts().get(0).getCourtName(),"Old Trafford");
    }

    @Test
    public void testAddSameCourt() {
        Court court = database.getCourt("Old Trafford");
        this.manchesterUnited.getTeamOwner().AddCourt(court);
        Assert.assertTrue(this.manchesterUnited.checkIfCourtExistIn(court));
        Court court2 = database.getCourt("Old Trafford");
        Assert.assertFalse(this.manchesterUnited.addCourt(court2));
    }

    @Test
    public void testRemovePlayer() {
        TeamMember userPlayer = (TeamMember) database.getUser("deGea");
        this.manchesterUnited.addPlayer(userPlayer);
        Assert.assertTrue(this.manchesterUnited.checkIfPlayerExistIn(userPlayer));
        this.manchesterUnited.removePlayer(userPlayer);
        Assert.assertFalse(this.manchesterUnited.checkIfPlayerExistIn(userPlayer));
    }

    @Test
    public void testRemoveCoach() {
        TeamMember userCoach = (TeamMember) database.getUser("oleGunar");
        this.manchesterUnited.addCoach(userCoach);
        Assert.assertTrue(this.manchesterUnited.checkIfCoachExistIn(userCoach));
        this.manchesterUnited.removeCoach(userCoach);
        Assert.assertFalse(this.manchesterUnited.checkIfCoachExistIn(userCoach));
    }

    @Test
    public void testRemoveTeamMember() {
        TeamMember userTeamMember = (TeamMember) database.getUser("hjhass");
        this.manchesterUnited.addManager(userTeamMember);
        Assert.assertTrue(this.manchesterUnited.checkIfManagerExistIn(userTeamMember));
        this.manchesterUnited.removeManager(userTeamMember);
        Assert.assertFalse(this.manchesterUnited.checkIfManagerExistIn(userTeamMember));
    }

    @Test
    public void testRemoveCourt() {
        Court court = database.getCourt("Old Trafford");
        this.manchesterUnited.getTeamOwner().AddCourt(court);
        Assert.assertTrue(this.manchesterUnited.checkIfCourtExistIn(court));
        this.manchesterUnited.removeCourt(court);
        this.manchesterUnited.checkIfCourtExistIn(court);
    }

    @Test (expected = IllegalArgumentException.class)
    public void addNullCourt(){
        this.manchesterUnited.addCourt(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNullTeamManager() {
        this.manchesterUnited.removeManager(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNullCoach() {
        this.manchesterUnited.removeCoach(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNullPlayer() {
        this.manchesterUnited.removePlayer(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNullOwner() {
        this.manchesterUnited.removeOwner(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNullCourt() {
        this.manchesterUnited.removeCourt(null);
    }

    @Test
    public void testRemovePlayerNotInTeam() {
        TeamMember tm2 = new TeamMember("Gal","Buz","GBZ","4567","Player",
                "12/12/12","lala@gmail.com",null);
        Assert.assertFalse(this.manchesterUnited.removePlayer(tm2));
    }

    @Test
    public void testRemoveCoachNotInTeam() {
        TeamMember tm3 = new TeamMember("Gal","Buz","GBZ","4567","Coach",
                "12/12/12","lala@gmail.com",null);
        Assert.assertFalse(this.manchesterUnited.removeCoach(tm3));
    }

    @Test
    public void testRemoveTeamManagerNotInTeam() {
        TeamMember tm4 = new TeamMember("Gal","Buz","GBZ","4567","TeamManager",
                "12/12/12","lala@gmail.com",null);
        Assert.assertFalse(this.manchesterUnited.removeManager(tm4));
    }

    @Test
    public void testRemoveOwnerNotInTeam() {
        TeamMember tm5 = new TeamMember("Gal","Buz","GBZ","4567","Owner",
                "12/12/12","lala@gmail.com",null);
        Assert.assertFalse(this.manchesterUnited.removeOwner(tm5));
    }

    @Test
    public void testRemoveOwner() {
        TeamMember tm6 = new TeamMember("Gal","Buz","GBZ","4567","Owner",
                "12/12/12","lala@gmail.com",this.manchesterUnited);
        this.manchesterUnited.addOwner(tm6);
        TeamMember userOwner = (TeamMember) database.getUser("glazer");
        Assert.assertEquals(this.manchesterUnited.getNumOfOwnersInTeam(),2);
        Assert.assertTrue(this.manchesterUnited.removeOwner(userOwner));
        Assert.assertEquals(this.manchesterUnited.getNumOfOwnersInTeam(),1);
        Assert.assertEquals(((TeamMember)this.manchesterUnited.getTeamOwners().get(0)).getUserName(),"GBZ");
        Assert.assertFalse(this.manchesterUnited.removeOwner(tm6));
    }

    @Test
    public void testRemoveCourtNotInTeam() {
        Court court3 = new Court("Santiago Bernabeu","Madrid",80000);
        Assert.assertFalse(this.manchesterUnited.removeCourt(court3));
    }

}








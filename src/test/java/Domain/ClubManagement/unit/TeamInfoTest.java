package Domain.ClubManagement.unit;
import Data.SystemDB.DB;
import Data.SystemDB.DataBaseInterface;
import Domain.ClubManagement.Court;
import Domain.ClubManagement.TeamInfo;
import Domain.User.Fan;
import Domain.User.Guest;
import Domain.User.TeamMember;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

//Test
public class TeamInfoTest {

    private DataBaseInterface database = DB.getInstance();

    private TeamInfo team1;
    private TeamInfo team2;



    @Before
    public void init() {
        database.resetDB();

        //owner
        Guest ge = new Guest();
        ge.SignUp("gdfg", "zbvcxbidan", "Rubi", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        ge.Login("1234", "Rubi");
        Fan f = database.getUser("Rubi");
        TeamMember ownerRubi = (TeamMember) f;

        this.team1 = new TeamInfo("Arsenal", 1902, true, "London", ownerRubi);
        ownerRubi.setTeam(team1);

    }

    @Test
    public void testGetEstablishYear(){
        Assert.assertEquals(this.team1.getEstablishYear(),1902);
    }

    @Test
    public void testSetTeamScores(){
        this.team1.setDefeatCount(10);
        this.team1.setDrawCount(15);
        this.team1.setWinCount(20);
        this.team1.setMatchesPlayed(45);
        this.team1.setReceivedGoalsAmount(25);
        this.team1.setPoints(50);
        this.team1.setScoreGoalsCount(30);
        Assert.assertEquals(this.team1.getDefeatCount(),10);
        Assert.assertEquals(this.team1.getDrawCount(),15);
        Assert.assertEquals(this.team1.getWinCount(),20);
        Assert.assertEquals(this.team1.getMatchesPlayed(),45);
        Assert.assertEquals(this.team1.getReceivedGoalsAmount(),25);
        Assert.assertEquals(this.team1.getPoints(),50);
        Assert.assertEquals(this.team1.getScoreGoalsCount(),30);
    }

    @Test
    public void testTeamStatus(){
        Assert.assertTrue(this.team1.isTeamActiveStatus());
        this.team1.setTeamActiveStatus(false);
        Assert.assertFalse(this.team1.isTeamActiveStatus());
        this.team1.setTeamActiveStatus(true);
    }

    @Test
    public void testTeamHomeCourt(){
        Court court1 = new Court("Emirates Stadium","London",60400);
        Assert.assertTrue(this.team1.addCourt(court1));
        Assert.assertEquals(this.team1.getTeamHomeCourt().getCourtName(),"Emirates Stadium");
    }

}


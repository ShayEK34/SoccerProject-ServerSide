package Domain.InformationPage.it;

import Domain.ClubManagement.TeamInfo;
import Domain.InformationPage.TeamInformationPage;
import Domain.User.TeamMember;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class TeamInformationPageIT {

    private TeamInformationPage informationPage;
    private TeamInfo team;
    TeamMember owner2;

    @Before
    public void init(){
        owner2 = new TeamMember("jozep ", "Bartomeo ", "JB26", "1111",
                null, "31/8/1991", "FlorentinoP@gmail.com", null);
        team = new TeamInfo("FC Barcelona", 1902, true, "Barcelona", (TeamMember) owner2);
        ((TeamMember)this.owner2).setTeam(team);
        informationPage=new TeamInformationPage(team);
    }

    @Test
    public void getTeamTest(){
        Assert.assertEquals(informationPage.getTeam(),team);
    }

    @Test
    public void setTeamTest(){
        TeamMember owner = new TeamMember("jozep ", "Bartomeo ", "JB26", "1111",
                null, "31/8/1991", "FlorentinoP@gmail.com", null);
        TeamInfo newTeam = new TeamInfo("Barcelona", 1902, true, "Barcelona", owner);
        (owner).setTeam(newTeam);

        informationPage.setTeam(newTeam);
        Assert.assertEquals(informationPage.getTeam(),newTeam);

    }
}

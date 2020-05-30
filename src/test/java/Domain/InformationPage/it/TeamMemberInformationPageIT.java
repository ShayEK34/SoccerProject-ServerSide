package Domain.InformationPage.it;

import Domain.InformationPage.TeamMemberInformationPage;
import Domain.User.TeamMember;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class TeamMemberInformationPageIT {

    private TeamMemberInformationPage informationPage;
    TeamMember user;

    @Before
    public void init(){
        user = new TeamMember("jozep ", "Bartomeo ", "JB26", "1111",
                null, "31/8/1991", "FlorentinoP@gmail.com", null);
        informationPage=new TeamMemberInformationPage(user);
    }

    @Test
    public void getTeamMemberTest(){
        Assert.assertEquals(informationPage.getUser(), user);
    }

    @Test
    public void setTeamMemberTest(){
        TeamMember newUser = new TeamMember("jozep ", "Bartomeo ", "JB26", "1111",
                null, "31/8/1991", "FlorentinoP@gmail.com", null);

        informationPage.setUser(newUser);
        Assert.assertEquals(informationPage.getUser(),newUser);

    }
}


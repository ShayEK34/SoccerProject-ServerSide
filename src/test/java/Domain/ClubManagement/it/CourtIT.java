package Domain.ClubManagement.it;

import Domain.ClubManagement.Court;
import Domain.ClubManagement.TeamInfo;
import Domain.User.OwnerInterface;
import Domain.User.TeamMember;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class CourtIT {


    private Court testCourt2;
    private OwnerInterface owner2;
    private TeamInfo barcelona;

    @Before
    public void initCourt(){
        testCourt2 = new Court ("Camp Nou", "Barcelona", 99000);
        owner2 = new TeamMember("jozep ", "Bartomeo ", "JB26", "1111",
                null, "31/8/1991", "FlorentinoP@gmail.com", null);
        barcelona = new TeamInfo("FC Barcelona", 1902, true, "Barcelona", (TeamMember) owner2);
        ((TeamMember)this.owner2).setTeam(barcelona);
    }

    @Test
    public void testAddTeamToCourt(){
        Assert.assertTrue(this.testCourt2.addTeamToCourtList(this.barcelona));
        Assert.assertFalse(this.testCourt2.addTeamToCourtList(this.barcelona));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testAddNullTeamToCourt(){
        Assert.assertFalse(this.testCourt2.addTeamToCourtList(null));
    }
}

package Domain.User.it;

import Data.SystemDB.DataBaseInterface;
import Domain.ClubManagement.TeamInfo;
import Data.SystemDB.DB;
import Domain.User.*;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class SystemManagerIT {

    private DataBaseInterface database = DB.getInstance();
    private SystemManager systemManager;
    Guest g;
    TeamInfo manUtd;

    @Before
    public void init(){
        database.resetDB();
        systemManager=new SystemManager("David","Levi","dav123","dav1234","No Occupation","21/1/1997","david@gmail.com");
        g = new Guest();
        g.SignUp("gdfg","zbvcxbidan","glazer","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","glazer");
        Fan f = database.getUser("glazer");
        TeamMember ownerGlazer = (TeamMember) f;
        manUtd = new TeamInfo("ManUtd",1906,true,"Manchester",ownerGlazer);
        database.addTeam("ManUtd",1906,true,"Manchester",ownerGlazer.getUserName(),"");

    }

    @Test
    public void closeTeamTest(){
        systemManager.closeTeamCompletely(manUtd);
        boolean a = manUtd.isTeamActiveStatus();
        Assert.assertFalse(manUtd.isTeamActiveStatus());
    }

    @Test
    public void openTeamTest(){
        systemManager.closeTeamCompletely(manUtd);
        systemManager.openTeamFromStart(manUtd);
        Assert.assertTrue(manUtd.isTeamActiveStatus());
    }

    @Test
    public void deleteTeamTest(){
        systemManager.deleteTeam(manUtd);
        Assert.assertNull(database.getTeam("ManUtd"));
    }

    @Test
    public void removeAccountTest(){
        g.SignUp("test","test","test","test","4/6/test","test","test","","");
        systemManager.RemoveAccount(database.getUser("test"));
        Assert.assertNull(database.getUser("test"));

    }

    @Test
    public void newMainRefereeTest(){
        g.SignUp("test","test","test","test","4/6/test","test","test","Referee","Main Referee");
        Assert.assertNotNull(database.getUser("test"));
        Assert.assertTrue(database.getUser("test") instanceof Referee);
    }
    @Test
    public void newSideRefereeTest(){
        g.SignUp("test","test","test","test","4/6/test","test","test","Referee","Side Referee");
        Assert.assertNotNull(database.getUser("test"));
        Assert.assertTrue(database.getUser("test") instanceof Referee);
    }
    @Test
    public void newCoachTest(){
        g.SignUp("test","test","test","test","4/6/test","test","test","Coach","Head Coach");
        TeamMember tm=(TeamMember)database.getUser("test");
        Assert.assertNotNull(tm);
        Assert.assertTrue(tm.isCoach());
    }

    @Test
    public void newOwnerTest(){
        g.SignUp("test","test","test","test","4/6/test","test","test","Owner","");
        TeamMember tm=(TeamMember)database.getUser("test");
        Assert.assertNotNull(tm);
        Assert.assertTrue(tm.isOwner());
    }
    @Test
    public void newAssociationTest(){
        g.SignUp("test","test","test","test","4/6/test","test","test","Association","");
        Assert.assertNotNull(database.getUser("test"));
        Assert.assertTrue(database.getUser("test") instanceof AssociationUser);
    }

    @Test
    public void newPlayerTest(){
        g.SignUp("test","test","test","test","4/6/test","test","test","Player","CF");
        TeamMember tm=(TeamMember)database.getUser("test");
        Assert.assertNotNull(tm);
        Assert.assertTrue(tm.isPlayer());
    }

    @Test
    public void newTeamManagerTest(){
        g.SignUp("test","test","test","test","4/6/test","test","test","TeamManager","");
        TeamMember tm=(TeamMember)database.getUser("test");
        Assert.assertNotNull(tm);
        Assert.assertTrue(tm.isTeamManager());
    }
}


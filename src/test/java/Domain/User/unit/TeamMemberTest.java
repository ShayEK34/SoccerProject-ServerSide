package Domain.User.unit;

import Data.SystemDB.DB;
import Data.SystemDB.DataBaseInterface;
import Domain.ClubManagement.Court;
import Domain.User.Fan;
import Domain.User.Guest;
import Domain.User.TeamMember;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class TeamMemberTest {
    private DataBaseInterface database = DB.getInstance();

    @Before
    public void init() {
        database.resetDB();
    }

    @Test
    public void TestEditCoachInfo() {
        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        coach.EditCoachInfo("eEli", "eRahamim", "eeli1222", "efdgsfd", "04/6/1954", "aesda@gmail.com");
        Assert.assertEquals(coach.getFirstName(), "eEli");
        Assert.assertEquals(coach.getLastName(), "eRahamim");
        Assert.assertEquals(coach.getPassword(), "eeli1222");
        Assert.assertEquals(coach.getOccupation(), "efdgsfd");
        Assert.assertEquals(coach.getBirthday(), "04/6/1954");
        Assert.assertEquals(coach.getEmail(), "aesda@gmail.com");
    }

    @Test (expected = IllegalArgumentException.class)
    public void TestEditCoachInfo2() {
        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        //player
        Guest guest2 = new Guest();
        guest2.SignUp("Avi", "Eliyaho", "aviEl", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan2 = database.getUser("aviEl");
        TeamMember player = (TeamMember) fan2;

        player.EditCoachInfo("eEli", "eRahamim", "eeli1222", "efdgsfd", "04/6/1954", "aesda@gmail.com");
    }

    @Test
    public void TestEditPlayerInfo() {
        //player
        Guest guest2 = new Guest();
        guest2.SignUp("Avi", "Eliyaho", "aviEl", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan2 = database.getUser("aviEl");
        TeamMember player = (TeamMember) fan2;

        player.EditPlayerInfo("aAvi", "aEliyaho", "aavi12345", "afdgsfd", "04/6/1954", "aAviabc@gmail.com");
        Assert.assertEquals(player.getFirstName(), "aAvi");
        Assert.assertEquals(player.getLastName(), "aEliyaho");
        Assert.assertEquals(player.getPassword(), "aavi12345");
        Assert.assertEquals(player.getOccupation(), "afdgsfd");
        Assert.assertEquals(player.getBirthday(), "04/6/1954");
        Assert.assertEquals(player.getEmail(), "aAviabc@gmail.com");
    }

    @Test
    public void TestsetOwner() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        boolean ownerrr=true;
        owner.setOwner(ownerrr);
    }

    @Test
    public void TestsetPlayer() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        boolean playerrr=true;
        owner.setPlayer(playerrr);
    }

    @Test
    public void TestsetCoach() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        boolean coachhh=true;
        owner.setCoach(coachhh);
    }

    @Test
    public void TestsetTeamManager() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        boolean teamManagerrr=true;
        owner.setTeamManager(teamManagerrr);
    }

    @Test
    public void TestisOwner() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        Assert.assertTrue( owner.isOwner());
    }

    @Test
    public void TestisPlayer() {
        //player
        Guest guest2 = new Guest();
        guest2.SignUp("Avi", "Eliyaho", "aviEl", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan2 = database.getUser("aviEl");
        TeamMember player = (TeamMember) fan2;

        Assert.assertTrue(player.isPlayer());
    }

    @Test
    public void TestisCoach() {
        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        Assert.assertTrue(coach.isCoach());
    }

    @Test
    public void TestgetPreparation() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;
        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        owner.EditCoachInformation(coach,"y", "");
        Assert.assertEquals(coach.getPreparation(),"y"); ;
    }

    @Test
    public void TestgetTeamRole() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        owner.EditCoachInformation(coach,"", "y");
        Assert.assertEquals(coach.getTeamRole(),"y"); ;
    }

    @Test
    public void TestgetRoleOnCourt() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        //player
        Guest guest2 = new Guest();
        guest2.SignUp("Avi", "Eliyaho", "aviEl", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan2 = database.getUser("aviEl");
        TeamMember player = (TeamMember) fan2;

        owner.EditPlayerInformation(player,"y");
        Assert.assertEquals(player.getRoleOnCourt(),"y"); ;
    }

    @Test (expected = IllegalArgumentException.class)
    public void AddCoachUserNotInTheSystemTest() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;
        TeamMember aaa = (TeamMember)database.getUser("aaa");
        Assert.assertFalse(owner.AddCoach(aaa,"s","g"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void AddPlayerUserNotInTheSystemTest() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;
        TeamMember aaa = (TeamMember)database.getUser("aaa");

        Assert.assertFalse(owner.AddPlayer(aaa,"s"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void RemoveCoachUserNotInTheSystemTest() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;
        TeamMember aaa = (TeamMember)database.getUser("aaa");

        Assert.assertFalse(owner.RemoveCoach(aaa));
    }

    //@Test (expected = IllegalArgumentException.class)
    public void RemovePlayerUserNotInTheSystemTest() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;
        TeamMember aaa = (TeamMember)database.getUser("aaa");
        Assert.assertFalse(owner.RemovePlayer(aaa));
    }

    @Test (expected = IllegalArgumentException.class)
    public void RemoveCourtUserNotInTheSystemTest() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;
        Court aaa = database.getCourt("aaa");

        Assert.assertFalse(owner.RemoveCourt(aaa));
    }

    @Test (expected = IllegalArgumentException.class)
    public void AddOwnerUserNotInTheSystemTest() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;
        TeamMember aaa = (TeamMember)database.getUser("aaa");
        Assert.assertFalse(owner.AddOwner(aaa));
    }

    @Test (expected = IllegalArgumentException.class)
    public void EditPlayerInfoUserIsntPlayer() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        owner.EditPlayerInfo("aAvi", "aEliyaho", "aavi12345", "afdgsfd", "04/6/1954", "aAviabc@gmail.com");
    }

    @Test (expected = IllegalArgumentException.class)
    public void AddTeamManagerUserNotInTheSystemTest() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;
        TeamMember aaa = (TeamMember)database.getUser("aaa");

        Assert.assertFalse(owner.AddTeamManager(aaa,true,true,true,true));
    }

    @Test (expected = IllegalArgumentException.class)
    public void RemoveOwnerUserNotInTheSystemTest() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;
        TeamMember aaa = (TeamMember)database.getUser("aaa");

        Assert.assertFalse(owner.RemoveOwner(aaa));
    }

    //@Test (expected = IllegalArgumentException.class)
    public void RemoveTeamManagerUserNotInTheSystemTest() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;
        TeamMember aaa = (TeamMember)database.getUser("aaa");
        Assert.assertFalse(owner.RemoveTeamManager(aaa));
    }

    @Test (expected = IllegalArgumentException.class)
    public void RemovePlayerUserNotOwnerOrTeamMemberTest() {
        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;
        TeamMember aaa = (TeamMember)database.getUser("aaa");

        Assert.assertFalse(coach.RemovePlayer(aaa));
    }

    @Test (expected = IllegalArgumentException.class)
    public void RemoveCourtUserNotOwnerOrTeamMemberTest() {
        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;
        Court aaa = database.getCourt("aaa");


        Assert.assertFalse(coach.RemoveCourt(aaa));
    }

    @Test (expected = IllegalArgumentException.class)
    public void EditCoachInformationUserNotOwnerOrTeamMemberTest() {
        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;
        TeamMember aaa = (TeamMember)database.getUser("aaa");

        coach.EditCoachInformation(aaa,"g","f");
    }

    @Test (expected = IllegalArgumentException.class)
    public void EditPlayerInformationUserNotOwnerOrTeamMemberTest() {
        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;
        TeamMember aaa = (TeamMember)database.getUser("aaa");
        coach.EditPlayerInformation(aaa,"g");
    }

    @Test (expected = IllegalArgumentException.class)
    public void EditPlayerInformationUserInTheSystemTest() {
        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        Court court = new Court ("Camp Nou", "Barcelona", 99000);
        database.addCourt("Camp Nou", "Barcelona", 99000);

        coach.EditCourtInfo(court, "Camp",600);
    }

    @Test (expected = IllegalArgumentException.class)
    public void EditCourtInfoUserInTheSystemTest() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;
        TeamMember aaa = (TeamMember)database.getUser("aaa");
        owner.EditPlayerInformation(aaa,"g");
    }

    @Test (expected = IllegalArgumentException.class)
    public void OwnerUserNotInTheSystemTest() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;
        TeamMember aaa = (TeamMember)database.getUser("aaa");
        Assert.assertFalse(owner.AddOwner(aaa));
    }

    @Test (expected = IllegalArgumentException.class)
    public void AddTeamManagerUserNotOwnerOrTeamMemberTest() {
        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;
        TeamMember aaa = (TeamMember)database.getUser("aaa");

        Assert.assertFalse(coach.AddTeamManager(aaa,true,true,true,true));
    }

    //@Test (expected = IllegalArgumentException.class)
    public void RemoveOwnerUserNotOwnerTest() {
        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;
        TeamMember aaa = (TeamMember)database.getUser("aaa");

        Assert.assertFalse(coach.RemoveOwner(aaa));
    }

    @Test (expected = IllegalArgumentException.class)
    public void RemoveTeamManagerUserNotOwnerTest() {
        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;
        TeamMember aaa = (TeamMember)database.getUser("aaa");

        Assert.assertFalse(coach.RemoveTeamManager(aaa));
    }

    @Test (expected = IllegalArgumentException.class)
    public void TestAddCoach3() {
        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        //player
        Guest guest2 = new Guest();
        guest2.SignUp("Avi", "Eliyaho", "aviEl", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan2 = database.getUser("aviEl");
        TeamMember player = (TeamMember) fan2;

        Assert.assertFalse(player.AddCoach(coach,"s","g"));
    }

    //@Test (expected = IllegalArgumentException.class)
    public void RemoveCoachCheckUserIsOwnerOrTeamMemberTest() {
        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        //player
        Guest guest2 = new Guest();
        guest2.SignUp("Avi", "Eliyaho", "aviEl", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan2 = database.getUser("aviEl");
        TeamMember player = (TeamMember) fan2;
        //player.RemoveCoach(coach);
        Assert.assertFalse(player.RemoveCoach(coach));
    }

    @Test (expected = IllegalArgumentException.class)
    public void TestsetMemberIdentity() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        owner.setMemberIdentity(false, false, false, false, "");
    }
}



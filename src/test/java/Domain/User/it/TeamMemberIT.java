package Domain.User.it;
import Data.SystemDB.DataBaseInterface;
import Domain.AssociationManagement.League;
import Domain.ClubManagement.Court;
import Domain.ClubManagement.TeamInfo;
import Domain.InformationPage.Post;
import Domain.InformationPage.TeamMemberInformationPage;
import Data.SystemDB.DB;
import Domain.User.AssociationUser;
import Domain.User.Fan;
import Domain.User.Guest;
import Domain.User.TeamMember;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Date;

public class TeamMemberIT {
    private DataBaseInterface database = DB.getInstance();

    @Before
    public void init() {
        database.resetDB();
    }

    @Test
    public void TestUploadContentInfo() {
        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        String content = "I live in Japan";
        TeamMemberInformationPage tmip = new TeamMemberInformationPage(coach);
        tmip.addPost(content);
        coach.UploadContentInfo(content);
        ArrayList<Post> posts = tmip.getPostsPage();
        for (Post p : posts) {
            Assert.assertEquals(p.getContent(), content);
        }
    }

    @Test
    public void TestRemoveTeamManager() {
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

        //player
        Guest guest2 = new Guest();
        guest2.SignUp("Avi", "Eliyaho", "aviEl", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan2 = database.getUser("aviEl");
        TeamMember player = (TeamMember) fan2;

        //Team Manager1
        Guest t_tm = new Guest();
        t_tm.SignUp("tim", "zbvcxbidan", "tmmu", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm = database.getUser("tmmu");
        TeamMember teamManager = (TeamMember) f_tm;

        //Team Manager2
        Guest t_tm2 = new Guest();
        t_tm2.SignUp("tomm", "zzzzz", "tommy", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm2 = database.getUser("tommy");
        TeamMember teamManager2 = (TeamMember) f_tm2;

        TeamInfo manchesterUnited = new TeamInfo("Manchester United", 1878, true, "Manchester", owner);
        owner.setTeam(manchesterUnited);
        teamManager.setTeam(manchesterUnited);

        owner.AddTeamManager(teamManager, true, true, false, true);
        owner.AddTeamManager(teamManager2, true, true, false, true);
        //owner.RemoveTeamManager(coach);
        Assert.assertTrue(owner.RemoveTeamManager(coach));
        Assert.assertTrue(owner.RemoveTeamManager(teamManager2));
    }


    @Test
    public void TestAddCoach() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        //team
        TeamInfo manchesterUnited = new TeamInfo("Manchester United", 1878, true, "Manchester", owner);
        owner.setTeam(manchesterUnited);

        //Team Managertest
        Guest t_tmtest = new Guest();
        t_tmtest.SignUp("tim", "zbvcxbidan", "tmmutest", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tmtest = database.getUser("tmmutest");
        TeamMember teamManagertest = (TeamMember) f_tmtest;
        owner.AddTeamManager(teamManagertest,true,true,true,true);

        teamManagertest.setTeam(manchesterUnited);

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

        //fan user
        Guest guest3 = new Guest();
        guest3.SignUp("Sami", "Akabai", "sami12", "fdgsfd", "4/6/1954", "samiii@gmail.com", "95555","","");
        Fan fan = database.getUser("sami12");

        Court court = new Court ("Camp Nou", "Barcelona", 99000);
        manchesterUnited.addCourt(court);
        database.addCourt("Camp Nou", "Barcelona", 99000);

        Assert.assertTrue(owner.AddCoach(coach,"s","g"));
        Assert.assertFalse(owner.AddCoach(player,"s","g"));
    }

    @Test (expected = ClassCastException.class)
    public void checkUserIsCoachTest() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        //owner2
        Guest g2 = new Guest();
        g2.SignUp("gdhjklfg", "zbvcxbbjkidan", "glsdfazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g2.Login("12345", "glsdfazer");
        Fan f2 = database.getUser("glsdfazer");
        TeamMember owner2 = (TeamMember) f2;

        //owner3
        Guest g3 = new Guest();
        g3.SignUp("gdhjklfg", "zbvcxbbjkidan", "gggggg", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g3.Login("12345", "gggggg");
        Fan f3 = database.getUser("gggggg");
        TeamMember owner3 = (TeamMember) f3;

        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        //coach2
        Guest guestcoach2 = new Guest();
        guestcoach2.SignUp("yossi", "Rahamim", "yossi", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fancoach2 = database.getUser("yossi");
        TeamMember coach2 = (TeamMember) fancoach2;

        //player
        Guest guest2 = new Guest();
        guest2.SignUp("Avi", "Eliyaho", "aviEl", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan2 = database.getUser("aviEl");
        TeamMember player = (TeamMember) fan2;

        //player2
        Guest guestplayer2 = new Guest();
        guestplayer2.SignUp("roni", "Eliyaho", "roni", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fanplayer2 = database.getUser("roni");
        TeamMember player2 = (TeamMember) fanplayer2;

        //Team Manager1
        Guest t_tm = new Guest();
        t_tm.SignUp("tim", "zbvcxbidan", "tmmu", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm = database.getUser("tmmu");
        TeamMember teamManager = (TeamMember) f_tm;

        //Team Manager2
        Guest t_tm2 = new Guest();
        t_tm2.SignUp("tomm", "zzzzz", "tommy", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm2 = database.getUser("tommy");
        TeamMember teamManager2 = (TeamMember) f_tm2;

        //fan user
        Guest guest3 = new Guest();
        guest3.SignUp("Sami", "Akabai", "sami12", "fdgsfd", "4/6/1954", "samiii@gmail.com", "95555","","");
        Fan fan = database.getUser("sami12");

        TeamInfo manchesterUnited = new TeamInfo("Manchester United", 1878, true, "Manchester", owner);
        owner.setTeam(manchesterUnited);
        teamManager.setTeam(manchesterUnited);

        TeamInfo manchester = new TeamInfo("Manchester", 1879, true, "Manchest", owner2);
        owner2.setTeam(manchester);

        Court court = new Court ("Camp Nou", "Barcelona", 99000);
        Court court2 = new Court ("Camp", "Barcelona", 99000);
        database.addCourt("Camp Nou", "Barcelona", 99000);
        database.addCourt("Camp", "Barcelona", 99000);

        Assert.assertFalse(owner.AddCoach((TeamMember)fan,"s","g"));
    }

    @Test
    public void TestAddPlayer() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        //owner2
        Guest g2 = new Guest();
        g2.SignUp("gdhjklfg", "zbvcxbbjkidan", "glsdfazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g2.Login("12345", "glsdfazer");
        Fan f2 = database.getUser("glsdfazer");
        TeamMember owner2 = (TeamMember) f2;

        //owner3
        Guest g3 = new Guest();
        g3.SignUp("gdhjklfg", "zbvcxbbjkidan", "gggggg", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g3.Login("12345", "gggggg");
        Fan f3 = database.getUser("gggggg");
        TeamMember owner3 = (TeamMember) f3;

        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        //coach2
        Guest guestcoach2 = new Guest();
        guestcoach2.SignUp("yossi", "Rahamim", "yossi", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fancoach2 = database.getUser("yossi");
        TeamMember coach2 = (TeamMember) fancoach2;

        //player
        Guest guest2 = new Guest();
        guest2.SignUp("Avi", "Eliyaho", "aviEl", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan2 = database.getUser("aviEl");
        TeamMember player = (TeamMember) fan2;

        //player2
        Guest guestplayer2 = new Guest();
        guestplayer2.SignUp("roni", "Eliyaho", "roni", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fanplayer2 = database.getUser("roni");
        TeamMember player2 = (TeamMember) fanplayer2;

        //Team Manager1
        Guest t_tm = new Guest();
        t_tm.SignUp("tim", "zbvcxbidan", "tmmu", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm = database.getUser("tmmu");
        TeamMember teamManager = (TeamMember) f_tm;

        //Team Manager2
        Guest t_tm2 = new Guest();
        t_tm2.SignUp("tomm", "zzzzz", "tommy", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm2 = database.getUser("tommy");
        TeamMember teamManager2 = (TeamMember) f_tm2;

        //fan user
        Guest guest3 = new Guest();
        guest3.SignUp("Sami", "Akabai", "sami12", "fdgsfd", "4/6/1954", "samiii@gmail.com", "95555","","");
        Fan fan = database.getUser("sami12");

        TeamInfo manchesterUnited = new TeamInfo("Manchester United", 1878, true, "Manchester", owner);
        owner.setTeam(manchesterUnited);
        teamManager.setTeam(manchesterUnited);

        TeamInfo manchester = new TeamInfo("Manchester", 1879, true, "Manchest", owner2);
        owner2.setTeam(manchester);

        Court court = new Court ("Camp Nou", "Barcelona", 99000);
        Court court2 = new Court ("Camp", "Barcelona", 99000);
        database.addCourt("Camp Nou", "Barcelona", 99000);
        database.addCourt("Camp", "Barcelona", 99000);
        owner.AddTeamManager(teamManager,true,true,true,true);

        Assert.assertFalse(owner.AddPlayer(coach,"yy"));
        Assert.assertTrue(owner.AddPlayer(player,"yy"));
    }

    @Test (expected = ClassCastException.class)
    public void TestAddPlayer2() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        //team
        TeamInfo manchesterUnited = new TeamInfo("Manchester United", 1878, true, "Manchester", owner);
        owner.setTeam(manchesterUnited);

        //Team Managertest
        Guest t_tmtest = new Guest();
        t_tmtest.SignUp("tim", "zbvcxbidan", "tmmutest", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tmtest = database.getUser("tmmutest");
        TeamMember teamManagertest = (TeamMember) f_tmtest;
        owner.AddTeamManager(teamManagertest,true,true,true,true);

        teamManagertest.setTeam(manchesterUnited);

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

        //fan user
        Guest guest3 = new Guest();
        guest3.SignUp("Sami", "Akabai", "sami12", "fdgsfd", "4/6/1954", "samiii@gmail.com", "95555","","");
        Fan fan = database.getUser("sami12");

        Court court = new Court ("Camp Nou", "Barcelona", 99000);
        manchesterUnited.addCourt(court);
        database.addCourt("Camp Nou", "Barcelona", 99000);

        Assert.assertFalse(owner.AddPlayer((TeamMember)fan,"s"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void TestAddPlayer3() {
        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        //player
        Guest guest2 = new Guest();
        guest2.SignUp("Avi", "Eliyaho", "aviEl", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan2 = database.getUser("aviEl");
        TeamMember player = (TeamMember) fan2;

        Assert.assertFalse(coach.AddPlayer(coach,"s"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void TestAddCourt2() {
        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        Court court = new Court ("Camp Nou", "Barcelona", 99000);
        database.addCourt("Camp Nou", "Barcelona", 99000);

        Assert.assertFalse(coach.AddCourt(court));
    }

    @Test
    public void TestRemoveCoach() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        //owner2
        Guest g2 = new Guest();
        g2.SignUp("gdhjklfg", "zbvcxbbjkidan", "glsdfazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g2.Login("12345", "glsdfazer");
        Fan f2 = database.getUser("glsdfazer");
        TeamMember owner2 = (TeamMember) f2;

        //owner3
        Guest g3 = new Guest();
        g3.SignUp("gdhjklfg", "zbvcxbbjkidan", "gggggg", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g3.Login("12345", "gggggg");
        Fan f3 = database.getUser("gggggg");
        TeamMember owner3 = (TeamMember) f3;

        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        //coach2
        Guest guestcoach2 = new Guest();
        guestcoach2.SignUp("yossi", "Rahamim", "yossi", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fancoach2 = database.getUser("yossi");
        TeamMember coach2 = (TeamMember) fancoach2;

        //player
        Guest guest2 = new Guest();
        guest2.SignUp("Avi", "Eliyaho", "aviEl", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan2 = database.getUser("aviEl");
        TeamMember player = (TeamMember) fan2;

        //player2
        Guest guestplayer2 = new Guest();
        guestplayer2.SignUp("roni", "Eliyaho", "roni", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fanplayer2 = database.getUser("roni");
        TeamMember player2 = (TeamMember) fanplayer2;

        //Team Manager1
        Guest t_tm = new Guest();
        t_tm.SignUp("tim", "zbvcxbidan", "tmmu", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm = database.getUser("tmmu");
        TeamMember teamManager = (TeamMember) f_tm;

        //Team Manager2
        Guest t_tm2 = new Guest();
        t_tm2.SignUp("tomm", "zzzzz", "tommy", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm2 = database.getUser("tommy");
        TeamMember teamManager2 = (TeamMember) f_tm2;

        //fan user
        Guest guest3 = new Guest();
        guest3.SignUp("Sami", "Akabai", "sami12", "fdgsfd", "4/6/1954", "samiii@gmail.com", "95555","","");
        Fan fan = database.getUser("sami12");

        TeamInfo manchesterUnited = new TeamInfo("Manchester United", 1878, true, "Manchester", owner);
        owner.setTeam(manchesterUnited);
        teamManager.setTeam(manchesterUnited);

        TeamInfo manchester = new TeamInfo("Manchester", 1879, true, "Manchest", owner2);
        owner2.setTeam(manchester);

        Court court = new Court ("Camp Nou", "Barcelona", 99000);
        Court court2 = new Court ("Camp", "Barcelona", 99000);
        database.addCourt("Camp Nou", "Barcelona", 99000);
        database.addCourt("Camp", "Barcelona", 99000);

        Guest t_tmtest = new Guest();
        t_tmtest.SignUp("tim", "zbvcxbidan", "tmmutest", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tmtest = database.getUser("tmmutest");
        owner.AddCoach(coach,"","");
        owner.AddCoach(coach2,"","");
        TeamMember teamManagertest = (TeamMember) f_tmtest;
        owner.AddTeamManager(teamManagertest,true,true,true,true);
        Assert.assertTrue(teamManagertest.RemoveCoach(coach));
        Assert.assertTrue(owner.RemoveCoach(coach2));
        Assert.assertFalse(owner.RemoveCoach(player));
    }

//    @Test
//    public void TestRemoveCoach2() {
//        //owner
//        Guest g = new Guest();
//        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
//        g.Login("1234", "glazer");
//        Fan f = database.getUser("glazer");
//        TeamMember owner = (TeamMember) f;
//
//        //team
//        TeamInfo manchesterUnited = new TeamInfo("Manchester United", 1878, true, "Manchester", owner);
//        owner.setTeam(manchesterUnited);
//
//        //Team Managertest
//        Guest t_tmtest = new Guest();
//        t_tmtest.SignUp("tim", "zbvcxbidan", "tmmutest", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
//        Fan f_tmtest = database.getUser("tmmutest");
//        TeamMember teamManagertest = (TeamMember) f_tmtest;
//        owner.AddTeamManager(teamManagertest,true,true,true,true);
//
//        teamManagertest.setTeam(manchesterUnited);
//
//        //coach
//        Guest guest1 = new Guest();
//        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
//        Fan fan1 = database.getUser("EliRah");
//        TeamMember coach = (TeamMember) fan1;
//
//        //player
//        Guest guest2 = new Guest();
//        guest2.SignUp("Avi", "Eliyaho", "aviEl", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
//        Fan fan2 = database.getUser("aviEl");
//        TeamMember player = (TeamMember) fan2;
//
//        //fan user
//        Guest guest3 = new Guest();
//        guest3.SignUp("Sami", "Akabai", "sami12", "fdgsfd", "4/6/1954", "samiii@gmail.com", "95555","","");
//        Fan fan = database.getUser("sami12");
//
//        Court court = new Court ("Camp Nou", "Barcelona", 99000);
//        manchesterUnited.addCourt(court);
//        database.addCourt("Camp Nou", "Barcelona", 99000);
//        owner.RemoveCoach((TeamMember)fan);
//        Assert.assertFalse(owner.RemoveCoach((TeamMember)fan));
//    }

    @Test
    public void TestAddCourt() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        //owner2
        Guest g2 = new Guest();
        g2.SignUp("gdhjklfg", "zbvcxbbjkidan", "glsdfazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g2.Login("12345", "glsdfazer");
        Fan f2 = database.getUser("glsdfazer");
        TeamMember owner2 = (TeamMember) f2;

        //owner3
        Guest g3 = new Guest();
        g3.SignUp("gdhjklfg", "zbvcxbbjkidan", "gggggg", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g3.Login("12345", "gggggg");
        Fan f3 = database.getUser("gggggg");
        TeamMember owner3 = (TeamMember) f3;

        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        //coach2
        Guest guestcoach2 = new Guest();
        guestcoach2.SignUp("yossi", "Rahamim", "yossi", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fancoach2 = database.getUser("yossi");
        TeamMember coach2 = (TeamMember) fancoach2;

        //player
        Guest guest2 = new Guest();
        guest2.SignUp("Avi", "Eliyaho", "aviEl", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan2 = database.getUser("aviEl");
        TeamMember player = (TeamMember) fan2;

        //player2
        Guest guestplayer2 = new Guest();
        guestplayer2.SignUp("roni", "Eliyaho", "roni", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fanplayer2 = database.getUser("roni");
        TeamMember player2 = (TeamMember) fanplayer2;

        //Team Manager1
        Guest t_tm = new Guest();
        t_tm.SignUp("tim", "zbvcxbidan", "tmmu", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm = database.getUser("tmmu");
        TeamMember teamManager = (TeamMember) f_tm;

        //Team Manager2
        Guest t_tm2 = new Guest();
        t_tm2.SignUp("tomm", "zzzzz", "tommy", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm2 = database.getUser("tommy");
        TeamMember teamManager2 = (TeamMember) f_tm2;

        //fan user
        Guest guest3 = new Guest();
        guest3.SignUp("Sami", "Akabai", "sami12", "fdgsfd", "4/6/1954", "samiii@gmail.com", "95555","","");
        Fan fan = database.getUser("sami12");

        TeamInfo manchesterUnited = new TeamInfo("Manchester United", 1878, true, "Manchester", owner);
        owner.setTeam(manchesterUnited);
        teamManager.setTeam(manchesterUnited);

        TeamInfo manchester = new TeamInfo("Manchester", 1879, true, "Manchest", owner2);
        owner2.setTeam(manchester);

        Court court = new Court ("Camp Nou", "Barcelona", 99000);
        Court court2 = new Court ("Camp", "Barcelona", 99000);
        database.addCourt("Camp Nou", "Barcelona", 99000);
        database.addCourt("Camp", "Barcelona", 99000);
        //Team Managertest
        Guest t_tmtest = new Guest();
        t_tmtest.SignUp("tim", "zbvcxbidan", "tmmutest", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tmtest = database.getUser("tmmutest");
        TeamMember teamManagertest = (TeamMember) f_tmtest;
        owner.AddTeamManager(teamManagertest,true,true,true,true);

        Assert.assertTrue(owner.AddCourt(court));
        Assert.assertTrue(owner.AddCourt(court2));

    }

    @Test
    public void TestRemoveCourt() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        //owner2
        Guest g2 = new Guest();
        g2.SignUp("gdhjklfg", "zbvcxbbjkidan", "glsdfazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g2.Login("12345", "glsdfazer");
        Fan f2 = database.getUser("glsdfazer");
        TeamMember owner2 = (TeamMember) f2;

        //owner3
        Guest g3 = new Guest();
        g3.SignUp("gdhjklfg", "zbvcxbbjkidan", "gggggg", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g3.Login("12345", "gggggg");
        Fan f3 = database.getUser("gggggg");
        TeamMember owner3 = (TeamMember) f3;

        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        //coach2
        Guest guestcoach2 = new Guest();
        guestcoach2.SignUp("yossi", "Rahamim", "yossi", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fancoach2 = database.getUser("yossi");
        TeamMember coach2 = (TeamMember) fancoach2;

        //player
        Guest guest2 = new Guest();
        guest2.SignUp("Avi", "Eliyaho", "aviEl", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan2 = database.getUser("aviEl");
        TeamMember player = (TeamMember) fan2;

        //player2
        Guest guestplayer2 = new Guest();
        guestplayer2.SignUp("roni", "Eliyaho", "roni", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fanplayer2 = database.getUser("roni");
        TeamMember player2 = (TeamMember) fanplayer2;

        //Team Manager1
        Guest t_tm = new Guest();
        t_tm.SignUp("tim", "zbvcxbidan", "tmmu", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm = database.getUser("tmmu");
        TeamMember teamManager = (TeamMember) f_tm;

        //Team Manager2
        Guest t_tm2 = new Guest();
        t_tm2.SignUp("tomm", "zzzzz", "tommy", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm2 = database.getUser("tommy");
        TeamMember teamManager2 = (TeamMember) f_tm2;

        //fan user
        Guest guest3 = new Guest();
        guest3.SignUp("Sami", "Akabai", "sami12", "fdgsfd", "4/6/1954", "samiii@gmail.com", "95555","","");
        Fan fan = database.getUser("sami12");

        TeamInfo manchesterUnited = new TeamInfo("Manchester United", 1878, true, "Manchester", owner);
        owner.setTeam(manchesterUnited);
        teamManager.setTeam(manchesterUnited);

        TeamInfo manchester = new TeamInfo("Manchester", 1879, true, "Manchest", owner2);
        owner2.setTeam(manchester);


        database.addCourt("Camp Nou", "Barcelona", 99000);
        database.addCourt("Camp", "Barcelona", 99000);

        Court court = database.getCourt("Camp Nou");
        Court court2 = database.getCourt("Camp");
        owner.AddCourt(court);
        Guest t_tmtest = new Guest();
        t_tmtest.SignUp("tim", "zbvcxbidan", "tmmutest", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tmtest = database.getUser("tmmutest");
        TeamMember teamManagertest = (TeamMember) f_tmtest;
        owner.AddTeamManager(teamManagertest,true,true,true,true);
        //teamManagertest.AddCourt(court2);
        Assert.assertTrue(owner.RemoveCourt(court));
        teamManagertest.RemoveCourt(court2);
        Assert.assertTrue(teamManagertest.RemoveCourt(court2));
    }

    @Test
    public void TestRemovePlayer() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        //owner2
        Guest g2 = new Guest();
        g2.SignUp("gdhjklfg", "zbvcxbbjkidan", "glsdfazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g2.Login("12345", "glsdfazer");
        Fan f2 = database.getUser("glsdfazer");
        TeamMember owner2 = (TeamMember) f2;

        //owner3
        Guest g3 = new Guest();
        g3.SignUp("gdhjklfg", "zbvcxbbjkidan", "gggggg", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g3.Login("12345", "gggggg");
        Fan f3 = database.getUser("gggggg");
        TeamMember owner3 = (TeamMember) f3;

        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        //coach2
        Guest guestcoach2 = new Guest();
        guestcoach2.SignUp("yossi", "Rahamim", "yossi", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fancoach2 = database.getUser("yossi");
        TeamMember coach2 = (TeamMember) fancoach2;

        //player
        Guest guest2 = new Guest();
        guest2.SignUp("Avi", "Eliyaho", "aviEl", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan2 = database.getUser("aviEl");
        TeamMember player = (TeamMember) fan2;

        //player2
        Guest guestplayer2 = new Guest();
        guestplayer2.SignUp("roni", "Eliyaho", "roni", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fanplayer2 = database.getUser("roni");
        TeamMember player2 = (TeamMember) fanplayer2;

        //Team Manager1
        Guest t_tm = new Guest();
        t_tm.SignUp("tim", "zbvcxbidan", "tmmu", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm = database.getUser("tmmu");
        TeamMember teamManager = (TeamMember) f_tm;

        //Team Manager2
        Guest t_tm2 = new Guest();
        t_tm2.SignUp("tomm", "zzzzz", "tommy", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm2 = database.getUser("tommy");
        TeamMember teamManager2 = (TeamMember) f_tm2;

        //fan user
        Guest guest3 = new Guest();
        guest3.SignUp("Sami", "Akabai", "sami12", "fdgsfd", "4/6/1954", "samiii@gmail.com", "95555","","");
        Fan fan = database.getUser("sami12");

        TeamInfo manchesterUnited = new TeamInfo("Manchester United", 1878, true, "Manchester", owner);
        owner.setTeam(manchesterUnited);
        owner.AddTeamManager(teamManager,true,true,true,true);
        teamManager.setTeam(manchesterUnited);

        TeamInfo manchester = new TeamInfo("Manchester", 1879, true, "Manchest", owner2);
        owner2.setTeam(manchester);

        Court court = new Court ("Camp Nou", "Barcelona", 99000);
        Court court2 = new Court ("Camp", "Barcelona", 99000);
        database.addCourt("Camp Nou", "Barcelona", 99000);
        database.addCourt("Camp", "Barcelona", 99000);

        //player
        Guest guest4 = new Guest();
        guest4.SignUp("Avi", "Eliyaho", "aviEll", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan4 = database.getUser("aviEll");
        TeamMember player4 = (TeamMember) fan4;
        owner.AddPlayer(player,"gk");
        player.setTeam(manchester);

        //player5
        Guest guest5 = new Guest();
        guest5.SignUp("aki", "avni", "aki", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan5 = database.getUser("aki");
        TeamMember player5 = (TeamMember) fan5;

        Assert.assertFalse(owner.RemovePlayer(coach));
        Assert.assertFalse(owner.RemovePlayer(player4));
        Assert.assertTrue(owner.RemovePlayer(player));
        Assert.assertFalse(owner.RemovePlayer(player5));
        Assert.assertFalse(teamManager.RemovePlayer(player4));

    }

    @Test (expected = ClassCastException.class)
    public void TestRemovePlayer2() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        //owner2
        Guest g2 = new Guest();
        g2.SignUp("gdhjklfg", "zbvcxbbjkidan", "glsdfazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g2.Login("12345", "glsdfazer");
        Fan f2 = database.getUser("glsdfazer");
        TeamMember owner2 = (TeamMember) f2;

        //owner3
        Guest g3 = new Guest();
        g3.SignUp("gdhjklfg", "zbvcxbbjkidan", "gggggg", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g3.Login("12345", "gggggg");
        Fan f3 = database.getUser("gggggg");
        TeamMember owner3 = (TeamMember) f3;

        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        //coach2
        Guest guestcoach2 = new Guest();
        guestcoach2.SignUp("yossi", "Rahamim", "yossi", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fancoach2 = database.getUser("yossi");
        TeamMember coach2 = (TeamMember) fancoach2;

        //player
        Guest guest2 = new Guest();
        guest2.SignUp("Avi", "Eliyaho", "aviEl", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan2 = database.getUser("aviEl");
        TeamMember player = (TeamMember) fan2;

        //player2
        Guest guestplayer2 = new Guest();
        guestplayer2.SignUp("roni", "Eliyaho", "roni", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fanplayer2 = database.getUser("roni");
        TeamMember player2 = (TeamMember) fanplayer2;

        //Team Manager1
        Guest t_tm = new Guest();
        t_tm.SignUp("tim", "zbvcxbidan", "tmmu", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm = database.getUser("tmmu");
        TeamMember teamManager = (TeamMember) f_tm;

        //Team Manager2
        Guest t_tm2 = new Guest();
        t_tm2.SignUp("tomm", "zzzzz", "tommy", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm2 = database.getUser("tommy");
        TeamMember teamManager2 = (TeamMember) f_tm2;

        //fan user
        Guest guest3 = new Guest();
        guest3.SignUp("Sami", "Akabai", "sami12", "fdgsfd", "4/6/1954", "samiii@gmail.com", "95555","","");
        Fan fan = database.getUser("sami12");

        TeamInfo manchesterUnited = new TeamInfo("Manchester United", 1878, true, "Manchester", owner);
        owner.setTeam(manchesterUnited);
        teamManager.setTeam(manchesterUnited);

        TeamInfo manchester = new TeamInfo("Manchester", 1879, true, "Manchest", owner2);
        owner2.setTeam(manchester);

        Court court = new Court ("Camp Nou", "Barcelona", 99000);
        Court court2 = new Court ("Camp", "Barcelona", 99000);
        database.addCourt("Camp Nou", "Barcelona", 99000);
        database.addCourt("Camp", "Barcelona", 99000);

        Assert.assertFalse(owner.RemovePlayer((TeamMember)fan));
    }

    @Test
    public void TestEditCoachInformation() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        //owner2
        Guest g2 = new Guest();
        g2.SignUp("gdhjklfg", "zbvcxbbjkidan", "glsdfazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g2.Login("12345", "glsdfazer");
        Fan f2 = database.getUser("glsdfazer");
        TeamMember owner2 = (TeamMember) f2;

        //owner3
        Guest g3 = new Guest();
        g3.SignUp("gdhjklfg", "zbvcxbbjkidan", "gggggg", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g3.Login("12345", "gggggg");
        Fan f3 = database.getUser("gggggg");
        TeamMember owner3 = (TeamMember) f3;

        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        //coach2
        Guest guestcoach2 = new Guest();
        guestcoach2.SignUp("yossi", "Rahamim", "yossi", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fancoach2 = database.getUser("yossi");
        TeamMember coach2 = (TeamMember) fancoach2;

        //player
        Guest guest2 = new Guest();
        guest2.SignUp("Avi", "Eliyaho", "aviEl", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan2 = database.getUser("aviEl");
        TeamMember player = (TeamMember) fan2;

        //player2
        Guest guestplayer2 = new Guest();
        guestplayer2.SignUp("roni", "Eliyaho", "roni", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fanplayer2 = database.getUser("roni");
        TeamMember player2 = (TeamMember) fanplayer2;

        //Team Manager1
        Guest t_tm = new Guest();
        t_tm.SignUp("tim", "zbvcxbidan", "tmmu", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm = database.getUser("tmmu");
        TeamMember teamManager = (TeamMember) f_tm;

        //Team Manager2
        Guest t_tm2 = new Guest();
        t_tm2.SignUp("tomm", "zzzzz", "tommy", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm2 = database.getUser("tommy");
        TeamMember teamManager2 = (TeamMember) f_tm2;

        //fan user
        Guest guest3 = new Guest();
        guest3.SignUp("Sami", "Akabai", "sami12", "fdgsfd", "4/6/1954", "samiii@gmail.com", "95555","","");
        Fan fan = database.getUser("sami12");

        TeamInfo manchesterUnited = new TeamInfo("Manchester United", 1878, true, "Manchester", owner);
        owner.setTeam(manchesterUnited);
        teamManager.setTeam(manchesterUnited);

        TeamInfo manchester = new TeamInfo("Manchester", 1879, true, "Manchest", owner2);
        owner2.setTeam(manchester);

        Court court = new Court ("Camp Nou", "Barcelona", 99000);
        Court court2 = new Court ("Camp", "Barcelona", 99000);
        database.addCourt("Camp Nou", "Barcelona", 99000);
        database.addCourt("Camp", "Barcelona", 99000);

        Guest t_tmtest = new Guest();
        t_tmtest.SignUp("tim", "zbvcxbidan", "tmmutest", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tmtest = database.getUser("tmmutest");
        TeamMember teamManagertest = (TeamMember) f_tmtest;
        owner.AddTeamManager(teamManagertest,true,true,true,true);
        owner.EditCoachInformation(coach,"sss", "ggg");
        Assert.assertEquals(coach.getPreparation(),"sss");
        Assert.assertEquals(coach.getTeamRole(),"ggg");
        teamManagertest.EditCoachInformation(coach2,"tt","zz");
        Assert.assertEquals(coach2.getPreparation(),"tt");
        Assert.assertEquals(coach2.getTeamRole(),"zz");
    }

    @Test
    public void TestEditPlayerInformation() {
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

        //player
        Guest guest2 = new Guest();
        guest2.SignUp("Avi", "Eliyaho", "aviEl", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan2 = database.getUser("aviEl");
        TeamMember player = (TeamMember) fan2;

        //player2
        Guest guestplayer2 = new Guest();
        guestplayer2.SignUp("roni", "Eliyaho", "roni", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fanplayer2 = database.getUser("roni");
        TeamMember player2 = (TeamMember) fanplayer2;

        //Team Manager1
        Guest t_tm = new Guest();
        t_tm.SignUp("tim", "zbvcxbidan", "tmmu", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm = database.getUser("tmmu");
        TeamMember teamManager = (TeamMember) f_tm;

        TeamInfo manchesterUnited = new TeamInfo("Manchester United", 1878, true, "Manchester", owner);
        owner.setTeam(manchesterUnited);
        teamManager.setTeam(manchesterUnited);

        Guest t_tmtest = new Guest();
        t_tmtest.SignUp("tim", "zbvcxbidan", "tmmutest", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tmtest = database.getUser("tmmutest");
        TeamMember teamManagertest = (TeamMember) f_tmtest;
        owner.AddTeamManager(teamManagertest,true,true,true,true);
        owner.EditPlayerInformation(player, "abcd");

        Assert.assertEquals(player.getRoleOnCourt(),"abcd");
        teamManagertest.EditPlayerInformation(player2,"aaa");
        Assert.assertEquals(player2.getRoleOnCourt(),"aaa");
    }

    @Test
    public void TestEditCourtInfo() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        //owner2
        Guest g2 = new Guest();
        g2.SignUp("gdhjklfg", "zbvcxbbjkidan", "glsdfazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g2.Login("12345", "glsdfazer");
        Fan f2 = database.getUser("glsdfazer");
        TeamMember owner2 = (TeamMember) f2;

        //owner3
        Guest g3 = new Guest();
        g3.SignUp("gdhjklfg", "zbvcxbbjkidan", "gggggg", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g3.Login("12345", "gggggg");
        Fan f3 = database.getUser("gggggg");
        TeamMember owner3 = (TeamMember) f3;

        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        //coach2
        Guest guestcoach2 = new Guest();
        guestcoach2.SignUp("yossi", "Rahamim", "yossi", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fancoach2 = database.getUser("yossi");
        TeamMember coach2 = (TeamMember) fancoach2;

        //player
        Guest guest2 = new Guest();
        guest2.SignUp("Avi", "Eliyaho", "aviEl", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan2 = database.getUser("aviEl");
        TeamMember player = (TeamMember) fan2;

        //player2
        Guest guestplayer2 = new Guest();
        guestplayer2.SignUp("roni", "Eliyaho", "roni", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fanplayer2 = database.getUser("roni");
        TeamMember player2 = (TeamMember) fanplayer2;

        //Team Manager1
        Guest t_tm = new Guest();
        t_tm.SignUp("tim", "zbvcxbidan", "tmmu", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm = database.getUser("tmmu");
        TeamMember teamManager = (TeamMember) f_tm;

        //Team Manager2
        Guest t_tm2 = new Guest();
        t_tm2.SignUp("tomm", "zzzzz", "tommy", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm2 = database.getUser("tommy");
        TeamMember teamManager2 = (TeamMember) f_tm2;

        //fan user
        Guest guest3 = new Guest();
        guest3.SignUp("Sami", "Akabai", "sami12", "fdgsfd", "4/6/1954", "samiii@gmail.com", "95555","","");
        Fan fan = database.getUser("sami12");

        TeamInfo manchesterUnited = new TeamInfo("Manchester United", 1878, true, "Manchester", owner);
        owner.setTeam(manchesterUnited);
        teamManager.setTeam(manchesterUnited);

        TeamInfo manchester = new TeamInfo("Manchester", 1879, true, "Manchest", owner2);
        owner2.setTeam(manchester);

        Court court = new Court ("Camp Nou", "Barcelona", 99000);
        Court court2 = new Court ("Camp", "Barcelona", 99000);
        database.addCourt("Camp Nou", "Barcelona", 99000);
        database.addCourt("Camp", "Barcelona", 99000);

        Guest t_tmtest = new Guest();
        t_tmtest.SignUp("tim", "zbvcxbidan", "tmmutest", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tmtest = database.getUser("tmmutest");
        TeamMember teamManagertest = (TeamMember) f_tmtest;
        owner.AddTeamManager(teamManagertest,true,true,true,true);
        owner.EditCourtInfo(court,"bla", 200);
        Assert.assertEquals(court.getCourtName(),"bla");
        Assert.assertEquals(court.getCourtCapacity(),200);
        teamManagertest.EditCourtInfo(court2,"cour", 100);
        Assert.assertEquals(court2.getCourtName(),"cour");
        Assert.assertEquals(court2.getCourtCapacity(),100);
    }

    @Test
    public void TestAddOwner() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        //owner2
        Guest g2 = new Guest();
        g2.SignUp("gdhjklfg", "zbvcxbbjkidan", "glsdfazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g2.Login("12345", "glsdfazer");
        Fan f2 = database.getUser("glsdfazer");
        TeamMember owner2 = (TeamMember) f2;

        //owner3
        Guest g3 = new Guest();
        g3.SignUp("gdhjklfg", "zbvcxbbjkidan", "gggggg", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g3.Login("12345", "gggggg");
        Fan f3 = database.getUser("gggggg");
        TeamMember owner3 = (TeamMember) f3;

        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        //coach2
        Guest guestcoach2 = new Guest();
        guestcoach2.SignUp("yossi", "Rahamim", "yossi", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fancoach2 = database.getUser("yossi");
        TeamMember coach2 = (TeamMember) fancoach2;

        //player
        Guest guest2 = new Guest();
        guest2.SignUp("Avi", "Eliyaho", "aviEl", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan2 = database.getUser("aviEl");
        TeamMember player = (TeamMember) fan2;

        //player2
        Guest guestplayer2 = new Guest();
        guestplayer2.SignUp("roni", "Eliyaho", "roni", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fanplayer2 = database.getUser("roni");
        TeamMember player2 = (TeamMember) fanplayer2;

        //Team Manager1
        Guest t_tm = new Guest();
        t_tm.SignUp("tim", "zbvcxbidan", "tmmu", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm = database.getUser("tmmu");
        TeamMember teamManager = (TeamMember) f_tm;

        //Team Manager2
        Guest t_tm2 = new Guest();
        t_tm2.SignUp("tomm", "zzzzz", "tommy", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm2 = database.getUser("tommy");
        TeamMember teamManager2 = (TeamMember) f_tm2;

        //fan user
        Guest guest3 = new Guest();
        guest3.SignUp("Sami", "Akabai", "sami12", "fdgsfd", "4/6/1954", "samiii@gmail.com", "95555","","");
        Fan fan = database.getUser("sami12");

        TeamInfo manchesterUnited = new TeamInfo("Manchester United", 1878, true, "Manchester", owner);
        owner.setTeam(manchesterUnited);
        teamManager.setTeam(manchesterUnited);

        TeamInfo manchester = new TeamInfo("Manchester", 1879, true, "Manchest", owner2);
        owner2.setTeam(manchester);

        Court court = new Court ("Camp Nou", "Barcelona", 99000);
        Court court2 = new Court ("Camp", "Barcelona", 99000);
        database.addCourt("Camp Nou", "Barcelona", 99000);
        database.addCourt("Camp", "Barcelona", 99000);

        Assert.assertTrue(owner.AddOwner(owner3));
        Assert.assertFalse(player.AddOwner(owner3));
        Assert.assertFalse(owner.AddOwner(owner2));
    }

    @Test (expected = ClassCastException.class)
    public void AddOwnerUsetIsntOwnerTest() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        //owner2
        Guest g2 = new Guest();
        g2.SignUp("gdhjklfg", "zbvcxbbjkidan", "glsdfazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g2.Login("12345", "glsdfazer");
        Fan f2 = database.getUser("glsdfazer");
        TeamMember owner2 = (TeamMember) f2;

        //owner3
        Guest g3 = new Guest();
        g3.SignUp("gdhjklfg", "zbvcxbbjkidan", "gggggg", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g3.Login("12345", "gggggg");
        Fan f3 = database.getUser("gggggg");
        TeamMember owner3 = (TeamMember) f3;

        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        //coach2
        Guest guestcoach2 = new Guest();
        guestcoach2.SignUp("yossi", "Rahamim", "yossi", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fancoach2 = database.getUser("yossi");
        TeamMember coach2 = (TeamMember) fancoach2;

        //player
        Guest guest2 = new Guest();
        guest2.SignUp("Avi", "Eliyaho", "aviEl", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan2 = database.getUser("aviEl");
        TeamMember player = (TeamMember) fan2;

        //player2
        Guest guestplayer2 = new Guest();
        guestplayer2.SignUp("roni", "Eliyaho", "roni", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fanplayer2 = database.getUser("roni");
        TeamMember player2 = (TeamMember) fanplayer2;

        //Team Manager1
        Guest t_tm = new Guest();
        t_tm.SignUp("tim", "zbvcxbidan", "tmmu", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm = database.getUser("tmmu");
        TeamMember teamManager = (TeamMember) f_tm;

        //Team Manager2
        Guest t_tm2 = new Guest();
        t_tm2.SignUp("tomm", "zzzzz", "tommy", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm2 = database.getUser("tommy");
        TeamMember teamManager2 = (TeamMember) f_tm2;

        //fan user
        Guest guest3 = new Guest();
        guest3.SignUp("Sami", "Akabai", "sami12", "fdgsfd", "4/6/1954", "samiii@gmail.com", "95555","","");
        Fan fan = database.getUser("sami12");

        TeamInfo manchesterUnited = new TeamInfo("Manchester United", 1878, true, "Manchester", owner);
        owner.setTeam(manchesterUnited);
        teamManager.setTeam(manchesterUnited);

        TeamInfo manchester = new TeamInfo("Manchester", 1879, true, "Manchest", owner2);
        owner2.setTeam(manchester);

        Court court = new Court ("Camp Nou", "Barcelona", 99000);
        Court court2 = new Court ("Camp", "Barcelona", 99000);
        database.addCourt("Camp Nou", "Barcelona", 99000);
        database.addCourt("Camp", "Barcelona", 99000);

        Assert.assertFalse(owner.AddOwner((TeamMember)fan));
    }

    @Test
    public void TestAddTeamManager() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        //owner2
        Guest g2 = new Guest();
        g2.SignUp("gdhjklfg", "zbvcxbbjkidan", "glsdfazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g2.Login("12345", "glsdfazer");
        Fan f2 = database.getUser("glsdfazer");
        TeamMember owner2 = (TeamMember) f2;

        //owner3
        Guest g3 = new Guest();
        g3.SignUp("gdhjklfg", "zbvcxbbjkidan", "gggggg", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g3.Login("12345", "gggggg");
        Fan f3 = database.getUser("gggggg");
        TeamMember owner3 = (TeamMember) f3;

        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        //coach2
        Guest guestcoach2 = new Guest();
        guestcoach2.SignUp("yossi", "Rahamim", "yossi", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fancoach2 = database.getUser("yossi");
        TeamMember coach2 = (TeamMember) fancoach2;

        //player
        Guest guest2 = new Guest();
        guest2.SignUp("Avi", "Eliyaho", "aviEl", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan2 = database.getUser("aviEl");
        TeamMember player = (TeamMember) fan2;

        //player2
        Guest guestplayer2 = new Guest();
        guestplayer2.SignUp("roni", "Eliyaho", "roni", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fanplayer2 = database.getUser("roni");
        TeamMember player2 = (TeamMember) fanplayer2;

        //Team Manager1
        Guest t_tm = new Guest();
        t_tm.SignUp("tim", "zbvcxbidan", "tmmu", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm = database.getUser("tmmu");
        TeamMember teamManager = (TeamMember) f_tm;

        //Team Manager2
        Guest t_tm2 = new Guest();
        t_tm2.SignUp("tomm", "zzzzz", "tommy", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm2 = database.getUser("tommy");
        TeamMember teamManager2 = (TeamMember) f_tm2;

        //fan user
        Guest guest3 = new Guest();
        guest3.SignUp("Sami", "Akabai", "sami12", "fdgsfd", "4/6/1954", "samiii@gmail.com", "95555","","");
        Fan fan = database.getUser("sami12");

        TeamInfo manchesterUnited = new TeamInfo("Manchester United", 1878, true, "Manchester", owner);
        owner.setTeam(manchesterUnited);
        teamManager.setTeam(manchesterUnited);

        TeamInfo manchester = new TeamInfo("Manchester", 1879, true, "Manchest", owner2);
        owner2.setTeam(manchester);

        Court court = new Court ("Camp Nou", "Barcelona", 99000);
        Court court2 = new Court ("Camp", "Barcelona", 99000);
        database.addCourt("Camp Nou", "Barcelona", 99000);
        database.addCourt("Camp", "Barcelona", 99000);


        //Team Manager1
        Guest t_tm1 = new Guest();
        t_tm1.SignUp("tim", "zbvcxbidan", "tmmuy", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm1 = database.getUser("tmmuy");
        TeamMember teamManager1 = (TeamMember) f_tm1;

        Guest t_tmtest = new Guest();
        t_tmtest.SignUp("tim", "zbvcxbidan", "tmmutest", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tmtest = database.getUser("tmmutest");
        TeamMember teamManagertest = (TeamMember) f_tmtest;
        owner.AddTeamManager(teamManagertest,true,true,true,true);

        teamManager.setTeam(manchesterUnited);

        Assert.assertFalse(owner.AddTeamManager(owner, true, true, false, false));
        Assert.assertTrue(owner.AddTeamManager(teamManager1, true, true, true, true));
        Assert.assertTrue(teamManagertest.AddTeamManager(teamManager2, true,true,true,true));
    }

    @Test (expected = ClassCastException.class)
    public void TestAddTeamManager2() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        //owner2
        Guest g2 = new Guest();
        g2.SignUp("gdhjklfg", "zbvcxbbjkidan", "glsdfazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g2.Login("12345", "glsdfazer");
        Fan f2 = database.getUser("glsdfazer");
        TeamMember owner2 = (TeamMember) f2;

        //owner3
        Guest g3 = new Guest();
        g3.SignUp("gdhjklfg", "zbvcxbbjkidan", "gggggg", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g3.Login("12345", "gggggg");
        Fan f3 = database.getUser("gggggg");
        TeamMember owner3 = (TeamMember) f3;

        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        //coach2
        Guest guestcoach2 = new Guest();
        guestcoach2.SignUp("yossi", "Rahamim", "yossi", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fancoach2 = database.getUser("yossi");
        TeamMember coach2 = (TeamMember) fancoach2;

        //player
        Guest guest2 = new Guest();
        guest2.SignUp("Avi", "Eliyaho", "aviEl", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan2 = database.getUser("aviEl");
        TeamMember player = (TeamMember) fan2;

        //player2
        Guest guestplayer2 = new Guest();
        guestplayer2.SignUp("roni", "Eliyaho", "roni", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fanplayer2 = database.getUser("roni");
        TeamMember player2 = (TeamMember) fanplayer2;

        //Team Manager1
        Guest t_tm = new Guest();
        t_tm.SignUp("tim", "zbvcxbidan", "tmmu", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm = database.getUser("tmmu");
        TeamMember teamManager = (TeamMember) f_tm;

        //Team Manager2
        Guest t_tm2 = new Guest();
        t_tm2.SignUp("tomm", "zzzzz", "tommy", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm2 = database.getUser("tommy");
        TeamMember teamManager2 = (TeamMember) f_tm2;

        //fan user
        Guest guest3 = new Guest();
        guest3.SignUp("Sami", "Akabai", "sami12", "fdgsfd", "4/6/1954", "samiii@gmail.com", "95555","","");
        Fan fan = database.getUser("sami12");

        TeamInfo manchesterUnited = new TeamInfo("Manchester United", 1878, true, "Manchester", owner);
        owner.setTeam(manchesterUnited);
        teamManager.setTeam(manchesterUnited);

        TeamInfo manchester = new TeamInfo("Manchester", 1879, true, "Manchest", owner2);
        owner2.setTeam(manchester);

        Court court = new Court ("Camp Nou", "Barcelona", 99000);
        Court court2 = new Court ("Camp", "Barcelona", 99000);
        database.addCourt("Camp Nou", "Barcelona", 99000);
        database.addCourt("Camp", "Barcelona", 99000);

        Assert.assertFalse(owner.AddTeamManager((TeamMember)fan,true,true,true,true));
    }

    @Test
    public void TestRemoveOwner() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        //owner2
        Guest g2 = new Guest();
        g2.SignUp("gdhjklfg", "zbvcxbbjkidan", "glsdfazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g2.Login("12345", "glsdfazer");
        Fan f2 = database.getUser("glsdfazer");
        TeamMember owner2 = (TeamMember) f2;

        //owner3
        Guest g3 = new Guest();
        g3.SignUp("gdhjklfg", "zbvcxbbjkidan", "gggggg", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g3.Login("12345", "gggggg");
        Fan f3 = database.getUser("gggggg");
        TeamMember owner3 = (TeamMember) f3;

        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        //coach2
        Guest guestcoach2 = new Guest();
        guestcoach2.SignUp("yossi", "Rahamim", "yossi", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fancoach2 = database.getUser("yossi");
        TeamMember coach2 = (TeamMember) fancoach2;

        //player
        Guest guest2 = new Guest();
        guest2.SignUp("Avi", "Eliyaho", "aviEl", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan2 = database.getUser("aviEl");
        TeamMember player = (TeamMember) fan2;

        //player2
        Guest guestplayer2 = new Guest();
        guestplayer2.SignUp("roni", "Eliyaho", "roni", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fanplayer2 = database.getUser("roni");
        TeamMember player2 = (TeamMember) fanplayer2;

        //Team Manager1
        Guest t_tm = new Guest();
        t_tm.SignUp("tim", "zbvcxbidan", "tmmu", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm = database.getUser("tmmu");
        TeamMember teamManager = (TeamMember) f_tm;

        //Team Manager2
        Guest t_tm2 = new Guest();
        t_tm2.SignUp("tomm", "zzzzz", "tommy", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm2 = database.getUser("tommy");
        TeamMember teamManager2 = (TeamMember) f_tm2;

        //fan user
        Guest guest3 = new Guest();
        guest3.SignUp("Sami", "Akabai", "sami12", "fdgsfd", "4/6/1954", "samiii@gmail.com", "95555","","");
        Fan fan = database.getUser("sami12");

        TeamInfo manchesterUnited = new TeamInfo("Manchester United", 1878, true, "Manchester", owner);
        owner.setTeam(manchesterUnited);
        teamManager.setTeam(manchesterUnited);

        TeamInfo manchester = new TeamInfo("Manchester", 1879, true, "Manchest", owner2);
        owner2.setTeam(manchester);

        Court court = new Court ("Camp Nou", "Barcelona", 99000);
        Court court2 = new Court ("Camp", "Barcelona", 99000);
        database.addCourt("Camp Nou", "Barcelona", 99000);
        database.addCourt("Camp", "Barcelona", 99000);

        owner.AddOwner(owner3);
        Assert.assertTrue(owner.RemoveOwner(owner3));
        Assert.assertFalse(owner.RemoveOwner(coach));
    }

    @Test (expected = ClassCastException.class)
    public void TestRemoveTeamManager2() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        //team
        TeamInfo manchesterUnited = new TeamInfo("Manchester United", 1878, true, "Manchester", owner);
        owner.setTeam(manchesterUnited);

        //Team Managertest
        Guest t_tmtest = new Guest();
        t_tmtest.SignUp("tim", "zbvcxbidan", "tmmutest", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tmtest = database.getUser("tmmutest");
        TeamMember teamManagertest = (TeamMember) f_tmtest;
        owner.AddTeamManager(teamManagertest,true,true,true,true);

        teamManagertest.setTeam(manchesterUnited);

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

        //fan user
        Guest guest3 = new Guest();
        guest3.SignUp("Sami", "Akabai", "sami12", "fdgsfd", "4/6/1954", "samiii@gmail.com", "95555","","");
        Fan fan = database.getUser("sami12");

        Court court = new Court ("Camp Nou", "Barcelona", 99000);
        manchesterUnited.addCourt(court);
        database.addCourt("Camp Nou", "Barcelona", 99000);

        Assert.assertFalse(owner.RemoveTeamManager((TeamMember)fan));
    }

    @Test
    public void TestCloseTeam() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        //owner2
        Guest g2 = new Guest();
        g2.SignUp("gdhjklfg", "zbvcxbbjkidan", "glsdfazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g2.Login("12345", "glsdfazer");
        Fan f2 = database.getUser("glsdfazer");
        TeamMember owner2 = (TeamMember) f2;

        //owner3
        Guest g3 = new Guest();
        g3.SignUp("gdhjklfg", "zbvcxbbjkidan", "gggggg", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g3.Login("12345", "gggggg");
        Fan f3 = database.getUser("gggggg");
        TeamMember owner3 = (TeamMember) f3;

        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        //coach2
        Guest guestcoach2 = new Guest();
        guestcoach2.SignUp("yossi", "Rahamim", "yossi", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fancoach2 = database.getUser("yossi");
        TeamMember coach2 = (TeamMember) fancoach2;

        //player
        Guest guest2 = new Guest();
        guest2.SignUp("Avi", "Eliyaho", "aviEl", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan2 = database.getUser("aviEl");
        TeamMember player = (TeamMember) fan2;

        //player2
        Guest guestplayer2 = new Guest();
        guestplayer2.SignUp("roni", "Eliyaho", "roni", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fanplayer2 = database.getUser("roni");
        TeamMember player2 = (TeamMember) fanplayer2;

        //Team Manager1
        Guest t_tm = new Guest();
        t_tm.SignUp("tim", "zbvcxbidan", "tmmu", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm = database.getUser("tmmu");
        TeamMember teamManager = (TeamMember) f_tm;

        //Team Manager2
        Guest t_tm2 = new Guest();
        t_tm2.SignUp("tomm", "zzzzz", "tommy", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm2 = database.getUser("tommy");
        TeamMember teamManager2 = (TeamMember) f_tm2;

        //fan user
        Guest guest3 = new Guest();
        guest3.SignUp("Sami", "Akabai", "sami12", "fdgsfd", "4/6/1954", "samiii@gmail.com", "95555","","");
        Fan fan = database.getUser("sami12");

        database.addTeam("Manchester United", 1879, true, "Manchest", owner.getUserName(),"");
        database.addTeam("Manchester", 1879, true, "Manchest", owner2.getUserName(),"");
        TeamInfo manchesterUnited = database.getTeam("Manchester United");
        TeamInfo manchester = database.getTeam("Manchester");

        owner.setTeam(manchesterUnited);
        teamManager.setTeam(manchesterUnited);

        owner2.setTeam(manchester);

        Court court = new Court ("Camp Nou", "Barcelona", 99000);
        Court court2 = new Court ("Camp", "Barcelona", 99000);
        database.addCourt("Camp Nou", "Barcelona", 99000);
        database.addCourt("Camp", "Barcelona", 99000);

        database.addTeam("Manchester", 1879, true, "Manchest", owner2.getUserName(),"");
        owner.CloseTeam();
        Assert.assertFalse(database.getTeam("Manchester United").isTeamActiveStatus());
        Assert.assertTrue(owner.CloseTeam());
        Assert.assertFalse(teamManager.CloseTeam());
    }

    @Test
    public void TestOpenTeam() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        //owner2
        Guest g2 = new Guest();
        g2.SignUp("gdhjklfg", "zbvcxbbjkidan", "glsdfazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g2.Login("12345", "glsdfazer");
        Fan f2 = database.getUser("glsdfazer");
        TeamMember owner2 = (TeamMember) f2;

        //owner3
        Guest g3 = new Guest();
        g3.SignUp("gdhjklfg", "zbvcxbbjkidan", "gggggg", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g3.Login("12345", "gggggg");
        Fan f3 = database.getUser("gggggg");
        TeamMember owner3 = (TeamMember) f3;

        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        //coach2
        Guest guestcoach2 = new Guest();
        guestcoach2.SignUp("yossi", "Rahamim", "yossi", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fancoach2 = database.getUser("yossi");
        TeamMember coach2 = (TeamMember) fancoach2;

        //player
        Guest guest2 = new Guest();
        guest2.SignUp("Avi", "Eliyaho", "aviEl", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan2 = database.getUser("aviEl");
        TeamMember player = (TeamMember) fan2;

        //player2
        Guest guestplayer2 = new Guest();
        guestplayer2.SignUp("roni", "Eliyaho", "roni", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fanplayer2 = database.getUser("roni");
        TeamMember player2 = (TeamMember) fanplayer2;

        //Team Manager1
        Guest t_tm = new Guest();
        t_tm.SignUp("tim", "zbvcxbidan", "tmmu", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm = database.getUser("tmmu");
        TeamMember teamManager = (TeamMember) f_tm;

        //Team Manager2
        Guest t_tm2 = new Guest();
        t_tm2.SignUp("tomm", "zzzzz", "tommy", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm2 = database.getUser("tommy");
        TeamMember teamManager2 = (TeamMember) f_tm2;

        //fan user
        Guest guest3 = new Guest();
        guest3.SignUp("Sami", "Akabai", "sami12", "fdgsfd", "4/6/1954", "samiii@gmail.com", "95555","","");
        Fan fan = database.getUser("sami12");

        database.addTeam("Manchester United", 1879, true, "Manchest", owner.getUserName(),"");
        database.addTeam("Manchester", 1879, true, "Manchest", owner2.getUserName(),"");
        TeamInfo manchesterUnited = database.getTeam("Manchester United");
        TeamInfo manchester = database.getTeam("Manchester");
        owner.setTeam(manchesterUnited);
        teamManager.setTeam(manchesterUnited);

        //TeamInfo manchester = new TeamInfo("Manchester", 1879, true, "Manchest", owner2);
        owner2.setTeam(manchester);

        Court court = new Court ("Camp Nou", "Barcelona", 99000);
        Court court2 = new Court ("Camp", "Barcelona", 99000);
        database.addCourt("Camp Nou", "Barcelona", 99000);
        database.addCourt("Camp", "Barcelona", 99000);


        owner.CloseTeam();
        owner.OpenTeam();
        Assert.assertTrue(database.getTeam("Manchester United").isTeamActiveStatus());
        Assert.assertTrue(owner.OpenTeam());
        Assert.assertFalse(teamManager.OpenTeam());
    }

    @Test
    public void TestAddIncoming() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        //owner2
        Guest g2 = new Guest();
        g2.SignUp("gdhjklfg", "zbvcxbbjkidan", "glsdfazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g2.Login("12345", "glsdfazer");
        Fan f2 = database.getUser("glsdfazer");
        TeamMember owner2 = (TeamMember) f2;

        //owner3
        Guest g3 = new Guest();
        g3.SignUp("gdhjklfg", "zbvcxbbjkidan", "gggggg", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g3.Login("12345", "gggggg");
        Fan f3 = database.getUser("gggggg");
        TeamMember owner3 = (TeamMember) f3;

        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        //coach2
        Guest guestcoach2 = new Guest();
        guestcoach2.SignUp("yossi", "Rahamim", "yossi", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fancoach2 = database.getUser("yossi");
        TeamMember coach2 = (TeamMember) fancoach2;

        //player
        Guest guest2 = new Guest();
        guest2.SignUp("Avi", "Eliyaho", "aviEl", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan2 = database.getUser("aviEl");
        TeamMember player = (TeamMember) fan2;

        //player2
        Guest guestplayer2 = new Guest();
        guestplayer2.SignUp("roni", "Eliyaho", "roni", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fanplayer2 = database.getUser("roni");
        TeamMember player2 = (TeamMember) fanplayer2;

        //Team Manager1
        Guest t_tm = new Guest();
        t_tm.SignUp("tim", "zbvcxbidan", "tmmu", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm = database.getUser("tmmu");
        TeamMember teamManager = (TeamMember) f_tm;

        //Team Manager2
        Guest t_tm2 = new Guest();
        t_tm2.SignUp("tomm", "zzzzz", "tommy", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm2 = database.getUser("tommy");
        TeamMember teamManager2 = (TeamMember) f_tm2;

        //fan user
        Guest guest3 = new Guest();
        guest3.SignUp("Sami", "Akabai", "sami12", "fdgsfd", "4/6/1954", "samiii@gmail.com", "95555","","");
        Fan fan = database.getUser("sami12");

        TeamInfo manchesterUnited = new TeamInfo("Manchester United", 1878, true, "Manchester", owner);
        owner.setTeam(manchesterUnited);
        teamManager.setTeam(manchesterUnited);

        TeamInfo manchester = new TeamInfo("Manchester", 1879, true, "Manchest", owner2);
        owner2.setTeam(manchester);

        Court court = new Court ("Camp Nou", "Barcelona", 99000);
        Court court2 = new Court ("Camp", "Barcelona", 99000);
        database.addCourt("Camp Nou", "Barcelona", 99000);
        database.addCourt("Camp", "Barcelona", 99000);

        Assert.assertTrue(owner.AddIncoming(6000, "Sell Player Robert" ));
    }

    @Test
    public void TestAddExpense() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        //owner2
        Guest g2 = new Guest();
        g2.SignUp("gdhjklfg", "zbvcxbbjkidan", "glsdfazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g2.Login("12345", "glsdfazer");
        Fan f2 = database.getUser("glsdfazer");
        TeamMember owner2 = (TeamMember) f2;

        //owner3
        Guest g3 = new Guest();
        g3.SignUp("gdhjklfg", "zbvcxbbjkidan", "gggggg", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g3.Login("12345", "gggggg");
        Fan f3 = database.getUser("gggggg");
        TeamMember owner3 = (TeamMember) f3;

        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        //coach2
        Guest guestcoach2 = new Guest();
        guestcoach2.SignUp("yossi", "Rahamim", "yossi", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fancoach2 = database.getUser("yossi");
        TeamMember coach2 = (TeamMember) fancoach2;

        //player
        Guest guest2 = new Guest();
        guest2.SignUp("Avi", "Eliyaho", "aviEl", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan2 = database.getUser("aviEl");
        TeamMember player = (TeamMember) fan2;

        //player2
        Guest guestplayer2 = new Guest();
        guestplayer2.SignUp("roni", "Eliyaho", "roni", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fanplayer2 = database.getUser("roni");
        TeamMember player2 = (TeamMember) fanplayer2;

        //Team Manager1
        Guest t_tm = new Guest();
        t_tm.SignUp("tim", "zbvcxbidan", "tmmu", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm = database.getUser("tmmu");
        TeamMember teamManager = (TeamMember) f_tm;

        //Team Manager2
        Guest t_tm2 = new Guest();
        t_tm2.SignUp("tomm", "zzzzz", "tommy", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm2 = database.getUser("tommy");
        TeamMember teamManager2 = (TeamMember) f_tm2;

        //fan user
        Guest guest3 = new Guest();
        guest3.SignUp("Sami", "Akabai", "sami12", "fdgsfd", "4/6/1954", "samiii@gmail.com", "95555","","");
        Fan fan = database.getUser("sami12");

        TeamInfo manchesterUnited = new TeamInfo("Manchester United", 1878, true, "Manchester", owner);
        owner.setTeam(manchesterUnited);
        teamManager.setTeam(manchesterUnited);

        TeamInfo manchester = new TeamInfo("Manchester", 1879, true, "Manchest", owner2);
        owner2.setTeam(manchester);

        Court court = new Court ("Camp Nou", "Barcelona", 99000);
        Court court2 = new Court ("Camp", "Barcelona", 99000);
        database.addCourt("Camp Nou", "Barcelona", 99000);
        database.addCourt("Camp", "Barcelona", 99000);

        Assert.assertTrue(owner.AddExpense(2000, "Buy Player Gal" ));
    }

    @Test (expected = IllegalArgumentException.class)
    public void AddIncomingUserNotOwnerTest() {
        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        Assert.assertFalse(coach.AddIncoming(500, "aaa"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void AddIncomingAmountNotNegativeTest() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        Assert.assertFalse(owner.AddIncoming(-500, "aaa"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void AddIncomingAmountNotZeroTest() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        Assert.assertFalse(owner.AddIncoming(0, "aaa"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullCreateNewTeamTest(){
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;
        owner.createNewTeam(null,1980,true,"test");
    }

    @Test (expected = IllegalArgumentException.class)
    public void AddExpenseUserNotOwnerTest() {
        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        Assert.assertFalse(coach.AddExpense(500, "aaa"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void AddExpenseAmountNotNegativeTest() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        Assert.assertFalse(owner.AddExpense(-500, "aaa"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void AddExpenseAmountNotZeroTest() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        Assert.assertFalse(owner.AddExpense(0, "aaa"));
    }

    @Test
    public void createNewTeamTest(){
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;
        owner.createNewTeam("test",1980,true,"test");
        database.addTeam("test",1980,true,"test",owner.getUserName(),"");
        Assert.assertNotNull(database.getTeam("test"));
        Assert.assertTrue(owner.getTeam().getTeamName().equals("test"));
    }

    @Test
    public void TestremoveAllRelatedUsers() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        //owner2
        Guest g2 = new Guest();
        g2.SignUp("gdhjklfg", "zbvcxbbjkidan", "glsdfazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g2.Login("12345", "glsdfazer");
        Fan f2 = database.getUser("glsdfazer");
        TeamMember owner2 = (TeamMember) f2;

        //owner3
        Guest g3 = new Guest();
        g3.SignUp("gdhjklfg", "zbvcxbbjkidan", "gggggg", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g3.Login("12345", "gggggg");
        Fan f3 = database.getUser("gggggg");
        TeamMember owner3 = (TeamMember) f3;

        //coach
        Guest guest1 = new Guest();
        guest1.SignUp("Eli", "Rahamim", "EliRah", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fan1 = database.getUser("EliRah");
        TeamMember coach = (TeamMember) fan1;

        //coach2
        Guest guestcoach2 = new Guest();
        guestcoach2.SignUp("yossi", "Rahamim", "yossi", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fancoach2 = database.getUser("yossi");
        TeamMember coach2 = (TeamMember) fancoach2;

        //coach3
        Guest guestcoach3 = new Guest();
        guestcoach3.SignUp("yossi", "Rahamim", "erez", "fdgsfd", "4/6/1954", "asda@gmail.com", "4321", "Coach","Head Coach");
        Fan fancoach3 = database.getUser("erez");
        TeamMember coach3 = (TeamMember) fancoach3;

        //player
        Guest guest2 = new Guest();
        guest2.SignUp("Avi", "Eliyaho", "aviEl", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fan2 = database.getUser("aviEl");
        TeamMember player = (TeamMember) fan2;

        //player2
        Guest guestplayer2 = new Guest();
        guestplayer2.SignUp("roni", "Eliyaho", "roni", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fanplayer2 = database.getUser("roni");
        TeamMember player2 = (TeamMember) fanplayer2;

        //player3
        Guest guestplayer3 = new Guest();
        guestplayer3.SignUp("rontti", "Eliyattho", "gal", "fdgsfd", "4/6/1954", "asda@gmail.com", "9876", "Player","CF");
        Fan fanplayer3 = database.getUser("gal");
        TeamMember player3 = (TeamMember) fanplayer3;

        //Team Manager1
        Guest t_tm = new Guest();
        t_tm.SignUp("tim", "zbvcxbidan", "tmmu", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm = database.getUser("tmmu");
        TeamMember teamManager = (TeamMember) f_tm;

        //Team Manager2
        Guest t_tm2 = new Guest();
        t_tm2.SignUp("tomm", "zzzzz", "tommy", "fdada", "4/6/1954", "lksa@gmail.com", "6789", "TeamManager","");
        Fan f_tm2 = database.getUser("tommy");
        TeamMember teamManager2 = (TeamMember) f_tm2;

        TeamInfo manchesterUnited = new TeamInfo("Manchester United", 1878, true, "Manchester", owner);
        owner.setTeam(manchesterUnited);

        owner.AddOwner(owner3);
        owner3.AddTeamManager(teamManager, true, true, true,true);
        owner3.AddTeamManager(teamManager2, true, true, true,true);
        teamManager.AddCoach(coach,"","");
        teamManager.AddCoach(coach2,"","");
        owner3.AddPlayer(player, "g");
        owner3.AddPlayer(player2, "r");
        owner3.AddPlayer(player3, "r");
        teamManager2.AddCoach(coach3,"","");

        Assert.assertTrue(owner.RemoveOwner(owner3));
    }

    @Test
    public void TestregisterTheTeamToLeague() {
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
        associationUser.addNewLeagueToSeason(2021,"PremierLeague",new Date().toString(),3,0,
                1,true,false,12,12);

        League pl = database.getLeagueByName("PremierLeague");

        //------------ManUtd---------------------------------------------------------------------------
        //==========================================================================================

        //owner
        Guest g = new Guest();
        g.SignUp("gdfg","zbvcxbidan","gggglazer","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","gggglazer");
        Fan f = database.getUser("gggglazer");
        TeamMember ownerGlazer = (TeamMember) f;
        TeamInfo manUtd = new TeamInfo("ManUtd",1906,true,"Manchester",ownerGlazer);
        Court oldTraf = new Court("Old Trafford","Manchester",80000);
        pl.addTeam(null);
        Assert.assertFalse(pl.addTeam(null));
        org.junit.Assert.assertFalse(pl.addTeam(manUtd));

        ownerGlazer.setTeam(manUtd);
        ownerGlazer.AddCourt(oldTraf);
        try{
            ownerGlazer.AddIncoming(19000,"tickets");
        }catch (Exception e){e.printStackTrace();}

        Assert.assertFalse(pl.addTeam(manUtd));

        //coach
        Guest g1 = new Guest();
        g1.SignUp("gdfg","zbvcxbidan","oleGunar","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Coach","Head Coach");
        Fan f1 = database.getUser("oleGunar");
        TeamMember coach = (TeamMember) f1;
        ownerGlazer.AddCoach(coach,"","head coach");

        Assert.assertFalse(pl.addTeam(manUtd));

        //player
        Guest g_gk = new Guest();
        g_gk.SignUp("gdfg","zbvcxbidan","deGea","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_gk = database.getUser("deGea");
        TeamMember gk = (TeamMember) f_gk;
        ownerGlazer.AddPlayer(gk,"gk");

        org.junit.Assert.assertFalse(pl.addTeam(manUtd));

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
        Assert.assertTrue(ownerGlazer.registerTheTeamToLeague("PremierLeague"));
        Assert.assertFalse(ownerGlazer.registerTheTeamToLeague(null));
    }

    @Test
    public void TestgetTeam() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        TeamInfo manchesterUnited = new TeamInfo("Manchester United", 1878, true, "Manchester", owner);
        owner.setTeam(manchesterUnited);

        owner.getTeam();
        Assert.assertEquals(owner.getTeam(),manchesterUnited);
    }

    @Test
    public void TestSetTeam() {
        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember owner = (TeamMember) f;

        //owner2
        Guest g2 = new Guest();
        g2.SignUp("gdhjklfg", "zbvcxbbjkidan", "glsdfazer", "fdgsfd", "4/6/1954", "asda@gmail.com", "12345", "Owner","");
        g2.Login("12345", "glsdfazer");
        Fan f2 = database.getUser("glsdfazer");
        TeamMember owner2 = (TeamMember) f2;

        TeamInfo manchester = new TeamInfo("Manchester", 1879, true, "Manchest", owner2);
        owner.setTeam(manchester);

        owner.setTeam(manchester);
        Assert.assertEquals(owner.getTeam(),manchester);
    }

    @Test
    public void TestGetInfoPage() {
        TeamMember userinfo = new TeamMember("jozep ", "Bartomeo ", "JB26", "1111",
                null, "31/8/1991", "FlorentinoP@gmail.com", null);
        TeamMemberInformationPage informationPage=new TeamMemberInformationPage(userinfo);
        Assert.assertTrue(userinfo.getInfoPage() instanceof TeamMemberInformationPage);
    }


}


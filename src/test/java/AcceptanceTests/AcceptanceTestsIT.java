package AcceptanceTests;

import Data.SystemDB.DB;
import Data.SystemDB.DataBaseInterface;
import Domain.AssociationManagement.League;
import Domain.AssociationManagement.Match;
import Domain.ClubManagement.Court;
import Domain.ClubManagement.TeamInfo;
import Domain.InformationPage.InformationPage;
import Domain.InformationPage.TeamInformationPage;
import Domain.User.*;
//import Service.User.*;
//import com.sun.org.apache.xpath.internal.objects.XObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

public class AcceptanceTestsIT {

    private DataBaseInterface database;

    @Before
    public void initForAcceptanceTests(){
        /**
         * ------------------------------------ Use case num 37 - start system --------------------------------
         */
        database = DB.getInstance();
        Guest sys = new Guest();
        SystemManager systemManager = (SystemManager) sys.Login("IAmGod", "God");
        Assert.assertNotNull(systemManager);
        System.out.println("System Manager is Online!");
        database.resetDB();

        /**
         * init for use case num 2
         */
        Guest guest4 = new Guest();
        guest4.SignUp("raz","ya","razyah","1234","4/6/1954",
                "razya@gmail.com","1234","","");
    }




    /**
     * ------------------------------------ Use case num 1 - guest assign to the system --------------------------------
     */


    /**
     * Scenario 1.1 - guest not exist in the system and assign to it.
     */
    @Test
    public void guestAssignToTheSystem(){
        Guest guest1 = new Guest();
        guest1.SignUp("raz","ya","razya","","4/6/1954",
                "razya@gmail.com","1204","","");
        Fan assignFan = database.getUser("razya");
        Assert.assertEquals(assignFan.getUserName(),"razya");
    }

    /**
     * Scenario 1.2 - guest not exist but try to assign with exist user name
     */
    @Test (expected = IllegalArgumentException.class)
    public void guestAssignToTheSystemExistUserName(){
        Guest guest2 = new Guest();
        guest2.SignUp("raz","ya","razya","","4/6/1954",
                "razya@gmail.com","1204","","");
        Guest guest3 = new Guest();
        guest3.SignUp("raziel","yahav","razya","","4/6/1954",
                "razya@gmail.com","1204","","");
    }


    /**
     * ------------------------------------ Use case num 2 - Fan enter into the system --------------------------------
     */

    /**
     * Scenario 2.1 - Exist Fan enter into the system.
     */
    @Test
    public void fanEnterToTheSystem(){
        Guest guest5 = new Guest();
        Fan user = guest5.Login("1234","razyah");
        Assert.assertEquals(user.getUserName(),"razyah");
    }

    /**
     * Scenario 2.2 - Exist Fan try to enter into the system with wrong password.
     */
    @Test (expected = IllegalArgumentException.class)
    public void fanEnterToTheSystemWrongPassword(){
        Guest guest5 = new Guest();
        Fan user = guest5.Login("0000","razyah");
    }

    /**
     * ------------------------------------ Use case num 3 - Fan/Guest search User -----------------------------------
     */

    /**
     * Scenario 3.1 - Guest search exist Fan.
     */
    @Test
    public void guestSearchExistUser() {
        Guest guest3 = new Guest();
        guest3.SignUp("Yossi", "yos", "Yossi", "student", "4/6/1991", "yos@gmail.com", "1234", "","");
        Guest guest = new Guest();
        ArrayList<Object> ans = guest.SearchByName("Yossi");
        Assert.assertFalse(ans.isEmpty());
    }

    /**
     * Scenario 3.2 - Guest search Fan that does not exist.
     */
    @Test
    public void guestSearchNoExistUser() {
        Guest guest3 = new Guest();
        guest3.SignUp("Yossi", "yos", "Yossi", "student", "4/6/1991", "yos@gmail.com", "1234", "","");
        Guest guest = new Guest();
        ArrayList<Object> ans= guest.SearchByName("Marsel");
        Assert.assertTrue(ans.isEmpty());
    }

    /**
     * ------------------------------------ Use case num 4 - Fan Logout from the System -----------------------------------
     */

    /**
     * Scenario 4.1 - Fan logout.
     */
    @Test
    public void fanLogout() {
        Guest guest4=new Guest();
        guest4.SignUp("Shay", "yos", "shayE", "student", "4/6/1991", "yos@gmail.com", "1234", "","");
        Fan fan4 = database.getUser("shayE");
        fan4.Login("1234", "shayE");
        fan4.Logout(fan4);
        Assert.assertTrue(fan4.getStatus().equals(Fan.Status.offline));
    }


    /**
     * Scenario 5.1 - Fan try to fallow an exist information Page.
     */
    @Test
    public void reqeustToFollowInfomationPages(){
        //fan
        Guest g0 = new Guest();
        g0.SignUp("orpaz","meshulam","orpazM","fdgsfd","4/6/1954","fgfdssg","1234","Fan","");
        g0.Login("1234","orpazM");
        Fan f0 = database.getUser("orpazM");

        //owner
        Guest g = new Guest();
        g.SignUp("gdfg","zbvcxbidan","glazer","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","glazer");
        Fan f = database.getUser("glazer");
        TeamMember ownerGlazer = (TeamMember) f;

        TeamInfo hta = new TeamInfo("hta",1926,true,"tlv",ownerGlazer);
        TeamInformationPage info= new TeamInformationPage(hta);
        f0.follow(info);
        Assert.assertEquals(f0.getFollowPages().get(0),info);

    }

    /**
     * Scenario 5.2 - Fan try to fallow an information Page that does not exist.
     */
    @Test
    public void wrongReqeustToFollowingInfomationPages(){
        //fan
        Guest g0 = new Guest();
        g0.SignUp("orpaz","meshulam","orpazM","fdgsfd","4/6/1954","fgfdssg","1234","Fan","");
        g0.Login("1234","orpazM");
        Fan f0 = database.getUser("orpazM");

        InformationPage info= null;
        f0.follow(info);
        Assert.assertFalse(f0.getFollowPages().contains(info));

    }

    /**
     * Scenario 8.1 - Fan try to Watch personal information.
     */
    @Test
    public void viewPersonalInformation(){
        //fan
        Guest g0 = new Guest();
        g0.SignUp("meytal","meshulam","meytalm","fdgsfd","4/6/1954","fgfdssg","1234","Fan","");
        g0.Login("1234","meytalm");

        Assert.assertEquals(database.getUser("meytalm").getFirstName(),"meytal");
        Assert.assertEquals(database.getUser("meytalm").getLastName(),"meshulam");

    }

    /**
     * Scenario 9.1 - player try to Edit personal information.
     */
    @Test
    public void editPersonalInformation(){
        //player
        Guest g = new Guest();
        g.SignUp("messi","zbvcxbidan","Messi","fdgsfd","cgfd","4/6/1954","messi@gmail.com","Player","CM");
        Fan f_p = database.getUser("Messi");
        TeamMember p = (TeamMember) f_p;
        p.editInfo("messiTheKing","zbvcxbidan","1234","No Occupation","24.06.1987","messi@gmail.com");
        Assert.assertEquals(p.getFirstName(),"messiTheKing");
        Assert.assertEquals(p.getLastName(),"zbvcxbidan");
        Assert.assertEquals(p.getPassword(),"1234");
        Assert.assertEquals(p.getOccupation(),"No Occupation");
        Assert.assertEquals(p.getBirthday(),"24.06.1987");
        Assert.assertEquals(p.getEmail(),"messi@gmail.com");
    }

    /**
     * Scenario 9.2 - player try to edit birthday as null in personal information.
     */
    @Test
    public void wrongEditPersonalInformation(){
        //player
        Guest g = new Guest();
        g.SignUp("messi","zbvcxbidan","Messi","fdgsfd","cgfd","4/6/1954","messi@gmail.com","Player","CF");
        Fan f_p = database.getUser("Messi");
        TeamMember p = (TeamMember) f_p;
        p.editInfo("messiTheKing","zbvcxbidan","1234","No Occupation",null,"messi@gmail.com");
        Assert.assertFalse(p.getFirstName().equals("messiTheKing"));
    }

    /**
     * Scenario 10.1 - player try to add post to his information page .
     */
    @Test
    public void playerAddContentInfo() {
        //player
        Guest g = new Guest();
        g.SignUp("messi","zbvcxbidan","Messi","fdgsfd","cgfd","4/6/1954","messi@gmail.com","Player","CF");
        Fan f_p = database.getUser("Messi");
        TeamMember p = (TeamMember) f_p;

        String content = "My visit to the Pope was one of my special days";
        p.UploadContentInfo(content);
        Assert.assertEquals(p.getInfoPage().getPostsPage().get(0).getContent(),content);

    }



    /**
     * ------------------------------------ Use case num 11 - add property to the team --------------------------------
     */

    /**
     * Scenario 11.1 - Exist owner user "Goldhar_Mitchell" that is the owner of the team "Macabi Tel-Aviv" add existing player "Dor Perez" to the team.
     */
    @Test
    public void ownerAddExistingPropertyToHisTeam(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);

        ownerGold.setTeam(macabiTelAviv);

        //player
        Guest g_gk = new Guest();
        g_gk.SignUp("Dor","Perez","DorPerez","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_gk = database.getUser("DorPerez");
        TeamMember playerDorPerez = (TeamMember) f_gk;

        Assert.assertTrue(ownerGold.AddPlayer(playerDorPerez,"gk"));
        Assert.assertEquals(playerDorPerez.getUserName(),"DorPerez");
    }

    /**
     * Scenario 11.2 - Exist owner user "Goldhar_Mitchell" that is the owner of the team "Macabi Tel-Aviv" add non-existing player "Avrahm Perez" to the team.
     */
    @Test (expected = ClassCastException.class)
    public void ownerAddNonExistingPropertyToHisTeam(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);
        Court oldTraf = new Court("Old Trafford","Manchester",80000);

        ownerGold.setTeam(macabiTelAviv);
        ownerGold.AddCourt(oldTraf);

        //player
        Guest g_gk = new Guest();
        g_gk.SignUp("Avrahm","Perez","AvrahmPerez","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","","");
        Fan f_gk = database.getUser("AvrahmPerez");

        Assert.assertFalse(ownerGold.AddPlayer((TeamMember)f_gk,"gk"));
    }

    /**
     * Scenario 11.3 - Exist owner user "Goldhar_Mitchell" that is the owner of the team "Macabi Tel-Aviv" add existing player "Dor Perez" that is already in the team to the team.
     */
    @Test
    public void ownerAddExistingPropertyFromTheTeamToHisTeam(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);

        ownerGold.setTeam(macabiTelAviv);

        //player
        Guest g_gk = new Guest();
        g_gk.SignUp("Dor","Perez","DorPerez","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_gk = database.getUser("DorPerez");
        TeamMember playerDorPerez = (TeamMember) f_gk;
        ownerGold.AddPlayer(playerDorPerez,"gk");
        playerDorPerez.setTeam(macabiTelAviv);

        Assert.assertFalse(ownerGold.AddPlayer(playerDorPerez,"yy"));
    }

    /**
     * Scenario 11.4 - Exist owner user "Goldhar_Mitchell" that is the owner of the team "Macabi Tel-Aviv" that wants to add non-existing user "Dor Perez" to the team.
     */
    @Test (expected = IllegalArgumentException.class)
    public void ownerAddNonExistingUserToHisTeam(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);
        Court oldTraf = new Court("Old Trafford","Manchester",80000);

        ownerGold.setTeam(macabiTelAviv);
        TeamMember DorPerezPlayer = (TeamMember)database.getUser("DorPerez");

        Assert.assertFalse(ownerGold.AddPlayer(DorPerezPlayer,"yy"));
    }

    /**
     * Scenario 11.5 - Exist player user "MohamadSalah" in the team "macbi tel aviv", that not an owner that wants to existing player user "Dor Perez" to the team.
     */
    @Test (expected = IllegalArgumentException.class)
    public void playerAddExistingPropertyToHisTeam(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);
        ownerGold.setTeam(macabiTelAviv);

        //player
        Guest g_gk = new Guest();
        g_gk.SignUp("Dor","Perez","DorPerez","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_gk = database.getUser("DorPerez");
        TeamMember playerDorPerez = (TeamMember) f_gk;
        ownerGold.AddPlayer(playerDorPerez,"gk");

        //player2
        Guest g_gk2 = new Guest();
        g_gk2.SignUp("Mohamad","Salah","MohamadSalah","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_gk2 = database.getUser("MohamadSalah");
        TeamMember playerMohamadSalah = (TeamMember) f_gk2;
        ownerGold.AddPlayer(playerMohamadSalah,"gk");
        playerMohamadSalah.setTeam(macabiTelAviv);

        Assert.assertFalse(playerMohamadSalah.AddPlayer(playerMohamadSalah,"yy"));
    }


    /**
     * ------------------------------------ Use case num 12 - remove property from the team --------------------------------
     */

    /**
     * Scenario 12.1 - Exist owner user "Goldhar_Mitchell" that is the owner of the team "Macabi Tel-Aviv" removes existing player "Dor Perez" from the team.
     */
    @Test
    public void ownerRemoveExistingPropertyFromHisTeam(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);
        Court oldTraf = new Court("Old Trafford","Manchester",80000);

        ownerGold.setTeam(macabiTelAviv);

        //player
        Guest g_gk = new Guest();
        g_gk.SignUp("Dor","Perez","DorPerez","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_gk = database.getUser("DorPerez");
        TeamMember playerDorPerez = (TeamMember) f_gk;
        ownerGold.AddPlayer(playerDorPerez,"gk");

        Assert.assertTrue(ownerGold.RemovePlayer(playerDorPerez));
    }

    /**
     * Scenario 12.2 - Exist owner user "Goldhar_Mitchell" that is the owner of the team "Macabi Tel-Aviv" removes non-existing player "Mohamad Salah" from the team.
     */
    @Test
    public void ownerRemoveNonExistingPropertyToHisTeam(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);
        Court oldTraf = new Court("Old Trafford","Manchester",80000);

        ownerGold.setTeam(macabiTelAviv);
        ownerGold.AddCourt(oldTraf);

        //player
        Guest g_gk = new Guest();
        g_gk.SignUp("Mohamad","Salah","MohamadSalah","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_gk = database.getUser("MohamadSalah");
        TeamMember playerMohamadSalah = (TeamMember) f_gk;
        //ownerGold.RemovePlayer(playerMohamadSalah);
        Assert.assertFalse(ownerGold.RemovePlayer(playerMohamadSalah));
    }

    /**
     * Scenario 12.3 - Exist owner user "Goldhar_Mitchell" that is the owner of the team "Macabi Tel-Aviv" removes existing user that isnt a player "Mohamad Salah" from the team.
     */
    @Test (expected = ClassCastException.class)
    public void ownerRemoveExistingUserThatItsntPlayerFromHisTeam(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);
        Court oldTraf = new Court("Old Trafford","Manchester",80000);

        ownerGold.setTeam(macabiTelAviv);
        ownerGold.AddCourt(oldTraf);

        //player
        Guest g_gk = new Guest();
        g_gk.SignUp("Mohamad","Salah","MohamadSalah","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","","");
        Fan f_gk = database.getUser("MohamadSalah");

        Assert.assertFalse(ownerGold.RemovePlayer((TeamMember)f_gk));
    }

    /**
     * Scenario 12.4 - Exist owner user "Goldhar_Mitchell" that is the owner of the team "Macabi Tel-Aviv" that wants to remove non-existing user "Dor Perez" to the team.
     */
    @Test (expected = IllegalArgumentException.class)
    public void ownerRemoveNonExistingUserToHisTeam(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);
        Court oldTraf = new Court("Old Trafford","Manchester",80000);

        ownerGold.setTeam(macabiTelAviv);
        Fan salah = database.getUser("MohamadSalah");
        TeamMember salahPlayer = (TeamMember) salah;
        Assert.assertFalse(ownerGold.AddPlayer(salahPlayer,"yy"));
    }

    /**
     * Scenario 12.5 - Exist player user "MohamadSalah" in the team "macbi tel aviv", that not an owner that wants to existing player user "Dor Perez" to the team.
     */
    @Test (expected = IllegalArgumentException.class)
    public void playerRemoveExistingPropertyToHisTeam(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);
        ownerGold.setTeam(macabiTelAviv);

        //player
        Guest g_gk = new Guest();
        g_gk.SignUp("Dor","Perez","DorPerez","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_gk = database.getUser("DorPerez");
        TeamMember playerDorPerez = (TeamMember) f_gk;
        ownerGold.AddPlayer(playerDorPerez,"gk");

        //player2
        Guest g_gk2 = new Guest();
        g_gk2.SignUp("Mohamad","Salah","MohamadSalah","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_gk2 = database.getUser("MohamadSalah");
        TeamMember playerMohamadSalah = (TeamMember) f_gk2;
        ownerGold.AddPlayer(playerMohamadSalah,"gk");
        playerMohamadSalah.setTeam(macabiTelAviv);

        Assert.assertFalse(playerMohamadSalah.RemovePlayer(playerDorPerez));
    }

    /**
     * ------------------------------------ Use case num 13 - edit property from the team --------------------------------
     */

    /**
     * Scenario 13.1 - Exist owner user "Goldhar_Mitchell" that is the owner of the team "Macabi Tel-Aviv" edit existing player "Dor Perez" from the team.
     */
    @Test
    public void ownerEditExistingPropertyFromHisTeam(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);
        Court oldTraf = new Court("Old Trafford","Manchester",80000);

        ownerGold.setTeam(macabiTelAviv);
//        ownerGold.AddCourt(oldTraf.getCourtName(),80000);

        //player
        Guest g_gk = new Guest();
        g_gk.SignUp("Dor","Perez","DorPerez","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_gk = database.getUser("DorPerez");
        TeamMember playerDorPerez = (TeamMember) f_gk;
        ownerGold.AddPlayer(playerDorPerez,"gk");

        ownerGold.EditPlayerInformation(playerDorPerez,"Shoer");
        Assert.assertEquals(playerDorPerez.getRoleOnCourt(),"Shoer");
    }

    /**
     * Scenario 13.2 - Exist owner user "Goldhar_Mitchell" that is the owner of the team "Macabi Tel-Aviv" edit non-existing player "Avi Perez" from the team.
     */
    @Test (expected = IllegalArgumentException.class)
    public void ownerEditNonEexistingPropertyToHisTeam(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);
        Court oldTraf = new Court("Old Trafford","Manchester",80000);

        ownerGold.setTeam(macabiTelAviv);
        ownerGold.AddCourt(oldTraf);
        TeamMember avi = (TeamMember)database.getUser("AviPerez");

        ownerGold.EditPlayerInformation(avi,"Shoer");
    }

    /**
     * ------------------------------------ Use case num 14 - Add owner user as an team owner --------------------------------
     */

    /**
     * Scenario 14.1 - Exist owner user "Goldhar_Mitchell" that is the owner of the team "Macabi Tel-Aviv" adds another team owner "Ron" that is the owner of the team "Macabi HAifa"
     */
    @Test
    public void ownerAddTeamOwner(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);
        ownerGold.setTeam(macabiTelAviv);

        //owner2
        Guest g2 = new Guest();
        g2.SignUp("Ron","Sade","Ron","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g2.Login("1234","Ron");
        Fan f2 = database.getUser("Ron");
        TeamMember ownerRon = (TeamMember) f2;
        TeamInfo macabiHaifa = new TeamInfo("Macabi Haifa",1906,true,"Haifa",ownerRon);
        ownerRon.setTeam(macabiHaifa);

        Assert.assertFalse(ownerGold.AddOwner(ownerRon));
    }

    /**
     * Scenario 14.2 - Exist owner user "Goldhar_Mitchell" that is the owner of the team "Macabi Tel-Aviv" adds another team owner "Ron" that has no team
     */
    @Test
    public void ownerAddOwner(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);
        ownerGold.setTeam(macabiTelAviv);

        //owner2
        Guest g2 = new Guest();
        g2.SignUp("Ron","Sade","Ron","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g2.Login("1234","Ron");
        Fan f2 = database.getUser("Ron");
        TeamMember ownerRon = (TeamMember) f2;

        Assert.assertTrue(ownerGold.AddOwner(ownerRon));
    }

    /**
     * Scenario 14.3 - Exist owner user "Goldhar_Mitchell" that is the owner of the team "Macabi Tel-Aviv" adds user that isnt an owner "Ron"
     */
    @Test (expected = ClassCastException.class)
    public void ownerAddNonOwner(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);
        ownerGold.setTeam(macabiTelAviv);

        //user
        Guest g2 = new Guest();
        g2.SignUp("Ron","Sade","Ron","fdgsfd","4/6/1954","fgfdssg","1234","","");
        g2.Login("1234","Ron");
        Fan f2 = database.getUser("Ron");


        Assert.assertFalse(ownerGold.AddOwner((TeamMember)f2));
    }

    /**
     * Scenario 14.4 - Exist owner user "Goldhar_Mitchell" that is the owner of the team "Macabi Tel-Aviv" adds non existing user "Ron"
     */
    @Test (expected = IllegalArgumentException.class)
    public void ownerAddNonexistingUser(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);
        ownerGold.setTeam(macabiTelAviv);
        Fan ownerRon = database.getUser("Ron");
        Assert.assertFalse(ownerGold.AddOwner((TeamMember)ownerRon));
    }

    /**
     * Scenario 14.5 - Exist player user "MohamadSalah" in the team "macbi tel aviv", that not an owner that wants Add to existing owner user "Ron" to the team.
     */
    @Test
    public void playerAddExistingOwnerToHisTeam(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);
        ownerGold.setTeam(macabiTelAviv);

        //owner2
        Guest g2 = new Guest();
        g2.SignUp("Ron","Sade","Ron","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g2.Login("1234","Ron");
        Fan f2 = database.getUser("Ron");
        TeamMember ownerRon = (TeamMember) f2;
        ownerGold.AddOwner(ownerRon);
        ownerRon.setTeam(macabiTelAviv);

        //player2
        Guest g_gk2 = new Guest();
        g_gk2.SignUp("Mohamad","Salah","MohamadSalah","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_gk2 = database.getUser("MohamadSalah");
        TeamMember playerMohamadSalah = (TeamMember) f_gk2;
        ownerGold.AddPlayer(playerMohamadSalah,"gk");
        playerMohamadSalah.setTeam(macabiTelAviv);

        Assert.assertFalse(playerMohamadSalah.AddOwner(ownerRon));
    }

    /**
     * ------------------------------------ Use case num 15 - Remove owner user as an team owner --------------------------------
     */

    /**
     * Scenario 15.1 - Exist owner user "Goldhar_Mitchell" that is the owner of the team "Macabi Tel-Aviv" removes another owner "Ron" that is nominate by him.
     */
    @Test
    public void ownerRemoveAnotherTeamOwner(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);
        ownerGold.setTeam(macabiTelAviv);

        //owner2
        Guest g2 = new Guest();
        g2.SignUp("Ron","Sade","Ron","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g2.Login("1234","Ron");
        Fan f2 = database.getUser("Ron");
        TeamMember ownerRon = (TeamMember) f2;
        ownerGold.AddOwner(ownerRon);
        ownerRon.setTeam(macabiTelAviv);

        Assert.assertTrue(ownerGold.RemoveOwner(ownerRon));
    }

    /**
     * Scenario 15.2 - Exist owner user "Goldhar_Mitchell" that is the owner of the team "Macabi Tel-Aviv" removes another owner "Ron" that is nominate by other owner "OR".
     */
    @Test
    public void ownerRemoveAnotherTeamOwnerNotNominateByHim(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);
        ownerGold.setTeam(macabiTelAviv);

        //owner3
        Guest g3 = new Guest();
        g3.SignUp("orr","Rot","OR","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g3.Login("1234","Goldhar_Mitchell");
        Fan f3 = database.getUser("OR");
        TeamMember ownerOR = (TeamMember) f3;
        ownerGold.AddOwner(ownerOR);
        ownerOR.setTeam(macabiTelAviv);

        //owner2
        Guest g2 = new Guest();
        g2.SignUp("Ron","Sade","Ron","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g2.Login("1234","Ron");
        Fan f2 = database.getUser("Ron");
        TeamMember ownerRon = (TeamMember) f2;
        ownerOR.AddOwner(ownerRon);
        ownerRon.setTeam(macabiTelAviv);
        //ownerGold.RemoveOwner(ownerRon);
        Assert.assertTrue(ownerGold.RemoveOwner(ownerRon));
    }

    /**
     * Scenario 15.3 - Exist owner user "Goldhar_Mitchell" that is the owner of the team "Macabi Tel-Aviv" removes non existing user "Ron"
     */
    @Test (expected = IllegalArgumentException.class)
    public void ownerRemoveNonexistingUser(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);
        ownerGold.setTeam(macabiTelAviv);
        Fan owner = database.getUser("Ron");
        Assert.assertFalse(ownerGold.RemoveOwner((TeamMember)owner));
    }

    /**
     * Scenario 15.4 - Exist player user "MohamadSalah" in the team "macbi tel aviv", that not an owner that wants Add to existing owner user "Ron" to the team.
     */
    //@Test (expected = IllegalArgumentException.class)
    public void playerRemoveExistingOwnerToHisTeam(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);
        ownerGold.setTeam(macabiTelAviv);

        //owner2
        Guest g2 = new Guest();
        g2.SignUp("Ron","Sade","Ron","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g2.Login("1234","Ron");
        Fan f2 = database.getUser("Ron");
        TeamMember ownerRon = (TeamMember) f2;
        ownerGold.AddOwner(ownerRon);
        ownerRon.setTeam(macabiTelAviv);

        //player2
        Guest g_gk2 = new Guest();
        g_gk2.SignUp("Mohamad","Salah","MohamadSalah","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_gk2 = database.getUser("MohamadSalah");
        TeamMember playerMohamadSalah = (TeamMember) f_gk2;
        ownerGold.AddPlayer(playerMohamadSalah,"gk");
        playerMohamadSalah.setTeam(macabiTelAviv);
        boolean ans =playerMohamadSalah.RemoveOwner(ownerRon);
        Assert.assertFalse(playerMohamadSalah.RemoveOwner(ownerRon));
    }

    /**
     * ------------------------------------ Use case num 16 - Add Team manager user as an Team manager --------------------------------
     */

    /**
     * Scenario 16.1 - Exist owner user "Goldhar_Mitchell" that is the owner of the team "Macabi Tel-Aviv" adds another Team manager "Ron" that isnt a manager of a team
     */
    @Test
    public void ownerAddTeamManager(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);
        ownerGold.setTeam(macabiTelAviv);

        //TeamManager
        Guest g2 = new Guest();
        g2.SignUp("Ron","Sade","Ron","fdgsfd","4/6/1954","fgfdssg","1234","TeamManager","");
        g2.Login("1234","Ron");
        Fan f2 = database.getUser("Ron");
        TeamMember teamManagerRon = (TeamMember) f2;

        Assert.assertTrue(ownerGold.AddTeamManager(teamManagerRon, true,true,true,true));
    }

    /**
     * Scenario 16.2 - Exist owner user "Goldhar_Mitchell" that is the owner of the team "Macabi Tel-Aviv" adds user "Ron" that is already team amnager of the team
     */
    @Test
    public void ownerAddTeamManagerWithATeam(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","CF");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);
        ownerGold.setTeam(macabiTelAviv);

        //TeamManager
        Guest g2 = new Guest();
        g2.SignUp("Ron","Sade","Ron","fdgsfd","4/6/1954","fgfdssg","1234","TeamManager","");
        g2.Login("1234","Ron");
        Fan f2 = database.getUser("Ron");
        TeamMember teamManagerRon = (TeamMember) f2;
        //TeamInfo macabiHaifa = new TeamInfo("Macabi Haifa",1906,true,"Haifa",teamManagerRon);
        teamManagerRon.setTeam(macabiTelAviv);

        Assert.assertFalse(ownerGold.AddTeamManager(teamManagerRon, true,true,true,true));
    }

    /**
     * Scenario 16.3 - Exist owner user "Goldhar_Mitchell" that is the owner of the team "Macabi Tel-Aviv" adds non existing user "Ron"
     */
    @Test (expected = IllegalArgumentException.class)
    public void ownerAddTeamManagerNonexistingUser(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);
        ownerGold.setTeam(macabiTelAviv);
        Fan f2 = database.getUser("Ron");
        TeamMember teamManagerRon = (TeamMember) f2;
        Assert.assertFalse(ownerGold.AddTeamManager(teamManagerRon, true,true,true,true));
    }

    /**
     * Scenario 16.4 - Exist owner user "Goldhar_Mitchell" that is the owner of the team "Macabi Tel-Aviv" adds user that isnt an team manager "Ron"
     */
    @Test (expected = ClassCastException.class)
    public void ownerAddNonTeamManager(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);
        ownerGold.setTeam(macabiTelAviv);

        //player
        Guest g2 = new Guest();
        g2.SignUp("Ron","Sade","Ron","fdgsfd","4/6/1954","fgfdssg","1234","","");
        g2.Login("1234","Ron");
        Fan f2 = database.getUser("Ron");

        Assert.assertFalse(ownerGold.AddTeamManager((TeamMember)f2,true,true,true,true));
    }

    /**
     * Scenario 16.5 - Exist player user "MohamadSalah" in the team "macbi tel aviv", that not an owner that wants Add to existing team manager user "Ron" to the team.
     */
    @Test (expected = IllegalArgumentException.class)
    public void playerAddExistingTeamManagerToHisTeam(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);
        ownerGold.setTeam(macabiTelAviv);

        //TeamManager
        Guest g2 = new Guest();
        g2.SignUp("Ron","Sade","Ron","fdgsfd","4/6/1954","fgfdssg","1234","TeamManager","");
        g2.Login("1234","Ron");
        Fan f2 = database.getUser("Ron");
        TeamMember teamManagerRon = (TeamMember) f2;
        ownerGold.AddTeamManager(teamManagerRon,true,true,true,true);
        teamManagerRon.setTeam(macabiTelAviv);

        //player2
        Guest g_gk2 = new Guest();
        g_gk2.SignUp("Mohamad","Salah","MohamadSalah","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_gk2 = database.getUser("MohamadSalah");
        TeamMember playerMohamadSalah = (TeamMember) f_gk2;
        ownerGold.AddPlayer(playerMohamadSalah,"gk");
        playerMohamadSalah.setTeam(macabiTelAviv);

        Assert.assertFalse(playerMohamadSalah.AddTeamManager(teamManagerRon,true,true,true,true));
    }

    /**
     * ------------------------------------ Use case num 17 - Remove owner user as an team manager --------------------------------
     */

    /**
     * Scenario 17.1 - Exist owner user "Goldhar_Mitchell" that is the owner of the team "Macabi Tel-Aviv" removes team manager "Ron" that is nominate by him.
     */
    @Test
    public void ownerRemoveAnotherTeamManager(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);
        ownerGold.setTeam(macabiTelAviv);

        //TeamManager
        Guest g2 = new Guest();
        g2.SignUp("Ron","Sade","Ron","fdgsfd","4/6/1954","fgfdssg","1234","TeamManager","");
        g2.Login("1234","Ron");
        Fan f2 = database.getUser("Ron");
        TeamMember teamManagerRon = (TeamMember) f2;
        ownerGold.AddTeamManager(teamManagerRon,true,true,true,true);
        teamManagerRon.setTeam(macabiTelAviv);

        Assert.assertTrue(ownerGold.RemoveTeamManager(teamManagerRon));
    }

    /**
     * Scenario 17.2 - Exist owner user "Goldhar_Mitchell" that is the owner of the team "Macabi Tel-Aviv" removes team manager "Ron" that is nominate by other owner "OR".
     */
    @Test
    public void ownerRemoveAnotherTeamManagerNotNominateByHim(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);
        ownerGold.setTeam(macabiTelAviv);

        //owner3
        Guest g3 = new Guest();
        g3.SignUp("orr","Rot","OR","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g3.Login("1234","Goldhar_Mitchell");
        Fan f3 = database.getUser("OR");
        TeamMember ownerOR = (TeamMember) f3;
        ownerGold.AddOwner(ownerOR);
        ownerOR.setTeam(macabiTelAviv);

        //TeamManager
        Guest g2 = new Guest();
        g2.SignUp("Ron","Sade","Ron","fdgsfd","4/6/1954","fgfdssg","1234","TeamManager","");
        g2.Login("1234","Ron");
        Fan f2 = database.getUser("Ron");
        TeamMember teamManagerRon = (TeamMember) f2;
        ownerOR.AddTeamManager(teamManagerRon,true,true,true,true);
        teamManagerRon.setTeam(macabiTelAviv);
        ownerGold.RemoveTeamManager(teamManagerRon);
        Assert.assertTrue(ownerGold.RemoveTeamManager(teamManagerRon));
    }

    /**
     * Scenario 17.3 - Exist owner user "Goldhar_Mitchell" that is the owner of the team "Macabi Tel-Aviv" removes non existing user "Ron"
     */
    @Test
    public void ownerRemoveTeamManagerNonexistingUser(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);
        ownerGold.setTeam(macabiTelAviv);
        Fan ron = database.getUser("Ron");
        TeamMember ron2 = (TeamMember) ron;
        Assert.assertFalse(ownerGold.RemoveTeamManager(ron2));
    }

    /**
     * Scenario 17.4 - Exist player user "MohamadSalah" in the team "macbi tel aviv", that not an owner that wants remove to existing team manager user "Ron" to the team.
     */
    @Test (expected = IllegalArgumentException.class)
    public void playerRemoveExistingTeamManagerToHisTeam(){
        //owner
        Guest g = new Guest();
        g.SignUp("Or","Rot","Goldhar_Mitchell","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","Goldhar_Mitchell");
        Fan f = database.getUser("Goldhar_Mitchell");
        TeamMember ownerGold = (TeamMember) f;
        TeamInfo macabiTelAviv = new TeamInfo("Macabi Tel-Aviv",1906,true,"Tel Aviv",ownerGold);
        ownerGold.setTeam(macabiTelAviv);

        //TeamManager
        Guest g2 = new Guest();
        g2.SignUp("Ron","Sade","Ron","fdgsfd","4/6/1954","fgfdssg","1234","TeamManager","");
        g2.Login("1234","Ron");
        Fan f2 = database.getUser("Ron");
        TeamMember teamManagerRon = (TeamMember) f2;
        ownerGold.AddTeamManager(teamManagerRon,true,true,true,true);
        teamManagerRon.setTeam(macabiTelAviv);

        //player2
        Guest g_gk2 = new Guest();
        g_gk2.SignUp("Mohamad","Salah","MohamadSalah","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","");
        Fan f_gk2 = database.getUser("MohamadSalah");
        TeamMember playerMohamadSalah = (TeamMember) f_gk2;
        ownerGold.AddPlayer(playerMohamadSalah,"gk");
        playerMohamadSalah.setTeam(macabiTelAviv);

        Assert.assertFalse(playerMohamadSalah.RemoveTeamManager(teamManagerRon));
    }
    /**
     * ------------------------------------ Use case num 18 - Create new team -----------------------------------
     */

    /**
     * Scenario 18.1 - Define new coach.
     */
    @Test
    public void DefineNewCoach(){
        database.resetDB();
        Guest g=new Guest();
        g.SignUp("dan","cohen","dan","coach","21/5/1970","coach@gmail.com","1234","Coach","Head Coach");
        g.SignUp("ran","cohen","ran","owner","20/6/1978","owner@gmail.com","1234","Owner","");
        TeamMember owner=(TeamMember)database.getUser("ran");
        TeamMember coach=(TeamMember)database.getUser("dan");
        owner.createNewTeam("test",1970,true,"test");
        database.addTeam("test",1970,true,"test","ran","");
        owner.AddCoach(coach,"test","test");
        TeamInfo team=database.getTeam("test");
        Assert.assertTrue(((TeamMember)database.getUser("dan")).getTeam().getTeamName().equals("test"));
    }

    /**
     * Scenario 18.2 - Define new player.
     */
    @Test
    public void DefineNewPlayer(){
        DB.getInstance().resetDB();
        Guest g=new Guest();
        g.SignUp("dan","cohen","dan","coach","21/5/1970","coach@gmail.com","1234","Player","CF");
        g.SignUp("ran","cohen","ran","owner","20/6/1978","owner@gmail.com","1234","Owner","");
        TeamMember owner=(TeamMember)database.getUser("ran");
        TeamMember player=(TeamMember)database.getUser("dan");
        owner.createNewTeam("test",1970,true,"test");
        database.addTeam("test",1970,true,"test","ran","");
        owner.AddPlayer(player,"side");
        TeamInfo team=database.getTeam("test");
        Assert.assertTrue(((TeamMember)database.getUser("dan")).getTeam().getTeamName().equals("test"));
    }


    /**
     * ------------------------------------ Use case num 19 - Close Team -----------------------------------
     */

    /**
     * Scenario 19.1 - Owner Close team.
     */
    @Test
    public void closeTeam() {
        Guest guest19=new Guest();
        guest19.SignUp("Ran", "ran", "ran", "owner", "4/6/1954", "owner@gmail.com", "1234", "Owner","");
        database.addTeam("barcelona", 1878, true, "Manchester", "ran","");
        TeamInfo barcelona = database.getTeam("barcelona");
        //TeamInfo barcelona = new TeamInfo("barcelona", 1878, true, "Manchester", (TeamMember) database.getUser("ran"));
        ((TeamMember) database.getUser("ran")).setTeam(barcelona);

        TeamMember owner = (TeamMember) database.getUser("ran");
        Assert.assertTrue(owner.CloseTeam());
    }

    /**
     * ------------------------------------ Use case num 20 - Open Team -----------------------------------
     */

    /**
     * Scenario 20.1 - Owner Open team.
     */
    @Test
    public void openTeam() {
        Guest guest19=new Guest();
        guest19.SignUp("Ran", "ran", "ran", "owner", "4/6/1954", "owner@gmail.com", "1234", "Owner","");
        database.addTeam("barcelona", 1878, true, "Manchester", "ran","");
        TeamInfo barcelona = database.getTeam("barcelona");
        ((TeamMember) database.getUser("ran")).setTeam(barcelona);
        TeamMember owner = (TeamMember) database.getUser("ran");
        owner.CloseTeam();
        Assert.assertTrue(owner.OpenTeam());
    }
//
    /**
     * Scenario 21.1 - System manager remove team.
     */
    @Test
    public void removeTeam() {
        Guest guest19=new Guest();
        guest19.SignUp("Ran", "ran", "ran", "owner", "4/6/1954", "owner@gmail.com", "1234", "Owner","");
        SystemManager systemManager = new SystemManager("sys", "man", "manager", "1234", "manager", "21/10/1988", "manager@gmail.com");
        database.addTeam("barcelona", 1878, true, "Manchester", "ran","");
        TeamInfo barcelona = database.getTeam("barcelona");
        ((TeamMember) database.getUser("ran")).setTeam(barcelona);
        systemManager.deleteTeam(barcelona);
        Assert.assertFalse(database.isTeamExist("barcelona"));
    }


    /**
     * ------------------------------------ Use case num 22 - Remove Account -----------------------------------
     */

    /**
     * Scenario 22.1 - System manager remove exist account.
     */
    @Test
    public void removeAccount() {
        SystemManager systemManager = new SystemManager("sys", "man", "manager", "1234", "manager", "21/10/1988", "manager@gmail.com");
        Guest guest=new Guest();
        guest.SignUp("dan","Levi","dan","student","21/5/1987","dan@gmail.com","123","","");
        systemManager.RemoveAccount(DB.getInstance().getUser("dan"));
        Assert.assertNull(database.getUser("dan"));
    }

    /**
     * Scenario 22.2 - System manager remove account that not exist.
     */
    @Test(expected = Exception.class)
    public void removeNotExistAccount() {

        SystemManager systemManager = new SystemManager("sys", "man", "manager", "1234", "manager", "21/10/1988", "manager@gmail.com");
        Guest guest=new Guest();
        guest.SignUp("dan","Levi","lev","student","21/5/1987","dan@gmail.com","123","","");
        systemManager.RemoveAccount(database.getUser("dan"));
    }



    /**
     * Scenario 26.1 - register a team to league by its owner
     */
    @Test
    public void registerTeamToLeague(){
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

        //------------ManUtd---------------------------------------------------------------------------
        //==========================================================================================

        //owner
        Guest g = new Guest();
        g.SignUp("gdfg","zbvcxbidan","glazer","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","glazer");
        Fan f = database.getUser("glazer");
        TeamMember ownerGlazer = (TeamMember) f;
        TeamInfo manUtd = new TeamInfo("ManUtd",1906,true,"Manchester",ownerGlazer);
        Court oldTraf = new Court("Old Trafford","Manchester",80000);

        ownerGlazer.setTeam(manUtd);
        ownerGlazer.AddCourt(oldTraf);
        try{
            ownerGlazer.AddIncoming(19000,"tickets");
        }catch (Exception e){e.printStackTrace();}

        //coach
        Guest g1 = new Guest();
        g1.SignUp("gdfg","zbvcxbidan","oleGunar","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Coach","Head Coach");
        Fan f1 = database.getUser("oleGunar");
        TeamMember coach = (TeamMember) f1;
        ownerGlazer.AddCoach(coach,"","head coach");


        //player
        Guest g_gk = new Guest();
        g_gk.SignUp("gdfg","zbvcxbidan","deGea","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_gk = database.getUser("deGea");
        TeamMember gk = (TeamMember) f_gk;
        ownerGlazer.AddPlayer(gk,"gk");

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
    }

    /**
     * Scenario 26.2 - dont register a team to league by its owner if the team doesnt have coach
     */
    @Test
    public void registerTeamToLeagueWithoutCoach(){

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
        //------------ManUtd---------------------------------------------------------------------------
        //==========================================================================================

        //owner
        Guest g = new Guest();
        g.SignUp("gdfg","zbvcxbidan","glazer","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","glazer");
        Fan f = database.getUser("glazer");
        TeamMember ownerGlazer = (TeamMember) f;
        TeamInfo manUtd = new TeamInfo("ManUtd",1906,true,"Manchester",ownerGlazer);
        Court oldTraf = new Court("Old Trafford","Manchester",80000);

        ownerGlazer.setTeam(manUtd);
        ownerGlazer.AddCourt(oldTraf);
        try{
            ownerGlazer.AddIncoming(19000,"tickets");
        }catch (Exception e){e.printStackTrace();}


        //player
        Guest g_gk = new Guest();
        g_gk.SignUp("gdfg","zbvcxbidan","deGea","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_gk = database.getUser("deGea");
        TeamMember gk = (TeamMember) f_gk;
        ownerGlazer.AddPlayer(gk,"gk");

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

        Assert.assertFalse(ownerGlazer.registerTheTeamToLeague("PremierLeague"));
    }


    /**
     * Scenario 26.3 - dont register a team to league by its owner if the team doesnt have 11 players
     */
    @Test
    public void registerTeamToLeagueWithoutAllPlayers(){
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
        //------------ManUtd---------------------------------------------------------------------------
        //==========================================================================================

        //owner
        Guest g = new Guest();
        g.SignUp("gdfg","zbvcxbidan","glazer","fdgsfd","4/6/1954","fgfdssg","1234","Owner","");
        g.Login("1234","glazer");
        Fan f = database.getUser("glazer");
        TeamMember ownerGlazer = (TeamMember) f;
        TeamInfo manUtd = new TeamInfo("ManUtd",1906,true,"Manchester",ownerGlazer);
        Court oldTraf = new Court("Old Trafford","Manchester",80000);

        ownerGlazer.setTeam(manUtd);
        ownerGlazer.AddCourt(oldTraf);
        try{
            ownerGlazer.AddIncoming(19000,"tickets");
        }catch (Exception e){e.printStackTrace();}

        //player
        Guest g_gk = new Guest();
        g_gk.SignUp("gdfg","zbvcxbidan","deGea","fdgsfd","cgfd","4/6/1954","fdgs@gmail.com","Player","CF");
        Fan f_gk = database.getUser("deGea");
        TeamMember gk = (TeamMember) f_gk;
        ownerGlazer.AddPlayer(gk,"gk");

        Assert.assertFalse(ownerGlazer.registerTheTeamToLeague("PremierLeague"));
    }

    /**
     * ------------------------------------------------------------------------------------------------------------------
     * ------------------------------------ Use case num 27 - define new league --------------------------------
     * ------------------------------------------------------------------------------------------------------------------
     */


    /**
     * Scenario 27.1 - define a league by association user
     */
    @Test
    public void createALeague(){
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

        Assert.assertNotNull(database.getLeagueByName("PremierLeague"));
        League league = database.getLeagueByName("PremierLeague");
        Assert.assertEquals(database.getSeasonByYear(2021).getYear(),AssociationUser.getBm().get(2021).getBudget().getSeason().getYear());
    }

    /**
     * Scenario 27.2 - try to define a league by association user that already exists
     */
    @Test
    public void createExistingLeague(){
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

        Assert.assertNotNull(database.getLeagueByName("PremierLeague"));
        League league = database.getLeagueByName("PremierLeague");
        Assert.assertEquals(database.getSeasonByYear(2021).getYear(),AssociationUser.getBm().get(2021).getBudget().getSeason().getYear());
        //        //try to add the same league
        Assert.assertFalse(associationUser.addNewLeagueToSeason(2021,"PremierLeague",new Date().toString(),3,0,
                1,true,false,12,12));

    }

    /**
     * ------------------------------------------------------------------------------------------------------------------
     * ------------------------------------ Use case num 28 - appoint referee user to be referee --------------------------------
     * ------------------------------------------------------------------------------------------------------------------
     */


    /**
     * Scenario 28.1 - appoint referee user to be referee
     */
    @Test
    public void appointReferee(){
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

        //Refs
        Guest r1 = new Guest();
        r1.SignUp("Alex", "zbvcxbidan", "ref1", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Referee","Main Referee");
        Fan re1 = database.getUser("ref1");
        Referee ref1 = (Referee) re1;

        Guest r2 = new Guest();
        r2.SignUp("Boris", "zbvcxbidan", "ref2", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Referee", "Side Referee");
        Fan re2 = database.getUser("ref2");
        Referee ref2 = (Referee) re2;

        Assert.assertTrue(associationUser.NominateReferee(ref1));
        Assert.assertTrue(associationUser.NominateReferee(ref2));


    }

//    /**
//     * Scenario 28.2 - try to appoint a user that is not referee to be referee
//     */
//    @Test
//    public void appointRefereeThatNotQualified(){
//        //association
//        Guest g0 = new Guest();
//        g0.SignUp("gdfg","zbvcxbidan","fa","fdgsfd","4/6/1954","fgfdssg","1234","Association");
//        g0.Login("1234","fa");
//        Fan f0 = database.getUser("fa");
//        AssociationUser associationUser = (AssociationUser) f0;
//        database.addSeason(2021,1000000);
//        database.addLeagueAndPolicyToSeason(2021,"PremierLeague",new Date().toString(),3,0,
//                1,true,false,12,12);
//        associationUser.createSeasonAndBudget(2021,1000000);
//        associationUser.addNewLeagueToSeason(2021,"PremierLeague",new Date().toString(),3,0,
//                1,true,false,12,12);
//
//        //Refs
//        Guest r1 = new Guest();
//        r1.SignUp("daniel", "zbvcxbidan", "daniel", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "fdgsdfg");
//        Fan re1 = database.getUser("daniel");
//
//        Guest r2 = new Guest();
//        r2.SignUp("shay", "zbvcxbidan", "shay", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "fdgdsfg");
//        Fan re2 = database.getUser("shay");
//        associationUser.NominateReferee((Referee)re1);
//        Assert.assertFalse(associationUser.NominateReferee((Referee)re1));
//        Assert.assertFalse(associationUser.NominateReferee((Referee)re2));
//    }

    /**
     * ------------------------------------------------------------------------------------------------------------------
     * ------------------------------------ Use case num 29 - remove referee user --------------------------------
     * ------------------------------------------------------------------------------------------------------------------
     */


    /**
     * Scenario 29.1 - remove referee user from appointment
     */
    @Test
    public void removeReferee(){
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

        //Refs
        Guest r1 = new Guest();
        r1.SignUp("Alex", "zbvcxbidan", "ref1", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Referee","Main Referee");
        Fan re1 = database.getUser("ref1");
        Referee ref1 = (Referee) re1;

        Guest r2 = new Guest();
        r2.SignUp("Boris", "zbvcxbidan", "ref2", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Referee","Side Referee");
        Fan re2 = database.getUser("ref2");
        Referee ref2 = (Referee) re2;

        Assert.assertTrue(associationUser.NominateReferee(ref1));
        Assert.assertTrue(associationUser.NominateReferee(ref2));

//        //remove
//        Assert.assertTrue(associationUser.removeRefereeFromJob("ref1"));
//        Assert.assertTrue(associationUser.removeRefereeFromJob("ref2"));

        //still registered
        Assert.assertEquals("Alex", database.getUser("ref1").getFirstName());
        Assert.assertEquals("Boris", database.getUser("ref2").getFirstName());

    }

//    /**
//     * Scenario 29.2 - remove user from appointment
//     */
//    @Test
//    public void removeRefereeThatNotQualified(){
//        //association
//        Guest g0 = new Guest();
//        g0.SignUp("gdfg","zbvcxbidan","fa","fdgsfd","4/6/1954","fgfdssg","1234","Association","");
//        g0.Login("1234","fa");
//        Fan f0 = database.getUser("fa");
//        AssociationUser associationUser = (AssociationUser) f0;
//        database.addSeason(2021,1000000);
//        database.addLeagueAndPolicyToSeason(2021,"PremierLeague",new Date().toString(),3,0,
//                1,true,false,12,12);
//        associationUser.createSeasonAndBudget(2021,1000000);
//        associationUser.addNewLeagueToSeason(2021,"PremierLeague",new Date().toString(),3,0,
//                1,true,false,12,12);
//
//        //Refs
//        Guest r1 = new Guest();
//        r1.SignUp("daniel", "zbvcxbidan", "daniel", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Referee","Main Referee");
//        Fan re1 = database.getUser("daniel");
//
//        Guest r2 = new Guest();
//        r2.SignUp("shay", "zbvcxbidan", "shay", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Referee","Main Referee");
//        Fan re2 = database.getUser("shay");
//
////        Assert.assertFalse(associationUser.removeRefereeFromJob("daniel"));
////        Assert.assertFalse(associationUser.removeRefereeFromJob("shay"));
//    }

    /**
     * ------------------------------------------------------------------------------------------------------------------
     * ------------------------------------ Use case num 30 - appoint referee to specific league --------------------------------
     * ------------------------------------------------------------------------------------------------------------------
     */


    /**
     * Scenario 30.1 - appoint referee to specific league
     */
    @Test
    public void appointRefereeToSpecificLeague(){
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
        League leg = database.getLeagueByName("PremierLeague");

        //Refs
        Guest r1 = new Guest();
        r1.SignUp("Alex", "zbvcxbidan", "ref1", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Referee","Main Referee");
        Fan re1 = database.getUser("ref1");


        Guest r2 = new Guest();
        r2.SignUp("Boris", "zbvcxbidan", "ref2", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Referee","Side Referee");
        Fan re2 = database.getUser("ref2");
        associationUser.NominateReferee((Referee)re1);
        Assert.assertTrue(associationUser.NominateReferee((Referee)re1));
        Assert.assertTrue(associationUser.NominateReferee((Referee)re2));

        Assert.assertTrue(associationUser.addRefereeToLeague(leg,(Referee)re1));
        Assert.assertTrue(associationUser.addRefereeToLeague(leg,(Referee)re2));

        Assert.assertEquals(database.getLeagueByName("PremierLeague").getSideReferees().get(0).getUserName(), re2.getUserName());
        Assert.assertEquals(database.getLeagueByName("PremierLeague").getMainReferees().get(0).getUserName(), re1.getUserName());
    }


    /**
     * Scenario 30.2 - appoint user to specific league -(not good) cant be done because user need to be nominated first -- cancel
     */
    @Test
    public void appointUserToSpecificLeague(){
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

        //Refs
        Guest r1 = new Guest();
        r1.SignUp("Alex", "zbvcxbidan", "ref1", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Referee","Main Referee");
        Fan re1 = database.getUser("ref1");


        Guest r2 = new Guest();
        r2.SignUp("Boris", "zbvcxbidan", "ref2", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Referee","Side Referee");
        Fan re2 = database.getUser("ref2");

        associationUser.createSeasonAndBudget(2021,1000000);
        associationUser.addNewLeagueToSeason(2021,"PremierLeague",new Date().toString(),3,0,
                1,true,false,12,12);
        League leg = database.getLeagueByName("PremierLeague");
        //associationUser.addRefereeToLeague(leg,(Referee)re1);

        Assert.assertTrue(associationUser.addRefereeToLeague(leg,(Referee)re1));
        Assert.assertTrue(associationUser.addRefereeToLeague(leg,(Referee)re2));
    }

    /**
     * ------------------------------------------------------------------------------------------------------------------
     * ------------------------------------ Use case num 31 - define/update points policy --------------------------------
     * ------------------------------------------------------------------------------------------------------------------
     */


    /**
     * Scenario 31.1 - define/update points policy
     */
    @Test
    public void defineUpdatePointsPolicy(){
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
        League league =  database.getLeagueByName("PremierLeague");

        //define
        associationUser.createRankAndPointsPolicy(league,3,1,0,true,false);
        Assert.assertTrue(associationUser.createRankAndPointsPolicy(league,3,1,0,true,false));



        Assert.assertEquals(league.getPointsPolicy().getPointsForWin(),3);

        //update
        Assert.assertTrue(associationUser.updateRankAndPointsPolicy(league,4,2,1,true,false));

        Assert.assertEquals(league.getPointsPolicy().getPointsForWin(),4);

    }

    /**
     * Scenario 31.2 - define Points Policy With Wrong Parameters
     */
    @Test
    public void definePointsPolicyWithWrongParameters(){
        //association
        Guest g0 = new Guest();
        g0.SignUp("gdfg","zbvcxbidan","fa","fdgsfd","4/6/1954","fgfdssg","1234","Association","");
        g0.Login("1234","fa");
        Fan f0 = database.getUser("fa");
        AssociationUser associationUser = (AssociationUser) f0;
        associationUser.createSeasonAndBudget(2021,1000000);
        associationUser.addNewLeagueToSeason(2021,"PremierLeague",new Date().toString(),3,0,
                1,true,false,12,12);
        League league =  database.getLeagueByName("PremierLeague");
        //define
        Assert.assertFalse(associationUser.createRankAndPointsPolicy(league,-3,1,0,true,false));

    }

    /**
     * Scenario 31.3 - update Points Policy With No Define
     */
    @Test
    public void updatePointsPolicyWithNoDefine(){
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

        League league =  database.getLeagueByName("PremierLeague");

        //update
        Assert.assertFalse(associationUser.updateRankAndPointsPolicy(league,4,2,1,true,false));

        Assert.assertNull(league.getPointsPolicy());

    }

    /**
     * Scenario 31.4 - define/update points policy to league which isnt defined yet
     */
    @Test
    public void defineUpdatePointsPolicyToLeagueNotDefined(){
        //association
        Guest g0 = new Guest();
        g0.SignUp("gdfg","zbvcxbidan","fa","fdgsfd","4/6/1954","fgfdssg","1234","Association","");
        g0.Login("1234","fa");
        Fan f0 = database.getUser("fa");
        AssociationUser associationUser = (AssociationUser) f0;
        associationUser.createSeasonAndBudget(2021,1000000);
        associationUser.addNewLeagueToSeason(2021,"PremierLeague",new Date().toString(),3,0,
                1,true,false,12,12);
        League league =  database.getLeagueByName("LaLiga");


        Assert.assertFalse(associationUser.createRankAndPointsPolicy(league,3,1,0,true,false));


    }

    /**
     * ------------------------------------------------------------------------------------------------------------------
     * ------------------------------------ Use case num 32 - update game events --------------------------------
     * ------------------------------------------------------------------------------------------------------------------
     */


    /**
     * Scenario 32.1 - update game events
     */
    @Test
    public void updateGameEvents(){
        //association
        Guest g0 = new Guest();
        g0.SignUp("gdfg", "zbvcxbidan", "fa", "fdgsfd", "4/6/1954", "fgfdssg", "1234", "Association","");
        g0.Login("1234", "fa");
        Fan f0 = database.getUser("fa");
        AssociationUser associationUser = (AssociationUser) f0;
        database.addSeason(2021,1000000);
        database.addLeagueAndPolicyToSeason(2021,"PremierLeague",new Date().toString(),3,0,
                1,true,false,12,12);
        associationUser.createSeasonAndBudget(2021,1000000);
        associationUser.addNewLeagueToSeason(2021,"PremierLeague",new Date().toString(),3,0,
                1,true,false,12,12);

        //------------ManUtd----------------------

        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "fgfdssg", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember ownerGlazer = (TeamMember) f;
        TeamInfo manUtd = new TeamInfo("ManUtd", 1906, true, "Manchester", ownerGlazer);
        Court oldTraf = new Court("Old Trafford", "Manchester", 80000);
        ownerGlazer.setTeam(manUtd);
        ownerGlazer.AddCourt(oldTraf);
        try {
            ownerGlazer.AddIncoming(20000, "tickets");
        } catch (Exception e) {
            e.printStackTrace();
        }


        //coach
        Guest g1 = new Guest();
        g1.SignUp("gdfg", "zbvcxbidan", "oleGunar", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Coach","Head Coach");
        Fan f1 = database.getUser("oleGunar");
        TeamMember coach = (TeamMember) f1;
        ownerGlazer.AddCoach(coach, "", "head coach");

        //player
        Guest g_gk = new Guest();
        g_gk.SignUp("gdfg", "zbvcxbidan", "deGea", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_gk = database.getUser("deGea");
        TeamMember gk = (TeamMember) f_gk;
        ownerGlazer.AddPlayer(gk, "gk");

        //player
        Guest g_rb = new Guest();
        g_rb.SignUp("gdfg", "zbvcxbidan", "awb", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rb = database.getUser("awb");
        TeamMember rb = (TeamMember) f_rb;
        ownerGlazer.AddPlayer(rb, "rb");

        //player
        Guest g_rcb = new Guest();
        g_rcb.SignUp("gdfg", "zbvcxbidan", "meguire", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rcb = database.getUser("meguire");
        TeamMember rcb = (TeamMember) f_rcb;
        ownerGlazer.AddPlayer(rcb, "rcb");

        //player
        Guest g_lcb = new Guest();
        g_lcb.SignUp("gdfg", "zbvcxbidan", "baily", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lcb = database.getUser("baily");
        TeamMember lcb = (TeamMember) f_lcb;
        ownerGlazer.AddPlayer(lcb, "lcb");

        //player
        Guest g_lb = new Guest();
        g_lb.SignUp("gdfg", "zbvcxbidan", "shaw", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lb = database.getUser("shaw");
        TeamMember lb = (TeamMember) f_lb;
        ownerGlazer.AddPlayer(lb, "lb");

        //player
        Guest g_rm = new Guest();
        g_rm.SignUp("gdfg", "zbvcxbidan", "sancho", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rm = database.getUser("sancho");
        TeamMember rm = (TeamMember) f_rm;
        ownerGlazer.AddPlayer(rm, "rm");

        //player
        Guest g_rcm = new Guest();
        g_rcm.SignUp("gdfg", "zbvcxbidan", "pogba", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rcm = database.getUser("pogba");
        TeamMember rcm = (TeamMember) f_rcm;
        ownerGlazer.AddPlayer(rcm, "rcm");

        //player
        Guest g_lcm = new Guest();
        g_lcm.SignUp("gdfg", "zbvcxbidan", "bruno", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lcm = database.getUser("bruno");
        TeamMember lcm = (TeamMember) f_lcm;
        ownerGlazer.AddPlayer(lcm, "lcm");

        //player
        Guest g_lm = new Guest();
        g_lm.SignUp("gdfg", "zbvcxbidan", "martial", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lm = database.getUser("martial");
        TeamMember lm = (TeamMember) f_lm;
        ownerGlazer.AddPlayer(lm, "lm");

        //player
        Guest g_ls = new Guest();
        g_ls.SignUp("gdfg", "zbvcxbidan", "rashford", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_ls = database.getUser("rashford");
        TeamMember ls = (TeamMember) f_ls;
        ownerGlazer.AddPlayer(ls, "ls");

        //player
        Guest g_rs = new Guest();
        g_rs.SignUp("gdfg", "zbvcxbidan", "greenwood", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rs = database.getUser("greenwood");
        TeamMember rs = (TeamMember) f_rs;
        ownerGlazer.AddPlayer(rs, "rs");


        //------------Liverpool----------------------

        //owner
        Guest g111 = new Guest();
        g111.SignUp("gdfg", "zbvcxbidan", "fenway", "fdgsfd", "4/6/1954", "fgfdssg", "1234", "Owner","");
        g111.Login("1234", "fenway");
        Fan f111 = database.getUser("fenway");
        TeamMember fenway = (TeamMember) f111;
        TeamInfo liverpool = new TeamInfo("Liverpool", 1906, true, "Liverpool", fenway);
        Court anfield = new Court("Anfield", "Liverpool", 80000);
        fenway.setTeam(liverpool);
        fenway.AddCourt(anfield);
        try {
            fenway.AddIncoming(20000, "tickets");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //coach
        Guest g11 = new Guest();
        g11.SignUp("gdfg", "zbvcxbidan", "klop", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Coach","Head Coach");
        Fan f11 = database.getUser("klop");
        TeamMember coach1 = (TeamMember) f11;
        fenway.AddCoach(coach1, "", "head coach");

        //player
        Guest g_gk1 = new Guest();
        g_gk1.SignUp("gdfg", "zbvcxbidan", "allison", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_gk1 = database.getUser("allison");
        TeamMember gk1 = (TeamMember) f_gk1;
        fenway.AddPlayer(gk1, "gk");

        //player
        Guest g_rb1 = new Guest();
        g_rb1.SignUp("gdfg", "zbvcxbidan", "arnold", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rb1 = database.getUser("arnold");
        TeamMember rb1 = (TeamMember) f_rb1;
        fenway.AddPlayer(rb1, "rb");

        //player
        Guest g_rcb1 = new Guest();
        g_rcb1.SignUp("gdfg", "zbvcxbidan", "vandijk", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rcb1 = database.getUser("vandijk");
        TeamMember rcb1 = (TeamMember) f_rcb1;
        fenway.AddPlayer(rcb1, "rcb");

        //player
        Guest g_lcb1 = new Guest();
        g_lcb1.SignUp("gdfg", "zbvcxbidan", "gomez", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lcb1 = database.getUser("gomez");
        TeamMember lcb1 = (TeamMember) f_lcb1;
        fenway.AddPlayer(lcb1, "lcb");

        //player
        Guest g_lb1 = new Guest();
        g_lb1.SignUp("gdfg", "zbvcxbidan", "robertson", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lb1 = database.getUser("robertson");
        TeamMember lb1 = (TeamMember) f_lb1;
        fenway.AddPlayer(lb1, "lb");

        //player
        Guest g_rm1 = new Guest();
        g_rm1.SignUp("gdfg", "zbvcxbidan", "wijnaldum", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rm1 = database.getUser("wijnaldum");
        TeamMember rm1 = (TeamMember) f_rm1;
        fenway.AddPlayer(rm1, "rm");

        //player
        Guest g_rcm1 = new Guest();
        g_rcm1.SignUp("gdfg", "zbvcxbidan", "fabinho", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rcm1 = database.getUser("fabinho");
        TeamMember rcm1 = (TeamMember) f_rcm1;
        fenway.AddPlayer(rcm1, "rcm");

        //player
        Guest g_lcm1 = new Guest();
        g_lcm1.SignUp("gdfg", "zbvcxbidan", "henderson", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lcm1 = database.getUser("henderson");
        TeamMember lcm1 = (TeamMember) f_lcm1;
        fenway.AddPlayer(lcm1, "lcm");

        //player
        Guest g_lm1 = new Guest();
        g_lm1.SignUp("gdfg", "zbvcxbidan", "mane", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lm1 = database.getUser("mane");
        TeamMember lm1 = (TeamMember) f_lm1;
        fenway.AddPlayer(lm1, "lm");

        //player
        Guest g_ls1 = new Guest();
        g_ls1.SignUp("gdfg", "zbvcxbidan", "firmino", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_ls1 = database.getUser("firmino");
        TeamMember ls1 = (TeamMember) f_ls1;
        fenway.AddPlayer(ls1, "ls");

        //player
        Guest g_rs1 = new Guest();
        g_rs1.SignUp("gdfg", "zbvcxbidan", "salah", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rs1 = database.getUser("salah");
        TeamMember rs1 = (TeamMember) f_rs1;
        fenway.AddPlayer(rs1, "rs");

        //Refs
        Guest r1 = new Guest();
        r1.SignUp("gdfg", "zbvcxbidan", "ref1", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Referee","Main Referee");
        Fan re1 = database.getUser("ref1");

        //-----------------------------------------------------------------------------------------------------
        //-----------------------------------------------------------------------------------------------------
        //-----------------------------------------------------------------------------------------------------
        database.addSeason(2021,1000000);
        database.addLeagueAndPolicyToSeason(2021,"PremierLeague",new Date().toString(),3,0,
                1,true,false,12,12);
        associationUser.createSeasonAndBudget(2021,1000000);
        associationUser.addNewLeagueToSeason(2021,"PremierLeague",new Date().toString(),3,0,
                1,true,false,12,12);
        League leg = database.getLeagueByName("PremierLeague");

        Assert.assertTrue(ownerGlazer.registerTheTeamToLeague("PremierLeague"));
        Assert.assertTrue(fenway.registerTheTeamToLeague("PremierLeague"));
        associationUser.NominateReferee((Referee)re1);
        Assert.assertTrue(associationUser.NominateReferee((Referee)re1));
        Assert.assertTrue(associationUser.addRefereeToLeague(leg, (Referee)re1));

        Referee referee1 = (Referee)database.getUser("ref1");
        Match game = new Match(manUtd,liverpool,manUtd.getTeamCourt(),new Date(),referee1);

        Assert.assertTrue(referee1.addGoalEvent(game,"greenwood","home"));
        Assert.assertTrue(referee1.addFoulEvent(game,"greenwood",""));
        Assert.assertTrue(referee1.addInjuryEvent(game,"mane"));
        Assert.assertTrue(referee1.addOffsideEvent(game,"salah"));
        Assert.assertTrue(referee1.addSubEvent(game,"greenwood","chong"));
        Assert.assertTrue(referee1.addCommentToGameEvents(game,"away crowd - racism calls"));

    }

    /**
     * Scenario 32.2 - update game events to game which isnt defined
     */
    @Test
    public void updateGameEventsInNullGame(){
        //association
        Guest g0 = new Guest();
        g0.SignUp("gdfg", "zbvcxbidan", "fa", "fdgsfd", "4/6/1954", "fgfdssg", "1234", "Association","");
        g0.Login("1234", "fa");
        Fan f0 = database.getUser("fa");
        AssociationUser associationUser = (AssociationUser) f0;
        database.addSeason(2021,1000000);
        database.addLeagueAndPolicyToSeason(2021,"PremierLeague",new Date().toString(),3,0,
                1,true,false,12,12);
        associationUser.createSeasonAndBudget(2021,1000000);
        associationUser.addNewLeagueToSeason(2021,"PremierLeague",new Date().toString(),3,0,
                1,true,false,12,12);
        League leg = database.getLeagueByName("PremierLeague");

        //------------ManUtd----------------------

        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "fgfdssg", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember ownerGlazer = (TeamMember) f;
        TeamInfo manUtd = new TeamInfo("ManUtd", 1906, true, "Manchester", ownerGlazer);
        Court oldTraf = new Court("Old Trafford", "Manchester", 80000);
        ownerGlazer.setTeam(manUtd);
        ownerGlazer.AddCourt(oldTraf);
        try {
            ownerGlazer.AddIncoming(20000, "tickets");
        } catch (Exception e) {
            e.printStackTrace();
        }


        //coach
        Guest g1 = new Guest();
        g1.SignUp("gdfg", "zbvcxbidan", "oleGunar", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Coach","Head Coach");
        Fan f1 = database.getUser("oleGunar");
        TeamMember coach = (TeamMember) f1;
        ownerGlazer.AddCoach(coach, "", "head coach");

        //player
        Guest g_gk = new Guest();
        g_gk.SignUp("gdfg", "zbvcxbidan", "deGea", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_gk = database.getUser("deGea");
        TeamMember gk = (TeamMember) f_gk;
        ownerGlazer.AddPlayer(gk, "gk");

        //player
        Guest g_rb = new Guest();
        g_rb.SignUp("gdfg", "zbvcxbidan", "awb", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rb = database.getUser("awb");
        TeamMember rb = (TeamMember) f_rb;
        ownerGlazer.AddPlayer(rb, "rb");

        //player
        Guest g_rcb = new Guest();
        g_rcb.SignUp("gdfg", "zbvcxbidan", "meguire", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rcb = database.getUser("meguire");
        TeamMember rcb = (TeamMember) f_rcb;
        ownerGlazer.AddPlayer(rcb, "rcb");

        //player
        Guest g_lcb = new Guest();
        g_lcb.SignUp("gdfg", "zbvcxbidan", "baily", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lcb = database.getUser("baily");
        TeamMember lcb = (TeamMember) f_lcb;
        ownerGlazer.AddPlayer(lcb, "lcb");

        //player
        Guest g_lb = new Guest();
        g_lb.SignUp("gdfg", "zbvcxbidan", "shaw", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lb = database.getUser("shaw");
        TeamMember lb = (TeamMember) f_lb;
        ownerGlazer.AddPlayer(lb, "lb");

        //player
        Guest g_rm = new Guest();
        g_rm.SignUp("gdfg", "zbvcxbidan", "sancho", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rm = database.getUser("sancho");
        TeamMember rm = (TeamMember) f_rm;
        ownerGlazer.AddPlayer(rm, "rm");

        //player
        Guest g_rcm = new Guest();
        g_rcm.SignUp("gdfg", "zbvcxbidan", "pogba", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rcm = database.getUser("pogba");
        TeamMember rcm = (TeamMember) f_rcm;
        ownerGlazer.AddPlayer(rcm, "rcm");

        //player
        Guest g_lcm = new Guest();
        g_lcm.SignUp("gdfg", "zbvcxbidan", "bruno", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lcm = database.getUser("bruno");
        TeamMember lcm = (TeamMember) f_lcm;
        ownerGlazer.AddPlayer(lcm, "lcm");

        //player
        Guest g_lm = new Guest();
        g_lm.SignUp("gdfg", "zbvcxbidan", "martial", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lm = database.getUser("martial");
        TeamMember lm = (TeamMember) f_lm;
        ownerGlazer.AddPlayer(lm, "lm");

        //player
        Guest g_ls = new Guest();
        g_ls.SignUp("gdfg", "zbvcxbidan", "rashford", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_ls = database.getUser("rashford");
        TeamMember ls = (TeamMember) f_ls;
        ownerGlazer.AddPlayer(ls, "ls");

        //player
        Guest g_rs = new Guest();
        g_rs.SignUp("gdfg", "zbvcxbidan", "greenwood", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rs = database.getUser("greenwood");
        TeamMember rs = (TeamMember) f_rs;
        ownerGlazer.AddPlayer(rs, "rs");


        //------------Liverpool----------------------

        //owner
        Guest g111 = new Guest();
        g111.SignUp("gdfg", "zbvcxbidan", "fenway", "fdgsfd", "4/6/1954", "fgfdssg", "1234", "Owner","");
        g111.Login("1234", "fenway");
        Fan f111 = database.getUser("fenway");
        TeamMember fenway = (TeamMember) f111;
        TeamInfo liverpool = new TeamInfo("Liverpool", 1906, true, "Liverpool", fenway);
        Court anfield = new Court("Anfield", "Liverpool", 80000);
        fenway.setTeam(liverpool);
        fenway.AddCourt(anfield);
        try {
            fenway.AddIncoming(20000, "tickets");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //coach
        Guest g11 = new Guest();
        g11.SignUp("gdfg", "zbvcxbidan", "klop", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Coach","Head Coach");
        Fan f11 = database.getUser("klop");
        TeamMember coach1 = (TeamMember) f11;
        fenway.AddCoach(coach1, "", "head coach");

        //player
        Guest g_gk1 = new Guest();
        g_gk1.SignUp("gdfg", "zbvcxbidan", "allison", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_gk1 = database.getUser("allison");
        TeamMember gk1 = (TeamMember) f_gk1;
        fenway.AddPlayer(gk1, "gk");

        //player
        Guest g_rb1 = new Guest();
        g_rb1.SignUp("gdfg", "zbvcxbidan", "arnold", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rb1 = database.getUser("arnold");
        TeamMember rb1 = (TeamMember) f_rb1;
        fenway.AddPlayer(rb1, "rb");

        //player
        Guest g_rcb1 = new Guest();
        g_rcb1.SignUp("gdfg", "zbvcxbidan", "vandijk", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rcb1 = database.getUser("vandijk");
        TeamMember rcb1 = (TeamMember) f_rcb1;
        fenway.AddPlayer(rcb1, "rcb");

        //player
        Guest g_lcb1 = new Guest();
        g_lcb1.SignUp("gdfg", "zbvcxbidan", "gomez", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lcb1 = database.getUser("gomez");
        TeamMember lcb1 = (TeamMember) f_lcb1;
        fenway.AddPlayer(lcb1, "lcb");

        //player
        Guest g_lb1 = new Guest();
        g_lb1.SignUp("gdfg", "zbvcxbidan", "robertson", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lb1 = database.getUser("robertson");
        TeamMember lb1 = (TeamMember) f_lb1;
        fenway.AddPlayer(lb1, "lb");

        //player
        Guest g_rm1 = new Guest();
        g_rm1.SignUp("gdfg", "zbvcxbidan", "wijnaldum", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rm1 = database.getUser("wijnaldum");
        TeamMember rm1 = (TeamMember) f_rm1;
        fenway.AddPlayer(rm1, "rm");

        //player
        Guest g_rcm1 = new Guest();
        g_rcm1.SignUp("gdfg", "zbvcxbidan", "fabinho", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rcm1 = database.getUser("fabinho");
        TeamMember rcm1 = (TeamMember) f_rcm1;
        fenway.AddPlayer(rcm1, "rcm");

        //player
        Guest g_lcm1 = new Guest();
        g_lcm1.SignUp("gdfg", "zbvcxbidan", "henderson", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lcm1 = database.getUser("henderson");
        TeamMember lcm1 = (TeamMember) f_lcm1;
        fenway.AddPlayer(lcm1, "lcm");

        //player
        Guest g_lm1 = new Guest();
        g_lm1.SignUp("gdfg", "zbvcxbidan", "mane", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lm1 = database.getUser("mane");
        TeamMember lm1 = (TeamMember) f_lm1;
        fenway.AddPlayer(lm1, "lm");

        //player
        Guest g_ls1 = new Guest();
        g_ls1.SignUp("gdfg", "zbvcxbidan", "firmino", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_ls1 = database.getUser("firmino");
        TeamMember ls1 = (TeamMember) f_ls1;
        fenway.AddPlayer(ls1, "ls");

        //player
        Guest g_rs1 = new Guest();
        g_rs1.SignUp("gdfg", "zbvcxbidan", "salah", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rs1 = database.getUser("salah");
        TeamMember rs1 = (TeamMember) f_rs1;
        fenway.AddPlayer(rs1, "rs");

        //Refs
        Guest r1 = new Guest();
        r1.SignUp("gdfg", "zbvcxbidan", "ref1", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Referee","Main Referee");
        Fan re1 = database.getUser("ref1");

        //-----------------------------------------------------------------------------------------------------
        //-----------------------------------------------------------------------------------------------------
        //-----------------------------------------------------------------------------------------------------

        Assert.assertTrue(ownerGlazer.registerTheTeamToLeague("PremierLeague"));
        Assert.assertTrue(fenway.registerTheTeamToLeague("PremierLeague"));

        Assert.assertTrue(associationUser.NominateReferee((Referee)re1));
        Assert.assertTrue(associationUser.addRefereeToLeague(leg, (Referee)re1));

        Referee referee1 = (Referee)database.getUser("ref1");
        Match game = new Match();

        Assert.assertFalse(referee1.addGoalEvent(game,"greenwood","home"));
        Assert.assertFalse(referee1.addFoulEvent(game,"greenwood",""));
        Assert.assertFalse(referee1.addInjuryEvent(game,"mane"));
        Assert.assertFalse(referee1.addOffsideEvent(game,"salah"));
        Assert.assertFalse(referee1.addSubEvent(game,"greenwood","chong"));
        Assert.assertFalse(referee1.addCommentToGameEvents(game,"away crowd - racism calls"));

    }

    /**
     * Scenario 32.3 - update game events to game with wrong referee
     */
    @Test
    public void updateGameEventsWithWrongReferee(){
        //association
        Guest g0 = new Guest();
        g0.SignUp("gdfg", "zbvcxbidan", "fa", "fdgsfd", "4/6/1954", "fgfdssg", "1234", "Association","");
        g0.Login("1234", "fa");
        Fan f0 = database.getUser("fa");
        AssociationUser associationUser = (AssociationUser) f0;
        database.addSeason(2021,1000000);
        database.addLeagueAndPolicyToSeason(2021,"PremierLeague",new Date().toString(),3,0,
                1,true,false,12,12);
        associationUser.createSeasonAndBudget(2021,1000000);
        associationUser.addNewLeagueToSeason(2021,"PremierLeague",new Date().toString(),3,0,
                1,true,false,12,12);
        League leg = database.getLeagueByName("PremierLeague");

        //------------ManUtd----------------------

        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "fgfdssg", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember ownerGlazer = (TeamMember) f;
        TeamInfo manUtd = new TeamInfo("ManUtd", 1906, true, "Manchester", ownerGlazer);
        Court oldTraf = new Court("Old Trafford", "Manchester", 80000);
        ownerGlazer.setTeam(manUtd);
        ownerGlazer.AddCourt(oldTraf);
        try {
            ownerGlazer.AddIncoming(20000, "tickets");
        } catch (Exception e) {
            e.printStackTrace();
        }


        //coach
        Guest g1 = new Guest();
        g1.SignUp("gdfg", "zbvcxbidan", "oleGunar", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Coach","Head Coach");
        Fan f1 = database.getUser("oleGunar");
        TeamMember coach = (TeamMember) f1;
        ownerGlazer.AddCoach(coach, "", "head coach");

        //player
        Guest g_gk = new Guest();
        g_gk.SignUp("gdfg", "zbvcxbidan", "deGea", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_gk = database.getUser("deGea");
        TeamMember gk = (TeamMember) f_gk;
        ownerGlazer.AddPlayer(gk, "gk");

        //player
        Guest g_rb = new Guest();
        g_rb.SignUp("gdfg", "zbvcxbidan", "awb", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rb = database.getUser("awb");
        TeamMember rb = (TeamMember) f_rb;
        ownerGlazer.AddPlayer(rb, "rb");

        //player
        Guest g_rcb = new Guest();
        g_rcb.SignUp("gdfg", "zbvcxbidan", "meguire", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rcb = database.getUser("meguire");
        TeamMember rcb = (TeamMember) f_rcb;
        ownerGlazer.AddPlayer(rcb, "rcb");

        //player
        Guest g_lcb = new Guest();
        g_lcb.SignUp("gdfg", "zbvcxbidan", "baily", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lcb = database.getUser("baily");
        TeamMember lcb = (TeamMember) f_lcb;
        ownerGlazer.AddPlayer(lcb, "lcb");

        //player
        Guest g_lb = new Guest();
        g_lb.SignUp("gdfg", "zbvcxbidan", "shaw", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lb = database.getUser("shaw");
        TeamMember lb = (TeamMember) f_lb;
        ownerGlazer.AddPlayer(lb, "lb");

        //player
        Guest g_rm = new Guest();
        g_rm.SignUp("gdfg", "zbvcxbidan", "sancho", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rm = database.getUser("sancho");
        TeamMember rm = (TeamMember) f_rm;
        ownerGlazer.AddPlayer(rm, "rm");

        //player
        Guest g_rcm = new Guest();
        g_rcm.SignUp("gdfg", "zbvcxbidan", "pogba", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rcm = database.getUser("pogba");
        TeamMember rcm = (TeamMember) f_rcm;
        ownerGlazer.AddPlayer(rcm, "rcm");

        //player
        Guest g_lcm = new Guest();
        g_lcm.SignUp("gdfg", "zbvcxbidan", "bruno", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lcm = database.getUser("bruno");
        TeamMember lcm = (TeamMember) f_lcm;
        ownerGlazer.AddPlayer(lcm, "lcm");

        //player
        Guest g_lm = new Guest();
        g_lm.SignUp("gdfg", "zbvcxbidan", "martial", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lm = database.getUser("martial");
        TeamMember lm = (TeamMember) f_lm;
        ownerGlazer.AddPlayer(lm, "lm");

        //player
        Guest g_ls = new Guest();
        g_ls.SignUp("gdfg", "zbvcxbidan", "rashford", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_ls = database.getUser("rashford");
        TeamMember ls = (TeamMember) f_ls;
        ownerGlazer.AddPlayer(ls, "ls");

        //player
        Guest g_rs = new Guest();
        g_rs.SignUp("gdfg", "zbvcxbidan", "greenwood", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rs = database.getUser("greenwood");
        TeamMember rs = (TeamMember) f_rs;
        ownerGlazer.AddPlayer(rs, "rs");


        //------------Liverpool----------------------

        //owner
        Guest g111 = new Guest();
        g111.SignUp("gdfg", "zbvcxbidan", "fenway", "fdgsfd", "4/6/1954", "fgfdssg", "1234", "Owner","");
        g111.Login("1234", "fenway");
        Fan f111 = database.getUser("fenway");
        TeamMember fenway = (TeamMember) f111;
        TeamInfo liverpool = new TeamInfo("Liverpool", 1906, true, "Liverpool", fenway);
        Court anfield = new Court("Anfield", "Liverpool", 80000);
        fenway.setTeam(liverpool);
        fenway.AddCourt(anfield);
        try {
            fenway.AddIncoming(20000, "tickets");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //coach
        Guest g11 = new Guest();
        g11.SignUp("gdfg", "zbvcxbidan", "klop", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Coach","Head Coach");
        Fan f11 = database.getUser("klop");
        TeamMember coach1 = (TeamMember) f11;
        fenway.AddCoach(coach1, "", "head coach");

        //player
        Guest g_gk1 = new Guest();
        g_gk1.SignUp("gdfg", "zbvcxbidan", "allison", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_gk1 = database.getUser("allison");
        TeamMember gk1 = (TeamMember) f_gk1;
        fenway.AddPlayer(gk1, "gk");

        //player
        Guest g_rb1 = new Guest();
        g_rb1.SignUp("gdfg", "zbvcxbidan", "arnold", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rb1 = database.getUser("arnold");
        TeamMember rb1 = (TeamMember) f_rb1;
        fenway.AddPlayer(rb1, "rb");

        //player
        Guest g_rcb1 = new Guest();
        g_rcb1.SignUp("gdfg", "zbvcxbidan", "vandijk", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rcb1 = database.getUser("vandijk");
        TeamMember rcb1 = (TeamMember) f_rcb1;
        fenway.AddPlayer(rcb1, "rcb");

        //player
        Guest g_lcb1 = new Guest();
        g_lcb1.SignUp("gdfg", "zbvcxbidan", "gomez", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lcb1 = database.getUser("gomez");
        TeamMember lcb1 = (TeamMember) f_lcb1;
        fenway.AddPlayer(lcb1, "lcb");

        //player
        Guest g_lb1 = new Guest();
        g_lb1.SignUp("gdfg", "zbvcxbidan", "robertson", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lb1 = database.getUser("robertson");
        TeamMember lb1 = (TeamMember) f_lb1;
        fenway.AddPlayer(lb1, "lb");

        //player
        Guest g_rm1 = new Guest();
        g_rm1.SignUp("gdfg", "zbvcxbidan", "wijnaldum", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rm1 = database.getUser("wijnaldum");
        TeamMember rm1 = (TeamMember) f_rm1;
        fenway.AddPlayer(rm1, "rm");

        //player
        Guest g_rcm1 = new Guest();
        g_rcm1.SignUp("gdfg", "zbvcxbidan", "fabinho", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rcm1 = database.getUser("fabinho");
        TeamMember rcm1 = (TeamMember) f_rcm1;
        fenway.AddPlayer(rcm1, "rcm");

        //player
        Guest g_lcm1 = new Guest();
        g_lcm1.SignUp("gdfg", "zbvcxbidan", "henderson", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lcm1 = database.getUser("henderson");
        TeamMember lcm1 = (TeamMember) f_lcm1;
        fenway.AddPlayer(lcm1, "lcm");

        //player
        Guest g_lm1 = new Guest();
        g_lm1.SignUp("gdfg", "zbvcxbidan", "mane", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lm1 = database.getUser("mane");
        TeamMember lm1 = (TeamMember) f_lm1;
        fenway.AddPlayer(lm1, "lm");

        //player
        Guest g_ls1 = new Guest();
        g_ls1.SignUp("gdfg", "zbvcxbidan", "firmino", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_ls1 = database.getUser("firmino");
        TeamMember ls1 = (TeamMember) f_ls1;
        fenway.AddPlayer(ls1, "ls");

        //player
        Guest g_rs1 = new Guest();
        g_rs1.SignUp("gdfg", "zbvcxbidan", "salah", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rs1 = database.getUser("salah");
        TeamMember rs1 = (TeamMember) f_rs1;
        fenway.AddPlayer(rs1, "rs");

        //Refs
        Guest r1 = new Guest();
        r1.SignUp("gdfg", "zbvcxbidan", "ref1", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Referee","Main Referee");
        Fan re1 = database.getUser("ref1");

        Guest r2 = new Guest();
        r2.SignUp("gdfg", "zbvcxbidan", "ref2", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Referee","Main Referee");
        Fan re2 = database.getUser("ref2");

        //-----------------------------------------------------------------------------------------------------
        //-----------------------------------------------------------------------------------------------------
        //-----------------------------------------------------------------------------------------------------

        Assert.assertTrue(ownerGlazer.registerTheTeamToLeague("PremierLeague"));
        //fenway.registerTheTeamToLeague("PremierLeague");
        Assert.assertTrue(fenway.registerTheTeamToLeague("PremierLeague"));

        Assert.assertTrue(associationUser.NominateReferee((Referee)re1));
        Assert.assertTrue(associationUser.addRefereeToLeague(leg, (Referee)re1));

        Assert.assertTrue(associationUser.NominateReferee((Referee)re2));
        Assert.assertTrue(associationUser.addRefereeToLeague(leg, (Referee)re2));

        Referee referee1 = (Referee)database.getUser("ref1");
        Referee referee2 = (Referee)database.getUser("ref2");
        Match game = new Match(manUtd,liverpool,manUtd.getTeamCourt(),new Date(),referee1);

        Assert.assertFalse(referee2.addGoalEvent(game,"greenwood","home"));
        Assert.assertFalse(referee2.addFoulEvent(game,"greenwood",""));
        Assert.assertFalse(referee2.addInjuryEvent(game,"mane"));
        Assert.assertFalse(referee2.addOffsideEvent(game,"salah"));
        Assert.assertFalse(referee2.addSubEvent(game,"greenwood","chong"));
        Assert.assertFalse(referee2.addCommentToGameEvents(game,"away crowd - racism calls"));

    }

/**
 * ------------------------------------------------------------------------------------------------------------------
 * ------------------------------------ Use case num 33 - update game events after it ends --------------------------------
 * ------------------------------------------------------------------------------------------------------------------
 */


    /**
     * Scenario 33.1 - update game events
     */
    @Test
    public void editGameEvents(){
        //association
        Guest g0 = new Guest();
        g0.SignUp("gdfg", "zbvcxbidan", "fa", "fdgsfd", "4/6/1954", "fgfdssg", "1234", "Association","");
        g0.Login("1234", "fa");
        Fan f0 = database.getUser("fa");
        AssociationUser associationUser = (AssociationUser) f0;
        database.addSeason(2021,1000000);
        database.addLeagueAndPolicyToSeason(2021,"PremierLeague","23/05/2020",3,0,
                1,true,false,12,12);
        associationUser.createSeasonAndBudget(2021,1000000);
        associationUser.addNewLeagueToSeason(2021,"PremierLeague","23/05/2020",3,0,
                1,true,false,12,12);
        League leg = database.getLeagueByName("PremierLeague");

        //------------ManUtd----------------------

        //owner
        Guest g = new Guest();
        g.SignUp("gdfg", "zbvcxbidan", "glazer", "fdgsfd", "4/6/1954", "fgfdssg", "1234", "Owner","");
        g.Login("1234", "glazer");
        Fan f = database.getUser("glazer");
        TeamMember ownerGlazer = (TeamMember) f;
        TeamInfo manUtd = new TeamInfo("ManUtd", 1906, true, "Manchester", ownerGlazer);
        Court oldTraf = new Court("Old Trafford", "Manchester", 80000);
        ownerGlazer.setTeam(manUtd);
        ownerGlazer.AddCourt(oldTraf);
        try {
            ownerGlazer.AddIncoming(20000, "tickets");
        } catch (Exception e) {
            e.printStackTrace();
        }


        //coach
        Guest g1 = new Guest();
        g1.SignUp("gdfg", "zbvcxbidan", "oleGunar", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Coach","Head Coach");
        Fan f1 = database.getUser("oleGunar");
        TeamMember coach = (TeamMember) f1;
        ownerGlazer.AddCoach(coach, "", "head coach");

        //player
        Guest g_gk = new Guest();
        g_gk.SignUp("gdfg", "zbvcxbidan", "deGea", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_gk = database.getUser("deGea");
        TeamMember gk = (TeamMember) f_gk;
        ownerGlazer.AddPlayer(gk, "gk");

        //player
        Guest g_rb = new Guest();
        g_rb.SignUp("gdfg", "zbvcxbidan", "awb", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rb = database.getUser("awb");
        TeamMember rb = (TeamMember) f_rb;
        ownerGlazer.AddPlayer(rb, "rb");

        //player
        Guest g_rcb = new Guest();
        g_rcb.SignUp("gdfg", "zbvcxbidan", "meguire", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rcb = database.getUser("meguire");
        TeamMember rcb = (TeamMember) f_rcb;
        ownerGlazer.AddPlayer(rcb, "rcb");

        //player
        Guest g_lcb = new Guest();
        g_lcb.SignUp("gdfg", "zbvcxbidan", "baily", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lcb = database.getUser("baily");
        TeamMember lcb = (TeamMember) f_lcb;
        ownerGlazer.AddPlayer(lcb, "lcb");

        //player
        Guest g_lb = new Guest();
        g_lb.SignUp("gdfg", "zbvcxbidan", "shaw", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lb = database.getUser("shaw");
        TeamMember lb = (TeamMember) f_lb;
        ownerGlazer.AddPlayer(lb, "lb");

        //player
        Guest g_rm = new Guest();
        g_rm.SignUp("gdfg", "zbvcxbidan", "sancho", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rm = database.getUser("sancho");
        TeamMember rm = (TeamMember) f_rm;
        ownerGlazer.AddPlayer(rm, "rm");

        //player
        Guest g_rcm = new Guest();
        g_rcm.SignUp("gdfg", "zbvcxbidan", "pogba", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rcm = database.getUser("pogba");
        TeamMember rcm = (TeamMember) f_rcm;
        ownerGlazer.AddPlayer(rcm, "rcm");

        //player
        Guest g_lcm = new Guest();
        g_lcm.SignUp("gdfg", "zbvcxbidan", "bruno", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lcm = database.getUser("bruno");
        TeamMember lcm = (TeamMember) f_lcm;
        ownerGlazer.AddPlayer(lcm, "lcm");

        //player
        Guest g_lm = new Guest();
        g_lm.SignUp("gdfg", "zbvcxbidan", "martial", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lm = database.getUser("martial");
        TeamMember lm = (TeamMember) f_lm;
        ownerGlazer.AddPlayer(lm, "lm");

        //player
        Guest g_ls = new Guest();
        g_ls.SignUp("gdfg", "zbvcxbidan", "rashford", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_ls = database.getUser("rashford");
        TeamMember ls = (TeamMember) f_ls;
        ownerGlazer.AddPlayer(ls, "ls");

        //player
        Guest g_rs = new Guest();
        g_rs.SignUp("gdfg", "zbvcxbidan", "greenwood", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rs = database.getUser("greenwood");
        TeamMember rs = (TeamMember) f_rs;
        ownerGlazer.AddPlayer(rs, "rs");


        //------------Liverpool----------------------

        //owner
        Guest g111 = new Guest();
        g111.SignUp("gdfg", "zbvcxbidan", "fenway", "fdgsfd", "4/6/1954", "fgfdssg", "1234", "Owner","");
        g111.Login("1234", "fenway");
        Fan f111 = database.getUser("fenway");
        TeamMember fenway = (TeamMember) f111;
        TeamInfo liverpool = new TeamInfo("Liverpool", 1906, true, "Liverpool", fenway);
        Court anfield = new Court("Anfield", "Liverpool", 80000);
        fenway.setTeam(liverpool);
        fenway.AddCourt(anfield);
        try {
            fenway.AddIncoming(20000, "tickets");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //coach
        Guest g11 = new Guest();
        g11.SignUp("gdfg", "zbvcxbidan", "klop", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Coach","Head Coach");
        Fan f11 = database.getUser("klop");
        TeamMember coach1 = (TeamMember) f11;
        fenway.AddCoach(coach1, "", "head coach");

        //player
        Guest g_gk1 = new Guest();
        g_gk1.SignUp("gdfg", "zbvcxbidan", "allison", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_gk1 = database.getUser("allison");
        TeamMember gk1 = (TeamMember) f_gk1;
        fenway.AddPlayer(gk1, "gk");

        //player
        Guest g_rb1 = new Guest();
        g_rb1.SignUp("gdfg", "zbvcxbidan", "arnold", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rb1 = database.getUser("arnold");
        TeamMember rb1 = (TeamMember) f_rb1;
        fenway.AddPlayer(rb1, "rb");

        //player
        Guest g_rcb1 = new Guest();
        g_rcb1.SignUp("gdfg", "zbvcxbidan", "vandijk", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rcb1 = database.getUser("vandijk");
        TeamMember rcb1 = (TeamMember) f_rcb1;
        fenway.AddPlayer(rcb1, "rcb");

        //player
        Guest g_lcb1 = new Guest();
        g_lcb1.SignUp("gdfg", "zbvcxbidan", "gomez", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lcb1 = database.getUser("gomez");
        TeamMember lcb1 = (TeamMember) f_lcb1;
        fenway.AddPlayer(lcb1, "lcb");

        //player
        Guest g_lb1 = new Guest();
        g_lb1.SignUp("gdfg", "zbvcxbidan", "robertson", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lb1 = database.getUser("robertson");
        TeamMember lb1 = (TeamMember) f_lb1;
        fenway.AddPlayer(lb1, "lb");

        //player
        Guest g_rm1 = new Guest();
        g_rm1.SignUp("gdfg", "zbvcxbidan", "wijnaldum", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rm1 = database.getUser("wijnaldum");
        TeamMember rm1 = (TeamMember) f_rm1;
        fenway.AddPlayer(rm1, "rm");

        //player
        Guest g_rcm1 = new Guest();
        g_rcm1.SignUp("gdfg", "zbvcxbidan", "fabinho", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rcm1 = database.getUser("fabinho");
        TeamMember rcm1 = (TeamMember) f_rcm1;
        fenway.AddPlayer(rcm1, "rcm");

        //player
        Guest g_lcm1 = new Guest();
        g_lcm1.SignUp("gdfg", "zbvcxbidan", "henderson", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lcm1 = database.getUser("henderson");
        TeamMember lcm1 = (TeamMember) f_lcm1;
        fenway.AddPlayer(lcm1, "lcm");

        //player
        Guest g_lm1 = new Guest();
        g_lm1.SignUp("gdfg", "zbvcxbidan", "mane", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_lm1 = database.getUser("mane");
        TeamMember lm1 = (TeamMember) f_lm1;
        fenway.AddPlayer(lm1, "lm");

        //player
        Guest g_ls1 = new Guest();
        g_ls1.SignUp("gdfg", "zbvcxbidan", "firmino", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_ls1 = database.getUser("firmino");
        TeamMember ls1 = (TeamMember) f_ls1;
        fenway.AddPlayer(ls1, "ls");

        //player
        Guest g_rs1 = new Guest();
        g_rs1.SignUp("gdfg", "zbvcxbidan", "salah", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Player","CF");
        Fan f_rs1 = database.getUser("salah");
        TeamMember rs1 = (TeamMember) f_rs1;
        fenway.AddPlayer(rs1, "rs");

        //Refs
        Guest r1 = new Guest();
        r1.SignUp("gdfg", "zbvcxbidan", "ref1", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Referee","Main Referee");
        Fan re1 = database.getUser("ref1");

        Guest r2 = new Guest();
        r1.SignUp("gdfg", "zbvcxbidan", "ref2", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Referee","Side Referee");
        Fan re2 = database.getUser("ref2");

        Guest r3 = new Guest();
        r1.SignUp("gdfg", "zbvcxbidan", "ref3", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Referee","Side Referee");
        Fan re3 = database.getUser("ref3");

        Guest r4 = new Guest();
        r1.SignUp("gdfg", "zbvcxbidan", "ref4", "fdgsfd", "cgfd", "4/6/1954", "fdgs@gmail.com", "Referee","Side Referee");
        Fan re4 = database.getUser("ref4");
        //-----------------------------------------------------------------------------------------------------
        //-----------------------------------------------------------------------------------------------------
        //-----------------------------------------------------------------------------------------------------

        ownerGlazer.registerTheTeamToLeague("PremierLeague");
        fenway.registerTheTeamToLeague("PremierLeague");
        leg.addTeam(manUtd);
        leg.addTeam(liverpool);
//        Assert.assertTrue(ownerGlazer.registerTheTeamToLeague("PremierLeague"));
//        //fenway.registerTheTeamToLeague("PremierLeague");
//        Assert.assertTrue(fenway.registerTheTeamToLeague("PremierLeague"));

        Assert.assertTrue(associationUser.NominateReferee((Referee)re1));
        Assert.assertTrue(associationUser.NominateReferee((Referee)re2));
        Assert.assertTrue(associationUser.NominateReferee((Referee)re3));
        Assert.assertTrue(associationUser.NominateReferee((Referee)re4));
        Assert.assertTrue(associationUser.addRefereeToLeague(leg, (Referee)re1));
        associationUser.addRefereeToLeague(leg, (Referee)re2);
        Assert.assertTrue(associationUser.addRefereeToLeague(leg, (Referee)re2));
        Assert.assertTrue(associationUser.addRefereeToLeague(leg, (Referee)re3));
        Assert.assertTrue(associationUser.addRefereeToLeague(leg, (Referee)re4));
        associationUser.updateGameScheduling(leg,2);
        Assert.assertEquals(leg.getFixtures().size(),2);
        Referee referee1 = (Referee)database.getUser("ref1");
        Match game = new Match(manUtd,liverpool,manUtd.getTeamCourt(),new Date(),referee1);


        referee1.EditGameEvents(game, "Good Game");
        Assert.assertEquals("Good Game" , game.getEvents().getAllGameEvents().get(0));


    }

/**
 * ------------------------------------------------------------------------------------------------------------------
 * ------------------------------------ Use case num 34 - update num of rounds in league --------------------------------
 * ------------------------------------------------------------------------------------------------------------------
 */


    /**
     * Scenario 34.1 - update game events
     */
    @Test
    public void setGameSchedulingPolicy(){
        //association
        Guest g0 = new Guest();
        g0.SignUp("gdfg", "zbvcxbidan", "fa", "fdgsfd", "4/6/1954", "fgfdssg", "1234", "Association","");
        g0.Login("1234", "fa");
        Fan f0 = database.getUser("fa");
        database.addSeason(2021,1000000);
        database.addLeagueAndPolicyToSeason(2021,"PremierLeague",new Date().toString(),3,0,
                1,true,false,12,12);
        AssociationUser associationUser = (AssociationUser) f0;
        associationUser.createSeasonAndBudget(2021,1000000);
        associationUser.addNewLeagueToSeason(2021,"PremierLeague",new Date().toString(),3,0,
                1,true,false,12,12);
        League league = database.getLeagueByName("PremierLeague");


        associationUser.updateGameScheduling(league , 2);
        //League league = database.getLeagueByName("PremierLeague");
        Assert.assertEquals(2, league.getNumOfRounds());

        associationUser.updateGameScheduling(league , 1);
        Assert.assertEquals(1, league.getNumOfRounds(),1);



    }

    /**
     * Scenario 34.2 - set Wrong Game Scheduling Policy
     */
    @Test
    public void setWrongGameSchedulingPolicy(){
        //association
        Guest g0 = new Guest();
        g0.SignUp("gdfg", "zbvcxbidan", "fa", "fdgsfd", "4/6/1954", "fgfdssg", "1234", "Association","");
        g0.Login("1234", "fa");
        Fan f0 = database.getUser("fa");
        g0.SignUp("avi", "cohen", "avic", "fdgsfd", "4/6/1954", "fgfdssg", "1234", "Owner","");
        Fan f1 = database.getUser("avic");
        TeamMember tm = (TeamMember)f1;
        database.addTeam("ManUtd", 1906, true, "Manchester", tm.getUserName(),"");
        g0.SignUp("gal", "cohen", "galc", "fdgsfd", "4/6/1954", "fgfdssg", "1234", "Owner","");
        Fan f2 = database.getUser("galc");
        TeamMember tm2 = (TeamMember)f2;
        database.addTeam("liv", 1906, true, "Manchester", tm2.getUserName(),"");
        TeamInfo team1= database.getTeam("ManUtd");
        TeamInfo team2= database.getTeam("liv");
        database.addSeason(2021,1000000);
        database.addLeagueAndPolicyToSeason(2021,"PremierLeague",new Date().toString(),3,0,
                1,true,false,12,12);
        AssociationUser associationUser = (AssociationUser) f0;
        associationUser.createSeasonAndBudget(2021,1000000);
        associationUser.addNewLeagueToSeason(2021,"PremierLeague",new Date().toString(),3,0,
                1,true,false,12,12);
        League league = database.getLeagueByName("PremierLeague");
        AssociationUser.addTeamToLeague(team1,"PremierLeague");
        AssociationUser.addTeamToLeague(team2,"PremierLeague");


        associationUser.updateGameScheduling(league , 2);

        Assert.assertEquals(2, league.getNumOfRounds());

        associationUser.updateGameScheduling(league , 3);
        Assert.assertEquals(2, league.getNumOfRounds());
    }

/**
 * ------------------------------------------------------------------------------------------------------------------
 * ------------------------------------ Use case num 36 - define season --------------------------------
 * ------------------------------------------------------------------------------------------------------------------
 */


    /**
     * Scenario 36.1 - define season
     */
    @Test
    public void defineSeason(){
        //association
        Guest g0 = new Guest();
        g0.SignUp("gdfg", "zbvcxbidan", "fa", "fdgsfd", "4/6/1954", "fgfdssg", "1234", "Association","");
        g0.Login("1234", "fa");
        Fan f0 = database.getUser("fa");
        database.addSeason(2021,1000000);
        database.addLeagueAndPolicyToSeason(2021,"PremierLeague",new Date().toString(),3,0,
                1,true,false,12,12);
        AssociationUser associationUser = (AssociationUser) f0;
        associationUser.createSeasonAndBudget(2021,1000000);


        Assert.assertEquals(2021 , database.getSeasonByYear(2021).getYear());

    }

}


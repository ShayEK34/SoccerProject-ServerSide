package Data.SystemDB;
import Domain.ClubManagement.Court;
import Domain.ClubManagement.TeamInfo;
import Domain.User.Fan;
import Domain.User.SystemManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MdbTestIT {
    public UserDaoMdb database;
    //check

    @Before
    public void init(){
        database = UserDaoMdb.getInstance();
    }

    /**
     //     * Gal insert to DB
     //     */

//    @Test
//    public void updateAlert(){
//        database.updateAlertDetails("gavri","Register Team - TeamName: Liverpool, ToLeague: Premier League");
//    }
//    @Test
//    public void updateStatus(){
//        database.updateUserDetails("glazer",true,"users","AssignToAlerts");
//    }
//    @Test
//    public void insertUsersForLeagueToDB(){
//
//        /**
//         * Man United
//         */
//
//        /**
//         * court
//         * */
//        database.addCourt("Old Trafford","Manchester",76813);
//
//        /**
//         * team
//         * */
//        database.addTeam("Manchester United",1892,true,"Manchester"
//                ,"glazer","Old Trafford");
//
//
//
//
//        /**
//         * owner
//         */
//
//        database.addUser("gdfg", "zbvcxbidan", "glazer", "111",
//                "TeamMember", "01/05/2015", "gfsdg@fdsg.gf", "Owner","");
//
//        database.updateUserDetails("glazer","Manchester United","owners","CurrentTeam");
//
//        /**
//         * team managers
//         * */
//        database.addUser("Gal","Buzaglo","GalBuz","111","TeamMember",
//                "18/7/1991","galbuz@gmail.com","TeamManager","");
//
//        database.updateUserDetails("GalBuz","Manchester United","team managers","CurrentTeam");
//
//        /**
//         * coach
//         */
//        database.addUser("gdfg", "zbvcxbidan", "oleGunar", "111", "TeamMember",
//                "4/6/1954", "fdgs@gmail.com", "Coach","Head Coach");
//
//        database.updateUserDetails("oleGunar","Manchester United","coaches","CurrentTeam");
//
//        /**
//         * players
//         */
//
//        database.addUser("david","degea","degea","111","TeamMember",
//                "24/6/1987","leomessi@gmail.com","Player","GK");
//
//        database.updateUserDetails("degea","Manchester United","players","CurrentTeam");
//
//        /**
//         * defence
//         */
//
//        database.addUser("aaron","wb","awb","111","TeamMember",
//                "24/6/1987","leomessi@gmail.com","Player","RB");
//
//        database.updateUserDetails("awb","Manchester United","players","CurrentTeam");
//
//        database.addUser("harry","mag","harry","111","TeamMember",
//                "24/6/1987","leomessi@gmail.com","Player","CB");
//
//        database.updateUserDetails("harry","Manchester United","players","CurrentTeam");
//
//        database.addUser("eric","bailly","bailly","111","TeamMember",
//                "24/6/1987","leomessi@gmail.com","Player","CB");
//
//        database.updateUserDetails("bailly","Manchester United","players","CurrentTeam");
//
//        database.addUser("luke","shaw","shaw","111","TeamMember",
//                "24/6/1987","leomessi@gmail.com","Player","LB");
//
//        database.updateUserDetails("shaw","Manchester United","players","CurrentTeam");
//
//        /**
//         * midfield
//         */
//
//        database.addUser("fred","fred","fred","111","TeamMember",
//                "24/6/1987","leomessi@gmail.com","Player","CM");
//
//        database.updateUserDetails("fred","Manchester United","players","CurrentTeam");
//
//        database.addUser("scot","mactominay","scottie","111","TeamMember",
//                "24/6/1987","leomessi@gmail.com","Player","CDM");
//
//        database.updateUserDetails("scottie","Manchester United","players","CurrentTeam");
//
//        database.addUser("bruno","fernandes","bruno","111","TeamMember",
//                "24/6/1987","leomessi@gmail.com","Player","CAM");
//
//        database.updateUserDetails("bruno","Manchester United","players","CurrentTeam");
//
//        /**
//         * attack
//         */
//        database.addUser("marcus","rashford","rashi","111","TeamMember",
//                "24/6/1987","leomessi@gmail.com","Player","ST");
//
//        database.updateUserDetails("rashi","Manchester United","players","CurrentTeam");
//
//        database.addUser("daniel","james","dan","111","TeamMember",
//                "24/6/1987","leomessi@gmail.com","Player","RW");
//
//        database.updateUserDetails("dan","Manchester United","players","CurrentTeam");
//
//        database.addUser("anthony","martial","martial","111","TeamMember",
//                "24/6/1987","leomessi@gmail.com","Player","LW");
//
//        database.updateUserDetails("martial","Manchester United","players","CurrentTeam");
//
//
//
//
//
//
//
//
//        /**
//         * Liverpool
//         */
//
//        /**
//         * court
//         * */
//        database.addCourt("Anfield","Liverpool",76813);
//
//        /**
//         * team
//         * */
//        database.addTeam("Liverpool",1892,true,"Liverpool"
//                ,"fenway","Anfield");
//
//
//        /**
//         * owner
//         */
//
//        database.addUser("gdfg", "zbvcxbidan", "fenway", "111",
//                "TeamMember", "01/05/2015", "gfsdg@fdsg.gf", "Owner","");
//
//        database.updateUserDetails("fenway","Liverpool","owners","CurrentTeam");
//
//        /**
//         * team managers
//         * */
//        database.addUser("fsdg","dsfgsdg","jimmy","111","TeamMember",
//                "18/7/1991","galbuz@gmail.com","TeamManager","");
//
//        database.updateUserDetails("jimmy","Liverpool","team managers","CurrentTeam");
//
//        /**
//         * coach
//         */
//        database.addUser("gdfg", "zbvcxbidan", "klopp", "111", "TeamMember",
//                "4/6/1954", "fdgs@gmail.com", "Coach","Head Coach");
//
//        database.updateUserDetails("klopp","Liverpool","coaches","CurrentTeam");
//
//        /**
//         * players
//         */
//
//        database.addUser("alison","alison","alison","111","TeamMember",
//                "24/6/1987","leomessi@gmail.com","Player","GK");
//
//        database.updateUserDetails("alison","Liverpool","players","CurrentTeam");
//
//        /**
//         * defance
//         */
//
//        database.addUser("trent","AA","taa","111","TeamMember",
//                "24/6/1987","leomessi@gmail.com","Player","RB");
//
//        database.updateUserDetails("taa","Liverpool","players","CurrentTeam");
//
//        database.addUser("virgil","van-dijk","virgil","111","TeamMember",
//                "24/6/1987","leomessi@gmail.com","Player","CB");
//
//        database.updateUserDetails("virgil","Liverpool","players","CurrentTeam");
//
//        database.addUser("joe","gomez","gomez","111","TeamMember",
//                "24/6/1987","leomessi@gmail.com","Player","CB");
//
//        database.updateUserDetails("gomez","Liverpool","players","CurrentTeam");
//
//        database.addUser("andy","robertson","robertson","111","TeamMember",
//                "24/6/1987","leomessi@gmail.com","Player","LB");
//
//        database.updateUserDetails("robertson","Liverpool","players","CurrentTeam");
//
//        /**
//         * midfield
//         */
//
//        database.addUser("jordan","henderson","henderson","111","TeamMember",
//                "24/6/1987","leomessi@gmail.com","Player","CM");
//
//        database.updateUserDetails("henderson","Liverpool","players","CurrentTeam");
//
//        database.addUser("fabinho","fabinho","fabinho","111","TeamMember",
//                "24/6/1987","leomessi@gmail.com","Player","CDM");
//
//        database.updateUserDetails("fabinho","Liverpool","players","CurrentTeam");
//
//        database.addUser("georginho","wijnaldom","wijnaldom","111","TeamMember",
//                "24/6/1987","leomessi@gmail.com","Player","CAM");
//
//        database.updateUserDetails("wijnaldom","Liverpool","players","CurrentTeam");
//
//        /**
//         * attack
//         */
//        database.addUser("bobby","firmino","firmino","111","TeamMember",
//                "24/6/1987","leomessi@gmail.com","Player","ST");
//
//        database.updateUserDetails("firmino","Liverpool","players","CurrentTeam");
//
//        database.addUser("mo","salah","salah","111","TeamMember",
//                "24/6/1987","leomessi@gmail.com","Player","RW");
//
//        database.updateUserDetails("salah","Liverpool","players","CurrentTeam");
//
//        database.addUser("sadio","mane","mane","111","TeamMember",
//                "24/6/1987","leomessi@gmail.com","Player","LW");
//
//        database.updateUserDetails("mane","Liverpool","players","CurrentTeam");
//
//
//        /**
//         * referees
//         */
//        database.addUser("mike","dean","dean","111","Referee",
//            "18/7/1991","meir@gmail.com","Referee","Main Referee");
//
//        database.addUser("michael","oliver","oliver","111","Referee",
//                "18/7/1991","meir@gmail.com","Referee","Side Referee");
//
//        database.addUser("martin","atkinson","atkinson","111","Referee",
//                "18/7/1991","meir@gmail.com","Referee","Side Referee");
//
//        database.addUser("anthony","taylor","taylor","111","Referee",
//                "18/7/1991","meir@gmail.com","Referee","Side Referee");

//            database.addUser("dfsggdf","tdfgsgdaylor","ahmed","111","Referee",
//                "18/7/1991","meir@gmail.com","Referee","Side Referee");
//
//
//        /**
//         * association user
//         */
//
//        database.addUser("gavri","levi","gavri","111","Association",
//                "18/7/1991","meir@gmail.com","Association","");
//
//
//
//
//
//    }


    /**
     * -----------------------------------------------------------------------------------------------------------
     */
//        @Test
//        public void checkAddUserSystemManagerToDB(){
////            database.addUser("admin","admin","God","IAmGod"
//////                    ,"SystemManager","0/0/0","note@westrenWall.god.il","","");
//////        System.out.println("check");
//            database.updateUserDetails("glazer","","owners","CurrentTeam");
//            database.updateUserDetails("fenway","","owners","CurrentTeam");
//
//    }
//    @Test
//    public void checkAddUsersToDB(){
//////        database.addUser("Shay","EK","Shayek","2609","TeamMember",
//////                "31/8/1991","shayek34@gmail.com","Owner");
//////        database.addUser("Leo","Messi ","LeoMessi10","1010","TeamMember",
//////                "24/6/1987","leomessi@gmail.com","Player");
//////        database.addUser("Jürgen ","Klopp","JKlopp","7777","TeamMember",
//////                "16/6/1967","Klopp1@gmail.com","Coach");
//////        database.addUser("Gal","Buzaglo","GalBuz","1807","TeamMember",
//////                "18/7/1991","galbuz@gmail.com","TeamManager");
//////        System.out.println("check");
//            database.addUser("Meir","Shitrit","Mshit","5555","Referee",
//            "18/7/1991","meir@gmail.com","Referee");
//        System.out.println("check");
//
//}

//        @Test
//        public void checkIfUserExistInDB(){
//            database.isUserExist("GalBuz");
//            System.out.println("check");
//
//    }

//        @Test
//        public void checkGetUserFromDB(){
//            Fan f1 = database.getUser("Shayek");
//            System.out.println("check");
//            Fan f2 = database.getUser("GalBuz");
//            System.out.println("check");
//            Fan f3 = database.getUser("LeoMessi10");
//            System.out.println("check");
//            Fan f4 = database.getUser("JKlopp");
//            System.out.println("check");
//        Assert.assertEquals(f1.getFirstName(),"Shay");
//        Assert.assertEquals(f1.getLastName(),"EK");
//        Assert.assertEquals(f2.getFirstName(),"Gal");
//        Assert.assertNotNull(f1);
//        Assert.assertNotNull(f2);
//        Assert.assertNotNull(f3);
//        Assert.assertNotNull(f4);
//    }

//    @Test
//    public void checkRemoveUserFromDB(){
////        Assert.assertTrue(database.removeUser("ShayEK"));
//        database.removeUser("dean");
//        database.removeUser("atkinson");
//        database.removeUser("oliver");
////        database.removeUser("Mshit");
//        database.removeUser("taylor");
//        System.out.println("check");
//    }

    //Team Tests
//    @Test
//    public void checkAddTeamToDB(){
//
//
//        /**
//         * team
//         * */
//        database.addTeam("Manchester United",1892,true,"Manchester"
//                ,"glazer","Old Trafford");
//
//                database.addTeam("Liverpool",1892,true,"Liverpool"
//                ,"fenway","Anfield");
//    }

//    @Test
//    public void checkIfTeamExistInDB(){
//        database.isTeamExist("Real Madrid");
//        System.out.println("check");
//    }

//    @Test
//    public void checkGetTeamFromDB(){
//       TeamInfo team1 = database.getTeam("Real Madrid");
//       Assert.assertNotNull(team1);
//    }

//    @Test
//    public void checkRemoveTeamFromDB(){
//         database.removeTeam("Real Madrid");
//         System.out.println("check");
//    }
//    @Test
//    public void checkAddCourtToDB(){
//        database.addCourt("Santiago Bernabéu","Madrid",81044);
//        System.out.println("check");
//    }
//    @Test
//    public void checkIfCourtExistInDB(){
//       database.isCourtExist("Santiago Bernabéu");
//       System.out.println("check");
//    }

//     @Test
//     public void checkGetCourtFromDB(){
//        Court c1 = database.getCourt("Santiago Bernabéu");
//        Court c2 = database.getCourt("Old Trafford");
//        Assert.assertNotNull(c1);
//        Assert.assertNull(c2);
//    }

//    @Test
//    public void checkRemoveCourtFromDB(){
//        database.removeCourt("Santiago Bernabéu");
//        Assert.assertFalse(database.removeCourt("Old Trafford"));
//    }

//    @Test
//    public void checkUpdatedValue(){
//            database.updateUserDetails("Shayek","shay@walla.com","users","Email");
//        System.out.println("check");
//
//    }

//    @Test
//    public void insertCourtsToDB(){
////        database.addCourt("Camp Nou","Barcelona",99354);
////        database.addCourt("Wembley","London",90000);
////        database.addCourt("Allianz Arena","Munich",75024);
////        database.addCourt("Santiago Bernabeu","Madrid",81044);
////        database.addCourt("Stadio Olimpico","Rome",72698);
////        database.addCourt("Metropolitano","Madrid",67703);
////        database.addCourt("Parc des Princes","Paris",48713);
////        database.addCourt("San Siro","Milano",75923);
////        database.addCourt("Etihad","Manchester",55097);
//    }

//    @Test
//    public void insertTeamsToLeagueToDB(){
//        database.addTeamToLeague("Liverpool", 2021 ,"Premier League");
//        database.addTeamToLeague("Manchester United", 2021 ,"Premier League");
//    }

//    @Test
//    public void insertAlertsToDB(){
//        database.addUserAlert("gavri","TeamAlert","Register Team - TeamName- Liverpool, ToLeague- Premier League",false);
//        database.addUserAlert("gavri","TeamAlert","Register Team - TeamName- Manchester United, ToLeague- Premier League",false);
//    }

}


package Domain;
//domain
//import Data.SystemDB.DB;
//import Data.SystemDB.UserDaoMdb;
//import Domain.AlertSystem.AlertSystem;
//import Domain.AssociationManagement.League;
//import Domain.AssociationManagement.Match;
//import Domain.AssociationManagement.Season;
//import Domain.ClubManagement.TeamInfo;
//import Domain.User.*;

import Data.SystemDB.UserDaoMdb;
import Domain.AlertSystem.*;
import Domain.ClubManagement.TeamInfo;
import Domain.Systems.SystemErrorLogs;
import Domain.Systems.SystemEventsLog;
import Domain.AssociationManagement.League;
import Domain.AssociationManagement.Match;
import Domain.ClubManagement.TeamInfo;
import Domain.User.*;
import Domain.User.Referee;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Vector;

public class Model extends Observable {

    private UserDaoMdb db;
    private AlertSystem alertSystem;
    private static ArrayList<String> allLoginUser=new ArrayList<String>();
//    private TeamMember tm;
//    private AssociationUser au;
//    private Referee ref;
//    private SystemManager sys;
//    private Fan fan;
//

    private int currentSeasonYear;

    //
    public Model() {
        db = UserDaoMdb.getInstance();
        currentSeasonYear = db.getTheCurrentSeason();
        alertSystem = AlertSystem.getInstance();
    }
//
//    /**
//     * =============================================================================================
//     * =======================================  Register ===========================================
//     * =============================================================================================
//     **/

    public String addUser(String firstName, String lastName, String userName, String password, String occupation, String birthday, String email, String verificationCode, String role) {
        if (!db.isUserExist(userName)) {
            SystemManager systemManager = (SystemManager) db.getUser("God");
            systemManager.addNewUserToDB(firstName, lastName, userName, password, occupation, birthday, email, verificationCode, role);
            return "User added successfully";
        } else {
            return "user already exist";
        }
    }

    public String loginUser(String username, String password) {
        String res="";
        Fan user = db.getUser(username);
        if(user ==null){
            return"login failed, user doesn't exist";
        }
        else{
            if(user.getPassword().equals(password)) {
                allLoginUser.add(username);
                if (user.getOccupation().equals("TeamMember")) {
                    db.updateUserDetails(username,false,"users","AssignToAlerts");
                    res= "TeamMember";
                    if(((TeamMember)user).isCoach())
                        res=res+":"+"true";
                    else
                        res=res+":"+"false";
                    if(((TeamMember)user).isPlayer())
                        res=res+":"+"true";
                    else
                        res=res+":"+"false";
                    if(((TeamMember)user).isOwner())
                        res=res+":"+"true";
                    else
                        res=res+":"+"false";
                    if(((TeamMember)user).isTeamManager())
                        res=res+":"+"true";
                        //+":"+((TeamMember)user).isCoachPermission()+":"+((TeamMember)user).isOwnerPermission()+":"+((TeamMember)user).isPlayerPermission()+":"+((TeamMember)user).isTeamManagerPermission();
                    else
                        res=res+":"+"false";
                    if(((TeamMember)user).getTeam()!=null)
                        res=res+":"+((TeamMember)user).getTeam().getTeamName()+":"+((TeamMember)user).getTeam().isTeamActiveStatus();
                    else
                        res=res+":"+"null"+":"+"false";
                    return res;

                } else if (user.getOccupation().equals("Association")) {
                    currentSeasonYear = db.getTheCurrentSeason();
                    return "Association";
                } else if (user.getOccupation().equals("Referee")) {
                    return "Referee" ;
                } else if (user.getOccupation().equals("SystemManager")) {
                    return "SystemManager";
                } else {
                    return "Fan";
                }
            } else { //wrong pass
                return "login failed, wrong password";
            }
        }
    }

    public String logout(String username){
        if(allLoginUser.contains(username)) {
            allLoginUser.remove(username);
        }
        return "true";
    }
    public String checkEventLogs(String username, String event){
        SystemEventsLog sysEvents=new SystemEventsLog();
        try {
            sysEvents.addEventLog(username,event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String checkErrorLogs(String username, String event){
        SystemErrorLogs sysEvents=new SystemErrorLogs();
        try {
            sysEvents.addErrorLog(username,event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    //    public boolean checkIfSeasonExist(int year) {
//        if (db.isSeasonExist(year)) {
    public String checkIfSeasonExist(String year) {
        if (db.isSeasonExist(Integer.parseInt(year))) {
//            setChanged();
//            notifyObservers("this season already exist, try again next year");
            return "true";
        } else {
            return "false";
        }
    }

    /**
     * Association
     */

    public String addSeason(String year, String sumOfIncome) {//need to check
        if (db.addSeason(Integer.parseInt(year), Double.parseDouble(sumOfIncome))) {
            return "added";
        }
        return "didnt added";
    }

    public String getAllReferees() {
        StringBuilder ans = new StringBuilder();
        ArrayList<Referee> list = db.getAllReferees();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1)
                ans.append(list.get(i).getUserName()).append(", ").append(list.get(i).getFirstName()).append(" ")
                        .append(list.get(i).getLastName()).append(", ").append(list.get(i).getRefereeRole());
            else
                ans.append(list.get(i).getUserName()).append(", ").append(list.get(i).getFirstName()).append(" ")
                        .append(list.get(i).getLastName()).append(", ").append(list.get(i).getRefereeRole()).append(":");
        }
        return ans.toString();
    }

    public String inviteRefereeToJudge(String assoUser, String refUsername) {
        if (db.addUserAlert(refUsername, "NominateReferee", assoUser + " invited you to be a referee in " + currentSeasonYear + " season", false)){
            /**
             * ===========
             * ===ALERT===
             * ===========
             */
            String content = "You have invitation to Judge from  " + assoUser;
            ArrayList<String> add = new ArrayList<String>();
            add.add(refUsername);
            String addressee = transferArrayToString(add);
            addAlertToDB(content,"MatchAlert",add);

            return "true" + ",,,," + "ALERT" + ",,," + content + ",,," + addressee;
        }
        else
            return "false";
    }

    public String getTeamReqs(String assoUser) {
        ArrayList<String> list = db.getAllTeamReqs(assoUser);
        StringBuilder serverAns = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1)
                serverAns.append(list.get(i));
            else
                serverAns.append(list.get(i)).append(":");
        }

        return serverAns.toString();
    }

    public String checkTeamRegistration(String username, String selectedReq) {//"Register Team - TeamName- Liverpool ,ToLeague- Premier League"
        String[] separated = selectedReq.split("[-,]");
        String teamName = separated[2].trim();
        String leagueName = separated[4].trim();
        TeamInfo t = db.getTeam(teamName);
        League l = db.getLeagueByName(leagueName);
        if (currentSeasonYear == 0) {
            currentSeasonYear = db.getTheCurrentSeason();
        }
        if (t != null && l != null) {
            if (l.addTeam(t)) {
                db.addTeamToLeague(t.getTeamName(), currentSeasonYear, l.getLeagueName());
                db.updateAlertDetails(username, selectedReq);
                /**
                 * ===========
                 * ===ALERT===
                 * ===========
                 */
                String content="The Team "+teamName+" added successfully to league "+leagueName;
                ArrayList<String>add=alertSystem.getAllAddressee(t);
                String addressee = transferArrayToString(add);
                addAlertToDB(content,"TeamAlert",add);

                return "team was added successfully"+ ",,,," + "ALERT" + ",,," + content + ",,," + addressee;
            } else {
                return "team isnt complete";
            }
        }

        return "";
    }

    public String getCurrentSeason() {
        return String.valueOf(db.getTheCurrentSeason());
    }

    public String isLeagueExist(String leagueName) {
        League tmp = db.getLeagueByName(leagueName);
        if (tmp == null) {
            return "false";
        } else {
            return "true";
        }
    }

    public String addLeagueToDB(String yearStr, String leaguename, String startdate, String ww, String ll, String dd,
                                String gd, String rrounds, String numofteams) {
        int year = Integer.parseInt(yearStr);
        String leagueName = leaguename;
        String startDate = startdate;
        int w = Integer.parseInt(ww);
        int l = Integer.parseInt(ll);
        int d = Integer.parseInt(dd);
        boolean goalD = false;
        boolean hTh = false;
        if (gd.equals("Goal Difference")) {
            goalD = true;
        } else {

            hTh = true;
        }
        int rounds = Integer.parseInt(rrounds.substring(0, 1));
        int numTeams = Integer.parseInt(numofteams);

        if (db.addLeagueAndPolicyToSeason(year, leagueName, startDate,
                w, l, d, goalD, hTh,
                rounds, numTeams)) { //add false of scheduling
            return "true";
        }

        return "false";
    }

    public String showLeaguesInSeason() {
        StringBuilder allLeagues = new StringBuilder();
        ArrayList<String> leagues = db.getAllLeaguesToChangePolicy();
        if (leagues != null && leagues.size() > 0) {
            for (int i = 0; i < leagues.size(); i++) {
                if (i == leagues.size() - 1)
                    allLeagues.append(leagues.get(i));
                else
                    allLeagues.append(leagues.get(i)).append(":");
            }
        }

        return allLeagues.toString();
    }

    public String showAllRefs() {
        StringBuilder allRefs = new StringBuilder();
        ArrayList<Referee> refs = db.getAllRefereesWithSeasonWithoutLeague();
        if (refs != null && refs.size() > 0) {
            for (int i = 0; i < refs.size(); i++) {
                if (i == refs.size() - 1)
                    allRefs.append(refs.get(i).getUserName()).append(" - ").append(refs.get(i).getFirstName()).append(" ").
                            append(refs.get(i).getLastName()).append(" - ").append(refs.get(i).getRefereeRole());
                else
                    allRefs.append(refs.get(i).getUserName()).append(" - ").append(refs.get(i).getFirstName()).append(" ").
                            append(refs.get(i).getLastName()).append(" - ").append(refs.get(i).getRefereeRole()).append(":");
            }
        }

        return allRefs.toString();
    }

    public String addRefToLeague(String selectedLeague, String selectedRef) {
        int lastChar = selectedRef.indexOf("-");
        String refUserName = selectedRef.substring(0, lastChar).trim();
        if (db.updateUserDetails(refUserName, selectedLeague, "referees", "LeagueName")) {
            /**
             * ===========
             * ===ALERT===
             * ===========
             */
            String content = "You add to league  " + selectedLeague;
            ArrayList<String> add = new ArrayList<String>();
            add.add(refUserName);
            String addressee = transferArrayToString(add);
            addAlertToDB(content,"MatchAlert",add);

            return "true" + ",,,," + "ALERT" + ",,," + content + ",,," + addressee;
        } else {
            return "false";
        }
    }

    public String changePointsForLeague(String leagueName, String pointsForWin, String pointsForDraw, String pointsForLoss, String goalDiffTieBreaker, String directResultsTieBreaker) {
        League league = db.getLeagueByName(leagueName);
        if (league != null && league.getTeams().size() == 0) {
            db.updateLeagueDetails(leagueName, Integer.parseInt(pointsForWin), "seasonsAndLeaguesPolicy", "PointsPerWin");
            db.updateLeagueDetails(leagueName, Integer.parseInt(pointsForDraw), "seasonsAndLeaguesPolicy", "PointsPerLoss");
            db.updateLeagueDetails(leagueName, Integer.parseInt(pointsForLoss), "seasonsAndLeaguesPolicy", "PointsPerDraw");
            boolean gd = false;
            boolean dir = false;
            if (goalDiffTieBreaker.equals("true")) {
                gd = true;
            }
            if (directResultsTieBreaker.equals("true")) {
                dir = true;
            }
            db.updateLeagueDetails(leagueName, gd, "seasonsAndLeaguesPolicy", "DifferenceGoals");
            db.updateLeagueDetails(leagueName, dir, "seasonsAndLeaguesPolicy", "StraightMeets");
            return "true";
        }
        return "false";
    }

    public String changeRoundsForLeague(String leagueName, String rounds) {
        League league = db.getLeagueByName(leagueName);
        if (league != null && league.getTeams().size() == 0) {
            db.updateLeagueDetails(leagueName, Integer.parseInt(rounds), "seasonsAndLeaguesPolicy", "NumOfRounds");
            return "true";
        }
        return "false";
    }

    public String createScheduleToLeague(String leagueName) {
        League league = null;
        ArrayList<String> leaguesInDB = db.getAllLeaguesToChangePolicy();
        for (int i = 0; i < leaguesInDB.size(); i++) {
            if (leaguesInDB.get(i).equals(leagueName)) {
                league = db.getLeagueByName(leaguesInDB.get(i));
            }
        }
        if (league != null) {
            Vector<TeamInfo> teamsInLeague = league.getTeams();
            if (teamsInLeague.size() % 2 == 0 && teamsInLeague.size() < league.getNumOfTeams()) {
                if (league.getMainReferees().size() >= (league.getTeams().size() / 2)) {
                    if (league.getSideReferees().size() >= (league.getTeams().size() / 2) * 3) {
                        ArrayList<ArrayList<Match>> leagueGames = league.createSeasonFixtures();
                        currentSeasonYear = db.getTheCurrentSeason();
                        for (int i = 0; i < leagueGames.size(); i++) {
                            for (int j = 0; j < leagueGames.get(i).size(); j++) {
                                Date date = leagueGames.get(i).get(j).getMatchDate();
                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                String strDate = dateFormat.format(date);

                                db.addGameDetails(currentSeasonYear, league.getLeagueName(), strDate,
                                        leagueGames.get(i).get(j).getHomeTeam().getTeamName(), leagueGames.get(i).get(j).getAwayTeam().getTeamName(),
                                        leagueGames.get(i).get(j).getHomeTeam().getTeamCourt().getCourtName(),
                                        leagueGames.get(i).get(j).getMainRef().getUserName(), leagueGames.get(i).get(j).getSideRefs().get(0).getUserName(),
                                        leagueGames.get(i).get(j).getSideRefs().get(1).getUserName(), leagueGames.get(i).get(j).getSideRefs().get(2).getUserName());
                            }
                        }
                        return "true";
                    } else {
                        return "add more side referees - total side referees should be equals to ( (number of teams in league/2) * 3) at least";
                    }
                } else {
                    return "add more main referees - total main referees should be equals to (number of teams in league/2) at least";
                }
            } else {
                return "odd number of teams in league";
            }
        }
        return "League doesn't exist!";
    }

    public String getRefReqs(String userName) {
        StringBuilder ans = new StringBuilder();
        ArrayList<String>  reqs =  db.getAllRefReqs(userName);
        for (int i = 0; i < reqs.size(); i++) {
            if (i == reqs.size() - 1)
                ans.append(reqs.get(i));
            else
                ans.append(reqs.get(i)).append(":");
        }

        return ans.toString();

    }

    public String getAllRefereeMatches(String usernameRef) {
        StringBuilder allGames = new StringBuilder();
        ArrayList<Match> getGames = db.getAllRefereeMatches(usernameRef);
        for (int i = 0; i < getGames.size(); i++) {
            String date = getGames.get(i).getGameDate();
            String home = getGames.get(i).getHomeTeam().getTeamName();
            String away = getGames.get(i).getAwayTeam().getTeamName();
            String mainRef = getGames.get(i).getMainRef().getUserName();
            String stadium = getGames.get(i).getCourt().getCourtName();

            if (i == getGames.size() - 1)
                allGames.append(date).append(" - ").append(home).append(" Against ").
                        append(away).append(" at ").append(stadium).append(" - Main Referee - ").append(mainRef);
            else
                allGames.append(date).append(" - ").append(home).append(" Against ").
                        append(away).append(" at ").append(stadium).append(" - Main Referee - ").append(mainRef).append("~");
        }


        return allGames.toString();
    }

    public String getPlayersToManageGame(String homeTeamName, String awayTeamName) {
        StringBuilder allPlayers = new StringBuilder();
        TeamInfo home = db.getTeam(homeTeamName);
        TeamInfo away = db.getTeam(awayTeamName);
        ArrayList<PlayerInterface> homePlayers = home.getTeamPlayers();
        ArrayList<PlayerInterface> awayPlayers = away.getTeamPlayers();


        for (int i = 0; i < homePlayers.size(); i++) {
            TeamMember tm = (TeamMember) homePlayers.get(i);
            allPlayers.append(homeTeamName).append(" - ").append(tm.getUserName()).append(" - ").append(tm.getFirstName())
                    .append(" ").append(tm.getLastName()).append(":");
        }

        for (int i = 0; i < awayPlayers.size(); i++) {
            TeamMember tm = (TeamMember) awayPlayers.get(i);
            if (i == awayPlayers.size() - 1) {
                allPlayers.append(awayTeamName).append(" - ").append(tm.getUserName()).append(" - ").append(tm.getFirstName())
                        .append(" ").append(tm.getLastName());
            } else
                allPlayers.append(awayTeamName).append(" - ").append(tm.getUserName()).append(" - ").append(tm.getFirstName())
                        .append(" ").append(tm.getLastName()).append(":");
        }

        return allPlayers.toString();
    }

    public String addGoalEvent(String homeTeamName, String awayTeamName, String mainRefUser,
                               String teamPlayerScored, String eventType, String minute) {
        if (currentSeasonYear == 0) {
            currentSeasonYear = db.getTheCurrentSeason();
        }
        String teamScored = teamPlayerScored.split("-")[0];
        String playerScored = teamPlayerScored.split("-")[1];
        ArrayList<Match> allGames = db.getAllRefereeMatches(mainRefUser);
        boolean found = false;
        Match game = null;
        for (int i = 0; !found && i < allGames.size(); i++) {
            if (allGames.get(i).getHomeTeam().getTeamName().equals(homeTeamName) &&
                    allGames.get(i).getAwayTeam().getTeamName().equals(awayTeamName)) {
                game = allGames.get(i);
                found = true;
            }
        }
        if (game != null) {
            if (db.addMatchEvent(currentSeasonYear, homeTeamName, awayTeamName, mainRefUser, minute, playerScored, "", eventType)
                    && db.updateMatchResult(currentSeasonYear, homeTeamName, awayTeamName, mainRefUser, teamScored)) {
                /**
                 * ===========
                 * ===ALERT===
                 * ===========
                 */
                String content="In a match between" +homeTeamName+ " and "+awayTeamName+" -> Goal to "+teamScored;
                ArrayList<String>add=alertSystem.getAllAddressee(game);
                add.addAll(db.getAllAssignUsersToAlerts());
                String addressee=transferArrayToString(add);
                addAlertToDB(content,"MatchAlert",add);

                return "true"+",,,,"+"ALERT"+",,,"+content+",,,"+addressee;
            }
        }

        return "false";
    }

    public String addOffsideFoulYellowRedInjuryEvent(String homeTeamName, String awayTeamName, String mainRefUser,
                                                     String teamPlayerScored, String eventType, String minute) {
        if (currentSeasonYear == 0) {
            currentSeasonYear = db.getTheCurrentSeason();
        }
        String playerScored = teamPlayerScored.split("-")[1];
        ArrayList<Match> allGames = db.getAllRefereeMatches(mainRefUser);
        boolean found = false;
        Match game = null;
        for (int i = 0; !found && i < allGames.size(); i++) {
            if (allGames.get(i).getHomeTeam().getTeamName().equals(homeTeamName) &&
                    allGames.get(i).getAwayTeam().getTeamName().equals(awayTeamName)) {
                game = allGames.get(i);
                found = true;
            }
        }
        if (game != null) {
            if (db.addMatchEvent(currentSeasonYear, homeTeamName, awayTeamName, mainRefUser, minute, playerScored, "", eventType)) {
                /**
                 * ===========
                 * ===ALERT===
                 * ===========
                 */
                String content="In a match between" +homeTeamName+ " and "+awayTeamName+" -> "+eventType+" "+teamPlayerScored;
                ArrayList<String>add=alertSystem.getAllAddressee(game);
                add.addAll(db.getAllAssignUsersToAlerts());
                String addressee=transferArrayToString(add);
                addAlertToDB(content,"MatchAlert",add);

                return "true"+",,,,"+"ALERT"+",,,"+content+",,,"+addressee;
            }
        }

        return "false";
    }

    public String addSubstituteEvent(String homeTeamName, String awayTeamName, String mainRefUser,
                                     String subs, String eventType, String minute) {
        if (currentSeasonYear == 0) {
            currentSeasonYear = db.getTheCurrentSeason();
        }
        String playerOut = subs.split("-")[0];
        String playerIn = subs.split("-")[1];
        ArrayList<Match> allGames = db.getAllRefereeMatches(mainRefUser);
        boolean found = false;
        Match game = null;
        for (int i = 0; !found && i < allGames.size(); i++) {
            if (allGames.get(i).getHomeTeam().getTeamName().equals(homeTeamName) &&
                    allGames.get(i).getAwayTeam().getTeamName().equals(awayTeamName)) {
                game = allGames.get(i);
                found = true;
            }
        }
        if (game != null) {
            if (db.addMatchEvent(currentSeasonYear, homeTeamName, awayTeamName, mainRefUser, minute, playerOut, playerIn, eventType)) {
                /**
                 * ===========
                 * ===ALERT===
                 * ===========
                 */
                String content="In a match between" +homeTeamName+ " and "+awayTeamName+" -> Substitute between " +playerOut+" and "+playerIn;
                ArrayList<String>add=alertSystem.getAllAddressee(game);
                add.addAll(db.getAllAssignUsersToAlerts());
                String addressee=transferArrayToString(add);
                addAlertToDB(content,"MatchAlert",add);

                return "true"+",,,,"+"ALERT"+",,,"+content+",,,"+addressee;
            }
        }

        return "false";
    }

    /**
     * Association End -------------------------------------------------------------------
     * -------------------------------------------------------------------
     * -------------------------------------------------------------------
     */
//
    public String addTeam(String teamName, String esYear, String isActive, String city, String owner, String court) {
        String res="";
        int esyear=Integer.parseInt(esYear);
        Boolean isactive=Boolean.parseBoolean(isActive);

        if (!db.isTeamExist(teamName) && db.checkOwnerNotHaveTeam(owner)) {
            //TeamInfo newTeam = (TeamInfo) db.getTeam(teamName);
            db.addTeam(teamName, esyear, isactive, city, owner, court);
            res="team was added to system!";
            return res;
        } else {
            res="team already exist";
            return res;
        }
    }
    //
//
//    public String ownerTeamName(String userName) {
//        if (tm.getTeam() != null) {
//            return tm.getTeam().getTeamName();
//        } else return "";
//    }
//
//    public boolean ownerTeamStatus(String userName) {
//        if (tm.getTeam() != null) {
//            return tm.getTeam().isTeamActiveStatus();
//        }
//        return false;
//    }
//
    public String chooseCourt(String city) {
        StringBuilder ans = new StringBuilder();
        if (city != null) {
            ArrayList<String> courts = new ArrayList<>();
            courts = db.getCourtByCity(city);
            for (int i = 0; i < courts.size(); i++) {
                if(i==courts.size()-1)
                    ans.append(courts.get(i));
                else
                    ans.append(courts.get(i)).append(":");
            }
        }
        return ans.toString();
    }
//
////
//    public int getCurrentSeason() {
//        return db.getTheCurrentSeason();
//    }
//
//
//    public boolean isLeagueExist(String leagueName) {
//        League tmp = db.getLeagueByName(leagueName);
//        if (tmp == null) {
//            return false;
//        } else {
//            return true;
//        }
//    }

    public String changeTeamStatus(String teamName) {
        TeamInfo tm=db.getTeam(teamName);
        if (tm.isTeamActiveStatus()) {
            db.updateTeamDetails(tm.getTeamName(), false, "teams", "TeamActiveStatus");
            /**
             * ===========
             * ===ALERT===
             * ===========
             */
            String content="The Team "+tm.getTeamName()+" closed.";
            ArrayList<String>add=alertSystem.getAllAddressee(tm,db.getAllSystemManagers());
            String addressee=transferArrayToString(add);
            addAlertToDB(content,"TeamAlert",add);


            //CloseTeam();
            return "false"+",,,,"+"ALERT"+",,,"+content+",,,"+addressee;
        } else {
            db.updateTeamDetails(tm.getTeamName(), true, "teams", "TeamActiveStatus");
            /**
             * ===========
             * ===ALERT===
             * ===========
             */
            String content="The Team "+tm.getTeamName()+" opened.";
            ArrayList<String>add=alertSystem.getAllAddressee(tm,db.getAllSystemManagers());
            String addressee=transferArrayToString(add);
            addAlertToDB(content,"TeamAlert",add);
            //OpenTeam();
            return "true"+",,,,"+"ALERT"+",,,"+content+",,,"+addressee;
        }
    }

//    public boolean isPlayer() {
//        return tm.isPlayer();
//    }
//
//    public boolean isCoach() {
//        return tm.isCoach();
//    }
//
//    public boolean isOwner() {
//        return tm.isOwner();
//    }
//
//    public boolean isTeamManager() {
//        return tm.isTeamManager();
//    }
//
//    //start check

    public String addOwnerToTeam(String newOwner, String username) {
        TeamMember teamMember = (TeamMember) db.getUser(username);
        String ans="";
        try {
            if (db.isUserExist(newOwner)) {
                TeamMember userExists = (TeamMember) db.getUser(newOwner);
                if (teamMember.AddOwner(userExists)) {
                    db.updateUserDetails(newOwner, teamMember.getTeam().getTeamName(), "owners", "CurrentTeam");
                    db.updateUserDetails(newOwner, teamMember.getUserName(), "owners", "EmployedBy");
                    ans = teamMember.getTeam().getTeamName() + ":" + "Owner added Successful";
                    /**
                     * ===========
                     * ===ALERT===
                     * ===========
                     */
                    String content = "The Owner " + newOwner + " join to " + teamMember.getTeam().getTeamName() + ".";
                    ArrayList<String> add = alertSystem.getAllAddressee(teamMember.getTeam());
                    add.add(newOwner);
                    String addressee = transferArrayToString(add);
                    ans = ans + ",,,," + "ALERT" + ",,," + content + ",,," + addressee;
                    addAlertToDB(content,"TeamAlert",add);
                }
                else {
                    ans=teamMember.getTeam().getTeamName()+":"+"Owner added isn't Successful";
                }
            }
        }catch (Exception e){
            ans=teamMember.getTeam().getTeamName()+":"+"Owner added isn't Successful";
        }
        return ans;
    }

    public String addCoachToTeam(String newCoach, String role, String userName) {
        TeamMember teamMember = (TeamMember) db.getUser(userName);
        String ans="";
        try {
            if (db.isUserExist(newCoach)) {
                TeamMember userExists = (TeamMember) db.getUser(newCoach);
                if (teamMember.AddCoach(userExists, "", role)) {
                    db.updateUserDetails(newCoach, teamMember.getTeam().getTeamName(), "coaches", "CurrentTeam");
                    db.updateUserDetails(newCoach, teamMember.getUserName(), "coaches", "EmployedBy");
                    ans=teamMember.getTeam().getTeamName()+":"+"Coach added Successful";
                    /**
                     * ===========
                     * ===ALERT===
                     * ===========
                     */
                    String content="The Coach "+newCoach+" join to "+teamMember.getTeam().getTeamName()+".";
                    ArrayList<String>add=alertSystem.getAllAddressee(teamMember.getTeam());
                    add.add(newCoach);
                    String addressee=transferArrayToString(add);
                    ans=ans+",,,,"+"ALERT"+",,,"+content+",,,"+addressee;
                    addAlertToDB(content,"TeamAlert",add);
                }
                else {
                    ans=teamMember.getTeam().getTeamName()+":"+"Coach added isn't Successful";
                }
            }
        }catch (Exception e){
            ans=teamMember.getTeam().getTeamName()+":"+"Coach added isn't Successful";
        }
        return ans;
    }

    public String addPlayerToTeam(String newPlayer, String role, String userName) {
        TeamMember teamMember = (TeamMember) db.getUser(userName);
        String ans="";
        try {
            if (db.isUserExist(newPlayer)) {
                TeamMember player = (TeamMember) db.getUser(newPlayer);
                if (teamMember.AddPlayer(player, role)) {
                    db.updateUserDetails(newPlayer, teamMember.getTeam().getTeamName(), "players", "CurrentTeam");
                    db.updateUserDetails(newPlayer, teamMember.getUserName(), "players", "EmployedBy");
                    ans=teamMember.getTeam().getTeamName()+":"+"Player added Successful";
                    /**
                     * ===========
                     * ===ALERT===
                     * ===========
                     */
                    String content="The Player "+newPlayer+" join to "+teamMember.getTeam().getTeamName()+".";
                    ArrayList<String>add=alertSystem.getAllAddressee(teamMember.getTeam());
                    add.add(newPlayer);
                    String addressee=transferArrayToString(add);
                    ans=ans+",,,,"+"ALERT"+",,,"+content+",,,"+addressee;
                    addAlertToDB(content,"TeamAlert",add);

                }

                else{
                    ans=teamMember.getTeam().getTeamName()+":"+"Player added isn't Successful";
                }
            }
        }catch (Exception e){
            ans=teamMember.getTeam().getTeamName()+":"+"Player added isn't Successful";
        }
        return ans;
    }

    public String addManagerToTeam(String user, String ownerP, String playerP, String coachP, String teamMP, String userName) {
        TeamMember teamMember = (TeamMember) db.getUser(userName);
        String ans="";
        try {
            if (db.isUserExist(user)) {
                TeamMember teamManager = (TeamMember) db.getInstance().getUser(user);
                if (teamMember.AddTeamManager(teamManager, Boolean.parseBoolean(ownerP), Boolean.parseBoolean(playerP), Boolean.parseBoolean(coachP), Boolean.parseBoolean(teamMP))) {
                    db.updateUserDetails(user, teamMember.getTeam().getTeamName(), "teamManagers", "CurrentTeam");
                    db.updateUserDetails(user, teamMember.getUserName(), "teamManagers", "EmployedBy");
                    db.updateUserDetails(user, Boolean.parseBoolean(coachP), "teamManagers", "CoachPermission");
                    db.updateUserDetails(user, Boolean.parseBoolean(playerP), "teamManagers", "PlayerPermission");
                    db.updateUserDetails(user, Boolean.parseBoolean(teamMP), "teamManagers", "TeamManagerPermission");
                    db.updateUserDetails(user, Boolean.parseBoolean(ownerP), "teamManagers", "OwnerPermission");
                    ans=teamMember.getTeam().getTeamName()+":"+"Manager added Successful";
                    /**
                     * ===========
                     * ===ALERT===
                     * ===========
                     */
                    String content="The Team Manager "+user+" join to "+teamMember.getTeam().getTeamName()+".";
                    ArrayList<String>add=alertSystem.getAllAddressee(teamMember.getTeam());
                    add.add(user);
                    String addressee = transferArrayToString(add);
                    ans = ans + ",,,," + "ALERT" + ",,," + content + ",,," + addressee;
                    addAlertToDB(content,"TeamAlert",add);
                }
                else {
                    ans=teamMember.getTeam().getTeamName()+":"+"Manager added isn't Successful";

                }
            }
        }catch (Exception e){
            ans=teamMember.getTeam().getTeamName()+":"+"Manager added isn't Successful";
        }
        return ans;
    }


    //    public void addLeagueToDB(ArrayList<String> newLeagueDetails) {
//        int year = Integer.parseInt(newLeagueDetails.get(0));
//        String leagueName = newLeagueDetails.get(1);
//        String startDate = newLeagueDetails.get(8);
//        int w = Integer.parseInt(newLeagueDetails.get(3));
//        int l = Integer.parseInt(newLeagueDetails.get(5));
//        int d = Integer.parseInt(newLeagueDetails.get(4));
//        boolean goalD = false;
//        boolean hTh = false;
//        if (newLeagueDetails.get(6).equals("Goal Difference")) {
//            goalD = true;
//        } else {
//
//            hTh = true;
//        }
//        int rounds = Integer.parseInt(newLeagueDetails.get(7).substring(0, 1));
//        int numTeams = Integer.parseInt(newLeagueDetails.get(2));
//
//        if (au.addNewLeagueToSeason(year, leagueName, startDate, w, l, d, goalD, hTh, rounds, numTeams)) {
//            UserDaoMdb.getInstance().addLeagueAndPolicyToSeason(year, leagueName, startDate,
//                    w, l, d, goalD, hTh,
//                    rounds, numTeams);
//            setChanged();
//            notifyObservers("The League " + leagueName + " was added!");
//        }
//    }
//
//    public ArrayList<Referee> getAllReferees() {
//        return db.getAllReferees();
//    }
//
    public String getCoachs() {

        StringBuilder ans = new StringBuilder();
        ArrayList<String> coaches = new ArrayList<>();
        coaches = db.getAvailableCoaches();
        for (int i = 0; i < coaches.size(); i++) {
            if(i==coaches.size()-1)
                ans.append(coaches.get(i));
            else
                ans.append(coaches.get(i)).append(":");
        }

        return ans.toString();


    }

    public String getPlayers() {
        StringBuilder ans = new StringBuilder();
        ArrayList<String> Players = new ArrayList<>();
        Players = db.getAvailablePlayers();
        for (int i = 0; i < Players.size(); i++) {
            if(i==Players.size()-1)
                ans.append(Players.get(i));
            else
                ans.append(Players.get(i)).append(":");
        }
        return ans.toString();
    }

    public String getOwners() {
        StringBuilder ans = new StringBuilder();
        ArrayList<String> owners = new ArrayList<>();
        owners = db.getAvailableOwners();
        for (int i = 0; i < owners.size(); i++) {
            if(i==owners.size()-1)
                ans.append(owners.get(i));
            else
                ans.append(owners.get(i)).append(":");
        }
        return ans.toString();
    }

    public String getManagers() {
        StringBuilder ans = new StringBuilder();
        ArrayList<String> managers = new ArrayList<>();
        managers = db.getAvailableManagers();
        for (int i = 0; i < managers.size(); i++) {
            if(i==managers.size()-1)
                ans.append(managers.get(i));
            else
                ans.append(managers.get(i)).append(":");
        }
        return ans.toString();
    }
    //
//    public ArrayList<TeamMember> getManagers() {
//        return db.getAvailableManagers();
//    }
//
//    public void inviteRefereeToJudge(String refUsername) {
//        db.addUserAlert(refUsername, "NominateReferee", au.getUserName() + " invited you to be a referee in " + currentSeasonYear + " season", false);
//    }
//
    public String refApprovesToJudge(String userName ,String reqContent) {
        String ans= "";
        Referee ref = (Referee) db.getUser(userName);
        int season = db.getTheCurrentSeason();
        if (ref.getRefereeRole().equals("Main Referee")) {
            db.updateUserDetails(userName, season, "referees", "SeasonYear");
            db.updateAlertDetails(userName, reqContent);
            ans ="True";
        } else if (ref.getRefereeRole().equals("Side Referee")) {
            db.updateUserDetails(userName, season, "referees", "SeasonYear");
            db.updateAlertDetails(userName, reqContent);
            ans ="True";
        }
        return ans;
    }

    public String addTeamToLeagueRequest(String team, String league){
        if(team !=null && league!=null){
            String ass= db.getAssUserFromDB();
            db.addUserAlert(ass,"TeamAlert","Register Team - TeamName- "+team+", ToLeague- "+league,false);
            return "true";
        }
        return "false";
    }

//
//    public ArrayList<League> getAllLeagues() {
//        return db.getAllLeaguesInCurrentSeason();
//    }
//
//    public void createScheduleToLeague(String leagueNameToSchedule) {
//        ArrayList<League> leaguesInDB = AssociationUser.getBm().get(currentSeasonYear).getBudget().getSeason().getAllLeagues();
//        League league = null;
//        for (int i = 0; i < leaguesInDB.size(); i++) {
//            if (leaguesInDB.get(i).getLeagueName().equals(leagueNameToSchedule)) {
//                league = leaguesInDB.get(i);
//            }
//        }
//        if (league != null) {
//            Vector<TeamInfo> teamsInLeague = league.getTeams();
//            if (teamsInLeague.size() % 2 == 0) {
//                if (league.getMainReferees().size() == (league.getTeams().size() / 2)) {
//                    if (league.getSideReferees().size() == (league.getTeams().size() / 2) * 3) {
//                        au.updateGameScheduling(league);
//                        ArrayList<ArrayList<Match>> leagueGames = league.getFixtures();
//                        for (int i = 0; i < leagueGames.size(); i++) {
//                            for (int j = 0; j < leagueGames.get(0).size(); j++) {
//                                db.addGameDetails(currentSeasonYear, league.getLeagueName(), leagueGames.get(i).get(j).getMatchDate().toString(),
//                                        leagueGames.get(i).get(j).getHomeTeam().getTeamName(), leagueGames.get(i).get(j).getAwayTeam().getTeamName(),
//                                        leagueGames.get(i).get(j).getHomeTeam().getTeamCourt().getCourtName(),
//                                        leagueGames.get(i).get(j).getMainRef().getUserName(), leagueGames.get(i).get(j).getSideRefs().get(0).getUserName(),
//                                        leagueGames.get(i).get(j).getSideRefs().get(1).getUserName(), leagueGames.get(i).get(j).getSideRefs().get(2).getUserName());
//                            }
//                        }
//                    } else {
//                        setChanged();
//                        notifyObservers("add more side referees - total main referees should be equals to ( (number of teams in league/2) * 3)");
//                    }
//                } else {
//                    setChanged();
//                    notifyObservers("add more main referees - total main referees should be equals to (number of teams in league/2)");
//                }
//            } else {
//                setChanged();
//                notifyObservers("odd number of teams in league");
//            }
//
//
//        }
//
//    }

    public String getTeamAssets(String userName) {
        TeamMember teamMember = (TeamMember) db.getUser(userName);
        ArrayList<String> allTm = new ArrayList<>();
        for (int i = 0; i < teamMember.getTeam().getTeamOwners().size(); i++) {
            String name = ((TeamMember) teamMember.getTeam().getTeamOwners().get(i)).getUserName() + ", " +
                    ((TeamMember) teamMember.getTeam().getTeamOwners().get(i)).getFirstName() + ", " +
                    ((TeamMember) teamMember.getTeam().getTeamOwners().get(i)).getLastName() + " - Owner";
            allTm.add(name);
        }
        for (int i = 0; i < teamMember.getTeam().getTeamManagers().size(); i++) {
            String name = ((TeamMember) teamMember.getTeam().getTeamManagers().get(i)).getUserName() + ", " +
                    ((TeamMember) teamMember.getTeam().getTeamManagers().get(i)).getFirstName() + ", " +
                    ((TeamMember) teamMember.getTeam().getTeamManagers().get(i)).getLastName() + " - Team Manager";
            allTm.add(name);
        }
        for (int i = 0; i < teamMember.getTeam().getTeamCoaches().size(); i++) {
            String name = ((TeamMember) teamMember.getTeam().getTeamCoaches().get(i)).getUserName() + ", " +
                    ((TeamMember) teamMember.getTeam().getTeamCoaches().get(i)).getFirstName() + ", " +
                    ((TeamMember) teamMember.getTeam().getTeamCoaches().get(i)).getLastName();
            if(!((TeamMember) teamMember.getTeam().getTeamCoaches().get(i)).getTeamRole().equals("null")){
                name= name+" - "+((TeamMember) teamMember.getTeam().getTeamCoaches().get(i)).getTeamRole();
            }
            allTm.add(name);
        }
        for (int i = 0; i < teamMember.getTeam().getTeamPlayers().size(); i++) {
            String name = ((TeamMember) teamMember.getTeam().getTeamPlayers().get(i)).getUserName() + ", " +
                    ((TeamMember) teamMember.getTeam().getTeamPlayers().get(i)).getFirstName() + ", " +
                    ((TeamMember) teamMember.getTeam().getTeamPlayers().get(i)).getLastName() + " - Player ";
            if(!((TeamMember) teamMember.getTeam().getTeamPlayers().get(i)).getTeamRole().equals("null")){
                name=name+"-"+((TeamMember) teamMember.getTeam().getTeamPlayers().get(i)).getTeamRole();
            }
            allTm.add(name);
        }
        allTm.add(teamMember.getTeam().getTeamHomeCourt().getCourtName() + " - Home Court");
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < allTm.size(); i++) {
            if (i == allTm.size() - 1)
                ans.append(allTm.get(i));
            else
                ans.append(allTm.get(i)).append(":");
        }
        return ans.toString();
    }

    public String removeAsset(String nameAsset, String userName ) {
        String ans="";
        String removeUsers="";
        TeamMember teamMember = (TeamMember) db.getUser(userName);
        try {
            for (int i = 0; i < teamMember.getTeam().getTeamCoaches().size(); i++) {
                if (((TeamMember) teamMember.getTeam().getTeamCoaches().get(i)).getUserName().equals(nameAsset)) {
                    TeamMember coach = (TeamMember) db.getUser(nameAsset);
                    if (teamMember.RemoveCoach(coach)) {
                        db.updateUserDetails(nameAsset, "", "coaches", "CurrentTeam");
                        removeUsers=coach.getUserName();
                    }
                    db.updateUserDetails(nameAsset, "", "coaches", "EmployedBy");
                    ans=teamMember.getTeam().getTeamName()+":"+"Remove Successful"+":"+"false";
                }
            }
            for (int i = 0; i < teamMember.getTeam().getTeamOwners().size(); i++) {
                if (((TeamMember) teamMember.getTeam().getTeamOwners().get(i)).getUserName().equals(nameAsset)) {
                    TeamMember owner = (TeamMember) db.getUser(nameAsset);
                    if (teamMember.RemoveOwner(owner)) {
                        String removeduser=  db.getAllEmplyedBy(nameAsset);
                        if(removeUsers.equals("")) {
                            removeUsers = removeduser;
                        }
                        else {
                            removeUsers = removeUsers + "," + removeduser;
                        }


//                        db.updateUserDetails(nameAsset, "", "owners", "CurrentTeam");
//                        db.updateUserDetails(nameAsset, "", "owners", "EmployedBy");
                        //ans = teamMember.getTeam().getTeamName() + ":" + "Remove Successful";
//                        if(removeduser.equals("")){
//                            ans=teamMember.getTeam().getTeamName()+":"+"Remove Successful"+":"+"false";
//                        }
//                        else{
                        ans=teamMember.getTeam().getTeamName()+":"+"Remove Successful"+":"+removeduser;
//                        }
                    }
                    else {
                        ans=teamMember.getTeam().getTeamName()+":"+"The user is not nominate by: " + userName +
                                " or the team must have at least one owner";
                    }
                }
            }
            for (int i = 0; i < teamMember.getTeam().getTeamPlayers().size(); i++) {
                if (((TeamMember) teamMember.getTeam().getTeamPlayers().get(i)).getUserName().equals(nameAsset)) {
                    TeamMember player = (TeamMember) db.getUser(nameAsset);
                    if (teamMember.RemovePlayer(player)) {
                        db.updateUserDetails(nameAsset, "", "players", "CurrentTeam");
                        db.updateUserDetails(nameAsset, "", "players", "EmployedBy");
                        ans=teamMember.getTeam().getTeamName()+":"+"Remove Successful"+":"+"false";
                        db.getAll();
                        if(removeUsers.equals("")) {
                            removeUsers = player.getUserName();
                        }
                        else {
                            removeUsers = removeUsers + "," + player.getUserName();
                        }
                        //db.getAllEmplyedPlayers();
                    }
                }
            }
            for (int i = 0; i < teamMember.getTeam().getTeamManagers().size(); i++) {
                if (((TeamMember) teamMember.getTeam().getTeamManagers().get(i)).getUserName().equals(nameAsset)) {
                    TeamMember teamManager = (TeamMember) db.getUser(nameAsset);
                    if (teamMember.RemoveTeamManager(teamManager)) {
                        String removeduser= db.getAllEmplyedBy(nameAsset);
//                        db.updateUserDetails(nameAsset, "", "teamManagers", "CurrentTeam");
//                        db.updateUserDetails(nameAsset, "", "teamManagers", "EmployedBy");
//                        db.updateUserDetails(nameAsset, false, "teamManagers", "CoachPermission");
//                        db.updateUserDetails(nameAsset, false, "teamManagers", "PlayerPermission");
//                        db.updateUserDetails(nameAsset, false, "teamManagers", "TeamManagerPermission");
//                        db.updateUserDetails(nameAsset, false, "teamManagers", "OwnerPermission");
                        ans=teamMember.getTeam().getTeamName()+":"+"Remove Successful"+":"+removeduser;
                        if(removeUsers.equals("")) {
                            removeUsers = removeduser;
                        }
                        else {
                            removeUsers = removeUsers + "," + removeduser;
                        }
                    }
                }
            }
            /**
             * ===========
             * ===ALERT===
             * ===========
             */
            String content = "The Assets " + removeUsers+" removed from "+teamMember.getTeam().getTeamName();
            ArrayList<String> add = alertSystem.getAllAddressee(teamMember.getTeam());
            String[] s=removeUsers.split(",");
            for (String u:s) {
                add.add(u);
            }
            String addressee = transferArrayToString(add);
            addAlertToDB(content,"TeamAlert",add);
            ans = ans + ",,,," + "ALERT" + ",,," + content + ",,," + addressee;
        }
        catch (Exception e){
            ans=teamMember.getTeam().getTeamName()+":"+"Remove isn't Successful";
        }
        return ans;
    }

////    public void addRefToLeague2(String selectedLeague, String selectedRef) {
////        if(au != null){
////            ArrayList<League> leagues = AssociationUser.getBm().get(currentSeasonYear).getBudget().getSeason().getAllLeagues();
////            for (int i = 0; i < leagues.size(); i++) {
////                if(selectedLeague.equals(leagues.get(i).getLeagueName())){
////                    int lastChar = selectedRef.indexOf("-");
////                    String refUserName = selectedRef.substring(0,lastChar).trim();
////                    Referee ref = (Referee) db.getUser(refUserName);
////                    League leg = leagues.get(i);
////                    if(au.addRefereeToLeague(leg,ref)) {
////                        //leagues.get(i).addMainRefereeToLeague(ref);
////                        db.updateUserDetails(refUserName, leagues.get(i).getLeagueName(), "referees", "LeagueName");
////                    }
////                }
////            }
////        }
////    }
//
////    public void addRefToLeague(String selectedLeague, String selectedRef) {
////        if(au != null){
////            ArrayList<League> leagues = AssociationUser.getBm().get(currentSeasonYear).getBudget().getSeason().getAllLeagues();
////            for (int i = 0; i < leagues.size(); i++) {
////                if(selectedLeague.equals(leagues.get(i).getLeagueName())){
////                    int lastChar = selectedRef.indexOf("-");
////                    String refUserName = selectedRef.substring(0,lastChar).trim();
////                    Referee ref = (Referee) db.getUser(refUserName);
////                    leagues.get(i).addMainRefereeToLeague(ref);
////                    db.updateUserDetails(refUserName,leagues.get(i).getLeagueName(),"referees","LeagueName");
////                }
////            }
////        }
////    }
//
////    public void checkTeamRegistration(String selectedReq) {//"Register Team - TeamName: Liverpool ,ToLeague: Premier League"
////        String[] makaf = selectedReq.split("-");
////        makaf[1].trim();
////        String[] psik = makaf[1].split(",");
////        psik[0].trim();
////        String[] dots1 = psik[0].split(":");
////        String[] dots2 = psik[1].split(":");
////        String teamName = dots1[1].trim();
////        String leagueName = dots2[1].trim();
////        TeamInfo t = db.getTeam(teamName);
////        League l = db.getLeagueByName(leagueName);
////        if(t!=null && l!=null){
////            if(l.addTeam(t)){
////                db.addTeamToLeague(t.getTeamName(),currentSeasonYear,l.getLeagueName());
////                setChanged();
////                notifyObservers("team was added successfully");
////            }
////            else{
////                setChanged();
////                notifyObservers("team isnt complete");
////            }
////        }
////
////
////    }
//
//    public void changePointsForLeague(String leagueName, int pointsForWin, int pointsForDraw, int pointsForLoss, boolean goalDiffTieBreaker, boolean directResultsTieBreaker) {
////      au.updateRankAndPointsPolicy(leagueName, pointsForWin,pointsForDraw,pointsForLoss,goalDiffTieBreaker,directResultsTieBreaker);
//        League league = db.getLeagueByName(leagueName);
//        au.updateRankAndPointsPolicy(league, pointsForWin, pointsForDraw, pointsForLoss, goalDiffTieBreaker, directResultsTieBreaker);
//        db.updateUserDetails(leagueName, pointsForWin, "seasonsAndLeaguesPolicy", "PointsPerWin");
//        db.updateUserDetails(leagueName, pointsForDraw, "seasonsAndLeaguesPolicy", "PointsPerLoss");
//        db.updateUserDetails(leagueName, pointsForLoss, "seasonsAndLeaguesPolicy", "PointsPerDraw");
//        db.updateUserDetails(leagueName, goalDiffTieBreaker, "seasonsAndLeaguesPolicy", "DifferenceGoals");
//        db.updateUserDetails(leagueName, directResultsTieBreaker, "seasonsAndLeaguesPolicy", "StraightMeets");
//    }
//
//    public ArrayList<String> showLeagueList() {
//        return db.getAllLeaguesToChangePolicy();
//    }
//
//    public void changeRoundsForLeague(String leagueName, int rounds) {
//        League league = db.getLeagueByName(leagueName);
//        au.updateGameScheduling(league, rounds);
//        db.updateUserDetails(leagueName, rounds, "seasonsAndLeaguesPolicy", "NumOfRounds");
//    }
//
//
//    public ArrayList<Referee> showAllRefs() {
//        ArrayList<Referee> all = db.getAllRefereesWithSeasonWithoutLeague();
//        return all;
//    }
//
//    public ArrayList<String> showLeaguesInSeason() {
//        ArrayList<String> leagues = new ArrayList<>();
//        if (au != null) {
//            ArrayList<League> l = AssociationUser.getBm().get(currentSeasonYear).getBudget().getSeason().getAllLeagues();
//            if (l.size() > 0) {
//                for (int i = 0; i < l.size(); i++) {
//                    leagues.add(l.get(i).getLeagueName());
//                }
//            }
//        }
//        return leagues;
//    }
//
//    public void addRefToLeague(String selectedLeague, String selectedRef) {
//        if (au != null) {
//            ArrayList<League> leagues = AssociationUser.getBm().get(currentSeasonYear).getBudget().getSeason().getAllLeagues();
//            for (int i = 0; i < leagues.size(); i++) {
//                if (selectedLeague.equals(leagues.get(i).getLeagueName())) {
//                    int lastChar = selectedRef.indexOf("-");
//                    String refUserName = selectedRef.substring(0, lastChar).trim();
//                    Referee ref = (Referee) db.getUser(refUserName);
//                    leagues.get(i).addMainRefereeToLeague(ref);
//                    db.updateUserDetails(refUserName, leagues.get(i).getLeagueName(), "referees", "LeagueName");
//                }
//            }
//        }
//    }
//
//    public ArrayList<String> getTeamReqs() {
//        return db.getAllTeamReqs(au.getUserName());
//    }
//
//
//    public void checkTeamRegistration(String selectedReq) {//"Register Team - TeamName: Liverpool ,ToLeague: Premier League"
//
//        String[] makaf = selectedReq.split("-");
//        makaf[1].trim();
//        String[] psik = makaf[1].split(",");
//        psik[0].trim();
//        String[] dots1 = psik[0].split(":");
//        String[] dots2 = psik[1].split(":");
//        String teamName = dots1[1].trim();
//        String leagueName = dots2[1].trim();
//        TeamInfo t = db.getTeam(teamName);
//        League l = db.getLeagueByName(leagueName);
//        if (t != null && l != null) {
//            if (l.addTeam(t)) {
//                db.addTeamToLeague(t.getTeamName(), currentSeasonYear, l.getLeagueName());
//                setChanged();
//                notifyObservers("team was added successfully");
//            } else {
//                setChanged();
//                notifyObservers("team isnt complete");
//            }
//        }
//
//
//
//    }
//
//    public ArrayList<String> getRefReqs(String userName) {
//        return db.getAllRefReqs(userName);
//    }

    public String getAlerts(String username){
        String ans="";
        ArrayList<String> alerts=new ArrayList<String>();
        ArrayList<AlertPop> a=db.getAllUserAlerts(username);
        for (AlertPop ap:a) {
            if((ap instanceof BudgetAlert) || (ap instanceof MatchAlert) || (ap instanceof TeamAlert)){
                alerts.add(ap.showAlert());
            }
        }
        for (String n:alerts) {
            ans=ans+":"+n;
        }
        return ans;
    }

    public String Subscribe(String username){
        db.Subscribe(username);
        return "true";
    }
    public String Unsubscribe(String username){
        db.Unsubscribe(username);
        return "true";
    }

    private void addAlertToDB(String content,String type, ArrayList<String> users){
        for (String user:users) {
            if(!allLoginUser.contains(user)) {
                db.addUserAlert(user, type, content, false);
            }
        }

    }

    private String transferArrayToString(ArrayList<String> arr){
        String str="";
        for(int i=0;i<arr.size();i++){
            if(i==0){
                str=arr.get(i);
            }
            else {
                str=str+",,"+arr.get(i);
            }
        }
        return str;
    }
}

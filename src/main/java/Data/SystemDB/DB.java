package Data.SystemDB;

import Domain.AssociationManagement.BudgetManagement;
import Domain.AssociationManagement.League;
import Domain.AssociationManagement.PointsPolicy;
import Domain.AssociationManagement.Season;
import Domain.ClubManagement.Court;
import Domain.ClubManagement.TeamInfo;
import Domain.Systems.ComplaintSys;
import Domain.User.*;


import java.sql.Ref;
import java.util.*;

public class DB implements DataBaseInterface {
    // static variable single_instance of type Singleton
    private static DB dbSystem = null;
    private HashMap<String,Fan> users;
    private HashMap<String,HashMap<String,Object>> players;
    private HashMap<String,HashMap<String,Object>> coaches;
    private HashMap<String,HashMap<String,Object>> owners;
    private HashMap<String,HashMap<String,Object>> teamManagers;
    private HashMap<String,HashMap<String,Object>> referees;
    private HashMap<String, TeamInfo> teams;
    private HashMap<String, Court> courts;
    private HashMap<Integer, HashMap<Season, BudgetManagement>> seasonsUpdated;
    private HashMap<Integer,HashMap<League, PointsPolicy>> leaguesAndPointsPolicy;
    private HashMap<Integer,HashMap<String, ArrayList<TeamInfo>>> teamsAndLeagues;
    private HashMap<Integer, Season> seasons;
    private HashMap<String, League> leagues;
    private static ArrayList<String> events;
    private ComplaintSys complaintSys;
    private SystemManager sysManager;


    // private constructor restricted to this class itself
    private DB()
    {
        users = new HashMap<>();
        teams = new HashMap<>();
        seasons = new HashMap<>();
        leagues = new HashMap<>();
        courts = new HashMap<>();
        players = new HashMap<>();
        coaches = new HashMap<>();
        owners = new HashMap<>();
        referees = new HashMap<>();
        teamManagers = new HashMap<>();
        seasonsUpdated = new HashMap<>();
        leaguesAndPointsPolicy = new HashMap<>();
        teamsAndLeagues = new HashMap<>();
        events = new ArrayList<>();
        complaintSys = new ComplaintSys();
        sysManager = new SystemManager("admin","admin","God","IAmGod"
                ,"God","0/0/0","note@westrenWall.god.il");
        users.put("God",sysManager);
    }

    // static method to create instance of Singleton class
    public static DB getInstance()
    {
        if (dbSystem == null)
            dbSystem = new DB();

        return dbSystem;
    }

    public void resetDB(){
        users = new HashMap<>();
        teams = new HashMap<>();
        seasons = new HashMap<>();
        leagues = new HashMap<>();
        courts = new HashMap<>();
        players = new HashMap<>();
        coaches = new HashMap<>();
        owners = new HashMap<>();
        referees = new HashMap<>();
        teamManagers = new HashMap<>();
        seasonsUpdated = new HashMap<>();
        leaguesAndPointsPolicy = new HashMap<>();
        teamsAndLeagues = new HashMap<>();
        events = new ArrayList<>();
        complaintSys = new ComplaintSys();
        sysManager = new SystemManager("admin","admin","God","IAmGod"
                ,"God","0/0/0","note@westrenWall.god.il");
        users.put("God",sysManager);
    }

    public HashMap<String, Fan> getUsers() {
        return users;
    }

//    public boolean addUser(Fan user){
//        if(user!=null){
//            users.put(user.getUserName(),user);
//            return true;
//        }
//        return false;
//    }

//    public Fan getAUser(String username){
//        if(username!=null && users.containsKey(username)){
//            return users.get(username);
//        }
//        return null;
//    }

//    public boolean deleteUser(String username){
//        if(username!=null && users.containsKey(username)){
//            users.remove(username);
//            return true;
//        }
//        return false;
//    }

    public HashMap<String, TeamInfo> getTeams() {
        return teams;
    }

//    public boolean addTeam(TeamInfo team){
//        if(team!=null){
//            teams.put(team.getTeamName(),team);
//            return true;
//        }
//        return false;
//    }

//    public TeamInfo getATeam(String team){
//        if(team!=null && teams.containsKey(team)){
//            return teams.get(team);
//        }
//        return null;
//    }

//    public boolean deleteTeam(String team){
//        if(team!=null && teams.containsKey(team)){
//            teams.remove(team);
//            return true;
//        }
//        return false;
//    }

    public HashMap<String, Court> getCourts() {
        return courts;
    }

//    public boolean addCourt(Court c){
//        if(c!=null){
//            courts.put(c.getCourtName(),c);
//            return true;
//        }
//        return false;
//    }

//    public Court getACourt(String court){
//        if(court!=null && courts.containsKey(court)){
//            return courts.get(court);
//        }
//        return null;
//    }

//    public boolean deleteCourt(String court){
//        if(court!=null && courts.containsKey(court)){
//            courts.remove(court);
//            return true;
//        }
//        return false;
//    }

    public HashMap<Integer, Season> getSeasons() {
        return seasons;
    }

    public boolean addSeason(Season season){
        if(season!=null){
            seasons.put(season.getYear(),season);
            return true;
        }
        return false;
    }

    public Season getSeason(int season){
        if(season>=1948 && seasons.containsKey(season)){
            return seasons.get(season);
        }
        return null;
    }

    @Override
    public boolean addUser(String firstName, String lastName, String userName, String password, String occupation, String birthday, String email, String verificationCode, String role) {
        if(!this.isUserExist(userName)) {
            if (verificationCode.equals("Player")) {
                Fan fan = new TeamMember(firstName, lastName, userName, password, occupation, birthday, email, null);
                ((TeamMember) fan).setPlayer(true);
                ((TeamMember) fan).setRoleOnCourt(role);
                users.put(userName, fan);
                HashMap<String, Object> playerDetails = new HashMap();
                playerDetails.put("PlayerRole", role);
                playerDetails.put("currentTeam", "");
                playerDetails.put("EmployedBy", "");
                players.put(userName, playerDetails);
            } else if (verificationCode.equals("Coach")) {
                //Fan fan = new TeamMember(firstName,lastName,userName,password,occupation,birthday,email,null);
                Fan fan = new TeamMember(firstName, lastName, userName, password, occupation, birthday, email, null);
                ((TeamMember) fan).setCoach(true);
                ((TeamMember) fan).setTeamRole(role);
                users.put(userName, fan);
                HashMap<String, Object> coachDetails = new HashMap();
                coachDetails.put("CoachRole", role);
                coachDetails.put("currentTeam", "");
                coachDetails.put("EmployedBy", "");
                coaches.put(userName, coachDetails);
            } else if (verificationCode.equals("Owner")) {
                //Fan fan = new TeamMember(firstName,lastName,userName,password,occupation,birthday,email,null);
                Fan fan = new TeamMember(firstName, lastName, userName, password, occupation, birthday, email, null);
                ((TeamMember) fan).setOwner(true);
                users.put(userName, fan);
                HashMap<String, Object> ownerDetails = new HashMap();
                ownerDetails.put("currentTeam", "");
                ownerDetails.put("EmployedBy", "");
                ownerDetails.put("CoachPermission", false);
                ownerDetails.put("PlayerPermission", false);
                ownerDetails.put("TeamManagerPermission", false);
                ownerDetails.put("OwnerPermission", false);
                ownerDetails.put("CourtPermission", false);
                owners.put(userName, ownerDetails);
            } else if (verificationCode.equals("TeamManager")) {
                //Fan fan = new TeamMember(firstName,lastName,userName,password,occupation,birthday,email,null);
                Fan fan = new TeamMember(firstName, lastName, userName, password, occupation, birthday, email, null);
                ((TeamMember) fan).setTeamManager(true);
                users.put(userName, fan);
                HashMap<String, Object> teamManagerDetails = new HashMap();
                teamManagerDetails.put("currentTeam", "");
                teamManagerDetails.put("EmployedBy", "");
                teamManagerDetails.put("CoachPermission", false);
                teamManagerDetails.put("PlayerPermission", false);
                teamManagerDetails.put("TeamManagerPermission", false);
                teamManagerDetails.put("OwnerPermission", false);
                teamManagerDetails.put("CourtPermission", false);
                teamManagers.put(userName, teamManagerDetails);
            }
            else if (verificationCode.equals("Referee")) {
                Fan fan = new Referee(firstName, lastName, userName, password, occupation, birthday, email, null);
                ((Referee)fan).setRefereeRole(role);
                users.put(userName, fan);
                HashMap<String, Object> refereeDetails = new HashMap();
                refereeDetails.put("RefereeRole", role);
                refereeDetails.put("SeasonYear", "");
                refereeDetails.put("LeagueName", "");
                referees.put(userName, refereeDetails);
            } else if (verificationCode.equals("Association")) {
                Fan fan = new AssociationUser(firstName, lastName, userName, password, occupation, birthday, email);
                users.put(userName, fan);
            } else if (verificationCode.equals("SystemManager")) {
                Fan fan = new SystemManager(firstName, lastName, userName, password, occupation, birthday, email);
                users.put(userName, fan);
            } else {
                Fan fan = new Fan(firstName, lastName, userName, password, occupation, birthday, email);
                users.put(userName, fan);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isUserExist(String userName) {
        if(users.containsKey(userName)){
            return true;
        }
        return false;
    }

    @Override
    public boolean removeUser(String userName) {
        if(this.isUserExist(userName)){
            String occupation = users.get(userName).getOccupation();
            if(occupation.equals("Player")){
                players.remove(userName);
            }
            else if(occupation.equals("Coach")){
                coaches.remove(userName);
            }
            else if(occupation.equals("TeamManager")){
                teamManagers.remove(userName);
            }
            else if(occupation.equals("Owner")){
                owners.remove(userName);
            }
            else if(occupation.equals("Referee")){
                referees.remove(userName);
            }
            users.remove(userName);
            return true;
        }
        return false;
    }

    @Override
    public Fan getUser(String userName){
        Fan fan = null;
        if(this.isUserExist(userName)) {
            fan = users.get(userName);
        }
        return fan;
    }

    @Override
    public boolean addTeam(String teamName, int establishYear, boolean teamActiveStatus, String city, String firstOwner, String homeCourt) {
        TeamMember owner = (TeamMember)this.getUser(firstOwner);
        TeamInfo team = new TeamInfo(teamName,establishYear,teamActiveStatus,city,owner);
        teams.put(teamName,team);
        return true;
    }

    @Override
    public boolean isTeamExist(String giveTeamName) {
        if(teams.containsKey(giveTeamName)){
            return true;
        }
        return false;
    }

    @Override
    public TeamInfo getTeam(String teamName){
        TeamInfo team = null;
        if(this.isTeamExist(teamName)){
            team = teams.get(teamName);
        }
        return team;
    }

    @Override
    public boolean removeTeam(String team) {
        if(this.isTeamExist(team)){
            this.resetAllPlayersTeam(team);
            this.resetAllCoachesTeam(team);
            this.resetAllOwnersTeam(team);
            this.resetAllTeamManagersTeam(team);
            teams.remove(team);
            return true;

        }
        return false;
    }

    private void resetAllTeamManagersTeam(String team) {
        for (HashMap.Entry<String, HashMap<String,Object>> entry : teamManagers.entrySet()) {
            String teamGet = (String) entry.getValue().get("currentTeam");
            if (teamGet.equals(team)) {
                entry.getValue().put("currentTeam", "");
            }
        }
    }

    private void resetAllOwnersTeam(String team) {
        for (HashMap.Entry<String, HashMap<String,Object>> entry : owners.entrySet()) {
            String teamGet = (String) entry.getValue().get("currentTeam");
            if (teamGet.equals(team)) {
                entry.getValue().put("currentTeam", "");
            }
        }
    }

    private void resetAllCoachesTeam(String team) {
        for (HashMap.Entry<String, HashMap<String,Object>> entry : coaches.entrySet()) {
            String teamGet = (String) entry.getValue().get("currentTeam");
            if (teamGet.equals(team)) {
                entry.getValue().put("currentTeam", "");
            }
        }
    }

    private void resetAllPlayersTeam(String team) {
        for (HashMap.Entry<String, HashMap<String,Object>> entry : players.entrySet()) {
            String teamGet = (String)entry.getValue().get("currentTeam");
            if(teamGet.equals(team)) {
                entry.getValue().put("currentTeam", "");
            }
        }

    }

    @Override
    public boolean addCourt(String courtName, String courtCityLocation, int courtCapacity) {
        Court court = new Court(courtName,courtCityLocation,courtCapacity);
        courts.put(courtName,court);
        return true;
    }

    @Override
    public boolean isCourtExist(String givenCourtName) {
        if(courts.containsKey(givenCourtName)){
            return true;
        }
        return false;
    }

    @Override
    public Court getCourt(String givenCourtName){
        Court court = null;
        if(this.isCourtExist(givenCourtName)){
            court = courts.get(givenCourtName);
        }
        return court;

    }

    @Override
    public boolean removeCourt(String givenCourtName) {
        if(this.isCourtExist(givenCourtName)){
            courts.remove(givenCourtName);
            return true;
        }
        return false;
    }

    @Override
    public boolean addSeason(int seasonYear, double seasonBudget) {
        Season season = new Season(seasonYear);
        HashMap<Season,BudgetManagement> seasonDetails = new HashMap();
        BudgetManagement budget = new BudgetManagement(season,seasonBudget);
        seasonDetails.put(season,budget);
        seasonsUpdated.put(seasonYear,seasonDetails);
        return true;
    }

    @Override
    public boolean isSeasonExist(int seasonYear) {
        if(seasonsUpdated.containsKey(seasonYear)){
            return true;
        }
        return false;
    }

    public boolean deleteSeason(int season){
        if(season>=1948 && seasons.containsKey(season)){
            seasons.remove(season);
            return true;
        }
        return false;
    }

    @Override
    public Season getSeasonByYear(int Season) {
        Season s = null;
        HashMap<Season, BudgetManagement> season = seasonsUpdated.get(Season);
        Set<Season> SeasonFound = season.keySet();
        Iterator it = SeasonFound.iterator();
        while (it.hasNext()) {
            s = (Season) it.next();
            if (s != null && s.getYear() == (Season)) {
                return s;
            }
        }
        return s;
    }


    @Override
    public boolean addLeagueAndPolicyToSeason(int seasonYear, String leagueName, String date, int pointsPerWin, int pointsPerLoss, int pointsPerDraw, boolean differenceGoals, boolean straightMeets, int roundsNum, int numOfTeams) {
        PointsPolicy leaguePolicy = new PointsPolicy(pointsPerWin,pointsPerLoss,pointsPerDraw,differenceGoals,
                straightMeets);
        HashMap <League,PointsPolicy> policyToLeague = new HashMap();
        League league = new League(leagueName,numOfTeams,roundsNum,date);
        policyToLeague.put(league,leaguePolicy);
        leaguesAndPointsPolicy.put(seasonYear,policyToLeague);
        return true;
    }

    @Override
    public boolean isLeagueExistInSeason(int seasonYear, String leagueName) {
        if(this.isSeasonExist(seasonYear)){
            if(leaguesAndPointsPolicy.get(seasonYear).containsKey(leagueName)){
                return true;
            }
        }
        return false;
    }

    public HashMap<String, League> getLeagues() {
        return leagues;
    }

    public boolean addLeague(League league){
        if(league!=null){
            leagues.put(league.getLeagueName(),league);
            return true;
        }
        return false;
    }

    public League getLeagueByName(String league){
        League returnLeague = null;
        if(league!=null) {
            //Iterator it = leaguesAndPointsPolicy.entrySet().iterator();
            for (Map.Entry mapElement : leaguesAndPointsPolicy.entrySet()) {
                HashMap<League, PointsPolicy> LeagueHash = (HashMap) mapElement.getValue();
                Set<League> allLeagues = LeagueHash.keySet();
                Iterator it = allLeagues.iterator();
                while (it.hasNext()) {
                    returnLeague = (League) it.next();
                    if (returnLeague != null && returnLeague.getLeagueName().equals(league)) {
                        return returnLeague;
                    }
                }
            }
        }
        return returnLeague;
    }

    public boolean deleteLeague(String league){
        if(league!=null && leagues.containsKey(league)){
            leagues.remove(league);
            return true;
        }
        return false;
    }



    @Override
    public boolean addTeamToLeague(String teamName, int seasonYear, String leagueName) {
        if(this.isTeamExist(teamName) && this.isLeagueExistInSeason(seasonYear,leagueName)){
            if(teamsAndLeagues.get(seasonYear).containsKey(leagueName)){
                teamsAndLeagues.get(seasonYear).get(leagueName).add(this.getTeam(teamName));
                return true;
            }
            else {
                ArrayList<TeamInfo> teams = new ArrayList<TeamInfo>();
                teams.add(this.getTeam(teamName));
                HashMap <String,ArrayList<TeamInfo>> teamsInLeague = new HashMap();
                teamsInLeague.put(leagueName,teams);
                this.teamsAndLeagues.put(seasonYear,teamsInLeague);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addGameDetails(int seasonYear, String leagueName, String date, String homeTeam, String awayTeam, String courtName, String mainReferee, String sideFirstRef, String sideSecRef, String assistantRef) {
        return false;
    }

    @Override
    public boolean updateUserDetails(String userName, Object newValue, String tableName, String tableCol) {
        return false;
    }

    @Override
    public boolean addUserAlert(String userNameDestination, String alertType, String alertContent, boolean isWatched) {
        return false;
    }

    @Override
    public boolean addTeamBudgetToSeason(String TeamName, double budgetTeam) {
        return false;
    }

    @Override
    public ArrayList<String> getAllTeamReqs(String userName) {
        return null;
    }

    @Override
    public ArrayList<String> getAllRefReqs(String userName) {
        return null;
    }


    public static ArrayList<String> getEvents() {
        return events;
    }

    public ComplaintSys getComplaintSys() {
        return complaintSys;
    }

    public SystemManager getSysManager() {
        return sysManager;
    }

    public HashMap<String, TeamInfo> getAllTeams(){
        return this.teams;
    }

}


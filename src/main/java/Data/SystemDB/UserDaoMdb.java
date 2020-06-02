package Data.SystemDB;

import Domain.AlertSystem.*;
import Domain.AssociationManagement.*;
import Domain.ClubManagement.Court;
import Domain.ClubManagement.TeamInfo;
import Domain.Systems.SystemErrorLogs;
import Domain.User.*;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.HashMap;

import static com.mongodb.client.model.Filters.eq;


/**
 * Tables:
 * -------------------------------------------------------------------------------------
 * Table 1- table name: users
 * columns: UserName, FirstName, LastName,Password, Occupation, Birthday, Email, AssignToAlerts.
 * -------------------------------------------------------------------------------------
 * Table 2- table name: players
 * columns: UserName, RoleOnCourt, CurrentTeam, EmployedBy, ConfirmationCode.
 * -------------------------------------------------------------------------------------
 * Table 3- table name: coaches
 * columns: UserName, CoachRole, CurrentTeam, EmployedBy, ConfirmationCode.
 * -------------------------------------------------------------------------------------
 * Table 4- table name: teamManagers
 * columns: UserName, CurrentTeam, EmployedBy, CoachPermission, PlayerPermission, TeamManagerPermission, OwnerPermission,
 * CourtPermission, ConfirmationCode.
 * -------------------------------------------------------------------------------------
 * Table 5- table name: owners
 * columns: UserName, CurrentTeam, EmployedBy, CoachPermission, PlayerPermission, TeamManagerPermission, OwnerPermission,
 * CourtPermission, ConfirmationCode.
 * -------------------------------------------------------------------------------------
 * Table 6- table name: referees
 * columns: UserName, RefereeRole,SeasonYear, LeagueName, ConfirmationCode.
 * -------------------------------------------------------------------------------------
 * Table 7- table name: courts
 * columns: CourtName, CourtCityLocation, CourtCapacity.
 * -------------------------------------------------------------------------------------
 * Table 8- table name: teams
 * columns: TeamName, EstablishYear, CityRepresent, TeamActiveStatus, HomeCourt, Owner.
 * -------------------------------------------------------------------------------------
 * Table 9- table name: seasons
 * columns: SeasonYear, AssociationBudget.
 * -------------------------------------------------------------------------------------
 * Table 10- table name: seasonsAndLeaguesPolicy
 * columns: SeasonYear, LeagueName, PointsPerWin, PointsPerLoss, PointsPerDraw, DifferenceGoals, StraightMeets, NumOfRounds, NumOfTeams.
 * -------------------------------------------------------------------------------------
 * Table 11- table name: teamsAndLeagues
 * columns: SeasonYear, LeagueName, TeamName, NumOfMatchPlayed, WinCount, DrawCount, DefeatCount, ScoreGoalsCount,
 * ScoreGoalsCount, ReceivedGoalsCount, Points.
 * -------------------------------------------------------------------------------------
 * Table 12- table name: matchesDetails
 * columns: SeasonYear, LeagueName, Date, HomeTeam, AwayTeam, CourtName, MainReferee, SideFirstReferee,
 * SideSecondReferee, AssistantReferee, HomeGoals , AwayGoals
 * ----------------------------------------------------------------------------------------
 * Table 13- table name: usersAlerts
 * columns: UserNameToSend, AlertType, AlertContent, isViewed.
 * ----------------------------------------------------------------------------------------------
 * Table 14- table name: matchesEvents
 * columns: Season, HomeTeam, AwayTeam, MainReferee, MinuteInGame, PlayerInvolved,PlayerSub, EventType.
 */

public class UserDaoMdb implements DataBaseInterface {

    private static final UserDaoMdb instance = new UserDaoMdb();
    private final String mongoClientURI = "mongodb://132.72.65.132:27017";
    private static SystemErrorLogs syserror=new SystemErrorLogs();
    HashMap<String,ArrayList<String>> allEmployedUsers;
    ArrayList<String> assetsRemoved;

//    public UserDaoMdb() {
//        this.allEmployedUsers = new HashMap<String,ArrayList<String>>();
//    }

    public static UserDaoMdb getInstance(){
        return instance;
    }


    public String login(String username) {
        Fan fan = null;
        boolean isFound = false;
        if (this.isUserExist(username)) {
            try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
                MongoDatabase database = mongoClient.getDatabase("footballdb");
                MongoCollection<Document> usersCollection = database.getCollection("users");
                MongoCursor<Document> cursor = usersCollection.find().iterator();
                while (cursor.hasNext() && !isFound) {
                    Document user = cursor.next();
                    if (user.containsValue(username)) {
                        isFound = true;
                        String userName = user.getString("UserName");
                        String firstName = user.getString("FirstName");
                        String lastName = user.getString("LastName");
                        String pass = user.getString("Password");
                        String occupation = user.getString("Occupation");
                        String birthday = user.getString("Birthday");
                        String email = user.getString("Email");
                        if (isFound) {
                            fan = new TeamMember(firstName, lastName, userName, pass, occupation, birthday, email, null);
                        }
                    }

                }
            } catch (MongoException e) {
                try {
                    syserror.addErrorLog("Server","Connection with db Lost");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                throw new MongoException("Failed to connect the DB!");
            }
        }
        if(fan==null){
            return "null";
        }
        else{
            return fan.toString();

        }
    }

    @Override
    public boolean addUser(String firstName, String lastName, String userName, String password, String occupation, String birthday, String email, String verificationCode, String role) {
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("users");
            Document user = new Document("UserName", userName)
                    .append("FirstName", firstName)
                    .append("LastName", lastName)
                    .append("Password", password)
                    .append("Occupation", occupation)
                    .append("Birthday", birthday)
                    .append("Email", email)
                    .append("AssignToAlerts", false);
            collection.insertOne(user);
            if(occupation.equals("TeamMember")){
                if(verificationCode.equals("Player")){
                    this.addPlayerToDB(userName,role,"","",verificationCode);
                }
                else if(verificationCode.equals("Coach")){
                    this.addCoachToDB(userName,role,"","",verificationCode);
                }
                else if(verificationCode.equals("TeamManager")){
                    this.addTeamMangerToDB(userName,"","",false,false,
                            false,false,false,verificationCode);
                }
                else if(verificationCode.equals("Owner")){
                    this.addOwnerToDB(userName,"","",false,false,
                            false,false,false,verificationCode);
                }
            }//occupation -team member
            else if(occupation.equals("Referee")){
                if(verificationCode.equals("Referee")){
                    this.addRefereeToDB(userName,role,0,"",verificationCode);
                }
            }
            return true;
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    @Override
    public Fan getUser(String givenUserName) {
        boolean isFound =false;
        Fan fan = null;
        TeamInfo team;
        League league;
        if(this.isUserExist(givenUserName)){
            try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
                MongoDatabase database = mongoClient.getDatabase("footballdb");
                MongoCollection<Document> usersCollection = database.getCollection("users");
                MongoCursor<Document> cursor = usersCollection.find().iterator();
                while(cursor.hasNext() &&!isFound){
                    Document user = cursor.next();
                    if(user.containsValue(givenUserName)){
                        isFound = true;
                        String userName = user.getString("UserName");
                        String firstName = user.getString("FirstName");
                        String lastName = user.getString("LastName");
                        String password = user.getString("Password");
                        String occupation = user.getString("Occupation");
                        String birthday = user.getString("Birthday");
                        String email = user.getString("Email");
                        boolean AssignToAlerts = (Boolean)user.get("AssignToAlerts");

                        if(occupation.equals("TeamMember")) {
                            fan = new TeamMember(firstName, lastName, userName, password, occupation, birthday, email,null);
                            fan.setAssignToAlerts(AssignToAlerts);
                            if(this.isOwnerExist(givenUserName)){
                                Document ownerDetails = this.getOwnerDetails(givenUserName);
                                String teamName = ownerDetails.getString("CurrentTeam");
                                boolean coachPermission = (Boolean)ownerDetails.get("CoachPermission");
                                boolean playerPermission = (Boolean)ownerDetails.get("PlayerPermission");
                                boolean teamManagerPermission = (Boolean)ownerDetails.get("TeamManagerPermission");
                                boolean ownerPermission = (Boolean)ownerDetails.get("OwnerPermission");
                                boolean courtPermission = (Boolean)ownerDetails.get("CourtPermission");
                                this.setOwnerDetails((TeamMember) fan, true, coachPermission, playerPermission,
                                        teamManagerPermission, ownerPermission, courtPermission);
                                if(!teamName.equals("")) {
                                    team = this.getTeam(teamName);
                                    team.setTeamOwner((OwnerInterface) fan);

                                    ((TeamMember) fan).setTeam(team);
                                }
                            }
                            if(this.isTeamManagerExist(givenUserName)){
                                Document teamManagerDetails = this.getTeamManagerDetails(givenUserName);
                                String teamName = teamManagerDetails.getString("CurrentTeam");
                                boolean coachPermission = (Boolean)teamManagerDetails.get("CoachPermission");
                                boolean playerPermission = (Boolean)teamManagerDetails.get("PlayerPermission");
                                boolean teamManagerPermission = (Boolean)teamManagerDetails.get("TeamManagerPermission");
                                boolean ownerPermission = (Boolean)teamManagerDetails.get("OwnerPermission");
                                boolean courtPermission = (Boolean)teamManagerDetails.get("CourtPermission");
                                this.setTeamManagerDetails((TeamMember) fan, true, coachPermission, playerPermission,
                                        teamManagerPermission, ownerPermission, courtPermission);
                                if(!teamName.equals("")) {
                                    team = this.getTeam(teamName);
                                    team.setTeamOwner(team.getTeamOwners().get(0));

                                    ((TeamMember) fan).setTeam(team);
                                }
                            }
                            if(this.isCoachExist(givenUserName)){
                                Document coachDetails = this.getCoachDetails(givenUserName);
                                String teamName = coachDetails.getString("CurrentTeam");
                                String coachRole = coachDetails.getString("CoachRole");
                                this.setCoachDetails((TeamMember) fan, true, coachRole);
                                if(!teamName.equals("")) {
                                    team = this.getTeam(teamName);
                                    team.setTeamOwner(team.getTeamOwners().get(0));
                                    ((TeamMember) fan).setTeam(team);
                                }
                            }
                            if(this.isPlayerExist(givenUserName)){
                                Document playerDetails = this.getPlayerDetails(givenUserName);
                                String teamName = playerDetails.getString("CurrentTeam");
                                String roleOnCourt = playerDetails.getString("RoleOnCourt");
                                this.setPlayerDetails((TeamMember) fan, true, roleOnCourt);
                                if(!teamName.equals("")) {
                                    team = this.getTeam(teamName);
                                    team.setTeamOwner(team.getTeamOwners().get(0));
                                    ((TeamMember) fan).setTeam(team);
                                }
                            }
                        }//end teamMember
                        else if(occupation.equals("Referee")) {
                            fan = new Referee(firstName, lastName, userName, password, occupation, birthday, email,null);
                            fan.setAssignToAlerts(AssignToAlerts);
                            if(this.isRefereeExist(givenUserName)){
                                Document refereeDetails = this.getRefereeDetails(givenUserName);
                                String refereeRole = refereeDetails.getString("RefereeRole");
                                String leagueName = refereeDetails.getString("LeagueName");
                                league = new League(leagueName);//need to check if exist in db
                                this.setRefereeDetails((Referee)fan,refereeRole,league);
                                this.fillRefereeMatches((Referee)fan,leagueName);
                            }
                        }
                        else if(occupation.equals("Association")) {
                            fan = new AssociationUser(firstName, lastName, userName, password, occupation, birthday, email);
                            fan.setAssignToAlerts(AssignToAlerts);
                            AssociationUser.setYear(this.getTheCurrentSeason());
                            this.setYearAndBudget();
                        }
                        else if(occupation.equals("SystemManager")) {
                            fan = new SystemManager(firstName, lastName, userName, password, occupation, birthday, email);
                            fan.setAssignToAlerts(AssignToAlerts);
                        }
                        else{
                            fan = new Fan(firstName, lastName, userName, password, occupation, birthday, email);
                            fan.setAssignToAlerts(AssignToAlerts);
                        }
                    }
                }
                return fan;

            }catch (MongoException e){
                try {
                    syserror.addErrorLog("Server","Connection with db Lost");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                throw new MongoException("Failed to connect the DB!");
            }
        }
        return null;
    }

    private void setYearAndBudget() {
        boolean isSeasonFound = false;
        //int currentSeason = this.getTheCurrentSeason();
        Season currentSeason = this.getSeason();
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> usersCollection = database.getCollection("seasons");
            MongoCursor<Document> cursor = usersCollection.find().iterator();
            while (cursor.hasNext() && !isSeasonFound) {
                Document season = cursor.next();
                if (season.containsValue(currentSeason.getYear())) {
                    isSeasonFound = true;
                    //int seasonYearFound = (Integer) season.get("SeasonYear");
                    double seasonBudget = (Double) season.get("AssociationBudget");//need to check
                    BudgetManagement budget = new BudgetManagement(currentSeason,seasonBudget);
                    AssociationUser.getBm().put(currentSeason.getYear(),budget);
                }
            }
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    private void fillRefereeMatches(Referee fan, String leagueName) {
        int currentSeason = this.getTheCurrentSeason();
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> usersCollection = database.getCollection("matchesDetails");
            MongoCursor<Document> cursor = usersCollection.find().iterator();
            while(cursor.hasNext()) {
                Document match = cursor.next();
                if (match.containsValue(currentSeason) && match.containsValue(leagueName) && match.containsValue(fan.getUserName())) {
                    String date = match.getString("Date");
                    String homeTeam = match.getString("HomeTeam");
                    String awayTeam = match.getString("AwayTeam");
                    String courtName = match.getString("CourtName");
                    String mainReferee = match.getString("MainReferee");
                    String sideFirstRef = match.getString("SideFirstReferee");
                    String sideSecondRef = match.getString("SideSecondReferee");
                    String assistantRef = match.getString("AssistantReferee");
                    TeamInfo teamHome1 = this.getTeam(homeTeam);
                    TeamInfo teamAway2 = this.getTeam(awayTeam);
                    Court gameCourt = this.getCourt(courtName);
                    Referee mainRef = (Referee)this.getReferee(mainReferee);
                    Referee sideFirstReferee = getReferee(sideFirstRef);
                    Referee sideSecondReferee = getReferee(sideFirstRef);
                    Referee assistantReferee = getReferee(sideFirstRef);
                    Match footballMatch = new Match(teamHome1,teamAway2,gameCourt,date,mainRef);
                    fan.addMatch(footballMatch);
                }
            }
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    private void setRefereeDetails(Referee referee, String refereeRole, League league) {
        referee.setLeague(league);
        referee.setRefereeRole(refereeRole);
    }

    private void setOwnerDetails(TeamMember teamMember, boolean isOwner, boolean coachPermission,
                                 boolean playerPermission,boolean teamManagerPermission, boolean ownerPermission,
                                 boolean courtPermission) {
        teamMember.setOwner(isOwner);
        teamMember.setCoachPermission(coachPermission);
        teamMember.setPlayerPermission(playerPermission);
        teamMember.setTeamManagerPermission(teamManagerPermission);
        teamMember.setOwnerPermission(ownerPermission);
        teamMember.setCourtPermission(courtPermission);
    }

    private void setTeamManagerDetails(TeamMember teamMember, boolean isTeamManager, boolean coachPermission,
                                       boolean playerPermission,boolean teamManagerPermission, boolean ownerPermission,
                                       boolean courtPermission) {
        teamMember.setTeamManager(isTeamManager);
        teamMember.setCoachPermission(coachPermission);
        teamMember.setPlayerPermission(playerPermission);
        teamMember.setTeamManagerPermission(teamManagerPermission);
        teamMember.setOwnerPermission(ownerPermission);
        teamMember.setCourtPermission(courtPermission);
    }

    private void setCoachDetails(TeamMember teamMember, boolean isCoach, String coachRole) {
        teamMember.setCoach(isCoach);
        teamMember.setTeamRole(coachRole);
    }

    private void setPlayerDetails(TeamMember teamMember, boolean isPlayer, String roleOnCourt) {
        teamMember.setPlayer(isPlayer);
        teamMember.setRoleOnCourt(roleOnCourt);
    }

    @Override
    public boolean isUserExist(String userName) {
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("users");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                Document user = cursor.next();
                if(user.containsValue(userName)){
                    return true;
                }
            }
            return false;
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    @Override
    public boolean removeUser(String givenUserName) {
        if (this.isUserExist(givenUserName)) {
            boolean isRemoved =false;
            String occupationUser = this.getOccupationOfUser(givenUserName);
            MongoCollection<Document> collection;
            MongoCollection<Document> usersCollection;
            DeleteResult deleteResult1;
            DeleteResult deleteResult2;
            try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
                MongoDatabase database = mongoClient.getDatabase("footballdb");
                usersCollection = database.getCollection("users");
                if(occupationUser.equals("TeamMember")){
                    if(this.isPlayerExist(givenUserName)){
                        collection = database.getCollection("players");
                        deleteResult2 = collection.deleteOne(eq("UserName", givenUserName));
                        if(deleteResult2.getDeletedCount()>0){
                            isRemoved =true;
                        }
                    }
                    else if(this.isCoachExist(givenUserName)){
                        collection = database.getCollection("coaches");
                        deleteResult2 = collection.deleteOne(eq("UserName", givenUserName));
                        if(deleteResult2.getDeletedCount()>0){
                            isRemoved =true;
                        }
                    }
                    else if(this.isTeamManagerExist(givenUserName)){
                        collection = database.getCollection("teamManagers");
                        deleteResult2 = collection.deleteOne(eq("UserName", givenUserName));
                        if(deleteResult2.getDeletedCount()>0){
                            isRemoved =true;
                        }
                    }
                    else if(this.isOwnerExist(givenUserName)){
                        collection = database.getCollection("owners");
                        deleteResult2 = collection.deleteOne(eq("UserName", givenUserName));
                        if(deleteResult2.getDeletedCount()>0){
                            isRemoved =true;
                        }
                    }
                }
                else if(occupationUser.equals("Referee")){
                    if(this.isRefereeExist(givenUserName)){
                        collection = database.getCollection("referees");
                        deleteResult2 = collection.deleteOne(eq("UserName", givenUserName));
                        if(deleteResult2.getDeletedCount()>0){
                            isRemoved =true;
                        }
                    }
                }
                //Association removing?

                deleteResult1 = usersCollection.deleteOne(eq("UserName", givenUserName));
                if(deleteResult1.getDeletedCount()>0 && isRemoved){
                    return true;
                }
                return false;
            }catch (MongoException e){
                try {
                    syserror.addErrorLog("Server","Connection with db Lost");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                throw new MongoException("Failed to connect the DB!");
            }
        }
        return false;
    }

    @Override
    public boolean addTeam(String teamName, int establishYear, boolean teamActiveStatus, String city, String firstOwner,String homeCourt) {
        if(this.isUserExist(firstOwner)) {
            if(this.checkOwnerNotHaveTeam(firstOwner)) {
                try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
                    MongoDatabase database = mongoClient.getDatabase("footballdb");
                    MongoCollection<Document> collection = database.getCollection("teams");
                    Document team = new Document("TeamName", teamName)
                            .append("EstablishYear", establishYear)
                            .append("CityRepresent", city)
                            .append("TeamActiveStatus", teamActiveStatus)
                            .append("HomeCourt", homeCourt)
                            .append("Owner", firstOwner);
                    collection.insertOne(team);
                    this.updateUserDetails(firstOwner,teamName,"owners","CurrentTeam");
                    return true;
                }catch (MongoException e){
                    try {
                        syserror.addErrorLog("Server","Connection with db Lost");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    throw new MongoException("Failed to connect the DB!");
                }
            }
        }
        return false;
    }

    public boolean checkOwnerNotHaveTeam(String ownerUserName) {
        boolean ownerNotHaveTeam =  false;
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("owners");
            Document userFound = (Document) collection.find(new Document("UserName", ownerUserName)).first();
            String  ownerTeam = userFound.getString("CurrentTeam");
            if(ownerTeam.equals("")){
                ownerNotHaveTeam =true;
            }
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
        return ownerNotHaveTeam;
    }

    @Override
    public boolean isTeamExist(String giveTeamName) {
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("teams");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                Document team = cursor.next();
                if(team.containsValue(giveTeamName)){
                    return true;
                }
            }
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
        return false;
    }

    @Override
    public TeamInfo getTeam(String givenTeamName) {
        boolean isFound =false;
        TeamInfo team = null;
        if(this.isTeamExist(givenTeamName)){
            try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
                MongoDatabase database = mongoClient.getDatabase("footballdb");
                MongoCollection<Document> collection = database.getCollection("teams");
                MongoCursor<Document> cursor = collection.find().iterator();
                while(cursor.hasNext() &&!isFound){
                    Document teamCheck = cursor.next();
                    if(teamCheck.containsValue(givenTeamName)){
                        isFound = true;
                        String teamName = teamCheck.getString("TeamName");
                        int establishYear = (Integer)teamCheck.get("EstablishYear");
                        boolean teamStatus = (Boolean)teamCheck.get("TeamActiveStatus");
                        String  city = teamCheck.getString("CityRepresent");
                        String teamHomeCourt = teamCheck.getString("HomeCourt");
                        //String  ownerName = teamCheck.getString("Owner");
                        //TeamMember owner = (TeamMember)this.getUser(ownerName);
                        team = new TeamInfo(teamName,establishYear,teamStatus,city);
                        if(this.isCourtExist(teamHomeCourt)) {
                            Court court = this.getCourt(teamHomeCourt);
                            team.setTeamHomeCourt(court);
                        }
                        team.setTeamPlayers(this.getAllTeamPlayers(givenTeamName));
                        team.setTeamManagers(this.getAllTeamManagers(givenTeamName));
                        team.setTeamCoaches(this.getAllTeamCoaches(givenTeamName));
                        team.setTeamOwners(this.getAllTeamOwners(givenTeamName,team));
                        team.setTeamOwner(team.getTeamOwners().get(0));
                        int currentSeason = this.getTheCurrentSeason();
                        Document teamDetails = this.getTeamDetailsInSeason(givenTeamName,currentSeason);
                        if(teamDetails!=null) {
                            int numGames = (Integer) teamDetails.get("NumOfMatchPlayed");
                            int winCount = (Integer) teamDetails.get("WinCount");
                            int drawCount = (Integer) teamDetails.get("DrawCount");
                            int defeatCount = (Integer) teamDetails.get("DefeatCount");
                            int scoreGoalsCount = (Integer) teamDetails.get("ScoreGoalsCount");
                            int receivedGoalsCount = (Integer) teamDetails.get("ReceivedGoalsCount");
                            int points = (Integer) teamDetails.get("Points");
//                            double budgetAmount = (Double) teamDetails.get("BudgetAmount");
                            this.setTeamLeagueDetails(team, numGames, winCount, drawCount, defeatCount, scoreGoalsCount, receivedGoalsCount,
                                    points);
                        }
                    }
                }
            }catch (MongoException e){
                try {
                    syserror.addErrorLog("Server","Connection with db Lost");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                throw new MongoException("Failed to connect the DB!");
            }
        }
        return team;
    }

    private void setTeamLeagueDetails(TeamInfo team, int numGames, int winCount, int drawCount, int defeatCount,
                                      int scoreGoalsCount, int getGoalsCount, int points) {
        team.setMatchesPlayed(numGames);
        team.setWinCount(winCount);
        team.setDrawCount(drawCount);
        team.setDefeatCount(defeatCount);
        team.setScoreGoalsCount(scoreGoalsCount);
        team.setReceivedGoalsAmount(getGoalsCount);
        team.setPoints(points);
//        team.getTeamSeasonBudget().setSeasonAmountBudget(budgetAmount);
    }

    @Override
    public boolean removeTeam(String givenTeam) {
        if (this.isTeamExist(givenTeam)) {
            try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
                MongoDatabase database = mongoClient.getDatabase("footballdb");
                MongoCollection<Document> collection = database.getCollection("teams");
                DeleteResult deleteResult = collection.deleteOne(eq("TeamName", givenTeam));
                if(deleteResult.getDeletedCount()>0){
                    return true;
                }
            }catch (MongoException e){
                try {
                    syserror.addErrorLog("Server","Connection with db Lost");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                throw new MongoException("Failed to connect the DB!");
            }
        }
        return false;
    }

    @Override
    public boolean addCourt(String courtName, String courtCityLocation, int courtCapacity) {
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("courts");
            Document court = new Document("CourtName", courtName)
                    .append("CourtCityLocation", courtCityLocation)
                    .append("CourtCapacity", courtCapacity);
            collection.insertOne(court);
            return true;
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    @Override
    public boolean isCourtExist(String givenCourtName) {
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("courts");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                Document court = cursor.next();
                if(court.containsValue(givenCourtName)){
                    return true;
                }
            }
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
        return false;
    }

    @Override
    public Court getCourt(String givenCourtName) {
        boolean isFound =false;
        Court court = null;
        if(this.isCourtExist(givenCourtName)) {
            try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
                MongoDatabase database = mongoClient.getDatabase("footballdb");
                MongoCollection<Document> collection = database.getCollection("courts");
                MongoCursor<Document> cursor = collection.find().iterator();
                while (cursor.hasNext() && !isFound) {
                    Document courtCheck = cursor.next();
                    if (courtCheck.containsValue(givenCourtName)) {
                        isFound = true;
                        String courtName = courtCheck.getString("CourtName");
                        String courtCityLocation = courtCheck.getString("CourtCityLocation");
                        int capacity = (Integer) courtCheck.get("CourtCapacity");
                        court = new Court(courtName, courtCityLocation, capacity);
                    }
                }
            }catch (MongoException e){
                try {
                    syserror.addErrorLog("Server","Connection with db Lost");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                throw new MongoException("Failed to connect the DB!");
            }
        }
        return court;
    }

    @Override
    public boolean removeCourt(String givenCourtName) {
        if (this.isCourtExist(givenCourtName)) {
            try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
                MongoDatabase database = mongoClient.getDatabase("footballdb");
                MongoCollection<Document> collection = database.getCollection("courts");
                DeleteResult deleteResult = collection.deleteOne(eq("CourtName", givenCourtName));
                if(deleteResult.getDeletedCount()>0){
                    return true;
                }
            }catch (MongoException e){
                try {
                    syserror.addErrorLog("Server","Connection with db Lost");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                throw new MongoException("Failed to connect the DB!");
            }
        }
        return false;
    }

    @Override
    public boolean addSeason(int seasonYear, double seasonBudget) {
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("seasons");
            Document leagueAndSeason = new Document("SeasonYear", seasonYear)
                    .append("AssociationBudget", seasonBudget);
            collection.insertOne(leagueAndSeason);
            return true;
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    @Override
    public boolean deleteSeason(int season) {
        return false;
    }

    @Override
    public Season getSeasonByYear(int Season) {
        return null;
    }

    @Override
    public boolean addLeagueAndPolicyToSeason(int seasonYear,String leagueName,String date, int pointsPerWin,int pointsPerLoss,int pointsPerDraw,
                                              boolean differenceGoals,boolean straightMeets, int roundsNum, int numOfTeams) {
        if(this.isSeasonExist(seasonYear)) {
            try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
                MongoDatabase database = mongoClient.getDatabase("footballdb");
                MongoCollection<Document> collection = database.getCollection("seasonsAndLeaguesPolicy");
                Document leagueAndSeasonPolicy = new Document("SeasonYear", seasonYear)
                        .append("LeagueName", leagueName)
                        .append("Date", date)
                        .append("PointsPerWin", pointsPerWin)
                        .append("PointsPerLoss", pointsPerLoss)
                        .append("PointsPerDraw", pointsPerDraw)
                        .append("DifferenceGoals", differenceGoals)
                        .append("StraightMeets", straightMeets)
                        .append("NumOfRounds", roundsNum)
                        .append("NumOfTeams", numOfTeams);
                collection.insertOne(leagueAndSeasonPolicy);
                return true;
            }catch (MongoException e){
                try {
                    syserror.addErrorLog("Server","Connection with db Lost");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                throw new MongoException("Failed to connect the DB!");
            }
        }
        return false;
    }

    @Override
    public boolean isLeagueExistInSeason(int seasonYear, String leagueName) {
        boolean isLeagueFound = false;
        if(this.isSeasonExist(seasonYear)) {
            try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
                MongoDatabase database = mongoClient.getDatabase("footballdb");
                MongoCollection<Document> usersCollection = database.getCollection("seasonsAndLeaguesPolicy");
                MongoCursor<Document> cursor = usersCollection.find().iterator();
                while (cursor.hasNext() && !isLeagueFound) {
                    Document seasonAndLeague = cursor.next();
                    if (seasonAndLeague.containsValue(seasonYear)) {
                        //isSeasonFound = true;
                        int seasonYearFound = (Integer) seasonAndLeague.get("SeasonYear");
                        String leagueFound = seasonAndLeague.getString("LeagueName");
                        if(seasonYear == seasonYearFound && leagueName.equals(leagueFound)){
                            isLeagueFound = true;
                        }
                    }
                }
            }catch (MongoException e){
                try {
                    syserror.addErrorLog("Server","Connection with db Lost");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                throw new MongoException("Failed to connect the DB!");
            }
        }
        return isLeagueFound;
    }

    @Override
    public League getLeagueByName(String leagueName) {
        League league = null;
        int currentSeason = this.getTheCurrentSeason();
        if(this.isLeagueExistInSeason(currentSeason,leagueName)){
            boolean isLeagueFound = false;
            try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
                MongoDatabase database = mongoClient.getDatabase("footballdb");
                MongoCollection<Document> usersCollection = database.getCollection("seasonsAndLeaguesPolicy");
                MongoCursor<Document> cursor = usersCollection.find().iterator();
                while (cursor.hasNext() && !isLeagueFound) {
                    Document seasonAndLeague = cursor.next();
                    if (seasonAndLeague.containsValue(currentSeason)) {
                        //isSeasonFound = true;
                        int seasonYearFound = (Integer) seasonAndLeague.get("SeasonYear");
                        String leagueFound = seasonAndLeague.getString("LeagueName");
                        if (currentSeason == seasonYearFound && leagueName.equals(leagueFound)) {
                            isLeagueFound = true;
                            String date = seasonAndLeague.getString("Date");
                            int pointsPerWin = (Integer) seasonAndLeague.get("PointsPerWin");
                            int pointsPerDraw = (Integer) seasonAndLeague.get("PointsPerDraw");
                            int pointsPerLoss = (Integer) seasonAndLeague.get("PointsPerLoss");
                            boolean differenceGoals = (Boolean) seasonAndLeague.get("DifferenceGoals");
                            boolean straightMeets = (Boolean) seasonAndLeague.get("StraightMeets");
                            int numOfRounds = (Integer) seasonAndLeague.get("NumOfRounds");
                            int numOfTeams = (Integer) seasonAndLeague.get("NumOfTeams");
                            league = new League(leagueFound,numOfTeams,numOfRounds,date);
                            PointsPolicy policy = new PointsPolicy(pointsPerWin,pointsPerDraw,pointsPerLoss,differenceGoals,
                                    straightMeets);
                            league.setPointsPolicy(policy);
//                            this.setLeagueDetails(league,pointsPerWin,pointsPerDraw,pointsPerLoss,differenceGoals,
//                                    straightMeets);
                            this.setTeamAndCourtsToLeague(currentSeason,league);
                            this.setRefereesToLeague(league);
                        }
                    }
                }
            }catch (MongoException e){
                try {
                    syserror.addErrorLog("Server","Connection with db Lost");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                throw new MongoException("Failed to connect the DB!");
            }
        }
        return league;
    }

    private void setRefereesToLeague(League league) {
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> usersCollection = database.getCollection("referees");
            MongoCursor<Document> cursor = usersCollection.find().iterator();
            while (cursor.hasNext()) {
                Document refereeData = cursor.next();
                if (refereeData.containsValue(league.getLeagueName())) {
                    String refUserName = refereeData.getString("UserName");
                    Referee ref = (Referee) this.getReferee(refUserName);
                    if(ref.getRefereeRole().equals("Main Referee")){
                        league.getMainReferees().add(ref);
                    }
                    if(ref.getRefereeRole().equals("Side Referee")){
                        league.getSideReferees().add(ref);
                    }
                }
            }
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    private void setTeamAndCourtsToLeague(int currentSeason, League league) {
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> usersCollection = database.getCollection("teamsAndLeagues");
            MongoCursor<Document> cursor = usersCollection.find().iterator();
            while (cursor.hasNext()) {
                Document seasonAndLeague = cursor.next();
                if (seasonAndLeague.containsValue(currentSeason)) {
                    //isSeasonFound = true;
                    int seasonYearFound = (Integer) seasonAndLeague.get("SeasonYear");
                    String leagueFound = seasonAndLeague.getString("LeagueName");
                    if(currentSeason == seasonYearFound && league.getLeagueName().equals(leagueFound)){
                        String teamName = seasonAndLeague.getString("TeamName");
                        TeamInfo team = this.getTeam(teamName);
                        league.getTeams().add(team);
                        league.getCourts().add(team.getTeamHomeCourt());
                    }
                }
            }
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

//    private void setLeagueDetails(League league, int pointsPerWin, int pointsPerDraw, int pointsPerLoss, boolean differenceGoals, boolean straightMeets) {
//        league.getPointsPolicy().setPointsForWin(pointsPerWin);
//        league.getPointsPolicy().setPointsForDraw(pointsPerDraw);
//        league.getPointsPolicy().setPointsForLoss(pointsPerLoss);
//        league.getPointsPolicy().setGoalDiffTieBreaker(differenceGoals);
//        league.getPointsPolicy().setDirectResultsTieBreaker(straightMeets);
//    }


    @Override
    public boolean deleteLeague(String league) {
        return false;
    }

    @Override
    public boolean addTeamToLeague(String teamName, int seasonYear, String leagueName) {
        if(this.isSeasonExist(seasonYear)){
            if(this.isLeagueExistInSeason(seasonYear,leagueName)){
                if(this.isTeamExist(teamName)) {
                    try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
                        MongoDatabase database = mongoClient.getDatabase("footballdb");
                        MongoCollection<Document> collection = database.getCollection("teamsAndLeagues");
                        Document teamSign = new Document("SeasonYear", seasonYear)
                                .append("LeagueName", leagueName)
                                .append("TeamName", teamName)
                                .append("NumOfMatchPlayed", 0)
                                .append("WinCount", 0)
                                .append("DrawCount", 0)
                                .append("DefeatCount", 0)
                                .append("ScoreGoalsCount", 0)
                                .append("ReceivedGoalsCount", 0)
                                .append("Points", 0);
                        collection.insertOne(teamSign);
                        return true;
                    }catch (MongoException e){
                        try {
                            syserror.addErrorLog("Server","Connection with db Lost");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        throw new MongoException("Failed to connect the DB!");
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean addGameDetails(int seasonYear, String leagueName, String date, String homeTeam, String awayTeam,
                                  String courtName, String mainReferee, String sideFirstRef, String sideSecRef, String assistantRef) {
        if(this.isSeasonExist(seasonYear)) {
            if (this.isLeagueExistInSeason(seasonYear, leagueName)) {
                try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
                    MongoDatabase database = mongoClient.getDatabase("footballdb");
                    MongoCollection<Document> collection = database.getCollection("matchesDetails");
                    Document match = new Document("SeasonYear", seasonYear)
                            .append("LeagueName", leagueName)
                            .append("Date", date)
                            .append("HomeTeam", homeTeam)
                            .append("AwayTeam", awayTeam)
                            .append("CourtName", courtName)
                            .append("MainReferee", mainReferee)
                            .append("SideFirstReferee", sideFirstRef)
                            .append("SideSecondReferee", sideSecRef)
                            .append("AssistantReferee", assistantRef)
                            .append("HomeGoals", 0)
                            .append("AwayGoals", 0);
                    collection.insertOne(match);
                    return true;
                }catch (MongoException e){
                    try {
                        syserror.addErrorLog("Server","Connection with db Lost");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    throw new MongoException("Failed to connect the DB!");
                }
            }
        }
        return false;
    }

    @Override
    public boolean updateUserDetails(String userName, Object newValue, String tableName, String tableCol) {
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection(tableName);
            Document userFound = (Document) collection.find(new Document("UserName", userName)).first();
            if (userFound != null) {
                Bson updatedValue = new Document(tableCol, newValue);
                Bson updateOperation = new Document("$set", updatedValue);
                collection.updateOne(userFound,updateOperation);
                return true;
            }
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
        return false;
    }

    //@Override
    public boolean updateTeamDetails(String teamName, Object newValue, String tableName, String tableCol) {
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection(tableName);
            Document userFound = (Document) collection.find(new Document("TeamName", teamName)).first();
            if (userFound != null) {
                Bson updatedValue = new Document(tableCol, newValue);
                Bson updateOperation = new Document("$set", updatedValue);
                collection.updateOne(userFound,updateOperation);
                return true;
            }
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
        return false;
    }

    //    @Override
    public boolean updateLeagueDetails(String leagueName, Object newValue, String tableName, String tableCol) {
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection(tableName);//"SeasonYear",getTheCurrentSeason()
            Document leagueFound = (Document) collection.find(new Document("SeasonYear",getTheCurrentSeason()).append("LeagueName", leagueName)).first();
            if (leagueFound != null) {
                Bson updatedValue = new Document(tableCol, newValue);
                Bson updateOperation = new Document("$set", updatedValue);
                collection.updateOne(leagueFound,updateOperation);
                return true;
            }
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
        return false;
    }

    public boolean updateAlertDetails(String userName,String alertContent) {
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("usersAlerts");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                Document alertUser = cursor.next();
                if(alertUser.containsValue(userName) && alertUser.containsValue(alertContent)){
                    Bson updatedValue = new Document("isViewed", true);
                    Bson updateOperation = new Document("$set", updatedValue);
                    collection.updateOne(alertUser,updateOperation);
                    return true;
                }
            }
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
        return false;
    }

    @Override
    public boolean addUserAlert(String userNameDestination, String alertType, String alertContent, boolean isWatched) {
        if(this.isUserExist(userNameDestination)) {
            try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
                MongoDatabase database = mongoClient.getDatabase("footballdb");
                MongoCollection<Document> collection = database.getCollection("usersAlerts");
                Document Alert = new Document("UserNameToSend", userNameDestination)
                        .append("AlertType", alertType)
                        .append("AlertContent", alertContent)
                        .append("isViewed", isWatched);
                collection.insertOne(Alert);
                return true;
            } catch (MongoException e) {
                try {
                    syserror.addErrorLog("Server","Connection with db Lost");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                throw new MongoException("Failed to connect the DB!");
            }
        }
        return false;
    }

    @Override
    public void resetDB() {

    }

    private boolean isUserAssignToAlerts(String userNameDestination) {
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("users");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                Document user = cursor.next();
                if(user.containsValue(userNameDestination)){
                    boolean isAssignToAlerts = (Boolean)user.get("AssignToAlerts");
                    if(isAssignToAlerts){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
            }
            return false;
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    private boolean addPlayerToDB(String userName, String roleOnCourt, String currentTeam, String employedBy, String conformationCode){
        if(conformationCode.equals("Player")){
            try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
                MongoDatabase database = mongoClient.getDatabase("footballdb");
                MongoCollection<Document> collection = database.getCollection("players");
                Document player = new Document("UserName", userName)
                        .append("RoleOnCourt", roleOnCourt)
                        .append("CurrentTeam", currentTeam)
                        .append("EmployedBy", employedBy)
                        .append("ConfirmationCode", conformationCode);
                collection.insertOne(player);
                return true;
            }catch (MongoException e){
                try {
                    syserror.addErrorLog("Server","Connection with db Lost");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                throw new MongoException("Failed to connect the DB!");
            }
        }
        return false;
    }

    private boolean addCoachToDB(String userName, String coachRole, String currentTeam, String employedBy, String conformationCode){
        if(conformationCode.equals("Coach")){
            try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
                MongoDatabase database = mongoClient.getDatabase("footballdb");
                MongoCollection<Document> collection = database.getCollection("coaches");
                Document coach = new Document("UserName", userName)
                        .append("CoachRole", coachRole)
                        .append("CurrentTeam", currentTeam)
                        .append("EmployedBy", employedBy)
                        .append("ConfirmationCode", conformationCode);
                collection.insertOne(coach);
                return true;
            }catch (MongoException e){
                try {
                    syserror.addErrorLog("Server","Connection with db Lost");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                throw new MongoException("Failed to connect the DB!");
            }
        }
        return false;
    }

    private boolean addTeamMangerToDB(String userName, String currentTeam, String employedBy,boolean coachPermission,
                                      boolean playerPermission,boolean teamManagerPermission,boolean ownerPermission,
                                      boolean courtPermission,String conformationCode){
        if(conformationCode.equals("TeamManager")){
            try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
                MongoDatabase database = mongoClient.getDatabase("footballdb");
                MongoCollection<Document> collection = database.getCollection("teamManagers");
                Document teamManager = new Document("UserName", userName)
                        .append("CurrentTeam", currentTeam)
                        .append("EmployedBy", employedBy)
                        .append("CoachPermission", coachPermission)
                        .append("PlayerPermission", playerPermission)
                        .append("TeamManagerPermission", teamManagerPermission)
                        .append("OwnerPermission", ownerPermission)
                        .append("CourtPermission", courtPermission)
                        .append("ConfirmationCode", conformationCode);
                collection.insertOne(teamManager);
                return true;
            }catch (MongoException e){
                try {
                    syserror.addErrorLog("Server","Connection with db Lost");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                throw new MongoException("Failed to connect the DB!");
            }
        }
        return false;
    }

    private boolean addOwnerToDB(String userName, String currentTeam, String employedBy,boolean coachPermission,
                                 boolean playerPermission,boolean teamManagerPermission,boolean ownerPermission,
                                 boolean courtPermission,String conformationCode){
        if(conformationCode.equals("Owner")){
            try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
                MongoDatabase database = mongoClient.getDatabase("footballdb");
                MongoCollection<Document> collection = database.getCollection("owners");
                Document owner = new Document("UserName", userName)
                        .append("CurrentTeam", currentTeam)
                        .append("EmployedBy", employedBy)
                        .append("CoachPermission", coachPermission)
                        .append("PlayerPermission", playerPermission)
                        .append("TeamManagerPermission", teamManagerPermission)
                        .append("OwnerPermission", ownerPermission)
                        .append("CourtPermission", courtPermission)
                        .append("ConfirmationCode", conformationCode);
                collection.insertOne(owner);
                return true;
            }catch (MongoException e){
                try {
                    syserror.addErrorLog("Server","Connection with db Lost");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                throw new MongoException("Failed to connect the DB!");
            }
        }
        return false;
    }

    private boolean isPlayerExist(String userName) {
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("players");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                Document user = cursor.next();
                if(user.containsValue(userName)){
                    return true;
                }
            }
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
        return false;
    }

    private boolean isCoachExist(String userName) {
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("coaches");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                Document user = cursor.next();
                if(user.containsValue(userName)){
                    return true;
                }
            }
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
        return false;
    }

    private boolean isOwnerExist(String userName) {
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("owners");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                Document user = cursor.next();
                if(user.containsValue(userName)){
                    return true;
                }
            }
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
        return false;
    }

    private boolean isTeamManagerExist(String userName) {
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("teamManagers");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                Document user = cursor.next();
                if(user.containsValue(userName)){
                    return true;
                }
            }
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
        return false;
    }

    private String getOccupationOfUser(String userName) {
        String occupation ="";
        boolean isFound = false;
        if (this.isUserExist(userName)) {
            try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
                MongoDatabase database = mongoClient.getDatabase("footballdb");
                MongoCollection<Document> collection = database.getCollection("users");
                MongoCursor<Document> cursor = collection.find().iterator();
                while (cursor.hasNext() && !isFound) {
                    Document user = cursor.next();
                    if (user.containsValue(userName)) {
                        isFound = true;
                        occupation = user.getString("Occupation");
                    }
                }
            }catch (MongoException e){
                try {
                    syserror.addErrorLog("Server","Connection with db Lost");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                throw new MongoException("Failed to connect the DB!");
            }
        }
        return occupation;
    }

    private Document getOwnerDetails(String userName){
        Document ownerDetails = null;
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("owners");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                ownerDetails = cursor.next();
                if(ownerDetails.containsValue(userName)){
                    return ownerDetails;
                }
            }
            return ownerDetails;
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    public Document getPlayerDetails(String userName){
        Document playerDetails = null;
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("players");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                playerDetails = cursor.next();
                if(playerDetails.containsValue(userName)){
                    return playerDetails;
                }
            }
            return playerDetails;
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    private Document getCoachDetails(String userName){
        Document coachDetails = null;
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("coaches");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                coachDetails = cursor.next();
                if(coachDetails.containsValue(userName)){
                    return coachDetails;
                }
            }
            return coachDetails;
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    private Document getTeamManagerDetails(String userName){
        Document teamManagerDetails = null;
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("teamManagers");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                teamManagerDetails = cursor.next();
                if(teamManagerDetails.containsValue(userName)){
                    return teamManagerDetails;
                }
            }
            return teamManagerDetails;
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    private boolean addRefereeToDB(String userName,String refereeRole,int seasonYear,String leagueName,String verificationCode) {
        if(verificationCode.equals("Referee")){
            try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
                MongoDatabase database = mongoClient.getDatabase("footballdb");
                MongoCollection<Document> collection = database.getCollection("referees");
                Document referee = new Document("UserName", userName)
                        .append("RefereeRole", refereeRole)
                        .append("SeasonYear", seasonYear)
                        .append("LeagueName", leagueName)
                        .append("ConfirmationCode", verificationCode);
                collection.insertOne(referee);
                return true;
            }catch (MongoException e){
                try {
                    syserror.addErrorLog("Server","Connection with db Lost");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                throw new MongoException("Failed to connect the DB!");
            }
        }
        return false;
    }

    private Document getRefereeDetails(String givenUserName) {
        Document refereeDetails = null;
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("referees");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                refereeDetails = cursor.next();
                if(refereeDetails.containsValue(givenUserName)){
                    return refereeDetails;
                }
            }
            return refereeDetails;
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    private boolean isRefereeExist(String givenUserName) {
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("referees");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                Document user = cursor.next();
                if(user.containsValue(givenUserName)){
                    return true;
                }
            }
            return false;
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    private ArrayList<PlayerInterface> getAllTeamPlayers(String teamName){
        ArrayList<PlayerInterface> teamPlayers = new ArrayList<PlayerInterface>();
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("players");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                Document player = cursor.next();
                if(player.containsValue(teamName)){
                    String userName = player.getString("UserName");
                    TeamMember teamPlayer = (TeamMember)this.getTeamMember(userName);
                    teamPlayers.add(teamPlayer);
                }
            }
            return teamPlayers;
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    private ArrayList<CoachInterface> getAllTeamCoaches(String teamName){
        ArrayList<CoachInterface> teamCoaches = new ArrayList<CoachInterface>();
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("coaches");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                Document coach = cursor.next();
                if(coach.containsValue(teamName)){
                    String userName = coach.getString("UserName");
                    TeamMember teamCoach = (TeamMember)this.getTeamMember(userName);
                    teamCoaches.add(teamCoach);
                }
            }
            return teamCoaches;
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    private ArrayList<OwnerInterface> getAllTeamOwners(String teamName, TeamInfo team){
        ArrayList<OwnerInterface> teamOwners = new ArrayList<OwnerInterface>();
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("owners");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                Document owner = cursor.next();
                if(owner.containsValue(teamName)){
                    String userName = owner.getString("UserName");
                    TeamMember teamOwner = (TeamMember)this.getTeamMember(userName);
                    teamOwner.setTeam(team);
                    teamOwners.add(teamOwner);
                }
            }
            return teamOwners;
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }

    }

    private ArrayList<TeamManagerInterface> getAllTeamManagers(String teamName){
        ArrayList<TeamManagerInterface> teamManagers = new ArrayList<TeamManagerInterface>();
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("teamManagers");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                Document Manager = cursor.next();
                if(Manager.containsValue(teamName)){
                    String userName = Manager.getString("UserName");
                    TeamMember teamManager = (TeamMember)this.getTeamMember(userName);
                    teamManagers.add(teamManager);
                }
            }
            return teamManagers;
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

//    private boolean isLeagueExist(String leagueName) {
//        boolean isLeagueExist = false;
//        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
//            MongoDatabase database = mongoClient.getDatabase("footballdb");
//            MongoCollection<Document> collection = database.getCollection("leagues and seasons");
//            Document leagueFound = (Document) collection.find(new Document("LeagueName", leagueName)).first();
//            if (leagueFound != null) {
//                String foundLeagueName = leagueFound.getString("LeagueName");
//                if (foundLeagueName.equals(leagueName)) {
//                    isLeagueExist = true;
//                }
//            }
//            return isLeagueExist;
//        }catch (MongoException e){
//            throw new MongoException("Failed to connect the DB!");
//        }
//    }

    @Override
    public boolean isSeasonExist(int seasonYear){
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("seasons");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                Document season = cursor.next();
                if(season.containsValue(seasonYear)){
                    return true;
                }
            }
            return false;
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    public Season getSeason(){
        Season returnSeason = null;
        boolean isSeasonFound = false;
        int currentSeason = this.getTheCurrentSeason();
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> usersCollection = database.getCollection("seasons");
            MongoCursor<Document> cursor = usersCollection.find().iterator();
            while (cursor.hasNext() && !isSeasonFound) {
                Document season = cursor.next();
                if (season.containsValue(currentSeason)) {
                    isSeasonFound = true;
                    int seasonYearFound = (Integer) season.get("SeasonYear");
                    //double seasonBudget = (Double) season.get("AssociationBudget");//need to check
                    returnSeason = new Season(seasonYearFound);
                    this.setAllLeaguesToTheSeason(returnSeason);
                    this.setAllRefereesToSeason(returnSeason);
                }
            }
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
        return returnSeason;
    }

    private void setAllRefereesToSeason(Season returnSeason) {
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> usersCollection = database.getCollection("referees");
            MongoCursor<Document> cursor = usersCollection.find().iterator();
            while (cursor.hasNext()) {
                Document referee = cursor.next();
                if (referee.containsValue(returnSeason.getYear())) {
                    String refereeUserName = referee.getString("UserName");
                    Referee ref = (Referee)this.getReferee(refereeUserName);
                    if(ref.getRefereeRole().equals("Main Referee")){
                        returnSeason.getMainActiveReferees().add(ref);
                    }
                    else if(ref.getRefereeRole().equals("Side Referee")){
                        returnSeason.getSideActiveReferees().add(ref);
                    }
                }
            }
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    private void setAllLeaguesToTheSeason(Season returnSeason) {
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> usersCollection = database.getCollection("seasonsAndLeaguesPolicy");
            MongoCursor<Document> cursor = usersCollection.find().iterator();
            while (cursor.hasNext()) {
                Document seasonAndLeague = cursor.next();
                if (seasonAndLeague.containsValue(returnSeason.getYear())) {
                    String leagueName = seasonAndLeague.getString("LeagueName");
                    League league = this.getLeagueByName(leagueName);
                    returnSeason.getAllLeagues().add(league);
                }
            }
        }
    }

    public int getTheCurrentSeason() {
        int currentSeasonYear = 0;
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> usersCollection = database.getCollection("seasons");
            MongoCursor<Document> cursor = usersCollection.find().iterator();
            while (cursor.hasNext()) {
                Document season = cursor.next();
                int seasonYearFound = (Integer) season.get("SeasonYear");
                if (seasonYearFound > currentSeasonYear) {
                    currentSeasonYear = seasonYearFound;
                }
            }
            return currentSeasonYear;
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    private Document getTeamDetailsInSeason(String teamName,int seasonYear){
        Document teamDetailsInSeason = null;
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("teamsAndLeagues");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                teamDetailsInSeason = cursor.next();
                if(teamDetailsInSeason.containsValue(seasonYear) && teamDetailsInSeason.containsValue(teamName)){
                    return teamDetailsInSeason;
                }
            }
            return teamDetailsInSeason;
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    public boolean updateMatchResult(int Season,String homeTeam, String awayTeam,String mainReferee,String teamScore) {
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("matchesDetails");
            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext()) {
                Document match = cursor.next();
                if (match.containsValue(Season) && match.containsValue(homeTeam) &&
                        match.containsValue(awayTeam) && match.containsValue(mainReferee)) {
                    String team1 = match.getString("HomeTeam");
                    String team2 = match.getString("AwayTeam");
                    if (team1.equals(teamScore)) {
                        int homeGoals = match.getInteger("HomeGoals");
                        homeGoals = homeGoals++;
                        Bson updatedValue = new Document("HomeGoals", homeGoals);
                        Bson updateOperation = new Document("$set", updatedValue);
                        collection.updateOne(match, updateOperation);
                        return true;
                    } else if (team2.equals(teamScore)) {
                        int awayGoals = match.getInteger("AwayGoals");
                        awayGoals = awayGoals++;
                        Bson updatedValue = new Document("AwayGoals", awayGoals);
                        Bson updateOperation = new Document("$set", updatedValue);
                        collection.updateOne(match, updateOperation);
                        return true;
                    }
                }
            }
        } catch (MongoException e) {
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
        return false;
    }

    public ArrayList<Referee> getAllReferees() {
        ArrayList<Referee> allReferees = new ArrayList<Referee>();
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("referees");
            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext()) {
                Document referee = cursor.next();
                String refereeUserName = referee.getString("UserName");
                int season = referee.getInteger("SeasonYear");
                if(season == 0){
                    Referee ref = (Referee) this.getUser(refereeUserName);
                    allReferees.add(ref);
                }
            }
            return allReferees;
        } catch (MongoException e) {
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    public ArrayList<Referee> getAllRefereesWithSeasonWithoutLeague() {
        ArrayList<Referee> allReferees = new ArrayList<Referee>();
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("referees");
            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext()) {
                Document referee = cursor.next();
                String refereeUserName = referee.getString("UserName");
                int season = referee.getInteger("SeasonYear");
                String league = referee.getString("LeagueName");
                if(season != 0 && league.equals("")){
                    Referee ref = (Referee) this.getUser(refereeUserName);
                    allReferees.add(ref);
                }
            }
            return allReferees;
        } catch (MongoException e) {
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    public ArrayList<League> getAllLeaguesInCurrentSeason() {
        int currentSeason = this.getTheCurrentSeason();
        ArrayList<League> allLeaguesInCurrentSeason = new ArrayList<League>();
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("seasonsAndLeaguesPolicy");
            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext()) {
                Document leagueInCurrentSeason = cursor.next();
                if(leagueInCurrentSeason.containsValue(currentSeason)){
                    String leagueName = leagueInCurrentSeason.getString("LeagueName");
                    League league = this.getLeagueByName(leagueName);
                    allLeaguesInCurrentSeason.add(league);
                }
            }
            return allLeaguesInCurrentSeason;
        } catch (MongoException e) {
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    public ArrayList<AssociationUser> getAllAssociationUsers(){
        ArrayList<AssociationUser> allAssociationUsers = new ArrayList<AssociationUser>();
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> usersCollection = database.getCollection("users");
            MongoCursor<Document> cursor = usersCollection.find().iterator();
            while (cursor.hasNext()) {
                Document user = cursor.next();
                String occupation = user.getString("Occupation");//Association
                if (occupation.equals("Association")) {
                    String userName = user.getString("UserName");
                    AssociationUser association = (AssociationUser) this.getUser(userName);
                    allAssociationUsers.add(association);
                }
            }
        } catch (MongoException e) {
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
        return allAssociationUsers;
    }

    public ArrayList<SystemManager> getAllSystemManagers(){
        ArrayList<SystemManager> allSystemManagers = new ArrayList<SystemManager>();
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> usersCollection = database.getCollection("users");
            MongoCursor<Document> cursor = usersCollection.find().iterator();
            while (cursor.hasNext()) {
                Document user = cursor.next();
                String occupation = user.getString("Occupation");//Association
                if (occupation.equals("SystemManager")) {
                    String userName = user.getString("UserName");
                    SystemManager systemManager = (SystemManager) this.getUser(userName);
                    allSystemManagers.add(systemManager);
                }
            }
        } catch (MongoException e) {
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
        return allSystemManagers;
    }

    public Fan getTeamMember(String givenUserName) {
        boolean isFound =false;
        Fan fan = null;
        if(this.isUserExist(givenUserName)){
            try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
                MongoDatabase database = mongoClient.getDatabase("footballdb");
                MongoCollection<Document> usersCollection = database.getCollection("users");
                MongoCursor<Document> cursor = usersCollection.find().iterator();
                while(cursor.hasNext() &&!isFound){
                    Document user = cursor.next();
                    if(user.containsValue(givenUserName)){
                        isFound = true;
                        String userName = user.getString("UserName");
                        String firstName = user.getString("FirstName");
                        String lastName = user.getString("LastName");
                        String password = user.getString("Password");
                        String occupation = user.getString("Occupation");
                        String birthday = user.getString("Birthday");
                        String email = user.getString("Email");
                        boolean AssignToAlerts = (Boolean)user.get("AssignToAlerts");
                        if(occupation.equals("TeamMember")) {
                            fan = new TeamMember(firstName, lastName, userName, password, occupation, birthday, email,null);
                            fan.setAssignToAlerts(AssignToAlerts);
                            if(this.isOwnerExist(givenUserName)){
                                Document ownerDetails = this.getOwnerDetails(givenUserName);
                                //String teamName = ownerDetails.getString("CurrentTeam");
                                boolean coachPermission = (Boolean)ownerDetails.get("CoachPermission");
                                boolean playerPermission = (Boolean)ownerDetails.get("PlayerPermission");
                                boolean teamManagerPermission = (Boolean)ownerDetails.get("TeamManagerPermission");
                                boolean ownerPermission = (Boolean)ownerDetails.get("OwnerPermission");
                                boolean courtPermission = (Boolean)ownerDetails.get("CourtPermission");
                                this.setOwnerDetails((TeamMember)fan,true,coachPermission,playerPermission,teamManagerPermission,
                                        ownerPermission,courtPermission);
                            }
                            if(this.isTeamManagerExist(givenUserName)){
                                Document teamManagerDetails = this.getTeamManagerDetails(givenUserName);
                                String teamName = teamManagerDetails.getString("CurrentTeam");
                                boolean coachPermission = (Boolean)teamManagerDetails.get("CoachPermission");
                                boolean playerPermission = (Boolean)teamManagerDetails.get("PlayerPermission");
                                boolean teamManagerPermission = (Boolean)teamManagerDetails.get("TeamManagerPermission");
                                boolean ownerPermission = (Boolean)teamManagerDetails.get("OwnerPermission");
                                boolean courtPermission = (Boolean)teamManagerDetails.get("CourtPermission");
                                this.setTeamManagerDetails((TeamMember)fan,true,coachPermission,playerPermission,teamManagerPermission,
                                        ownerPermission,courtPermission);
                            }
                            if(this.isCoachExist(givenUserName)){
                                Document coachDetails = this.getCoachDetails(givenUserName);
                                String teamName = coachDetails.getString("CurrentTeam");
                                String coachRole = coachDetails.getString("CoachRole");
                                this.setCoachDetails((TeamMember)fan,true,coachRole);
                            }
                            if(this.isPlayerExist(givenUserName)){
                                Document playerDetails = this.getPlayerDetails(givenUserName);
                                String teamName = playerDetails.getString("CurrentTeam");
                                String roleOnCourt = playerDetails.getString("RoleOnCourt");
                                this.setCoachDetails((TeamMember)fan,true,roleOnCourt);
                            }
                        }//end teamMember
                    }
                }
                return fan;

            }catch (MongoException e){
                try {
                    syserror.addErrorLog("Server","Connection with db Lost");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                throw new MongoException("Failed to connect the DB!");
            }
        }
        return null;
    }

    public Fan getTeamMemberTeam(String givenUserName) {
        boolean isFound =false;
//        Fan fan = null;
//        if(this.isUserExist(givenUserName)){
//            try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
//                MongoDatabase database = mongoClient.getDatabase("footballdb");
//                MongoCollection<Document> teamMenagerCollection = database.getCollection("teamManagers");
//                MongoCollection<Document> ownerCollection = database.getCollection("owners");
//                MongoCursor<Document> cursor1 = teamMenagerCollection.find().iterator();
//                MongoCursor<Document> cursor2 = ownerCollection.find().iterator();
//                while(cursor1.hasNext() &&!isFound){
//                    Document user = cursor1.next();
//                    if(user.containsValue(givenUserName)){
//                        isFound = true;
//                        String userName = user.getString("UserName");
        if(this.isOwnerExist(givenUserName)){
            Document ownerDetails = this.getOwnerDetails(givenUserName);
            //String status=ownerDetails;
            String teamName = ownerDetails.getString("CurrentTeam");
        }
        if(this.isTeamManagerExist(givenUserName)) {
            Document teamManagerDetails = this.getTeamManagerDetails(givenUserName);
            String teamName = teamManagerDetails.getString("CurrentTeam");
        }
//            }catch (MongoException e){
//                throw new MongoException("Failed to connect the DB!");
//            }
        //}
        return null;
    }

    public Referee getReferee(String givenUserName) {
        boolean isFound =false;
        Referee fan = null;
        if(this.isUserExist(givenUserName)){
            try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
                MongoDatabase database = mongoClient.getDatabase("footballdb");
                MongoCollection<Document> usersCollection = database.getCollection("users");
                MongoCursor<Document> cursor = usersCollection.find().iterator();
                while(cursor.hasNext() &&!isFound){
                    Document user = cursor.next();
                    if(user.containsValue(givenUserName)){
                        isFound = true;
                        String userName = user.getString("UserName");
                        String firstName = user.getString("FirstName");
                        String lastName = user.getString("LastName");
                        String password = user.getString("Password");
                        String occupation = user.getString("Occupation");
                        String birthday = user.getString("Birthday");
                        String email = user.getString("Email");
                        boolean AssignToAlerts = (Boolean)user.get("AssignToAlerts");
                        if(occupation.equals("Referee")) {
                            fan = new Referee(firstName, lastName, userName, password, occupation, birthday, email,null);
                            String role = getRefereeRole(userName);
                            fan.setRefereeRole(role);
                            fan.setAssignToAlerts(AssignToAlerts);
                        }
                    }
                }
                return fan;

            }catch (MongoException e){
                try {
                    syserror.addErrorLog("Server","Connection with db Lost");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                throw new MongoException("Failed to connect the DB!");
            }
        }
        return null;
    }

    private String getRefereeRole(String userName) {
        String role= "";
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> usersCollection = database.getCollection("referees");
            MongoCursor<Document> cursor = usersCollection.find().iterator();
            while (cursor.hasNext()) {
                Document user = cursor.next();
                if (user.containsValue(userName)) {
                    role = user.getString("RefereeRole");
                }
            }
        }
        return role;

    }

    public ArrayList<String> getAvailableCoaches(){
        ArrayList<String> AvailablCoaches = new ArrayList<String>();
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("coaches");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                Document coach = cursor.next();
                String teamName = coach.getString("CurrentTeam");
                if(teamName.equals("")){
                    String userName = coach.getString("UserName");
                    TeamMember teamCoach = (TeamMember)this.getUser(userName);
                    //AvailablCoaches.add(teamCoach.getUserName()+", "+teamCoach.getTeamRole());
                    if(!teamCoach.getTeamRole().equals("null")){
                        AvailablCoaches.add(teamCoach.getUserName()+", "+teamCoach.getTeamRole());
                    }
                    else{
                        AvailablCoaches.add(teamCoach.getUserName());

                    }
                }
            }
            return AvailablCoaches;
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    public ArrayList<String> getAvailablePlayers(){
        ArrayList<String> AvailablPlayer = new ArrayList<String>();
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("players");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                Document player = cursor.next();
                String teamName = player.getString("CurrentTeam");
                if(teamName.equals("")){
                    String userName = player.getString("UserName");
                    TeamMember teamplayer = (TeamMember)this.getUser(userName);
                    //AvailablPlayer.add(teamplayer.getUserName()+", "+teamplayer.getRoleOnCourt());
                    if(!teamplayer.getRoleOnCourt().equals("null")){
                        AvailablPlayer.add(teamplayer.getUserName()+", "+teamplayer.getRoleOnCourt());
                    }
                    else{
                        AvailablPlayer.add(teamplayer.getUserName());
                    }
                }
            }
            return AvailablPlayer;
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    public ArrayList<String> getCourtByCity(String cityName) {

        boolean isFound =false;
        ArrayList<String> courtByCity = new ArrayList<String>();
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("courts");
            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext()) {
                Document courtCheck = cursor.next();
                if (courtCheck.containsValue(cityName)) {
                    String courtName = courtCheck.getString("CourtName");
                    String courtCityLocation = courtCheck.getString("CourtCityLocation");
                    int capacity = (Integer) courtCheck.get("CourtCapacity");
                    Court court = new Court(courtName, courtCityLocation, capacity);
                    courtByCity.add(court.getCourtName());
                }
            }
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }

        return courtByCity;
    }

    public ArrayList<String> getAvailableOwners(){
        ArrayList<String> AvailablOwners = new ArrayList<String>();
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("owners");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                Document owner = cursor.next();
                String teamName = owner.getString("CurrentTeam");
                if(teamName.equals("")){
                    String userName = owner.getString("UserName");
                    TeamMember teamplayer = (TeamMember)this.getUser(userName);
                    AvailablOwners.add(teamplayer.getUserName()+", "+teamplayer.getFirstName()+", "+teamplayer.getLastName());
                }
            }
            return AvailablOwners;
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    public ArrayList<String> getTeamOwners(String teamN){
        ArrayList<String> teamOwners = new ArrayList<String>();
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("owners");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                Document owner = cursor.next();
                String teamName = owner.getString("CurrentTeam");
                if(teamName.equals(teamN)){
                    String userName = owner.getString("UserName");
                    teamOwners.add(userName);
                }
            }
            return teamOwners;
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    public ArrayList<String> getAvailableManagers(){
        ArrayList<String> AvailableManagers = new ArrayList<String>();
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("teamManagers");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                Document manager = cursor.next();
                String teamName = manager.getString("CurrentTeam");
                if(teamName.equals("")){
                    String userName = manager.getString("UserName");
                    TeamMember teamManager = (TeamMember)this.getUser(userName);
                    AvailableManagers.add(teamManager.getUserName()+", "+teamManager.getFirstName()+", "+teamManager.getLastName());
                }
            }
            return AvailableManagers;
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    public ArrayList<String> getTeamManagers(String teamN){
        ArrayList<String> teamManagers = new ArrayList<String>();
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("teamManagers");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                Document manager = cursor.next();
                String teamName = manager.getString("CurrentTeam");
                if(teamName.equals(teamN)){
                    String userName = manager.getString("UserName");
                    teamManagers.add(userName);
                }
            }
            return teamManagers;
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    public ArrayList<String> getAllAssignUsersToAlerts(){
        ArrayList<String> assignUsersAlerts = new ArrayList<String>();
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("users");
            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext()) {
                Document user = cursor.next();
                Boolean isAssignAlerts = (Boolean) user.get("AssignToAlerts");
                if (isAssignAlerts) {
                    String userName = user.getString("UserName");
                    assignUsersAlerts.add(userName);
                }
            }
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
        return assignUsersAlerts;
    }

    public ArrayList<AlertPop> getAllUserAlerts(String userName){
        ArrayList<AlertPop> userAlerts = new ArrayList<AlertPop>();
        if(this.isUserExist(userName)) {
            try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
                MongoDatabase database = mongoClient.getDatabase("footballdb");
                MongoCollection<Document> collection = database.getCollection("usersAlerts");
                MongoCursor<Document> cursor = collection.find().iterator();
                while (cursor.hasNext()) {
                    Document user = cursor.next();
                    if (user.containsValue(userName)) {
                        Boolean isAlertViewed = (Boolean) user.get("isViewed");
                        if (!isAlertViewed) {
                            String alertType = user.getString("AlertType");
                            String alertContent = user.getString("AlertContent");
                            if (alertType.equals("TeamAlert")) {
                                TeamAlert alertTeam = new TeamAlert(alertContent);
                                userAlerts.add(alertTeam);
                                updateAlertDetails(userName,alertContent);//************
                            } else if (alertType.equals("MatchAlert")) {
                                MatchAlert alertMatch = new MatchAlert(alertContent);
                                userAlerts.add(alertMatch);
                                updateAlertDetails(userName,alertContent);
                            } else if (alertType.equals("BudgetAlert")) {
                                BudgetAlert alertBudget = new BudgetAlert(alertContent);
                                userAlerts.add(alertBudget);
                                updateAlertDetails(userName,alertContent);
                            }  else if(alertType.equals("TeamApprovalAlert")){
                                TeamApprovalAlert teamApprovalAlert=new TeamApprovalAlert(alertContent);
                                userAlerts.add(teamApprovalAlert);
                            } else if(alertType.equals("NominateRefereeAlert")){
                                NominateRefereeAlert nominateRefereeAlert=new NominateRefereeAlert(alertContent);
                                userAlerts.add(nominateRefereeAlert);
                            }
                        }
                    }
                }
            } catch (MongoException e) {
                try {
                    syserror.addErrorLog("Server","Connection with db Lost");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                throw new MongoException("Failed to connect the DB!");
            }
        }
        return userAlerts;
    }

    public boolean isExistMatchForLeagueInCurrentSeason(String leagueName) {
        int currentSeason = this.getTheCurrentSeason();
        if (this.isLeagueExistInSeason(currentSeason, leagueName)) {
            try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
                MongoDatabase database = mongoClient.getDatabase("footballdb");
                MongoCollection<Document> collection = database.getCollection("matchesDetails");
                MongoCursor<Document> cursor = collection.find().iterator();
                while (cursor.hasNext()) {
                    Document match = cursor.next();
                    if (match.containsValue(currentSeason) && match.containsValue(leagueName)) {
                        return true;
                    }
                }
                return false;
            }catch (MongoException e){
                try {
                    syserror.addErrorLog("Server","Connection with db Lost");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                throw new MongoException("Failed to connect the DB!");
            }
        }
        return false;
    }

    public ArrayList<String> getAllLeaguesToChangePolicy(){
        ArrayList<String> leaguesNames = new ArrayList<>();
        int currentSeason = this.getTheCurrentSeason();
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("seasonsAndLeaguesPolicy");
            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext()) {
                Document leagueInCurrentSeason = cursor.next();
                if (leagueInCurrentSeason.containsValue(currentSeason)) {
                    String leagueName = leagueInCurrentSeason.getString("LeagueName");
                    if (!this.isExistMatchForLeagueInCurrentSeason(leagueName)) {
                        leaguesNames.add(leagueName);
                    }
                }
            }
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
        return leaguesNames;
    }

    //    private Document getBudgetForCurrentSeason(){
//        Document seasonAndBudget = null;
//        int currentSeason = this.getTheCurrentSeason();
//        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
//            MongoDatabase database = mongoClient.getDatabase("footballdb");
//            MongoCollection<Document> usersCollection = database.getCollection("seasons");
//            MongoCursor<Document> cursor = usersCollection.find().iterator();
//            while (cursor.hasNext()) {
//                seasonAndBudget = cursor.next();
//                int seasonYearFound = (Integer) seasonAndBudget.get("SeasonYear");
//                if (seasonYearFound == currentSeason) {
//                    return seasonAndBudget;
//                }
//            }
//        }
    public boolean addMatchEvent(int currentSeason,String homeTeam, String awayTeam, String mainReferee,
                                 String minInMatch,String playerInvolved,String playerSub, String eventType) {
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("matchesEvents");
            Document eventGame = new Document("Season", currentSeason)
                    .append("HomeTeam", homeTeam)
                    .append("AwayTeam", awayTeam)
                    .append("MainReferee", mainReferee)
                    .append("MinuteInGame", minInMatch)
                    .append("PlayerInvolved", playerInvolved)
                    .append("PlayerSub", playerSub)
                    .append("EventType", eventType);
            collection.insertOne(eventGame);
            return true;
        } catch (MongoException e) {
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    public ArrayList<String> getAllMatchEvents(int currentSeason, String leagueName,String matchDate,
                                               String hostTeam, String awayTeam){
        ArrayList<String> matchEvents = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("matchesEvents");
            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext()) {
                Document matchEvent = cursor.next();
                if(matchEvent.containsValue(currentSeason) && matchEvent.containsValue(leagueName) &&
                        matchEvent.containsValue(matchDate) && matchEvent.containsValue(hostTeam) &&
                        matchEvent.containsValue(awayTeam)){
                    String eventType = matchEvent.getString("EventType");
                    String minInMatch = matchEvent.getString("MinInMatch");
                    String eventDetails = matchEvent.getString("EventDetails");
                    String playersInvolved = matchEvent.getString("PlayersInvolved");

                    String eventInMatch = "Event type - " + eventType + "," + "Minute in match- " +minInMatch +
                            "," + "Event details - " + eventDetails + "," + "Players involved" + playersInvolved;
                    matchEvents.add(eventInMatch);
                }
            }
        }
        return matchEvents;

    }




    @Override
    public boolean addTeamBudgetToSeason(String TeamName, double budgetTeam) {
        int currentSeason = this.getTheCurrentSeason();
        if(getTeamBudgetCurrentSeason(TeamName) != 0){
            return false;
        }
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("teamBudgetPerSeason");
            Document budget = new Document("Season", currentSeason)
                    .append("TeamName", TeamName)
                    .append("Budget", budgetTeam);
            collection.insertOne(budget);
            return true;
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    public ArrayList<Match> getAllRefereeMatches(String refereeUserName) {
        ArrayList<Match> refereeMatches = new ArrayList<>();
        int season = getTheCurrentSeason();
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("matchesDetails");
            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext()) {
                Document match = cursor.next();
                if(match.containsValue(season) && match.containsValue(refereeUserName)){
                    String date = match.getString("Date");
                    String homeTeam = match.getString("HomeTeam");
                    String awayTeam = match.getString("AwayTeam");
                    String courtName = match.getString("CourtName");
                    String mainReferee = match.getString("MainReferee");
                    String sideFirstRef = match.getString("SideFirstReferee");
                    String sideSecondRef = match.getString("SideSecondReferee");
                    String assistantRef = match.getString("AssistantReferee");
                    TeamInfo teamHome1 = this.getTeam(homeTeam);
                    TeamInfo teamAway2 = this.getTeam(awayTeam);
                    Court gameCourt = this.getCourt(courtName);
                    Referee mainRef = (Referee)this.getReferee(mainReferee);
                    Referee sideFirstReferee = getReferee(sideFirstRef);
                    Referee sideSecondReferee = getReferee(sideFirstRef);
                    Referee assistantReferee = getReferee(sideFirstRef);
                    Match footballMatch = new Match(teamHome1,teamAway2,gameCourt,date,mainRef);
                    refereeMatches.add(footballMatch);
                }
            }
        }
        return refereeMatches;
    }



    public double getTeamBudgetCurrentSeason(String teamName) {
        boolean isFound =false;
        int currentSeason = this.getTheCurrentSeason();
        double budget = 0;
        if(this.isTeamExist(teamName)) {
            try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
                MongoDatabase database = mongoClient.getDatabase("footballdb");
                MongoCollection<Document> collection = database.getCollection("teamBudgetPerSeason");
                MongoCursor<Document> cursor = collection.find().iterator();
                while (cursor.hasNext() && !isFound) {
                    Document courtCheck = cursor.next();
                    if (courtCheck.containsValue(currentSeason) && courtCheck.containsValue(teamName)) {
                        isFound = true;
                        budget = (Double) courtCheck.get("Budget");
                    }
                }
            }catch (MongoException e){
                try {
                    syserror.addErrorLog("Server","Connection with db Lost");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                throw new MongoException("Failed to connect the DB!");
            }
        }
        return budget;
    }

    @Override
    public ArrayList<String> getAllTeamReqs(String userName){
        ArrayList<String> userAlerts = new ArrayList<>();
        if(this.isUserExist(userName)) {
            try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
                MongoDatabase database = mongoClient.getDatabase("footballdb");
                MongoCollection<Document> collection = database.getCollection("usersAlerts");
                MongoCursor<Document> cursor = collection.find().iterator();
                while (cursor.hasNext()) {
                    Document user = cursor.next();
                    if (user.containsValue(userName)) {
                        Boolean isAlertViewed = (Boolean) user.get("isViewed");
                        if (!isAlertViewed) {
                            String alertType = user.getString("AlertType");
                            String alertContent = user.getString("AlertContent");
                            if (alertType.equals("TeamAlert")) {
                                TeamAlert alertTeam = new TeamAlert(alertContent);
                                userAlerts.add(alertContent);
                            }
                        }
                    }
                }
            } catch (MongoException e) {
                try {
                    syserror.addErrorLog("Server","Connection with db Lost");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                throw new MongoException("Failed to connect the DB!");
            }
        }
        return userAlerts;
    }

    @Override
    public ArrayList<String> getAllRefReqs(String userName) {
        ArrayList<String> userAlerts = new ArrayList<>();
        if(this.isUserExist(userName)) {
            try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
                MongoDatabase database = mongoClient.getDatabase("footballdb");
                MongoCollection<Document> collection = database.getCollection("usersAlerts");
                MongoCursor<Document> cursor = collection.find().iterator();
                while (cursor.hasNext()) {
                    Document user = cursor.next();
                    if (user.containsValue(userName)) {
                        Boolean isAlertViewed = (Boolean) user.get("isViewed");
                        if (!isAlertViewed) {
                            String alertType = user.getString("AlertType");
                            String alertContent = user.getString("AlertContent");
                            if (alertType.equals("NominateReferee")) {
                                TeamAlert alertTeam = new TeamAlert(alertContent);
                                userAlerts.add(alertContent);
                            }
                        }
                    }
                }
            } catch (MongoException e) {
                try {
                    syserror.addErrorLog("Server","Connection with db Lost");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                throw new MongoException("Failed to connect the DB!");
            }
        }
        return userAlerts;
    }
    public String getAssUserFromDB() {
        String name= "";
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> usersCollection = database.getCollection("users");
            MongoCursor<Document> cursor = usersCollection.find().iterator();
            while (cursor.hasNext()) {
                Document user = cursor.next();
                String occ = user.getString("Occupation");
                if (occ.equals("Association")) {
                    name = user.getString("UserName");
                }
            }
        }
        return name;
    }

    public HashMap<String,ArrayList<String>> getAll(){
        HashMap<String,ArrayList<String>> usersEmployed1 = new HashMap<String,ArrayList<String>>();
        HashMap<String,ArrayList<String>> usersEmployed2 = new HashMap<String,ArrayList<String>>();
        HashMap<String,ArrayList<String>> usersEmployed3 = new HashMap<String,ArrayList<String>>();
        HashMap<String,ArrayList<String>> usersEmployed4 = new HashMap<String,ArrayList<String>>();
        HashMap<String,ArrayList<String>> allUsers = new HashMap<String,ArrayList<String>>();
        usersEmployed1.putAll(getAllEmplyedPlayers());
        usersEmployed2.putAll(getAllEmplyedCoachs());
        usersEmployed3.putAll(getAllEmplyedOwners());
        usersEmployed4.putAll(getAllEmplyedTeamMangers());
        for (String term:usersEmployed1.keySet()) {
            if(!allUsers.containsKey(term)) {
                allUsers.put(term, new ArrayList<>());
            }
            allUsers.get(term).addAll(usersEmployed1.get(term));
        }
        for (String term:usersEmployed2.keySet()) {
            if(!allUsers.containsKey(term)) {
                allUsers.put(term, new ArrayList<>());
            }
            allUsers.get(term).addAll(usersEmployed2.get(term));
        }
        for (String term:usersEmployed3.keySet()) {
            if(!allUsers.containsKey(term)) {
                allUsers.put(term, new ArrayList<>());
            }
            allUsers.get(term).addAll(usersEmployed3.get(term));
        }
        for (String term:usersEmployed4.keySet()) {
            if(!allUsers.containsKey(term)) {
                allUsers.put(term, new ArrayList<>());
            }
            allUsers.get(term).addAll(usersEmployed4.get(term));
        }
        return allUsers;
    }


    public String getAllEmplyedBy(String userName) {
        String ans="";
        assetsRemoved=new ArrayList<String>();
        HashMap<String,ArrayList<String>> golb=getAll();
        ArrayList<String> nominated=golb.get(userName);
        getAllEmplyed(golb,nominated,userName);
        for (String user:assetsRemoved) {
            if(ans.equals("")){
                ans=user;
            }else {
                ans = ans + "," + user;
            }
        }
        return ans;
    }


    public void getAllEmplyed(HashMap<String,ArrayList<String>> allEmployedUsers, ArrayList<String> nominated, String userName ) {
        String ans="";
        assetsRemoved.add(userName);
        if(nominated!=null) {
            for (String user : nominated) {
                ArrayList<String> userNominte = new ArrayList<String>();
                if (allEmployedUsers.get(user) != null) {
                    userNominte.addAll(allEmployedUsers.get(user));
                    getAllEmplyed(allEmployedUsers, userNominte, user);
                }
                removeAllLastNominates(user);
                if(!assetsRemoved.contains(userName)) {
                    assetsRemoved.add(userName);
                }
                if(!assetsRemoved.contains(user)) {
                    assetsRemoved.add(user);
                }
            }
        }
        removeAllLastNominates(userName);
    }


//    private String resetNominated(String userName) {
//        String ans="";
//        //ArrayList<String > toRemove=new ArrayList<String>();
//        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
//            MongoDatabase database = mongoClient.getDatabase("footballdb");
//            MongoCollection<Document> collection1 = database.getCollection("teamManagers");
//            MongoCollection<Document> collection2 = database.getCollection("owners");
//            MongoCollection<Document> collection3 = database.getCollection("coaches");
//            MongoCollection<Document> collection4 = database.getCollection("players");
//             MongoCursor<Document> cursor1 = collection1.find().iterator();
//             MongoCursor<Document> cursor2 = collection2.find().iterator();
//             MongoCursor<Document> cursor3 = collection3.find().iterator();
//             MongoCursor<Document> cursor4 = collection4.find().iterator();
//            while (cursor1.hasNext()) {
//                Document user1 = cursor1.next();
//                String user = user1.getString("UserName");
//                if (user.equals(userName)) {
//            //ans = teammanager.getString("UserName");
//            //toRemove.add(teammanager.getString("UserName"));
//             //}
//            collection1.updateMany(
//                    Filters.eq("UserName", userName),
//                    Updates.combine(
//                            Updates.set("CurrentTeam", ""),
//                            Updates.set("EmployedBy", "")
//                    ));
//            }
//            while (cursor2.hasNext()) {
//                Document user2 = cursor2.next();
//                //String nominateBy = teammanager.getString("EmployedBy");
//                //if (nominateBy.equals(userName)) {
//                //ans = teammanager.getString("UserName");
//                //toRemove.add(teammanager.getString("UserName"));
//                //}
//                collection2.updateMany(
//                        Filters.eq("UserName", userName),
//                        Updates.combine(
//                                Updates.set("CurrentTeam", ""),
//                                Updates.set("EmployedBy", "")
//                        ));
//            }
//            while (cursor3.hasNext()) {
//                Document user3 = cursor3.next();
//                //String nominateBy = teammanager.getString("EmployedBy");
//                //if (nominateBy.equals(userName)) {
//                //ans = teammanager.getString("UserName");
//                //toRemove.add(teammanager.getString("UserName"));
//                //}
//                collection3.updateMany(
//                        Filters.eq("UserName", userName),
//                        Updates.combine(
//                                Updates.set("CurrentTeam", ""),
//                                Updates.set("EmployedBy", "")
//                        ));
//            }
//            while (cursor4.hasNext()) {
//                Document user4 = cursor4.next();
//                //String nominateBy = teammanager.getString("EmployedBy");
//                //if (nominateBy.equals(userName)) {
//                //ans = teammanager.getString("UserName");
//                //toRemove.add(teammanager.getString("UserName"));
//                //}
//                collection4.updateMany(
//                        Filters.eq("UserName", userName),
//                        Updates.combine(
//                                Updates.set("CurrentTeam", ""),
//                                Updates.set("EmployedBy", "")
//                        ));
//            }
//        } catch (MongoException e) {
//            try {
//                syserror.addErrorLog("Server","Connection with db Lost");
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//            throw new MongoException("Failed to connect the DB!");
//        }
//        return userName;
//    }


    public HashMap<String,ArrayList<String>> getAllEmplyedPlayers(){
        HashMap<String,ArrayList<String>> AvailablPlayer = new HashMap<String,ArrayList<String>>();
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("players");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                Document player = cursor.next();
                String nominateBy = player.getString("EmployedBy");
                if(!nominateBy.equals("")){
                    String userName = player.getString("UserName");
                    if (!AvailablPlayer.containsKey(nominateBy)) {
                        AvailablPlayer.put(nominateBy, new ArrayList<>());
                    }
                    AvailablPlayer.get(nominateBy).add(userName);
                }
            }
            return AvailablPlayer;
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    public HashMap<String,ArrayList<String>> getAllEmplyedCoachs(){
        HashMap<String,ArrayList<String>> Availablcoach = new HashMap<String,ArrayList<String>>();
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("coaches");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                Document player = cursor.next();
                String nominateBy = player.getString("EmployedBy");
                if(!nominateBy.equals("")){
                    String userName = player.getString("UserName");
                    if(!Availablcoach.containsKey(nominateBy)){
                        Availablcoach.put(nominateBy, new ArrayList<>());
                    }
                    Availablcoach.get(nominateBy).add(userName);
                }
            }
            return Availablcoach;
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    public HashMap<String,ArrayList<String>> getAllEmplyedOwners(){
        HashMap<String,ArrayList<String>> Availablowner = new HashMap<String,ArrayList<String>>();
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("owners");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                Document player = cursor.next();
                String nominateBy = player.getString("EmployedBy");
                if(!nominateBy.equals("")){
                    String userName = player.getString("UserName");
                    if(!Availablowner.containsKey(nominateBy)){
                        Availablowner.put(nominateBy, new ArrayList<>());
                    }
                    Availablowner.get(nominateBy).add(userName);
                }
            }
            return Availablowner;
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    public HashMap<String,ArrayList<String>> getAllEmplyedTeamMangers(){
        HashMap<String,ArrayList<String>> AvailablteamMangers = new HashMap<String,ArrayList<String>>();
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("teamManagers");
            MongoCursor<Document> cursor = collection.find().iterator();
            while(cursor.hasNext()){
                Document player = cursor.next();
                String nominateBy = player.getString("EmployedBy");
                if(!nominateBy.equals("")){
                    String userName = player.getString("UserName");
                    if(!AvailablteamMangers.containsKey(nominateBy)){
                        AvailablteamMangers.put(nominateBy, new ArrayList<>());
                    }
                    AvailablteamMangers.get(nominateBy).add(userName);
                }
            }
            return AvailablteamMangers;
        }catch (MongoException e){
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    public void Subscribe(String username){
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("users");
            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext()) {
                Document player = cursor.next();
                String user = player.getString("UserName");
                if (user.equals(username)) {
                    collection.updateMany(
                            Filters.eq("UserName", username),
                            Updates.combine(
                                    Updates.set("AssignToAlerts", true)
                            ));
                }
            }
        } catch (MongoException e) {
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    public void Unsubscribe(String username){
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("users");
            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext()) {
                Document player = cursor.next();
                String user = player.getString("UserName");
                if (user.equals(username)) {
                    collection.updateMany(
                            Filters.eq("UserName", username),
                            Updates.combine(
                                    Updates.set("AssignToAlerts", false)
                            ));
                }
            }
        } catch (MongoException e) {
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
    }

    public void removeAllLastNominates(String userName){
        resetAllPlayerNominated(userName);
        resetAllCoachesNominated(userName);
        resetAllOwnersNominated(userName);
        resetAllTeamManagersNominated(userName);
    }

    private String resetAllTeamManagersNominated(String userName) {
        String ans="";
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("teamManagers");
            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext()) {
                Document teammanager = cursor.next();
                String nominateBy = teammanager.getString("EmployedBy");
                if (nominateBy.equals(userName)) {
                    //ans = teammanager.getString("UserName");
                }
                collection.updateMany(
                        Filters.eq("UserName", userName),
                        Updates.combine(
                                Updates.set("CurrentTeam", ""),
                                Updates.set("EmployedBy", ""),
                                Updates.set("CoachPermission", false),
                                Updates.set("PlayerPermission", false),
                                Updates.set("TeamManagerPermission", false),
                                Updates.set("OwnerPermission", false)
                        ));
            }
        } catch (MongoException e) {
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
        return userName;
    }

    private String resetAllOwnersNominated(String userName) {
        String ans="";
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("owners");
            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext()) {
                Document owner = cursor.next();
                String nominateBy = owner.getString("EmployedBy");
                if (nominateBy.equals(userName)) {
                    //ans = ans +", "+ owner.getString("UserName");
                }
                collection.updateMany(
                        Filters.eq("UserName", userName),
                        Updates.combine(
                                Updates.set("CurrentTeam", ""),
                                Updates.set("EmployedBy", ""),
                                Updates.set("CoachPermission", false),
                                Updates.set("PlayerPermission", false),
                                Updates.set("TeamManagerPermission", false),
                                Updates.set("OwnerPermission", false)
                        ));
            }
        } catch (MongoException e) {
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
        return userName;
    }

    private String resetAllCoachesNominated(String userName) {
        String ans="";
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("coaches");
            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext()) {
                Document coach = cursor.next();
                String nominateBy = coach.getString("EmployedBy");
                if (nominateBy.equals(userName)) {
                    ans = ans + ", " + coach.getString("UserName");

                }
                collection.updateMany(
                        Filters.eq("UserName", userName),
                        Updates.combine(
                                Updates.set("CurrentTeam", ""),
                                Updates.set("EmployedBy", "")
                        ));
            }
        } catch (MongoException e) {
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
        return userName;
    }

    private String resetAllPlayerNominated(String userName) {
        try (MongoClient mongoClient = MongoClients.create(mongoClientURI)) {
            MongoDatabase database = mongoClient.getDatabase("footballdb");
            MongoCollection<Document> collection = database.getCollection("players");
            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext()) {
                Document player = cursor.next();
                String user = player.getString("UserName");
                if (user.equals(userName)) {
                    collection.updateMany(
                            Filters.eq("UserName", userName),
                            Updates.combine(
                                    Updates.set("CurrentTeam", ""),
                                    Updates.set("EmployedBy", "")
                            ));
                }
            }
        } catch (MongoException e) {
            try {
                syserror.addErrorLog("Server","Connection with db Lost");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new MongoException("Failed to connect the DB!");
        }
        return userName;
    }

}//end class



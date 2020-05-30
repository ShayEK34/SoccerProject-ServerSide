package Data.SystemDB;

import Domain.AssociationManagement.League;
import Domain.AssociationManagement.Season;
import Domain.ClubManagement.Court;
import Domain.ClubManagement.TeamInfo;
import Domain.User.Fan;

import java.util.ArrayList;

public interface DataBaseInterface {

    boolean addUser(String firstName,String lastName,String userName,String password,String occupation,String birthday,String email,String verificationCode,String role);
    boolean isUserExist(String userName);
    boolean removeUser(String userName);
    Fan getUser(String givenUserName);


    boolean addTeam(String teamName, int establishYear, boolean teamActiveStatus, String city, String firstOwner,String homeCourt);
    boolean isTeamExist(String giveTeamName);
    boolean removeTeam(String team);
    TeamInfo getTeam(String givenTeamName);

    boolean addCourt(String courtName, String courtCityLocation,int courtCapacity);
    boolean isCourtExist(String givenCourtName);
    boolean removeCourt(String givenCourtName);
    Court getCourt(String givenCourtName);

    boolean addSeason(int seasonYear, double seasonBudget);
    boolean isSeasonExist(int seasonYear);
    boolean deleteSeason(int season);//not implemented yet
    Season getSeasonByYear(int Season);

    boolean addLeagueAndPolicyToSeason(int seasonYear,String leagueName,String date, int pointsPerWin,int pointsPerLoss,int pointsPerDraw,
                                       boolean differenceGoals,boolean straightMeets, int roundsNum, int numOfTeams);
    boolean isLeagueExistInSeason(int seasonYear,String leagueName);
    boolean deleteLeague(String league);//not implemented yet
    League getLeagueByName(String leagueName);

    boolean addTeamToLeague(String teamName,int seasonYear,String leagueName);

    boolean addGameDetails(int seasonYear, String leagueName, String date, String homeTeam, String awayTeam,
                           String courtName, String mainReferee, String sideFirstRef, String sideSecRef,
                           String assistantRef);

    //boolean addMatchEvent(String leagueName,String date, String homeTeam, String awayTeam, String minuteGame, String event, String playersInvolved);

    boolean updateUserDetails(String userName,Object newValue, String tableName, String tableCol);

    boolean addUserAlert(String userNameDestination, String alertType,String alertContent, boolean isWatched);

    void resetDB();

    boolean addTeamBudgetToSeason(String TeamName, double budgetTeam);

    ArrayList<String> getAllTeamReqs(String userName);

    ArrayList<String> getAllRefReqs(String userName);
}

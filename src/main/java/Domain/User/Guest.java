package Domain.User;

import Data.SystemDB.DB;
import Domain.AssociationManagement.League;
import Domain.AssociationManagement.Season;
import Domain.ClubManagement.TeamInfo;

import java.util.ArrayList;
import java.util.HashMap;

import static Domain.User.SystemManager.*;

/**
 * This class represent a guest in the system.
 */
public class Guest {

    /**
     * constructor
     */
    public Guest() {
    }

    /**
     * This function allow for guest to sign up to the system.
     * @param firstName
     * @param lastName
     * @param userName
     * @param occupation
     * @param birthday
     * @param email
     * @param password
     * @param verificationCode
     */
    public void SignUp(String firstName, String lastName, String userName, String occupation, String birthday, String email, String password,String verificationCode,String role){
        addNewUser(firstName,lastName,userName,password,occupation,birthday,email, verificationCode,role);
    }

    /**
     * This function allow for guest to login to the system.
     * @param pass
     * @param userName
     * @return
     */
    public Fan Login(String pass, String userName){
        if(pass==null || userName==null){
            throw new IllegalArgumentException("One of the details is NULL");
        }
        Fan user=checkUser(pass, userName);
        user.setStatus(Fan.Status.online);
        return user;
    }

//    /**
//     * This function allow for guest to search user in the system.
//     * @param userName
//     * @return
//     */
//    public  Fan Search(String userName, String category){
//        if(userName==null){
//            throw new IllegalArgumentException("userName is NULL");
//        }
//        return search(userName);
//    }

    public ArrayList<Object> SearchByName(String text){
        ArrayList<Object> ans= new ArrayList<Object>();
        //users
        HashMap<String,Fan> allUsers=DB.getInstance().getUsers();
        for (String user: allUsers.keySet()) {
            if(user.equals(text) ||allUsers.get(user).getFirstName().equals(text) ||allUsers.get(user).getLastName().equals(text)){
                ans.add(allUsers.get(user));
            }
        }
        //team
        HashMap<String, TeamInfo> allTeams=DB.getInstance().getAllTeams();
        for (String team: allTeams.keySet()) {
            if(allTeams.get(team).getTeamName().equals(text)){
                ans.add(allTeams.get(team));
            }
        }
        //leagues
        HashMap<String, League> allLeagues=DB.getInstance().getLeagues();
        for (String league: allLeagues.keySet()) {
            if(allLeagues.get(league).getLeagueName().equals(text)){
                ans.add(allLeagues.get(league));
            }
        }
        //seasons
        HashMap<Integer, Season> allSeasons=DB.getInstance().getSeasons();
        for (Integer season: allSeasons.keySet()) {
            if(allSeasons.get(season).getYear()== Integer.parseInt(text)){
                ans.add(allSeasons.get(season));
            }
        }
        return ans;
    }

    public ArrayList<Object> SearchByCategory(String category){
        ArrayList<Object> ans= new ArrayList<Object>();
        //users
        if(category.equals("User")){
            HashMap<String,Fan> allUsers=DB.getInstance().getUsers();
            for (String user: allUsers.keySet()) {
                if(user.equals(category) ||allUsers.get(user).getFirstName().equals(category) ||allUsers.get(user).getLastName().equals(category)){
                    ans.add(allUsers.get(user));
                }
            }
        }

        //team
        else  if(category.equals("Team")) {
            HashMap<String, TeamInfo> allTeams = DB.getInstance().getAllTeams();
            for (String team : allTeams.keySet()) {
                if (allTeams.get(team).getTeamName().equals(category)) {
                    ans.add(allTeams.get(team));
                }
            }
        }
        //leagues
        else  if(category.equals("Laegue")) {
            HashMap<String, League> allLeagues = DB.getInstance().getLeagues();
            for (String league : allLeagues.keySet()) {
                if (allLeagues.get(league).getLeagueName().equals(category)) {
                    ans.add(allLeagues.get(league));
                }
            }
        }
        //seasons
        else  if(category.equals("Season")) {
            HashMap<Integer, Season> allSeasons = DB.getInstance().getSeasons();
            for (Integer season : allSeasons.keySet()) {
                if (allSeasons.get(season).getYear() == Integer.parseInt(category)) {
                    ans.add(allSeasons.get(season));
                }
            }
        }
        return ans;
    }


}


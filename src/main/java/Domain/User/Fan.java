package Domain.User;

import Domain.AssociationManagement.League;
import Domain.AssociationManagement.Season;
import Domain.ClubManagement.TeamInfo;
import Domain.InformationPage.InformationPage;
import Data.SystemDB.DB;
import Domain.Systems.Complain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static Domain.User.SystemManager.addUserComplain;


public class Fan extends Guest {

    public enum Status{online,offline};
    private String firstName;
    private String lastName ;
    private String userName;
    private String password;
    private String occupation;
    private String birthday;
    private String email;
    private Status status;
    private boolean assignToAlerts;

    private ArrayList<InformationPage> followPages;
    private HashMap<String, String> fanComplain;
    private ArrayList<String> historyOfSearch;

    /**
     * constructor
     * @param firstName
     * @param lastName
     * @param userName
     * @param password
     * @param occupation
     * @param birthday
     * @param email
     */
    public Fan( String firstName, String lastName, String userName, String password, String occupation, String birthday, String email) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.occupation = occupation;
        this.birthday = birthday;
        this.email = email;
        this.status=Status.offline;
        this.followPages=new ArrayList<InformationPage>();
        this.fanComplain=new HashMap<String, String>();
        this.historyOfSearch=new ArrayList<String>();
        this.assignToAlerts = false;
    }

    /**
     * This function allow for user to log out from the system.
     */
    public void Logout(Fan fan){
        fan.setStatus(Status.offline);
    }

    /**
     * This function allow for user fallow up a Information page.
     * @param page
     */
    public void follow(InformationPage page){
        if(page!=null){
            this.followPages.add(page);
        }
    }

    public boolean isAssignToAlerts() {
        return assignToAlerts;
    }

    public void setAssignToAlerts(boolean assignToAlerts) {
        this.assignToAlerts = assignToAlerts;
    }

    /**
     * Set Status.
     * @param status
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Get First Name.
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Get Last Name.
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * get Service.User Name.
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Get Password.
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get Occupation.
     * @return
     */
    public String getOccupation() {
        return occupation;
    }

    /**
     * Get Birthday.
     * @return
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * Get Email.
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set Status.
     * @return
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Set First name.
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Set Last name.
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Set Password.
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Set Occupation.
     * @param occupation
     */
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    /**
     * Set Birthday.
     * @param birthday
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     * Set Birthday.
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * This function allow for user to edit his details.
     * @param firstName
     * @param lastName
     * @param password
     * @param occupation
     * @param birthday
     * @param email
     */
    public void editInfo(String firstName, String lastName, String password, String occupation, String birthday, String email){
        if(firstName!=null&&lastName!=null&&password!=null&&birthday!=null&&email!=null) {
            this.setFirstName(firstName);
            this.setLastName(lastName);
            this.setPassword(password);
            this.setOccupation(occupation);
            this.setBirthday(birthday);
            this.setEmail(email);
        }
    }


    /**
     * The function return all the Information pages that the user follow.
     * @return
     */
    public ArrayList<InformationPage> getFollowPages() {
        return followPages;
    }

    /**
     * This function report complain to the system manager.
     * @param complain
     */
    public void reportIncorrectInfo(String complain){
        addUserComplain(complain, this.getUserName());
        this.fanComplain.put(complain,"no answer");
    }

    /**
     * The function change the answer of complain.
     * @param complain
     */
    public void setComplainAnswer(Complain complain){
        this.fanComplain.remove(complain.getComplain());
        this.fanComplain.put(complain.getComplain(),complain.getAnswer());
    }

    /**
     * The function show to user all his complain and their answers.
     * @return
     */
    public String showUserComplains(){
        StringBuilder complains=new StringBuilder();
        complains.append("Your Complains:"+"\n\n");
        for (String comp:this.fanComplain.keySet()) {
            complains.append("Complain: "+comp+"\n"+"Answer: "+this.fanComplain.get(comp)+"\n"+"\n");
        }
        return String.valueOf(complains);
    }

    @Override
    public ArrayList<Object> SearchByName(String text) {
        Date today=new Date();
        String line=today+" - Search by the name: "+text+".";
        this.historyOfSearch.add(line);
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

    @Override
    public ArrayList<Object> SearchByCategory(String category) {
        Date today=new Date();
        String line=today+" - Search by the category: "+category+".";
        this.historyOfSearch.add(line);
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

    public HashMap<String, String> getFanComplain() {
        return fanComplain;
    }

    public ArrayList<String> getHistoryOfSearch() {
        return historyOfSearch;
    }

    @Override
    public String toString() {
        return "Fan{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", occupation='" + occupation + '\'' +
                ", birthday='" + birthday + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                ", assignToAlerts=" + assignToAlerts +
                ", followPages=" + followPages +
                ", fanComplain=" + fanComplain +
                ", historyOfSearch=" + historyOfSearch +
                '}';
    }
}

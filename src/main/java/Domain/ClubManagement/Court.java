package Domain.ClubManagement;
import java.util.ArrayList; // import just the ArrayList class

/**
 * This class represent a court/ stadium for team that this court is their home court.
 */
public class Court {

    private String courtName;
    private String courtCityLocation;
    private int courtCapacity;
    private ArrayList <TeamInfo> teams;

    /**
     * This constructor get a court name, city that court is located and capacity number of seats and
     * create new court stadium that.
     * @param courtName - The given court name.
     * @param courtCityLocation - The given city that court located.
     * @param courtCapacity - The given capacity of the stadium.
     */
    public Court(String courtName, String courtCityLocation,int courtCapacity){
        if(courtName ==null || courtCityLocation==null){
            throw new IllegalArgumentException("Parameters can't be null");
        }
        if(courtCapacity <= 0){
            throw new IllegalArgumentException("court capacity can't be negative!");
        }
        this.courtName = courtName;
        this.courtCityLocation = courtCityLocation;
        this.courtCapacity = courtCapacity;
        this.teams = new ArrayList<TeamInfo>();
    }

    /**
     * This function add team to the list of team that this court is their home court.
     * @param team - The given team to add.
     */
    public boolean addTeamToCourtList(TeamInfo team){
        if(team == null){
            throw new IllegalArgumentException("Parameters can't be null");
        }
        else {
            if (!(this.checkTeamSignCourt(team))) {
                if(checkIfTheLocationTeam(team)) {
                    this.teams.add(team);
                    System.out.println("The team: " + team.getTeamName() + " added to this court teams list");
                    return true;
                }
                else{
                    System.out.println("The team and the court not from the same city");
                    return false;
                }
            }
            else {
                System.out.println("The team already in the court teams list");
                return false;
            }
        }
    }

    /**
     * This function return the number of teams that this court it's their home court.
     * @return - THe number of teams that this court it's their home court.
     */
    public int courtNumTeams(){
        return this.teams.size();
    }

    /**
     * This function get a name for the court and set it.
     * @param courtName - The given court name.
     */
    public void setCourtName(String courtName) {
        if (courtName == null){
            throw new IllegalArgumentException("court capacity can't be negative!");
        }
        else {
            this.courtName = courtName;
        }
    }

    /**
     * This function return the name of the court/ stadium.
     * @return - The name of the court/ stadium.
     */
    public String getCourtName() {
        return courtName;
    }

    /**
     * This function return the name of the city that the court is located.
     * @return - The name of the city that the court is located.
     */
    public String getCourtCityLocation() {
        return courtCityLocation;
    }

    /**
     * This function return the capacity number of the court/ stadium.
     * @return - The capacity number of the court/ stadium.
     */
    public int getCourtCapacity() {
        return courtCapacity;
    }

    /**
     * This function return array list of the teams that this court it's their home court.
     * @return - Array list of the teams that this court it's their home court.
     */
    public ArrayList<TeamInfo> getTeams() {
        return teams;
    }

    public void setCourtCityLocation(String courtCityLocation) {
        if (courtCityLocation == null){
            throw new IllegalArgumentException("court capacity can't be negative!");
        }
        else {
            this.courtCityLocation = courtCityLocation;
        }
    }


    /**
     * This function get a new capacity of court check it and set it.
     * @param courtCapacity - The given new capacity
     */
    public void setCourtCapacity(int courtCapacity) {
        if (courtCapacity <= 0){
            throw new IllegalArgumentException("court capacity can't be negative!");
        }
        else {
            this.courtCapacity = courtCapacity;
        }
    }

    /**
     * This function check if team already in the team list of the court.
     * @param team - The given court
     * @return - true if the team already in the list else false.
     */
    public boolean checkTeamSignCourt(TeamInfo team){
        for (int i = 0; i < this.courtNumTeams(); i++){
            if (this.getTeams().get(i).getTeamName().equals(team.getTeamName())){
                return true;
            }
        }
        return false;
    }

    /**
     * This function check if the city of the team and the city of the court are the same.
     * @param team - The given team.
     * @return - true if the cities the same else false/
     */
    public boolean checkIfTheLocationTeam(TeamInfo team){
        if(this.getCourtCityLocation().equals(team.getCityRepresent())){
            return true;
        }
        return false;
    }

}


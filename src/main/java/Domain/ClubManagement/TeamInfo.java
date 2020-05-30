package Domain.ClubManagement;
import Domain.BudgetControl.Budget;
import Domain.InformationPage.TeamInformationPage;
import Domain.User.*;
import java.util.ArrayList; // import just the ArrayList class....

/**
 * This class represent a football team.
 */

public class TeamInfo {

    private String teamName;
    private String cityRepresent;
    private int establishYear;
    private boolean teamActiveStatus;
    private OwnerInterface teamOwner;
    private ArrayList<OwnerInterface> teamOwners;
    private ArrayList<CoachInterface> teamCoaches;
    private ArrayList<PlayerInterface> teamPlayers;
    private ArrayList<TeamManagerInterface> teamManagers;
    private Court teamHomeCourt;
    private int winCount;
    private int drawCount;
    private int defeatCount;
    private int scoreGoalsCount;
    private int receivedGoalsAmount;
    private int points;
    private int matchesPlayed;
    private Budget teamSeasonBudget;
    private ArrayList<Court> teamCourts;
    private TeamInformationPage teamPage;

    /**
     * This constructor get team name, establish year, status of the team, court for the team and owner and
     * create new team.
     *
     * @param teamName         - The given name for the team.
     * @param establishYear    - The given establish year of the team.
     * @param teamActiveStatus - The given active status of the team.
     * @param firstOwner       - The first and main owner of the team.
     */
    public TeamInfo(String teamName, int establishYear, boolean teamActiveStatus, String city, TeamMember firstOwner) {
        this.teamName = teamName;
        this.cityRepresent = city;
        this.establishYear = establishYear;
        this.teamActiveStatus = teamActiveStatus;
        this.teamOwners = new ArrayList<OwnerInterface>();
        this.teamCoaches = new ArrayList<CoachInterface>();
        this.teamPlayers = new ArrayList<PlayerInterface>();
        this.teamManagers = new ArrayList<TeamManagerInterface>();
        this.teamCourts = new ArrayList<Court>();
        this.teamHomeCourt = null;
        this.teamOwner = firstOwner;
        this.winCount = 0;
        this.drawCount = 0;
        this.defeatCount = 0;
        this.scoreGoalsCount = 0;
        this.receivedGoalsAmount = 0;
        this.points = 0;
        this.matchesPlayed = 0;
        this.addOwner(firstOwner);
        this.teamSeasonBudget = new Budget(0, null);
        this.teamPage = new TeamInformationPage(this);

    }

    public TeamInfo(String teamName, int establishYear, boolean teamActiveStatus, String city) {
        this.teamName = teamName;
        this.cityRepresent = city;
        this.establishYear = establishYear;
        this.teamActiveStatus = teamActiveStatus;
        this.teamOwners = new ArrayList<OwnerInterface>();
        this.teamCoaches = new ArrayList<CoachInterface>();
        this.teamPlayers = new ArrayList<PlayerInterface>();
        this.teamManagers = new ArrayList<TeamManagerInterface>();
        this.teamCourts = new ArrayList<Court>();
        this.teamHomeCourt = null;
        this.winCount = 0;
        this.drawCount = 0;
        this.defeatCount = 0;
        this.scoreGoalsCount = 0;
        this.receivedGoalsAmount = 0;
        this.points = 0;
        this.matchesPlayed = 0;
        this.teamSeasonBudget = new Budget(0, null);
        this.teamPage = new TeamInformationPage(this);

    }

    public void setTeamOwner(OwnerInterface teamOwner) {
        this.teamOwner = teamOwner;
    }

    /**
     * This function get owner and add him to the list owners of the team.
     *
     * @param owner - The given owner.
     */
    public boolean addOwner(TeamMember owner) {
        if (owner == null) {
            throw new IllegalArgumentException("Parameters can't be null");
        }
        if (!checkIfOwnerExistIn(owner)) {
            this.teamOwners.add(owner);
            System.out.println("Owner " + owner.getFirstName() + " added to the team " + this.getTeamName() + " !");
            return true;
        } else {
            System.out.println("The Owner " + owner.getFirstName() + "is already in the team");
            return false;
        }
    }

    /**
     * This function get player and add him to the list players of the team.
     *
     * @param player - The given player.
     */

    public boolean addPlayer(TeamMember player) {
        if (player == null) {
            throw new IllegalArgumentException("Parameters can't be null");
        }
        if (!checkIfPlayerExistIn(player)) {
            this.teamPlayers.add(player);
            System.out.println("Player " + player.getFirstName() + " added to the team " + this.getTeamName() + " !");
            return true;
        } else {
            System.out.println("The Player " + player.getFirstName() + "is already in the team");
            return false;
        }
    }

    /**
     * This function get coach and add him to the list coaches of the team.
     *
     * @param coach - The given coach.
     */

    public boolean addCoach(TeamMember coach) {
        if (coach == null ) {
            throw new IllegalArgumentException("Parameters can't be null");
        }
        if (!checkIfCoachExistIn(coach)) {
            this.teamCoaches.add(coach);
            System.out.println("Coach " + coach.getFirstName() + " added to the team " + this.getTeamName() + " !");
            return true;
        } else {
            System.out.println("The coach " + coach.getFirstName() + "is already in the team");
            return false;
        }
    }

    /**
     * This function get manager and add him to the list managers of the team.
     *
     * @param manager - The given manager.
     */

    public boolean addManager(TeamMember manager) {
        if (manager == null) {
            throw new IllegalArgumentException("Parameters can't be null");
        }
        if (!checkIfManagerExistIn(manager)) {
            this.teamManagers.add(manager);
            System.out.println("Manager " + manager.getFirstName() + " added to the team " + this.getTeamName() + " !");
            return true;
        } else {
            System.out.println("The manager " + manager.getFirstName() + "is already in the team");
            return false;
        }
    }

    /**
     * This function get manager and add him to the list managers of the team.
     *
     * @param court - The given manager.
     */
    public boolean addCourt(Court court) {
        if (court == null) {
            throw new IllegalArgumentException("Parameters can't be null");
        } else {
            if (!checkIfCourtExistIn(court)) {
                this.teamCourts.add(court);
                System.out.println("Court " + court.getCourtName() + " added to the team courts " + this.getTeamName() + " !");
                if (this.teamHomeCourt == null) {
                    this.teamHomeCourt = court;
                    System.out.println("Court " + court.getCourtName() + " is home court of: " + this.getTeamName() + " !");
                }
                return true;
            }
            System.out.println("The Court " + court.getCourtName() + "is already in the team list courts");
            return false;
        }
    }

    /**
     * This function remove owner from the owner list of the team
     * @param owner - The given owner
     * @return - true if the owner remove
     */
    public boolean removeOwner(TeamMember owner) {
        if (owner == null) {
            throw new IllegalArgumentException("Parameters can't be null");
        } else {
            if (checkIfOwnerExistIn(owner)) {
                if(this.teamOwners.size()>1) {
                    if(((TeamMember)this.teamOwner).getUserName().equals(owner.getUserName())){
                        transferOwnerShip(owner);
                    }
                    this.teamOwners.remove(owner);
                    System.out.println("Owner remove from team: " + this.getTeamName() + " !");
                    return true;
                }
                else{
                    System.out.println("Team must have a least one owner");
                    return false;
                }
            } else {
                System.out.println("Owner not in owners team list.");
                return false;
            }
        }
    }

    public boolean removeOwnerFromTeam(String owner) {
        if (owner == null) {
            throw new IllegalArgumentException("Parameters can't be null");
        } else {

            for(int i=0; i<teamOwners.size(); i++){
                if(((TeamMember)teamOwners.get(i)).getUserName().equals(owner)){
                    this.teamOwners.remove(i);
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * This function remove coach from the coaches list of the team
     * @param coach - The given coach
     * @return - true if the coach remove
     */
    public boolean removeCoach(TeamMember coach) {
        if (coach == null) {
            throw new IllegalArgumentException("Parameters can't be null");
        } else {
            if (checkIfCoachExistIn(coach)) {
                this.teamCoaches.remove(coach);
                System.out.println("Coach remove from team: " + this.getTeamName() + " !");
                return true;
            } else {
                System.out.println("Coach not in coaches team list.");
                return false;
            }
        }
    }

    public boolean removeCoachFromTeam(String coach) {
        if (coach == null) {
            throw new IllegalArgumentException("Parameters can't be null");
        } else {
            for(int i=0; i<teamCoaches.size(); i++){
                if(((TeamMember)teamCoaches.get(i)).getUserName().equals(coach)){
                    this.teamCoaches.remove(i);
                    System.out.println("Coach remove from team: " + this.getTeamName() + " !");
                    return true;
                }
            }
            return false;
        }
    }


    /**
     * This function remove player from the players list of the team
     * @param player -The given player
     * @return - true if the player remove
     */
    public boolean removePlayer(TeamMember player) {
        if (player == null) {
            throw new IllegalArgumentException("Parameters can't be null");
        } else {
            if (checkIfPlayerExistIn(player)) {
                this.teamPlayers.remove(player);

                System.out.println("Player remove from team: " + this.getTeamName() + " !");
                return true;
            } else {
                System.out.println("player not in players team list.");
                return false;
            }
        }
    }

    public boolean removePlayerFromTeam(String player) {
        if (player == null) {
            throw new IllegalArgumentException("Parameters can't be null");
        } else {
            for (int i = 0; i < teamPlayers.size(); i++) {
                if (((TeamMember) teamPlayers.get(i)).getUserName().equals(player)) {
                    this.teamPlayers.remove(i);
                    System.out.println("Player remove from team: " + this.getTeamName() + " !");
                    return true;
                }
            }
            return false;
        }
    }



    /**
     * This function remove manager from the managers list of the team
     * @param manager - The given manager
     * @return - true if the manager remove
     */
    public boolean removeManager(TeamMember manager) {
        if (manager == null) {
            throw new IllegalArgumentException("Parameters can't be null");
        } else {
            if (checkIfManagerExistIn(manager)) {
                this.teamManagers.remove(manager);
                System.out.println("Manager remove from team: " + this.getTeamName() + " !");
                return true;
            } else {
                System.out.println("player not in players team list.");
                return false;
            }
        }
    }

    public boolean removeManagerFromTeam(String manager) {
        if (manager == null) {
            throw new IllegalArgumentException("Parameters can't be null");
        } else {
            for(int i=0; i<teamManagers.size(); i++){
                if(((TeamMember)teamManagers.get(i)).getUserName().equals(manager)){
                    this.teamManagers.remove(i);
                }
            }

            System.out.println("Player remove from team: " + this.getTeamName() + " !");
            return true;
        }
    }

    /**
     * This function remove court from the courts list of the team
     * @param court - The given court
     * @return - true if the manager remove
     */
    public boolean removeCourt(Court court) {
        if (court == null) {
            throw new IllegalArgumentException("Parameters can't be null");
        } else {
            if (checkIfCourtExistIn(court)) {
                this.teamCourts.remove(court);
                System.out.println("Court remove from team: " + this.getTeamName() + " !");
                return true;
            }
            else{
                System.out.println("Court not in court team list.");
                return false;
            }
        }
    }

    /**
     * This function return the number of the team owners.
     *
     * @return - The number of the team owners.
     */
    public int getNumOfOwnersInTeam() {
        return this.teamOwners.size();
    }

    /**
     * This function return the number of the team players.
     *
     * @return - The number of the team players.
     */
    public int getNumOfPlayersInTeam() {
        return this.teamPlayers.size();
    }

    /**
     * This function return the number of the team coaches.
     *
     * @return - The number of the team coaches.
     */
    public int getNumOfCoachesInTeam() {
        return this.teamCoaches.size();
    }

    /**
     * This function return the number of the team managers.
     *
     * @return - The number of the team managers.
     */
    public int getNumOfManagersInTeam() {
        return this.teamManagers.size();
    }

    /**
     * This function return the team name.
     *
     * @return - The team name.
     */
    public String getTeamName() {
        return teamName;
    }

    /**
     * This function return the team establish year.
     *
     * @return - The team establish year.
     */
    public int getEstablishYear() {
        return establishYear;
    }

    /**
     * This function return the team active status.
     *
     * @return - The team active status.
     */
    public boolean isTeamActiveStatus() {
        return teamActiveStatus;
    }

    /**
     * This function return the team home court.
     *
     * @return- The team home court.
     */
    public Court getTeamCourt() {
        return teamHomeCourt;
    }

    /**
     * This function return the team first owner.
     *
     * @return- The team first owner.
     */
    public OwnerInterface getTeamOwner() {
        return teamOwner;
    }

    /**
     * This function return the team list owners.
     *
     * @return - The team list owners.
     */
    public ArrayList<OwnerInterface> getTeamOwners() {
        return teamOwners;
    }

    /**
     * This function return the team list coaches.
     *
     * @return - The team list coaches.
     */
    public ArrayList<CoachInterface> getTeamCoaches() {
        return teamCoaches;
    }

    /**
     * This function return the team list players.
     *
     * @return - The team list players.
     */
    public ArrayList<PlayerInterface> getTeamPlayers() {
        return teamPlayers;
    }

    /**
     * This function return the team list managers.
     *
     * @return - The team list managers.
     */
    public ArrayList<TeamManagerInterface> getTeamManagers() {
        return teamManagers;
    }

    /**
     * This function return the wins number of the team.
     *
     * @return - The wins number of the team.
     */
    public int getWinCount() {
        return winCount;
    }

    /**
     * This function get wins number of the team and set it.
     *
     * @param winCount - The given wins number.
     */
    public void setWinCount(int winCount) {
        this.winCount = winCount;
    }

    /**
     * This function return the draw number of the team.
     *
     * @return - The draw number of the team.
     */
    public int getDrawCount() {
        return drawCount;
    }

    /**
     * This function get draw number of the team and set it.
     *
     * @param drawCount - The given draw number.
     */
    public void setDrawCount(int drawCount) {
        this.drawCount = drawCount;
    }

    /**
     * This function return the defeat number of the team.
     *
     * @return - The defeat number of the team.
     */
    public int getDefeatCount() {
        return defeatCount;
    }

    /**
     * This function get defeat number of the team and set it.
     *
     * @param defeatCount - The given defeat number.
     */
    public void setDefeatCount(int defeatCount) {
        this.defeatCount = defeatCount;
    }

    /**
     * This function return the goals number of the team score.
     *
     * @return - The goals number of the team.
     */
    public int getScoreGoalsCount() {
        return scoreGoalsCount;
    }

    /**
     * This function get a goal count number that the team score and set it.
     *
     * @param scoreGoalsCount - The given goal count number that the team score.
     */
    public void setScoreGoalsCount(int scoreGoalsCount) {
        this.scoreGoalsCount = scoreGoalsCount;
    }

    /**
     * This function return the goals number that the team get.
     *
     * @return - The goals number that the team get.
     */
    public int getReceivedGoalsAmount() {
        return receivedGoalsAmount;
    }

    /**
     * This function get the goal count number that the team get and set it.
     *
     * @param receivedGoalsAmount - The given goal count number that the team get.
     */
    public void setReceivedGoalsAmount(int receivedGoalsAmount) {
        this.receivedGoalsAmount = receivedGoalsAmount;
    }

    /**
     * This function return the points number of the team.
     *
     * @return - The points number of the team.
     */
    public int getPoints() {
        return points;
    }

    /**
     * This function get a new points number for the team and set it.
     *
     * @param points - The given new points number for the team.
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * This function return the number of matches that the team played.
     *
     * @return - The number of matches that the team played.
     */
    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    /**
     * This function get a new number of the games that the team played and set it.
     *
     * @param matchesPlayed
     */
    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    /**
     * This function get a court and set it for the team.
     *
     * @param teamCourt - The given court.
     */
    public void setTeamHomeCourt(Court teamCourt) {
        this.teamHomeCourt = teamCourt;
    }

    /**
     * This function get a active status for the team and set it.
     *
     * @param teamActiveStatus - The given active status.
     */
    public void setTeamActiveStatus(boolean teamActiveStatus) {
        this.teamActiveStatus = teamActiveStatus;
    }

    /**
     * This function return the budget of the team
     * @return - the budget of the team
     */
    public Budget getTeamSeasonBudget() {
        return teamSeasonBudget;
    }

    /**
     * This function set the budget of the team
     * @param teamSeasonBudget - the new budget.
     */
    public void setTeamSeasonBudget(Budget teamSeasonBudget) {
        this.teamSeasonBudget = teamSeasonBudget;
    }

    /**
     * This function return list of the team courts
     * @return - list of the team courts
     */
    public ArrayList<Court> getTeamCourts() {
        return teamCourts;
    }

    /**
     * This function check if the given coach already in the team.
     * @param coach - The given coach
     * @return - true if the owner already in the team else false.
     */
    public boolean checkIfCoachExistIn(TeamMember coach) {
        for (int i = 0; i < this.teamCoaches.size(); i++) {
            if (((TeamMember) (this.teamCoaches.get(i))).getUserName().equals(coach.getUserName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * This function check if the given player already in the team.
     * @param player - The given player
     * @return -true if the player already in the team else false.
     */
    public boolean checkIfPlayerExistIn(TeamMember player) {
        for (int i = 0; i < this.teamPlayers.size(); i++) {
            if (((TeamMember) (this.teamPlayers.get(i))).getUserName().equals(player.getUserName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * This function check if the given team manager already in the team.
     * @param teamManager - The given Team manager
     * @return -true if the team manager already in the team else false.
     */
    public boolean checkIfOwnerExistIn(TeamMember teamManager) {
        for (int i = 0; i < this.teamOwners.size(); i++) {
            if (((TeamMember) (this.teamOwners.get(i))).getUserName().equals(teamManager.getUserName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * This function check if the given owner already in the team.
     * @param owner - The given owner
     * @return - true if the owner already in the team else false.
     */
    public boolean checkIfManagerExistIn(TeamMember owner) {
        for (int i = 0; i < this.teamManagers.size(); i++) {
            if (((TeamMember) (this.teamManagers.get(i))).getUserName().equals(owner.getUserName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * This function check if the given court already in the team court list.
     * @param court -The given court.
     * @return - true if the owner already in the team else false.
     */
    public boolean checkIfCourtExistIn(Court court) {
        for (int i = 0; i < this.teamCourts.size(); i++) {
            if (this.teamCourts.get(i).getCourtName().equals(court.getCourtName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * This function return the city that the team represent
     * @return - The name of the city that the team represent.
     */
    public String getCityRepresent() {
        return cityRepresent;
    }

    /**
     * This function transfer ownership of team if the first owner want to leave.
     * @param owner - the given owner to transfer ownership
     */
    public void transferOwnerShip(TeamMember owner){
        for (int i = 0; i < this.teamOwners.size(); i++){
            if(!((TeamMember)this.teamOwners.get(i)).getUserName().equals(owner.getUserName())){
                this.teamOwner = (TeamMember)this.teamOwners.get(i);
            }
        }
    }

    /**
     * This function return the name of the home court of the team
     * @return - The name of the home court of the team
     */
    public Court getTeamHomeCourt() {
        return teamHomeCourt;
    }

    /**
     * This function return the information page of the team
     * @return - the information page of the team
     */
    public TeamInformationPage getTeamPage() {
        return teamPage;
    }

    public void setTeamOwners(ArrayList<OwnerInterface> teamOwners) {
        this.teamOwners = teamOwners;
    }

    public void setTeamCoaches(ArrayList<CoachInterface> teamCoaches) {
        this.teamCoaches = teamCoaches;
    }

    public void setTeamPlayers(ArrayList<PlayerInterface> teamPlayers) {
        this.teamPlayers = teamPlayers;
    }

    public void setTeamManagers(ArrayList<TeamManagerInterface> teamManagers) {
        this.teamManagers = teamManagers;
    }

    public void setTeamCourts(ArrayList<Court> teamCourts) {
        this.teamCourts = teamCourts;
    }

}





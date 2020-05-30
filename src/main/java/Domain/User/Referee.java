
package Domain.User;

import Domain.AssociationManagement.League;
import Domain.AssociationManagement.Match;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * referee user in our system
 */
public class Referee extends Fan {

    //public enum Role{main,side};
    private ArrayList<Match> refereeMatches; //all the referee matches
    //private Role Preparation;
    private League league;
    private String refereeRole;


    /**
     * Constructor
     * @param firstName
     * @param lastName
     * @param userName
     * @param password
     * @param occupation
     * @param birthday
     * @param email
     * @param refRole
     */
    public Referee(String firstName, String lastName, String userName, String password, String occupation, String birthday, String email, String refRole) {
        super(firstName, lastName, userName, password, occupation, birthday, email);
        //Preparation = preparation;
        refereeMatches = new ArrayList<>();
        refereeRole= refRole;
        league = null;
    }

    /**
     * edits the referee information
     * @param firstName
     * @param lastName
     * @param password
     * @param occupation
     * @param birthday
     * @param email
     */
    public void EditRefereeInfo(String firstName, String lastName, String password, String occupation, String birthday, String email){
        this.editInfo(firstName,lastName,password,occupation, birthday, email);
    }

    /**
     * shows all the games that the referee assigned to.
     * @return ArrayList<Match>
     */
    public ArrayList<Match> ViewAssignGames(){
        return this.refereeMatches;
    }

    /** referee can update an ongoing game that he active in.
     * @param match
     */
    public boolean addCommentToGameEvents(Match match, String Comment){
        if(match!=null && match.getEvents()!=null && match.getMainRef().getUserName().equals(this.getUserName())){
            return match.getEvents().getAllGameEvents().add(Comment);
        }
        return false;
    }

    /**
     * referee adds GOAL in ongoing game that he's active in.
     * @param match
     */
    public boolean addGoalEvent(Match match, String player, String homeORaway){
        if(match!=null && match.getEvents()!=null && match.getMainRef().getUserName().equals(this.getUserName())){
            return match.getEvents().addGoalEvent(player,homeORaway);
        }
        return false;
    }

    /**
     * referee adds OFFSIDE in ongoing game that he's active in.
     * @param match
     */
    public boolean addOffsideEvent(Match match, String player){
        if(match!=null && match.getEvents()!=null && match.getMainRef().getUserName().equals(this.getUserName())){
            return match.getEvents().addOffsideEvent(player);
        }
        return false;
    }

    /**
     * referee adds FOUL in ongoing game that he's active in.
     * @param match
     */
    public boolean addFoulEvent(Match match, String player, String card){
        if(match!=null && match.getEvents()!=null && match.getMainRef().getUserName().equals(this.getUserName())){
            return match.getEvents().addFoulEvent(player,card);
        }
        return false;
    }

    /**
     * referee adds INJURY in ongoing game that he's active in.
     * @param match
     */
    public boolean addInjuryEvent(Match match, String player){
        if(match!=null && match.getEvents()!=null && match.getMainRef().getUserName().equals(this.getUserName())){
            return match.getEvents().addInjuryEvent(player);
        }
        return false;
    }

    /**
     * referee adds SUBSTITUTION in ongoing game that he's active in.
     * @param match
     */
    public boolean addSubEvent(Match match, String playerOut, String playerIn){
        if(match!=null && match.getEvents()!=null && match.getMainRef().getUserName().equals(this.getUserName())){
            return match.getEvents().addSubstituteEvent(playerOut,playerIn);
        }
        return false;
    }

    /**
     * main referee can edit game events in time slot of 5 hours since the game ended
     */
    /**
     * main referee can edit game events in time slot of 5 hours since the game ended
     */
    public void EditGameEvents(Match match, String eventContent){
        if(match!=null && match.getEvents()!=null && this.getRefereeRole().equals("Main Referee") && match.getMainRef().getUserName().equals(this.getUserName()) ){
            Date endGame=match.getEndDate();
            Date today = Calendar.getInstance().getTime();
            long timeDiff=today.getTime()-endGame.getTime();
            long diffHours= timeDiff/(60*60*1000);
            if (diffHours<=5){
                match.getEvents().getAllGameEvents().add(eventContent);
            }
            else{
                throw new IllegalArgumentException("you cant change the game events");
                //System.out.println("you cant change the game events");
            }
        }
        else{
            throw new IllegalArgumentException("you are not main referee");

            // System.out.println("you are not main referee");
        }
    }

    public boolean inviteToBeReferee() {
        return true; //need to invite in GUI and choose yes/no
    }

    /**
     * adds a match to specific referee
     * @param match
     */
    public void addMatch(Match match){
        this.refereeMatches.add(match);
    }

    public void setRefereeRole(String refereeRole) {
        this.refereeRole = refereeRole;
    }

    public String getRefereeRole() {
        return refereeRole;
    }

    public ArrayList<Match> getRefereeMatches() {
        return refereeMatches;
    }

    public void setLeague(League league) {
        this.league = league;
    }
}

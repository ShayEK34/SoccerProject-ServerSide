package Domain.AssociationManagement;

import Domain.User.Referee;

import java.util.ArrayList;
import java.util.Vector;

/**
 * class represents a single season which can include some leagues
 */
public class Season {

    private ArrayList<League> allLeagues;
    private int year;
    private Vector<Referee> mainActiveReferees;
    private Vector<Referee> sideActiveReferees;

    /**
     * init with current year
     * @param year
     */
    public Season(int year) {
        this.year = year;
        this.allLeagues = new ArrayList<>();
        mainActiveReferees = new Vector<>();
        sideActiveReferees = new Vector<>();
    }

    /**
     * add league to current season and add the season to the clubs history
     * @param league
     */
    public boolean addLeagueToSeasonAndSeasonToLeague(League league){
        if(league!=null){
            allLeagues.add(league);
            league.addSeasonToLeague(this);
            return true;
        }
        return false;
    }

    public ArrayList<League> getAllLeagues() {
        return allLeagues;
    }

    public void setAllLeagues(ArrayList<League> allLeagues) {
        this.allLeagues = allLeagues;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    /**
     * add a fan to be an active referee
     * @param ref
     */
    public boolean addMainRefereeToActiveReferees(Referee ref){
        if(ref != null){
            mainActiveReferees.add(ref);
            return true;
        }
        return false;
    }

    /**
     * remove a fan from active referees
     * @param ref
     */
    public boolean removeMainRefereeFromActiveReferees(Referee ref){
        if(ref != null){
            for (int i = 0; i < mainActiveReferees.size(); i++) {
                if(mainActiveReferees.get(i).getUserName().equals(ref.getUserName())){
                    mainActiveReferees.remove(ref);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * add a fan to be an active referee
     * @param ref
     */
    public boolean addSideRefereeToActiveReferees(Referee ref){
        if(ref != null){
            sideActiveReferees.add(ref);
            return true;
        }
        return false;
    }

    /**
     * remove a fan from active referees
     * @param ref
     */
    public boolean removeSideRefereeFromActiveReferees(Referee ref){
        if(ref != null){
            for (int i = 0; i < sideActiveReferees.size(); i++) {
                if(sideActiveReferees.get(i).getUserName().equals(ref.getUserName())){
                    sideActiveReferees.remove(ref);
                    return true;
                }
            }
        }
        return false;
    }

    public Vector<Referee> getMainActiveReferees() {
        return mainActiveReferees;
    }

    public Vector<Referee> getSideActiveReferees() {
        return sideActiveReferees;
    }

    public void setMainActiveReferees(Vector<Referee> mainActiveReferees) {
        this.mainActiveReferees = mainActiveReferees;
    }

    public void setSideActiveReferees(Vector<Referee> sideActiveReferees) {
        this.sideActiveReferees = sideActiveReferees;
    }
}

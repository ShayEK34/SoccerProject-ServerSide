package Domain.User;

import Data.SystemDB.UserDaoMdb;
import Domain.AssociationManagement.BudgetManagement;
import Domain.AssociationManagement.League;
import Domain.AssociationManagement.PointsPolicy;
import Domain.AssociationManagement.Season;
import Domain.ClubManagement.TeamInfo;

import java.util.*;

/**
 * represent an association user.
 */
public class AssociationUser extends Fan {
    private int salary;
    private static HashMap<Integer, BudgetManagement> bm;
    private static int year;


    /**
     * constructor
     *
     * @param firstName
     * @param lastName
     * @param userName
     * @param password
     * @param occupation
     * @param birthday
     * @param email
     */
    public AssociationUser(String firstName, String lastName, String userName, String password, String occupation, String birthday, String email) {
        super(firstName, lastName, userName, password, occupation, birthday, email);
        bm = new HashMap<>();
    }

    public static void setBm(HashMap<Integer, BudgetManagement> bm) {
        AssociationUser.bm = bm;
    }

    public static void setYear(int year) {
        AssociationUser.year = year;
    }



//    public void createSeasonAndBudget() {
//        Calendar cal = new GregorianCalendar();
//        year = cal.get(Calendar.YEAR) + 1;
//        Season s = new Season(year); //2019-2020 = 2020
//        DB.getInstance().addSeason(s);
//        BudgetManagement budgetManagement = new BudgetManagement(s);
//        bm.put(year, budgetManagement);
//    }

    public boolean createSeasonAndBudget(int year, double startingBudget) {
        AssociationUser.year = year;
        Season s = new Season(year); //2019-2020 = 2020
        BudgetManagement budgetManagement = new BudgetManagement(s,startingBudget);
        bm.put(year, budgetManagement);
        return true;
    }


    public static boolean addTeamToLeague(TeamInfo team, String leagueName) { //----------add in team member
        if (team != null && leagueName != null) {
            ArrayList<League> leagues = bm.get(year).getBudget().getSeason().getAllLeagues();
            if (leagues != null) {
                for (League league : leagues) {
                    if (league.getLeagueName().equals(leagueName) && bm.get(year).registerTeamToLeague(team, league)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * the function define new referee.
     *
     * @return
     */
    public boolean NominateReferee(Referee ref) {

        if (ref instanceof Referee) {
            boolean ans = ((Referee) ref).inviteToBeReferee();
            if (ans && ((Referee) ref).getRefereeRole().equals("Main Referee")) {
                bm.get(year).getBudget().getSeason().addMainRefereeToActiveReferees((Referee) ref);
                return true;
            } else if (ans && ((Referee) ref).getRefereeRole().equals("Side Referee")) {
                bm.get(year).getBudget().getSeason().addSideRefereeToActiveReferees((Referee) ref);
                return true;
            }
        }
        return false;
    }
    public boolean addRefereeToLeague(League leagueName, Referee ref){
        if(leagueName != null && ref != null){
            leagueName.addMainRefereeToLeague(ref);
            return true;
        }
        return false;
    }

//    public boolean addRefereeToLeague(String leagueName, String username) {
//        Referee refereeUser;
//        if (DB.getInstance().getUser(username) instanceof Referee) {
//            refereeUser = (Referee) DB.getInstance().getUser(username);
//        } else return false;
//        if (refereeUser != null) {
//            Vector<Referee> mainRefs = bm.get(year).getBudget().getSeason().getMainActiveReferees();
//            Vector<Referee> sideRefs = bm.get(year).getBudget().getSeason().getSideActiveReferees();
//            ArrayList<League> allLeague = bm.get(year).getBudget().getSeason().getAllLeagues();
//            if (leagueName != null && username != null) {
//                boolean isMain = false;
//                boolean isSide = false;
//                for (Referee ref : mainRefs) {
//                    if (ref.getUserName().equals(username)) {
//                        isMain = true;
//                    }
//                }
//                for (Referee ref : sideRefs) {
//                    if (ref.getUserName().equals(username)) {
//                        isSide = true;
//                    }
//                }
//                if (isMain) {
//                    Referee referee = (Referee) DB.getInstance().getUser(username);
//                    if (referee != null) {
//                        for (int i = 0; i < allLeague.size(); i++) {
//                            if (allLeague.get(i).getLeagueName().equals(leagueName)) {
//                                bm.get(year).getBudget().getSeason().getAllLeagues().get(i).addMainRefereeToLeague(referee);
//                                referee.setLeague(allLeague.get(i));
//                                return true;
//                            }
//                        }
//                    }
//                } else if (isSide) {
//                    Referee referee = (Referee) DB.getInstance().getUser(username);
//                    if (referee != null) {
//                        for (int i = 0; i < allLeague.size(); i++) {
//                            if (allLeague.get(i).getLeagueName().equals(leagueName)) {
//                                bm.get(year).getBudget().getSeason().getAllLeagues().get(i).addSideRefereeToLeague(refereeUser);
//                                referee.setLeague(allLeague.get(i));
//                                return true;
//                            }
//                        }
//                    }
//                }
//                else if (refereeUser.getPreparation().toString().equals("main")) {
//                    for (int i = 0; i < allLeague.size(); i++) {
//                        if (allLeague.get(i).getLeagueName().equals(leagueName)) {
//                            bm.get(year).getBudget().getSeason().getAllLeagues().get(i).addMainRefereeToLeague(refereeUser);
//                            return true;
//                        }
//                    }
//                }
//                else if (refereeUser.getPreparation().toString().equals("side")) {
//                    for (int i = 0; i < allLeague.size(); i++) {
//                        if (allLeague.get(i).getLeagueName().equals(leagueName)) {
//                            bm.get(year).getBudget().getSeason().getAllLeagues().get(i).addSideRefereeToLeague(refereeUser);
//                            return true;
//                        }
//                    }
//                }
//            }
//        }
//        return false;
//    }

//    public boolean removeRefereeFromJob(String username) {
//        Referee ref;
//        if (DB.getInstance().getUser(username) instanceof Referee) {
//            ref = (Referee) DB.getInstance().getUser(username);
//        } else return false;
//
//        if (ref.getPreparation().toString().equals("main")) {
//            boolean fired = bm.get(year).getBudget().getSeason().removeMainRefereeFromActiveReferees(ref);
//            if (fired) {
//                ArrayList<League> allLeague = bm.get(year).getBudget().getSeason().getAllLeagues();
//                for (int i = 0; i < allLeague.size(); i++) {
//                    allLeague.get(i).removeMainRefereeFromLeague(ref);
//                }
//            }
//            return true;
//        } else if (ref.getPreparation().toString().equals("side")) {
//            boolean fired = bm.get(year).getBudget().getSeason().removeSideRefereeFromActiveReferees(ref);
//            if (fired) {
//                ArrayList<League> allLeague = bm.get(year).getBudget().getSeason().getAllLeagues();
//                for (int i = 0; i < allLeague.size(); i++) {
//                    allLeague.get(i).removeSideRefereeFromLeague(ref);
//                }
//            }
//            return true;
//        }
//
//        return false;
//    }

//    public boolean removeRefereeFromLeague(String username, String leagueName) {
//        Referee ref;
//        if (DB.getInstance().getUser(username) instanceof Referee) {
//            ref = (Referee) DB.getInstance().getUser(username);
//        } else return false;
//
//        ArrayList<League> allLeague = bm.get(year).getBudget().getSeason().getAllLeagues();
//
//        if (ref.getPreparation().toString().equals("main")) {
//            for (int i = 0; i < allLeague.size(); i++) {
//                if (allLeague.get(i).removeMainRefereeFromLeague(ref)) {
//                    bm.get(year).getBudget().getSeason().removeMainRefereeFromActiveReferees(ref);
//                    return true;
//                }
//            }
//        } else if (ref.getPreparation().toString().equals("side")) {
//            for (int i = 0; i < allLeague.size(); i++) {
//                if (allLeague.get(i).removeSideRefereeFromLeague(ref)) {
//                    bm.get(year).getBudget().getSeason().removeSideRefereeFromActiveReferees(ref);
//                    return true;
//                }
//            }
//        }
//
//        return false;
//    }


//    /**
//     * the function define new league.
//     *
//     * @param leagueName
//     * @param numOfTeams
//     * @param numOfRounds
//     * @param date
//     * @return
//     */
//    public boolean DefineLeague(String leagueName, int numOfTeams, int numOfRounds, Date date) {
//        Season s = DB.getInstance().getSeason(year);
//        if (s != null) {
//            for (League exist : s.getAllLeagues()) {
//                if (exist.getLeagueName().equals(leagueName)) {
//                    return false;
//                }
//            }
//            League league = new League(leagueName, numOfTeams, numOfRounds, date);
//            s.addLeagueToSeasonAndSeasonToLeague(league);
//            DB.getInstance().addLeague(league);
//            return true;
//        }
//        return false;
//    }

    public boolean addNewLeagueToSeason(int seasonYear, String leagueName, String date, int pointsPerWin,
                                        int pointsPerLoss, int pointsPerDraw, boolean differenceGoals,
                                        boolean straightMeets, int roundsNum, int numOfTeams) {
        Season s = bm.get(seasonYear).getBudget().getSeason();
        if (s != null) {
            for (League exist : s.getAllLeagues()) {
                if (exist.getLeagueName().equals(leagueName)) {
                    return false;
                }
            }
            League league = new League(leagueName, numOfTeams, roundsNum, date);
            s.addLeagueToSeasonAndSeasonToLeague(league);
            return true;

        }
        return false;
    }

    public boolean createRankAndPointsPolicy(League leagueName, int pointsForWin, int pointsForDraw, int pointsForLoss, boolean goalDiffTieBreaker, boolean directResultsTieBreaker) {
        if (leagueName != null && pointsForWin >= 0 && pointsForDraw >= 0 && pointsForLoss >= 0) {
            PointsPolicy pp = new PointsPolicy(pointsForWin, pointsForDraw, pointsForLoss, goalDiffTieBreaker, directResultsTieBreaker);
            if (leagueName != null) {
                leagueName.setPointsPolicy(pp);
                return true;
            }
        }
        return false;
    }

    public boolean updateRankAndPointsPolicy(League leagueName, int pointsForWin, int pointsForDraw, int pointsForLoss, boolean goalDiffTieBreaker, boolean directResultsTieBreaker) {
        if (leagueName != null && pointsForWin >= 0 && pointsForDraw >= 0 && pointsForLoss >= 0) {

            if (leagueName != null && leagueName.getPointsPolicy() != null) {
                leagueName.getPointsPolicy().setPointsForWin(pointsForWin);
                leagueName.getPointsPolicy().setPointsForDraw(pointsForDraw);
                leagueName.getPointsPolicy().setPointsForLoss(pointsForLoss);
                leagueName.getPointsPolicy().setGoalDiffTieBreaker(goalDiffTieBreaker);
                leagueName.getPointsPolicy().setDirectResultsTieBreaker(directResultsTieBreaker);
                return true;
            }
        }
        return false;
    }


    public boolean updateGameScheduling(League leagueName, int numOfRounds) {
        if (leagueName != null && (numOfRounds == 1 || numOfRounds == 2)) {
            if (leagueName != null) {
                leagueName.setNumOfRounds(numOfRounds);
                leagueName.createSeasonFixtures();
                return true;
            }
        }
        return false;
    }

    public boolean updateGameScheduling(League league) {
        if (league!=null && league.getLeagueName() != null && (league.getNumOfRounds() == 1 || league.getNumOfRounds() == 2)) {
            league.setNumOfRounds(league.getNumOfRounds());
            league.createSeasonFixtures();
            return true;
        }
        return false;
    }

//    /**
//     * the function activate the game policy.
//     */
//    public boolean ActivateGamePolicy(String leagueName) {
//        if(leagueName != null) {
//            League league = DB.getInstance().getLeague(leagueName);
//            if (league != null) {
//                league.createSeasonFixtures();
//                return true;
//            }
//        }
//         return false;
//    }

//    private void addAssoUserToEmployees(){
//        bm.get(year).addEmployee(this);
//    }

    /**
     * get salary.
     *
     * @return
     */
    public int getSalary() {
        return salary;
    }

    /**
     * set salary.
     *
     * @param salary
     */
    public void setSalary(int salary) {
        this.salary = salary;
    }

    public static HashMap<Integer, BudgetManagement> getBm() {
        return bm;
    }
}


package Domain.AssociationManagement;
import Domain.ClubManagement.Court;
import Domain.ClubManagement.TeamInfo;

import Domain.User.Referee;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
///////
/**
 * this class describes a league with the teams playing in the league!!!
 */
public class League {

    private String leagueName;
    private Vector<TeamInfo> teams;
    private Vector<Court> courts;
    private ArrayList<ArrayList<Match>> fixtures;
    private int numOfTeams;
    private Vector<Referee> mainReferees;
    private Vector<Referee> sideReferees;
    private PointsPolicy pointsPolicy;
    private int numOfRounds;
    private TeamInfo winner;
    private Date tournamentBeginDate;
    private String tournamentStartDate;
    private ArrayList<Season> seasons;
    private GameScheduling scheduler;


    public League(String leagueName) {
        this.leagueName = leagueName;
        this.teams = new Vector<>();
        numOfTeams = 0;
        mainReferees = new Vector<>();
        sideReferees = new Vector<>();
        numOfRounds = 0;
        courts = new Vector<>();
        fixtures = new ArrayList<>();
        seasons = new ArrayList<>();
    }

    public League(String leagueName, int numOfTeams, int numOfRounds, Date date) {
        this.leagueName = leagueName;
        this.numOfTeams = numOfTeams;
        this.teams = new Vector<>();
        this.numOfRounds = numOfRounds;
        tournamentBeginDate = date;
        this.tournamentStartDate = date.toString();
        mainReferees = new Vector<>();
        sideReferees = new Vector<>();
        courts = new Vector<>();
        fixtures = new ArrayList<>();
        seasons = new ArrayList<>();
    }

    //need to check
    public League(String leagueName, int numOfTeams, int numOfRounds, String date) {
        this.leagueName = leagueName;
        this.numOfTeams = numOfTeams;
        this.teams = new Vector<>();
        this.numOfRounds = numOfRounds;
        tournamentBeginDate = null;
        this.tournamentStartDate = date;
        mainReferees = new Vector<>();
        sideReferees = new Vector<>();
        courts = new Vector<>();
        fixtures = new ArrayList<>();
        seasons = new ArrayList<>();
    }




    /**
     * add team to the league if there is room and it doesn't in the league already
     * @param t
     */
    public boolean addTeam(TeamInfo t){
        boolean ans = false;
        boolean existTeam = false;
        boolean existCourt = false;
        boolean hasPlayers = false;
        int players = 0;
        boolean hasOwner = false;
        boolean hasCoach = false;

        if(t!=null){
            if(t.getTeamOwner()!=null){
                hasOwner = true;
            }

            if(t.getTeamCoaches() != null && t.getTeamCoaches().size() > 0){
                hasCoach = true;
            }

            for (int i = 0; i < t.getTeamPlayers().size(); i++) {
                if(t.getTeamPlayers().get(i)!=null){
                    players++;
                }
            }
            if(players>=11){
                hasPlayers = true;
            }

            if(teams.size() < numOfTeams){
                for (int i = 0; i < teams.size() ; i++) {
                    if(teams.get(i).getTeamName().equals(t.getTeamName())){
                        existTeam = true;
                    }
                }

                for (int i = 0; i < courts.size(); i++) {
                    if( courts.get(i).getCourtName().equals(t.getTeamCourt().getCourtName())){
                        existCourt = true;
                    }
                }
                if(hasOwner && hasCoach && hasPlayers && !existTeam){
                    teams.add(t);
                    ans = true;
                    if(!existCourt){
                        courts.add(t.getTeamCourt());
                    }
                }else{
                    System.out.println("team is already in the league");
                }
            }else{
                System.out.println("the league is full, no room for extra team");
            }
        }
        return ans;
    }

//    /**
//     * remove team from league
//     * @param t
//     */
//    public boolean removeTeam(TeamInfo t){
//        boolean ans = false;
//        int teamsWithSameCourt = 0;
//        if( t!= null){
//            if(teams.contains(t)){
//                teams.remove(t);
//                DB.getInstance().deleteTeam(t.getTeamName());
//                ans = true;
//                System.out.println("The team "+ t.getTeamName() + " was removed from the league");
//                for (int i = 0; i < teams.size() ; i++) {
//                    if(((TeamInfo)teams.toArray()[i]).getTeamCourt().equals(t.getTeamCourt())){
//                        teamsWithSameCourt++;
//                    }
//                }
//                if(teamsWithSameCourt == 0){
//                    courts.remove(t.getTeamCourt());
//                }
//            }
//            else {
//                System.out.println( "The team "+ t.getTeamName() + " is not in the league");
//            }
//        }
//        else{
//            System.out.println("Please enter a team to remove");
//        }
//        return ans;
//    }

    /**
     * create the games schedule for the rest of the season
     * @return
     */
    public ArrayList<ArrayList<Match>> createSeasonFixtures()
    {
        scheduler = new GameScheduling(numOfRounds);
        ArrayList<Match> allMatches = scheduler.createMatches(this);
        this.fixtures = scheduler.createSchedule(this, allMatches, mainReferees,sideReferees);
        return fixtures;
    }

    public boolean addMainRefereeToLeague(Referee ref) {
        if(ref != null){
            if(ref.getRefereeRole().equals("Main Referee") && !mainReferees.contains(ref)){
                mainReferees.add(ref);
                return true;
            }
            else if(ref.getRefereeRole().equals("Side Referee") && !sideReferees.contains(ref)){
                sideReferees.add(ref);
                return true;
            }

        }
        return false;
    }
    public boolean addSideRefereeToLeague(Referee ref) {
        if(ref != null){
            if(!sideReferees.contains(ref)){
                sideReferees.add(ref);
                return true;
            }

        }
        return false;
    }

    public boolean removeMainRefereeFromLeague(Referee ref) {
        if(ref!=null){
            for (int i = 0; i < mainReferees.size(); i++) {
                if(ref.getUserName().equals(mainReferees.get(i).getUserName())){
                    mainReferees.remove(i);
                    return true;
                }
            }
        }
        return false;
    }
    public boolean removeSideRefereeFromLeague(Referee ref) {
        if(ref!=null){
            for (int i = 0; i < sideReferees.size(); i++) {
                if(ref.getUserName().equals(sideReferees.get(i).getUserName())){
                    sideReferees.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean addSeasonToLeague(Season season) {
        if(season!=null){
            if(!seasons.contains(season)){
                seasons.add(season);
                return true;
            }
        }
        return false;
    }



    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }



    public Vector<TeamInfo> getTeams() {
        return teams;
    }
    public void setTeams(Vector<TeamInfo> teams) {
        this.teams = teams;
    }


    public int getNumOfTeams() {
        return numOfTeams;
    }

    public void setNumOfTeams(int numOfTeams) {
        this.numOfTeams = numOfTeams;
    }




    public Vector<Referee> getMainReferees() {
        return mainReferees;
    }

    public void setMainReferees(Vector<Referee> mainReferees) {
        this.mainReferees = mainReferees;
    }




    public PointsPolicy getPointsPolicy() {
        return pointsPolicy;
    }

    public void setPointsPolicy(PointsPolicy pointsPolicy) {
        this.pointsPolicy = pointsPolicy;
    }




    public int getNumOfRounds() {
        return numOfRounds;
    }

    public void setNumOfRounds(int numOfRounds) {
        this.numOfRounds = numOfRounds;
    }




    public TeamInfo getWinner() {
        return winner;
    }

    public void setWinner(TeamInfo winner) {
        this.winner = winner;
    }




    public Date getTournamentBeginDate() {
        return tournamentBeginDate;
    }

    public void setTournamentBeginDate(Date tournamentBeginDate) {
        this.tournamentBeginDate = tournamentBeginDate;
    }




    public Vector<Court> getCourts() {
        return courts;
    }

    public void setCourts(Vector<Court> courts) {
        this.courts = courts;
    }




    public Vector<Referee> getSideReferees() {
        return sideReferees;
    }

    public void setSideReferees(Vector<Referee> sideReferees) {
        this.sideReferees = sideReferees;
    }



    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(ArrayList<Season> seasons) {
        this.seasons = seasons;
    }

    public ArrayList<ArrayList<Match>> getFixtures() {
        return fixtures;
    }

    public String getTournamentStartDate() {
        return tournamentStartDate;
    }

    public void setTournamentStartDate(String tournamentStartDate) {
        this.tournamentStartDate = tournamentStartDate;
    }

    //    /**
//     * prints the current table of the league
//     */
//    public void printTable() {
//
//        String[][] table = new String[teams.size()+1][];
//        table[0] = new String[] {"Team Name", "Games Played", "Wins", "Draws", "Losses", "Goal Difference", "Total Points"};
//        int index = 0;
//        for (int i = 1; i < table.length; i++,index++) {
//            table[i] = new String[] {teams.get(index).getTeamName() ,""+teams.get(index).getMatchesPlayed(), ""+teams.get(index).getWinCount(),
//                    ""+teams.get(index).getDrawCount(), ""+teams.get(index).getDefeatCount() ,
//                    ""+ (teams.get(index).getScoreGoalsCount()-teams.get(index).getReceivedGoalsAmount()) ,
//                    ""+ (teams.get(index).getWinCount()*pointsPolicy.getPointsForWin()+teams.get(index).getDrawCount()*pointsPolicy.getPointsForDraw()+teams.get(index).getDefeatCount()*pointsPolicy.getPointsForLoss())};
//        }
//
//        for (Object[] row :
//                table) {
//            System.out.format("%-25s%15s%15s%15s%15s%20s%15s\n", row);
//        }
//    }


}



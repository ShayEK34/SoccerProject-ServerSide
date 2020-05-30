package Domain.AssociationManagement;

import Domain.ClubManagement.Court;
import Domain.ClubManagement.TeamInfo;
import Domain.User.Referee;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Match {

    private TeamInfo homeTeam;
    private TeamInfo awayTeam;
    private Court court;
    private Date matchDate;
    private String gameDate;
    private String result;
    private boolean played;
    private GameEvents events;
    private Referee mainRef;
    private ArrayList<Referee> sideRefs;

    public Match()
    {
        this.homeTeam = null;
        this.awayTeam = null;
        this.court = null;
        this.matchDate = null;
        this.played = false;
        this.result = null;
        events = null;
        mainRef = null;
        sideRefs = new ArrayList<>();
        this.gameDate = "";
    }

    public Match(TeamInfo team1, TeamInfo team2, Court court, Date matchDate,Referee ref) {
        this.homeTeam = team1;
        this.awayTeam = team2;
        this.court = court;
        this.matchDate = matchDate;
        this.played = false;
        this.result = null;
        events = new GameEvents();
        mainRef = ref;
        sideRefs = new ArrayList<>();
        this.gameDate = "";
    }

    public Match(TeamInfo team1, TeamInfo team2, Court court, String matchDate,Referee ref) {
        this.homeTeam = team1;
        this.awayTeam = team2;
        this.court = court;
        this.matchDate = null;
        this.played = false;
        this.result = null;
        events = new GameEvents();
        mainRef = ref;
        sideRefs = new ArrayList<>();
        this.gameDate = matchDate;
    }

    public void setTeams(TeamInfo team1, TeamInfo team2) {
        this.homeTeam = team1;
        this.awayTeam = team2;
    }

    public Court getCourt() {
        return court;
    }

    public void setCourt(Court court) {
        this.court = court;
    }

    public Date getMatchDate() {
        return matchDate;
    }

    public TeamInfo getHomeTeam() {
        return homeTeam;
    }

    public TeamInfo getAwayTeam() {
        return awayTeam;
    }

    public void setHomeTeam(TeamInfo homeTeam) {
        this.homeTeam = homeTeam;
    }

    public void setAwayTeam(TeamInfo awayTeam) {
        this.awayTeam = awayTeam;
    }

    public void setMatchDate(Date matchDate) {
        this.matchDate = matchDate;
    }

    public boolean isPlayed() {
        return played;
    }


    public void setPlayed(boolean played) {
        this.played = played;
    }

//    public String getResult()
//    {
//        return events.getHomeGoals()+" - "+events.getAwayGoals();
//    }

//    @Override
//    public String toString() {
//        return homeTeam.getTeamName() + " against " + awayTeam.getTeamName() + " on " + DateParser.toString(matchDate) + " at " + court.getCourtName() + "\n";
//    }

    public Referee getMainRef() {
        return mainRef;
    }

    public void setMainRef(Referee mainRef) {
        this.mainRef = mainRef;
    }
    public GameEvents getEvents() {
        return events;
    }

    public ArrayList<Referee> getSideRefs() {
        return sideRefs;
    }

    private Calendar minuteIncrementer(Date currentDate, int minuteToIncrement)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.MINUTE, minuteToIncrement);  // number of days to add

        return cal;
    }

    public Date getEndDate() {
        Date end = minuteIncrementer(matchDate , 110).getTime();
        return end;
    }

    public void setEvents(GameEvents events) {
        this.events = events;
    }

    public String getGameDate() {
        return gameDate;
    }

    public String getResult() {
        return result;
    }
}

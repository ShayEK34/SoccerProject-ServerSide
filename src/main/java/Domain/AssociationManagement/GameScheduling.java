package Domain.AssociationManagement;

import Domain.ClubManagement.Court;
import Domain.ClubManagement.TeamInfo;
import Domain.User.Referee;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

/**
 * this class responsible for creating the schedule of the season's games
 */
public class GameScheduling {

    private int numOfRounds;
    DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");


    public GameScheduling(int numOfRounds) {
        this.numOfRounds = numOfRounds;
    }

    /**
     * create all possible games that should be play in the league during the season
     * @param league
     * @return
     */
    public ArrayList<Match> createMatches(League league) {
        if (league != null) {
            ArrayList<Match> matches = new ArrayList<Match>();
            int indexInMatches = 0;

            int numberOfRounds = league.getNumOfRounds();
            ArrayList<TeamInfo> teamsList = new ArrayList<TeamInfo>(league.getTeams());
            int numOfMatches = (league.getTeams().size()*(league.getTeams().size()-1)/2)*league.getNumOfRounds();
            int totTeams = league.getTeams().size();

            for (int k = 1; k <= numberOfRounds; k++) {

                for (int i = 0; i < totTeams - 1; i++) {

                    for (int j = i + 1; j < totTeams; j++) {
                        Match match = new Match();
                        if(indexInMatches < numOfMatches/2){
                            match.setTeams(teamsList.get(i), teamsList.get(j));
                        }else{
                            match.setTeams(teamsList.get(j),teamsList.get(i));
                        }
                        matches.add(match);
                        indexInMatches++;
                    }
                }
            }
            return matches;
        }
        return null;
    }


    /**
     * creating schedule of days and hours to the games of the league considering home/away games and court's limitations
     * @param league
     * @param matches
     * @param mainReferees
     * @param sideReferees
     * @return
     */
    public ArrayList<ArrayList<Match>> createSchedule(League league, ArrayList<Match> matches, Vector<Referee> mainReferees, Vector<Referee> sideReferees) //matches in order of 1-2,1-3,1-4,...
    {
        ArrayList<ArrayList<Match>> fixtures = null;
        if (league != null && matches != null) {
            fixtures = new ArrayList<ArrayList<Match>>(); // set fixtures

            for (int i = 0; i < ((league.getTeams().size() - 1) * numOfRounds) ; i++) {
                ArrayList<Match> f = new ArrayList<>();
                fixtures.add(f);
            }

            int mainRefIndex = 0;
            int sideRefIndex = 0;
            int[] isScheduledGame = new int[matches.size()];
            int numOfMatches = isScheduledGame.length;
            ArrayList<Court> courtsList = new ArrayList<>(league.getCourts());
            int[] isCourtTakenDay1 = new int[courtsList.size()];
            int[] isCourtTakenDay2 = new int[courtsList.size()];
            int[] isCourtTakenDay3 = new int[courtsList.size()];
            int numOfGamesPerFixture = league.getTeams().size()/2;
            String startLeagueDate = league.getTournamentStartDate();
            Date date = null;
            try{
                date = sourceFormat.parse(startLeagueDate);
            }catch (Exception e){e.printStackTrace();}
//            Date date = league.getTournamentBeginDate();
            int fixtureNum = 0;

            while(numOfMatches > 0){
                for (int i = 0; i < matches.size() && numOfGamesPerFixture > 0 ; i++) {
                    if(isScheduledGame[i]==0){
                        ArrayList<Match> fixtureGames = fixtures.get(fixtureNum);
                        TeamInfo home = matches.get(i).getHomeTeam();
                        TeamInfo away = matches.get(i).getAwayTeam();
                        matches.get(i).setMainRef(league.getMainReferees().get(mainRefIndex));
                        league.getMainReferees().get(mainRefIndex).addMatch(matches.get(i));
                        mainRefIndex++;
                        matches.get(i).getSideRefs().add(league.getSideReferees().get(sideRefIndex));
                        league.getSideReferees().get(sideRefIndex).addMatch(matches.get(i));
                        matches.get(i).getSideRefs().add(league.getSideReferees().get(sideRefIndex+1));
                        league.getSideReferees().get(sideRefIndex+1).addMatch(matches.get(i));
                        matches.get(i).getSideRefs().add(league.getSideReferees().get(sideRefIndex+2));
                        league.getSideReferees().get(sideRefIndex+2).addMatch(matches.get(i));
                        sideRefIndex += 3;

                        //add set match to a referee
                        if( !isTeamPlayingInFixture( fixtureGames , home) && !isTeamPlayingInFixture( fixtureGames , away) ){
                            int courtIndex = getCourtIndex( home, courtsList );
                            if(isCourtTakenDay1[courtIndex] == 0){
                                matches.get(i).setMatchDate(date);
                                isCourtTakenDay1[courtIndex] = 1;

                            }else if(isCourtTakenDay2[courtIndex] == 0){
                                Date date1 = dateIncrementer(date , 1).getTime();
                                matches.get(i).setMatchDate(date1);
                                isCourtTakenDay2[courtIndex] = 1;

                            }else if(isCourtTakenDay3[courtIndex] == 0){
                                Date date1 = dateIncrementer(date , 2).getTime();
                                matches.get(i).setMatchDate(date1);
                                isCourtTakenDay3[courtIndex] = 1;
                            }
                            matches.get(i).setCourt(home.getTeamCourt());
//                            matches.get(i).setMatchDate(date);
                            fixtureGames.add(matches.get(i));
                            numOfGamesPerFixture--;
                            numOfMatches--;
                            isScheduledGame[i] = 1;
                        }
                    }
                }
                isCourtTakenDay1 = new int[courtsList.size()];
                isCourtTakenDay2 = new int[courtsList.size()];
                isCourtTakenDay3 = new int[courtsList.size()];
                fixtureNum++;
                date = dateIncrementer(date,7).getTime();
                numOfGamesPerFixture = league.getTeams().size()/2;
                mainRefIndex = 0;
                sideRefIndex = 0;
            }



        }
        return fixtures;
    }

    /**
     * find the index of team's court
     * @param home
     * @param courtsList
     * @return
     */
    private int getCourtIndex(TeamInfo home, ArrayList<Court> courtsList) {
        int index = 0;
        for (int i = 0; i < courtsList.size(); i++) {
            if(courtsList.get(i).equals(home.getTeamCourt())){
                index = i;
            }
        }
        return index;
    }

    /**
     * find if a team is playing in a fixture to know if it uses it's court
     * @param fixture
     * @param team
     * @return
     */
    private boolean isTeamPlayingInFixture( ArrayList<Match> fixture , TeamInfo team ){
        boolean ans = false;
        for (int i = 0; i < fixture.size() && !ans; i++) {
            Match m = fixture.get(i);
            if( m.getHomeTeam().equals(team) || m.getAwayTeam().equals(team)){
                ans = true;
            }
        }
        return ans;
    }


    /**
     * increments the date to schedule a game in different day.
     * @param currentDate
     * @param numberOfDaysToIncrement
     * @return
     */
    private Calendar dateIncrementer(Date currentDate, int numberOfDaysToIncrement)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.DATE, numberOfDaysToIncrement);  // number of days to add

        return cal;
    }






}




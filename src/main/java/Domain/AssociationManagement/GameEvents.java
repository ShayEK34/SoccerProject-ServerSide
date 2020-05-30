package Domain.AssociationManagement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * this class describes all the game events that can happen in a single soccer match
 */
public class GameEvents {

    enum Event {goal, offside, foul, yellowCard, redCard, injury, substitute}

    private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

    private int homeGoals;
    private int awayGoals;
    private Date beginMatch;
    private ArrayList<String> allGameEvents;


    public GameEvents() {
        homeGoals = 0;
        awayGoals = 0;
        beginMatch = new Date();
        allGameEvents = new ArrayList<String>();
    }

    /**
     * adding goal event in the form of date,minute in game, scorer's name
     * @param player
     * @param homeAway
     */
    public boolean addGoalEvent(String player, String homeAway) {
        if(player!=null && homeAway!=null){
            if(homeAway.equals("home")){
                homeGoals++;
            }
            else if (homeAway.equals("away")){
                awayGoals++;
            }else{
                return false;
            }

            StringBuilder event = new StringBuilder();
            String dateToday = DateParser.toString(beginMatch).substring(12);
            int minute = getMinuteInMatch();
            event.append(dateToday).append(" ").append(minute).append(" ").append(Event.goal.toString()).append(" - ").append(player);

            allGameEvents.add(event.toString());
            return true;
        }
        return false;
    }

    public int getMinuteInMatch(){
        Date d1 = new Date();
        String start = DateParser.toString(beginMatch).substring(0, 8);
        String goal = DateParser.toString(d1).substring(0, 8);
        int diffMinutes = 0;
        int diffHours = 0;
        Date tmpStart = null;
        Date tmpEnd = null;
        try {
            tmpStart = format.parse(start);
            tmpEnd = format.parse(goal);

            //in milliseconds
            long diff = tmpEnd.getTime() - tmpStart.getTime();

            diffMinutes = (int) (diff / (60 * 1000) % 60);
            diffHours = (int) (diff / (60 * 60 * 1000) % 24);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return diffMinutes + diffHours*60;
    }

    /**
     * adding an offside event in the form date,minute in game, player's name who caught offside
     * @param player
     */
    public boolean addOffsideEvent(String player) {
        if(player!=null){
            StringBuilder event = new StringBuilder();
            String dateToday = DateParser.toString(beginMatch).substring(12);
            int minute = getMinuteInMatch();
            event.append(dateToday).append(" ").append(minute).append(" ").append(Event.offside.toString()).append(" - ").append(player);
            allGameEvents.add(event.toString());
            return true;
        }
        return false;
    }

    /**
     * adding a foul event and specify if a card pulled out to the player
     * @param player
     * @param card
     */
    public boolean addFoulEvent(String player , String card) {
        if(player!=null && card!=null){
            StringBuilder event = new StringBuilder();
            String dateToday = DateParser.toString(beginMatch).substring(12);
            int minute = getMinuteInMatch();

            if(card.equals("")){
                event.append(dateToday).append(" ").append(minute).append(" ").append(Event.foul.toString()).append(" - ").append(player);
            }else if(card.equals("yellow")){
                event.append(dateToday).append(" ").append(minute).append(" ").append("Yellow Card").append(" - ").append(player);
            }else if(card.equals("red")){
                event.append(dateToday).append(" ").append(minute).append(" ").append("Red Card").append(" - ").append(player);
            }else return false;

            allGameEvents.add(event.toString());
            return true;
        }
        return false;
    }

    /**
     * adding an injury event in form of date,minute in game, player's name who got injured
     * @param player
     */
    public boolean addInjuryEvent(String player) {
        if(player != null){
            StringBuilder event = new StringBuilder();
            String dateToday = DateParser.toString(beginMatch).substring(12);
            int minute = getMinuteInMatch();
            event.append(dateToday).append(" ").append(minute).append(" ").append(Event.injury.toString()).append(" - ").append(player);
            allGameEvents.add(event.toString());
            return true;
        }
        return false;
    }

    /**
     * adding a substitute event of the player who comes off the court and the player who comes on.
     * @param playerOut
     * @param playerIn
     */
    public boolean addSubstituteEvent(String playerOut , String playerIn) {
        if(playerOut!=null && playerIn!=null){
            StringBuilder event = new StringBuilder();
            String dateToday = DateParser.toString(beginMatch).substring(12);
            int minute = getMinuteInMatch();
            event.append(dateToday).append(" ").append(minute).append(" ").append(Event.substitute.toString()).append(" : Player Out - ").append(playerOut).append(" , Player In - ").append(playerIn);
            allGameEvents.add(event.toString());
            return true;
        }
        return false;
    }


    public SimpleDateFormat getFormat() {
        return format;
    }

    public void setFormat(SimpleDateFormat format) {
        this.format = format;
    }

    public int getHomeGoals() {
        return homeGoals;
    }

    public void setHomeGoals(int homeGoals) {
        this.homeGoals = homeGoals;
    }

    public int getAwayGoals() {
        return awayGoals;
    }

    public void setAwayGoals(int awayGoals) {
        this.awayGoals = awayGoals;
    }

    public Date getBeginMatch() {
        return beginMatch;
    }

    public void setBeginMatch(Date beginMatch) {
        this.beginMatch = beginMatch;
    }

    public ArrayList<String> getAllGameEvents() {
        return allGameEvents;
    }

    public void setAllGameEvents(ArrayList<String> allGameEvents) {
        this.allGameEvents = allGameEvents;
    }
}


package service;

import Domain.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private Model model;
    ArrayList<ClientHandler> clients;

    public ClientHandler(Socket client,ArrayList<ClientHandler> clients) throws IOException {
        this.client = client;
        in= new BufferedReader((new InputStreamReader((client.getInputStream()))));
        out= new PrintWriter(client.getOutputStream(),true);
        model=new Model();
        this.clients=clients;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String requestIn = in.readLine();
                if(requestIn!=null) {
                    String[] splittedstr = parse(requestIn);
                    Object reply = new Object();

                    //-------------------------------All Cases ------------------------------

                    if (splittedstr[0].equals("loginUser")) {
                        reply = model.loginUser(splittedstr[1], splittedstr[2]);
                        out.println(reply);
                        //System.out.println((((String)reply)));
                    } else if(splittedstr[0].equals("logout")) {
                        reply=model.logout(splittedstr[1]);
                        out.println(reply);
                    }else if (splittedstr[0].equals("changeTeamStatus")) {
                        reply = model.changeTeamStatus(splittedstr[1]);
                        String []split=((String)reply).split(",,,,");
                        out.println(split[0]);
                        sendAllClients(split[1]);
                        //System.out.println((((String)reply)));
                    } else if (splittedstr[0].equals("addTeam")) {
                        reply = model.addTeam(splittedstr[1], splittedstr[2], splittedstr[3], splittedstr[4], splittedstr[5], splittedstr[6]);
                        out.println(reply);
                        //System.out.println((((String)reply)));
                    } else if (splittedstr[0].equals("chooseCourt")) {
                        reply = model.chooseCourt(splittedstr[1]);
                        out.println(reply);
                        //System.out.println((((String)reply)));
                    } else if (splittedstr[0].equals("addUser")) {
                        reply = model.addUser(splittedstr[1], splittedstr[2], splittedstr[3], splittedstr[4], splittedstr[5], splittedstr[6], splittedstr[7], splittedstr[8], splittedstr[9]);
                        out.println(reply);
                        //System.out.println((((String)reply)));
                    } else if (splittedstr[0].equals("checkEventLogs")) {
                        reply = model.checkEventLogs(splittedstr[1], splittedstr[2]);
                        out.println(reply);
                        //System.out.println((((String)reply)));
                    } else if (splittedstr[0].equals("checkErrorLogs")) {
                        reply = model.checkErrorLogs(splittedstr[1], splittedstr[2]);
                        out.println(reply);
                        //System.out.println((((String)reply)));
                    } else if (splittedstr[0].equals("getCoachs")) {
                        reply = model.getCoachs();
                        out.println(reply);
                    } else if (splittedstr[0].equals("getPlayers")) {
                        reply = model.getPlayers();
                        out.println(reply);
                    } else if (splittedstr[0].equals("getOwners")) {
                        reply = model.getOwners();
                        out.println(reply);
                    } else if (splittedstr[0].equals("getManagers")) {
                        reply = model.getManagers();
                        out.println(reply);
                    } else if (splittedstr[0].equals("addPlayerToTeam")) {
                        reply = model.addPlayerToTeam(splittedstr[1], splittedstr[2], splittedstr[3]);
                        if(((String)reply).contains("ALERT")){
                            String []split=((String)reply).split(",,,,");
                            out.println(split[0]);
                            sendAllClients(split[1]);
                        }
                        else {
                            out.println(reply);
                        }
                    } else if (splittedstr[0].equals("addCoachToTeam")) {
                        reply = model.addCoachToTeam(splittedstr[1], splittedstr[2], splittedstr[3]);
                        if(((String)reply).contains("ALERT")){
                            String []split=((String)reply).split(",,,,");
                            out.println(split[0]);
                            sendAllClients(split[1]);
                        }
                        else {
                            out.println(reply);
                        }
                    } else if (splittedstr[0].equals("addOwnerToTeam")) {
                        reply = model.addOwnerToTeam(splittedstr[1], splittedstr[2]);
                        if(((String)reply).contains("ALERT")){
                            String []split=((String)reply).split(",,,,");
                            out.println(split[0]);
                            sendAllClients(split[1]);
                        }
                        else {
                            out.println(reply);
                        }
                    } else if (splittedstr[0].equals("addManagerToTeam")) {
                        reply = model.addManagerToTeam(splittedstr[1], splittedstr[2], splittedstr[3], splittedstr[4], splittedstr[5], splittedstr[6]);
                        if(((String)reply).contains("ALERT")){
                            String []split=((String)reply).split(",,,,");
                            out.println(split[0]);
                            sendAllClients(split[1]);
                        }
                        else {
                            out.println(reply);
                        }
                    } else if (splittedstr[0].equals("getTeamAssets")) {
                        reply = model.getTeamAssets(splittedstr[1]);
                        out.println(reply);
                    } else if (splittedstr[0].equals("removeAsset")) {
                        reply = model.removeAsset(splittedstr[1], splittedstr[2]);
                        if(((String)reply).contains("ALERT")){
                            String []split=((String)reply).split(",,,,");
                            out.println(split[0]);
                            sendAllClients(split[1]);
                        }
                        else {
                            out.println(reply);
                        }
                    }
                    else if (splittedstr[0].equals("addTeamToLeagueRequest")) {
                        reply = model.addTeamToLeagueRequest(splittedstr[1], splittedstr[2]);
                        out.println(reply);
                    } else if(splittedstr[0].equals("getAlerts")) {
                        reply=model.getAlerts(splittedstr[1]);
                        out.println(reply);
                    } else if(splittedstr[0].equals("subscribe")) {
                        reply=model.Subscribe(splittedstr[1]);
                        out.println(reply);
                    } else if(splittedstr[0].equals("unsubscribe")) {
                        reply=model.Unsubscribe(splittedstr[1]);
                        out.println(reply);
                    }

//                else if(number.equals("3"))
//                {
//                    reply = "three";
//                }

                    //referee
                    else if (splittedstr[0].equals("getRefReqs")) {
                        reply = model.getRefReqs(splittedstr[1]);
                        out.println(reply);
                    } else if (splittedstr[0].equals("refApprovesToJudge")) {
                        String request = "";
                        reply = model.refApprovesToJudge(splittedstr[1], splittedstr[2]);
                        out.println(reply);
                    }

                    /**
                     * Association
                     */

                    else if (splittedstr[0].equals("checkIfSeasonExist")) {
                        reply = model.checkIfSeasonExist(splittedstr[1]);
                        out.println(reply);
                    } else if (splittedstr[0].equals("addSeason")) {
                        reply = model.addSeason(splittedstr[1], splittedstr[2]);
                        out.println(reply);
                    } else if (splittedstr[0].equals("getAllReferees")) {
                        reply = model.getAllReferees();
                        out.println(reply);
                    } else if (splittedstr[0].equals("inviteRefereeToJudge")) {
                        String assoUser = splittedstr[1];
                        String refUser = splittedstr[2];
                        reply = model.inviteRefereeToJudge(assoUser, refUser);
                        if(((String)reply).contains("ALERT")){
                            String []split=((String)reply).split(",,,,");
                            out.println(split[0]);
                            sendAllClients(split[1]);
                        }
                        else {
                            out.println(reply);
                        }
                    } else if (splittedstr[0].equals("getTeamReqs")) {
                        String assoUser = splittedstr[1];
                        reply = model.getTeamReqs(assoUser);
                        out.println(reply);
                    } else if (splittedstr[0].equals("checkTeamRegistration")) {
                        reply = model.checkTeamRegistration(splittedstr[1],splittedstr[2]);
                        if(((String)reply).contains("ALERT")){
                            String []split=((String)reply).split(",,,,");
                            out.println(split[0]);
                            sendAllClients(split[1]);
                        }
                        else {
                            out.println(reply);
                        }
                    } else if (splittedstr[0].equals("addLeagueToDB")) {
                        reply = model.addLeagueToDB(splittedstr[1], splittedstr[2], splittedstr[3], splittedstr[4], splittedstr[5],
                                splittedstr[6], splittedstr[7], splittedstr[8], splittedstr[9]);
                        out.println(reply);
                    } else if (splittedstr[0].equals("getCurrentSeason")) {
                        reply = model.getCurrentSeason();
                        out.println(reply);
                    } else if (splittedstr[0].equals("isLeagueExist")) {
                        reply = model.isLeagueExist(splittedstr[1]);
                        out.println(reply);
                    } else if (splittedstr[0].equals("showLeaguesInSeason")) {
                        reply = model.showLeaguesInSeason();
                        out.println(reply);
                    } else if (splittedstr[0].equals("showAllRefs")) {
                        reply = model.showAllRefs();
                        out.println(reply);
                    } else if (splittedstr[0].equals("addRefToLeague")) {
                        reply = model.addRefToLeague(splittedstr[1], splittedstr[2]);
                        if(((String)reply).contains("ALERT")){
                            String []split=((String)reply).split(",,,,");
                            out.println(split[0]);
                            sendAllClients(split[1]);
                        }
                        else {
                            out.println(reply);
                        }
                    } else if (splittedstr[0].equals("changePointsForLeague")) {
                        reply = model.changePointsForLeague(splittedstr[1], splittedstr[2], splittedstr[3], splittedstr[4]
                                , splittedstr[5], splittedstr[6]);
                        out.println(reply);
                    } else if (splittedstr[0].equals("changeRoundsForLeague")) {
                        reply = model.changeRoundsForLeague(splittedstr[1], splittedstr[2]);
                        out.println(reply);
                    } else if (splittedstr[0].equals("createScheduleToLeague")) {
                        reply = model.createScheduleToLeague(splittedstr[1]);
                        out.println(reply);
                    }
                    else if (splittedstr[0].equals("getAllRefereeMatches")) {
                        reply = model.getAllRefereeMatches(splittedstr[1]);
                        out.println(reply);
                    }else if(splittedstr[0].equals("getPlayersToManageGame")){
                        reply = model.getPlayersToManageGame(splittedstr[1],splittedstr[2]);
                        out.println(reply);
                    }
                    else if( splittedstr[0].equals("addGoalEvent") ){
                        reply = model.addGoalEvent(splittedstr[1],splittedstr[2],splittedstr[3],splittedstr[4],splittedstr[5],splittedstr[6]);
                        if(((String)reply).contains("ALERT")){
                            String []split=((String)reply).split(",,,,");
                            out.println(split[0]);
                            sendAllClients(split[1]);
                        }
                        else {
                            out.println(reply);
                        }
                    }
                    else if( splittedstr[0].equals("addOffsideEvent") ){
                        reply = model.addOffsideFoulYellowRedInjuryEvent(splittedstr[1],splittedstr[2],splittedstr[3],splittedstr[4],splittedstr[5],splittedstr[6]);
                        if(((String)reply).contains("ALERT")){
                            String []split=((String)reply).split(",,,,");
                            out.println(split[0]);
                            sendAllClients(split[1]);
                        }
                        else {
                            out.println(reply);
                        }
                    }
                    else if( splittedstr[0].equals("addFoulEvent") ){
                        reply = model.addOffsideFoulYellowRedInjuryEvent(splittedstr[1],splittedstr[2],splittedstr[3],splittedstr[4],splittedstr[5],splittedstr[6]);
                        if(((String)reply).contains("ALERT")){
                            String []split=((String)reply).split(",,,,");
                            out.println(split[0]);
                            sendAllClients(split[1]);
                        }
                        else {
                            out.println(reply);
                        }
                    }
                    else if( splittedstr[0].equals("addYellowEvent") ){
                        reply = model.addOffsideFoulYellowRedInjuryEvent(splittedstr[1],splittedstr[2],splittedstr[3],splittedstr[4],splittedstr[5],splittedstr[6]);
                        if(((String)reply).contains("ALERT")){
                            String []split=((String)reply).split(",,,,");
                            out.println(split[0]);
                            sendAllClients(split[1]);
                        }
                        else {
                            out.println(reply);
                        }
                    }
                    else if( splittedstr[0].equals("addRedEvent") ){
                        reply = model.addOffsideFoulYellowRedInjuryEvent(splittedstr[1],splittedstr[2],splittedstr[3],splittedstr[4],splittedstr[5],splittedstr[6]);
                        if(((String)reply).contains("ALERT")){
                            String []split=((String)reply).split(",,,,");
                            out.println(split[0]);
                            sendAllClients(split[1]);
                        }
                        else {
                            out.println(reply);
                        }
                    }
                    else if( splittedstr[0].equals("addInjuryEvent") ){
                        reply = model.addOffsideFoulYellowRedInjuryEvent(splittedstr[1],splittedstr[2],splittedstr[3],splittedstr[4],splittedstr[5],splittedstr[6]);
                        if(((String)reply).contains("ALERT")){
                            String []split=((String)reply).split(",,,,");
                            out.println(split[0]);
                            sendAllClients(split[1]);
                        }
                        else {
                            out.println(reply);
                        }
                    }
                    else if(splittedstr[0].equals("addSubstituteEvent")){
                        reply = model.addSubstituteEvent(splittedstr[1],splittedstr[2],splittedstr[3],splittedstr[4],splittedstr[5],splittedstr[6]);
                        if(((String)reply).contains("ALERT")){
                            String []split=((String)reply).split(",,,,");
                            out.println(split[0]);
                            sendAllClients(split[1]);
                        }
                        else {
                            out.println(reply);
                        }
                    }

                }else{
                    break;
                }
            }
        } catch (SocketException e) {
            if (e.toString().contains("Socket closed") || e.toString().contains("Connection reset")
                    || e.toString().contains("Broken pipe")) {
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(in!=null)
                    in.close();
                if(out!=null)
                    out.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendAllClients(String content){
        for (ClientHandler c:clients) {
            c.out.println(content);
        }
    }
    public static String[] parse(String str){
        String[] splitted=str.split(":");
        return splitted;
    }
}

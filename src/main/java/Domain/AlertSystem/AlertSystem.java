package Domain.AlertSystem;

import Data.SystemDB.DB;
import Data.SystemDB.UserDaoMdb;
import Domain.AssociationManagement.Match;
import Domain.ClubManagement.TeamInfo;
import Domain.User.*;

//dd
import java.util.ArrayList;
//Singleton.
public class AlertSystem {

    private static AlertSystem alertSystem=null;
    private UserDaoMdb DB;

    private AlertSystem() {
        this.DB = UserDaoMdb.getInstance();
    }

    public static AlertSystem getInstance() {
        if (alertSystem == null) {
            alertSystem = new AlertSystem();
        }
        return alertSystem;
    }

    /**
     * save alert in database
     * =================================
     * ALERT_CODE:
     * *oct-open close team.
     * *dtm- delete team member.
     * *ctc- close team completely.
     * *nr- nominate referee
     * =================================
     * KEYWORD:
     * *name of team
     * *name of user
     * =================================
     */
    public void sendAlert(String alertContent, String kewWord, String alertCode){
        ArrayList<String> addressee=new ArrayList<String>();
        if(alertCode.equals("oct")){
            addressee=getManageTeamMembers(kewWord);
            ArrayList<SystemManager> systemManagers=DB.getAllSystemManagers();
            for (SystemManager sysM:systemManagers) {
                if(!addressee.contains(sysM.getUserName())){
                    addressee.add(sysM.getUserName());
                }
            }
            for (String user: addressee) {
                DB.addUserAlert(user,"TeamAlert",alertContent,false);
            }
        }
        else if(alertCode.equals("dtm")){
            addressee.add(kewWord);
            for (String user: addressee) {
                DB.addUserAlert(user,"TeamAlert",alertContent,false);
            }
        }
        else if(alertCode.equals("ctc")) {
            addressee=getManageTeamMembers(kewWord);
            for (String user: addressee) {
                DB.addUserAlert(user,"TeamAlert",alertContent,false);
            }
        }
        else if(alertCode.equals("nr")){
            addressee.add(kewWord);
            for (String user: addressee) {
                DB.addUserAlert(user,"NominateRefereeAlert",alertContent,false);
            }
        }

    }

    /**
     * save alert in database
     * =================================
     * ALERT_CODE:
     * *ms-match score.
     * *b- budget.
     * *ta- team approval
     * =================================
     *
     */
    public void sendAlert(String alertContent, String alertCode){
        ArrayList<String> addressee=new ArrayList<String>();
        if(alertCode.equals("b")){
            ArrayList<AssociationUser> associationUsers=DB.getAllAssociationUsers();
            for (AssociationUser assU:associationUsers) {
                addressee.add(assU.getUserName());
            }
            for (String user: addressee) {
                DB.addUserAlert(user,"BudgetAlert",alertContent,false);
            }
        }
        else if(alertCode.equals("ms")){
            //add all users that subscribe to alert system.
            addressee= DB.getAllAssignUsersToAlerts();
            for (String user: addressee) {
                DB.addUserAlert(user,"MatchAlert",alertContent,false);
            }
        }
        else if(alertCode.equals("ta")){
            ArrayList<AssociationUser> associationUsers=DB.getAllAssociationUsers();
            for (AssociationUser assU:associationUsers) {
                addressee.add(assU.getUserName());
            }
            for (String user: addressee) {
                DB.addUserAlert(user,"TeamApprovalAlert",alertContent,false);
            }
        }


    }

    /**
     *save alert in database.
     * alerts about change location or time of match.
     */
    public void sendAlert(String alertContent, Match match){
        ArrayList<String> addressee=new ArrayList<String>();
        //add all users that subscribe to alert system.
        addressee=DB.getAllAssignUsersToAlerts();
        if(!addressee.contains(match.getMainRef().getUserName())) {
            addressee.add(match.getMainRef().getUserName());
        }
        for (Referee ref:match.getSideRefs()) {
            if(!addressee.contains(ref.getUserName())){
                addressee.add(ref.getUserName());
            }
        }
        for (String user: addressee) {
            DB.addUserAlert(user,"MatchAlert",alertContent,false);
        }
    }

    public void Subscribe(String username){
        DB.updateUserDetails(username,"true","users","AssignToAlerts");
    }

    public void Unsubscribe(String username){
        DB.updateUserDetails(username,"false","users","AssignToAlerts");
    }






    /**
     * for alerts about open and close team
     * @param teamName
     * @return
     */
    private ArrayList<String> getManageTeamMembers(String teamName){
        ArrayList<String> ans=new ArrayList<String>();

        ArrayList<String> allTeamManagers= DB.getTeamManagers(teamName);
        for (String manager :allTeamManagers) {
            if(!ans.contains(manager)){
                ans.add(manager);
            }
        }
        ArrayList<String> allTeamOwners = DB.getTeamOwners(teamName);
        for (String owner :allTeamOwners) {
            if(!ans.contains(owner)){
                ans.add(owner);
            }
        }
        return ans;
    }

    private ArrayList<String> mergeArray(ArrayList<String> arr1, ArrayList<String> arr2){
        for (String user :arr1) {
            if(!arr2.contains((user))){
                arr2.add(user);
            }
        }
        return arr2;
    }
}


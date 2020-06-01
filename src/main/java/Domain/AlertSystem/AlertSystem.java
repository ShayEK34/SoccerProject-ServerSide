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


    private AlertSystem() {

    }

    public static AlertSystem getInstance() {
        if (alertSystem == null) {
            alertSystem = new AlertSystem();
        }
        return alertSystem;
    }

    /**
     * =================================
     * ALERT
     * delete team.
     * =================================
     * @param team
     * @return
     */
    public ArrayList<String> getAllAddressee(TeamInfo team){
        ArrayList<String> addressee=getManageTeamMembers(team);
        return addressee;
    }

    /**
     * =================================
     * ALERT
     * open or close team.
     * =================================
     * @param team
     * @return
     */
    public ArrayList<String> getAllAddressee(TeamInfo team, ArrayList<SystemManager> systemManagers){
        ArrayList<String> addressee=getManageTeamMembers(team);
        for (SystemManager sysM:systemManagers) {
            if(!addressee.contains(sysM.getUserName())){
                addressee.add(sysM.getUserName());
            }
        }
        return addressee;
    }

    /**
     * =================================
     * ALERT
     * delete user from team.
     * =================================
     * @return
     */
    public ArrayList<String> getAllAddressee(Fan user, ArrayList<String> users){
        ArrayList<String> addressee=users;
        if(!addressee.contains(user.getUserName())){
            addressee.add(user.getUserName());
        }
        return addressee;
    }

    /**
     * =================================
     * ALERT
     * about Match.
     * =================================
     * @return
     */
    public ArrayList<String> getAllAddressee(Match match){
        ArrayList<String> addressee=new ArrayList<String>();
        addressee.add(match.getMainRef().getUserName());
        for (Referee referee:match.getSideRefs()) {
            if(!addressee.contains(referee.getUserName())){
                addressee.add(referee.getUserName());
            }
        }
        return addressee;
    }










    /**
     * for alerts about open and close team
     * @param team
     * @return
     */
    private ArrayList<String> getManageTeamMembers(TeamInfo team){
        ArrayList<String> ans=new ArrayList<String>();

        for (OwnerInterface tm:team.getTeamOwners()) {
            if(!ans.contains(((TeamMember)tm).getUserName())){
                ans.add(((TeamMember)tm).getUserName());
            }
        }
        for (TeamManagerInterface tm:team.getTeamManagers()) {
            if(!ans.contains(((TeamMember)tm).getUserName())){
                ans.add(((TeamMember)tm).getUserName());
            }
        }
        return ans;
    }

//    private ArrayList<String> mergeArray(ArrayList<String> arr1, ArrayList<String> arr2){
//        for (String user :arr1) {
//            if(!arr2.contains((user))){
//                arr2.add(user);
//            }
//        }
//        return arr2;
//    }
}
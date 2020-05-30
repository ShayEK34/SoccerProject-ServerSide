package Domain.User;

import Data.SystemDB.DataBaseInterface;
import Data.SystemDB.UserDaoMdb;
import Domain.ClubManagement.TeamInfo;
import Data.SystemDB.DB;

/**
 * system manager in our system that responsible for all the system managing
 */
public class SystemManager extends Fan  {

    public enum VerificationCode{Referee, Coach, Association,Owner, Player, TeamManager,SystemManager};

    //private SystemEvents sysEvents;
    //private DataBaseInterface database = DB.getInstance();

    /**
     * Constructor
     * @param firstName
     * @param lastName
     * @param userName
     * @param password
     * @param occupation
     * @param birthday
     * @param email
     */
    public SystemManager(String firstName, String lastName, String userName, String password, String occupation, String birthday, String email) {
        super(firstName, lastName, userName, password, occupation, birthday, email);
    }

    /**
     * This function closes the team.
     * @param team
     */
    public void closeTeamCompletely(TeamInfo team){
        team.setTeamActiveStatus(false);
    }

    /**
     * This function open from start close team.
     * @param team
     */
    public void openTeamFromStart(TeamInfo team){
        team.setTeamActiveStatus(true);
    }

    /**
     * This function delete team from DB.
     * @param team
     */
    public void deleteTeam(TeamInfo team){
        DB.getInstance().removeTeam(team.getTeamName());
    }

    /**
     * removes user account from the system
     * @param username
     */
    public void RemoveAccount(Fan username){
        DB.getInstance().removeUser(username.getUserName());
        //System.out.println(username.getUserName()+" removed");
    }


    /**
     * This function create new user in accordance his code.
     * @param firstName
     * @param lastName
     * @param userName
     * @param password
     * @param occupation
     * @param birthday
     * @param email
     * @param verificationCode
     */
    public static void addNewUser(String firstName,String lastName,String userName,String password,String occupation,String birthday,String email,String verificationCode,String role){
        if(userName.equals("") || password.equals("")){
            throw new IllegalArgumentException("You Must enter user name and password");
        }
        if(DB.getInstance().getUsers().containsKey(userName)){
            throw new IllegalArgumentException("Service.User already exist");
        }
        DB.getInstance().addUser(firstName, lastName, userName, password, occupation, birthday, email, verificationCode, role);
//        if(verificationCode.equals("Referee")){
//            Referee ref = new Referee(firstName,lastName,userName,password,occupation,birthday,email, Referee.Role.main);
//            ref.setRefereeRole(role);
//            DB.getInstance().addUser(firstName, lastName, userName, password, occupation, birthday, email, verificationCode, role);
//            DB.getInstance().getUsers().put(userName,ref);
//        }
//        else if(verificationCode.equals("Coach")){
//            TeamMember newCouch=new TeamMember(firstName,lastName,userName,password,occupation,birthday,email,null);
//            newCouch.setMemberIdentity(true,false,false,false,VerificationCode.Coach.toString());
//            newCouch.setTeamRole(role);
//            DB.getInstance().getUsers().put(newCouch.getUserName() ,newCouch);
//        }
//        else if(verificationCode.equals("Association")){
//            DB.getInstance().getUsers().put(userName, new AssociationUser(firstName,lastName,userName,password,occupation,birthday,email));
//        }
//        else if(verificationCode.equals("Owner")){
//            TeamMember newOwner=new TeamMember(firstName,lastName,userName,password,occupation,birthday,email,null);
//            newOwner.setMemberIdentity(false,false,true,false,VerificationCode.Owner.toString());
//            DB.getInstance().getUsers().put(newOwner.getUserName() ,newOwner);
//        }
//        else if(verificationCode.equals("Player")){
//            TeamMember newPlayer=new TeamMember(firstName,lastName,userName,password,occupation,birthday,email,null);
//            newPlayer.setMemberIdentity(false,true,false,false,VerificationCode.Player.toString());
//            newPlayer.setRoleOnCourt(role);
//            DB.getInstance().getUsers().put(newPlayer.getUserName() ,newPlayer);
//        }
//        else if(verificationCode.equals("TeamManager")){
//            TeamMember newTeamManager=new TeamMember(firstName,lastName,userName,password,occupation,birthday,email,null);
//            newTeamManager.setMemberIdentity(false,false,false,true,VerificationCode.TeamManager.toString());
//            DB.getInstance().getUsers().put(newTeamManager.getUserName() ,newTeamManager);
//        }
//        else{
//            DB.getInstance().getUsers().put(userName, new Fan (firstName,lastName,userName,password,occupation,birthday,email));
//        }
    }

    public void addNewUserToDB(String firstName,String lastName,String userName,String password,String occupation,String birthday,String email,String verificationCode, String role){
        if(userName.equals("") || password.equals("")){
            throw new IllegalArgumentException("You Must enter user name and password");
        }
        if(UserDaoMdb.getInstance().isUserExist(userName)){
            throw new IllegalArgumentException("Service.User already exist");
        }
        else{
            UserDaoMdb.getInstance().addUser(firstName,lastName,userName,password,occupation,birthday,email,verificationCode,role);
        }
    }

    /**
     * This function return user from DB in accordance his username and his password.
     * @param pass
     * @param userName
     * @return
     */
    public static Fan checkUser(String pass, String userName){
        if(userName==null || pass==null || pass.equals("") || userName.equals("") ){
            throw new IllegalArgumentException("PLEASE FILL ALL THE FILED");
        }
        Fan user = DB.getInstance().getUser(userName);
        if(user==null){
            throw new IllegalArgumentException("Service.User doesn't exist");
        }
        if(!user.getPassword().equals(pass)){
            throw new IllegalArgumentException("Wrong password");
        }
        return user;
    }

    /**
     * This function return user from DB in accordance his userName
     * @param userName
     * @return
     */
    public static Fan search(String userName){
        return DB.getInstance().getUser(userName);
    }



    /**
     * This function add new complain to the Complain system.
     * @param complain
     */
    public static void addUserComplain(String complain ,String username){
        DB.getInstance().getComplaintSys().addComplain(complain , username);
    }

    public void answerForUserComplain(int index, String answer){
        DB.getInstance().getComplaintSys().answerComplain(index, answer);
    }

    public String showAllComplain(){
        return DB.getInstance().getComplaintSys().showAllComplains();
    }

    public String showUnAnswerComplain(){
        return DB.getInstance().getComplaintSys().showUnAnswerComplains();
    }

//    /**
//     * This function get complain from the Complain system in accordance complain's index.
//     * @param index
//     * @return
//     */
//    public static String getComplain(int index){
//        return DB.getInstance().getComplaintSys().getComplains().get(index);
//    }


}

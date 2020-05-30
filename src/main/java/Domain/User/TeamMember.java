package Domain.User;
import Domain.BudgetControl.Transaction;
import Domain.ClubManagement.Court;
import Domain.ClubManagement.TeamInfo;
import Domain.InformationPage.TeamMemberInformationPage;
import java.util.ArrayList;

/**
 *Team member in the system that can be owner, team manager, player or coach.
 */
public class TeamMember extends Fan implements CoachInterface,PlayerInterface,TeamManagerInterface,OwnerInterface  {

    private TeamInfo team;
    private boolean isOwner;
    private boolean isPlayer ;
    private boolean isCoach;
    private boolean isTeamManager;
    private TeamMemberInformationPage infoPage;
    private boolean CoachPermission;
    private boolean PlayerPermission;
    private boolean CourtPermission;
    private boolean OwnerPermission;
    private boolean TeamManagerPermission;
    private String Preparation;//no need
    private String TeamRole;//coachOccupation
    private String RoleOnCourt;//playerRoleOnCourt
    private ArrayList<TeamMember> selfOwners=new ArrayList<TeamMember>();
    private ArrayList<TeamMember> selfTeamManager=new ArrayList<TeamMember>();
    private ArrayList<TeamMember> selfPlayer=new ArrayList<TeamMember>();
    private ArrayList<TeamMember> selfCoach=new ArrayList<TeamMember>();

    /**
     *Constructor
     * @param firstName
     * @param lastName
     * @param userName
     * @param password
     * @param occupation
     * @param birthday
     * @param email
     * @param team
     */
    public TeamMember(String firstName, String lastName, String userName, String password, String occupation, String birthday, String email, TeamInfo team) {
        super(firstName, lastName, userName, password, occupation, birthday, email);
        this.team=team;
        this.isOwner=false;
        this.isCoach=false;
        this.isPlayer=false;
        this.isTeamManager=false;
        this.CoachPermission=false;
        this.OwnerPermission = false;
        this.PlayerPermission=false;
        this.CourtPermission=false;
        this.TeamManagerPermission=false;
        this.Preparation="";
        this.TeamRole="";
        this.RoleOnCourt="";
        this.infoPage=new TeamMemberInformationPage(this);
    }

    /**
     * edit all the coach information
     * @param firstName
     * @param lastName
     * @param password
     * @param occupation
     * @param birthday
     * @param email
     */
    //Coach action
    @Override
    public void EditCoachInfo(String firstName, String lastName, String password, String occupation, String birthday, String email) {
        if(this.isCoach){
            this.setFirstName(firstName);
            this.setLastName(lastName);
            this.setPassword(password);
            this.setOccupation(occupation);
            this.setBirthday(birthday);
            this.setEmail(email);
            System.out.println("Edit Coach Info succeed!");
        }
        else {
            throw new IllegalArgumentException("The action is illegal");
        }
    }

    /**
     *upload content information of a player or coach to personal pages.
     * @param content
     */
    //Coach and Player action
    @Override
    public void UploadContentInfo(String content) {
        infoPage.addPost(content);
    }

    /**
     * edit the player user information
     * @param firstName
     * @param lastName
     * @param password
     * @param occupation
     * @param birthday
     * @param email
     */
    //Player action
    @Override
    public void EditPlayerInfo(String firstName, String lastName, String password, String occupation, String birthday, String email) {
        if(this.isPlayer){
            this.setFirstName(firstName);
            this.setLastName(lastName);
            this.setPassword(password);
            this.setOccupation(occupation);
            this.setBirthday(birthday);
            this.setEmail(email);
            System.out.println("Edit Player Info succeed!");
        }
        else {
            throw new IllegalArgumentException("The action is illegal");
        }
    }

    /**
     * add new coach to the system
     * @param coach
     * @param prep
     * @param teamRole
     */
    @Override
    public boolean AddCoach(TeamMember coach, String prep, String teamRole) {
        boolean ans = false;
        if (this.isOwner == true || (this.isTeamManager == true && this.CoachPermission == true)) {
            if (coach != null && coach instanceof TeamMember) {
                try {
                    if (coach.isCoach && !coach.isPlayer && !coach.isTeamManager) {
                        if (coach.getTeam() == null) {
                            coach.team = this.team;
                            this.getTeam().addCoach(coach);
                            coach.Preparation = prep;
                            coach.TeamRole = teamRole;
                            this.selfCoach.add(coach);
                            ans = true;
                        }
                        else {
                            System.out.println("The User already part of the team");
                            ans = false;
                        }
                    }
                } catch (Exception e) {
                    throw new ClassCastException("The user isnt a coach");
                }
            }
            else {
                throw new IllegalArgumentException("This user name is not in the system");
            }
        }
        else{
            throw new IllegalArgumentException("The action is illegal");
        }
        return ans;
    }

    /**
     * add new player to the system
     * @param player
     * @param roleOnCourt
     */
    @Override
    public boolean AddPlayer(TeamMember player, String roleOnCourt) {
        boolean ans = false;
        if (this.isOwner == true || (this.isTeamManager == true && this.PlayerPermission == true)) {
            if (player != null && player instanceof TeamMember ) {
                try {
                    if (!player.isCoach && player.isPlayer && !player.isTeamManager && player.getTeam() == null) {
                        team.addPlayer(player);
                        player.team = this.team;
                        player.RoleOnCourt = roleOnCourt;
                        this.selfPlayer.add(player);

                        ans = true;
                    } else {
                        System.out.println("The User already part of the team");
                        ans = false;

                    }
                } catch (Exception e) {
                    throw new ClassCastException("The user isnt a player");
                }
            } else {
                throw new IllegalArgumentException("This user name is not in the system");
            }
        }
        else{
            throw new IllegalArgumentException("The action is illegal");
        }
        return ans;
    }

    /**
     * add new court to the system
     * @param newCourt
     */
    @Override
    public boolean AddCourt(Court newCourt) {
        boolean ans = false;
        if(this.isOwner==true || (this.isTeamManager==true)) {
            if(newCourt != null){
                this.team.addCourt(newCourt);
                System.out.println("Add Court succeed!");
                ans = true;
            }
//            else{
//                this.team.addCourt(court);
//                ans = true;
//            }
        }
        else{
            throw new IllegalArgumentException("The action is illegal");
        }
        return ans;
    }

    /**
     * remove existing coach from the system
     * @param coach
     */
    @Override
    public boolean RemoveCoach (TeamMember coach) {
        boolean ans = false;
        if (coach != null && coach instanceof TeamMember) {
            if (this.isOwner == true || (this.isTeamManager == true && this.CoachPermission == true)) {

                try {
                    if(team.removeCoachFromTeam(coach.getUserName())){
                        ans = true;
                    }
                } catch (Exception e) {
                    throw new ClassCastException("The user isnt a coach");
                }
            }
        }
        else {
            throw new IllegalArgumentException("This username is not in the system");
        }
        return ans;
    }

    /**
     * remove existing player from the system
     * @param player
     */
    @Override
    public boolean RemovePlayer(TeamMember player) {
        boolean ans = false;
        if (this.isOwner == true || (this.isTeamManager == true && this.PlayerPermission == true)) {
            if (player instanceof TeamMember) {
                try {

                    if(team.removePlayerFromTeam(player.getUserName())){
                        ans = true;
                    }
                    //this.RoleOnCourt = "";
                } catch (Exception e) {
                    throw new ClassCastException("The user isnt a player");
                }
            }
        }
        else {
            throw new IllegalArgumentException("This user name is not in the system");
        }
        return ans;
    }

    /**
     * remove existing court from the system
     * @param court
     */
    @Override
    public boolean RemoveCourt(Court court) {
        boolean ans = false;
        if(this.isOwner==true || (this.isTeamManager==true)) {
            if (court != null) {
                team.removeCourt(court);
                System.out.println("Remove Court "+court.getCourtName()+" succeed!");
                ans=true;
            } else {
                throw new IllegalArgumentException("This court is not in the system");
            }
        }
        else{
            throw new IllegalArgumentException("The action is illegal");
        }
        return ans;
    }

    /**
     * edit existing coach information
     * @param preparation
     * @param teamRole
     */
    @Override
    public void EditCoachInformation(TeamMember user, String preparation, String teamRole) {
        if(this.isOwner==true || (this.isTeamManager==true && this.CoachPermission==true) ) {
            if (user != null) {
                if(user instanceof TeamMember && ((TeamMember) user).isCoach==true) {
                    user.Preparation=preparation;
                    user.TeamRole=teamRole;
                    System.out.println("Edit Coach Information by owner/team manager succeed!");
                }
            }
        }
        else{
            throw new IllegalArgumentException("The action is illegal");
        }
    }

    /**
     * edit existing player information
     * @param roleOnCourt
     */
    @Override
    public void EditPlayerInformation(TeamMember user, String roleOnCourt) {
        if(this.isOwner==true || (this.isTeamManager==true && this.PlayerPermission==true)) {
            if (user != null) {
                if(user instanceof TeamMember && ((TeamMember) user).isPlayer==true)
                    user.RoleOnCourt = roleOnCourt;
                System.out.println("Edit Player Information by owner/team manager succeed!");
            }
            else{
                throw new IllegalArgumentException("The action is illegal");
            }
        }
        else{
            throw new IllegalArgumentException("The action is illegal");
        }
    }

    /**
     * edit existing court information
     * @param court
     * @param courtName
     * @param courtCapacity
     */
    @Override
    public void EditCourtInfo(Court court, String courtName, int courtCapacity) {
        if(this.isOwner==true || (this.isTeamManager==true && this.CoachPermission)) {
            court.setCourtName(courtName);
            court.setCourtCapacity(courtCapacity);
            System.out.println("Edit Court Info by owner/team manager succeed!");
        }
        else{
            throw new IllegalArgumentException("The action is illegal");
        }
    }

    /**
     * add new owner to the system
     * @param newOwner
     */
    @Override
    public boolean AddOwner(TeamMember newOwner) {
        boolean ans=false;
        if (newOwner != null && newOwner instanceof TeamMember) {
            try{
                if (this.isOwner) {
                    if (newOwner.getTeam() == null) {
                        team.addOwner(newOwner);
                        newOwner.team = this.team;
                        newOwner.CourtPermission = true;
                        newOwner.PlayerPermission = true;
                        newOwner.CoachPermission = true;
                        newOwner.TeamManagerPermission = true;
                        this.selfOwners.add(newOwner);
                        ans = true;
                    } else {
                        ans = false;
                    }
                }
            }
            catch (Exception e) {
                throw new ClassCastException("The user isnt a owner");
            }
        }
        else {
            throw new IllegalArgumentException("This user name is not in the system");
        }
        return ans;
    }

    /**
     * add new team manager to the system
     * @param user
     * @param playerP
     * @param coachP
     * @param teamMP
     */
    @Override
    public boolean AddTeamManager(TeamMember user, boolean ownerP, boolean playerP, boolean coachP, boolean teamMP) {
        boolean ans=false;
        if (this.isOwner==true || (this.isTeamManager == true && this.TeamManagerPermission == true)) {
            if (user != null && user instanceof TeamMember) {
                try {
                    if (!user.isCoach && !user.isPlayer && user.isTeamManager) {
                        if (user.getTeam() == null) {
                            user.CoachPermission = coachP;
                            user.PlayerPermission = playerP;
                            user.TeamManagerPermission = teamMP;
                            user.OwnerPermission = ownerP;
                            //user.setCourtPermission();
                            this.team.addManager(user);
                            user.team = this.team;
                            this.selfTeamManager.add(user);

                            System.out.println("Add TeamManager succeed!");
                            ans = true;
                        } else {
                            System.out.println("The user is already part of the team");
                            ans = false;
                        }
                    }
                }
                catch (Exception e){
                    throw new ClassCastException("The user isnt a team manager");
                }
            }
            else {
                throw new IllegalArgumentException("This user name is not in the system");
            }
        }
        else {
            throw new IllegalArgumentException("The action is illegal");
        }
        return ans;
    }

    /**
     * removes existing owner from the system
     * @param user
     */
    @Override
    public boolean RemoveOwner(TeamMember user) {
        boolean ans=false;
        if (this.isOwner == true) {
            if (user != null && user instanceof TeamMember) {
                try {
                    if (this.team.getTeamOwners().size() > 1) {
                        //removeAllRelatedUsers(owner);

                        if(this.team.removeOwnerFromTeam(user.getUserName())){
                            ans = true;
                            for (int i = 0; i < selfOwners.size(); i++) {
                                if (((TeamMember) selfOwners.get(i)).getUserName().equals(user.getUserName())) {
                                    this.selfOwners.remove(i);
                                }
                            }
                        }
                    } else {
                        System.out.println("The user is not nominate by: " + this.getUserName() +
                                " or the team must have at least one owner");
                        ans = false;
                    }
                } catch (Exception e) {
                    throw new ClassCastException("The user isnt a owner");
                }
            } else {
                throw new IllegalArgumentException("This user is not in the system");
            }
        }
        return ans;
    }

    /**
     * removes existing team manager from the system
     * @param teamManager
     */
    @Override
    public boolean RemoveTeamManager(TeamMember teamManager) {
        boolean ans=false;
        if (this.isOwner == true) {
            if (teamManager != null && teamManager instanceof TeamMember) {
                try {
                    //removeAllRelatedUsers(teamManager);

                    if(this.team.removeManagerFromTeam(teamManager.getUserName())){
                        ans = true;
                        for (int i = 0; i < selfTeamManager.size(); i++) {
                            if (((TeamMember) selfTeamManager.get(i)).getUserName().equals(teamManager.getUserName())) {
                                this.selfTeamManager.remove(i);
                            }
                        }
                    }

                } catch (Exception e) {
                    throw new ClassCastException("The user isnt a team manager");
                }
            }
        }
        else {
            throw new IllegalArgumentException("This user name is not in the system");
        }
        return ans;
    }

    /**
     * close team, changes its activation status
     */
    @Override
    public boolean CloseTeam() {
        if(this.isOwner==true){
            this.team.setTeamActiveStatus(false);
            System.out.println("Close Team succeed!");
            return true;
        }
        return false;
    }

    /**
     * open team, changes its activation status
     */
    @Override
    public boolean OpenTeam() {
        if(this.isOwner==true){
            this.team.setTeamActiveStatus(true);
            System.out.println("Open Team succeed!");
            return true;
        }
        return false;
    }

    /**
     * add income
     */
    @Override
    public boolean AddIncoming(double amount, String description) {
        if(isOwner==true) {
            if (amount>0) {
                Transaction tran = new Transaction(amount, description, "Income");
                this.team.getTeamSeasonBudget().addIncomeTransaction(tran);
                System.out.println("Add Incoming succeed!");
                return true;
            }
            else{
                throw new IllegalArgumentException("the amount cant be less then 0 or negative");
            }
        }
        else{
            throw new IllegalArgumentException("You are not the owner");
        }
    }

    /**
     * add expense
     */
    @Override
    public boolean AddExpense(double amount, String description){
        if(isOwner==true) {
            if (amount>0) {
                Transaction tran=new Transaction(amount, description, "Expense");
                this.team.getTeamSeasonBudget().addExpenseTransaction(tran);
                System.out.println("Add Expense succeed!");
                return true;
            }
            else{
                throw new IllegalArgumentException("the amount cant be less then 0 or negative");
            }
        }
        else{
            throw new IllegalArgumentException("You are not the owner");
        }
    }

    /**
     * removes all the users that is related to the user that we want to remove
     * @param user
     */
    private void removeAllRelatedUsers(TeamMember user){
        ArrayList<TeamMember> tmp1=new ArrayList<TeamMember>();
        ArrayList<TeamMember> tmp2=new ArrayList<TeamMember>();
        ArrayList<TeamMember> tmp3=new ArrayList<TeamMember>();
        ArrayList<TeamMember> tmp4=new ArrayList<TeamMember>();
        for (TeamMember u : user.selfCoach) {
            tmp1.add(u);
            //user.selfCoach.remove(u);
            user.RemoveCoach(u);
        }
        for (TeamMember u : user.selfOwners) {
            tmp2.add(u);
            //user.selfOwners.remove(u);
            user.RemoveOwner(u);
        }
        for (TeamMember u : user.selfPlayer) {
            tmp3.add(u);
            //user.selfPlayer.remove(u);
            user.RemovePlayer(u);
        }
        for (TeamMember u : user.selfTeamManager) {
            tmp4.add(u);
            //user.selfTeamManager.remove(u);
            user.RemoveTeamManager(u);
        }
        user.selfCoach.removeAll(tmp1);
        user.selfOwners.removeAll(tmp2);
        user.selfPlayer.removeAll(tmp3);
        user.selfTeamManager.removeAll(tmp4);
    }

    public boolean registerTheTeamToLeague(String leagueName){
        if(leagueName!=null){
            return AssociationUser.addTeamToLeague(team,leagueName);
        }
        return false;
    }

    public void setCoachPermission(boolean coachPermission) {
        CoachPermission = coachPermission;
    }

    public void setPlayerPermission(boolean playerPermission) {
        PlayerPermission = playerPermission;
    }

    public void setCourtPermission(boolean courtPermission) {
        CourtPermission = courtPermission;
    }

    public void setTeamManagerPermission(boolean teamManagerPermission) {
        TeamManagerPermission = teamManagerPermission;
    }

    public void setOwnerPermission(boolean ownerPermission) {
        OwnerPermission = ownerPermission;
    }

    public void setTeamRole(String teamRole) {
        TeamRole = teamRole;
    }

    public void setRoleOnCourt(String roleOnCourt) {
        RoleOnCourt = roleOnCourt;
    }

    /**
     * setter
     * @param team
     */
    public void setTeam(TeamInfo team) {
        this.team = team;
    }

    public void setMemberIdentity(boolean coach, boolean player,boolean owner,boolean teamManager, String verCode){
        if(verCode.equals("Coach")){
            this.isCoach=coach;
        }
        else if(verCode.equals("Owner")){
            this.isOwner=owner;
        }
        else if(verCode.equals("Player")){
            this.isPlayer=player;
        }
        else if(verCode.equals("TeamManager")){
            this.isTeamManager=teamManager;
        }
        else{
            throw new IllegalArgumentException("The user can't change this field");
        }
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public void setPlayer(boolean player) {
        isPlayer = player;
    }

    public void setCoach(boolean coach) {
        isCoach = coach;
    }

    public void setTeamManager(boolean teamManager) {
        isTeamManager = teamManager;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public boolean isCoach() {
        return isCoach;
    }

    public boolean isTeamManager() {
        return isTeamManager;
    }

    public TeamMemberInformationPage getInfoPage() {
        return infoPage;
    }

    public String getPreparation() {
        return Preparation;
    }

    public String getTeamRole() {
        return TeamRole;
    }

    public String getRoleOnCourt() {
        return RoleOnCourt;
    }

    public TeamInfo getTeam() {

        return team;
    }

    public void createNewTeam(String teamName, int establishYear, boolean teamActiveStatus, String city){
        if(this.isOwner && this.team==null){
            if(teamName==null || city==null){
                throw new IllegalArgumentException("Illegal input");
            }
            TeamInfo team=new TeamInfo(teamName,establishYear, teamActiveStatus, city,this);
            this.team=team;
        }
        else{
            throw new IllegalArgumentException("Illegal Action");
        }
    }
}


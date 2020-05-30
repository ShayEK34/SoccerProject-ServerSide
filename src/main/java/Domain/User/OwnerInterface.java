package Domain.User;

import Domain.ClubManagement.Court;

public interface OwnerInterface {
    boolean AddOwner(TeamMember user);
    boolean AddTeamManager(TeamMember user, boolean courtP, boolean playerP, boolean coachP, boolean teamMP);
    boolean AddCoach(TeamMember user, String prep, String teamRole);
    boolean AddPlayer(TeamMember user, String roleOnCourt);
    boolean AddCourt(Court newCourt);
    boolean RemoveOwner(TeamMember user);
    boolean RemoveTeamManager(TeamMember user);
    boolean RemoveCoach(TeamMember user);
    boolean RemovePlayer(TeamMember user);
    boolean RemoveCourt(Court court);
    //void EditTeamManagerInfo();
    void EditCoachInformation(TeamMember user, String preparation, String teamRole);
    void EditPlayerInformation(TeamMember user, String roleOnCourt);
    void EditCourtInfo(Court court, String courtName, int courtCapacity);
    boolean CloseTeam();
    boolean OpenTeam();
    boolean AddIncoming(double amount, String description) throws Exception;
    boolean AddExpense(double amount, String description) throws Exception;
}

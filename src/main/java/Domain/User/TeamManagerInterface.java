package Domain.User;

import Domain.ClubManagement.Court;

public interface TeamManagerInterface {
    boolean AddTeamManager(TeamMember user, boolean courtP, boolean playerP, boolean coachP, boolean teamMP);
    boolean AddCoach(TeamMember user, String prep, String teamRole);
    boolean AddPlayer(TeamMember user, String roleOnCourt);
    boolean AddCourt(Court newCourt);
    boolean RemoveTeamManager(TeamMember user);
    boolean RemoveCoach(TeamMember user);
    boolean RemovePlayer(TeamMember user);
    boolean RemoveCourt(Court court);
    //void EditTeamManagerInfo();
    void EditCoachInformation(TeamMember user, String preparation, String teamRole);
    void EditPlayerInformation(TeamMember user, String roleOnCourt);
    void EditCourtInfo(Court court, String courtName, int courtCapacity);
}


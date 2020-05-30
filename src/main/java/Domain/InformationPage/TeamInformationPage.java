package Domain.InformationPage;

import Domain.ClubManagement.TeamInfo;

public class TeamInformationPage extends InformationPage {

    private TeamInfo team;

    public TeamInformationPage(TeamInfo team) {
        super();
        this.team = team;
    }

    public TeamInfo getTeam() {
        return team;
    }

    public void setTeam(TeamInfo team) {
        this.team = team;
    }
}


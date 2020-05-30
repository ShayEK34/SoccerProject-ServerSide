package Domain.InformationPage;

import Domain.User.TeamMember;

//dgtheter
public class TeamMemberInformationPage extends InformationPage{
    private TeamMember user;

    public TeamMemberInformationPage(TeamMember user) {
        super();
        this.user = user;
    }

    public void setUser(TeamMember user) {this.user = user;}

    public TeamMember getUser() {return user;}

}


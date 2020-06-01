package Domain.AlertSystem;

public class TeamAlert extends A_AlertPop {

    public TeamAlert(String content) {
        super(content, "Team");
    }

    @Override
    public String showAlert() {
        String str=super.showAlert();
        return str;
    }
}


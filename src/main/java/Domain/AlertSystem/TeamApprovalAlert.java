package Domain.AlertSystem;

public class TeamApprovalAlert extends A_AlertPop  {

    public TeamApprovalAlert(String content) {
        super(content, "TeamApproval");
    }

    @Override
    public String showAlert() {
        String str= super.showAlert();
        return str;
    }
}


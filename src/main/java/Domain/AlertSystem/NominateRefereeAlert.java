package Domain.AlertSystem;

public class NominateRefereeAlert extends A_AlertPop {
    public NominateRefereeAlert(String content) {
        super(content, "NominateReferee");
    }

    @Override
    public String showAlert() {
        String str = super.showAlert();
        return str;

    }
}
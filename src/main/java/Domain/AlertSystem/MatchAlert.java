package Domain.AlertSystem;

public class MatchAlert  extends A_AlertPop{


    public MatchAlert(String content) {
        super(content, "Match");
    }

    @Override
    public String showAlert() {
        String str= super.showAlert();
        return str;
    }
}

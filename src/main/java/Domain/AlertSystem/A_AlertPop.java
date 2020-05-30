package Domain.AlertSystem;

public class A_AlertPop implements AlertPop {
    String content;
    String type;

    public A_AlertPop(String content, String type) {
        this.content = content;
        this.type=type;
    }

    @Override
    public String showAlert() {
        return content;
    }


    public String getType() {
        return type;
    }
}


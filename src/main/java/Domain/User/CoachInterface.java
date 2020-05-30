package Domain.User;

public interface CoachInterface {
    void EditCoachInfo(String firstName, String lastName, String password, String occupation, String birthday, String email);
    void UploadContentInfo(String content);
}

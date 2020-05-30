package Domain.User;

public interface PlayerInterface {
    void EditPlayerInfo(String firstName, String lastName, String password, String occupation, String birthday, String email);
    void UploadContentInfo(String content);
}

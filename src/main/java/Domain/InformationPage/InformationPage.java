package Domain.InformationPage;

import java.util.ArrayList;

public abstract class InformationPage  {
    private ArrayList<Post> postsPage;


    public InformationPage() {
        postsPage=new ArrayList<Post>();
    }

    public void addPost(String content){
        Post newPost = new Post(content);
        postsPage.add(newPost);
    }

    public ArrayList<Post> getPostsPage() {
        return postsPage;
    }
}

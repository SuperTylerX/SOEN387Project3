package dao;

import model.Post;

import java.sql.Connection;
import java.util.Set;

public class PostDAO {


    //    TODO: probably we need a service layer to handle the logic process, not in DAO...
    public Post createPost(String title, String content, int postAuthorID, int postAttachID) {

        Connection connection = DBConnection.getConnection();

        Post post = new Post(title, content, postAuthorID);

        // TODO: Update database

        // Should get the postID
        int postID = 0; // for example;
        post.setPostID(postID);

        // TODO: Update the attachment


        AttachmentDAO attachmentDAO = new AttachmentDAO();
        attachmentDAO.updateAttachmentID(postAttachID, postID);

        return post;
    }

    public boolean deletePost(int postID) {

        // TODO: delete a post(database), return ture if successful delete

        return true;
    }

    public boolean updatePost(int postID, String title, String content, int postAttachID) {

        // TODO: update a post(database)

        // TODO: update the associated attachment

        //return ture if successful update
        return true;
    }

    public Set<Post> ReadPosts(int pageNum) {

        return null;
    }
}

package dao;

import model.Attachment;
import model.Post;
import model.UserManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
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

        return post;
    }

    public boolean deletePost(int postID) {
        Connection connection = DBConnection.getConnection();
        try {
            String query = "delete from posts where post_id = ? ;";
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, postID);
            int i = ps.executeUpdate();
            return i == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkValidOwner(long userId, int postID) {
        Connection connection = DBConnection.getConnection();
        try {
            String query = "select * from posts where post_id = ? AND post_author_id = ?;";
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, postID);
            ps.setLong(2, userId);
            ResultSet rs = ps.executeQuery();
            int cnt=0;
            while (rs.next()){
                cnt++;
            }
            return cnt == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePost(int postID, String title, String content, int postAttachID) {

        // TODO: update a post(database)


        //return ture if successful update
        return true;
    }

    public ArrayList<Post> readPosts() {
        ArrayList<Post> posts = new ArrayList<>();
        Connection connection = DBConnection.getConnection();
        try {

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from posts,attachment where post_attach_id=attach_id order by post_modified_date desc limit 10");
            while (rs.next()) {
                Post p = new Post();
                p.setPostID(rs.getInt("post_id"));

                int authorId = rs.getInt("post_author_id");
                String username = UserManager.getInstance().getUserNameById(authorId);
                p.setPostAuthorID(authorId);
                p.setPostAuthorName(username);
                p.setPostContent(rs.getString("post_content"));
                p.setPostCreatedDate(rs.getLong("post_created_date"));
                p.setPostModifiedDate(rs.getLong("post_modified_date"));
                p.setPostTitle(rs.getString("post_title"));
                Attachment att = new Attachment();
                att.setAttachID(rs.getInt("attach_id"));
                att.setAttachName(rs.getString("attach_name"));
                p.setAttachment(att);
                posts.add(p);
            }
            return posts;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}

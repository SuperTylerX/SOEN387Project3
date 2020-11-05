package dao;

import config.AppConfig;
import model.Attachment;
import model.Post;
import model.UserManager;

import java.sql.*;
import java.util.ArrayList;

public class PostDAO {

    //    TODO: probably we need a service layer to handle the logic process, not in DAO...
    public int createPost(Post post, int attachID) {

        Connection connection = DBConnection.getConnection();

        try {
            String query = "INSERT INTO posts (post_title,post_content,post_author_id,post_created_date,post_modified_date,post_attach_id) VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, post.getPostTitle());
            ps.setString(2, post.getPostContent());
            ps.setInt(3, (int) post.getPostAuthorID());
            ps.setLong(4, post.getPostCreatedDate());
            ps.setLong(5, post.getPostModifiedDate());
            ps.setInt(6, attachID);

            int i = ps.executeUpdate();

            // get the postID
            if (i == 1) {

                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        post.setPostID(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Creating post failed, no PostID obtained.");
                    }
                }
            }

            // set the postID
            int postID = post.getPostID();
            post.setPostID(postID);

            return postID;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean deletePost(int postID) {
        Connection connection = DBConnection.getConnection();

        try {
            int j = 1;
            String query_get_post_id = "select post_attach_id from posts where post_id= ? ";
            PreparedStatement ps = connection.prepareStatement(query_get_post_id, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, postID);
            ResultSet rs = ps.executeQuery();
            int post_attach_id = -1;
            while (rs.next()) {
                post_attach_id = rs.getInt("post_attach_id");
            }
            //delete post
            String query = "delete from posts where post_id = ? ;";
            PreparedStatement ps1 = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps1.setInt(1, postID);
            int i = ps1.executeUpdate();
            //delete attachment
            if (post_attach_id != 0) {
                String del_att = "delete from attachment where attach_id = ? ;";
                PreparedStatement ps2 = connection.prepareStatement(del_att, Statement.RETURN_GENERATED_KEYS);
                ps2.setInt(1, post_attach_id);
                j = ps2.executeUpdate();
            }
            return i == 1 && j == 1;
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
            int cnt = 0;
            while (rs.next()) {
                cnt++;
            }
            return cnt == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePost(Post post, int postId, int attachId) {

        Connection connection = DBConnection.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE posts SET post_title=?,post_content=?,post_modified_date=?,post_attach_id=? WHERE post_id=?");

            ps.setString(1, post.getPostTitle());
            ps.setString(2, post.getPostContent());
            ps.setLong(3, post.getPostModifiedDate());
            ps.setInt(4, attachId);
            ps.setInt(5, postId);

            int i = ps.executeUpdate();

            if (i == 1) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    public ArrayList<Post> readPosts() {
        ArrayList<Post> posts = new ArrayList<>();
        Connection connection = DBConnection.getConnection();
        try {
            int postNum = Integer.parseInt(AppConfig.getInstance().POST_NUM);
            Statement stmt = connection.createStatement();
            String query = "select * from posts LEFT JOIN attachment on post_attach_id=attach_id order by post_modified_date desc limit ?;";
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, postNum);
            ResultSet rs = ps.executeQuery();

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

                if (rs.getInt("attach_id") != 0) {
                    Attachment att = new Attachment();
                    att.setAttachID(rs.getInt("attach_id"));
                    att.setAttachName(rs.getString("attach_name"));
                    p.setAttachment(att);
                }

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

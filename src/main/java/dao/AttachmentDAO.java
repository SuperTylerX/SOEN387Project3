package dao;

import model.Attachment;
import java.sql.*;
import java.io.*;

public class AttachmentDAO {

    public void createAttachment(String attachName,File file) {
        Attachment attachment = new Attachment(attachName);

        Connection conn = DBConnection.getConnection();
        String sql="INSERT INTO attachment(attach_name,attach_size,attach_mime,attach_file) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement ps=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,attachment.getAttachName());
            ps.setInt(2,(int)attachment.getAttachSize());
            ps.setString(3,attachment.getAttachMIME());
            InputStream inputStream=new FileInputStream(file);
            ps.setBlob(4,inputStream);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void updateAttachmentID(int attachID, int postID) {

    }

    public void deleteAttachmentID(int attachID) {

    }
}

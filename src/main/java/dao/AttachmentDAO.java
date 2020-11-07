package dao;

import model.Attachment;

import java.sql.*;

public class AttachmentDAO {

    public int createAttachment(Attachment attach) {

        Connection connection = DBConnection.getConnection();

        try {
            String query = "INSERT INTO attachment (attach_name,attach_size,attach_mime,attach_file) VALUES(?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, attach.getAttachName());
            ps.setLong(2, attach.getAttachSize());
            ps.setString(3, attach.getAttachMIME());
            Blob blob = connection.createBlob();
            blob.setBytes(1, attach.getFileContent());
            ps.setBlob(4, blob);

            int i = ps.executeUpdate();

            // get the attachID
            if (i == 1) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        attach.setAttachID(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Creating attachment failed, no attachID obtained.");
                    }
                }
            }
            return attach.getAttachID();

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

    public void updateAttachmentID(int attachID, int postID) {

    }

    public void deleteAttachmentID(int attachID) {

    }


    public Attachment readAttachment(int attachId) {

        Connection connection = DBConnection.getConnection();
        try {

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM attachment WHERE attach_id=" + attachId);

            Attachment attach = new Attachment();

            if (rs.next()) {
                attach.setAttachName(rs.getString("attach_name"));
                attach.setAttachSize(rs.getLong("attach_size"));
                attach.setAttachMIME(rs.getString("attach_mime"));
                attach.setFileContent(rs.getBytes("attach_file"));
            }
            return attach;
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

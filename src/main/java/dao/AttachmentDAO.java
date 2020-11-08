package dao;

import model.Attachment;

public class AttachmentDAO {

    public void createAttachment(String attachName) {
        Attachment attachment = new Attachment(attachName);

    }

    public void updateAttachmentID(int attachID, int postID) {

    }

    public void deleteAttachmentID(int attachID) {

    }
}

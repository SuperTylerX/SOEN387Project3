package model;

public class Attachment {

    private int attachID;
    private String attachName;
    private long attachSize;
    private String attachMIME;
    private byte[] fileContent;

    public Attachment() {
    }

    public int getAttachID() {
        return attachID;
    }

    public void setAttachID(int attachID) {
        this.attachID = attachID;
    }

    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }

    public long getAttachSize() {
        return attachSize;
    }

    public void setAttachSize(long attachSize) {
        this.attachSize = attachSize;
    }

    public String getAttachMIME() {
        return attachMIME;
    }

    public void setAttachMIME(String attachMIME) {
        this.attachMIME = attachMIME;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }
}

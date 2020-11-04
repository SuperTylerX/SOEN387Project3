package model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static config.AppConfig.ATTACH_DIR;

public class Attachment {

    private int attachID;
    private String attachName;
    private long attachSize;
    private String attachMIME;




    // Used for first created
    public Attachment(String attachName) {
        this.attachName = attachName;
        this.initAttach();
    }

    public Attachment(int attachID, String attachName, int attachSize, String attachMIME) {
        this.attachID = attachID;

        this.attachName = attachName;
        this.attachSize = attachSize;
        this.attachMIME = attachMIME;
    }

    public Attachment() {
    }

    private void initAttach() {

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


}

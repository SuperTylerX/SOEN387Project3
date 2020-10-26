package model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static config.AppConfig.ATTACH_DIR;

public class Attachment {

    private int attachID;
    private int attachPostID;
    private String attachName;
    private long attachSize;
    private String attachMIME;

    private String attachURL;

    // Used for first created
    public Attachment(String attachName) {
        this.attachName = attachName;
        this.initAttach();
    }

    public Attachment(int attachID, int attachPostID, String attachName, int attachSize, String attachMIME) {
        this.attachID = attachID;
        this.attachPostID = attachPostID;
        this.attachName = attachName;
        this.attachSize = attachSize;
        this.attachMIME = attachMIME;
    }

    private void initAttach() {
        this.attachURL = ATTACH_DIR + this.attachName;

        try {
            this.attachMIME = Files.probeContentType(Paths.get(this.attachURL));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //KB
        this.attachSize= new File(attachURL).length();
    }


}

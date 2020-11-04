package model;

import java.util.Date;

public class Post {

    private int postID;
    private int postAuthorID;
    private String postAuthorName;
    private String postTitle;
    private String postContent;
    private long postCreatedDate;
    private long postModifiedDate;
    private Attachment attachment;


    public Post() {
    }

    //    This constructor used for first creating a post
    public Post(String postTitle, String postContent, int postAuthorID) {
        this.postAuthorID = postAuthorID;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postCreatedDate = new Date().getTime();
        this.postModifiedDate = this.postCreatedDate;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public int getPostAuthorID() {
        return postAuthorID;
    }

    public void setPostAuthorID(int postAuthorID) {
        this.postAuthorID = postAuthorID;
    }

    public String getPostAuthorName() {
        return postAuthorName;
    }

    public void setPostAuthorName(String postAuthorName) {
        this.postAuthorName = postAuthorName;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public long getPostCreatedDate() {
        return postCreatedDate;
    }

    public void setPostCreatedDate(long postCreatedDate) {
        this.postCreatedDate = postCreatedDate;
    }

    public long getPostModifiedDate() {
        return postModifiedDate;
    }

    public void setPostModifiedDate(long postModifiedDate) {
        this.postModifiedDate = postModifiedDate;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }
}

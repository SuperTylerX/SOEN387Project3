package model;

import com.google.gson.annotations.Expose;

import java.util.Date;

public class Post {

    @Expose
    private int postID;
    private long postAuthorID;
    private String postAuthorName;
    private String postTitle;
    private String postContent;
    private long postCreatedDate;
    private long postModifiedDate;
    private Attachment attachment;


    public Post() {
    }
    //for test
    public Post(int postID, long postAuthorID, String postAuthorName, String postTitle, String postContent, long postCreatedDate, long postModifiedDate, Attachment attachment) {
        this.postID = postID;
        this.postAuthorID = postAuthorID;
        this.postAuthorName = postAuthorName;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postCreatedDate = postCreatedDate;
        this.postModifiedDate = postModifiedDate;
        this.attachment = attachment;
    }

    //    This constructor used for first creating a post
    public Post(String postTitle, String postContent, long postAuthorID) {
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

    public long getPostAuthorID() {
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

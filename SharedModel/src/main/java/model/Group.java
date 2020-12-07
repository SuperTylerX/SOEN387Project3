package model;

import com.google.gson.annotations.Expose;

public class Group {

    @Expose
    private long groupId;
    @Expose
    private String groupName;
    private long groupParent;

    public Group(long groupId, String groupName, long groupParent) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupParent = groupParent;
    }

    public Group(Group g){
        this.groupId = g.groupId;
        this.groupName = g.groupName;
        this.groupParent = g.groupParent;
    }

    public Group clone(){
        return new Group(this);
    }
    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public long getGroupParent() {
        return groupParent;
    }

    public void setGroupParent(long groupParent) {
        this.groupParent = groupParent;
    }

    public String toString() {
        return "Group Name: " + groupName + ", Group Id: " + groupId + ";";
    }
}

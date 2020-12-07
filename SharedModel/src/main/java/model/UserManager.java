package model;

import java.util.ArrayList;

public interface UserManager {

    public User authUser(String username, String password);

    public String getUserNameById(long id);

    public long getUserIdByName(String userName);

    public User getUserById(long id);

    public long getUserGroupIdByUserID(long id);

    public Group getUserGroupByGroupId(long id);

    public String getGroupNameByGroupId(long groupId);

    public ArrayList<Group> findChildren(long groupId) throws Exception;

    public void findChildren(long groupId, ArrayList<Group> l) throws Exception;

    public boolean checkGroupValidity(long userID, long postGroupID) throws Exception;
}

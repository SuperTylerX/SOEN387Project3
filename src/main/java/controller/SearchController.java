package controller;

import com.google.gson.Gson;
import dao.PostDAO;
import model.Group;
import model.Post;
import model.UserManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet(name = "SearchController")
public class SearchController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //check ID
        if (request.getSession().getAttribute("userId") == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        // get parameter
        long groupID = (long) (request.getSession().getAttribute("userGroupId"));

        // generate an array of group ID that allows to be viewed
        ArrayList<Group> validGroups = null;
        try {
            validGroups = UserManager.getInstance().findChildren(groupID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long[] groupIDToRead = new long[validGroups.size() + 1];
        groupIDToRead[0] = 0;
        int i = 1;
        for (Group g : validGroups) {
            groupIDToRead[i++] = g.getGroupId();
        }

        //get data for search needs
        int attachId = 0;
        if (request.getParameter("attachId") != null) {
            attachId = Integer.parseInt(request.getParameter("attachId"));
        }
        String author = request.getParameter("authorName");
        long userID = -2;
        if (author != null) {
            userID = UserManager.getInstance().getUserIdByName(author);
        }
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String tags = request.getParameter("tags");
        String[] tagArr = {};
        System.out.println("tags:" + tags);
        if (tags != null) {
            tagArr = tags.split(",");
        }
        //create dao and search methods
        PostDAO postdao = new PostDAO();
        ArrayList<Post> posts = new ArrayList<>();
        if (userID == -1) {
            //not a correct user
            String resultJson = arrToSuccessJson(posts);
            sendInfo(response, resultJson);
        } else if (userID != -2 && (startDate == null || endDate == null) && tagArr.length == 0) {
            posts = postdao.readPostsByAutherId(userID, groupIDToRead);
            // add group name to each post
            for (Post p : posts) {
                p.setPostGroupName(UserManager.getInstance().getGroupNameByGroupId(p.getPostGroupID()));
            }
            String resultJson = arrToSuccessJson(posts);
            sendInfo(response, resultJson);
        } else if (userID == -2 && startDate != null && endDate != null && tagArr.length == 0) {
            posts = postdao.readPostsByDate(Long.parseLong(startDate), Long.parseLong(endDate), groupIDToRead);
            // add group name to each post
            for (Post p : posts) {
                p.setPostGroupName(UserManager.getInstance().getGroupNameByGroupId(p.getPostGroupID()));
            }
            String resultJson = arrToSuccessJson(posts);
            sendInfo(response, resultJson);
        } else if (userID == -2 && (startDate == null || endDate == null) && tagArr.length != 0) {
            for (String s : tagArr) {
                ArrayList<Post> temp = new ArrayList<>();
                temp = postdao.readPostsByContent(s, groupIDToRead);
                posts.addAll(temp);
            }
            removeDuplicated(posts);
            // add group name to each post
            for (Post p : posts) {
                p.setPostGroupName(UserManager.getInstance().getGroupNameByGroupId(p.getPostGroupID()));
            }
            String resultJson = arrToSuccessJson(posts);
            sendInfo(response, resultJson);

        } else if (userID != -2 && startDate != null && endDate != null && tagArr.length == 0) {
            posts = postdao.readPostsByAutherIdAndDate(userID, Long.parseLong(startDate), Long.parseLong(endDate), groupIDToRead);
            // add group name to each post
            for (Post p : posts) {
                p.setPostGroupName(UserManager.getInstance().getGroupNameByGroupId(p.getPostGroupID()));
            }
            String resultJson = arrToSuccessJson(posts);
            sendInfo(response, resultJson);

        } else if (userID == -2 && startDate != null && endDate != null && tagArr.length != 0) {
            for (String s : tagArr) {
                ArrayList<Post> temp = new ArrayList<>();
                temp = postdao.readPostsByContentAndDate(s, Long.parseLong(startDate), Long.parseLong(endDate), groupIDToRead);
                posts.addAll(temp);
            }
            removeDuplicated(posts);
            // add group name to each post
            for (Post p : posts) {
                p.setPostGroupName(UserManager.getInstance().getGroupNameByGroupId(p.getPostGroupID()));
            }

            String resultJson = arrToSuccessJson(posts);
            sendInfo(response, resultJson);
        } else if (userID != -2 && (startDate == null || endDate == null) && tagArr.length != 0) {
            for (String s : tagArr) {
                ArrayList<Post> temp = new ArrayList<>();
                temp = postdao.readPostsByAutherIdAndTag(userID, s, groupIDToRead);
                posts.addAll(temp);
            }
            removeDuplicated(posts);
            // add group name to each post
            for (Post p : posts) {
                p.setPostGroupName(UserManager.getInstance().getGroupNameByGroupId(p.getPostGroupID()));
            }
            String resultJson = arrToSuccessJson(posts);
            sendInfo(response, resultJson);

        } else if (userID != -2 && startDate != null && endDate != null && tagArr.length != 0) {
            for (String s : tagArr) {
                ArrayList<Post> temp = new ArrayList<>();
                temp = postdao.readPostsByAll(userID, Long.parseLong(startDate), Long.parseLong(endDate), s, groupIDToRead);
                posts.addAll(temp);
            }
            removeDuplicated(posts);
            // add group name to each post
            for (Post p : posts) {
                p.setPostGroupName(UserManager.getInstance().getGroupNameByGroupId(p.getPostGroupID()));
            }
            String resultJson = arrToSuccessJson(posts);
            sendInfo(response, resultJson);

        } else {
            // no limit return all posts
            posts = postdao.readPostsByGroup(groupIDToRead);
            // add group name to each post
            for (Post p : posts) {
                p.setPostGroupName(UserManager.getInstance().getGroupNameByGroupId(p.getPostGroupID()));
            }
            String resultJson = arrToSuccessJson(posts);
            sendInfo(response, resultJson);
        }

    }

    private String arrToSuccessJson(ArrayList<Post> posts) {
        HashMap<String, Object> res = new HashMap<>();
        res.put("status", 200);
        res.put("data", posts);
        Gson gson = new Gson();
        String resultJson = gson.toJson(res);
        return resultJson;
    }

    private String wrongJson() {
        HashMap<String, Integer> res = new HashMap<>();
        res.put("status", 403);
        Gson gson = new Gson();
        String resultJson = gson.toJson(res);
        return resultJson;
    }

    private void sendInfo(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    private void removeDuplicated(ArrayList<Post> posts) {
        for (int i = 0; i < posts.size(); i++) {
            for (int j = i + 1; j < posts.size(); j++) {
                if (posts.get(i).getPostID() == posts.get(j).getPostID()) {
                    posts.remove(j);
                    j--;
                }
            }
        }

    }

}

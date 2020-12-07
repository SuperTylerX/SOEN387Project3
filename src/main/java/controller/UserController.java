package controller;

import com.google.gson.*;
import model.Group;
import model.User;
import model1.UserManagerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet(name = "UserController")
public class UserController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
    }

    //    Get the user information
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userId") == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        long userId = (long) request.getSession().getAttribute("userId");
        User user = UserManagerFactory.getInstance().getUserById(userId);

        // Get the group and children group that belong to the user
        long groupId = user.getUserGroup();
        ArrayList<Group> grouplist = new ArrayList<>();
        try {
            grouplist = UserManagerFactory.getInstance().findChildren(groupId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // return status in form of json
        HashMap<String, Object> res = new HashMap<>();

        res.put("status", 200);

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        JsonElement userElement = gson.toJsonTree(user);
        JsonElement groupElement = gson.toJsonTree(grouplist);
        userElement.getAsJsonObject().add("userGroup", groupElement);
        userElement.getAsJsonObject().addProperty("isAdmin", user.getUserGroup() == 1);
        res.put("data", userElement);

        String resultJson = gson.toJson(res);
        sendInfo(response, resultJson);

    }

    private void sendInfo(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

}

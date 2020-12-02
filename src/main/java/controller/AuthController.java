package controller;

import model.User;
import model.UserManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@WebServlet(name = "AuthController")
public class AuthController extends HttpServlet {

    // Authorized Users Login
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        response.setContentType("application/json; charset=utf-8");

        User user = UserManager.getInstance().authUser(username, password);
        JSONObject resJson = new JSONObject();
        if (user != null) {
            // Set Session
            HttpSession session = request.getSession();
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("userGroupId", user.getUserGroup());
            // Set Response
            resJson.put("status", 200);
            PrintWriter out = response.getWriter();
            out.println(resJson.toJSONString());
            out.close();
        } else {
            resJson.put("status", 403);
            PrintWriter out = response.getWriter();
            out.println(resJson.toJSONString());
            out.close();
        }
    }


    // Logout
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // If the session exists, remove the session
        if (request.getSession().getAttribute("userId") != null){
            request.getSession().invalidate();
            response.setContentType("application/json; charset=utf-8");
            JSONObject resJson = new JSONObject();
            resJson.put("status", 200);
            PrintWriter out = response.getWriter();
            out.println(resJson.toJSONString());
            out.close();
        }else{
            // Session not exist, send an error
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}

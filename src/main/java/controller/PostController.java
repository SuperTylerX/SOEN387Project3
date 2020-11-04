package controller;

import com.google.gson.Gson;
import dao.PostDAO;
import model.Post;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

@WebServlet(name = "PostController")
public class PostController extends HttpServlet {

    // Create a new Post
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userId") == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        long userId = (long) request.getSession().getAttribute("userId");


    }

    // Get a list of post
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        if (request.getSession().getAttribute("userId") == null) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
//            return;
//        }

        PostDAO postdao = new PostDAO();
        ArrayList posts = postdao.readPosts();
        HashMap<String, Object> res = new HashMap<>();
        res.put("status", 200);
        res.put("data", posts);
        Gson gson = new Gson();
        String resultJson=gson.toJson(res);

        sendInfo(response,resultJson);

    }

    // Delete a existed post
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userId") == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        long userId = (long) request.getSession().getAttribute("userId");


    }

    // Update a existed post
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userId") == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        long userId = (long) request.getSession().getAttribute("userId");


    }

    private void sendInfo(HttpServletResponse response, String json) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
//        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");// cross domain request/CORS
        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.getWriter().write(json);
    }

}

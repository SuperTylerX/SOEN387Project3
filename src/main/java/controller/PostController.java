package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "PostController")
public class PostController extends HttpServlet {

    // Create a new Post
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userId") == null){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        long userId = (long) request.getSession().getAttribute("userId");


    }

    // Get a list of post
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userId") == null){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        long userId = (long) request.getSession().getAttribute("userId");


    }

    // Delete a existed post
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userId") == null){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        long userId = (long) request.getSession().getAttribute("userId");


    }

    // Update a existed post
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userId") == null){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        long userId = (long) request.getSession().getAttribute("userId");


    }

}

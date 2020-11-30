package controller;

import dao.PostDAO;
import model.Post;
import model.User;
import model.UserManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "postDownloadController")
public class PostDownloadController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userId") == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        // get parameter
        long userId = (long) request.getSession().getAttribute("userId");
        int postId = Integer.parseInt(request.getParameter("postId"));

        //search post
        PostDAO postdao=new PostDAO();
        Post post=postdao.readPostsByPostId(postId);
        long groupId=post.getPostGroupID();
        try {
            if (UserManager.getInstance().checkGroupValidity(userId,groupId)){
                //todo: turn the post to xml
            }else{
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

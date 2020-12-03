package controller;

import dao.PostDAO;
import model.Post;
import model.User;
import model.UserManager;
import utils.XMLTransformer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "postDownloadController")
public class PostDownloadController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
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
        PostDAO postdao = new PostDAO();
        Post post = postdao.getPostsByPostId(postId);
        long groupId = post.getPostGroupID();
        try {
            if (UserManager.getInstance().checkGroupValidity(userId, groupId)) {
                XMLTransformer transformer = XMLTransformer.getInstance();
                String str = transformer.toXMLString(post);
                response.setContentType("application/xml");
                response.setHeader("Content-Disposition", "attachment;filename=" + post.getPostTitle() + ".xml");
                PrintWriter out = response.getWriter();
                out.println(str);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

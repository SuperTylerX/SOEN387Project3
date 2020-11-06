package controller;

import dao.AttachmentDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@WebServlet(name = "AttachController")
public class AttachController extends HttpServlet {

    // Upload a new attachment
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userId") == null){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        long userId = (long) request.getSession().getAttribute("userId");

        File file=(File)request.getSession().getAttribute("file");

        AttachmentDAO attachmentDAO=new AttachmentDAO();
        //Haven't figure out how to get name,
        attachmentDAO.createAttachment("name",file);



    }

    // Download a existed attachment
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userId") == null){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        long userId = (long) request.getSession().getAttribute("userId");

    }

    // Delete a existed attachment
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userId") == null){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        long userId = (long) request.getSession().getAttribute("userId");



    }

}

package controller;

import dao.AttachmentDAO;
import model.Attachment;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@WebServlet(name = "AttachController")
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5)
public class AttachController extends HttpServlet {

    // Upload a new attachment
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userId") == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        long userId = (long) request.getSession().getAttribute("userId");

        if (ServletFileUpload.isMultipartContent(request)) {

            Part part = request.getPart("file");
            String MIME = part.getContentType();
            String fileName = part.getSubmittedFileName();
            long size = part.getSize();
            InputStream inputStream = part.getInputStream();
            byte[] fileContent = {};
            try {
                fileContent = readInputStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Attachment attach = new Attachment();
            attach.setAttachMIME(MIME);
            attach.setAttachName(fileName);
            attach.setAttachSize(size);
            attach.setFileContent(fileContent);
            AttachmentDAO attachmentDAO = new AttachmentDAO();
            attachmentDAO.createAttachment(attach);
        }


    }

    // Download a existed attachment
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userId") == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        long userId = (long) request.getSession().getAttribute("userId");

    }

    // Delete a existed attachment
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userId") == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        long userId = (long) request.getSession().getAttribute("userId");


    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }
}

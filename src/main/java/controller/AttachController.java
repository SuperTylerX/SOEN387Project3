package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.io.*;
import java.util.HashMap;

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
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // create an attachment object
            Attachment attach = new Attachment();
            attach.setAttachMIME(MIME);
            attach.setAttachName(fileName);
            attach.setAttachSize(size);
            attach.setFileContent(fileContent);
            AttachmentDAO attachmentDAO = new AttachmentDAO();
            int attachId = attachmentDAO.createAttachment(attach);

            // return status in form of json
            HashMap<String, Object> res = new HashMap<>();
            if (attachId != -1) {
                res.put("data", attach);
                res.put("status", 200);
            } else {
                res.put("status", 403);
            }
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            String resultJson = gson.toJson(res);
            sendInfo(response, resultJson);
        }
    }

    // Download a existed attachment
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userId") == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        final int BUFFER_SIZE = 4096;

        // get request parameters
        long userId = (long) request.getSession().getAttribute("userId");
        int attachId = 0;
        if (request.getParameter("attachId") != null) {
            attachId = Integer.parseInt(request.getParameter("attachId"));
        }
//        int postId = 0;
//        if (request.getParameter("postId") != null) {
//            postId = Integer.parseInt(request.getParameter("postId"));
//        }

        AttachmentDAO attachmentDAO = new AttachmentDAO();
        Attachment attach = attachmentDAO.readAttachment(attachId);
        try {
            InputStream inputStream = new ByteArrayInputStream(attach.getFileContent());

            if (attach != null && inputStream != null) {
                // set content properties and header attributes for the response
                response.setHeader("pragma", "no-cache");
                response.setHeader("cache-control", "no-cache");
                response.setDateHeader("expires", 0);
                response.setHeader("Content-Disposition", "attachment;filename=" + attach.getAttachName());
                response.setContentType(attach.getAttachMIME());
                response.setContentLength(inputStream.available());
                // writes the file to the client

                OutputStream outputStream = response.getOutputStream();
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead = -1;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                inputStream.close();
                outputStream.close();

            } else {
                response.getWriter().print("File not found for the id: " + attachId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void sendInfo(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}

package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.PostDAO;
import model.Post;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "PostController")
public class PostController extends HttpServlet {

    // Create a new Post
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userId") == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // get parameter
        long userId = (long) request.getSession().getAttribute("userId");
//        System.out.println(userId);
        String postTitle = request.getParameter("postTitle");
        String postContent = request.getParameter("postContent");
        int attachId = 0;
        if (request.getParameter("attachId") != null) {
            attachId = Integer.parseInt(request.getParameter("attachId"));
        }

        // create post object
        Post post = new Post(postTitle, postContent, userId);
        PostDAO postdao = new PostDAO();
        int postID = postdao.createPost(post, attachId);

        // return status in form of json
        HashMap<String, Object> res = new HashMap<>();
        if (postID != -1) {
            res.put("data", post);
            res.put("status", 200);
        } else {
            res.put("status", 403);
        }
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String resultJson = gson.toJson(res);
        sendInfo(response, resultJson);
    }

    // Get a list of post
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userId") == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        PostDAO postdao = new PostDAO();
        ArrayList posts = postdao.readPosts();
        HashMap<String, Object> res = new HashMap<>();
        res.put("status", 200);
        res.put("data", posts);
        Gson gson = new Gson();
        String resultJson = gson.toJson(res);
        sendInfo(response, resultJson);

    }

    // Delete a existed post
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userId") == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        long userId = (long) request.getSession().getAttribute("userId");
        PostDAO postdao = new PostDAO();

        String body = getBodyString(request);
        String[] arr = body.split("=");
        int postId = Integer.parseInt(arr[1].trim());

        HashMap<String, Integer> resulthm = new HashMap<>();
        if (postdao.checkValidOwner(userId, postId) && postdao.deletePost(postId)) {
            resulthm.put("status", 200);
        } else {
            resulthm.put("status", 403);
        }
        Gson gson = new Gson();
        String res = gson.toJson(resulthm);
        sendInfo(response, res);

    }

    // Update a existed post
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userId") == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        long userId = (long) request.getSession().getAttribute("userId");

        // retrieve request
        String body = getBodyString(request);
        System.out.println(body);
        String[] pairs = body.split("&");
        HashMap<String, String> postInfoMap = new HashMap<>();
        for (String pair : pairs) {
            String[] fields = pair.split("=");
            postInfoMap.put(fields[0], URLDecoder.decode(fields[1], "UTF-8"));
        }

        // retrieve post INFO
        int postId = Integer.parseInt(postInfoMap.get("postId"));
        String postTitle = postInfoMap.get("postTitle");
        String postContent = postInfoMap.get("postContent");
        int attachId = 0;
        if (postInfoMap.get("attachId") != null) {
            attachId = Integer.parseInt(postInfoMap.get("attachId"));
        }

        // create updated post object
        Post post = new Post(postTitle, postContent, userId);
        post.setPostModifiedDate(new Date().getTime());
        PostDAO postdao = new PostDAO();

        HashMap<String, Integer> resulthm = new HashMap<>();
        if (postdao.updatePost(post, postId, attachId)) {
            resulthm.put("status", 200);
        } else {
            resulthm.put("status", 403);
        }
        Gson gson = new Gson();
        String res = gson.toJson(resulthm);
        sendInfo(response, res);

    }

    private void sendInfo(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }


    protected String getBodyString(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader;
        try {
            reader = request.getReader();
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(specailCharFilter(line)).append('\n');
                }
                String body = sb.toString();
                return body.trim();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            return "";
        }
        return "";
    }

    private String specailCharFilter(String input) {
        Pattern singleQuotePattern = Pattern.compile("[^\\\\]'");
        Matcher m = singleQuotePattern.matcher(input);
        List<String> singleQuoteList = new ArrayList<String>();
        while (m.find()) {
            singleQuoteList.add(m.group(0));
        }
        for (String temp : singleQuoteList) {
            String newString = temp.substring(0, 1) + "\\\\" + temp.substring(1);
            input = input.replace(temp, newString);
        }

        return input;
    }


}

package controller;

import imp.UserManagerImp;
import model1.UserManagerFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UserManagerFactory.class})
public class PostControllerTest{


    @Test
    public void testCreatePost() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute("userId")).thenReturn((long)3);
        when(request.getParameter("postTitle")).thenReturn("title of post");
        when(request.getParameter("postContent")).thenReturn("content of post");
        when(request.getParameter("postGroupId")).thenReturn("3");

//        doNothing().when(UserManager.class).getClassLoader();
//        UserManagerImp userManagerMock = mock(UserManagerImp.class);

//        String path = "src/test/resources/Users.json";
//        File file = new File(path);
//        String absolutePath = file.getAbsolutePath();
//        UserManagerImp userManager = new UserManagerImp(absolutePath);
//        doNothing().when(userManagerFactoryMock).loadUserListAndGroup();

        UserManagerImp userManagerImpMock = mock(UserManagerImp.class);

        PowerMockito.mockStatic(UserManagerFactory.class);
        when(UserManagerFactory.getInstance()).thenReturn(userManagerImpMock);

        when(userManagerImpMock.checkGroupValidity(3,3 )).thenReturn(true);


        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        new PostController().doPost(request, response);

        verify(request, atLeast(1)).getParameter("postTitle"); // only if you want to verify username was called...
        writer.flush(); // it may not have been flushed yet...
        assertTrue(stringWriter.toString().contains("200"));
    }
}
package model1;

import imp.UserManagerImp;
import org.junit.Test;

import java.io.File;

public class UserManagerTest {

    @Test(expected = Exception.class)
    public void undefinedGroupTest() throws Exception {
        String path = "src/test/resources/undefinedGroupTest.json";
        File file = new File(path);
        String absolutePath = file.getAbsolutePath();
//        System.out.println(absolutePath);

        new UserManagerImp(absolutePath);
    }

    @Test(expected = IllegalArgumentException.class)
    public void undefinedUserTest() throws Exception {
        String path = "src/test/resources/undefinedUserTest.json";
        File file = new File(path);
        String absolutePath = file.getAbsolutePath();

        new UserManagerImp(absolutePath);
    }


    @Test(expected = Exception.class)
    public void nonExistingParentTest() throws Exception {
        String path = "src/test/resources/nonExistingParentTest.json";
        File file = new File(path);
        String absolutePath = file.getAbsolutePath();

        new UserManagerImp(absolutePath);
    }

    @Test(expected = Exception.class)
    public void circularParentChildTest() throws Exception {
        String path = "src/test/resources/circularParentChildTest.json";
        File file = new File(path);
        String absolutePath = file.getAbsolutePath();

        new UserManagerImp(absolutePath).findChildren(3);
    }

}



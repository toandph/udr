package architectgroup.fact.access.test;

import architectgroup.fact.access.UserAccess;
import architectgroup.fact.access.util.FactAccessFactory;
import architectgroup.fact.dto.UserDto;
import org.jetbrains.annotations.Nullable;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 3/27/13
 * Time: 10:51 AM
 */
public class UserAccessTest {
    static FactAccessFactory factAccess;
    static UserAccess userAccess;

    @BeforeClass
    public static void setUp() throws Exception {
        userAccess = new UserAccess(factAccess);
    }

    @Test
    public void testGetAnonymousUser() throws Exception {
        UserDto userDto = userAccess.getAnonymousUser();

        if (userDto == null)
            fail("Message");
        else {
            assertEquals(userDto.getUsername(), "anonymous");
        }
    }

    @Test
    public void testGetUserList() throws Exception {
        List<UserDto> list = userAccess.getUserList();
        if (list != null) {
            for (UserDto user : list) {
                if (user == null) {
                    fail("User is null");
                } else {
                    if (user.getId() < 0) {
                        fail("Invalid user id");
                    }
                    if (user.getUsername().length() == 0) {
                        fail("Username can not be blank");
                    }
                }
            }
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        factAccess = null;
        userAccess = null;
    }
}

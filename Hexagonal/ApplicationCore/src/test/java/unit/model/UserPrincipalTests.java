package unit.model;

import org.junit.BeforeClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;
import pl.lodz.p.model.user.Admin;
import pl.lodz.p.model.user.User;
import pl.lodz.p.model.user.UserPrincipal;

public class UserPrincipalTests {

    static UserPrincipal userPrincipal;
    static User user = new Admin("John", "Doe", "JDoe", "a", "jdoe@example.com");
    @BeforeAll
    public static void setUp() {
        userPrincipal = new UserPrincipal(user);
    }

    @Test
    public void getAuthorities() {
        Assertions.assertEquals("[ADMIN]", userPrincipal.getAuthorities().toString());
    }

    @Test
    public void getUsername() {
        Assertions.assertEquals("JDoe", userPrincipal.getUsername());
    }

    @Test
    public void getPassword() {
        Assertions.assertTrue(BCrypt.checkpw("a",  userPrincipal.getPassword()));
    }
}

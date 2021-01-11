/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import de.hspf.homeimprovementfrontend.login.LoginBean;
import de.hspf.homeimprovementfrontend.login.ProfileBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author Marcel
 */
@ExtendWith(MockitoExtension.class)
public class LoginBeanTest {
    
    @InjectMocks
    private LoginBean loginBean;
    @Mock
    ProfileBean profileBean;
    
    public LoginBeanTest() {
    }
    
    @BeforeEach
    public void setUpClass() {
        loginBean = new LoginBean();
    }

     @Test
     public void whenGetAuthURL_thenStringShouldBeReturned() {
         String url = loginBean.getAuthURL();
         
         assertTrue(String.class.isInstance(url));
         assertTrue(!url.isEmpty());
         assertTrue(url.startsWith("http"));
     }
     
     @Test
     public void whenGetProfileURL_thenStringShouldBeReturned() {
         String url = loginBean.getProfileURL();
         
         assertTrue(String.class.isInstance(url));
         assertTrue(!url.isEmpty());
         assertTrue(url.startsWith("http"));
     }
     
     @Test
     public void whenGetDate_thenLongShouldBeReturned() {
         long date = loginBean.getDate();
         
         assertTrue(Long.class.isInstance(date));
     }
     
     @Test
     public void whenAuthenticate_thenUserShouldBeLoggedIn() {
         loginBean.authenticate();
         
         assertTrue(!loginBean.isLoggedIn());
     }
}

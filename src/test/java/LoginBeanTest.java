/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import de.hspf.homeimprovementfrontend.login.LoginBean;
import de.hspf.homeimprovementfrontend.profile.ProfileBean;
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
     public void whenGetDate_thenLongShouldBeReturned() {
         long date = loginBean.getDate();
         
         assertTrue(Long.class.isInstance(date));
     }
}

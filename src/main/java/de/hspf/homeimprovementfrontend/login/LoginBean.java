package de.hspf.homeimprovementfrontend.login;

import de.hspf.homeimprovementfrontend.models.Account;
import de.hspf.homeimprovementfrontend.config.ViewContextUtil;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author thomas
 */
@Named(value = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final long serialVersionUID = -383992898674810212L;

    private boolean loggedIn;
    private String token;
    @NotEmpty
    @Size(min = 8, message = "Password must have at least 8 characters")
    private String password;

    @NotEmpty
    @Email(message = "Please provide a valid e-mail")
    private String userName;

    @Inject
    private SecurityContext securityContext;

    public LoginBean() {
        setLoggedIn(false);
    }

    /**
     * *
     * This method will do a authentication. If the authentication is
     * successful, the user will be redirected to a personal deshboard page. If
     * the authentication is not successful the user will remain on the login
     * page.
     *
     * @return
     */
    public void authenticate() throws IOException {
        logger.log(Level.INFO, "Try to login user: {0}", userName);
        Account account = new Account();
        account.setEmail(userName);
        account.setPassword(password);
        
        WebTarget target = ClientBuilder.newClient().target("http://localhost:8080/authservice/data/auth/login");
        Response response = target.request().post(Entity.entity(account, MediaType.APPLICATION_JSON));

        if (response.getStatus() == 200) {
            token = response.readEntity(String.class);
            setLoggedIn(true);
            this.test();
            securityContext.authenticate((HttpServletRequest) ViewContextUtil.getExternalContext().getRequest(),
                (HttpServletResponse) ViewContextUtil.getExternalContext().getResponse(),
                AuthenticationParameters.withParams()
                  .credential(new UsernamePasswordCredential(userName, password)));
            ViewContextUtil.getExternalContext().redirect(ViewContextUtil.getExternalContext().getRequestContextPath() + "/app/index.xhtml");
        } else {
            ViewContextUtil.getFacesContext().addMessage(null, new FacesMessage("Login failed. Please give it another try!"));
        }
    }
    
    public void test(){
        System.out.println("Token");
        System.out.println(token);
        WebTarget target = ClientBuilder.newClient().target("http://localhost:8180/authservice/data/user");
        Response response = target.request().header("authorization", "Bearer " + token).buildGet().invoke();
        System.out.println(String.format(response.readEntity(String.class)));
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

}

package de.hspf.homeimprovementfrontend.login;

import com.google.gson.Gson;
import de.hspf.homeimprovementfrontend.models.Account;
import de.hspf.homeimprovementfrontend.config.ViewContextUtil;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
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
 * @author marcel
 */
@Named(value = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final long serialVersionUID = -383992898674810212L;

    private boolean loggedIn;
    private String token;
    @NotEmpty
    @Size(min = 4, message = "Password must have at least 4 characters")
    private String password;

    @NotEmpty
    @Email(message = "Please provide a valid e-mail")
    private String userName;

    private String email;

    private String roles;

    @Inject
    private SecurityContext securityContext;

    public LoginBean() {
        setLoggedIn(false);
    }
    
    public void authenticate() {
        logger.log(Level.INFO, "Try to login user: {0}", userName);
        Response response = null;
        Account account = new Account();
        account.setEmail(userName);
        account.setPassword(password);

        try {
            WebTarget target = ClientBuilder.newClient().target("http://localhost:8080/authservice/data/auth/login");
            response = target.request().post(Entity.entity(account, MediaType.APPLICATION_JSON));
        } catch (Exception e) {
            logger.info("Not able to access authentication service");
        }

        if (response.getStatus() == 200) {
            token = response.readEntity(String.class);
            setLoggedIn(true);
            this.loadUserProfile();
            securityContext.authenticate((HttpServletRequest) ViewContextUtil.getExternalContext().getRequest(),
                    (HttpServletResponse) ViewContextUtil.getExternalContext().getResponse(),
                    AuthenticationParameters.withParams()
                            .credential(new UsernamePasswordCredential(email, password)));
            try {
                ViewContextUtil.getExternalContext().redirect(ViewContextUtil.getExternalContext().getRequestContextPath() + "/app/index.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            ViewContextUtil.getFacesContext().addMessage(null, new FacesMessage("Login failed. Please give it another try!"));
        }
    }

    public void loadUserProfile() {
        try {
            WebTarget target = ClientBuilder.newClient().target("http://localhost:8180/authservice/data/user");
            Response response = target.request().header("authorization", "Bearer " + token).buildGet().invoke();
            Account account;
            Gson g = new Gson();
            String s = String.format(response.readEntity(String.class));
            account = g.fromJson(s, Account.class);
            this.userName = account.getUsername();
            this.email = account.getEmail();
            this.roles = account.getRoles().toString();
        } catch (Exception e) {
            logger.info("Not able to access profile service");
            this.userName = "undefined";
            this.email = "unefined";
            this.roles = "[user]";
        }
    }

    public void redirect() throws IOException {
        ViewContextUtil.getExternalContext().redirect(ViewContextUtil.getExternalContext().getRequestContextPath() + "/app/profile.xhtml");
    }

    public void redirectToHomepage() throws IOException {
        ViewContextUtil.getExternalContext().redirect(ViewContextUtil.getExternalContext().getRequestContextPath() + "/app/index.xhtml");
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

}

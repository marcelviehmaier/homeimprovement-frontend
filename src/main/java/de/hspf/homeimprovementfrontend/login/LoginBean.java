package de.hspf.homeimprovementfrontend.login;

import com.google.gson.Gson;
import de.hspf.homeimprovementfrontend.models.Account;
import de.hspf.homeimprovementfrontend.config.ViewContextUtil;
import de.hspf.homeimprovementfrontend.registration.RegistrationBean;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Properties;
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
    private String username;
    private String email;
    private String roles;
    private String authURL;
    private String profileURL;
    @Inject
    private SecurityContext securityContext;

    public LoginBean() {
        // Load URL for Authentication and Profile Service from config.properties
        try (InputStream input = RegistrationBean.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                logger.info("Not able to load config file");
            }
            prop.load(input);
            this.setAuthURL(prop.getProperty("authservice.url"));
            this.setProfileURL(prop.getProperty("profileservice.url"));
        } catch (IOException ex) {
        }
        
        setLoggedIn(false);
    }
    
    public void authenticate() {
        Response response = null;
        Account account = new Account();
        account.setEmail(username);
        account.setPassword(password);

        try {
            WebTarget target = ClientBuilder.newClient().target(this.getAuthURL() + "/data/auth/login");
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
                    AuthenticationParameters.withParams().credential(new UsernamePasswordCredential(email, password)));
            ViewContextUtil.internalRedirect(ViewContextUtil.getExternalContext().getRequestContextPath() + "/app/index.xhtml");
        } else {
            ViewContextUtil.getFacesContext().addMessage(null, new FacesMessage("Login failed. Please give it another try!"));
        }
    }

    public void loadUserProfile() {
        try {
            WebTarget target = ClientBuilder.newClient().target(this.getProfileURL() + "/data/user");
            Response response = target.request().header("authorization", "Bearer " + token).buildGet().invoke();
            Account account;
            Gson g = new Gson();
            String s = String.format(response.readEntity(String.class));
            account = g.fromJson(s, Account.class);
            this.username = account.getUsername();
            this.email = account.getEmail();
            this.roles = account.getRoles().toString();
        } catch (Exception e) {
            logger.info("Not able to access profile service");
            this.username = "undefined";
            this.email = "unefined";
            this.roles = "[user]";
        }
    }
    
    public String logout() {
        ViewContextUtil.getFacesContext().getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }

    public void redirect() throws IOException {
        ViewContextUtil.getExternalContext().redirect(ViewContextUtil.getExternalContext().getRequestContextPath() + "/app/profile.xhtml");
    }

    public void redirectToHomepage() throws IOException {
        ViewContextUtil.getExternalContext().redirect(ViewContextUtil.getExternalContext().getRequestContextPath() + "/app/index.xhtml");
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
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

    public String getAuthURL() {
        return authURL;
    }

    public void setAuthURL(String authURL) {
        this.authURL = authURL;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    } 

}
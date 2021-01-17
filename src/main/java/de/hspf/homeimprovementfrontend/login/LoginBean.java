package de.hspf.homeimprovementfrontend.login;

import de.hspf.homeimprovementfrontend.api.APIManager;
import de.hspf.homeimprovementfrontend.profile.ProfileBean;
import de.hspf.homeimprovementfrontend.models.Account;
import de.hspf.homeimprovementfrontend.config.ViewContextUtil;
import java.io.IOException;
import javax.inject.Named;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

/**
 *
 * @author marcel
 */
@Named(value = "loginBean")
@ApplicationScoped
public class LoginBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final long serialVersionUID = -383992898674810212L;

    private boolean loggedIn;
    private String password;
    private String username;
    private String email;
    private Account account;

    @Inject
    private SecurityContext securityContext;
    @Inject
    ProfileBean profileBean;
    @Inject
    private APIManager apiManager;

    public void authenticate() {
        Account acc = new Account();
        acc.setEmail(username);
        acc.setPassword(password);

        Response response = this.apiManager.loginAccount(acc);

        if (response.getStatus() == 200) {
            setLoggedIn(true);
            this.apiManager.setToken(response.readEntity(String.class));
            this.account = profileBean.loadUserProfile();
            this.email = this.account.getEmail();
            this.username = this.account.getUsername();

            securityContext.authenticate((HttpServletRequest) ViewContextUtil.getExternalContext().getRequest(),
                    (HttpServletResponse) ViewContextUtil.getExternalContext().getResponse(),
                    AuthenticationParameters.withParams()
                            .credential(new UsernamePasswordCredential(this.email, this.password)));
            ViewContextUtil.internalRedirect("/app/index.xhtml");
        } else {
            ViewContextUtil.getFacesContext().addMessage(null, new FacesMessage("Login failed. Please give it another try!"));
        }
    }

    public long getDate() {
        return System.currentTimeMillis();
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

}

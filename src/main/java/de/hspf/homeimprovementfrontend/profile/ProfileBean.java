package de.hspf.homeimprovementfrontend.profile;

import de.hspf.homeimprovementfrontend.api.APIManager;
import de.hspf.homeimprovementfrontend.models.Account;
import javax.inject.Named;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

/**
 *
 * @author marcel
 */
@Named(value = "profileBean")
@SessionScoped
public class ProfileBean implements Serializable {

    @Inject
    private APIManager apiManager;
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final long serialVersionUID = -3839928986748109482L;
    private String profileURL;
    private Account account;
    private String token;
    
    public Account loadUserProfile() {
        this.account = this.apiManager.getAccount();
        return this.account;
    }

    public void updateProfile(){
        this.account = this.apiManager.putAccount(this.account);
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
}
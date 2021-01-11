package de.hspf.homeimprovementfrontend.login;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import de.hspf.homeimprovementfrontend.models.Account;
import de.hspf.homeimprovementfrontend.registration.RegistrationBean;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Properties;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author marcel
 */
@Named(value = "profileBean")
@SessionScoped
public class ProfileBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final long serialVersionUID = -3839928986748109482L;
    private String profileURL;
    private Account account;
    private String token;
    
    public ProfileBean() {
        // Load URL for Authentication and Profile Service from config.properties
        try (InputStream input = RegistrationBean.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            this.setProfileURL(prop.getProperty("profileservice.url"));
        } catch (IOException ex) {
        }
    }
    
    public Account loadUserProfile(String token) {
        LOGGER.info("Load user profile from profile service...");
        try {
            WebTarget target = ClientBuilder.newClient().target(this.getProfileURL() + "/data/user");
            Response response = target.request().header("authorization", "Bearer " + token).buildGet().invoke();
            Account acc;
            Gson gson = new Gson();
            String text = String.format(response.readEntity(String.class));
            acc = gson.fromJson(text, Account.class);
            this.account = acc;
            this.token = token;
            LOGGER.log(Level.INFO, "Return {0}", this.account.toString());
            return this.account;
        } catch (JsonSyntaxException e) {
            LOGGER.info("Not able to access profile service");
            return null;
        }
    }

    public void updateProfile(){
        try {
            LOGGER.info("Send request 1" + this.account.getDescription());
            WebTarget target = ClientBuilder.newClient().target(this.getProfileURL()+ "/data/user");
            Response response = target.request().header("authorization", "Bearer " + this.token).put(Entity.entity(this.getAccount(), MediaType.APPLICATION_JSON));
           LOGGER.info("Sent request");
            Account acc;
            Gson gson = new Gson();
            String text = String.format(response.readEntity(String.class));
            acc = gson.fromJson(text, Account.class);
            this.account = acc;
            LOGGER.info("updated user" + this.account.getDescription());
        } catch (Exception e) {
            LOGGER.info((Supplier<String>) e);
        }
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
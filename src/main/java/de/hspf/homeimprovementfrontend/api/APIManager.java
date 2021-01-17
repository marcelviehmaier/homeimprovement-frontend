/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hspf.homeimprovementfrontend.api;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import de.hspf.homeimprovementfrontend.models.Account;
import de.hspf.homeimprovementfrontend.registration.RegistrationBean;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Supplier;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Marcel
 */
@ApplicationScoped
public class APIManager {

    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final long serialVersionUID = 5698088241579720726L;
    private String authURL;
    private String profileURL;
    private String token;

    public APIManager() {
        // Load URL for Authentication and Profile Service from config.properties
        try (InputStream input = RegistrationBean.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                LOGGER.info("Not able to load config file");
            }
            prop.load(input);
            this.setAuthURL(prop.getProperty("authservice.url"));
            this.setProfileURL(prop.getProperty("profileservice.url"));
        } catch (IOException ex) {
        }
    }
    
    public Response loginAccount(Account account){
        try {
            WebTarget target = ClientBuilder.newClient().target(this.getAuthURL() + "/data/auth/login");
            Response response = target.request().post(Entity.entity(account, MediaType.APPLICATION_JSON));
            return response;
        } catch (Exception e) {
            LOGGER.info("Not able to login account");
            return null;
        }
    }

    public Response postAccount(Account account) {
        try {
            WebTarget target = ClientBuilder.newClient().target(this.getAuthURL() + "/data/auth/signup");
            Response response = target.request().post(Entity.entity(account, MediaType.APPLICATION_JSON));
            return response;
        } catch (Exception e) {
            return null;
        }
    }
    
    public Account putAccount(Account account){
        try {
            WebTarget target = ClientBuilder.newClient().target(this.getProfileURL()+ "/data/user");
            Response response = target.request().header("authorization", "Bearer " + this.token).put(Entity.entity(account, MediaType.APPLICATION_JSON));
            Account acc;
            Gson gson = new Gson();
            String text = String.format(response.readEntity(String.class));
            acc = gson.fromJson(text, Account.class);
            return acc;
        } catch (Exception e) {
            LOGGER.info((Supplier<String>) e);
            return null;
        }
    }
    
    public Account getAccount() {
        LOGGER.info("Load user profile from profile service...");
        try {
            WebTarget target = ClientBuilder.newClient().target(this.getProfileURL() + "/data/user");
            Response response = target.request().header("authorization", "Bearer " + token).buildGet().invoke();
            Account acc;
            Gson gson = new Gson();
            String text = String.format(response.readEntity(String.class));
            acc = gson.fromJson(text, Account.class);
            return acc;
        } catch (JsonSyntaxException e) {
            LOGGER.info("Not able to access profile service");
            return null;
        }
    }

    public String getAuthURL() {
        return authURL;
    }

    private void setAuthURL(String authURL) {
        this.authURL = authURL;
    }

    public String getProfileURL() {
        return profileURL;
    }

    private void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    
}

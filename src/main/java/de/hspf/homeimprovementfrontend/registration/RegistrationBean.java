package de.hspf.homeimprovementfrontend.registration;

import de.hspf.homeimprovementfrontend.models.Account;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import de.hspf.homeimprovementfrontend.config.ViewContextUtil;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author marcel
 */
@Named(value = "registrationBean")
@SessionScoped
public class RegistrationBean implements Serializable {

    private Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static final long serialVersionUID = 5698088241579720720L;

    private String name;
    private String surName;
    private String email;
    private String username;
    private String password;
    private String repeatedpassword;

    public RegistrationBean() {
    }

    public String register() throws IOException {
        logger.info("Try to register user");
        if (!password.equals(repeatedpassword)) {
            ViewContextUtil.getFacesContext().addMessage(null, new FacesMessage("Sing Up failed. You provided two different passwords."));
            return "";
        }
        Account account = new Account();
        account.setEmail(email);
        account.setPassword(password);
        account.setUsername(username);

        WebTarget target = ClientBuilder.newClient().target("http://localhost:8080/authservice/data/auth/signup");
        Response response = target.request().post(Entity.entity(account, MediaType.APPLICATION_JSON));

        if (response.getStatus() == 200) {
            ViewContextUtil.getFacesContext().addMessage(null, new FacesMessage("Signup was successfull. Now you are able to login"));
            return "login";
        } else {
            ViewContextUtil.getFacesContext().addMessage(null, new FacesMessage("Signup failed. Please give it another try!"));
            return "registration";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatedpassword() {
        return repeatedpassword;
    }

    public void setRepeatedpassword(String repeatedpassword) {
        this.repeatedpassword = repeatedpassword;
    }

}

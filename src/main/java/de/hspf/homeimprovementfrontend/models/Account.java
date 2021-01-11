/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hspf.homeimprovementfrontend.models;

import java.util.List;
import java.util.Set;

/**
 *
 * @author Marcel
 */
public class Account {
    
    private int id;
    private String username;
    private String email;
    private String password;
    private Set<String> roles;
    private byte[] profilePic;
    private int age;
    private String job;
    private ExpertLevel level;
    private  List<String> topics;
    private String description;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public byte[] getprofilePic() {
        return profilePic;
    }

    public void setprofilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public ExpertLevel getLevel() {
        return level;
    }

    public void setLevel(ExpertLevel level) {
        this.level = level;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
    
}

package com.telecomportal.bean;

import com.telecomportal.dao.CustomerDao;
import com.telecomportal.model.Customer;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.io.IOException;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {
    private String email;
    private String password;
    private Customer loggedInUser;
    private CustomerDao customerDao = new CustomerDao();

    // ✅ Login method
    public String login() {
        Customer user = customerDao.login(email, password);
        if (user != null) {
            loggedInUser = user;

            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                return "admin_dashboard.xhtml?faces-redirect=true";
            } else {
                return "user_dashboard.xhtml?faces-redirect=true";
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                new javax.faces.application.FacesMessage("Invalid credentials!"));
            return null;
        }
    }

    // ✅ Logout method
    public void logout() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
    }

    // Getters and setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Customer getLoggedInUser() { return loggedInUser; }
}

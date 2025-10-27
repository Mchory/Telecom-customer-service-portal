package com.telecomportal.bean;

import com.telecomportal.dao.*;
import com.telecomportal.model.*;
import com.telecomportal.bean.SessionUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ManagedBean
@SessionScoped
public class CustomerBean implements Serializable {

    private Customer customer = new Customer();
    private Customer loggedInCustomer;
    private List<Subscription> subscriptions;
    private List<Billing> bills;

    private CustomerDao customerDao = new CustomerDao();
    private SubscriptionDAO subscriptionDao = new SubscriptionDAO();
    private BillingDAO billingDao = new BillingDAO();

    // =============== Registration ===============
    public void register() {
        customer.setRegistrationDate(new Date());
        boolean success = customerDao.registerCustomer(customer);

        FacesContext context = FacesContext.getCurrentInstance();
        if (success) {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Registration successful!", "You can now log in."));
            customer = new Customer(); // Reset form
        } else {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registration failed!", "Please try again."));
        }
    }

    // =============== Login ===============
    public String login() {
        loggedInCustomer = customerDao.login(customer.getEmail(), customer.getPhoneNumber());
        FacesContext context = FacesContext.getCurrentInstance();

        if (loggedInCustomer != null) {
            HttpSession session = SessionUtils.getSession();
            session.setAttribute("loggedInCustomer", loggedInCustomer);
            return "customerDashboard.xhtml?faces-redirect=true";
        } else {
            context.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed!", "Invalid credentials."));
            return null;
        }
    }

    // =============== Load Subscriptions ===============
    public void loadSubscriptions() {
        if (loggedInCustomer != null) {
            subscriptions = subscriptionDao.listSubscriptionsByCustomer(loggedInCustomer.getId());
        }
    }

    // =============== Load Billing History ===============
    public void loadBillingHistory() {
        if (loggedInCustomer != null) {
            bills = billingDao.listBillsByCustomer(loggedInCustomer.getId());
        }
    }

    // =============== Logout ===============
    public void logout() throws IOException {
        SessionUtils.invalidateSession();
        FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
    }

    // =============== Getters & Setters ===============
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Customer getLoggedInCustomer() {
        return loggedInCustomer;
    }

    public void setLoggedInCustomer(Customer loggedInCustomer) {
        this.loggedInCustomer = loggedInCustomer;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<Billing> getBills() {
        return bills;
    }

    public void setBills(List<Billing> bills) {
        this.bills = bills;
    }
}

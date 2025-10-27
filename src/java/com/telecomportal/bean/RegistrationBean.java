package com.telecomportal.bean;

import com.telecomportal.dao.CustomerDao;
import com.telecomportal.model.Customer;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class RegistrationBean {
    private Customer customer = new Customer();
    private CustomerDao customerDao = new CustomerDao();

    public String register() {
        boolean success = customerDao.registerCustomer(customer);
        if (success) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Registration successful!", null));
            return "login.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registration failed!", null));
            return null;
        }
    }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
}

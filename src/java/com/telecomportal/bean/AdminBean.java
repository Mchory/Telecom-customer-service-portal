package com.telecomportal.bean;

import com.telecomportal.dao.*;
import com.telecomportal.model.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped
public class AdminBean implements Serializable {

    private List<Customer> customers;
    private List<Subscription> subscriptions;
    private List<Billing> bills;

    private Billing newBill = new Billing();

    private CustomerDao customerDao = new CustomerDao();
    private SubscriptionDAO subscriptionDao = new SubscriptionDAO();
    private BillingDAO billingDao = new BillingDAO();

    // ✅ Automatically load dashboard data when view is created
    @PostConstruct
    public void init() {
        loadDashboardData();
    }

    // ✅ Load all data for admin dashboard
    public void loadDashboardData() {
        loadCustomers();
        loadSubscriptions();
        loadBilling();
    }

    // ✅ Load all customers
    public void loadCustomers() {
        customers = customerDao.listAllCustomers();
    }

    // ✅ Load all subscriptions
    public void loadSubscriptions() {
        subscriptions = subscriptionDao.listAllSubscriptions();
    }

    // ✅ Load all billing records
    public void loadBilling() {
        bills = billingDao.listAllBills();
    }

    // ✅ Delete a customer
    public void deleteCustomer(int id) {
        boolean success = customerDao.deleteCustomer(id);
        if (success) loadCustomers();
    }

    // ✅ Promote a customer to admin
    public void promoteToAdmin(int id) {
        boolean success = customerDao.promoteToAdmin(id);
        if (success) loadCustomers();
    }

    // ✅ Generate a new bill
    public void generateBill() {
        boolean success = billingDao.generateBill(newBill);
        if (success) {
            newBill = new Billing(); // reset form
            loadBilling();
        }
    }

    // ✅ Mark an existing bill as paid
    public void markBillAsPaid(int billId) {
        boolean success = billingDao.markBillAsPaid(billId);
        if (success) loadBilling();
    }

    // --- Getters & Setters ---
    public List<Customer> getCustomers() {
        return customers;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public List<Billing> getBills() {
        return bills;
    }

    public Billing getNewBill() {
        return newBill;
    }

    public void setNewBill(Billing newBill) {
        this.newBill = newBill;
    }
}

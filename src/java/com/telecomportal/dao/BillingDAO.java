package com.telecomportal.dao;

import com.telecomportal.model.Billing;
import com.telecomportal.util.DBConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillingDAO {

    // ✅ Generate a new bill (Admin use)
    public boolean generateBill(Billing bill) {
        String sql = "INSERT INTO billing (customer_id, amount, billing_date, paid) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bill.getCustomerId());
            ps.setDouble(2, bill.getAmount());
            ps.setDate(3, new java.sql.Date(bill.getBillingDate().getTime()));
            ps.setBoolean(4, bill.isPaid());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Mark bill as paid
    public boolean markBillAsPaid(int billId) {
        String sql = "UPDATE billing SET paid = TRUE WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, billId);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ List all bills (Admin view)
    public List<Billing> listAllBills() {
        List<Billing> bills = new ArrayList<>();
        String sql = "SELECT b.id, c.name AS customer_name, b.customer_id, b.amount, b.billing_date, b.paid " +
                     "FROM billing b JOIN customers c ON b.customer_id = c.id " +
                     "ORDER BY b.billing_date DESC";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Billing bill = new Billing();
                bill.setId(rs.getInt("id"));
                bill.setCustomerId(rs.getInt("customer_id"));
                bill.setCustomerName(rs.getString("customer_name"));
                bill.setAmount(rs.getDouble("amount"));
                bill.setBillingDate(rs.getDate("billing_date"));
                bill.setPaid(rs.getBoolean("paid"));
                bills.add(bill);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    // ✅ List bills by customer (Customer view)
    public List<Billing> listBillsByCustomer(int customerId) {
        List<Billing> bills = new ArrayList<>();
        String sql = "SELECT b.id, c.name AS customer_name, b.amount, b.billing_date, b.paid " +
                     "FROM billing b JOIN customers c ON b.customer_id = c.id " +
                     "WHERE c.id = ? ORDER BY b.billing_date DESC";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Billing bill = new Billing();
                bill.setId(rs.getInt("id"));
                bill.setCustomerName(rs.getString("customer_name"));
                bill.setAmount(rs.getDouble("amount"));
                bill.setBillingDate(rs.getDate("billing_date"));
                bill.setPaid(rs.getBoolean("paid"));
                bills.add(bill);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }
}

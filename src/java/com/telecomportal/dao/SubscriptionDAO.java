/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telecomportal.dao;

import com.telecomportal.model.Subscription;
import com.telecomportal.util.DBConnectionManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionDAO {

    // ✅ Activate a service for a customer
    public boolean activateService(int customerId, int serviceId) {
        String sql = "INSERT INTO subscriptions (customer_id, service_id, start_date, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            ps.setInt(2, serviceId);
            ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            ps.setString(4, "ACTIVE");

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Deactivate a service for a customer
    public boolean deactivateService(int subscriptionId) {
        String sql = "UPDATE subscriptions SET status = ? WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "INACTIVE");
            ps.setInt(2, subscriptionId);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ List all subscriptions (for Admin view)
    public List<Subscription> listAllSubscriptions() {
        List<Subscription> subscriptions = new ArrayList<>();
        String sql = "SELECT s.id, c.name AS customer_name, sv.name AS service_name, " +
                     "s.start_date, s.status " +
                     "FROM subscriptions s " +
                     "JOIN customers c ON s.customer_id = c.id " +
                     "JOIN services sv ON s.service_id = sv.id";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Subscription sub = new Subscription();
                sub.setId(rs.getInt("id"));
                sub.setCustomerName(rs.getString("customer_name"));
                sub.setServiceName(rs.getString("service_name"));
                sub.setStartDate(rs.getDate("start_date"));
                sub.setStatus(rs.getString("status"));
                subscriptions.add(sub);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subscriptions;
    }

    // ✅ List subscriptions for a specific customer (for Customer view)
    public List<Subscription> listSubscriptionsByCustomer(int customerId) {
        List<Subscription> subscriptions = new ArrayList<>();
        String sql = "SELECT s.id, c.name AS customer_name, sv.name AS service_name, " +
                     "s.start_date, s.status " +
                     "FROM subscriptions s " +
                     "JOIN customers c ON s.customer_id = c.id " +
                     "JOIN services sv ON s.service_id = sv.id " +
                     "WHERE c.id = ?";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Subscription sub = new Subscription();
                sub.setId(rs.getInt("id"));
                sub.setCustomerName(rs.getString("customer_name"));
                sub.setServiceName(rs.getString("service_name"));
                sub.setStartDate(rs.getDate("start_date"));
                sub.setStatus(rs.getString("status"));
                subscriptions.add(sub);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subscriptions;
    }
}

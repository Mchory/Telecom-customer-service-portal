/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telecomportal.dao;

import com.telecomportal.model.Service;
import com.telecomportal.util.DBConnectionManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {

    // Add a new service (for admin use)
    public boolean addService(Service service) {
        String sql = "INSERT INTO services (name, description, monthly_fee) VALUES (?, ?, ?)";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, service.getName());
            ps.setString(2, service.getDescription());
            ps.setBigDecimal(3, service.getMonthlyFee());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // List all available services
    public List<Service> listAllServices() {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM services";

        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Service s = new Service();
                s.setId(rs.getInt("id"));
                s.setName(rs.getString("name"));
                s.setDescription(rs.getString("description"));
                s.setMonthlyFee(rs.getBigDecimal("monthly_fee"));
                services.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }
}

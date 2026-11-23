package com.ecotrack;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/saveActivity")
public class EcoActivityServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String activity = request.getParameter("activity");
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        String sql = "INSERT INTO eco_activities " +
                     "(user_id, activity, quantity) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Temporary user ID
            ps.setInt(1, 1);

            ps.setString(2, activity);
            ps.setInt(3, quantity);

            ps.executeUpdate();

            response.setContentType("text/html");
            response.getWriter().println(
                "<h2>✅ Eco Activity saved successfully!</h2>"
            );

            response.getWriter().println(
                "<a href='activities.html'>Back to Eco Activities</a>"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.setContentType("text/html");
            response.getWriter().println(
                "<h2>❌ Database error</h2>"
            );

            response.getWriter().println(
                "<p>" + e.getMessage() + "</p>"
            );
        }
    }
}
package com.ecotrack;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/progress")
public class ProgressServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        int ecoActivities = 0;
        double recycling = 0;
        double carbonFootprint = 0;
        int progress = 0;

        try {

            Connection con = DBConnection.getConnection();

            // Count eco activities
            String activitySql =
                    "SELECT COUNT(*) FROM eco_activities " +
                    "WHERE user_id = ?";

            PreparedStatement activityStmt =
                    con.prepareStatement(activitySql);

            activityStmt.setInt(1, 1);

            ResultSet activityRs =
                    activityStmt.executeQuery();

            if (activityRs.next()) {
                ecoActivities = activityRs.getInt(1);
            }


            // Calculate total recycling
            String recyclingSql =
                    "SELECT COALESCE(SUM(quantity), 0) " +
                    "FROM eco_activities " +
                    "WHERE user_id = ? AND activity = 'Recycling'";

            PreparedStatement recyclingStmt =
                    con.prepareStatement(recyclingSql);

            recyclingStmt.setInt(1, 1);

            ResultSet recyclingRs =
                    recyclingStmt.executeQuery();

            if (recyclingRs.next()) {
                recycling = recyclingRs.getDouble(1);
            }


            // Get latest carbon footprint
            String carbonSql =
                    "SELECT COALESCE((travel * 0.21) + " +
                    "(electricity * 0.42) + (meals * 1.5), 0) " +
                    "FROM carbon_records " +
                    "WHERE user_id = ? " +
                    "ORDER BY id DESC LIMIT 1";

            PreparedStatement carbonStmt =
                    con.prepareStatement(carbonSql);

            carbonStmt.setInt(1, 1);

            ResultSet carbonRs =
                    carbonStmt.executeQuery();

            if (carbonRs.next()) {
                carbonFootprint = carbonRs.getDouble(1);
            }


            // Calculate progress
            if (ecoActivities >= 5) {
                progress = 100;
            } else {
                progress = ecoActivities * 20;
            }

            con.close();


            // Return JSON
            out.print("{");

            out.print("\"ecoActivities\":" + ecoActivities + ",");

            out.print("\"recycling\":" + recycling + ",");

            out.print("\"carbonFootprint\":"
                    + String.format("%.2f", carbonFootprint) + ",");

            out.print("\"progress\":" + progress);

            out.print("}");

        } catch (Exception e) {

            e.printStackTrace();

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            out.print("{");

            out.print("\"ecoActivities\":0,");
            out.print("\"recycling\":0,");
            out.print("\"carbonFootprint\":0,");
            out.print("\"progress\":0");

            out.print("}");
        }
    }
}
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

@WebServlet("/rewards")
public class RewardsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        int activities = 0;
        double recycling = 0;

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
                activities = activityRs.getInt(1);
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

            con.close();

            // Calculate eco points
            int points =
                    (activities * 10) + (int)(recycling * 2);

            String badge;

            if (points >= 100) {
                badge = "Eco Champion";
            } else if (points >= 50) {
                badge = "Recycling Hero";
            } else if (points >= 20) {
                badge = "Green Starter";
            } else {
                badge = "Eco Beginner";
            }

            // Return JSON
            out.print("{");

            out.print("\"points\":" + points + ",");

            out.print("\"badge\":\"" + badge + "\",");

            out.print("\"activities\":" + activities + ",");

            out.print("\"recycling\":" + recycling);

            out.print("}");

        } catch (Exception e) {

            e.printStackTrace();

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            out.print("{");

            out.print("\"points\":0,");
            out.print("\"badge\":\"Eco Beginner\",");
            out.print("\"activities\":0,");
            out.print("\"recycling\":0");

            out.print("}");
        }
    }
}
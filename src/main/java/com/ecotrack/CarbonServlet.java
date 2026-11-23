package com.ecotrack;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/saveCarbon")
public class CarbonServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    double travel = Double.parseDouble(request.getParameter("travel"));
	    double electricity = Double.parseDouble(request.getParameter("electricity"));
	    int meals = Integer.parseInt(request.getParameter("meals"));

	    double carbon = (travel * 0.21)
	                  + (electricity * 0.42)
	                  + (meals * 1.5);

	    String sql = "INSERT INTO carbon_records "
	            + "(user_id, travel, electricity, meals) "
	            + "VALUES (?, ?, ?, ?)";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, 1);
	        ps.setDouble(2, travel);
	        ps.setDouble(3, electricity);
	        ps.setInt(4, meals);

	        ps.executeUpdate();

	        response.setContentType("text/html");

	        response.getWriter().println("<html><body>");
	        response.getWriter().println(
	                "<h2>✅ Carbon record saved successfully!</h2>");

	        response.getWriter().println(
	                "<h2>🌱 Estimated Carbon Footprint: "
	                + String.format("%.2f", carbon)
	                + " kg CO₂/day</h2>");

	        response.getWriter().println(
	                "<p>Travel: " + travel + " km</p>");

	        response.getWriter().println(
	                "<p>Electricity: " + electricity + " kWh</p>");

	        response.getWriter().println(
	                "<p>Meals: " + meals + "</p>");

	        response.getWriter().println(
	                "<br><a href='carbon.html'>← Back to Carbon Tracker</a>");

	        response.getWriter().println("</body></html>");

	    } catch (Exception e) {
	        e.printStackTrace();

	        response.setContentType("text/html");
	        response.getWriter().println("<h2>Database error</h2>");
	        response.getWriter().println("<p>" + e.getMessage() + "</p>");
	    }
	}
}
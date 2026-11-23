package com.ecotrack;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ecotrack")
public class EcoTrackServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>EcoTrack</title>");
        out.println("</head>");

        out.println("<body style='font-family:Arial;text-align:center;"
                + "background-color:#f1f8e9;padding:50px;'>");

        out.println("<h1 style='color:#2e7d32;'>🌱 Welcome to EcoTrack</h1>");

        out.println("<p style='font-size:18px;'>"
                + "EcoTrack application is running successfully!"
                + "</p>");

        out.println("</body>");
        out.println("</html>");
    }
}
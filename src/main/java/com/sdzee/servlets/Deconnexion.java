package com.sdzee.servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet( "/deconnexion" )
public class Deconnexion extends HttpServlet {
    private static final long  serialVersionUID = 1L;

    public static final String URL_REDIRECTION  = "/pro/connexion";

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        /* Récupération et destruction de la session en cours */
        HttpSession session = request.getSession();
        session.invalidate();

        /* Redirection vers le Site du Zéro ! */
        response.sendRedirect( URL_REDIRECTION );

    }

    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

    }

}

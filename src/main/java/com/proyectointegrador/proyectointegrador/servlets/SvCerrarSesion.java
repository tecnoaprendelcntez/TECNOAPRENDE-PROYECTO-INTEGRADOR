package com.proyectointegrador.proyectointegrador.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "SvCerrarSesion", urlPatterns = {"/SvCerrarSesion"})
public class SvCerrarSesion extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            // --- VALIDAR TOKEN CSRF antes de cerrar sesión ---
            String tokenSesion = (String) session.getAttribute("csrfToken");
            String tokenRecibido = request.getParameter("csrfToken");

            if (tokenSesion == null || tokenRecibido == null
                    || !tokenSesion.equals(tokenRecibido)) {
                // Token inválido o ausente — posible ataque CSRF
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Solicitud no válida.");
                return;
            }

            // Invalidar la sesión completamente (elimina todos los atributos y la cookie)
            session.invalidate();
        }

        // Redirigir a la página principal
        response.sendRedirect("index.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // GET no debe cerrar sesión — redirigir al inicio sin hacer nada
        response.sendRedirect("index.jsp");
    }

    @Override
    public String getServletInfo() {
        return "Cierra sesión al usuario con validación CSRF";
    }
}

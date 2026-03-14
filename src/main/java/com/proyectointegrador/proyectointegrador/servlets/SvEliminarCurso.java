package com.proyectointegrador.proyectointegrador.servlets;

import Logica.Controladora;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SvEliminarCurso", urlPatterns = {"/SvEliminarCurso"})
public class SvEliminarCurso extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        Controladora control = new Controladora();

        int idCurso = Integer.parseInt(request.getParameter("idCurso"));
        control.borrarCurso(idCurso);

        response.sendRedirect("listaCursos.jsp");
    }

    @Override
    public String getServletInfo() {
        return "Elimina el Curso de la Base de Datos";
    }
}

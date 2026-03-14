package com.proyectointegrador.proyectointegrador.servlets;

import Logica.Controladora;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SvEliminarActividad", urlPatterns = {"/SvEliminarActividad"})
public class SvEliminarActividad extends HttpServlet {

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

        int idActividad = Integer.parseInt(request.getParameter("idActividad"));
        int idCurso = Integer.parseInt(request.getParameter("idCurso"));

        control.borrarActividad(idActividad);
        response.sendRedirect("listaActividades.jsp?idCurso=" + idCurso);
    }

    @Override
    public String getServletInfo() {
        return "Elimina la Actividad";
    }
}

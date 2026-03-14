package com.proyectointegrador.proyectointegrador.servlets;

import Logica.Actividad;
import Logica.Controladora;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SvEditarActividad", urlPatterns = {"/SvEditarActividad"})
public class SvEditarActividad extends HttpServlet {

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
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        int idActividad = Integer.parseInt(request.getParameter("idActividad"));
        int idCurso = Integer.parseInt(request.getParameter("idCurso"));
        String titulo = request.getParameter("titulo");
        String descripcion = request.getParameter("descripcion");
        String tipo = request.getParameter("tipo");

        Actividad actividad = control.traerActividad(idActividad);
        if (actividad != null) {
            actividad.setTitulo(titulo);
            actividad.setDescripcion(descripcion);
            actividad.setTipo(tipo);

            control.editarActividad(actividad);
        }

        response.sendRedirect("listaActividades.jsp?idCurso=" + idCurso);
    }

    @Override
    public String getServletInfo() {
        return "Edita la actividad";
    }
}

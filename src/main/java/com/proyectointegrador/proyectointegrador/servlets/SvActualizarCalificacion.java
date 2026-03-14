package com.proyectointegrador.proyectointegrador.servlets;

import Logica.Controladora;
import Logica.Progreso;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SvActualizarCalificacion", urlPatterns = {"/SvActualizarCalificacion"})
public class SvActualizarCalificacion extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Controladora control = new Controladora();
        int idProgreso = Integer.parseInt(request.getParameter("idProgreso"));
        double nuevaCalificacion = Double.parseDouble(request.getParameter("calificacion"));

        Progreso progreso = control.traerProgreso(idProgreso);
        if (progreso != null) {
            progreso.setCalificacion(nuevaCalificacion);
            control.editarProgreso(progreso);
        }

        response.sendRedirect("listaActividadesInstructor.jsp?idCurso=" + progreso.getCurso().getIdCurso());
    }
    
    @Override
    public String getServletInfo() {
        return "Actualiza la Calificación del Usuario";
    }
}

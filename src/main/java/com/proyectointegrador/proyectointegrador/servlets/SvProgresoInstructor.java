//SvProgresoInstructor.java
package com.proyectointegrador.proyectointegrador.servlets;

import Logica.Controladora;
import Logica.Progreso;
import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SvProgresoInstructor", urlPatterns = {"/SvProgresoInstructor"})
public class SvProgresoInstructor extends HttpServlet {
    
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

    try {
        int idCurso = Integer.parseInt(request.getParameter("idCurso"));
        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
        int idActividad = Integer.parseInt(request.getParameter("idActividad"));

        int idProgreso = 0;
        String idProgStr = request.getParameter("idProgreso");

        if (idProgStr != null && !idProgStr.isEmpty()) {
            idProgreso = Integer.parseInt(idProgStr);
        }

        double calificacion = 0;
        String calStr = request.getParameter("calificacion");
        if (calStr != null && !calStr.isEmpty()) {
            calificacion = Double.parseDouble(calStr);
        }

        String estado = request.getParameter("estado");

        Progreso progreso;

        if (idProgreso > 0) {
            progreso = control.traerProgreso(idProgreso);

            if (progreso != null) {
                progreso.setCalificacion(calificacion);
                progreso.setEstado(estado);
                progreso.setFecha(new Date());
                progreso.setActividad(control.traerActividad(idActividad));

                control.editarProgreso(progreso);
            }

        } else {
            progreso = new Progreso();
            progreso.setCalificacion(calificacion);
            progreso.setEstado(estado);
            progreso.setFecha(new Date());

            progreso.setUsuario(control.traerUsuario(idUsuario));
            progreso.setCurso(control.traerCurso(idCurso));
            progreso.setActividad(control.traerActividad(idActividad));

            control.crearProgreso(progreso);
        }
        response.sendRedirect("listaProgresoInstructor.jsp?idCurso=" + idCurso + "&idUsuario=" + idUsuario);

    } catch (Exception e) {
        e.printStackTrace();
        response.sendRedirect("error.jsp?msg=Error al actualizar el progreso");
    }
    }

    @Override
    public String getServletInfo() {
        return "Servlet que permite al instructor calificar y actualizar el estado de una actividad del estudiante";
    }
}

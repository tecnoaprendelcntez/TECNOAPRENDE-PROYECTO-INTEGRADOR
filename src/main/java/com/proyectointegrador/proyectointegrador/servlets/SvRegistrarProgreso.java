package com.proyectointegrador.proyectointegrador.servlets;

import Logica.Actividad;
import Logica.Controladora;
import Logica.Curso;
import Logica.Progreso;
import Logica.Usuario;
import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SvRegistrarProgreso", urlPatterns = {"/SvRegistrarProgreso"})
public class SvRegistrarProgreso extends HttpServlet {
    
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

        int idCurso = Integer.parseInt(request.getParameter("idCurso"));
        int idActividad = Integer.parseInt(request.getParameter("idActividad"));
        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
        String estado = request.getParameter("estado");
        double calificacion = Double.parseDouble(request.getParameter("calificacion"));

        String[] respuestas = request.getParameterValues("respuesta");
        String respuestaFinal = "";
        if (respuestas != null) {
            respuestaFinal = String.join(", ", respuestas);
        } else {
            String respuestaTexto = request.getParameter("respuestaTexto");
            if (respuestaTexto != null) {
                respuestaFinal = respuestaTexto;
            }
        }

        Curso curso = control.traerCurso(idCurso);
        Actividad actividad = control.traerActividad(idActividad);
        Usuario usuario = control.traerUsuario(idUsuario);

        Progreso progreso = new Progreso();
        progreso.setCurso(curso);
        progreso.setActividad(actividad);
        progreso.setUsuario(usuario);
        progreso.setEstado(estado);
        progreso.setCalificacion(calificacion);
        progreso.setFecha(new Date());

        control.crearProgreso(progreso);
        response.sendRedirect("cursoPrincipiante.jsp");
    }

    @Override
    public String getServletInfo() {
        return "Registra el progreso y las respuestas de cada usuario";
    }
}

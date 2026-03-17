//SvProgresoInstructor.java
package com.proyectointegrador.proyectointegrador.servlets;

import Logica.Controladora;
import Logica.Progreso;
import Logica.Usuario;
import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "SvProgresoInstructor", urlPatterns = {"/SvProgresoInstructor"})
public class SvProgresoInstructor extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("panelInstructor.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        //VERIFICAR SESIÓN Y ROL
        HttpSession session = request.getSession(false);
        Usuario instructor = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;

        if (instructor == null || !"instructor".equalsIgnoreCase(instructor.getRol())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado.");
            return;
        }
        
        //VERIFICAR TOKEN CSRF
        String csrfToken = request.getParameter("csrfToken");
        String csrfEnSession = (String) session.getAttribute("csrfToken");

        if (csrfToken == null || !csrfToken.equals(csrfEnSession)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token CSRF inválido.");
            return;
        }
        
        //PROCESAR PETICIÓN
        try {
            int idCurso = Integer.parseInt(request.getParameter("idCurso"));
            int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
            int idActividad = Integer.parseInt(request.getParameter("idActividad"));

            int idProgreso = 0;
            String idProgStr = request.getParameter("idProgreso");
            if (idProgStr != null && !idProgStr.isEmpty()) {
                idProgreso = Integer.parseInt(idProgStr.trim());
            }

            double calificacion = 0;
            String calStr = request.getParameter("calificacion");
            if (calStr != null && !calStr.isEmpty()) {
                calificacion = Double.parseDouble(calStr.trim());
                // Validar rango de calificación
                if (calificacion < 0) calificacion = 0;
                if (calificacion > 100) calificacion = 100;
            }

            String estado = request.getParameter("estado");
            if (estado == null || estado.trim().isEmpty()) {
                estado = "sin estado";
            }
            
            Controladora control = new Controladora();
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
            response.sendRedirect("error.jsp?msg=Error+al+actualizar+el+progreso");
        }
    }

    @Override
    public String getServletInfo() {
        return "Actualizar progreso de estudiante — solo accesible por instructores autenticados";
    }
}

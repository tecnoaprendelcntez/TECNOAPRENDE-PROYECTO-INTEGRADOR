package com.proyectointegrador.proyectointegrador.servlets;

import Logica.Controladora;
import Logica.EvaluacionFinal;
import Logica.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "SvIniciarEvaluacion", urlPatterns = {"/SvIniciarEvaluacion"})
public class SvIniciarEvaluacion extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Controladora control = new Controladora();

        String sIdCurso = request.getParameter("idCurso");
        if (sIdCurso == null) {
            response.sendError(400, "Falta idCurso");
            return;
        }
        Integer idCurso = Integer.parseInt(sIdCurso);

        EvaluacionFinal ev = control.getEvaluacionPorCurso(idCurso);
        String vista = request.getParameter("vista");
        request.setAttribute("idCurso", idCurso);
        request.setAttribute("vista", vista);
        if (ev == null) {
            request.setAttribute("mensaje", "No hay evaluación final para este curso.");

            if ("avanzado".equals(vista)) {
                request.getRequestDispatcher("cursoAvanzado.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("cursoPrincipiante.jsp").forward(request, response);
            }
            return;
        }

        HttpSession ses = request.getSession();
        Usuario usuario = (Usuario) ses.getAttribute("usuarioLogueado");

        if (usuario == null) {
            response.sendRedirect("login_registro.jsp");
            return;
        }

        Integer idUsuario = usuario.getId();

        boolean puede = control.puedeIntentar(idUsuario, ev.getIdEvaluacion());
        request.setAttribute("puedeIntentar", puede);

        int intentos = (int) control.contarIntentos(idUsuario, ev.getIdEvaluacion());
        request.setAttribute("intentos", intentos);
        request.setAttribute("evaluacion", ev);
        request.getRequestDispatcher("evaluacion.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(405, "POST no soportado en SvIniciarEvaluacion");
    }

    @Override
    public String getServletInfo() {
        return "Inicia la evaluación";
    }
}


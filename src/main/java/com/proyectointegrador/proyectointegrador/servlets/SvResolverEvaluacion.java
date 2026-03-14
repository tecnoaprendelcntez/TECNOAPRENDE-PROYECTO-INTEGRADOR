package com.proyectointegrador.proyectointegrador.servlets;

import Logica.Controladora;
import Logica.EvaluacionFinal;
import Logica.PreguntaEvaluacion;
import Logica.ResultadoEvaluacion;
import Logica.Usuario;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "SvResolverEvaluacion", urlPatterns = {"/SvResolverEvaluacion"})
public class SvResolverEvaluacion extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Controladora control = new Controladora();

        String sIdEvaluacion = request.getParameter("idEvaluacion");
        if (sIdEvaluacion == null) {
            response.sendError(400, "Falta idEvaluacion");
            return;
        }
        Integer idEvaluacion = Integer.parseInt(sIdEvaluacion);

        HttpSession ses = request.getSession();
        Usuario usuario = (Usuario) ses.getAttribute("usuarioLogueado");

        if (usuario == null) {
            response.sendRedirect("login_registro.jsp");
            return;
        }

        Integer idUsuario = usuario.getId();

        boolean puede = control.puedeIntentar(idUsuario, idEvaluacion);
        if (!puede) {

            EvaluacionFinal evaluacion = control.getEvaluacionPorCurso(idEvaluacion);

            request.setAttribute("evaluacion", evaluacion);
            request.setAttribute("mensajeIntentos", 
                "Ya realizaste los 2 intentos. No puedes volver a enviar la evaluación.");

            request.getRequestDispatcher("evaluacion.jsp").forward(request, response);
            return;
        }


        Map<Integer, String> respuestas = new HashMap<>();
        Map<String, String[]> params = request.getParameterMap();

        for (String key : params.keySet()) {
            if (key.startsWith("resp_")) {
                try {
                    Integer idPreg = Integer.parseInt(key.substring(5));
                    String val = request.getParameter(key);
                    respuestas.put(idPreg, val);
                } catch (NumberFormatException ex) {
                }
            }
        }

        Map<String, Object> resumen = control.generarResumen(idEvaluacion, respuestas);

        ResultadoEvaluacion resultadoGuardado =
                control.procesarYGuardarResultado(idUsuario, idEvaluacion, respuestas);

        EvaluacionFinal evaluacion = control.getEvaluacionPorCurso(idEvaluacion);
        List<PreguntaEvaluacion> preguntas = control.getPreguntasDeEvaluacion(idEvaluacion);

        request.setAttribute("evaluacion", evaluacion);
        request.setAttribute("preguntas", preguntas);
        request.setAttribute("resumen", resumen);
        request.setAttribute("resultado", resultadoGuardado);
        request.setAttribute("intentos", resultadoGuardado.getIntentos());
        request.getRequestDispatcher("evaluacion.jsp").forward(request, response);
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(405, "GET no soportado en SvResolverEvaluacion");
    }

    @Override
    public String getServletInfo() {
        return "Procesa una evaluación y guarda el resultado";
    }
}

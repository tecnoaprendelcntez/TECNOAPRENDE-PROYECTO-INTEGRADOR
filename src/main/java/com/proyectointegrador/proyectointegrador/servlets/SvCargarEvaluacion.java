package com.proyectointegrador.proyectointegrador.servlets;

import Logica.Controladora;
import Logica.EvaluacionFinal;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SvCargarEvaluacion", urlPatterns = {"/SvCargarEvaluacion"})
public class SvCargarEvaluacion extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Controladora control = new Controladora();

        int idEv = Integer.parseInt(request.getParameter("idEvaluacion"));
        EvaluacionFinal evaluacion = control.getEvaluacionPorCurso(idEv);

        request.setAttribute("evaluacion", evaluacion);

        request.getRequestDispatcher("evaluacion.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Carga la evaluacion por curso";
    }
}

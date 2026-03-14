package com.proyectointegrador.proyectointegrador.servlets;

import Logica.Controladora;
import Logica.Instructor;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SvEliminarInstructor", urlPatterns = {"/SvEliminarInstructor"})
public class SvEliminarInstructor extends HttpServlet {

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

        int idInstructor = Integer.parseInt(request.getParameter("idInstructor"));

        Instructor instructor = control.traerInstructor(idInstructor);
        if (instructor != null) {
            int idUsuario = instructor.getUsuario().getId();

            control.borrarInstructor(idInstructor);
            control.borrarUsuario(idUsuario);
        }

        response.sendRedirect("listaInstructores.jsp");
    }

    @Override
    public String getServletInfo() {
        return "Elimina Instructor por ID";
    }
}

package com.proyectointegrador.proyectointegrador.servlets;

import Logica.Controladora;
import Logica.Curso;
import Logica.Instructor;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SvEditarCurso", urlPatterns = {"/SvEditarCurso"})
public class SvEditarCurso extends HttpServlet {
    
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

        int idCurso = Integer.parseInt(request.getParameter("idCurso"));
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        int idInstructor = Integer.parseInt(request.getParameter("idInstructor"));

        Curso curso = control.traerCurso(idCurso);
        Instructor instructor = control.traerInstructor(idInstructor);

        if (curso != null && instructor != null) {
            curso.setNombre(nombre);
            curso.setDescripcion(descripcion);
            curso.setInstructor(instructor);

            control.editarCurso(curso);
        }

        response.sendRedirect("listaCursos.jsp");
    }

    @Override
    public String getServletInfo() {
        return "Edita la informacion del Curso";
    }
}

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

@WebServlet(name = "SvCrearCurso", urlPatterns = {"/SvCrearCurso"})
public class SvCrearCurso extends HttpServlet {

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

        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        int idInstructor = Integer.parseInt(request.getParameter("idInstructor"));

        Instructor instructor = control.traerInstructor(idInstructor);

        Curso nuevoCurso = new Curso();
        nuevoCurso.setNombre(nombre);
        nuevoCurso.setDescripcion(descripcion);
        nuevoCurso.setInstructor(instructor);

        control.crearCurso(nuevoCurso);

        response.sendRedirect("listaCursos.jsp");
    }

    @Override
    public String getServletInfo() {
        return "Agrega Cursos a la Base de Datos";
    }
}

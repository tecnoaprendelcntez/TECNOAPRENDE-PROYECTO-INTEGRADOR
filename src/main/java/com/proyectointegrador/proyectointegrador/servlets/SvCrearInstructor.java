package com.proyectointegrador.proyectointegrador.servlets;

import Logica.Controladora;
import Logica.Instructor;
import Logica.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SvCrearInstructor", urlPatterns = {"/SvCrearInstructor"})
public class SvCrearInstructor extends HttpServlet {

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
        String apellidos = request.getParameter("apellidos");
        String nom_usuario = request.getParameter("usuario");
        String contrasena = request.getParameter("contrasena");

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setApellidos(apellidos);
        nuevoUsuario.setNom_usuario(nom_usuario);
        nuevoUsuario.setContrasena(contrasena);
        nuevoUsuario.setRol("instructor");

        control.crearUsuario(nuevoUsuario);
        
        Usuario usuarioGuardado = control.buscarUsuarioPorNombre(nom_usuario);

        if (usuarioGuardado != null) {
            Instructor nuevoInstructor = new Instructor();
            nuevoInstructor.setUsuario(usuarioGuardado);
            control.crearInstructor(nuevoInstructor);
        }

        response.sendRedirect("listaInstructores.jsp");
    }
    
    @Override
    public String getServletInfo() {
        return "Crea un nuevo instructor";
    }
}

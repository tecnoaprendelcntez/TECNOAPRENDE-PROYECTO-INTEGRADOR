package com.proyectointegrador.proyectointegrador.servlets;

import Logica.Controladora;
import Logica.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SvEditarInstructor", urlPatterns = {"/SvEditarInstructor"})
public class SvEditarInstructor extends HttpServlet {
    
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

        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
        int idInstructor = Integer.parseInt(request.getParameter("idInstructor"));

        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String nom_usuario = request.getParameter("nom_usuario");
        String contrasena = request.getParameter("contrasena");

        Usuario usuario = control.traerUsuario(idUsuario);
        if (usuario != null) {
            usuario.setNombre(nombre);
            usuario.setApellidos(apellidos);
            usuario.setNom_usuario(nom_usuario);
            usuario.setContrasena(contrasena);

            control.editarUsuario(usuario);
        }

        response.sendRedirect("listaInstructores.jsp");
    }

    @Override
    public String getServletInfo() {
        return "Edita Instructores por ID";
    }
}

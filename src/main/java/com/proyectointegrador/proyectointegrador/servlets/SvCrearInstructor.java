package com.proyectointegrador.proyectointegrador.servlets;

import Logica.Controladora;
import Logica.Instructor;
import Logica.PasswordUtil;
import Logica.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "SvCrearInstructor", urlPatterns = {"/SvCrearInstructor"})
public class SvCrearInstructor extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("panelAdmin.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
                
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        //VERIFICAR SESIÓN Y ROL
        HttpSession session = request.getSession(false);
        Usuario admin = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;

        if (admin == null || !"admin".equalsIgnoreCase(admin.getRol())) {
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
        
        //OBTENER Y VALIDAR PARÁMETROS
        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String nom_usuario = request.getParameter("usuario");
        String contrasena = request.getParameter("contrasena");
        
        if (nombre == null || apellidos == null || nom_usuario == null || contrasena == null
                || nombre.trim().isEmpty() || apellidos.trim().isEmpty()
                || nom_usuario.trim().isEmpty() || contrasena.trim().isEmpty()) {
            response.sendRedirect("listaInstructores.jsp");
            return;
        }
        
        if (nombre.length() > 100 || apellidos.length() > 100 || nom_usuario.length() > 50) {
            response.sendRedirect("listaInstructores.jsp");
            return;
        }
        
        if (contrasena.length() < 8 || contrasena.length() > 200) {
            response.sendRedirect("listaInstructores.jsp");
            return;
        }
        
        Controladora control = new Controladora();
        
        // Verificar que el nombre de usuario no esté ya en uso
        if (control.existeUsuario(nom_usuario.trim())) {
            request.setAttribute("error", "El nombre de usuario ya está registrado.");
            request.getRequestDispatcher("listaInstructores.jsp").forward(request, response);
            return;
        }
        
        //HASHEAR CONTRASEÑA Y CREAR INSTRUCTOR
        String contrasenaHasheada = PasswordUtil.hashear(contrasena);

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre.trim());
        nuevoUsuario.setApellidos(apellidos.trim());
        nuevoUsuario.setNom_usuario(nom_usuario.trim());
        nuevoUsuario.setContrasena(contrasenaHasheada);
        nuevoUsuario.setRol("instructor");

        control.crearUsuario(nuevoUsuario);
        
        Usuario usuarioGuardado = control.buscarUsuarioPorNombre(nom_usuario.trim());
        if (usuarioGuardado != null) {
            Instructor nuevoInstructor = new Instructor();
            nuevoInstructor.setUsuario(usuarioGuardado);
            control.crearInstructor(nuevoInstructor);
        }

        response.sendRedirect("listaInstructores.jsp");
    }
    
    @Override
    public String getServletInfo() {
        return "Crear instructor con verificación de sesión, CSRF y hashing de contraseña";
    }
}

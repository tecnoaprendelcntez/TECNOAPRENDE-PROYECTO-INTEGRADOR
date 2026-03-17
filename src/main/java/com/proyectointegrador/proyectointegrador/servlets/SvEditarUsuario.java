package com.proyectointegrador.proyectointegrador.servlets;

import Logica.Controladora;
import Logica.PasswordUtil;
import Logica.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet(name = "SvEditarUsuario", urlPatterns = {"/SvEditarUsuario"})
public class SvEditarUsuario extends HttpServlet {

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
        String idStr = request.getParameter("id");
        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String nom_usuario = request.getParameter("nom_usuario");
        String nuevaContrasena = request.getParameter("contrasena");
        
        if (idStr == null || nombre == null || apellidos == null || nom_usuario == null) {
            response.sendRedirect("listaUsuarios.jsp");
            return;
        }
        
        int id;
        try {
            id = Integer.parseInt(idStr.trim());
        } catch (NumberFormatException e) {
            response.sendRedirect("listaUsuarios.jsp");
            return;
        }
        
        // Limitar longitudes para prevenir desbordamiento
        if (nombre.length() > 100 || apellidos.length() > 100 || nom_usuario.length() > 50) {
            response.sendRedirect("listaUsuarios.jsp");
            return;
        }
        
        //ACTUALIZAR USUARIO
        Controladora control = new Controladora();
        Usuario usuario = control.traerUsuario(id);

        if (usuario != null) {
            usuario.setNombre(nombre.trim());
            usuario.setApellidos(apellidos.trim());
            usuario.setNom_usuario(nom_usuario.trim());
            
            // Solo actualizar contraseña si se proporcionó una nueva
            if (nuevaContrasena != null && !nuevaContrasena.trim().isEmpty()) {
                if (nuevaContrasena.length() >= 8 && nuevaContrasena.length() <= 200) {
                    usuario.setContrasena(PasswordUtil.hashear(nuevaContrasena));
                }
            }
            // Si viene vacía, se conserva el hash existente (no se modifica)
            control.editarUsuario(usuario);
        }

        response.sendRedirect("listaUsuarios.jsp");
    }

    @Override
    public String getServletInfo() {
        return "Editar usuario con verificación de sesión, CSRF y hashing de contraseña";
    }
}

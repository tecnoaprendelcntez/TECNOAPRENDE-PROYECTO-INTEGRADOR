package com.proyectointegrador.proyectointegrador.servlets;

import Logica.Controladora;
import Logica.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "SvEliminar", urlPatterns = {"/SvEliminar"})
public class SvEliminar extends HttpServlet {
    
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
        
        //OBTENER Y VALIDAR ID
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendRedirect("listaUsuarios.jsp");
            return;
        }

        int idEliminar;
        try {
            idEliminar = Integer.parseInt(idStr.trim());
        } catch (NumberFormatException e) {
            response.sendRedirect("listaUsuarios.jsp");
            return;
        }
        
        //EVITAR AUTO-ELIMINACIÓN
        if (idEliminar == admin.getId()) {
            request.setAttribute("error", "No puedes eliminar tu propia cuenta de administrador.");
            request.getRequestDispatcher("listaUsuarios.jsp").forward(request, response);
            return;
        }
        
        //ELIMINAR USUARIO
        Controladora control = new Controladora();
        control.borrarUsuario(idEliminar);
        
        response.sendRedirect("listaUsuarios.jsp");
    }

    @Override
    public String getServletInfo() {
        return "Eliminar usuario con verificación de sesión, rol admin y CSRF";
    }
}

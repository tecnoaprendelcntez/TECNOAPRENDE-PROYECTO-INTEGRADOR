package com.proyectointegrador.proyectointegrador.servlets;

import Logica.Controladora;
import Logica.Usuario;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "SvLoginAdmin", urlPatterns = {"/SvLoginAdmin"})
public class SvLoginAdmin extends HttpServlet {

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
        
        String usuario = request.getParameter("usuario");
        String contrasena = request.getParameter("contrasena");

        List<Usuario> listaUsuarios = control.traerUsuarios();
        Usuario admin = null;

        for (Usuario u : listaUsuarios) {
            if (u.getNom_usuario().equals(usuario) && u.getContrasena().equals(contrasena) && "admin".equals(u.getRol())) {
                admin = u;
                break;
            }
        }

        if (admin != null) {
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogueado", admin);
            response.sendRedirect("panelAdmin.jsp");
        } else {
            request.setAttribute("error", "Usuario o contraseña incorrectos.");
            request.getRequestDispatcher("loginAdmin.jsp").forward(request, response);
        }
    }
    
    @Override
    public String getServletInfo() {
        return "Inicia Sesion el Administrador";
    }
}

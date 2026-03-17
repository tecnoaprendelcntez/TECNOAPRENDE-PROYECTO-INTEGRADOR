package com.proyectointegrador.proyectointegrador.servlets;

import Logica.Controladora;
import Logica.PasswordUtil;
import Logica.Usuario;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "SvLoginAdmin", urlPatterns = {"/SvLoginAdmin"})
public class SvLoginAdmin extends HttpServlet {
    
    private static final int MAX_INTENTOS = 5;
    private static final long TIEMPO_BLOQUEO_MS = 15 * 60 * 1000L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("loginAdmin.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String usuario = request.getParameter("usuario");
        String contrasena = request.getParameter("contrasena");
        
        // VALIDACIÓN BÁSICA
        if (usuario == null || usuario.trim().isEmpty()
                || contrasena == null || contrasena.trim().isEmpty()) {
            request.setAttribute("error", "Complete todos los campos.");
            request.getRequestDispatcher("loginAdmin.jsp").forward(request, response);
            return;
        }
        
        usuario = usuario.trim();
        if (usuario.length() > 50 || contrasena.length() > 200) {
            request.setAttribute("error", "Datos inválidos.");
            request.getRequestDispatcher("loginAdmin.jsp").forward(request, response);
            return;
        }
        
        //PROTECCIÓN ANTI-FUERZA BRUTA (por sesión)
        HttpSession session = request.getSession(true);
        Integer intentosFallidos = (Integer) session.getAttribute("adminIntentosFallidos");
        Long tiempoBloqueo = (Long) session.getAttribute("adminTiempoBloqueo");
        
        if (intentosFallidos == null) intentosFallidos = 0;

        if (tiempoBloqueo != null) {
            long tiempoRestante = tiempoBloqueo - System.currentTimeMillis();
            if (tiempoRestante > 0) {
                long minutosRestantes = (tiempoRestante / 1000 / 60) + 1;
                request.setAttribute("error",
                    "Demasiados intentos fallidos. Intente de nuevo en " + minutosRestantes + " minuto(s).");
                request.getRequestDispatcher("loginAdmin.jsp").forward(request, response);
                return;
            } else {
                session.removeAttribute("adminIntentosFallidos");
                session.removeAttribute("adminTiempoBloqueo");
                intentosFallidos = 0;
            }
        }
        
        //BUSCAR ADMIN Y VERIFICAR CON BCRYPT
        Controladora control = new Controladora();
        Usuario adminEncontrado = control.buscarUsuarioPorNombre(usuario);
        
        boolean credencialesCorrectas = false;
        if (adminEncontrado != null && "admin".equals(adminEncontrado.getRol())) {
            credencialesCorrectas = PasswordUtil.verificar(contrasena, adminEncontrado.getContrasena());
        }
        
        if (credencialesCorrectas) {
            //INICIO DE SESIÓN EXITOSO
            session.invalidate();
            session = request.getSession(true);

            session.setAttribute("usuarioLogueado", adminEncontrado);
            session.setAttribute("csrfToken", UUID.randomUUID().toString());
            session.setMaxInactiveInterval(30 * 60);

            response.sendRedirect("panelAdmin.jsp");
        } else {
            intentosFallidos++;
            session.setAttribute("adminIntentosFallidos", intentosFallidos);

            if (intentosFallidos >= MAX_INTENTOS) {
                session.setAttribute("adminTiempoBloqueo",
                    System.currentTimeMillis() + TIEMPO_BLOQUEO_MS);
                request.setAttribute("error",
                    "Cuenta bloqueada por demasiados intentos. Intente en 15 minutos.");
            } else {
                int restantes = MAX_INTENTOS - intentosFallidos;
                request.setAttribute("error",
                    "Usuario o contraseña incorrectos. Intentos restantes: " + restantes);
            }

            request.getRequestDispatcher("loginAdmin.jsp").forward(request, response);
        }
    }
    
    @Override
    public String getServletInfo() {
        return "Login de administrador con BCrypt, anti-fuerza bruta y anti-session fixation";
    }
}

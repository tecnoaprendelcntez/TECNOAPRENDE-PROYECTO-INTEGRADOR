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

@WebServlet(name = "SvInicioSesion", urlPatterns = {"/SvInicioSesion"})
public class SvInicioSesion extends HttpServlet {

    // Máximo de intentos fallidos antes de bloqueo temporal
    private static final int MAX_INTENTOS = 5;
    // Tiempo de bloqueo en milisegundos (15 minutos)
    private static final long TIEMPO_BLOQUEO_MS = 15 * 60 * 1000L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // GET no debe procesar login — redirigir al formulario
        response.sendRedirect("login_registro.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String nom_usuario = request.getParameter("usuario");
        String contrasena = request.getParameter("contrasena");

        // --- 1. VALIDACIÓN BÁSICA DE CAMPOS ---
        if (nom_usuario == null || nom_usuario.trim().isEmpty()
                || contrasena == null || contrasena.trim().isEmpty()) {
            request.setAttribute("error", "Por favor, complete todos los campos.");
            request.getRequestDispatcher("login_registro.jsp").forward(request, response);
            return;
        }

        nom_usuario = nom_usuario.trim();

        // Limitar longitud para evitar ataques de desbordamiento
        if (nom_usuario.length() > 50 || contrasena.length() > 200) {
            request.setAttribute("error", "Datos inválidos.");
            request.getRequestDispatcher("login_registro.jsp").forward(request, response);
            return;
        }

        // --- 2. PROTECCIÓN CONTRA FUERZA BRUTA (por sesión/IP) ---
        HttpSession session = request.getSession(true);

        Integer intentosFallidos = (Integer) session.getAttribute("intentosFallidos");
        Long tiempoBloqueo = (Long) session.getAttribute("tiempoBloqueo");

        if (intentosFallidos == null) intentosFallidos = 0;

        // Verificar si está bloqueado temporalmente
        if (tiempoBloqueo != null) {
            long tiempoRestante = tiempoBloqueo - System.currentTimeMillis();
            if (tiempoRestante > 0) {
                long minutosRestantes = (tiempoRestante / 1000 / 60) + 1;
                request.setAttribute("error",
                    "Demasiados intentos fallidos. Intente de nuevo en " + minutosRestantes + " minuto(s).");
                request.getRequestDispatcher("login_registro.jsp").forward(request, response);
                return;
            } else {
                // El bloqueo expiró, reiniciar contadores
                session.removeAttribute("intentosFallidos");
                session.removeAttribute("tiempoBloqueo");
                intentosFallidos = 0;
            }
        }

        // --- 3. BUSCAR USUARIO EN BD Y VERIFICAR CONTRASEÑA CON BCRYPT ---
        Controladora control = new Controladora();
        // Usa el método existente en tu Controladora: buscarUsuarioPorNombre()
        Usuario usuarioEncontrado = control.buscarUsuarioPorNombre(nom_usuario);

        boolean credencialesCorrectas = false;
        if (usuarioEncontrado != null) {
            // Verificar contraseña usando BCrypt
            credencialesCorrectas = PasswordUtil.verificar(contrasena, usuarioEncontrado.getContrasena());
        }

        if (credencialesCorrectas) {
            // --- 4. INICIO DE SESIÓN EXITOSO ---
            if ("admin".equalsIgnoreCase(usuarioEncontrado.getRol())) {
                request.setAttribute("error", 
                    "Acceso Denegado.");
                request.getRequestDispatcher("login_registro.jsp").forward(request, response);
                return;
            }
            
            // Invalidar sesión anterior y crear una nueva (previene Session Fixation)
            session.invalidate();
            session = request.getSession(true);

            // Guardar usuario en sesión
            session.setAttribute("usuarioLogueado", usuarioEncontrado);

            // Generar y guardar token CSRF para esta sesión
            String csrfToken = UUID.randomUUID().toString();
            session.setAttribute("csrfToken", csrfToken);

            // Tiempo de inactividad máximo: 30 minutos
            session.setMaxInactiveInterval(30 * 60);

            // Si la contraseña fue restablecida por un instructor, forzar
            // cambio de contraseña antes de continuar a cualquier otra página.
            if (usuarioEncontrado.isRequiereCambioContrasena()) {
                response.sendRedirect("cambiarContrasena.jsp");
                return;
            }

            // Redirigir según rol
            switch (usuarioEncontrado.getRol()) {
                case "instructor":
                    response.sendRedirect("panelInstructor.jsp");
                    break;
                case "usuario":
                default:
                    response.sendRedirect("index.jsp");
                    break;
            }

        } else {
            // --- 5. CREDENCIALES INCORRECTAS: registrar intento fallido ---
            intentosFallidos++;
            session.setAttribute("intentosFallidos", intentosFallidos);

            if (intentosFallidos >= MAX_INTENTOS) {
                session.setAttribute("tiempoBloqueo",
                    System.currentTimeMillis() + TIEMPO_BLOQUEO_MS);
                request.setAttribute("error",
                    "Cuenta bloqueada temporalmente por demasiados intentos. Intente en 15 minutos.");
            } else {
                int intentosRestantes = MAX_INTENTOS - intentosFallidos;
                request.setAttribute("error",
                    "Usuario o contraseña incorrectos. Intentos restantes: " + intentosRestantes);
            }

            request.getRequestDispatcher("login_registro.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Inicia sesión al usuario con protección BCrypt y anti-fuerza bruta";
    }
}

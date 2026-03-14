package com.proyectointegrador.proyectointegrador.servlets;

import Logica.Usuario;
import Logica.Controladora;
import Logica.PasswordUtil;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "SvUsuarios", urlPatterns = {"/SvUsuarios"})
public class SvUsuarios extends HttpServlet {

    // Solo letras, números, guiones y guion bajo. Sin espacios.
    private static final Pattern PATRON_USUARIO = Pattern.compile("^[a-zA-Z0-9_\\-]{3,50}$");

    // Al menos 8 caracteres
    private static final int MIN_LONGITUD_CONTRASENA = 8;
    private static final int MAX_LONGITUD_CONTRASENA = 100;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String nombre     = request.getParameter("nombre");
        String apellidos  = request.getParameter("apellidos");
        String nom_usuario = request.getParameter("usuario");
        String contrasena = request.getParameter("contrasena");

        // --- 1. VALIDACIÓN DE CAMPOS OBLIGATORIOS ---
        if (esVacio(nombre) || esVacio(apellidos)
                || esVacio(nom_usuario) || esVacio(contrasena)) {
            request.setAttribute("errorMessage", "Todos los campos son obligatorios.");
            request.getRequestDispatcher("/login_registro.jsp").forward(request, response);
            return;
        }

        nombre     = nombre.trim();
        apellidos  = apellidos.trim();
        nom_usuario = nom_usuario.trim();

        // --- 2. VALIDAR LONGITUD Y FORMATO ---
        if (nombre.length() > 100) {
            request.setAttribute("errorMessage", "El nombre no puede superar 100 caracteres.");
            request.getRequestDispatcher("/login_registro.jsp").forward(request, response);
            return;
        }

        if (apellidos.length() > 100) {
            request.setAttribute("errorMessage", "Los apellidos no pueden superar 100 caracteres.");
            request.getRequestDispatcher("/login_registro.jsp").forward(request, response);
            return;
        }

        if (!PATRON_USUARIO.matcher(nom_usuario).matches()) {
            request.setAttribute("errorMessage",
                "El nombre de usuario solo puede contener letras, números, guión y guión bajo. "
                + "Mínimo 3 caracteres, máximo 50.");
            request.getRequestDispatcher("/login_registro.jsp").forward(request, response);
            return;
        }

        if (contrasena.length() < MIN_LONGITUD_CONTRASENA) {
            request.setAttribute("errorMessage",
                "La contraseña debe tener al menos " + MIN_LONGITUD_CONTRASENA + " caracteres.");
            request.getRequestDispatcher("/login_registro.jsp").forward(request, response);
            return;
        }

        if (contrasena.length() > MAX_LONGITUD_CONTRASENA) {
            request.setAttribute("errorMessage", "La contraseña es demasiado larga.");
            request.getRequestDispatcher("/login_registro.jsp").forward(request, response);
            return;
        }

        // --- 3. VERIFICAR SI EL USUARIO YA EXISTE ---
        Controladora control = new Controladora();

        if (control.existeUsuario(nom_usuario)) {
            request.setAttribute("errorMessage", "El nombre de usuario ya está registrado.");
            request.getRequestDispatcher("/login_registro.jsp").forward(request, response);
            return;
        }

        // --- 4. HASHEAR LA CONTRASEÑA ANTES DE GUARDAR ---
        String contrasenaHasheada = PasswordUtil.hashear(contrasena);

        // --- 5. CREAR Y GUARDAR EL USUARIO ---
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellidos(apellidos);
        usuario.setNom_usuario(nom_usuario);
        usuario.setContrasena(contrasenaHasheada); // Siempre guardar el hash, nunca texto plano
        usuario.setRol("usuario");

        control.crearUsuario(usuario);

        int idNuevoUsuario = usuario.getId();
        control.inicializarProgresoParaUsuario(idNuevoUsuario);

        // --- 6. INICIAR SESIÓN AUTOMÁTICAMENTE TRAS EL REGISTRO ---
        HttpSession session = request.getSession(true);
        session.setAttribute("usuarioLogueado", usuario);

        // Generar token CSRF para la nueva sesión
        String csrfToken = UUID.randomUUID().toString();
        session.setAttribute("csrfToken", csrfToken);

        // Tiempo de inactividad máximo: 30 minutos
        session.setMaxInactiveInterval(30 * 60);

        response.sendRedirect("index.jsp");
    }

    private boolean esVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    @Override
    public String getServletInfo() {
        return "Registra usuarios con contraseña hasheada (BCrypt) y validación de campos";
    }
}

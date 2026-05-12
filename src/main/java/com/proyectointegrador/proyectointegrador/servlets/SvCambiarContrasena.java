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

/**
 * Permite al usuario cambiar su propia contraseña.
 * Se usa para el flujo de cambio obligatorio tras un restablecimiento por
 * parte del instructor (cuando requiereCambioContrasena = true), pero también
 * funciona como cambio voluntario.
 */
@WebServlet(name = "SvCambiarContrasena", urlPatterns = {"/SvCambiarContrasena"})
public class SvCambiarContrasena extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("cambiarContrasena.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 1. Verificar sesión
        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;
        if (usuario == null) {
            response.sendRedirect("login_registro.jsp");
            return;
        }

        // 2. Verificar token CSRF
        String csrfToken = request.getParameter("csrfToken");
        String csrfEnSession = (String) session.getAttribute("csrfToken");
        if (csrfToken == null || !csrfToken.equals(csrfEnSession)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token CSRF inválido.");
            return;
        }

        // 3. Validar parámetros
        String actual = request.getParameter("contrasenaActual");
        String nueva = request.getParameter("contrasenaNueva");
        String confirma = request.getParameter("contrasenaConfirma");

        if (actual == null || nueva == null || confirma == null
                || actual.isEmpty() || nueva.isEmpty() || confirma.isEmpty()) {
            request.setAttribute("error", "Por favor, complete todos los campos.");
            request.getRequestDispatcher("cambiarContrasena.jsp").forward(request, response);
            return;
        }

        if (nueva.length() < 8 || nueva.length() > 200) {
            request.setAttribute("error", "La nueva contraseña debe tener entre 8 y 200 caracteres.");
            request.getRequestDispatcher("cambiarContrasena.jsp").forward(request, response);
            return;
        }

        if (!nueva.equals(confirma)) {
            request.setAttribute("error", "La nueva contraseña y la confirmación no coinciden.");
            request.getRequestDispatcher("cambiarContrasena.jsp").forward(request, response);
            return;
        }

        if (nueva.equals(actual)) {
            request.setAttribute("error", "La nueva contraseña debe ser distinta a la actual.");
            request.getRequestDispatcher("cambiarContrasena.jsp").forward(request, response);
            return;
        }

        // 4. Releer al usuario desde BD para obtener hash actualizado
        Controladora control = new Controladora();
        Usuario fresco = control.traerUsuario(usuario.getId());
        if (fresco == null) {
            response.sendRedirect("login_registro.jsp");
            return;
        }

        if (!PasswordUtil.verificar(actual, fresco.getContrasena())) {
            request.setAttribute("error", "La contraseña actual es incorrecta.");
            request.getRequestDispatcher("cambiarContrasena.jsp").forward(request, response);
            return;
        }

        // 5. Guardar nueva contraseña y limpiar bandera de cambio obligatorio
        fresco.setContrasena(PasswordUtil.hashear(nueva));
        fresco.setRequiereCambioContrasena(false);
        control.editarUsuario(fresco);

        // 6. Actualizar usuario en sesión
        session.setAttribute("usuarioLogueado", fresco);

        // 7. Redirigir según rol
        if ("instructor".equalsIgnoreCase(fresco.getRol())) {
            response.sendRedirect("panelInstructor.jsp?cambioOk=1");
        } else {
            response.sendRedirect("index.jsp?cambioOk=1");
        }
    }

    @Override
    public String getServletInfo() {
        return "Cambia la contraseña del usuario logueado";
    }
}

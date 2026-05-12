package com.proyectointegrador.proyectointegrador.servlets;

import Logica.Controladora;
import Logica.PasswordUtil;
import Logica.Usuario;
import java.io.IOException;
import java.security.SecureRandom;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Permite al instructor restablecer la contraseña de uno de sus estudiantes.
 * Genera una contraseña temporal aleatoria, la guarda hasheada en BD y marca
 * al usuario para que sea obligado a cambiarla en su próximo inicio de sesión.
 * La contraseña temporal en texto plano se devuelve al instructor a través
 * de un parámetro en la URL de redirección (mostrada una sola vez).
 */
@WebServlet(name = "SvRestablecerContrasena", urlPatterns = {"/SvRestablecerContrasena"})
public class SvRestablecerContrasena extends HttpServlet {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String ALFABETO = "abcdefghijkmnpqrstuvwxyz23456789";
    private static final int LONGITUD_TEMP = 8;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("listaCursosInstructor.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 1. Verificar sesión y rol instructor
        HttpSession session = request.getSession(false);
        Usuario instructor = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;

        if (instructor == null || !"instructor".equalsIgnoreCase(instructor.getRol())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado.");
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
        String idUsuarioStr = request.getParameter("idUsuario");
        String idCursoStr = request.getParameter("idCurso");

        int idUsuario;
        int idCurso;
        try {
            idUsuario = Integer.parseInt(idUsuarioStr);
            idCurso = Integer.parseInt(idCursoStr);
        } catch (NumberFormatException | NullPointerException e) {
            response.sendRedirect("listaCursosInstructor.jsp");
            return;
        }

        Controladora control = new Controladora();
        Usuario estudiante = control.traerUsuario(idUsuario);

        // 4. Solo se permite restablecer la contraseña de usuarios con rol "usuario"
        //    (no de otros instructores ni administradores)
        if (estudiante == null || !"usuario".equalsIgnoreCase(estudiante.getRol())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Operación no permitida.");
            return;
        }

        // 5. Generar contraseña temporal segura
        String temporal = generarContrasenaTemporal();

        // 6. Guardar hash en BD y marcar para cambio obligatorio
        estudiante.setContrasena(PasswordUtil.hashear(temporal));
        estudiante.setRequiereCambioContrasena(true);
        control.editarUsuario(estudiante);

        // 7. Redirigir al instructor mostrándole la contraseña temporal una vez
        String url = "listaEstudiantes.jsp?idCurso=" + idCurso
                + "&temp=" + URLEncoder.encode(temporal, StandardCharsets.UTF_8.name())
                + "&tempUser=" + URLEncoder.encode(estudiante.getNom_usuario(), StandardCharsets.UTF_8.name());
        response.sendRedirect(url);
    }

    private String generarContrasenaTemporal() {
        StringBuilder sb = new StringBuilder(LONGITUD_TEMP);
        for (int i = 0; i < LONGITUD_TEMP; i++) {
            sb.append(ALFABETO.charAt(RANDOM.nextInt(ALFABETO.length())));
        }
        return sb.toString();
    }

    @Override
    public String getServletInfo() {
        return "Restablecer contraseña de un estudiante (uso por instructor)";
    }
}

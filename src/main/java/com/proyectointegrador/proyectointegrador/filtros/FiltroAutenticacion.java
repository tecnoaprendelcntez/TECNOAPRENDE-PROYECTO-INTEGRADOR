package com.proyectointegrador.proyectointegrador.filtros;

import Logica.Usuario;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Filtro de autenticación: bloquea el acceso a páginas protegidas
 * si el usuario no ha iniciado sesión.
 *
 * Las rutas protegidas se declaran en urlPatterns.
 * Agregar aquí todas las páginas que requieran sesión activa.
 */
@WebFilter(urlPatterns = {
    "/cursoPrincipiante.jsp",
    "/cursoAvanzado.jsp",
    "/panelInstructor.jsp",
    "/informacion_adicional.jsp"
    // Agregar más rutas protegidas según sea necesario
})
public class FiltroAutenticacion implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialización (no requerida en este caso)
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;

        if (usuario == null) {
            // Sin sesión activa — redirigir al login
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login_registro.jsp");
            return;
        }

        // Verificar que no se acceda al panel de instructor sin el rol correcto
        String rutaSolicitada = httpRequest.getServletPath();
        if ("/panelInstructor.jsp".equals(rutaSolicitada) && !"instructor".equals(usuario.getRol())) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado.");
            return;
        }

        // Usuario autenticado — continuar la solicitud
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Limpieza (no requerida en este caso)
    }
}

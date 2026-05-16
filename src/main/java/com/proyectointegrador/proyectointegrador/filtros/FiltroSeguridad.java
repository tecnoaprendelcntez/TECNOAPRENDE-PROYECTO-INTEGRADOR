package com.proyectointegrador.proyectointegrador.filtros;

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

/**
 * Filtro global de cabeceras de seguridad HTTP.
 * Se aplica a TODAS las rutas de la aplicación.
 *
 * Protege contra:
 * - XSS (Cross-Site Scripting)
 * - Clickjacking
 * - MIME type sniffing
 * - Conexiones inseguras (HTTP)
 */
@WebFilter(urlPatterns = {"/*"})
public class FiltroSeguridad implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialización (no requerida en este caso)
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // =========================
        // CABECERAS DE SEGURIDAD
        // =========================

        // Evita que el navegador intente adivinar el tipo MIME
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");

        // Impide que TU sitio sea embebido dentro de iframes de otros sitios
        // (protección contra clickjacking)
        // Esto NO impide que TU sitio embeba Google Drive o YouTube.
        httpResponse.setHeader("X-Frame-Options", "DENY");

        // Protección XSS para navegadores antiguos
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block");

        // Política de referencia
        httpResponse.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");

        // Forzar HTTPS (descomentar cuando uses SSL en todos los entornos)
        // httpResponse.setHeader("Strict-Transport-Security",
        //         "max-age=31536000; includeSubDomains");

        // =========================
        // CONTENT SECURITY POLICY
        // =========================
        // Permite:
        // - Scripts desde jsDelivr (Chart.js)
        // - Estilos y fuentes desde cdnjs (Font Awesome)
        // - Imágenes locales, data URI y externas HTTPS
        // - Iframes desde Google Drive y YouTube
        // - Bloquea que otros sitios embeban tu aplicación
        httpResponse.setHeader("Content-Security-Policy",
            "default-src 'self'; "
            + "script-src 'self' 'unsafe-inline' https://cdn.jsdelivr.net; "
            + "style-src 'self' 'unsafe-inline' https://cdnjs.cloudflare.com; "
            + "font-src 'self' https://cdnjs.cloudflare.com; "
            + "img-src 'self' data: https:; "
            + "frame-src 'self' "
                + "https://drive.google.com "
                + "https://www.youtube.com "
                + "https://www.youtube-nocookie.com; "
            + "frame-ancestors 'none';"
        );

        // =========================
        // CONTROL DE CACHÉ
        // =========================
        // Evita que páginas dinámicas queden almacenadas en caché
        String uri = httpRequest.getRequestURI();
        if (uri.endsWith(".jsp") || uri.endsWith("/")) {
            httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            httpResponse.setHeader("Pragma", "no-cache");
            httpResponse.setDateHeader("Expires", 0);
        }

        // Continuar con la petición
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Limpieza (no requerida en este caso)
    }
}
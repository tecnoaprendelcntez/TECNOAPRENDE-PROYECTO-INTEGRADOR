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

        // --- CABECERAS DE SEGURIDAD ---

        // Previene que el navegador "adivine" el tipo de contenido (MIME sniffing)
        httpResponse.setHeader("X-Content-Type-Options", "nosniff");

        // Previene que la página sea embebida en un iframe (clickjacking)
        httpResponse.setHeader("X-Frame-Options", "DENY");

        // Activa el filtro XSS del navegador (compatibilidad con navegadores antiguos)
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block");

        // Fuerza HTTPS por 1 año en todos los subdominios (solo activar si usas HTTPS)
        // Descomenta esta línea cuando tu servidor tenga certificado SSL:
        // httpResponse.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");

        // Política de referencia: no enviar la URL completa a sitios externos
        httpResponse.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");

        // Content Security Policy: restringe los orígenes de recursos permitidos.
        // Ajusta los dominios según los recursos externos que uses en tu aplicación.
        httpResponse.setHeader("Content-Security-Policy",
            "default-src 'self'; "
            + "script-src 'self' 'unsafe-inline'; "
            + "style-src 'self' 'unsafe-inline' https://cdnjs.cloudflare.com; "
            + "font-src 'self' https://cdnjs.cloudflare.com; "
            + "img-src 'self' data:; "
            + "frame-ancestors 'none';"
        );

        // Deshabilitar caché para páginas dinámicas
        // (Esto se complementa con lo que ya tienes en index.jsp)
        String uri = httpRequest.getRequestURI();
        if (uri.endsWith(".jsp") || uri.endsWith("/")) {
            httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            httpResponse.setHeader("Pragma", "no-cache");
            httpResponse.setDateHeader("Expires", 0);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Limpieza (no requerida en este caso)
    }
}

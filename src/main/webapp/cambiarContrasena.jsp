<%-- cambiarContrasena.jsp --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Logica.*"%>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
    if (usuario == null) {
        response.sendRedirect("login_registro.jsp");
        return;
    }
    String csrfToken = (String) session.getAttribute("csrfToken");
    boolean obligatorio = usuario.isRequiereCambioContrasena();
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Cambiar contraseña</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <header class="encabezado">
        <img src="images/ITSZ-LCNTEZ.png" class="imagen-encabezado">
        <div class="acciones">
            <p><strong><%= usuario.getNom_usuario() %></strong></p>
            <a href="SvCerrarSesion?csrfToken=<%= csrfToken %>"><button>Cerrar sesión</button></a>
        </div>
    </header>

    <main class="contenedor_lista">
        <h1>Cambiar contraseña</h1>

        <% if (obligatorio) { %>
            <p style="background:#fff3cd;border:1px solid #ffeeba;padding:10px;border-radius:6px;color:#856404;">
                Tu contraseña fue restablecida por un instructor. Debes elegir una nueva contraseña antes de continuar.
            </p>
        <% } %>

        <% if (error != null) { %>
            <p style="background:#f8d7da;border:1px solid #f5c6cb;padding:10px;border-radius:6px;color:#721c24;">
                <%= error %>
            </p>
        <% } %>

        <form class="form-cambiar-password" action="SvCambiarContrasena" method="post" autocomplete="off" style="max-width:420px;">
            <input type="hidden" name="csrfToken" value="<%= csrfToken %>">

            <label for="contrasenaActual">Contraseña actual</label>
            <input type="password" id="contrasenaActual" name="contrasenaActual" required minlength="8" maxlength="100">

            <label for="contrasenaNueva">Nueva contraseña</label>
            <input type="password" id="contrasenaNueva" name="contrasenaNueva" required minlength="8" maxlength="100">

            <label for="contrasenaConfirma">Confirmar nueva contraseña</label>
            <input type="password" id="contrasenaConfirma" name="contrasenaConfirma" required minlength="8" maxlength="100">

            <button type="submit" class="registrar" style="margin-top:12px;">Guardar nueva contraseña</button>
        </form>
    </main>
</body>
</html>

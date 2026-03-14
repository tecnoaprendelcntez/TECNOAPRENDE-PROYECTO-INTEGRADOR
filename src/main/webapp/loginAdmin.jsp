<%-- loginAdmin.jsp --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Administrador</title>
        <link rel="stylesheet" href="styles.css">
    </head>
    <body class="inicio_sesion_registro">
        <div class="encabezado1">
            <img src="images/ITSZ-LCNTEZ.png" alt="Encabezado de logos"  class="imagen-encabezado1">
        </div>
        <h1 class="formulario">Iniciar sesión</h1>
        <form action="SvLoginAdmin" method="POST" accept-charset="UTF-8">
            <h2>Administrador</h2>
            <label>Nombre de Usuario</label>
            <input type="text" name="usuario" placeholder="Ingrese su Nombre de Usuario" required>
            <label>Comtraseña</label>
            <input type="password" name="contrasena" placeholder="Ingrese su Contraseña" required>
            <button type="submit" class="boton_iniciosesion_registro">Ingresar</button>
        </form>
        <%
        String error = (String) request.getAttribute("error");
        if (error != null) {
        %>
            <p style="color: red;"><%= error %></p>
        <%
            }
        %>
    </body>
</html>

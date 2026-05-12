<%-- loginAdmin.jsp --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Administrador</title>
        <link rel="stylesheet" href="styles.css">
        <link rel="icon" href="assets/favicon.ico" type="image/x-icon">
    </head>
    <body>
        <header class="encabezado">
            <img src="images/ITSZ-LCNTEZ.png" alt="Encabezado de logos" class="imagen-encabezado">
            <img src="images/tecnoaprende.png" alt="Logo TecnoAprende" class="tecnoaprende">
            <div class="acciones">
                <a href="index.jsp"><button>Panel Principal</button></a>
            </div>
        </header>
        
        <div class="login-admin-container">
            <h1 class="formulario">Iniciar sesión</h1>

            <form action="SvLoginAdmin" method="POST" accept-charset="UTF-8" class="login-admin-form">
                <h2>Administrador</h2>

                <label>Nombre de Usuario</label>
                <input type="text" name="usuario" placeholder="Ingrese su Nombre de Usuario" required>

                <label>Contraseña</label>
                <input type="password" name="contrasena" placeholder="Ingrese su Contraseña" required>

                <button type="submit" class="boton_iniciosesion_registro">Ingresar</button>
            </form>
        </div>
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

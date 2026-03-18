<%-- panelInstructor.jsp --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Logica.Usuario"%>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
    if (usuario == null || !"instructor".equals(usuario.getRol())) {
        response.sendRedirect("login_registro.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Panel del Instructor</title>
        <link rel="stylesheet" href="styles.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    </head>
    <body>
        <header class="encabezado">
            <img src="images/ITSZ-LCNTEZ.png" class="imagen-encabezado">
            <div class="acciones">
                <p>Bienvenido, <strong><%= usuario.getNom_usuario() %></strong> (Instructor)</p>
                <a href="SvCerrarSesion"><button>Cerrar sesión</button></a>
            </div>
        </header>

        <div class="titulo">
            <h1>Panel del Instructor</h1>
        </div>

        <div class="botones_admin">
            <button class="listas lista_usuarios" onclick="location.href='listaCursosInstructor.jsp'">Mis Cursos</button>
        </div>
        <!-- Pie de página -->
        <footer class="pie_de_pagina">
            <div class="footer-contenido">
            <div class="instituto">
                <a href="https://zongolica.tecnm.mx/">Instituto Tecnológico Superior de Zongolica</a>
                <div class="redes">
                    <a href="https://www.facebook.com/TecNMZongolica" target="_blank">
                        <i class="fab fa-facebook"></i>
                    </a>
                    <a href="https://www.youtube.com/channel/UCi0_QXTliS2p_2MDwhfF8ww" target="_blank">
                        <i class="fab fa-youtube"></i>
                    </a>
                    <a href="https://x.com/somositsz?lang=es" target="_blank">
                        <i class="fab fa-x"></i>
                    </a>
                </div>
            </div>

            <div class="casa">
                <a href="https://lcntez.org.mx/">La Casa de los Niños de Tezonapa</a>
                <div class="redes">
                    <a href="https://www.facebook.com/profile.php?id=100078645709893" target="_blank">
                        <i class="fab fa-facebook"></i>
                    </a>
                    <a href="https://www.instagram.com/ninos_tezonapa?utm_source=ig_web_button_share_sheet&igsh=ZDNlZDc0MzIxNw%3D%3D" target="_blank">
                        <i class="fab fa-instagram"></i>
                    </a>
                    <a href="https://x.com/Ninos_Tezonapa" target="_blank">
                        <i class="fab fa-x"></i>
                    </a>
                </div>
            </div>
            </div>
            <div class="creditos-equipo">
                    <a href="creditos.jsp"><p>© 2025 TECNOAPRENDE. Plataforma desarrollada por equipo BOX Code. Todos los derechos reservados.</p></a>
                </div>
        </footer>
    </body>
</html>

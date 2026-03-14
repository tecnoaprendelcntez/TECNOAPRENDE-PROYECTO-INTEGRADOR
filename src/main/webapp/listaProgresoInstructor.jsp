<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="Logica.*"%>

<%
    // Validar sesión
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
    if (usuario == null || !"instructor".equals(usuario.getRol())) {
        response.sendRedirect("iniciosesion.jsp");
        return;
    }

    int idCurso = Integer.parseInt(request.getParameter("idCurso"));
    int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));

    Controladora control = new Controladora();
    Usuario estudiante = control.traerUsuario(idUsuario);

    List<Actividad> actividades = control.traerActividadesPorCurso(idCurso);
    List<Progreso> progresos = control.traerProgresoPorCursoYUsuario(idCurso, idUsuario);

    // Crear mapa para acceso rápido por ID de actividad
    Map<Integer, Progreso> mapaProgreso = new HashMap<>();
    for (Progreso prog : progresos) {
        mapaProgreso.put(prog.getActividad().getIdActividad(), prog);
    }

    int total = actividades.size();
    int completadas = 0;

    for (Actividad act : actividades) {
        Progreso p = mapaProgreso.get(act.getIdActividad());
        if (p != null && "completado".equalsIgnoreCase(p.getEstado())) {
            completadas++;
        }
    }

    int porcentaje = total > 0 ? (completadas * 100 / total) : 0;
%>

<!DOCTYPE html>
<html>
<head>
    <title>Progreso del Estudiante</title>
    <link rel="stylesheet" href="styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">      
</head>

<body>

<header class="encabezado">
    <img src="images/ITSZ-LCNTEZ.png" class="imagen-encabezado">
    <div class="acciones">
        <p><strong><%= usuario.getNom_usuario() %></strong></p>
        <a href="SvCerrarSesion"><button>Cerrar sesión</button></a>
        <a href="panelInstructor.jsp"><button>Panel Principal</button></a>
    </div>
</header>

<main class="contenedor_lista">

    <h1>Progreso de <%= estudiante.getNombre() + " " + estudiante.getApellidos() %></h1>
    <h3>Porcentaje de avance: <%= porcentaje %>%</h3>

    <table>
        <tr>
            <th>Actividad</th>
            <th>Estado</th>
            <th>Calificación</th>
            <th>Acción</th>
        </tr>

        <% for (Actividad a : actividades) {
               Progreso p = mapaProgreso.get(a.getIdActividad());
               String estado = (p != null) ? p.getEstado() : "Sin registro";
               String calificacion = (p != null) ? String.valueOf(p.getCalificacion()) : "-";
        %>

        <tr>
            <td><%= a.getTitulo() %></td>
            <td><%= estado %></td>
            <td><%= calificacion %></td>
            <td>
                <button type="button" class="boton-curso" onclick="abrirModal(<%= a.getIdActividad() %>)">
                    Ver / Calificar
                </button>
            </td>
        </tr>

        <!-- MODAL ÚNICO PARA CADA ACTIVIDAD -->
        <div class="modal_registro" id="modal<%= a.getIdActividad() %>">
            <div class="modal_content_formulario">

                <h3><%= a.getTitulo() %></h3>
                <h4><strong>Descripción:</strong> <%= a.getDescripcion() %></h4>
                <hr>

                <form action="SvProgresoInstructor" method="POST">

                    <!-- Si no existe progreso, enviar idProgreso = 0 -->
                    <input type="hidden" name="idProgreso" value="<%= (p != null ? p.getIdProgreso() : 0) %>">

                    <input type="hidden" name="idActividad" value="<%= a.getIdActividad() %>">
                    <input type="hidden" name="idCurso" value="<%= idCurso %>">
                    <input type="hidden" name="idUsuario" value="<%= idUsuario %>">

                    <label>Calificación:</label>
                    <input type="number" name="calificacion"
                           min="0" max="100"
                           value="<%= (p != null ? p.getCalificacion() : "") %>">

                    <label>Estado:</label>
                    <select name="estado">
                        <option value="pendiente"
                            <%= (p != null && "pendiente".equalsIgnoreCase(p.getEstado())) ? "selected" : "" %>>
                            Pendiente
                        </option>
                        <option value="en revisión"
                            <%= (p != null && "en revisión".equalsIgnoreCase(p.getEstado())) ? "selected" : "" %>>
                            En revisión
                        </option>
                        <option value="completado"
                            <%= (p != null && "completado".equalsIgnoreCase(p.getEstado())) ? "selected" : "" %>>
                            Completado
                        </option>
                    </select>

                    <div class="modal-buttons_lista">
                        <button type="button" onclick="cerrarModal(<%= a.getIdActividad() %>)">Cerrar</button>
                        <button type="submit">Guardar</button>
                    </div>
                </form>

            </div>
        </div>

        <% } %>

    </table>

</main>
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

<script>
function abrirModal(id) {
    document.getElementById("modal" + id).style.display = "flex";
}

function cerrarModal(id) {
    document.getElementById("modal" + id).style.display = "none";
}
</script>

</body>
</html>

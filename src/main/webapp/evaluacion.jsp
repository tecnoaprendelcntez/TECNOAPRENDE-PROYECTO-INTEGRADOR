<%--evaluacion.jsp--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, Logica.*" %>
<%@ page isErrorPage="true" %>
<%@ page errorPage="" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
    if (usuario == null) {
        response.sendRedirect("login_registro.jsp");
        return;
    }
    
    Controladora control = new Controladora();
    Integer idCurso = (Integer) request.getAttribute("idCurso");
    String vista = (String) request.getAttribute("vista");

    if (idCurso == null) idCurso = 1; // fallback
    if (vista == null) vista = "principiante";

    
    EvaluacionFinal ev = (EvaluacionFinal) request.getAttribute("evaluacion");
    if (ev == null) {
        out.println("<h2>No se pudo cargar la evaluación.</h2>");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8">
    <title>Evaluación Finals</title>
    <link rel="stylesheet" href="styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
    <!-- Encabezado con imagen y sesión -->
    <header class="encabezado">
        <img src="images/ITSZ-LCNTEZ.png" alt="Encabezado de logos" class="imagen-encabezado">
        <div class="acciones">
            <p><strong><%= usuario.getNom_usuario() %></strong></p>
            <a href="SvCerrarSesion"><button>Cerrar sesión</button></a>
            <a href="index.jsp"><button>Panel Principal</button></a>
        </div>
    </header>

<%
    EvaluacionFinal eval = (EvaluacionFinal) request.getAttribute("evaluacion");

    Map<String,Object> resumen = (Map<String,Object>) request.getAttribute("resumen");
    ResultadoEvaluacion result = (ResultadoEvaluacion) request.getAttribute("resultado");

    boolean mostrarModal = (resumen != null && result != null);
%>
<main class="contenedor-curso">
<div class="container_evaluacionfinal">
    <h1><%= eval.getTitulo() %></h1>
    <%
        Integer intentos = (Integer) request.getAttribute("intentos");
        if (intentos == null) intentos = 0;
    %>


    <h3><strong><%= intentos %> / 2 intentos</strong></h3>
    
    <p><%= eval.getInstrucciones() %></p>
    
    <% if (request.getAttribute("mensajeIntentos") != null) { %>
        <div class="mensajeIntentos">
            <strong><%= request.getAttribute("mensajeIntentos") %></strong>
        </div>
    <% } %>

    <form action="SvResolverEvaluacion" method="POST">

        <input type="hidden" name="idEvaluacion" value="<%= eval.getIdEvaluacion() %>">

        <% for (PreguntaEvaluacion p : eval.getPreguntas()) { %>
            <div class="container_evaluacionfinal_pregunta">
                <strong><%= p.getEnunciado() %></strong>

                <div class="container_evaluacionfinal_opciones">
                    <label> <span class="texto-respuesta"><%= p.getOpcionA() %></span> <input type="radio" name="resp_<%= p.getIdPregunta() %>" value="A" required> </label>
                    <label> <span class="texto-respuesta"><%= p.getOpcionB() %></span> <input type="radio" name="resp_<%= p.getIdPregunta() %>" value="B"> </label>
                    <label> <span class="texto-respuesta"><%= p.getOpcionC() %></span> <input type="radio" name="resp_<%= p.getIdPregunta() %>" value="C"> </label>
                    <label> <span class="texto-respuesta"><%= p.getOpcionD() %></span> <input type="radio" name="resp_<%= p.getIdPregunta() %>" value="D"> </label>
                </div>
            </div>
        <% } %>
        
        <%
            Boolean puedeIntentar = (Boolean) request.getAttribute("puedeIntentar");
            if (puedeIntentar == null) puedeIntentar = true;
        %>

        <button type="submit" class="container_evaluacionfinal_btn-enviar" <%= !puedeIntentar ? "disabled" : "" %>>
            Enviar evaluación
        </button>
            
        <% if (!puedeIntentar) { %>
            <div style="padding:10px; background:#ffe5e5; border-left:4px solid red; margin:15px 0; font-weight:bold;">
                Ya realizaste los 2 intentos permitidos. No puedes enviar la evaluación nuevamente.
            </div>
        <% } %>
    </form>

</div>

<!-- Modal con calificación -->
<div id="modalResultado" class="modal_calificacion">
    <div class="modal-content_calificacion">

        <h2>Resultado de la evaluación</h2>

        <p><strong>Calificación:</strong> <%= result != null ? result.getCalificacion() : "" %>%</p>
        <p><strong>Minima para aprobar:</strong> 60%</p>
        <p><strong>Estado:</strong> <%= result != null ? result.getEstado() : "" %></p>
        
        <h3>Detalle de preguntas:</h3>

        <ul>
            <% if (resumen != null) {
                List<Map<String,String>> detalles = (List<Map<String,String>>) resumen.get("detalles");
                for (Map<String,String> det : detalles) {
            %>
                <li>
                    <strong><%= det.get("pregunta") %></strong><br>
                    Respuesta: <%= det.get("respuestaUsuario") %><br>

                    <% if (det.get("correcta").equals("true")) { %>
                        <span class="respuesta_correcta">✔ Correcta</span>
                    <% } else { %>
                        <span class="respuesta_incorrecta">✘ Incorrecta</span>
                    <% } %>
                </li>
                <br>
            <% }} %>
        </ul>

        <% 
            boolean aprobado = result != null && result.getCalificacion() >= 60;
        %>
        <h2 style="<%= aprobado ? "color:green" : "color:red" %>">
            <%= aprobado ? "¡APROBADO!" : "REPROBADO" %>
        </h2>
        
        <div class="modal_calificacion_volver">
            <a href="<%= vista.equals("avanzado") ? "cursoAvanzado.jsp" : "cursoPrincipiante.jsp" %>">
                <button class="container_evaluacionfinal_btn-enviar">Volver al curso</button>
            </a>
        </div>
    </div>
</div>
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
    <% if (mostrarModal) { %>
        document.getElementById('modalResultado').style.display = 'flex';
    <% } %>
</script>

</body>
</html>

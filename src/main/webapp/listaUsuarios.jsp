<%-- listaUsuarios.jsp --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Logica.Usuario"%>
<%@page import="java.util.List"%>
<%@page import="Logica.Controladora"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
    // SEGURIDAD: Verificar sesión y rol admin
    Usuario admin = (Usuario) session.getAttribute("usuarioLogueado");
    if (admin == null || !"admin".equalsIgnoreCase(admin.getRol())) {
        response.sendRedirect("loginAdmin.jsp");
        return;
    }
    
    // SEGURIDAD: Obtener token CSRF de sesión
    String csrfToken = (String) session.getAttribute("csrfToken");

    Controladora control = new Controladora();
    List<Usuario> listaUsuarios = control.traerUsuarios();
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Lista de Usuarios</title>
        <link rel="stylesheet" href="styles.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    </head>
    <body>
        <header class="encabezado">
            <img src="images/ITSZ-LCNTEZ.png" alt="Encabezado de logos" class="imagen-encabezado">
            <div class="acciones">
                <p><strong><%=org.apache.commons.text.StringEscapeUtils.escapeHtml4(admin.getNom_usuario())%></strong></p>
                <a href="SvCerrarSesion"><button>Cerrar sesión</button></a>
                <a href="panelAdmin.jsp"><button>Panel Principal</button></a>
            </div>
        </header>
        
        <main class="contenedor_lista">
            <h1>Usuarios Registrados<button class="registrar" onclick="abrirRegistro()">Registrar Usuario</button></h1>
        <table>
            <tr>
                <th>ID</th><th>Nombre</th><th>Apellidos</th><th>Usuario</th><th>Acciones</th>
            </tr>
            <%
                for (Usuario u : listaUsuarios) {
                    if ("usuario".equalsIgnoreCase(u.getRol())) {
                    String nombreEsc   = org.apache.commons.text.StringEscapeUtils.escapeHtml4(u.getNombre());
                    String apellidoEsc = org.apache.commons.text.StringEscapeUtils.escapeHtml4(u.getApellidos());
                    String usuarioEsc  = org.apache.commons.text.StringEscapeUtils.escapeHtml4(u.getNom_usuario());
                    String nombreJs   = org.apache.commons.text.StringEscapeUtils.escapeEcmaScript(u.getNombre());
                    String apellidoJs = org.apache.commons.text.StringEscapeUtils.escapeEcmaScript(u.getApellidos());
                    String usuarioJs  = org.apache.commons.text.StringEscapeUtils.escapeEcmaScript(u.getNom_usuario());
            %>
            <tr>
                <td><%= u.getId() %></td>
                <td><%= nombreEsc %></td>
                <td><%= apellidoEsc %></td>
                <td><%= usuarioEsc %></td>
                <td>
                    <button class="editar" onclick="abrirEditar(<%= u.getId() %>, '<%= nombreJs %>', '<%= apellidoJs %>', '<%= usuarioJs %>')">Editar</button>
                    <button class="eliminar" onclick="confirmarEliminar(<%= u.getId() %>)">Eliminar</button>
                </td>
            </tr>
            <% } } %>
        </table>
        
        <!-- MODAL REGISTRAR -->
        <div class="modal_registro" id="modalRegistro">
            <div class="modal_content_formulario">
                <h3>Registrar Usuario</h3>
                <form action="SvUsuarios" method="POST" accept-charset="UTF-8">
                    <%-- SEGURIDAD: Token CSRF en todos los formularios --%>
                    <input type="hidden" name="csrfToken" value="<%= csrfToken %>">
                    <p><label>Nombre: </label></p>
                    <input type="text" name="nombre" placeholder="Nombre" maxlength="100" required>
                    <p><label>Apellidos: </label></p>
                    <input type="text" name="apellidos" placeholder="Apellidos" maxlength="100" required>
                    <p><label>Nombre de Usuario: </label></p>
                    <input type="text" name="usuario" placeholder="Nombre de usuario" maxlength="50" required>
                    <p><label>Contraseña: </label></p>
                    <input type="password" name="contrasena" placeholder="Mínimo 8 caracteres" minlength="8" maxlength="100" required>
                    <div class="modal-buttons_lista">
                        <button type="button" onclick="cerrarModal()">Cancelar</button>
                        <button type="submit">Registrar</button>
                    </div>
                </form>
            </div>
        </div>

        <!-- MODAL DE EDICIÓN -->
        <div class="modal_editar" id="modalEditar">
            <div class="modal_content_formulario">
                <h3>Editar Usuario</h3>
                <form action="SvEditarUsuario" method="POST" accept-charset="UTF-8">
                    <%-- SEGURIDAD: Token CSRF --%>
                    <input type="hidden" name="csrfToken" value="<%= csrfToken %>">
                    <input type="hidden" name="id" id="editId">
                    <p><label>Nombre </label></p>
                    <input type="text" name="nombre" id="editNombre" placeholder="Nombre" maxlength="100" required>
                    <p><label>Apellidos: </label></p>
                    <input type="text" name="apellidos" id="editApellidos" placeholder="Apellidos" maxlength="100" required>
                    <p><label>Nombre de Usuario: </label></p>
                    <input type="text" name="nom_usuario" id="editUsuario" placeholder="Usuario" maxlength="50" required>
                    <p><label>Nueva Contraseña (dejar vacío para no cambiar): </label></p>
                    <input type="password" name="contrasena" id="editContrasena" placeholder="Nueva contraseña (opcional)" minlength="8" maxlength="100">
                    <div class="modal-buttons_lista">
                        <button type="button" onclick="cerrarModal()">Cancelar</button>
                        <button type="submit">Guardar</button>
                    </div>
                </form>
            </div>
        </div>

        <!-- MODAL DE CONFIRMACIÓN DE ELIMINACIÓN -->
        <div class="modal_eliminar" id="modalEliminar">
            <div class="modal_content_formulario">
                <h3>¿Estás seguro de eliminar este usuario?</h3>
                <form action="SvEliminar" method="POST">
                    <%-- SEGURIDAD: Token CSRF --%>
                    <input type="hidden" name="csrfToken" value="<%= csrfToken %>">
                    <input type="hidden" name="id" id="deleteId">
                    <div class="modal-buttons_lista">
                        <button type="button" onclick="cerrarModal()">No</button>
                        <button type="submit">Sí, eliminar</button>
                    </div>
                </form>
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
                    <a href="creditos.jsp"><p>© 2026 TECNOAPRENDE. Plataforma desarrollada por equipo BOX Code. Todos los derechos reservados.</p></a>
                </div>
        </footer>

        <script>
            function abrirRegistro() {
                document.getElementById('modalRegistro').style.display = 'flex';
            }
            
            function abrirEditar(id, nombre, apellidos, usuario) {
                document.getElementById('editId').value = id;
                document.getElementById('editNombre').value = nombre;
                document.getElementById('editApellidos').value = apellidos;
                document.getElementById('editUsuario').value = usuario;
                document.getElementById('editContrasena').value = '';
                document.getElementById('modalEditar').style.display = 'flex';
            }

            function confirmarEliminar(id) {
                document.getElementById('deleteId').value = id;
                document.getElementById('modalEliminar').style.display = 'flex';
            }

            function cerrarModal() {
                document.getElementById('modalRegistro').style.display = 'none';
                document.getElementById('modalEditar').style.display = 'none';
                document.getElementById('modalEliminar').style.display = 'none';
            }
        </script>

    </body>
</html>

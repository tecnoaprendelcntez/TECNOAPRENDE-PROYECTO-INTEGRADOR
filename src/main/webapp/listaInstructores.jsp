<%-- listaInstructores.jsp --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Logica.Usuario"%>
<%@page import="Logica.Instructor"%>
<%@page import="Logica.Controladora"%>
<%@page import="java.util.List"%>
<%
    Usuario admin = (Usuario) session.getAttribute("usuarioLogueado");
    if (admin == null || !"admin".equalsIgnoreCase(admin.getRol())) {
        response.sendRedirect("iniciosesion.jsp");
        return;
    }

    Controladora control = new Controladora();
    List<Instructor> listaInstructores = control.traerInstructores();
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Lista de Instructores</title>
        <link rel="stylesheet" href="styles.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    </head>
    <body>
        <header class="encabezado">
            <img src="images/ITSZ-LCNTEZ.png" alt="Encabezado de logos" class="imagen-encabezado">
            <div class="acciones">
                <p><strong><%= admin.getNom_usuario() %></strong></p>
                <a href="SvCerrarSesion"><button>Cerrar sesión</button></a>
                <a href="panelAdmin.jsp"><button>Panel Principal</button></a>
            </div>
        </header>
                
        <main class="contenedor_lista">
            <h1>Instructores Registrados <button class="registrar" onclick="abrirRegistro()">Registrar Instructor</button></h1>
            
            <table>
                <tr>
                    <th>ID Instructor</th><th>ID Usuario</th><th>Nombre</th><th>Apellidos</th><th>Usuario</th><th>Contraseña</th><th>Acciones</th>
                </tr>
                <%
                    for (Instructor ins : listaInstructores) {
                        Usuario u = ins.getUsuario();
                %>
                <tr>
                    <td><%= ins.getIdInstructor() %></td>
                    <td><%= u.getId() %></td>
                    <td><%= u.getNombre() %></td>
                    <td><%= u.getApellidos() %></td>
                    <td><%= u.getNom_usuario() %></td>
                    <td><%= u.getContrasena() %></td>
                    <td>
                        <button class="editar" onclick="abrirEditar(<%= ins.getIdInstructor() %>, <%= u.getId() %>, '<%= u.getNombre() %>', '<%= u.getApellidos() %>', '<%= u.getNom_usuario() %>', '<%= u.getContrasena() %>')">Editar</button>
                        <button class="eliminar" onclick="confirmarEliminar(<%= ins.getIdInstructor() %>)">Eliminar</button>
                    </td>
                </tr>
                <% } %>
            </table>
            
            <!-- MODAL REGISTRAR -->
            <div class="modal_registro" id="modalRegistro">
                <div class="modal_content_formulario">
                    <h3>Registrar Instructor</h3>
                    <form action="SvCrearInstructor" method="POST" accept-charset="UTF-8">
                        <p><label>Nombre: </label></p>
                        <input type="text" name="nombre" placeholder="Nombre" required>
                        <p><label>Apellidos: </label></p>
                        <input type="text" name="apellidos" placeholder="Apellidos" required>
                        <p><label>Nombre de Usuario: </label></p>
                        <input type="text" name="usuario" placeholder="Nombre de usuario" required>
                        <p><label>Contraseña: </label></p>
                        <input type="password" name="contrasena" placeholder="Contraseña" required>
                        <div class="modal-buttons_lista">
                            <button type="button" onclick="cerrarModal()">Cancelar</button>
                            <button type="submit">Registrar</button>
                        </div>
                    </form>
                </div>
            </div>

            <!-- MODAL EDITAR -->
            <div class="modal_editar" id="modalEditar">
                <div class="modal_content_formulario">
                    <h3>Editar Instructor</h3>
                    <form action="SvEditarInstructor" method="POST" accept-charset="UTF-8">
                        <input type="hidden" name="idInstructor" id="editIdInstructor">
                        <input type="hidden" name="idUsuario" id="editIdUsuario">
                        <p><label>Nombre: </label></p>
                        <input type="text" name="nombre" id="editNombre" placeholder="Nombre" required>
                        <p><label>Apellidos: </label></p>
                        <input type="text" name="apellidos" id="editApellidos" placeholder="Apellidos" required>
                        <p><label>Nombre de Usuario: </label></p>
                        <input type="text" name="nom_usuario" id="editUsuario" placeholder="Usuario" required>
                        <p><label>Contraseña: </label></p>
                        <input type="password" name="contrasena" id="editContrasena" placeholder="Contraseña" required>
                        <div class="modal-buttons_lista">
                            <button type="button" onclick="cerrarModal()">Cancelar</button>
                            <button type="submit">Guardar</button>
                        </div>
                    </form>
                </div>
            </div>

            <!-- MODAL ELIMINAR -->
            <div class="modal_eliminar" id="modalEliminar">
                <div class="modal_content_formulario">
                    <h3>¿Eliminar este instructor?</h3>
                    <form action="SvEliminarInstructor" method="POST">
                        <input type="hidden" name="idInstructor" id="deleteIdInstructor">
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
                    <a href="creditos.jsp"><p>© 2025 TECNOAPRENDE. Plataforma desarrollada por equipo BOX Code. Todos los derechos reservados.</p></a>
                </div>
        </footer>

        <script>
            function abrirRegistro() {
                document.getElementById('modalRegistro').style.display = 'flex';
            }
            
            function abrirEditar(idInstructor, idUsuario, nombre, apellidos, usuario, contrasena) {
                document.getElementById('editIdInstructor').value = idInstructor;
                document.getElementById('editIdUsuario').value = idUsuario;
                document.getElementById('editNombre').value = nombre;
                document.getElementById('editApellidos').value = apellidos;
                document.getElementById('editUsuario').value = usuario;
                document.getElementById('editContrasena').value = contrasena;
                document.getElementById('modalEditar').style.display = 'flex';
            }

            function confirmarEliminar(idInstructor) {
                document.getElementById('deleteIdInstructor').value = idInstructor;
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


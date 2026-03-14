<%-- listaActividades.jsp --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="Logica.*"%>
<%@page import="java.util.*"%>
<%
    Usuario admin = (Usuario) session.getAttribute("usuarioLogueado");
    if (admin == null || !"admin".equalsIgnoreCase(admin.getRol())) {
        response.sendRedirect("loginAdmin.jsp");
        return;
    }
    
    int idCurso = Integer.parseInt(request.getParameter("idCurso"));
    Controladora control = new Controladora();
    Curso curso = control.traerCurso(idCurso);
    List<Actividad> actividades = control.traerActividadesPorCurso(idCurso);
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Actividades - <%= curso.getNombre() %></title>
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
        <a href="listaCursos.jsp"><button class="registrar">Lista de Cursos</button></a>
        <h1>
            Actividades del curso: <%= curso.getNombre() %>
            <button class="registrar" onclick="abrirRegistro()">Agregar Actividad</button>
        </h1>
                

        <table>
            <tr>
                <th>ID</th><th>Título</th><th>Descripción</th><th>Tipo</th><th>Acciones</th>
            </tr>
            <%
                for (Actividad a : actividades) {
            %>
            <tr>
                <td><%= a.getIdActividad() %></td>
                <td><%= a.getTitulo() %></td>
                <td><%= a.getDescripcion() %></td>
                <td><%= a.getTipo() %></td>
                <td>
                    <button class="editar"
                    onclick="abrirEditar(
                        '<%= a.getIdActividad() %>',
                        `<%= a.getTitulo() %>`,
                        `<%= a.getDescripcion() %>`,
                        '<%= a.getTipo() %>'
                    )">
                    Editar
                    </button>

                    <button class="eliminar" onclick="abrirEliminar('<%= a.getIdActividad() %>')">Eliminar</button>
                </td>
            </tr>
            <% } %>
        </table>
        
        <!-- MODAL REGISTRAR -->
            <div class="modal_registro" id="modalRegistro">
                <div class="modal_content_formulario">
                    <h3>Registrar Actividad</h3>
                    <form action="SvCrearActividad" method="POST" accept-charset="UTF-8">
                        <input type="hidden" name="idCurso" value="<%= idCurso %>">
                        <p><label>Titulo:</label></p>
                        <input type="text" name="titulo" placeholder="Título de la actividad" required>
                        <p><label>Descripción:</label></p>
                        <textarea name="descripcion" placeholder="Descripción" required></textarea>
                        <p><label>Tipo:</label></p>
                        <select name="tipo" id="crearTipo" required>
                            <option value="relación">Relación</option>
                            <option value="identificación">Identificación</option>
                            <option value="cuestionario">Cuestionario</option>
                            <option value="exploración">Exploración</option>
                            <option value="adivinanzas">Adivinanzas</option>
                            <option value="práctica">Práctica</option>
                        </select>
                        <div class="modal-buttons_lista">
                            <button type="button" onclick="cerrarModal()">Cancelar</button>
                            <button type="submit">Guardar</button>
                        </div>
                    </form>
                </div>
            </div>

        <!-- Modal Editar -->
        <div class="modal_editar" id="modalEditar">
            <div class="modal_content_formulario">
                <h3>Editar Actividad</h3>
                <form action="SvEditarActividad" method="POST" accept-charset="UTF-8">
                    <input type="hidden" name="idActividad" id="editId">
                    <input type="hidden" name="idCurso" value="<%= idCurso %>">
                    <p><label>Titulo:</label></p>
                    <input type="text" name="titulo" id="editTitulo" placeholder="Título" required>
                    <p><label>Descripción:</label></p>
                    <textarea name="descripcion" id="editDescripcion" placeholder="Descripción" required></textarea>
                    <p><label>Tipo:</label></p>
                    <select name="tipo" id="editTipo" required>
                        <option value="relación">Relación</option>
                        <option value="identificación">Identificación</option>
                        <option value="cuestionario">Cuestionario</option>
                        <option value="exploración">Exploración</option>
                        <option value="adivinanzas">Adivinanzas</option>
                        <option value="práctica">Práctica</option>
                    </select>
                    <div class="modal-buttons_lista">
                        <button type="button" onclick="cerrarModal()">Cancelar</button>
                        <button type="submit">Guardar</button>
                    </div>
                </form>
            </div>
        </div>

        <!-- Modal Eliminar -->
        <div class="modal_eliminar" id="modalEliminar">
            <div class="modal_content_formulario">
                <h3>¿Eliminar esta actividad?</h3>
                <form action="SvEliminarActividad" method="POST">
                    <input type="hidden" name="idActividad" id="deleteId">
                    <input type="hidden" name="idCurso" value="<%= idCurso %>">
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
            
            function abrirEditar(id, titulo, descripcion, tipo) {
                document.getElementById('editId').value = id;
                document.getElementById('editTitulo').value = titulo;
                document.getElementById('editDescripcion').value = descripcion;
                document.getElementById('editTipo').value = tipo;
                document.getElementById('modalEditar').style.display = 'flex';
            }

            function abrirEliminar(id) {
                document.getElementById('deleteId').value = id;
                document.getElementById('modalEliminar').style.display = 'flex';
            }

            function cerrarModal() {
                document.getElementById('modalRegistro').style.display = 'none';
                document.getElementById('modalEditar').style.display = 'none';
                document.getElementById('modalEliminar').style.display = 'none';
            }
            
            const customSelect = document.getElementById('selectInstructor');
            const header = customSelect.querySelector('.select-header');
            const options = customSelect.querySelectorAll('.select-options div');
            const hiddenInput = document.getElementById('idInstructorHidden');

            header.addEventListener('click', () => {
              customSelect.classList.toggle('active');
            });

            // Cuando seleccionas una opción
            options.forEach(opt => {
              opt.addEventListener('click', () => {
                header.textContent = opt.textContent;
                hiddenInput.value = opt.dataset.value;
                customSelect.classList.remove('active');
              });
            });

            // Cierra si se da clic fuera del select
            document.addEventListener('click', (e) => {
              if (!customSelect.contains(e.target)) {
                customSelect.classList.remove('active');
              }
            });
        </script>

    </body>
</html>
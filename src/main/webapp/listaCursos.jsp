<%-- listaCursos.jsp --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Logica.Usuario"%>
<%@page import="Logica.Controladora"%>
<%@page import="Logica.Curso"%>
<%@page import="Logica.Instructor" %>
<%@page import="java.util.List"%>

<%
    Usuario admin = (Usuario) session.getAttribute("usuarioLogueado");
    if (admin == null || !"admin".equalsIgnoreCase(admin.getRol())) {
        response.sendRedirect("loginAdmin.jsp");
        return;
    }
    Controladora control = new Controladora();
    List<Curso> cursos = control.traerCursos();
    List<Instructor> instructores = control.traerInstructores();
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lista de Cursos</title>
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

    <h1>
        Lista de Cursos
        <button class="registrar" onclick="abrirRegistro()" style="min-width: 100px;">Agregar Curso</button>
    </h1>

    <table>
        <tr>
            <th>ID</th><th>Nombre</th><th>Descripción</th><th>Instructor</th><th>Acciones</th>
        </tr>
        <%
            for (Curso c : cursos) {
                Instructor i = c.getInstructor();
        %>
        <tr>
            <td><%= c.getIdCurso() %></td>
            <td><%= c.getNombre() %></td>
            <td><%= c.getDescripcion() %></td>
            <td><%= (i != null) ? i.getUsuario().getNombre() : "Sin asignar" %></td>
            <td>
                <a href="listaActividades.jsp?idCurso=<%= c.getIdCurso() %>">
                    <button class="actividades">Actividades</button>
                </a>
                <button class="editar" onclick="abrirEditar('<%= c.getIdCurso() %>', '<%= c.getNombre() %>', '<%= c.getDescripcion() %>', '<%= (i != null ? i.getIdInstructor() : "") %>')">Editar</button>
                <button class="eliminar" onclick="confirmarEliminar('<%= c.getIdCurso() %>')">Eliminar</button>
            </td>
        </tr>
        <% } %>
    </table>
    
    <!-- MODAL REGISTRAR -->
    <div class="modal_registro" id="modalRegistro">
        <div class="modal_content_formulario">
          <h3>Registrar Curso</h3>
          <form action="SvCrearCurso" method="POST" accept-charset="UTF-8">
            <input type="text" name="nombre" placeholder="Nombre del curso" required>
            <textarea name="descripcion" placeholder="Descripción" rows="3" style="width: 85%" required></textarea>

            <!-- Custom Select -->
            <div class="custom-select" id="selectInstructor">
              <div class="select-header">Selecciona un instructor</div>
              <div class="select-options">
                <% for (Instructor i : instructores) { %>
                  <div data-value="<%= i.getIdInstructor() %>">
                    <%= i.getUsuario().getNombre() %> <%= i.getUsuario().getApellidos() %>
                  </div>
                <% } %>
              </div>
              <!-- Campo oculto para enviar valor al servidor -->
              <input type="hidden" name="idInstructor" id="idInstructorHidden" required>
            </div>

            <div class="modal-buttons_lista">
              <button type="button" onclick="cerrarModal()">Cancelar</button>
              <button type="submit">Guardar</button>
            </div>
          </form>
        </div>
    </div>
    
    <!-- MODAL EDITAR -->
    <div class="modal_editar" id="modalEditar">
        <div class="modal_content_formulario">
            <h3>Editar Curso</h3>
            <form action="SvEditarCurso" method="POST">
                <input type="hidden" name="idCurso" id="editIdCurso">
                <input type="text" name="nombre" id="editNombre" placeholder="Nombre del curso" required>
                <textarea name="descripcion" id="editDescripcion" placeholder="Descripción" rows="3" style="width: 85%" required></textarea>

                    <select name="idInstructor" id="editInstructor" required>

                        <option value="">Selecciona un instructor</option>
                        <%
                            for (Instructor inst : instructores) {
                        %>
                            <option value="<%= inst.getIdInstructor() %>"><%= inst.getUsuario().getNombre() %> <%= inst.getUsuario().getApellidos() %></option>
                        <%
                            }
                        %>
                    </select>
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
            <h3>¿Eliminar este curso?</h3>
            <form action="SvEliminarCurso" method="POST">
                <input type="hidden" name="idCurso" id="deleteIdCurso">
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
        
        function abrirEditar(id, nombre, descripcion, idInstructor) {
            document.getElementById("editIdCurso").value = id;
            document.getElementById("editNombre").value = nombre;
            document.getElementById("editDescripcion").value = descripcion;
            document.getElementById("editInstructor").value = idInstructor;
            document.getElementById("modalEditar").style.display = 'flex';
        }

        function confirmarEliminar(id) {
            document.getElementById("deleteIdCurso").value = id;
            document.getElementById("modalEliminar").style.display = 'flex';
        }

        function cerrarModal() {
            document.getElementById('modalRegistro').style.display = 'none';
            document.getElementById("modalEditar").style.display = 'none';
            document.getElementById("modalEliminar").style.display = 'none';
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

<%-- login_registro.jsp --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    // Prevenir que la página quede en caché del navegador
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    // Si ya hay sesión activa, redirigir al inicio
    if (session.getAttribute("usuarioLogueado") != null) {
        response.sendRedirect("index.jsp");
        return;
    }

    // Recuperar el token CSRF de la sesión (si ya existe)
    // El token se genera en SvInicioSesion y SvUsuarios al iniciar sesión
    // Para el formulario de login/registro no es obligatorio (no hay sesión aún),
    // pero lo incluimos si ya existe por seguridad adicional.
    String csrfToken = (String) session.getAttribute("csrfToken");
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Acceso / Registro — TECNOAPRENDE</title>
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

        <div class="tabs">
            <button onclick="showTab('login')" class="active" id="tab-login">Iniciar Sesión</button>
            <button onclick="showTab('register')" id="tab-register">Registrarse</button>
        </div>

        <div class="form-wrapper">
            <div class="form-container" id="form-container">

                <%-- ===== FORMULARIO DE INICIO DE SESIÓN ===== --%>
                <div id="login" class="tab-content active">
                    <h1 class="formulario">Iniciar Sesión</h1>
                    <form action="SvInicioSesion" method="POST" accept-charset="UTF-8" autocomplete="off">

                        <%-- Token CSRF (si la sesión ya lo tiene) --%>
                        <% if (csrfToken != null) { %>
                            <input type="hidden" name="csrfToken" value="<%= csrfToken %>">
                        <% } %>

                        <p>
                            <label for="loginUsuario">Usuario:</label>
                            <input type="text"
                                   id="loginUsuario"
                                   name="usuario"
                                   placeholder="Ingrese su Nombre de Usuario"
                                   maxlength="50"
                                   required
                                   autocomplete="username">
                        </p>
                        <p>
                            <label for="loginContrasena">Contraseña:</label>
                            <input type="password"
                                   id="loginContrasena"
                                   name="contrasena"
                                   placeholder="Ingrese su Contraseña"
                                   maxlength="200"
                                   required
                                   autocomplete="current-password">
                        </p>
                        <button type="submit" class="boton_iniciosesion_registro">Iniciar sesión</button>
                    </form>

                    <%-- Mensaje de error de inicio de sesión --%>
                    <%
                        String error = (String) request.getAttribute("error");
                        if (error != null) {
                    %>
                        <p style="color: red; text-align: center;"><%= error %></p>
                    <% } %>
                </div>

                <%-- ===== FORMULARIO DE REGISTRO ===== --%>
                <div id="register" class="tab-content">
                    <h1 class="formulario">Registrarse</h1>
                    <form action="SvUsuarios" id="miFormulario" method="POST"
                          accept-charset="UTF-8" autocomplete="off"
                          onsubmit="return validarFormularioRegistro()">

                        <p>
                            <label for="regNombre">Nombre:</label>
                            <input type="text"
                                   id="regNombre"
                                   name="nombre"
                                   placeholder="Ingrese su Nombre"
                                   maxlength="100"
                                   required
                                   autocomplete="given-name">
                        </p>
                        <p>
                            <label for="regApellidos">Apellidos:</label>
                            <input type="text"
                                   id="regApellidos"
                                   name="apellidos"
                                   placeholder="Ingrese sus Apellidos"
                                   maxlength="100"
                                   required
                                   autocomplete="family-name">
                        </p>
                        <p>
                            <label for="regUsuario">Nombre de Usuario:</label>
                            <input type="text"
                                   id="regUsuario"
                                   name="usuario"
                                   placeholder="Sin espacios. Ej: juan_perez"
                                   maxlength="50"
                                   pattern="[a-zA-Z0-9_\-]{3,50}"
                                   title="Solo letras, números, guión y guión bajo. Mínimo 3 caracteres."
                                   required
                                   autocomplete="username">
                        </p>
                        <p>
                            <label for="regContrasena">Contraseña:</label>
                            <input type="password"
                                   id="regContrasena"
                                   name="contrasena"
                                   placeholder="Mínimo 8 caracteres"
                                   minlength="8"
                                   maxlength="100"
                                   required
                                   autocomplete="new-password">
                        </p>
                        <p>
                            <label for="regConfirmar">Confirmar Contraseña:</label>
                            <input type="password"
                                   id="regConfirmar"
                                   placeholder="Repita su contraseña"
                                   maxlength="100"
                                   required
                                   autocomplete="new-password">
                        </p>
                        <button type="submit" class="boton_iniciosesion_registro">Registrarse</button>
                    </form>

                    <%-- Mensaje de error de registro --%>
                    <%
                        String errorMessage = (String) request.getAttribute("errorMessage");
                        if (errorMessage != null) {
                    %>
                        <div style="color:red; text-align: center;"><%= errorMessage %></div>
                    <% } %>
                </div>

            </div>
        </div>

        <script>
            // ===== Validación del formulario de registro en el cliente =====
            function validarFormularioRegistro() {
                const contrasena = document.getElementById('regContrasena').value;
                const confirmar  = document.getElementById('regConfirmar').value;
                const usuario    = document.getElementById('regUsuario').value;

                // Verificar que las contraseñas coincidan
                if (contrasena !== confirmar) {
                    alert('Las contraseñas no coinciden. Por favor, verifique.');
                    return false;
                }

                // Verificar longitud mínima
                if (contrasena.length < 8) {
                    alert('La contraseña debe tener al menos 8 caracteres.');
                    return false;
                }

                // Verificar formato del nombre de usuario
                const patronUsuario = /^[a-zA-Z0-9_\-]{3,50}$/;
                if (!patronUsuario.test(usuario)) {
                    alert('El nombre de usuario solo puede contener letras, números, guión y guión bajo. Mínimo 3 caracteres.');
                    return false;
                }

                return true;
            }

            // ===== Tabs de login / registro =====
            function showTab(tabId) {
                document.querySelectorAll('.tab-content').forEach(div => {
                    div.classList.remove('active');
                });
                document.querySelectorAll('.tabs button').forEach(button => {
                    button.classList.remove('active');
                });

                document.getElementById(tabId).classList.add('active');
                document.getElementById('tab-' + tabId).classList.add('active');

                const container = document.getElementById('form-container');
                if (tabId === 'register') {
                    container.classList.add('slide-to-register');
                    container.classList.remove('slide-to-login');
                } else {
                    container.classList.remove('slide-to-register');
                    container.classList.add('slide-to-login');
                }
            }

            // Abrir la pestaña correcta según el parámetro de la URL
            document.addEventListener('DOMContentLoaded', () => {
                const urlParams = new URLSearchParams(window.location.search);
                const initialTab = urlParams.get('tab');
                showTab(initialTab === 'register' ? 'register' : 'login');
            });
        </script>
    </body>
</html>

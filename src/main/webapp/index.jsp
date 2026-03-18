<%-- index.jsp --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="Logica.Usuario"%>
<%
    // No cache
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
%>


<!DOCTYPE html>
<html lang="es">
    <head>
      <meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1.0">
      <title>TECNOAPRENDE</title>
      <link rel="stylesheet" href="styles.css">
      <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    </head>
    <body>
        <!-- Encabezado -->
        <header class="encabezado">
          <img src="images/ITSZ-LCNTEZ.png" alt="Encabezado de logos" class="imagen-encabezado">
          <div class="acciones">
              <c:choose>
                <c:when test="${sessionScope.usuarioLogueado == null}">
                    <a href="login_registro.jsp"><button>Iniciar sesión</button></a>
                    <a href="login_registro.jsp?tab=register"><button>Registrarse</button></a>
                </c:when>

                <c:otherwise>
                    <p>Bienvenido, 
                       <strong>${fn:escapeXml(sessionScope.usuarioLogueado.nom_usuario)}</strong>
                    </p>

                    <form action="SvCerrarSesion" method="POST" style="display:inline;">
                        <input type="hidden" name="csrfToken" value="<%= session.getAttribute("csrfToken") %>">
                        <button type="submit">Cerrar sesión</button>
                    </form>
                </c:otherwise>
            </c:choose>
          </div>
        </header>
          
        <!-- Barra de navegación tipo Capacítate -->
        <nav class="barra-pasos">
            <div class="paso paso1">
                <span class="numero">1</span>
                <span class="texto">Bienvenida</span>
            </div>
            <div class="paso paso2">
                <span class="numero">2</span>
                <span class="texto">Inscríbete</span>
            </div>
            <div class="paso paso3">
                <span class="numero">3</span>
                <span class="texto">Cursos</span>
            </div>
            <div class="paso paso4">
                <span class="numero">4</span>
                <span class="texto">Más información...</span>
            </div>
        </nav>

        <!-- Carrusel -->
        <section class="carrusel">
            <div class="slides">
                <div class="slide activo">
                    <img src="images/slide1.jpg" alt="Cursos TIC">
                    <div class="texto-slide">
                        <h2>Aprende e impulsa tus habilidades digitales</h2>
                        <p>Cursos gratuitos de TICs para todos</p>
                    </div>
                </div>

                <div class="slide">
                    <img src="images/slide2.jpg" alt="Certificados">
                    <div class="texto-slide">
                        <h2>Certifícate y mejora tu perfil</h2>
                        <p>Conocimiento práctico y accesible</p>
                    </div>
                </div>

                <div class="slide">
                    <img src="images/slide3.jpg" alt="Educación">
                    <div class="texto-slide">
                        <h2>Educación para el futuro</h2>
                        <p>Aprende a tu ritmo</p>
                    </div>
                </div>
            </div>

            <div class="indicadores">
                <span class="dot activo"></span>
                <span class="dot"></span>
                <span class="dot"></span>
            </div>
        </section>
        
      <!-- Cursos -->
        <section class="tarjetas-home">
            <% if (usuario != null) { %>

            <div class="tarjeta">
                <i class="fas fa-user-graduate"></i>
                <h3>Curso Principiante</h3>
                <p>Aprende desde cero las bases de las TIC.</p>
                <button onclick="location.href='cursoPrincipiante.jsp'">Ver curso</button>
            </div>

            <div class="tarjeta">
                <i class="fas fa-laptop-code"></i>
                <h3>Curso Avanzado</h3>
                <p>Refuerza tus conocimientos y habilidades digitales.</p>
                <button onclick="location.href='cursoAvanzado.jsp'">Ver curso</button>
            </div>

            <div class="tarjeta">
                <i class="fas fa-circle-info"></i>
                <h3>Más información</h3>
                <p>Conoce más sobre la plataforma TecnoAprende.</p>
                <button onclick="location.href='informacion_adicional.jsp'">Ver más</button>
            </div>

            <% } else { %>

            <div class="tarjeta">
                <i class="fas fa-user-graduate"></i>
                <h3>Curso Principiante</h3>
                <p>Inicia sesión para acceder al contenido.</p>
                <button onclick="location.href='login_registro.jsp'">Iniciar sesión</button>
            </div>

            <div class="tarjeta">
                <i class="fas fa-laptop-code"></i>
                <h3>Curso Avanzado</h3>
                <p>Regístrate para continuar aprendiendo.</p>
                <button onclick="location.href='login_registro.jsp'">Registrarse</button>
            </div>

            <div class="tarjeta">
                <i class="fas fa-circle-info"></i>
                <h3>Más información</h3>
                <p>Descubre qué es TecnoAprende.</p>
                <button onclick="location.href='login_registro.jsp'">Ver información</button>
            </div>

            <% } %>
        </section>

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
    let index = 0;
    const slides = document.querySelectorAll(".slide");
    const dots = document.querySelectorAll(".dot");

    setInterval(() => {
        slides[index].classList.remove("activo");
        dots[index].classList.remove("activo");

        index = (index + 1) % slides.length;

        slides[index].classList.add("activo");
        dots[index].classList.add("activo");
    }, 5000);
    </script>

    </body>
</html>

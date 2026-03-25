<%--creditos.jsp--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Créditos</title>
        <link rel="stylesheet" href="styles.css">
    </head>
    <body>

    <div class="creditos-container">
        <h1>Créditos del Proyecto</h1>
        <p class="creditos-subtitulo">Plataforma de Aprendizaje de TICS para la Casa de los Niños de Tezonapa</p>

        <h2>Equipo BOX Code</h2>

        <div class="creditos-cards">

            <!-- Integrante 1 -->
            <div class="creditos-card">
                <img src="images/fotos/brian.jpeg" alt="Foto">
                <h3>Brian Flores Zavaleta</h3>
                <h4>Líder del Equipo</h4>
                <p class="info">Estudiante de Ingeniería en Sistemas Computacionales</p>
            </div>

            <!-- Integrante 2 -->
            <div class="creditos-card">
                <img src="images/fotos/orlando.jpeg" alt="Foto">
                <h3>Víctor Orlando Contreras Cabrera</h3>
                <p class="info">Estudiante de Ingeniería en Sistemas Computacionales</p>
            </div>

            <!-- Integrante 3 -->
            <div class="creditos-card">
                <img src="images/fotos/ximena.jpeg" alt="Foto">
                <h3>Ximena Rosario Méndez Antemate</h3>
                <p class="info">Estudiante de Ingeniería en Sistemas Computacionales</p>
            </div>

        </div>

        <h2>Asesora del Proyecto</h2>
        <div class="creditos-card">
            <div class="card">
                <img src="images/fotos/irene.jpeg" alt="Asesor">
                <h3>Mtra. Irene Gutíerrez Mora</h3>
                <p>Asesor Académico y Técnico</p>
            </div>
        </div>

        <h2>Institución</h2>
        <div class="creditos-institucion">
            <img src="images/fotos/logoITSZ.jpg" class="creditos-logo">
            <p>Instituto Tecnológico Superior de Zongolica</p>
            <p>Ingeniería en Sistemas Computacionales</p>
            <p>Proyecto desarrollado en 2024 - 2026</p>
        </div>

    </div>

    </body>
</html>

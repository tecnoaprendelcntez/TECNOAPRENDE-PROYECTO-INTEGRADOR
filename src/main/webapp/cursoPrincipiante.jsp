<%-- cursoPrincipiante.jsp --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Logica.*"%>
<%@page import="java.util.*"%>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
    if (usuario == null) {
        response.sendRedirect("login_registro.jsp");
        return;
    }
    
    Controladora control = new Controladora();
    int idCurso = 1; // ID real del curso principiante
    List<Actividad> actividades = control.traerActividadesPorCurso(idCurso);
    List<Progreso> progresos = control.traerProgresoPorCursoYUsuario(idCurso, usuario.getId());
    
    // Preparar listas para JS
    List<String> nombresAct = new ArrayList<>();
    List<Double> calificacionesAct = new ArrayList<>();

    double sumaActividades = 0.0;
    for (Actividad act : actividades) {
        nombresAct.add(act.getTitulo());
        double cal = 0.0;
        for (Progreso p : progresos) {
            if (p.getActividad() != null && p.getActividad().getIdActividad() == act.getIdActividad()) {
                cal = p.getCalificacion();
                break;
            }
        }
        calificacionesAct.add(cal);
        sumaActividades += cal;
    }

    // Promedio de actividades (si no hay actividades -> 0)
    double promedioActividades = 0.0;
    if (actividades != null && actividades.size() > 0) {
        promedioActividades = sumaActividades / actividades.size();
    }

    // Calificación de la evaluación final (si no existe, control.traerCalificacionEvaluacion debe devolver 0)
    double calificacionEvaluacion = control.traerCalificacionEvaluacion(usuario.getId(), idCurso);

    // Ponderaciones
    double ponderacionActividades = 0.6; // 60%
    double ponderacionEvaluacion = 0.4;  // 40%

    double aporteActividades = promedioActividades * ponderacionActividades;
    double aporteEvaluacion = calificacionEvaluacion * ponderacionEvaluacion;
    double calificacionFinal = aporteActividades + aporteEvaluacion;

    // Preparar listas para la gráfica: actividades + etiqueta Evaluación final
    List<String> etiquetasGrafica = new ArrayList<>(nombresAct);
    etiquetasGrafica.add("Evaluación final");

    List<Double> datosGrafica = new ArrayList<>(calificacionesAct);
    datosGrafica.add(calificacionEvaluacion);

    // Redondeos para mostrar
    double calificacionFinalRedondeada = Math.round(calificacionFinal * 100.0) / 100.0; // 2 decimales
    double promedioActividadesRedondeado = Math.round(promedioActividades * 100.0) / 100.0;
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Curso Principiante - TecnoAprende</title>
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

        <main class="contenedor-curso">
            <section class="introduccion-modulo">
                <h1>Curso Principiante</h1>
                <nav class="menu-navegacion">
                    <a class="activo" id="btn-curso">Curso</a>
                    <a id="btn-progreso">Progreso</a>
                </nav>
            </section>

            <!-- CONTENIDO: menú lateral + area principal -->
            <div class="contenido-curso">
                <div class="menu-container">
                    <!-- MENÚ LATERAL -->
                    <div class="menu-lateral">
                        <!-- 1.1 -->
                        <button class="menu-btn" onclick="toggleSubmenu('sub-1-1')">
                            1.1. Introducción a la Computadora: ¿Qué es una computadora? y Sus partes principales 
                            <span class="chev">▸</span>
                        </button>
                        <div id="sub-1-1" class="submenu">
                            <button onclick="cargarContenido('1.1.doc')">Documento</button>
                            <button onclick="cargarContenido('1.1.actividad')">Actividad</button>
                        </div>

                        <!-- 1.2 -->
                        <button class="menu-btn" onclick="toggleSubmenu('sub-1-2')">
                            1.2. Encendido y Apagado: Procedimientos Seguros 
                            <span class="chev">▸</span>
                        </button>
                        <div id="sub-1-2" class="submenu">
                            <button onclick="cargarContenido('1.2.doc')">Documento</button>
                            <button onclick="cargarContenido('1.2.actividad')">Actividad</button>
                        </div>

                        <!-- 1.3 -->
                        <button class="menu-btn" onclick="toggleSubmenu('sub-1-3')">
                            1.3. Interfaz Gráfica: Explorar el escritorio, iconos y ventanas 
                            <span class="chev">▸</span>
                        </button>
                        <div id="sub-1-3" class="submenu">
                            <button onclick="cargarContenido('1.3.doc')">Documento</button>
                            <button onclick="cargarContenido('1.3.actividad')">Actividad</button>
                        </div>

                        <!-- 1.4 -->
                        <button class="menu-btn" onclick="toggleSubmenu('sub-1-4')">
                            1.4. Uso del Ratón: Clic izquierdo, clic derecho, doble clic y arrastrar 
                            <span class="chev">▸</span>
                        </button>
                        <div id="sub-1-4" class="submenu">
                            <button onclick="cargarContenido('1.4.doc')">Documento</button>
                            <button onclick="cargarContenido('1.4.actividad')">Actividad</button>
                        </div>

                        <!-- 1.5 -->
                        <button class="menu-btn" onclick="toggleSubmenu('sub-1-5')">
                            1.5. Teclado: Teclas principales y funciones básicas 
                            <span class="chev">▸</span>
                        </button>
                        <div id="sub-1-5" class="submenu">
                            <button onclick="cargarContenido('1.5.doc')">Documento</button>
                            <button onclick="cargarContenido('1.5.actividad')">Actividad</button>
                        </div>

                        <!-- 1.6 -->
                        <button class="menu-btn" onclick="toggleSubmenu('sub-1-6')">
                            1.6. Exploración de Archivos: Carpetas, documentos, crear, abrir y guardar archivos 
                            <span class="chev">▸</span>
                        </button>
                        <div id="sub-1-6" class="submenu">
                            <button onclick="cargarContenido('1.6.doc')">Documento</button>
                            <button onclick="cargarContenido('1.6.actividad')">Actividad</button>
                        </div>

                        <form action="SvIniciarEvaluacion" method="GET">
                            <input type="hidden" name="idCurso" value="1">
                            <input type="hidden" name="vista" value="principiante">
                            <button type="submit" class="menu-btn">Realizar evaluación final</button>
                        </form>
                    </div>

                    <!-- AREA DE CONTENIDO -->
                    <div class="contenido-principal">
                        <div id="contenidoCurso" class="area-contenido">
                            <h2>Selecciona un documento o actividad del menú</h2>
                            <h3>Los documentos se abrirán en esta área.</h3>
                        </div>

                        <div id="contenidoProgreso" class="seccion">
                            <!-- Elementos que el script actualiza -->
                            <div style="margin-bottom:10px;">
                                <strong>Promedio actividades:</strong> <span id="promedioActividades"><%= promedioActividadesRedondeado %></span>% 
                                &nbsp; | &nbsp;
                                <strong>Evaluación final (último intento):</strong> <span id="notaEvaluacion"><%= calificacionEvaluacion %></span>%
                            </div>
                            
                            <div id="graficaWrapper">
                                <canvas id="graficaProgreso"></canvas>
                            </div>

                            <h3 style="margin-top:12px;">Calificación final del curso: <span id="calificacionFinal"><%= calificacionFinalRedondeada %></span>%</h3>
                        </div>
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
                        <a href="https://www.facebook.com/TecNMZongolica" target="_blank"><i class="fab fa-facebook"></i></a>
                        <a href="https://www.youtube.com/channel/UCi0_QXTliS2p_2MDwhfF8ww" target="_blank"><i class="fab fa-youtube"></i></a>
                        <a href="https://x.com/somositsz?lang=es" target="_blank"><i class="fab fa-x"></i></a>
                    </div>
                </div>
                <div class="casa">
                    <a href="https://lcntez.org.mx/">La Casa de los Niños de Tezonapa</a>
                    <div class="redes">
                        <a href="https://www.facebook.com/profile.php?id=100078645709893" target="_blank"><i class="fab fa-facebook"></i></a>
                        <a href="https://www.instagram.com/ninos_tezonapa" target="_blank"><i class="fab fa-instagram"></i></a>
                        <a href="https://x.com/Ninos_Tezonapa" target="_blank"><i class="fab fa-x"></i></a>
                    </div>
                </div>
            </div>
            <div class="creditos-equipo">
                <a href="creditos.jsp"><p>© 2025 TECNOAPRENDE. Plataforma desarrollada por equipo BOX Code. Todos los derechos reservados.</p></a>
            </div>
        </footer>
        
        <!-- FUNCIONES ACTIVIDADES -->
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script>
            // --- Menú lateral dinámico ---
            function toggleSubmenu(id) {
                const s = document.getElementById(id);
                if (!s) return;
                s.style.display = s.style.display === "block" ? "none" : "block";
            }
            
            // Contenidos (documentos y galerías)
            const contenidos = {
                /* 1. Curso Principiante */
                "1.1.doc": `<div class="visor-pdf">
                                <iframe src="docs/CursoPrincipiante/Documentos/1.1.CursoPrincipiante-doc.pdf#zoom=page-width"></iframe>
                            </div>`,
                "1.1.actividad": `<div class="visor-pdf">
                                    <iframe src="docs/CursoPrincipiante/Actividades/1.1.CursoPrincipiante-act.pdf"></iframe>
                                  </div>`,

                "1.2.doc": `<div class="visor-pdf">
                                <iframe src="docs/CursoPrincipiante/Documentos/1.2.CursoPrincipiante-doc.pdf"></iframe>
                            </div>`,
                "1.2.actividad": `<div class="visor-pdf">
                                    <iframe src="docs/CursoPrincipiante/Actividades/1.2.CursoPrincipiante-act.pdf"></iframe>
                                  </div>`,

                "1.3.doc": `<div class="visor-pdf">
                                <iframe src="docs/CursoPrincipiante/Documentos/1.3.CursoPrincipiante-doc.pdf"></iframe>
                            </div>`,
                "1.3.actividad": `<div class="visor-pdf">
                                    <iframe src="docs/CursoPrincipiante/Actividades/1.3.CursoPrincipiante-act.pdf"></iframe>
                                  </div>`,

                "1.4.doc": `<div class="visor-pdf">
                                <iframe src="docs/CursoPrincipiante/Documentos/1.4.CursoPrincipiante-doc.pdf"></iframe>
                            </div>`,
                "1.4.actividad": `<div class="visor-pdf">
                                    <iframe src="docs/CursoPrincipiante/Actividades/1.4.CursoPrincipiante-act.pdf"></iframe>
                                  </div>`,

                "1.5.doc": `<div class="visor-pdf">
                                <iframe src="docs/CursoPrincipiante/Documentos/1.5.CursoPrincipiante-doc.pdf"></iframe>
                            </div>`,
                "1.5.actividad": `<div class="visor-pdf">
                                    <iframe src="docs/CursoPrincipiante/Actividades/1.5.CursoPrincipiante-act.pdf"></iframe>
                                  </div>`,

                "1.6.doc": `<div class="visor-pdf">
                                <iframe src="docs/CursoPrincipiante/Documentos/1.6.CursoPrincipiante-doc.pdf"></iframe>
                            </div>`,
                "1.6.actividad": `<div class="visor-pdf">
                                    <iframe src="docs/CursoPrincipiante/Actividades/1.6.CursoPrincipiante-act.pdf"></iframe>
                                  </div>`
            };
            
            // Mostrar sección curso o progreso
            const btnCurso = document.getElementById("btn-curso");
            const btnProgreso = document.getElementById("btn-progreso");
            const contenidoCurso = document.getElementById("contenidoCurso");
            const contenidoProgreso = document.getElementById("contenidoProgreso");

            btnCurso.addEventListener("click", () => {
                btnCurso.classList.add("activo");
                btnProgreso.classList.remove("activo");
                contenidoCurso.style.display = "block";
                contenidoProgreso.style.display = "none";
            });

            btnProgreso.addEventListener("click", () => {
                btnCurso.classList.remove("activo");
                btnProgreso.classList.add("activo");
                contenidoCurso.style.display = "none";
                contenidoProgreso.style.display = "block";
                cargarGraficaProgreso();
            });
            
            function cargarContenido(clave) {
                const area = document.getElementById("contenidoCurso");  
                if (!area) return;

                if (!contenidos[clave]) {
                    area.innerHTML = `<h2>Contenido no disponible</h2><p>No se encontró contenido para "${clave}".</p>`;
                    return;
                }

                // Limpiar contenido previo
                area.innerHTML = "";
                area.scrollTop = 0;

                // Insertar PDF o actividad
                area.innerHTML = contenidos[clave];

                // Si está en celular o tableta, hacer scroll suave
                if (window.innerWidth <= 768) { 
                    area.scrollIntoView({ behavior: 'smooth', block: 'start' });
                }
            }


              // Marcar botón activo en el menú
            const botonesMenu = document.querySelectorAll(".menu-btn");

            botonesMenu.forEach(btn => {
                btn.addEventListener("click", function () {

                    // Quitar el activo a todos
                    botonesMenu.forEach(b => b.classList.remove("activo"));

                    // Agregar activo al que se presiona
                    this.classList.add("activo");
                });
            });
            
            // Datos pasados desde JSP a JS
            const etiquetas = [<% for (int i = 0; i < etiquetasGrafica.size(); i++) {
                                    out.print("\"" + etiquetasGrafica.get(i).replace("\"","\\\"") + "\"");
                                    if (i < etiquetasGrafica.size() - 1) out.print(",");
                                } %>];

            const datos = [<% for (int i = 0; i < datosGrafica.size(); i++) {
                                out.print(datosGrafica.get(i));
                                if (i < datosGrafica.size() - 1) out.print(",");
                            } %>];

            // Grafica: actividades + evaluación final
            let chartInstance = null;

            function cargarGraficaProgreso() {
                const canvas = document.getElementById('graficaProgreso');
                if (!canvas) return;
                const ctx = canvas.getContext('2d');

                // Si ya existe, destruirla para recrear
                if (chartInstance) {
                    chartInstance.destroy();
                    chartInstance = null;
                }

                // Colores: dar un color distinto a la última barra (evaluación)
                const backgroundColors = datos.map((v, idx) => {
                    if (idx === datos.length - 1) return 'rgba(247, 108, 108, 0.8)'; // evaluación
                    return 'rgba(2, 109, 52, 0.7)'; // actividades
                });
                const borderColors = datos.map((v, idx) => {
                    if (idx === datos.length - 1) return 'rgba(247, 108, 108, 1)';
                    return 'rgba(2, 109, 52, 1)';
                });

                chartInstance = new Chart(ctx, {
                    type: 'bar',
                    data: {
                        labels: etiquetas,
                        datasets: [{
                            label: 'Calificación (%)',
                            data: datos,
                            backgroundColor: backgroundColors,
                            borderColor: borderColors,
                            borderWidth: 1
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: true, // <- Cambiado a true para evitar reflow infinito
                        scales: {
                            y: {
                                beginAtZero: true,
                                max: 100,
                                title: { display: true, text: 'Porcentaje (%)' }
                            }
                        },
                        plugins: {
                            legend: { display: false },
                            tooltip: {
                                callbacks: {
                                    label: function(context) {
                                        // Chart v3/v4 usa context.parsed.y
                                        const v = context.parsed && context.parsed.y !== undefined ? context.parsed.y : context.raw;
                                        return v + '%';
                                    }
                                }
                            }
                        }
                    }
                });

                // actualizar los valores mostrados arriba (por si cambian)
                const promedioEl = document.getElementById('promedioActividades');
                if (promedioEl) promedioEl.innerText = "<%= promedioActividadesRedondeado %>";
                const notaEvalEl = document.getElementById('notaEvaluacion');
                if (notaEvalEl) notaEvalEl.innerText = "<%= calificacionEvaluacion %>";
                const finalEl = document.getElementById('calificacionFinal');
                if (finalEl) finalEl.innerText = "<%= calificacionFinalRedondeada %>";
            }
        </script>
    </body>
</html>
package Logica;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import persistencia.ControladoraPersistencia;

public class Controladora {

    ControladoraPersistencia controlPersis = new ControladoraPersistencia();
    private final double MIN_PARA_APROBAR = 60.0;
    private final int MAX_INTENTOS = 2;

    // --- Usuario ---
    public void crearUsuario(Usuario usuario) {
        controlPersis.crearUsuario(usuario);
    }

    public List<Usuario> traerUsuarios() {
        return controlPersis.traerUsuarios();
    }

    public void borrarUsuario(int id_eliminar) {
        controlPersis.borrarUsuario(id_eliminar);
    }

    public Usuario traerUsuario(int id_editar) {
        return controlPersis.traerUsuario(id_editar);
    }

    public void editarUsuario(Usuario usuario) {
        controlPersis.editarUsuario(usuario);
    }

    public boolean existeUsuario(String nom_usuario) {
        return controlPersis.existeUsuario(nom_usuario);
    }

    // --- Instructor ---
    public void crearInstructor(Instructor instructor) {
        controlPersis.crearInstructor(instructor);
    }

    public List<Instructor> traerInstructores() {
        return controlPersis.traerInstructores();
    }

    public void borrarInstructor(int id_eliminar) {
        controlPersis.borrarInstructor(id_eliminar);
    }

    public Instructor traerInstructor(int id_editar) {
        return controlPersis.traerInstructor(id_editar);
    }
    
    public Usuario buscarUsuarioPorNombre(String nomUsuario) {
        return controlPersis.buscarUsuarioPorNombre(nomUsuario);
    }

    public void editarInstructor(Instructor instructor) {
        controlPersis.editarInstructor(instructor);
    }
    
    public List<Curso> traerCursosPorInstructor(int idInstructor) {
        return controlPersis.traerCursosPorInstructor(idInstructor);
    }
    
    // --- Administrador ---
    public void crearAdministrador(Administrador administrador) {
        controlPersis.crearAdministrador(administrador);
    }

    public List<Administrador> traerAdministradores() {
        return controlPersis.traerAdministradores();
    }

    public void borrarAdministrador(int id_eliminar) {
        controlPersis.borrarAdministrador(id_eliminar);
    }

    public Administrador traerAdministrador(int id_editar) {
        return controlPersis.traerAdministrador(id_editar);
    }

    public void editarAdministrador(Administrador administrador) {
        controlPersis.editarAdministrador(administrador);
    }

    // --- Curso ---
    public void crearCurso(Curso curso) {
        controlPersis.crearCurso(curso);
    }

    public List<Curso> traerCursos() {
        return controlPersis.traerCursos();
    }

    public void borrarCurso(int id_eliminar) {
        controlPersis.borrarCurso(id_eliminar);
    }

    public Curso traerCurso(int id_editar) {
        return controlPersis.traerCurso(id_editar);
    }

    public void editarCurso(Curso curso) {
        controlPersis.editarCurso(curso);
    }
    
    public List<Usuario> traerEstudiantesPorCurso(int idCurso) {
        return controlPersis.traerEstudiantesPorCurso(idCurso);
    }

    // --- Actividad ---
    public void crearActividad(Actividad actividad) {
        controlPersis.crearActividad(actividad);
    }

    public List<Actividad> traerActividades() {
        return controlPersis.traerActividades();
    }

    public void borrarActividad(int id_eliminar) {
        controlPersis.borrarActividad(id_eliminar);
    }

    public Actividad traerActividad(int id_editar) {
        return controlPersis.traerActividad(id_editar);
    }
    
    public List<Actividad> traerActividadesPorCurso(int idCurso) {
        return controlPersis.traerActividadesPorCurso(idCurso);
    }

    public void editarActividad(Actividad actividad) {
        controlPersis.editarActividad(actividad);
    }

    // --- Progreso ---
    public void crearProgreso(Progreso progreso) {
        controlPersis.crearProgreso(progreso);
    }

    public List<Progreso> traerProgresos() {
        return controlPersis.traerProgresos();
    }

    public void borrarProgreso(int id_eliminar) {
        controlPersis.borrarProgreso(id_eliminar);
    }

    public Progreso traerProgreso(int id_editar) {
        return controlPersis.traerProgreso(id_editar);
    }
    
    public List<Progreso> traerProgresoPorCursoYUsuario(int idCurso, int idUsuario) {
        return controlPersis.traerProgresoPorCursoYUsuario(idCurso, idUsuario);
    }

    public void editarProgreso(Progreso progreso) {
        controlPersis.editarProgreso(progreso);
    }
    
    public void actualizarProgreso(int idProgreso, String estado, int calificacion) {
        controlPersis.actualizarProgreso(idProgreso, estado, calificacion);
    }
    
    public void inicializarProgresoParaUsuario(int idUsuario) {
        System.out.println(">>> Inicializando progreso para usuario ID: " + idUsuario);
        Usuario usuario = this.traerUsuario(idUsuario);

        List<Curso> cursos = this.traerCursos();

        for (Curso curso : cursos) {

            List<Actividad> actividades = this.traerActividadesPorCurso(curso.getIdCurso());

            for (Actividad act : actividades) {
                System.out.println("Creando progreso para actividad: " + act.getTitulo());
                Progreso prog = new Progreso();
                prog.setUsuario(usuario);
                prog.setCurso(curso);
                prog.setActividad(act);

                prog.setEstado("No iniciado");
                prog.setCalificacion(0);
                prog.setFecha(new java.util.Date());

                this.crearProgreso(prog);
            }
        }
    }
    
    // --- EvaluaciónFinal ---
    public EvaluacionFinal getEvaluacionPorCurso(Integer idCurso) {
        List<EvaluacionFinal> l = controlPersis.obtenerEvaluacionesPorCurso(idCurso);
        if (l == null || l.isEmpty()) return null;
        return l.get(0);
    }

    public List<PreguntaEvaluacion> getPreguntasDeEvaluacion(Integer idEvaluacion) {
        return controlPersis.obtenerPreguntasPorEvaluacion(idEvaluacion);
    }

    public boolean puedeIntentar(Integer idUsuario, Integer idEvaluacion) {
        long intentos = controlPersis.contarIntentos(idUsuario, idEvaluacion);
        return intentos < MAX_INTENTOS;
    }
    
    public long contarIntentos(Integer idUsuario, Integer idEvaluacion) {
        return (int) controlPersis.contarIntentos(idUsuario, idEvaluacion);
    }

    
    // --- PreguntaEvaluación ---
    public Map<String, Object> generarResumen(Integer idEvaluacion, Map<Integer, String> respuestas) {

        List<PreguntaEvaluacion> preguntas = getPreguntasDeEvaluacion(idEvaluacion);

        int correctas = 0;
        List<Map<String, String>> detalles = new ArrayList<>();

        for (PreguntaEvaluacion p : preguntas) {

            String respUsuario = respuestas.get(p.getIdPregunta());
            boolean ok = respUsuario != null && p.getRespuestaCorrecta().equalsIgnoreCase(respUsuario);

            if (ok) correctas++;

            Map<String, String> det = new HashMap<>();
            det.put("pregunta", p.getEnunciado());
            det.put("respuestaUsuario", respUsuario != null ? respUsuario : "Sin responder");
            det.put("respuestaCorrecta", p.getRespuestaCorrecta());
            det.put("resultado", ok ? "Correcto" : "Incorrecto");
            det.put("correcta", ok ? "true" : "false");

            detalles.add(det);
        }

        int total = preguntas.size();
        double porcentaje = ((double) correctas / total) * 100.0;
        String estado = porcentaje >= MIN_PARA_APROBAR ? "Aprobado" : "Reprobado";

        Map<String, Object> resumen = new HashMap<>();
        resumen.put("total", total);
        resumen.put("correctas", correctas);
        resumen.put("porcentaje", porcentaje);
        resumen.put("estado", estado);
        resumen.put("detalles", detalles);

        return resumen;
    }


    
    // --- Resultado Evaluación ---
    public ResultadoEvaluacion procesarYGuardarResultado(Integer idUsuario, Integer idEvaluacion, Map<Integer, String> respuestas) {
        List<PreguntaEvaluacion> preguntas = getPreguntasDeEvaluacion(idEvaluacion);
        if (preguntas == null || preguntas.isEmpty()) return null;

        int total = preguntas.size();
        int correctas = 0;

        for (PreguntaEvaluacion p : preguntas) {
            String dado = respuestas.get(p.getIdPregunta());
            String correcta = p.getRespuestaCorrecta();
            if (dado != null && dado.equalsIgnoreCase(correcta)) {
                correctas++;
            }
        }

        double porcentaje = ((double) correctas / total) * 100.0;
        String estado = porcentaje >= MIN_PARA_APROBAR ? "Aprobado" : "Reprobado";

        ResultadoEvaluacion r = new ResultadoEvaluacion();
        r.setCalificacion((int) Math.round(porcentaje));
        r.setEstado(estado);
        r.setFecha(LocalDateTime.now(ZoneId.of("America/Mexico_City")));
        r.setUsuario(controlPersis.traerUsuario(idUsuario));
        r.setEvaluacion(controlPersis.obtenerEvaluacionPorId(idEvaluacion));

        long prevIntentos = controlPersis.contarIntentos(idUsuario, idEvaluacion);
        r.setIntentos((int) prevIntentos + 1);

        controlPersis.guardarResultado(r);

        return r;
    }
    
    public double traerCalificacionEvaluacion(int idUsuario, int idCurso) {
        EvaluacionFinal evaluacion = getEvaluacionPorCurso(idCurso);
        if (evaluacion == null) return 0;

        int idEvaluacion = evaluacion.getIdEvaluacion();

        List<ResultadoEvaluacion> resultados = controlPersis.obtenerResultadosPorUsuarioYEvaluacion(idUsuario, idEvaluacion);

        if (resultados != null && !resultados.isEmpty()) {
            return resultados.get(0).getCalificacion(); 
        }

        return 0;
    }
}

package persistencia;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import Logica.Usuario;
import Logica.Instructor;
import Logica.Administrador;
import Logica.Curso;
import Logica.Actividad;
import Logica.Progreso;
import Logica.EvaluacionFinal;
import Logica.PreguntaEvaluacion;
import Logica.ResultadoEvaluacion;
import persistencia.exceptions.NonexistentEntityException;

public class ControladoraPersistencia {

    UsuarioJpaController usuarioJpa = new UsuarioJpaController();
    InstructorJpaController instructorJpa = new InstructorJpaController();
    AdministradorJpaController administradorJpa = new AdministradorJpaController();
    CursoJpaController cursoJpa = new CursoJpaController();
    ActividadJpaController actividadJpa = new ActividadJpaController();
    ProgresoJpaController progresoJpa = new ProgresoJpaController();
    EvaluacionFinalJpaController evaluacionfinalJpa = new EvaluacionFinalJpaController();
    PreguntaEvaluacionJpaController preguntaevaluacionJpa = new PreguntaEvaluacionJpaController();
    ResultadoEvaluacionJpaController resultadoevaluacionJpa = new ResultadoEvaluacionJpaController();

    // ------------------- Usuario -------------------

    public void crearUsuario(Usuario usuario) {
        usuarioJpa.create(usuario);
    }

    public List<Usuario> traerUsuarios() {
        return usuarioJpa.findUsuarioEntities();
    }

    public void borrarUsuario(int id_eliminar) {
        try {
            usuarioJpa.destroy(id_eliminar);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Usuario traerUsuario(int id_editar) {
        return usuarioJpa.findUsuario(id_editar);
    }

    public void editarUsuario(Usuario usuario) {
        try {
            usuarioJpa.edit(usuario);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean existeUsuario(String nom_usuario) {
        return usuarioJpa.existeUsuarioPorNombre(nom_usuario);
    }

    // ------------------- Instructor -------------------

    public void crearInstructor(Instructor instructor) {
        instructorJpa.create(instructor);
    }

    public List<Instructor> traerInstructores() {
        return instructorJpa.findInstructorEntities();
    }

    public void borrarInstructor(int id_eliminar) {
        try {
            instructorJpa.destroy(id_eliminar);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Instructor traerInstructor(int id_editar) {
        return instructorJpa.findInstructor(id_editar);
    }

    public Usuario buscarUsuarioPorNombre(String nomUsuario) {
        return usuarioJpa.FindUsuarioByName(nomUsuario);
    }
    
    public void editarInstructor(Instructor instructor) {
        try {
            instructorJpa.edit(instructor);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Curso> traerCursosPorInstructor(int idInstructor) {
        return cursoJpa.findCursosByInstructor(idInstructor);
    }

    // ------------------- Administrador -------------------

    public void crearAdministrador(Administrador administrador) {
        administradorJpa.create(administrador);
    }

    public List<Administrador> traerAdministradores() {
        return administradorJpa.findAdministradorEntities();
    }

    public void borrarAdministrador(int id_eliminar) {
        try {
            administradorJpa.destroy(id_eliminar);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Administrador traerAdministrador(int id_editar) {
        return administradorJpa.findAdministrador(id_editar);
    }

    public void editarAdministrador(Administrador administrador) {
        try {
            administradorJpa.edit(administrador);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ------------------- Curso -------------------

    public void crearCurso(Curso curso) {
        cursoJpa.create(curso);
    }

    public List<Curso> traerCursos() {
        return cursoJpa.findCursoEntities();
    }

    public void borrarCurso(int id_eliminar) {
        try {
            cursoJpa.destroy(id_eliminar);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Curso traerCurso(int id_editar) {
        return cursoJpa.findCurso(id_editar);
    }

    public void editarCurso(Curso curso) {
        try {
            cursoJpa.edit(curso);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Usuario> traerEstudiantesPorCurso(int idCurso) {
        return usuarioJpa.findUsuariosByCurso(idCurso);
    }

    // ------------------- Actividad -------------------

    public void crearActividad(Actividad actividad) {
        actividadJpa.create(actividad);
    }

    public List<Actividad> traerActividades() {
        return actividadJpa.findActividadEntities();
    }

    public void borrarActividad(int id_eliminar) {
        try {
            actividadJpa.destroy(id_eliminar);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Actividad traerActividad(int id_editar) {
        return actividadJpa.findActividad(id_editar);
    }
    
    public List<Actividad> traerActividadesPorCurso(int idCurso) {
        return actividadJpa.findActividadesPorCurso(idCurso);
    }

    public void editarActividad(Actividad actividad) {
        try {
            actividadJpa.edit(actividad);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ------------------- Progreso -------------------

    public void crearProgreso(Progreso progreso) {
        progresoJpa.create(progreso);
    }

    public List<Progreso> traerProgresos() {
        return progresoJpa.findProgresoEntities();
    }

    public void borrarProgreso(int id_eliminar) {
        try {
            progresoJpa.destroy(id_eliminar);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Progreso traerProgreso(int id_editar) {
        return progresoJpa.findProgreso(id_editar);
    }
    
    public List<Progreso> traerProgresoPorCursoYUsuario(int idCurso, int idUsuario) {
        return progresoJpa.findByCursoAndUsuario(idCurso, idUsuario);
    }

    public void editarProgreso(Progreso progreso) {
        try {
            progresoJpa.edit(progreso);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void actualizarProgreso(int idProgreso, String estado, int calificacion) {
        try {
            Progreso prog = progresoJpa.findProgreso(idProgreso);
            if (prog != null) {
                prog.setEstado(estado);
                prog.setCalificacion(calificacion);
                progresoJpa.edit(prog);
            }
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // ------------------- EvaluaciónFinal -------------------
    
    public void crearEvaluacion(EvaluacionFinal e) {
        evaluacionfinalJpa.create(e);
    }
    public EvaluacionFinal obtenerEvaluacionPorId(Integer id) {
        return evaluacionfinalJpa.findEvaluacionFinal(id);
    }
    public List<EvaluacionFinal> obtenerEvaluacionesPorCurso(Integer idCurso) {
        return evaluacionfinalJpa.findByCurso(idCurso);
    }
    
    // ------------------- PreguntaEvaluación -------------------
    
    public void crearPregunta(PreguntaEvaluacion p) {
        preguntaevaluacionJpa.create(p);
    }
    public List<PreguntaEvaluacion> obtenerPreguntasPorEvaluacion(Integer idEvaluacion) {
        return preguntaevaluacionJpa.findByEvaluacion(idEvaluacion);
    }
    
    // ------------------- ResultadoEvaluación -------------------
    
    public void guardarResultado(ResultadoEvaluacion r) {
        resultadoevaluacionJpa.create(r);
    }
    public List<ResultadoEvaluacion> obtenerResultadosPorUsuarioYEvaluacion(Integer idUsuario, Integer idEvaluacion) {
        return resultadoevaluacionJpa.findByUserAndEvaluacion(idUsuario, idEvaluacion);
    }
    public long contarIntentos(Integer idUsuario, Integer idEvaluacion) {
        return resultadoevaluacionJpa.countByUserAndEvaluacion(idUsuario, idEvaluacion);
    }

    public void editarResultado(ResultadoEvaluacion r) throws Exception {
        resultadoevaluacionJpa.edit(r);
    }
}

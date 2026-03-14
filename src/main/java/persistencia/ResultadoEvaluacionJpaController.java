package persistencia;

import Logica.ResultadoEvaluacion;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class ResultadoEvaluacionJpaController implements Serializable {
    private EntityManagerFactory emf = null;

    public ResultadoEvaluacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public ResultadoEvaluacionJpaController() {
        emf = Persistence.createEntityManagerFactory("JavaWebPU");
    }

    public void create(ResultadoEvaluacion resultado) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(resultado);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void edit(ResultadoEvaluacion resultado) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            resultado = em.merge(resultado);
            em.getTransaction().commit();
        } catch (Exception ex) { throw ex; } finally { if (em != null) em.close(); }
    }

    public void destroy(Integer id) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ResultadoEvaluacion res = em.find(ResultadoEvaluacion.class, id);
            if (res == null) throw new Exception("No existe resultado con id " + id);
            em.remove(res);
            em.getTransaction().commit();
        } finally { if (em != null) em.close(); }
    }

    public ResultadoEvaluacion findResultadoEvaluacion(Integer id) {
        EntityManager em = getEntityManager();
        try { return em.find(ResultadoEvaluacion.class, id); } finally { em.close(); }
    }

    public List<ResultadoEvaluacion> findResultadoEvaluacionEntities() {
        return findResultadoEvaluacionEntities(true, -1, -1);
    }

    private List<ResultadoEvaluacion> findResultadoEvaluacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ResultadoEvaluacion.class));
            Query q = em.createQuery(cq);
            if (!all) { q.setMaxResults(maxResults); q.setFirstResult(firstResult); }
            return q.getResultList();
        } finally { em.close(); }
    }

    public int getResultadoEvaluacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ResultadoEvaluacion> rt = cq.from(ResultadoEvaluacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally { em.close(); }
    }

    // Custom: buscar por usuario y evaluacion
    public List<ResultadoEvaluacion> findByUserAndEvaluacion(Integer idUsuario, Integer idEvaluacion) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<ResultadoEvaluacion> q = em.createQuery(
                "SELECT r FROM ResultadoEvaluacion r " +
                "WHERE r.usuario.id = :idUsuario AND r.evaluacion.idEvaluacion = :idEvaluacion " +
                "ORDER BY r.fecha DESC",
                ResultadoEvaluacion.class);
            q.setParameter("idUsuario", idUsuario);
            q.setParameter("idEvaluacion", idEvaluacion);
            return q.getResultList();
        } finally {
            em.close();
        }
    }


    public long countByUserAndEvaluacion(Integer idUsuario, Integer idEvaluacion) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery(
                "SELECT COUNT(r) FROM ResultadoEvaluacion r " +
                "WHERE r.usuario.id = :idUsuario AND r.evaluacion.idEvaluacion = :idEvaluacion"
            );
            q.setParameter("idUsuario", idUsuario);
            q.setParameter("idEvaluacion", idEvaluacion);
            Long count = (Long) q.getSingleResult();
            return count.intValue();
        } finally {
            em.close();
        }
    }
}


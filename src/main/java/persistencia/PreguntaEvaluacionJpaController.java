package persistencia;

import Logica.PreguntaEvaluacion;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class PreguntaEvaluacionJpaController implements Serializable {
    private EntityManagerFactory emf = null;

    public PreguntaEvaluacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public PreguntaEvaluacionJpaController() {
        emf = JPAUtil.getEMF();
    }

    public void create(PreguntaEvaluacion pregunta) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(pregunta);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void edit(PreguntaEvaluacion pregunta) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            pregunta = em.merge(pregunta);
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            em.close();
        }
    }

    public void destroy(Integer id) throws Exception {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            PreguntaEvaluacion pregunta = em.find(PreguntaEvaluacion.class, id);
            if (pregunta == null) throw new Exception("No existe pregunta con id " + id);
            em.remove(pregunta);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public PreguntaEvaluacion findPreguntaEvaluacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PreguntaEvaluacion.class, id);
        } finally {
            em.close();
        }
    }

    public List<PreguntaEvaluacion> findPreguntaEvaluacionEntities() {
        return findPreguntaEvaluacionEntities(true, -1, -1);
    }

    private List<PreguntaEvaluacion> findPreguntaEvaluacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PreguntaEvaluacion.class));
            Query q = em.createQuery(cq);
            if (!all) { q.setMaxResults(maxResults); q.setFirstResult(firstResult); }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public int getPreguntaEvaluacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PreguntaEvaluacion> rt = cq.from(PreguntaEvaluacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<PreguntaEvaluacion> findByEvaluacion(Integer idEvaluacion) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<PreguntaEvaluacion> q = em.createQuery(
                "SELECT p FROM PreguntaEvaluacion p WHERE p.evaluacion.idEvaluacion = :idEval ORDER BY p.idPregunta", PreguntaEvaluacion.class);
            q.setParameter("idEval", idEvaluacion);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}


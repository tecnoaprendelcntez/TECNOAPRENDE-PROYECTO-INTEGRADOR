package persistencia;

import Logica.EvaluacionFinal;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class EvaluacionFinalJpaController implements Serializable {
    private EntityManagerFactory emf = null;

    public EvaluacionFinalJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public EvaluacionFinalJpaController() {
        emf = Persistence.createEntityManagerFactory("JavaWebPU");
    }
    
    public void create(EvaluacionFinal evaluacion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(evaluacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) em.close();
        }
    }

    public void edit(EvaluacionFinal evaluacion) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            evaluacion = em.merge(evaluacion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (em != null) em.close();
        }
    }

    public void destroy(Integer id) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EvaluacionFinal evaluacion = em.find(EvaluacionFinal.class, id);
            if (evaluacion == null) {
                throw new Exception("La evaluacion con id " + id + " no existe.");
            }
            em.remove(evaluacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) em.close();
        }
    }

    public EvaluacionFinal findEvaluacionFinal(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EvaluacionFinal.class, id);
        } finally {
            em.close();
        }
    }

    public List<EvaluacionFinal> findEvaluacionFinalEntities() {
        return findEvaluacionFinalEntities(true, -1, -1);
    }

    public List<EvaluacionFinal> findEvaluacionFinalEntities(int maxResults, int firstResult) {
        return findEvaluacionFinalEntities(false, maxResults, firstResult);
    }

    private List<EvaluacionFinal> findEvaluacionFinalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EvaluacionFinal.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public int getEvaluacionFinalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EvaluacionFinal> rt = cq.from(EvaluacionFinal.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<EvaluacionFinal> findByCurso(int idCurso) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT e FROM EvaluacionFinal e WHERE e.curso.idCurso = :idCurso", EvaluacionFinal.class)
                     .setParameter("idCurso", idCurso)
                     .getResultList();
        } finally {
            em.close();
        }
    }
}

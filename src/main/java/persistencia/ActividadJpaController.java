package persistencia;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Logica.Actividad;
import javax.persistence.TypedQuery;
import persistencia.exceptions.NonexistentEntityException;

public class ActividadJpaController implements Serializable {

    private EntityManagerFactory emf = null;

    public ActividadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public ActividadJpaController() {
        emf = Persistence.createEntityManagerFactory("JavaWebPU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Actividad actividad) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(actividad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Actividad actividad) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            actividad = em.merge(actividad);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = actividad.getIdActividad();
                if (findActividad(id) == null) {
                    throw new NonexistentEntityException("La Actividad con id " + id + " no existe.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Actividad actividad;
            try {
                actividad = em.getReference(Actividad.class, id);
                actividad.getIdActividad();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("La Actividad con ID " + id + " no existe.", enfe);
            }
            em.remove(actividad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Actividad> findActividadEntities() {
        return findActividadEntities(true, -1, -1);
    }

    public List<Actividad> findActividadEntities(int maxResults, int firstResult) {
        return findActividadEntities(false, maxResults, firstResult);
    }

    private List<Actividad> findActividadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<Actividad> cq = em.getCriteriaBuilder().createQuery(Actividad.class);
            cq.select(cq.from(Actividad.class));
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

    public Actividad findActividad(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Actividad.class, id);
        } finally {
            em.close();
        }
    }
    
    public List<Actividad> findActividadesPorCurso(int idCurso) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Actividad> q = em.createQuery(
                "SELECT a FROM Actividad a WHERE a.curso.idCurso = :id ORDER BY a.idActividad ASC",
                Actividad.class
            );
            q.setParameter("id", idCurso);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Actividad> findAll() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<Actividad> cq = em.getCriteriaBuilder().createQuery(Actividad.class);
            cq.select(cq.from(Actividad.class));
            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Actividad> findRange(int[] range) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<Actividad> cq = em.getCriteriaBuilder().createQuery(Actividad.class);
            cq.select(cq.from(Actividad.class));
            Query q = em.createQuery(cq);
            q.setMaxResults(range[1] - range[0]);
            q.setFirstResult(range[0]);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public int getEntityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<Long> cq = em.getCriteriaBuilder().createQuery(Long.class);
            Root<Actividad> rt = cq.from(Actividad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}

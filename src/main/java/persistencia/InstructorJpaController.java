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
import Logica.Instructor;
import persistencia.exceptions.NonexistentEntityException;

public class InstructorJpaController implements Serializable {

    private EntityManagerFactory emf = null;

    public InstructorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public InstructorJpaController() {
        emf = Persistence.createEntityManagerFactory("JavaWebPU");
    }
    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Instructor instructor) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(instructor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Instructor instructor) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            instructor = em.merge(instructor);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = instructor.getIdInstructor();
                if (findInstructor(id) == null) {
                    throw new NonexistentEntityException("El Instructor con id " + id + " no existe.");
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
            Instructor instructor;
            try {
                instructor = em.getReference(Instructor.class, id);
                instructor.getIdInstructor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("El Instructor con ID " + id + " no existe.", enfe);
            }
            em.remove(instructor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Instructor> findInstructorEntities() {
        return findInstructorEntities(true, -1, -1);
    }

    public List<Instructor> findInstructorEntities(int maxResults, int firstResult) {
        return findInstructorEntities(false, maxResults, firstResult);
    }

    private List<Instructor> findInstructorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<Instructor> cq = em.getCriteriaBuilder().createQuery(Instructor.class);
            cq.select(cq.from(Instructor.class));
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

    public Instructor findInstructor(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Instructor.class, id);
        } finally {
            em.close();
        }
    }

    public List<Instructor> findAll() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<Instructor> cq = em.getCriteriaBuilder().createQuery(Instructor.class);
            cq.select(cq.from(Instructor.class));
            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Instructor> findRange(int[] range) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<Instructor> cq = em.getCriteriaBuilder().createQuery(Instructor.class);
            cq.select(cq.from(Instructor.class));
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
            Root<Instructor> rt = cq.from(Instructor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}

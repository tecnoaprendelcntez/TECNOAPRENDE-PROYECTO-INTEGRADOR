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
import Logica.Progreso;
import persistencia.exceptions.NonexistentEntityException;

public class ProgresoJpaController implements Serializable {

    private EntityManagerFactory emf = null;

    public ProgresoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public ProgresoJpaController() {
        emf = Persistence.createEntityManagerFactory("JavaWebPU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Progreso progreso) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(progreso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Progreso progreso) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            progreso = em.merge(progreso);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = progreso.getIdProgreso();
                if (findProgreso(id) == null) {
                    throw new NonexistentEntityException("El Progreso con id " + id + " no existe.");
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
            Progreso progreso;
            try {
                progreso = em.getReference(Progreso.class, id);
                progreso.getIdProgreso();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("El Progreso con ID " + id + " no existe.", enfe);
            }
            em.remove(progreso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Progreso> findProgresoEntities() {
        return findProgresoEntities(true, -1, -1);
    }

    public List<Progreso> findProgresoEntities(int maxResults, int firstResult) {
        return findProgresoEntities(false, maxResults, firstResult);
    }

    private List<Progreso> findProgresoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<Progreso> cq = em.getCriteriaBuilder().createQuery(Progreso.class);
            cq.select(cq.from(Progreso.class));
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

    public Progreso findProgreso(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Progreso.class, id);
        } finally {
            em.close();
        }
    }
    
    public List<Progreso> findByCursoAndUsuario(int idCurso, int idUsuario) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Progreso p WHERE p.curso.idCurso = :idCurso AND p.usuario.id = :idUsuario", Progreso.class)
                     .setParameter("idCurso", idCurso)
                     .setParameter("idUsuario", idUsuario)
                     .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Progreso> findAll() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<Progreso> cq = em.getCriteriaBuilder().createQuery(Progreso.class);
            cq.select(cq.from(Progreso.class));
            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Progreso> findRange(int[] range) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<Progreso> cq = em.getCriteriaBuilder().createQuery(Progreso.class);
            cq.select(cq.from(Progreso.class));
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
            Root<Progreso> rt = cq.from(Progreso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}

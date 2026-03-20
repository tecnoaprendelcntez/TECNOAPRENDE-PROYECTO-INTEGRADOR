package persistencia;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Logica.Administrador;
import persistencia.exceptions.NonexistentEntityException;

public class AdministradorJpaController implements Serializable {

    private EntityManagerFactory emf = null;

    public AdministradorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public AdministradorJpaController() {
        emf = JPAUtil.getEMF();
    }
    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Administrador administrador) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(administrador);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Administrador administrador) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            administrador = em.merge(administrador);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = administrador.getIdAdministrador();
                if (findAdministrador(id) == null) {
                    throw new NonexistentEntityException("El Administrador con id " + id + " no existe.");
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
            Administrador administrador;
            try {
                administrador = em.getReference(Administrador.class, id);
                administrador.getIdAdministrador();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("El Administrador con ID " + id + " no existe.", enfe);
            }
            em.remove(administrador);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Administrador> findAdministradorEntities() {
        return findAdministradorEntities(true, -1, -1);
    }

    public List<Administrador> findAdministradorEntities(int maxResults, int firstResult) {
        return findAdministradorEntities(false, maxResults, firstResult);
    }

    private List<Administrador> findAdministradorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<Administrador> cq = em.getCriteriaBuilder().createQuery(Administrador.class);
            cq.select(cq.from(Administrador.class));
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

    public Administrador findAdministrador(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Administrador.class, id);
        } finally {
            em.close();
        }
    }

    public List<Administrador> findAll() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<Administrador> cq = em.getCriteriaBuilder().createQuery(Administrador.class);
            cq.select(cq.from(Administrador.class));
            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Administrador> findRange(int[] range) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<Administrador> cq = em.getCriteriaBuilder().createQuery(Administrador.class);
            cq.select(cq.from(Administrador.class));
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
            Root<Administrador> rt = cq.from(Administrador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}

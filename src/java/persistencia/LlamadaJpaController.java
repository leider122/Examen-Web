/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import DTO.Llamada;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import DTO.Usuario;
import DTO.Pais;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author USER
 */
public class LlamadaJpaController implements Serializable {

    public LlamadaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public LlamadaJpaController() {
        this.emf = Persistence.createEntityManagerFactory("Examen-PGWEBPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Llamada llamada) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario cedula = llamada.getCedula();
            if (cedula != null) {
                cedula = em.getReference(cedula.getClass(), cedula.getCedula());
                llamada.setCedula(cedula);
            }
            Pais idPais = llamada.getIdPais();
            if (idPais != null) {
                idPais = em.getReference(idPais.getClass(), idPais.getIdPais());
                llamada.setIdPais(idPais);
            }
            em.persist(llamada);
            if (cedula != null) {
                cedula.getLlamadaCollection().add(llamada);
                cedula = em.merge(cedula);
            }
            if (idPais != null) {
                idPais.getLlamadaCollection().add(llamada);
                idPais = em.merge(idPais);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Llamada llamada) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Llamada persistentLlamada = em.find(Llamada.class, llamada.getId());
            Usuario cedulaOld = persistentLlamada.getCedula();
            Usuario cedulaNew = llamada.getCedula();
            Pais idPaisOld = persistentLlamada.getIdPais();
            Pais idPaisNew = llamada.getIdPais();
            if (cedulaNew != null) {
                cedulaNew = em.getReference(cedulaNew.getClass(), cedulaNew.getCedula());
                llamada.setCedula(cedulaNew);
            }
            if (idPaisNew != null) {
                idPaisNew = em.getReference(idPaisNew.getClass(), idPaisNew.getIdPais());
                llamada.setIdPais(idPaisNew);
            }
            llamada = em.merge(llamada);
            if (cedulaOld != null && !cedulaOld.equals(cedulaNew)) {
                cedulaOld.getLlamadaCollection().remove(llamada);
                cedulaOld = em.merge(cedulaOld);
            }
            if (cedulaNew != null && !cedulaNew.equals(cedulaOld)) {
                cedulaNew.getLlamadaCollection().add(llamada);
                cedulaNew = em.merge(cedulaNew);
            }
            if (idPaisOld != null && !idPaisOld.equals(idPaisNew)) {
                idPaisOld.getLlamadaCollection().remove(llamada);
                idPaisOld = em.merge(idPaisOld);
            }
            if (idPaisNew != null && !idPaisNew.equals(idPaisOld)) {
                idPaisNew.getLlamadaCollection().add(llamada);
                idPaisNew = em.merge(idPaisNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = llamada.getId();
                if (findLlamada(id) == null) {
                    throw new NonexistentEntityException("The llamada with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Llamada llamada;
            try {
                llamada = em.getReference(Llamada.class, id);
                llamada.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The llamada with id " + id + " no longer exists.", enfe);
            }
            Usuario cedula = llamada.getCedula();
            if (cedula != null) {
                cedula.getLlamadaCollection().remove(llamada);
                cedula = em.merge(cedula);
            }
            Pais idPais = llamada.getIdPais();
            if (idPais != null) {
                idPais.getLlamadaCollection().remove(llamada);
                idPais = em.merge(idPais);
            }
            em.remove(llamada);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Llamada> findLlamadaEntities() {
        return findLlamadaEntities(true, -1, -1);
    }

    public List<Llamada> findLlamadaEntities(int maxResults, int firstResult) {
        return findLlamadaEntities(false, maxResults, firstResult);
    }

    private List<Llamada> findLlamadaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Llamada.class));
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

    public Llamada findLlamada(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Llamada.class, id);
        } finally {
            em.close();
        }
    }

    public int getLlamadaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Llamada> rt = cq.from(Llamada.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

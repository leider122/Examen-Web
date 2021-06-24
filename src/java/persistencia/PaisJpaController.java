/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import DTO.Llamada;
import DTO.Pais;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import persistencia.exceptions.IllegalOrphanException;
import persistencia.exceptions.NonexistentEntityException;
import persistencia.exceptions.PreexistingEntityException;

/**
 *
 * @author USER
 */
public class PaisJpaController implements Serializable {

    public PaisJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    public PaisJpaController() {
        this.emf = Persistence.createEntityManagerFactory("Examen-PGWEBPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pais pais) throws PreexistingEntityException, Exception {
        if (pais.getLlamadaCollection() == null) {
            pais.setLlamadaCollection(new ArrayList<Llamada>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Llamada> attachedLlamadaCollection = new ArrayList<Llamada>();
            for (Llamada llamadaCollectionLlamadaToAttach : pais.getLlamadaCollection()) {
                llamadaCollectionLlamadaToAttach = em.getReference(llamadaCollectionLlamadaToAttach.getClass(), llamadaCollectionLlamadaToAttach.getId());
                attachedLlamadaCollection.add(llamadaCollectionLlamadaToAttach);
            }
            pais.setLlamadaCollection(attachedLlamadaCollection);
            em.persist(pais);
            for (Llamada llamadaCollectionLlamada : pais.getLlamadaCollection()) {
                Pais oldIdPaisOfLlamadaCollectionLlamada = llamadaCollectionLlamada.getIdPais();
                llamadaCollectionLlamada.setIdPais(pais);
                llamadaCollectionLlamada = em.merge(llamadaCollectionLlamada);
                if (oldIdPaisOfLlamadaCollectionLlamada != null) {
                    oldIdPaisOfLlamadaCollectionLlamada.getLlamadaCollection().remove(llamadaCollectionLlamada);
                    oldIdPaisOfLlamadaCollectionLlamada = em.merge(oldIdPaisOfLlamadaCollectionLlamada);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPais(pais.getIdPais()) != null) {
                throw new PreexistingEntityException("Pais " + pais + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pais pais) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pais persistentPais = em.find(Pais.class, pais.getIdPais());
            Collection<Llamada> llamadaCollectionOld = persistentPais.getLlamadaCollection();
            Collection<Llamada> llamadaCollectionNew = pais.getLlamadaCollection();
            List<String> illegalOrphanMessages = null;
            for (Llamada llamadaCollectionOldLlamada : llamadaCollectionOld) {
                if (!llamadaCollectionNew.contains(llamadaCollectionOldLlamada)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Llamada " + llamadaCollectionOldLlamada + " since its idPais field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Llamada> attachedLlamadaCollectionNew = new ArrayList<Llamada>();
            for (Llamada llamadaCollectionNewLlamadaToAttach : llamadaCollectionNew) {
                llamadaCollectionNewLlamadaToAttach = em.getReference(llamadaCollectionNewLlamadaToAttach.getClass(), llamadaCollectionNewLlamadaToAttach.getId());
                attachedLlamadaCollectionNew.add(llamadaCollectionNewLlamadaToAttach);
            }
            llamadaCollectionNew = attachedLlamadaCollectionNew;
            pais.setLlamadaCollection(llamadaCollectionNew);
            pais = em.merge(pais);
            for (Llamada llamadaCollectionNewLlamada : llamadaCollectionNew) {
                if (!llamadaCollectionOld.contains(llamadaCollectionNewLlamada)) {
                    Pais oldIdPaisOfLlamadaCollectionNewLlamada = llamadaCollectionNewLlamada.getIdPais();
                    llamadaCollectionNewLlamada.setIdPais(pais);
                    llamadaCollectionNewLlamada = em.merge(llamadaCollectionNewLlamada);
                    if (oldIdPaisOfLlamadaCollectionNewLlamada != null && !oldIdPaisOfLlamadaCollectionNewLlamada.equals(pais)) {
                        oldIdPaisOfLlamadaCollectionNewLlamada.getLlamadaCollection().remove(llamadaCollectionNewLlamada);
                        oldIdPaisOfLlamadaCollectionNewLlamada = em.merge(oldIdPaisOfLlamadaCollectionNewLlamada);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pais.getIdPais();
                if (findPais(id) == null) {
                    throw new NonexistentEntityException("The pais with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pais pais;
            try {
                pais = em.getReference(Pais.class, id);
                pais.getIdPais();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pais with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Llamada> llamadaCollectionOrphanCheck = pais.getLlamadaCollection();
            for (Llamada llamadaCollectionOrphanCheckLlamada : llamadaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pais (" + pais + ") cannot be destroyed since the Llamada " + llamadaCollectionOrphanCheckLlamada + " in its llamadaCollection field has a non-nullable idPais field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(pais);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pais> findPaisEntities() {
        return findPaisEntities(true, -1, -1);
    }

    public List<Pais> findPaisEntities(int maxResults, int firstResult) {
        return findPaisEntities(false, maxResults, firstResult);
    }

    private List<Pais> findPaisEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pais.class));
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

    public Pais findPais(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pais.class, id);
        } finally {
            em.close();
        }
    }

    public int getPaisCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pais> rt = cq.from(Pais.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
